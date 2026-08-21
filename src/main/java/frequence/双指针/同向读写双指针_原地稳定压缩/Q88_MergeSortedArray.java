package frequence.双指针.同向读写双指针_原地稳定压缩;

/**
 * LeetCode 88：合并两个有序数组。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743311699。用户版本见{@link #myMerge}。
 *
 * <p>{@code nums1} 的长度为{@code m + n}，前{@code m}个位置是有效有序数据，末尾预留
 * {@code n}个位置；{@code nums2}包含{@code n}个有序元素。要求将结果原地合并到
 * {@code nums1}。
 *
 * <p><b>模型：两个输入指针 + 一个反向输出指针。</b>
 * {@code i}和{@code j}分别读取两个数组的有效末尾，{@code write}从{@code nums1}末尾写入。
 * 每次比较两个未处理区间的最大值，将较大者放到结果末尾。
 *
 * <p>不能直接从前向后写，因为会覆盖{@code nums1}中尚未读取的有效元素。反向合并利用了
 * {@code nums1}末尾的预留空间，不会破坏待处理数据。
 *
 * <p>主循环只需要保证{@code nums2}处理完：若{@code nums1}先耗尽，继续复制
 * {@code nums2}；若{@code nums2}先耗尽，{@code nums1}剩余元素已经位于正确位置。
 * 时间复杂度为 O(M + N)，额外空间复杂度为 O(1)。
 */
public class Q88_MergeSortedArray {

    /**
     * 必须从后往前，是因为为了避免覆盖。
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void myMerge(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1;
        int p2 = n - 1;
        int index = m + n - 1;
        while (p1 >= 0 && p2 >= 0) {
            nums1[index--] = nums1[p1] > nums2[p2] ? nums1[p1--] : nums2[p2--];
        }

        while (p2 >= 0) {
            nums1[index--] = nums2[p2--];
        }
    }



    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;
        int j = n - 1;
        int write = m + n - 1;

        while (j >= 0) {
            if (i >= 0 && nums1[i] > nums2[j]) {
                nums1[write--] = nums1[i--];
            } else {
                nums1[write--] = nums2[j--];
            }
        }
    }
}
