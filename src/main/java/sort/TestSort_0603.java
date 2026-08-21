package sort;

public class TestSort_0603 {

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    // 1. quick sort
    public static int[] partition(int[] arr, int l, int r) {
        int less = l - 1;
        int more = r;
        int i = l;
        int pivot = arr[r];
        while (i < more) {
            if (arr[i] < pivot) {
                swap(arr, i++, ++less);
            } else if (arr[i] > pivot) {
                swap(arr, i, --more);
            } else {
                i++;
            }
        }
        swap(arr, more, r);
        return new int[]{less + 1, more};
    }

    public static void process1(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        int len = r - l + 1;
        int randomIndex = l + (int) (Math.random() * len);
        swap(arr, randomIndex, r);
        int[] p = partition(arr, l, r);
        process1(arr, l, p[0] - 1);
        process1(arr, p[1] + 1, r);
    }

    public static void quickSort(int[] arr) {
        process1(arr, 0, arr.length - 1);
    }

    // 2. mergeSort
    public static void merge(int[] arr, int l, int m, int r) {
        int p1 = l;
        int p2 = m + 1;
        int len = r - l + 1;
        int[] help = new int[len];
        int i = 0;
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < len; i++) {
            arr[l + i] = help[i];
        }
    }

    public static void process2(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }

        int m = (l + r) / 2;
        process2(arr, l, m);
        process2(arr, m + 1, r);
        merge(arr, l, m, r);
    }

    public static void mergeSort(int[] arr) {
        process2(arr, 0, arr.length - 1);
    }


    // 3. heapSort
    public static void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1;
        while (left < heapSize) {
            int largestIndex = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            if (arr[index] < arr[largestIndex]) {
                swap(arr, index, largestIndex);
                index = largestIndex;
                left = index * 2 + 1;
            } else {
                break;
            }
        }
    }

    public static void heapSort(int[] arr) {
        // build 一步 + swap 两步
        for (int i = arr.length - 1; i >= 0; i--) {   // STEP1: build配for循环，里面只有一行
            heapify(arr, i, arr.length);
        }

        int heapSize = arr.length;
        // TODO: 【错误点】 所有容器的 【size必须>0 (or size>=1)时，才能执行循环体】
//        while (heapSize >= 0) {
        while (heapSize > 0) {  // STEP2: sort配while(size>0)，里面只有两行swap+heapify
            swap(arr, 0, --heapSize);
            heapify(arr, 0, heapSize);
        }
    }


}
