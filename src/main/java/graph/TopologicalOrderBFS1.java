package graph;

import java.util.*;

/**
 * 给定一个有向图，图节点的拓扑排序定义如下:
 * <p>
 * 对于图中的每一条有向边 A -> B , 在拓扑排序中A一定在B之前.
 * 拓扑排序中的第一个节点可以是图中的任何一个没有其他节点指向它的节点.
 * <p>
 * <p>
 * BFS就是 队列Queue
 * https://www.lintcode.com/problem/127/
 *
 *      * 正确解答:
 *      *  1. 核心围绕知识点：入度；入参为 点集graph；输出为List拓扑序
 *      * 实现步骤：
 *      *  1. 构建inMap：
 *      *      a. 一定要先初始化所有的点 inMap(node, 0)
 *      *      b. 再基于node.nexts 给 next节点的入度+1
 *      *      c. 目的：这样我们就能够筛选出入度为0的节点
 *      *  2. 构建BFS Queue。将入度为0的节点加入queue
 *      *  3. 开始BFS:
 *      *      a. 每次弹出的节点为当前拓扑序，加入list-ans
 *      *      b. 将该节点的nexts 入度全部-1；
 *      *      c. 检查是否next 入度变为0
 *      *      d. 如果变为0，则满足拓扑序，加入queue
 *      *  4. 完成BFS时，则ans存储的就是拓扑序。
 *      *  5. 注意：如果有环，则ans.size < graph.size。 说明该图不是一个完整的拓扑结构
 */
public class TopologicalOrderBFS1 {

    /**
     * Definition for Directed graph.
     **/
    class DirectedGraphNode {
        int label;
        List<DirectedGraphNode> neighbors;

        DirectedGraphNode(int x) {
            label = x;
            neighbors = new ArrayList<DirectedGraphNode>();
        }
    }


    public class Solution {
        /**
         * @param graph: A list of Directed graph node
         * @return: Any topological order for the given graph.
         */
        public ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
            // write your code here
            HashMap<DirectedGraphNode, Integer> inMap = new HashMap<>();
            // S1:必须先初始化所有节点 -> 因为有些节点入度为0 -> 不能通过检查是否存在 再添加（那样只会添加有入度的）
            for (DirectedGraphNode node : graph) {
                inMap.put(node, 0);
            }

            // S2: 统计所有节点的入度信息
            for (DirectedGraphNode node : graph) {
                for (DirectedGraphNode next : node.neighbors) {
                    inMap.put(next, inMap.get(next) + 1);
                }
            }

            // S3: 初始化zeroQueue 以及 List ans。 zeroQueue放置入度为0的节点，每次弹出时使用ans收集 && 将node neighbor的入度-1
            Queue<DirectedGraphNode> queue = new LinkedList<>();
            ArrayList<DirectedGraphNode> ans = new ArrayList<>();

            // S4: BFS初始话 -> 统计初始入度为0的节点 -> 这些节点就是 拓扑序的起点
            for (DirectedGraphNode node : inMap.keySet()) {
                if (inMap.get(node) == 0) {
                    queue.add(node);
                }
            }

            // S5: 开始真正的BFS（队列实现）
            // zeroQueue 弹出节点即为 拓扑序的顺序。 parent弹出，将parent.neighbor indegree--，由此可以BFS获得每一层入度为0的节点。即得到拓扑序
            while (!queue.isEmpty()) {
                DirectedGraphNode node = queue.poll();
                ans.add(node);
                for (DirectedGraphNode next : node.neighbors) {
                    inMap.put(next, inMap.get(next) - 1);
                    if (inMap.get(next) == 0) {
                        queue.add(next);
                    }
                }
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        // 下面的代码是 说明Arrays.fill() 的坑的
        List<Integer>[] graphList = new List[10 + 1];
        Arrays.fill(graphList, new ArrayList<>());
        graphList[0].add(1);
        System.out.println(Arrays.toString(graphList));
        // [[1], [1], [1], [1], [1], [1], [1], [1], [1], [1], [1]]
    }

    /**
     * 正确解答:
     *  1. 核心围绕知识点：入度；入参为 点集graph；输出为List拓扑序
     * 实现步骤：
     *  1. 构建inMap：
     *      a. 一定要先初始化所有的点 inMap(node, 0)
     *      b. 再基于node.nexts 给 next节点的入度+1
     *      c. 目的：这样我们就能够筛选出入度为0的节点
     *  2. 构建BFS Queue。将入度为0的节点加入queue
     *  3. 开始BFS:
     *      a. 每次弹出的节点为当前拓扑序，加入list-ans
     *      b. 将该节点的nexts 入度全部-1；
     *      c. 检查是否next 入度变为0
     *      d. 如果变为0，则满足拓扑序，加入queue
     *  4. 完成BFS时，则ans存储的就是拓扑序。
     *  5. 注意：如果有环，则ans.size < graph.size。 说明该图不是一个完整的拓扑结构
     *
     */

    public ArrayList<DirectedGraphNode> topSortTest(ArrayList<DirectedGraphNode> graph) {
        // 核心思路为 入度
        HashMap<DirectedGraphNode, Integer> inMap = new HashMap<>();
        for (DirectedGraphNode node : graph) {
            inMap.put(node, 0);
        }
        for (DirectedGraphNode node : graph) {
            for (DirectedGraphNode next : node.neighbors) {
                inMap.put(next, inMap.get(next) + 1);
            }
        }

        Queue<DirectedGraphNode> zeroQueue = new LinkedList<>();
        for (DirectedGraphNode node : inMap.keySet()) {
            if (inMap.get(node) == 0) {
                zeroQueue.add(node);
            }
        }
        ArrayList<DirectedGraphNode> ans = new ArrayList<>();
        while (!zeroQueue.isEmpty()) {
            DirectedGraphNode node = zeroQueue.poll();
            ans.add(node);
            for (DirectedGraphNode next : node.neighbors) {
                inMap.put(next, inMap.get(next) - 1);
                if (inMap.get(next) == 0) {
                    zeroQueue.add(next);
                }
            }
        }

        return ans;
    }

}
