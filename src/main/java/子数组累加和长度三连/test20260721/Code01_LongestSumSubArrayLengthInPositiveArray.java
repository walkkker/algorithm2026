package 子数组累加和长度三连.test20260721;
/**
 * 第一连：
 *  给定一个正整数组成的无序数组arr，给定一个正整数值K
 *  找到arr的所有子数组里，哪个子数组的累加和等于K，并且是长度最大的
 *  返回其长度
 *
 *  第二连对应的题目是 arr[i] 正负0， K正负0  =》 失去单调性。使用常规做法：前缀和 + HashMap
 */
public class Code01_LongestSumSubArrayLengthInPositiveArray {
    /**
     * 本题可以使用下列滑动窗口实现O(N)核心是 窗口内具备单调性！！！   为什么具有单调性？正整数数组！！！R往右扩一定变大
     * @param arr
     * @param K
     * @return
     */
    public static int getMaxLength(int[] arr, int K) {
        int L = 0;
        int maxLen = 0;
        int sum = 0;
        for (int R = 0; R < arr.length; R++) {
            sum += arr[R];   // 这个地方一定注意， 本题子数组累加和，R移动后，sum一定要先加
            // TODO: 【注意】滑动窗口for(R)循环框架，如果不是固定窗口大小的话，L的移动要使用while -> 即L移动到不能再移动为止
            while (sum > K) {
                sum -= arr[L];
                L++;
            }
            if (sum == K) {
                maxLen = Math.max(maxLen, R - L + 1);
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
    public static int[] generatePositiveArray(int size, int value) {
        int[] ans = new int[size];
        for (int i = 0; i != size; i++) {
            ans[i] = (int) (Math.random() * value) + 1;
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
            int[] arr = generatePositiveArray(len, value);
            int K = (int) (Math.random() * value) + 1;
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
