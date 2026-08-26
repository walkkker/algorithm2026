package frequence.双指针.SlidingWindow;

import java.util.*;
/**
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长 子串 的长度。
 *
 * <p><b>专题分类：</b>连续子串+可变长度滑动窗口，不属于DP。专题索引参见
 * {@code frequence/substringandsubsequence/子串与子序列区别.md}。
 *
 * TODO：【注意】本题s的元素范围是 s 由英文字母、数字、符号和空格组成。
 *
 * <p>滑动窗口核心不变量：
 * <pre>{@code
 * 每次更新答案之前，[L, R] 中没有重复字符。
 * }</pre>
 * R 加入窗口之前，原窗口已经合法，因此加入后唯一可能重复的字符就是 chs[R]。
 * 如果 chs[R] 重复，就持续移动 L，并删除左侧字符，直到 chs[R] 的词频恢复为 1。
 *
 * <p>虽然代码结构是 for + while，但 R 最多右移 N 次，L 最多右移 N 次，
 * 因此总时间复杂度为 O(N)。
 *
 * <p>字符范围与计数容器的详细选型见：
 * {@code frequence/字符集与计数容器选择.md}。
 */
public class Q3_LongestSubstringWithoutRepeatingCharacters {

    /**
     * 下面版本是错误的。
     * TODO：【错误】错误的核心是， 当R右移，导致窗口出现重复字符时（chs[R]词频变成2），这个chs[R]不一定对应窗口内的哪一个位置。所以L需要while移动，直到 chs[R]的词频回到1.
     * TODO：【错误】int[26] 和 ch - 'a' 只支持小写字母，不符合本题包含大写字母、数字、符号和空格的字符范围。
     *
     * @param s
     * @return
     */
    public int IncorrectVersionLengthOfLongestSubstring(String s) {
        int ans = 0;
        char[] chs = s.toCharArray();
        int[] count = new int[26];
        int L = 0;
        for (int R = 0; R < chs.length; R++) {
            count[chs[R] - 'a']++;

            // TODO: 【错误】 !=0 不代表重复，只代表存在 （一个或两个都有可能）  if (count[chs[L] - 'a'] != 0) {
            if (count[chs[L] - 'a'] == 2) {
                count[chs[L] - 'a']--;
                L++;
            }

            ans = Math.max(ans, R - L + 1);
        }
        return ans;
    }

    /**
     * HashMap 版本。
     * Character 作为 key，Integer 记录字符在当前窗口中的出现次数。
     *
     * <p>平均时间复杂度 O(N)，额外空间复杂度 O(min(N, 字符集大小))。
     *
     * @param s
     * @return 不含重复字符的最长子串长度
     */
    public int myLengthOfLongestSubstring(String s) {
        int ans = 0;
        char[] chs = s.toCharArray();
        HashMap<Character, Integer> map = new HashMap<>();
        int L = 0;
        for (int R = 0; R < chs.length; R++) {
            // 将R位置字符加入窗口。保留分支写法，明确区分首次出现和重复出现。
            if (!map.containsKey(chs[R])) {
                map.put(chs[R], 1);
            } else {
                map.put(chs[R], map.get(chs[R]) + 1);
            }

            // 加入chs[R]之前窗口合法，所以新字符的词频最多从1变成2。
            if (map.get(chs[R]) == 2) {
                // chs[R]可能在窗口中的任意位置重复，必须持续移动L，不能只删除一次。
                while (map.get(chs[R]) == 2) {
                    map.put(chs[L], map.get(chs[L]) - 1);
                    L++;
                }
            }

            // 此时[L, R]已经恢复为无重复字符窗口，才能更新答案。
            ans = Math.max(ans, R - L + 1);
        }
        return ans;
    }

    /**
     * ASCII 计数数组版本。
     *
     * <p>本题字符由英文字母、数字、符号和空格组成，可以直接使用字符的 ASCII
     * 编码作为 count 下标。相比 HashMap，该版本避免了哈希计算以及 Character、
     * Integer 的装箱，常数开销更小。
     *
     * <p>时间复杂度 O(N)，额外空间复杂度 O(128)，即 O(1)。
     *
     * @param s 仅包含 ASCII 字符的字符串
     * @return 不含重复字符的最长子串长度
     */
    public int asciiLengthOfLongestSubstring(String s) {
        int ans = 0;
        char[] chs = s.toCharArray();
        int[] count = new int[128];
        int L = 0;

        for (int R = 0; R < chs.length; R++) {
            // char可以直接作为int下标，表示对应ASCII字符在窗口中的词频。
            count[chs[R]]++;

            // 新加入的chs[R]是当前唯一可能重复的字符。
            while (count[chs[R]] > 1) {
                count[chs[L]]--;
                L++;
            }

            // 窗口恢复合法后再统计长度。
            ans = Math.max(ans, R - L + 1);
        }

        return ans;
    }
}
