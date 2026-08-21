package sort;

import java.util.Arrays;

/*
包含三个基础排序：冒泡，选择，插入
（1） 必要函数 swap
（2）升序：
    冒泡：每次卡个右边界 -> 最大值每次升到最右侧
    选择：每次卡左边界 -> 每次选择最小值，与最左侧交换
    插入排序：每次卡[0, k]区间 -> 从左往右遍历，每次将当前值与左侧值比对
 */
public class BasicSort {

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j + 1 <= i; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 这个地方注意：不要写成错误的 swap(arr, i, j)
                    swap(arr, j, j + 1);
                }
            }
        }
    }


    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int minValue = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = i; j < n; j++) {
                if (arr[j] <= minValue) {
                    minValue = arr[j];
                    minIndex = j;
                }
            }
            swap(arr, i, minIndex);
        }
    }

    public static void insertionSort(int[] arr) {
        int n = arr.length;
//        if (n < 2) {
//            return;
//        }

        // i = 0 没有意义，因为此时 左区间（插排区间）只有一个数字，不需要比较
        // 第二行：当前数j 一直跟前一个数j-1比较， 若j<j-1，则交换位置
        for (int i = 1; i < n; i++) {
            for (int j = i; j - 1 >= 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j, j - 1);
                } else {
                    // TODO: 这句非常重要，我跟左神写的不一样。因为前面的数字已经排好序了，此时不再需要检查前面部分
                    //  ，直接break退出本次循环。 进入下一个插排区间的检查。
                    //  【注意】只有加上这个break，才算是插排。 才能实现**好情况下的 时间常数项优化**
                    break;
                }
            }
        }


    }


    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    public static void main(String[] args) {
        int[] arr = new int[]{5, 7, 3, 6, 78, 1, 3, 67, 87};
        insertionSort(arr);
        System.out.println(Arrays.toString(arr));
    }


}
