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

    /**
     * 2026-08-29 超级经典错误版，完整保留当时的实现和错误现场。
     *
     * <p><b>核心致命错误：</b>
     * <pre>{@code
     * swap(nums, index++, nums[index]);
     * }</pre>
     * Java 方法实参按照从左到右的顺序求值。执行到这行时，第二个实参
     * {@code index++}会先返回旧值，并立即完成自增；第三个实参
     * {@code nums[index]}随后读取的已经是自增后的index。
     *
     * <p>失败用例{@code [100000, 3, 4000, 2, 15, 1, 99999]}执行到错误现场时：
     * <pre>{@code
     * index == 5
     * nums[5] == 1
     *
     * int i = index++;      // i = 5，随后index变成6
     * int j = nums[index];  // 读取nums[6]，得到99999
     * swap(nums, i, j);     // 实际调用swap(nums, 5, 99999)
     * }</pre>
     * 最终在swap内部访问{@code nums[99999]}，抛出ArrayIndexOutOfBoundsException。
     *
     * <p><b>最小修正：</b>
     * <pre>{@code
     * int targetIndex = nums[index];
     * swap(nums, index, targetIndex);
     * }</pre>
     * 交换后不能执行index++，因为换到当前位置的新值仍未检查，必须让while重新处理当前index。
     * 因此原来的大小分支也没有必要，两边都应该执行同一个“不移动index的交换”。
     *
     * <p>完整复盘、截图和标准原地哈希版本见：
     * {@code frequence/超级经典必看错误/Q41_方法实参求值顺序导致数组越界.md}。
     */
    public static class Solution_superMistake_20260829 {

        /**
         * 标准原地哈希一般是：值v应该放到下标v - 1 。 不过我们映射比较特殊，是把 nums.length放到0的位置上了。
         *
         * 没有出现的最小的正整数。
         * （O(N) + O(1)）
         *
         * 技巧： 关键看 寻找最小正整数 =》 利用数组下标。
         */
        public int firstMissingPositive(int[] nums) {
            int index = 0;
            while (index < nums.length) {
                if (nums[index] < 0 || nums[index] >= nums.length || index == nums[index]) {
                    index++;
                } else {
                    // 这里是难点。如果目标下标上面的值已经等于目标下标，那么当前index直接跳过。
                    // 不然，如果目标下标是后面的，就会死循环停留在原地。
                    if (nums[nums[index]] == nums[index]) {
                        index++;
                    } else {
                        if (nums[index] > index) {
                            swap(nums, index, nums[index]);
                        } else {
                            // TODO: 【2026-08-29 超级致命错误】方法实参从左到右求值。
                            // index++先完成自增，后面的nums[index]使用的是新index，不是旧index。
                            // 失败现场实际等价于swap(nums, 5, nums[6])，即swap(nums, 5, 99999)。
                            // 错误行：swap(nums, index++, nums[index]);
                            // 正确语句：int targetIndex = nums[index]; swap(nums, index, targetIndex);
                            // 特别注意：交换后不能index++，换到当前位置的新值还需要重新检查。
                            swap(nums, index++, nums[index]);
                        }
                    }
                }
            }

            // TODO: 【错误1】返回这里是错的。如果全部都贴合，那么就返回不存在数组中的下一个正整数，
            // 即nums.length。不存在无效值的返回结果。
            // 错误行：return 0;

            // TODO: 【错误2 - 超级错误！！！】题目要的是最小正整数。但是有可能[3,1,2]这种，
            // 最小正整数可不是3！！！
            // 所以，当确认missingValue后（这里特指：数组下标都满足后缺失nums.length），
            // 要拿着缺失值去nums[0]确认一下。
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] != i) {
                    return i;
                }
            }
            int missing = nums.length;
            return nums[0] == missing ? missing + 1 : missing;
        }

        private void swap(int[] nums, int i, int j) {
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }
    }

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
