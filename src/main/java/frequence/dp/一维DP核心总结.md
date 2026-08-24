# 一维 DP 核心总结

## 1. 为什么一维 DP 反而更难

高维 DP 的维度通常直接来自题目：两个字符串对应 `dp[i][j]`，区间左右边界对应
`dp[left][right]`，物品下标与剩余容量对应 `dp[index][rest]`。一维 DP 只留下一个下标，
真正困难的是决定这个下标和 `dp` 值分别代表什么。

必须先区分两个概念：

```text
逻辑状态维度：为了唯一描述一个子问题，需要多少个独立变化参数。
代码存储维度：经过滚动数组或变量压缩后，实际使用几维数组。
```

例如 Q416 的原始状态是 `dp[index][target]`，逻辑上是二维 0-1 背包；代码可以压缩成
`boolean[] dp`，但它不能因此被当成原生一维模型。压缩只是利用依赖关系复用存储空间，
不是凭空消掉问题中的物品维度。

> 学习顺序必须是：暴力递归 -> 完整 DP -> 空间压缩。直接背压缩代码，只能记住循环方向，
> 无法解释方向为什么正确，也很难在题目变化后重新推导。

## 2. 当前十道题复盘

| 题目 | 模型定位 | 原实现的不足或易错点 | 应建立的核心认识 |
|---|---|---|---|
| [Q70 爬楼梯](./Q70_ClimbingStairs.java) | 固定前驱一维 DP | 直接进入矩阵快速幂，幂次曾写错；虽然达到 `O(log N)`，但跳过了最基础状态推导 | 先得到 `dp[i]=dp[i-1]+dp[i-2]`，再压缩为两个变量；矩阵快速幂是后续专用优化 |
| [Q118 杨辉三角](./Q118_PascalsTriangle.java) | 分层递推构造，不是典型一维最优 DP | 把 `ArrayList` 容量误认为逻辑长度 | 当前行依赖上一行，但题目要求输出所有行，重点是层间依赖和容器语义，不要强行套一维 DP |
| [Q139 单词拆分](./Q139_WordBreak.java) | 一维前缀可行性 DP | 布尔问题被改成“最少单词数”，引入 `-1`、无穷大和手工字符比较，状态值比题目要求更重 | `dp[end]` 表示前缀 `s[0,end)` 能否拆分；固定结尾，枚举最后一段起点 |
| [Q152 乘积最大子数组](./Q152_MaximumProductSubarray.java) | 一维位置结尾型、多属性状态 | 原压缩版本先覆盖 `maxProduct`，随后用新值计算 `minProduct` | 先写 `maxDp[i]/minDp[i]`；压缩后本轮两个新状态必须同时读取上一轮旧状态 |
| [Q198 打家劫舍](./Q198_HouseRobber.java) | 一维前缀/后缀最优 DP | 原 `O(N)` 空间实现正确，但未继续利用固定依赖压缩空间 | `dp[i]` 是范围整体最优，不要求必须偷当前房屋；只依赖两个状态，可压缩为 `O(1)` |
| [Q279 完全平方数](./Q279_PerfectSquares.java) | 一维目标值 DP，也可视为完全背包 | 曾创建 `new int[n]` 却访问 `dp[n]`；没有先统一 `dp[0]` 和不可达语义 | 可直接枚举“最后使用哪个平方数”；也可从二维完全背包推导到容量正序的一维实现 |
| [Q300 最长递增子序列](./Q300_LongestIncreasingSubsequence.java) | 一维位置结尾型 DP | 记忆化状态使用 `(index,lastValue)` 的字符串键，产生大量对象；`MIN_VALUE` 哨兵还排除了真实最小值 | 改变状态视角：`dp[i]` 表示必须以 `nums[i]` 结尾，枚举 `j<i`，得到稳定的 `O(N^2)` DP |
| [Q322 零钱兑换](./Q322_CoinChange.java) | 完全背包最小值 | 二维状态中又枚举硬币数量 `k`，正确但多一层循环 | 先理解 `dp[i][rest]`，再利用同一行的 `dp[i][rest-coin]` 消除 `k`，最后压缩并正序容量 |
| [Q32 最长有效括号](./Q32_LongestValidParentheses.java) | 一维位置结尾型、结构跳跃 | 栈版本最初把字符而不是下标入栈；DP 转移容易漏接匹配左括号之前的有效段 | `dp[i]` 必须以右括号 `i` 结尾；利用 `dp[i-1]` 跳过完整结构，再连接左侧相邻结构 |
| [Q416 分割等和子集](./Q416_PartitionEqualSubsetSum.java) | 0-1 背包可行性 | 曾误以为两个子集元素数量相等；二维格子曾漏写回；压缩后若正序会重复使用元素 | 先转成“每个数最多一次，能否恰好凑出 `sum/2`”，再从二维状态推导容量倒序 |

