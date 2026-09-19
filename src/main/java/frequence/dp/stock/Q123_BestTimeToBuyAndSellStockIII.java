package frequence.dp.stock;

/**
 * 123. 买卖股票的最佳时机III。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743526451。
 *
 * <p><b>DP类型：</b>状态拓扑属于“带交易次数的线性状态机DP”，状态值语义属于“最大值DP”。
 * K=2时将交易次数与持股状态展开为四个常数状态。
 *
 * <p>最多完成两次交易，同一时间最多持有一只股票，求最大利润。
 *
 * <p>把交易次数展开成{@code buy1、sell1、buy2、sell2}四个状态。每轮都从上一天的状态快照
 * 转移，避免本轮状态相互污染。本题是Q188在K=2时的常数状态展开。
 *
 * <p>时间复杂度O(N)，额外空间O(1)。参见同包《股票问题总结.md》。
 */
public class Q123_BestTimeToBuyAndSellStockIII {

    /**
     * 用户复习版本（2026-09-20）：至多两次交易，hold1、cash1、hold2、cash2。
     *
     * <p><b>核心：这里是“至多”，不是“恰好”。状态语义由定义、初始化和转移共同决定，
     * 不能仅根据变量名中的1、2，理解成已经完成了对应次数的交易。
     * 一次完整交易包括买入和卖出，持股状态不能直接等同于完成交易次数。
     *
     * <ul>
     * <li>hold1：截至当天，至多买入1次且当前持股的最大收益（尚未完成卖出）。</li>
     * <li>cash1：截至当天，至多完成1次交易且当前不持股的最大收益，包含0次交易。</li>
     * <li>hold2：截至当天，至多买入2次且当前持股的最大收益，至多完成1次卖出。</li>
     * <li>cash2：截至当天，至多完成2次交易且当前不持股的最大收益，包含0、1、2次交易。</li>
     * </ul>
     *
     * <p><b>初始化为什么重要：</b>第一天cash1=cash2=0，表示允许不交易；
     * hold1=hold2=-prices[0]，表示两个持股状态都允许只买入一次。
     * hold2并不是第一天已经进行了第二次买入，而是拥有至多两次买入的额度。
     *
     * <p><b>转移为什么仍然是“至多”：</b>oldCash1本身包含0次或1次完整交易，
     * 所以oldCash1-prices[i]既可能是第一次买入，也可能是第二次买入。
     *
     * <p><b>与“恰好”的区别：</b>如果状态表示恰好执行第1次买入、第1次卖出、
     * 第2次买入、第2次卖出，并且严格从前一天转移，那么第一天仅hold1可达，
     * cash1、hold2、cash2都应初始化为不可达，而不是0或-prices[0]。
     * 不可达状态转移需要保护，避免负无穷哨兵参与运算造成溢出。
     * 此时若题目仍问“至多两次”，最终需要比较0、cash1、cash2。
     *
     * <p><b>当前版本直接返回cash2：</b>它已包含少做交易的选择，因此不必再与cash1或0取最大值。
     * 时间O(N)，额外空间O(1)。
     */
    public static class SolutionReviewed20260920 {

        public int maxProfit(int[] prices) {
            if (prices.length < 2) {
                return 0;
            }

            // TODO: 【状态定义】至多额度，不是恰好次数；不交易收益为0。
            int cash1 = 0;
            int hold1 = -prices[0];
            int cash2 = 0;
            // TODO: 【易错理解】允许只买入一次，并不要求先完成一次交易。
            int hold2 = -prices[0];

            for (int i = 1; i < prices.length; i++) {
                // 步骤1：保存前一天状态，四个转移统一使用旧值和当天价格。
                int oldHold1 = hold1;
                int oldCash1 = cash1;
                int oldHold2 = hold2;
                int oldCash2 = cash2;

                // 步骤2：每个状态比较“保持不动”和“今天执行一次买入/卖出”。
                hold1 = Math.max(oldHold1, -prices[i]);
                cash1 = Math.max(oldCash1, oldHold1 + prices[i]);
                // oldCash1包含0次交易，所以此处既能第一次买入，也能第二次买入。
                hold2 = Math.max(oldHold2, oldCash1 - prices[i]);
                cash2 = Math.max(oldCash2, oldHold2 + prices[i]);
            }
            // TODO: 【返回值】cash2已经覆盖0、1、2次交易，直接返回即可。
            return cash2;
        }
    }

    /**
     * 用户在LeetCode独立完成的AC版本。
     */
    public int myMaxProfit(int[] prices) {
        int buy1 = -prices[0];
        int sell1 = 0;
        int buy2 = -prices[0];
        int sell2 = 0;

        for (int i = 1; i < prices.length; i++) {
            int oldBuy1 = buy1;
            int oldSell1 = sell1;
            int oldBuy2 = buy2;
            int oldSell2 = sell2;

            buy1 = Math.max(oldBuy1, -prices[i]);
            sell1 = Math.max(oldSell1, oldBuy1 + prices[i]);
            buy2 = Math.max(oldBuy2, oldSell1 - prices[i]);
            sell2 = Math.max(oldSell2, oldBuy2 + prices[i]);
        }
        return sell2;
    }

    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int buy1 = -prices[0];
        int sell1 = 0;
        int buy2 = -prices[0];
        int sell2 = 0;

        for (int i = 1; i < prices.length; i++) {
            int price = prices[i];
            int oldBuy1 = buy1;
            int oldSell1 = sell1;
            int oldBuy2 = buy2;
            int oldSell2 = sell2;

            buy1 = Math.max(oldBuy1, -price);
            sell1 = Math.max(oldSell1, oldBuy1 + price);
            buy2 = Math.max(oldBuy2, oldSell1 - price);
            sell2 = Math.max(oldSell2, oldBuy2 + price);
        }
        return sell2;
    }
}
