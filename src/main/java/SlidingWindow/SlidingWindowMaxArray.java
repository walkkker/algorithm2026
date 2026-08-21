package SlidingWindow;


import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * https://leetcode.cn/problems/sliding-window-maximum/
 *
 * qmax 窗口最大值的更新结构
 * <p>
 * deque 布局：大中小
 * <p>
 * qmax内部放**下标**
 * <p>
 * 实现方式：3步走   简单记：R不断右移的过程中，检查L是否需要右移，然后检查是否统计结果 （R右移就是队尾弹出，L右移就是队首弹出）
 * (1) R右走 ->  注意=也要弹出 ->   qmax时， arr[qmax.peekLast()] <= arr[i] 均弹出
 * (2) L右走 -> 注意：检查window>width? 是则L右移
 * (3) 统计 -> 注意：检查当前window是否满足长度，满足则统计结果
 */
public class SlidingWindowMaxArray {

    public static int[] getMaxWindow1(int[] arr, int w) {
        Deque<Integer> deque = new LinkedList<>();
        int L = 0;
        int[] ans = new int[arr.length - w + 1];   //  [w - 1, arr.length - 1]
        int index = 0;
        for (int R = 0; R < arr.length; R++) {
            while (!deque.isEmpty() && arr[R] >= arr[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(R);

            if (R - L + 1 > w) {
                // TODO: 【错误点】下面的顺序写反了！！ L++后，就没法 deque.peekFirst()与L比较了！！！
                //   L++;
                //   if (deque.peekFirst() == L) {
                //       deque.pollFirst();
                //   }
                if (L == deque.peekFirst()) {
                    deque.pollFirst();
                }
                L++;
            }

            if (R - L + 1 == w) {
                ans[index++] = arr[deque.peekFirst()];
            }

        }
        return ans;
    }


    public static int[] getMaxWindow(int[] arr, int w) {
        if (arr == null || w < 1 || arr.length < w) {
            return null;
        }
        Deque<Integer> qmax = new LinkedList<>();
        int[] res = new int[arr.length - w + 1];
        int index = 0;
        int L = 0;
        for (int R = 0; R < arr.length; R++) {
            // S1: R右移
            while (!qmax.isEmpty() && arr[qmax.peekLast()] <= arr[R]) {
                qmax.pollLast();
            }
            qmax.offerLast(R);

            // S2 和 S3都是为了应对 初始滑动时的窗口大小不足问题

            // S2：L右移 （需要检查）
            if (R - L + 1 > w) {
                if (L == qmax.peekFirst()) {
                    qmax.pollFirst();
                }
                L++;
            }

            // S3: 满足窗口大小时 统计数据
            if (R - L + 1 == w) {
                res[index++] = arr[qmax.peekFirst()];
            }
        }
        return res;
    }


    // 暴力的对数器方法
    public static int[] right(int[] arr, int w) {
        if (arr == null || w < 1 || arr.length < w) {
            return null;
        }
        int N = arr.length;
        int[] res = new int[N - w + 1];
        int index = 0;
        int L = 0;
        int R = w - 1;
        while (R < N) {
            int max = arr[L];
            for (int i = L + 1; i <= R; i++) {
                max = Math.max(max, arr[i]);

            }
            res[index++] = max;
            L++;
            R++;
        }
        return res;
    }


    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (maxValue + 1));
        }
        return arr;
    }

    // for test
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int testTime = 100000;
        int maxSize = 100;
        int maxValue = 100;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
            int w = (int) (Math.random() * (arr.length + 1));
            int[] ans1 = getMaxWindow(arr, w);
            int[] ans2 = right(arr, w);
            if (!isEqual(ans1, ans2)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish");
    }



}
