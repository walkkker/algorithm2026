# K 路归并

## 相关代码

- [合并 K 个升序数组](./MergeKSortedArrays.java)
- [Q23：合并 K 个升序链表](../../LinkedList/Q23_MergeKSortedLists.java)

## 统一模型

给定 `K` 个内部有序的序列，将全部元素合并为一个有序序列。

统一记号：

- `K`：有序序列数量。
- `T`：所有序列的元素总数。
- 如果每个序列长度都是 `N`，则 `T = K * N`。

数组和链表的算法完全相同，区别只在于如何取得同一序列的下一个元素：

| 数据结构 | 堆中保存的内容 | 取得下一个元素 |
|---|---|---|
| K 个升序数组 | `(value, arrayIndex, elementIndex)` | `elementIndex + 1` |
| K 个升序链表 | 当前节点 `ListNode` | `node.next` |

## 方法一：小根堆

### 核心思想

任何时刻，最终结果中的下一个最小值，只可能来自每个序列当前尚未处理的第一个元素。

1. 将每个非空序列的第一个元素放入小根堆。
2. 弹出堆顶，将其值写入答案。
3. 将它所属序列的下一个元素放入堆。
4. 重复以上过程，直到堆为空。

堆中最多只有 `K` 个元素，因此：

- 时间复杂度：`O(T log K)`。
- 额外空间复杂度：`O(K)`，不计算返回数组。

### 数组版本核心代码

```java
static class HeapNode {
    int value;
    int arrayIndex;
    int elementIndex;

    HeapNode(int value, int arrayIndex, int elementIndex) {
        this.value = value;
        this.arrayIndex = arrayIndex;
        this.elementIndex = elementIndex;
    }
}

PriorityQueue<HeapNode> heap =
        new PriorityQueue<>((a, b) -> Integer.compare(a.value, b.value));

for (int i = 0; i < arrays.length; i++) {
    if (arrays[i] != null && arrays[i].length > 0) {
        heap.add(new HeapNode(arrays[i][0], i, 0));
    }
}

while (!heap.isEmpty()) {
    HeapNode cur = heap.poll();
    ans[write++] = cur.value;

    int nextIndex = cur.elementIndex + 1;
    if (nextIndex < arrays[cur.arrayIndex].length) {
        heap.add(new HeapNode(
                arrays[cur.arrayIndex][nextIndex],
                cur.arrayIndex,
                nextIndex
        ));
    }
}
```

关键点：堆节点不能只保存 `value`，还必须保存它来自哪个数组以及数组内下标，否则无法找到同一序列的下一个元素。

## 方法二：平衡的两两归并

### 核心思想

像归并排序一样，先两两合并，再对合并结果继续两两合并：

```text
A B C D E F G H
 \ /   \ /   \ /   \ /
 AB    CD    EF    GH
   \  /        \  /
   ABCD        EFGH
       \      /
       ABCDEFGH
```

树高为 `log K`。在每一层中，所有归并操作处理的元素数量之和都是 `T`：

```text
每层 O(T) × log K 层 = O(T log K)
```

额外空间通常为 `O(T)`，用于保存归并结果。

### 两个数组的归并模板

```java
int[] mergeTwo(int[] a, int[] b) {
    int[] ans = new int[a.length + b.length];
    int p1 = 0;
    int p2 = 0;
    int write = 0;

    while (p1 < a.length && p2 < b.length) {
        ans[write++] = a[p1] <= b[p2] ? a[p1++] : b[p2++];
    }
    while (p1 < a.length) {
        ans[write++] = a[p1++];
    }
    while (p2 < b.length) {
        ans[write++] = b[p2++];
    }
    return ans;
}
```

## 为什么不是 `O(KT)`

“两两归并”必须区分两种组织方式。

### 平衡两两归并

假设有 `8` 个数组，每个长度为 `N`，总元素数 `T = 8N`：

```text
第 1 层：4 次合并，每次处理 2N，共 8N = T
第 2 层：2 次合并，每次处理 4N，共 8N = T
第 3 层：1 次合并，处理 8N，共 8N = T
```

一共 `log 8 = 3` 层，所以复杂度为：

```text
O(T log K)
```

### 顺序累积归并

如果按照下面的顺序合并：

```text
result = merge(A, B)
result = merge(result, C)
result = merge(result, D)
...
```

当每个原始数组长度均为 `N` 时，处理量为：

```text
2N + 3N + 4N + ... + KN = O(NK^2)
```

因为 `T = KN`，也可以写成：

```text
O(TK)
```

退化的原因不是两个有序数组的 `merge` 不够快，而是已经合并过的元素被反复扫描了很多次。

## 面试选择

| 方法 | 时间复杂度 | 额外空间 | 特点 |
|---|---:|---:|---|
| 小根堆 | `O(T log K)` | `O(K)` | 直接、通用，适合流式取出结果 |
| 平衡两两归并 | `O(T log K)` | 通常 `O(T)` | 复用两个有序序列的归并模板 |
| 顺序累积归并 | `O(TK)` | 通常 `O(T)` | 实现简单，但会反复扫描已有结果 |

面试时优先选择小根堆；如果已经有可靠的 `mergeTwo` 模板，也可以使用平衡的分治归并。
