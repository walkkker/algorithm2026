package SlidingWindow;

import java.util.Deque;
import java.util.LinkedList;
// 本题与 max-min<=target 求子数组总和的 Code02为一模一样框架
// https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/description/
// 该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit。 => 翻译过来就是， max-min<=limit
public class Leetcode_1478 {

    public int longestSubarray(int[] nums, int limit) {
        Deque<Integer> maxDeque = new LinkedList<>();
        Deque<Integer> minDeque = new LinkedList<>();
        int L = 0;
        int ans = 0;
        for (int R = 0; R < nums.length; R++) {
            // Step1: R右移动
            while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] <= nums[R]) {
                maxDeque.pollLast();
            }
            maxDeque.offerLast(R);
            while (!minDeque.isEmpty() && nums[R] <= nums[minDeque.peekLast()]) {
                minDeque.pollLast();
            }
            minDeque.offerLast(R);

            // Step2: L右移动。  【注意错误点】一定先检查当前的L是否与peekFirst()相同， 然后再 L++
            int max = nums[maxDeque.peekFirst()];
            int min = nums[minDeque.peekFirst()];
            while (max - min > limit) {
                if (L == maxDeque.peekFirst()) {
                    maxDeque.pollFirst();
                }
                if (L == minDeque.peekFirst()) {
                    minDeque.pollFirst();
                }
                L++;
                max = nums[maxDeque.peekFirst()];
                min = nums[minDeque.peekFirst()];
            }

            ans = Math.max(ans, R - L + 1);

        }
        return ans;
    }

}
