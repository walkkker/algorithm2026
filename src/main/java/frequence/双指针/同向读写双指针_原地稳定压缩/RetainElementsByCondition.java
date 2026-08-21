package frequence.双指针.同向读写双指针_原地稳定压缩;

import java.util.Objects;
import java.util.function.IntPredicate;

/**
 * 按条件原地保留数组元素的通用模板。
 * 将满足 {@link IntPredicate} 的元素稳定压缩到数组前部，并返回有效长度。
 *
 * <p>例如只保留偶数：
 * <pre>{@code
 * int[] nums = {5, 2, 7, 4, 6, 9};
 * int length = RetainElementsByCondition.retain(nums, value -> (value & 1) == 0);
 *
 * length = 3
 * nums[0...length) = [2, 4, 6]
 * }</pre>
 *
 * <p><b>通用不变量：</b>
 * <pre>{@code
 * [0, slow) 保存 nums[0...fast) 中所有满足保留条件的元素，并且相对顺序不变。
 * }</pre>
 * Q27_RemoveElement 的“元素不等于 val”和 Q283_MoveZeroes 的“元素不等于 0”，
 * 本质上都是把某个条件
 * 传入这个模板。时间复杂度为 O(N)，额外空间复杂度为 O(1)。
 */
public class RetainElementsByCondition {

    private RetainElementsByCondition() {
    }

    /**
     * 将满足条件的元素稳定保留在数组前部。
     *
     * @param nums          待原地修改的数组
     * @param keepCondition 返回 true 表示保留当前元素
     * @return 数组前部有效结果的长度
     */
    public static int retain(int[] nums, IntPredicate keepCondition) {
        Objects.requireNonNull(nums, "nums");
        Objects.requireNonNull(keepCondition, "keepCondition");

        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (keepCondition.test(nums[fast])) {
                nums[slow++] = nums[fast];
            }
        }
        return slow;
    }
}
