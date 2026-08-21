# SBT 解题改写手册

## 1. 模板定位

本目录把 SBT 单独作为一个专题：

- [SBTSet.java](SBTSet.java)：算法题主模板，只保存 `K`，支持顺序统计。
- [SBTSetInterview.java](SBTSetInterview.java)：精简面试默写版，只保留高频闭环。
- [SBTreeMap.java](SBTreeMap.java)：确实需要 `K -> V` 映射时使用。
- [SBTTemplateComparator.java](SBTTemplateComparator.java)：两个模板和重复值方案的对数器。
- [SBT帮助记忆.markdown](SBT帮助记忆.markdown)：分层记忆、题型识别和默写训练。
- [SBT错误汇总与修正.markdown](SBT错误汇总与修正.markdown)：个人错误、确认缺陷、正确设计与增强项。

面试默认选择 `SBTSet<K>`。数组类题目通常只关心元素顺序、排名和第 k 小，不需要 `V`。

## 2. 与当前标准 SBT 的差异

基线代码：[test20260713/SBT.java](../test20260713/SBT.java)

| 位置 | 当前标准 SBT | 新模板 |
| --- | --- | --- |
| 节点 | `SBTNode<K,V>` | `SBTSet` 删除 `V`；`SBTreeMap` 保留 `V` |
| 插入接口 | `put(K,V)` | `SBTSet.add(K)` 返回是否新增 |
| 重复数值 | 相同 key 更新 value | `(value,id)` 转成唯一复合 key |
| 顺序统计 | 没有公开接口 | 增加 `getIndexKey`、`countLess`、`countLessOrEqual` |
| 删除维护 | 回溯重算 `size` | 回溯 `pull` 后严格调用 `maintain` |
| 旋转字段更新 | 复用旧根 `size` | 旧根、新根依次执行 `pull` |

Java 模板中的每个变化都用以下前缀标记：

```java
// TODO: 【相对标准SBT的改写】
```

## 3. SBTSet 的统一能力

```java
boolean add(K key);
boolean remove(K key);
boolean contains(K key);

K getIndexKey(int index);       // 0-based，第 index 小
int countLess(K key);           // 完整 key < key 的数量
int countLessOrEqual(K key);    // 完整 key <= key 的数量

K floor(K key);
K ceiling(K key);
K first();
K last();
int size();
```

普通不重复整数直接使用：

```java
SBTSet<Integer> tree = new SBTSet<>();
tree.add(7);
tree.add(2);
tree.add(9);

int second = tree.getIndexKey(1); // 7
int rank = tree.countLess(9);     // 2
```

### size 的唯一语义

```text
cur.size = size(cur.left) + size(cur.right) + 1
```

`size` 始终表示子树的完整 key 节点数量，同时用于：

1. SBT 的规模平衡。
2. 第 k 小。
3. 排名统计。

不要把重复次数直接累加进标准 SBT 的 `size`。这样会改变平衡因子的语义；本模板统一使用复合键，
每个元素仍然对应一个独立节点。

## 4. 重复值统一改成 `(value,id)`

```java
public static final class ValueIndex implements Comparable<ValueIndex> {
    private final int value;
    private final long id;

    @Override
    public int compareTo(ValueIndex other) {
        int valueCompare = Integer.compare(value, other.value);
        return valueCompare != 0
                ? valueCompare
                : Long.compare(id, other.id);
    }
}
```

要求：

- `id` 在同一个数据集合中唯一；数组题直接使用下标。
- `value` 和 `id` 插入后不能修改，所以字段必须是 `final`。
- 比较器禁止使用减法，避免整数溢出。
- 只在 SBT 中使用时不要求 `equals/hashCode`；作为哈希 key 时再补充。

例如三个相同数值会形成三个不同的完整 key：

```text
(5,1) < (5,4) < (5,9)
```

### 旋转不会破坏 id 顺序

左旋、右旋只改变树形，不改变中序遍历。只要 `compareTo` 定义了
`value` 升序、相同 `value` 时 `id` 升序，旋转前后的中序顺序完全一致。

## 5. value 级排名和重复数量

`SBTSet` 的排名接口比较的是完整复合 key。查询纯 `value` 边界时，使用保留的哨兵 id：

```java
SBTSet<SBTSet.ValueIndex> tree = new SBTSet<>();

int less = tree.countLess(
        new SBTSet.ValueIndex(value, Long.MIN_VALUE));

int lessOrEqual = tree.countLess(
        new SBTSet.ValueIndex(value, Long.MAX_VALUE));

int equal = lessOrEqual - less;
```

语义：

