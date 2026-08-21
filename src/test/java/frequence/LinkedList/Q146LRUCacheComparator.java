package frequence.LinkedList;

import frequence.cache.Q146_LRUCache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * {@link Q146_LRUCache} 对数器。
 *
 * <p>使用开启访问顺序的 {@link LinkedHashMap} 作为基准实现，分别验证：
 * 1. LeetCode标准操作流程；
 * 2. 容量为1时的频繁淘汰；
 * 3. 更新已有key后是否变为MRU；
 * 4. 随机get/put组合下，被测实现与基准实现是否一致。
 */
public class Q146LRUCacheComparator {

    private static final int RANDOM_CASES = 1_000;
    private static final int OPERATIONS_PER_CASE = 1_000;

    public static void main(String[] args) {
        testStandardSequence();
        testCapacityOne();
        testUpdateExistingKey();
        randomCompare();
        System.out.println("Q146LRUCacheComparator passed");
    }

    private static void testStandardSequence() {
        Q146_LRUCache cache = new Q146_LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1), "get(1)");
        cache.put(3, 3);
        assertEquals(-1, cache.get(2), "key 2 should be evicted");
        cache.put(4, 4);
        assertEquals(-1, cache.get(1), "key 1 should be evicted");
        assertEquals(3, cache.get(3), "get(3)");
        assertEquals(4, cache.get(4), "get(4)");
    }

    private static void testCapacityOne() {
        Q146_LRUCache cache = new Q146_LRUCache(1);
        cache.put(1, 10);
        assertEquals(10, cache.get(1), "capacity=1 get existing");
        cache.put(2, 20);
        assertEquals(-1, cache.get(1), "capacity=1 old key should be evicted");
        assertEquals(20, cache.get(2), "capacity=1 new key");
    }

    private static void testUpdateExistingKey() {
        Q146_LRUCache cache = new Q146_LRUCache(2);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(1, 100);
        cache.put(3, 30);

        assertEquals(100, cache.get(1), "updated key should remain");
        assertEquals(-1, cache.get(2), "key 2 should be LRU and evicted");
        assertEquals(30, cache.get(3), "new key should remain");
    }

    private static void randomCompare() {
        Random random = new Random(20260728L);
        for (int test = 1; test <= RANDOM_CASES; test++) {
            int capacity = random.nextInt(8) + 1;
            Q146_LRUCache actual = new Q146_LRUCache(capacity);
            ReferenceLRU expected = new ReferenceLRU(capacity);

            for (int operation = 1; operation <= OPERATIONS_PER_CASE; operation++) {
                int key = random.nextInt(20);
                if (random.nextBoolean()) {
                    int value = random.nextInt(2_001) - 1_000;
                    actual.put(key, value);
                    expected.put(key, value);
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

    /**
     * accessOrder=true表示每次get/put已有key后，该key都会移动到访问顺序末尾。
     * 容量超限时删除迭代器返回的第一个元素，即LRU。
     */
    private static class ReferenceLRU {
        private final int capacity;
        private final LinkedHashMap<Integer, Integer> map =
                new LinkedHashMap<>(16, 0.75F, true);

        ReferenceLRU(int capacity) {
            this.capacity = capacity;
        }

        int get(int key) {
            Integer value = map.get(key);
            return value == null ? -1 : value;
        }

        void put(int key, int value) {
            map.put(key, value);
            if (map.size() > capacity) {
                Iterator<Map.Entry<Integer, Integer>> iterator =
                        map.entrySet().iterator();
                iterator.next();
                iterator.remove();
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
