package binary_search;

import java.util.Arrays;

/**
 * 小于等于的最右
 */
public class BSNearRight {
    // <= 就记录，然后看右区间
    // > 看左区间
//    public static int lessEqualMostRight(int[] arr, int val) {
//        if (arr == null || arr.length < 1) {
//            return -1;
//        }
//        int l = 0;
//        int r = arr.length - 1;    // 【错误】数组最后一个下标是 **n - 1** !!!!!! 不是！！！！
//        int mostRight = -1;
//        while (l <= r) {
//            int mid = l + (r - l) / 2;
//            if (arr[mid] <= val) {
//                mostRight = mid;   // 记下来
//                l = mid + 1;      // 看右区间，有没有更大的
//            } else {
//                r = mid - 1;
//            }
//        }
//        return mostRight;
//    }


    public static int lessEqualMostRight(int[] arr, int val) {
        int l = 0;
        int r = arr.length - 1;
        int ans = -1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] <= val) {
                ans = mid;
                l = mid  + 1;
            } else {
                r = mid - 1;
            }
        }
        return ans;
    }


    // for test
    public static int test(int[] arr, int value) {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] <= value) {
                return i;
            }
        }
        return -1;
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
    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 10;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
            Arrays.sort(arr);
            int value = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
            if (test(arr, value) != lessEqualMostRight(arr, value)) {
                printArray(arr);
                System.out.println(value);
                System.out.println(test(arr, value));
                System.out.println(lessEqualMostRight(arr, value));
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");
    }

}
