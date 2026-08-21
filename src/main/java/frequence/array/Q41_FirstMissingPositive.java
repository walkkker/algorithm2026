package frequence.array;

/**
 * LeetCode 41：缺失的第一个正数。
 *
 * <p><b>核心模型：有限值域 + 值与下标一一映射 = 原地哈希（下标归位）。</b>
 *
 * <p>数组长度为 N，答案一定在 {@code [1, N + 1]}：
 * <pre>{@code
 * 如果[1,N]中存在缺失值，答案就在[1,N]；
 * 如果1到N全部存在，答案就是N+1。
 * }</pre>
 * 因此，{@code <= 0} 或 {@code > N} 的数字都不会影响答案，可以直接跳过。
 *
 * <p>将输入数组本身当作哈希表，为每个有效数字安排唯一目标下标：
 * <pre>{@code
 * 数字1 -> 下标0
 * 数字2 -> 下标1
 * ...
 * 数字x -> 下标x-1
 * ...
 * 数字N -> 下标N-1
 * }</pre>
 * 整理完成后，理想状态是 {@code nums[i] == i + 1}。
 *
 * <p><b>归位过程的关键：</b>
 * <pre>{@code
 * 1. nums[i]不在[1,N]：不是候选答案，i++。
 * 2. 目标位置已经是nums[i]：当前值已归位，或者遇到重复值，i++。
 * 3. 否则将nums[i]交换到下标nums[i]-1；交换后不能i++，
 *    因为换到当前位置的新数字仍然没有处理。
 * }</pre>
 *
 * <p>重复值判断不能省略。例如 {@code [1,1]} 中，第二个 1 的目标位置已经是 1；
 * 如果仍然交换，两个相同数字会被无限交换，形成死循环。
 *
 * <p>例如：
 * <pre>{@code
 * 输入：[3, 4, -1, 1]
 * 归位：[1, -1, 3, 4]
 *
 * 下标1应该存放数字2，但实际不是2，所以答案为2。
 * }</pre>
 *
 * <p>最后从左到右寻找第一个不满足 {@code nums[i] == i + 1} 的位置，
 * 返回 {@code i + 1}；如果全部满足，返回 {@code N + 1}。
 *
 * <p>虽然归位阶段包含 while 和交换，但每次有效交换都会把至少一个数字放到
 * 它的最终目标位置，成功归位次数最多为 N；指针 i 也最多右移 N 次。
 * 因此时间复杂度为 O(N)，额外空间复杂度为 O(1)。
 *
 * <p>该算法会修改输入数组。适用条件是：
 * <pre>{@code
 * 1. 关键值域与[0,N]或[1,N]相关；
 * 2. 每个有效值都能映射到唯一数组下标；
 * 3. 允许修改原数组；
 * 4. 要求O(N)时间和O(1)额外空间。
 * }</pre>
 *
 * <p>原地哈希的通用模型、正负号标记模板和相关题型见：
 * {@code frequence/array/原地哈希.md}。
 */
public class Q41_FirstMissingPositive {

    public int firstMissingPositive(int[] nums) {
        int i = 0;
        while (i < nums.length) {
            if (nums[i] >= 1 && nums[i] <= nums.length) {
                // 目标位置已经是相同数字：可能已经归位，也可能是重复值，不能继续交换。
                if (nums[nums[i] - 1] == nums[i]) {
                    i++;
                } else {
                    // 将有效数字x放到下标x-1。交换后当前位置得到新数字，暂时不能i++。
                    swap(nums, nums[i] - 1, i);
                }
            } else {
                // 非正数和大于N的数字不可能成为[1,N]中的缺失答案。
                i++;
            }
        }

        for (i = 0; i < nums.length; i++) {
            // 第一个未保存正确数字的位置，其对应正数i+1就是最小缺失正数。
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return nums.length + 1;
    }


    public void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
