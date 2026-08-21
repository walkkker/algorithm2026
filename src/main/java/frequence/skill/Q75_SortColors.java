package frequence.skill;

/**
 * 75. 颜色分类
 *
 * <p>给定一个只包含{@code 0、1、2}的整数数组，要求原地把相同数字排列在一起，并按照
 * {@code 0、1、2}的顺序排序。不能调用库内置排序函数。
 *
 * <p><b>荷兰国旗模型：</b>使用1作为划分值，维护三个区域：
 * <pre>
 * [left, less]       小于1，即全部为0
 * [less + 1, index)  等于1
 * [index, more)      尚未检查
 * [more, right]      大于1，即全部为2
 * </pre>
 * 遇到0就与左边界的下一个位置交换，遇到1直接前进，遇到2就与右边界的前一个位置交换。
 * 与右侧交换后{@code index}不能前进，因为换回来的元素还没有被检查。
 *
 * <p>它是读写双指针的“多分区边界”扩展，不再只有有效区和未处理区，而是维护：
 * <pre>{@code
 * [0, left)          全是0
 * [left, current)    全是1
 * [current, right]   尚未处理
 * (right, n)         全是2
 * }</pre>
 * 从左侧交换过来的元素位于已经扫描过的区域，所以{@code current}可以前进；从右侧换回来的元素
 * 来源于未处理区，必须停在当前位置继续检查。这是本题最关键的不对称操作。
 *
 * <p>时间复杂度{@code O(N)}，额外空间复杂度{@code O(1)}。
 */
public class Q75_SortColors {

    public static class Solution {

        public void sortColors(int[] nums) {
            // TODO: 【Java语法误解】调用有返回值的方法时，可以直接忽略返回值，不需要变量接住。
            // 原代码：int[] p = partition(nums, 0, nums.length - 1, 1);
            partition(nums, 0, nums.length - 1, 1);
        }

        /**
         * 荷兰国旗通用模板，返回等于区域的左右边界。
         */
        private int[] partition(int[] nums, int left, int right, int pivot) {
            int less = left - 1;
            int more = right + 1;

            // TODO: 【通用模板边界】原实现从0开始并固定循环nums.length次，只适用于整个数组。
            // 错误/受限行：int index = 0;
            // 错误/受限行：for (int i = 0; i < nums.length; i++) {
            // 修正为从left开始，并以“未检查区域为空”作为循环结束条件，才能处理任意[left, right]。
            int index = left;
            while (index < more) {
                if (nums[index] < pivot) {
                    swap(nums, index++, ++less);
                } else if (nums[index] > pivot) {
                    swap(nums, index, --more);
                } else {
                    index++;
                }
            }
            return new int[]{less + 1, more - 1};
        }

        private void swap(int[] nums, int first, int second) {
            int temp = nums[first];
            nums[first] = nums[second];
            nums[second] = temp;
        }
    }
}
