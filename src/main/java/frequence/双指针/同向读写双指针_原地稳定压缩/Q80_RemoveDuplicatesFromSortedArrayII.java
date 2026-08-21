package frequence.双指针.同向读写双指针_原地稳定压缩;

/**
 * LeetCode 80：删除有序数组中的重复项 II。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743171986。
 *
 * <p>原地修改有序数组，使每个元素最多出现两次，返回有效结果长度。只保证返回后
 * {@code [0, 返回长度)} 是有效结果。
 *
 * <p><b>模型：保留条件依赖压缩结果。</b>{@code fast} 读取原数组，{@code slow}
 * 指向下一个写入位置，{@code [0, slow)} 始终是已经满足“每个值最多出现两次”的结果。
 *
 * <p>保留当前元素的统一条件是：
 * <pre>{@code
 * slow < 2 || nums[fast] != nums[slow - 2]
 * }</pre>
 * 当结果不足两个元素时直接保留；否则，如果当前值等于结果中倒数第二个值，由于数组有序，
 * 结果末尾两个值和当前值必然相同，继续写入就会出现第三次。
 *
 * <p>该结论可以推广为“每个值最多保留 {@code k} 次”：
 * <pre>{@code
 * slow < k || nums[fast] != nums[slow - k]
 * }</pre>
 * 这里比较的是压缩结果 {@code nums[slow - k]}，不是原始输入中的相邻位置。
 *
 * <p>时间复杂度为 O(N)，额外空间复杂度为 O(1)。
 */
public class Q80_RemoveDuplicatesFromSortedArrayII {

    /**
     * 用户在LeetCode独立完成的AC版本。
     */
    public int myRemoveDuplicates(int[] nums) {
        int write = 0;
        for (int read = 0; read < nums.length; read++) {
            if (write < 2 || nums[read] != nums[write - 2]) {
                nums[write++] = nums[read];
            }
        }
        return write;
    }

    public int removeDuplicates(int[] nums) {
        return removeDuplicatesAtMostK(nums, 2);
    }

    /**
     * 有序数组中，每个值最多保留 {@code k} 次的通用模板。
     */
    public int removeDuplicatesAtMostK(int[] nums, int k) {
        if (k <= 0) {
            return 0;
        }

        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (slow < k || nums[fast] != nums[slow - k]) {
                nums[slow++] = nums[fast];
            }
        }
        return slow;
    }
}
