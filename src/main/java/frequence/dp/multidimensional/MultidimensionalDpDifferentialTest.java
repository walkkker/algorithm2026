package frequence.dp.multidimensional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 多维DP章节对数器。
 *
 * <p>默认运行全部测试，也可以通过main参数只运行某一题：
 * {@code q5}、{@code q62}、{@code q64}、{@code q72}、{@code q1143}。
 *
 * <p>每组测试都包含固定边界用例和固定随机种子的随机用例。验证时不仅比较两个DP实现，
 * 还使用独立的暴力递归或记忆化搜索作为基准，防止两个实现因为共享同一种错误而同时返回错误结果。
 */
public class MultidimensionalDpDifferentialTest {

    private static final long RANDOM_SEED = 20260805L;
    private static final int RANDOM_TIMES = 2000;
    private static final Map<String, Runnable> TEST_REGISTRY = new HashMap<String, Runnable>();

    static {
        TEST_REGISTRY.put("q5", new Runnable() {
            @Override
            public void run() {
                testQ5();
            }
        });
        TEST_REGISTRY.put("q62", new Runnable() {
            @Override
            public void run() {
                testQ62();
            }
        });
        TEST_REGISTRY.put("q64", new Runnable() {
            @Override
            public void run() {
                testQ64();
            }
        });
        TEST_REGISTRY.put("q72", new Runnable() {
            @Override
            public void run() {
                testQ72();
            }
        });
        TEST_REGISTRY.put("q1143", new Runnable() {
            @Override
            public void run() {
                testQ1143();
            }
        });
    }

    public static void main(String[] args) {
        if (args.length == 0 || "all".equalsIgnoreCase(args[0])) {
            for (Map.Entry<String, Runnable> entry : TEST_REGISTRY.entrySet()) {
                entry.getValue().run();
                System.out.println(entry.getKey() + " passed");
            }
            System.out.println("all multidimensional DP tests passed");
            return;
        }

        Runnable test = TEST_REGISTRY.get(args[0].toLowerCase());
        if (test == null) {
            throw new IllegalArgumentException("unknown target: " + args[0]
                    + ", supported: q5, q62, q64, q72, q1143, all");
        }
        test.run();
        System.out.println(args[0].toLowerCase() + " passed");
    }

    private static void testQ62() {
        Q62_UniquePaths.OriginalSolution original = new Q62_UniquePaths.OriginalSolution();
        Q62_UniquePaths.SpaceOptimizedSolution optimized
                = new Q62_UniquePaths.SpaceOptimizedSolution();

        // 穷举小棋盘，递归基准直接枚举所有向右/向下路径。
        for (int m = 1; m <= 8; m++) {
            for (int n = 1; n <= 8; n++) {
                int expected = uniquePathsBrute(m, n, 0, 0);
                assertEquals(expected, original.uniquePaths(m, n), "Q62 original");
                assertEquals(expected, optimized.uniquePaths(m, n), "Q62 optimized");
            }
        }
    }

    private static int uniquePathsBrute(int m, int n, int row, int column) {
        if (row == m - 1 && column == n - 1) {
            return 1;
        }
        int ans = 0;
        if (row + 1 < m) {
            ans += uniquePathsBrute(m, n, row + 1, column);
        }
        if (column + 1 < n) {
            ans += uniquePathsBrute(m, n, row, column + 1);
        }
        return ans;
    }

    private static void testQ64() {
        Q64_MinimumPathSum.OriginalSolution original = new Q64_MinimumPathSum.OriginalSolution();
        Q64_MinimumPathSum.SpaceOptimizedSolution optimized
                = new Q64_MinimumPathSum.SpaceOptimizedSolution();
        Random random = new Random(RANDOM_SEED);

        // 随机覆盖1行、1列、正方形和矩形；递归基准枚举全部合法路径。
        for (int time = 0; time < RANDOM_TIMES; time++) {
            int m = 1 + random.nextInt(5);
            int n = 1 + random.nextInt(5);
            int[][] grid = new int[m][n];
            for (int row = 0; row < m; row++) {
                for (int column = 0; column < n; column++) {
                    grid[row][column] = random.nextInt(10);
                }
            }

            int expected = minPathSumBrute(grid, 0, 0);
            assertEquals(expected, original.minPathSum(grid), "Q64 original");
            assertEquals(expected, optimized.minPathSum(grid), "Q64 optimized");
        }
    }

    private static int minPathSumBrute(int[][] grid, int row, int column) {
        if (row == grid.length - 1 && column == grid[0].length - 1) {
            return grid[row][column];
        }
        int next = Integer.MAX_VALUE;
        if (row + 1 < grid.length) {
            next = Math.min(next, minPathSumBrute(grid, row + 1, column));
        }
        if (column + 1 < grid[0].length) {
            next = Math.min(next, minPathSumBrute(grid, row, column + 1));
        }
        return grid[row][column] + next;
    }

