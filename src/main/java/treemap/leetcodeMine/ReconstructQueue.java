package treemap.leetcodeMine;


import java.util.*;

/**
 * 1. 本题核心是 贪心算法： （1）先排序：身高逆序，k升序。 （2）对每个int[] p 根据 p[1] 依次插入list即可
 * 本质是，假设当前插入k位置的元素是i，保证i元素插入后，不会再有 >= i高度的 元素插进去。
 * 2. 所以，基于贪心排序，有下列两种解法：
 * （1）直接使用List.add(index, V)方法， 每次调用时间复杂度为O(N)，所以该算法最终时间复杂度为O(N^2)
 * （2）使用SBT优化（直接复用代码 AddRemoveGetIndexGreat），实现add(index, V) O(logN)，进而最终时间复杂度为 O(N * logN)
 */
public class ReconstructQueue {
    // 核心是贪心算法：保证每一个插入之后，后面没有>=他的人，插在他的前面。
//      所以，身高降序，k 升序
    public int[][] reconstructQueue(int[][] people) {
        // TODO: 【错误！！！】写反了 应该是：身高降序，k 升序
        //      Arrays.sort(people, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);

        Arrays.sort(people, (a, b) -> a[0] != b[0] ? b[0] - a[0] : a[1] - b[1]);  // 因为都是正数，所以不存在v-v 负数溢出问题// 因为都是正数，所以不存在v-v 负数溢出问题

        SBTList<int[]> list = new SBTList<>();
        for (int[] p : people) {
            // System.out.println(Arrays.toString(p));
            list.add(p[1], p);
        }

        int[][] ans = new int[list.size()][];
        int index = 0;
        // TODO: 【写错了】for (int p : list.inOrder()) {
        for (int[] p : list.inOrder()) {
            ans[index++] = p;
        }
        return ans;

        // 下面这个是针对List<> = ArrayList/LinkedList
        // return ans.toArray(new int[ans.size()][]);
    }

    // 因为是完全基于index做add remove 和 get。所以不需要 Comparable
    public static class SBTNode<V> {
        V v;
        SBTNode<V> l;
        SBTNode<V> r;
        int size;

        public SBTNode(V _v) {
            v = _v;
            size = 1;
        }
    }


    public static class SBTList<V> {

        SBTNode<V> root;

        public SBTList() {
            root = null;
        }

        private SBTNode<V> leftRotate(SBTNode<V> cur) {
            SBTNode<V> right = cur.r;
            cur.r = right.l;
            right.l = cur;
            right.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return right;
        }

        private SBTNode<V> rightRotate(SBTNode<V> cur) {
            SBTNode<V> left = cur.l;
            // cur.r = left.r;  // TODO: 【重大错误】我是错在这里，你敢信吗？？？ 所以一定要全神贯注！
            cur.l = left.r;
            left.r = cur;
            left.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return left;
        }

        private SBTNode<V> maintain(SBTNode<V> cur) {
            if (cur == null) {
                return null;
            }
            int ls = cur.l != null ? cur.l.size : 0;
            int lls = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
            int lrs = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            int rs = cur.r != null ? cur.r.size : 0;
            int rls = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
            int rrs = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
            if (lls > rs) {
                cur = rightRotate(cur);
            } else if (lrs > rs) {
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
            } else if (rrs > ls) {
                cur = leftRotate(cur);
            } else if (rls > ls) {
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
            } else {
                return cur;
            }
            cur.l = maintain(cur.l);
            cur.r = maintain(cur.r);
            cur = maintain(cur);
            return cur;
        }

        private SBTNode<V> add(SBTNode<V> cur, int kth, V v) {
            if (cur == null) {
                return new SBTNode<>(v);
            }
            // TODO: 【错误】千万不要忘了 cur.size++!!!!
            cur.size++;
            int leftAndMiddle = (cur.l != null ? cur.l.size : 0) + 1;
            if (kth <= leftAndMiddle) {
                // System.out.println(cur.l);
                cur.l = add(cur.l, kth, v);
            } else {
                cur.r = add(cur.r, kth - leftAndMiddle, v);
            }

            return maintain(cur);
        }

        public void add(int index, V v) {
            // TODO： 【特别注意】这一步转换必不可少
            int kth = index + 1;
            root = add(root, kth, v);
        }

        private SBTNode<V> delete(SBTNode<V> cur, int kth) {
            // TODO： 第一步，别忘了 size--
            if (cur == null) {
                return null;
            }
            cur.size--;
            int leftAndMiddle = (cur.l != null ? cur.l.size : 0) + 1;
            if (kth < leftAndMiddle) {
                cur.l = delete(cur.l, kth);
            } else if (kth > leftAndMiddle) {
                cur.r = delete(cur.r, kth - leftAndMiddle);
            } else {  // kth == leftAndMiddle
                if (cur.l == null && cur.r == null) {
                    cur = null;
                } else if (cur.l != null && cur.r == null) {
                    cur = cur.l;
                } else if (cur.l == null && cur.r != null) {
                    cur = cur.r;
                } else {
                    SBTNode<V> des = cur.r;
                    while (des.l != null) {
                        des = des.l;
                    }
                    cur.r = delete(cur.r, 1);  // 这个地方是有点区别的， 因为我们比较的是kth，所以这里完全不传 des.v什么的了，就传1 （代表右子树的第一个节点）
                    des.l = cur.l;
                    des.r = cur.r;
                    cur = des;
                }
            }
            if (cur != null) {
                cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            }
            return cur;
        }

        public void remove(int index) {
            // TODO： 【特别注意】这一步转换必不可少
            int kth = index + 1;
            root = delete(root, kth);
        }

        private SBTNode<V> get(SBTNode<V> cur, int kth) {
            int leftAndMiddle = (cur.l != null ? cur.l.size : 0) + 1;
            if (kth == leftAndMiddle) {
                return cur;
            } else if (kth < leftAndMiddle) {
                return get(cur.l, kth);
            } else {
                return get(cur.r, kth - leftAndMiddle);
            }
        }

        public V get(int index) {
            int kth = index + 1;
            return get(root, kth).v;
        }

        public int size() {
            return root == null ? 0 : root.size;
        }

        // TODO： 本题需增加下列方法，返回顺序遍历的结果
        private void in(SBTNode<V> cur, List<V> list) {
            if (cur == null) {
                // TODO: 【错误】 使用中序遍历，使用collection收集元素值， 就没有返回值了！！！
                //      return null;
                return;
            }
            in(cur.l, list);
            list.add(cur.v);
            in(cur.r, list);
        }

        public List<V> inOrder() {
            List<V> ans = new ArrayList<>();
            in(root, ans);
            return ans;
        }


    }
}

