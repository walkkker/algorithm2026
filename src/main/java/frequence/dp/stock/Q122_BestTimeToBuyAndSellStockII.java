package frequence.dp.stock;

/**
 * 122. 买卖股票的最佳时机II。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743387153。
 *
 * <p><b>DP类型：</b>状态拓扑属于“线性状态机DP”，状态值语义属于“最大值DP”。业务状态为
 * 当天结束时持股或不持股，允许完成任意次交易。
 *
 * <p>可以买卖任意多次，但同一时间最多持有一只股票，求最大利润。
 *
 * <p>{@code cash/hold}分别表示当天结束时不持股/持股的最大收益。卖出后未来允许再次买入，所以
 * 新持股状态可以从昨日{@code cash}减去今日价格转移。
 *
 * <p>时间复杂度O(N)，额外空间O(1)。参见同包《股票问题总结.md》。
 */
public class Q122_BestTimeToBuyAndSellStockII {

    /**
     * 用户在LeetCode独立完成的AC版本。
     */
    public int myMaxProfit(int[] prices) {
        int cash = 0;
        int hold = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            // 空间压缩后必须用快照，保证两个状态都从第i-1天转移。
            int oldCash = cash;
            int oldHold = hold;
            cash = Math.max(oldCash, oldHold + prices[i]);
            hold = Math.max(oldHold, oldCash - prices[i]);
        }
        // 完成全部交易时必须不持股，因此返回cash，而不是max(cash, hold)。
        return cash;
    }

    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }

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
