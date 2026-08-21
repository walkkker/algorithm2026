package frequence.dp;

import frequence.dp.multidimensional.Q115_DistinctSubsequences;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Q115纯计数DP与Q673最优值+计数DP的对数器。
 *
 * <p>默认运行全部测试，也可以通过main参数指定{@code q115}或{@code q673}。
 */
public class CountingDpDifferentialTest {

    private static final long RANDOM_SEED = 20260805L;
    private static final int RANDOM_TIMES = 5000;
    private static final Map<String, Runnable> TEST_REGISTRY = new HashMap<String, Runnable>();

    static {
        TEST_REGISTRY.put("q115", new Runnable() {
            @Override
            public void run() {
                testQ115();
            }
        });
        TEST_REGISTRY.put("q673", new Runnable() {
            @Override
            public void run() {
                testQ673();
            }
        });
    }

    public static void main(String[] args) {
        if (args.length == 0 || "all".equalsIgnoreCase(args[0])) {
            for (Map.Entry<String, Runnable> entry : TEST_REGISTRY.entrySet()) {
                entry.getValue().run();
                System.out.println(entry.getKey() + " passed");
            }
            System.out.println("all counting DP tests passed");
            return;
        }

        Runnable test = TEST_REGISTRY.get(args[0].toLowerCase());
        if (test == null) {
            throw new IllegalArgumentException("unknown target: " + args[0]
                    + ", supported: q115, q673, all");
        }
        test.run();
        System.out.println(args[0].toLowerCase() + " passed");
    }

    private static void testQ115() {
        Q115_DistinctSubsequences.RecommendedSolution twoDimensional
                = new Q115_DistinctSubsequences.RecommendedSolution();
        Q115_DistinctSubsequences.SpaceOptimizedSolution optimized
                = new Q115_DistinctSubsequences.SpaceOptimizedSolution();
        Random random = new Random(RANDOM_SEED);

        String[][] fixedCases = {
                {"", ""},
                {"abc", ""},
                {"", "a"},
                {"rabbbit", "rabbit"},
                {"babgbag", "bag"},
                {"aaa", "aa"}
        };
        for (String[] oneCase : fixedCases) {
            checkQ115(oneCase[0], oneCase[1], twoDimensional, optimized);
        }

        // 小字符串使用独立递归枚举“选择/跳过当前source字符”的全部方案。
        for (int time = 0; time < RANDOM_TIMES; time++) {
            String source = randomString(random, random.nextInt(11));
            String target = randomString(random, random.nextInt(7));
            checkQ115(source, target, twoDimensional, optimized);
        }
    }

    private static void checkQ115(
            String source,
            String target,
            Q115_DistinctSubsequences.RecommendedSolution twoDimensional,
            Q115_DistinctSubsequences.SpaceOptimizedSolution optimized) {

        int expected = distinctSubsequenceBrute(
                source.toCharArray(), target.toCharArray(), 0, 0);
        assertEquals(expected, twoDimensional.numDistinct(source, target),
                "Q115 two-dimensional, source=" + source + ", target=" + target);
        assertEquals(expected, optimized.numDistinct(source, target),
                "Q115 optimized, source=" + source + ", target=" + target);
    }

    private static int distinctSubsequenceBrute(
            char[] source, char[] target, int sourceIndex, int targetIndex) {

        if (targetIndex == target.length) {
            return 1;
        }
        if (sourceIndex == source.length
                || source.length - sourceIndex < target.length - targetIndex) {
            return 0;
        }

        int ans = distinctSubsequenceBrute(
                source, target, sourceIndex + 1, targetIndex);
        if (source[sourceIndex] == target[targetIndex]) {
            ans += distinctSubsequenceBrute(
                    source, target, sourceIndex + 1, targetIndex + 1);
        }
        return ans;
    }

    private static void testQ673() {
        Q673_NumberOfLongestIncreasingSubsequence solution
                = new Q673_NumberOfLongestIncreasingSubsequence();
        Random random = new Random(RANDOM_SEED);

        int[][] fixedCases = {
                {1},
                {1, 3, 5, 4, 7},
                {2, 2, 2, 2, 2},
                {1, 2, 4, 3, 5, 4, 7, 2},
                {5, 4, 3, 2, 1}
        };
        for (int[] nums : fixedCases) {
            assertEquals(numberOfLisBrute(nums), solution.findNumberOfLIS(nums),
                    "Q673 fixed");
        }

        // 位掩码枚举全部非空下标子序列，独立统计最长长度及其方案数量。
        for (int time = 0; time < RANDOM_TIMES; time++) {
            int n = 1 + random.nextInt(11);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = random.nextInt(9) - 4;
            }
            assertEquals(numberOfLisBrute(nums), solution.findNumberOfLIS(nums),
                    "Q673 random");
        }
    }

    private static int numberOfLisBrute(int[] nums) {
        int maxLength = 0;
        int count = 0;
        int totalMasks = 1 << nums.length;

        for (int mask = 1; mask < totalMasks; mask++) {
            int previous = 0;
            int length = 0;
            boolean hasPrevious = false;
            boolean increasing = true;

            for (int index = 0; index < nums.length; index++) {
                if ((mask & (1 << index)) == 0) {
                    continue;
                }
                if (hasPrevious && previous >= nums[index]) {
                    increasing = false;
                    break;
                }
                previous = nums[index];
                hasPrevious = true;
                length++;
            }

            if (!increasing) {
                continue;
            }
            if (length > maxLength) {
                maxLength = length;
                count = 1;
            } else if (length == maxLength) {
                count++;
            }
        }
        return count;
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
            throw new AssertionError(scene + ": expected=" + expected + ", actual=" + actual);
        }
    }
}
