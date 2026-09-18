# Quickselect快速选择：第K大与第K小

## 最重要的识别提示

> 无序数组中寻找第k大、第k小，应该想到Quickselect（快速选择）：使用分区算法，快速定位目标排名的元素。

你的记忆入口：**荷兰国旗分区 + 类二分的区间淘汰，随机化后期望O(N)。**

之前做题时遗漏的重点是：荷兰国旗不仅可以服务于快速排序，也可以用来解决排名选择问题。
只要知道目标属于哪一段，就只处理这一段，无需把整个数组排好序。

对应实现：[Q215数组中的第K个最大元素](./Q215_KthLargestElementInAnArray.java)，重点复习
`SolutionReviewed20260919`。分类入口：[Hot100堆逐题详解](./Hot100堆逐题详解.md)。

## 目录

1. [三个算法之间的关系](#三个算法之间的关系)
2. [先把排名转换成下标](#先把排名转换成下标)
3. [分区后为什么能够排除一侧](#分区后为什么能够排除一侧)
4. [Java核心流程](#java核心流程)
5. [为什么期望线性](#为什么期望线性)
6. [与大小为k的堆比较](#与大小为k的堆比较)
7. [复习检查清单](#复习检查清单)

## 三个算法之间的关系

| 模型 | 核心操作 | 目的 |
| --- | --- | --- |
| 荷兰国旗分区 | 按pivot划分小于、等于、大于三区 | 建立分区关系 |
| QuickSort | 分区后继续处理左右两侧 | 完整排序 |
| Quickselect | 分区后只处理目标排名所在的一侧 | 找目标顺序统计量 |

荷兰国旗是分区方法，Quickselect是利用分区结果选择目标排名的完整算法。
Quickselect也可以使用两路partition；三路分区能一次排除整个等值区间，对重复元素尤其合适。

“类二分”指每次根据证据排除不含答案的区间，不表示每次恰好折半。
普通二分利用已有的有序性；Quickselect先扫描当前区间，通过partition建立大小关系。

## 先把排名转换成下标

这里k是1-based排名，目标是完整升序数组中的0-based下标：

| 问题 | targetIndex |
| --- | --- |
| 第k小 | `k - 1` |
| 第k大 | `nums.length - k` |

例如`[3,1,2,2]`排序后是`[1,2,2,3]`，第2大对应下标`4-2=2`，答案是2。
**重复元素参与排名，第k大不等于第k个不同的值。**

## 分区后为什么能够排除一侧

维护不变量：目标升序下标始终位于当前闭区间`[left,right]`内，已排除区域与保留区域之间
的大小关系已经确定。partition返回闭区间`[equalLeft,equalRight]`：

```text
[left, equalLeft)            小于pivot
[equalLeft, equalRight]      等于pivot
(equalRight, right]          大于pivot
```

各区内部可以无序，但等于pivot的值占据的最终排名区间已经确定。
因此目标下标小于`equalLeft`时只保留左侧，大于`equalRight`时只保留右侧，否则直接返回pivot。
重复元素彼此无需确定唯一身份，只需确定相同数值对应的排名范围。

例如寻找`[3,2,1,5,6,4]`第2大，目标下标为4。如果选择pivot=4，分区结果可以是：

```text
[3,2,1 | 4 | 6,5]
         3   4 5
```

下标4在右侧，只需要继续处理`[6,5]`。左侧`[3,2,1]`不需要继续排序。

## Java核心流程

以下方法可分别用于第k小和第k大，完整partition实现参见Q215代码文件：

```java
// 第k小：select(nums, k - 1)
// 第k大：select(nums, nums.length - k)
// 前提：nums非空，0 <= targetIndex < nums.length。
private int select(int[] nums, int targetIndex) {
    int left = 0;
    int right = nums.length - 1;
    while (left <= right) {
        int pivotIndex = left + (int) (Math.random() * (right - left + 1));
        swap(nums, pivotIndex, right);
        int[] equal = partition(nums, left, right);
        if (targetIndex < equal[0]) {
            right = equal[0] - 1;
        } else if (targetIndex > equal[1]) {
            left = equal[1] + 1;
        } else {
            return nums[targetIndex];
        }
    }
    throw new IllegalArgumentException("目标下标越界");
}
```

此处partition应使用Q215中“预留nums[right]作为pivot，返回等于区间闭边界”的版本。
原地分区会改变输入数组的排列；如果需要保留原数组，复制会额外增加O(N)空间。

## 为什么期望线性

**每次partition需要扫描当前区间，成本是当前区间长度，不是O(log N)。**
Quickselect的效率来自每轮只继续处理一侧。如果理想情况下每轮减半：

```text
N + N/2 + N/4 + ... < 2N
```

这解释了几何级数，但随机pivot并不保证每轮减半。
更稳妥的期望分析是：随机pivot有常数概率落在当前区间按排名排列的中间一半，此时继续保留
的区间至多约为原来的3/4。等待一次这样的有效缩减，期望只需常数次分区，于是总期望工作量
可按`N、3N/4、(3/4)^2N……`这些规模阶段求和，得到O(N)。重复值通过等值区间一起排除。

快速排序还需要处理另一侧，因此平衡情况下每层合计O(N)，有约log N层，随机化后期望
O(N log N)。Quickselect只沿目标所在的一条路径继续。

如果连续选择极端pivot，并且目标总在较大的剩余区间中：

```text
N + (N-1) + (N-2) + ... = O(N²)
```

所以随机Quickselect是**期望O(N)、最坏O(N²)**。迭代原地实现的峰值辅助空间为O(1)，
每轮返回固定长度的边界数组不会改变该空间阶数。保证最坏O(N)需要更复杂的枢轴选择算法，
例如BFPRT，不能把它的保证直接套到随机Quickselect上。

## 与大小为k的堆比较

寻找第k大时，小根堆保留目前最大的k个元素，堆顶就是当前第k大。

| 方法 | 时间 | 额外空间 | 特点 |
| --- | --- | --- | --- |
| 大小为k的小根堆 | 最坏O(N log(k+1))，通常写O(N log k) | O(k) | 支持流式输入，通常无需修改输入 |
| 随机Quickselect | 期望O(N)，最坏O(N²) | 迭代原地版本O(1) | 适合内存中数组的一次排名查询 |
| 完整排序 | 通常O(N log N) | 取决于排序实现 | 可直接回答多个排名查询 |

堆中的元素可能需要O(log k)的调整；不超过门槛的元素可以O(1)跳过。
因此不能把大小为k的堆普遍标成O(N)。`log(k+1)`是为了兼顾k=1时的严谨表达。

## 复习检查清单

- 看到无序数组第k大／第k小，是否想到了Quickselect？
- 是否先将排名转换为升序目标下标？
- 是否清楚partition返回的等值区间是闭区间？
- 分区后是否只继续处理目标所在的一侧？
- 是否把“随机化期望O(N)”误记成“每次保证减半”或“最坏O(N)”？
- 能否解释QuickSort处理两侧，而Quickselect只处理一侧？

> 荷兰国旗负责建立大小分区，Quickselect根据目标排名排除无关区域。只求一个排名时，无需完成整个排序。
