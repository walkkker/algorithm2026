### 贪心算法 : 解法上基本都是 **【排序】（使用比较器）或【堆】（使用哈夫曼编码/ 大根堆配合小根堆）**
```
    (1)贪心算法就是 通过 【取得每一个步中的局部最优（这个局部最优就是制定的贪心策略）】， 【最终组合起来就是 全局最优解的方法】。所以贪心策略也有可能就是错的。
    (2)该算法 存在 错误的概率，所以 是一种类似于猜的算法。
    (3)虽然在大多数情况下是有用的，但是 我们想要知道 贪心算法 是否是可行方案时， 只要我们能够举出一种反例（意思就是局部最优，无法组成全局最优），那么就说明 该问题使用贪心算法无效。
   ```
- [给定一个由字符串组成的数组strs，必须把所有的字符串拼接起来，返回所有的拼接结果中，字典序最小的结果](src/main/java/greedyAlgorithm/LowestLexicography.java):字典序就是java里面字符串的排序方式。
- [会议室的最佳安排](src/main/java/greedyAlgorithm/BestArrange.java)(具体题目都写在MyBase中): 排序， 以结束时间早晚排序 为 贪心策略
- [Light](src/main/java/greedyAlgorithm/Light.java): 分析放灯的情况，选择最贪心的位置进行放灯（比如说 三个位置 灯放中间）
- [IPO](src/main/java/greedyAlgorithm/IPO.java):
    - Initial Public Offering(首次公开募股). https://leetcode-cn.com/problems/ipo/ .
    - 大小堆。 小堆：按成本排序。 大堆：可以购买的项目中，按profit排序。
    - 策略： 小根堆弹出来进入到大根堆。 每次都现根据cost在可以选择的项目中，选取profit最大的。最后到达K个项目，结束。
    - 注意检查 大根堆为空的情况，which means 当前w无法支付的起任何一个项目/项目没有了 
- [最小代价分割金条](src/main/java/greedyAlgorithm/LessMoneySplitGold.java): 哈夫曼编码，堆。 策略：【每次选最小的两个合并】，然后组成新的数组。每次合并就是成本。最终合并成只有一个数字的时候，即得到最终答案。

### 补充
1. 这一章里面的暴力递归都很值得一看，左神写的挺好。 按照我的理解，都属于回溯，列出全部可能。