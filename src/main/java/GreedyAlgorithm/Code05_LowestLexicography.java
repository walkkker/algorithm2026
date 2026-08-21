package GreedyAlgorithm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;


/**
 * 该题贪心为 排序策略： (s1 + s2).compareTo(s2 + s1)
 * [Ab, B, DcB, d, dCeA, dEb]   s1.compareTo(s2)
 * [Ab, B, DcB, dCeA, dEb, d]   (s1 + s2).compareTo(s2 + s1)
 * <p>
 * 这个策略跟 s1.compareTo(s2) 主要针对的场景是 d, dc这种。 d+dc > dc+d
 * <p>
 * 这个贪心策略+排序策略的解释是：首先知道他是 基于比较的排序
 * 以Ab为例，在0-n-1范围内， 选出一个element，
 * 1）该element与其余元素全部【比较】，element排在前面（相比于对手排在前面）都是最小的。
 * 2）所以不管怎么排列，最小的字典序排列串的 第一个元素一定是element
 */

/**
 * 最小字典序
 * <p>
 * 解法: a + b < b + a, 则 a 排列在前
 * <p>
 * <p>
 * <p>
 * 证明：
 * （1）需要先证明 传递性 -> 数学推导
 * （2）使用交换法 -> 证明 ...a...b... 假设a.b<b.a。 任意两个交换，字典序只增不减。 进而证明两个字符串一点不能动，一动就会字典序上升。
 * 先证任意两个， 再证任意三个，4 5 最终数学归纳法  收敛于N个
 */
public class Code05_LowestLexicography {

    public static String lowestString1(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        TreeSet<String> ans = process(strs);
        return ans.size() == 0 ? "" : ans.first();
    }

    // strs中所有字符串全排列，返回所有可能的结果
    public static TreeSet<String> process(String[] strs) {
        TreeSet<String> ans = new TreeSet<>();
        if (strs.length == 0) {
            ans.add("");
            return ans;
        }
        for (int i = 0; i < strs.length; i++) {
            String first = strs[i];
            String[] nexts = removeIndexString(strs, i);
            TreeSet<String> next = process(nexts);
            for (String cur : next) {
                ans.add(first + cur);
            }
        }
        return ans;
    }

    // {"abc", "cks", "bct"}
    // 0 1 2
    // removeIndexString(arr , 1) -> {"abc", "bct"}
    public static String[] removeIndexString(String[] arr, int index) {
        int N = arr.length;
        String[] ans = new String[N - 1];
        int ansIndex = 0;
        for (int i = 0; i < N; i++) {
            if (i != index) {
                ans[ansIndex++] = arr[i];
            }
        }
        return ans;
    }

    public static class MyComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return (a + b).compareTo(b + a);
//			return a.compareTo(b);
        }
    }

    public static String lowestString2(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
//		Arrays.sort(strs, new MyComparator());
        Arrays.sort(strs, ((s1, s2) -> (s1 + s2).compareTo(s2 + s1)));
        String res = "";
        for (int i = 0; i < strs.length; i++) {
            res += strs[i];
        }
        return res;
    }

    // for test
    public static String generateRandomString(int strLen) {
        char[] ans = new char[(int) (Math.random() * strLen) + 1];
        for (int i = 0; i < ans.length; i++) {
            int value = (int) (Math.random() * 5);
            ans[i] = (Math.random() <= 0.5) ? (char) (65 + value) : (char) (97 + value);
        }
        return String.valueOf(ans);
    }

    // for test
    public static String[] generateRandomStringArray(int arrLen, int strLen) {
        String[] ans = new String[(int) (Math.random() * arrLen) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = generateRandomString(strLen);
        }
        return ans;
    }

    // for test
    public static String[] copyStringArray(String[] arr) {
        String[] ans = new String[arr.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = String.valueOf(arr[i]);
        }
        return ans;
    }

    public static void main(String[] args) {
        int arrLen = 6;
        int strLen = 5;
        int testTimes = 10000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            String[] arr1 = generateRandomStringArray(arrLen, strLen);
            String[] arr2 = copyStringArray(arr1);
            if (!lowestString1(arr1).equals(lowestString2(arr2))) {
                for (String str : arr1) {
                    System.out.print(str + ",");
                }
                System.out.println();
                System.out.println("Oops!");
                Arrays.sort(arr1, (s1, s2) -> (s1.compareTo(s2)));
                System.out.println(Arrays.toString(arr1));
                Arrays.sort(arr1, (s1, s2) -> ((s1 + s2).compareTo(s2 + s1)));
                System.out.println(Arrays.toString(arr1));
                return;
            }
        }
        System.out.println("finish!");
    }

}
