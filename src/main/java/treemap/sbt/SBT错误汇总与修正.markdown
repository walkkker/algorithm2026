# SBT 错误汇总与修正

## 0. 错误速查总表

| 编号 | 分类 | 原错误或疑问 | 直接后果 | 修正结论 |
| --- | --- | --- | --- | --- |
| 1 | 确认错误 | `maintain(cur.l/r)` 没有接住返回值 | 旋转后的子树新根丢失，节点可能脱离树 | `cur.l = maintain(cur.l)`，右树同理 |
| 2 | 确认错误 | 递归插入写成 `cur = add(cur.l/r, key)` | 当前子树根被孩子覆盖，原树结构丢失 | `cur.l/r = add(cur.l/r, key)` |
| 3 | 确认错误 | 节点字段是 `l/r`，查找时使用 `left/right` | 编译失败或改写时访问错误字段 | 整个模板统一使用一种字段名 |
| 4 | 确认错误 | 新节点构造器漏掉 `size = 1` | 叶子规模为 0，平衡、排名、第 k 小全部错误 | 每个新节点初始化 `size = 1` |
| 5 | 确认错误 | 左右孩子声明为原始类型 `SBTNode l/r` | 丢失泛型检查，产生 unchecked 转换 | 使用 `SBTNode<K> l/r` |
| 6 | 概念错误 | 认为 `SBTNode<K>` 必须增加 `K extends Comparable<K>` | 给不负责比较的节点增加无意义约束 | 只在真正调用 `compareTo` 的 `SBTSet<K>` 上加约束 |
| 7 | 概念错误 | 把两个同名 `add/delete` 称为 override | 混淆重载和重写 | 同类同名、参数不同是 overload；子类替换父类实现才是 override |
| 8 | 概念错误 | 把 `K extends Comparable<K>` 解释为“A 必须继承 B” | 泛型上界理解错误 | 它表示 K 必须具备 `Comparable<K>` 能力 |
| 9 | 确认错误 | `ValueIndex implements Comparable` 省略泛型参数 | 使用原始接口，失去比较目标类型检查 | `implements Comparable<ValueIndex>` |
| 10 | 确认错误 | 对 `int` 调用 `value.compareTo(...)` | 基本类型没有实例方法，编译失败 | 使用 `Integer.compare(value, other.value)` |
| 11 | 确认错误 | 使用 `value - other.value` 作为比较结果 | 极值相减可能溢出并反转顺序 | 使用 `Integer.compare` / `Long.compare` |
| 12 | 确认错误 | 两个 `int` 先相加，再转 `double` 求中位数 | 加法可能在转换前溢出 | 相加前转 `long/double`，推荐 `(aLong + bLong) / 2.0` |
| 13 | 概念错误 | 认为整数 `a / 2 + b / 2` 可以安全替代平均值 | 整数除法截断，丢失 `.5` | 使用 `(aLong + bLong) / 2.0` 或 `a / 2.0 + b / 2.0` |
| 14 | 风险增强 | `getIndex` 不检查范围，非法下标返回 `null` | 错误延迟到调用点，难以定位 | 检查 `0 <= index < size`，非法时抛异常 |
| 15 | 风险增强 | `ValueIndex.value/index` 可以被修改 | 插入后修改排序字段会破坏 BST 有序性 | 字段声明为 `final` |
| 16 | 风险增强 | 递归 `delete(Node,key)` 暴露为 `public` | 调用方可绕过 `contains`，破坏 `size--` 前提 | 递归方法设为 `private`，只开放包装方法 |
| 17 | 非错误澄清 | `private add` 使用 `cur.size++` | 在“一定新增一个节点”时没有问题 | 公开 `add` 先 `contains`，因此与回溯 `pull(cur)` 等价 |
| 18 | 非错误澄清 | `private delete` 使用 `cur.size--` | 在“一定删除一个节点”时没有问题 | 公开 `delete` 先 `contains`，且双子替换后 `pull(successor)` |
| 19 | 非错误澄清 | `maintain` 内没有额外调用 `pull` | 当前调用契约下不会造成 size 错误 | 增删更新数量，旋转内部 pull，maintain 只判断和旋转 |
| 20 | 非错误澄清 | 当前中位数写成 `((double) a + b) / 2` | 不会发生 int 加法溢出 | 第一个操作数已转 double；改用 long 只是更直观 |

短时间复习时，优先检查编号 `1、2、4、5、10、11、12`，这些问题会直接导致结构错误、编译失败或答案错误。

## 1. 结论分级

### 必须修正

1. `maintain(cur.l)`、`maintain(cur.r)` 没有接住旋转后的子树新根。
2. `SBTNode l/r` 使用了原始类型，丢失泛型约束。
3. `K extends Comparable<K>`、`override` 等术语解释不准确。
4. 注释中的 `a / 2 + b / 2` 如果是整数除法，会损失小数。

