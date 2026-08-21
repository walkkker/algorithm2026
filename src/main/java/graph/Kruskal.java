package graph;

import java.io.*;
import java.util.PriorityQueue;

// 并查集 + 堆（建议使用 **排序**  其实后续处理步骤都是一样的）
// https://www.nowcoder.com/questionTerminal/c23eab7bb39748b6b224a8a3afbe396b
public class Kruskal {

    // 按理说 Kruskal适合用邻接表做，但是我下面这个不是邻接表，是一个 边的集合
    // LeetCode 1135. 最低成本连通所有城市：输入是 connections数组，每个元素是 [city1, city2, cost]。
    public static int kruskal(int[][] edges, int n) {
        int ans = 0;
        PriorityQueue<int[]> pQ = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
        // TODO: 不需要boolean visit 。 是否添加过边，使用unionFind来记录
        //      boolean[] visited = new boolean[n];
        UnionFind uf = new UnionFind(n);
        // S1: 把边全部加入优先队列
        for (int[] edge : edges) {    // int[3] (from, to, weight)
            pQ.add(edge);
        }

        while (!pQ.isEmpty()) {
            int[] edge = pQ.poll();
            int from = edge[0];
            int to = edge[1];
            int weight = edge[2];


            // TODO： Kruskal可不是这样的。 因为from也没确定看过没看过
            //            if (visited[to]) {
            //                continue;
            //            } else {
            //
            //            }
            if (uf.isSameSet(from, to)) {
                continue;     // 说明from,to已经有连接路径了，所以不要，要了成环
            } else {
                // 这个边要了
                uf.union(from, to);
                ans += weight;
            }
        }
        return ans;
    }

    public static class UnionFind {
        int[] parent;
        int[] size;
        int[] stack;
        int sets;

        // 假设接收n个元素，对应下标 0 - n-1
        public UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            stack = new int[n];
            sets = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int i) {
            int si = 0;
            while (parent[i] != i) {
                stack[si++] = i;
                i = parent[i];
            }
            int ans = i;
            for (--si; si >= 0; si--) {
                parent[stack[si]] = i;
            }
            return ans;
        }

        public boolean isSameSet(int x, int y) {
            return find(x) == find(y);
        }

        public void union(int x, int y) {
            int fx = find(x);
            int fy = find(y);

            // TODO: 下面这些都写错了！！！ union 在考虑size对比和 小挂大 以及size累加上，都是以祖先节点来的，应该是 fx & fy
//            if (xF != yF) {
//                int xSize = size[x];
//                int ySize = size[y];
//                if (xSize <= ySize) {
//                    parent[x] = y;
//                    size[y] += size[x];
//                } else {   // xSize > ySize
//                    parent[y] = x;
//                    size[x] += size[y];
//                }

            if (fx != fy) {   // 祖先不同，需要合并
                int fxSize = size[fx];
                int fySize = size[fy];
                if (fxSize <= fySize) {
                    parent[fx] = fy;       // 祖先 合并
                    size[fy] += size[fx];  // size更新
                } else {   // fxSize > fySize
                    parent[fy] = fx;
                    size[fx] += size[fy];
                }
            }
            sets--;
        }

        public int sets() {
            return sets;
        }
    }
}


// 注意类名必须为 Main, 不要有任何 package xxx 信息
class NewCoder {
    public static void main(String[] args) throws Exception {
        int n = 0;
        int m = 0;
        int[][] edges = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            n = (int) in.nval;
            in.nextToken();
            m = (int) in.nval;
            edges = new int[m][3];
            for (int i = 0; i < m; i++) {
                in.nextToken();
                edges[i][0] = (int) in.nval;
                in.nextToken();
                edges[i][1] = (int) in.nval;
                in.nextToken();
                edges[i][2] = (int) in.nval;
            }
        }
        int minSum = kruskal(edges, n);
        out.println(minSum);
        out.flush();
    }

    public static int kruskal(int[][] edges, int n) {
        int ans = 0;
        PriorityQueue<int[]> pQ = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
        // TODO: 不需要boolean visit 。 是否添加过边，使用unionFind来记录
        //      boolean[] visited = new boolean[n];
        UnionFind uf = new UnionFind(n);
        // S1: 把边全部加入优先队列
        for (int[] edge : edges) {    // int[3] (from, to, weight)
            pQ.add(edge);
        }

        while (!pQ.isEmpty()) {
            int[] edge = pQ.poll();
            int from = edge[0];
            int to = edge[1];
            int weight = edge[2];


            // TODO： Kruskal可不是这样的。 因为from也没确定看过没看过
            //            if (visited[to]) {
            //                continue;
            //            } else {
            //
            //            }
            if (uf.isSameSet(from, to)) {
                continue;     // 说明from,to已经有连接路径了，所以不要，要了成环
            } else {
                // 这个边要了
                uf.union(from, to);
                ans += weight;
            }
        }
        return ans;
    }

    public static class UnionFind {
        int[] parent;
        int[] size;
        int[] stack;
        int sets;

        // TODO: 这里的初始化也是要注意的。 因为输入范围是 1-n 对应不同节点。 所以我们使用的数组下标范围也变成了 1-n。 而不是0-n-1. 说白了，这个就是与输入一一对应，尽量不要做转换，多用点空间无所谓。
        public UnionFind(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            stack = new int[n + 1];
            sets = n;
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int i) {
            int si = 0;
            while (parent[i] != i) {
                stack[si++] = i;
                i = parent[i];
            }
            int ans = i;
            for (--si; si >= 0; si--) {
                parent[stack[si]] = i;
            }
            return ans;
        }

        public boolean isSameSet(int x, int y) {
            return find(x) == find(y);
        }

        // TODO: 在union方法里面，除了find用到了输入参数x,y之外，都是使用的 fx, fy！！！！
        public void union(int x, int y) {
            int fx = find(x);
            int fy = find(y);

            if (fx != fy) {
                // TODO: 起源也是这里的 变量名写的不好
//                int xSize = size[x];
//                int ySize = size[y];

                // TODO: 【超级错】搞了半天，一直报错，最后通过打印sets==-639发现并查集写错了。对于连通的图而言，kruskal完成后，并查集的sets一定为1，最终只剩一个集合（只剩一片连通区）
                //   看下面超级错！！！经过不断检查，犯晕了，果然就犯错了！！！ 理解原理呀，union合并的时候，是**头对头（祖先对祖先）**的合并！！！
//                if (xSize <= ySize) {
//                    parent[x] = y;
//                    size[y] += size[x];
//                } else {   // xSize > ySize
//                    parent[y] = x;
//                    size[x] += size[y];
//                }
                int fxSize = size[fx];
                int fySize = size[fy];
                if (fxSize <= fySize) {
                    parent[fx] = fy;
                    size[fy] += size[fx];
                } else { // fxSize > fySize
                    parent[fy] = fx;
                    size[fx] += size[fy];
                }

                sets--;
            }
        }

        public int sets() {
            return sets;
        }
    }

}