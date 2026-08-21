package graph;

import java.util.HashSet;
import java.util.List;
import java.util.*;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 构建最小生成树
 * <p>
 * 对于邻接矩阵求Prim: https://leetcode.cn/problems/min-cost-to-connect-all-points/description/ , 邻接矩阵->完全图->朴素Prim
 * (1) 朴素prim + 邻接矩阵
 * (2) points[][] 转化为 graph[][]
 * (3) 由于graph[i][j] 计算复杂度为 O(1) 。 因此可以优化空间复杂度，不缓存graph[][]，而是封装为计算函数。每次计算调用函数。   空间复杂度 O(N^2) -> O(N)
 * (4) 写法上，先写出 适配器版本的。 再进一步优化为最优解。
 * <p>
 * 所以看起来，Prim堆优化版本适合 稀疏图（边数远小于N^2）-》对应邻接表  O(E log E)
 * 朴素Prim适合 稠密图 -》对应邻接矩阵（完全图）  O(N^2) 而此时 堆优化为O(N^2 * logN) 因为E=N*(N-1)
 */
public class Prim {
    // 解锁节点：selectedNodes.add(node)
    // 解锁相邻边: toNode.edges 进 优先队列
    // 图版本
    public static Set<Edge> primMST(Graph graph) {
        // 三容器：1)用来排列Edge的优先队列; 2)记录结果的Set; 3)用来记录解锁的点的 集合
        // TODO: PriorityQueue<Edge> 一定要传比较器啊！！！
        // 解锁的边进入小根堆
        PriorityQueue<Edge> pQ = new PriorityQueue<>((o1, o2) -> o1.weight - o2.weight);
        // 哪些点被解锁出来了
        Set<Node> selectedNodes = new HashSet<>();
        // Set记录生成MST的边
        Set<Edge> ans = new HashSet<>();

        // 为什么用for呢？ 是为了防森林。 如果保证图是连通的，直接 下面加一个break 或者 只写 for循环内部的执行体也可以
        for (Node node : graph.nodes.values()) {
            if (!selectedNodes.contains(node)) {
                // S1： 为while做初始化： 解锁start节点 + 解锁相邻边
                selectedNodes.add(node);
                for (Edge edge : node.edges) {
                    pQ.add(edge);
                }
                while (!pQ.isEmpty()) {
                    Edge edge = pQ.poll();
                    Node toNode = edge.to;
                    // 注意，此时边不一定可用，要做检查     了解prim朴素版就知道，edge 是跟 edge.to 挂在一起的
                    if (selectedNodes.contains(toNode)) {   // 边.toNode 已经被解锁过了， 这个边就舍弃掉了
                        continue;
                    } else {
                        // 三件事：结果集加边，解锁node，新边进堆 （加边，加点，进队列）
                        ans.add(edge);
                        selectedNodes.add(toNode);
                        for (Edge nextEdge : toNode.edges) {
                            pQ.add(nextEdge);
                        }
                    }
                }
            }
            // 如果给到的图，保证只有一个 连通树（没有森林），那么可以在下面加一个break直接退出。
            // break
        }
        return ans;
    }


