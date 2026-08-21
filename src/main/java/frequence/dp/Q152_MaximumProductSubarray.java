package frequence.dp;

/**
 * 152. 乘积最大子数组
 *
 * <p>求数组中乘积最大的连续非空子数组。
 *
 * <p><b>DP类型：</b>状态拓扑属于“一维位置结尾型DP”，状态值语义属于“最大值DP+双极值属性”。
 * 每个位置同时保存必须以i结尾的最大乘积和最小乘积，处理负数导致的极值翻转。
 *
 * <p><b>核心状态：</b>概念上定义{@code maxDp[i]}和{@code minDp[i]}，分别表示必须以i位置结尾
 * 的最大乘积与最小乘积。负数会反转大小关系，因此最小负乘积乘以负数可能成为新的最大正乘积，
 * 只维护最大值会丢失必要状态。
 *
 * <p>当前元素对应三种选择：从当前元素重新开始、连接前一个最大乘积、连接前一个最小乘积。
 * 每个状态只依赖i-1，所以标准实现压缩为两个变量，时间复杂度O(N)，额外空间O(1)。
 *
 * <p>本题属于“一维位置结尾型DP”，详细模型参见同目录《一维位置结尾型DP.md》；总体分类参见
 * 《动态规划题型共性总结.md》。“必须以i结尾”解决连续子数组边界，“最大+最小双状态”解决
 * 乘法符号翻转。
 *
 * <p><b>一维DP复盘：</b>学习时应先保留{@code maxDp[i]}和{@code minDp[i]}两个数组，明确本轮
 * 两个状态都只读取{@code i-1}位置的旧值，再压缩为两个变量。原错误不是转移公式缺失，而是变量
 * 压缩后先更新max、再用本轮新max计算min，破坏了未压缩模型的上一层依赖。完整的“先数组、后变量”
 * 推导参见同目录《一维DP核心总结.md》。
 *
 * <p><b>与股票状态机的关系：</b>本题的max/min双状态和股票问题的hold/cash双状态都属于
 * “一维位置推进 + 同位置多状态”，压缩后都必须使用上一轮状态快照；区别是本题状态描述数值极值，
 * 股票状态描述互斥业务状态。详细对比参见同目录《乘积最大子数组与股票状态机DP对比.md》。
 *
 * <p>连续子数组与非连续子序列的模型对照参见
 * {@code frequence/substringandsubsequence/子串与子序列专题.md}。
 */
public class Q152_MaximumProductSubarray {


    /**
     *
     * TODO：错误版本
     【必须以i结尾的】 乘积最大子数组&&乘积最小子数组

     这道题要与Q300做对比，那个是递增子序列。设置的也是以i结尾的子序列，但是每次循环里面，还枚举了nums[i]之前的数字，寻找nums[j]<nums[i]的j位置中的最大值dp[j]。
     */
    class Solution {
        public int maxProduct(int[] nums) {
            int len = nums.length;
            // int[] maxProduct = new int[len];
            // int[] minProduct = new int[len];
            // maxProduct[0] = nums[0];
            // minProduct[0] = nums[0];
            int ans = nums[0];
            int maxProduct = nums[0];
            int minProduct = nums[0];
            for (int i = 1; i < len; i++) {
                int p = nums[i];
                maxProduct = Math.max(p, Math.max(p * maxProduct, p * minProduct));
                // TODO: 【错误-状态污染】minProduct应读取上一位置的oldMax/oldMin；此处maxProduct
                // 已经更新为本轮状态。空间压缩不能改变未压缩公式“两个新状态同时读取上一轮”的语义。
                // 反例：[-2,3,-4,-1]会制造不存在的-96，并在下一轮得到错误答案96；正确答案是24。
                minProduct = Math.min(p, Math.min(p * maxProduct, p * minProduct));
                ans = Math.max(ans, maxProduct);
            }
            return ans;
        }
    }


    /**
     * TODO: 【修正版本】
     【必须以i结尾的】 乘积最大子数组&&乘积最小子数组

     这道题要与Q300做对比，那个是递增子序列。设置的也是以i结尾的子序列，但是每次循环里面，还枚举了nums[i]之前的数字，寻找nums[j]<nums[i]的j位置中的最大值dp[j]。
     */
    class CorrectSolution {
        public int maxProduct(int[] nums) {
            int len = nums.length;
            // int[] maxProduct = new int[len];
            // int[] minProduct = new int[len];
            // maxProduct[0] = nums[0];
            // minProduct[0] = nums[0];
            int ans = nums[0];
            int maxProduct = nums[0];
            int minProduct = nums[0];
            for (int i = 1; i < len; i++) {
                int p = nums[i];
                int tmpMax = maxProduct;
                int tmpMin = minProduct;

                maxProduct = Math.max(p, Math.max(p * tmpMax, p * tmpMin));
                // TODO: 【错误】错误在于：你更新 minProduct 时，使用的 maxProduct 已经是本轮更新后的值，不再是 i - 1 位置的旧状态。
                // TODO: 【修正】你需要在进入循环体时，把maxProduct和minProduct这两个量固定好。 使用额外的变量引用，然后计算时只使用 固定好的tmpMin和 tmpMax，才能避免错误。
                minProduct = Math.min(p, Math.min(p * tmpMax, p * tmpMin));
                ans = Math.max(ans, maxProduct);
            }
            return ans;
        }
    }


    /**
     * AI推荐版本
     */
    public static class RecommendedSolution {

        public int maxProduct(int[] nums) {
            int maxEnd = nums[0];
            int minEnd = nums[0];
            int ans = nums[0];

            for (int i = 1; i < nums.length; i++) {
                int current = nums[i];

                // 三种可能必须使用更新前的maxEnd和minEnd同时计算。
                int startNew = current;
                int connectMax = maxEnd * current;
                int connectMin = minEnd * current;

                maxEnd = Math.max(startNew, Math.max(connectMax, connectMin));
                minEnd = Math.min(startNew, Math.min(connectMax, connectMin));
                ans = Math.max(ans, maxEnd);
            }
            return ans;
        }
    }
}
