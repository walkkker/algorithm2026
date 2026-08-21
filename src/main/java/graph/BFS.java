package graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * 功能：从head出发，进行宽度优先遍历。
 *
 * 实现：Queue（类似二叉树的层级遍历） + HashSet（去重 -> 防环）
 *      (1) queue弹出时，进行该节点遍历
 *      (2) queue.add(node) 与 set.add(node) 总是同时发生的。 因为避免后续相同node再次进入queue
 *
 */
public class BFS {

    public static void bfs(Node start) {
        if (start == null) {
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        Set<Node> set = new HashSet<>();
        queue.add(start);
        set.add(start);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.print(node.value + " ");
            for (Node next : node.nexts) {
                if (!set.contains(next)) {
                    queue.add(next);
                    set.add(next);
                }
            }
        }
    }

    public static void BFS() {

    }
}
