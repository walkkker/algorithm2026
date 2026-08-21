package frequence.Heap;

/**
 * 215. 数组中的第K个最大元素
 *
 * TODO: 这道题其实挺难的。 解题算法是quickSelect，是quickSort的变种。基于partition，但是由于每次舍弃一般，所以最终数据量是O(N)。
 * TODO：多多感受，很重要这道题。   T(N) = T(N/2) + O(N)  < 2N
 *
 * <p>给定整数数组{@code nums}和整数{@code k}，返回数组中第{@code k}个最大的元素。
 *
 * <p>需要查找的是数组排序后的第{@code k}个最大元素，而不是第{@code k}个不同的元素。
 */
public class Q215_KthLargestElementInAnArray {

    /**
     本题核心算法是 QuickSelect: 实现上就是 类似二分 + partition。  神似快排，但是因为扫描的数据量不一样，所以平均时间复杂度为O(N)，最差退化到O(N^2)
     */
    class Solution {
        public int findKthLargest(int[] nums, int k) {
            int targetIndex = nums.length - k;
            int l = 0;
            int r = nums.length - 1;
            while (l <= r) {
                int[] p = partition(nums, l, r);
                // TODO: 【错误】你这里要用 targetIndex去比较了。  因为targetIndex才对应 升序数组。
                // if (k >= p[0] && k <= p[1]) {
                //     return nums[p[0]];
                // } else if (k < p[0]) {
                //     r = p[0] - 1;
                // } else {
                //     l = p[1] + 1;
                // }
                if (targetIndex >= p[0] && targetIndex <= p[1]) {
                    return nums[targetIndex];
                } else if (targetIndex < p[0]) {
                    r = p[0] - 1;
                } else {
                    l = p[1] + 1;
                }
            }
            return 0;
        }

        public int[] partition(int[] arr, int l, int r) {
            int pivotIndex = l + (int) (Math.random() * (r - l + 1));
            int pivot = arr[pivotIndex];
            int less = l - 1;
            int more = r + 1;
            int index = l;
            while (index < more) {
                if (arr[index] == pivot) {
                    index++;
                } else if (arr[index] < pivot) {
                    swap(arr, index++, ++less);
                } else {
                    swap(arr, index, --more);
                }
            }
            return new int[]{less + 1 , more - 1};
        }

        public void swap(int[] arr, int i, int j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }}
