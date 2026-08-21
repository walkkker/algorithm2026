package sort.mergeSortExt;

/*
(1) https://leetcode.cn/problems/count-of-range-sum/
(2) 给你一个整数数组 nums 以及两个整数 lower 和 upper 。求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的 区间和的个数 。
    区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
 */
public class CountOfRangeSum {
    // 区间累加和，涉及到频繁计算 -> 转化成前缀和数组
    // 归并排序将 O(N^2)-以每个元素为结尾 优化到 O(N*logN)-依然是以每个元素为结尾，用归并优化比较
    public int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        // 构建前缀和数组
        // TODO: 【严重错误！！！】左神这里用了long[] preSum， 不是严谨，必须这么用！！！！！
        //  前缀和数组/累加和数组 必须用long，不然 对于int[] arr 会导致溢出。导致 样例包含大数字的 报错！！！！
        long[] preSum = new long[nums.length];
        preSum[0] = nums[0];
        for (int i = 1; i < preSum.length; i++) {
            preSum[i] = preSum[i - 1] + nums[i];
        }

        // 归并排序在preSum上面玩
        // TODO: 【错误】都说了在preSum上面玩，结果你传了个 nums 原数组？？？？
//        return process(nums, 0, nums.length - 1, lower, upper);
        return process(preSum, 0, nums.length - 1, lower, upper);
    }


    public int process(long[] arr, int l, int r, int lower, int upper) {
        if (l == r) {    // 这个表示 单个前缀和元素，即 [0, i]。 此处的base case要计算是否满足要求的
            return arr[l] >= lower && arr[l] <= upper ? 1 : 0;
        }
        int m = l + (r - l) / 2;

        return process(arr, l, m, lower, upper)
                + process(arr, m + 1, r, lower, upper)
                + merge(arr, l, m, r, lower, upper);
    }


    public int merge(long[] arr, int l, int m, int r, int lower, int upper) {
        int windowL = l;
        int windowR = l;
        int count = 0;
        // TODO: 起始值与 滑动窗口边界的滑动， 以及右侧区间每个元素的移动  这段代码细细品味
        for (int i = m + 1; i <= r; i++) {
            long newLower = arr[i] - upper;
            long newUpper = arr[i] - lower;
            while (windowL <= m && arr[windowL] < newLower) {     // windowL先走
                windowL++;   // inclusive
            }

            while (windowR <= m && arr[windowR] <= newUpper) {     // windowR 分开走
                windowR++;   // exclusive
            }
            count += windowR - windowL;
        }
        long[] help = new long[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = m + 1;
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }

        while (p1 <= m) {
            help[i++] = arr[p1++];
        }

        while (p2 <= r) {
            help[i++] = arr[p2++];
        }

        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
        return count;
    }
}
