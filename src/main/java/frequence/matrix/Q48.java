package frequence.matrix;

public class Q48 {
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
