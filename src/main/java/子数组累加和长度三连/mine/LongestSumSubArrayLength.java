package 子数组累加和长度三连.mine;

import java.util.HashMap;

/**
 * 续问二连： arr[i] 正负0
 *
 * TODO: 本道题的【核心错误点】是 当你发现【某个子数组的累加和】==k时，子数组的长度问题！！！
 *      (1) 如果是 preSum[R] == k , 此时范围是[0, R]， 此时长度为 R+1
 *      (2) 如果是 preSum[R] - preSum[L] == k, 此时范围是(L, R]，此时长度为 R - L
 *
 * TODO: 本题还有别的小tips，可以看zuoshen的代码：
 *      如果想把 preSum[R]的情况与 (L, R]统一起来。
 *      可以通过提前在hashMap里面添加一个 (0, -1)来实现。  因为这个就代表存在 -1 的位置，它的前缀和为0.   完美匹配任何一个preSum[R]。
 */
public class LongestSumSubArrayLength {

    public static int maxLength(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int[] preSum = new int[arr.length];
        preSum[0] = arr[0];
        for (int i = 1; i < preSum.length; i++) {
            preSum[i] = preSum[i - 1] + arr[i];
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        int maxLen = 0;
        for (int R = 0; R < preSum.length; R++) {
            if (!map.containsKey(preSum[R])) {
                map.put(preSum[R], R);
            }

            if (preSum[R] == k) {
                //  TODO: 【错误】下面这个错误怎么能犯呢？比较的是长度！！！
                //  maxLen = Math.max(maxLen, preSum[R]);
                maxLen = Math.max(maxLen, R + 1);
            } else  {
                int remain = preSum[R] - k;
                if (map.containsKey(remain)) {
                    int L = map.get(remain);
                    // TODO: 【错误】本题是前缀和的 preSum[R] - preSum[L] = k ，此时的子数组范围是 (L, R]， 所以长度是 R-L！！！
//                    maxLen = Math.max(maxLen, R - L + 1);
                    maxLen = Math.max(maxLen, R - L);
                } else {
                    continue;
                }
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
            int ans1 = maxLength(arr, K);
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