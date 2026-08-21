## SBT 专题

- [SBT 解题改写手册](sbt/SBT解题改写手册.markdown)：万能 `SBTSet`、复合键重复值、顺序统计、隐式 SBT 和 `SBTreeMap`。
- [SBT 帮助记忆](sbt/SBT帮助记忆.markdown)：最小默写闭环、口诀、题型映射和训练顺序。
- [SBT 错误汇总与修正](sbt/SBT错误汇总与修正.markdown)：区分必须修正、当前正确设计和可选增强。
- 完整模板位于 `treemap.sbt` 子包，所有相对标准 SBT 的变化均使用 `TODO: 【相对标准SBT的改写】` 标记。

## 错误警示： SBT也不支持同一个节点存储多个相同元素！！！  节点之间必须独立，不能合并
1. 我通过size++的方式允许key相同的节点加入treemap。 需要修改delete方法，put方法或者直接调用add方法，以及getindexkey方法
   这类 delete和getindexkey都涉及到 获取当前节点的size，
   重点： 超级大错误，我错就错在这里 -》 当前节点的size不能直接取 cur.size！！！！
2. cur.size代表的是cur为头的整棵子树的size。
   正确求cur.size需要 剪掉左右子树的size，单独计算一个变量！！！！！
3. 但是这个方法不够好，因为你通过size记录了 重复元素的数量，但是这也打破了平衡因子size的本来的含义，从而破坏平衡性。 举个例子，cur.l是一个元素，但是超大size。 cur右子树不断添加全局最大元素，那么最右边界会无限延长，但是永远不触发平衡动作。 此时平衡性被破坏！！！！
4. 综上，size增加 来保存 重复元素 的方法不可取！
5. 最终结论，还是要使用左神的方法，public static class Node{int index, int value} + 实现Comparable方法，先比较value升序，相同时比较index升序。   SBT只有一处修改: 仅保留SBT<K>

## SBT vs. AVL 实现区别
下面这份可以直接作为笔记保存：

```java
/**
 * SBT 与 AVL 的 Java 实现区别
 *
 * 一、节点维护信息不同
 *
 * AVL：
 * 1. 每个节点维护高度 h。
 * 2. 平衡条件：
 *      abs(height(left) - height(right)) <= 1
 *
 * SBT：
 * 1. 每个节点维护子树节点数量 size。
 * 2. 平衡条件：
 *      每棵孙子树的 size 不能大于其叔叔子树的 size。
 *
 *
 * 二、节点定义不同
 *
 * AVL：
 * class Node {
 *     K key;
 *     V value;
 *     Node left;
 *     Node right;
 *     int height;
 * }
 *
 * SBT：
 * class Node {
 *     K key;
 *     V value;
 *     Node left;
 *     Node right;
 *     int size;
 * }
 *
 *
 * 三、维护字段的计算方式不同
 *
 * AVL：
 * cur.height = Math.max(height(cur.left), height(cur.right)) + 1;
 *
 * SBT：
 * cur.size = size(cur.left) + size(cur.right) + 1;
 *
 *
 * 四、判断旋转方向不同
 *
 * AVL 根据高度判断 LL、LR、RR、RL：
 *
 * leftHeight - rightHeight > 1 说明左侧失衡；
 * rightHeight - leftHeight > 1 说明右侧失衡。
 *
 * 然后通过孩子的左右子树高度，进一步判断单旋还是双旋。
 *
 *
 * SBT 根据 size 判断 LL、LR、RR、RL：
 *
 * LL：size(cur.left.left)  > size(cur.right)
 * LR：size(cur.left.right) > size(cur.right)
 * RR：size(cur.right.right) > size(cur.left)
 * RL：size(cur.right.left)  > size(cur.left)
 *
 *
 * 五、maintain 方法不同
 *
 * AVL：
 * 1. 单旋或双旋后，当前子树即可恢复平衡。
 * 2. maintain 本身通常不需要递归调用。
 * 3. 祖先节点由 add/delete 的递归回溯继续维护。
 *
 * SBT：
 * 1. 旋转只能先修复当前发现的 size 违规。
 * 2. 旋转会改变多棵子树之间的关系。
 * 3. 旋转后可能在孩子节点或新根节点产生新的失衡。
 * 4. 因此 maintain 通常需要递归维护孩子和当前根。
 *
 * 例如 LL 型：
 *
 * cur = rightRotate(cur);
 * cur.right = maintain(cur.right);
 * cur = maintain(cur);
 *
 *
 * 六、旋转后更新的信息不同
 *
 * AVL 旋转后更新 height：
 *
 * updateHeight(oldRoot);
 * updateHeight(newRoot);
 *
 * SBT 旋转后更新 size：
 *
 * updateSize(oldRoot);
 * updateSize(newRoot);
 *
 * 注意：必须先更新下降的旧根，再更新上升的新根。
 *
 *
 * 七、插入过程不同
 *
 * AVL：
 * 1. 按照 BST 规则插入。
 * 2. 递归回溯时更新 height。
 * 3. 调用 maintain 修复高度失衡。
 *
 * SBT：
 * 1. 按照 BST 规则插入。
 * 2. 插入路径上的节点 size 增加。
 * 3. 调用 maintain 修复规模失衡。
 *
 * Map 场景需要注意：
 * 如果 key 已存在，只更新 value，不能重复增加 size。
 *
 *
 * 八、删除过程不同
 *
 * AVL：
 * 1. 按照 BST 规则删除。
 * 2. 使用右子树最左节点作为后继节点。
 * 3. 递归回溯时重新计算 height。
 * 4. 每一层都需要调用 maintain。
 *
 * SBT：
 * 1. 删除路径上的节点需要重新计算或减少 size。
 * 2. 两个孩子都存在时，同样可以使用后继节点替换。
 * 3. 严格实现应在删除后重新执行 maintain。
 * 4. 部分竞赛模板删除后不 maintain，代码较短，但可能降低平衡质量。
 *
 *
 * 九、功能侧重点不同
 *
 * AVL：
 * 1. 主要用于保证查找、插入、删除为 O(logN)。
 * 2. 适合实现有序 Map、Set。
 * 3. 只维护 height 时，不方便实现排名查询。
 *
 * SBT：
 * 1. size 是结构本身必须维护的信息。
 * 2. 天然支持排名相关操作。
 * 3. 可以快速实现：
 *      第 k 小元素
 *      key 的排名
 *      根据下标获取元素
 *
 *
 * 十、面试记忆
 *
 * AVL：
 * 高度平衡，维护 height；
 * maintain 不递归；
 * 增删回溯时逐层维护。
 *
 * SBT：
 * 规模平衡，维护 size；
 * 孙子和叔叔比较；
 * 旋转后需要递归 maintain；
 * 天然支持排名查询。
 */
```

最核心的对照可以压缩成一句话：

> AVL 比较左右子树的高度，旋转后当前层直接恢复；SBT 比较孙子树与叔叔树的节点数量，旋转会重组规模关系，因此需要递归维护。
