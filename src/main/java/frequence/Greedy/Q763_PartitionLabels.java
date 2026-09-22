package frequence.Greedy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 763. 划分字母区间
 *
 * <p>把字符串划分为尽可能多的片段，使每个字母最多出现在一个片段中，返回每个片段的长度。
 *
 * <p>当前实现来自LeetCode已保存代码。{@code count}维护每个字母在尚未扫描部分中的剩余次数，
 * {@code set}记录当前及历史片段已经出现过的字母。当集合中所有字母剩余次数均为0时，当前
 * 位置就是一个合法且最早的切分点。
 *
 * <p>题目字符集固定为26个小写字母，{@code isClear}每次最多检查26项，所以总体时间复杂度
 * 仍为{@code O(N)}。含toCharArray的版本额外空间为O(N)，最后位置法使用charAt可达O(1)。
 * 不计返回结果。详见同目录《划分字母区间_剩余频次与最后位置.md》。
 */
public class Q763_PartitionLabels {

    public static class SolutionReviewed20260922 {
        /**
         * 用户思路：尽可能多地划分区间，统计词频；当前区段所有字符词频为0时收集。
         *
         * <p>准确含义：count是尚未扫描后缀中的剩余字符频次，set是当前区段已出现字符。
         * 先将当前字符加入set并扣减count，再检查当前区段的全部字符是否在后面都不出现。
         * 若都结束，就可以切分并清空set，开始下一段。
         *
         * <p>贪心证明：这是最早合法切分点，该段字符不会影响后缀；任何跨过这个点的合法划分
         * 都可以在此再切一刀而不破坏约束，因此立即切分不会减少片段数。
         * isClear最多检查26个字符，时间O(26N)=O(N)，已经是渐近最优时间。
         * TODO: 【空间】toCharArray分配O(N)空间，不能把这个版本记成O(1)。
         */
        public List<Integer> partitionLabels(String s) {
            List<Integer> ans = new ArrayList<>();
            Set<Character> set = new HashSet<>();
            char[] chs = s.toCharArray();
            int[] count = new int[26];
            // 1. 统计所有词频；扫描中递减后，变成尚未扫描后缀的频次。
            for (char c : chs) {
                count[c - 'a']++;
            }
            int letters = 0;
            for (int i = 0; i < chs.length; i++) {
                letters++;
                set.add(chs[i]);
                count[chs[i] - 'a']--;
                if (isClear(set, count)) {
                    ans.add(letters);
                    letters = 0;
                    set.clear();
                }
            }
            return ans;
        }

        /** 逐个检查当前段字符：只要一个字符还会在后面出现，就不能切分。 */
        private boolean isClear(Set<Character> set, int[] count) {
            for (char c : set) {
                if (count[c - 'a'] != 0) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class LastOccurrenceSolution {
        /**
         * 【核心转换】不反复询问“每个字符是否结束”，而是记录“每个字符要求本段至少延伸到哪里”。
         *
         * <p>last有26个槽，last[0]对应a，last[1]对应b，直到last[25]对应z。
         * s.charAt(i)-'a'只负责把字符映射到槽；槽中存的是它在原字符串最后出现的下标，不是频次。
         * 从左到右扫描并反复赋值last[c-'a']=i，最终保留下标最大的那次出现。
         * 未出现字母的默认0不会影响答案，因为第二次扫描只查询真正遇到的字符。
         *
         * <p>若当前段出现a，且a最后在位置8出现，则本段必须至少覆盖8，否则a会跨段。
         * 扫描到新字符b时，若b最后在10出现，边界必须继续扩到10。
         * 因此end始终是当前段已扫描字符的最后位置的最大值，不能只覆盖赋值成当前字符的last。
         *
         * <p>当i==end，当前段所有字符都已完整覆盖，可以在最早合法位置切分。
         * 这等价于用户isClear返回true，但把最多26项的反复检查压缩为一个最大值边界。
         * 时间O(N)，额外空间O(1)，不计输出。使用charAt避免额外复制整个字符串。
         */
        public List<Integer> partitionLabels(String s) {
            int[] last = new int[26];
            for (int i = 0; i < s.length(); i++) {
                last[s.charAt(i) - 'a'] = i;
            }
            List<Integer> ans = new ArrayList<>();
            int start = 0;
            int end = 0;
            for (int i = 0; i < s.length(); i++) {
                // 区段内每个字符都提出最低覆盖要求，必须满足所有要求，所以取最大值。
                end = Math.max(end, last[s.charAt(i) - 'a']);
                if (i == end) {
                    ans.add(end - start + 1);
                    start = i + 1;
                }
            }
            return ans;
        }
    }

    public static class Solution {

        public List<Integer> partitionLabels(String s) {
            int[] count = new int[26];
            char[] chars = s.toCharArray();
            for (char current : chars) {
                count[current - 'a']++;
            }

            int letters = 0;
            Set<Character> set = new HashSet<>();
            List<Integer> ans = new ArrayList<>();
            for (char current : chars) {
                letters++;
                set.add(current);
                count[current - 'a']--;
                if (isClear(set, count)) {
                    ans.add(letters);
                    letters = 0;
                }
            }
            return ans;
        }

        public boolean isClear(Set<Character> set, int[] count) {
            // TODO: Set直接使用增强for遍历；Map才使用keySet()或entrySet()。
            for (char current : set) {
                if (count[current - 'a'] != 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
