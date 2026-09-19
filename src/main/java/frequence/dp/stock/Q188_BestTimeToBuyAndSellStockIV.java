package frequence.dp.stock;

import java.util.Arrays;

/**
 * 188. 买卖股票的最佳时机IV。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743547503。
 *
 * <p><b>DP类型：</b>状态拓扑属于“位置+交易次数+持股状态的状态机DP”，状态值语义属于
 * “最大值DP”。压缩天数维后仍需保留交易次数维。
 *
 * <p>最多完成K次交易，同一时间最多持有一只股票，求最大利润。
 *
 * <p>{@code cash[t]}表示最多完成t次卖出且当前不持股的最大收益；{@code hold[t]}表示为完成
 * 至多买入t次且当前持股时的最大收益。当前压缩写法为了保留昨日依赖，t倒序更新，
 * 使{@code cash[t-1]}仍保持昨日状态。
 * 当K不小于天数的一半时，次数限制不会生效，退化为不限交易次数模型。
 *
 * <p>时间复杂度O(NK)，退化分支为O(N)；额外空间O(K)。参见同包《股票问题总结.md》。
 */
public class Q188_BestTimeToBuyAndSellStockIV {

    /**
     * 用户复习版本（2026-09-20）。
     *
     * <p>用户理解：dp类型的问题，一定要先写【状态方程】。
     * 补充：先明确状态定义，再写初始化与转移，最后根据依赖确定遍历顺序。
     *
     * <p><b>第二维是至多额度，不是恰好次数：</b>
     * cash[i][j]表示截至第i天，至多完成j次交易且当前不持股的最大收益；
     * hold[i][j]表示截至第i天，至多买入j次且当前持股的最大收益，至多完成j-1次卖出。
     * “买入时+1次交易”准确地说是买入消耗一次额度，卖出不再消耗；完整交易仍包含买入和卖出。
     *
     * <p><b>初始化：</b>cash[0][j]=0允许不交易，hold[0][j]=-prices[0]允许只买入一次。
     * 所以j很大并不意味着第一天真的买了多次。cash[len-1][k]已包含0到k次交易的所有选择，
     * 不需要再枚举j取最大值。
     *
     * <p><b>原注释保留并纠正：</b>“下面的状态转移不是很正确，建议基于前一天”。
     * 原转移在本题按j正序计算时是正确的，不应标记成算法错误；它允许同日卖出再买入、
     * 买入再卖出，但同价操作不增加收益且可以消去。统一读取前一天，是为了语义更清晰。
     * 真正的执行错误是内层循环把j++写成了k++。
     *
     * <p><b>遍历方向：</b>原同日依赖需要j正序；改成全部读取i-1行后，i仍正序，
     * j正序或倒序均可。压缩成一维后需要重新分析覆盖关系，不能直接照搬二维循环方向。
     * 时间O(NK)，额外空间O(NK)。保留二维版本方便复盘；下方已有O(K)空间版本。
     */
    public static class SolutionReviewed20260920 {
        public int maxProfit(int k, int[] prices) {
            // TODO: 【边界】保留原样：当前实现要求prices非空。
            // 若扩展支持空数组，应在这里添加：if (prices.length == 0) return 0;
            int len = prices.length;
            int[][] hold = new int[len][k + 1];
            int[][] cash = new int[len][k + 1];
            /*
             * 用户原定义：当买入时，就表示+1次交易；卖出时交易次数不变。
             * 补充：这里计数的是买入额度，不是说买入就完成一次完整交易。
             *
             * 原同日转移（本题正确，建议统一改为前一天）：
             * hold[i][j] = Math.max(hold[i - 1][j], cash[i][j - 1] - prices[i]);
             * cash[i][j] = Math.max(cash[i - 1][j], hold[i][j] + prices[i]);
             * 原理解：从上往下、从左往右，初始化0行。
             * 修正后：全部依赖上一行，因此列j不再必须从左往右。
             */
            for (int j = 1; j <= k; j++) {
                hold[0][j] = -prices[0];
            }
            // TODO: 【状态完整性】hold[i][0]语义上不可达，但本实现从未读取它，
            // 所以保留默认0不影响答案；若以后使用该状态，应标记不可达并保护转移。
            // cash[i][0]=0则是真实可达：不进行任何交易。

            for (int i = 1; i < len; i++) {
                // TODO: 【这种错误你能犯错？】for (int j = 1; j <= k; k++) {
                // 修正：递增j，而不是修改交易次数上限k；否则j不变，k增大后可能越界。
                for (int j = 1; j <= k; j++) {
                    // TODO: 【修改建议，不是原算法错误】同日转移允许同日买卖，
                    // 本题不增加收益；统一读取前一天便于解释与复盘。
                    // hold[i][j] = Math.max(hold[i - 1][j], cash[i][j - 1] - prices[i]);
                    // cash[i][j] = Math.max(cash[i - 1][j], hold[i][j] + prices[i]);
                    hold[i][j] = Math.max(hold[i - 1][j], cash[i - 1][j - 1] - prices[i]);
                    cash[i][j] = Math.max(cash[i - 1][j], hold[i - 1][j] + prices[i]);
                }
            }
            // 至多k次，不是恰好k次；无需再对cash[len-1][0..k]取最大值。
            return cash[len - 1][k];
        }
    }

    /**
     * 用户在LeetCode独立完成的二维DP版本。交易次数在买入时消耗。
     */
    public int myMaxProfit(int k, int[] prices) {
        // TODO: 【曾出错】i表示prices的0-based下标，范围是[0, prices.length - 1]；
        // j表示交易次数，范围是[0, k]，因此第一维长度是prices.length。
        int[][] buy = new int[prices.length][k + 1];
        int[][] sell = new int[prices.length][k + 1];
        for (int j = 1; j <= k; j++) {
            buy[0][j] = -prices[0];
            sell[0][j] = 0;
        }

        for (int i = 1; i < prices.length; i++) {
            for (int j = 1; j <= k; j++) {
                buy[i][j] = Math.max(buy[i - 1][j], sell[i - 1][j - 1] - prices[i]);
                sell[i][j] = Math.max(sell[i - 1][j], buy[i - 1][j] + prices[i]);
            }
        }
        // TODO: 【曾出错】最后一天的下标是prices.length - 1，不是prices.length。
        return sell[prices.length - 1][k];
    }

    public int maxProfit(int k, int[] prices) {
        if (k <= 0 || prices == null || prices.length < 2) {
            return 0;
        }

        if (k >= prices.length / 2) {
            return unlimitedTransactions(prices);
        }

        int[] cash = new int[k + 1];
        int[] hold = new int[k + 1];
        Arrays.fill(hold, -prices[0]);

        for (int day = 1; day < prices.length; day++) {
            int price = prices[day];
            // 倒序保证cash[t-1]尚未被今天更新，仍表示上一天状态。
            for (int t = k; t >= 1; t--) {
                cash[t] = Math.max(cash[t], hold[t] + price);
                hold[t] = Math.max(hold[t], cash[t - 1] - price);
            }
        }
        return cash[k];
    }

    private int unlimitedTransactions(int[] prices) {
        int cash = 0;
        int hold = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            int oldCash = cash;
            int oldHold = hold;
            cash = Math.max(oldCash, oldHold + prices[i]);
            hold = Math.max(oldHold, oldCash - prices[i]);
        }
        return cash;
    }
}
