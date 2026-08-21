package frequence;

import frequence.Heap.Q295_FindMedianFromDataStream;
import frequence.Stack.Q155_MinStack;
import frequence.Stack.Q20_ValidParentheses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Hot 100三个原空壳文件的对数器，验证从LeetCode同步的实现。
 *
 * <p>覆盖内容：
 * <ul>
 *     <li>Q20：合法、类型不匹配、顺序错误和空串；</li>
 *     <li>Q155：随机push/pop/top/getMin与线性容器比较；</li>
 *     <li>Q295：每次插入后都与排序结果的中位数比较。</li>
 * </ul>
 */
public class Hot100PlaceholderDifferentialTest {

    private static final int TEST_TIMES = 3_000;
    private static final Random RANDOM = new Random(20260806L);

    public static void main(String[] args) {
        testValidParentheses();
        testMinStack();
        testMedianFinder();
        System.out.println("Hot 100 placeholder differential test passed.");
    }

    private static void testValidParentheses() {
        Q20_ValidParentheses solution = new Q20_ValidParentheses();
        assertBoolean(true, solution.isValid(""), "Q20 empty string");
        assertBoolean(true, solution.isValid("()[]{}"), "Q20 independent pairs");
        assertBoolean(true, solution.isValid("{[()]}"), "Q20 nested pairs");
        assertBoolean(false, solution.isValid("(]"), "Q20 type mismatch");
        assertBoolean(false, solution.isValid("([)]"), "Q20 wrong order");
        assertBoolean(false, solution.isValid("]"), "Q20 missing left bracket");
        assertBoolean(false, solution.isValid("(("), "Q20 missing right bracket");
    }

    private static void testMinStack() {
        Q155_MinStack.MinStack actual = new Q155_MinStack.MinStack();
        List<Integer> expected = new ArrayList<>();
        for (int test = 0; test < TEST_TIMES; test++) {
            if (expected.isEmpty() || RANDOM.nextBoolean()) {
                int value = RANDOM.nextInt(200_001) - 100_000;
                actual.push(value);
                expected.add(value);
            } else {
                assertEquals(expected.get(expected.size() - 1), actual.top(), "Q155 top");
                assertEquals(Collections.min(expected), actual.getMin(), "Q155 min");
                actual.pop();
                expected.remove(expected.size() - 1);
            }
            if (!expected.isEmpty()) {
                assertEquals(expected.get(expected.size() - 1), actual.top(), "Q155 top after operation");
                assertEquals(Collections.min(expected), actual.getMin(), "Q155 min after operation");
            }
        }
    }

    private static void testMedianFinder() {
        Q295_FindMedianFromDataStream.MedianFinder actual =
                new Q295_FindMedianFromDataStream.MedianFinder();
        List<Integer> expected = new ArrayList<>();
        for (int test = 0; test < TEST_TIMES; test++) {
            int value = RANDOM.nextInt(200_001) - 100_000;
            actual.addNum(value);
            expected.add(value);
            Collections.sort(expected);
            double expectedMedian;
            int size = expected.size();
            if ((size & 1) == 1) {
                expectedMedian = expected.get(size / 2);
            } else {
                expectedMedian = ((double) expected.get(size / 2 - 1)
                        + expected.get(size / 2)) / 2;
            }
            double actualMedian = actual.findMedian();
            if (Double.compare(expectedMedian, actualMedian) != 0) {
                throw new AssertionError("Q295 failed, size=" + size
                        + ", expected=" + expectedMedian + ", actual=" + actualMedian);
            }
        }
    }

    private static void assertBoolean(boolean expected, boolean actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ", expected=" + expected + ", actual=" + actual);
        }
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ", expected=" + expected + ", actual=" + actual);
        }
    }
}
