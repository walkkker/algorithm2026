package frequence.dp.stock;

/**
 * 309. 买卖股票的最佳时机含冷冻期。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743391482。
 *
 * <p><b>DP类型：</b>状态拓扑属于“线性状态机DP”，状态值语义属于“最大值DP”。冷冻期使
 * 不持股状态必须进一步拆成刚卖出与可买入两个业务状态。
 *
 * <p>可以完成任意次交易，但卖出股票后的下一天不能买入。
 *
 * <p>仅有hold/cash无法区分“不持股且今天能否买入”，因此维护三个互斥状态：{@code hold}为
 * 今天结束时持股，{@code sold}为今天刚卖出，{@code rest}为今天不持股且没有在今天卖出。买入只能
 * 从昨日rest转移，不能从昨日sold转移，这条限制表达冷冻期。
 *
 * <p>时间复杂度O(N)，额外空间O(1)。参见同包《股票问题总结.md》。
 */
public class Q309_BestTimeToBuyAndSellStockWithCooldown {

    /**
     * 用户在LeetCode独立完成的cash/hold版本，通过preCash保存cash[i-2]。
     */
    public int myMaxProfit(int[] prices) {
        if (prices.length == 1) {
            return 0;
        }
        if (prices.length == 2) {
            return Math.max(0, prices[1] - prices[0]);
        }

        int preCash = 0;
        int cash = Math.max(0, prices[1] - prices[0]);
        int hold = Math.max(-prices[0], -prices[1]);
        // TODO: 【曾出错】第三天对应0-based下标2，循环从2开始，不能从3开始。
        for (int i = 2; i < prices.length; i++) {
            int oldCash = cash;
            int oldHold = hold;
            cash = Math.max(oldCash, oldHold + prices[i]);
            hold = Math.max(oldHold, preCash - prices[i]);
            preCash = oldCash;
        }
        return cash;
    }

    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int hold = -prices[0];
        int sold = Integer.MIN_VALUE / 2;
        int rest = 0;

        for (int i = 1; i < prices.length; i++) {
            int oldHold = hold;
            int oldSold = sold;
            int oldRest = rest;

            hold = Math.max(oldHold, oldRest - prices[i]);
            sold = oldHold + prices[i];
            rest = Math.max(oldRest, oldSold);
        }
        return Math.max(sold, rest);
    }
}