### 当前实现正确，不应判错

1. `private add` 沿递归路径执行 `cur.size++`。
2. `private delete` 沿递归路径执行 `cur.size--`。
3. `maintain` 内部不额外执行 `pull`。
4. 中位数计算先将一个操作数转换成 `double`。

这些结论都依赖下文列出的调用前提。

### 可选增强，不影响本题正确性

1. `getIndex` 增加下标范围检查。
2. `ValueIndex` 字段增加 `final`。
3. 私有递归方法不暴露为 `public`。
4. `add/delete` 返回是否实际修改集合。

## 2. size++ 与 pull 的关系

你的 `add` 写法是正确的：

```java
private SBTNode<K> add(SBTNode<K> cur, K key) {
    if (cur == null) {
        return new SBTNode<>(key);
    }
    cur.size++;
    // 递归插入一个节点
    ...
    return maintain(cur);
}
```

它成立的前提是公开方法先排除重复完整 key：

```java
public void add(K key) {
    if (!contains(key)) {
        root = add(root, key);
    }
}
```

因为 `private add` 每次一定且只会新增一个节点，所以插入路径上的每个祖先都有：

```text
newSize = oldSize + 1
```

因此：

```java
cur.size++;
```

与递归结束后执行：

```java
pull(cur);
```

在结果上等价。

两种风格的区别：

| 写法 | 依据 | 优点 | 风险 |
| --- | --- | --- | --- |
| `cur.size++` | 本次一定新增一个节点 | 简短，符合经典 SBT 模板 | 私有方法若允许重复 key 或插入失败，size 会错误 |
| `pull(cur)` | 根据真实左右孩子重算 | 更稳健，容易扩展 `sum/max` | 多写一个辅助方法 |

本题中 `(value,index)` 是唯一完整 key，公开 `add` 又先调用 `contains`，所以 `size++` 没有问题。

## 3. size-- 与 pull 的关系

你的删除入口同样建立了明确前提：

```java
public void delete(K key) {
    if (contains(key)) {
        root = delete(root, key);
    }
}
```

因此 `private delete` 每次一定且只会删除一个节点，搜索路径上的每个祖先都有：

```text
newSize = oldSize - 1
```

所以递归入口执行：

```java
cur.size--;
```

是正确的。

双子节点删除时，你还执行了：

```java
cur.r = delete(cur.r, successor.k);
successor.l = cur.l;
successor.r = cur.r;
pull(successor);
cur = successor;
```

其中 `pull(successor)` 很重要，因为后继节点被提升为当前子树新根后，左右孩子都已经改变，
不能继续使用它删除前的旧 `size`。

`size--` 写法依赖以下约束：

```text
1. private delete 只能由 public delete 调用。
2. public delete 必须先通过 contains 确认 key 存在。
3. 每次调用只删除一个完整 key。
```

如果以后去掉 `contains`，或者允许私有删除处理不存在的 key，应改成递归返回阶段统一 `pull(cur)`。

## 4. overload，不是 override

下面两个 `add` 在同一个类中，方法名相同、参数列表不同：

```java
private SBTNode<K> add(SBTNode<K> cur, K key)
public void add(K key)
```

这叫方法重载：

```text
overload：同一个类中，同名方法的参数列表不同。
```

不是方法重写：

```text
override：子类重新实现父类或接口中已有的方法。
```

`delete` 的两个方法同样属于重载。

这两个重载方法的职责是：

```text
public add/delete：参数校验、contains 检查、更新 root。
private add/delete：递归修改具体子树，并返回修改后的子树新根。
```

## 5. 确认错误：maintain 必须接住返回值

### 错误语句

```java
maintain(cur.l);
maintain(cur.r);
return maintain(cur);
```

### 错误原因

`maintain(cur.l)` 可能通过旋转产生新的左子树根。忽略返回值以后，`cur.l` 仍然指向旋转前的旧根，
旋转上升的新根可能从父节点引用中丢失。

### 正确写法

```java
cur.l = maintain(cur.l);
cur.r = maintain(cur.r);
return maintain(cur);
```

记忆：

```text
凡是可能通过旋转改变子树根的递归方法，返回值都必须接回父节点指针。
```

同一规则也适用于：

```java
cur.l = add(cur.l, key);
cur.r = add(cur.r, key);
cur.l = delete(cur.l, key);
cur.r = delete(cur.r, key);
root = add(root, key);
root = delete(root, key);
```

## 6. maintain 中不需要额外 pull

当前调用链是：

