package frequence.dp.multidimensional;

/**
 * 62. 不同路径
 *
 * <p>机器人位于{@code m * n}网格左上角，每次只能向右或向下移动一步，求到达右下角的路径数量。
 *
 * <p><b>DP类型：</b>状态拓扑属于“棋盘位置DP”，状态值语义属于“纯计数DP”。来自上方和
 * 左方的全部合法路径数量直接相加。
 *
 * <p><b>状态定义：</b>{@code dp[row][column]}表示从左上角走到坐标
 * {@code (row, column)}的路径数量。这里两个维度都是真实的棋盘坐标，因此数组大小是
 * {@code m * n}，最终答案是{@code dp[m - 1][n - 1]}。
 *
 * <p><b>状态转移：</b>当前位置只能从上方或左方到达：
 * {@code dp[row][column] = dp[row - 1][column] + dp[row][column - 1]}。
 * 第一行和第一列都只有一种走法，统一初始化为1。
 *
 * <p>时间复杂度O(MN)，二维版本额外空间O(MN)。详细分类参见同目录
 * 《多维DP核心总结.md》的“棋盘位置模型”。
 */
public class Q62_UniquePaths {

    /** 浏览器中保存的原实现。 */
    public static class OriginalSolution {

        public int uniquePaths(int m, int n) {
            // TODO: 【原错误】曾写成new int[m + 1][n + 1]并返回dp[m][n]。
            // 原因：混淆了“坐标型状态”和“前缀长度型状态”。本题row/column就是实际坐标，
            // 合法范围分别是[0, m - 1]、[0, n - 1]，所以数组应为m * n。
            int[][] dp = new int[m][n];
            dp[0][0] = 1;

            for (int row = 0; row < m; row++) {
                dp[row][0] = 1;
            }
            for (int column = 0; column < n; column++) {
                dp[0][column] = 1;
            }

            for (int row = 1; row < m; row++) {
                for (int column = 1; column < n; column++) {
                    dp[row][column] = dp[row - 1][column] + dp[row][column - 1];
                }
            }
            return dp[m - 1][n - 1];
        }
    }

    /**
     * 空间压缩版本。逻辑状态仍然是二维，只是同一行计算时复用一维数组。
     */
    public static class SpaceOptimizedSolution {

        public int uniquePaths(int m, int n) {
            int[] dp = new int[n];
            for (int column = 0; column < n; column++) {
                dp[column] = 1;
            }

            for (int row = 1; row < m; row++) {
                for (int column = 1; column < n; column++) {
                    // 更新前dp[column]是上方，dp[column - 1]是本行左方。
                    dp[column] = dp[column] + dp[column - 1];
                }
            }
            return dp[n - 1];
        }
    }
}
