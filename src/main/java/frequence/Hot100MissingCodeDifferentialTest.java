package frequence;

import frequence.Greedy.Q45_JumpGameII;
import frequence.Greedy.Q55_JumpGame;
import frequence.Greedy.Q763_PartitionLabels;
import frequence.array.Q560_SubarraySumEqualsK;
import frequence.matrix.Q240_SearchA2DMatrixII;
import frequence.双指针.SlidingWindow.Q239_SlidingWindowMaximum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Hot 100六个补录文件的对数器，验证从LeetCode复制到项目后的实现没有发生偏差。
 */
public class Hot100MissingCodeDifferentialTest {

    private static final int TEST_TIMES = 3_000;
    private static final Random RANDOM = new Random(20260806L);

    public static void main(String[] args) {
        testSubarraySum();
        testSlidingWindowMaximum();
        testSearchMatrix();
        testJumpGame();
        testJumpGameII();
        testPartitionLabels();
        System.out.println("Hot 100 missing-code differential test passed.");
    }

    private static void testSubarraySum() {
        Q560_SubarraySumEqualsK.Solution solution = new Q560_SubarraySumEqualsK.Solution();
        for (int test = 0; test < TEST_TIMES; test++) {
            int[] nums = randomArray(RANDOM.nextInt(20) + 1, -10, 10);
            int k = RANDOM.nextInt(31) - 15;
            int expected = subarraySumBruteForce(nums, k);
            int actual = solution.subarraySum(nums, k);
            assertEquals(expected, actual, "Q560", nums);
        }
    }

    private static void testSlidingWindowMaximum() {
        Q239_SlidingWindowMaximum.Solution solution = new Q239_SlidingWindowMaximum.Solution();
        for (int test = 0; test < TEST_TIMES; test++) {
            int[] nums = randomArray(RANDOM.nextInt(30) + 1, -50, 50);
            int k = RANDOM.nextInt(nums.length) + 1;
            int[] expected = slidingWindowMaximumBruteForce(nums, k);
            int[] actual = solution.maxSlidingWindow(nums, k);
            if (!Arrays.equals(expected, actual)) {
                throw new AssertionError("Q239 failed, nums=" + Arrays.toString(nums)
                        + ", k=" + k + ", expected=" + Arrays.toString(expected)
                        + ", actual=" + Arrays.toString(actual));
            }
        }
    }

    private static void testSearchMatrix() {
        Q240_SearchA2DMatrixII.Solution solution = new Q240_SearchA2DMatrixII.Solution();
        for (int test = 0; test < TEST_TIMES; test++) {
            int rows = RANDOM.nextInt(12) + 1;
            int columns = RANDOM.nextInt(12) + 1;
            int[][] matrix = increasingMatrix(rows, columns);
            int target = RANDOM.nextInt(rows * columns * 3 + 20) - 10;
            boolean expected = containsBruteForce(matrix, target);
            boolean actual = solution.searchMatrix(matrix, target);
            if (expected != actual) {
                throw new AssertionError("Q240 failed, target=" + target
                        + ", matrix=" + Arrays.deepToString(matrix));
            }
        }
    }

    private static void testJumpGame() {
        Q55_JumpGame.Solution solution = new Q55_JumpGame.Solution();
        for (int test = 0; test < TEST_TIMES; test++) {
            int[] nums = randomArray(RANDOM.nextInt(25) + 1, 0, 8);
            boolean expected = canJumpByDp(nums);
            boolean actual = solution.canJump(nums);
            if (expected != actual) {
                throw new AssertionError("Q55 failed, nums=" + Arrays.toString(nums)
                        + ", expected=" + expected + ", actual=" + actual);
            }
        }
    }

    private static void testJumpGameII() {
        Q45_JumpGameII.Solution solution = new Q45_JumpGameII.Solution();
        for (int test = 0; test < TEST_TIMES; test++) {
            int[] nums = reachableJumpArray(RANDOM.nextInt(24) + 1);
            int expected = minimumJumpsByDp(nums);
            int actual = solution.jump(nums);
            assertEquals(expected, actual, "Q45", nums);
        }
    }