这十题中，Q118 的主要问题不是一维 DP；Q70、Q198、Q279 的原思路基本正确但需要补齐基础模型或
空间优化；Q139、Q300、Q322 属于“做出来但状态过重或转移枚举过多”；Q152、Q32、Q416 的主要风险
是状态定义与更新细节没有始终保持一致。

## 3. 从暴力递归识别 DP 维度

先写递归含义，再看真正变化并影响结果的参数：

```text
process(index)                    -> 通常是一维 dp[index]
process(index, rest)              -> 通常是二维 dp[index][rest]
process(left, right)              -> 通常是区间 dp[left][right]
process(index, previousIndex)     -> 通常是二维状态
```

输入数组、字典、固定目标值如果始终不变，不属于状态维度。递归参数也不是越少越好：如果删除某个参数后，
相同状态无法决定相同后续结果，这个参数就不能删除。

### Q198：递归只有位置参数

定义后缀递归：

```text
process(i) = 在房屋[i,n-1]范围内可以获得的最大金额
```

当前位置只有两种选择：

```text
偷i：nums[i] + process(i+2)
不偷i：process(i+1)
```

因此：

```text
process(i) = max(nums[i] + process(i+2), process(i+1))
```

参数只有 `i`，天然对应一维 `dp[i]`。

### Q300：原递归为什么显得更重

原选择模型：

```text
process(index, lastValue)
```

需要同时知道当前位置和之前选中的值，这是二维逻辑状态。强行用 `index + "|" + lastValue` 做键，
不会减少状态维度，只会增加字符串、HashMap 节点和装箱开销。

更好的关键不是“把二维数组压成一维”，而是重新定义问题：

```text
dp[i] = 必须以nums[i]结尾的最长递增子序列长度
```

`lastValue` 被明确固定为 `nums[i]`，再通过枚举所有 `j<i` 寻找前驱。这是**重新设计状态**，不是滚动数组压缩。

## 4. 原生一维 DP 的四种核心模型

### 4.1 前缀或后缀整体最优

状态描述一个完整范围的最优答案，当前位置不一定被选择：

```text
dp[i] = 只考虑前缀[0,i]时的整体最优答案
```

或者：

```text
dp[i] = 只考虑后缀[i,n-1]时的整体最优答案
```

Q198 的后缀定义：

```java
dp[i] = Math.max(nums[i] + dp[i + 2], dp[i + 1]);
```

这类状态已经包含“不选当前位置”的情况，最终答案通常就是边界状态，例如 `dp[0]` 或 `dp[n-1]`。

### 4.2 必须以 i 结尾

状态人为固定右边界，让当前方案保留足够的结尾信息：

```text
dp[i] = 必须以位置i结尾的局部最优答案
```

这类状态强制当前位置参与，最终最优方案可能在任何位置结束，因此通常需要：

```java
ans = Math.max(ans, dp[i]);
```

前驱有三种常见来源：

```text
连续结构：只连接i-1，例如Q152；
非连续子序列：枚举所有j<i，例如Q300；
结构跳跃：由已有长度计算前驱位置，例如Q32。
```

详细对比另见[《一维位置结尾型DP.md》](./一维位置结尾型DP.md)。

### 4.3 前缀分割型

Q139 的状态：

```text
dp[end] = 前缀s[0,end)能否被字典单词完整拆分
```

固定最后边界 `end`，枚举最后一个单词的起点 `start`：

```java
for (int end = 1; end <= s.length(); end++) {
    for (int start = 0; start < end; start++) {
        if (dp[start] && words.contains(s.substring(start, end))) {
            dp[end] = true;
            break;
        }
    }
}
```

其中 `dp[0]=true` 表示空前缀已经被合法拆分，它是后续第一个单词能够开始的基础。

