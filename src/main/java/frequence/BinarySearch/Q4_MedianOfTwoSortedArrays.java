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
 * <p>TODO: 【历史实现的建模方式】旧版本二分的是“shortest左半部分最后一个元素的下标mid”，
 * {@code ans == -1}表示shortest左侧一个元素也没有。该模型可以实现，但边界分支较多。
 * 更稳定的标准模板是二分“shortest左侧选择了几个元素i”，使{@code i}的范围天然为
 * {@code [0, shortest.length]}，再通过正负无穷哨兵统一处理数组两端。
 *
 * <p>TODO: 【历史版本的边界错误，并非下方第三遍版本】题目允许其中一个数组为空。当shortest为空且总长度
 * 为偶数时，循环不会执行，随后偶数分支会访问{@code shortest[0]}并抛出数组越界异常。
 * 反例：{@code nums1=[]，nums2=[1,2]}。标准实现和完整推演参见同目录
 * {@code 两个正序数组中位数.md}。
 */
public class Q4_MedianOfTwoSortedArrays {

    /**
     * 第三遍复习（2026-09-22）：用户报告已完成，保留提交的实现。
     *
     * <p><b>用户理解：</b>1. 定位：nums1短、nums2长。left表示nums1左侧长度，
     * 通过公式换算nums2左侧长度。奇数时left多包含一个，偶数时left和right一样多。
     * 2. 追求满足的条件：nums1左 &lt; nums2右，nums2左 &lt; nums1右。
     * 【难点】虚拟边界，因为左/右不始终对应实际下标。
     *
     * <p><b>注释精确化，代码无需修改：</b>奇偶平衡指的是两个数组合起来的左半区与右半区，
     * 不是nums1自身的左右两部分；count是合并后的左半区总数，mid1是nums1贡献的个数。
     * 左/右边界比较的是分割线两侧的最大/最小元素，而不是整个数组或分区长度。
     * 允许重复值，因此合法条件是ALeft &lt;= BRight且BLeft &lt;= ARight，不能要求严格小于。
     * 当前代码仅在严格违反条件时移动边界，所以代码已正确处理相等情况。
     *
     * <p>l/r搜索范围[0,nums1.length]包含空左分区与整个短数组；mid2=count-mid1。
     * 在短数组上搜索保证mid2落在长数组合法切分范围内。
     * 空左分区使用MIN_VALUE，空右分区使用MAX_VALUE，统一大小比较而不访问越界下标。
     * 偶数答案在相加前转double，避免两个int相加溢出。
     *
     * <p>TODO: 【可选健壮性，不是本题错误】合法有序且总长度非0的输入必然找到切分，
     * 末尾return -1不会执行；通用API可抛异常，但-1本身可能是合法中位数，不能作通用失败标志。
     * 时间O(log(min(m,n)+1))，额外空间O(1)，交换参数最多递归一次。
     */
    public static class SolutionThirdReview20260922 {
        public double findMedianSortedArrays(int[] nums1, int[] nums2) {
            if (nums2.length < nums1.length) {
                return findMedianSortedArrays(nums2, nums1);
            }
            int count = (nums1.length + nums2.length + 1) / 2;
            int l = 0;
            int r = nums1.length; // l/r不是元素下标，而是nums1左侧区域的个数。
            while (l <= r) {
                int mid1 = (l + r) / 2;
                int mid2 = count - mid1;

                int ALeft = mid1 == 0 ? Integer.MIN_VALUE : nums1[mid1 - 1];
                int ARight = mid1 == nums1.length ? Integer.MAX_VALUE : nums1[mid1];
                int BLeft = mid2 == 0 ? Integer.MIN_VALUE : nums2[mid2 - 1];
                int BRight = mid2 == nums2.length ? Integer.MAX_VALUE : nums2[mid2];

                if (ALeft > BRight) {
                    r = mid1 - 1; // A左侧取多了。
                } else if (ARight < BLeft) {
                    l = mid1 + 1; // A左侧取少了。
                } else {
                    if ((nums1.length + nums2.length) % 2 == 1) {
                        return (double) Math.max(ALeft, BLeft);
                    } else {
                        return ((double) Math.max(ALeft, BLeft) + Math.min(ARight, BRight)) / 2;
                    }
                }
            }
            return -1;
        }
    }

