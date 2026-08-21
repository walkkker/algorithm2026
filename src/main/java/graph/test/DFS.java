package graph.test;

import graph.test.MyGraph.*;

import java.util.HashSet;
import java.util.Stack;


/**
 * solution: stack + set
 *
 * 为什么queue bfs是弹出时遍历：因为一个节点只会弹出一次
 * 为什么stack dfs是加入时遍历：因为一个节点在stack会反复弹出多次，弹出的目的是dfs另一个孩子分支
 *     1. 因此代码细节多一些，首先是 遍历有效孩子时再压栈，不是弹出时遍历
 *     2. 每次成功遍历后，先压父，再压子 （相当于保存当前dfs路径）
 *     3. 成功遍历->压栈后，要break；这样才能进入下一轮while 弹出最下面的节点，进一步探索dfs。   没有break，就横向遍历一级孩子了，不是dfs了。
 */
public class DFS {

    public static void dfs(Node start) {
        Stack<Node> stack = new Stack<>();
        HashSet<Node> set = new HashSet<>();
        System.out.println(start.value);
        stack.push(start);
        set.add(start);
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            for (Node next : cur.nexts) {
                if (!set.contains(next)) {
                    System.out.println(next.value);
                    stack.push(cur);   // TODO: 【重点1】 对于未遍历过的next，再次压栈，注意先压父，再压子
                    stack.push(next);
                    set.add(next);
                    break;    // TODO: 【重点2】因为是dfs，所以成功遍历一个孩子后，要退出 for(Node next : nexts)循环。 这样才能进入下一次的while，从而进一步深入孩子分支
                }
            }
        }



    }

}
