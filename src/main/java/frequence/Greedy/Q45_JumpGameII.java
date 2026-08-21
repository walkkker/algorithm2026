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
