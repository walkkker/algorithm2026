package frequence.dp;

import java.util.Arrays;

/**
 * 279. 完全平方数
 *
 * <p>求组成正整数n所需要的最少完全平方数数量。
 *
 * <p><b>DP类型：</b>状态拓扑属于“目标值DP/完全背包”，状态值语义属于“最小值DP”。
 * 每个平方数可以重复使用，一维背包压缩时目标值正序遍历。
 *
 * <p><b>正确思路：</b>定义{@code dp[i]}为组成i所需的最少平方数数量，枚举最后使用的平方数
 * square，执行{@code dp[i] = min(dp[i], dp[i-square] + 1)}。这是元素可以重复使用的完全背包模型。
 *
 * <p><b>原错误：</b>答案访问{@code dp[n]}，数组长度必须为n+1。原实现修正后结果正确；推荐版本
 * 统一使用{@code dp[0]=0}和不可达哨兵，避免单独设置{@code dp[1]}。
 *
 * <p>题型归纳参见同目录《动态规划题型共性总结.md》的“枚举转移与完全背包”章节。
 *
 * <p><b>一维DP复盘：</b>原实现采用原生目标值状态：{@code dp[target]}表示组成target所需的
 * 最少平方数，枚举最后使用的平方数，逻辑与复杂度都正确；主要错误是状态范围包含0到n，却曾只创建
 * 长度n的数组。推荐版本从完全背包理解：先有“前若干种平方数、目标值target”的二维状态，再利用
 * 当前行更小容量消除物品维，容量正序允许同一平方数重复使用。两条推导路径参见同目录
 * 《一维DP核心总结.md》。
 */
public class Q279_PerfectSquares {

    public static class OriginalSolution {

        public int numSquares(int n) {
            // TODO: 【原错误】需要访问dp[n]，数组长度必须是n+1。
            // 错误行：int[] dp = new int[n];
            int[] dp = new int[n + 1];
            dp[1] = 1;
            for (int i = 2; i <= n; i++) {
                int ans = i; // 最差情况是全部使用1。
                for (int k = 1; k * k <= i; k++) {
                    // 枚举最后使用的平方数k*k，前驱目标因此是i-k*k。
                    ans = Math.min(ans, dp[i - k * k] + 1);
                }
                dp[i] = ans;
            }
            return dp[n];
        }
    }

    public static class RecommendedSolution {

        public int numSquares(int n) {
            int[] dp = new int[n + 1];
            Arrays.fill(dp, n + 1);
            dp[0] = 0;

            for (int squareRoot = 1; squareRoot * squareRoot <= n; squareRoot++) {
                int square = squareRoot * squareRoot;
                // 完全背包压缩：正序枚举容量，允许读取本轮已经更新的dp[target-square]，
                // 从而使同一个平方数可以重复使用。
                for (int target = square; target <= n; target++) {
                    dp[target] = Math.min(dp[target], dp[target - square] + 1);
                }
            }
            return dp[n];
        }
    }
}
