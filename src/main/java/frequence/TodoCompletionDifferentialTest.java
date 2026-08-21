package frequence;

import frequence.cache.Q460_LFUCacheTemplate;
import frequence.dp.stock.Q122_BestTimeToBuyAndSellStockII;
import frequence.dp.stock.Q123_BestTimeToBuyAndSellStockIII;
import frequence.dp.stock.Q188_BestTimeToBuyAndSellStockIV;
import frequence.dp.stock.Q309_BestTimeToBuyAndSellStockWithCooldown;
import frequence.dp.stock.Q714_BestTimeToBuyAndSellStockWithTransactionFee;
import frequence.双指针.同向读写双指针_原地稳定压缩.Q1047_RemoveAllAdjacentDuplicatesInString;
import frequence.双指针.同向读写双指针_原地稳定压缩.Q1089_DuplicateZeros;
import frequence.双指针.同向读写双指针_原地稳定压缩.Q1209_RemoveAllAdjacentDuplicatesInStringII;
import frequence.双指针.同向读写双指针_原地稳定压缩.Q26_RemoveDuplicatesFromSortedArray;
import frequence.双指针.同向读写双指针_原地稳定压缩.Q27_RemoveElement;
import frequence.双指针.同向读写双指针_原地稳定压缩.Q283_MoveZeroes;
import frequence.双指针.同向读写双指针_原地稳定压缩.Q443_StringCompression;
import frequence.双指针.同向读写双指针_原地稳定压缩.Q80_RemoveDuplicatesFromSortedArrayII;
import frequence.双指针.同向读写双指针_原地稳定压缩.Q88_MergeSortedArray;

import java.util.Arrays;
import java.util.Random;

/**
 * 本轮从LeetCode同步的独立实现对数器。
 *
 * <p>覆盖原地数组压缩、原地字符串栈、股票状态机DP，以及LFU随机get/put操作序列。
 * 每个{@code my...}实现都与同文件标准实现比较，避免只验证少量固定样例。
 */
public class TodoCompletionDifferentialTest {

    private static final int TEST_TIMES = 5_000;
    private static final Random RANDOM = new Random(20260821L);

    public static void main(String[] args) {
        testArrayCompression();
        testStringCompression();
        testStocks();
        testLfu();
        System.out.println("TODO completion differential test passed.");
    }

    private static void testArrayCompression() {
        Q26_RemoveDuplicatesFromSortedArray q26 = new Q26_RemoveDuplicatesFromSortedArray();
        Q27_RemoveElement q27 = new Q27_RemoveElement();
        Q80_RemoveDuplicatesFromSortedArrayII q80 = new Q80_RemoveDuplicatesFromSortedArrayII();
        Q88_MergeSortedArray q88 = new Q88_MergeSortedArray();
        Q283_MoveZeroes q283 = new Q283_MoveZeroes();
        Q1089_DuplicateZeros q1089 = new Q1089_DuplicateZeros();

        for (int test = 0; test < TEST_TIMES; test++) {
            int[] sorted = randomSortedArray(1 + RANDOM.nextInt(20));
            assertPrefixEqual(sorted, q26::myRemoveDuplicates, q26::removeDuplicates, "Q26");
            assertPrefixEqual(sorted, q80::myRemoveDuplicates, q80::removeDuplicates, "Q80");

            int[] raw = randomArray(RANDOM.nextInt(20), 7);
            int val = RANDOM.nextInt(7) - 3;
            int[] q27a = raw.clone();
            int[] q27b = raw.clone();
            int q27LenA = q27.myRemoveElement(q27a, val);
            int q27LenB = q27.removeElement(q27b, val);
            assertArrayEquals(Arrays.copyOf(q27b, q27LenB), Arrays.copyOf(q27a, q27LenA), "Q27");

            int[] left = randomSortedArray(RANDOM.nextInt(12));
            int[] right = randomSortedArray(RANDOM.nextInt(12));
            int[] mergeA = Arrays.copyOf(left, left.length + right.length);
            int[] mergeB = mergeA.clone();
            q88.myMerge(mergeA, left.length, right, right.length);
            q88.merge(mergeB, left.length, right, right.length);
            assertArrayEquals(mergeB, mergeA, "Q88");

            int[] zerosA = randomArray(RANDOM.nextInt(25), 5);
            int[] zerosB = zerosA.clone();
            q283.myMoveZeroes(zerosA);
            q283.moveZeroes(zerosB);
            assertArrayEquals(zerosB, zerosA, "Q283");

            int[] duplicateA = randomArray(RANDOM.nextInt(25), 5);
            int[] duplicateB = duplicateA.clone();
            q1089.myDuplicateZeros(duplicateA);
            q1089.duplicateZeros(duplicateB);
            assertArrayEquals(duplicateB, duplicateA, "Q1089");
        }
    }

