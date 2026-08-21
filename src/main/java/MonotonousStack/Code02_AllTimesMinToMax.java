package MonotonousStack;

import java.util.Stack;

/**
 * 给定一个只包含正数的数组arr，arr中【任何】一个子数组sub，
 * 一定都可以算出(sub累加和)*(sub中的最小值)是什么，
 * 那么所有子数组中，这个值最大是多少?
 * <p>
 * A：子数组的min问题（单调栈） + 子数组累加和（前缀和数组）
 * <p>
 * 测试链接 : https://leetcode.cn/problems/maximum-subarray-min-product/
 * 注意测试题目数量大，要取模，但是思路和课上讲的是完全一样的
 * 注意溢出的处理即可，也就是用long类型来表示累加和
 * 还有优化就是，你可以用自己手写的数组栈，来替代系统实现的栈，也会快很多
 */
public class Code02_AllTimesMinToMax {

    /**
     * 题目明确说明了： 可以使用Long 在不取模的情况下，存储所有的 最小乘积。 所以直接preSum和max全部使用 long类型存储。
     */
    class Solution20260707 {
        public int maxSumMinProduct(int[] arr) {
            long[] preSum = preSum(arr);
            long max = -1;
            Stack<Integer> stack = new Stack<>();
            for (int i = 0; i < arr.length; i++) {
                while (!stack.isEmpty() && arr[i] <= arr[stack.peek()]) {
                    int popI = stack.pop();
                    if (arr[i] < arr[popI]) {
                        int l = stack.isEmpty() ? -1 : stack.peek();
                        int r = i;
                        if (l == -1) {
                            // max = Math.max(max, preSum[r - 1]);
                            max = Math.max(max, preSum[r - 1] * arr[popI]);
                        } else {
                            max = Math.max((preSum[r - 1] - preSum[l]) * arr[popI], max);
                        }
                    }
                }
                stack.push(i);
            }
            while (!stack.isEmpty()) {
                int popI = stack.pop();
                int l = stack.isEmpty() ? -1 : stack.peek();
                int r = arr.length;
                if (l == -1) {
                    max = Math.max(max, preSum[r - 1] * arr[popI]);
                } else {
                    max = Math.max((preSum[r - 1] - preSum[l]) * arr[popI], max);
                }
            }
            return (int) (max % 1000000007);
        }

        public long[] preSum(int[] arr) {
            long[] preSum = new long[arr.length];
            preSum[0] = arr[0] % 1000000007;
            for (int i = 1; i < arr.length; i++) {
                preSum[i] = arr[i] + preSum[i - 1];
            }
            return preSum;
        }
    }




