package frequence.Backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 枚举字符串的所有非空子字符串。
 *
 * <p>子字符串必须由原字符串中的一段连续区间组成。长度为{@code N}的字符串共有
 * {@code N * (N + 1) / 2}个非空位置区间。
 *
 * <p>本题按照位置区间收集答案，因此内容相同但位置不同的子字符串需要重复保留。例如
 * {@code "aaa"}应得到3个{@code "a"}、2个{@code "aa"}和1个{@code "aaa"}。
 * 返回顺序不限，对数器会按照多重集合比较结果。
 *
 * <p><b>专题分类：</b>连续子串的全区间枚举。专题索引参见
 * {@code frequence/substringandsubsequence/子串与子序列区别.md}。
 */
public class AllSubstrings {

    /**
     * 返回{@code s}的所有非空连续子字符串。
     *
     * @param s 非null字符串
     * @return 所有非空子字符串；相同内容来自不同区间时保留重复项
     */
    public List<String> allSubstrings(String s) {
        List<String> ans = new ArrayList<>();
        char[] chs = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {
                ans.add(s.substring(i, j + 1));
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        AllSubstrings solver = new AllSubstrings();

        try {
            runDeterministicTests(solver);
            runRandomTests(solver, 10_000, 8, 20260730L);
            System.out.println("AllSubstrings tests passed.");
        } catch (UnsupportedOperationException e) {
            System.out.println("请先实现allSubstrings(String s)，再运行对数器。");
        }
    }

    /**
     * 固定用例覆盖：
     * 空字符串、单字符、全部字符不同、全部字符相同，以及部分重复字符。
     */
    private static void runDeterministicTests(AllSubstrings solver) {
        List<String> cases = Arrays.asList(
                "",
                "a",
                "ab",
                "abc",
                "aaa",
                "abca"
        );

        for (String input : cases) {
            checkOneCase(solver, input);
        }
    }

    /**
     * 随机用例使用较小字符集，主动制造重复字符，验证相同内容子字符串的出现次数。
     */
    private static void runRandomTests(
            AllSubstrings solver,
            int testTimes,
            int maxLength,
            long seed) {

        Random random = new Random(seed);

        for (int test = 0; test < testTimes; test++) {
            int length = random.nextInt(maxLength + 1);
            char[] chars = new char[length];
            for (int i = 0; i < length; i++) {
                chars[i] = (char) ('a' + random.nextInt(4));
            }
            checkOneCase(solver, new String(chars));
        }
    }

    private static void checkOneCase(AllSubstrings solver, String input) {
        List<String> expected = referenceAllSubstrings(input);
        List<String> actual = solver.allSubstrings(input);

        if (actual == null) {
            throw new AssertionError("返回值不能为null，input=" + input);
        }

        // 复制后排序，既不要求实现采用固定输出顺序，又可以保留并比较重复项数量。
        List<String> sortedExpected = new ArrayList<>(expected);
        List<String> sortedActual = new ArrayList<>(actual);
        Collections.sort(sortedExpected);
        Collections.sort(sortedActual);

        if (!sortedExpected.equals(sortedActual)) {
            throw new AssertionError(
                    "Mismatch, input=" + input
                            + ", expected=" + sortedExpected
                            + ", actual=" + sortedActual
            );
        }
    }

    /**
     * 对数器基准实现：每个{@code [left, right)}连续区间对应一个子字符串。
     */
    private static List<String> referenceAllSubstrings(String s) {
        List<String> ans = new ArrayList<>();
        for (int left = 0; left < s.length(); left++) {
            for (int right = left + 1; right <= s.length(); right++) {
                ans.add(s.substring(left, right));
            }
        }
        return ans;
    }
}