    private static void testPartitionLabels() {
        Q763_PartitionLabels.Solution solution = new Q763_PartitionLabels.Solution();
        for (int test = 0; test < TEST_TIMES; test++) {
            char[] chars = new char[RANDOM.nextInt(40) + 1];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) ('a' + RANDOM.nextInt(8));
            }
            String s = new String(chars);
            List<Integer> expected = partitionLabelsByLastPosition(s);
            List<Integer> actual = solution.partitionLabels(s);
            if (!expected.equals(actual)) {
                throw new AssertionError("Q763 failed, s=" + s
                        + ", expected=" + expected + ", actual=" + actual);
            }
        }
    }

    private static int subarraySumBruteForce(int[] nums, int k) {
        int ans = 0;
        for (int left = 0; left < nums.length; left++) {
            int sum = 0;
            for (int right = left; right < nums.length; right++) {
                sum += nums[right];
                if (sum == k) {
                    ans++;
                }
            }
        }
        return ans;
    }

    private static int[] slidingWindowMaximumBruteForce(int[] nums, int k) {
        int[] ans = new int[nums.length - k + 1];
        for (int left = 0; left + k <= nums.length; left++) {
            int max = nums[left];
            for (int i = left + 1; i < left + k; i++) {
                max = Math.max(max, nums[i]);
            }
            ans[left] = max;
        }
        return ans;
    }

    private static boolean containsBruteForce(int[][] matrix, int target) {
        for (int[] row : matrix) {
            for (int value : row) {
                if (value == target) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean canJumpByDp(int[] nums) {
        boolean[] reachable = new boolean[nums.length];
        reachable[0] = true;
        for (int i = 0; i < nums.length; i++) {
            if (!reachable[i]) {
                continue;
            }
            int end = Math.min(nums.length - 1, i + nums[i]);
            for (int next = i + 1; next <= end; next++) {
                reachable[next] = true;
            }
        }
        return reachable[nums.length - 1];
    }

    private static int minimumJumpsByDp(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 0; i < nums.length; i++) {
            int end = Math.min(nums.length - 1, i + nums[i]);
            for (int next = i + 1; next <= end; next++) {
                dp[next] = Math.min(dp[next], dp[i] + 1);
            }
        }
        return dp[nums.length - 1];
    }

    private static List<Integer> partitionLabelsByLastPosition(String s) {
        int[] last = new int[26];
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            last[chars[i] - 'a'] = i;
        }
        List<Integer> ans = new ArrayList<>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < chars.length; i++) {
            end = Math.max(end, last[chars[i] - 'a']);
            if (i == end) {
                ans.add(end - start + 1);
                start = i + 1;
            }
        }
        return ans;
    }

    private static int[][] increasingMatrix(int rows, int columns) {
        int[][] matrix = new int[rows][columns];
        int value = RANDOM.nextInt(11) - 10;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                int top = row == 0 ? Integer.MIN_VALUE : matrix[row - 1][column];
                int left = column == 0 ? Integer.MIN_VALUE : matrix[row][column - 1];
                value = Math.max(value, Math.max(top, left)) + RANDOM.nextInt(3) + 1;
                matrix[row][column] = value;
            }
        }
        return matrix;
    }

    private static int[] reachableJumpArray(int length) {
        int[] nums = new int[length];
        for (int i = 0; i < length - 1; i++) {
            nums[i] = RANDOM.nextInt(7) + 1;
        }
        return nums;
    }

    private static int[] randomArray(int length, int min, int max) {
        int[] nums = new int[length];
        for (int i = 0; i < length; i++) {
            nums[i] = min + RANDOM.nextInt(max - min + 1);
        }
        return nums;
    }

    private static void assertEquals(int expected, int actual, String problem, int[] nums) {
        if (expected != actual) {
            throw new AssertionError(problem + " failed, nums=" + Arrays.toString(nums)
                    + ", expected=" + expected + ", actual=" + actual);
        }
    }
}
