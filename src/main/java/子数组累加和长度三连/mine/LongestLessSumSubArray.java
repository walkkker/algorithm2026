package 子数组累加和长度三连.mine;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 三连：
 * /**
 * * 题目三
 * * 给定一个整数组成的无序数组arr，值可能正、可能负、可能0
 * * 给定一个整数值K
 * * 找到arr的所有子数组里，哪个子数组的累加和<=K，并且是长度最大的
 * * 返回其长度
 *
 *
 * 提供了两个解决方法：
 * （1）舍弃可能性，最优解， O(N) 很难
 * (2) 直观， 前缀和（快速求子数组累加和） +  线段树（快速求 某一个无序区间内 >= ? 的最左元素下标）
 *
 **/
public class LongestLessSumSubArray {
    // 解法1：这是最优解 dp+滑动窗口： O(n)
    public static int maxLengthAwesome(int[] arr, int k) {
        /*
            1. dp 计算以每个位置开头得到的最小子数组累加和 && 对应的end
            2. 很特殊的滑动窗口：从左向右遍历数组，维持一个[i,end)窗口代表<=k的最大长度。 基于1.的数组，可以测试能否不断往右扩
         */
        // S1
        int n = arr.length;
        int[] minSum = new int[arr.length];
        int[] minSumEnd = new int[arr.length];
        // DP
        minSum[n - 1] = arr[n - 1];
        minSumEnd[n - 1] = n - 1;
        for (int i = n - 2; i >= 0; i--) {
            if (minSum[i + 1] <= 0) {  // == 0 算在这里，因为我要minSum最长
                minSum[i] = arr[i] + minSum[i + 1];
                minSumEnd[i] = minSumEnd[i + 1];
            } else {
                minSum[i] = arr[i];
                minSumEnd[i] = i;
            }
        }
        // 滑动窗口 [i, end)
        int end = 0;
        int sum = 0;
        int maxLen = 0;
        // 下面是L指针的位置
        for (int i = 0; i < n; i++) {
            // end右边界 最远能够扩到哪里？  范围是[i, n - 1]
            while (end < n && sum + minSum[end] <= k) {
                sum += minSum[end];
                end = minSumEnd[end] + 1;
            }
            maxLen = Math.max(maxLen, end - i);

            // TODO: 【错误】只有当窗口内有数字的时候，L右移才会sum-arr[i]
            if (end > i) {
                sum -= arr[i];
            } else if (i == end) { // 当前end==i这个数字太大了，没有办法加入sum。 因为基于minSum理论，后面没有能让他更小的了，所以sum就没有加这个数字。=》结论就是，这个数字我们就不应该要，所以下一轮L++，我们也要自动的把end++
                end = end + 1;  // 当前end一点都匹配不上，下一轮从end+1开始匹配
            }
        }
        return maxLen;
    }

