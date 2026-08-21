package treemap;

import treemap.compare.Code02_SkipListMap;

import java.util.ArrayList;


/**
 * TODO:【特别多的错误！！！】特别注意下面这段语句：
 *             SkipListNode<K, V> pre = getMostRightLessNodeInTree(key);
 *             SkipListNode<K, V> next = pre.nexts.get(0);
 *   TODO：特别注意 next可能为null （pre跑到了任意level层的最右侧节点，下一个节点就是null），只能保证pre!=null（因为最小的是head）
 */
public class Code_SkipListMap {

    public static class SkipListNode<K extends Comparable<K>, V> {
        K k;
        V v;
        ArrayList<SkipListNode<K, V>> nexts;

        public SkipListNode(K _k, V _v) {
            k = _k;
            v = _v;
            nexts = new ArrayList<>();
        }

        public boolean isKeyLess(K otherKey) {
            if (k == null && otherKey != null) {   // 此时,k在head节点上
                return true;
            } else if (k != null && otherKey != null && k.compareTo(otherKey) < 0) {
                return true;
            } else {
                return false;
            }
        }

        public boolean isKeyEqual(K otherKey) {
            if (k == null && otherKey == null) {
                return true;
            }
            if (k != null && otherKey != null && k.compareTo(otherKey) == 0) {
                return true;
            }
            return false;
        }
    }


    public static class SkipListMap<K extends Comparable<K>, V> {

        // TODO: 【遗漏】
        private static final double PROBABILITY = 0.5;
        private SkipListNode<K, V> head;
        private int size;
        private int maxLevel;


        public SkipListMap() {
            head = new SkipListNode(null, null);
            size = 0;
            maxLevel = 0;
            // TODO: 【错误-遗漏】 初始化时，因为maxLevel=0，所以第0层的 每个节点都必须有指针，因此必须 arrayList.add(null)
            head.nexts.add(null);
        }


        // 基础方法private: getMostRightLessNodeInTree. getMostRightLessNodeInLevel
        // 公共方法: put remove size containsKey floorKey ceilingKey firstKey lastKey

        // TODO: 【错误】参数漏掉了 SkipListNode pre。 这个方法支持从 level层 任意一个pre节点 找到小于key的最右节点
        private SkipListNode<K, V> getMostRightLessNodeInLevel(SkipListNode<K, V> pre, K key, int level) {
            SkipListNode<K, V> cur = pre;
            SkipListNode<K, V> next = cur.nexts.get(level);
            while (next != null && next.isKeyLess(key)) {
                cur = next;
                next = next.nexts.get(level);
            }
            return cur;
        }


        private SkipListNode<K, V> getMostRightLessNodeInTree(K key) {
            int level = maxLevel;
            SkipListNode<K, V> pre = head;
            while (level >= 0) {
                pre = getMostRightLessNodeInLevel(pre, key, level);
                level--;
            }
            return pre;
        }


        public void put(K key, V value) {
            if (key == null) {
                return;
            }

            SkipListNode<K, V> pre = getMostRightLessNodeInTree(key);
            SkipListNode<K, V> next = pre.nexts.get(0);
            // TODO: 【错误】遗漏next==null的情况
            //  if (next.isKeyEqual(key)) {
            //  修复建议：只有满足下面的要求，才说明 skipListMap 内包含该key节点
            if (next != null && next.isKeyEqual(key)) {
                next.v = value;
            } else {
                size++;  // TODO: 遗漏size修改
                // 本质就是newNodeLevel每层都操作一个链表插入的动作
                SkipListNode<K, V> newNode = new SkipListNode<>(key, value);
                int newNodeLevel = 0;
                while (Math.random() < PROBABILITY) {
                    newNodeLevel++;
                }
                int level = newNodeLevel;
                while (level >= 0) {
                    newNode.nexts.add(null);
                    level--;
                }
                while (maxLevel < newNodeLevel) {
                    head.nexts.add(null);
                    maxLevel++;
                }

                pre = head;
                level = maxLevel;
                while (level >= 0) {
                    pre = getMostRightLessNodeInLevel(pre, key, level);
                    if (level <= newNodeLevel) {
                        newNode.nexts.set(level, pre.nexts.get(level));
                        pre.nexts.set(level, newNode);
                    }
                    level--;
                }
            }
        }

