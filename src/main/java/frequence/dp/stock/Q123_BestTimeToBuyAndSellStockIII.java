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
