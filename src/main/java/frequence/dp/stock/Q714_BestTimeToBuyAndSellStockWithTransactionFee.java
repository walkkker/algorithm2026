package frequence.dp.stock;

/**
 * 714. 买卖股票的最佳时机含手续费。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743388378。
 *
 * <p><b>DP类型：</b>状态拓扑属于“线性状态机DP”，状态值语义属于“最大值DP”。手续费只改变
 * 买入或卖出转移边的权重，不需要新增业务状态。
 *
 * <p>可以完成任意次交易，每次完整交易需要支付固定手续费fee，同一时间最多持有一只股票。
 *
 * <p>状态仍为cash/hold，不需要增加维度；本实现统一在卖出边扣除手续费。也可以统一在买入边扣费，
 * 但不能买入和卖出两边重复扣除。
 *
 * <p>时间复杂度O(N)，额外空间O(1)。参见同包《股票问题总结.md》。
 */
public class Q714_BestTimeToBuyAndSellStockWithTransactionFee {

    /**
     * 用户在LeetCode独立完成的AC版本：统一在买入状态扣除手续费。
     */
    public int myMaxProfit(int[] prices, int fee) {
        int cash = 0;
        int hold = -prices[0] - fee;
        for (int i = 1; i < prices.length; i++) {
            int oldCash = cash;
            int oldHold = hold;
            cash = Math.max(oldCash, oldHold + prices[i]);
            hold = Math.max(oldHold, oldCash - prices[i] - fee);
        }
        return cash;
    }

    public int maxProfit(int[] prices, int fee) {
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int cash = 0;
        int hold = -prices[0];

        for (int i = 1; i < prices.length; i++) {
            int oldCash = cash;
            int oldHold = hold;

            cash = Math.max(oldCash, oldHold + prices[i] - fee);
            hold = Math.max(oldHold, oldCash - prices[i]);
        }
        return cash;
    }
}
