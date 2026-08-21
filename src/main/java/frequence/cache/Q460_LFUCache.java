package frequence.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 460. LFU 缓存
 *
 * <p>设计并实现最近最少使用（Least Frequently Used，LFU）缓存：
 * <ul>
 *   <li>{@code get(key)}：key不存在时返回-1；存在时返回value，并将访问频次加1。</li>
 *   <li>{@code put(key, value)}：更新已有key时同时将访问频次加1；插入新key时频次为1。</li>
 *   <li>容量满时，优先淘汰访问频次最低的节点。</li>
 *   <li>多个节点访问频次相同时，淘汰其中最久未使用的节点，即在同频次内执行LRU。</li>
 *   <li>{@code get} 和 {@code put} 的平均时间复杂度都必须为 {@code O(1)}。</li>
 * </ul>
 *
 * <p>核心结构：
 * <pre>
 * keyMap：key -&gt; Node
 *     负责在O(1)平均时间内定位缓存节点。
 *
 * freqMap：frequency -&gt; DoubleLinkedList
 *     按访问频次分桶，每个桶内部再维护一条LRU双向链表。
 *
 * minFreq：
 *     记录当前缓存中的最小访问频次，使淘汰时可以O(1)定位目标桶。
 * </pre>
 *
 * <p>每个频次桶内部的顺序不变量：
 * <pre>
 * head(dummy) &lt;-&gt; 同频次MRU ... 同频次LRU &lt;-&gt; tail(dummy)
 * </pre>
 * 新进入该频次桶的节点放在head后面；需要淘汰时，删除
 * {@code freqMap.get(minFreq)} 中的 {@code tail.prev}。
 *
 * <p>三个必须记住的状态变化：
 * <ol>
 *   <li>访问节点：从旧频次桶删除，{@code frequency++}，加入新频次桶头部。</li>
 *   <li>旧桶为空且旧频次等于minFreq：{@code minFreq = oldFrequency + 1}。</li>
 *   <li>插入新节点：频次一定为1，因此 {@code minFreq = 1}。</li>
 * </ol>
 *
 * <p>记忆结论：
 * <pre>
 * keyMap负责找节点；
 * freqMap负责找频次桶；
 * 双向链表负责同频次内的LRU；
 * minFreq负责O(1)找到淘汰桶。
 * </pre>
 *
 * <p>复杂度：{@code get/put} 的平均时间复杂度均为 {@code O(1)}；
 * 所有Map和链表保存的节点总数不超过capacity，空间复杂度为 {@code O(capacity)}。
 * LRU与LFU的结构对比参见同包{@code 缓存淘汰策略.md}。
 */
public class Q460_LFUCache {

    private final int capacity;

    /**
     * key到缓存节点的映射，用于O(1)定位节点。
     */
    private final Map<Integer, Node> keyMap = new HashMap<>();

    /**
     * 访问频次到双向链表的映射。
     * 每个节点只属于一个频次桶，所有桶内节点数量之和等于keyMap.size()。
     */
    private final Map<Integer, DoubleLinkedList> freqMap = new HashMap<>();

    /**
     * 当前缓存中存在的最小访问频次。
     * 缓存为空时其值没有业务意义；插入新节点后会重置为1。
     */
    private int minFreq;

    public Q460_LFUCache(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        Node node = keyMap.get(key);
        if (node == null) {
            return -1;
        }

        // get命中代表节点被访问一次：频次+1，并成为新频次桶内的MRU。
        increaseFrequency(node);
        return node.value;
    }

    public void put(int key, int value) {
        // 容量为0时不能保存任何节点，否则后续无法找到可淘汰的频次桶。
        if (capacity == 0) {
            return;
        }

        Node node = keyMap.get(key);
        if (node != null) {
            // 更新已有key也算访问一次，不能只修改value而遗漏升频。
            node.value = value;
            increaseFrequency(node);
            return;
        }

        if (keyMap.size() == capacity) {
            // 先定位最小频次桶，再淘汰该桶内最久未使用的节点。
            DoubleLinkedList minFreqList = freqMap.get(minFreq);
            Node deletedNode = minFreqList.removeLast();
            keyMap.remove(deletedNode.key);

            if (minFreqList.isEmpty()) {
                freqMap.remove(minFreq);
            }
        }

        // 新节点的访问频次固定为1，并且刚插入，所以是频次1桶内的MRU。
        Node newNode = new Node(key, value);
        keyMap.put(key, newNode);
        addToFrequencyList(newNode);

        // TODO: 【关键】只要插入了新节点，当前全局最小频次就一定重新变成1。
        minFreq = 1;
    }

    /**
     * 将节点从频次f移动到频次f+1。
     *
     * <p>这是get和更新已有key共同复用的核心操作。
     */
    private void increaseFrequency(Node node) {
        int oldFrequency = node.frequency;
        DoubleLinkedList oldList = freqMap.get(oldFrequency);
        oldList.remove(node);

        if (oldList.isEmpty()) {
            freqMap.remove(oldFrequency);

            // TODO: 【最容易写错】只有“空桶恰好是当前最小频次桶”时，才能更新minFreq。
            // 当前节点马上会进入oldFrequency + 1桶，所以新的minFreq可以直接加1。
            if (minFreq == oldFrequency) {
                minFreq = oldFrequency + 1;
            }
        }

        node.frequency++;
        addToFrequencyList(node);
    }

    /**
     * 将节点加入其当前frequency对应链表的头部，使它成为该频次下的MRU。
     */
    private void addToFrequencyList(Node node) {
        DoubleLinkedList list = freqMap.get(node.frequency);
        if (list == null) {
            list = new DoubleLinkedList();
            freqMap.put(node.frequency, list);
        }
        list.addFirst(node);
    }

    /**
     * 缓存节点。
     *
     * <p>必须保存key：淘汰链表节点时，需要通过key同步执行
     * {@code keyMap.remove(node.key)}。
     */
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
     * 一个访问频次桶对应一条双向链表。
     *
     * <p>head和tail是永久存在的哨兵节点：
     * {@code head.next} 是桶内MRU，{@code tail.prev} 是桶内LRU。
     */
    private static class DoubleLinkedList {
        private final Node head = new Node(0, 0);
        private final Node tail = new Node(0, 0);

        DoubleLinkedList() {
            head.next = tail;
            tail.prev = head;
        }

        /**
         * 将节点插入head后面，使其成为桶内MRU。
         */
        void addFirst(Node node) {
            Node first = head.next;

            head.next = node;
            node.prev = head;
            node.next = first;
            first.prev = node;
        }

        /**
         * 在O(1)内删除已知节点。
         * 两个哨兵保证真实节点始终同时具有prev和next。
         */
        void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
        }

        /**
         * 删除并返回桶内LRU节点。
         */
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
