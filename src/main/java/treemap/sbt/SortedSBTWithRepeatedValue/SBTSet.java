package treemap.sbt.SortedSBTWithRepeatedValue;

/**
 * 面向算法题的顺序统计 SBT Set 模板。
 *
 * <h3>直接使用结论</h3>
 *
 * <p>1. 不重复 key：直接使用 {@code SBTSet<Integer>} 或其他 {@code Comparable} 类型。</p>
 *
 * <pre>
 * SBTSet&lt;Integer&gt; tree = new SBTSet&lt;&gt;();
 * tree.add(num);
 * tree.remove(num);
 * int kth = tree.getIndexKey(k); // k 是 0-based
 * int less = tree.countLess(num);
 * int lessOrEqual = tree.countLessOrEqual(num);
 * </pre>
 *
 * <p>2. value 允许重复：统一使用 {@code (value,id)} 作为唯一复合 key。
 * 数组和滑动窗口题直接使用数组下标作为 id。</p>
 *
 * <pre>
 * SBTSet&lt;ValueIndex&gt; tree = new SBTSet&lt;&gt;();
 * tree.add(new ValueIndex(nums[index], index));
 * tree.remove(new ValueIndex(nums[index], index)); // value 和 index 必须与插入时一致
 * </pre>
 *
 * <p>3. 传入真实 {@code (value,id)} 时，排名针对完整复合 key。
 * 如果只想按照 value 统计，必须使用 id 的左右边界：</p>
 *
 * <pre>
 * int less = tree.countLess(
 *         new ValueIndex(value, Long.MIN_VALUE));       // value &lt; target
 *
 * int lessOrEqual = tree.countLessOrEqual(
 *         new ValueIndex(value, Long.MAX_VALUE));       // value &lt;= target
 *
 * int equal = lessOrEqual - less;                       // value == target
 * </pre>
 *
 * <p>约束：{@code Long.MIN_VALUE} 和 {@code Long.MAX_VALUE} 保留为查询哨兵，
 * 不能作为真实 id；数组非负下标天然满足要求。</p>
 *
 * <p>4. 第 k 小和中位数：</p>
 *
 * <pre>
 * int kthValue = tree.getIndexKey(k).getValue();        // k 是 0-based
 *
 * // 窗口长度为奇数
 * double median = tree.getIndexKey(windowSize / 2).getValue();
 *
 * // 窗口长度为偶数，相加前先转 long，防止 int 溢出
 * long a = tree.getIndexKey(windowSize / 2 - 1).getValue();
 * long b = tree.getIndexKey(windowSize / 2).getValue();
 * double median = (a + b) / 2.0;
 * </pre>
 *
 * <p>5. 复合 key 的 {@code compareTo} 必须先比较 value，再比较唯一 id；
 * 字段必须不可变。SBT 旋转只改变树形，不改变中序顺序，因此不会破坏
 * 相同 value 下的 id 排序。</p>
 *
 * <p>6. 本模板只保存唯一完整 key，不保存额外 value。确实需要
 * {@code K -> V} 映射时使用 {@link SBTreeMap}。</p>
 *
 * <p>相对于 treemap.test20260713.SBT.SBTreeMap 的所有结构性变化，
 * 都使用“TODO: 【相对标准SBT的改写】”标记。</p>
 */
public class SBTSet<K extends Comparable<K>> {

    /**
     * TODO: 【相对标准SBT的改写1：删除 V】
     * （1）原模板：SBTNode<K, V> 同时保存 key 和 value。
     * （2）改写原因：多数算法题只需要排序、排名和第 k 小，不需要 K -> V 映射。
     * （3）迁移方式：节点只保留 K；需要携带下标等信息时，直接封装进复合 key。
     */
    private static class SBTNode<K> {
        private K key;
        private SBTNode<K> left;
        private SBTNode<K> right;
        private int size;

        private SBTNode(K key) {
            this.key = key;
            this.size = 1;
        }
    }

    private SBTNode<K> root;

    private int size(SBTNode<K> node) {
        return node == null ? 0 : node.size;
    }

