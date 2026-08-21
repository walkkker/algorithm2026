package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * TODO：什么是Dijkstra算法？最短路径树
 *   在【边权值非负】的图中，从一个指定的源节点出发，计算它到图中所有其他节点的最短路径（距离）。
 *   算法核心思想 ：它采用贪心策略，通过不断选择当前距离源点最近的节点来逐步扩展最短路径树。简单来说，就是“先找最近的，再找次近的”，以此类推。
 * <p>
 * 因为dijkstra是通过锁住点，然后解锁边的方式计算。所以每个边（实际上是边的toNode）都会参与计算，记边的数量为E。
 * 假设节点的数量记为V，边的数量记为E。 所以无论何种方法，时间复杂度都是 O(E * ${每解锁一个边的代价})
 * 好，所以，加强堆的最优解 与 系统优先级队列 的优化主要在 ${每次解锁边的代价} 和 ${弹出堆顶}上。
 * 同一个节点，进入堆一次，出堆一次。 （加强堆下，）
 * <p>
 * 加强堆通过直接修改对象Node的值，使得 heap.size<=V
 * 而 priorityQueue由于不支持修改内部元素的值，需要每次把新解锁的边的ToNode加入优先级队列，所以导致 重复进入队列，因此 heap.size <= E
 * 因此最终的时间复杂度如下：
 * <p>
 * <p>
 * 方法1最优解：（写完普通版讨论时，interviewer强制要求时）
 * 使用 加强堆 实现 Dijkestra算法
 * 时间复杂度 O(E * logV) 》 V为节点数量， E为边的总数量
 * 避免节点重复进入堆
 * <p>
 * 方法2：(面试写的版本)
 * 仅使用 PriorityQueue实现，通过检查 HashMap.containsKey 判断 节点是否已经被锁住（map.containsKey代表1）被访问过了 2）hashmap就是存储已经确定的节点距离的）
 * 时间复杂度 为 O(E * log E) -》 E为边的总数量
 */

/**
 * 实现细节：
 * <p>
 * 堆： 每次弹出最小distance的node，锁住该节点，然后解锁边，然后看解锁后新的节点距离是否能够更新更小。 循环如此
 * <p>
 * 普通版本实现： 使用记录锁定节点距离的HashMap.containsKey 来协助 判断当前heap.pop节点是否 该被忽略
 * <p>
 * 锁住节点的时机： 注意，是 pQ 弹出时，才锁住。 因为此时是 全局可用的最小。 （注意：此时要检查 hashmap.containsKey(node)，yes则忽略该节点）
 *
 * 本着功利心态去记住：加入HashMap的节点，（1）不会再更新，因为已经确认最小距离了 （2）(optional) 解锁边的时候，toNode 在HashMap的可以剪枝。因为这个toNode已经确定minDistance了，你还把它加到堆里干嘛呢？即便等弹出，也是会被1)的检查忽略。
 */
public class Dijkstra {

    public static Map<Node, Integer> dijkstraWithPriorityQueue(Node start) {
        Map<Node, Integer> ans = new HashMap<>();
        // TODO: 【错误】特别注意，这里的泛型是 NodeRecord 不是 Node！！
        // TODO: 【超级错误-超级要注意！！！】自定义类型的 PriorityQueue 一定要 传递比较器！！！
        PriorityQueue<NodeRecord> pQ = new PriorityQueue<>((o1, o2) -> o1.distance - o2.distance);
        pQ.add(new NodeRecord(start, 0));
        while (!pQ.isEmpty()) {
            NodeRecord cur = pQ.poll();
            Node curNode = cur.node;
            int curDistance = cur.distance;

            // TODO：【错误-切记】弹出的NodeRecord 可能是 无用的，需要跳过。 因为普通版本下允许节点重复进入队列，所以heap内会存在 同一个Node的多条NodeRecord。 当一个Node被锁住后，heap里面可能还会留有它的记录。
            if (ans.containsKey(curNode)) {
                continue;
            } else {
                // 没有被选中过，那么 现在选中你， 更新map， 更新selected set
                ans.put(curNode, curDistance);

                // 解锁新的边，更新对应节点的距离
                for (Edge edge : curNode.edges) {
                    int weight = edge.weight;
                    Node toNode = edge.to;
                    if (!ans.containsKey(toNode)) {  // 这句可有可没有，剪枝作用。 语义：没有被锁住，那么就 构建对象 -> 入堆
                        pQ.add(new NodeRecord(toNode, curDistance + weight));
                    }
                }
            }
        }
        return ans;
    }

    // TODO: 普通版 千万别忘了 这个自定义类。 跟堆相关的很多题，都会涉及自定义类，需要你自己把题目给的 变量 组装起来。
    //  这样才能让堆在按照比较规则（比如 属性height） 弹出时，**同时**能够获取到所属的原始对象（比如 human）
    public static class NodeRecord {
        Node node;
        int distance;

        public NodeRecord(Node _node, int _distance) {
            node = _node;
            distance = _distance;
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {{2, 3, 5},
                {1, 7, 9},
                {5, 4, 6},
                {3, 7, 2},
                {3, 2, 3},
                {9, 5, 1},
                {2, 5, 7},
                {9, 3, 4}};
        Graph graph = GraphGenerator.createGraph(matrix);
        int size = 9;
        Node head = graph.nodes.get(2); // 头节点
        Map<Node, Integer> ans1 = dijkstraWithPriorityQueue(head);
//        HashMap<Node, Integer> ans2 = dijkstra2(head, size);
//        HashMap<Node, Integer> ans3 = dijkstra3(head);
//        HashMap<Node, Integer> ans4 = dijkstraNew(head);

        printMap(ans1);
        System.out.println();
//        printMap(ans2);
//        System.out.println();
//        printMap(ans3);
//        System.out.println();
//        printMap(ans4);
    }

    public static void printMap(Map<Node, Integer> map) {
        for (Map.Entry<Node, Integer> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey().value + ", Value: " + entry.getValue());
        }
    }


}



