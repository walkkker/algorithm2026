### 错误点
1. 我们的框架下是初始化L=0; for(int R = 0; R < arr.length; R++), 然后循环体内 依据条件移动L
2. ！！！【特别注意错误点】移动L时，注意顺序： 1. 一定是 先L比较deque.peekFirst()； 2. L++移动
3. 续2：千万不能弄反了，反了 deque里面的L元素永远不出来了

### 滑动窗口 （两个结构：窗口内最大值最小值的更新结构/全部子数组max-min<=target的 求总和或最长 动态滑动窗口结构） - 使用双端队列实现
- [滑动窗口基本题](src/main/java/slideWindow/SlidingWindowMaxArray.java)：
    - https://leetcode-cn.com/problems/sliding-window-maximum/
    - 固定大小窗口，返回窗口遍历数字得到的 最大值数组

- [max - min <= target的子数组数量](src/main/java/slideWindow/AllLessNumSubArray.java):
    - 【错误点】当没有元素 和 有元素时 同时处理的逻辑顺序很差， 需要反复做来加强

- [加油站](src/main/java/slideWindow/GasStation.java):
    - https://leetcode-cn.com/problems/gas-station/
    - 涉及到环路问题，注意 可以 将 数组 复制成 两倍长，从而从任意一个点出发， 都可以直接遍历到 结尾处。

#### 简单队列实现滑动窗口
- [滑动窗口的平均值]：
    - https://leetcode-cn.com/problems/qIsx9U/
    - 固定大小K，即为固定大小的队列长度
    - 【错误点】使用数组模拟了队列，关于R的设置没有理清思路。 --》 环形数组 实现 队列 ， 该练习还需强化
