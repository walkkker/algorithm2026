package frequence.BinarySearch;

/**
 * 33. 搜索旋转排序数组
 *
 * <p>一个元素互不相同、原本按升序排列的整数数组，可能在某个未知下标处经过旋转。给定旋转后
 * 的数组{@code nums}和目标值{@code target}，如果目标值存在则返回其下标，否则返回
 * {@code -1}。
 *
 * <p>要求使用时间复杂度为{@code O(log N)}的算法。
 *
 * <p><b>核心依据：</b>升序数组旋转后只有一个断点。使用{@code mid}切分当前搜索区间时，
 * {@code [left, mid]}和{@code [mid, right]}至少有一侧保持有序。因此每轮先判断哪一侧
 * 有序，再判断{@code target}是否落在该有序侧的值域中：
 * <pre>
 * target在有序侧：进入有序侧
 * target不在有序侧：进入另一侧
 * </pre>
 *
 * <p><b>二分的本质：</b>普通升序数组能够二分，是因为整体有序，可以根据
 * {@code nums[mid]}与{@code target}的大小关系，证明其中一半不可能包含答案并整体舍弃。
 * 旋转数组也遵循相同原则，只是整体有序变成了“至少一侧有序”：
 * <pre>
 * 先找到有序段；
 * 再判断target是否落在该有序段的值域中；
 *
 * 在：保留该有序段，舍弃另一段；
 * 不在：该有序段可以确定舍弃，进入另一段。
 * </pre>
 * 所以二分并不是机械地折半，而是利用有序性或单调性，安全排除不可能包含答案的区间。
 *
 * <p><b>旋转数组走势图：</b>
 * <pre>
 * nums = [4,5,6,7,0,1,2]
 *
 * 值
 * 7 |          *
 * 6 |       *
 * 5 |    *
 * 4 | *
 * 3 |
 * 2 |                   *
 * 1 |                *
 * 0 |             *
 *   +----------------------&gt; 下标
 *     0  1  2  3  4  5  6
 *                ^
 *           旋转断点/断崖
 * </pre>
 * 整体由“高值递增段 + 一次断崖式下降 + 低值递增段”组成。Q33的本质就是判断
 * {@code mid}位于哪一段，从而识别当前区间的有序侧。
 *
 * <p><b>当前实现：</b>通过比较{@code nums[mid]}与全局最后一个元素，判断{@code mid}
 * 位于旋转点左侧的高值段还是右侧的低值段。在本题“元素互不相同”的约束下逻辑正确。
 *
 * <p><b>更常用的当前区间模板：</b>
 * <pre>
 * if (nums[left] &lt;= nums[mid]) {
 *     // [left, mid]有序
 * } else {
 *     // [mid, right]有序
 * }
 * </pre>
 * 这种写法直接描述当前搜索区间，不依赖原数组的全局末尾位置，更容易迁移到其他旋转数组题。
 *
 * <p><b>限制条件：</b>本方法依赖元素互不相同。如果允许重复值，可能出现
 * {@code nums[left] == nums[mid] == nums[right]}，此时无法判断哪一侧有序，需要通过
 * {@code left++、right--}消除无效边界，最坏时间复杂度会退化为{@code O(N)}。
 *
 * <p><b>扩展题目：</b>
 * <ul>
 *     <li>Q81 搜索旋转排序数组II：允许重复元素，需要处理三点相等的歧义；</li>
 *     <li>Q153 寻找旋转排序数组中的最小值：比较{@code nums[mid]}与{@code nums[right]}，
 *     不断收缩最小值所在区间；</li>
 *     <li>Q154 寻找旋转排序数组中的最小值II：允许重复元素，最坏退化为线性时间。</li>
 * </ul>
 *
 * <p>元素互不相同时，每轮至少排除一半区间，时间复杂度为{@code O(log N)}，额外空间复杂度
 * 为{@code O(1)}。
 */
public class Q33_SearchInRotatedSortedArray {

    class Solution {
        public int search(int[] nums, int target) {
            int l = 0;
            int r = nums.length - 1;
            while (l <= r) {
                // TODO: 【可优化-通用写法】当前题目数组长度较小，不会溢出；通用模板建议写成：
                // int mid = l + (r - l) / 2;
                int mid = (l + r) / 2;
                if (nums[mid] == target) {
                    return mid;
                }

                // TODO: 【限制条件】该判断依赖Q33元素互不相同。
                // nums[mid] < 全局末尾值，说明mid位于旋转点右侧的低值有序段。
                // 如果题目允许重复值，不能通过该条件稳定判断有序侧，需要参考Q81。
                if (nums[mid] < nums[nums.length - 1]) {
                    // [mid, r]有序：target属于(nums[mid], nums[r]]时进入右侧。
                    if (target > nums[mid] && target <= nums[r]) {
                        l = mid + 1;
                    } else {
                        r = mid - 1;
                    }
                } else {
                    // [l, mid]有序。
                    // TODO: 【可优化-边界表达】前面已经排除target == nums[mid]，
                    // 所以target <= nums[mid]可以写成更准确的target < nums[mid]。
                    // 当前写法只是包含了一个已经排除的情况，不影响正确性。
                    if (target >= nums[l] && target <= nums[mid]) {
                        r = mid - 1;
                    } else {
                        l = mid + 1;
                    }
                }
            }
            return -1;
        }
    }

    /**
     * 推荐模板：每轮只分析当前搜索区间{@code [l, r]}，先判断哪一侧有序，再根据有序侧的
     * 值域排除一半。该写法与二分查找的“当前区间不变量”更一致。
     */
    class RecommendedSolution {
        public int search(int[] nums, int target) {
            int l = 0;
            int r = nums.length - 1;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                if (nums[mid] == target) {
                    return mid;
                }

                if (nums[l] <= nums[mid]) {
                    // [l, mid]有序；前面已排除mid，所以右边界使用严格小于。
                    if (nums[l] <= target && target < nums[mid]) {
                        r = mid - 1;
                    } else {
                        l = mid + 1;
                    }
                } else {
                    // [mid, r]有序；前面已排除mid，所以左边界使用严格大于。
                    if (nums[mid] < target && target <= nums[r]) {
                        l = mid + 1;
                    } else {
                        r = mid - 1;
                    }
                }
            }
            return -1;
        }
    }
}