    /**
     * pull 的含义是：从左右孩子“向上拉取并汇总”信息，重新计算当前节点的聚合字段。
     *
     * <p>当前 SBT 只维护 size，所以计算公式是：</p>
     * <pre>
     * node.size = left.size + right.size + 1
     * </pre>
     *
     * <p>它等价于原模板中反复手写的 size 重算语句，但集中成方法后，旋转、插入和删除
     * 都可以复用。以后增加 sum、max 等子树聚合字段，也应该统一在 pull 中更新。</p>
     *
     * <p>职责区分：</p>
     * <ul>
     *     <li>pull：根据当前树形修正节点字段。</li>
     *     <li>maintain：根据 size 判断是否失衡，并通过旋转修正树形。</li>
     * </ul>
     */
    private void pull(SBTNode<K> node) {
        node.size = size(node.left) + size(node.right) + 1;
    }

    private SBTNode<K> leftRotate(SBTNode<K> cur) {
        SBTNode<K> right = cur.right;
        cur.right = right.left;
        right.left = cur;

        /*
         * TODO: 【相对标准SBT的改写2：统一使用 pull】
         * （1）原模板：right.size = cur.size，再单独重算 cur.size。
         * （2）改写原因：两种写法等价；统一 pull 更适合删除后重新 maintain，字段来源更直观。
         * （3）迁移方式：先更新下降的旧根 cur，再更新上升的新根 right。
         */
        // 旋转后必须从下往上更新：新根 right.size 依赖下降后的旧根 cur.size。
        pull(cur);
        pull(right);
        return right;
    }

    private SBTNode<K> rightRotate(SBTNode<K> cur) {
        SBTNode<K> left = cur.left;
        cur.left = left.right;
        left.right = cur;
        // 旋转后必须从下往上更新：新根 left.size 依赖下降后的旧根 cur.size。
        pull(cur);
        pull(left);
        return left;
    }

    /**
     * 与当前标准 SBT 相同：依次检查 LL、LR、RR、RL，旋转后递归维护孩子和新根。
     */
    private SBTNode<K> maintain(SBTNode<K> cur) {
        if (cur == null) {
            return null;
        }
        int leftSize = size(cur.left);
        int rightSize = size(cur.right);
        int leftLeftSize = cur.left == null ? 0 : size(cur.left.left);
        int leftRightSize = cur.left == null ? 0 : size(cur.left.right);
        int rightLeftSize = cur.right == null ? 0 : size(cur.right.left);
        int rightRightSize = cur.right == null ? 0 : size(cur.right.right);

        if (leftLeftSize > rightSize) {
            cur = rightRotate(cur);
        } else if (leftRightSize > rightSize) {
            cur.left = leftRotate(cur.left);
            cur = rightRotate(cur);
        } else if (rightRightSize > leftSize) {
            cur = leftRotate(cur);
        } else if (rightLeftSize > leftSize) {
            cur.right = rightRotate(cur.right);
            cur = leftRotate(cur);
        } else {
            // TODO： 【超级重要】递归 maintain 必须有稳定出口，如果没有违规，那么直接返回；否则已平衡节点会无限递归。
            return cur;
        }

        cur.left = maintain(cur.left);
        cur.right = maintain(cur.right);
        return maintain(cur);
    }

    private SBTNode<K> add(SBTNode<K> cur, K key) {
        if (cur == null) {
            return new SBTNode<>(key);
        }
        if (key.compareTo(cur.key) < 0) {
            cur.left = add(cur.left, key);
        } else {
            cur.right = add(cur.right, key);
        }
        pull(cur);
        return maintain(cur);
    }

    /**
     * TODO: 【相对标准SBT的改写3：put 改为 add】
     * （1）原模板：put(K,V)；相同 key 更新 value。
     * （2）改写原因：Set 没有 value，相同完整 key 不重复插入。
     * （3）迁移方式：add 返回是否真的新增；重复 value 使用不同 id 组成不同完整 key。
     */
    public boolean add(K key) {
        requireKey(key);
        if (contains(key)) {
            return false;
        }
        root = add(root, key);
        return true;
    }

    private SBTNode<K> delete(SBTNode<K> cur, K key) {
        int compare = key.compareTo(cur.key);
        if (compare < 0) {
            cur.left = delete(cur.left, key);
        } else if (compare > 0) {
            cur.right = delete(cur.right, key);
        } else {
            if (cur.left == null) {
                return cur.right;
            }
            if (cur.right == null) {
                return cur.left;
            }

            SBTNode<K> successor = cur.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            cur.right = delete(cur.right, successor.key);
            successor.left = cur.left;
            successor.right = cur.right;
            cur = successor;
        }

        /*
         * TODO: 【相对标准SBT的改写4：删除后 pull + maintain】
         * （1）原模板：递归入口 cur.size--，返回前重算 size，但不重新 maintain。
         * （2）改写原因：连续动态删除可能破坏规模平衡；直接重算比依赖 size-- 更稳健。
         * （3）迁移方式：子树删除完成后，先 pull 当前节点，再严格执行 maintain。
         */
        pull(cur);
        return maintain(cur);
    }

