package frequence.dp;

import java.util.HashMap;

/**
 * 300. 最长递增子序列
 *
 * <p>求数组中最长严格递增子序列的长度，子序列不要求连续，但必须保持原下标顺序。
 *
 * <p><b>DP类型：</b>状态拓扑属于“一维位置结尾型DP”，状态值语义属于“最大值DP”。
 * {@code dp[i]}表示必须以{@code nums[i]}结尾的LIS长度，需要枚举所有合法前驱{@code j < i}。
 *
 * <p><b>原记忆化版本：</b>递归选择与否的逻辑正确，状态数量为O(N^2)；但每个状态都构造
 * String key，并使用HashMap节点和Integer装箱。在N=2500时可能产生数百万个重对象，导致超时或
 * 内存压力。通用场景下，{@code Integer.MIN_VALUE}哨兵还会导致真实最小整数无法被选中。
 *
 * <p><b>推荐面试思路：</b>定义{@code dp[i]}为必须以nums[i]结尾的最长递增子序列长度，枚举
 * 所有j&lt;i；当{@code nums[j] < nums[i]}时，用{@code dp[j]+1}更新{@code dp[i]}。
 *
 * <p>本题属于“一维位置结尾型DP”，详细模型参见同目录《一维位置结尾型DP.md》；与其他动态
 * 规划类型的关系参见《动态规划题型共性总结.md》。“tails + 二分”是O(N log N)专用优化，
 * 面试中应先能从O(N^2)状态定义推导，再决定是否使用。
 *
 * <p><b>一维DP复盘：</b>原{@code process(index,lastValue)}是正确的选择模型，但它保留了两个
 * 变化参数，并用字符串HashMap承载大量状态。推荐解法不是普通滚动压缩，而是重新定义状态：固定
 * 右边界，令{@code dp[i]}表示必须以{@code nums[i]}结尾的LIS长度，再枚举所有合法前驱j。
 * 这是学习一维DP时最重要的“改变状态视角”案例，详细推导参见同目录《一维DP核心总结.md》。
 *
 * <p>子序列DP与双指针、贪心方法的选择参见
 * {@code frequence/substringandsubsequence/子串与子序列区别.md}。
 */
public class Q300_LongestIncreasingSubsequence {

    public static class OriginalMemoizedSolution {

        private HashMap<String, Integer> map;

        public int lengthOfLIS(int[] nums) {
            map = new HashMap<>();
            return process(nums, 0, Integer.MIN_VALUE);
        }

        private int process(int[] nums, int index, int lastValue) {
            // TODO: 【性能问题】每个状态都创建String及其底层数据，并触发HashMap和Integer装箱开销。
            String key = index + "|" + lastValue;
            if (map.containsKey(key)) {
                return map.get(key);
            }

            if (index == nums.length) {
                map.put(key, 0);
                return 0;
            }

            int ans = Integer.MIN_VALUE;
            if (nums[index] > lastValue) {
                ans = Math.max(ans, 1 + process(nums, index + 1, nums[index]));
            }
            ans = Math.max(ans, process(nums, index + 1, lastValue));
            map.put(key, ans);
            return ans;
        }
    }

    public static class RecommendedSolution {

        public int lengthOfLIS(int[] nums) {
            int[] dp = new int[nums.length];
            int ans = 0;

            for (int i = 0; i < nums.length; i++) {
                // 单独选择nums[i]，至少能形成长度为1的递增子序列。
                dp[i] = 1;
                for (int j = 0; j < i; j++) {
                    // j只是枚举转移来源，不是dp的第二个维度；真正保存的子问题仍是dp[i]。
                    if (nums[j] < nums[i]) {
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }
                // 最长递增子序列不一定以最后一个数组元素结尾。
                ans = Math.max(ans, dp[i]);
            }
            return ans;
        }
    }
}
