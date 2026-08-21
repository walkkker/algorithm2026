package treemap.test20260713;

/**
 * 测试接口：put remove floorKey ceilingKey firstKey lastKey get
 */
public class AVL {
    /**
     * TODO：【错误点】
     * 1. 本题h的更新不是 l.h + r.h + 1 !!!  => 正确：Math.max(l.h, r.h) + 1
     * 2. class AVLTreeMap 的 size变量 全部/只在 put和remove方法中执行
     *
     * @param <K>
     * @param <V>
     */

    public static class AVLNode<K extends Comparable<K>, V> {
        K k;
        V v;
        AVLNode<K, V> l;
        AVLNode<K, V> r;
        int h;

        public AVLNode(K _k, V _v) {
            k = _k;
            v = _v;
            h = 1;
        }
    }

    public static class AVLTreeMap<K extends Comparable<K>, V> {
        AVLNode<K, V> root;
        int size;

        public AVLTreeMap() {
            root = null;
            size = 0;
        }

        public AVLNode<K, V> leftRotate(AVLNode<K, V> cur) {
            AVLNode<K, V> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            cur.h = Math.max((cur.l == null ? 0 : cur.l.h), (cur.r == null ? 0 : cur.r.h)) + 1;
            r.h = Math.max((r.l == null ? 0 : r.l.h), (r.r == null ? 0 : r.r.h)) + 1;
            return r;
        }

        public AVLNode<K, V> rightRotate(AVLNode<K, V> cur) {
            AVLNode<K, V> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            cur.h = Math.max((cur.l == null ? 0 : cur.l.h), (cur.r == null ? 0 : cur.r.h)) + 1;
            l.h = Math.max((l.l == null ? 0 : l.l.h), (l.r == null ? 0 : l.r.h)) + 1;
            return l;
        }

        public AVLNode<K, V> maintain(AVLNode<K, V> cur) {
            if (cur == null) {
                return null;
            }
            int lH = cur.l == null ? 0 : cur.l.h;
            int rH = cur.r == null ? 0 : cur.r.h;
            if (Math.abs(lH - rH) > 1) {
                if (lH > rH) {
                    int llH = cur.l.l == null ? 0 : cur.l.l.h;
                    int lrH = cur.l.r == null ? 0 : cur.l.r.h;
                    if (llH >= lrH) {
                        cur = rightRotate(cur);
                    } else {
                        cur.l = leftRotate(cur.l);
                        cur = rightRotate(cur);
                    }
                } else {
                    int rlH = cur.r.l == null ? 0 : cur.r.l.h;
                    int rrH = cur.r.r == null ? 0 : cur.r.r.h;
                    if (rrH >= rlH) {
                        cur = leftRotate(cur);
                    } else {
                        cur.r = rightRotate(cur.r);
                        cur = leftRotate(cur);
                    }
                }
            }
            return cur;
        }

        // TODO: 【注意前提】因为叫add，前提条件为 k 不存在 treemap中。 该前提由上游方法保证，而且必须有上层方法控制size++
        private AVLNode<K, V> add(AVLNode<K, V> cur, K k, V v) {
            if (cur == null) {
                return new AVLNode<K, V>(k, v);
            }
            if (k.compareTo(cur.k) > 0) {
                cur.r = add(cur.r, k, v);
            } else if (k.compareTo(cur.k) < 0) {
                // TODO: 【错误点】千万注意，递归交给孩子后 由孩子指针接住返回的新头。 还有不要写错了，看下面两个错误示例
                //  错误1：cur = add(cur.l, k, v)  cur.l递归，那么返回左子树新头，要由 cur.l 接住
                //  错误2：cur.r = add(cur.l, k, v);  这个单纯是迷糊了


                cur.l = add(cur.l, k, v);
            }
            cur.h = Math.max((cur.l == null ? 0 : cur.l.h), (cur.r == null ? 0 : cur.r.h)) + 1;
            return maintain(cur);
        }

        // TODO: 【注意前提】因为叫delete，前提为k必须已经在treemap。 该前提由上游方法保证，且必须有上游方法来保证size--
        private AVLNode<K, V> delete(AVLNode<K, V> cur, K k) {
            if (k.compareTo(cur.k) > 0) {
                cur.r = delete(cur.r, k);
            } else if (k.compareTo(cur.k) < 0) {
                cur.l = delete(cur.l, k);
            } else {
                if (cur.l == null && cur.r == null) {
                    cur = null;
                } else if (cur.l == null && cur.r != null) {
                    cur = cur.r;
                } else if (cur.l != null && cur.r == null) {
                    cur = cur.l;
                } else {
                    AVLNode<K, V> des = cur.r;
                    while (des.l != null) {
                        des = des.l;
                    }
                    // TODO: 错误原因：删除两个孩子节点时，只需要在右子树中删除后继节点；如果从 cur 整棵子树删除 des.k，后续一旦 delete 内部做 maintain 旋转，cur 可能变成旋转后的新头，再用 des 接管 cur.l/cur.r 会有丢失结构的风险。
                    //  修改意见：用 cur.r 接住右子树删除后的新头。
                    //  原语句：cur = delete(cur, des.k);  正确语句：cur.r = delete(cur.r, des.k);
                    cur.r = delete(cur.r, des.k);
                    des.l = cur.l;
                    des.r = cur.r;
                    cur = des;
                }
            }
            if (cur != null) {
                cur.h = Math.max((cur.l == null ? 0 : cur.l.h), (cur.r == null ? 0 : cur.r.h)) + 1;
            }
            // TODO: 错误原因：delete 后只更新高度但没有执行 maintain，删除会破坏 AVL 平衡条件；
            //  修改意见：删除回溯阶段和 add 一样，更新高度后必须返回 maintain(cur) 的新头。
            //  原语句： return cur; 正确语句： return maintain(cur)
            return maintain(cur);
        }

        private AVLNode<K, V> findLastIndex(K k) {
            if (root == null) {
                return null;
            }
            AVLNode<K, V> cur = root;
            AVLNode<K, V> des = null;
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

        private AVLNode<K, V> findLastNoSmallIndex(K k) {
            AVLNode<K, V> cur = root;
            AVLNode<K, V> des = null;
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
            AVLNode<K, V> node = findLastIndex(k);
            return node != null && k.compareTo(node.k) == 0;
        }


        public void put(K k, V v) {
            AVLNode<K, V> des = findLastIndex(k);
            if (des != null && k.compareTo(des.k) == 0) {
                des.v = v;
            } else {
                size++;
                root = add(root, k, v);
            }
        }

        public void remove(K k) {
            // TODO: 【错误点】class AVLTreeMap 的 size变量 全部/只在 put和remove方法中执行
            if (containsKey(k)) {
                root = delete(root, k);
                size--;
            }
        }

        public K floorKey(K k) {
            AVLNode<K, V> des = findLastIndex(k);
            return des == null ? null : des.k;
        }

        public K ceilingKey(K k) {
            AVLNode<K, V> des = findLastNoSmallIndex(k);
            return des == null ? null : des.k;
        }

        public V get(K k) {
            AVLNode<K, V> des = findLastIndex(k);
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
            AVLNode<K, V> pre = null;
            AVLNode<K, V> cur = root;
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
            AVLNode<K, V> pre = null;
            AVLNode<K, V> cur = root;
            while (cur != null) {
                pre = cur;
                cur = cur.r;
            }
            return pre.k;
        }

        public int size() {
            return size;
        }

    }


}
