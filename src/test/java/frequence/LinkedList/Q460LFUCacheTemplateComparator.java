package frequence.LinkedList;

import frequence.cache.Q460_LFUCache;
import frequence.cache.Q460_LFUCacheTemplate;

import java.util.Random;

/**
 * LFU面试模板对数器。
 *
 * <p>使用已经通过独立朴素基准验证的 {@link Q460_LFUCache} 作为正确答案，
 * 对比模板在固定流程和大量随机get/put操作下的结果。
 */
public class Q460LFUCacheTemplateComparator {

    private static final int RANDOM_CASES = 1_000;
    private static final int OPERATIONS_PER_CASE = 1_000;

    public static void main(String[] args) {
        testStandardSequence();
        randomCompare();
        System.out.println("Q460LFUCacheTemplateComparator passed");
    }

    private static void testStandardSequence() {
        Q460_LFUCacheTemplate cache = new Q460_LFUCacheTemplate(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1), "get(1)");
        cache.put(3, 3);
        assertEquals(-1, cache.get(2), "key 2 should be evicted");
        assertEquals(3, cache.get(3), "get(3)");
        cache.put(4, 4);
        assertEquals(-1, cache.get(1), "key 1 should be evicted");
        assertEquals(3, cache.get(3), "get(3) after eviction");
        assertEquals(4, cache.get(4), "get(4)");
    }

    private static void randomCompare() {
        Random random = new Random(20260728L);
        for (int test = 1; test <= RANDOM_CASES; test++) {
            int capacity = random.nextInt(9);
            Q460_LFUCache expected = new Q460_LFUCache(capacity);
            Q460_LFUCacheTemplate actual = new Q460_LFUCacheTemplate(capacity);

            for (int operation = 1; operation <= OPERATIONS_PER_CASE; operation++) {
                int key = random.nextInt(20);
                if (random.nextBoolean()) {
                    int value = random.nextInt(2_001) - 1_000;
                    expected.put(key, value);
                    actual.put(key, value);
                } else {
                    int expectedValue = expected.get(key);
                    int actualValue = actual.get(key);
                    if (expectedValue != actualValue) {
                        throw new AssertionError(
                                "random mismatch: test=" + test
                                        + ", operation=" + operation
                                        + ", capacity=" + capacity
                                        + ", key=" + key
                                        + ", expected=" + expectedValue
                                        + ", actual=" + actualValue
                        );
                    }
                }
            }
        }
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(
                    message + ": expected=" + expected + ", actual=" + actual
            );
        }
    }
}
