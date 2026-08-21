package frequence.dp.multidimensional;

/**
 * 115. 不同的子序列
 *
 * <p><b>DONE: 【已独立完成】</b>当前实现已通过计数DP对数器。
 * 状态总表参见{@code frequence/待独立完成题目清单.md}。
 *
 * <p>给定字符串s和t，统计s的子序列中等于t的方案数量。不同方案由选择的原字符串下标区分；
 * 即使得到的字符内容相同，只要使用的下标组合不同，也属于不同方案。
 *
 * <p><b>DP类型：</b>状态拓扑属于“双序列前缀DP”，状态值语义属于“纯计数DP”。
 * {@code dp[i][j]}表示使用{@code s[0, i)}组成{@code t[0, j)}的方案数量。
 * 本题没有最大值或最小值目标，所有合法来源都直接累加。
 *
 * <p><b>转移：</b>不使用{@code s[i - 1]}时继承{@code dp[i - 1][j]}；如果当前字符相等，
 * 还可以使用它匹配{@code t[j - 1]}，增加{@code dp[i - 1][j - 1]}种方案。
 *
 * <p><b>边界：</b>任意s前缀组成空字符串都只有一种方案，即一个字符也不选择，故
 * {@code dp[i][0] = 1}；空s不能组成非空t，故{@code dp[0][j] = 0}。
 *
 * <p>详细分类参见上级目录《动态规划题型共性总结.md》以及专题
 * {@code frequence/substringandsubsequence/子串与子序列专题.md}。
 */
public class Q115_DistinctSubsequences {

    /**
     子序列问题：
     1. 解题思路：双序列前缀 + 思考方式：最后一步（最后一个字符是否匹配）
     - 【特别注意】前缀dp i,j对应【长度】而不是【下标】。    当状态转移涉及下标时，对应s[i-1], target[j-1]
     2. 含义：dp[i][j] s的前i个字符子序列 拼出t的前j个字符的方案数
     - dp[i][j] = source前i个字符中，能够组成target前j个字符的子序列数量

     思路：
     a. 字符相同：选当前字符 + 不选当前字符
     b. 字符不同：只能不选当前字符

     代码：
     ```
         for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
             if (source[i - 1] == target[j - 1]) {
              dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
              } else {
                 dp[i][j] = dp[i - 1][j];
             }
             }
         }
     ```
     */
    class MySolution {
        public int numDistinct(String s, String t) {
            char[] chs = s.toCharArray();
            char[] target = t.toCharArray();
            int m = chs.length;
            int n = target.length;

            int[][] dp = new int[m + 1][n + 1];
            /**
             dp[i][j]方程
                 if (chs[i-1] == target[j-1]) {
                  dp[i][j] = dp[i-1][j-1] + dp[i-1][j];
                 } else {
                  dp[i][j] = dp[i - 1][j];
                 }
             */
            // 从上往下，从左往右  => 初始化，上 和 左 行

            // 初始化列：任意source都只有一种方式组成空字符串：什么都不选
            for (int i = 0; i <= m; i++) {
                dp[i][0] = 1;
            }
            // 初始化行：dp[0][j]默认是0：空source无法组成非空target

            for (int i = 1; i <= m; i++) {
                // TODO: 本题的依赖方向是 左上和上 => 推导方向是 其实只需要从上到下 => 但是你想要用一套公式满足所有 格子的话 => 这个范围需要你进一步缩小！！！   要充分考虑哪些地方需要初始化！！！ 错在只初始化上行
                // 错误行：for (int j = 0; j <= n; j++) {   导致target[j-1]越界
                for (int j = 1; j <= n; j++) {
                    if (chs[i - 1] == target[j - 1]) {
                        dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];  // 要字符一套方案， 不要字符一套方案
                    } else {
                        dp[i][j] = dp[i - 1][j];  // 只有不要字符 这一套方案
                    }
                }
            }
            return dp[m][n];
        }
    }



    /** 二维前缀DP，时间复杂度O(MN)，额外空间O(MN)。 */
    public static class RecommendedSolution {

        public int numDistinct(String s, String t) {
            char[] source = s.toCharArray();
            char[] target = t.toCharArray();
            long[][] dp = new long[source.length + 1][target.length + 1];

            for (int sourceLength = 0; sourceLength <= source.length; sourceLength++) {
                dp[sourceLength][0] = 1;
            }

            for (int sourceLength = 1; sourceLength <= source.length; sourceLength++) {
                for (int targetLength = 1; targetLength <= target.length; targetLength++) {
                    // 不使用当前source字符。
                    dp[sourceLength][targetLength] = dp[sourceLength - 1][targetLength];

                    if (source[sourceLength - 1] == target[targetLength - 1]) {
                        // 使用当前source字符匹配当前target字符。
                        dp[sourceLength][targetLength]
                                += dp[sourceLength - 1][targetLength - 1];
                    }
                }
            }
            return (int) dp[source.length][target.length];
        }
    }

    /**
     * 一维空间压缩，额外空间O(N)。targetLength必须倒序，确保读取的是尚未被当前source字符
     * 更新的上一行状态，避免同一个source字符在一轮内被重复使用。
     */
    public static class SpaceOptimizedSolution {

        public int numDistinct(String s, String t) {
            char[] source = s.toCharArray();
            char[] target = t.toCharArray();
            long[] dp = new long[target.length + 1];
            dp[0] = 1;

            for (int sourceIndex = 0; sourceIndex < source.length; sourceIndex++) {
                int maxTargetLength = Math.min(sourceIndex + 1, target.length);
                for (int targetLength = maxTargetLength; targetLength >= 1; targetLength--) {
                    if (source[sourceIndex] == target[targetLength - 1]) {
                        dp[targetLength] += dp[targetLength - 1];
                    }
                }
            }
            return (int) dp[target.length];
        }
    }
}
