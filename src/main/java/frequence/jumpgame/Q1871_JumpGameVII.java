package frequence.jumpgame;

/**
 * 1871. 跳跃游戏VII：下标可达性DP + 滑动窗口优化转移。
 * 用户复习版本与前缀和对照版本。详见同目录《跳跃游戏系列.md》。
 */
public class Q1871_JumpGameVII {

    public static class SolutionReviewed20260922 {
        /**
         * 用户理解：minJump、maxJump，chs[0]='0'，结尾型DP。
         *
         * <p><b>更准确的状态定义：</b>dp[i]表示从下标0出发，能否实际到达下标i。
         * 它是下标可达性DP，不是前i个字符的长度状态。字符为0只表示允许落脚，不保证可达。
         * dp[0]=true由题目起点条件确定。
         *
         * <p><b>前驱窗口：</b>前驱j必须满足minJump <= i-j <= maxJump，
         * 即i-maxJump <= j <= i-minJump。有效窗口是[max(0,i-maxJump), i-minJump]，
         * 右端小于0时为空。valid统计这个窗口中dp[j]==true的位置数，不是字符0的数量。
         *
         * <p><b>一出一进：</b>目标从i-1移动到i时，原始窗口从
         * [i-1-maxJump,i-1-minJump]变成[i-maxJump,i-minJump]。
         * left=i-maxJump是新左边界，离开的是left-1，而不是left；right=i-minJump是新进入位置。
         * 先出后进正确，两次更新完成后valid才对应当前窗口。中间临时值不用于状态转移。
         *
         * <p><b>转移必须同时满足两个条件：</b>当前字符为0，并且窗口内存在实际可达前驱。
         * dp[j]已经包含前驱字符为0和前驱可达这两层条件，所以入窗时只检查dp[j]即可。
         * 即使当前字符是1，窗口也必须照常移动，不能直接continue跳过计数更新。
         *
         * <p>TODO: 【可精简，不是错误】布尔表达式后的? true : false多余；保留用户原写法。
         * minJump>=1保证所有前驱下标小于i，读取的dp都已计算。
         * 时间O(N)，额外空间O(N)，包括toCharArray和dp数组。
         */
        public boolean canReach(String s, int minJump, int maxJump) {
            char[] chs = s.toCharArray();
            if (chs[chs.length - 1] != '0') {
                return false;
            }
            boolean[] dp = new boolean[chs.length];
            int left = 0;
            int right = 0;
            int valid = 0;
            dp[0] = true;

            // TODO: 【重点】滑动窗口：left管出，right管进。
            for (int i = 1; i < dp.length; i++) {
                left = i - maxJump;
                // 新左边界的前一个位置离开；仅移除实际可达位置的贡献。
                if (left - 1 >= 0 && dp[left - 1]) {
                    valid--;
                }
                right = i - minJump;
                // 新右边界进入；dp[right]包含“允许落脚且实际可达”。
                if (right >= 0 && dp[right]) {
                    valid++;
                }
                // TODO: 【错误项】状态转移需要满足两个条件，不能只关注窗口内valid。
                // 错误行：dp[i] = valid > 0 ? true : false;
                // 可简写为：dp[i] = chs[i] == '0' && valid > 0;
                dp[i] = chs[i] == '0' && valid > 0 ? true : false;
            }
            return dp[dp.length - 1];
        }
    }

    /** 前缀和对照：状态与转移不变，只改变前驱区间的计数查询方式。 */
    public static class PrefixSumSolution {
        public boolean canReach(String s, int minJump, int maxJump) {
            int n = s.length();
            boolean[] dp = new boolean[n];
            // prefix[t]表示dp[0..t-1]中true的数量，不是原字符串中0的数量。
            int[] prefix = new int[n + 1];
            dp[0] = true;
            prefix[1] = 1;
            for (int i = 1; i < n; i++) {
                int left = Math.max(0, i - maxJump);
                int right = i - minJump;
                if (s.charAt(i) == '0' && left <= right) {
                    dp[i] = prefix[right + 1] - prefix[left] > 0;
                }
                prefix[i + 1] = prefix[i] + (dp[i] ? 1 : 0);
            }
            return dp[n - 1];
        }
    }
}
