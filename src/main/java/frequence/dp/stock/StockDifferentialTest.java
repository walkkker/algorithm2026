package frequence.dp.stock;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * 股票系列对数器。
 *
 * <p><b>验证范围：</b>
 * <ul>
 *     <li>Q121：最多1次交易；</li>
 *     <li>Q122：不限交易次数；</li>
 *     <li>Q123：最多2次交易；</li>
 *     <li>Q188：最多K次交易，并覆盖K=0和退化为不限次数的情况；</li>
 *     <li>Q309：卖出后下一天禁止买入；</li>
 *     <li>Q714：随机手续费，包括fee=0。</li>
 * </ul>
 *
 * <p>基准方法按天穷举“跳过、买入、卖出”所有合法操作，不复用被测状态转移。随机数组规模保持较小，
 * 以穷举结果作为可靠答案，再与各题优化实现比较。
 *
 * <p>运行全部：{@code java frequence.dp.stock.StockDifferentialTest}
 *
 * <p>运行单题：{@code java frequence.dp.stock.StockDifferentialTest q309}
 */
public class StockDifferentialTest {

    private static final int NEGATIVE_INFINITY = Integer.MIN_VALUE / 4;

    public static void main(String[] args) {
        Map<String, Runnable> registry = new LinkedHashMap<>();
        registry.put("q121", StockDifferentialTest::testQ121);
        registry.put("q122", StockDifferentialTest::testQ122);
        registry.put("q123", StockDifferentialTest::testQ123);
        registry.put("q188", StockDifferentialTest::testQ188);
        registry.put("q309", StockDifferentialTest::testQ309);
        registry.put("q714", StockDifferentialTest::testQ714);

        String target = args.length == 0 ? "all" : args[0].toLowerCase();
        if ("all".equals(target)) {
            for (Map.Entry<String, Runnable> entry : registry.entrySet()) {
                entry.getValue().run();
                System.out.println(entry.getKey() + " passed");
            }
            System.out.println("all stock differential tests passed");
            return;
        }

        Runnable test = registry.get(target);
        if (test == null) {
            throw new IllegalArgumentException(
                    "unknown target: " + target + ", available=" + registry.keySet());
        }
        test.run();
        System.out.println(target + " passed");
    }

