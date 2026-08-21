package kmp.test;

public class KMP {


    public static int[] getNextArray(char[] chs) {
        if (chs.length == 1) {
            return new int[]{-1};
        }
        int len = chs.length;
        int[] next = new int[len];
        next[0] = -1;
        next[1] = 0;
        int c = 0;
        int i = 2;
        while (i < len) {
            if (chs[c] == chs[i - 1]) {
                next[i++] = ++c;
            } else if (c != 0) {
                c = next[c];
            } else {
                i++;
            }
        }
        return next;
    }

    public static int indexOf(String s, String m) {
        char[] chs1 = s.toCharArray();
        char[] chs2 = m.toCharArray();
        int[] next = getNextArray(chs2);
        int x = 0;
        int y = 0;
        while (x < chs1.length && y < chs2.length) {
            if (chs1[x] == chs2[y]) {
                x++;
                y++;
            } else if (y != 0) {
                y = next[y];
            } else {
                x++;
            }
        }
        return y == chs2.length ? x - y : -1;
    }


}
