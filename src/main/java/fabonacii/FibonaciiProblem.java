package fabonacii;

/**
 * https://leetcode.cn/problems/fei-bo-na-qi-shu-lie-lcof/
 */
public class FibonaciiProblem {

    /**
     * TODO: version 1是最新写的，思路很清晰。
     *
     * 套路点：
     * 1. n >= 45时，必然发生整数溢出。  int范围2*10^9, 而题目要求取模1e9+7，那么矩阵相乘过程中 a*b 必然超过10^9
     * 2. 因此 一阶递推类题目，但凡涉及取模1e9+7，TODO:  **矩阵全部设置为 long[][]**
     * 3. TODO：续2，i行j列计算res[i][j]时， 每次 x = a[i][k]*b[k][j]%1e9 取模，然后 ans+=x后还要取模，让每次计算前的数字都在摸范围内，那么最大的计算值就是 1e9 * 1e9 不会超过long。
     * 4. TODO：所以对于3的套路很清晰，就改 multiMatrix两行代码就可以，加上取模。 因为本质上是在每次涉及到增量计算的地方都立即取模。
     *
     * 错误点：
     * 1. 一阶递推题目，必须要加base case！！！  不然斐波那契f(0)=0，但是走函数会返回m[0][0]=1！
     * 2. int[][] a 或者 long[][] b。 所有的数组类型[]里面都必须是int类型。
     * 3. long不能自动转int。 属于lossy conversion，需要手动转。
     */


    /**
     * 方法2：矩阵快速幂
     * <p>
     * TODO：最大的坑就是，int溢出，取模！！！ 所以一旦n>45/题目说到取模：
     *      在int溢出，取模题目中的绝对结论（在这上面踩了太多坑了！！！）：
     *      1. 涉及到int溢出的计算（题目明确说了取模），就全部使用long类型，不要妄图使用int类型了。
     *      2. 对于斐波那契数列n==100，此时中间结果就会到达10^21，大于Long。 所以尽可能在每次计算时也取模，不要光结尾取模！！！不用管冗余代码问题，先一次性做对！！！
     */

    class Solution {
        public int fib(int n) {
            if (n == 0) {
                return 0;
            }
            if (n == 1) {
                return 1;
            }
            long[][] m = {{1, 1}, {1, 0}};
            m = matrixPow(m, n - 1);
            return (int) m[0][0];
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


    /**
     * 方法1: O(N) + O(1) 递推式实现   0<=n<=100
     */
    // TODO: 【超级错误】 斐波那契 一定要注意取模呀！！！！
    class Solution1 {
        // O(N)版本，递推
        public int fib(int n) {
            // TODO: 【错误】一定不能忽视 边界条件！！！
            if (n == 0) {
                return 0;
            }

            if (n == 1) {
                return 1;
            }
            // TODO: 需要全部改为Long. 不然 pre2 = cur 是语法错误的！！！
            // 其实因为每次循环的计算都取模了 =》 所以 pre1 pre2 cur都取int就可以。 pre1 + pre2 永远不会超过 1e9+7，所以全部使用int完全满足范围
            long pre1 = 0;
            long pre2 = 1;
            // TODO： 【错误】这里面，cur不能使用int，不然加法就错了 （还没来得及取模就错了）
            // int cur = 0;
            long cur = 0;
            for (int i = 2; i <= n; i++) {
                // TODO: 【錯誤】每一次加完之后都要取模
                //      进一步改进1e9+7(1000000007)  不使用Math.pow() ，能直接写数字1000000007，为什么要 计算呢？笨！！！
                cur = (pre1 + pre2) % 1000000007;
                pre1 = pre2;
                pre2 = cur;
            }
            return (int) cur;
        }
    }


}
