package frequence.Heap;

/**
 * 215. 数组中的第K个最大元素
 *
 * TODO: 【题型识别】无序数组第k大／第k小，想到Quickselect：荷兰国旗分区 + 类二分区间淘汰。
 * 第k小对应升序下标k-1，第k大对应nums.length-k，重复元素参与排名。
 * TODO: 【原理解修正】原记忆“每次舍弃一半，T(N)=T(N/2)+O(N)”只是理想平衡情况。
 * 随机pivot不保证折半；随机Quickselect期望O(N)，最坏O(N^2)。迭代原地版本辅助空间O(1)。
 *
 * <p><b>重要复习入口：</b>同目录{@code Quickselect快速选择_第K大与第K小.md}，详细对照
 * 荷兰国旗、QuickSort、Quickselect以及大小为k的堆。完整排序处理两侧，快速选择只处理目标所在侧。
 *
 * <p>给定整数数组{@code nums}和整数{@code k}，返回数组中第{@code k}个最大的元素。
 *
 * <p>需要查找的是数组排序后的第{@code k}个最大元素，而不是第{@code k}个不同的元素。
 */
public class Q215_KthLargestElementInAnArray {

    /**
     * 2026-09-19复盘版本：荷兰国旗分区 + 类二分的区间淘汰，也就是Quickselect。
     *
     * <p><b>核心认识：</b>荷兰国旗分区把当前区间划分为“小于pivot、等于pivot、大于pivot”
     * 三段，并返回等于区间。pivot最终排序位置因此被确定：目标下标在等于区间左侧就只保留
     * 左区间，在右侧就只保留右区间，落在等于区间就直接返回。
     *
     * <p>TODO: 【重要修正】Quickselect像二分，但随机pivot不保证每次恰好去掉一半。随机化的
     * 作用是让剩余问题规模在期望意义上按固定比例缩小。每轮分区成本依次近似为
     * {@code N + aN + a^2N + ...}，其中期望{@code 0 < a < 1}，因此总和为{@code O(N)}。
     * 最坏情况下pivot连续落在端点，规模只减少1，复杂度仍可能退化为{@code O(N^2)}。
     *
     * <p><b>与快速排序对比：</b>快速排序分区后还要处理左右两侧，同一递归层的总工作量为
     * {@code O(N)}，期望高度为{@code O(log N)}，所以期望为{@code O(N log N)}；Quickselect
     * 每轮只进入目标所在的一侧，所以随机化后的期望总扫描量是几何级数{@code O(N)}。
     *
     * <p>TODO: 【错误1-闭区间右边界】当前搜索区间是{@code [l,r]}，所以初始右边界必须是
     * {@code nums.length - 1}，不能写成{@code nums.length}。
     *
     * <p>TODO: 【错误2-第K大到升序下标的转换】partition确定的是元素在升序数组中的下标。
     * 第k大对应的0-based升序下标是{@code nums.length - k}，不能直接拿题目的k与分区边界比较。
     */
    class SolutionReviewed20260919 {
        public int findKthLargest(int[] nums, int k) {
            int l = 0;
            // TODO: 【错误点1】闭区间[l,r]的右边界必须是len-1。
            // 错误行：int r = nums.length;
            int r = nums.length - 1;

            // TODO: 【错误点2】第k大必须转换为升序数组中的0-based目标下标。
            int targetIndex = nums.length - k;

            while (l <= r) {
                // 随机选择pivot并换到r，让下面的partition以arr[r]作为pivot。
                int random = l + (int) (Math.random() * (r - l + 1));
                swap(nums, random, r);

                int[] equalRange = partition(nums, l, r);
                if (targetIndex < equalRange[0]) {
                    r = equalRange[0] - 1;
                } else if (targetIndex > equalRange[1]) {
                    l = equalRange[1] + 1;
                } else {
                    // targetIndex落在等于pivot的区域，区域内任意元素都等于答案。
                    return nums[equalRange[0]];
                }
            }

            // 题目保证1 <= k <= nums.length，因此正常不会到达这里。
            throw new IllegalArgumentException("k超出数组范围");
        }

        /**
         * 荷兰国旗版本一：less和more表示区域边界位置。
         * <pre>
         * [l, less)   &lt; pivot
         * [less, i)   == pivot
         * [i, more]   待处理
         * (more, r]   &gt; pivot
         * </pre>
         * 循环结束后等于区间为{@code [less, more]}，不需要额外交换pivot。
         */
        private int[] partitionMyVersion(int[] arr, int l, int r) {
            int pivot = arr[r];
            int less = l;
            int more = r;
            int i = l;

            while (i <= more) {
                if (arr[i] == pivot) {
                    i++;
                } else if (arr[i] < pivot) {
                    swap(arr, i++, less++);
                } else {
                    swap(arr, i, more--);
                }
            }
            return new int[]{less, more};
        }

        /**
         * 荷兰国旗版本二：pivot预留在r位置，less和more分别是小于区闭区间右边界、
         * 大于区闭区间左边界。
         * <pre>
         * [l, less]   &lt; pivot
         * (less, i)   == pivot
         * [i, more)   待处理
         * [more, r)   &gt; pivot
         * r           pivot
         * </pre>
         */
        private int[] partition(int[] arr, int l, int r) {
            int pivot = arr[r];
            int less = l - 1;
            int more = r;
            int i = l;

            while (i < more) {
                if (arr[i] == pivot) {
                    i++;
                } else if (arr[i] < pivot) {
                    swap(arr, i++, ++less);
                } else {
                    swap(arr, i, --more);
                }
            }

            // 预留在r的pivot放入等于区，最终等于区是(less, more]。
            swap(arr, more, r);
            return new int[]{less + 1, more};
        }

        private void swap(int[] arr, int i, int j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

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
