package sort.mergeSortExt;

/**
 * 求每一个数num 右边有多少个数*2之后 <num
 * <p>
 * 【数组每个元素】 【右侧】 【小于】 问题 ==》 mergeSort类型问题
 * <p>
 * 但是， 要 ?*2 < num ， 所以 【非元素本身】进行比较 ==》 计算过程（使用指针不回退的技巧， for外循环 + while内部条件） 与 merge过程分开即可 （因为这类题，本质是 依赖 mergeSort的有序性）
 */
public class BiggerThanRightTwice {

    // 思路，从后往前走，可以计算个数
    public static int biggerThanRightTwice(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        return process(arr, 0, arr.length - 1);
    }


    public static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        int m = l + (r - l) / 2;
        return process(arr, l, m)
                + process(arr, m + 1, r)
                + merge(arr, l, m, r);
    }

    public static int merge(int[] arr, int l, int m, int r) {
        if (l == r) {
            return 0;
        }
        // 先做指针不回退操作  - 因为是看 > 右侧两倍的数字， 所以倒着走可以直接算出个数
        int p1 = m;
        int p2 = r;
        int count = 0;
        while (p1 >= l && p2 >= m + 1) {
            if (arr[p1] > 2 * arr[p2]) {
                count += p2 - m;
                p1--;
            } else {
                p2--;
            }
        }

        p1 = l;
        p2 = m + 1;
        int[] help = new int[r - l + 1];
        int i = 0;
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }

        while (p1 <= m) {
            help[i++] = arr[p1++];
        }

        while (p2 <= r) {
            help[i++] = arr[p2++];
        }

        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
        return count;
    }




    // for test
    public static int comparator(int[] arr) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > (arr[j] << 1)) {
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
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) ((maxValue + 1) * Math.random());
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
            if (biggerThanRightTwice(arr1) != comparator(arr2)) {
                System.out.println("Oops!");
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println("测试结束");
    }
}