### 4.4 目标值或容量型

状态下标不是数组位置，而是要凑出的数值：

```text
dp[target] = 凑出target时的最优值、方案数或可行性
```

Q279 可以直接定义：

```text
dp[target] = 组成target所需的最少完全平方数数量
```

枚举最后一个平方数：

```java
dp[0] = 0;
for (int target = 1; target <= n; target++) {
    for (int root = 1; root * root <= target; root++) {
        int square = root * root;
        dp[target] = Math.min(dp[target], dp[target - square] + 1);
    }
}
```

这个写法本身就是完整的一维目标值 DP，不一定必须先经过二维背包；但从完全背包角度推导，可以统一理解
Q279 与 Q322 的正序容量写法。

### 4.5 一维位置推进 + 有限状态机

当未来决策不仅需要位置，还需要知道当前位置结束时的业务状态，可以为每个位置维护固定数量的状态：

```text
cash[i] = 第i天结束时不持股的最大收益
hold[i] = 第i天结束时持股的最大收益
```

股票基础转移只依赖上一天，完整数组可压缩成常数变量；手续费、冷冻期和交易次数限制分别通过修改
转移边或增加状态表达。参见[《股票问题总结.md》](./stock/股票问题总结.md)以及
[《乘积最大子数组与股票状态机DP对比.md》](./乘积最大子数组与股票状态机DP对比.md)。

## 5. 状态定义必须包含的四项信息

不要写成“`dp[i]` 表示最大值”，必须写完整：

```text
1. 范围：前缀[0,i]、后缀[i,n-1]、还是容量i；
2. 约束：是否必须选择或必须以i结尾；
3. 目标：最大值、最小值、方案数还是可行性；
4. 边界：下标i是元素下标，还是前i个元素的长度。
```

例如：

```text
错误：dp[i]表示最长长度。
正确：dp[i]表示必须以nums[i]结尾的最长严格递增子序列长度。

错误：dp[i]表示字符串能否拆分。
正确：dp[i]表示长度为i的前缀s[0,i)能否被字典单词完整拆分。
```

状态定义不完整，后续通常会在初始化、转移范围和答案位置上同时出错。

## 6. 一维 DP 的前驱分类

| 前驱类型 | 典型转移 | 题目 |
|---|---|---|
| 固定相邻前驱 | `dp[i]` 依赖 `dp[i-1]、dp[i-2]` | Q70、Q198 |
| 枚举所有历史前驱 | 枚举 `j<i` | Q300、Q139 |
| 只连接前一位置 | 连续结构只能来自 `i-1` | Q152 |
| 根据已有长度跳跃 | `matchingLeft=i-dp[i-1]-1` | Q32 |
| 同一位置多个属性 | 同时维护最大/最小、持有/不持有 | Q152、股票状态机 |
| 容量转移 | 从 `dp[target-item]` 到 `dp[target]` | Q279、Q322、Q416 |

循环有两层不代表二维 DP。Q300 有 `i、j` 两层循环，但保存的子问题只有 `dp[i]`；`j` 只是枚举
转移来源，不是状态维度。

## 7. Q152：先写双数组，再压缩变量

未压缩状态：

```java
int[] maxDp = new int[nums.length];
int[] minDp = new int[nums.length];
maxDp[0] = nums[0];
minDp[0] = nums[0];

for (int i = 1; i < nums.length; i++) {
    int current = nums[i];
    maxDp[i] = Math.max(current,
            Math.max(current * maxDp[i - 1], current * minDp[i - 1]));
    minDp[i] = Math.min(current,
            Math.min(current * maxDp[i - 1], current * minDp[i - 1]));
}
```

从数组可以清楚看出：`maxDp[i]` 与 `minDp[i]` 必须同时依赖上一位置的两个旧状态。压缩后必须先固定旧值：

```java
int oldMax = maxEnd;
int oldMin = minEnd;
maxEnd = Math.max(current, Math.max(current * oldMax, current * oldMin));
minEnd = Math.min(current, Math.min(current * oldMax, current * oldMin));
```

原错误先覆盖 `maxEnd` 再计算 `minEnd`，本质是空间压缩破坏了未压缩版本的“读取上一行”语义。

## 8. 背包一：Q416 从暴力递归到一维 0-1 背包

### 8.1 问题转换

