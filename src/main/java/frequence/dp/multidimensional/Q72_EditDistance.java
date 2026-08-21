package frequence.dp.multidimensional;

/**
 * 72. 编辑距离
 *
 * <p>给定两个单词，求将{@code word1}转换为{@code word2}所需的最少操作数。允许对word1
 * 插入、删除或替换一个字符。
 *
 * <p><b>DP类型：</b>状态拓扑属于“双序列前缀DP”，状态值语义属于“最小值DP”。
 *
 * <p><b>状态定义：</b>{@code dp[i][j]}表示将{@code word1[0, i)}转换为
 * {@code word2[0, j)}的最少操作数。i、j表示前缀长度，因此当前字符下标是i-1、j-1。
 *
 * <p><b>边界：</b>{@code dp[i][0] = i}表示删除word1的i个字符；
 * {@code dp[0][j] = j}表示向空串插入word2的j个字符。
 *
 * <p><b>转移：</b>末尾字符相同时无需操作，读取左上角。末尾字符不同时，分别枚举替换、删除、
 * 插入三种最后一步，取最小值。不能根据两个完整字符串的长度关系提前决定操作类型。
 *
 * <p>时间复杂度O(MN)，额外空间O(MN)。详细分类参见同目录《多维DP核心总结.md》的
 * “双序列前缀模型”。
 */
public class Q72_EditDistance {

    /** 浏览器中保存的原实现，保留原错误分析并执行修正后的转移。 */
    public static class OriginalSolution {

        public int minDistance(String word1, String word2) {
            int m = word1.length();
            int n = word2.length();
            int[][] dp = new int[m + 1][n + 1];

            for (int i = 0; i <= m; i++) {
                dp[i][0] = i;
            }
            for (int j = 0; j <= n; j++) {
                dp[0][j] = j;
            }

            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    // TODO: 【原错误1】错误行使用word1.charAt(i)、word2.charAt(j)。
                    // 原因：i、j表示前缀长度而不是字符下标，当前字符必须读取i-1、j-1。
                    if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else {
                        // TODO: 【原错误2】曾按word1.length()与word2.length()的大小，
                        // 只选择替换、插入或删除中的一种操作。
                        // 原因：全局长度不能决定局部前缀的最优最后一步；三种合法来源必须同时比较。
                        int replace = dp[i - 1][j - 1] + 1;
                        int deleteFromWord1 = dp[i - 1][j] + 1;
                        int insertIntoWord1 = dp[i][j - 1] + 1;
                        dp[i][j] = Math.min(replace,
                                Math.min(deleteFromWord1, insertIntoWord1));
                    }
                }
            }
            return dp[m][n];
        }
    }

    /**
     * 同一状态定义的标准命名版本。二维表更适合第一次理解编辑距离，不建议学习阶段直接背空间压缩。
     */
    public static class RecommendedSolution {

        public int minDistance(String word1, String word2) {
            char[] source = word1.toCharArray();
            char[] target = word2.toCharArray();
            int[][] dp = new int[source.length + 1][target.length + 1];

            for (int sourceLength = 1; sourceLength <= source.length; sourceLength++) {
                dp[sourceLength][0] = sourceLength;
            }
            for (int targetLength = 1; targetLength <= target.length; targetLength++) {
                dp[0][targetLength] = targetLength;
            }

            for (int sourceLength = 1; sourceLength <= source.length; sourceLength++) {
                for (int targetLength = 1; targetLength <= target.length; targetLength++) {
                    if (source[sourceLength - 1] == target[targetLength - 1]) {
                        dp[sourceLength][targetLength]
                                = dp[sourceLength - 1][targetLength - 1];
                        continue;
                    }

                    int replace = dp[sourceLength - 1][targetLength - 1];
                    int delete = dp[sourceLength - 1][targetLength];
                    int insert = dp[sourceLength][targetLength - 1];
                    dp[sourceLength][targetLength]
                            = 1 + Math.min(replace, Math.min(delete, insert));
                }
            }
            return dp[source.length][target.length];
        }
    }
}
