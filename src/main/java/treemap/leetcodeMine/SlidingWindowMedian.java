package treemap.leetcodeMine;


import java.util.*;


/**
 * 这道题做了两遍，每次一个多小时，每次都错一堆！！！！！
 *
 * 第一次错误记录，看 package leetcodezuo
 * 本class为第二次错误，一定要看标记的注释！！！！
 *
 *
 * ps补充，下面都是两两搭配的，不要想着只写一个了：
 * 模式都是     递归   +  对外直接调用
 *        private add + public put
 *        private delete + public remove
 *        private getKthNode + K getIndex
 */
public class SlidingWindowMedian {
    /**
     有序表改造：1)允许重复元素插入和区分 2) 求index
     输入是一个数组

     TODO: 【超级错误1】SBT 一定要注意private add() + private delete() 里面一定要size++和size--。  又一次倒在这上面了！！！ 因为SBT支持getKthIndex()，所以size的更新特别重要（不只是平衡用了！！！）

     TODO: 【超级错误2-再次强调】Node的compareTo方法，里面的计算特别容易越界！！！ value - value就是会越界的，会导致大小反转(很难排查)！！！ 所以，要避免使用这样的减法，改成直接比较，手动赋值返回-1/1。 见下方写法
     */
    class Solution {
        public double[] medianSlidingWindow(int[] nums, int k) {
            int n = nums.length;
            double[] ans = new double[n - k + 1];
            int index = 0;
            SizeBalancedTreeMap<Node> sbt = new SizeBalancedTreeMap<>();
            int L = 0;
            for (int R = 0; R < n; R++) {
                sbt.put(new Node(nums[R], R));

                if (R - L + 1 > k) {
                    sbt.remove(new Node(nums[L], L));
                    // TODO: 【错误-严重遗漏！！！】固定滑动窗口，L一定包含两部分！！！ =》 （1） 光处理L元素右移带来的影响。 (2) L本身的指针移动！！！
                    L++;
                }

                if (R - L + 1 == k) {
                    // List<Node> list = new ArrayList<>();
                    // sbt.inOrder(list);
                    // for (Node node : list) {
                    //     System.out.print(node.value + " ");
                    // }
                    // System.out.println();
                    if ((k & 1) == 1) {
                        ans[index++] = (double) sbt.getIndex(k / 2).value;
                    } else {
                        int p1 = sbt.getIndex((k / 2)).value;
                        int p2 = sbt.getIndex((k / 2) - 1).value;
                        System.out.println(p1 + " : " + p2);
                        ans[index++] = ((double) p1 + (double) p2) / 2;
                    }
                }
            }
            return ans;
        }

        public class Node implements Comparable<Node> {
            int value;
            int index;

            public Node(int _v, int _i) {
                value = _v;
                index = _i;
            }

            // TODO: 【特别错误！！】又是错在这里，不要在简单的使用减法了！！！
            public int compareTo(Node o2) {
                // TODO: 拿下面这句来说， value - o2.value (虽然两个value都是int，但是这样计算是可以越界的！！！) 【所以以后统一解决方案：严格比较，然后自己写-1 1最合适。】
                //      return value != o2.value ? value - o2.value : index - o2.index;
                return value != o2.value ? (value < o2.value ? -1 : 1) : (index - o2.index);
            }
        }

        public class SBTNode<K extends Comparable<K>> {
            K k;
            SBTNode<K> l;
            SBTNode<K> r;
            int size;

            public SBTNode(K _k) {
                k = _k;
                size = 1;
            }
        }

        public class SizeBalancedTreeMap<K extends Comparable<K>> {

            SBTNode<K> root;

            public SizeBalancedTreeMap() {
                root = null;
            }

            private SBTNode<K> leftRotate(SBTNode<K> cur) {
                SBTNode<K> right = cur.r;
                cur.r = right.l;
                right.l = cur;
                right.size = cur.size;
                cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
                return right;
            }

            private SBTNode<K> rightRotate(SBTNode<K> cur) {
                SBTNode<K> left = cur.l;
                cur.l = left.r;
                left.r = cur;
                left.size = cur.size;
                cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
                return left;
            }

            private SBTNode<K> maintain(SBTNode<K> cur) {
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

            private SBTNode<K> add(SBTNode<K> cur, K k) {
                if (cur == null) {
                    return new SBTNode<>(k);
                }
                // TODO: 【错误】SBT add方法一定要size++呀
                cur.size++;
                if (cur.k.compareTo(k) < 0) {
                    cur.r = add(cur.r, k);
                } else {
                    cur.l = add(cur.l, k);
                }
                return maintain(cur);
            }

            public void put(K k) {
                root = add(root, k);
            }

            private SBTNode<K> delete(SBTNode<K> cur, K k) {
                if (cur == null) {
                    return null;
                }
                cur.size--;
                if (cur.k.compareTo(k) < 0) {
                    cur.r = delete(cur.r, k);
                } else if (cur.k.compareTo(k) > 0) {
                    cur.l = delete(cur.l, k);
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
                        // TODO: 【错误！！！】
                        //      cur.r = delete(cur.r, des);
                        cur.r = delete(cur.r, des.k);
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

            public void remove(K k) {
                root = delete(root, k);
            }

            private SBTNode<K> getKthNode(SBTNode<K> cur, int kth) {
                int leftAndMiddle = (cur.l != null ? cur.l.size : 0) + 1;
                if (kth == leftAndMiddle) {
                    return cur;
                } else if (kth < leftAndMiddle) {
                    return getKthNode(cur.l, kth);
                } else {
                    return getKthNode(cur.r, kth - leftAndMiddle);
                }
            }

            public K getIndex(int index) {
                int kth = index + 1;
                SBTNode<K> node = getKthNode(root, kth);
                return node.k;
            }

            public void in(SBTNode<K> cur, List<K> list) {
                if (cur == null) {
                    return;
                }
                in(cur.l, list);
                list.add(cur.k);
                in(cur.r, list);
            }

            public void inOrder(List<K> list) {
                in(root, list);
            }
        }

    }
}