总和为 `sum`，如果能选出一个子集恰好为 `sum/2`，剩余元素自然也是 `sum/2`。每个数组元素
只能选择一次，因此是 0-1 背包可行性问题，而不是要求两个子集包含相同数量的元素。

### 8.2 暴力递归

```text
process(index, rest) = 从index开始的元素中选择，能否恰好凑出rest
```

选择：

```text
不选nums[index]：process(index+1, rest)
选择nums[index]：process(index+1, rest-nums[index])
```

注意两条分支都进入 `index+1`，这正是“每个元素最多一次”。

### 8.3 二维 DP

```text
dp[index][rest] = 从index开始的元素中选择，能否恰好凑出rest
```

```java
boolean[][] dp = new boolean[n + 1][target + 1];
for (int index = 0; index <= n; index++) {
    dp[index][0] = true;
}

for (int index = n - 1; index >= 0; index--) {
    for (int rest = 1; rest <= target; rest++) {
        dp[index][rest] = dp[index + 1][rest];
        if (rest >= nums[index]) {
            dp[index][rest] = dp[index][rest]
                    || dp[index + 1][rest - nums[index]];
        }
    }
}
```

### 8.4 压缩成一维

去掉 `index` 维后，`dp[rest]` 在处理当前元素前代表上一轮状态，处理后代表加入当前元素后的状态。
选择当前元素时必须读取上一轮的 `dp[rest-num]`。

```java
boolean[] dp = new boolean[target + 1];
dp[0] = true;

for (int num : nums) {
    for (int rest = target; rest >= num; rest--) {
        dp[rest] = dp[rest] || dp[rest - num];
    }
}
```

容量必须倒序。若正序，较小的 `dp[rest-num]` 可能已经在本轮使用当前 `num` 更新，当前元素就会被
重复选择，模型会错误地变成完全背包。

## 9. 背包二：Q322 从枚举数量到一维完全背包

### 9.1 暴力递归

```text
process(index, rest) = 使用[index,n-1]种硬币凑出rest的最少硬币数
```

因为当前硬币可使用任意次，最直接的尝试是枚举数量 `count`：

```text
process(index, rest)
    = min(count + process(index+1, rest-count*coins[index]))
```

这就是原实现的来源，逻辑正确，但 DP 表中仍然保留了一层 `count` 枚举。

### 9.2 二维 DP 的基础版本

```java
for (int index = n - 1; index >= 0; index--) {
    for (int rest = 0; rest <= amount; rest++) {
        for (int count = 0; count * coins[index] <= rest; count++) {
            // 从dp[index+1][rest-count*coin]转移
        }
    }
}
```

时间复杂度包含三层枚举。

### 9.3 消除硬币数量枚举

二维状态存在如下关系：

```text
dp[index][rest] = min(
    dp[index+1][rest],                 // 一枚当前硬币也不用
    1 + dp[index][rest-coin]           // 至少使用一枚，剩余仍可继续使用当前硬币
)
```

第二项读取同一行的更小容量，已经隐含了“继续使用0枚、1枚、2枚……”的所有情况，因此不再需要
显式枚举 `count`。

```java
for (int index = n - 1; index >= 0; index--) {
    for (int rest = 0; rest <= amount; rest++) {
        dp[index][rest] = dp[index + 1][rest];
        if (rest >= coins[index] && dp[index][rest - coins[index]] != INF) {
            dp[index][rest] = Math.min(
                    dp[index][rest],
                    dp[index][rest - coins[index]] + 1
            );
        }
    }
}
```

### 9.4 压缩成一维

```java
int[] dp = new int[amount + 1];
Arrays.fill(dp, amount + 1);
dp[0] = 0;

for (int coin : coins) {
    for (int rest = coin; rest <= amount; rest++) {
        dp[rest] = Math.min(dp[rest], dp[rest - coin] + 1);
    }
}
```

完全背包容量正序，是为了允许 `dp[rest-coin]` 使用本轮已经更新的状态，从而重复使用当前硬币。

## 10. Q279 的两种正确理解

### 10.1 枚举最后一个平方数

```text
dp[target] = 组成target所需的最少平方数数量
```

```java
for (int target = 1; target <= n; target++) {
    for (int root = 1; root * root <= target; root++) {
        int square = root * root;
        dp[target] = Math.min(dp[target], dp[target - square] + 1);
    }
}
```