        // 实现Key方法前，先实现 containsKey方法
        public boolean containsKey(K key) {
            SkipListNode<K, V> pre = getMostRightLessNodeInTree(key);
            SkipListNode<K, V> find = pre.nexts.get(0);
            // TODO: 【错误】一样的错误，pre.nexts.get(0) 可能为Null
            if (find != null && find.isKeyEqual(key)) {
                return true;
            } else {
                return false;
            }
        }

        public void remove(K key) {
            // 核心就是 依次检查每层是否存在key节点，有的话就执行 删除链表节点
            if (key == null || !containsKey(key)) {
                return;
            }
            size--;  // TODO: 遗漏size修改
            int level = maxLevel;
            SkipListNode<K, V> pre = head;
            while (level >= 0) {
                pre = getMostRightLessNodeInLevel(pre, key, level);
                SkipListNode<K, V> next = pre.nexts.get(level);
                // 你要检查这一层有没有 Key节点
                // TODO: 【一样的错误】看上面，next==null
                //  if (next.isKeyEqual(key)) {
                if (next != null && next.isKeyEqual(key)) {
                    pre.nexts.set(level, next.nexts.get(level));
                }
                // 检查是否当前层 为空层，删除
                // TODO: 【超级错误！！！】 检查条件必须包含 level != 0 !!!!! ==> 不然一旦出现remove后的空表情况，将无法使用 head.nexts.get(0) 导致查找0层下一个节点报错了。
                //  if (pre == head && pre.nexts.get(level) == null) {
                if (level != 0 && pre == head && pre.nexts.get(level) == null) {
                    head.nexts.remove(level);
                    maxLevel--;
                }
                level--;
            }
        }

        public V get(K key) {
            if (key == null) {
                return null;
            }
            SkipListNode<K, V> pre = getMostRightLessNodeInTree(key);
            SkipListNode<K, V> find = pre.nexts.get(0);
            if (find.isKeyEqual(key)) {
                return find.v;
            } else {
                return null;
            }
        }

        public K firstKey() {
            return head.nexts.get(0) == null ? null : head.nexts.get(0).k;
        }

        public K lastKey() {
            int level = maxLevel;
            SkipListNode<K, V> cur = head;
            while (level >= 0) {
                while (cur.nexts.get(level) != null) {
                    cur = cur.nexts.get(level);
                }
                level--;
            }
            return cur.k;
        }

        // TODO: 需要关注 next可能为null。 我们只能保证getMostRightLessNodeInTree 返回的是有效节点，因为最差返回 head
        public K floorKey(K key) {
            if (key == null) {
                return null;
            }
            SkipListNode<K, V> pre = getMostRightLessNodeInTree(key);
            SkipListNode<K, V> next = pre.nexts.get(0);
            return next != null && next.isKeyEqual(key) ? key : pre.k;
        }


        public K ceilingKey(K key) {
            if (key == null) {
                return null;
            }
            SkipListNode<K, V> pre = getMostRightLessNodeInTree(key);
            SkipListNode<K, V> next = pre.nexts.get(0);
            // TODO: 这里正常一定返回next就行了，因为pre < key。 所以必定next>=key。
            //       但是特别注意,只能保证 pre!=null(最差为head)，但是next maybe is null。 所以要分类讨论
            return next == null ? null : next.k;
        }

        public int size() {
            return size;
        }
    }


    // for test
    public static void printAll(SkipListMap<String, String> obj) {
        for (int i = obj.maxLevel; i >= 0; i--) {
            System.out.print("Level " + i + " : ");
            SkipListNode<String, String> cur = obj.head;
            while (cur.nexts.get(i) != null) {
                SkipListNode<String, String> next = cur.nexts.get(i);
                System.out.print("(" + next.k + " , " + next.v + ") ");
                cur = next;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SkipListMap<String, String> test = new SkipListMap<>();
        printAll(test);
        System.out.println("======================");
        test.put("A", "10");
        printAll(test);
        System.out.println("======================");
        test.remove("A");
        printAll(test);
        System.out.println("======================");
        test.put("E", "E");
        test.put("B", "B");
        test.put("A", "A");
        test.put("F", "F");
        test.put("C", "C");
        test.put("D", "D");
        printAll(test);
        System.out.println("======================");
        System.out.println(test.containsKey("B"));
        System.out.println(test.containsKey("Z"));
        System.out.println(test.firstKey());
        System.out.println(test.lastKey());
        System.out.println(test.floorKey("D"));
        System.out.println(test.ceilingKey("D"));
        System.out.println("======================");
        test.remove("D");
        printAll(test);
        System.out.println("======================");
        System.out.println(test.floorKey("D"));
        System.out.println(test.ceilingKey("D"));


    }
}
