package frequence.skill;

import frequence.permutation.Q31_NextPermutation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Hot 100“技巧”章节对数器。
 *
 * <p>覆盖五道题：
 * <ul>
 *     <li>Q136：随机生成“一个数出现一次、其他数出现两次”的数组，与HashSet基准比较。</li>
 *     <li>Q169：随机生成存在严格多数元素的数组，与词频统计基准比较。</li>
 *     <li>Q75：随机生成只含0、1、2的数组，与Arrays.sort结果比较。</li>
 *     <li>Q31：穷举小规模排列，与“排序后枚举字典序”的基准比较。</li>
 *     <li>Q287：随机生成[1,n]值域内只有一个重复整数的数组，与Set基准比较。</li>
 * </ul>
 */
public class SkillChapterDifferentialTest {

    private static final int RANDOM_TEST_TIMES = 5_000;
    private static final Random RANDOM = new Random(20260806L);

    public static void main(String[] args) {
        testSingleNumber();
        testMajorityElement();
        testSortColors();
        testNextPermutation();
        testFindDuplicate();
        System.out.println("Hot 100 skill chapter differential test passed.");
    }

    private static void testSingleNumber() {
        Q136_SingleNumber.Solution solution = new Q136_SingleNumber.Solution();
        for (int test = 0; test < RANDOM_TEST_TIMES; test++) {
            int pairKinds = RANDOM.nextInt(20);
            Set<Integer> used = new HashSet<>();
            int single = nextUnused(used);
            int[] nums = new int[pairKinds * 2 + 1];
            nums[0] = single;
            int index = 1;
            for (int i = 0; i < pairKinds; i++) {
                int value = nextUnused(used);
                nums[index++] = value;
                nums[index++] = value;
            }
            shuffle(nums);
            assertEquals(single, solution.singleNumber(nums), "Q136", nums);
        }
    }

    private static void testMajorityElement() {
        Q169_MajorityElement.Solution solution = new Q169_MajorityElement.Solution();
        for (int test = 0; test < RANDOM_TEST_TIMES; test++) {
            int length = RANDOM.nextInt(60) + 1;
            int majority = RANDOM.nextInt(101) - 50;
            int majorityCount = length / 2 + 1 + RANDOM.nextInt(length - length / 2);
            int[] nums = new int[length];
            Arrays.fill(nums, 0, majorityCount, majority);
            for (int i = majorityCount; i < length; i++) {
                int value;
                do {
                    value = RANDOM.nextInt(101) - 50;
                } while (value == majority);
                nums[i] = value;
            }
            shuffle(nums);
            assertEquals(majorityByCount(nums), solution.majorityElement(nums), "Q169", nums);
        }
    }

    private static void testSortColors() {
        Q75_SortColors.Solution solution = new Q75_SortColors.Solution();
        for (int test = 0; test < RANDOM_TEST_TIMES; test++) {
            int[] actual = new int[RANDOM.nextInt(80)];
            for (int i = 0; i < actual.length; i++) {
                actual[i] = RANDOM.nextInt(3);
            }
            int[] expected = Arrays.copyOf(actual, actual.length);
            Arrays.sort(expected);
            solution.sortColors(actual);
            if (!Arrays.equals(expected, actual)) {
                throw new AssertionError("Q75 failed, expected=" + Arrays.toString(expected)
                        + ", actual=" + Arrays.toString(actual));
            }
        }
    }

    private static void testNextPermutation() {
        Q31_NextPermutation.Solution solution = new Q31_NextPermutation.Solution();
        int[][] cases = {
                {1},
                {1, 2, 3},
                {3, 2, 1},
                {1, 1, 5},
                {1, 3, 5, 4, 2},
                {2, 2, 0, 1}
        };
        for (int[] source : cases) {
            int[] expected = bruteNextPermutation(source);
            int[] actual = Arrays.copyOf(source, source.length);
            solution.nextPermutation(actual);
            if (!Arrays.equals(expected, actual)) {
                throw new AssertionError("Q31 failed, input=" + Arrays.toString(source)
                        + ", expected=" + Arrays.toString(expected)
                        + ", actual=" + Arrays.toString(actual));
            }
        }
    }

    private static void testFindDuplicate() {
        Q287_FindTheDuplicateNumber.Solution solution = new Q287_FindTheDuplicateNumber.Solution();
        int[] repeatedMoreThanTwice = {2, 2, 2, 2, 2};
        assertEquals(2, solution.findDuplicate(repeatedMoreThanTwice),
                "Q287 repeated more than twice", repeatedMoreThanTwice);
        for (int test = 0; test < RANDOM_TEST_TIMES; test++) {
            int n = RANDOM.nextInt(80) + 1;
            int duplicate = RANDOM.nextInt(n) + 1;
            int[] nums = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                nums[i - 1] = i;
            }
            nums[n] = duplicate;
            shuffle(nums);
            assertEquals(duplicateBySet(nums), solution.findDuplicate(nums), "Q287", nums);
        }
    }

    private static int nextUnused(Set<Integer> used) {
        int value;
        do {
            value = RANDOM.nextInt();
        } while (!used.add(value));
        return value;
    }

    private static int majorityByCount(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            int current = count.getOrDefault(num, 0) + 1;
            count.put(num, current);
            if (current > nums.length / 2) {
                return num;
            }
        }
        throw new AssertionError("No majority element: " + Arrays.toString(nums));
    }

    private static int duplicateBySet(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) {
                return num;
            }
        }
        throw new AssertionError("No duplicate: " + Arrays.toString(nums));
    }

    private static int[] bruteNextPermutation(int[] nums) {
        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted);
        int[] candidate = Arrays.copyOf(sorted, sorted.length);
        do {
            if (compare(candidate, nums) > 0) {
                return Arrays.copyOf(candidate, candidate.length);
            }
        } while (nextPermutationForOracle(candidate));
        return sorted;
    }

    private static boolean nextPermutationForOracle(int[] nums) {
        int pivot = nums.length - 2;
        while (pivot >= 0 && nums[pivot] >= nums[pivot + 1]) {
            pivot--;
        }
        if (pivot < 0) {
            return false;
        }
        int successor = nums.length - 1;
        while (nums[successor] <= nums[pivot]) {
            successor--;
        }
        swap(nums, pivot, successor);
        reverse(nums, pivot + 1, nums.length - 1);
        return true;
    }

    private static int compare(int[] first, int[] second) {
        for (int i = 0; i < first.length; i++) {
            int compare = Integer.compare(first[i], second[i]);
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    private static void shuffle(int[] nums) {
        for (int i = nums.length - 1; i > 0; i--) {
            swap(nums, i, RANDOM.nextInt(i + 1));
        }
    }

    private static void reverse(int[] nums, int left, int right) {
        while (left < right) {
            swap(nums, left++, right--);
        }
    }

    private static void swap(int[] nums, int first, int second) {
        int temp = nums[first];
        nums[first] = nums[second];
        nums[second] = temp;
    }

    private static void assertEquals(int expected, int actual, String problem, int[] nums) {
        if (expected != actual) {
            throw new AssertionError(problem + " failed, input=" + Arrays.toString(nums)
                    + ", expected=" + expected + ", actual=" + actual);
        }
    }
}
