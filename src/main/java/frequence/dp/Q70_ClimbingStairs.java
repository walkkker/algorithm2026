package frequence.dp;

/**
 * 70. 爬楼梯
 *
 * <p>每次可以爬1级或2级台阶，计算到达第n级台阶的不同方法数。
 *
 * <p><b>DP类型：</b>状态拓扑属于“固定前驱线性DP”，状态值语义属于“纯计数DP”。
 * {@code dp[i]}只依赖{@code dp[i - 1]}和{@code dp[i - 2]}，两个合法来源的方案数量直接相加。
 *
 * <p><b>原实现：</b>使用斐波那契型递推关系和矩阵快速幂，时间复杂度为O(log N)。核心错误记录是
 * 矩阵幂次与初始向量没有通过统一公式推导，曾把幂次写成n-1；正确关系要求使用n-2。
 * 原{@code multiMatrix}只适用于本题2x2方阵，不是通用矩阵乘法模板。
 *
 * <p><b>推荐面试解法：</b>本题约束较小，使用两个变量保存前两阶答案，时间复杂度O(N)，
 * 空间复杂度O(1)，代码更稳定。
 *
 * <p><b>一维DP复盘：</b>原实现直接使用矩阵快速幂，时间复杂度更优，但跳过了本题最重要的基础推导：
 * 先定义{@code dp[i]}为到达第i阶的方法数，再从“最后一步走1阶或2阶”得到
 * {@code dp[i]=dp[i-1]+dp[i-2]}。确认该一维状态后，才能可靠地压缩成两个变量；矩阵快速幂
 * 是建立在线性递推之上的后续优化，不应替代状态定义。
 *
 * <p>一维状态推导和空间压缩顺序参见同目录《一维DP核心总结.md》。
 *
 * <p>题型归纳参见同目录《动态规划题型共性总结.md》的“固定前驱线性DP”章节。
 */
public class Q70_ClimbingStairs {

    public static class OriginalSolution {

        public int climbStairs(int n) {
            if (n == 1) {
                return 1;
            }
            if (n == 2) {
                return 2;
            }

            int[][] base = new int[][]{
                    {1, 1},
                    {1, 0}
            };
            // TODO: 【原错误】矩阵快速幂必须先写出统一递推矩阵关系，再由初始向量推导幂次。
            // 错误行：int[][] p = matrixPow(base, n - 1);
            int[][] p = matrixPow(base, n - 2);
            return 2 * p[0][0] + p[0][1];
        }

        private int[][] multiMatrix(int[][] a, int[][] b) {
            int M = a.length;
            int N = a[0].length;
            int[][] ans = new int[M][N];
            // TODO: 【适用范围】当前三层循环只适用于两个同阶方阵，本题固定为2x2，因此结果正确。
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    for (int k = 0; k < M; k++) {
                        ans[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
            return ans;
        }

        private int[][] matrixPow(int[][] a, int pow) {
            int M = a.length;
            int N = a[0].length;
            int[][] tmp = a;

            int[][] ans = new int[M][N];
            for (int i = 0; i < M; i++) {
                ans[i][i] = 1;
            }

            for (int i = pow; i > 0; i >>= 1) {
                if ((i & 1) == 1) {
                    ans = multiMatrix(ans, tmp);
                }
                tmp = multiMatrix(tmp, tmp);
            }
            return ans;
        }
    }

    public static class RecommendedSolution {

        public int climbStairs(int n) {
            if (n <= 2) {
                return n;
            }

            int previousTwo = 1;
            int previousOne = 2;
            for (int current = 3; current <= n; current++) {
                // 未压缩模型：dp[current] = dp[current - 1] + dp[current - 2]。
                // 先计算本轮答案，再移动旧状态，避免覆盖仍需读取的dp[current - 1]。
                int ways = previousOne + previousTwo;
                previousTwo = previousOne;
                previousOne = ways;
            }
            return previousOne;
        }
    }
}