这是原生一维目标值 DP，直接从“最后使用哪个平方数”推导。

### 10.2 完全背包

把 `1、4、9...` 看作可以无限使用的物品：

```java
for (int root = 1; root * root <= n; root++) {
    int square = root * root;
    for (int target = square; target <= n; target++) {
        dp[target] = Math.min(dp[target], dp[target - square] + 1);
    }
}
```

两种循环顺序在“求最少数量”这个目标下都能得到正确结果。若问题改成“统计方案数”，物品与容量的
循环顺序会决定统计组合还是排列，不能随意交换。

## 11. 完全背包一维化的完整判断框架

### 11.1 先区分原生一维与二维压缩

代码中最终只有一个 `dp[]`，不代表问题的逻辑状态一定是一维。

**原生一维目标值 DP：**只知道 `target` 就足以确定后续可选集合和子问题，递归原型可以直接写成：

```text
process(target)
```

Q279、Q322 的最少数量写法属于这一类：

```text
dp[target] = 凑出target所需的最少元素数量
dp[target] = min(dp[target-weight] + 1)
```

这里枚举的是“最后选择哪个元素”，不需要记录已经处理到第几种物品。

**二维背包压缩：**逻辑上仍然需要描述物品范围和容量：

```text
dp[index][rest]
```

只是当前层只依赖相邻层和本层已计算位置，所以复用同一个数组。Q416 的0-1背包以及Q518的组合计数
都属于这一类。判断时应问：

```text
只知道target，能否唯一确定合法前驱？
是否还必须知道“当前允许使用到第几种物品”？
```

前者成立时可以直接设计原生一维状态；后者存在时，应先保留二维语义，再考虑空间压缩。

### 11.2 背包的前缀定义与后缀定义

背包没有固定必须使用前缀或后缀。教材中的迭代模板通常使用前缀定义：

```text
dp[i][rest] = 使用前i种物品[0,i-1]解决rest的答案
```

物品正序计算，最终答案是 `dp[n][bag]`。

从暴力递归 `process(index, rest)` 改DP时，通常自然得到后缀定义：

```text
dp[index][rest] = 使用物品[index,n-1]解决rest的答案
```

物品倒序计算，最终答案是 `dp[0][bag]`。

空间压缩本身不会强制改变前缀或后缀语义。严格压缩Q322的后缀二维状态，可以继续倒序处理物品：

```java
for (int index = coins.length - 1; index >= 0; index--) {
    int coin = coins[index];
    for (int rest = coin; rest <= amount; rest++) {
        dp[rest] = Math.min(dp[rest], dp[rest - coin] + 1);
    }
}
```

每轮结束后，`dp[rest]` 表示使用 `coins[index,n-1]` 凑出 `rest` 的答案。若改成正序处理物品：

```java
for (int coin : coins) {
    // 更新容量
}
```

则每轮结束后表示使用已经处理过的前缀物品。Q322求的是全部硬币下的最小值，物品加入顺序不影响
最终结果，所以两种方向都正确。不能把“物品下标方向”和“容量方向”混为一谈。

### 11.3 为什么容量从 `coin` 开始

当 `rest < coin` 时，当前硬币无法参与，二维状态只能继承“不使用当前硬币”的答案：

```text
dp[i][rest] = dp[i-1][rest]     // 前缀定义
```

压缩后，更新前的 `dp[rest]` 本来就保存着上一层答案。跳过这些位置等价于执行：

```java
dp[rest] = dp[rest];
```

因此可以把：

```java
for (int rest = 0; rest <= amount; rest++) {
    if (rest >= coin) {
        // 转移
    }
}
```

简化为：

```java
for (int rest = coin; rest <= amount; rest++) {
    // 转移
}
```

### 11.4 容量正序和倒序的本质

这里的正序、倒序特指容量维度，不是物品下标方向。

**0-1背包容量倒序：**当前物品只能使用一次，必须读取上一层旧状态：

```java
for (int item : items) {
    for (int rest = bag; rest >= item; rest--) {
        dp[rest] = Math.max(dp[rest], dp[rest - item] + value);
    }
}
```

较小的 `dp[rest-item]` 尚未在本轮更新，因此不会重复使用当前物品。

**完全背包容量正序：**当前物品可以无限使用，需要读取本层新状态：

