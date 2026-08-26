# K 路归并

## 相关代码

- [合并 K 个升序数组](./MergeKSortedArrays.java)
- [Q23：合并 K 个升序链表](../../LinkedList/Q23_MergeKSortedLists.java)

## 输入前提：二维数组到底哪里有序

K 路归并的前提不是“整个二维数组全局有序”，而是：

```text
每一行内部已经升序；
不同行之间不要求有序。
```

例如：

```text
[
    [1, 4, 9],
    [2, 3, 10],
    [5, 6, 7]
]
```

三行各自有序，但按行直接拼接不是全局有序结果。K 路归并利用的正是“每一行当前未处理的第一个元素，
就是该行剩余元素中的最小值”这一性质。

规则矩阵统一记号：

```text
m：行数，也是有序序列数量
n：每行元素数量
N = m * n：全部元素数量
```

如果各行不等长，则使用更通用的记号：

```text
K：非空有序序列数量
T：所有序列的元素总数
```

## 统一模型

给定 `K` 个内部有序的序列，将全部元素合并为一个有序序列。

统一记号：

- `K`：有序序列数量。
- `T`：所有序列的元素总数。
- 如果每个序列长度都是 `N`，则 `T = K * N`。

数组和链表的算法完全相同，区别只在于如何取得同一序列的下一个元素：

| 数据结构 | 堆中保存的内容 | 取得下一个元素 |
|---|---|---|
| K 个升序数组 | `(row, column)`；value可由原数组取得 | `column + 1` |
| K 个升序链表 | 当前节点 `ListNode` | `node.next` |

## 有序二维数组：K 路归并与拍平排序

### 1. K 路归并

堆中始终只保存每一行当前尚未处理的第一个元素，所以堆大小最多为 `m`：

```text
每个元素入堆、出堆：O(log m)
元素总数：m*n
总时间：O(m*n*log m)
```

这里的对数项是 `log m`，不是 `log n`。原因是堆的规模由“有多少路”决定，而不是由单行长度决定。

### 2. 最直接的方法：拍平成一维数组后排序

```text
复制所有元素：O(m*n)
排序全部元素：O(m*n*log(m*n))
```

利用：

```text
log(m*n) = log m + log n
```

两种方法可以写成：

```text
K路归并：O(m*n*log m)
拍平排序：O(m*n*(log m + log n))
```

### 3. 为什么有时感觉差别不大

当 `m` 与 `n` 的数量级接近，例如 `m=n`：

```text
K路归并：O(n^2*log n)
拍平排序：O(n^2*log(n^2)) = O(2*n^2*log n)
```

忽略常数以后，两者属于同一渐进数量级。并且 Java 的 `Arrays.sort(int[])` 针对基本类型数组高度优化，
而 `PriorityQueue<int[]>` 或 `PriorityQueue<Node>`存在比较器调用、堆调整和对象分配开销。因此在数据规模
不大、需要输出全部元素时，拍平后排序不仅代码短，实际运行也可能更快。

但当行数很少、每行很长时，K 路归并的优势明显。例如 `m=4`、`n` 很大：

```text
K路归并：O(N*log 4)，接近O(N)
拍平排序：O(N*log N)
```

极端情况下只有一行，输入本身已经有序，K 路处理只需 `O(N)`，重新排序则需要 `O(N log N)`。

### 4. K 路归并的额外价值

即使两种方法在某些规模下同阶，K 路归并仍然具有以下能力：

1. 利用了各行已有的有序性，不重新排序全部元素。
2. 可以流式输出，每弹出一个堆顶就得到下一个全局最小值。
3. 如果只需要前 `P` 小，只处理 `P` 个元素，时间为 `O(P log m)`。
4. 堆只保存每一路的一个候选，额外空间为 `O(m)`，不计算输出结果。

## 无序二维数组：先逐行排序是否值得

如果每一行原本无序，先把每行排序，再进行 K 路归并：