    // 朴素版：Prim朴素版  直接基于【邻接矩阵】
    // 入参: int[i][j] graph 代表 nodei到nodej 的距离
    public static int prim(int[][] graph) {
        int sum = 0;
        int n = graph.length;
        int[] distance = new int[n]; // distance[i] 记录的是 到i节点（不记录源节点是哪个）的 最小距离。
        boolean[] visit = new boolean[n];
        // 将index=0作为start节点
        visit[0] = true;
        for (int j = 0; j < n; j++) {
            distance[j] = graph[0][j];
        }


        // n个节点，则MST要选n-1个边
        for (int k = 1; k <= n - 1; k++) {
            // S1: 相当于优先队列 -> 找到当前最小边
            int minValue = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < n; j++) {
                if (!visit[j] && distance[j] < minValue) {
                    minValue = distance[j];
                    minIndex = j;
                }
            }

            // TODO: 不排除森林的情况，此时可能没有到达n-1次时，就没有有效的最小值了
            if (minIndex == -1) {
                return sum;
            }

            // S2: 锁边 + 锁toNode
            sum += minValue;
            visit[minIndex] = true;

            // S3: toNode相邻边 添加 优先队列
            for (int j = 0; j < n; j++) {
                // 边（toNode）没有被解锁过，这些边才会考虑。 || toNode 到 j的距离 < 已经统计过的某个节点到j的距离。  那么说明，这是 到j距离的最小值
                // 特别注意下面的 if条件，一定要排除 !selectedNode[j]，不然会出错。
                // 因为他更新的方向是 从 【解锁的点】 到 【未解锁的点】 的边权重。  如果你漏掉这个条件，就会变成【后解锁的点】到【先解锁的点】的权重也更新上去，边的方向错了！！！可能会把已经选好的边覆盖掉。
                if (!visit[j] && distance[j] > graph[minIndex][j]) {
                    distance[j] = graph[minIndex][j];
                }
            }

        }
        return sum;
    }

    // TODO: 这个也是 邻接矩阵的输入。 看起来是堆优化版本，但是 如果输入是邻接矩阵的话，时间复杂度是 O(E * logE)，对于临界矩阵 边数量为n的情况，就是 O(N^2 * logN)
    // 注意：E的最大数量就是 N*(N-1) <= N^2。 所以，如果不是邻接矩阵，尤其是对于稀疏矩阵而言，E小(E <<< N^2)，那么prim堆优化的优势就出来了，O(E log E)
    public static int primWithHeap(int[][] graph) {
        int sum = 0;
        int n = graph.length;
        // int[0]=weight int[1]=toNode
        PriorityQueue<int[]> pQ = new PriorityQueue<>();
        boolean[] visit = new boolean[n];
        // 将index=0作为start节点
        visit[0] = true;
        for (int j = 1; j < n; j++) {
            if (graph[0][j] != Integer.MAX_VALUE) {
                pQ.add(new int[]{graph[0][j], j});
            }
        }

        while (!pQ.isEmpty()) {
            int[] cur = pQ.poll();
            int weight = cur[0];
            int toNode = cur[1];

            if (visit[toNode]) {
                continue;
            }

            sum += weight;  // 加边
            visit[toNode] = true; // 加点
            // 扩展新边
            for (int j = 0; j < n; j++) {
                if (graph[toNode][j] != Integer.MAX_VALUE) {
                    pQ.add(new int[]{graph[toNode][j], j});
                }
            }

        }
        return sum;
    }

    // TODO: 邻接表法 Prim堆优化  edge直接用int[]表示，原生支持
    public static int primWithGraphList(List<int[]>[] graph) {
        if (graph.length < 1) {
            return 0;
        }

        int n = graph.length;
        // 边的 representation是什么？ int[] len=2 (toNode, weight)，所以泛型如下
        PriorityQueue<int[]> pQ = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);
        boolean[] visited = new boolean[n];
        int ans = 0;
        // TODO: 注意：因为节点范围[1, n]，所以 首元素不能去index=0，需要取index=1
        visited[1] = true;
        for (int[] edge : graph[1]) {
            pQ.add(edge);
        }
        while (!pQ.isEmpty()) {
            int[] edge = pQ.poll();
            int toNode = edge[0], weight = edge[1];

            if (visited[toNode]) {
                continue;
            } else {
                // 选边， 选点， 再加新边  =》 如此循环
                ans += weight;
                visited[toNode] = true;
                for (int[] nextEdge : graph[toNode]) {
                    pQ.add(nextEdge);
                }
            }
        }
        return ans;
    }


    // TODO: 纯边的输入参数 int[m][3] edges (from ,to ,weight) 以及 总的点数
    // Answer：这种就不太好用prim搞，需要转化成 邻接表。 因为边集没有点边的明确概念，与prim不符，所以需要转化。
    public static int primWithEdges(int[][] edges, int n) {
        // TODO: 下面这种写法会报错 - 数组越界: index 1  size 0.  for循环内只能使用 add（），执行n+1次
//        List<List<int[]>> graphList = new ArrayList<>(n + 1);   // 因为 节点范围[1, n]
//        for (int i = 1; i <= n; i++) {
//            graphList.set(i, new ArrayList<>());
//        }

        // TODO: 这样写是错误的，因为不能包含泛型。因为会进行类型擦除，所以报错。
//        List<int[]>[] graph = new ArrayList<int[]>[n+1];  // 下标范围对应[1,n]
        List<int[]>[] graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }


        for (int[] edge : edges) {
            // TODO: 连续赋值 可以使用下列写法
            int from = edge[0], to = edge[1], weight = edge[2];
            graph[from].add(new int[]{to, weight});
            // TODO: 【大错误】无向图，使用邻接表 表示时，必须添加双向边，不仅需要上一行，必须添加下一行
            graph[to].add(new int[]{from, weight});
        }

        // edges list -> graph list转换结束
        int ans = primWithGraphList(graph);
        return ans;
    }
}
