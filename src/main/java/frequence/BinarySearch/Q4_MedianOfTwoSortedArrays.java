package frequence.BinarySearch;

/**
 * 4. 寻找两个正序数组的中位数
 *
 * <p>给定两个大小分别为{@code M}和{@code N}的正序整数数组{@code nums1}和{@code nums2}，
 * 返回这两个正序数组合并后的中位数。
 *
 * <p>要求算法的时间复杂度为{@code O(log(M + N))}。
 *
 * <p>TODO: 【难题-核心模型】不要真正合并数组，而是在两个数组中分别确定分割线，使：
 * <pre>
 * 左半部分元素数量固定；
 * 左半部分所有元素 <= 右半部分所有元素。
 * </pre>
 * 找到合法分割后，中位数只与两条分割线左右相邻的四个元素有关。
 *
 * <p>TODO: 【当前实现的建模方式】当前版本二分的是“shortest左半部分最后一个元素的下标mid”，
 * {@code ans == -1}表示shortest左侧一个元素也没有。该模型可以实现，但边界分支较多。
 * 更稳定的标准模板是二分“shortest左侧选择了几个元素i”，使{@code i}的范围天然为
 * {@code [0, shortest.length]}，再通过正负无穷哨兵统一处理数组两端。
 *
 * <p>TODO: 【当前版本确定存在的边界错误】题目允许其中一个数组为空。当shortest为空且总长度
 * 为偶数时，循环不会执行，随后偶数分支会访问{@code shortest[0]}并抛出数组越界异常。
 * 反例：{@code nums1=[]，nums2=[1,2]}。标准实现和完整推演参见同目录
 * {@code 两个正序数组中位数.md}。
 */
public class Q4_MedianOfTwoSortedArrays {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length;
        int n = nums2.length;

        int leftSize = (m + n + 1) / 2;

        // 二分 不是分的下标。是分的 左区间个数。
        int l = 0;
        int r = m;
        while (l <= r) {
            int i = (l + r) / 2;
            int j = leftSize - i;

            int ALeft = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
            int ARight = i == m ? Integer.MAX_VALUE : nums1[i];
            int BLeft = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
            // TODO: 【错误】nums2是要判断 是否j 占满了nums所有元素。  要与n（即nums2.length）对比
            //  【错误行】int BRight = j == m ? Integer.MAX_VALUE : nums2[j];
            int BRight = j == n ? Integer.MAX_VALUE : nums2[j];

            if (ALeft > BRight) {
                r = i - 1;
            } else if (BLeft > ARight) {
                l = i + 1;
            } else {
                if ((m + n) % 2 == 1) {
                    return Math.max(ALeft, BLeft);
                } else {
                    int leftMax = Math.max(ALeft, BLeft);
                    int rightMin = Math.min(ARight, BRight);
                    return ((double) leftMax + rightMin) / 2;
                }
            }
        }
        throw new IllegalArgumentException("需要保证入参数组有序");
    }
}