    private static void testStringCompression() {
        Q443_StringCompression q443 = new Q443_StringCompression();
        Q1047_RemoveAllAdjacentDuplicatesInString q1047 =
                new Q1047_RemoveAllAdjacentDuplicatesInString();
        Q1209_RemoveAllAdjacentDuplicatesInStringII q1209 =
                new Q1209_RemoveAllAdjacentDuplicatesInStringII();

        for (int test = 0; test < TEST_TIMES; test++) {
            String source = randomString(1 + RANDOM.nextInt(30), 4);
            char[] charsA = source.toCharArray();
            char[] charsB = source.toCharArray();
            int lenA = q443.myCompress(charsA);
            int lenB = q443.compress(charsB);
            assertArrayEquals(Arrays.copyOf(charsB, lenB), Arrays.copyOf(charsA, lenA), "Q443");

            String actual1047 = q1047.myRemoveDuplicates(source);
            String expected1047 = q1047.removeDuplicates(source);
            assertEquals(expected1047, actual1047, "Q1047");

            int k = 2 + RANDOM.nextInt(5);
            String actual1209 = q1209.myRemoveDuplicates(source, k);
            String expected1209 = q1209.removeDuplicates(source, k);
            assertEquals(expected1209, actual1209, "Q1209");
        }
    }

    private static void testStocks() {
        Q122_BestTimeToBuyAndSellStockII q122 = new Q122_BestTimeToBuyAndSellStockII();
        Q123_BestTimeToBuyAndSellStockIII q123 = new Q123_BestTimeToBuyAndSellStockIII();
        Q188_BestTimeToBuyAndSellStockIV q188 = new Q188_BestTimeToBuyAndSellStockIV();
        Q309_BestTimeToBuyAndSellStockWithCooldown q309 =
                new Q309_BestTimeToBuyAndSellStockWithCooldown();
        Q714_BestTimeToBuyAndSellStockWithTransactionFee q714 =
                new Q714_BestTimeToBuyAndSellStockWithTransactionFee();

        for (int test = 0; test < TEST_TIMES; test++) {
            int[] prices = randomPositiveArray(1 + RANDOM.nextInt(12), 20);
            assertEquals(q122.maxProfit(prices), q122.myMaxProfit(prices), "Q122");
            assertEquals(q123.maxProfit(prices), q123.myMaxProfit(prices), "Q123");

            int k = 1 + RANDOM.nextInt(5);
            assertEquals(q188.maxProfit(k, prices), q188.myMaxProfit(k, prices), "Q188");
            assertEquals(q309.maxProfit(prices), q309.myMaxProfit(prices), "Q309");

            int fee = RANDOM.nextInt(6);
            assertEquals(q714.maxProfit(prices, fee), q714.myMaxProfit(prices, fee), "Q714");
        }
    }

    private static void testLfu() {
        for (int test = 0; test < 500; test++) {
            int capacity = RANDOM.nextInt(6);
            Q460_LFUCacheTemplate expected = new Q460_LFUCacheTemplate(capacity);
            Q460_LFUCacheTemplate.MyLFUCache actual =
                    new Q460_LFUCacheTemplate.MyLFUCache(capacity);
            for (int operation = 0; operation < 200; operation++) {
                int key = RANDOM.nextInt(10);
                if (RANDOM.nextBoolean()) {
                    int value = RANDOM.nextInt(1000);
                    expected.put(key, value);
                    actual.put(key, value);
                } else {
                    assertEquals(expected.get(key), actual.get(key), "Q460");
                }
            }
        }
    }

    private static void assertPrefixEqual(int[] source, PrefixOperation actualOperation,
                                          PrefixOperation expectedOperation, String message) {
        int[] actual = source.clone();
        int[] expected = source.clone();
        int actualLength = actualOperation.apply(actual);
        int expectedLength = expectedOperation.apply(expected);
        assertArrayEquals(Arrays.copyOf(expected, expectedLength),
                Arrays.copyOf(actual, actualLength), message);
    }

    private static int[] randomArray(int length, int bound) {
        int[] ans = new int[length];
        for (int i = 0; i < length; i++) {
            ans[i] = RANDOM.nextInt(bound) - bound / 2;
        }
        return ans;
    }

    private static int[] randomPositiveArray(int length, int bound) {
        int[] ans = new int[length];
        for (int i = 0; i < length; i++) {
            ans[i] = RANDOM.nextInt(bound + 1);
        }
        return ans;
    }

    private static int[] randomSortedArray(int length) {
        int[] ans = randomArray(length, 9);
        Arrays.sort(ans);
        return ans;
    }

    private static String randomString(int length, int alphabetSize) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = (char) ('a' + RANDOM.nextInt(alphabetSize));
        }
        return new String(chars);
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ", expected=" + expected + ", actual=" + actual);
        }
    }

    private static void assertEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ", expected=" + expected + ", actual=" + actual);
        }
    }

    private static void assertArrayEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message + ", expected=" + Arrays.toString(expected)
                    + ", actual=" + Arrays.toString(actual));
        }
    }

    private static void assertArrayEquals(char[] expected, char[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message + ", expected=" + Arrays.toString(expected)
                    + ", actual=" + Arrays.toString(actual));
        }
    }

    @FunctionalInterface
    private interface PrefixOperation {
        int apply(int[] nums);
    }
}
