package frequence.双指针.同向读写双指针_原地稳定压缩;

/**
 * LeetCode 27：移除元素。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743171033。
 *
 * 原地移除数组中所有等于 val 的元素，并返回剩余元素数量。
 * 返回后只保证 {@code [0, 返回长度)} 是有效结果，后面的内容不需要处理。
 *
 * <p><b>模型：快慢指针 + 原地稳定压缩。</b>
 * {@code fast} 检查每个元素，{@code slow} 指向下一个保留元素的写入位置。
 * 循环不变量是：
 * <pre>{@code
 * [0, slow)      已保留元素
 * [slow, fast)   已检查但不属于有效结果的区域
 * [fast, n)      尚未检查区域
 * }</pre>
 * 更严格地说，{@code [0, slow)}保存{@code nums[0...fast)}中所有不等于{@code val}
 * 的元素，并且相对顺序不变。
 *
 * <p>每个输入元素只会产生0或1个输出：等于{@code val}时不输出，否则写入
 * {@code nums[slow++]}。题目只关心返回长度之前的前缀，所以不需要交换，也不需要清理尾部。
 *
 * <p>例如 {@code nums = [3, 2, 2, 3], val = 3}：
 * <pre>{@code
 * fast 遇到 3：丢弃
 * fast 遇到 2：写入 nums[0]
 * fast 遇到 2：写入 nums[1]
 * fast 遇到 3：丢弃
 * 返回 2，有效区间 nums[0...2) = [2, 2]
 * }</pre>
 * 时间复杂度为 O(N)，额外空间复杂度为 O(1)。
 */
public class Q27_RemoveElement {

    /**
     * 用户在LeetCode独立完成的AC版本。
     */
    public int myRemoveElement(int[] nums, int val) {
        int write = 0;
        for (int read = 0; read < nums.length; read++) {
            if (nums[read] != val) {
                nums[write++] = nums[read];
            }
        }
        return write;
    }

    public int removeElement(int[] nums, int val) {
        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != val) {
                nums[slow++] = nums[fast];
            }
        }
        return slow;
    }
}
