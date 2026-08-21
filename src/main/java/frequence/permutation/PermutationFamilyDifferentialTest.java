package frequence.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 字典序排列家族对数器。
 *
 * <p>默认执行全部测试，也可以使用参数{@code q31}、{@code q46}、{@code q60}
 * 单独执行一组。
 *
 * <p>基准算法使用独立回溯枚举所有排列，再按字典序排序：
 * <ul>
 *     <li>Q31：与基准排列表中的下一项对比，包括含重复值数组。</li>
 *     <li>Q46：验证生成结果是全部不重复排列，且顺序严格为字典序。</li>
 *     <li>Q60：将每个{@code n}的全部合法{@code k}与回溯基准的第k项对比。</li>
 * </ul>
 */
public class PermutationFamilyDifferentialTest {

    private static final long RANDOM_SEED = 20260806L;
    private static final int RANDOM_TIMES = 500;

    public static void main(String[] args) {
        String target = args.length == 0 ? "all" : args[0].toLowerCase();
        if ("q31".equals(target) || "all".equals(target)) {
            testQ31();
            System.out.println("q31 passed");
        }
        if ("q46".equals(target) || "all".equals(target)) {
            testQ46();
            System.out.println("q46 passed");
        }
        if ("q60".equals(target) || "all".equals(target)) {
            testQ60();
            System.out.println("q60 passed");
        }
        if (!"q31".equals(target) && !"q46".equals(target)
                && !"q60".equals(target) && !"all".equals(target)) {
            throw new IllegalArgumentException(
                    "unknown target: " + target + ", supported: q31, q46, q60, all");
        }
        if ("all".equals(target)) {
            System.out.println("all permutation family tests passed");
        }
    }

    private static void testQ31() {
        Q31_NextPermutation.Solution solution = new Q31_NextPermutation.Solution();

        assertNext(solution, new int[0], new int[0]);
        assertNext(solution, new int[]{1}, new int[]{1});
        assertNext(solution, new int[]{1, 2, 3}, new int[]{1, 3, 2});
        assertNext(solution, new int[]{3, 2, 1}, new int[]{1, 2, 3});
        assertNext(solution, new int[]{1, 1, 5}, new int[]{1, 5, 1});
        assertNext(solution, new int[]{1, 5, 1}, new int[]{5, 1, 1});
        assertNext(solution, new int[]{2, 3, 1, 3, 3}, new int[]{2, 3, 3, 1, 3});

        Random random = new Random(RANDOM_SEED);
        for (int time = 0; time < RANDOM_TIMES; time++) {
            int length = random.nextInt(8);
            int[] input = new int[length];
            for (int i = 0; i < length; i++) {
                input[i] = random.nextInt(4);
            }

            List<List<Integer>> expectedOrder = bruteUniquePermutations(input);
            int currentIndex = findPermutation(expectedOrder, input);
            int nextIndex = (currentIndex + 1) % expectedOrder.size();
            int[] expected = toArray(expectedOrder.get(nextIndex));
            int[] actual = input.clone();
            solution.nextPermutation(actual);
            assertArrayEquals(expected, actual, "Q31 random input=" + Arrays.toString(input));
        }
    }

    private static void assertNext(Q31_NextPermutation.Solution solution,
                                   int[] input,
                                   int[] expected) {
        int[] actual = input.clone();
        solution.nextPermutation(actual);
        assertArrayEquals(expected, actual, "Q31 fixed input=" + Arrays.toString(input));
    }

    private static void testQ46() {
        Q46_PermutationsByLexicographicOrder.Solution solution
                = new Q46_PermutationsByLexicographicOrder.Solution();
        Random random = new Random(RANDOM_SEED);

        for (int time = 0; time < RANDOM_TIMES; time++) {
            int length = random.nextInt(8);
            int[] input = new int[length];
            for (int i = 0; i < length; i++) {
                input[i] = random.nextInt(4);
            }
            int[] original = input.clone();

            List<List<Integer>> expected = bruteUniquePermutations(input);
            List<List<Integer>> actual = solution.permute(input);
            assertListEquals(expected, actual, "Q46 input=" + Arrays.toString(input));
            assertArrayEquals(original, input, "Q46 must not modify caller input");
        }
    }

    private static void testQ60() {
        Q60_PermutationSequence.Solution solution = new Q60_PermutationSequence.Solution();

        assertStringEquals("123456789", solution.getPermutation(9, 1),
                "Q60 n=9 first permutation");
        assertStringEquals("987654321", solution.getPermutation(9, 362880),
                "Q60 n=9 last permutation");

        for (int n = 1; n <= 8; n++) {
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = i + 1;
            }
            List<List<Integer>> expectedOrder = bruteUniquePermutations(nums);
            for (int k = 1; k <= expectedOrder.size(); k++) {
                String expected = join(expectedOrder.get(k - 1));
                String actual = solution.getPermutation(n, k);
                if (!expected.equals(actual)) {
                    throw new AssertionError("Q60 n=" + n + ", k=" + k
                            + ", expected=" + expected + ", actual=" + actual);
                }
            }
        }
    }

    private static List<List<Integer>> bruteUniquePermutations(int[] nums) {
        List<List<Integer>> all = new ArrayList<List<Integer>>();
        boolean[] used = new boolean[nums.length];
        collectPermutations(nums, used, new ArrayList<Integer>(), all);

        Set<List<Integer>> unique = new HashSet<List<Integer>>(all);
        List<List<Integer>> result = new ArrayList<List<Integer>>(unique);
        result.sort((first, second) -> compareLexicographically(first, second));
        return result;
    }

    private static void collectPermutations(int[] nums,
                                            boolean[] used,
                                            List<Integer> path,
                                            List<List<Integer>> ans) {
        if (path.size() == nums.length) {
            ans.add(new ArrayList<Integer>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            path.add(nums[i]);
            collectPermutations(nums, used, path, ans);
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }

    private static int compareLexicographically(List<Integer> first, List<Integer> second) {
        for (int i = 0; i < first.size(); i++) {
            int compare = Integer.compare(first.get(i), second.get(i));
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(first.size(), second.size());
    }

    private static int findPermutation(List<List<Integer>> permutations, int[] target) {
        for (int i = 0; i < permutations.size(); i++) {
            if (Arrays.equals(toArray(permutations.get(i)), target)) {
                return i;
            }
        }
        throw new AssertionError("permutation not found: " + Arrays.toString(target));
    }

    private static int[] toArray(List<Integer> values) {
        int[] result = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
    }

    private static String join(List<Integer> values) {
        StringBuilder builder = new StringBuilder();
        for (int value : values) {
            builder.append(value);
        }
        return builder.toString();
    }

    private static void assertArrayEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message + ", expected=" + Arrays.toString(expected)
                    + ", actual=" + Arrays.toString(actual));
        }
    }

    private static void assertListEquals(List<List<Integer>> expected,
                                         List<List<Integer>> actual,
                                         String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ", expected=" + expected + ", actual=" + actual);
        }
    }

    private static void assertStringEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ", expected=" + expected + ", actual=" + actual);
        }
    }
}
