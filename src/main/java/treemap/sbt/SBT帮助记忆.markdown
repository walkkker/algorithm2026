# SBT 帮助记忆

## 1. 不要背完整参考版

完整参考：[SBTSet.java](SBTSet.java)

面试默写：[SBTSetInterview.java](SBTSetInterview.java)

SBT 应拆成三层记忆：

```text
结构核心：必须默写
查询接口：根据 BST 路径现场推导
题目适配：根据重复值、排名和删除需求临时包装
```

## 2. 必须默写的七个模块

固定按照调用依赖顺序书写：

```text
Node
  -> size / pull
  -> leftRotate / rightRotate
  -> maintain
  -> add / delete
  -> getIndexKey
  -> countLess
```

### 2.1 Node：普通 BST 加 size

```java
class Node<K> {
    K key;
    Node<K> left;
    Node<K> right;
    int size = 1;
}
```

### 2.2 pull：左右加自己

```java
void pull(Node<K> cur) {
    cur.size = size(cur.left) + size(cur.right) + 1;
}
```

```text
pull：孩子变了，当前节点重新计算字段。
maintain：规模失衡了，旋转修正树形。
```

### 2.3 旋转：旧根先掉下去，新根再上来

以左旋为例：

```java
Node<K> leftRotate(Node<K> cur) {
    Node<K> right = cur.right;
    cur.right = right.left;
    right.left = cur;
    pull(cur);    // 先更新下降的旧根
    pull(right);  // 再更新上升的新根
    return right;
}
```

右旋完全镜像。记忆：

```text
谁上来就返回谁；字段从下往上更新。
```

### 2.4 maintain：孙子欺负叔叔

```text
LL > R：右旋
LR > R：左孩子左旋，再右旋
RR > L：左旋
RL > L：右孩子右旋，再左旋
```

记忆：

```text
孙子 size 不能大于叔叔 size；
外侧违规单旋，内侧违规双旋。
```

它与 AVL 的 LL、LR、RR、RL 旋转方向一致，只是判断依据不同：

```text
AVL：height
SBT：孙子 size 与叔叔 size
```

旋转后继续执行：

```java
cur.left = maintain(cur.left);
cur.right = maintain(cur.right);
return maintain(cur);
```

没有违规时必须直接 `return cur`，否则会无限递归。

### 2.5 增删：普通 BST 加 pull、maintain

插入：

```text
BST 递归插入
-> pull(cur)
-> maintain(cur)
```

删除：

```text
无左孩子：返回右孩子
无右孩子：返回左孩子
两个孩子：复制右子树最左节点的 key，再删除后继
-> pull(cur)
-> maintain(cur)
```

### 2.6 第 k 小：只看左树大小

本模板 `index` 从 0 开始：

```text
index < leftSize：去左边
index = leftSize：当前节点就是答案
index > leftSize：去右边，并减去 leftSize + 1
```

### 2.7 排名：向右时结算

查询 `< key` 的数量：

```text
key < cur.key：向左
key = cur.key：ans + leftSize
key > cur.key：结算 leftSize + 1，然后向右
```

核心原因：当目标大于当前节点时，当前节点和整个左子树都已经确定小于目标。

`<= key` 只在相等时多计算当前节点：

```text
countLess 相等：        ans + leftSize
countLessOrEqual 相等： ans + leftSize + 1
```

## 3. 核心函数防错卡

| 函数 | 固定书写顺序 | 返回值 | 最容易写错 |
| --- | --- | --- | --- |
| `pull` | `left + right + 1` | 无 | 忘记 `+1` |
| `leftRotate` | 保存右、接中间、右接当前、pull旧、pull新 | 新根 `right` | pull 顺序反了 |
| `rightRotate` | 保存左、接中间、左接当前、pull旧、pull新 | 新根 `left` | 指针方向没有完全镜像 |
| `maintain` | 取六个 size、判断四种违规、递归维护 | 当前子树新根 | 叔叔比较错、漏稳定出口 |
| `add` | BST 插入、pull、maintain | 当前子树新根 | 忘记把返回值接回左右孩子 |
| `delete` | BST 删除、pull、maintain | 当前子树新根 | 双子后继、size、根节点没有接回 |
| `getIndexKey` | 比较 index 和 leftSize | 第 index 小 key | 去右边忘减 `leftSize + 1` |
| `countLess` | 向右时结算左树加当前 | 严格小于数量 | 相等时漏掉累计的 ans |

### 写完旋转立即检查

```text
1. 中序顺序是否不变？
2. 返回的是不是上升后的新根？
3. 是否先 pull 下降的旧根？
4. 是否再 pull 上升的新根？
```

### 写完 maintain 立即检查

```text
LL、LR 是否都和 R 比？
RR、RL 是否都和 L 比？
外侧是否单旋？内侧是否双旋？
没有违规时是否直接 return？
旋转后是否维护 left、right、cur？
```

