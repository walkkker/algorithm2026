package frequence.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Q146 LRU缓存的参考实现，重点展示方法拆分。
 *
 * <p>链表方向约定：
 * <pre>
 * head(dummy) &lt;-&gt; MRU ... LRU &lt;-&gt; tail(dummy)
 * </pre>
 *
 * <p>方法分为三个层次：
 * <ol>
 *   <li>对外功能：{@link #get(int)}、{@link #put(int, int)}。</li>
 *   <li>组合操作：{@link #moveToHead(Node)}、{@link #removeLRU()}。</li>
 *   <li>链表原子操作：{@link #addToHead(Node)}、{@link #removeNode(Node)}。</li>
 * </ol>
 *
 * <p>记忆重点：复杂逻辑只负责组织流程，所有指针修改集中在两个原子操作中，
 * 从而避免在get、put、移动和淘汰流程中重复编写双向链表指针代码。
 */
public class Q146_LRUCacheReference {

    private final int capacity;
    private final Map<Integer, Node> map = new HashMap<>();
    private final Node head = new Node(0, 0);
    private final Node tail = new Node(0, 0);

    public Q146_LRUCacheReference(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node == null) {
            return -1;
        }

        moveToHead(node);
        return node.value;
    }

    public void put(int key, int value) {
        Node node = map.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
            return;
        }

        Node newNode = new Node(key, value);
        map.put(key, newNode);
        addToHead(newNode);

        // 选择“先插入，超容量后再淘汰”，因此capacity=0也能自然处理。
        if (map.size() > capacity) {
            Node deletedNode = removeLRU();
            map.remove(deletedNode.key);
        }
    }

    /**
     * 组合操作：已有节点被访问后，先删除，再插入MRU位置。
     */
    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    /**
     * 链表原子操作一：将节点插入head后面，使其成为MRU。
     */
    private void addToHead(Node node) {
        Node first = head.next;

        head.next = node;
        node.prev = head;
        node.next = first;
        first.prev = node;
    }

    /**
     * 链表原子操作二：从双向链表中删除一个已知的真实节点。
     */
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /**
     * 组合操作：删除并返回tail前面的LRU真实节点。
     */
    private Node removeLRU() {
        Node lru = tail.prev;
        removeNode(lru);
        return lru;
    }

    private static class Node {
        int key;
        int value;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
