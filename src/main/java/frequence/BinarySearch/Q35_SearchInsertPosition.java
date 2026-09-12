package frequence.BinarySearch;

/**
 * 35. 搜索插入位置
 *
 * <p>给定一个排序数组{@code nums}和一个目标值{@code target}，如果目标值存在于数组中，
 * 返回它的下标；否则返回它按顺序插入数组时应处于的位置。
 *
 * <p>数组中不存在重复元素，要求使用时间复杂度为{@code O(log N)}的算法。
 */
public class Q35_SearchInsertPosition {

    /**
     * 2026-09-12 复盘版本：普通闭区间二分结束后确定插入位置。
     */
    class SolutionReviewed20260912 {

        /**
         * TODO: 【致命错误】本题有一个致命错误，就是判断target不在数组中时，需要插入的位置。
         * 如果target要插入到nums[m]的前面，那么target的插入下标其实是m，而不是m-1。
         */
        public int searchInsert(int[] nums, int target) {
            int len = nums.length;
            int left = 0;
            int right = len - 1;
            int mid = -1;

            while (left <= right) {
                mid = left + (right - left) / 2;
                if (nums[mid] == target) {
                    return mid;
                } else if (nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // TODO: 【超级错误】target与nums[mid]相比较，下列逻辑是错误的。
            // 错误行：return nums[mid] < target ? mid + 1 : mid - 1;
            // 如果nums[mid]>target，target应插入到nums[mid]前面，对应下标就是mid。
            return nums[mid] < target ? mid + 1 : mid;
        }
    }

    /**
     * 2026-09-12 推荐写法：循环结束后直接返回left。
     *
     * <p>循环始终排除已经确定的位置。退出时{@code left == right + 1}，并且：
     * <pre>
     * [0, left)            所有元素都小于target
     * [left, nums.length)  所有元素都大于target
     * </pre>
     * Q35保证元素互不相同且前面已经处理相等情况，因此left就是第一个大于target的位置，
     * 也就是target应当插入的位置。这个结论不依赖最后一次mid，更稳定、更容易证明。
     */
    class SolutionReturnLeft20260912 {

        public int searchInsert(int[] nums, int target) {
            int left = 0;
            int right = nums.length - 1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (nums[mid] == target) {
                    return mid;
                } else if (nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // TODO: 【核心】插入位置是分界点left，不是mid的前一个位置。
            return left;
        }
    }

    /**
     TODO:【特别重要-适合所有题】【遗漏】ans==-1 的情况一定要考虑！！！ 这种你初始值，进循环，但是退出循环时 还保留初始值的情况，一定要在最后返回时 做分支检查！！！
     */
    class Solution {
        public int searchInsert(int[] nums, int target) {
            // 求 >= target 的 最左
            int l = 0;
            int r = nums.length - 1;
            int ans = -1;
            while (l <= r) {
                // TODO: 【可优化-通用写法】建议使用l + (r - l) / 2，避免l + r理论上发生整数溢出。
                int mid = (l + r) / 2;
                // TODO: 【限制条件】题目保证数组元素互不相同，因此命中target时可以直接返回。
                // 若把本方法迁移为“存在重复值时寻找最左插入位置”，这里不能直接返回，
                // 而要记录mid并继续向左搜索。
                if (nums[mid] == target) {
                    return mid;
                } else if (nums[mid] > target) {
                    ans = mid;
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            // TODO: 【遗漏】ans==-1 的情况一定要考虑！！！ 这种你初始值，进循环，但是退出循环时 还保留初始值的情况，一定要在最后返回时 做检查！！！
            // TODO: 【错误】反例：[1,3,5,6] target=7。 要注意ans==-1的情况，说明没有找到>target的位置，也就是说，nums[x]全部<target。 【此时，应该插入 num.length的位置】
            // 错误行： return ans;
            return ans == -1 ? nums.length : ans;
        }
    }

    /**
     * 推荐模板：在左闭右开区间{@code [l, r)}中寻找第一个{@code >= target}的位置。
     * 返回值天然属于{@code [0, nums.length]}，无需使用{@code -1}哨兵或在结尾补分支。
     */
    class RecommendedSolution {
        public int searchInsert(int[] nums, int target) {
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
    }
}
