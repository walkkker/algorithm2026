package frequence.dp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 139. 单词拆分
 *
 * <p>判断字符串s能否由字典中的一个或多个单词拼接而成，字典中的单词可以重复使用。
 *
 * <p><b>DP类型：</b>状态拓扑属于“一维前缀切分DP”，状态值语义属于“可行性DP”。
 * {@code dp[end]}回答前缀{@code s[0, end)}能否完成合法切分。
 *
 * <p><b>原实现：</b>使用index作为唯一递归参数并记忆化，状态设计正确；但为了回答布尔问题，
 * 额外计算了最少单词数量，并手写逐字符比较，状态和值域都比必要情况复杂。
 *
 * <p><b>正确思路：</b>定义{@code dp[i]}表示前缀{@code s[0,i)}能否被拆分。枚举最后一个单词的
 * 起点j，如果{@code dp[j]}成立且{@code s[j,i)}属于字典，则{@code dp[i]}成立。
 *
 * <p>本题既属于一维前缀DP，也具有完全背包的“单词可重复使用”特征。归纳参见同目录
 * 《动态规划题型共性总结.md》的“字符串切分DP”章节。
 *
 * <p><b>一维DP复盘：</b>原递归{@code process(index)}已经说明唯一变化参数是字符串位置，适合
 * 一维状态；问题在于返回值被设计成“最少单词数”，与题目只问可行性不一致，从而额外引入
 * {@code -1}、无穷大和最小值比较。更贴合题意的状态是：{@code dp[end]}表示前缀
 * {@code s[0,end)}能否完整拆分。固定end后枚举最后一个单词的起点start。完整方法论参见
 * 同目录《一维DP核心总结.md》。
 *
 * <p>子串与子序列的连续性对照参见
 * {@code frequence/substringandsubsequence/子串与子序列专题.md}。
 */
public class Q139_WordBreak {

    public static class OriginalSolution {

        private HashMap<Integer, Integer> map;

        public boolean wordBreak(String s, List<String> wordDict) {
            map = new HashMap<>();
            int ans = process(s, 0, wordDict);
            return ans == -1 ? false : true;
        }

        private int process(String s, int index, List<String> wordDict) {
            if (map.containsKey(index)) {
                return map.get(index);
            }

            if (index == s.length()) {
                map.put(index, 0);
                return 0;
            }

            // TODO: 【状态过重】题目只要求可行性，这里计算最少单词数不是错误，但做了额外工作。
            int ans = Integer.MAX_VALUE;
            for (String word : wordDict) {
                int len = word.length();
                int start = index;
                int end = start + len - 1;
                if (isEqual(s, start, end, word)) {
                    int tmp = process(s, index + len, wordDict);
                    // TODO: 【原错误】临时无穷大Integer.MAX_VALUE与递归无解值-1必须严格区分。
                    // 错误判断：if (tmp != Integer.MAX_VALUE)
                    if (tmp != -1) {
                        ans = Math.min(ans, 1 + tmp);
                    }
                }
            }
            ans = ans == Integer.MAX_VALUE ? -1 : ans;
            map.put(index, ans);
            return ans;
        }

        private boolean isEqual(String s, int start, int end, String word) {
            if (end >= s.length()) {
                return false;
            }
            for (int i = start; i <= end; i++) {
                // TODO: 本题要求原字符串片段与word逐位置相等，不是判断异位词。
                if (s.charAt(i) != word.charAt(i - start)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class RecommendedSolution {

        public boolean wordBreak(String s, List<String> wordDict) {
            Set<String> words = new HashSet<>(wordDict);
            boolean[] dp = new boolean[s.length() + 1];
            // 空前缀不需要任何单词即可完成拆分，是第一个真实单词能够开始转移的基础。
            dp[0] = true;

            for (int end = 1; end <= s.length(); end++) {
                for (int start = 0; start < end; start++) {
                    // 左侧前缀已经可拆分，并且最后一段s[start,end)是单词，整个前缀才可拆分。
                    if (dp[start] && words.contains(s.substring(start, end))) {
                        dp[end] = true;
                        break;
                    }
                }
            }
            return dp[s.length()];
        }
    }
}
