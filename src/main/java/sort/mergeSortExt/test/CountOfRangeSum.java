package sort.mergeSortExt.test;
/*
  TODO：本题请认真看
 (1) https://leetcode.cn/problems/count-of-range-sum/
 (2) 给你一个整数数组 nums 以及两个整数 lower 和 upper 。求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的 区间和的个数 。
    区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
*/
// TODO: 【超级技巧 + 超级错误点】
// TODO： 1. 前缀和数组一定要用long[]
// TODO: 2. 不要忘记 S(0,1,2,3,n)这种前缀和也属于区间和，所以可以让preSum[0]=0, preSum[1] = nums[0] + preSum[0];
// TODO: 续2: 如果不想按2的方式做，那就遍单独遍历一遍前缀和数组，做 [lower, upper]的检查

/**
 *
 * Count of Range Sum：前缀和 + 归并排序
 *
 * 1. 前缀和必须包含初始 0，并使用 long：
 *
 *    preSum[0] = 0
 *    preSum[i + 1] = preSum[i] + nums[i]
 *
 * 2. 核心公式：[TODO：超级核心呀] => 因此，对每一个右侧sum，求左侧sum的区间内个数
 *
 *    lower <= rightSum - leftSum <= upper
 *
 *    等价于：
 *
 *    rightSum - upper <= leftSum <= rightSum - lower
 *
 * 3. 归并过程中，左右两部分已经有序。
 *    对右侧每个 rightSum，在左侧寻找：
 *
 *    windowL：第一个 >= rightSum - upper
 *    windowR：第一个 >  rightSum - lower
 *
 *    当前合法数量：
 *
 *    windowR - windowL
 *
 * 4. 分治答案：
 *
 *    左侧答案 + 右侧答案 + 跨左右区域的答案
 *
 * 5. 复杂度：
 *
 *    时间 O(N log N)
 *    空间 O(N)
 *
 */
public class CountOfRangeSum {

    public int countRangeSum(int[] nums, int lower, int upper) {

        // TODO: 【超级技巧】1. 前缀和数组一定要用long[]
        //  2. 不要忘记 S(0,1,2,3,n)这种前缀和也属于区间和，所以可以让preSum[0]=0, preSum[1] = nums[0] + preSum[0];
        //  续2: 如果不像按2的方式做，那就遍单独遍历一遍前缀和数组，做 [lower, upper]的检查

        int len = nums.length;
        long[] preSum = new long[len + 1];
        preSum[0] = 0;
        for (int i = 1; i < preSum.length; i++) {
            preSum[i] = preSum[i - 1] + nums[i - 1];
        }

        return process(preSum, 0, preSum.length - 1, lower, upper);
    }

    public static int process(long[] arr, int l, int r, int lower, int upper) {
        if (l >= r) {
            return 0;
        }
        int m = (l + r) / 2;

        return process(arr, l, m, lower, upper)
                + process(arr, m + 1, r,lower, upper)
                + merge(arr, l, m ,r, lower, upper);
    }

    public static int merge(long[] arr, int l, int m, int r, int lower, int upper) {
        int len = r - l + 1;

        // 模板start
        int ans = 0;
        int windowL = l;
        int windowR = l;   // TODO: 【初始】左闭右开 -> windowR - windowL == 0
        for (int R = m + 1; R <= r; R++) {
            while (windowL <= m && arr[windowL] < arr[R] - upper) {
                windowL++;
            }
            while (windowR <= m && arr[windowR] <= arr[R] - lower) {
                windowR++;
            }
            ans += windowR - windowL;
        }
        // 模板end

        long[] help = new long[len];
        int p1 = l;
        int p2 = m + 1;
        int index = 0;
        while (p1 <= m && p2 <= r) {
            help[index++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= m) {
            help[index++] = arr[p1++];
        }

        while (p2 <= r) {
            help[index++] = arr[p2++];
        }

        for (index = 0; index < len; index++) {
            arr[l + index] = help[index];
        }
        return ans;
    }



}
