package dp;

/**
 * TODO：什么模型？ 范围尝试模型： s1的范围 与 s2的范围 中存在的最长公共子串。  对于范围尝试模型，就是缩小范围。
 *      i 的含义代表i-end,  j代表的含义就是j-end  =》 这就与 『范围』对应上了
 *      =》 范围尝试模型  最开始的位置&&return的也是最初的dp状态=》dp[0][0]（这两个范围的最长xx）=》通过思考这个dp的语义进一步帮助建立后面的思路 =》 就可以快速相处如何拆成子问题了（修改/缩小问题规模）
 *                                                                       |
 * TODO: 依然还是从 （i,j）代表起始位置到终点 的含义。  这个概念是通用的啊！！！    |
 * TODO: (i,j) 代表 s1 [i,end] 与 s2 [j,end] 这两个范围内的最长公共子序列长度！！！
 * <p>
 * 由此，就可以拆解了： -> 依赖方向确定 -> 推数方向确定 -> base case 的边确定
 * （1）s1[i] == s2[j] -> 1 + [i+1][j+1]
 * (2) s1[i] != s2[j] -> Math.max(缩小s1范围后的最大值， 缩小s2范围后的最大值)
 * <p>
 * TODO：【重点】由此， review的结果是 dp的本质也是要寻找 缩小问题规模的方式。 对于范围尝试模型，就是缩小范围。
 * TODO： BaseCase 往往是n+1,m+1，因为越界的位置，可以直接得出答案。
 *       还有一个，就是要创造『最好比较』的base case。本题中，只要i到达了s1.length，就是说这个位置没有值了。那么这一行都是base case=0（没有字母与任何范围的s2，都是0）-> (base case的确定当然也是根据 依赖方向可以直接看出来)
 * <p>
 * 这个问题leetcode上可以直接测
 * 链接：https://leetcode.com/problems/longest-common-subsequence/
 */
public class LongestCommonSubsequence {
    class Solution {
        public int longestCommonSubsequence(String s1, String s2) {
            char[] chs1 = s1.toCharArray();
            char[] chs2 = s2.toCharArray();
            int m = chs1.length;
            int n = chs2.length;
            int[][] dp = new int[m + 1][n + 1];
            for (int i = m - 1; i >= 0; i--) {
                for (int j = n - 1; j >= 0; j--) {
                    if (chs1[i] == chs2[j]) {
                        dp[i][j] = 1 + dp[i + 1][j + 1];
                    } else {
                        dp[i][j] = Math.max(dp[i + 1][j], dp[i][j + 1]);
                    }
                }
            }
            return dp[0][0];
        }
    }

    public static int dpTest(String s1, String s2) {
        char[] chs1 = s1.toCharArray();
        char[] chs2 = s2.toCharArray();
        int len1 = chs1.length;
        int len2 = chs2.length;
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = len1 - 1; i >= 0; i--) {
            for (int j = len2 - 1; j >= 0; j--) {
                int ans = 0;
                if (chs1[i] != chs2[j]) {
                    ans = Math.max(dp[i + 1][j], dp[i][j + 1]);
                } else {
                    ans = 1 + dp[i + 1][j + 1];
                }
                dp[i][j] = ans;
            }
        }
        return dp[0][0];
    }
}
