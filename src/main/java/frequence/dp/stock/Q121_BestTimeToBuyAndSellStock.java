package frequence.dp.stock;

/**
 * 121. 买卖股票的最佳时机。
 *
 * <p><b>DP类型：</b>状态拓扑属于“线性状态机DP”，状态值语义属于“最大值DP”。业务状态为
 * 当天结束时持股或不持股，并额外受“最多完成一次交易”约束。
 *
 * <p>最多完成一次买入和一次卖出，且买入必须发生在卖出之前，求最大利润。
 *
 * <p><b>状态：</b>{@code cash}表示当天结束时不持股的最大收益，{@code hold}表示当天结束时
 * 持股的最大收益。由于最多交易一次，持股状态只能来自“继续持有”或者直接以初始现金0买入，不能使用
 * 历史卖出利润再次买入。
 *
 * <p>时间复杂度O(N)，额外空间O(1)。系列总结参见同包《股票问题总结.md》；与Q152双状态DP的
 * 结构对比参见上级目录《乘积最大子数组与股票状态机DP对比.md》。
 */
public class Q121_BestTimeToBuyAndSellStock {

    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int cash = 0;
        int hold = -prices[0];

        for (int i = 1; i < prices.length; i++) {
            int oldCash = cash;
            int oldHold = hold;

            // 今天不持股：继续空仓，或者卖出唯一一次持仓。
            cash = Math.max(oldCash, oldHold + prices[i]);

            // 最多一次交易：买入只能从初始收益0出发，不能从oldCash再次买入。
            hold = Math.max(oldHold, -prices[i]);
        }
        return cash;
    }
}
