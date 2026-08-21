package manacher;

/**
 * manacher求 一个字符串中的 最长回文子串   可以进一步求 -> 长度/位置/是什么
 */
public class Manacher {

    // 主方法包含两个函数: （1） manacherString解决奇偶数回文长度的不统一处理问题。支持统一以i为中心，暴力往两边扩的逻辑 (2) 主方法 manacher
    public static char[] manacherString(String str) {
        if (str == null) {
            return null;
        }

        char[] chs = str.toCharArray();
        char[] res = new char[2 * chs.length + 1];
        // 通过举例子，我们发现  偶数0 2 4 都是# ， 奇数 1 3 5 对应原数组0 1 2   new = (old * 2) + 1
        // Option2: 这里也可以写一个int index=0。 每次奇数时，res[i] = chs[index++] 也可以，这样就不用做 下标转换了（也适合 发现不了下标转换公式的时候用）
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : chs[(i - 1) / 2];
        }
        return res;
    }

    public static int manacher(String str) {
        if (str == null || str.length() < 1) {
            return 0;
        }
        // 三要素：manacherString, 回文半径数组， 最右边界+对应的中心点
        char[] chs = manacherString(str);
        int[] rArr = new int[chs.length];
        int R = 0;   // 左神这里为了好写代码， R作为 开区间
        int C = 0;
        int maxLen = 0;
        for (int i = 0; i < chs.length; i++) {
            // C - (i - C)  = 2 * C - i
            // 先定起始值
            rArr[i] = i < R ? Math.min(R - i, rArr[2 * C - i]) : 1;
            // while里面对应的是 接下来要探测的两边的有效的位置
            while (i + rArr[i] < chs.length && i - rArr[i] > -1) { // 看下一个位置能不能匹配
                if (chs[i + rArr[i]] == chs[i - rArr[i]]) {
                    rArr[i]++;
                } else {
                    break;
                }
            }

            if (i + rArr[i] > R) {
                R = i + rArr[i];
                C = i;
            }

            // TODO：【重要】上面就是manacher算法核心步骤，下面这个可以根据题意自己写逻辑
            maxLen = Math.max(maxLen, rArr[i]);
        }
        // 这个是计算出来的。 对于原字符串中 奇数个回文 和 偶数个回文，通过举例子发现，对应到manacherString 回文半径m长度, 都是 m - 1 = 原始回文串的全部字符数量
        // 可以自己举例子就发现了
        // TODO: 【错误-第二次做也错了】这是manacherString的回文半径！！！ 不是全长！！！
        // return (max - 1) / 2;
        return maxLen - 1;
    }



    // for test
    // 暴力方法就是 ， 还是要先变成manachar串，然后遍历每个i，遍历时暴力往两边扩。
    public static int right(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = manacherString(s);
        int max = 0;
        for (int i = 0; i < str.length; i++) {
            int L = i - 1;
            int R = i + 1;
            while (L >= 0 && R < str.length && str[L] == str[R]) {
                L--;
                R++;
            }
            max = Math.max(max, R - L - 1);
        }
        return max / 2;
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
        int testTimes = 5000000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            String str = getRandomString(possibilities, strSize);
            if (manacher(str) != right(str) || manachar(str) != right(str)) {
                System.out.println("Oops!");
                break;
            }
        }
        System.out.println("test finish");
    }


    public static int manachar(String s) {
        char[] chs = manacharString(s);
        int R = 0;
        int C = -1;
        int[] rArray = new int[chs.length];
        int maxLen = -1;
        for (int i = 0; i < chs.length; i++) {
            rArray[i] = i >= R ? 1 : Math.min(rArray[2 * C - i], R - i);
            while (i - rArray[i] >= 0 && i + rArray[i] <= chs.length - 1) {
                if (chs[i - rArray[i]] == chs[i + rArray[i]]) {
                    rArray[i]++;
                } else {
                    break;
                }
            }

            if (i + rArray[i] > R) {
                C = i;
                R = i + rArray[i];
            }
            maxLen = Math.max(maxLen, rArray[i]);
        }
        return maxLen - 1;   // 不管原始字符串是偶数还是奇数，对应到 manachar string 的 回文半径，结果都是 rArray[i] - 1 == 原回文串长度
    }


    public static char[] manacharString(String s) {
        char[] oldChar = s.toCharArray();
        int len = oldChar.length;
        char[] newChar = new char[len * 2 + 1];
        int index = 0;
        for (int i = 0; i < newChar.length; i++) {
            newChar[i] = (i & 1) == 0 ? '#' : oldChar[index++];
        }
        return newChar;
    }


}