    public boolean remove(K key) {
        requireKey(key);
        if (!contains(key)) {
            return false;
        }
        root = delete(root, key);
        return true;
    }

    public boolean contains(K key) {
        requireKey(key);
        SBTNode<K> cur = root;
        while (cur != null) {
            int compare = key.compareTo(cur.key);
            if (compare == 0) {
                return true;
            }
            cur = compare < 0 ? cur.left : cur.right;
        }
        return false;
    }

    /**
     * TODO: 【相对标准SBT的改写5：增加顺序统计接口】
     * （1）原模板：只提供普通 Map 查询，没有根据 size 查询第 k 小。
     * （2）改写原因：第 k 小和排名是算法题选择 SBT 的核心价值。
     * （3）迁移方式：index 使用 0-based；比较 index 与左子树 size。
     */
    public K getIndexKey(int index) {
        checkIndex(index);
        SBTNode<K> cur = root;
        while (cur != null) {
            int leftSize = size(cur.left);
            if (index < leftSize) {
                cur = cur.left;
            } else if (index == leftSize) {
                return cur.key;
            } else {
                index -= leftSize + 1;
                cur = cur.right;
            }
        }
        throw new IllegalStateException("unreachable index search state");
    }

    /** 返回严格小于 key 的完整 key 数量。 */
    public int countLess(K key) {
        requireKey(key);
        int ans = 0;
        SBTNode<K> cur = root;
        while (cur != null) {
            if (key.compareTo(cur.key) <= 0) {
                cur = cur.left;
            } else {
                ans += size(cur.left) + 1;
                cur = cur.right;
            }
        }
        return ans;
    }

    /** 返回小于等于 key 的完整 key 数量。 */
    public int countLessOrEqual(K key) {
        requireKey(key);
        int ans = 0;
        SBTNode<K> cur = root;
        while (cur != null) {
            if (key.compareTo(cur.key) < 0) {
                cur = cur.left;
            } else {
                ans += size(cur.left) + 1;
                cur = cur.right;
            }
        }
        return ans;
    }

    public K floor(K key) {
        requireKey(key);
        SBTNode<K> cur = root;
        SBTNode<K> ans = null;
        while (cur != null) {
            int compare = key.compareTo(cur.key);
            if (compare < 0) {
                cur = cur.left;
            } else {
                ans = cur;
                if (compare == 0) {
                    break;
                }
                cur = cur.right;
            }
        }
        return ans == null ? null : ans.key;
    }

    public K ceiling(K key) {
        requireKey(key);
        SBTNode<K> cur = root;
        SBTNode<K> ans = null;
        while (cur != null) {
            int compare = key.compareTo(cur.key);
            if (compare > 0) {
                cur = cur.right;
            } else {
                ans = cur;
                if (compare == 0) {
                    break;
                }
                cur = cur.left;
            }
        }
        return ans == null ? null : ans.key;
    }

    public K first() {
        if (root == null) {
            return null;
        }
        SBTNode<K> cur = root;
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur.key;
    }

