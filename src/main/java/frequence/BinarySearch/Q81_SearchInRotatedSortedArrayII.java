package frequence.BinarySearch;

/**
 * 81. 搜索旋转排序数组 II
 *
 * <p>给定一个按非递减顺序排列、经过某个未知下标旋转并且可能包含重复元素的整数数组
 * {@code nums}，判断目标值{@code target}是否存在于数组中。
 *
 * <p><b>二分排除原则：</b>二分查找必须先证明某一部分不可能包含答案，才能整体舍弃该区间。
 * Q33中至少有一侧可以明确判断为有序段，再根据{@code target}是否属于该有序值域排除一半。
 * 本题的困难正是重复值可能破坏这个判断依据。
 *
 * <p><b>与Q33的区别：</b>Q33保证元素互不相同，可以通过比较{@code nums[left]}、
 * {@code nums[mid]}和{@code nums[right]}判断哪一侧有序。本题允许重复值，可能出现：
 * <pre>
 * nums[left] == nums[mid] == nums[right]
 * </pre>
 * 此时无法判断旋转断点位于哪一侧，也就无法证明哪一半一定不包含{@code target}。因此不能
 * 强行舍弃一半，只能收缩重复边界后继续二分。
 *
 * <p><b>方法一，面试首选模板：</b>先判断{@code nums[mid] == target}。如果
 * {@code nums[left] == nums[mid] && nums[mid] == nums[right]}，当前无法判断断崖在哪一侧，执行
 * {@code left++}和{@code right--}后继续。排除该歧义后，再使用
 * {@code nums[left] <= nums[mid]}判断左侧有序，否则右侧有序。
 * <pre>
 * if (nums[left] == nums[mid] && nums[mid] == nums[right]) {
 *     left++;
 *     right--;
 *     continue;
 * }
 * if (nums[left] <= nums[mid]) {
 *     // 左侧有序，判断target是否属于[nums[left], nums[mid])
 * } else {
 *     // 右侧有序，判断target是否属于(nums[mid], nums[right]]
 * }
 * </pre>
 * 三点相等的情况必须先处理。处理后，若{@code nums[left] < nums[mid]}，左侧显然没有
 * 跨越断崖；若二者相等但左侧跨越了断崖，则右端点也会相等，又会落入刚刚排除的三点相等
 * 分支。因此此时可以使用{@code <=}判断左侧有序。
 *
 * <p><b>方法二，低记忆负担模板：</b>在确认{@code nums[mid] != target}后，如果
 * {@code nums[left] == nums[mid]}，则{@code nums[left]}也一定不是目标值，可以安全执行
 * {@code left++}。排除相等后，再使用{@code nums[left] < nums[mid]}判断左侧严格有序。
 * 该等值排除依赖“正在搜索确定的target”，不是所有二分问题的通用规则。
 *
 * <p><b>选择：</b>面试遇到Q81原题，优先使用方法一。它是更标准的写法，并且在三点相等时
 * 一次可以排除两个无信息边界；方法二更容易记忆，可作为备选。两种方法的渐进复杂度相同。
 *
 * <p><b>复杂度限制：</b>大多数情况下仍然能够二分搜索；但当数组包含大量重复元素时，每次
 * 可能只能排除一个边界元素，最坏时间复杂度会从{@code O(log N)}退化为{@code O(N)}。
 * 两种方法的额外空间复杂度都是{@code O(1)}。
 *
 * <p>详细模型参见同目录{@code 旋转排序数组二分查找.md}。
 */
public class Q81_SearchInRotatedSortedArrayII {

    /**
     * 重点：旋转数组只有一个【断崖】。实现时优先采用Javadoc中的方法一。
     * DONE: 【已独立完成】当前实现已通过非递减数组、重复值及任意旋转位置的穷举对数验证。
     * @param nums
     * @param target
     * @return
     */
    public boolean search(int[] nums, int target) {
        int l = 0;
        int r = nums.length - 1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (nums[mid] == target) {
                return true;
            } else if (nums[l] == nums[mid] && nums[mid] == nums[r]) {
                l++;
                r--;
            } else if (nums[l] <= nums[mid]) {  // 左侧有序
                if (target >= nums[l] && target <= nums[mid]) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            } else {    // 右侧有序
                if (target >= nums[mid] && target <= nums[r]) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
        }
        return false;
    }
}