```text
(value, Long.MIN_VALUE) 位于所有真实 (value,id) 之前
(value, Long.MAX_VALUE) 位于所有真实 (value,id) 之后
```

真实 `id` 不能使用两个哨兵值。数组下标为非负整数，天然满足这个约束。

## 6. 滑动窗口中位数

```java
SBTSet<SBTSet.ValueIndex> tree = new SBTSet<>();

// 进入窗口
tree.add(new SBTSet.ValueIndex(nums[right], right));

// 离开窗口：value 和 index 共同定位唯一节点
tree.remove(new SBTSet.ValueIndex(nums[left], left));

double median;
if ((k & 1) == 1) {
    median = tree.getIndexKey(k / 2).getValue();
} else {
    long a = tree.getIndexKey(k / 2 - 1).getValue();
    long b = tree.getIndexKey(k / 2).getValue();
    median = (a + b) / 2.0;
}
```

为什么推荐 `(value,index)`：

- 直接复用唯一键 SBTSet，不增加 `count/all` 等字段。
- 可以精确删除离开窗口的元素。
- `size` 继续同时服务平衡和顺序统计，不改变原模板含义。
- 物理节点数量始终等于窗口长度，严格为 `O(K)`。

## 7. 什么时候使用 SBTreeMap

只有 key 需要关联独立 value 时，才使用：

```java
SBTreeMap<Integer, String> map = new SBTreeMap<>();
map.put(10, "task-A");
map.put(10, "task-B"); // 更新 value，不增加节点
```

它支持：

```java
get / put / remove / containsKey
floorKey / ceilingKey / firstKey / lastKey
getIndexKey / getIndexValue
countLessKey / countLessOrEqualKey
```

如果附加信息本身属于一个排序元素，可以直接封装进复合 `K`，仍然使用 `SBTSet<K>`。

## 8. 隐式 SBT 适合什么题目

现有代码：[Code03_AddRemoveGetIndexGreat.java](../leetcodezuo/Code03_AddRemoveGetIndexGreat.java)

隐式 SBT 不按照 `value` 比较，而是用左子树大小表示序列下标：

```java
int leftSize = size(cur.left);

index < leftSize   -> 去左子树
index == leftSize  -> 当前节点
index > leftSize   -> 去右子树，并减去 leftSize + 1
```

适合：

- 动态数组：按下标 `add/remove/get`。
- 队列重建：按照目标排名插入元素。
- 动态序列中的第 index 个元素。
- 约瑟夫环、动态排列等位置不断变化的问题。
- 增加 lazy 标记后实现区间翻转、剪切、拼接等序列操作。

关键区别：

```text
SBTSet 的中序顺序 = key 的排序顺序
隐式 SBT 的中序顺序 = 动态序列的当前位置顺序
```

所以 `(value,id)` 万能模板覆盖的是“按值排序的动态有序表题”，不能替代隐式 SBT。
两者共享旋转、`size` 和 `maintain`，但 `add/get/remove` 的导航规则不同。

## 9. 其他增强边界

如果题目要求前 k 小元素之和、子树最大值等信息，需要在节点增加：

```java
long sum;
int max;
```

并在 `pull` 中统一更新。`size` 仍只表示节点数量，不能复用为其他含义。

区间修改、区间聚合通常优先使用线段树；频繁 `split/merge` 通常优先使用 Treap。

## 10. 面试改写检查表

1. 是否只需要排序 key？是则使用 `SBTSet`，不要携带 `V`。
2. value 是否重复？重复则定义唯一、不可变的 `(value,id)`。
3. `compareTo` 是否使用 `Integer.compare/Long.compare`？禁止直接相减。
4. 第 k 小采用 0-based 还是 1-based？本模板统一 0-based。
5. 删除是否精确传入完整复合 key？
6. 删除回溯时是否先 `pull`，再 `maintain`？
7. 偶数中位数相加前是否转成 `long`？
8. 题目维护的是数值排序还是序列位置？后者应使用隐式 SBT。

## 11. 复杂度

在 SBT 保持平衡的前提下：

| 操作 | 复杂度 |
| --- | --- |
| add / remove / contains | `O(log N)` |
| getIndexKey | `O(log N)` |
| countLess / countLessOrEqual | `O(log N)` |
| floor / ceiling | `O(log N)` |
| first / last | `O(log N)` |
| size | `O(1)` |

运行对数器：

```bash
mvn -q -DskipTests compile
java -cp target/classes treemap.sbt.SortedSBTWithRepeatedValue.SBTTemplateComparator
```

术语：对数器是测试程序；对拍是待测实现与参照实现反复比较的验证过程。
