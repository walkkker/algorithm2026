package frequence.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * LFU缓存面试模板。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：742177419。用户版本作为
 * {@link MyLFUCache}保留；缓存题的构造器和{@code get/put}属于固定API，不重命名方法。
 *
 * <p>固定记忆：
 * <pre>
 * keyMap：key -&gt; Node，负责O(1)找节点
 * freqMap：frequency -&gt; NodeList，负责O(1)找频次桶
 * NodeList：同频次内按照MRU到LRU排列
 * minFreq：负责O(1)找到淘汰桶
 * </pre>
 *
 * <p>关键状态变化：
 * <pre>
 * get命中 / put更新：
 *     旧桶删除 -&gt; frequency++ -&gt; 加入新桶头部
 *
 * 插入新节点：
 *     frequency = 1，minFreq = 1
 *
 * 容量已满：
 *     删除freqMap[minFreq]的尾部真实节点
 * </pre>
 */
public class Q460_LFUCacheTemplate {

    /**
     * 用户在LeetCode独立完成的AC版本。
     */
    public static class MyLFUCache {
        private final HashMap<Integer, MyNode> nodeMap = new HashMap<>();
        private final HashMap<Integer, MyNodeList> freqMap = new HashMap<>();
        private int minFreq = 1;
        private final int capacity;

        public MyLFUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            MyNode node = nodeMap.get(key);
            if (node == null) {
                return -1;
            }
            increaseFreq(node);
            return node.value;
        }

        public void put(int key, int value) {
            if (capacity == 0) {
                return;
            }

            MyNode node = nodeMap.get(key);
            if (node != null) {
                node.value = value;
                increaseFreq(node);
                return;
            }

            if (nodeMap.size() == capacity) {
                MyNodeList minNodeList = freqMap.get(minFreq);
                MyNode lfu = minNodeList.removeLru();
                // TODO: 【曾遗漏】淘汰链表节点后，必须同步从nodeMap注销。
                nodeMap.remove(lfu.key);
                if (minNodeList.isEmpty()) {
                    freqMap.remove(minFreq);
                }
            }

            MyNode newNode = new MyNode(key, value);
            // TODO: 【曾遗漏】新节点必须先注册到nodeMap，再加入频次桶。
            nodeMap.put(key, newNode);
            addToNodeList(newNode);
            minFreq = 1;
        }

        private void increaseFreq(MyNode node) {
            int oldFreq = node.freq;
            node.freq++;

            MyNodeList oldNodeList = freqMap.get(oldFreq);
            oldNodeList.removeNode(node);
            if (oldNodeList.isEmpty()) {
                freqMap.remove(oldFreq);
                if (oldFreq == minFreq) {
                    minFreq++;
                }
            }
            addToNodeList(node);
        }

        private void addToNodeList(MyNode node) {
            if (!freqMap.containsKey(node.freq)) {
                freqMap.put(node.freq, new MyNodeList());
            }
            MyNodeList nodeList = freqMap.get(node.freq);
            nodeList.addToHead(node);
        }

        private static class MyNode {
            int key;
            int value;
            int freq = 1;
            MyNode next;
            MyNode last;

            MyNode(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private static class MyNodeList {
            private final MyNode head = new MyNode(0, 0);
            private final MyNode tail = new MyNode(0, 0);

            MyNodeList() {
                // TODO: 【曾遗漏】两个哨兵初始化时必须先成链。
                head.next = tail;
                tail.last = head;
            }

            MyNode removeNode(MyNode node) {
                node.last.next = node.next;
                node.next.last = node.last;
                return node;
            }

            void addToHead(MyNode node) {
                MyNode first = head.next;
                head.next = node;
                node.next = first;
                first.last = node;
                node.last = head;
            }

            MyNode removeLru() {
                return removeNode(tail.last);
            }

            boolean isEmpty() {
                return head.next == tail;
            }
        }
    }

    private final int capacity;
    private int minFreq;
    private final Map<Integer, Node> keyMap = new HashMap<>();
    private final Map<Integer, NodeList> freqMap = new HashMap<>();

    public Q460_LFUCacheTemplate(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        Node node = keyMap.get(key);
        if (node == null) {
            return -1;
        }
        increaseFrequency(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }

        Node node = keyMap.get(key);
        if (node != null) {
            node.value = value;
            increaseFrequency(node);
            return;
        }

        if (keyMap.size() == capacity) {
            NodeList minList = freqMap.get(minFreq);
            Node deletedNode = minList.removeLast();
            keyMap.remove(deletedNode.key);
            if (minList.isEmpty()) {
                freqMap.remove(minFreq);
            }
        }

        Node newNode = new Node(key, value);
        keyMap.put(key, newNode);
        addToFrequencyList(newNode);

        // 新节点的频次固定为1，所以全局最小频次重置为1。
        minFreq = 1;
    }

    /**
     * 节点升频：旧桶删除，频次加1，加入新桶头部。
     */
    private void increaseFrequency(Node node) {
        int oldFrequency = node.frequency;
        NodeList oldList = freqMap.get(oldFrequency);
        oldList.remove(node);

        if (oldList.isEmpty()) {
            freqMap.remove(oldFrequency);

            // 只有被清空的桶恰好是最小频次桶时，才更新minFreq。
            if (minFreq == oldFrequency) {
                minFreq++;
            }
        }

        node.frequency++;
        addToFrequencyList(node);
    }

    /**
     * 将节点加入其frequency对应桶的头部，成为同频次下的MRU。
     */
    private void addToFrequencyList(Node node) {
        NodeList list = freqMap.get(node.frequency);
        if (list == null) {
            list = new NodeList();
            freqMap.put(node.frequency, list);
        }
        list.addFirst(node);
    }

    private static class Node {
        int key;
        int value;
        int frequency = 1;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 单个频次桶：head.next是MRU，tail.prev是LRU。
     */
    private static class NodeList {
        private final Node head = new Node(0, 0);
        private final Node tail = new Node(0, 0);

        NodeList() {
            head.next = tail;
            tail.prev = head;
        }

        void addFirst(Node node) {
            Node first = head.next;
            head.next = node;
            node.prev = head;
            node.next = first;
            first.prev = node;
        }

        void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        Node removeLast() {
            Node last = tail.prev;
            remove(last);
            return last;
        }

        boolean isEmpty() {
            return head.next == tail;
        }
    }
}
