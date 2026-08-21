package treemap.sbt.DynamicArrayForImplicitSBT;

import java.util.*;

/**
 * 核心思想是贪心：先h降序，后k升序。  => 后面只需依次取出元素，执行 数组的add(k, int[] p)即可
 */
public class QueueReconstructionByHeight {
    // 下列版本是 ArrayList版本
    // h,k
    public static int[][] reconstructQueue(int[][] people) {
        // TODO: 【最重点】其实这道题的最重点，是 1. 使用 Arrays.sort() 对 int[][] 排序
        //          2. Arrays.sort() 里面的lambda表达式写法 -> 开头一定是 (a, b) -> () -> a,b代指所有，此题代指 int[2]
        // 错误行： Arrays.sort(people, ([a1, b1], [a2, b2]) -> (a1 == a2 ? b1 - b2 : a2 - a1));
        Arrays.sort(people, (a, b) -> (a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]));
        // TODO：【错误点】千万注意这里的 泛型是int[]。 你实际存储的元素是int[]
        //           ArrayList<Integer> list = new ArrayList<>();
        ArrayList<int[]> list = new ArrayList<>();
        for (int[] p : people) {
            list.add(p[1], p);
        }
        int[][] ans = new int[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            ans[i] = list.get(i);
        }
        return ans;
    }

    // TODO: 下列版本为 隐式SBT实现动态数组，其实主代码基本一致
    public int[][] reconstructQueue2(int[][] people) {
        // Arrays.sort(people, ([a1, b1], [a2, b2]) -> (a1 == a2 ? b1 - b2 : a2 - a1));
        Arrays.sort(people, (a, b) -> (a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]));

        SBT<int[]> sbt = new SBT<>();
        for (int[] p : people) {
            sbt.add(p[1], p);
        }
        int size = people.length;
        int[][] ans = new int[size][2];
        for (int i = 0; i < size; i++) {
            ans[i] = sbt.get(i);
        }
        return ans;
    }


    public static class Node<V> {
        V value;
        Node<V> l;
        Node<V> r;
        int size = 1;

        public Node(V _value) {
            value = _value;
        }
    }

    public static class SBT<V> {
        Node<V> root;

        public SBT() {
            root = null;
        }

        private int size(Node<V> cur) {
            return cur == null ? 0 : cur.size;
        }

        private void pull(Node<V> cur) {
            if (cur != null) {
                cur.size = size(cur.l) + size(cur.r) + 1;
            }
        }

        private Node<V> leftRotate(Node<V> cur) {
            Node<V> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            pull(cur);
            pull(r);
            return r;
        }

        private Node<V> rightRotate(Node<V> cur) {
            Node<V> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            pull(cur);
            pull(l);
            return l;
        }

        private Node<V> maintain(Node<V> cur) {
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
            // TODO: 【超级错误1！！！】【错了不只一次了！！！】【maintain孩子会换头呀】
            //   maintain(cur.l);
            //   maintain(cur.r);
            cur.l = maintain(cur.l);
            cur.r = maintain(cur.r);
            return maintain(cur);
        }

        private Node<V> add(Node<V> cur, int index, V value) {
            // TODO: 【错误-遗漏-递归BaseCase】
            if (cur == null) {
                return new Node<>(value);
            }

            int lSize = size(cur.l);
            if (index <= lSize) {
                cur.l = add(cur.l, index, value);
            } else {
                index -= lSize + 1;
                cur.r = add(cur.r, index, value);
            }
            pull(cur);
            return maintain(cur);
        }

        public void add(int index, V value) {
            // TODO: 【范围检查错误 - 尾插】 index范围是[0,size(root)] ，不是[0, size[root]-1]。 因为可以插入最后一个位置
//            if (index < 0 || index >= size(root)) {
//                throw new RuntimeException("add index is out of bounds");
//            }
            root = add(root, index, value);
        }

        private Node<V> delete(Node<V> cur, int index) {
            if (index < 0 || index >= size(root)) {
                throw new RuntimeException("add index is out of bounds");
            }
            if (root == null) {
                return null;
            }
            int lSize = size(cur.l);
            if (index < lSize) {
                cur.l = delete(cur.l, index);
            } else if (index > lSize) {
                // TODO: 【超级错误！！！】【一定要牢记！！！】
                //  【delete 进入右子树时没有转换成右子树局部下标。】/
                //   cur.r = delete(cur.r, index);
                index = index - lSize - 1;
                cur.r = delete(cur.r, index);
            } else {
                if (cur.l == null && cur.r == null) {
                    cur = null;
                } else if (cur.l == null) {
                    cur = cur.r;
                } else if (cur.r == null) {
                    cur = cur.l;
                } else {
                    Node<V> des = cur.r;
                    while (des.l != null) {
                        des = des.l;
                    }
                    cur.r = delete(cur.r, 0);
                    des.l = cur.l;
                    des.r = cur.r;
                    cur = des;
                }
            }
            pull(cur);
            return maintain(cur);
        }

        public void delete(int index) {
            root = delete(root, index);
        }

        private Node<V> getIndex(Node<V> cur, int index) {
            if (cur == null) {
                return null;
            }
            int lSize = size(cur.l);
            if (index == lSize) {
                return cur;
            } else if (index < lSize) {
                return getIndex(cur.l, index);
            } else {
                index -= lSize + 1;
                return getIndex(cur.r, index);
            }
        }

        public V get(int index) {
            return getIndex(root, index).value;
        }

        public int size() {
            return size(root);
        }


    }
}

