package frequence.cache;


import java.util.HashMap;
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
 【nodeMap, freqMap, capacity, minFreq.  通过minFreq标记最小频次桶，避免遍历行为，实现O（1）查找最小频次桶】。
 1. 核心错误是 长代码下，容易思路乱，前后变量不一。 =》需要多练本题
 2. minFreq定位两个场景： (1)increasingFreq (2) 新增节点
 3. increasingFreq中，何时才新增 minFreq？ 需要同时满足 【旧桶删除&&旧桶是最小桶】
 4. 本题lfu添加元素时的删除规则： 先清除lfu，再添加。  一定要注意这个顺序。如果反过来，那么可能你每次新添加元素后，它就会被删除（freq=1只有这一个元素）。 这是完全不对的。

 */
class Q460_LFUCacheSecondWrong {

    class LFUCache {

        HashMap<Integer, Node> nodeMap ;
        HashMap<Integer, NodeList> freqMap ;
        int minFreq;
        int capacity;


        public LFUCache(int capacity) {
            nodeMap = new HashMap<>();
            freqMap = new HashMap<>();
            minFreq = 1;
            this.capacity = capacity;
        }

        public int get(int key) {
            Node node = nodeMap.get(key);
            if (node == null) {
                return -1;
            } else {
                increaseFreq(node);
                return node.value;
            }
        }

        public void put(int key, int value) {
            if (capacity == 0) {
                return;
            } else {
                Node node = nodeMap.get(key);
                if (node != null) {
                    node.value = value;
                    increaseFreq(node);
                } else {  // 删旧 加新  // TODO: 【错误】删旧 你要同时删除nodeMap 取消注册
                    if (nodeMap.size() == capacity) {   // 这里的顺序要注意一下：1. 先找最小桶 2. 最小桶删除lru，得到lfu 3.此时先不删除nodeMap，先检查最小桶是否为空，如果为空，删除最小桶 4. nodeMap删除lfu.key
                        NodeList minNodeList = freqMap.get(minFreq);
                        Node lfu = minNodeList.removeLru();
                        if (minNodeList.isEmpty()) {
                            freqMap.remove(minFreq);
                        }
                        // TODO: 【错误-见下行】map.remove(key) 才是对的
                        // nodeMap.remove(lfu);
                        nodeMap.remove(lfu.key);
                    }
                    Node newNode = new Node(key, value);
                    // TODO: 【错误】一定不要忘了，先把新节点注册进 nodeMap！！！  先注册节点，再对节点分配桶。
                    nodeMap.put(key, newNode);
                    addToNodeList(newNode);
                    minFreq = 1;
                }
            }
        }


        private void increaseFreq(Node node) {
            int oldFreq = node.freq;
            node.freq++;

            NodeList oldNodeList = freqMap.get(oldFreq);
            oldNodeList.removeNode(node);
            if (oldNodeList.isEmpty()) {
                freqMap.remove(oldFreq);
                if (oldFreq == minFreq) {
                    minFreq++;
                }
            }

            addToNodeList(node);
        }

        private void addToNodeList(Node node) {
            int freq = node.freq;
            if (!freqMap.containsKey(freq)) {
                freqMap.put(freq, new NodeList());
            }
            NodeList nodeList = freqMap.get(freq);
            nodeList.addToHead(node);
        }

        public class Node {
            int key;
            int value;
            int freq;
            Node next;
            Node last;

            public Node(int _k, int _v) {
                key = _k;
                value = _v;
                freq = 1;
            }
        }

        public class NodeList {
            Node head;
            Node tail;
            public NodeList() {
                head = new Node(0,0);
                tail = new Node(0,0);
                // TODO: 【错误】初始化时，要成链！！！   head tail空哨兵，这样 添加节点和删除节点，都是统一处理中间节点，不用处理边界。
                head.next = tail;
                tail.last = head;
            }

            public Node removeNode(Node node) {
                node.last.next = node.next;
                node.next.last = node.last;
                return node;
            }

            public void addToHead(Node node) {
                Node first = head.next;
                head.next = node;
                node.next = first;
                first.last = node;
                node.last = head;
            }

            public Node removeLru() {
                Node prev = tail.last;
                return removeNode(prev);
            }

            public boolean isEmpty() {
                return head.next == tail;
            }
        }
    }

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
}
