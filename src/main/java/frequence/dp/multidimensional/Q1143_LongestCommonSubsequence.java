package frequence.dp.multidimensional;

/**
 * 1143. 最长公共子序列
 *
 * <p>给定两个字符串，返回它们最长公共子序列的长度。子序列可以删除字符，但不能改变剩余字符
 * 的相对顺序。
 *
 * <p><b>DP类型：</b>状态拓扑属于“双序列前缀DP”，状态值语义属于“最大值DP”。
 *
 * <p><b>状态定义：</b>{@code dp[i][j]}表示{@code text1[0, i)}与
 * {@code text2[0, j)}的最长公共子序列长度。这里i、j是前缀长度，不是字符下标，因此当前字符
 * 分别是{@code text1[i - 1]}与{@code text2[j - 1]}，并且需要额外的第0行、第0列表示空前缀。
 *
 * <p><b>状态转移：</b>当前字符相等时可以共同进入答案，读取左上角；不相等时至少舍弃其中一个
 * 当前字符，取上方与左方的最大值。
 *
 * <p>时间复杂度O(MN)，二维版本额外空间O(MN)。详细分类参见同目录
 * 《多维DP核心总结.md》的“双序列前缀模型”，以及专题
 * {@code frequence/substringandsubsequence/子串与子序列区别.md}。
 */
public class Q1143_LongestCommonSubsequence {

    /** 浏览器中保存的原实现，修正字符下标后逻辑正确。 */
    public static class OriginalSolution {

        public int longestCommonSubsequence(String text1, String text2) {
            char[] chs1 = text1.toCharArray();
            char[] chs2 = text2.toCharArray();
            int[][] dp = new int[chs1.length + 1][chs2.length + 1];

            for (int i = 1; i <= chs1.length; i++) {
                for (int j = 1; j <= chs2.length; j++) {
                    // TODO: 【原错误】错误行：if (chs1[i] == chs2[j])。
                    // 原因：i、j表示前缀长度，对应的最后一个字符下标必须减1。
                    if (chs1[i - 1] == chs2[j - 1]) {
                        dp[i][j] = 1 + dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    }
                }
            }
            return dp[chs1.length][chs2.length];
        }
    }

    /** 空间压缩版本，额外空间O(N)。 */
    public static class SpaceOptimizedSolution {

        public int longestCommonSubsequence(String text1, String text2) {
            if (text1.length() < text2.length()) {
                return longestCommonSubsequence(text2, text1);
            }

            char[] longChars = text1.toCharArray();
            char[] shortChars = text2.toCharArray();
            int[] dp = new int[shortChars.length + 1];

            for (int i = 1; i <= longChars.length; i++) {
                int oldLeftUp = 0;
                for (int j = 1; j <= shortChars.length; j++) {
                    int oldUp = dp[j];
                    if (longChars[i - 1] == shortChars[j - 1]) {
                        dp[j] = oldLeftUp + 1;
                    } else {
                        dp[j] = Math.max(dp[j], dp[j - 1]);
                    }
                    oldLeftUp = oldUp;
                }
            }
            return dp[shortChars.length];
        }
    }
}
