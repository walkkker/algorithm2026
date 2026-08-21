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
