package frequence.双指针.同向读写双指针_原地稳定压缩;

/**
 * LeetCode 26：删除有序数组中的重复项。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743171495。
 *
 * 每种数字只保留一个，并返回去重后的有效长度。
 * 返回后只保证 {@code [0, 返回长度)} 是有效结果。
 *
 * <p><b>有序是关键前提：</b>相同数字在有序数组中必然连续，
 * 所以当前元素只需要和“最后一个已经保留的元素”比较。
 *
 * <p><b>模型：快慢指针 + 原地稳定压缩。</b>
 * {@code fast} 寻找新的数字，{@code slow} 指向下一个新数字的写入位置。
 * 循环不变量是：
 * <pre>{@code
 * [0, slow) 保存已经扫描部分的全部不同数字，并且保持升序。
 * nums[slow - 1] 是最后一个已经保留的数字。
 * }</pre>
 * 是否保留当前元素依赖的是压缩结果{@code nums[slow - 1]}，而不是无效区域中的残留值。
 * {@code [slow, fast)}可能仍保存旧数据，但它已经不属于有效结果，不能参与去重判断。
 *
 * <p>例如 {@code [0, 0, 1, 1, 1, 2]}：
 * <pre>{@code
 * 第一个 0 直接保留
 * 后续 0 与最后保留值相同：跳过
 * 遇到 1，与最后保留值 0 不同：写入 slow 位置
 * 后续 1 跳过；遇到 2 时写入 slow 位置
 * 返回 3，有效区间为 [0, 1, 2]
 * }</pre>
 * 时间复杂度为 O(N)，额外空间复杂度为 O(1)。
 */
public class Q26_RemoveDuplicatesFromSortedArray {

    /**
     * 用户在LeetCode独立完成的AC版本。
     */
    public int myRemoveDuplicates(int[] nums) {
        int write = 1;
        for (int read = 1; read < nums.length; read++) {
            if (nums[read] != nums[write - 1]) {
                nums[write++] = nums[read];
            }
        }
        return write;
    }

    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        int slow = 1;
        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow - 1]) {
                nums[slow++] = nums[fast];
            }
        }
        return slow;
    }
}