```java
for (int item : items) {
    for (int rest = item; rest <= bag; rest++) {
        dp[rest] = Math.max(dp[rest], dp[rest - item] + value);
    }
}
```

例如物品重量为2，正序会依次得到：

```text
dp[2] <- dp[0]   使用1次
dp[4] <- dp[2]   使用2次
dp[6] <- dp[4]   使用3次
```

统一判断只有一句：

```text
更新dp[rest]时，是否允许dp[rest-item]已经使用过当前物品？
允许则容量正序；不允许则容量倒序。
```

### 11.5 目标外层与物品外层不是简单的代码风格

对于Q279和Q322的最少数量，可以直接使用目标外层：

```java
for (int target = 1; target <= bag; target++) {
    for (int weight : weights) {
        if (weight <= target) {
            dp[target] = Math.min(dp[target], dp[target - weight] + 1);
        }
    }
}
```

它是原生一维“最后一步DP”：枚举最后使用的元素。不同选择顺序可能重复到达同一结果，但 `min`、
`max` 或逻辑或等聚合不会因重复路径改变最终值。

物品外层则更直接对应二维完全背包压缩：

```java
for (int weight : weights) {
    for (int target = weight; target <= bag; target++) {
        dp[target] = Math.min(dp[target], dp[target - weight] + 1);
    }
}
```

两者在Q279、Q322的最小数量目标下，时间和空间复杂度相同。目标外层更适合现场从最后一步推导；
物品外层更适合展示完整背包模型。不要为了形式统一，强行把自然的一维问题先扩展成二维。

### 11.6 Q377与Q518：计数语义决定循环顺序

计数问题不能随意交换循环。假设元素为 `[1,2]`，目标为3。

Q377统计排列：

```text
1+1+1、1+2、2+1
```

`1+2`与`2+1`是不同答案。定义 `dp[target]` 为组成目标的排列数，枚举最后一个元素：

```java
dp[0] = 1;
for (int target = 1; target <= bag; target++) {
    for (int num : nums) {
        if (num <= target) {
            dp[target] += dp[target - num];
        }
    }
}
```

Q518统计无序组合：

```text
1+1+1、1+2
```

`1+2`与`2+1`只能计算一次。它的逻辑原型必须记录物品范围：

```text
dp[i][target] = 只使用前i种硬币组成target的组合数量
dp[i][target] = dp[i-1][target] + dp[i][target-coin]
```

压缩后仍必须保留物品外层：

```java
dp[0] = 1;
for (int coin : coins) {
    for (int target = coin; target <= amount; target++) {
        dp[target] += dp[target - coin];
    }
}
```

物品外层强制组合按固定物品顺序生成，每个组合只有一条生成路径。这不是使用集合事后去重，而是通过
状态拓扑从源头避免重复。因此：

```text
目标外层 + 求和：统计有序排列；
物品外层 + 容量正序 + 求和：统计无序组合。
```

### 11.7 原生一维目标值DP的判断条件

遇到新题时依次检查：

```text
1. 知道target后，可选元素集合是否已经完全确定？
2. 每种元素是否允许重复使用，或使用限制是否已包含在其他状态中？
3. 枚举最后一个元素是否能覆盖所有合法答案？
4. 聚合操作是min、max、boolean、排列计数，还是需要去重的组合计数？
```

前三项成立，且聚合语义允许按最后一步生成时，可以直接设计 `dp[target]`。如果还必须知道当前允许使用
到第几种物品，逻辑状态就是二维背包；最终即使压缩为 `dp[target]`，物品外层也不能消失。

| 题目 | 结果语义 | 状态来源 | 推荐循环 |
|---|---|---|---|
| Q279 完全平方数 | 最小数量 | 原生目标值DP | 目标外层，枚举最后平方数 |
| Q322 零钱兑换 | 最小数量 | 原生目标值DP或完全背包 | 目标外层更直观；物品外层也正确 |
| Q377 组合总和IV | 有序排列数量 | 原生目标值DP | 目标外层 |
| Q518 零钱兑换II | 无序组合数量 | 二维完全背包压缩 | 物品外层、容量正序 |
| Q416 分割等和子集 | 每个物品最多一次 | 二维0-1背包压缩 | 物品外层、容量倒序 |

### 11.8 面试中的选择

