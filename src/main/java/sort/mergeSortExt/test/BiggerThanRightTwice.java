package sort.mergeSortExt.test;

/**
 * 求每一个数num 右边有多少个数*2之后 <num
 */
public class BiggerThanRightTwice {

    public static int biggerThanRightTwice(int[] nums) {
        return process(nums, 0, nums.length - 1);
    }

    public static int process(int[] arr, int l, int r) {
        if (l >= r) {
            return 0;
        }
        int m = (l + r) / 2;

        return process(arr, l, m)
                + process(arr, m + 1, r)
                + merge(arr, l, m ,r);

        // TODO: 【错误】左process 右process 也都是有值的。 只要涉及左右merge，就会计算每一个数字"比右侧两倍大"这一个事情。 所以每一个process你都要加上
//        process(arr, l, m);
//        process(arr, m + 1, r);
//        return merge(arr, l, m, r);
    }

    public static int merge(int[] arr, int l, int m, int r) {
        int len = r - l + 1;
//        int p1 = l;
//        int p2 = m + 1;
//        int ans = 0;
//        while (p1 <= m && p2 <= r) {
//            if (arr[p1] > 2 * arr[p2]) {
//                p2++;
//            } else {
//                ans += p2 - (m + 1);
//                p1++;
//            }
//        }
//        if (p2 > r) {
//            ans += (m - p1 + 1) * (r - m);
//        }
        int ans = 0;
        int p1 = l;
        int p2 = m + 1;
        for (; p2 <= r; p2++) {
            while (p1 <= m && arr[p1] <= 2 * arr[p2]) {
                p1++;
            }
            ans += m + 1 - p1;
        }



        int[] help = new int[len];
        p1 = l;
        p2 = m + 1;
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
