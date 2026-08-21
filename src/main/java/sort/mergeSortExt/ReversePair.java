package sort.mergeSortExt;


/**
 * 求每一个元素右侧比它小的个数
 */
public class ReversePair {



    public static int reversePairNumber(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        return process(arr, 0, arr.length - 1);
    }

    // 归并排序的递归改写
    // 意义: (1)排序并且(2)返回l,r范围上的逆序对
    // base case: l >= r  return arr[l]
    // 拆解方式： mid -> 左递归(l, mid) 右递归(mid+1, r)
    public static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        int mid = l + (r - l) / 2;
        int left = process(arr, l, mid);
        int right = process(arr, mid + 1, r);
        // TODO: merge()
        int own = merge(arr, l, mid, r);
        return left + right + own;
    }


    // 输入：l~m有序  m+1~r有序
    // 函数语义：排序 同时 返回右侧对左侧的小和累加和
    public static int merge(int[] arr, int l, int mid, int r) {
        if (l == r) {
            return 0;
        }
        int p1 = l;
        int p2 = mid + 1;
        int sum = 0;
        int[] help = new int[r - l + 1];
        int i = 0;
        while (p1 <= mid && p2 <= r) {
            // TODO： 相交于 小和问题：求 左<右时的 右侧大的个数， 这个是求 右<左时的 左侧大的个数
            //  只需要修改下列部分
//            if (arr[p2] > arr[p1]) {   // 左侧落help，就要 计算smallSum。 必须明确p1小于 右侧全部
//                smallSum += arr[p1] * (r - p2 + 1);
//                help[i++] = arr[p1++];
//            } else {   // arr[p2] <= arr[p1]
//                help[i++] = arr[p2++];
//            }
            if (arr[p2] < arr[p1]) {
                sum += mid - p1 + 1;
                help[i++] = arr[p2++];
            } else {   // arr[p2] >= arr[p1]
                help[i++] = arr[p1++];
            }
        }

        while (p1 <= mid) {
            help[i++] = arr[p1++];
        }

        while (p2 <= r) {
            help[i++] = arr[p2++];
        }


        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }

        return sum;
    }


    // for test
    public static int comparator(int[] arr) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    ans++;
                }
            }
        }
        return ans;
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
        int maxSize = 100;
        int maxValue = 100;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            if (reversePairNumber(arr1) != comparator(arr2)) {
                System.out.println("Oops!");
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println("测试结束");
    }


}