先保证状态定义和时间复杂度正确，不需要为追求最小空间强行背压缩模板：

```text
能自然定义dp[target]：直接写原生一维模型；
需要物品范围：先写二维状态和转移；
空间约束严格或压缩足够熟练：再根据依赖方向压缩；
压缩容易覆盖旧状态：保留二维实现并口头说明可优化。
```

背包压缩后的空间通常是 `O(bag)`，不是 `O(1)`。面试官更关注能否说明“删除哪一维、为何不会覆盖
仍需读取的数据”，而不是机械地把所有DP都压缩到最小空间。

## 12. 初始化由答案类型决定

| 目标 | `dp[0]` | 其他状态 | 含义 |
|---|---:|---:|---|
| 可行性 | `true` | `false` | 0可以由空集合凑出 |
| 最少数量 | `0` | `INF` | 其他目标初始不可达 |
| 最大价值且允许不装满 | `0` | `0` | 什么都不选价值为0 |
| 最大价值且要求恰好装满 | `0` | `-INF` | 非0容量初始不可达 |
| 方案数 | `1` | `0` | 凑出0有一种空方案 |

最小值问题常用 `amount+1` 作为安全哨兵，因为凑出 `amount` 最多不会需要超过 `amount` 枚正整数
硬币。若使用 `Integer.MAX_VALUE`，必须先判断前驱可达，避免执行 `MAX_VALUE+1` 溢出。

## 13. 空间压缩的统一原则

压缩前先画出依赖关系，不要先背遍历方向：

```text
当前状态只读上一层：覆盖时要避免读到本层新值；
当前状态允许读取本层：遍历顺序要让所需的新值已经产生；
只依赖固定数量相邻状态：保存旧值并按正确顺序移动变量。
```

对应结论：

| 模型 | 压缩后方向 | 本质 |
|---|---|---|
| 0-1 背包 | 容量倒序 | `dp[rest-item]` 必须是上一轮旧状态 |
| 完全背包 | 容量正序 | 允许读取本轮新状态，重复使用当前物品 |
| Q70/Q198 | 保存两个相邻状态 | 当前值计算完成后再移动旧状态 |
| Q152 | 同时保存旧最大、旧最小 | 多个新状态不能互相污染 |

> 正序、倒序不是口诀本身，而是对“本轮新状态是否允许再次参与转移”的控制。

## 14. 最终答案为什么不总在最后一个格子

状态定义决定答案位置：

```text
前缀/后缀整体最优：通常是dp[n-1]、dp[n]或dp[0]；
必须以i结尾：最优方案可能提前结束，通常取所有dp[i]的最大值；
目标值/容量：通常是dp[target]；
二维递归改表：答案位置对应原递归入口参数。
```

例如 Q152、Q300、Q32 都必须维护全局答案，不能默认返回最后一个位置的状态。

## 15. 高频错误检查表

1. 是否完整写出了 `dp[i]` 的范围、约束和目标？
2. `i` 是元素下标，还是前缀长度？对应区间是 `[0,i]` 还是 `[0,i)`？
3. 当前元素是否必须参与？
4. 前驱是固定位置、任意 `j<i`、结构跳跃，还是容量差值？
5. 一个状态是否足够，未来是否还需要最大/最小等不同属性？
6. base case 是否有业务含义，例如 `dp[0]=true/0/1`？
7. 最值问题的不可达哨兵是否会参与错误转移或溢出？
8. 最终答案是边界状态还是所有结尾状态的最优值？
9. 空间压缩后是否覆盖了仍然需要的旧状态？
10. 背包压缩时，本轮状态是否允许重复使用当前物品？

## 16. 面试中的稳定书写顺序

```text
1. 写暴力递归含义，明确每个参数；
2. 列出当前位置所有选择；
3. 判断是否存在重复子问题；
4. 写记忆化或完整DP，明确每个格子含义；
5. 写base case、遍历方向和答案位置；
6. 验证最小样例、空状态和边界下标；
7. 最后再做滚动数组、背包方向、变量压缩或专用优化。
```

一维 DP 的目标不是记住更多公式，而是每次都能回答以下四句话：

```text
dp[i]准确表示什么？
当前位置有哪些选择？
每个选择读取哪个旧状态？
题目最终答案存在哪个状态？
```

只要这四句话没有歧义，代码通常只是状态定义的直接翻译。
