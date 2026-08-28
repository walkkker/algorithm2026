package frequence.超级经典必看错误;

/**
 * LeetCode 41：缺失的第一个正数。
 *
 * <p>2026-08-29 超级经典错误：在方法实参中修改index，导致后续实参使用了新值。
 *
 * <pre>{@code
 * swap(nums, index++, nums[index]);
 * }</pre>
 *
 * <p>Java从左到右计算实参。{@code index++}作为表达式的值是旧index，
 * 但在计算后续{@code nums[index]}之前，index本身已经完成加一。
 *
 * <p>失败用例：{@code [100000, 3, 4000, 2, 15, 1, 99999]}。
 * 错误现场实际调用为{@code swap(nums, 5, 99999)}。
 *
 * <p>错误截图：
 * <br><img src="doc-files/Q41_ArrayIndexOutOfBounds_20260829.png" alt="Q41 ArrayIndexOutOfBoundsException">
 *
 * <p>详细数据流、最小修正和标准模板见同目录：
 * {@code Q41_方法实参求值顺序导致数组越界.md}。
 */
public class Q41_FirstMissingPositive_SuperMistake_20260829 {

    /**
     * 当时的完整错误代码。该代码用于复盘，不能作为正确实现使用。
     */
    public static class Solution_superMistake_20260829 {

        /**
         * 标准原地哈希一般是：值v应该放到下标v - 1。
         * 不过这个错误版的映射比较特殊，是把nums.length放到0的位置上。
         *
         * 没有出现的最小的正整数。
         * （O(N) + O(1)）
         *
         * 技巧：关键看 寻找最小正整数 =》 利用数组下标。
         */
        public int firstMissingPositive(int[] nums) {
            int index = 0;
            while (index < nums.length) {
                if (nums[index] < 0 || nums[index] >= nums.length || index == nums[index]) {
                    index++;
                } else {
                    // 如果目标下标上的值已经等于目标下标，当前index直接跳过。
                    // 否则重复值会使交换永远停留在原地。
                    if (nums[nums[index]] == nums[index]) {
                        index++;
                    } else {
                        if (nums[index] > index) {
                            swap(nums, index, nums[index]);
                        } else {
                            // TODO: 【核心错误】Java从左到右计算方法实参。
                            // 1. index++向第二个实参提供旧值，但index立即加一。
                            // 2. nums[index]作为第三个实参，读取的是加一后的index。
                            // 3. 失败现场实际变成swap(nums, 5, 99999)。
                            // 错误行：
                            swap(nums, index++, nums[index]);
                            // 正确语句：int targetIndex = nums[index]; swap(nums, index, targetIndex);
                            // 交换后不能index++，当前位置换回来的新值还需要检查。
                        }
                    }
                }
            }

            // TODO: 【错误1】如果全部都贴合，返回的是数组中不存在的下一个正整数，不能return 0。
            // 错误行：return 0;

            // TODO: 【错误2 - 超级错误！！！】题目要的是最小正整数。
            // [3,1,2]中最小缺失正整数不是3，需要拿缺失值再去nums[0]确认。
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

    /**
     * 保留原特殊映射的最小修正版。核心是交换前缓存目标下标，
     * 并且交换后不移动index。
     */
    public static class Solution_minimalFix_20260829 {

        public int firstMissingPositive(int[] nums) {
            int index = 0;
            while (index < nums.length) {
                if (nums[index] < 0 || nums[index] >= nums.length || index == nums[index]) {
                    index++;
                } else if (nums[nums[index]] == nums[index]) {
                    index++;
                } else {
                    int targetIndex = nums[index];
                    swap(nums, index, targetIndex);
                }
            }

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

    /**
     * 面试推荐的标准原地哈希：值v归位到下标v - 1。
     */
    public static class Solution_standardIndexHash_20260829 {

        public int firstMissingPositive(int[] nums) {
            int index = 0;
            while (index < nums.length) {
                int value = nums[index];
                if (value >= 1
                        && value <= nums.length
                        && nums[value - 1] != value) {
                    swap(nums, index, value - 1);
                } else {
                    index++;
                }
            }

            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != i + 1) {
                    return i + 1;
                }
            }
            return nums.length + 1;
        }

        private void swap(int[] nums, int i, int j) {
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }
    }
}
