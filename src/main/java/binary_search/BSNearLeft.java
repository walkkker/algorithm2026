package binary_search;

import java.util.Arrays;

/**
 * >=val 的最左
 */
public class BSNearLeft {

    // >= 最左
//    public static int moreEqualMostLeft(int[] arr, int val) {
//        if (arr == null || arr.length < 1) {
//            return -1;
//        }
//        int l = 0;
//        int r = arr.length - 1;
//        int index = -1;
//        while (l <= r) {
//            int mid = l + (r - l) / 2;
//            if (arr[mid] >= val) {
//                index = mid;
//                r = mid - 1;
//            } else {
//                l = mid + 1;
//            }
//        }
//        return index;
//    }

    // moreEqualMostLeft
    public static int moreEqualMostLeft(int[] arr, int val) {

        int l = 0;
        int r = arr.length - 1;

        int ans = -1;  // TODO: 要先假设不存在，-1
//        while (l >= r) {  // TODO: 【错误点！！！】 不要跟排序递归base case搞混了！！！ 那个是base条件，if(l>=r)； 这个是二分条件，必须保证while(l <= r)
        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] >= val) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return ans;
    }


    // for test
    public static int test(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= value) {
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
            if (test(arr, value) != moreEqualMostLeft(arr, value)) {
                printArray(arr);
                System.out.println(value);
                System.out.println(test(arr, value));
                System.out.println(moreEqualMostLeft(arr, value));
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");
    }


}
