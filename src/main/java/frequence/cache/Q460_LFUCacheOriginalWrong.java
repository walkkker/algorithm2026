package frequence.cache;


import java.util.*;
/**
 * Q460 LFU缓存：第一次独立实现的错误版本归档。
 *
 * <p><b>保留方式：</b>下面的原始代码包含无法通过编译的语法和变量错误，因此整体放在注释区，
 * 防止破坏项目构建。每处都按照“{@code TODO}错误说明、注释原错误语句、紧跟正确语句”的格式
 * 标记；完整正确实现参见同包{@code Q460_LFUCache.java}和{@code Q460_LFUCacheTemplate.java}。
 *
 * <p><b>错误汇总：</b>
 * <ol>
 *     <li>构造函数发生参数遮蔽，{@code capacity = capacity}没有给成员变量赋值。</li>
 *     <li>新key的{@code put}顺序违反题意：缓存已满时应先淘汰已有LFU节点，再插入新节点；
 *         原代码先插入并把{@code minFreq}重置为1，会使新节点错误参与本次淘汰。</li>
 *     <li>新增分支把值为{@code null}的旧变量{@code node}传入频次桶，而不是新建的节点，
 *         会触发空指针异常。</li>
 *     <li>缺少{@code capacity == 0}的边界保护。改成正确的“先淘汰、后插入”顺序后，
 *         如果没有该保护，会尝试从空缓存淘汰节点。</li>
 *     <li>{@code removeLFU}使用了不存在的变量{@code node}和{@code lfu}，无法编译。</li>
 *     <li>{@code private}拼写为{@code privtae}，无法编译。</li>
 *     <li>{@code NodeList.removeNode}声明返回{@code Node}，但所有路径都没有返回值，无法编译。</li>
 *     <li>对{@code minFreq++}的判断有误：当最小频次桶因节点从f升到f+1而变空时，
 *         {@code minFreq++}、赋值{@code oldFreq + 1}和赋值{@code newFreq}完全等价。
 *         “minFreq遗漏为1”意味着更早的位置已经破坏了不变量，不能靠本分支修复。</li>
 * </ol>
 *
 * <p><b>本次题意理解错误：</b>“淘汰频次最低的节点”只在执行当前{@code put}之前已经存在的
 * 缓存节点中选择。新节点尚未成功插入，不参与本次淘汰竞争。否则当所有旧节点频次都大于1时，
 * 任何频次从1开始的新key都会立即被淘汰，无法进入缓存。
 */


/**
 *
 TODO: 这是我自己的总结！！！
 1. 核心错误是 长代码下，容易思路乱，前后变量不一。 =》需要多练本题
 2. minFreq定位两个场景： (1)increasingFreq (2) 新增节点
 3. increasingFreq中，何时才新增 minFreq？ 需要同时满足 【旧桶删除&&旧桶是最小桶】
 4. 本题lfu添加元素时的删除规则： 先清除lfu，再添加。  一定要注意这个顺序。如果反过来，那么可能你每次新添加元素后，它就会被删除（freq=1只有这一个元素）。 这是完全不对的。

 */
class Q460_LFUCacheOriginalWrong {

    int capacity;
    HashMap<Integer, Node> nodeMap;
    HashMap<Integer, NodeList> freqMap;
    int minFreq;
    // TODO: 【错误】不需要额外一个size变量。直接使用nodeMap.size()更准确

    public Q460_LFUCacheOriginalWrong(int capacity) {
        this.capacity = capacity;
        nodeMap = new HashMap<>();
        freqMap = new HashMap<>();
        minFreq = 0;
    }

    public int get(int key) {
        Node node = nodeMap.get(key);
        if (node == null) {
            return -1;
        } else {
            increasingFrequency(node);
            return node.value;
        }
    }

    public void put(int key, int value) {
        Node node = nodeMap.get(key);
        if (node == null) {
            if (capacity == 0) {
                return;
            } else {
                // TODO: 【错误-遗漏】只有当size==capacity时，才删除
                if (nodeMap.size() == capacity) {
                    NodeList nodeList = freqMap.get(minFreq);
                    Node deleted = nodeList.removeLru();
                    nodeMap.remove(deleted.key);
                    if (nodeList.isEmpty()) {
                        freqMap.remove(minFreq);  // TODO：此时不需要检查minFreq，检查也没用。   在put方法中，如果你删除了一个元素，那必然会新添加一个元素，此时minFreq必等于1。
                    }
                }

                Node newNode = new Node(key, value);
                nodeMap.put(newNode.key, newNode);
                addToFrequencyList(newNode);
                minFreq = 1;
            }
        } else {
            node.value = value;
            increasingFrequency(node);
        }
    }

