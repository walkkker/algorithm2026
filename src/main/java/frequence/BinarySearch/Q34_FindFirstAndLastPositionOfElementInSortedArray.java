package frequence.BinarySearch;

/**
 * 34. 在排序数组中查找元素的第一个和最后一个位置
 *
 * <p>给定一个按非递减顺序排列的整数数组{@code nums}和一个目标值{@code target}，返回目标值
 * 在数组中的开始位置和结束位置。如果数组中不存在目标值，返回{@code [-1, -1]}。
 *
 * <p>要求使用时间复杂度为{@code O(log N)}的算法。
 */
public class Q34_FindFirstAndLastPositionOfElementInSortedArray {

    /**
     * 2026-09-13 复盘版本：分别寻找小于等于target的最右位置和大于等于target的最左位置。
     *
     * <p>两次二分首先寻找边界候选，不要求循环中必须命中target。二分结束后只要确认
     * {@code mostRight}确实指向target，就能确定target存在；此时{@code mostLeft}和
     * {@code mostRight}自然分别是第一次和最后一次出现的位置。
     *
     * <p>时间复杂度为{@code O(log N)}，额外空间复杂度为{@code O(1)}。
     */
    class SolutionReviewed20260913 {

        public int[] searchRange(int[] nums, int target) {
            // 第一轮：寻找nums[index] <= target的最右位置。
            // -1表示不存在任何<=target的数组元素，也能覆盖空数组和target小于全部元素的情况。
            int mostRight = -1;
            int mostLeft = nums.length;
            int l = 0;
            int r = nums.length - 1;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                if (nums[mid] <= target) {
                    mostRight = mid;
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }

            // 第二轮：寻找nums[index] >= target的最左位置。
            // nums.length表示不存在任何>=target的数组元素，是合法的虚拟插入位置。
            l = 0;
            r = nums.length - 1;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                if (nums[mid] >= target) {
                    mostLeft = mid;
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }

            // TODO: 【关键】第一轮得到的只是“<=target的最右候选”，不一定等于target，
            // 所以必须做存在性验证。||具有短路特性，mostRight==-1时不会访问nums[-1]。
            if (mostRight == -1 || nums[mostRight] != target) {
                return new int[]{-1, -1};
            } else {
                // target存在时，第二轮得到的mostLeft必然是target第一次出现的位置。
                return new int[]{mostLeft, mostRight};
            }
        }
    }

    class Solution {
        public int[] searchRange(int[] nums, int target) {
            // TODO: 【错误】限制范围中 nums.length >=0。 所以必须单独考虑，因为 后面代码检验是否存在target的过程存在访问下标。
            if (nums.length == 0) {
                return new int[]{-1, -1};
            }

            // <= target的最右  和 >= target的最左
            int l = 0;
            int r = nums.length - 1;  // TODO: 【错误】出现好几次了！！！  r = nums.length - 1啊！！！
            // TODO: 【可优化-初始值依赖】ansr初始化为0依赖前面的空数组判断，以及二分结束后的
            // nums[ansr] != target校验。当前代码正确，但模板迁移时漏掉任一保护都会产生越界或误判。
            // 更稳健的写法是把“第一个>=target的位置”作为插入位置返回，再统一检查是否越界。
            int ansr = 0;
            while (l <= r) {
                // TODO: 【可优化-通用写法】建议使用l + (r - l) / 2，避免l + r理论上发生整数溢出。
                int mid = (l + r) / 2;
                if (nums[mid] <= target) {
                    ansr = mid;
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }

            if (nums[ansr] != target) {
                return new int[]{-1, -1};
            }

            l = 0;
            r = nums.length - 1;
            int ansl = 0;
            while (l <= r) {
                int mid = (l + r) / 2;
                if (nums[mid] >= target) {
                    ansl = mid;
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            return new int[]{ansl, ansr};
        }
    }

    /**
     * 推荐模板：只记住一个lowerBound方法。
     *
     * <p>{@code lowerBound(nums, x)}返回第一个{@code >= x}的位置，因此：
     * <pre>
     * target第一次出现的位置 = lowerBound(target)
     * target最后一次出现的位置 = lowerBound(target + 1) - 1
     * </pre>
     *
     * <p>这里不直接计算{@code target + 1}，避免{@code target == Integer.MAX_VALUE}时溢出；
     * 第二次二分通过{@code <= target}寻找第一个大于target的位置。
     */
    class RecommendedSolution {
        public int[] searchRange(int[] nums, int target) {
            int first = firstGreaterOrEqual(nums, target);
            if (first == nums.length || nums[first] != target) {
                return new int[]{-1, -1};
            }
            int last = firstGreater(nums, target) - 1;
            return new int[]{first, last};
        }

        private int firstGreaterOrEqual(int[] nums, int target) {
            int l = 0;
            int r = nums.length;
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (nums[mid] >= target) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            return l;
        }

        private int firstGreater(int[] nums, int target) {
            int l = 0;
            int r = nums.length;
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (nums[mid] > target) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            return l;
        }
    }
}