```text
逐行排序：m * O(n log n) = O(m*n*log n)
K路归并：O(m*n*log m)
总时间：O(m*n*(log n + log m))
       = O(m*n*log(m*n))
```

也可以把两个阶段写成：

```text
O(m*n*log n + m*n*log m)
```

写成两项的最大值在渐进意义上也同阶，因为对于非负的 `A、B`：

```text
max(A,B) <= A+B <= 2*max(A,B)
```

但是保留加法更能表现“行内排序 + K 路归并”两个实际阶段。

如果直接拍平后统一排序：

```text
O(m*n*log(m*n))
```

因此，当各行原本无序，并且题目要求输出全部排序结果时，两种方案的渐进时间复杂度相同。此时通常优先：

```java
复制到int[] -> Arrays.sort(result)
```

它更短、更容易验证，而且通常有更小的运行时常数。先逐行排序再 K 路归并只有在还需要保留每行有序结果、
支持分阶段并行排序、外部排序或流式合并时才有额外价值。

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

### 数组版本核心代码：只保存 row、column

当原二维数组在整个归并过程中可访问且内容不发生变化时，堆节点不需要重复保存 value。通过
`matrix[row][column]`即可取得比较值：

```java
int[] mergeByPositionHeap(int[][] matrix) {
    if (matrix == null || matrix.length == 0) {
        return new int[0];
    }

    int total = 0;
    PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) ->
            Integer.compare(
                    matrix[a[0]][a[1]],
                    matrix[b[0]][b[1]]
            ));

    for (int row = 0; row < matrix.length; row++) {
        if (matrix[row] != null && matrix[row].length > 0) {
            total += matrix[row].length;
            heap.offer(new int[]{row, 0});
        }
    }

    int[] ans = new int[total];
    int write = 0;

    while (!heap.isEmpty()) {
        int[] position = heap.poll();
        int row = position[0];
        int column = position[1];

        ans[write++] = matrix[row][column];

        int nextColumn = column + 1;
        if (nextColumn < matrix[row].length) {
            heap.offer(new int[]{row, nextColumn});
        }
    }

    return ans;
}
```

`PriorityQueue<int[]>`没有自然排序规则，必须提供 Comparator。并且不要修改仍在堆内的 `int[]` 中
参与比较的 `row、column`；`PriorityQueue`不会在对象字段变化后自动重新建堆。推荐每次加入一个新的：

```java
new int[]{row, nextColumn}
```

### 自定义节点版本

如果希望字段语义更清晰，可以封装位置。value 仍然是可选字段：

```java
static class Position {
    int row;
    int column;

    Position(int row, int column) {
        this.row = row;
        this.column = column;
    }
}

PriorityQueue<Position> heap = new PriorityQueue<>((a, b) ->
        Integer.compare(
                matrix[a.row][a.column],
                matrix[b.row][b.column]
        ));
```

如果数据来自流、原矩阵不能持续访问，或者希望比较器不捕获外部矩阵，也可以把 value 一并保存：

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

关键点不是“必须保存 value”，而是**必须保存来源位置或后继访问方式**。只保存 value 无法知道弹出后
应该把哪一行的下一个元素加入堆；保存 `(row,column)`后，value 可以通过原矩阵按需取得。

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
| 拍平后全量排序 | `O(T log T)` | 结果数组`O(T)` | 最简单；输入无序时通常优先 |

面试选择依据：

```text
每行有序，题目明确要求利用有序性：小根堆或平衡两两归并。
每行有序，只取前P小或需要流式输出：小根堆。
每行无序，需要输出全部结果：拍平后Arrays.sort通常最务实。
```

如果面试题明确是“合并 K 个有序数组”，应主动利用输入有序性；也可以先说明拍平排序的正确基线，
再给出 `O(T log K)` 的 K 路归并，体现自己理解两种方案的真实差异。
