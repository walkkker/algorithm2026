package frequence.BinarySearch;

/**
 * 153. 寻找旋转排序数组中的最小值
 *
 * <p>给定一个元素互不相同、原本按升序排列并经过若干次旋转的整数数组{@code nums}，返回数组
 * 中的最小元素。
 *
 * <p>要求使用时间复杂度为{@code O(log N)}的算法。
 *
 * <p><b>二分排除原则：</b>普通二分根据整体有序性舍弃不可能包含目标值的一半。Q153虽然
 * 没有具体target，但仍然通过旋转数组的两段有序结构，判断哪一部分不可能包含最小值：
 * <pre>
 * mid在高值段：高值段中的mid及其左侧不可能是最小值，可以舍弃；
 * mid在低值段：mid可能就是最小值，只能舍弃mid右侧。
 * </pre>
 * 本题同样不是机械折半，而是先获得确定的范围结论，再安全缩小答案所在区间。
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
 *          最小值/断崖后的第一个点
 * </pre>
 *
 * <p>旋转数组由两个递增段组成，最小值就是断崖式下降之后、低值递增段的第一个元素。
 * 因此Q153不是搜索某个target，而是不断缩小“断崖后的第一个点”所在区间。
 *
 * <p><b>二分判断：</b>
 * <pre>
 * nums[mid] &gt; nums[right]
 *     mid位于高值段，断崖和最小值一定在mid右侧
 *     left = mid + 1
 *
 * nums[mid] &lt; nums[right]
 *     mid位于低值段，mid可能就是最小值
 *     right = mid
 * </pre>
 *
 * <p>{@code right = mid}不能写成{@code mid - 1}，因为第二种情况下{@code mid}自身可能
 * 就是断崖后的第一个点，不能排除。
 *
 * <p><b>限制与扩展：</b>Q153保证元素互不相同。如果允许重复值，
 * {@code nums[mid] == nums[right]}时无法判断最小值在哪一侧，只能执行
 * {@code right--}缩小范围，这就是Q154，最坏时间复杂度会退化为{@code O(N)}。
 */
public class Q153_FindMinimumInRotatedSortedArray {

    /**
     旋转数组的走势图如下，可以很好的帮助本题：
       1   -》 高值有序段
      1
     1
           1 -》低值有序段
          1
        1
     */
    class Solution {
        public int findMin(int[] nums) {
            int l = 0;
            int r = nums.length - 1;
            while (l < r) {
                // TODO: 【可优化-通用写法】建议使用l + (r - l) / 2，避免l + r理论上发生整数溢出。
                int mid = (l + r) / 2;
                // TODO: 【可优化-统一不变量】与全局最后一个元素nums[nums.length - 1]比较，
                // 在Q153元素互不相同的约束下是正确的；更通用、更容易迁移到Q154的模板是
                // 始终与当前搜索区间的右边界nums[r]比较。
                if (nums[mid] < nums[nums.length - 1]) {
                    r = mid;  // TODO：【注意这里】小于右侧，说明在 右侧有序区间里面（这里是可能包含最小值的，所以r=mid）
                } else {
                    l = mid + 1;
                }
            }
            return nums[l];
        }
    }

    /**
     * 推荐模板：维护“最小值始终位于当前闭区间{@code [l, r]}”这一不变量。
     */
    class RecommendedSolution {
        public int findMin(int[] nums) {
            int l = 0;
            int r = nums.length - 1;
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (nums[mid] > nums[r]) {
                    // mid位于高值段，最小值一定在mid右侧。
                    l = mid + 1;
                } else {
                    // mid位于低值段，mid自身可能就是最小值，不能排除。
                    r = mid;
                }
            }
            return nums[l];
        }
    }
}
