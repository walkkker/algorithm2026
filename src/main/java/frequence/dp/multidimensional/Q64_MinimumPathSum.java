package frequence.dp.multidimensional;

/**
 * 64. 最小路径和
 *
 * <p>给定非负整数网格，从左上角出发，每次只能向右或向下移动，求到达右下角的最小路径和。
 *
 * <p><b>DP类型：</b>状态拓扑属于“棋盘位置DP”，状态值语义属于“最小值DP”。
 *
 * <p><b>状态定义：</b>{@code dp[row][column]}表示到达真实坐标
 * {@code (row, column)}的最小路径和。
 *
 * <p><b>状态转移：</b>
 * {@code dp[row][column] = min(dp[row - 1][column], dp[row][column - 1])
 * + grid[row][column]}。第一行只能从左侧到达，第一列只能从上方到达，必须单独初始化。
 *
 * <p>时间复杂度O(MN)，二维版本额外空间O(MN)。详细分类参见同目录
 * 《多维DP核心总结.md》的“棋盘位置模型”。
 */
public class Q64_MinimumPathSum {

    /** 浏览器中保存的原实现，逻辑正确。 */
    public static class OriginalSolution {

        public int minPathSum(int[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            int[][] dp = new int[m][n];

            dp[0][0] = grid[0][0];
            for (int row = 1; row < m; row++) {
                dp[row][0] = grid[row][0] + dp[row - 1][0];
            }
            for (int column = 1; column < n; column++) {
                dp[0][column] = grid[0][column] + dp[0][column - 1];
            }

            for (int row = 1; row < m; row++) {
                for (int column = 1; column < n; column++) {
                    dp[row][column] = Math.min(dp[row - 1][column], dp[row][column - 1])
                            + grid[row][column];
                }
            }
            return dp[m - 1][n - 1];
        }
    }

    /** 空间压缩版本，额外空间O(N)。 */
    public static class SpaceOptimizedSolution {

        public int minPathSum(int[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            int[] dp = new int[n];

            dp[0] = grid[0][0];
            for (int column = 1; column < n; column++) {
                dp[column] = dp[column - 1] + grid[0][column];
            }

            for (int row = 1; row < m; row++) {
                dp[0] += grid[row][0];
                for (int column = 1; column < n; column++) {
                    // 更新前dp[column]表示上方，更新后的dp[column - 1]表示左方。
                    dp[column] = Math.min(dp[column], dp[column - 1]) + grid[row][column];
                }
            }
            return dp[n - 1];
        }
    }
}
