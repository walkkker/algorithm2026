package frequence.matrix;

public class Q48 {

    /**
     * 2026-08-30 我的分圈原地旋转版本。
     *
     * <p>分圈结构用于正方形。相比Q54矩形螺旋遍历，本题不存在需要收集的oneLine：
     * 偶数阶矩阵最终边界直接交错；奇数阶矩阵最终只剩中心点，而中心点旋转后位置不变，
     * 因此无需处理。
     *
     * <p>只有正方形才能在保持原二维数组形状、不创建新矩阵的前提下完成90度原地旋转。
     * 矩形旋转90度后行列数量交换，无法继续复用同一个固定形状的二维数组。
     *
     * <p><b>外层不变量：</b>{@code p1}和{@code p2}分别表示当前待旋转正方形圈的
     * 左上角和右下角。每处理完一圈，两点同步向内收缩。
     *
     * <p>TODO：【错误】{@code p1}、{@code p2}是{@code int[2]}，数组对象不能直接使用
     * {@code <}比较。必须比较坐标，例如{@code p1[0] < p2[0]}。
     *
     * <p><b>circle是核心：</b>
     * <ol>
     *     <li>for循环只执行边长减一次，避免一圈的起点被重复轮换。</li>
     *     <li>一次处理上、左、下、右四个对应点，本质是扩大版swap，只需要一个临时变量。</li>
     *     <li>先根据偏移量i写出四个点的坐标，再按照逆方向赋值，实现顺时针旋转。</li>
     * </ol>
     */
    public static class Solution20260830 {

        public void rotate(int[][] matrix) {
            int[] p1 = {0, 0};
            int[] p2 = {matrix.length - 1, matrix[0].length - 1};

            // TODO: 【错误】p1、p2是int[2]，不能写while (p1 < p2)。
            // 正方形中行、列边界同步收缩，比较行坐标即可。
            while (p1[0] < p2[0]) {
                circle(matrix, p1, p2);
                p1[0]++;
                p1[1]++;
                p2[0]--;
                p2[1]--;
            }
        }

        // TODO: 【重要】这个方法是核心呀！！！
        // 三个点：
        // 1. for循环里面变量范围的设置。
        // 2. 四个点依次交换值，其实就是扩大版的swap，只需要一个临时变量，剩余代码跟swap一样。
        // 3. 确定每个点随着for循环的位置变化。
        private void circle(int[][] matrix, int[] p1, int[] p2) {
            // 正方形当前圈的边长减一，也是每条边参与四点轮换的元素组数。
            int len = p2[0] - p1[0];
            for (int i = 0; i < len; i++) {
                // 四个点：
                // 上：(p1[0],     p1[1] + i)
                // 右：(p1[0] + i, p2[1])
                // 下：(p2[0],     p2[1] - i)
                // 左：(p2[0] - i, p1[1])

                // TODO: 【可删除】原代码中的last没有参与后续计算，每轮创建int[]没有意义：
                // int[] last = {p1[0], p1[1] + i};

                int tmp = matrix[p1[0]][p1[1] + i];
                matrix[p1[0]][p1[1] + i] = matrix[p2[0] - i][p1[1]];
                matrix[p2[0] - i][p1[1]] = matrix[p2[0]][p2[1] - i];
                matrix[p2[0]][p2[1] - i] = matrix[p1[0] + i][p2[1]];
                matrix[p1[0] + i][p2[1]] = tmp;
            }
        }
    }

    /**
     题目说了是n*n的二维矩阵，那么就是正方形。 相比于Q54_螺旋矩阵打印 的退化单行单列分类逻辑，可以省略。
     */
    class Solution {
        public void rotate(int[][] matrix) {
            int M = matrix.length;
            int N = matrix[0].length;
            int r1 = 0, c1 = 0, r2 = M - 1, c2 = N - 1; // TODO: 【语法错误！！！】连续赋值变量，需要使用**逗号**
            while (r1 <= r2 && c1 <= c2) {
                rotateRing(r1, c1, r2, c2, matrix);
                r1++;
                c1++;
                r2--;
                c2--;
            }

        }

        public void rotateRing(int r1, int c1, int r2, int c2, int[][] matrix) {
            int len = c2 - c1 + 1;
            // TODO: 【下面是超级错误！！！】
            // for (int i = 0; i < len; i++) {   // TODO：【超级错误】我们说了对于每一个边，不遍历最后一个元素。（因为这样才能让顶点无缝旋转） 现在这样写对应[0, len -1]代表的是整行全遍历！！！
            for (int i = 0; i < len - 1; i++) {
                int tmp = matrix[r1][c1 + i];
                matrix[r1][c1 + i] = matrix[r2 - i][c1];
                matrix[r2 - i][c1] = matrix[r2][c2 - i];
                matrix[r2][c2 - i] = matrix[r1 + i][c2];
                matrix[r1 + i][c2] = tmp;
            }
        }
    }
}
