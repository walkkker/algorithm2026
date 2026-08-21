package frequence.permutation;

/**
 * 31. 下一个排列
 *
 * <p>DONE: 【已独立完成】当前实现已通过排列家族对数器。状态见
 * {@code frequence/待独立完成题目清单.md}。
 *
 * <p>给定一个整数数组，将它原地修改成字典序中紧接着的下一个排列。
 * 如果当前已经是最大排列，则将它重排为最小排列。只允许使用常数额外空间。
 *
 * <p><b>字典序排列家族定位：</b>Q31是排列空间的“直接后继操作”。
 * 从最小排列开始反复调用本方法，就能按字典序枚举全部排列；Q60则不经过
 * 中间排列，直接定位第k个排列。详见同目录《字典序排列家族.md》。
 *
 * <p><b>核心结构：</b>从右向左找第一个{@code nums[pivot] < nums[pivot + 1]}。
 * {@code pivot}右侧必然是非递增序列，表示这个后缀已经处于自己的最大排列。
 * 想让整体变大，必须修改{@code pivot}；想让增量最小，必须使用右侧中大于
 * {@code nums[pivot]}的最小元素替换它，然后把新后缀变成最小升序排列。
 *
 * <p><b>三步模板：</b>
 * <ol>
 *     <li>从右向左找最右上升点{@code pivot}。</li>
 *     <li>从右向左找第一个大于{@code nums[pivot]}的元素并交换。</li>
 *     <li>反转{@code [pivot + 1, n - 1]}，使后缀从最大降序变成最小升序。</li>
 * </ol>
 *
 * <p>例如{@code [1,3,5,4,2]}：最右上升点是3，右侧最小的更大元素是4，
 * 交换得到{@code [1,4,5,3,2]}，再反转后缀得到{@code [1,4,2,3,5]}。
 *
 * <p>如果找不到{@code pivot}，整个数组是非递增的最大排列，反转全部即回到最小排列。
 * 该算法同样适用于含重复值的数组。
 *
 * <p>时间复杂度{@code O(N)}，额外空间复杂度{@code O(1)}。
 */
public class Q31_NextPermutation {

    /**
     寻找最右升序（必须是右侧第一个升序的，别的方法都不行） + 交换 (跟右侧刚刚大于一点的那个数交换) + 后缀反转完成升序（此时，右侧依然全部是降序，反转即升序）

     可以画一张图，去理解上述逻辑。 第一个升序，右侧都是降序。 => 此时你就知道，为什么 必须按上述步骤做，才能得到下一个排列。
     */
    class MySolution {
        public void nextPermutation(int[] nums) {
            // 从右侧遍历，找第一个 右大 左小的pair，然后swap
            // 如果没有，则说明当前最大
            // TODO：【错误】需要找右侧第一个升序的。 而不是下面这个寻找第一个左大 右小。这不对 -> 因为中间跳过的有可能组成 next permutation。

            // for (int i = nums.length - 1; i >= 0; i--) {
            //     for (int j = i - 1; j >= 0; j--) {
            //         if (nums[i] > nums[j]) {
            for (int i = nums.length - 2; i >= 0; i--) {
                if (nums[i] < nums[i + 1]) { // TODO： 【错误】必须是第一个升序的！！！ 升序说明，有next permutation。   接下来要寻找，谁接替i这个位置
                    for (int j = nums.length - 1; j >= i + 1; j--) {
                        if (nums[j] > nums[i]) {
                            swap(nums, i, j);
                            int l = i + 1;
                            int r = nums.length - 1;
                            while (l <= r) {
                                swap(nums, l++, r--);
                            }
                            return;
                        }
                    }
                }
            }

            // 此时一路降序。 直接反转整个数组
            int l = 0;
            int r = nums.length - 1;
            while (l <= r) {
                swap(nums, l++, r--);
            }
        }

        private void swap(int[] arr, int i, int j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }


    public static class Solution {

        public void nextPermutation(int[] nums) {
            int pivot = nums.length - 2;
            while (pivot >= 0 && nums[pivot] >= nums[pivot + 1]) {
                pivot--;
            }

            if (pivot >= 0) {
                // 右侧是非递增序列，从右遇到的第一个更大元素就是最小后继。
                int successor = nums.length - 1;
                while (nums[successor] <= nums[pivot]) {
                    successor--;
                }
                swap(nums, pivot, successor);
            }

            // pivot == -1时反转整个数组；否则只将后缀恢复为最小升序。
            reverse(nums, pivot + 1, nums.length - 1);
        }

        private void reverse(int[] nums, int left, int right) {
            while (left < right) {
                swap(nums, left++, right--);
            }
        }

        private void swap(int[] nums, int first, int second) {
            int temp = nums[first];
            nums[first] = nums[second];
            nums[second] = temp;
        }
    }
}
