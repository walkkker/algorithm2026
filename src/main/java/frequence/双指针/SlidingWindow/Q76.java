package frequence.双指针.SlidingWindow;

/**
 * 因此，对于每个右端点，算法都会找到：
 * 【以R结尾的最短合法窗口】 -> 滑动窗口
 * 再对所有右端点的结果取最小值，就得到全局最短窗口。
 *
 * <p><b>专题分类：</b>连续子串+可变长度滑动窗口，不属于DP。专题索引参见
 * {@code frequence/substringandsubsequence/子串与子序列专题.md}。
 */
public class Q76 {
    // My version TODO：一定要看错误点。 一个是最后【绝对不能忘记】检查ans=Integer.MAX_VALUE，进而返回无效答案；-> 【最终返回什么取决于题目对“不存在答案”的约定】
    //              一个是 String.substring(左闭，右开)
    class Solution {
        public String minWindow(String s, String t) {
            char[] chs = s.toCharArray();
            char[] m = t.toCharArray();

            int[] count1 = new int[128];   // int[128]时，不需要 count[chs[i]-'a'],直接count[chs[i]]。  因为都是ascii码，其范围就是[0,127]
            int[] count2 = new int[128];

            for (int i = 0; i < m.length; i++) {
                count2[m[i]]++;
            }

            int ans = Integer.MAX_VALUE;
            int start = -1;
            int L = 0;
            for (int R = 0; R < chs.length; R++) {
                count1[chs[R]]++;
                // TODO
                while (L <= R && isInclude(count1, count2)) {
                    if (R - L + 1 < ans) {
                        ans = R - L + 1;
                        start = L;
                    }
                    // TODO: 【错误】发现更小的长度时，才更新ans和start
                    // ans = Math.min(ans, R - L + 1);
                    // start = L;
                    count1[chs[L]]--;
                    L++;
                }
            }
            // TODO: 【超级错误】为了比较小，使用ans=Integer.MAX_VALUE初始化。 最后一定要检查ans == Integer.MAX_VALUE。 这种情况属于没有满足要求的答案，没有触发ans赋值，需要特殊处理。
            // 【依然错误】return ans == Integer.MAX_VALUE ? "" : s.substring(L, L + ans - 1);

            // TODO: 【唯一错误-核心错误】检查了半天，错误集中在以下两点
            //        1. 起始位置变量是 start，不是L, fuck!!!
            //        2. 【知识点一定要记牢】String.substring(左闭右开)，右侧是开区间！！！
            return ans == Integer.MAX_VALUE ? "" : s.substring(start, start + ans);
        }

        public boolean isInclude(int[] count1, int[] count2) {
            for (int i = 0; i < 128; i++) {
                if (count1[i] < count2[i]) {
                    return false;
                }
            }
            return true;
        }
    }


    // AI version -> 主要学习规范化思路 和 变量起名
    class SolutionForChatGPTVersion {

        public String minWindow(String s, String t) {
            if (t.length() == 0 || s.length() < t.length()) {
                return "";
            }

            char[] source = s.toCharArray();

            int[] windowCount = new int[128];
            int[] targetCount = new int[128];

            for (int i = 0; i < t.length(); i++) {
                targetCount[t.charAt(i)]++;
            }

            int left = 0;

            // 合法窗口最大长度为s.length()，所以使用s.length() + 1表示尚未找到。
            int minLength = s.length() + 1;
            int bestStart = -1;

            for (int right = 0; right < source.length; right++) {
                // 右侧字符进入窗口。
                windowCount[source[right]]++;

                // 当前窗口覆盖t后，持续收缩左边界，寻找以right结尾的最小覆盖窗口。
                while (left <= right && isCovered(windowCount, targetCount)) {
                    int windowLength = right - left + 1;

                    if (windowLength < minLength) {
                        minLength = windowLength;
                        bestStart = left;
                    }

                    // 左侧字符离开窗口。
                    windowCount[source[left]]--;
                    left++;
                }
            }

            return bestStart == -1
                    ? ""
                    : s.substring(bestStart, bestStart + minLength);
        }

        /**
         * 判断当前窗口是否完全覆盖目标字符串。
         */
        private boolean isCovered(int[] windowCount, int[] targetCount) {
            for (int i = 0; i < targetCount.length; i++) {
                if (windowCount[i] < targetCount[i]) {
                    return false;
                }
            }
            return true;
        }
    }
}
