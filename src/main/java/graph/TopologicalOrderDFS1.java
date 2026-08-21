package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 这个定义很重要啊！ 没有这个定义的话，不好做DFS，尤其是第一条。 所以可以遍历graphNodes
 * <p>
 * 给定一个有向图，图节点的拓扑排序定义如下:
 * <p>
 * (1) 对于图中的每一条有向边 A -> B , 在拓扑排序中A一定在B之前.
 * (2) 拓扑排序中的第一个节点可以是图中的任何一个没有其他节点指向它的节点.
 *
 *
 * ===》 本文代码，依然基于 基于入度 实现DFS，收集拓扑序。
 */
public class TopologicalOrderDFS1 {


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

        /**
         * 这是我自己的一个版本，有很多的问题。
         *
         * （主要问题在于回溯写法上的很多误区，直接看代码注释）
         */

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

            // S3: DFS，不要Queue了
            ArrayList<DirectedGraphNode> ans = new ArrayList<>();
            ArrayList<DirectedGraphNode> initialList = new ArrayList<>();

            // S4: BFS初始话 -> 统计初始入度为0的节点 -> 这些节点就是 拓扑序的起点
            for (DirectedGraphNode node : inMap.keySet()) {
                if (inMap.get(node) == 0) {
                    initialList.add(node);
                }
            }

            // DFS
            for (DirectedGraphNode start : initialList) {
                f(start, inMap, ans);
            }

            return ans;
        }

        public void f(DirectedGraphNode node, HashMap<DirectedGraphNode, Integer> inMap, List<DirectedGraphNode> ans) {
            // 只有当 curNode indegree 变为0 了，才能减neighbor的入度。    不然不能动  e.g. a(in=3) -> b(1) ，则a.in!=0时，都不能消减b.indegree。
            // 换句话说，跟BFS逻辑很像，只有当in==0，才能假设把curNode移除，此时nextNode.in--
            // TODO: 回溯。 不满足条件时，该节点会直接返回递归，到上一级，实现回溯的剪枝，没有必要往下走了。  而后，在拓扑序的图结构中，一定会有下次的某个parent 将cur.in=0，然后成功递归到该节点执行代码逻辑
            if (inMap.get(node) == 0) {
                ans.add(node);
                // TODO: 【下面是错误的-纠正回溯】会导致多次来到 入度==0的节点。  这不是真正的回溯！！！ 哪能这样随意的分开两步？？ 应该是 一个child更改，递归child，循环 更改另一个child，递归child =》 这才是正确的顺序啊！！！
                //  可以举一个反例：lintcode 图表示 -> 1,2,3,4#2,4#3#4  按照下面错误写法，递归到2时，发现4.in==0，打印4；而后完成递归回到1后， 继续for循环递归到4，再次打印4
                //  而 按照下面正确的回溯写法，是不会发生的。 递归到2时，还是会递归到4，但是此时4.in!=0，直接返回2，继续返回1。 而后for循环 准备递归4，此时 4.in-1=0，递归进4节点，执行if 打印4。 符合拓扑序： 4在 1 和 2 的后面！！！
                // for (DirectedGraphNode next : node.neighbors) {
                //     inMap.put(next, inMap.get(next) - 1);
                // }
                // for (DirectedGraphNode next : node.neighbors) {
                //     f(next, inMap, ans);
                // }
                for (DirectedGraphNode next : node.neighbors) {
                    // TODO：这才是回溯的顺序。    for循环内，每次递归子节点前，只能够针对 该子节点做改动，然后递归该子节点；  整个操作与 其他节点隔离（aka.不动其他节点的任何信息）
                    inMap.put(next, inMap.get(next) - 1);
                    f(next, inMap, ans);
                }
            }
        }
    }


    /**
     *  1. 核心为入度，构建 inMap: 注意一定要分两个阶段：先依据graph初始化全部为0，然后依据next增加入度
     *  2. 根据入度为0的节点构建 List-initialList
     *  3. 开始DFS-本质是回溯.  dfs(node, inMap, ans) ， for(next)时，对next入度-1，检查是否next.in==0，若为0，则dfs下去；否则for循环下一个
     * @param graph
     * @return
     */
    public ArrayList<DirectedGraphNode> topSortTest(ArrayList<DirectedGraphNode> graph) {
        HashMap<DirectedGraphNode, Integer> inMap = new HashMap<>();
        // 必须分两阶段，不然入度为0的 node 没有加入到inMap
        for (DirectedGraphNode node : graph) {
            inMap.put(node, 0);
        }
        for (DirectedGraphNode node : graph) {
            for (DirectedGraphNode next : node.neighbors) {
                inMap.put(next, inMap.get(next) + 1);
            }
        }

        List<DirectedGraphNode> initialList = new ArrayList<>();
        for (DirectedGraphNode node : inMap.keySet()) {
            if (inMap.get(node) == 0) {
                initialList.add(node);
            }
        }

        ArrayList<DirectedGraphNode> ans = new ArrayList<>();

        for (DirectedGraphNode node : initialList) {
            dfs(node, inMap, ans);
        }
        return ans;
    }

    public void dfs(DirectedGraphNode node, HashMap<DirectedGraphNode, Integer> inMap, List<DirectedGraphNode> ans) {
        if (inMap.get(node) == 0) {
            ans.add(node);
            for (DirectedGraphNode next : node.neighbors) {
                inMap.put(next, inMap.get(next) - 1);
                if (inMap.get(next) == 0) {
                    dfs(next, inMap, ans);
                }
            }
        }
    }



}
