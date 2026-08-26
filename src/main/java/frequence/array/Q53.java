package frequence.array;

/**
 * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 *
 * 子数组是数组中的一个连续部分。
 *
 * <p><b>专题分类：</b>连续子数组。可使用前缀和减左侧最小前缀，也可定义“必须以i结尾”的
 * 最大值DP（Kadane）。对照索引参见
 * {@code frequence/substringandsubsequence/子串与子序列区别.md}。
 */
public class Q53 {
    // O(N) 空间:O(N) 不是最优解
    public int myMaxSubArray(int[] nums) {
        int len = nums.length;
        long[] pre = new long[len];
        pre[0] = nums[0];
        for (int i = 1; i < len; i++) {
            pre[i] = pre[i - 1] + nums[i];
        }

        long leftMin = Integer.MAX_VALUE;
        long ans = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            ans = Math.max(ans, pre[i]);

            if (i == 0) {
                leftMin = pre[i];
            } else {
                ans = Math.max(ans, pre[i] - leftMin);
                leftMin = Math.min(leftMin, pre[i]);
            }
        }
        return (int) ans;
    }

    public int maxSubArray(int[] nums) {
        int leftMin = 0;
        int pre = 0;
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (i == 0) {
                pre += nums[i];
                ans = pre;
                leftMin = pre;
            } else {
                pre += nums[i];
                ans = Math.max(ans, pre);
                ans = Math.max(ans, pre - leftMin);
                leftMin = Math.min(leftMin, pre);
            }
        }
        return ans;
    }
    }
