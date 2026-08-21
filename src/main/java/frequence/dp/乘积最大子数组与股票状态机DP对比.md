# 乘积最大子数组与股票状态机 DP 对比

## 1. 统一结构

Q152 乘积最大子数组与股票系列题都可以抽象成：

```text
dp[i][state] = 处理到位置i，并且结束时处于state状态的最优答案
```

它们的共同点不是“恰好使用两个数组”，而是：**单个状态不足以支持未来转移，同一个位置必须保留
多个互补状态**。当前状态只依赖上一位置，因此完整数组最终都可以压缩成常数个变量。

对应代码：

- [Q152 乘积最大子数组](./Q152_MaximumProductSubarray.java)
- [股票问题总结](./stock/股票问题总结.md)

## 2. 状态定义对比

| 问题 | 状态一 | 状态二 | 为什么一个状态不够 |
|---|---|---|---|
| Q152 | `maxEnd[i]`：必须以i结尾的最大乘积 | `minEnd[i]`：必须以i结尾的最小乘积 | 当前数为负数时，旧最小值可能转成新最大值 |
| 股票 | `cash[i]`：第i天结束时不持股的最大收益 | `hold[i]`：第i天结束时持股的最大收益 | 买入和卖出依赖不同的昨日持仓状态 |

Q152 的两个状态是同一个结尾位置上的**数值极值属性**；股票的两个状态是一天结束时的**互斥业务状态**。

## 3. 未压缩转移

### Q152

```java
maxEnd[i] = Math.max(nums[i], Math.max(
        nums[i] * maxEnd[i - 1],
        nums[i] * minEnd[i - 1]
));

minEnd[i] = Math.min(nums[i], Math.min(
        nums[i] * maxEnd[i - 1],
        nums[i] * minEnd[i - 1]
));
```

### 不限制交易次数的股票

```java
cash[i] = Math.max(
        cash[i - 1],
        hold[i - 1] + prices[i]
);

hold[i] = Math.max(
        hold[i - 1],
        cash[i - 1] - prices[i]
);
```

两个模型都体现了：新状态必须从上一位置的完整状态集合转移，不能只保留上一轮的单个最优值。

## 4. 空间压缩的共同风险

未压缩时，右侧明确读取 `[i-1]`；压缩后数组下标消失，最容易把本轮新状态误当成上一轮旧状态。

稳定写法是先保存快照：

```java
int oldStateA = stateA;
int oldStateB = stateB;

stateA = transferA(oldStateA, oldStateB);
stateB = transferB(oldStateA, oldStateB);
```

Q152：

```java
int oldMax = maxEnd;
int oldMin = minEnd;
maxEnd = Math.max(current, Math.max(current * oldMax, current * oldMin));
minEnd = Math.min(current, Math.min(current * oldMax, current * oldMin));
```

股票：

```java
int oldCash = cash;
int oldHold = hold;
cash = Math.max(oldCash, oldHold + price);
hold = Math.max(oldHold, oldCash - price);
```

即使某些股票公式在同一天买卖收益为0的条件下，使用本轮新值仍可能碰巧得到相同结果，也不要依赖这种
偶然等价。统一保存旧状态，才能在手续费、冷冻期和交易次数限制加入后保持定义正确。

## 5. 初始化与最终答案不同

### Q152

非空子数组必须选择第一个元素：

```text
maxEnd = nums[0]
minEnd = nums[0]
ans = nums[0]
```

`maxEnd[i]` 强制以i结尾，最优子数组可能提前结束，因此答案是所有 `maxEnd[i]` 的最大值。

### 股票

初始不持股收益为0，持股相当于买入产生负现金流：

```text
cash = 0
hold = -prices[0]
```

`cash[i]` 已经包含“今天不操作，继承昨天答案”，因此最终通常返回最后一天合法的不持股状态。

## 6. 状态扩展方式

Q152 的状态数量由乘法符号决定，两个极值状态已经足够。股票变体则通过业务规则扩展状态：

```text
手续费：修改卖出转移；
冷冻期：增加sold/rest等状态；
最多K次交易：增加交易次数维度；
最多两次交易：可以展开成buy1/sell1/buy2/sell2四个状态。
```

股票问题的统一解题方法是：

```text
1. 列出每天结束时所有互斥状态；
2. 为每个状态枚举它可以从哪些昨日状态到达；
3. 写出未压缩转移；
4. 最后保存旧状态并压缩空间。
```

## 7. 记忆结论

```text
Q152：位置i + 最大/最小结尾属性。
股票：日期i + 持有/不持有等业务状态。

共同点：一维位置推进、多状态、只依赖上一轮、可压缩到O(1)。
区别：Q152状态来自数值符号，股票状态来自交易规则。
```