### 写完增删立即检查

递归调用返回的是“修改后的子树根”，所以必须接回：

```java
cur.left = add(cur.left, key);
cur.right = delete(cur.right, key);
root = add(root, key);
root = delete(root, key);
```

任何一层漏接返回值，旋转产生的新根或者删除后的新根都会丢失。

### 写完顺序统计立即检查

```text
getIndexKey：当前排名 = leftSize（0-based）
countLess：目标更大时，结算 leftSize + 1
相等提前返回：必须返回 ans + leftSize，不能只返回 leftSize
countLessOrEqual 相等：再加当前节点 1
```

### 四种旋转最小验证输入

```text
LL：3, 2, 1
LR：3, 1, 2
RR：1, 2, 3
RL：1, 3, 2
```

无论触发哪种旋转，第 0、1、2 小都必须依次为 `1、2、3`。

删除至少验证：

```text
删除叶子节点
删除只有一个孩子的节点
删除拥有两个孩子的根节点
删除不存在的 key
```

这些定向用例已经加入 `SBTTemplateComparator.testInterviewCoreCases()`。

## 4. 不需要背的接口

以下方法都是普通 BST 搜索，可以现场推导：

```text
contains
floor
ceiling
first
last
```

推导规则：

```text
floor：当前 <= 目标时记录当前，继续向右找更大的。
ceiling：当前 >= 目标时记录当前，继续向左找更小的。
first：一路向左。
last：一路向右。
```

## 5. 重复值只记一个适配器

SBTSet 保存唯一完整 key。value 重复时统一包装：

```java
class ValueIndex implements Comparable<ValueIndex> {
    final int value;
    final long id;

    public int compareTo(ValueIndex other) {
        int cmp = Integer.compare(value, other.value);
        return cmp != 0 ? cmp : Long.compare(id, other.id);
    }
}
```

数组题直接使用下标作为 `id`：

```java
tree.add(new ValueIndex(nums[index], index));
tree.remove(new ValueIndex(nums[index], index));
```

字段必须不可变，比较禁止直接相减。旋转只改变树形，不改变复合 key 的中序顺序。

## 6. 只按 value 统计

真实 `(value,id)` 查询的是完整复合 key 排名。只关注 value 时使用哨兵边界：

```java
int less = tree.countLess(
        new ValueIndex(value, Long.MIN_VALUE));

int lessOrEqual = tree.countLessOrEqual(
        new ValueIndex(value, Long.MAX_VALUE));

int equal = lessOrEqual - less;
```

记忆：

```text
MIN 放在所有相同 value 之前。
MAX 放在所有相同 value 之后。
```

真实 `id` 不能使用两个哨兵值；数组非负下标天然满足要求。

## 7. 做题识别流程

### 7.1 数据是否动态变化

数据一次给出，之后只查询：

```text
排序 + 二分
```

通常不需要 SBT。

### 7.2 是否需要动态有序统计

出现以下组合时考虑 SBT：

```text
动态插入 / 删除
+
第 k 小 / 排名 / 中位数 / 前驱后继
```

### 7.3 value 是否重复

```text
不重复：SBTSet<Integer>
重复：SBTSet<ValueIndex>，key = (value,index)
```

### 7.4 按值还是按位置组织

```text
按 value 排序：SBTSet
按动态序列下标：隐式 SBT
```

## 8. 高频题型映射

| 题型 | SBT 操作 |
| --- | --- |
| 动态第 k 小 | `add/remove + getIndexKey(k)` |
| 动态排名 | `countLess(key)` |
| 重复值数量 | `countLessOrEqual(MAX) - countLess(MIN)` |
| 滑动窗口中位数 | `(value,index)` 精确增删 + 第 k 小 |
| 前驱后继 | `floor/ceiling` |
| 动态数组按下标增删 | 隐式 SBT，不是当前模板 |

## 9. 15 分钟默写训练

不要每次从完整文件抄写。按以下顺序训练：

1. 2 分钟：默写 `Node、size、pull`。
2. 3 分钟：默写左右旋，检查 `pull` 顺序。
3. 4 分钟：根据“孙子欺负叔叔”默写 `maintain`。
4. 3 分钟：默写 `add/delete`。
5. 2 分钟：默写 `getIndexKey/countLess`。
6. 1 分钟：检查 0-based、比较器溢出和删除后的 `pull + maintain`。

完成后运行：

```bash
mvn -q -DskipTests compile
java -cp target/classes treemap.sbt.SortedSBTWithRepeatedValue.SBTTemplateComparator
```

只有脱离参考文件默写并通过对数器，才算真正掌握。

## 10. 最终压缩口诀

```text
普通 BST 加 size；
孩子变化就 pull；
孙子欺负叔叔；
外单旋，内双旋；
增删回溯 pull + maintain；
第 k 小只看左树；
向右时结算左树加当前；
重复值包装 value + index。
```
