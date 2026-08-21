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
 * 仍为{@code O(N)}，额外空间复杂度为{@code O(1)}。
 */
public class Q763_PartitionLabels {

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
