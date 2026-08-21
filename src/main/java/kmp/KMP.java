package kmp;
import kmp.*;

/**
 * Parameters:
 * (1) string to be searched
 * and (2) string to be matched
 * <p>
 * Return:
 * Function is like built-in s.indexOf(h);
 * which means returning the first index that matches happen.
 * <p>
 * 实现的效果：时间复杂度 O(N * M) -> O(N)     (N为匹配串，M为模式串，M < N)
 */
public class KMP {

    // 包含两个函数：（1） getNextArray(char[] m) (2) getIndexOf(String s, String m)
    public static int[] getNextArray(char[] m) {
        if (m.length == 1) {
            return new int[]{-1};
        }
        int[] next = new int[m.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2;
        int c = 0;
        while (i < next.length) {
            if (m[c] == m[i-1]) {
                next[i++] = ++c;
            } else if (c == 0) {
                i++;  // next[i++] = 0
            } else {
                c = next[c];
            }
        }
        return next;
    }

    public static int getIndexOf(String s, String m) {
        if (m.length() == 0) {
            return 0;
        }
        char[] chs1 = s.toCharArray();
        char[] chs2 = m.toCharArray();
        int[] next = getNextArray(chs2);
        int x = 0;
        int y = 0;
        while (x < chs1.length && y < chs2.length) {
            if (chs1[x] == chs2[y]) {
                x++;
                y++;
            } else if (y == 0) {
                x++;
            } else {
                y = next[y];
            }
        }
        return y == chs2.length ? x - y : -1;
    }

    public static int[] getNextArray2(char[] m) {
        // S1: 只有length>2的模式串才需要跑主逻辑。  上游已经保证m!=null。
        if (m.length == 1) {
            return new int[]{-1};
        }
        int[] next = new int[m.length];
        // S2: len>=2时，固定 arr[0]=-1， arr[1]=0
        next[0] = -1;
        next[1] = 0;
        // S3: 前两个位置的值是固定的。所以初始化时，要比较的位置从i=2开始，第一个比较的cn=0 （与i=1比较）
        int i = 2;
        int cn = 0;
        // S4：利用前面的信息
        while (i < next.length) {
            if (m[i - 1] == m[cn]) {
                next[i++] = ++cn;
            } else if (cn > 0) {   // m[i-1] != m[cn]时，如果cn还能跳(cn > 0)，那么cn=next[cn]
                cn = next[cn];
            } else {
                // m[i-1] != m[cn]时，如果cn=0代表 一个前后缀都没匹配上。所以此时next[i]=0，然后i++
                next[i++] = 0;
            }
        }
        return next;
    }

    // 如果匹配不上，则返回-1
    public static int getIndexOf2(String s, String m) {
        if (s == null || m == null || s.length() < 1 || m.length() < 1 || s.length() < m.length()) {
            return -1;
        }
        char[] str = s.toCharArray();
        char[] match = m.toCharArray();
        // O(M) m <= n
        int[] next = getNextArray2(match);
        int x = 0;
        int y = 0;
        // O(N)
        while (x < str.length && y < match.length) {
            if (str[x] == match[y]) {
                x++;
                y++;
            } else if (next[y] != -1) {
                y = next[y];
            } else {     // 匹配失败又不能再跳了。 next[y] == -1  ==> y = 0。 简单理解：匹配失败了的前提下，y跳到不能再跳了。只能str[x++]
                x++;     // 这个意味着 最小的 str[x] 与 match[0] 都匹配不上。那只能x++了，也就是 前进到下一个字符，重新（从match[0]）进行匹配。
            }
        }
        // y == match.length 代表模式串匹配成功了。 [start, x) 长度为match.length==y， 所以start=x-y
        return y == match.length ? x - y : -1;
    }


    // for test
    public static String getRandomString(int possibilities, int size) {
        char[] ans = new char[(int) (Math.random() * size) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (char) ((int) (Math.random() * possibilities) + 'a');
        }
        return String.valueOf(ans);
    }

    public static void main(String[] args) {
        int possibilities = 5;
        int strSize = 20;
        int matchSize = 5;
        int testTimes = 5000000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            String str = getRandomString(possibilities, strSize);
            String match = getRandomString(possibilities, matchSize);
            if (getIndexOf(str, match) != str.indexOf(match)) {
                System.out.println("test1 Oops!");
            }
            if (kmp.test.KMP.indexOf(str, match) != str.indexOf(match)) {
                System.out.println("test2 Oops");
            }
        }
        System.out.println("test finish");
    }
}
