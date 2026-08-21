package frequence.dp;

/**
 * 198. 打家劫舍
 *
 * <p>不能偷窃相邻房屋，求能够偷窃到的最大金额。
 *
 * <p><b>DP类型：</b>状态拓扑属于“固定前驱线性DP”，状态值语义属于“最大值DP”。当前房屋
 * 对应“偷并跳过相邻位置”与“不偷”两种来源。
 *
 * <p><b>正确思路：</b>对每个位置只有“偷当前房屋”和“不偷当前房屋”两种选择。原实现定义
 * {@code dp[i]}为从区间[i,n-1]能够获得的最大金额，转移为
 * {@code dp[i] = max(nums[i] + dp[i+2], dp[i+1])}，逻辑正确。
 *
 * <p><b>优化：</b>每个状态只依赖后面两个状态，可以压缩为两个变量，将额外空间从O(N)降为O(1)。
 * 题型归纳参见同目录《动态规划题型共性总结.md》的“固定前驱线性DP”章节。
 *
 * <p><b>一维DP复盘：</b>原状态定义和转移均正确，不足只在于保存了完整数组。这里的{@code dp[i]}
 * 表示后缀{@code [i,n-1]}的整体最优答案，并不要求必须偷第i间房，因此答案直接位于{@code dp[0]}；
 * 这与Q300、Q152“必须以i结尾，最后还要取所有状态最大值”不同。固定前驱与变量压缩参见同目录
 * 《一维DP核心总结.md》。
 */
public class Q198_HouseRobber {

    public static class OriginalSolution {

        public int rob(int[] nums) {
            if (nums.length == 1) {
                return nums[0];
            }

            int len = nums.length;
            int[] dp = new int[len]; // dp[i]表示i到n-1范围内的最大收益。
            dp[len - 1] = nums[len - 1];
            dp[len - 2] = Math.max(nums[len - 2], nums[len - 1]);
            for (int i = len - 3; i >= 0; i--) {
                // 选择1：偷当前房屋，因此下一间只能从i+2开始。
                int p1 = nums[i] + dp[i + 2];
                // 选择2：不偷当前房屋，直接继承i+1的最优答案。
                int p2 = dp[i + 1];
                dp[i] = Math.max(p1, p2);
            }
            return dp[0];
        }
    }

    public static class RecommendedSolution {

        public int rob(int[] nums) {
            int nextTwo = 0;
            int nextOne = 0;
            for (int i = nums.length - 1; i >= 0; i--) {
                // 未压缩模型：dp[i] = max(nums[i] + dp[i + 2], dp[i + 1])。
                int current = Math.max(nums[i] + nextTwo, nextOne);
                // current计算完成后再整体移动，确保本轮读取的都是旧状态。
                nextTwo = nextOne;
                nextOne = current;
            }
            return nextOne;
        }
    }
}
