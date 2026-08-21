1. 【表格内的无效值连锁】 [SplitSumClosedSizeHalf.java](splitSumClosed%2FSplitSumClosedSizeHalf.java)
    1. 特别注意无效值！ dp[i][j][z] 中dp，i,j,z都是有各自含义的。 当i,j,z三元组 处于无效状态时，dp要赋予无效值。
    2. 后续依赖的所有格子，如果依赖无效值，全部自动赋予无效值。
2. 【对应1，超出边界的有效值快速计算】上面讲的是【边界内的无效值连锁】，下面讲【超出边界的有效值快速计算】
    1. [Code01_KillMonster.java](%E6%A6%82%E7%8E%87%E7%B1%BB%2FCode01_KillMonster.java)
    2. 这道题就是要统计 【出表格的有效值】
    3. 直接去看dpTest吧，不再赘述
3. 从左到右的尝试模型，尤其是有steps的这种：
    1. 我们一般设置dp[steps+1][i][j]，含义是step到end 怎么怎么样
    2. 这里注意 step=0时代表初始状态，step=1（dp[1][i][j]）代表的就是走完第一步之后，i,j怎么怎么样
    3. 【重点】核心意思是，dp[step][][] 代表的意思是 当前已经完成了step所处的状态！
       （而不是step还没做的状态，是从step-1到step完成了，所处的状态 =》 所以总结为 step完成的状态）

## 题型上的区分

1. 组合数问题：
    1. 初始化阶段-可以不设置无效值，而是直接设置成0（或者说无效值设置为0，不需要设置为-1&&进行无效值检查），进而避免代码进行大量检查。
       因为状态转移不存在 dp[i] = 某个数字 + dp[i+1], 一般都是 dp[i] = dp[i +1]，所以此时无效值等同于0&&加上ans，其实对最终结果没有影响（最终结果也是0）。
    2. 初始化阶段-有效值要设置为1. 错题：convertToLetterString。 dp[end-end]为有效，组合数应该赋值为1。这里可以有两个角度去理解：
        1. 前面成功凑出组合，最终走到了i=end，所以此时dp[end]=1 而不能为0，代表成功的一种组合。
        2. dp[end-end]本身也对应着一个组合，所以对应组合数为1.

## 范围尝试模型

1. 对于dp[l][r] 代表l,r范围对应某个dp值 这种范围尝试模型。
    1. 一般dp二维表是一个正方形，因为l与r的总长度相同。
    2. 【重点】
        1. 正方形左下区域是无效区间（对应l>r） =》 不用管
        2. 右上区域为有效区间（状态转移求值） =》 双重for循环的填格子阶段
        3. 正对角线是base case（此时l==r，只存在唯一数字） =》 初始化，重要，记住！！！
2. 以最长回文子序列为例，这种 l,r 方式的范围尝试模型。 它的范围需要控制在 数组的有效区间内，即 dp[][] = new int[len][len] ，而不是从左到右的尝试模型的dp[][] = new int[len + 1][?]
    1. 核心就是不能像从左到右的尝试模型那样，step的范围设置成[0,len] (对应 new int[len + 1])，其中len代表结尾步之后的下一步。
    2. 续1：范围尝试模型，需要确保l,r在数组下标。 所以 dp[][] = new int[len][len];
       确保l,r取值时，都是落在有效区间内（即 [0, len - 1]）。 始终对应一段有效数组区间


## 难题
1. splitNum 确实比较难，主要是思路比较难（从小到大拆分）设置dp[pre][remain] +  遗漏 最后一个数字直接取remain
2. stickersToSpellWord: 
   1. 只能做记忆化搜索，因为 2个变动参数（int i, String target）超出了int范畴。
      1. 一个HashMap<String, Integer> 就足够， (i, target)二元组只要组成唯一性就可以，可以拼成【i + "|" + target】
   2. 易漏：有一个while 逐步minus过程，漏掉target=after会导致死循环
   3. int yes = count + process(i, target...)。 但是process存在无效值，
      1. 【对】那么一定要 先判断process是否有效（if process==-1），再int yes=count+process
      2. 【错！】绝对不要 int yes = count + process, 再判断 if yes==-1 =》 
      3. 2是完全错误的！！！无效值是针对process/dp直接而言的，任何加工前先判断是否有效
   4. 本题还有很多小细节易错，且耗时很长。反复练习！