package graph.test;

import java.util.Arrays;

/**
 * 适合边集
 * solution: UnionFind + sort
 */
public class Kruskal {

    // (from, to, weight)
    public static int kruskal(int[][] edges, int n) {
        Arrays.sort(edges, (a, b) -> a[2] - b[2]);   // TODO: 贪心
        UnionFind uf = new UnionFind(n);
        int ans = 0;
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            int weight = edge[2];
            if (!uf.isSameSet(from, to)) {
                ans += weight;
                uf.union(from, to);
            }
            // optional optimization
            if (uf.sets == 1) {
                break;
            }
        }
        return ans;
    }


    public static class UnionFind {

        int[] father;
        int[] size;
        int[] stack;
        int sets;

        public UnionFind(int n) {
            father = new int[n];
            size = new int[n];
            stack = new int[n];
            sets = n;
            for (int i = 0; i < n; i++) {
                father[i] = i;
                size[i] = 1;
            }
        }

        private int find(int cur) {
            int i = 0;
            while (cur != father[cur]) {
                stack[i++] = cur;
                cur = father[cur];
            }

            while (i > 0) {
                father[stack[--i]] = cur;
            }
            return cur;
        }

        public void union(int a, int b) {
            int fa = find(a);
            int fb = find(b);
            if (fa != fb) {
                if (size[fa] > size[fb]) {
                    father[fb] = fa;
                    size[fa] += size[fb];
                } else {
                    father[fa] = fb;
                    size[fb] += size[fa];
                }
                sets--;
            }
        }

        public int sets() {
            return sets;
        }

        public boolean isSameSet(int a, int b) {
            int fa = find(a);
            int fb = find(b);
            return fa == fb;
        }
    }
}