    // TODO: 注意点：
    //  1) 累加和问题 =》 preSum => Long类型      =》 尤其本题还是 int * sum
    //  2）因为结果较大所以要取模，取模时候注意细节：看下面代码 =》 简而言之，Math.pow() 返回的是double类型，使用double类型参与计算前，先强制类型转换int / long
    public int myMaxSumMinProduct(int[] arr) {
        int n = arr.length;
        long[] preSum = new long[n];
        preSum[0] = arr[0];
        for (int i = 1; i < n; i++) {
            preSum[i] = preSum[i - 1] + arr[i];
        }
        Stack<Integer> stack = new Stack<>();
        long ans = -1;
        for (int i = 0; i < n; i++) {
            // 求min
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                int cur = stack.pop();
                // 因为都是正整数，所以你可以剪枝。
                if (arr[cur] > arr[i]) {
                    int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
                    int rightLessIndex = i;
                    long sum = preSum[(i - 1)] - (leftLessIndex == -1 ? 0 : preSum[leftLessIndex]);
                    ans = Math.max(ans, sum * arr[cur]);
                }
            }
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            int index = stack.pop();
            int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
            int rightLessIndex = n;
            long sum = preSum[(rightLessIndex - 1)] - (leftLessIndex == -1 ? 0 : preSum[leftLessIndex]);
            ans = Math.max(ans, sum * arr[index]);
        }
        // TODO: 【错误】这里有一个错误！！！ 在取值很大的情况，%后的结果 跟标准答案不一致。
        //        怀疑是double类型导致的精度丢失问题。  所以给double结果 先进行了 long强制转换，再进行取模运算 => 结果正确
        // 错误语句如下： return (int) (ans % (Math.pow(10,9) + 7)); 相当于   ${long} % ${double} 这样是不行的
        //             =》 要强制类型转换变成 ${long} % (long) ${double} 才能避免精度丢失问题导致的结果错误
        return (int) (ans % (long) (Math.pow(10, 9) + 7));
    }


    public static int max1(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                int minNum = Integer.MAX_VALUE;
                int sum = 0;
                for (int k = i; k <= j; k++) {
                    sum += arr[k];
                    minNum = Math.min(minNum, arr[k]);
                }
                max = Math.max(max, minNum * sum);
            }
        }
        return max;
    }

    public static int max2(int[] arr) {
        int size = arr.length;
        int[] sums = new int[size];
        sums[0] = arr[0];
        for (int i = 1; i < size; i++) {
            sums[i] = sums[i - 1] + arr[i];
        }
        int max = Integer.MIN_VALUE;
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < size; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                int j = stack.pop();
                max = Math.max(max, (stack.isEmpty() ? sums[i - 1] : (sums[i - 1] - sums[stack.peek()])) * arr[j]);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int j = stack.pop();
            max = Math.max(max, (stack.isEmpty() ? sums[size - 1] : (sums[size - 1] - sums[stack.peek()])) * arr[j]);
        }
        return max;
    }

    public static int[] gerenareRondomArray() {
        int[] arr = new int[(int) (Math.random() * 20) + 10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 101);
        }
        return arr;
    }

    public static void main(String[] args) {
        int testTimes = 2000000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            int[] arr = gerenareRondomArray();
            if (max1(arr) != max2(arr)) {
                System.out.println("FUCK!");
                break;
            }
        }
        System.out.println("test finish");
    }

    // 本题可以在leetcode上找到原题
    // 测试链接 : https://leetcode.com/problems/maximum-subarray-min-product/
    // 注意测试题目数量大，要取模，但是思路和课上讲的是完全一样的
    // 注意溢出的处理即可，也就是用long类型来表示累加和
    // 还有优化就是，你可以用自己手写的数组栈，来替代系统实现的栈，也会快很多
    public static int maxSumMinProduct(int[] arr) {
        int size = arr.length;
        long[] sums = new long[size];
        sums[0] = arr[0];
        for (int i = 1; i < size; i++) {
            sums[i] = sums[i - 1] + arr[i];
        }
        long max = Long.MIN_VALUE;
        int[] stack = new int[size];
        int stackSize = 0;
        for (int i = 0; i < size; i++) {
            while (stackSize != 0 && arr[stack[stackSize - 1]] >= arr[i]) {
                int j = stack[--stackSize];
                max = Math.max(max,
                        (stackSize == 0 ? sums[i - 1] : (sums[i - 1] - sums[stack[stackSize - 1]])) * arr[j]);
            }
            stack[stackSize++] = i;
        }
        while (stackSize != 0) {
            int j = stack[--stackSize];
            max = Math.max(max,
                    (stackSize == 0 ? sums[size - 1] : (sums[size - 1] - sums[stack[stackSize - 1]])) * arr[j]);
        }
        return (int) (max % 1000000007);
    }


    public int testMaxSumMinProduct(int[] arr) {
        int[] preSum = preSum(arr);
        int max = -1;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[i] <= arr[stack.peek()]) {
                int popI = stack.pop();
                if (arr[i] < arr[popI]) {
                    int l = stack.isEmpty() ? -1 : stack.peek();
                    int r = i;
                    if (l == -1) {
                        max = Math.max(max, preSum[r - 1]);
                    } else {
                        max = Math.max((preSum[r - 1] - preSum[l]) * arr[popI], max);
                    }
                }
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int popI = stack.pop();
            int l = stack.isEmpty() ? -1 : stack.peek();
            int r = arr.length;
            if (l == -1) {
                max = Math.max(max, preSum[r - 1]);
            } else {
                max = Math.max((preSum[r - 1] - preSum[l]) * arr[popI], max);
            }
        }
        return max;
    }

    public int[] preSum(int[] arr) {
        int[] preSum = new int[arr.length];
        preSum[0] = arr[0] % 1000000007;
        for (int i = 1; i < arr.length; i++) {
            preSum[i] = ((arr[i]  % 1000000007) + preSum[i - 1]) % 1000000007;
        }
        return preSum;
    }
}