    /**
     * 原子性做得干净一点，就是只加到list
     * @param node
     */
    private void addToFrequencyList(Node node) {
        int freq = node.frequency;
        if (!freqMap.containsKey(freq)) {
            freqMap.put(freq, new NodeList());
        }
        NodeList nodeList = freqMap.get(freq);
        nodeList.addToHead(node);
    }

    // TODO: 【错误1】错误一：升频时删除了桶内 LRU，不是当前节点
    // TODO: 【错误2】错误2：非最小频次桶被清空时，也执行了 minFreq++
    private void increasingFrequency(Node node) {
        int oldFreq = node.frequency;
        int newFreq = ++node.frequency;
        NodeList oldNodeList = freqMap.get(oldFreq);
        // 【错误1】 Node lru = oldNodeList.removeLru();
        //  修正：删除当前访问节点，而不是删除LRU
        oldNodeList.removeNode(node);
        // 【错误2】旧桶不一定对应着 最小桶！！！
        // if (oldNodeList.isEmpty()) {
        //    minFreq++;
        //    freqMap.remove(oldFreq);
        // }
        //  修正：只有最小频次桶消失时才更新

        if (oldNodeList.isEmpty()) {
            freqMap.remove(oldFreq);
            if (oldFreq == minFreq) {   // 检查消失桶，是否是最小频次桶。只有是最小频次桶时，才minFreq++；
                minFreq++;
            }
        }

        if (!freqMap.containsKey(newFreq)) {
            freqMap.put(newFreq, new NodeList());
        }
        NodeList newNodeList = freqMap.get(newFreq);
        newNodeList.addToHead(node);
    }

    public static class Node {
        int key;
        int value;
        int frequency = 1;
        Node last;
        Node next;

        public Node(int _key, int _value) {
            key = _key;
            value = _value;
        }
    }

    public static class NodeList {
        Node head = new Node(0, 0);
        Node tail = new Node(0, 0);

        public NodeList() {
            head.next = tail;
            tail.last = head;
        }

        public void addToHead(Node node) {
            Node first = head.next;

            head.next = node;
            node.next = first;
            first.last = node;
            node.last = head;
        }

        public Node removeNode(Node node) {
            node.last.next = node.next;
            node.next.last = node.last;
            node.last = null;
            node.next = null;
            return node;
        }

        public Node removeLru() {
            Node deleted = tail.last;
            removeNode(deleted);
            return deleted;
        }

