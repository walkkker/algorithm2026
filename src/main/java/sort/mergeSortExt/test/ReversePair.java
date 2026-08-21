package sort.mergeSortExt.test;

public class ReversePair {

    class Solution {
        public int reversePairs(int[] record) {
            return process(record, 0, record.length - 1);
        }

        public int merge(int[] arr, int l, int m, int r) {
            // TODO: 【错误！】 merge方法里面对应的 len 是 这一段merge子数组的长度！！！
            // int len = arr.length;
            int ans = 0;
            int windowL = l;
            for (int R = m + 1; R <= r; R++) {
                // TODO: 【错误点-遗漏点】 windowL <= m 这个条件一定不能漏
                while (windowL <= m && arr[windowL] <= arr[R]) {
                    windowL++;
                }
                ans += m + 1 - windowL;
            }

            int p1 = l;
            int p2 = m + 1;
            int len = r - l + 1;
            int[] help = new int[len];
            int index = 0;
            while (p1 <= m && p2 <= r) {
                help[index++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
            }
            while (p1 <= m) {
                help[index++] = arr[p1++];
            }
            // TODO: 【错误点】专心问题，专心啊！！！while (p2 <= m) {
            while (p2 <= r) {
                help[index++] = arr[p2++];
            }
            for (index = 0; index < len; index++) {
                arr[l + index] = help[index];
            }
            return ans;
        }

        public int process(int[] arr, int l, int r) {
            if (l >= r) {
                return 0;
            }
            int mid = (l + r) / 2;
            return process(arr, l, mid)
                    + process(arr, mid + 1, r)
                    + merge(arr, l, mid, r);
        }
    }
}
