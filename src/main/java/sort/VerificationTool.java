package sort;

import java.util.Arrays;

public class VerificationTool {

    // Math.random() 返回类型为double,在[0,1)范围上等概率返回
    // 实现：
    // 1. [0, randomLength] 任意数组长度
    // 2. arr.element 取值范围 [-randomValue, randomValue]
    public static int[] generateRandomArray(int randomLength, int randomValue) {
        int len = (int) (Math.random() * (randomLength + 1));
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * (randomValue + 1)) - (int) (Math.random() * (randomValue + 1));
        }
        return arr;
    }

    public static void comparator(int[] arr) {
        Arrays.sort(arr);
    }


    public static int[] copyArr(int[] arr) {
        int len = arr.length;
        int[] copyArr = new int[len];
        for (int i = 0; i < len; i++) {
            copyArr[i] = arr[i];
        }
        return copyArr;
    }

    public static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }

        int len = arr1.length;
        for (int i = 0; i < len; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        int length = 1000;
        int value = 100000;
        int times = 10000;
        for (int i = 0; i < times; i++) {
            int[] arr = generateRandomArray(length, value);
//            System.out.println(Arrays.toString(arr));
            int[] arr1 = copyArr(arr);
            int[] arr2 = copyArr(arr);
            int[] arr3 = copyArr(arr);
            int[] arr4 = copyArr(arr);
            int[] arr5 = copyArr(arr);
            int[] arr6 = copyArr(arr);
            int[] arr7 = copyArr(arr);
            int[] arr8 = copyArr(arr);
            comparator(arr);
            BasicSort.selectionSort(arr1);
            BasicSort.bubbleSort(arr2);
            BasicSort.insertionSort(arr3);
//            MergeSort.mergeSort1(arr4);
            TestSort_0603.mergeSort(arr4);
            MergeSort.mergeSort2(arr5);
//            QuickSort.quickSort1(arr6);
            TestSort_0603.quickSort(arr6);
            QuickSort.quickSort2(arr7);
//            HeapSort.heapSort(arr8);
            TestSort_0603.heapSort(arr8);
            // java中，逻辑与 -> &&，逻辑或 -> ||
            // & 和 | 用于 位运算
            // if (!(a && b && c)) == if (!a || !b || !c)
            // 德摩根定律在逻辑运算中的具体应用。对于多个条件的逻辑与（&&）取反，等价于各自取反后的逻辑或（||）。
            if (!isEqual(arr, arr1)
                    || !isEqual(arr, arr2)
                    || !isEqual(arr, arr3)
                    || !isEqual(arr, arr4)
                    || !isEqual(arr, arr5)
                    || !isEqual(arr, arr6)
                    || !isEqual(arr, arr7)
                    || !isEqual(arr, arr8)) {
                System.out.println("Tests failed");
                return;
            }
//            System.out.println(Arrays.toString(arr));
        }
        System.out.println("Tests passed");
    }
}
