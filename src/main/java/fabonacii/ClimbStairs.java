package fabonacii;

/**
 * https://leetcode.cn/problems/climbing-stairs/description/
 */
public class ClimbStairs {
    public int climbStairs(int n) {
        // TODO: 【错误】我们既然做了 矩阵快速幂。 那么<初始k的所有元素，需要 直接检查返回。 我们只处理>k的。 （k是指 递推式的元素个数）
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }

        // TODO: 【提示】java初始化数组可以直接这么写！！！
        int[][] base = {{1, 1}, {1, 0}};
        // 此处要注意，它最底是从f(1)开始的，这与原始斐波那契是不一样的。 所以matrix pow也要修改。  pow + 2 = n = > pow = n-2
        base = myMatrixPow(base, n - 2);
        return 2 * base[0][0] + 1 * base[0][1];
    }

    public static int[][] myMultiMatrix(int[][] a, int[][] b) {
        // S1: 结果矩阵是 行列式，对应a的行数 * b的列数
        int[][] res = new int[a.length][b[0].length];
        // S2: 填值res的每个位置。 res[i][j] = a的i行 与 b的j列依次相乘
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                // a的i行 * b的j列 =》 a的列数==b的行数
                for (int k = 0; k < a[0].length; k++) {
                    res[i][j] += a[i][k] * b[k][j]; // 累加
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
        for (; pow > 0; pow >>= 1) {
            if ((pow & 1) == 1) { // & 位运算优先级非常落后，一定要括号包起来
                res = myMultiMatrix(res, tmp);
            }
            tmp = myMultiMatrix(tmp, tmp);
        }
        return res;
    }
}

