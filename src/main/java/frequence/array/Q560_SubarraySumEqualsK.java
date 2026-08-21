package frequence.array;

import java.util.HashMap;

/**
 * 560. 和为 K 的子数组
 *
 * <p>给定整数数组{@code nums}和整数{@code k}，返回和为{@code k}的连续子数组数量。
 *
 * <p>当前实现来自LeetCode已保存代码。核心等式是
 * {@code pre[right] - pre[left] = k}。同一个前缀和值可能出现多次，而每一次出现都代表
 * 一个不同的左边界，因此必须使用{@code HashMap<前缀和, 出现次数>}，不能使用HashSet。
 *
 * <p>时间复杂度{@code O(N)}，额外空间复杂度{@code O(N)}。
 */
public class Q560_SubarraySumEqualsK {

    public static class Solution {

        public int subarraySum(int[] nums, int k) {
            int len = nums.length;
            long[] pre = new long[len];
            pre[0] = nums[0];
            for (int i = 1; i < len; i++) {
                pre[i] = pre[i - 1] + nums[i];
            }

            int ans = 0;
            // HashSet<Long> set = new HashSet<>();
            // TODO: 不能用Set，可能存在多个相同的前缀和，需要把这些左边界全部计数。
            HashMap<Long, Integer> map = new HashMap<>();
            for (int i = 0; i < len; i++) {
                // 单独处理从下标0开始的子数组。
                if (pre[i] == k) {
                    ans++;
                }

                if (map.containsKey(pre[i] - k)) {
                    ans += map.get(pre[i] - k);
                }

                map.put(pre[i], map.getOrDefault(pre[i], 0) + 1);
            }
            return ans;
        }
    }
}
