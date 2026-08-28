package frequence.array;


/**
 * TODO: 两个超重要错误：
 *  1. while双指针 -> 双指针L,R在循环体结尾一定要移动！！！
 *  2. 向右/向左轮转次数k 可以大于 nums.length。 此时有效的轮转次数为 k % nums.length   (因为 k=nums.length时，轮转后数组 等于 原数组)
 */


/**
 * 给定一个整数数组 nums，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。
 *
 * <p><b>最优解：三次反转。</b>
 * 时间复杂度为 O(N)，额外空间复杂度为 O(1)。
 *
 * <p><b>一、左旋和右旋</b>
 * <pre>{@code
 * nums = [1, 2, 3, 4, 5, 6, 7]
 *
 * 右旋3位：把后3个元素移动到开头
 * [5, 6, 7, 1, 2, 3, 4]
 *
 * 左旋3位：把前3个元素移动到末尾
 * [4, 5, 6, 7, 1, 2, 3]
 * }</pre>
 *
 * <p>长度为 N 时：
 * <pre>{@code
 * 左旋k位 = 右旋(N - k)位
 * 右旋k位 = 左旋(N - k)位
 * }</pre>
 * 如果 k 可能大于 N，需要先执行 {@code k %= N}。完整等价关系为：
 * <pre>{@code
 * 左旋k位 = 右旋 (N - k % N) % N 位
 * }</pre>
 * 最后的 {@code % N} 用于处理 {@code k % N == 0} 的情况。
 *
 * <p><b>二、三次反转的分块推导</b>
 *
 * <p>右旋 k 位时，将数组拆成两个连续块：
 * <pre>{@code
 * A = 前N-k个元素
 * B = 后k个元素
 *
 * 原数组：A B
 * 目标：  B A
 * }</pre>
 *
 * 利用反转恒等式：
 * <pre>{@code
 * reverse(A B) = reverse(B) reverse(A)
 * }</pre>
 *
 * 三次反转过程：
 * <pre>{@code
 * A B
 *   --整体反转--> reverse(B) reverse(A)
 *   --反转前k个--> B reverse(A)
 *   --反转剩余部分--> B A
 * }</pre>
 *
 * 例如：
 * <pre>{@code
 * 原始数组：    [1, 2, 3, 4, 5, 6, 7]
 * 整体反转：    [7, 6, 5, 4, 3, 2, 1]
 * 反转前3个：   [5, 6, 7, 4, 3, 2, 1]
 * 反转后4个：   [5, 6, 7, 1, 2, 3, 4]
 * }</pre>
 *
 * <p><b>三、右旋代码骨架</b>
 * <pre>{@code
 * public void rotate(int[] nums, int k) {
 *     int n = nums.length;
 *     k %= n;
 *
 *     reverse(nums, 0, n - 1);
 *     reverse(nums, 0, k - 1);
 *     reverse(nums, k, n - 1);
 * }
 *
 * private void reverse(int[] nums, int left, int right) {
 *     while (left < right) {
 *         swap(nums, left++, right--);
 *     }
 * }
 * }</pre>
 *
 * <p><b>四、复杂度</b>
 * <pre>{@code
 * 整体反转：O(N)
 * 前k个反转：O(K)
 * 后N-k个反转：O(N-K)
 *
 * 总时间：O(N + K + N-K) = O(N)
 * 额外空间：O(1)
 * }</pre>
 *
 * <p><b>五、模型定位</b>
 *
 * <p>三次反转不是滑动窗口那样覆盖大量题目的通用框架，而是适用于
 * “循环移位、原地交换两个连续块”的稳定模板。不要机械记忆三次操作，
 * 应该记住：
 * <pre>{@code
 * 将数组拆成 A B，目标是 B A；
 * 整体反转改变块顺序，块内反转恢复每个块的内部顺序。
 * }</pre>
 *
 * reverse 内部使用相向双指针，但本题的核心模型是“序列分块 + 反转恒等式”，
 * 双指针只是实现反转的工具。
 */
public class Q189_RotateArray {

    /**
     * 技巧:关于原地O(1) 实现数组轮转的核心关键： 就是 【反转部分数组】！！！
     *     1. 【非轮转部分】 和 【轮转部分】 单独反转
     *     2. 总体反转！！！
     *
     * TODO：【错误】题目存在k > nums.length的情况，必须要 取余！！！
     * - 1 <= nums.length <= 10^5
     * - 0 <= k <= 10^5  => 没有说 nums.length 和 k的大小问题！！！
     */
    public static class Solution20260829 {

        public void rotate(int[] nums, int k) {
            // TODO： 【错误】题目存在k > nums.length的情况，必须要 取余！！！
            k = k % nums.length;

            // TODO: ai建议下面这个加上
            if (k == 0) {
                return;
            }
            reverse(nums, 0, nums.length - k - 1);
            reverse(nums, nums.length - k, nums.length - 1);
            reverse(nums, 0, nums.length - 1);
        }

        private void reverse(int[] nums, int s, int e) {
            while (s <= e) {
                swap(nums, s, e);
                s++;
                e--;
            }
        }

        private void swap(int[] nums, int i, int j) {
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }
    }

    public void rotate(int[] nums, int k) {
        // TODO: 【超级错误-超级重要】右旋次数k 可以大于 nums.length，即代表多次 向右轮转。
        //  你会发现 如果nums.length=3,向右轮转3次，那么数组原封不动。 所以有效的k = k%nums.length；
        k = k % nums.length;

        int len = nums.length;
        reverse(nums, 0, len - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, len - 1);
    }


    public void reverse(int[] arr, int l, int r) {
        int L = l;
        int R = r;
        while (L <= R) {
            int tmp = arr[L];
            arr[L] = arr[R];
            arr[R] = tmp;
            // TODO: 【错误】while双指针，不要忘了 移动指针啊！！！
            L++;
            R--;
        }
    }

}
