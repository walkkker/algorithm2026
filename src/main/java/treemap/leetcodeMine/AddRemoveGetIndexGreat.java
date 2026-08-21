package treemap.leetcodeMine;

import treemap.leetcodezuo.Code03_AddRemoveGetIndexGreat;

import java.util.ArrayList;

// TODO: 【不可想象的重大错误】必须全神贯注的写！（往下看有标注）  这个情况导致的问题是 无限递归stackOverflow。 =》 所以出现add get无限递归，说明永远到不了base case，那么就说明有环！！！！ 去旋转的代码找问题！！！！
public class AddRemoveGetIndexGreat {

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
            // TODO: 【不可想象的重大错误】必须全神贯注的写！  这个情况导致的问题是 无限递归stackOverflow。 =》 所以出现add get无限递归，说明永远到不了base case，那么就说明有环！！！！ 去旋转的代码找问题！！！！
            // cur.r = left.r;
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

    }


    // 通过以下这个测试，
    // 可以很明显的看到LinkedList的插入、删除、get效率不如SbtList
    // LinkedList需要找到index所在的位置之后才能插入或者读取，时间复杂度O(N)
    // SbtList是平衡搜索二叉树，所以插入或者读取时间复杂度都是O(logN)
    public static void main(String[] args) {
        // 功能测试
        int test = 50000;
        int max = 1000000;
        boolean pass = true;
        ArrayList<Integer> list = new ArrayList<>();
        SBTList<Integer> sbtList = new SBTList<>();
        for (int i = 0; i < test; i++) {
            if (list.size() != sbtList.size()) {
                pass = false;
                break;
            }
            if (list.size() > 1 && Math.random() < 0.5) {
                int removeIndex = (int) (Math.random() * list.size());
                list.remove(removeIndex);
                sbtList.remove(removeIndex);
            } else {
                int randomIndex = (int) (Math.random() * (list.size() + 1));
                int randomValue = (int) (Math.random() * (max + 1));
                list.add(randomIndex, randomValue);
                sbtList.add(randomIndex, randomValue);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(sbtList.get(i))) {
                pass = false;
                break;
            }
        }
        System.out.println("功能测试是否通过 : " + pass);

        // 性能测试
        test = 500000;
        list = new ArrayList<>();
        sbtList = new SBTList<>();
        long start = 0;
        long end = 0;

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (list.size() + 1));
            int randomValue = (int) (Math.random() * (max + 1));
            list.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList插入总时长(毫秒) ： " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            list.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList读取总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * list.size());
            list.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList删除总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (sbtList.size() + 1));
            int randomValue = (int) (Math.random() * (max + 1));
            sbtList.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("SbtList插入总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            sbtList.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SbtList读取总时长(毫秒) :  " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * sbtList.size());
            sbtList.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SbtList删除总时长(毫秒) :  " + (end - start));

    }

}
