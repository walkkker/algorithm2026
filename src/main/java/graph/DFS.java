package graph;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * (1) 推荐：非递归
 * - 栈（深度就是用栈） + HashSet (去重->防环)
 * - 第一次入栈时 遍历该节点
 * - 每次压栈，先压父，再压子（当前节点）。 形成深度路径。
 *
 * <p>
 * (2) 不推荐：递归
 * - 本质是用系统栈替代用户态的栈， 依然需要使用HashSet防环
 */
public class DFS {

    public static void dfsWithUnRecursive(Node start) {
        if (start == null) {
            return;
        }

        Stack<Node> stack = new Stack<>();
        Set<Node> set = new HashSet<>();
        System.out.print(start.value + " ");
        stack.push(start);
        set.add(start);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            for (Node next : node.nexts) {
                if (!set.contains(next)) {
                    System.out.print(next.value + " ");
                    set.add(next);
                    stack.push(node);
                    stack.push(next);
                    // TODO： 这个break千万别忘了。 必须这样，才能退出横向压栈，转而变成纵向压栈
                    break;
                }
            }
        }
    }


    public static void dfsWithRecursive(Node start) {
        if (start == null) {
            return;
        }
        Set<Node> set = new HashSet<>();
        process(start, set);
    }

    // 语义：深度优先 打印cur
    public static void process(Node cur, Set<Node> set) {
        // TODO： 有一个经验：对于 孩子为list的 多叉树/图 结构，自带base case -> 也就是 list.size==0之时
        //   其实base case就是 当前递归节点 为递归树的叶子节点 -> 【本质就是 本次执行体内不会再调用递归函数】
        if (!set.contains(cur)) {   // 我觉得这个是神来之笔
            set.add(cur);
            System.out.print(cur.value + " ");
            for (Node next : cur.nexts) {
                process(next, set);    // 先打印 第一个孩子树， 再打印第二个孩子数....  实现深度优先遍历
            }
        }
    }


    public static void main(String[] args) {
        int[][] matrix = {{2,3,5},
                {1,7,9},
                {5,4,6},
                {3,7,2},
                {3,2,3},
                {9,5,1},
                {2,5,7},
                {9,3,4}};
        Graph graph = GraphGenerator.createGraph(matrix);
        Node head = graph.nodes.get(2);
        dfsWithRecursive(head);
        System.out.println();
        dfsWithUnRecursive(head);
    }


}
