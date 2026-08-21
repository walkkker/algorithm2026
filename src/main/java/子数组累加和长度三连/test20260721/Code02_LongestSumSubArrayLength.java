package 子数组累加和长度三连.test20260721;

import java.util.*;

/**
 * 题目二: 与一连的区别　
 * 给定一个整数组成的无序数组arr 【值可能正、可能负、可能0】
 * 给定一个整数值K
 * 找到arr的所有子数组里，哪个子数组的累加和等于K，并且是长度最大的
 * 返回其长度
 *
 *
 * TODO: 想不出来方法（被套在对 一连的延伸里面了），【那么要考虑 扩充空间复杂度的方法】
 */
public class Code02_LongestSumSubArrayLength {

    /**
     * 基于题目元素范围 +-0，我们只能使用 子数组的正规军方法， 每个元素开头/每个元素结尾？ 想错了，子数组累加和的正规军是preSum 以及 preSum相减
     *
     * 【看答案了】其实也很简单， 前缀和 + HashMap
     *
     * 【注意点】类似于 SBT实现的前缀和 => 你需要边遍历，边计算。 不能全部塞进去，再计算，这样会计算到后面的数字。
     */
    public static int getMaxLength(int[] arr, int K) {
        int maxLen = 0;
        int len = arr.length;
        long[] pre = new long[len];
        pre[0] = arr[0];
        for (int i = 1; i < len; i++) {
            pre[i] = pre[i - 1] + arr[i];
        }

        HashMap<Long, Integer> map = new HashMap<>();

        // TODO: 跟 SBT实现rangeSum一样。 必须一边遍历，一边计算，一边存储。 因为不能计算到后面的元素
        for (int i = 0; i < len; i++) {   // TODO: 在我们这种前缀和实现里，一定要 分两层计算。 1. 前缀和本身0...i  2. 中间子数组 1...i
            // 第一层: 0...i
            if (pre[i] == K) {
                maxLen = Math.max(maxLen, i + 1);
            }

            // 第二层: 剩余子数组情况
            if (map.containsKey(pre[i] - K)) {
                int tmp = i - map.get(pre[i] - K);
                maxLen = Math.max(maxLen, tmp);
            }

            // 因为要的是最长结果。所以，相同preSum[i]的结果，只保留最早的那个
            if (!map.containsKey(pre[i])) {
                map.put(pre[i], i);
            }
        }
        return maxLen;
    }


    // for test
    public static int right(int[] arr, int K) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (valid(arr, i, j, K)) {
                    max = Math.max(max, j - i + 1);
                }
            }
        }
        return max;
    }

    // for test
    public static boolean valid(int[] arr, int L, int R, int K) {
        int sum = 0;
        for (int i = L; i <= R; i++) {
            sum += arr[i];
        }
        return sum == K;
    }

    // for test
    public static int[] generateRandomArray(int size, int value) {
        int[] ans = new int[(int) (Math.random() * size) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * value) - (int) (Math.random() * value);
        }
        return ans;
    }

    // for test
    public static void printArray(int[] arr) {
        for (int i = 0; i != arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int len = 50;
        int value = 100;
        int testTime = 500000;

        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(len, value);
            int K = (int) (Math.random() * value) - (int) (Math.random() * value);
            int ans1 = getMaxLength(arr, K);
            int ans2 = right(arr, K);
            if (ans1 != ans2) {
                System.out.println("Oops!");
                printArray(arr);
                System.out.println("K : " + K);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");

    }

}
