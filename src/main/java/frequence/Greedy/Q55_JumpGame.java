package frequence.Greedy;

/**
 * 55. 跳跃游戏
 *
 * <p>给定非负整数数组，每个元素表示从当前位置最多可以向右跳多少步，判断能否到达最后一个下标。
 *
 * <p>当前实现来自LeetCode已通过提交。{@code end}表示当前所有可达位置能够继续扩展到的最远
 * 下标。只有{@code index <= end}时，当前位置才可达；遍历过程中持续扩大{@code end}，一旦
 * 覆盖最后一个下标即可返回成功。
 *
 * <p>时间复杂度{@code O(N)}，额外空间复杂度{@code O(1)}。
 */
public class Q55_JumpGame {

    public static class Solution {

        public boolean canJump(int[] nums) {
            int end = nums[0];
            int index = 0;
            while (index <= end) {
                end = Math.max(end, index + nums[index]);
                if (end >= nums.length - 1) {
                    return true;
                }

                // TODO: 【错误-遗漏】while循环末尾不要忘记推进循环变量。
                index++;
            }
            return false;
        }
    }
}
