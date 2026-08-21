package treemap.sbt.leetcode;

/**
 * 注意点：
 * 1. 我们采用的是：手动统计从下标 0 开始的子数组 + SBT 统计从下标 1...i 开始的子数组(preSum相减)  => 因此我们的preSum实现就是 long[] preSum = new long[arr.length]; pre[i] = pre[i-1] + arr[i];
 * TODO 超级错误点:
 *  1. ！！错误：预先放入所有前缀和，再统一查询！！
 *     正确：【每轮查询历史前缀和；再放当前前缀和】
 *  2. 注意主函数的两个for循环，不能合并！！！ 第一个构造preSum的for循环是i=1开头，但是我们的计算需要时i=0开始。
 */
public class CountOfRangeSum {
    // lower <= r - l <= upper , r - upper <= l <= r - lower
    public static int countOfRangeSum(int[] arr, int lower, int upper) {
        int length = arr.length;
        long[] preSum = new long[length];
        preSum[0] = arr[0];
        int ans = 0;
        SBT<ValueIndex> sbt = new SBT<>();
        for (int i = 1; i < preSum.length; i++) {
            preSum[i] = preSum[i - 1] + arr[i];
            // TODO: 【错误点】不要直接在这里计算下面的循环体！！！ 这里的循环体少了i=0，他是从 i=1开始的！！！
        }

        // TODO: 【原始错误点】不要直接在上面的 presum 构造段落里，计算ans ==> 上面的构造是从i=1开始的
        for (int i = 0; i < preSum.length; i++) {
            // TODO: 【遗漏-需要单独计算】千万别漏了这一部分。 0,1...i 从0开头的子数组需要单独计算 （因为我们不采用在preSum插入0开头，那么每次都要单独计算preSum[i]，代表0,1,2...i的子数组）
            if (preSum[i] >= lower && preSum[i] <= upper) {
                ans++;
            }
            // 下面是 1...i的子数组
            long u = preSum[i] - lower;    // TODO: 【错误点】特别注意，preSum是long数组，所以 基于它的计算结果变量都是 long类型
            long l = preSum[i] - upper;
            ans += sbt.countLessOrEqual(new ValueIndex(u, Integer.MAX_VALUE)) - sbt.countLess(new ValueIndex(l, Integer.MIN_VALUE));

            // TODO: 这块需要计算ans后才 AddToSBT
            sbt.add(new ValueIndex(preSum[i], i));
        }


        /**
         * TODO：【错误点】
         *   SBT 的 add、pull、maintain、countLess、countLessOrEqual 实现本身正确；错误集中在 SBT 的使用流程：
         *   ！！错误：预先放入所有前缀和，再统一查询！！
         *   正确：【每轮查询历史前缀和；再放当前前缀和】
         */
//        for (int i = 0; i < preSum.length; i++) {
//            if (preSum[i] >= lower && preSum[i] <= upper) {
//                ans++;
//            }
//            long u = preSum[i] - lower;
//            long l = preSum[i] - upper;
//            ans += sbt.countLessOrEqual(new ValueIndex(u, Integer.MAX_VALUE)) - sbt.countLess(new ValueIndex(l, Integer.MIN_VALUE));
//        }

        return ans;
    }

    public static class Node<K extends Comparable<K>> {
        K key;
        Node<K> l;
        Node<K> r;
        int size = 1;

        public Node(K _key) {
            key = _key;
        }
    }

    public static class SBT<K extends Comparable<K>> {

        Node<K> root;

        public SBT() {
            root = null;
        }


        private int size(Node<K> cur) {
            return cur == null ? 0 : cur.size;
        }

        private void pull(Node<K> cur) {
            if (cur != null) {
                cur.size = size(cur.l) + size(cur.r) + 1;
            }
        }

        private Node<K> leftRotate(Node<K> cur) {
            Node<K> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            pull(cur);
            pull(r);
            return r;
        }

        private Node<K> rightRotate(Node<K> cur) {
            Node<K> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            pull(cur);
            pull(l);
            return l;
        }

        private Node<K> maintain(Node<K> cur) {
            if (cur == null) {
                return null;
            }
            int l = size(cur.l);
            int r = size(cur.r);
            int ll = cur.l == null ? 0 : size(cur.l.l);
            int lr = cur.l == null ? 0 : size(cur.l.r);
            int rl = cur.r == null ? 0 : size(cur.r.l);
            int rr = cur.r == null ? 0 : size(cur.r.r);
            if (ll > r) {
                cur = rightRotate(cur);
            } else if (lr > r) {
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
            } else if (rr > l) {
                cur = leftRotate(cur);
            } else if (rl > l) {
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
            } else {
                return cur;
            }
            cur.l = maintain(cur.l);
            cur.r = maintain(cur.r);
            return maintain(cur);
        }

        private Node<K> add(Node<K> cur, K key) {
            if (cur == null) {
                return new Node<K>(key);
            }
            if (key.compareTo(cur.key) < 0) {
                cur.l = add(cur.l, key);
            } else {
                cur.r = add(cur.r, key);
            }
            pull(cur);
            return maintain(cur);
        }

        public void add(K key) {
            // TODO: 标准版本这里应该通过 find返回Node，看是 更新 还是 新增
            //  但是本题，是 ValueIndex，一定不重复，所以省略了
            root = add(root, key);
        }

        public int countLess(K key) {
            if (root == null) {
                return 0;
            }
            int ans = 0;
            Node<K> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.key) > 0) {
                    ans += size(cur.l) + 1;
                    cur = cur.r;
                } else {
                    cur = cur.l;
                }
            }
            return ans;
        }

        public int countLessOrEqual(K key) {
            if (root == null) {
                return 0;
            }
            int ans = 0;
            Node<K> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.key) >= 0) {
                    ans += size(cur.l) + 1;
                    cur = cur.r;
                } else {
                    cur = cur.l;
                }
            }
            return ans;
        }
    }

    public static class ValueIndex implements Comparable<ValueIndex> {
        long value;
        int index;

        public ValueIndex(long _value, int _index) {
            value = _value;
            index = _index;
        }

        public int compareTo(ValueIndex other) {
            return value == other.value ? Integer.compare(index, other.index) : Long.compare(value, other.value);
        }
    }


}
