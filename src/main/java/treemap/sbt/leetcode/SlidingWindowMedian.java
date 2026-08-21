package treemap.sbt.leetcode;

public class SlidingWindowMedian {

    public double[] medianSlidingWindow(int[] nums, int k) {
        SBTSet<ValueIndex> set = new SBTSet<>();
        int len  = nums.length;
        // int[] ans = new int[len - k + 1];
        double[] ans = new double[len - k + 1];
        int index = 0;
        // 3 /2;    4 /2-1&&/2
        int L = 0;
        for (int R = 0; R < len; R++) {
            set.add(new ValueIndex(nums[R], R));

            if (R - L + 1 > k) {
                set.delete(new ValueIndex(nums[L], L));
                L++;
            }

            if (R - L + 1 == k) {
                if ((k & 1) == 1) {
                    ans[index++] = set.getIndex(k/2).value;
                } else {
                    // TODO：【错误点】涉及到相加要格外注意 元素取值区间。 要不就转成long/double，要不提前 /2
                    // 错误： ans[index++] = (double) (set.getIndex(k/2 - 1).value + set.getIndex(k/2).value) / 2;
                    ans[index++] =  ((double)set.getIndex(k/2 - 1).value + set.getIndex(k/2).value) / 2;
                    /**
                     TODO：下面是openai给的建议，真不错：
                     long a = set.getIndex(k / 2 - 1).value;
                     long b = set.getIndex(k / 2).value;
                     ans[index++] = (a + b) / 2.0;     // 2.0 太妙了
                     */
                }
            }
        }
        return ans;
    }

    // public static class SBTSet<K> {
    // TODO: 【错误点】必须加extends Comparable<K>，不然内部方法无法使用 .compareTo =》编译报错cannot find symbol
    public static class SBTSet<K extends Comparable<K>> {

        // K root;
        SBTNode<K> root;

        private int size(SBTNode<K> node) {
            return node == null ? 0 : node.size;
        }

        private void pull(SBTNode<K> node) {
            node.size = size(node.l) + size(node.r) + 1;
        }


        private SBTNode<K> leftRotate(SBTNode<K> cur) {
            SBTNode<K> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            pull(cur);
            pull(r);
            return r;
        }
        private SBTNode<K> rightRotate(SBTNode<K> cur) {
            SBTNode<K> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            pull(cur);
            pull(l);
            return l;
        }

        private SBTNode<K> maintain(SBTNode<K> cur) {
            if (cur == null) {
                return null;
            }
            int lSize = size(cur.l);
            int rSize = size(cur.r);
            int llSize = cur.l == null ? 0 : size(cur.l.l);
            int lrSize = cur.l == null ? 0 : size(cur.l.r);
            int rlSize = cur.r == null ? 0 : size(cur.r.l);
            int rrSize = cur.r == null ? 0 : size(cur.r.r);
            if (llSize > rSize) {
                cur = rightRotate(cur);
            } else if (lrSize > rSize) {
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
            } else if (rrSize > lSize) {
                cur = leftRotate(cur);
            } else if (rlSize > lSize) {
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
            } else {
                return cur;
            }
            /**
             TODO：【超级大错误！！！】maintain(cur.l) 内部可能发生旋转。旋转后会返回新的子树根，如果不接住返回值，父节点仍然指向旋转前的旧根，严重时会导致节点脱离树。
             */
            //  错误：maintain(cur.l);
            //  错误：maintain(cur.r);
            cur.l = maintain(cur.l);
            cur.r = maintain(cur.r);
            return maintain(cur);
        }
        private SBTNode<K> add(SBTNode<K> cur, K k) {
            if (cur == null) {
                return new SBTNode<K>(k);
            }
            cur.size++;
            if (k.compareTo(cur.k) > 0) {
                // TODO: 【错误点】一定注意，去cur.l加，那么一定要再用cur.l接住返回的头！！！ 不是用cur!!!
                // cur = add(cur.r, k);
                cur.r = add(cur.r, k);
            } else {
                // TODO: 【错误如上】cur = add(cur.l, k);
                cur.l = add(cur.l, k);
            }
            return maintain(cur);
        }

        public SBTNode<K> find(K k) {
            SBTNode<K> cur = root;
            while (cur != null) {
                if (k.compareTo(cur.k) == 0) {
                    return cur;
                } else if (k.compareTo(cur.k) > 0) {
                    // TODO: 【错误点】SBTNode 使用l,r做指针， 后面一定要统一属性名
                    // cur = cur.right;
                    cur = cur.r;
                } else {
                    // cur = cur.left;
                    cur = cur.l;
                }
            }
            return null;
        }

        public boolean contains(K k) {
            SBTNode<K> node = find(k);
            return node != null;
        }

        public void add(K k) {
            if (contains(k)) {
                return;
            } else {
                root = add(root, k);
            }
        }

        public SBTNode<K> delete(SBTNode<K> cur, K k) {
            cur.size--;
            if (k.compareTo(cur.k) < 0) {
                cur.l = delete(cur.l, k);
            } else if (k.compareTo(cur.k) > 0) {
                cur.r = delete(cur.r, k);
            } else {
                if (cur.l == null && cur.r == null) {
                    cur = null;
                } else if (cur.l != null && cur.r == null) {
                    cur = cur.l;
                } else if (cur.l == null && cur.r != null) {
                    cur = cur.r;
                } else {
                    SBTNode<K> des = cur.r;
                    while (des.l != null) {
                        des = des.l;
                    }
                    cur.r = delete(cur.r, des.k);
                    des.l = cur.l;
                    des.r = cur.r;
                    pull(des);
                    cur = des;
                }
            }
            return maintain(cur);
        }

        public void delete(K k) {
            if (contains(k)) {
                root = delete(root, k);
            }
        }

        // 0-based
        public K getIndex(int index) {
            SBTNode<K> cur = root;
            while (cur != null) {
                int lSize = size(cur.l);
                if (lSize == index) {
                    return cur.k;
                } else if (index < lSize) {
                    cur = cur.l;
                } else {
                    index -= size(cur.l) + 1;
                    cur = cur.r;
                }
            }
            return null;
        }

    }

    // public static class SBTNode<K> {
    // TODO: 【与SBTSet一样的错误】必须加extends Comparable<K>，不然内部方法无法使用 .compareTo =》 编译报错cannot find symbol

    public static class SBTNode<K extends Comparable<K>> {
        K k;
        SBTNode l;
        SBTNode r;
        int size;

        public SBTNode(K _k) {
            k = _k;
            // TODO: 【错误点-遗漏点】每个属性要【逐个】检查，是否赋值。 下面的size漏了！！
            size = 1;

        }
    }

    // TODO: 【错误点】这里必须写Comparable<K>，不能省略里面的K: class ValueIndex implements Comparable<ValueIndex> ！！！！  <K>相当于类型检查,影响compareTo(K other)
    public static class ValueIndex implements Comparable<ValueIndex> {
        int value;
        int index;

        public ValueIndex(int _value, int _index) {
            value = _value;
            index = _index;
        }

        public int compareTo(ValueIndex other) {

            // TODO: 【超级错误】注意下面两个错误：1. value/index 都是int 不是对象！，只能计算-或者Integer.compare
            // int cmp = value.compareTo(other.value);
            // return cmp != 0 ? cmp : index.compareTo(other.index);
            int cmp = Integer.compare(value, other.value);
            return cmp != 0 ? cmp : Integer.compare(index, other.index);
        }
    }



}
