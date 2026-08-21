package sort.mergeSortExt.test;

/**
 * 数组中，每一个元素 左侧范围 比它小的数字 加起来的累加和，称之为小和。求整个数组每个元素的小和总和
 * <p>
 * 每个元素 左侧 比它小的元素总和  可以转换为=》 求 每个元素 【右侧有多少个数比它大】 的总和 -》 将对每个数左侧的累加和 变成了 每个数 * 右侧大于它的个数 （比较过程就会算出个数）
 * <p>
 * 于是 可以使用 mergeSort, 然后在 merge 操作中， 从左至右，左组拷贝的时候，计算右组的个数
 * <p>
 */
public class SmallSum {

    public static int smallSum(int[] arr) {
        return  (int) (process(arr, 0, arr.length - 1));
    }

    public static long process(int[] arr, int l, int r) {
        if (l >= r) {
            return 0;
        }
        int m = (l + r) / 2;
        return process(arr, l, m)
                + process(arr, m + 1, r)
                + merge(arr, l, m, r);
    }


    public static long merge(int[] arr, int l, int m, int r) {
        int len = r - l + 1;
        long[] preSum = new long[len];
        preSum[0] = arr[l];
        for (int i = 1; i < len; i++) {
            preSum[i] = preSum[i - 1] + arr[l + i];
        }

        int ans = 0;
        int windowL = l;
        for (int R = m + 1; R <= r; R++) {
            while (windowL <= m && arr[windowL] < arr[R]) {
                windowL++;
            }
//  TODO: 【错误-漏掉了下标转换！！！】 ans += windowL == l ? 0 : preSum[windowL - 1];
            ans += windowL == l ? 0 : preSum[windowL - 1 - l];
        }

        int p1 = l;
        int p2 = m + 1;
        int[] help = new int[len];
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



    // for test
    public static int comparator(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int res = 0;
        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < i; j++) {
                res += arr[j] < arr[i] ? arr[j] : 0;
            }
        }
        return res;
    }

    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
    }

    // for test
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
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

    // for test
    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    // for test
    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 1000;
        int maxValue = 1000;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            if (smallSum(arr1) != comparator(arr2)) {
                succeed = false;
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");
    }

}
