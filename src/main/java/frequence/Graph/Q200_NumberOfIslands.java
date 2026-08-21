package frequence.Graph;

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
