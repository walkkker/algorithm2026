package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 左神：深度deep实现
 * <p>
 * 使用递归函数统计深度deep -> 排序deep
 *
 * TODO: 【错误】你不要搞反了。统计完HashMap<Node, deep>后，排序是 deep的逆序！！！ 不是升序！！！
 *      依照深度来做的话 -> 拓扑序中，deep越大，位置越靠前
 *
 * TODO: 【错误2】在Lintcode上跑，你会发现 -> 超时。 这是因为存在 重复的递归行为 -> 记忆化搜索 （其实我们本来统计hashmap这个结构，就能用来做记忆化搜索）
 */
public class TopologicalOrderDFS2 {

    class DirectedGraphNode {
        int label;
        List<DirectedGraphNode> neighbors;

        DirectedGraphNode(int x) {
            label = x;
            neighbors = new ArrayList<DirectedGraphNode>();
        }
    }


    public static class Info {
        DirectedGraphNode node;
        int deep;

        public Info(DirectedGraphNode _node, int _deep) {
            node = _node;
            deep = _deep;
        }
    }

    public ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
        ArrayList<DirectedGraphNode> ans = new ArrayList<>();
        HashMap<DirectedGraphNode, Integer> deepMap = new HashMap<>();
        for (DirectedGraphNode start : graph) {
            if (!deepMap.containsKey(start)) {   // 这个原因是防止存在 多个并行的入口，所以需要遍历
                process(start, deepMap);
            }
        }
        // TODO: 还有这里的 Map.Entry 的提取 ，以及必须加上 泛型 这些细节都要牢牢记住
        ArrayList<Map.Entry<DirectedGraphNode, Integer>> entries = new ArrayList<>(deepMap.entrySet());
        // TODO: 【错误-理解有问题！！】依照深度来做的话 -> 拓扑序中，deep越大，位置越靠前。 所以要逆序！！！
//        entries.sort((o1, o2) -> o1.getValue() - o2.getValue());
        entries.sort((o1, o2) -> o2.getValue() - o1.getValue());
        for (Map.Entry<DirectedGraphNode, Integer> entry : entries) {
            ans.add(entry.getKey());
        }
        return ans;
    }

    // 因为题目说明必有拓扑序，所以必定无环，所以不使用Set。 当然你也可以使用map.containsKey来防环
    // 你要是有环的话，哪来的 deep深度呢？
    public int process(DirectedGraphNode cur, HashMap<DirectedGraphNode, Integer> deepMap) {
        // TODO: 记忆化搜索，因为存在重复的递归行为（重复取一个子节点的 深度）
        //  不然lintcode 失败 exceeded time limit
        if (deepMap.containsKey(cur)) {
            return deepMap.get(cur);
        }

        if (cur.neighbors.isEmpty()) {
            // TODO: 【错误-base case警示！！！】base case也要把所有操作都做满！！！ hashMap 要添加 node, deep!!!
            deepMap.put(cur, 1);
            return 1;
        }

        int maxDeep = -1;
        for (DirectedGraphNode next : cur.neighbors) {
            int deep = process(next, deepMap);
            maxDeep = Math.max(maxDeep, deep);
        }
        int curDeep = maxDeep + 1;
        deepMap.put(cur, curDeep);
        return curDeep;
    }



}
