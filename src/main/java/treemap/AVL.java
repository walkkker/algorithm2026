package treemap;

public class AVL {

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
        private AVLNode<K, V> root;
        private int size;

        public AVLTreeMap() {
            root = null;
            size = 0;
        }

        // 旋转相关函数 private类： leftRotate(), rightRotate(), maintain()
        // 剩余的是SBT private基础操作：findLastIndex, findLastNoBigIndex, findLastNoSmallIndex, add, delete
        // 续 public操作： size containsKey put remove get firstKey lastKey floorKey ceilingKey
        private AVLNode<K, V> leftRotate(AVLNode<K, V> cur) {
            // 旋转
            AVLNode<K, V> right = cur.r;
            cur.r = right.l;
            right.l = cur;
            // 调整平衡因子 - avl为h  => 注意先子后父
            cur.h = Math.max(cur.l == null ? 0 : cur.l.h, cur.r == null ? 0 : cur.r.h) + 1;
            right.h = Math.max(right.l == null ? 0 : right.l.h, right.r == null ? 0 : right.r.h) + 1;
            return right;
        }

        private AVLNode<K, V> rightRotate(AVLNode<K, V> cur) {
            AVLNode<K, V> left = cur.l;
            cur.l = left.r;
            left.r = cur;
            cur.h = Math.max(cur.l == null ? 0 : cur.l.h, cur.r == null ? 0 : cur.r.h) + 1;
            left.h = Math.max(left.l == null ? 0 : left.l.h, left.r == null ? 0 : left.r.h) + 1;
            return left;
        }


        private AVLNode<K, V> maintain(AVLNode<K, V> cur) {
            if (cur == null) {
                return null;
            }
            int lh = cur.l == null ? 0 : cur.l.h;
            int rh = cur.r == null ? 0 : cur.r.h;
            if (Math.abs(lh - rh) > 1) {
                if (lh > rh) {   // LL 型 还是 LR型
                    int llh = cur.l.l == null ? 0 : cur.l.l.h;
                    int lrh = cur.l.r == null ? 0 : cur.l.r.h;
                    if (llh >= lrh) {
                        cur = rightRotate(cur);  // TODO: 这里可能换头，所以一定要定义变量接住
                    } else {
                        cur.l = leftRotate(cur.l);
                        cur = rightRotate(cur);
                    }
                } else {  // 判断RR RL
                    int rrh = cur.r.r == null ? 0 : cur.r.r.h;
                    int rlh = cur.r.l == null ? 0 : cur.r.l.h;
                    if (rrh >= rlh) {
                        cur = leftRotate(cur);
                    } else {
                        cur.r = rightRotate(cur.r);
                        cur = leftRotate(cur);
                    }
                }
            }
            return cur;
        }

        // 功能：找到最近的一个节点 -> 1) key节点本身 2) 最近的节点(可能< 或者 >)
        private AVLNode<K, V> findLastNode(K key) {
            AVLNode<K, V> cur = root;
            AVLNode<K, V> pre = root;
            while (cur != null) {
                pre = cur;
                if (key.compareTo(cur.k) == 0) {
                    break;
                } else if (key.compareTo(cur.k) < 0) {
                    cur = cur.l;
                } else if (key.compareTo(cur.k) > 0) {
                    cur = cur.r;
                }
            }
            return pre;
        }

        // <= key的 最大
        private AVLNode<K, V> findLastNoBigNode(K key) {
            AVLNode<K, V> ans = null;
            AVLNode<K, V> cur = root;
            while (cur != null) {
                if (cur.k.compareTo(key) <= 0) {
                    ans = cur;
                    cur = cur.r;
                } else {
                    cur = cur.l;
                }
            }
            return ans;
        }

        // >= 最小
        private AVLNode<K, V> findLastNoSmallNode(K key) {
            AVLNode<K, V> ans = null;
            AVLNode<K, V> cur = root;
            while (cur != null) {
                if (cur.k.compareTo(key) >= 0) {
                    ans = cur;
                    cur = cur.l;
                } else {
                    cur = cur.r;
                }
            }
            return ans;
        }

