package frequence.jumpgame;

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

    /**
     * 用户复习版本（2026-09-21）：维护可达边界，原实现正确。
     *
     * <p>步骤：先通过i <= avail保证当前位置可达，再利用i + nums[i]扩展边界；
     * 覆盖终点就返回true，否则继续扫描。若i超过边界，说明所有可达位置已处理完，返回false。
     *
     * <p>TODO: 【可精简，不是错误】每轮开始avail == farthest，两者是重复状态，
     * 可以合并成一个变量。这里保留用户原样，便于复盘。
     * Q45则不同：avail固定表示当前层边界，farthest在层内不断收集下一层边界。
     *
     * <p>TODO: 【边界】本题数组非空；边界一旦覆盖终点就提前返回，因此不会读到nums[n]。
     * 不要删除提前返回后仍只依赖i <= avail控制访问范围。
     * 时间O(N)，额外空间O(1)。
     */
    public static class SolutionReviewed20260921 {
        public boolean canJump(int[] nums) {
            int avail = 0;
            int farthest = 0;
            int i = 0;
            while (i <= avail) {
                // 只能利用可达位置扩展边界；比较的是落点i + nums[i]，不是nums[i]。
                farthest = Math.max(farthest, i + nums[i]);
                if (farthest >= nums.length - 1) {
                    return true;
                }
                avail = farthest;
                i++;
            }
            return false;
        }
    }

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
