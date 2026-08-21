package 子数组累加和长度三连.test20260721;

/**
 * 题目三
 * 给定一个整数组成的无序数组arr，值可能正、可能负、可能0
 * 给定一个整数值K
 * 找到arr的所有子数组里，哪个子数组的累加和<=K，并且是长度最大的
 * 返回其长度
 */
public class Code03_LongestLessSumSubArrayLength {
    // TODO：核心： r - l <= K  推出  l >= r - K， 目标是针对每一个r，找到满足条件的第一个k (大于等于的最左)
    // TODO：[难点]：preSum不是有序的，但是观察后，可以转换为 maxPre数组，一定有序递增
    public static int getMaxLength(int[] arr, int K) {
        int len = arr.length;
        long[] pre = new long[len];
        pre[0] = arr[0];
        long[] maxPre = new long[len];
        maxPre[0] = pre[0];
        for (int i = 1; i < len; i++) {
            pre[i] = pre[i - 1] + arr[i];
            maxPre[i] = Math.max(maxPre[i - 1], pre[i]);
        }

        int ans = 0;
        for (int i = 0; i < len; i++) {
            if (pre[i] <= K) {
                ans = Math.max(ans, i + 1);
            }

            long tmp = pre[i] - K;   // TODO：【错误】注意，所有跟pre/maxPre相关的计算都是long类型
            int l = findEarliest(maxPre, 0, i - 1, tmp);
            if (l != -1) {
                ans = Math.max(ans, i - l);
            }
        }
        return ans;
    }

    // TODO: 【错误】参数类型 -> public static int findEarliest(long[] arr, int l, int r, int target) {
    public static int findEarliest(long[] arr, int l, int r, long target) {
        int ans = -1;
        while (l <= r) {
            int m = (l + r) / 2;
            if (arr[m] >= target) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans;
    }


    public static int maxLengthAwesome(int[] arr, int K) {
        int len = arr.length;
        int[] minSum = new int[len];
        int[] minSumEnd = new int[len];   // 注意，这里追求的是最短
        minSum[len - 1] = arr[len - 1];
        minSumEnd[len - 1] = len - 1;
        for (int i = len - 2; i >= 0; i--) {
            // TODO: 下面写的全错了！！！ 不是看arr[i]+-0，是取决于minSum[i+1]！！！
//            if (arr[i] < 0) {
//                minSum[i] = minSum[i + 1] + arr[i];
//                minSumEnd[i] = minSumEnd[i + 1];
//            } else {
//                minSum[i] = arr[i];
//                minSumEnd[i] = i;
//            }
            // TODO: 【特别注意】 下面的if   < 还是 <= 结果都正确。
            if (minSum[i + 1] < 0) {     // TODO: 一定要注意，这里if比较的是 minSum[i+1]！！！ 这样才可以借由【之前计算的结果】加速当前计算。
                minSum[i] = minSum[i + 1] + arr[i];
                minSumEnd[i] = minSumEnd[i + 1];
            } else {
                minSum[i] = arr[i];
                minSumEnd[i] = i;
            }
        }

        int sum = 0;
        int end = 0;
        int ans = 0;
        for (int i = 0; i < len; i++) {
            while (end < len && sum + minSum[end] <= K) {
                sum += minSum[end];
                end = minSumEnd[end] + 1;     // TODO: 注意这里end一直是下一个位置， 开区间
            }


            // TODO: 【易错！！！遗漏】下面这两个分类，以及两个分类对应的处理逻辑，一定要注意！！！
            if (end > i) {   // 成功向右扩了
                ans = Math.max(ans, end - i);
                sum -= arr[i];
            } else {   // [i,i) 区间，arr[i] > K ，当前元素都没法扩
                end = end + 1;
            }
        }
        return ans;
    }

    public static int maxLength(int[] arr, int k) {
        int[] h = new int[arr.length + 1];
        int sum = 0;
        h[0] = sum;
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            h[i + 1] = Math.max(sum, h[i]);
        }
        sum = 0;
        int res = 0;
        int pre = 0;
        int len = 0;
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            pre = getLessIndex(h, sum - k);
            len = pre == -1 ? 0 : i - pre + 1;
            res = Math.max(res, len);
        }
        return res;
    }

    public static int getLessIndex(int[] arr, int num) {
        int low = 0;
        int high = arr.length - 1;
        int mid = 0;
        int res = -1;
        while (low <= high) {
            mid = (low + high) / 2;
            if (arr[mid] >= num) {
                res = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }

    // for test
    public static int[] generateRandomArray(int len, int maxValue) {
        int[] res = new int[len];
        for (int i = 0; i != res.length; i++) {
            res[i] = (int) (Math.random() * maxValue) - (maxValue / 3);
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        for (int i = 0; i < 10000000; i++) {
            int[] arr = generateRandomArray(10, 20);
            int k = (int) (Math.random() * 20) - 5;
            if (maxLengthAwesome(arr, k) != maxLength(arr, k)) {
                System.out.println("awesome Oops!");
            }
            if (getMaxLength(arr, k) != maxLength(arr, k)) {
                System.out.println("mine Oops!");
            }
        }
        System.out.println("test finish");
    }

}
