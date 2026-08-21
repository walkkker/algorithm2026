package 卡特兰数;


import java.math.BigInteger;

/**
 * 使用公式 k(n) = c(2n, n) / (n + 1)
 *
 * TODO：最初的辗转相除法是不对的，使用BigInteger，见 卡特兰数.CatalanNumber#myCatalanNum(int) 方法
 */
public class CatalanNumber {
    // TODO： 【很重要】元宝给的方法，这个方法好！！！！
    // 计算组合数 C(n, k)
    public static BigInteger comb(int n, int k) {
        if (k > n - k) k = n - k;
        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < k; i++) {
            result = result.multiply(BigInteger.valueOf(n - i))
                    .divide(BigInteger.valueOf(i + 1));
        }
        return result;
    }

    // 卡特兰数公式 C_n = C(2n, n) / (n+1)
    public static BigInteger catalanFormula(int n) {
        if (n < 0) return BigInteger.ZERO;
        BigInteger c = comb(2 * n, n);
        return c.divide(BigInteger.valueOf(n + 1));
    }


    /**
     * 推导使用卡特兰数的第一公式和第三公式。
     * <p>
     * <p>
     * 代码计算时使用第二公式。
     *
     * @param N 单侧括号数。 具体来说，这里的N代表的是 单纯左括号的数量。 也就是总的括号数量（左括号+右括号）实际上是2N。
     * @return 注意返回值是 long类型，因为组合数会很大
     */
    public static BigInteger myCatalanNum(int N) {
        if (N == 0) {
            return BigInteger.valueOf(0);
        }

        if (N == 1) {   // 就是说只有一个 左括号 + 一个右括号，能够拼出的组合数 就是1
            return BigInteger.valueOf(1);
        }

        BigInteger a = BigInteger.valueOf(1);
        BigInteger b = BigInteger.valueOf(1);
        // C(2n, n) = (2n)! / n! * n! => 2n*(2n-1)*(2n-2)*...*(n+1) / n!
        for (int i = 1, j = N + 1; i <= N; i++, j++) {
            a = a.multiply(BigInteger.valueOf(i));
            b = b.multiply(BigInteger.valueOf(j));
        }
        BigInteger ans = b.divide(a).divide(BigInteger.valueOf(N + 1));
        return ans;
    }

    // TODO: 注意，入参和返回值 类型都是 long
    public static long myGcd(long b, long a) {
        if (a == 0) {
            return b;
        }
        return myGcd(a, b % a);

    }

    // 注意使用 long 类型
    public static long catalanNum(int N) {
        if (N < 0) {
            return 0;
        }
        if (N < 2) {
            return 1;
        }

        long a = 1;
        long b = 1;

        for (int i = 1, j = N + 1; i <= N; i++, j++) {
            a *= i;
            b *= j;

            long gcd = gcd(a, b);

            a /= gcd;
            b /= gcd;
        }

        return (b / a) / (N + 1);

    }

    // 功能函数 -> 求最大公约数
    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static void main(String[] args) {
        int N = 45;
        System.out.println(catalanNum(N));
        System.out.println(myCatalanNum(N));
        System.out.println(catalanFormula(N));
        System.out.println(myCatalanNum(N).equals(catalanFormula(N)));
    }
}
