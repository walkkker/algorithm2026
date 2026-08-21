package frequence.双指针.SlidingWindow;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 239. 滑动窗口最大值
 *
 * <p>给定整数数组{@code nums}和窗口大小{@code k}，窗口从数组最左侧移动到最右侧，
 * 返回每个窗口中的最大值。
 *
 * <p>当前实现来自LeetCode已保存代码。双端队列保存数组下标，并维持对应值从队首到队尾
 * 单调递减：新元素进入前删除所有不大于它的队尾元素；窗口左边界离开时，如果该下标位于
 * 队首，则将其弹出。队首始终是当前窗口最大值的下标。
 *
 * <p>每个下标最多入队、出队各一次，时间复杂度{@code O(N)}，额外空间复杂度{@code O(K)}。
 */
public class Q239_SlidingWindowMaximum {

    public static class Solution {

        public int[] maxSlidingWindow(int[] nums, int k) {
            int len = nums.length;
            int[] ans = new int[len - k + 1];
            int index = 0;
            Deque<Integer> deque = new LinkedList<>();
            int left = 0;
            for (int right = 0; right < len; right++) {
                while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[right]) {
                    deque.pollLast();
                }
                deque.offerLast(right);

                if (right - left + 1 > k) {
                    if (deque.peekFirst() == left) {
                        deque.pollFirst();
                    }
                    left++;
                }

                if (right - left + 1 == k) {
                    ans[index++] = nums[deque.peekFirst()];
                }
            }
            return ans;
        }
    }
}
