package SlidingWindow;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 「统计所有最大值减最小值不大于 K 的子数组个数」
 * 相似题(逻辑一模一样，唯一区别一个求总数，一个求最长)： 1438. 绝对差不超过限制的最长连续子数组
 * <p>
 * 涉及【所有子数组】的问题：以每个索引开头的子数组/以每个索引结尾的子数组
 */
// 统计每个以R结尾的区间， 有多少个子数组。
public class Code02_AllLessNumSubArray {

    // 我觉得先出一个很重要的结论，if [i,j] max-min>target，then [i,j后面]一定max-min>target。 因为[i,j后面]包含[i,j]，因此max只能更大，min只能更小。  in other words,以i为开头，越往右扩，max越大 min越小， max-min 越大

    /**
     * 20260708 二遍过，错误如下。  核心点是如何构建 max-min<target的 动态滑动窗口。
     * 理论基础如上//注释，所以可以 以每个R为结尾的每个子数组 去求子数组总数。（有点DP的味道，复用了R-1的信息。）
     * @param arr
     * @param target
     * @return
     */
    public static int lessThanTarget2(int[] arr, int target) {
        Deque<Integer> maxDeque = new LinkedList<>();
        Deque<Integer> minDeque = new LinkedList<>();
        int L = 0;
        int ans = 0;
        for (int R = 0; R < arr.length; R++) {
            // 一样的三步走： Step1: 扩右
            while (!maxDeque.isEmpty() && arr[R] >= arr[maxDeque.peekLast()]) {
                maxDeque.pollLast();
            }
            maxDeque.offerLast(R);

            while (!minDeque.isEmpty() && arr[R] <= arr[minDeque.peekLast()]) {
                minDeque.pollLast();
            }
            minDeque.offerLast(R);

            int max = arr[maxDeque.peekFirst()];
            int min = arr[minDeque.peekFirst()];

            // Step2: 找出满足条件的 L。
            // TODO: 【错误点】题目要的是不大于target的子数组数量。 因此这里不满足的情况为max-min>target，即退出时max-min<=target,此时可收集结果
            while (max - min > target) {
                if (maxDeque.peekFirst() == L) {
                    maxDeque.pollFirst();
                }
                if (minDeque.peekFirst() == L) {
                    minDeque.pollFirst();
                }
                L++;
                max = arr[maxDeque.peekFirst()];
                min = arr[minDeque.peekFirst()];
            }

            // Step3: 针对当前窗口，计算结果值
            ans += R - L + 1;
        }
        return ans;
    }



    public static int lessThanTarget(int[] arr, int target) {
        int n = arr.length;
        int L = 0;
        Deque<Integer> maxDeque = new LinkedList<>();
        Deque<Integer> minDeque = new LinkedList<>();
        int ans = 0;
        int max = 0;
        int min = 0;
        for (int R = 0; R < n; R++) {   // 以每个R为结尾统计数量
            while (!maxDeque.isEmpty() && arr[maxDeque.peekLast()] <= arr[R]) {
                maxDeque.pollLast();
            }
            maxDeque.offerLast(R);
            max = arr[maxDeque.peekFirst()];

            while (!minDeque.isEmpty() && arr[minDeque.peekLast()] >= arr[R]) {
                minDeque.pollLast();
            }
            minDeque.offer(R);
            min = arr[minDeque.peekFirst()];


            if (max - min <= target) {
                ans += R - L + 1;   // TODO：【注意】一开始想错了，这个也很重要啊  [L, R]区间的子数组数量，你可以想长度为3的数组，
                //  有多少个以R结尾/L开头的子数组？  子数组数量==数组长度 [1,2,3] -> 以R结尾 -> [3] [2,3] [1,2,3] => 以L开头也一样，就是等于 子数组长度
                continue;
            } else {
                while (max - min > target) {
                    if (maxDeque.peekFirst() == L) {
                        maxDeque.pollFirst();
                    }
                    if (minDeque.peekFirst() == L) {
                        minDeque.pollFirst();
                    }
                    L++;
                    max = arr[maxDeque.peekFirst()];
                    min = arr[minDeque.peekFirst()];
                }
                ans += R - L + 1;
            }
        }
        return ans;
    }



    // 暴力的对数器方法
    public static int right(int[] arr, int sum) {
        if (arr == null || arr.length == 0 || sum < 0) {
            return 0;
        }
        int N = arr.length;
        int count = 0;
        for (int L = 0; L < N; L++) {
            for (int R = L; R < N; R++) {
                int max = arr[L];
                int min = arr[L];
                for (int i = L + 1; i <= R; i++) {
                    max = Math.max(max, arr[i]);
                    min = Math.min(min, arr[i]);
                }
                if (max - min <= sum) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int num(int[] arr, int sum) {
        if (arr == null || arr.length == 0 || sum < 0) {
            return 0;
        }
        int N = arr.length;
        int count = 0;
        LinkedList<Integer> maxWindow = new LinkedList<>();
        LinkedList<Integer> minWindow = new LinkedList<>();
        int R = 0;
        for (int L = 0; L < N; L++) {
            while (R < N) {
                while (!maxWindow.isEmpty() && arr[maxWindow.peekLast()] <= arr[R]) {
                    maxWindow.pollLast();
                }
                maxWindow.addLast(R);
                while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[R]) {
                    minWindow.pollLast();
                }
                minWindow.addLast(R);
                if (arr[maxWindow.peekFirst()] - arr[minWindow.peekFirst()] > sum) {
                    break;
                } else {
                    R++;
                }
            }
            count += R - L;
            if (maxWindow.peekFirst() == L) {
                maxWindow.pollFirst();
            }
            if (minWindow.peekFirst() == L) {
                minWindow.pollFirst();
            }
        }
        return count;
    }

    // for test
    public static int[] generateRandomArray(int maxLen, int maxValue) {
        int len = (int) (Math.random() * (maxLen + 1));
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * (maxValue + 1));
        }
        return arr;
    }

    // for test
    public static void printArray(int[] arr) {
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int maxLen = 100;
        int maxValue = 200;
        int testTime = 100000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxLen, maxValue);
            int sum = (int) (Math.random() * (maxValue + 1));
            int ans1 = right(arr, sum);
            int ans2 = num(arr, sum);
            int ans3 = lessThanTarget2(arr, sum);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Oops!");
                printArray(arr);
                System.out.println(sum);
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                break;
            }
        }
        System.out.println("测试结束");

    }

}
