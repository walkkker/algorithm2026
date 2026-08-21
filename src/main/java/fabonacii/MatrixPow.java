package fabonacii;

/**
 * 【这个是最重要的】 ——> 固定公式，请务必记牢！
 * <p>
 * （1） 矩阵相乘 multiMatrix
 * （2）矩阵快速幂 matrixPow（基于矩阵相乘功能函数）
 */
public class MatrixPow {

    /**
     * 自己再实现一遍 20260316
     **/
    public static int[][] myMultiMatrix(int[][] a, int[][] b) {
        // S1: 结果矩阵是 行列式，对应a的行数 * b的列数
        int[][] res = new int[a.length][b[0].length];
        // S2: 填值res的每个位置。 res[i][j] = a的i行 与 b的j列依次相乘
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                // a的i行 * b的j列 =》 a的列数==b的行数
                for (int k = 0; k < a[0].length; k++) {
                    res[i][j] += a[i][k] * b[k][j];   // 累加
                }
            }
        }
        return res;
    }

    public static int[][] myMatrixPow(int[][] a, int pow) {
        int n = a.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            res[i][i] = 1;
        }
        int[][] tmp = a;
        for (; pow > 0 ; pow >>= 1) {
            if ((pow & 1) == 1) {   // & 位运算优先级非常落后，一定要括号包起来
                res = myMultiMatrix(res, tmp);
            }
            tmp = myMultiMatrix(tmp, tmp);
        }
        return res;
    }


    // 矩阵相乘函数
    // 返回值： 两个矩阵相乘得到的 【二维矩阵】
    // 参数： 二维矩阵A， 二维矩阵B
    public int[][] multiMatrix(int[][] A, int[][] B) {
        int[][] ans = new int[A.length][B[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < B.length; k++) {
                    // 【错误点】 这里经常写错，务必记牢。 是要使用 【+=】  +  【*】
                    ans[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return ans;
    }

    public int[][] matrixPow(int[][] mat, int pow) {
        int[][] ans = new int[mat.length][mat[0].length];
        // 将矩阵设置成 【单位矩阵】
        for (int i = 0; i < ans.length; i++) {
            ans[i][i] = 1;
        }
        int[][] tmp = mat;
        for (; pow > 0; pow >>= 1) {
            if ((pow & 1) == 1) {
                ans = multiMatrix(ans, tmp);
            }
            tmp = multiMatrix(tmp, tmp);
        }
        return ans;
    }


}
