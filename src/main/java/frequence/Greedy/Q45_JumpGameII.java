package frequence.Greedy;

/**
 * 45. 跳跃游戏 II
 *
 * <p>给定非负整数数组，每个元素表示从当前位置最多可以向右跳多少步。题目保证最后一个下标
 * 可达，返回到达最后一个下标所需的最少跳跃次数。
 *
 * <p>当前实现来自LeetCode已保存代码，使用“贪心 + BFS分层”模型：
 * {@code currentEnd}是当前跳跃次数能够覆盖的层边界，{@code farthest}是扫描当前层期间发现的
 * 下一层最远边界。扫描完当前层才增加一次跳跃次数，因此不会把层内每个位置都算作一次跳跃。
 *
 * <p>时间复杂度{@code O(N)}，额外空间复杂度{@code O(1)}。
 */
public class Q45_JumpGameII {

    /**
     * 用户复习版本（2026-09-21）：这就是分层，用连续可达区间压缩BFS，不需要显式队列。
     *
     * <p>count是当前跳数，avail是至多跳count次能到达的最远边界；
     * farthest是扫描当前层过程中逐步计算出的下一层最远边界。
     * 层内只扩展farthest，直到i == avail才统一更新avail并增加count。
     * 所有当前层位置都已参与比较，因此得到下一层最远覆盖范围，首次覆盖终点的层数就是最少跳数。
     *
     * <p>例如[2,3,1,1,4]：扫描位置0后，count=1、avail=2；
     * 扫描位置1和2后，count=2、avail=4，返回2。
     *
     * <p>TODO: 【顺序】先更新farthest，再检查层末，不能漏掉当前层最后一个位置的贡献。
     * TODO: 【终止】虽然循环扫描范围包含终点，但覆盖终点后立即返回，不会多算从终点出发的一跳。
     *
     * <p>TODO: 【前提】本题保证终点可达，当前版本正确；末尾return -1并不代表兼容不可达输入。
     * 反例[0,2,0]会利用不可达位置1并错误返回2。若扩展到不可达输入，
     * 应在i == avail分支内、更新avail之前增加：
     * {@code if (farthest == avail) return -1;}，表示当前层无法向前扩展。
     * 保留原代码，不加入原题不需要的分支。时间O(N)，额外空间O(1)。
     */
    public static class SolutionReviewed20260921 {
        public int jump(int[] nums) {
            if (nums.length <= 1) {
                return 0;
            }

            int avail = 0;
            int farthest = 0;
            int count = 0;
            for (int i = 0; i < nums.length; i++) {
                farthest = Math.max(farthest, i + nums[i]);
                // 当前层扫描完成，下一层边界已经确定，才增加跳数。
                if (i == avail) {
                    avail = farthest;
                    count++;
                    if (avail >= nums.length - 1) {
                        return count;
                    }
                }
            }
            return -1;
        }
    }

    public static class Solution {

        public int jump(int[] nums) {
            // TODO: 【遗漏-错误】长度为1时已经位于终点，不需要跳跃。
            if (nums.length == 1) {
                return 0;
            }

            int jumps = 0;
            int currentEnd = 0;
            int farthest = 0;

            for (int i = 0; i < nums.length; i++) {
                farthest = Math.max(farthest, i + nums[i]);

                if (farthest >= nums.length - 1) {
                    return jumps + 1;
                }

                if (i == currentEnd) {
                    jumps++;
                    currentEnd = farthest;
                }
            }
            return -1;
        }
    }
}
