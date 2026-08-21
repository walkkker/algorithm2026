package treemap.test20260713;

public class SBT {

    public static class SBTNode<K extends Comparable<K>, V> {
        K k;
        V v;
        SBTNode<K, V> l;
        SBTNode<K, V> r;
        int size;

        public SBTNode(K _k, V _v) {
            k = _k;
            v = _v;
            size = 1;
        }
    }

    public static class SBTreeMap<K extends Comparable<K>, V> {

        SBTNode<K, V> root;

        public SBTreeMap() {
            root = null;
        }

        private SBTNode<K, V> leftRotate(SBTNode<K, V> cur) {
            SBTNode<K, V> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            r.size = cur.size;
            cur.size = (cur.l == null ? 0 : cur.l.size) + (cur.r == null ? 0 : cur.r.size) + 1;
            return r;
        }

        private SBTNode<K, V> rightRotate(SBTNode<K, V> cur) {
            SBTNode<K, V> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            l.size = cur.size;
            cur.size = (cur.l == null ? 0 : cur.l.size) + (cur.r == null ? 0 : cur.r.size) + 1;
            return l;
        }

        private SBTNode<K, V> maintain(SBTNode<K, V> cur) {
            if (cur == null) {
                return null;
            }
            int lSize = cur.l == null ? 0 : cur.l.size;
            int rSize = cur.r == null ? 0 : cur.r.size;
            int llSize = cur.l == null ? 0 : (cur.l.l == null ? 0 : cur.l.l.size);
            int lrSize = cur.l == null ? 0 : (cur.l.r == null ? 0 : cur.l.r.size);
            int rlSize = cur.r == null ? 0 : (cur.r.l == null ? 0 : cur.r.l.size);
            int rrSize = cur.r == null ? 0 : (cur.r.r == null ? 0 : cur.r.r.size);
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
            } else {  // TODO: 在我们的写法中，如果你在最后加了三连maintain，你必须有这个else分支，不然会陷入 无限迭代（因为当cur没有问题时，它也会不断调用cur=maintain(cur)）
                return cur;
            }
            cur.l = maintain(cur.l);
            cur.r = maintain(cur.r);
            cur = maintain(cur);
            return cur;
        }

        private SBTNode<K, V> add(SBTNode<K, V> cur, K k, V v) {
            if (cur == null) {
                return new SBTNode<>(k, v);
            }
            cur.size++;   // TODO： 【注意】千万不能忘了
            if (k.compareTo(cur.k) < 0) {
                cur.l = add(cur.l, k, v);
            } else {
                cur.r = add(cur.r, k, v);
            }
            return maintain(cur);
        }

        private SBTNode<K, V> delete(SBTNode<K, V> cur, K k) {
            cur.size--;   // TODO: 【注意】千万不能忘了
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
                    SBTNode<K, V> des = cur.r;
                    while (des.l != null) {
                        des = des.l;
                    }
                    cur.r = delete(cur.r, des.k);
                    des.l = cur.l;
                    des.r = cur.r;
                    cur = des;
                }
            }
            // TODO: 【删除后 size 维护错误】
            //  （1）当初错在哪里：delete 只在递归入口执行 cur.size--，但双子节点删除时，后继节点 des 被从右子树删除后又复用为当前子树的新根，返回前没有重新计算新 cur 的 size。 （具体的代码就是 没有写下面这段代码）
            //  （2）错误原因：des 在 delete 过程中 size 已经被减过；重新挂接左右子树后仍保留旧 size，会导致 size 变成 0 或负数，进而使 maintain 根据错误的规模信息误判并执行非法旋转。 （因为是非法旋转，最终报错为 rightRotate 中报错cur.l=cur.l.r 报错NPE）
            //  （3）如何修正：delete 返回前，只要 cur 不为空，就根据左右子树的实际 size 统一重算 cur.size，保证递归回溯过程中每个节点的 size 都与当前树结构一致。
            //  实现补充：（1）下面两行与AVL一样：【检查cur!=null && 调整 cur.平衡因子】千万别忘了，不然又是NPE
            if (cur != null) {
                cur.size = (cur.l == null ? 0 : cur.l.size) + (cur.r == null ? 0 : cur.r.size) + 1;
            }
            return cur;
        }
        
        /**  下面这段是直接copy AVL 共享代码 =》 属于BST的部分   **/

        private SBTNode<K, V> findLastIndex(K k) {
            if (root == null) {
                return null;
            }
            SBTNode<K, V> cur = root;
            SBTNode<K, V> des = null;
            while (cur != null) {
                if (k.compareTo(cur.k) < 0) {
                    cur = cur.l;
                } else if (k.compareTo(cur.k) > 0) {
                    des = cur;
                    cur = cur.r;
                } else {
                    des = cur;
                    break;
                }
            }
            return des;
        }

        private SBTNode<K, V> findLastNoSmallIndex(K k) {
            SBTNode<K, V> cur = root;
            SBTNode<K, V> des = null;
            while (cur != null) {
                if (k.compareTo(cur.k) < 0) {
                    des = cur;
                    cur = cur.l;
                } else if (k.compareTo(cur.k) > 0) {
                    cur = cur.r;
                } else {
                    des = cur;
                    break;
                }
            }
            return des;
        }

        public boolean containsKey(K k) {
            SBTNode<K, V> node = findLastIndex(k);
            return node != null && k.compareTo(node.k) == 0;
        }


        public void put(K k, V v) {
            SBTNode<K, V> des = findLastIndex(k);
            if (des != null && k.compareTo(des.k) == 0) {
                des.v = v;
            } else {
                root = add(root, k, v);
            }
        }

        public void remove(K k) {
            // TODO: 【错误点】class AVLTreeMap 的 size变量 全部/只在 put和remove方法中执行
            if (containsKey(k)) {
                root = delete(root, k);
            }
        }

        public K floorKey(K k) {
            SBTNode<K, V> des = findLastIndex(k);
            return des == null ? null : des.k;
        }

        public K ceilingKey(K k) {
            SBTNode<K, V> des = findLastNoSmallIndex(k);
            return des == null ? null : des.k;
        }

        public V get(K k) {
            SBTNode<K, V> des = findLastIndex(k);
            if (des != null && k.compareTo(des.k) == 0) {
                return des.v;
            } else {
                return null;
            }
        }

        public K firstKey() {
            if (root == null) {
                return null;
            }
            SBTNode<K, V> pre = null;
            SBTNode<K, V> cur = root;
            while (cur != null) {
                pre = cur;
                cur = cur.l;
            }
            return pre.k;
        }

        public K lastKey() {
            if (root == null) {
                return null;
            }
            SBTNode<K, V> pre = null;
            SBTNode<K, V> cur = root;
            while (cur != null) {
                pre = cur;
                cur = cur.r;
            }
            return pre.k;
        }

        public int size() {
            return root == null ? 0 : root.size;
        }
        

    }

}



