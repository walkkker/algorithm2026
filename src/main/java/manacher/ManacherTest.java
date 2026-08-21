package manacher;

public class ManacherTest {
    // TODO：字符串问题，都转为char[]。  如果比较的是字符串本身，那么就变成String[]
    public static char[] manacherString(String s) {
        char[] chs = s.toCharArray();
        char[] ans = new char[chs.length * 2 + 1];
        int index = 0;
        for (int i = 0; i < ans.length; i++) {
            if ((i & 1) == 1) {
                ans[i] = chs[index++];
            } else {
                ans[i] = '#';
            }
        }
        return ans;
    }

    public static int manacher(String s) {
        char[] chs = manacherString(s);
        int[] pArr = new int[chs.length];
        int C = 0;
        int R = 0;
        int max = 0;
        for (int i = 0; i < chs.length; i++) {
            pArr[i] = i >= R ? 1 : Math.min(R - i, pArr[2 * C - i]);

            while (i - pArr[i] >= 0
                    && i + pArr[i] < chs.length
                    && chs[i - pArr[i]] == chs[i + pArr[i]]) {
                pArr[i]++;
            }

            if (i + pArr[i] > R) {
                R = i + pArr[i];
                C = i;
            }

            max = Math.max(max, pArr[i]);

        }

        // TODO: 【错误】这是manacherString的回文半径！！！ 不是全长！！！
        // return (max - 1) / 2;
        return max - 1;
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
            if (manacher(str) != right(str)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish");
    }



}
