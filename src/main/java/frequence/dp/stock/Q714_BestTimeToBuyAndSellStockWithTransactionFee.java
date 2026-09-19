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
 * <p>状态仍为cash/hold，不需要增加维度；下方maxProfit统一在卖出边扣除手续费，用户版本在买入边扣费，
 * 但不能买入和卖出两边重复扣除。
 *
 * <p>时间复杂度O(N)，额外空间O(1)。参见同包《股票问题总结.md》。
 */
public class Q714_BestTimeToBuyAndSellStockWithTransactionFee {

    /**
     * 用户复习版本（2026-09-20），保留买入扣费的实现和错误提醒。
     *
     * <p>TODO: 【非常重要的错误，一定要review】
     * TODO: 【这道题竟然做错了！！！】【重点提醒】
     * TODO: 【错误点】错误在初始化漏扣手续费！！！！！
     *
     * <p>TODO: 除了每次买入要扣手续费之外，其余代码与股票II（无限次交易）一模一样！
     * 手续费fee：无限次交易，每笔交易包含一次买入和一次卖出。我们定义买入时付手续费。
     *
     * <p><b>关键补充：</b>“每次买入”包含第0天的初始买入，不只是循环中的买入转移。
     * DP初始化就是边界状态的答案，也必须遵守同一状态定义。
     * cash表示当天结束不持股的最大收益，hold表示当天结束持股、且已扣买入手续费的最大收益。
     * 卖出不再扣费，保证每笔完整交易只收一次手续费。
     *
     * <p><b>最小反例：</b>prices=[1,3]、fee=1。错误初始化hold=-1会算出收益2；
     * 正确初始化hold=-2，卖出后收益为1。循环从第1天开始，不会补扣第0天漏掉的手续费。
     *
     * <p>当前修正版本正确，时间O(N)，额外空间O(1)。
     */
    public static class SolutionReviewed20260920 {
        public int maxProfit(int[] prices, int fee) {
            /*
             * cash[i] = Math.max(cash[i - 1], hold[i - 1] + prices[i]);
             * hold[i] = Math.max(hold[i - 1], cash[i - 1] - prices[i] - fee);
             * 因为只依赖i-1，所以可以优化成两个状态变量。
             */
            if (prices.length < 2) {
                return 0;
            }

            int cash = 0;
            // int hold = -prices[0]; // TODO: 【唯一错误点，但是很难发现！！！！】
            // 因为定义买入扣除手续费！！！这里要多扣手续费！！！！
            // TODO: 【初始化遵守状态定义】初始买入同样是买入，不能漏扣。
            int hold = -prices[0] - fee;
            for (int i = 1; i < prices.length; i++) {
                // 保存前一天状态，避免读取刚更新的当天状态。
                int oldCash = cash;
                int oldHold = hold;

                cash = Math.max(oldCash, oldHold + prices[i]);
                hold = Math.max(oldHold, oldCash - prices[i] - fee);
            }
            return cash;
        }
    }

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
