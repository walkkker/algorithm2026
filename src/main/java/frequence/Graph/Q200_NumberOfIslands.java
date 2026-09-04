package frequence.Graph;

import java.util.*;

/**
 * 200. 岛屿数量
 *
 * TODO: 【核心】感染模型。char[][] grid由'0'和'1'组成。 感染时，每次把'1'变成'0'，是可以的。
 *
 * <p>给定一个由字符{@code '1'}和{@code '0'}组成的二维网格{@code grid}，其中{@code '1'}
 * 表示陆地，{@code '0'}表示水域。岛屿由水平方向或竖直方向相邻的陆地连接而成，网格边界外
 * 均视为水域。返回网格中岛屿的数量。
 */
public class Q200_NumberOfIslands {

    /**
     * 2026-09-04 并查集版本。
     *
     * <p><b>解题步骤：</b>
     * <ol>
     *     <li>把二维坐标{@code (row, col)}编码成一维编号{@code row * cols + col}。</li>
     *     <li>只为陆地初始化并查集，每块陆地最初都是一个独立集合，{@code sets++}。</li>
     *     <li>遍历网格，只检查每个位置的下方和右方，避免同一条邻接边被重复处理。</li>
     *     <li>相邻位置均为陆地时执行union；两个不同集合合并后{@code sets--}。</li>
     * </ol>
     *
     * <p><b>遗漏点与易错点：</b>
     * <ol>
     *     <li>检查右、下方向时仍必须遍历完整矩阵。最右列可能向下连接，最下行可能向右连接；
     *     不能把两层循环都缩成{@code rows - 1/cols - 1}。</li>
     *     <li>读取邻居之前先检查边界，再判断当前格和邻居是否均为陆地。</li>
     *     <li>{@code find}的内部契约是一维编号；{@code union}是矩阵层接口，负责把二维坐标
     *     转成一维编号。这是接口分层，不是由public/private本身决定的。</li>
     *     <li>只有两个代表节点不同才能合并并执行{@code sets--}，否则重复合并会错误减少集合数。</li>
     *     <li>当前实现没有初始化水域的parent和size，所以union的调用前提必须是两端均为陆地。</li>
     * </ol>
     *
     * <p>时间复杂度近似O(MN)，严格写为O(MN * alpha(MN))；额外空间O(MN)。
     */
    class UnionFindSolution20260904 {

        public int numIslands(char[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            UnionFind uf = new UnionFind(grid);

            // TODO: 【错误】方向右下的思路是没错。但是右下对应下面的二重循环是错误的。
            // 因为最右列存在向下，最下行存在向右。
            // for (int i = 0; i < m - 1; i++) {
            //     for (int j = 0; j < n - 1; j++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    // TODO: 【错误-优化】存在下节点，&& 当前节点和下节点都是'1'，则合并。
                    // 这里的思想跟cur != null && cur.val == targetVal很像：先确保存在，才能取值比较。
                    if (i + 1 < m && (grid[i][j] == '1' && grid[i + 1][j] == '1')) {
                        uf.union(i, j, i + 1, j);
                    }
                    // TODO: 【错误-优化】同理，有右方节点，&& 满足双节点都为'1'，则合并。
                    if (j + 1 < n && (grid[i][j] == '1' && grid[i][j + 1] == '1')) {
                        uf.union(i, j, i, j+1);
                    }
                }
            }
            return uf.sets();
        }

        /** 二维网格使用的一维数组并查集。 */
        public class UnionFind {
            int[] parent;
            int[] size;
            int sets;
            int[] stack;
            int m;
            int n;

            public UnionFind(char[][] grid) {
                m = grid.length;
                n = grid[0].length;
                parent = new int[m * n];
                size = new int[m * n];
                stack = new int[m * n];
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        if (grid[i][j] == '1') {
                            int index = index(i, j);
                            parent[index] = index;
                            size[index] = 1;
                            sets++;
                        }
                    }
                }
            }

            public int index(int i, int j) {
                return i * n + j;
            }

            // TODO: 【错误】find不对外开放，所以参数不需要适配器(i,j)，只需要保留原样index。
            // 对应地，union是对外接口，所以对(i,j)做适配，内部再转换为一维index。
            // TODO: 【AI补充】更准确地说，这是内外接口的数据模型分层，不是private关键字本身决定的。
            private int find(int index) {
                int stackI = 0;
                while (index != parent[index]) {
                    stack[stackI++] = index;
                    index = parent[index];
                }

                while (stackI > 0) {
                    parent[stack[--stackI]] = index;
                }
                return index;
            }

            public void union(int i1, int j1, int i2, int j2) {
                int index1 = index(i1, j1);
                int index2 = index(i2, j2);
                int parent1 = find(index1);
                int parent2 = find(index2);

                if (parent1 != parent2) {   // TODO: 【错误-特别注意】union的必要前提条件。一定不能缺了。
                    if (size[parent1] > size[parent2]) {
                        parent[parent2] = parent1;
                        size[parent1] += size[parent2];
                    } else {
                        parent[parent1] = parent2;
                        size[parent2] += size[parent1];
                    }
                    sets--;
                }
            }

            public int sets() {
                return sets;
            }
        }
    }

    /**
     * 2026-09-04 感染模型复写版本。
     *
     * <p>外层每发现一块未访问陆地，答案加一，然后DFS把整个四连通分量改成水域。
     * 每个格子最多被处理一次，时间复杂度O(MN)，递归栈最坏O(MN)。该算法会修改输入矩阵。
     *
     * <p><b>原样保留说明：</b>下面保留复写时使用NUL字符{@code 0}作为感染标记，并同时判断
     * {@code 0 || '0'}的代码。Java中的{@code 0}和{@code '0'}不是同一个值：前者码值为0，
     * 后者码值为48。推荐方案只写在TODO中，不替换这份用于review的原代码。
     */
    class InfectSolution20260904 {

        public int numIslands(char[][] grid) {
            int ans = 0;
            int m = grid.length;
            int n = grid[0].length;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == '1') {
                        ans++;
                        infect(grid, i, j);
                    }
                }
            }
            return ans;
        }

        private void infect(char[][] grid, int i, int j) {
            int m = grid.length;
            int n = grid[0].length;
            if (i < 0 || i >= m || j < 0 || j >= n) {
                return;
            }

            // TODO: 【错误】你赋值为0，标记是感染对象。但是要注意'0'是水域，也要立即返回。
            // if (grid[i][j] == 0) {
            if (grid[i][j] == 0 || grid[i][j] == '0') {
                return;
            }
            // TODO: 【AI推荐】统一使用题目字符语义更清晰：
            // if (grid[i][j] != '1') return;
            // grid[i][j] = '0';
            // 当前复盘版本保留原写法：使用NUL字符0作为“已经感染”标记。
            grid[i][j] = 0;
            infect(grid, i - 1, j);
            infect(grid, i + 1, j);
            infect(grid, i, j - 1);
            infect(grid, i, j + 1);
        }
    }

    public int numIslands(char[][] grid) {
        int M = grid.length;
        int N = grid[0].length;
        int ans = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == '1') {
                    ans++;
                    infect(grid, i, j);
                }
            }
        }
        return ans;
    }

    public void infect(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
            return;
        }
        if (grid[i][j] == '0') {
            return;
        }
        grid[i][j] = '0';
        infect(grid, i - 1, j);
        infect(grid, i + 1, j);
        infect(grid, i, j - 1);
        infect(grid, i, j + 1);
    }
}
