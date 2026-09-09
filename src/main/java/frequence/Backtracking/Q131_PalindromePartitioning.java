package frequence.Backtracking;

import java.util.*;

/**
 * 131. 分割回文串
 *
 * <p>给定一个字符串{@code s}，将其分割成若干个子串，使每个子串都是回文串。返回所有可能的
 * 分割方案。
 *
 * <p><b>专题分类：</b>回溯枚举字符串切分点，使用回文子串判断作为分支合法性条件；可用区间DP
 * 预处理回文性。专题索引参见{@code frequence/substringandsubsequence/子串与子序列区别.md}。
 *
 * <p><b>当前版本的递归树：</b>{@code index}表示下一段子串的起始位置；当前层枚举
 * {@code end = index..s.length()-1}，每个回文区间{@code [index, end]}都是当前层的一种
 * 合法选择，下一层从{@code end + 1}继续分割。
 *
 * <p><b>当前版本正确，但存在重复计算：</b>{@code isPalindrome(s, index, end)}每次需要
 * 线性扫描区间，同一个回文区间可能在递归过程中被重复判断。宽松时间上界可以写成
 * {@code O(N^2 * 2^N)}。
 *
 * <p><b>优化方案：</b>先使用{@code boolean[][] palindrome}预处理所有回文区间，时间和
 * 空间均为{@code O(N^2)}；回溯时可以在{@code O(1)}时间判断区间是否为回文串。最坏情况下
 * 分割方案数量本身为指数级，回溯和复制结果需要{@code O(N * 2^N)}时间。
 */
public class Q131_PalindromePartitioning {

    /**
     * 2026-09-09 复盘版本：回文区间DP预处理 + 回溯枚举切分位置。
     *
     * <p>这道题是在看过答案后完成的，复习时应重点独立说明两个状态：
     * {@code dp[start][end]}回答当前候选子串是否为回文串；{@code start}回答回溯下一段
     * 应该从哪里开始。DP只负责把回文判断降为O(1)，不会代替对所有分割方案的回溯枚举。
     *
     * <p>{@code end}是当前递归层的选择变量。选择闭区间{@code [start, end]}后，下一层必须
     * 从{@code end + 1}继续；返回当前层后删除路径末尾元素，才能枚举下一个兄弟分支。
     */
    class SolutionReviewed20260909 {

        /**
         * 【错误点-注意】是boolean[][] dp, 不是 int[][] dp。
         *
         * 【解题思路】
         * 1. dp解回文子串问题，但是保留boolean[][]，实现O(1)查询[start, end]是否为回文子串。
         * 2. 递归回溯，核心是内部进行枚举（具体为end在[start, len)的所有位置全部枚举），
         *    然后检查是否回文。
         * 2.1 注意list记录选择 + 恢复现场。
         */
        public List<List<String>> partition(String s) {
            char[] chs = s.toCharArray();
            int len = chs.length;
            boolean[][] dp = new boolean[len][len];
            for (int i = len - 1; i >= 0; i--) {
                for (int j = i; j < len; j++) {
                    // TODO: 【AI补充-正确性】j-i<=2等价于区间长度<=3。
                    // 长度为1或2时没有内部区间；长度为3且两端相等时，中间单字符天然回文。
                    // 短区间必须先截断，才能避免访问dp[i+1][j-1]时出现非法下标。
                    dp[i][j] = (chs[i] == chs[j]) && (j - i <= 2 || dp[i + 1][j - 1]);
                }
            }
            List<String> tmp = new ArrayList<>();
            List<List<String>> ans = new ArrayList<>();
            process(chs, 0, tmp, dp, s, ans);
            return ans;
        }

