package frequence.双指针.同向读写双指针_原地稳定压缩;

import java.util.Arrays;

/**
 * “快慢指针 + 原地稳定压缩”四个模板的功能测试。
 */
public class StableCompactionTest {

    public static void main(String[] args) {
        testMoveZeroes();
        testRemoveElement();
        testRemoveDuplicates();
        testRetainByCondition();
        System.out.println("StableCompactionTest passed");
    }

    private static void testMoveZeroes() {
        int[] nums = {0, 1, 0, 3, 12};
        new Q283_MoveZeroes().moveZeroes(nums);
        assertArrayEquals(new int[]{1, 3, 12, 0, 0}, nums, "moveZeroes");
    }

    private static void testRemoveElement() {
        int[] nums = {3, 2, 2, 3};
        int length = new Q27_RemoveElement().removeElement(nums, 3);
        assertEquals(2, length, "removeElement length");
        assertPrefixEquals(new int[]{2, 2}, nums, length, "removeElement prefix");
    }

    private static void testRemoveDuplicates() {
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int length = new Q26_RemoveDuplicatesFromSortedArray().removeDuplicates(nums);
        assertEquals(5, length, "removeDuplicates length");
        assertPrefixEquals(new int[]{0, 1, 2, 3, 4}, nums, length, "removeDuplicates prefix");
    }

    private static void testRetainByCondition() {
        int[] nums = {5, 2, 7, 4, 6, 9};
        int length = RetainElementsByCondition.retain(nums, value -> (value & 1) == 0);
        assertEquals(3, length, "retain length");
        assertPrefixEquals(new int[]{2, 4, 6}, nums, length, "retain prefix");
    }

    private static void assertPrefixEquals(int[] expected, int[] actual, int length, String message) {
        int[] prefix = Arrays.copyOf(actual, length);
        assertArrayEquals(expected, prefix, message);
    }

    private static void assertArrayEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message + ": expected=" + Arrays.toString(expected)
                    + ", actual=" + Arrays.toString(actual));
        }
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ": expected=" + expected + ", actual=" + actual);
        }
    }
}