        // 前提：调用方已经确保 treeMap里面没有这个key -> 真的就是添加这个节点
        // 要递归
        private AVLNode<K, V> add(AVLNode<K, V> cur, K key, V value) {
            if (cur == null) {  // base case
                return new AVLNode(key, value);
            }
            if (cur.k.compareTo(key) < 0) {
                cur.r = add(cur.r, key, value);
            } else {
                cur.l = add(cur.l, key, value);
            }
            // TODO: 【错误-遗漏】加完节点后，要更新当前节点的平衡因子 - h
            cur.h = Math.max(cur.l == null ? 0 : cur.l.h, cur.r == null ? 0 : cur.r.h) + 1;
            return maintain(cur);
        }

        // 前提是 调用方已经确认key存在当前AVLTreeMap里了
        private AVLNode<K, V> delete(AVLNode<K, V> cur, K key) {
            if (cur.k.compareTo(key) < 0) {
                cur.r = delete(cur.r, key);
            } else if (cur.k.compareTo(key) > 0) {
                cur.l = delete(cur.l, key);
            } else {
                if (cur.l == null && cur.r == null) {
                    return null;
                } else if (cur.l != null && cur.r == null) {
                    cur = cur.l;
                } else if (cur.l == null && cur.r != null) {
                    cur = cur.r;
                } else {   // cur.l != null && cur.r != null ===> 此时头结点需要换成 右树的最左节点 || 左树的最右节点
                    AVLNode<K, V> des = cur.r;
                    while (des.l != null) {
                        des = des.l;
                    }
                    cur.r = delete(cur.r, des.k);   // TODO: 【重点-错误这里不能填cur,会重复】在右树删除des节点 ->  因为需要des(右树最左节点)往上的所有节点都调整
                    des.l = cur.l;
                    des.r = cur.r;
                    cur = des;
                }
            }
            // TODO: 调整平衡因子 - 高度 (因为 cur.l==null && cur.r==null时 直接return null，所以此时保证cur != null)
            cur.h = Math.max(cur.l == null ? 0 : cur.l.h, cur.r == null ? 0 : cur.r.h) + 1;
            return maintain(cur);
        }

        /**
         * 下面是public调用接口
         **/
        public int size() {
            return size;
        }

        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            AVLNode<K, V> lastNode = findLastNode(key);
            // TODO: 【错误】防空指针
            if (lastNode != null && lastNode.k.compareTo(key) == 0) {
                return true;
            } else {
                return false;
            }
        }

        public void put(K key, V value) {
            if (key == null) {
                return;
            }
            // 先找有无该节点： 有则只更新节点， 无则调用add方法
            AVLNode<K, V> lastNode = findLastNode(key);
            if (lastNode != null && lastNode.k.compareTo(key) == 0) {
                lastNode.v = value;
            } else {
                size++;
                // TODO: 你要用root去接add的返回值，因为可能会换头的
                root = add(root, key, value);
            }
        }

        public void remove(K key) {
            if (key == null) {
                return;
            }
            AVLNode<K, V> lastNode = findLastNode(key);
            if (lastNode != null && lastNode.k.compareTo(key) == 0) {
                root = delete(root, key);
                size--; // TOTO: 删除不要忘了更新size
            }
        }

        public V get(K key) {
            if (key == null) {
                return null;
            }
            AVLNode<K, V> lastNode = findLastNode(key);
            if (lastNode != null && lastNode.k.compareTo(key) == 0) {
                return lastNode.v;
            } else {
                return null;
            }
        }

        public K firstKey() {
            // TODO: 【遗漏】要考虑到root=null的情况
            if (root == null) {
                return null;
            }
            AVLNode<K, V> cur = root;
            while (cur.l != null) {
                cur = cur.l;
            }
            return cur.k;
        }

        public K lastKey() {
            if (root == null) {
                return null;
            }
            AVLNode<K, V> cur = root;
            while (cur.r != null) {
                cur = cur.r;
            }
            return cur.k;
        }

        // TODO: 下面这两个public方法才是最重要的  <= 的最大
        public K floorKey(K key) {
            if (key == null) {
                return null;
            }
            AVLNode<K, V> lastNoBigNode = findLastNoBigNode(key);
            // TODO: 【错误-防空指针】<= 的最大 ，万一左右的节点都大于key呢？那么此时会返回一个null
            //      错误语句：return noBigNode.k;
            return lastNoBigNode == null ? null : lastNoBigNode.k;
        }

        public K ceilingKey(K key) {
            if (key == null) {
                return null;
            }
            AVLNode<K, V> lastNoSmallNode = findLastNoSmallNode(key);
            return lastNoSmallNode == null ? null : lastNoSmallNode.k;
        }


    }
}
