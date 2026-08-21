package graph;

import java.io.*;
import java.util.*;

/**
 * 给定一个有向图，图节点的拓扑排序定义如下:
 * <p>
 * （1）对于图中的每一条有向边 A -> B , 在拓扑排序中A一定在B之前.
 * （2）拓扑排序中的第一个节点可以是图中的任何一个没有其他节点指向它的节点.
 * <p>
 * <p>
 * BFS就是 队列Queue
 */
public class TopologicalOrderBFS2 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            // TODO: 注意这个in.nval 是double类型，你赋值int时候 要做类型转换。
            //  基本上所有题都是int，所以统一格式是 (int) in.nval
            int n = (int) in.nval;
            in.nextToken();
            int m = (int) in.nval;

            // 下面是m行 (u, v) -> 边列表 -> 转换成 邻接表
            List<Integer>[] graphList = new List[n + 1];
            for (int i = 1; i <= n; i++) {
                graphList[i] = new ArrayList<>();
            }

            for (int t = 1; t <= m; t++) {
                in.nextToken();
                int u = (int) in.nval;
                in.nextToken();
                int v = (int) in.nval;
                graphList[u].add(v);
            }

            // 得到了 邻接表（无权重值） -> 开始 拓扑序BFS
            // 数组形式就可以
            int[] indegreeMap = new int[n + 1];  // HashMap完全可以
            Queue<Integer> zeroQueue = new LinkedList<>();
            List<Integer> ans = new ArrayList<>();

            // S2: 统计所有节点的入度
            for (int i = 1; i <= n; i++) {
                for (int to : graphList[i]) {
                    indegreeMap[to]++;
                }
            }

            // S3：BFS初始化 -> 统计入度为0的节点 进queue
            for (int i = 1; i <= n; i++) {
                if (indegreeMap[i] == 0) {
                    zeroQueue.add(i);
                }
            }

            // S4：开始真正的BFS
            // 弹出 zeroQueue，收集ans，将next节点入度--，看是否入度为0，yes则将新的next也加入zeroQueue。 等到后续弹出
            while (!zeroQueue.isEmpty()) {
                int index = zeroQueue.poll();
                ans.add(index);
                for (int to : graphList[index]) {
                    indegreeMap[to]--;
                    if (indegreeMap[to] == 0) {
                        zeroQueue.add(to);
                    }
                }
            }

            // TODO：【错误】题目要求：若图存在拓扑序，输出一行n个整数，表示拓扑序。否则输出
            //  −1。 注意：输出的最后一个数后面不要带空格。
            // 如果不满足拓扑序，则 ans.size() ！= n, n为总结点数 （比如有环就不行）
            if (ans.size() != n) {
                out.print(-1);
            } else {
                for (int i = 0; i < ans.size() - 1; i++) {
                    out.print(ans.get(i) + " ");
                }
                out.print(ans.get(ans.size() - 1));
            }
            // TODO: 下面这个flush()一定要调用，不然程序结束都不会 有stdout！！！
            out.flush();
        }
    }
}