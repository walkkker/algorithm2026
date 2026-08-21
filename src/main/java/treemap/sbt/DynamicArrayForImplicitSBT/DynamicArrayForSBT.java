package treemap.sbt.DynamicArrayForImplicitSBT;

public class DynamicArrayForSBT {

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
            cur.r= maintain(cur.r);
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
