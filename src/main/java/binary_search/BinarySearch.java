package binary_search;

import java.util.Arrays;

/**
 * 二分法的重点就是【缩小区间】 l,r不断缩小范围
 * 四道题：
 * 1. 二分法查找一个数字是否存在 =》 前提：排序数组
 * 2. BS查找 <= 一个数字的最右 =》 前提：排序数组，这种求最左最右的 一定要二分到底
 * 3. BS查找 >= 一个数字的最左 =》 前提：排序数组，这种求最左最右的 一定要二分到底
 * 4. 不排序的情况：给定一个数组，任一相邻的元素不相等，局部最小问题
 */
public class BinarySearch {

//    public static int BSExist(int[] arr, int val) {
//        if (arr == null || arr.length < 1) {
//            return -1;
//        }
//        int l = 0;
//        int r = arr.length - 1;
//        while (l <= r) {
//            int mid = l + (r - l) / 2;
//            if (arr[mid] == val) {
//                return mid;
//            } else if (arr[mid] > val) {     // arr[mid]>val， 看左区间
//                r = mid - 1;
//            } else {
//                l = mid + 1;
//            }
//        }
//        return -1;    // 找不到返回-1
//    }

    // TODO: 【错误点】不要搞反了！！！   arr[mid] 和 mid的关系，自己脑子里画一张图
    public static int BSExist(int[] arr, int pivot) {
        int l = 0;
        int r = arr.length - 1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] < pivot) {
                l = mid + 1;
            } else if (arr[mid] > pivot) {
                r = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // for test
    public static int test(int[] sortedArr, int num) {
        for (int i = 0; i < sortedArr.length; i++) {
            if (sortedArr[i] == num) {
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

    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 10;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
            Arrays.sort(arr);
            int value = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
            if ((test(arr, value) != -1) != (BSExist(arr, value) != -1)) {
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");
    }
}
