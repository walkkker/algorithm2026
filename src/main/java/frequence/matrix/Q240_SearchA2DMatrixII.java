package frequence.matrix;

/**
 * 240. 搜索二维矩阵 II
 *
 * <p>给定一个每行从左到右递增、每列从上到下递增的矩阵，判断目标值是否存在。
 *
 * <p>当前实现来自LeetCode已保存代码。从右上角开始，该位置同时具有“当前行最大”和
 * “当前列最小”的单调方向：当前值大于目标时排除当前列，当前值小于目标时排除当前行。
 * 每次比较都能排除一整行或一整列。
 *
 * <p>时间复杂度{@code O(M + N)}，额外空间复杂度{@code O(1)}。
 */
public class Q240_SearchA2DMatrixII {

    /**
     * 2026-08-30 我的右上角排除法。
     *
     * <p>时间复杂度O(M + N)，额外空间复杂度O(1)。方法是每次比较至少舍弃一行或者一列，
     * 指针最多向下移动M次、向左移动N次。
     *
     * <p><b>核心点：</b>
     * <ol>
     *     <li>起点总是处在“大于一批数据a，同时小于另一批数据b”的位置上。</li>
     *     <li>将起点与target比较后，可以确定舍弃哪一整批数据，同时也唯一决定行走方向。</li>
     * </ol>
     *
     * <p>依据这个核心条件，起始点只能选择右上角或者左下角：
     * <pre>{@code
     * 右上角：左侧更小，下方更大；大了向左，小了向下。
     * 左下角：上方更小，右侧更大；大了向上，小了向右。
     * }</pre>
     * 左上角的右侧和下方都更大，右下角的左侧和上方都更小，一次比较无法判断应该排除
     * 哪个方向，因此不能作为这种排除法的起点。
     *
     * <p><b>循环不变量：</b>在当前尚未排除的搜索区域中，位置{@code (row, column)}是
     * 当前行的最大值、当前列的最小值：
     * <ul>
     *     <li>当前值小于target：当前行左侧全部更小，整行都不可能命中，执行row++。</li>
     *     <li>当前值大于target：当前列下方全部更大，整列都不可能命中，执行column--。</li>
     * </ul>
     */
    public static class Solution20260830 {

        public boolean searchMatrix(int[][] matrix, int target) {
            // 本题选择右上角作为起点，所以行进方向只有：大了往左，小了往下。
            int m = matrix.length;
            int n = matrix[0].length;
            int[] p = {0, n - 1};

            // 因为只会向下或向左，所以行不能越过m，列不能小于0。
            while (p[0] < m && p[1] >= 0) {
                if (matrix[p[0]][p[1]] == target) {
                    return true;
                } else if (matrix[p[0]][p[1]] < target) {
                    // 当前行中尚未排除的其他值都更小，可以舍弃当前整行。
                    p[0]++;
                } else {
                    // 当前列中尚未排除的其他值都更大，可以舍弃当前整列。
                    p[1]--;
                }
            }
            return false;
        }
    }

    public static class Solution {

        public boolean searchMatrix(int[][] matrix, int target) {
            int rows = matrix.length;
            int columns = matrix[0].length;
            int row = 0;
            int column = columns - 1;
            while (row < rows && column >= 0) {
                if (matrix[row][column] > target) {
                    column--;
                } else if (matrix[row][column] < target) {
                    row++;
                } else {
                    return true;
                }
            }
            return false;
        }
    }
}
