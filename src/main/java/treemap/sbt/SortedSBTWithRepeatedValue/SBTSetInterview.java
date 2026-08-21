package treemap.sbt.SortedSBTWithRepeatedValue;

/**
 * SBTSet 面试默写版：只保留最小结构闭环和高频顺序统计。
 *
 * <p>默写顺序固定为：</p>
 * <pre>
 * Node -> size/pull -> 左右旋 -> maintain
 *      -> add/delete -> getIndex -> countLess
 * </pre>
 *
 * <p>核心口诀：</p>
 * <pre>
 * 节点：普通 BST + size
 * pull：左 + 右 + 自己
 * 旋转：先改指针，先 pull 旧根，再 pull 新根
 * 平衡：孙子欺负叔叔；外侧单旋，内侧双旋
 * 增删：普通 BST + pull + maintain
 * 第 k 小：比较 k 和左树大小，去右边减 leftSize + 1
 * 排名：目标大于当前，结算“左树 + 当前”
 * </pre>
 *
 * <p>本模板只接受唯一完整 key。重复 value 使用 {@link ValueIndex}，
 * 数组题以 index 作为唯一 id。</p>
 */
public class SBTSetInterview<K extends Comparable<K>> {

    private static class Node<K> {
        K key;
        Node<K> left;
        Node<K> right;
        int size = 1;

        Node(K key) {
            this.key = key;
        }
    }

    private Node<K> root;

    private int size(Node<K> cur) {
        return cur == null ? 0 : cur.size;
    }

    // 孩子结构发生变化后，重新计算当前节点：左子树 + 右子树 + 自己。
    private void pull(Node<K> cur) {
        cur.size = size(cur.left) + size(cur.right) + 1;
    }

    /**
     * 左旋固定五步：保存右孩子、接过右孩子的左树、右孩子接管当前节点、
     * pull 旧根、pull 新根。返回值必须是新的子树根 right。
     */
    private Node<K> leftRotate(Node<K> cur) {
        Node<K> right = cur.right;
        cur.right = right.left;
        right.left = cur;
        // 从下往上：先更新下降的旧根，再更新上升的新根。
        pull(cur);
        pull(right);
        return right;
    }

    /** 右旋与左旋完全镜像；返回新的子树根 left。 */
    private Node<K> rightRotate(Node<K> cur) {
        Node<K> left = cur.left;
        cur.left = left.right;
        left.right = cur;
        pull(cur);
        pull(left);
        return left;
    }

    /**
     * SBT 平衡口诀：孙子 size 不能大于叔叔 size。
     *
     * <pre>
     * LL > R：右旋
     * LR > R：左孩子左旋，再右旋
     * RR > L：左旋
     * RL > L：右孩子右旋，再左旋
     * </pre>
     *
     * <p>防错点：</p>
     * <ol>
     *     <li>LL、LR 都与右叔叔比较；RR、RL 都与左叔叔比较。</li>
     *     <li>外侧 LL/RR 单旋，内侧 LR/RL 双旋。</li>
     *     <li>没有违规时必须直接返回，否则最后的递归会无限执行。</li>
     *     <li>发生旋转后，要继续维护左右孩子和当前新根。</li>
     * </ol>
     */
    private Node<K> maintain(Node<K> cur) {
        if (cur == null) {
            return null;
        }

        int leftSize = size(cur.left);
        int rightSize = size(cur.right);
        int ll = cur.left == null ? 0 : size(cur.left.left);
        int lr = cur.left == null ? 0 : size(cur.left.right);
        int rl = cur.right == null ? 0 : size(cur.right.left);
        int rr = cur.right == null ? 0 : size(cur.right.right);

        if (ll > rightSize) {
            cur = rightRotate(cur);
        } else if (lr > rightSize) {
            cur.left = leftRotate(cur.left);
            cur = rightRotate(cur);
        } else if (rr > leftSize) {
            cur = leftRotate(cur);
        } else if (rl > leftSize) {
            cur.right = rightRotate(cur.right);
            cur = leftRotate(cur);
        } else {
            return cur;
        }

        cur.left = maintain(cur.left);
        cur.right = maintain(cur.right);
        return maintain(cur);
    }

    private Node<K> add(Node<K> cur, K key) {
        if (cur == null) {
            return new Node<>(key);
        }
        if (key.compareTo(cur.key) < 0) {
            cur.left = add(cur.left, key);
        } else {
            cur.right = add(cur.right, key);
        }
        pull(cur);
        return maintain(cur);
    }

    public boolean add(K key) {
        checkKey(key);
        if (contains(key)) {
            return false;
        }
        root = add(root, key);
        return true;
    }

    private Node<K> delete(Node<K> cur, K key) {
        int cmp = key.compareTo(cur.key);
        if (cmp < 0) {
            cur.left = delete(cur.left, key);
        } else if (cmp > 0) {
            cur.right = delete(cur.right, key);
        } else {
            if (cur.left == null) {
                return cur.right;
            }
            if (cur.right == null) {
                return cur.left;
            }

            // 双子节点：复制右子树最左节点的 key，再递归删除后继节点。
            Node<K> successor = cur.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            cur.key = successor.key;
            cur.right = delete(cur.right, successor.key);
        }

        // 防错：必须先根据删除后的真实孩子重算 size，再根据新 size 调整平衡。
        pull(cur);
        return maintain(cur);
    }

    public boolean remove(K key) {
        checkKey(key);
        if (!contains(key)) {
            return false;
        }
        root = delete(root, key);
        return true;
    }

    public boolean contains(K key) {
        checkKey(key);
        Node<K> cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                return true;
            }
            cur = cmp < 0 ? cur.left : cur.right;
        }
        return false;
    }

    /**
     * 返回第 index 小的 key，index 从 0 开始。
     *
     * <p>防错：当前节点的 0-based 排名就是 leftSize；进入右树时必须减掉
     * 整个左树和当前节点，即 leftSize + 1。</p>
     */
    public K getIndexKey(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size());
        }

        Node<K> cur = root;
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

    /**
     * 返回严格小于 key 的数量。
     *
     * <p>防错：只有 key > cur.key 时，才能确认“左树 + 当前”全部小于目标并结算；
     * 相等时返回 ans + leftSize，不能漏掉搜索途中已经累计的 ans。</p>
     */
    public int countLess(K key) {
        checkKey(key);
        int ans = 0;
        Node<K> cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                ans += size(cur.left) + 1;
                cur = cur.right;
            } else {
                return ans + size(cur.left);
            }
        }
        return ans;
    }

    /**
     * 返回小于等于 key 的数量。
     * 相比 countLess，只在相等时多计算当前节点，所以返回 ans + leftSize + 1。
     */
    public int countLessOrEqual(K key) {
        checkKey(key);
        int ans = 0;
        Node<K> cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                ans += size(cur.left) + 1;
                cur = cur.right;
            } else {
                return ans + size(cur.left) + 1;
            }
        }
        return ans;
    }

    public int size() {
        return size(root);
    }

    private void checkKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
    }

    /** 重复 value 的统一适配器：先按 value 排序，相同时按唯一 id 排序。 */
    public static final class ValueIndex implements Comparable<ValueIndex> {
        public final int value;
        public final long id;

        public ValueIndex(int value, long id) {
            this.value = value;
            this.id = id;
        }

        @Override
        public int compareTo(ValueIndex other) {
            int cmp = Integer.compare(value, other.value);
            return cmp != 0 ? cmp : Long.compare(id, other.id);
        }
    }
}