    private static void testQ1143() {
        Q1143_LongestCommonSubsequence.OriginalSolution original
                = new Q1143_LongestCommonSubsequence.OriginalSolution();
        Q1143_LongestCommonSubsequence.SpaceOptimizedSolution optimized
                = new Q1143_LongestCommonSubsequence.SpaceOptimizedSolution();
        Random random = new Random(RANDOM_SEED);

        // 随机覆盖空串、完全相同、完全不同和含重复字符的双序列。
        for (int time = 0; time < RANDOM_TIMES; time++) {
            String first = randomString(random, random.nextInt(8));
            String second = randomString(random, random.nextInt(8));
            int expected = lcsBrute(first.toCharArray(), second.toCharArray(), 0, 0);
            assertEquals(expected, original.longestCommonSubsequence(first, second),
                    "Q1143 original");
            assertEquals(expected, optimized.longestCommonSubsequence(first, second),
                    "Q1143 optimized");
        }
    }

    private static int lcsBrute(char[] first, char[] second, int i, int j) {
        if (i == first.length || j == second.length) {
            return 0;
        }
        if (first[i] == second[j]) {
            return 1 + lcsBrute(first, second, i + 1, j + 1);
        }
        return Math.max(lcsBrute(first, second, i + 1, j),
                lcsBrute(first, second, i, j + 1));
    }

    private static void testQ72() {
        Q72_EditDistance.OriginalSolution original = new Q72_EditDistance.OriginalSolution();
        Q72_EditDistance.RecommendedSolution recommended
                = new Q72_EditDistance.RecommendedSolution();
        Random random = new Random(RANDOM_SEED);

        // 随机覆盖空串，并使用独立记忆化递归枚举插入、删除、替换三种最后一步。
        for (int time = 0; time < RANDOM_TIMES; time++) {
            String source = randomString(random, random.nextInt(7));
            String target = randomString(random, random.nextInt(7));
            int expected = editDistanceBrute(source.toCharArray(), target.toCharArray(),
                    0, 0, new HashMap<String, Integer>());
            assertEquals(expected, original.minDistance(source, target), "Q72 original");
            assertEquals(expected, recommended.minDistance(source, target),
                    "Q72 recommended");
        }
    }

    private static int editDistanceBrute(char[] source, char[] target, int i, int j,
                                         Map<String, Integer> memo) {
        if (i == source.length) {
            return target.length - j;
        }
        if (j == target.length) {
            return source.length - i;
        }

        String key = i + "|" + j;
        Integer cached = memo.get(key);
        if (cached != null) {
            return cached;
        }

        int ans;
        if (source[i] == target[j]) {
            ans = editDistanceBrute(source, target, i + 1, j + 1, memo);
        } else {
            int replace = editDistanceBrute(source, target, i + 1, j + 1, memo);
            int delete = editDistanceBrute(source, target, i + 1, j, memo);
            int insert = editDistanceBrute(source, target, i, j + 1, memo);
            ans = 1 + Math.min(replace, Math.min(delete, insert));
        }
        memo.put(key, ans);
        return ans;
    }

    private static void testQ5() {
        Q5_LongestPalindromicSubstring.ManacherSolution manacher
                = new Q5_LongestPalindromicSubstring.ManacherSolution();
        Q5_LongestPalindromicSubstring.IntervalDpSolution intervalDp
                = new Q5_LongestPalindromicSubstring.IntervalDpSolution();
        Random random = new Random(RANDOM_SEED);

        String[] fixed = {"a", "aa", "ab", "babad", "cbbd", "aaaa", "abacdfgdcaba"};
        for (String value : fixed) {
            assertLongestPalindrome(value, manacher.longestPalindrome(value), "Q5 Manacher");
            assertLongestPalindrome(value, intervalDp.longestPalindrome(value), "Q5 interval DP");
        }

        // 最长回文可能有多个，因此不直接比较字符串，而是验证结果合法性和最优长度。
        for (int time = 0; time < RANDOM_TIMES; time++) {
            String value = randomString(random, 1 + random.nextInt(10));
            assertLongestPalindrome(value, manacher.longestPalindrome(value), "Q5 Manacher");
            assertLongestPalindrome(value, intervalDp.longestPalindrome(value), "Q5 interval DP");
        }
    }

    private static void assertLongestPalindrome(String source, String actual, String scene) {
        if (!source.contains(actual) || !isPalindrome(actual)) {
            throw new AssertionError(scene + " returned invalid substring: source="
                    + source + ", actual=" + actual);
        }
        int expectedLength = longestPalindromeLengthBrute(source);
        if (actual.length() != expectedLength) {
            throw new AssertionError(scene + " length mismatch: source=" + source
                    + ", expected=" + expectedLength + ", actual=" + actual);
        }
    }

    private static int longestPalindromeLengthBrute(String value) {
        int maxLength = 0;
        for (int left = 0; left < value.length(); left++) {
            for (int right = left; right < value.length(); right++) {
                String current = value.substring(left, right + 1);
                if (isPalindrome(current)) {
                    maxLength = Math.max(maxLength, current.length());
                }
            }
        }
        return maxLength;
    }

    private static boolean isPalindrome(String value) {
        int left = 0;
        int right = value.length() - 1;
        while (left < right) {
            if (value.charAt(left++) != value.charAt(right--)) {
                return false;
            }
        }
        return true;
    }

    private static String randomString(Random random, int length) {
        char[] chars = new char[length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('a' + random.nextInt(3));
        }
        return new String(chars);
    }

    private static void assertEquals(int expected, int actual, String scene) {
        if (expected != actual) {
            throw new AssertionError(scene + " mismatch: expected=" + expected
                    + ", actual=" + actual);
        }
    }
}