    // 解法2：前缀和+线段树：次优解O(n * logN) 但是这个方法很好理解
    // TMD: 照样一堆错，啊啊啊！！！！！！！！ 不专心啊
    public static int maxLengthAwesome2(int[] arr, int k) {
        // 前缀和， preSum[R] - preSum[L] <= k 即可算出子数组长度
        // 优化项在于 【在无序数组中 快速寻找 【一个区间的】 preSum[L] >= preSum[R] - k 的 最左元素】
        // 注意，这个无法使用 有序表，需要使用线段树。 （有序表只能找到>=k的最小元素，而不是 >=k的最左元素-因为这是一个无序数组，有可能>=k的最左元素）是一个非常大的值。

        // TODO:线段树的使用方式就是 max线段树。 然后着重于query()，查看孩子的max, 左>=k的就往左走，否则往右走，都不满足那么就返回-1说明这个区间没有目标数字
        int[] preSum = new int[arr.length];
        preSum[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            preSum[i] = preSum[i - 1] + arr[i];
        }
        MaxSegmentTree st = new MaxSegmentTree(preSum);
        // TODO: 【错误】st.build(1, 0, preSum.length); 起始L应该为1，而不是0！！！
        st.build(1, 1, preSum.length);
        int maxLen = 0;
        for (int i = 0; i < preSum.length; i++) {
            if (preSum[i] <= k) {
                maxLen = Math.max(maxLen, i + 1);
            } else {
// TODO: 【错误】你是不是沙雕？？？ 你算了 pre[L]>=pre[R]-k,然后最后写个k？？？  int mostLeft = st.queryMoreThanMostLeft(1, 1, preSum.length, 1, i + 1, k);
                int mostLeft = st.queryMoreThanMostLeft(1, 1, preSum.length, 1, i, preSum[i]-k);  // 你当前在i，你需要搜索的范围是[0,i-1] 对应到线段树是[1,i]！！！
                // TODO：注意可能存在无效值，因为有可能左侧的值都 <k,      不存在>=的,此时返回-1代表没有下标
                if (mostLeft != -1) {
                    //maxLen = Math.max(maxLen, i - mostLeft);  // 因为从线段树返回的mostLeft 为 index+1了，所以从 (L 变成了 [L  TODO: 【错误】，你都想到了，所以计算长度的时候，怎么能写 R-L呢？ 这样的话，长度不应该是R-L+1吗？
                    maxLen = Math.max(maxLen, i - mostLeft + 1);
                }
            }
        }
        return maxLen;
    }

    public static class MaxSegmentTree {

        int[] arr;
        int[] max;
        int N;

        public MaxSegmentTree(int[] _arr) {
            N = _arr.length + 1;
            arr = new int[N];
            max = new int[4 * N];
            for (int i = 1; i < N; i++) {
                arr[i] = _arr[i - 1];
            }
        }

        private void pushUp(int i) {
            max[i] = Math.max(max[i * 2], max[i * 2 + 1]);
        }

        public void build(int i, int L, int R) {
            if (L == R) {
                max[i] = arr[L];
                return;
            }
            int mid = L + (R - L) / 2;
            build(i * 2, L, mid);
            build(i * 2 + 1, mid + 1, R);
            pushUp(i);
            return;
        }

        // TODO: 【设计错误】就是每次求一个区间的 >=a 的最左值
        public int queryMoreThanMostLeft(int i, int L, int R, int l, int r, int k) {
            if (L == R) {
                return L;
            }
            int mid = L + (R - L) / 2;
            int ans = -1;
            if (max[2 * i] >= k && l <= mid) {  // TODO: 注意这两个l<=mid和 r>mid ，一定要清晰左右区间的范围，不要搞乱了
                ans = queryMoreThanMostLeft(i * 2, L, mid, l, r, k);
            } else if (r > mid && max[2 * i + 1] >= k) {
                ans = queryMoreThanMostLeft(2 * i + 1, mid + 1, R, l, r, k);
            } else {
                ans = -1;
            }
            return ans;
        }


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
            int ans1 = maxLengthAwesome2(arr, k);
            int ans2 = maxLength(arr, k);
            if (ans1 != ans2) {
                System.out.println(Arrays.toString(arr));
                System.out.println(k);
                System.out.println("ans1: " + ans1 + " + " + "ans2: " + ans2);
                System.out.println("Oops!");
                break;
            }
        }
        System.out.println("test finish");
    }

    public static void mai2(String[] args) {
        int[] arr = new int[]{0, 1, 10, 15, 26, 21, 30, 31, 31, 37, 45};
        MaxSegmentTree st = new MaxSegmentTree(arr);
        st.build(1, 1, arr.length);
        System.out.println(Arrays.toString(st.arr));
        System.out.println(Arrays.toString(st.max));
        int ans = st.queryMoreThanMostLeft(1, 1, arr.length, 1, 8, 20);
        System.out.println(ans);

    }
}
