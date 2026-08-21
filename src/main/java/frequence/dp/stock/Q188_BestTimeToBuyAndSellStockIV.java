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
 * 第t次交易而持股时的最大收益。压缩天数维后，t必须倒序更新，使{@code cash[t-1]}仍保持昨日状态。
 * 当K不小于天数的一半时，次数限制不会生效，退化为不限交易次数模型。
 *
 * <p>时间复杂度O(NK)，退化分支为O(N)；额外空间O(K)。参见同包《股票问题总结.md》。
 */
public class Q188_BestTimeToBuyAndSellStockIV {

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