    private static void testQ121() {
        Q121_BestTimeToBuyAndSellStock solution = new Q121_BestTimeToBuyAndSellStock();
        require(solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4}) == 5,
                "Q121 deterministic case");

        Random random = new Random(121L);
        for (int round = 0; round < 3000; round++) {
            int[] prices = randomPrices(random);
            int expected = bruteAtMostK(prices, 1);
            int actual = solution.maxProfit(prices);
            require(actual == expected, detail("Q121", prices, 1, 0, expected, actual));
        }
    }

    private static void testQ122() {
        Q122_BestTimeToBuyAndSellStockII solution = new Q122_BestTimeToBuyAndSellStockII();
        require(solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4}) == 7,
                "Q122 deterministic case");

        Random random = new Random(122L);
        for (int round = 0; round < 3000; round++) {
            int[] prices = randomPrices(random);
            int expected = bruteAtMostK(prices, prices.length / 2);
            int actual = solution.maxProfit(prices);
            require(actual == expected, detail("Q122", prices, 0, 0, expected, actual));
        }
    }

    private static void testQ123() {
        Q123_BestTimeToBuyAndSellStockIII solution = new Q123_BestTimeToBuyAndSellStockIII();
        require(solution.maxProfit(new int[]{3, 3, 5, 0, 0, 3, 1, 4}) == 6,
                "Q123 deterministic case");

        Random random = new Random(123L);
        for (int round = 0; round < 3000; round++) {
            int[] prices = randomPrices(random);
            int expected = bruteAtMostK(prices, 2);
            int actual = solution.maxProfit(prices);
            require(actual == expected, detail("Q123", prices, 2, 0, expected, actual));
        }
    }

    private static void testQ188() {
        Q188_BestTimeToBuyAndSellStockIV solution = new Q188_BestTimeToBuyAndSellStockIV();
        require(solution.maxProfit(2, new int[]{2, 4, 1}) == 2,
                "Q188 deterministic case");

        Random random = new Random(188L);
        for (int round = 0; round < 3000; round++) {
            int[] prices = randomPrices(random);
            int k = random.nextInt(6);
            int expected = bruteAtMostK(prices, k);
            int actual = solution.maxProfit(k, prices);
            require(actual == expected, detail("Q188", prices, k, 0, expected, actual));
        }
    }

    private static void testQ309() {
        Q309_BestTimeToBuyAndSellStockWithCooldown solution =
                new Q309_BestTimeToBuyAndSellStockWithCooldown();
        require(solution.maxProfit(new int[]{1, 2, 3, 0, 2}) == 3,
                "Q309 deterministic case");

        Random random = new Random(309L);
        for (int round = 0; round < 3000; round++) {
            int[] prices = randomPrices(random);
            int expected = bruteCooldown(prices);
            int actual = solution.maxProfit(prices);
            require(actual == expected, detail("Q309", prices, 0, 0, expected, actual));
        }
    }

    private static void testQ714() {
        Q714_BestTimeToBuyAndSellStockWithTransactionFee solution =
                new Q714_BestTimeToBuyAndSellStockWithTransactionFee();
        require(solution.maxProfit(new int[]{1, 3, 2, 8, 4, 9}, 2) == 8,
                "Q714 deterministic case");

        Random random = new Random(714L);
        for (int round = 0; round < 3000; round++) {
            int[] prices = randomPrices(random);
            int fee = random.nextInt(6);
            int expected = bruteFee(prices, fee);
            int actual = solution.maxProfit(prices, fee);
            require(actual == expected, detail("Q714", prices, 0, fee, expected, actual));
        }
    }

    private static int bruteAtMostK(int[] prices, int k) {
        Integer[][][] memo = new Integer[prices.length + 1][k + 1][2];
        return bruteAtMostK(prices, 0, k, 0, memo);
    }

    /**
     * remaining记录未来最多还允许完成多少次卖出；一次完整交易在卖出时计数。
     */
    private static int bruteAtMostK(
            int[] prices,
            int day,
            int remaining,
            int holding,
            Integer[][][] memo) {

        if (day == prices.length) {
            return holding == 0 ? 0 : NEGATIVE_INFINITY;
        }
        if (memo[day][remaining][holding] != null) {
            return memo[day][remaining][holding];
        }

        int ans = bruteAtMostK(prices, day + 1, remaining, holding, memo);
        if (holding == 1 && remaining > 0) {
            ans = Math.max(ans,
                    prices[day] + bruteAtMostK(prices, day + 1, remaining - 1, 0, memo));
        } else if (holding == 0 && remaining > 0) {
            ans = Math.max(ans,
                    -prices[day] + bruteAtMostK(prices, day + 1, remaining, 1, memo));
        }

        memo[day][remaining][holding] = ans;
        return ans;
    }

    private static int bruteCooldown(int[] prices) {
        Integer[][][] memo = new Integer[prices.length + 1][2][2];
        return bruteCooldown(prices, 0, 0, 0, memo);
    }

    private static int bruteCooldown(
            int[] prices,
            int day,
            int holding,
            int cooldown,
            Integer[][][] memo) {

        if (day == prices.length) {
            return holding == 0 ? 0 : NEGATIVE_INFINITY;
        }
        if (memo[day][holding][cooldown] != null) {
            return memo[day][holding][cooldown];
        }

        // 跳过今天后，昨日卖出造成的冷冻状态结束。
        int ans = bruteCooldown(prices, day + 1, holding, 0, memo);
        if (holding == 1) {
            ans = Math.max(ans,
                    prices[day] + bruteCooldown(prices, day + 1, 0, 1, memo));
        } else if (cooldown == 0) {
            ans = Math.max(ans,
                    -prices[day] + bruteCooldown(prices, day + 1, 1, 0, memo));
        }

        memo[day][holding][cooldown] = ans;
        return ans;
    }

    private static int bruteFee(int[] prices, int fee) {
        Integer[][] memo = new Integer[prices.length + 1][2];
        return bruteFee(prices, fee, 0, 0, memo);
    }

    private static int bruteFee(
            int[] prices,
            int fee,
            int day,
            int holding,
            Integer[][] memo) {

        if (day == prices.length) {
            return holding == 0 ? 0 : NEGATIVE_INFINITY;
        }
        if (memo[day][holding] != null) {
            return memo[day][holding];
        }

        int ans = bruteFee(prices, fee, day + 1, holding, memo);
        if (holding == 1) {
            ans = Math.max(ans,
                    prices[day] - fee + bruteFee(prices, fee, day + 1, 0, memo));
        } else {
            ans = Math.max(ans,
                    -prices[day] + bruteFee(prices, fee, day + 1, 1, memo));
        }

        memo[day][holding] = ans;
        return ans;
    }

    private static int[] randomPrices(Random random) {
        int[] prices = new int[1 + random.nextInt(8)];
        for (int i = 0; i < prices.length; i++) {
            prices[i] = 1 + random.nextInt(10);
        }
        return prices;
    }

    private static String detail(
            String name,
            int[] prices,
            int k,
            int fee,
            int expected,
            int actual) {

        return name + " prices=" + Arrays.toString(prices)
                + ", k=" + k
                + ", fee=" + fee
                + ", expected=" + expected
                + ", actual=" + actual;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}

