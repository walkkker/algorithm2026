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

    /**
     * 2026-09-19用户复盘：动态维护前缀最小值。
     *
     * <p>枚举卖出日i，固定卖出日后，选择此前价格最低的一天买入一定最优。
     * 原本每个i都要枚举此前的买入日，维护leftMin可将O(N^2)降为O(N)。
     *
     * <p>TODO: 【区间不变量】计算利润时leftMin表示[0,i-1]的最低价格；
     * 更新leftMin后才表示[0,i]的最低价格。这个执行顺序直接表达先买入、后卖出。
     * ans维护截至当前卖出日，最多交易一次的最大利润；初始0表示允许不交易。
     *
     * <p>分类为“枚举卖出日 + 前缀最小值”，也可从贪心或DP角度解释。
     * 与下方状态机等价：hold=-leftMin，cash=ans。时间O(N)，额外空间O(1)。
     * 关联文档：同目录《股票问题总结.md》的Q121章节。
     */
    public static class SolutionReviewed20260919 {
        public int maxProfit(int[] prices) {
            if (prices.length < 2) {
                return 0;
            }
            int ans = 0;
            int leftMin = prices[0];
            for (int i = 1; i < prices.length; i++) {
                // 动态维护前缀最小值：此时leftMin对应[0,i-1]，枚举今天卖出。
                ans = Math.max(ans, prices[i] - leftMin);
                // 更新后leftMin对应[0,i]，供下一轮使用。
                leftMin = Math.min(leftMin, prices[i]);
            }
            return ans;
        }
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

            // 今天不持股：继续空仓，或者卖出唯一一次持仓。
            cash = Math.max(oldCash, oldHold + prices[i]);

            // 最多一次交易：买入只能从初始收益0出发，不能从oldCash再次买入。
            hold = Math.max(oldHold, -prices[i]);
        }
        return cash;
    }
}
