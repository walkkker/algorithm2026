package 子数组累加和长度三连.mine;

/**
 * 给定一个正整数组成的无序数组arr，给定一个正整数值K
 * 找到arr的所有子数组里，哪个子数组的累加和等于K，并且是长度最大的
 * 返回其长度
 *
 * TODO：采用滑动窗口的特别重要的决策基础是：【窗口内sum具有单调性 — R右移增大，L右移减小】、 比如第二题，正负都有，那么窗口就不具有单调性了，就不适合滑动窗口了。
 *
 * TODO：【非常重要】本题不简单！！！
 *      （1）子数组累加和最长长度，但是不使用前缀和，使用滑动窗口
 *          - 1.1 所有的滑动窗口（L， R指针），其实都是与子数组问题有关的（因为在连续子数组上面滑）
 *          - 1.2 而且子数组问题一定可以使用 ->  For(int R = 0; R < arr.length; R++)的公式 + （特别注意）内部三步骤处理所有题
 *          - 1.3 滑动窗口/双指针 套路的本质是 -> 求每一个位置为结尾时的局部答案，最终汇聚成所有子数组的最终答案
 *      （2）1.2提到的三步骤实际上是非常具有扩展性的：
 *          2.1 第一步：来到当前R， 滑动窗口值（sum,单调队列....）更新arr[R]
 *          2.2 第二部：【最重要的】L移动，这里可以直接执行/if/while，取决于问题。
 *              - 比如固定大小的滑动窗口，那么每次第二步骤都会直接执行 取消arr[L] && L++
 *              - 但是像本题，我使用while移动L,因为单调窗口已经具有单调性，所以while(sum > K) 那么就一直L++ 直到sum<=K
 *          2.3 第三步：统计结果： 可以直接统计/使用if条件检查是否满足特定条件（所以这一步是与第二部紧密关联的，第二步让条件有成立的可能性，然后第三步检查是否满足条件，满足则统计结果）
 *
 *
 *
 */
public class LongestSumSubArrayLengthInPositiveArray {

    public static int getMaxLength(int[] arr, int K) {
        if (arr == null || arr.length < 1) {
            return 0;
        }
        int L = 0;
        int maxLen = 0;
        int sum = 0;
        for (int R = 0; R < arr.length; R++) {
            // 目标：滑动窗口用来求 以每个元素为结尾时的满足K累加和的最长长度
            // 步骤：还是经典三部曲：右指针右移 -> 左指针左移到满足条件的位置（if/while） -> if检查是否满足统计条件

            // S1: 来到新的R,将arr[R]记录到窗口容器。  然后求以当前R为结尾的maxLen
            sum += arr[R];

            // S2: 此处要使用while而不是if了（单调队列是因为固定大小窗口，每次一定移动1）。找到最早的L，满足 窗口值 <= K （所以当>K时，L要不断右移->从而删除L位置的元素）
            while (sum > K) {
                sum -= arr[L];
                L++;
            }


            // S3: 检查总是在最后一步！！！  通过if条件判断，当前以R结尾的子数组，是否满足 sum==K。（因为另一个可能是<K，这种情况不取值）
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
