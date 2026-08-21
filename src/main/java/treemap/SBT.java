package treemap;

import treemap.compare.Code01_SizeBalancedTreeMap;

import static treemap.compare.Code01_SizeBalancedTreeMap.printAll;

/**
 * 平衡因子为 size
 *
 * TODO：【错误点】
 *  1. getLastIndex, getLastNoBigIndex, getLastNoSmallIndex 都存在情况返回null，【对结果处理时一定要注意 空指针处理】
 *  2. private add / delete 方法，这是SBT，所以一定要在方法开头 cur.size++ / cur.size--，使得每个遍历的节点都调整node.size
 */
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

        private SBTNode<K, V> root;

        public SBTreeMap() {
            root = null;
        }

        private SBTNode<K, V> leftRotate(SBTNode<K, V> cur) {
            SBTNode<K, V> right = cur.r;
            cur.r = right.l;
            right.l = cur;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            right.size = (right.l != null ? right.l.size : 0) + (right.r != null ? right.r.size : 0) + 1;
            return right;
        }

        private SBTNode<K, V> rightRotate(SBTNode<K, V> cur) {
            SBTNode<K, V> left = cur.l;
            cur.l = left.r;
            left.r = cur;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            left.size = (left.l != null ? left.l.size : 0) + (left.r != null ? left.r.size : 0) + 1;
            return left;
        }

        private SBTNode<K, V> maintain(SBTNode<K, V> cur) {
            if (cur == null) {
                return null;
            }
            int lSize = cur.l != null ? cur.l.size : 0;
            int rSize = cur.r != null ? cur.r.size : 0;
            int llSize = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
            int lrSize = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            int rrSize = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
            int rlSize = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
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
            cur.l = maintain(cur.l);
            cur.r = maintain(cur.r);
            cur = maintain(cur);
            return cur;
        }


        private SBTNode<K, V> getLastIndex(K key) {
            SBTNode<K, V> cur = root;
            SBTNode<K, V> pre = root;
            while (cur != null) {
                pre = cur;
                if (cur.k.compareTo(key) == 0) {
                    break;
                } else if (cur.k.compareTo(key) < 0) {
                    cur = cur.r;
                } else if (cur.k.compareTo(key) > 0) {
                    cur = cur.l;
                }
            }
            return pre;
        }


        // <=
        private SBTNode<K, V> getLastNoBigIndex(K key) {
            SBTNode<K, V> cur = root;
            SBTNode<K, V> ans = null;
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

        // >=
        private SBTNode<K, V> getLastNoSmallIndex(K key) {
            SBTNode<K, V> cur = root;
            SBTNode<K, V> ans = null;
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

        /**
         * 保证 0< kth <= size
         *
         * @param cur
         * @param kth
         * @return
         */
        private SBTNode<K, V> getKthIndex(SBTNode<K, V> cur, int kth) {
            int leftAndMiddle = (cur.l != null ? cur.l.size : 0) + 1;
            if (leftAndMiddle == kth) {
                return cur;
            } else if (kth < leftAndMiddle) {
                return getKthIndex(cur.l, kth);
            } else {
                return getKthIndex(cur.r, kth - leftAndMiddle);
            }
        }

        // TODO: 【错误】切记，这是SBT，对于遍历的每个节点要size++！！
        private SBTNode<K, V> add(SBTNode<K, V> cur, K key, V value) {
            if (cur == null) {
                return new SBTNode<>(key, value);
            }
            cur.size++;
            if (cur.k.compareTo(key) < 0) {
                cur.r = add(cur.r, key, value);
            } else {
                cur.l = add(cur.l, key, value);
            }
            return maintain(cur);
        }

        // TODO: 【错误】这是SBT，对于每个遍历的节点要size--
        private SBTNode<K, V> delete(SBTNode<K, V> cur, K key) {
            cur.size--;
            if (cur.k.compareTo(key) < 0) {
                cur.r = delete(cur.r, key);
            } else if (cur.k.compareTo(key) > 0) {
                cur.l = delete(cur.l, key);
            } else {
                if (cur.l == null && cur.r == null) {
                    cur = null;
                } else if (cur.l != null && cur.r == null) {
                    cur = cur.l;
                } else if (cur.l == null && cur.r != null) {
                    cur = cur.r;
                } else {
                    // 后继节点
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
            if (cur != null) {
                // 主要针对最后一种情况， successor换到了cur的位置，需要重新计算size
                cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            }
            return cur;
        }

        public int size() {
            return root == null ? 0 : root.size;
        }

        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            SBTNode<K, V> lastNode = getLastIndex(key);
            return lastNode.k.compareTo(key) == 0;
        }

        public void put(K key, V value) {
            SBTNode<K, V> lastNode = getLastIndex(key);
            // TODO: 【错误】 getLastIndex（） 可能返回null (root==null时)，所以if条件要完善
            //  if (lastNode.k.compareTo(key) == 0) {
            if (lastNode != null && lastNode.k.compareTo(key) == 0) {
                lastNode.v = value;
            } else {
                root = add(root, key, value);
            }
        }

        public void remove(K key) {
            if (key == null) {
                return;
            }
            if (containsKey(key)) {
                root = delete(root, key);
            }
        }

        // start from 0
        public K getIndexKey(int index) {
            if (root == null) {
                return null;
            }
            // TODO: 需要检查index范围，必须[0, size) 。因为 getKthIndex不做校验检查，必须调用方保证kth符合要求
            if (index < 0 || index >= root.size) {
                return null;
            }
            int kth = index + 1;
            SBTNode<K, V> kthNode = getKthIndex(root, kth);
            return kthNode.k;
        }

        public V get(K key) {
            SBTNode<K, V> lastNode = getLastIndex(key);
            return lastNode == null ? null : lastNode.v;
        }

        public K floorKey(K key) {
            if (key == null) {
                return null;
            }
            SBTNode<K, V> lastNoBigNode = getLastNoBigIndex(key);
            // TODO: 特别注意, getLastNoBigIndex && getLastNoSmallIndex 都会返回null（代表不存在该目标key），所以要做空指针检查！！！
            //  return lastNoBigNode.k;
            return lastNoBigNode == null ? null : lastNoBigNode.k;
        }

        public K ceilingKey(K key) {
            if (key == null) {
                return null;
            }
            SBTNode<K, V> node = getLastNoSmallIndex(key);
            return node == null ? null : node.k;
        }

        public K firstKey() {
            if (root == null) {
                return null;
            }
            SBTNode<K, V> cur = root;
            while (cur.l != null) {
                cur = cur.l;
            }
            return cur.k;
        }

        public K lastKey() {
            if (root == null) {
                return null;
            }
            SBTNode<K, V> cur = root;
            while (cur.r != null) {
                cur = cur.r;
            }
            return cur.k;
        }

    }

    // for test
    public static void printAll(SBTNode<String, Integer> head) {
        System.out.println("Binary Tree:");
        printInOrder(head, 0, "H", 17);
        System.out.println();
    }

    // for test
    public static void printInOrder(SBTNode<String, Integer> head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printInOrder(head.r, height + 1, "v", len);
        String val = to + "(" + head.k + "," + head.v + ")" + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printInOrder(head.l, height + 1, "^", len);
    }

    // for test
    public static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        SBTreeMap<String, Integer> sbt = new SBTreeMap<String, Integer>();
        sbt.put("d", 4);
        sbt.put("c", 3);
        sbt.put("a", 1);
        sbt.put("b", 2);
        // sbt.put("e", 5);
        sbt.put("g", 7);
        sbt.put("f", 6);
        sbt.put("h", 8);
        sbt.put("i", 9);
        sbt.put("a", 111);
        System.out.println(sbt.get("a"));
        sbt.put("a", 1);
        System.out.println(sbt.get("a"));
        for (int i = 0; i < sbt.size(); i++) {
            System.out.println(sbt.getIndexKey(i) + " , " + sbt.get(sbt.getIndexKey(i)));
        }
        printAll(sbt.root);
        System.out.println(sbt.firstKey());
        System.out.println(sbt.lastKey());
        System.out.println(sbt.floorKey("g"));
        System.out.println(sbt.ceilingKey("g"));
        System.out.println(sbt.floorKey("e"));
        System.out.println(sbt.ceilingKey("e"));
        System.out.println(sbt.floorKey(""));
        System.out.println(sbt.ceilingKey(""));
        System.out.println(sbt.floorKey("j"));
        System.out.println(sbt.ceilingKey("j"));
        sbt.remove("d");
        printAll(sbt.root);
        sbt.remove("f");
        printAll(sbt.root);

    }

}
