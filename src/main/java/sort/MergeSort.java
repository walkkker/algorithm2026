package sort;

import java.util.Arrays;

/***
 * 递归和非递归都用到了核心的 merge方法
 */
public class MergeSort {

    public static void merge(int[] arr, int L, int M, int R) {
        if (L == R) {
            return;
        }
        int len = R - L + 1;
        int[] help = new int[len];
        int p1 = L;
        int p2 = M + 1;
        int i = 0;
        while (p1 <= M && p2 <= R) {
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= M) {
            help[i++] = arr[p1++];
        }
        while (p2 <= R) {
            help[i++] = arr[p2++];
        }
        // 系统建议替换：System.arraycopy(help, 0, arr, L + 0, len);
        for (int j = 0; j < len; j++) {
            arr[L + j] = help[j];
        }
    }

    public static void process(int[] arr, int l, int r) {
        // 递归不应该做业务层次判断，应该由调用方决定！！！
        // 递归只需要定位 base case
//        if (arr == null || arr.length < 2) {
//            return;
//        }
        if (l == r) {
            return;
        }
        int m = l + (r - l) / 2;
        process(arr, l, m);
        process(arr, m + 1, r);
        merge(arr, l, m, r);
    }

    public static void mergeSort1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    // 非递归  步长+循环
    public static void mergeSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int len = arr.length;
        int step = 1;
        while (step < len) {
            int l = 0;
            while (l < len) {
                int m = Math.min(l + step - 1, len - 1);
                int r = Math.min(m + 1 + step - 1, len - 1);
                merge(arr, l, m, r);
                l = r + 1;
            }
            // 防溢出，java中数组的长度是int类型表示 -> 2^31 - 1
            // int a = Integer.MAX_VALUE; a * 2 = -2
            // TODO：这里犯了大错误！！！，int b = a/2  b * 2 < a存在可能（2&5） 所以=时，一定要继续做while循环
            //  而且，本着「分层思想，各司其职」的代码编写风格。防溢出的代码应该只break 可能溢出部分，对于所有不溢出的情况，
            //  都应该放行，正常 *2 走while循环！！！ （不要僭越，想不明白的时候容易出问题）
            //
//            if (step < len / 2) {
            if (step <= len / 2) {
                step *= 2;
            } else {
                break;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {45, 23, 1, -5, -8, 234};
        mergeSort2(arr);
        System.out.println(Arrays.toString(arr));

        int a = Integer.MAX_VALUE;
        System.out.println(a * 2);
    }
}