```text
add/delete 负责更新节点数量
leftRotate/rightRotate 负责更新旋转节点的 size
maintain 只负责判断失衡并旋转
```

旋转方法已经执行：

```java
pull(oldRoot);
pull(newRoot);
```

递归维护某个孩子只改变树形，不改变该孩子子树的节点总数，因此父节点的 `size` 不会改变。
所以修正返回值以后：

```java
cur.l = maintain(cur.l);
cur.r = maintain(cur.r);
return maintain(cur);
```

不需要再额外调用 `pull(cur)`。

## 7. 确认错误：SBTNode 左右指针丢失泛型

### 错误语句

```java
public static class SBTNode<K extends Comparable<K>> {
    K k;
    SBTNode l;
    SBTNode r;
}
```

### 错误原因

`SBTNode l/r` 是原始类型，会产生 unchecked 警告，并失去左右孩子的编译期类型检查。

### 正确写法

```java
public static class SBTNode<K> {
    K k;
    SBTNode<K> l;
    SBTNode<K> r;
    int size;
}
```

`SBTNode` 自己没有调用 `compareTo`，因此节点类不需要 `K extends Comparable<K>`。
真正比较 key 的 `SBTSet` 才需要：

```java
public static class SBTSet<K extends Comparable<K>>
```

## 8. Comparable 语义修正

### 泛型约束

```java
K extends Comparable<K>
```

表示实际类型 `K` 必须具备 `Comparable<K>` 能力。Java 泛型上界统一使用 `extends`，
无论上界是类还是接口。

### 具体类实现接口

```java
class ValueIndex implements Comparable<ValueIndex>
```

记忆：

```text
泛型上界：extends
具体类实现接口：implements
```

下面的写法缺少比较目标类型，不应使用：

```java
implements Comparable
```

正确写法是：

```java
implements Comparable<ValueIndex>
```

## 9. 你已经正确标记的错误

### 9.1 递归返回的新根必须接回

错误：

```java
cur = add(cur.r, key);
```

正确：

```java
cur.r = add(cur.r, key);
```

左子树同理。

### 9.2 指针字段名必须统一

节点定义使用 `l/r` 时，查找中也必须使用：

```java
cur = cur.l;
cur = cur.r;
```

不能混用不存在的 `left/right`。

### 9.3 新节点必须初始化 size

```java
public SBTNode(K key) {
    this.k = key;
    this.size = 1;
}
```

漏掉 `size = 1` 会导致叶子节点规模为 0，后续旋转、第 k 小和排名全部错误。

### 9.4 int 比较不能调用实例 compareTo

`value/index` 是基本类型 `int`，不能写：

```java
value.compareTo(other.value);
```

应该使用：

```java
int cmp = Integer.compare(value, other.value);
return cmp != 0 ? cmp : Integer.compare(index, other.index);
```

也不要直接执行 `value - other.value`，极值输入可能溢出并反转比较结果。

## 10. 中位数溢出说明

你当前的实际写法是安全的：

```java
ans[index++] = ((double) left.value + right.value) / 2;
```

第一个操作数转换成 `double` 后，加法会以 `double` 执行，不会先发生 `int` 溢出。

更推荐写成：

```java
long a = left.value;
long b = right.value;
ans[index++] = (a + b) / 2.0;
```

注释中的下面写法不安全：

```java
a / 2 + b / 2
```

如果 `a/b` 是 `int`，会执行整数除法并丢失小数。只有下面这样才成立：

```java
a / 2.0 + b / 2.0
```

## 11. 可选增强项

### ValueIndex 不可变

```java
final int value;
final int index;
```

插入有序树后修改排序字段，会破坏 BST 有序性。

### getIndex 范围检查

```java
if (index < 0 || index >= size(root)) {
    throw new IndexOutOfBoundsException();
}
```

滑动窗口调用产生的下标一定合法，因此这属于模板健壮性增强。

### 限制递归方法可见性

```java
private SBTNode<K> delete(SBTNode<K> cur, K key)
```

这样可以保证调用方必须经过公开方法的 `contains` 检查，不会破坏 `size--` 的前提。

## 12. 最终检查清单

```text
[ ] Node 的左右孩子是否携带 <K>？
[ ] 新节点 size 是否初始化为 1？
[ ] ValueIndex 是否实现 Comparable<ValueIndex>？
[ ] 比较是否使用 Integer.compare，避免减法溢出？
[ ] add/delete/maintain 的子树返回值是否全部接回？
[ ] public add/delete 是否更新 root？
[ ] size++ 是否有“不重复且一定新增”的前提？
[ ] size-- 是否有“key 存在且一定删除”的前提？
[ ] 双子删除后是否重算 successor.size？
[ ] 偶数中位数是否在加法前转成 long/double？
```
