package frequence.双指针.SlidingWindow;

import java.util.*;

/**
 * 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
 * <p>
 * Note: s 和 p 仅包含小写字母
 *
 * <p><b>专题分类：</b>连续子串+固定长度滑动窗口，不属于DP。专题索引参见
 * {@code frequence/substringandsubsequence/子串与子序列区别.md}。
 *
 *
 * （1）Anagrams1 不是最优解。 问题出在isMatch的比较上
 * （2）Anagrams2 是最优解O(N)。 通过双指针在滑动时计算 窗口词频（O(1)）。 实现isMatch的词频比较，O(26)常数。 从而总体最优解
 *
 *
 */
public class Q438 {

    /**
     * 这个版本的时间复杂度是O(N*K)  因为每次比较isMatch()时间复杂度为O(K)，K为匹配串的长度。
     *
     * @param s
     * @param p
     * @return
     */
    public List<Integer> findAnagrams1(String s, String p) {
        char[] chs = s.toCharArray();
        char[] match = p.toCharArray();
        int k = match.length;
        int L = 0;
        List<Integer> ans = new ArrayList<>();
        for (int R = 0; R < chs.length; R++) {
            if (R - L + 1 > k) {
                L++;
            }

            if (R - L + 1 == k) {
                if (isMatch(chs, L, R, match)) {
                    ans.add(L);
                }
            }
        }
        return ans;

    }

    public boolean isMatch(char[] chs1, int l, int r, char[] chs2) {

        int len = r - l + 1;
        int[] count = new int[26];
        for (int i = l; i <= r; i++) {
            count[chs1[i] - 'a']++;
        }

        for (int i = 0; i < len; i++) {
            count[chs2[i] - 'a']--;
        }

        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 这是最优解。 把比较isMatch() 方法控制在O(26) -> 从而总时间复杂度为O(N)
     * @param s
     * @param p
     * @return
     */
    public List<Integer> findAnagrams2(String s, String p) {
        char[] chs = s.toCharArray();
        char[] match = p.toCharArray();
        int[] count1 = new int[26];
        int[] count2 = new int[26];
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < match.length; i++) {
            count2[match[i] - 'a']++;
        }

        int L = 0;
        for (int R = 0; R < chs.length; R++) {
            count1[chs[R] - 'a']++;

            if (R - L + 1 > match.length) {
                count1[chs[L] - 'a']--;
                L++;
            }

            if (R - L + 1 == match.length && isMatch(count1, count2)) {
                ans.add(L);
            }
        }
        return ans;
    }

    public boolean isMatch(int[] count1, int[] count2) {
        for (int i = 0; i < count1.length; i++) {
            if (count1[i] != count2[i]) {
                return false;
            }
        }
        return true;
    }
}