        public boolean isEmpty() {
            return head.next == tail;
        }

    }
}
/*
原始错误代码，仅用于复盘，不参与编译：

class LFUCache {

    int capacity;

    int size;

    int minFreq;

    HashMap<Integer, Node> nodeMap;

    HashMap<Integer, NodeList> freqMap;

    public LFUCache(int capacity) {
        // TODO: 【致命错误-参数遮蔽】左右两侧都是形参capacity，成员变量仍为默认值0。
        // capacity = capacity;
        this.capacity = capacity;
        nodeMap = new HashMap<>();
        freqMap = new HashMap<>();
    }

    public int get(int key) {
        Node node = nodeMap.get(key);
        if (node == null) {
            return -1;
        } else {
            increasingFreq(node);
        }
        return node.value;
    }

    public void put(int key, int value) {
        // TODO: 【边界遗漏】没有单独处理capacity == 0。采用正确淘汰顺序后，必须避免从空缓存淘汰。
        if (capacity == 0) {
            return;
        }

        Node node = nodeMap.get(key);
        if (node != null) {
            node.value = value;
            increasingFreq(node);
        } else {
            // TODO: 【题意理解错误-致命】缓存满时，题目要求先淘汰已有LFU节点，再插入新节点。
            // 原代码先插入新节点，导致新节点错误参与本次淘汰。
            // 错误顺序：
            // Node newNode = new Node(key, value);
            // nodeMap.put(key, newNode);
            // minFreq = 1;
            // addToFrequenceList(node);
            // size++;
            // if (size > capacity) {
            //     removeLFU();
            // }
            if (size == capacity) {
                removeLFU();
            }

            Node newNode = new Node(key, value);
            nodeMap.put(key, newNode);

            // TODO: 【致命错误-变量写混】node来自nodeMap.get(key)，当前分支中必定为null。
            // 这里传入null会在addToFrequenceList内部访问node.freq时触发NullPointerException。
            // addToFrequenceList(node);
            addToFrequenceList(newNode);
            size++;

            // TODO: 【题意理解错误-错误顺序】此时minFreq已经被重置为1。
            // 如果旧缓存最小频次大于1，就会淘汰刚插入的频次1新节点，而不是淘汰已有节点。
            // if (size > capacity) {
            //     removeLFU();
            // }
            minFreq = 1;
        }
    }

    private Node removeLFU() {
        NodeList nodeList = freqMap.get(minFreq);
        Node lru = nodeList.removeLRU();
        if (nodeList.isEmpty()) {
            freqMap.remove(minFreq);
        }

        // TODO: 【编译错误-变量不存在】当前方法中没有名为node的变量。
        // nodeMap.remove(node.key);
        nodeMap.remove(lru.key);
        size--;

        // TODO: 【编译错误-变量名写错】当前变量名是lru，不存在lfu。
        // return lfu;
        return lru;
    }

    // TODO: 【编译错误-关键字拼写】privtae不是Java关键字，方法无法通过编译。
    // privtae void increasingFreq(Node node) {
    private void increasingFreq(Node node) {
        int oldFreq = node.freq;
        int newFreq = ++node.freq;
        NodeList oldNodeList = freqMap.get(oldFreq);
        oldNodeList.removeNode(node);
        if (oldNodeList.isEmpty()) {
            freqMap.remove(oldFreq);
            // TODO: 【正确遗漏点】旧频次桶被删除时，必须检查它是不是当前minFreq桶。
            if (minFreq == oldFreq) {
                // TODO: 【认知错误】这里使用minFreq++并没有错。
                // 进入分支已经保证minFreq == oldFreq，而newFreq == oldFreq + 1，二者等价。
                // “minFreq遗漏为1但节点从2升到3”表示minFreq不变量此前已损坏，且不会进入本分支。
                // 错误认知：minFreq++不能使用，必须写成minFreq = newFreq。
                minFreq++;
            }
        }

        addToFrequenceList(node);
    }

    private void addToFrequenceList(Node node) {
        int nodeFreq = node.freq;
        if (!freqMap.containsKey(nodeFreq)) {
            freqMap.put(nodeFreq, new NodeList());
        }
        NodeList nodeList = freqMap.get(nodeFreq);
        nodeList.addToHead(node);
    }

    public static class Node {
        int key;
        int value;
        int freq = 1;
        Node next;
        Node last;

        public Node(int _key, int _value) {
            key = _key;
            value = _value;
        }
    }

    public static class NodeList {
        Node head;
        Node tail;

        public NodeList() {
            head = new Node(0, 0);
            tail = new Node(0, 0);
            head.next = tail;
            tail.last = head;
        }

        // TODO: 【编译错误-返回值遗漏】方法声明返回Node，但执行结束时没有return语句。
        // private Node removeNode(Node node) {
        private void removeNode(Node node) {
            Node lastNode = node.last;
            Node nextNode = node.next;
            lastNode.next = nextNode;
            nextNode.last = lastNode;
        }

        private void addToHead(Node node) {
            Node first = head.next;
            head.next = node;
            node.last = head;
            node.next = first;
            first.last = node;
        }

        private Node removeLRU() {
            Node lru = tail.last;
            removeNode(lru);
            return lru;
        }

        // NOTE: 【冗余方法】当前LFU流程不会在同一个频次桶内直接移动节点；访问节点时会跨频次桶。
        // 该方法不会造成错误，但当前实现中没有调用价值。
        private void moveToHead(Node node) {
            removeNode(node);
            addToHead(node);
        }

        private boolean isEmpty() {
            return head.next == tail;
        }
    }
}
*/
