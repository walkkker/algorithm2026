package graph.test;

import graph.test.MyGraph.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


/**
 * solution: queue（bfs） + set(deduplicate)
 */
public class BFS {

    public static void bfs(Node start) {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> set = new HashSet<>();

        queue.add(start);
        set.add(start);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            System.out.println(cur);
            for (Node toNode : cur.nexts) {
                if (!set.contains(toNode)) {
                    queue.add(toNode);
                    set.add(toNode);
                }
            }
        }
    }




}