        private void process(
                char[] chs,
                int start,
                List<String> list,
                boolean[][] dp,
                String s,
                List<List<String>> ans) {

            if (start == chs.length) {
                // TODO: 【注意】必须保存路径快照，不能直接ans.add(list)。
                ans.add(new ArrayList<>(list));

                // TODO: 【错误-遗漏】base case收集答案后应立即return。
                // 当前代码即使不return，下面的for也不会执行，因此结果暂时不受影响；
                // 但终止条件的语义应当完整，避免以后在for循环前增加代码时产生越界或重复处理。
                return;
            }
            for (int end = start; end < chs.length; end++) {
                if (dp[start][end]) {
                    // 当前层选择闭区间[start, end]，下一层从end+1开始。
                    list.add(s.substring(start, end + 1));
                    process(chs, end + 1, list, dp, s, ans);
                    // TODO: 【回溯核心】list是所有递归分支共享的可变对象，返回后必须恢复现场。
                    list.remove(list.size() - 1);
                }
            }
        }
    }

    public List<List<String>> partition(String s) {
        List<List<String>> ans = new ArrayList<>();
        List<String> tmp = new ArrayList<>();
        process(s, 0, tmp, ans);
        return ans;
    }

    public void process(String s, int index, List<String> tmp, List<List<String>> ans) {
        // index走到字符串末尾，说明整条路径已经完成一次合法分割。
        if (index == s.length()) {
            ans.add(new ArrayList<>(tmp));  // TODO: 这次一定记着了，不能直接add(tmp)，要加一个快照
            // TODO: 【关键】这是终止型base case，收集答案后必须return，
            // 否则会继续执行下面的区间枚举逻辑。
            return;
        }
        // 当前层枚举从index开始的所有候选结束位置。
        for (int end = index; end < s.length(); end++) {
            // TODO: 【可优化】当前判断正确，但会重复扫描字符区间。
            // 推荐版本预处理palindrome[index][end]，将这里的判断降为O(1)。
            if (isPalindrome(s, index, end)) {
                // 做选择：把当前回文子串加入共享路径。
                tmp.add(s.substring(index, end + 1));
                process(s, end + 1, tmp, ans);
                // 恢复现场：下一个end对应当前递归节点的兄弟分支。
                tmp.remove(tmp.size() - 1);
            }
        }
    }

    private boolean isPalindrome(String s, int l, int r) {
        while (l <= r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }


    /**
     * 推荐版本：动态规划预处理回文区间 + 回溯枚举分割方案。
     *
     * <p>状态定义：
     * <pre>
     * palindrome[left][right]
     *     = 闭区间[left, right]对应的子字符串是否为回文串
     * </pre>
     *
     * <p>状态转移：
     * <pre>
     * chs[left] == chs[right]
     * 并且：
     *     区间长度不超过3
     *     或者palindrome[left + 1][right - 1]为true
     * </pre>
     *
     * <p>{@code left}必须从右向左遍历，因为当前状态依赖下一行的
     * {@code palindrome[left + 1][right - 1]}。
     */
    class SolutionWithPalindromeDP {

        public List<List<String>> partition(String s) {
            char[] chs = s.toCharArray();
            boolean[][] palindrome = buildPalindrome(chs);

            List<List<String>> ans = new ArrayList<>();
            List<String> path = new ArrayList<>();
            process(s, 0, palindrome, path, ans);
            return ans;
        }

        private void process(
                String s,
                int index,
                boolean[][] palindrome,
                List<String> path,
                List<List<String>> ans) {

            if (index == s.length()) {
                ans.add(new ArrayList<>(path));
                return;
            }

            for (int end = index; end < s.length(); end++) {
                // 预处理后，回文区间判断为O(1)。
                if (!palindrome[index][end]) {
                    continue;
                }

                path.add(s.substring(index, end + 1));
                process(s, end + 1, palindrome, path, ans);
                path.remove(path.size() - 1);
            }
        }

        private boolean[][] buildPalindrome(char[] chs) {
            int n = chs.length;
            boolean[][] palindrome = new boolean[n][n];

            // 必须从右向左填写left，保证依赖的内部区间已经计算完成。
            for (int left = n - 1; left >= 0; left--) {
                for (int right = left; right < n; right++) {
                    palindrome[left][right] =
                            chs[left] == chs[right]
                                    && (right - left <= 2
                                    || palindrome[left + 1][right - 1]);
                }
            }

            return palindrome;
        }
    }
}
