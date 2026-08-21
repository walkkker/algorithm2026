package dp;

/**
 * https://www.lintcode.com/problem/125/
 * 继CardsInLine后续：
 *
 * 有 n 个物品和一个大小为 m 的背包. 给定数组 A 表示每个物品的大小和数组 V 表示每个物品的价值.
 *
 * 问最多能装入背包的总价值是多大?
 *
 * */
 // TODO： 之前是说 process的baseCase  <=  int[][] dp的 baseCase
 // TODO： 补充：填dp的时候，
 //   （1）跑出矩阵的依赖，当作没有 （以本题为例，size+w[i] > bag，从dp层面是>列数，从语义层面是 这个尝试不符合题意 =》 所以最终结论都是 抛弃掉这个尝试
 //    (2) 补充（1），dp[i][j]依赖xxx的问题：
//          a. 【1】先说结论：dp 也要先把所有的base case填充完，再推导 general node
//          b. 【重要】原因：如果一个dp[i][j]所有的依赖都越界，那么根据(1)就说明 所有的依赖都【不合理】，所以dp[i][j]只依赖i,j自己的状态值 就可以得出dp值。！！！ 此时这种dp[i][j] 就是 base case。 一定发生在特定的位置上（边界行，边界列，对角线），需要直接把值推导出来。
//          c. 【2】直接记住结论就可以 -> 【推导dp的循环体内，需要 保证 每个元素dp[i][j] 至少存在一个依赖，这样方便分类讨论】
//      一些小技巧：
//          1. 【重要】依赖方向跟推的方向是相反的   a->b 则 b=>a (->表示依赖，=>表示推)
//          2. 所以，如果dp[i][j]依赖的是 下和右下 =》 那么推的方向就是 【 由下向上（对应向下依赖）&& 由右向左（对应向右依赖『右下』） 】

public class Knapsack {

    public static int dp(int[] w, int[] v, int bag) {
        int m = w.length + 1;
        int n = bag + 1;
        int[][] dp = new int[m][n];
        for (int i = m - 2; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (j + w[i] <= bag) {
                    dp[i][j] = Math.max(v[i] + dp[i+1][j + w[i]], dp[i+1][j]);
                } else {
                    dp[i][j] = dp[i+1][j];
                }
            }
        }
        return dp[0][0];
    }

    public static void main(String[] args) {
        int[] weights = { 3, 2, 4, 7, 3, 1, 7, 9 };
        int[] values = { 5, 6, 3, 19, 12, 4, 2, 19 };
        int bag = 25;
        System.out.println(dp(weights, values, bag));

        System.out.println(dpTest(weights, values, bag));
    }

    public static int dpTest(int[] w, int[] v, int bag) {
        int len = w.length;
        int[][] dp = new int[len + 1][bag + 1];
        for (int i = len - 1; i >= 0; i--) {
            for (int j = 0; j <= bag; j++) {
                int ans = dp[i + 1][j];
                if (j - w[i] >= 0) {
                    ans = Math.max(ans, v[i] + dp[i + 1][j - w[i]]);
                }
                dp[i][j] = ans;
            }
        }
        return dp[0][bag];
    }


}
