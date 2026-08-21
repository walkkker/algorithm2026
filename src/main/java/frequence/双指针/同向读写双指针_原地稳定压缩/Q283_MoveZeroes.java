package frequence.双指针.同向读写双指针_原地稳定压缩;

/**
 * TODO： 同向快慢指针  ->   fast 找保留元素，slow 指向写入位置
 *
 * LeetCode 283：移动零。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743172291。用户独立完成的是
 * {@link #myMoveZeroes(int[])}两阶段覆盖版本；{@link Solution1}保留为{@code O(N^2)}对照版本。
 *
 * 将所有 0 移动到数组末尾，同时保持非零元素的相对顺序，要求原地完成。
 *
 * <p><b>模型：快慢指针 + 原地稳定压缩。</b>
 * {@code fast} 从左到右扫描元素，{@code slow} 指向下一个非零元素应该放置的位置。
 * 循环过程中始终维持以下不变量：
 * <pre>{@code
 * [0, slow)    已整理完成的非零元素，并且相对顺序正确
 * [slow, fast) 全部是零
 * [fast, n)    尚未检查
 * }</pre>
 * 因此当{@code slow < fast}且{@code nums[fast] != 0}时，可以确定
 * {@code nums[slow] == 0}。交换会把当前非零值放入有效区，同时把零带到后面，
 * {@code slow++}后不变量继续成立，所以遍历结束时不需要再次补零。
 *
 * <p>例如 {@code [0, 0, 2, 2]}：
 * <pre>{@code
 * fast = 2：交换下标 0 和 2，得到 [2, 0, 0, 2]，slow = 1
 * fast = 3：交换下标 1 和 3，得到 [2, 2, 0, 0]，slow = 2
 * }</pre>
 * fast 按照原顺序发现非零元素，slow 也按照原顺序接收，因此算法具有稳定性。
 * 时间复杂度为 O(N)，额外空间复杂度为 O(1)。
 *
 * <p>也可以用“两阶段覆盖法”：先把非零元素覆盖到数组前部，再把剩余位置统一写0。
 * 两种写法复杂度相同。交换法一趟扫描即可完成结果；覆盖法更接近通用过滤模板。
 * 若把交换简化为{@code nums[slow] = nums[fast]; nums[fast] = 0}，必须保留
 * {@code slow != fast}判断，否则两个指针重合时会把当前非零元素清零。
 *
 * <p>{@link Solution1} 是用于对比的 O(N^2) 解法。它虽然使用了两个指针，
 * 但每发现一个 0 都要移动后续元素。使用两个指针变量不代表算法一定是 O(N)，
 * 还要分析所有指针和内部循环的总移动次数。
 */
public class Q283_MoveZeroes {

    /**
     * 用户在LeetCode独立完成的AC版本：先稳定压缩非零元素，再将剩余位置补零。
     */
    public void myMoveZeroes(int[] nums) {
        int write = 0;
        for (int read = 0; read < nums.length; read++) {
            if (nums[read] != 0) {
                nums[write++] = nums[read];
            }
        }
        while (write < nums.length) {
            nums[write++] = 0;
        }
    }

    /**
     * 最优解：将扫描到的非零元素依次交换到 slow 位置。
     *
     * @param nums 待原地修改的数组
     */
    public void moveZeroes(int[] nums) {
        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                if (slow != fast) {
                    swap(nums, slow, fast);
                }
                slow++;
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 类似冒泡的对照解法。结果正确且保持稳定性，但每发现一个 0 都要移动后续元素，
     * 例如全零数组的交换次数为 {@code (N - 1) + (N - 2) + ... + 1}，
     * 因此最坏时间复杂度为 O(N^2)。
     */
    class Solution1 {
        public void moveZeroes(int[] nums) {
            int left = 0;
            int right = nums.length - 1;
            while (left <= right) {
                if (nums[left] == 0) {
                    // 移动后 nums[left] 仍可能是 0，因此这里不能执行 left++。
                    for (int i = left; i < right; i++) {
                        swap(nums, i, i + 1);
                    }
                    // 当前 0 已经移动到 right，待处理区间相应缩小。
                    right--;
                } else {
                    left++;
                }
            }
        }
    }
}
