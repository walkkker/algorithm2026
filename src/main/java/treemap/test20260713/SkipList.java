package treemap.test20260713;

import java.util.ArrayList;

public class SkipList {

    public static class SkipListNode<K extends Comparable<K>, V> {
        K k;
        V v;
        ArrayList<SkipListNode<K, V>> nextNodes;

        public SkipListNode(K _k, V _v) {
            k = _k;
            v = _v;
            // TODO: 【错误点】初始化遗漏 -> ArrayList 必须初始化！！！
            nextNodes = new ArrayList<>();
        }
    }

    public static class SkipListMap<K extends Comparable<K>, V> {
        SkipListNode<K, V> head;
        int maxLevel;
        int size;


        public SkipListMap() {
            head = new SkipListNode<>(null, null);
            head.nextNodes.add(null);
            maxLevel = 0;
            size = 0;
        }

        private SkipListNode<K, V> getMostRightLessNodeInLevel(SkipListNode<K, V> cur, K k, int level) {
            SkipListNode<K, V> next = cur.nextNodes.get(level);
            while (next != null && next.k.compareTo(k) < 0) {
                cur = next;
                next = next.nextNodes.get(level);
            }
            return cur;
        }

        private SkipListNode<K, V> getMostRightLessNodeInTree(K k) {
            SkipListNode<K, V> cur = head;
            int level = maxLevel;
            while (level >= 0) {
                cur = getMostRightLessNodeInLevel(cur, k, level);
                level--;
            }
            return cur;
        }

        private void add(K k, V v) {
            SkipListNode<K, V> newNode = new SkipListNode<>(k, v);
            int level = 0;
            while (Math.random() < 0.5) {
                level++;
            }
            // TODO: 这个地方容易漏掉
            for (int i = 0; i <= level; i++) {
                newNode.nextNodes.add(null);
            }

            if (level > maxLevel) {
                maxLevel = level;
                while (head.nextNodes.size() < maxLevel + 1) {
                    head.nextNodes.add(null);
                }
            }

            SkipListNode<K, V> cur = head;
            int _maxLevel = maxLevel;
            // while (_maxLevel-- >= 0) {   // TODO: 【错误点】这样写，会导致_maxLevel==0时，但是循环体执行时，getMostRightLessNodeInLevel 拿到的是-1，直接报错！！！
            // TODO: 【特别注意】上面这种写法只适合 计数 =》 也就是 1.这个变量其他地方不会用了（因为会被修改） 2. 循环体内不使用 （不然就会出现上述描述的问题 -> 解决方案见当前版本：把--动作放在循环体末尾，类似for的效果）
            while (_maxLevel >= 0) {
                cur = getMostRightLessNodeInLevel(cur, k, _maxLevel);
                if (_maxLevel <= level) {
                    // SkipListNode<K, V> nextNode = cur.nextNodes.get(level);  TODO: 错误。就是因为循环变量混乱，导致写的时候乱了。 不如直接用for
                    SkipListNode<K, V> nextNode = cur.nextNodes.get(_maxLevel);
                    cur.nextNodes.set(_maxLevel, newNode);
                    newNode.nextNodes.set(_maxLevel, nextNode);
                }
                _maxLevel--;
            }
        }

        private void delete(K k) {
            SkipListNode<K, V> cur = head;
            for (int i = maxLevel; i >= 0; i--) {
                // TODO: 错误！！！ 需要复用cur。下面错误行使得每个level都从head从头开始遍历
                // cur = getMostRightLessNodeInLevel(head, k, i);
                cur = getMostRightLessNodeInLevel(cur, k, i);
                SkipListNode<K, V> next = cur.nextNodes.get(i);
                if (next != null && next.k.compareTo(k) == 0) {
                    cur.nextNodes.set(i, next.nextNodes.get(i));
                }

                // TODO: 【易错点】1. 遗漏maxLevel--的部分;
                // TODO: 【易错点】2. 你可以选择减少maxLevel，但是一定注意 要保留maxLevel=0。 不然会把head.nextNodes的 0坐标也删除！！！
                //  错误点：if (head.nextNodes.get(i) == null) {
                //  【一定要记住】 head以及所有节点，level=0 第0层一定是保留的！！！
                if (maxLevel > 0 && head.nextNodes.get(i) == null) {
                    head.nextNodes.remove(i);
                    maxLevel--;
                }
            }
        }

        public boolean containsKey(K k) {
            SkipListNode<K, V> cur = getMostRightLessNodeInTree(k);
            SkipListNode<K, V> next = cur.nextNodes.get(0);
            return next != null && next.k.compareTo(k) == 0;
        }

        public void put(K k, V v) {
            SkipListNode<K, V> cur = getMostRightLessNodeInTree(k);
            SkipListNode<K, V> next = cur.nextNodes.get(0);
            if (next != null && next.k.compareTo(k) == 0) {
                next.v = v;
            } else {
                size++;
                add(k, v);
            }
        }

        public void remove(K k) {
            if (containsKey(k)) {
                delete(k);
                size--;
            }
        }

        public V get(K k) {
            SkipListNode<K, V> cur = getMostRightLessNodeInTree(k);
            SkipListNode<K, V> next = cur.nextNodes.get(0);
            if (next != null && next.k.compareTo(k) == 0) {
                return next.v;
            } else {
                return null;
            }
        }

        public K ceilingKey(K k) {
            SkipListNode<K, V> cur = getMostRightLessNodeInTree(k);
            SkipListNode<K, V> next = cur.nextNodes.get(0);
            if (next == null) {
                return null;
            } else {
                return next.k;
            }
        }

        public K floorKey(K k) {
            SkipListNode<K, V> cur = getMostRightLessNodeInTree(k);
            SkipListNode<K, V> next = cur.nextNodes.get(0);
            if (next != null && next.k.compareTo(k) == 0) {
                return next.k;
            } else {
                return cur.k;
            }
        }

        public K firstKey() {
            return head.nextNodes.get(0) == null ? null : head.nextNodes.get(0).k;
        }

        public K lastKey() {
            SkipListNode<K, V> cur = head;
            for (int i = maxLevel; i >= 0; i--) {
                SkipListNode<K, V> next = cur.nextNodes.get(i);
                while (next != null) {
                    cur = next;
                    next = next.nextNodes.get(i);
                }
            }
            return cur == head ? null : cur.k;
        }

        public int size() {
            return size;
        }
    }


}
