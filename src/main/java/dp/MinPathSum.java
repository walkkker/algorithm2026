package dp;

/**
 * https://leetcode.cn/problems/minimum-path-sum/description/
 * <p>
 * 给定一个二维数组matrix，一个人必须从左上角出发，
 * 最后到达右下角沿途只可以向下或者向右走，沿途的数字都累加就是距离累加和,返回最小距离累加和
 *
 *
 * 样本对应模型，每个dp[i][j]对应matrix[i][j]， 跟左右范围模型一样，int[][] dp = new int[N][M]; 而不是 [N+1][M+1]
 */
public class MinPathSum {

    public static int minPathSum1(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] dp = new int[m][n];
        dp[m - 1][n - 1] = matrix[m - 1][n - 1];
        for (int j = n - 2; j >= 0; j--) {
            dp[m - 1][j] = dp[m - 1][j + 1] + matrix[m - 1][j];
        }
        for (int i = m - 2; i >= 0; i--) {
            dp[i][n - 1] = dp[i + 1][n - 1] + matrix[i][n - 1];
        }

        for (int i = m - 2; i >= 0; i--) {
            for (int j = n - 2; j >= 0; j--) {
                int p1 = dp[i + 1][j] + matrix[i][j];
                int p2 = dp[i][j + 1] + matrix[i][j];
                dp[i][j] = Math.min(p1, p2);
            }
        }
        return dp[0][0];
    }

    public static int dpTest(int[][] m) {
        int N = m.length;
        int M = m[0].length;
        int[][] dp = new int[N][M];
        dp[N - 1][M - 1] = m[N - 1][M - 1];
        for (int j = M - 2; j >= 0; j--) {
            dp[N - 1][j] = m[N - 1][j] + dp[N - 1][j + 1];
        }

        for (int i = N - 2; i >= 0; i--) {
            dp[i][M - 1] = m[i][M - 1] + dp[i + 1][M - 1];
        }

        for (int i = N - 2; i >= 0; i--) {
            for (int j = M - 2; j >= 0; j--) {
                int ans = Math.min(m[i][j] + dp[i + 1][j], m[i][j] + dp[i][j + 1]);
                // TODO: 【唯一错误点】又是写了ans，没有赋值 dp[i][j]!!!!!!!
                dp[i][j] = ans;
            }
        }
        return dp[0][0];
    }


}
