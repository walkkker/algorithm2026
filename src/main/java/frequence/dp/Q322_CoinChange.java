package frequence.dp;

import java.util.Arrays;

/**
 * 322. 零钱兑换
 *
 * <p>给定不同面额的硬币和总金额amount，每种硬币可以使用任意次，求凑成总金额所需的最少硬币数；
 * 无法凑成时返回-1。
 *
 * <p><b>DP类型：</b>状态拓扑属于“目标值DP/完全背包”，状态值语义属于“最小值DP”。
 * 每种硬币可重复使用，一维压缩时金额正序遍历。
 *
 * <p><b>原实现：</b>二维DP正确表达了“从第i种硬币开始凑金额j”，但又枚举当前硬币使用数量k，
 * 时间复杂度较高。利用完全背包的同层依赖，可以消除k枚举，压缩为一维DP。
 *
 * <p><b>易错点：</b>最小值DP必须区分“尚未求得的临时无穷大”和“题目要求返回的无解值-1”；
 * 只有前置状态可达时才能执行加1转移。
 *
 * <p>题型归纳参见同目录《动态规划题型共性总结.md》的“枚举转移与完全背包”章节。
 *
 * <p><b>一维DP复盘：</b>原实现正确但还停留在三层枚举：物品种类、目标金额、当前硬币数量。
 * 应先保留二维含义{@code dp[index][rest]}，再发现“至少使用一枚当前硬币”可以读取同一行的
 * {@code dp[index][rest-coin]+1}，从而消除数量枚举；最后去掉index维，容量正序保留同层依赖。
 * 完整的暴力递归、二维优化和一维压缩过程参见同目录《一维DP核心总结.md》。
 */
public class Q322_CoinChange {

    public static class OriginalSolution {

        public int coinChange(int[] coins, int amount) {
            int len = coins.length;
            int[][] dp = new int[len + 1][amount + 1];
            for (int j = 1; j <= amount; j++) {
                dp[len][j] = -1;
            }
            for (int i = len - 1; i >= 0; i--) {
                for (int j = 1; j <= amount; j++) {
                    int ans = Integer.MAX_VALUE;
                    // TODO: 【可优化】枚举当前硬币数量k是正确的，但会增加一层循环。二维状态可改为：
                    // dp[i][j] = min(dp[i+1][j], 1 + dp[i][j-coins[i]])，第二项的同层依赖
                    // 已经包含继续使用当前硬币的全部数量，因此可以消除k枚举。
                    for (int k = 0; k * coins[i] <= j; k++) {
                        if (dp[i + 1][j - k * coins[i]] != -1) {
                            ans = Math.min(ans, k + dp[i + 1][j - k * coins[i]]);
                        }
                    }
                    dp[i][j] = ans == Integer.MAX_VALUE ? -1 : ans;
                }
            }
            return dp[0][amount];
        }
    }

    public static class RecommendedSolution {

        public int coinChange(int[] coins, int amount) {
            int[] dp = new int[amount + 1];
            Arrays.fill(dp, amount + 1);
            dp[0] = 0;

            for (int coin : coins) {
                // 完全背包压缩后正序遍历：dp[target-coin]允许是本轮新状态，
                // 这正好对应同一种硬币可以重复参与转移。
                for (int target = coin; target <= amount; target++) {
                    dp[target] = Math.min(dp[target], dp[target - coin] + 1);
                }
            }
            return dp[amount] == amount + 1 ? -1 : dp[amount];
        }
    }
}
