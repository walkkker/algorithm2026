package frequence.BinarySearch;

/**
 * 74. 搜索二维矩阵
 *
 * <p>给定一个二维整数矩阵{@code matrix}和一个目标值{@code target}。矩阵每行中的整数从左到右
 * 按非严格递增顺序排列，并且每一行的第一个整数大于前一行的最后一个整数。判断目标值是否
 * 存在于矩阵中。
 *
 * <p>要求使用时间复杂度为{@code O(log(MN))}的算法。
 */
public class Q74_SearchA2DMatrix {

    /**
     二维映射一维 / 一维映射二维
     TODO： 一维坐标 映射 二维坐标规则(到处通用)： row=index/N;  col=index%N
     */
    class Solution {
        public boolean searchMatrix(int[][] matrix, int target) {
            // TODO: 【限制条件】LeetCode本题保证matrix至少有一行一列，所以可直接访问matrix[0]。
            // 若迁移到不保证输入合法的业务方法，应先判断matrix == null、matrix.length == 0、
            // matrix[0] == null或matrix[0].length == 0。
            int M = matrix.length;
            int N = matrix[0].length;
            int l = 0;
            // TODO: 【限制条件】本题数据规模下M * N不会溢出。通用场景若矩阵规模可能超过
            // int范围，应使用long保存一维下标和乘积。
            int r = M * N - 1;
            while (l <= r) {
                // TODO: 【可优化-通用写法】建议使用l + (r - l) / 2，避免l + r理论上发生整数溢出。
                int mid = (l + r) / 2;
                // 下面是最重要的
                int row = mid / N;
                int col = mid % N;
                if (matrix[row][col] == target) {
                    return true;
                } else if (matrix[row][col] < target) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
            return false;
        }
    }

    /**
     * 推荐模板：核心算法与原实现相同，补充通用输入边界，并使用防溢出的中点写法。
     */
    class RecommendedSolution {
        public boolean searchMatrix(int[][] matrix, int target) {
            if (matrix == null || matrix.length == 0
                    || matrix[0] == null || matrix[0].length == 0) {
                return false;
            }

            int rows = matrix.length;
            int cols = matrix[0].length;
            int l = 0;
            int r = rows * cols - 1;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                int value = matrix[mid / cols][mid % cols];
                if (value == target) {
                    return true;
                } else if (value < target) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
            return false;
        }
    }
}
