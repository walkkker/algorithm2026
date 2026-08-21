package fabonacii.test;

public class MatrixPow {


    public static int[][] multiMatrix(int[][] a, int[][] b) {
        int len = a.length;
        int[][] res = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                int ans = 0;
                for (int k = 0; k < len; k++) {
                    ans += a[i][k] * b[k][j];
                }
                res[i][j] = ans;
            }
        }
        return res;
    }

    public static int[][] matrixPow(int[][] matrix, int pow) {
        int len = matrix.length;
        int[][] tmp = matrix;
        int[][] ans = new int[len][len];
        for (int i = 0; i < len; i++) {
            ans[i][i] = 1;
        }
        for (; pow > 0; pow >>= 1) {
            if ((pow & 1) == 1) {
                ans = multiMatrix(ans, tmp);
            }
            tmp = multiMatrix(tmp, tmp);
        }
        return ans;
    }


    // 主方法示例如下
    public int climbStairs(int n) {
        // TODO: 【错误点1】 一定要有base case。 只有超出base case的部分，才会进入通用部分
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        int[][] m = {{1,1},{1,0}};
        m = matrixPow(m, n - 2);   // pow + (最大的f(base case)) = n；   即最大的f(2),则pow=n-2. 无所谓最小的是f(0)还是f(1)，
        return m[0][0] * 2 + m[0][1] * 1;
    }

}