    public K last() {
        if (root == null) {
            return null;
        }
        SBTNode<K> cur = root;
        while (cur.right != null) {
            cur = cur.right;
        }
        return cur.key;
    }

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size());
        }
    }

    private void requireKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
    }

    /**
     * TODO: 【相对标准SBT的改写6：复合键支持重复 value】
     * （1）原模板：K 唯一，相同 key 只能更新 value，不能保存多个相同数值。
     * （2）改写原因：滑动窗口中会同时存在多个相同 value，并且需要精确删除离开窗口的元素。
     * （3）迁移方式：以 (value, index) 作为完整 key；先比较 value，再比较唯一 id。
     *
     * <p>只供 SBT/TreeSet 排序时不需要 equals 和 hashCode。字段必须不可变，插入后修改
     * value 或 id 会破坏 BST 有序性。</p>
     */
    public static final class ValueIndex implements Comparable<ValueIndex> {
        private final int value;
        private final long id;

        public ValueIndex(int value, long id) {
            this.value = value;
            this.id = id;
        }

        public int getValue() {
            return value;
        }

        public long getId() {
            return id;
        }

        @Override
        public int compareTo(ValueIndex other) {
            int valueCompare = Integer.compare(value, other.value);
            return valueCompare != 0 ? valueCompare : Long.compare(id, other.id);
        }

        @Override
        public String toString() {
            return "(" + value + "," + id + ")";
        }
    }

    /**
     * 核心接口使用示例。
     *
     * <p>重点区分：</p>
     * <ul>
     *     <li>传入真实 (value,id)：查询的是完整复合 key 的排名。</li>
     *     <li>传入 (value, MIN_ID/MAX_ID)：查询的是只关注 value 的边界排名。</li>
     * </ul>
     */
    public static void main(String[] args) {
        integerKeyExample();
        valueIndexExample();
    }

    private static void integerKeyExample() {
        SBTSet<Integer> tree = new SBTSet<>();
        tree.add(7);
        tree.add(2);
        tree.add(9);
        tree.add(5);

        System.out.println("=== Integer key ===");
        System.out.println("size = " + tree.size());                    // 4
        System.out.println("index 0 = " + tree.getIndexKey(0));        // 2
        System.out.println("index 2 = " + tree.getIndexKey(2));        // 7
        System.out.println("countLess(7) = " + tree.countLess(7));     // 2: 2、5
        System.out.println("countLessOrEqual(7) = "
                + tree.countLessOrEqual(7));                            // 3: 2、5、7
        System.out.println("floor(6) = " + tree.floor(6));             // 5
        System.out.println("ceiling(6) = " + tree.ceiling(6));         // 7
    }

    private static void valueIndexExample() {
        SBTSet<ValueIndex> tree = new SBTSet<>();

        // value 可以重复，index/id 保证完整 key 唯一。
        tree.add(new ValueIndex(5, 0));
        tree.add(new ValueIndex(5, 3));
        tree.add(new ValueIndex(6, 1));
        tree.add(new ValueIndex(6, 4));
        tree.add(new ValueIndex(8, 2));

        System.out.println("=== Composite key: (value,index) ===");
        for (int index = 0; index < tree.size(); index++) {
            System.out.println("index " + index + " = " + tree.getIndexKey(index));
        }

        ValueIndex realKey = new ValueIndex(6, 4);

        /*
         * 这里比较完整复合 key：
         * 小于 (6,4) 的是 (5,0)、(5,3)、(6,1)，所以结果为 3。
         */
        System.out.println("countLess((6,4)) = "
                + tree.countLess(realKey));                             // 3

        /*
         * 小于等于 (6,4) 时还包括 (6,4) 本身，所以结果为 4。
         * 这不是“value <= 6”的通用写法，而是“完整 key <= (6,4)”。
         */
        System.out.println("countLessOrEqual((6,4)) = "
                + tree.countLessOrEqual(realKey));                      // 4

        /*
         * 只统计 value < 6：
         * (6, Long.MIN_VALUE) 排在所有真实的 (6,id) 之前。
         */
        int valueLessCount = tree.countLess(
                new ValueIndex(6, Long.MIN_VALUE));

        /*
         * 只统计 value <= 6：
         * (6, Long.MAX_VALUE) 排在所有真实的 (6,id) 之后。
         *
         * 这里使用 countLessOrEqual，语义最直观。由于 Long.MAX_VALUE 被保留为
         * 边界 id、不会作为真实 id 插入，使用 countLess 也会得到相同结果。
         */
        int valueLessOrEqualCount = tree.countLessOrEqual(
                new ValueIndex(6, Long.MAX_VALUE));

        int valueEqualCount = valueLessOrEqualCount - valueLessCount;

        System.out.println("value < 6 count = " + valueLessCount);      // 2
        System.out.println("value <= 6 count = "
                + valueLessOrEqualCount);                               // 4
        System.out.println("value == 6 count = " + valueEqualCount);   // 2

        /*
         * 滑动窗口精确删除：必须使用进入窗口时相同的 value 和 index。
         */
        tree.remove(new ValueIndex(6, 1));
        System.out.println("remove (6,1), value == 6 count = "
                + (tree.countLessOrEqual(new ValueIndex(6, Long.MAX_VALUE))
                - tree.countLess(new ValueIndex(6, Long.MIN_VALUE)))); // 1
    }
}
