package fabonacii;

/**
 * TODO:
 *
 *      与 fibonacii唯二的区别：
 *      1. n==0是 本题为1。  而fib(0)=0
 *      2. 但是递推式相同，因为m相同
 *      3. 因为 f(0)=1，因此最终返回f(n)时， 要计算矩阵的 m[0][0] + m[0][1] ，特别**注意先取模，再转int**
 *
 *
 *
 *
 * 2. 人家ai都说了：
 *    1. 注意事项
 *       - 【超重要】大数取模：由于斐波那契数列增长极快，题目要求对结果取模 1000000007。注意在循环中每一步都要取模，防止中间结果溢出。
 *       - 【超重要】边界条件：注意 n=0和 n=1的特殊情况。
 *       - 时间复杂度：循环解法为 O(n)，在题目给定的 n <= 100范围内完全可行。
 *
 *
 *
 * 所有相关题目都在 Journal.md里面。
 * 直接套用 MatrixPow class的代码。
 * 剩下的就是你的思路 以及 扩展到任意递推式！！！
 *
 * 爬楼梯进阶：
 * https://leetcode.cn/problems/qing-wa-tiao-tai-jie-wen-ti-lcof/description/
 * 本题叫做『跳跃训练』，题目说与 climbStairs爬楼梯相同，其实一点也不一样！！！
 * 1） n [1,45] => [0,100] 这带来的是一个
 *      a. base case增加n==0的讨论
 *      b. 同时所有计算换成Long，
 *      c. 【这个也很重要！！！看代码】同时需要每次计算完一个数字之后也要%，因为 n=100时 超过了long类型大小 是21位数字 （int 10的9次方， long 10的18次方）
 *
 *
 *  TODO: 此版本为正式版本：
 *      (1) 核心：将所有 二维矩阵从 int[][] -> long[][]
 *      (2) 最后返回的时候，别忘了 在强制类型转换会int
 */
public class NumWaysOfFrog {
    // TODO: 【错误点】：n=100时，斐波那契354224848179261915075，所以long也装不下！！！
//      int最大是 10^9量级， long 10^18量级
    /**
     与 fibonacii唯二的区别：
     1. n==0是 本题为1。  而fib(0)=0
     2. 但是递推式相同，因为m相同
     3. 因为 f(0)=1，因此最终返回f(n)时， 要计算矩阵的 m[0][0] + m[0][1] ，特别**注意先取模，再转int**
     **/
    class Solution {
        public int trainWays(int n) {
            if (n == 0) {
                return 1;
            }
            if (n == 1) {
                return 1;
            }
            long[][] m = { { 1, 1 }, { 1, 0 } };
            m = matrixPow(m, n - 1);
            return (int) ((m[0][0] + m[0][1]) % 1000000007);
        }

        public long[][] multiMatrix(long[][] a, long[][] b) {
            int len = a.length;
            long[][] res = new long[len][len];
            for (int i = 0; i < len; i++) { //TODO: 【错误点】数组a[][] 不管初始化还是取值，[]内部的类型必须是int
                for (int j = 0; j < len; j++) {
                    long ans = 0;
                    for (int k = 0; k < len; k++) {
                        ans += (a[i][k] * b[k][j]) % 1000000007;
                        ans %= 1000000007;
                    }
                    res[i][j] = ans;
                }
            }
            return res;
        }

        public long[][] matrixPow(long[][] m, long pow) {
            long[][] tmp = m;
            int len = m.length;
            long[][] base = new long[len][len];
            for (int i = 0; i < len; i++) { // TODO: for循环里面的变量基本都是int
                base[i][i] = 1;
            }
            for (; pow > 0; pow >>= 1) {
                if ((pow & 1) == 1) {
                    base = multiMatrix(base, tmp);
                }
                tmp = multiMatrix(tmp, tmp);
            }
            return base;
        }
    }
}
