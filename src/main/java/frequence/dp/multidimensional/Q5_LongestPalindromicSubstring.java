package frequence.dp.multidimensional;

/**
 * 5. 最长回文子串
 *
 * <p>返回字符串中最长的回文子串。子串必须连续。
 *
 * <p><b>DP类型：</b>区间DP实现的状态拓扑属于“单序列区间DP”，格子保存区间回文可行性，
 * 再从全部可行区间中维护最大长度。Manacher实现不属于DP。
 *
 * <p>本题放在多维DP章节时，对应的是区间DP：{@code dp[left][right]}表示闭区间
 * {@code [left, right]}是否为回文串。但浏览器中的原实现使用Manacher算法，它不是DP，时间复杂度
 * O(N)，比区间DP的O(N^2)更优。为了保留你的实现并说明本章节模型，文件同时提供两种方法。
 *
 * <p>详细分类参见同目录《多维DP核心总结.md》的“单序列区间模型”，以及专题
 * {@code frequence/substringandsubsequence/子串与子序列专题.md}。
 */
public class Q5_LongestPalindromicSubstring {

    /** 浏览器中的Manacher实现，保留原错误行并在其下给出正确语句。 */
    public static class ManacherSolution {

        public String longestPalindrome(String s) {
            char[] manacherChars = manacherString(s);
            int[] radius = new int[manacherChars.length];
            int center = 0;
            int rightBoundary = 0;
            int maxRadius = 0;
            int maxCenter = 0;

            for (int i = 0; i < manacherChars.length; i++) {
                // TODO: 【原语法错误】Math.min的两个实参之间必须使用逗号，不能使用冒号。
                // 错误行：radius[i] = i >= rightBoundary
                //         ? 1 : Math.min(radius[2 * center - i] : rightBoundary - i);
                radius[i] = i >= rightBoundary
                        ? 1 : Math.min(radius[2 * center - i], rightBoundary - i);

                while (i - radius[i] >= 0
                        && i + radius[i] < manacherChars.length
                        && manacherChars[i - radius[i]] == manacherChars[i + radius[i]]) {
                    radius[i]++;
                }

                if (i + radius[i] > rightBoundary) {
                    center = i;
                    rightBoundary = i + radius[i];
                }
                if (radius[i] > maxRadius) {
                    maxCenter = i;
                    maxRadius = radius[i];
                }
            }

            // 加工串中的回文左边界为maxCenter - maxRadius + 1。
            // 每组“# + 原字符”占两个位置，因此除以2可映射为原串起点。
            int originalStart = (maxCenter - maxRadius + 1) / 2;
            int originalLength = maxRadius - 1;
            return s.substring(originalStart, originalStart + originalLength);
        }

        private char[] manacherString(String s) {
            char[] source = s.toCharArray();
            char[] transformed = new char[source.length * 2 + 1];
            for (int i = 0; i < transformed.length; i++) {
                transformed[i] = (i & 1) == 0 ? '#' : source[i / 2];
            }
            // TODO: 【原遗漏】原方法声明返回char[]，构造完成后必须返回transformed。
            return transformed;
        }
    }

    /**
     * 多维DP章节对应的标准区间DP。
     *
     * <p>{@code dp[left][right]}表示s[left..right]是否为回文串。两端字符相同，并且内部区间
     * 是回文串时，当前区间才是回文串。长度不超过3时，两端相等即可，避免访问空的内部区间。
     */
    public static class IntervalDpSolution {

        public String longestPalindrome(String s) {
            char[] chars = s.toCharArray();
            boolean[][] dp = new boolean[chars.length][chars.length];
            int bestStart = 0;
            int bestLength = 1;

            // dp[left][right]依赖dp[left + 1][right - 1]，所以left必须从大到小。
            for (int left = chars.length - 1; left >= 0; left--) {
                for (int right = left; right < chars.length; right++) {
                    if (chars[left] == chars[right]
                            && (right - left <= 2 || dp[left + 1][right - 1])) {
                        dp[left][right] = true;
                        int currentLength = right - left + 1;
                        if (currentLength > bestLength) {
                            bestStart = left;
                            bestLength = currentLength;
                        }
                    }
                }
            }
            return s.substring(bestStart, bestStart + bestLength);
        }
    }
}
