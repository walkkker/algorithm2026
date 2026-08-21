package treemap.sbt.SortedSBTWithRepeatedValue;

/**
 * 保留 K -> V 映射能力的 SBT Map 完整模板。
 *
 * <p>本类以 treemap.test20260713.SBT.SBTreeMap 为基线。旋转和平衡判定保持一致，
 * 新增或改变的部分均使用“TODO: 【相对标准SBT的改写】”标记。</p>
 */
public class SBTreeMap<K extends Comparable<K>, V> {

    private static class SBTNode<K, V> {
        private K key;
        private V value;
        private SBTNode<K, V> left;
        private SBTNode<K, V> right;
        private int size;

        private SBTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.size = 1;
        }
    }

    private SBTNode<K, V> root;

    private int size(SBTNode<K, V> node) {
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
     * <p>它等价于原模板中反复手写的 size 重算语句。旋转、插入、删除改变树形后，
     * 调用 pull 可以根据真实的左右孩子重新计算 size，而不依赖 size++ 或 size-- 的推断。</p>
     *
     * <p>pull 负责修正节点字段；maintain 负责判断规模失衡并旋转，两者职责不同。</p>
     */
    private void pull(SBTNode<K, V> node) {
        node.size = size(node.left) + size(node.right) + 1;
    }

    private SBTNode<K, V> leftRotate(SBTNode<K, V> cur) {
        SBTNode<K, V> right = cur.right;
        cur.right = right.left;
        right.left = cur;

        /*
         * TODO: 【相对标准SBT的改写1：统一使用 pull】
         * 原模板通过 right.size = cur.size 复用旧值；本模板依次重算旧根和新根。
         * 两者结果相同，但 pull 让删除后的字段维护路径保持统一。
         */
        // 旋转后必须从下往上更新：新根 right.size 依赖下降后的旧根 cur.size。
        pull(cur);
        pull(right);
        return right;
    }

    private SBTNode<K, V> rightRotate(SBTNode<K, V> cur) {
        SBTNode<K, V> left = cur.left;
        cur.left = left.right;
        left.right = cur;
        // 旋转后必须从下往上更新：新根 left.size 依赖下降后的旧根 cur.size。
        pull(cur);
        pull(left);
        return left;
    }

    private SBTNode<K, V> maintain(SBTNode<K, V> cur) {
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
            return cur;
        }

        cur.left = maintain(cur.left);
        cur.right = maintain(cur.right);
        return maintain(cur);
    }

    private SBTNode<K, V> add(SBTNode<K, V> cur, K key, V value) {
        if (cur == null) {
            return new SBTNode<>(key, value);
        }
        if (key.compareTo(cur.key) < 0) {
            cur.left = add(cur.left, key, value);
        } else {
            cur.right = add(cur.right, key, value);
        }
        pull(cur);
        return maintain(cur);
    }

    /** 相同 key 更新 value，不增加节点数量。 */
    public void put(K key, V value) {
        requireKey(key);
        SBTNode<K, V> node = findNode(key);
        if (node != null) {
            node.value = value;
        } else {
            root = add(root, key, value);
        }
    }

    private SBTNode<K, V> delete(SBTNode<K, V> cur, K key) {
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

            SBTNode<K, V> successor = cur.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            cur.right = delete(cur.right, successor.key);
            successor.left = cur.left;
            successor.right = cur.right;
            cur = successor;
        }

        /*
         * TODO: 【相对标准SBT的改写2：严格维护删除后的平衡】
         * （1）原模板在递归入口 size--，回溯时重算 size，但没有调用 maintain。
         * （2）本模板根据删除后的真实孩子 pull，再调用 maintain，保证持续增删下的规模平衡。
         * （3）迁移时可以删除入口处 cur.size--，统一改为回溯阶段 pull。
         */
        pull(cur);
        return maintain(cur);
    }

    /**
     * TODO: 【相对标准SBT的改写3：remove 返回删除结果】
     * 原模板 remove 为 void；返回 boolean 便于对数器和调用方确认 key 是否存在。
     */
    public boolean remove(K key) {
        requireKey(key);
        if (!containsKey(key)) {
            return false;
        }
        root = delete(root, key);
        return true;
    }

    private SBTNode<K, V> findNode(K key) {
        SBTNode<K, V> cur = root;
        while (cur != null) {
            int compare = key.compareTo(cur.key);
            if (compare == 0) {
                return cur;
            }
            cur = compare < 0 ? cur.left : cur.right;
        }
        return null;
    }

    public boolean containsKey(K key) {
        requireKey(key);
        return findNode(key) != null;
    }

    public V get(K key) {
        requireKey(key);
        SBTNode<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    /**
     * TODO: 【相对标准SBT的改写4：增加第 k 小接口】
     * index 为 0-based；节点排名等于左子树 size。
     */
    public K getIndexKey(int index) {
        return getIndexNode(index).key;
    }

    public V getIndexValue(int index) {
        return getIndexNode(index).value;
    }

    private SBTNode<K, V> getIndexNode(int index) {
        checkIndex(index);
        SBTNode<K, V> cur = root;
        while (cur != null) {
            int leftSize = size(cur.left);
            if (index < leftSize) {
                cur = cur.left;
            } else if (index == leftSize) {
                return cur;
            } else {
                index -= leftSize + 1;
                cur = cur.right;
            }
        }
        throw new IllegalStateException("unreachable index search state");
    }

    /**
     * TODO: 【相对标准SBT的改写5：增加排名接口】
     * 返回严格小于 key 的节点数量。
     */
    public int countLessKey(K key) {
        requireKey(key);
        int ans = 0;
        SBTNode<K, V> cur = root;
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

    public int countLessOrEqualKey(K key) {
        requireKey(key);
        int ans = 0;
        SBTNode<K, V> cur = root;
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

    public K floorKey(K key) {
        requireKey(key);
        SBTNode<K, V> cur = root;
        SBTNode<K, V> ans = null;
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

    public K ceilingKey(K key) {
        requireKey(key);
        SBTNode<K, V> cur = root;
        SBTNode<K, V> ans = null;
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

    public K firstKey() {
        if (root == null) {
            return null;
        }
        SBTNode<K, V> cur = root;
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur.key;
    }

    public K lastKey() {
        if (root == null) {
            return null;
        }
        SBTNode<K, V> cur = root;
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
}