    /**
     * 2026-09-14复盘版本：二分的是较短数组贡献给合并后左半区的元素数量，
     * 而不是寻找某个元素下标。时间复杂度为{@code O(log(min(m,n)))}。
     *
     * <p><b>核心思想和步骤：</b>
     * <ol>
     *     <li><b>确定数量不变量。</b>中位数来自左右分区：总长度为偶数时左右数量相等；
     *     总长度为奇数时让左侧多一个。因此
     *     {@code leftSize = (m + n + 1) / 2}。如果短数组左侧取{@code i}个，
     *     长数组左侧就必须取{@code j = leftSize - i}个。</li>
     *     <li><b>确定值域不变量。</b>合法分割必须满足左侧所有元素不大于右侧所有元素。
     *     两个数组内部本来就有序，所以只需检查两个交叉条件：
     *     {@code ALeft <= BRight}和{@code BLeft <= ARight}。</li>
     *     <li><b>使用虚拟边界。</b>当某个分区为空时，以负无穷表示空左区间的最大值，
     *     以正无穷表示空右区间的最小值。这样比较交叉条件和提取中间值都不需要额外分支。</li>
     * </ol>
     *
     * <p>TODO: 【难点1】{@code i}和{@code j}表示元素个数，因此{@code i}的范围是
     * {@code [0,m]}，右边界必须是{@code m}而不是{@code m - 1}。
     *
     * <p>TODO: 【难点2】四个虚拟边界同时承担两项职责：判断当前切分是否合法，以及在合法后
     * 直接取得左半区最大值和右半区最小值。哨兵的主要目的不是专门处理偶数情况，而是消除
     * 空分区的特殊判断；奇数和偶数都会受益。
     *
     * <p>TODO: 【核心推导】如果{@code ALeft > BRight}，说明短数组左侧取多了，减小i；
     * 如果{@code BLeft > ARight}，说明短数组左侧取少了，增大i。两个条件都不成立时，
     * 数量不变量和值域不变量同时满足，分割线就是中位数分界线。
     */
    class SolutionReviewed20260914 {
        public double findMedianSortedArrays(int[] nums1, int[] nums2) {
            // 只在短数组上二分：既得到最优复杂度，也保证j始终落在[0,n]。
            if (nums1.length > nums2.length) {
                return findMedianSortedArrays(nums2, nums1);
            }

            int m = nums1.length;
            int n = nums2.length;
            int leftSize = (m + n + 1) / 2; // TODO: 【难点1】对应Javadoc中的数量不变量。

            // 这里二分的是左侧元素个数，不是元素下标；0和m都是合法切分位置。
            int l = 0;
            int r = m;
            while (l <= r) {
                int i = l + (r - l) / 2; // nums1左侧区间的元素个数。
                int j = leftSize - i;    // nums2左侧区间的元素个数。

                // TODO: 【难点2】虚拟边界同时服务于合法性比较和最终中位数计算。
                int aLeft = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
                int aRight = i == m ? Integer.MAX_VALUE : nums1[i];
                int bLeft = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
                int bRight = j == n ? Integer.MAX_VALUE : nums2[j];

                if (aLeft > bRight) {
                    // nums1左侧取多了，i必须减小。
                    r = i - 1;
                } else if (bLeft > aRight) {
                    // nums1左侧取少了，i必须增大。
                    l = i + 1;
                } else {
                    // 此时aLeft <= bRight且bLeft <= aRight，找到了合法分割线。
                    if ((m + n) % 2 == 1) {
                        // 奇数时左侧多一个，中位数就是左半区最大值。
                        return Math.max(aLeft, bLeft);
                    } else {
                        // 偶数时取左半区最大值与右半区最小值的平均数。
                        int leftMax = Math.max(aLeft, bLeft);
                        int rightMin = Math.min(aRight, bRight);
                        return ((double) leftMax + rightMin) / 2;
                    }
                }
            }

            // TODO: 【不合理兜底】原代码写return 0会把非法输入伪装成合法结果。
            // 对于满足题目约束的两个有序数组，循环一定能找到合法切分；到达此处说明前提被破坏。
            // 错误行：return 0;
            throw new IllegalArgumentException("需要保证入参数组有序");
        }
    }

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
