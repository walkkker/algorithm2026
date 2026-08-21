package frequence.dp;

/**
 * 416. 分割等和子集
 *
 * <p>判断数组能否被分成两个元素和相等的子集。
 *
 * <p><b>DP类型：</b>状态拓扑属于“目标值DP/0-1背包”，状态值语义属于“可行性DP”。
 * 每个数组元素最多使用一次，一维空间压缩时目标值必须倒序遍历。
 *
 * <p><b>问题转换：</b>总和为奇数时一定无解；总和为偶数时，问题等价于从每个元素最多选择一次，
 * 判断能否恰好凑出sum/2，因此是0-1背包可行性问题。
 *
 * <p><b>原概念错误：</b>两个子集和相等不代表必须选择n/2个元素；只要求存在任意元素数量的子集，
 * 其和为sum/2。二维原实现修正赋值遗漏后逻辑正确。
 *
 * <p><b>语法注意：</b>Java不存在{@code ||=}复合赋值。布尔值可以使用{@code p |= condition}，
 * 但它不短路；普通条件组合通常写成{@code p = p || condition}。
 *
 * <p>题型归纳参见同目录《动态规划题型共性总结.md》的“0-1背包”章节。
 *
 * <p><b>一维DP复盘：</b>本题逻辑上首先是二维状态：{@code dp[index][rest]}表示从index开始
 * 选择能否恰好凑出rest。选择和不选择当前数都进入index+1，体现每个元素最多使用一次。压缩掉
 * index维后，{@code dp[rest-num]}必须仍是上一轮旧状态，所以容量必须倒序；正序会在同一轮重复
 * 使用当前num。暴力递归、二维表和一维压缩的完整对应关系参见同目录《一维DP核心总结.md》。
 */
public class Q416_PartitionEqualSubsetSum {

    public static class OriginalSolution {

        public boolean canPartition(int[] nums) {
            int len = nums.length;
            int sum = 0;
            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
            }
            if ((sum & 1) == 1) {
                return false;
            } else {
                int resSum = sum / 2;
                boolean[][] dp = new boolean[len + 1][resSum + 1];
                for (int i = 0; i <= len; i++) {
                    dp[i][0] = true;
                }

                for (int i = len - 1; i >= 0; i--) {
                    for (int j = 1; j <= resSum; j++) {
                        boolean p = dp[i + 1][j];
                        if (j - nums[i] >= 0) {
                            // TODO: 【原语法错误】Java没有||=；布尔复合赋值可以使用|=。
                            // 错误行：p ||= dp[i + 1][j - nums[i]];
                            p |= dp[i + 1][j - nums[i]];
                        }
                        // TODO: 【原遗漏】临时变量p计算完成后必须写回当前dp格子，并且必须位于最内层循环。
                        dp[i][j] = p;
                    }
                }
                return dp[0][resSum];
            }
        }
    }

    public static class RecommendedSolution {

        public boolean canPartition(int[] nums) {
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            if ((sum & 1) == 1) {
                return false;
            }

            int target = sum / 2;
            boolean[] dp = new boolean[target + 1];
            dp[0] = true;

            for (int num : nums) {
                // 0-1背包压缩：倒序保证dp[rest-num]仍是处理当前num之前的上一轮状态，
                // 对应二维公式dp[index+1][rest-num]，避免同一个num在本轮被重复使用。
                for (int rest = target; rest >= num; rest--) {
                    dp[rest] = dp[rest] || dp[rest - num];
                }
                if (dp[target]) {
                    return true;
                }
            }
            return dp[target];
        }
    }
}
