package frequence.cache;

import java.util.*;

/**
 * 146. LRU 缓存
 *
 * TODO：【错误点】双链表，删除节点时， prev 和 next 建立连接 也要是双向的！！！ 对应两行代码啊！！ 别只next！！
 *
 * <p>设计并实现一个满足最近最少使用（Least Recently Used，LRU）淘汰策略的缓存：
 *
 * <ul>
 *   <li>{@code get(key)}：若键存在，返回对应值；否则返回 {@code -1}。</li>
 *   <li>{@code put(key, value)}：若键已存在则更新其值；否则插入新的键值对。</li>
 *   <li>插入导致缓存容量超限时，移除最久未被访问的键值对。</li>
 *   <li>{@code get} 和 {@code put} 操作的平均时间复杂度均应为 {@code O(1)}。</li>
 * </ul>
 *
 * <p>核心数据结构：
 * <ol>
 *   <li>{@code HashMap<key, Node>}：根据key在 {@code O(1)} 平均时间内定位链表节点。
 *       Map必须保存Node，不能只保存value，否则无法在 {@code O(1)} 内移动指定节点。</li>
 *   <li>双向链表：在已获得Node引用后，可以在 {@code O(1)} 内删除并移动该节点，
 *       同时维护所有缓存项的访问时间顺序。</li>
 * </ol>
 *
 * <p>本实现的链表顺序不变量：
 * <pre>
 * head(dummy) &lt;-&gt; MRU ... LRU &lt;-&gt; tail(dummy)
 * </pre>
 * {@code head.next} 是最近使用的真实节点，{@code tail.last} 是最久未使用的真实节点。
 * {@code get} 命中或者 {@code put} 更新已有key，都代表该节点刚被访问，必须移动到head一侧。
 *
 * <p>两个哨兵节点的作用：head和tail永久存在，空链表时满足
 * {@code head.next == tail && tail.last == head}。因此所有真实节点始终都有前驱和后继，
 * 插入、删除头尾节点时可以复用普通中间节点的指针操作，无需针对空链表、单节点、
 * 真实头节点和真实尾节点分别编写分支。
 *
 * <p>复杂度：{@code get/put} 的平均时间复杂度都是 {@code O(1)}；
 * HashMap和双向链表保存的节点数最多为capacity，空间复杂度为 {@code O(capacity)}。
 * LRU与LFU的结构对比参见同包{@code 缓存淘汰策略.md}。
 */
public class Q146_LRUCache {

    // TODO: 【可优化-封装性】这些字段均属于内部实现，可以声明为private；初始化后不换引用的字段可进一步声明为final。
    HashMap<Integer, Node> map;
    Node head;
    Node tail;
    int cap;

    public Q146_LRUCache(int capacity) {
        // LeetCode保证capacity >= 1。若写成通用组件，应在此校验capacity，防止容量为0时删除head哨兵。
        cap = capacity;
        head = new Node(0, 0);
        tail = new Node(0, 0);
        // 初始化两个哨兵。此时链表没有真实节点：head <-> tail。
        head.next = tail;
        tail.last = head;
        map = new HashMap<>();
    }

    public int get(int key) {
        // TODO: 【可优化-一次哈希查找】containsKey后再get会查询两次；
        //  可以直接Node target = map.get(key)，再通过target == null判断key是否存在。
        if (!map.containsKey(key)) {
            return -1;
        } else {
            Node target = map.get(key);
            int ans = target.value;
            // get命中也算一次访问，必须将节点移动到MRU位置。
            moveNodeToHead(target);
            return ans;
        }
    }

    public void put(int key, int value) {
        // TODO: 【可优化-一次哈希查找】这里同样可以先map.get(key)，避免containsKey和get重复查询。
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            // 更新已有key也算一次访问，该节点应成为MRU。
            moveNodeToHead(node);
        } else {
            // 当前实现选择“插入前淘汰”：满容量时先移除LRU，再插入新节点。
            if (map.size() == cap) {
                // tail是哨兵，tail.last才是真实的LRU节点。
                Node deletedNode = tail.last;
                onlyDeleteNode(deletedNode);
                map.remove(deletedNode.key);   // TODO: 【不能遗漏】必须同步删除双链表 和 HashMap
            }
            Node addedNode = new Node(key, value);
            // 新节点刚被访问，应插入head之后，成为新的MRU。
            // TODO: 【可优化-减少重复】这段插入逻辑可以抽成addNodeToHead(node)，并由moveNodeToHead复用。
            Node headNext = head.next;
            head.next = addedNode;
            addedNode.next = headNext;
            headNext.last = addedNode;
            addedNode.last = head;
            map.put(key, addedNode);
        }
    }

    /**
     * 同时保存key和value：
     * value用于get返回，key用于淘汰节点时同步执行 {@code map.remove(node.key)}。
     *
     * TODO: 【可优化】Node不依赖外部Q146_LRUCache实例，可以声明为private static class，
     * 避免每个Node隐式持有外部类引用；last通常命名为prev，与next语义更对称。
     */
    public class Node {
        int key;
        int value;
        Node last;
        Node next;

        public Node(int _key, int _value) {
            key = _key;
            value = _value;
        }
    }

    /**
     * 将已有真实节点移动到head之后，使其成为MRU。
     *
     * TODO: 【可优化-封装性】该方法只供LRU内部使用，可以声明为private。
     */
    public void moveNodeToHead(Node cur) {

        // TODO: 【错误-遗漏】双向链表，删除节点一定要 next 和 last 分别连两次
        //  所以我直接单独拎出来一个方法，防止乱了
        onlyDeleteNode(cur);

        Node headNext = head.next;
        head.next = cur;
        cur.next = headNext;
        headNext.last = cur;
        cur.last = head;
    }

    /**
     * 从双向链表中删除一个真实节点。
     *
     * <p>两个哨兵保证真实节点的last和next都不为null，因此不需要区分它位于头部、
     * 尾部还是中间。但该方法不能传入head或tail哨兵；本题capacity >= 1且只删除真实缓存节点，
     * 所以当前调用过程满足这一前提。
     *
     * TODO: 【可优化-封装性】该方法只供LRU内部使用，可以声明为private。
     */
    public void onlyDeleteNode(Node node) {
        Node pre = node.last;
        Node next = node.next;
        pre.next = next;
        next.last = pre;
    }
}
