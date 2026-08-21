package frequence.LinkedList;

import frequence.cache.Q460_LFUCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * {@link Q460_LFUCache} 对数器。
 *
 * <p>基准实现使用HashMap保存value、frequency和最后访问时间，每次淘汰时通过遍历找出：
 * 1. frequency最小的节点；
 * 2. frequency相同时，最后访问时间最早的节点。
 *
 * <p>分别验证官方操作流程、容量为0、更新已有key，以及大量随机get/put操作。
 */
public class Q460LFUCacheComparator {

    private static final int RANDOM_CASES = 1_000;
    private static final int OPERATIONS_PER_CASE = 1_000;

    public static void main(String[] args) {
        testStandardSequence();
        testZeroCapacity();
        testUpdateExistingKey();
        randomCompare();
        System.out.println("Q460LFUCacheComparator passed");
    }

    private static void testStandardSequence() {
        Q460_LFUCache cache = new Q460_LFUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1), "get(1)");
        cache.put(3, 3);
        assertEquals(-1, cache.get(2), "key 2 should be evicted");
        assertEquals(3, cache.get(3), "get(3)");
        cache.put(4, 4);
        assertEquals(-1, cache.get(1), "key 1 should be evicted by LRU tie-break");
        assertEquals(3, cache.get(3), "get(3) after tie-break");
        assertEquals(4, cache.get(4), "get(4)");
    }

    private static void testZeroCapacity() {
        Q460_LFUCache cache = new Q460_LFUCache(0);
        cache.put(1, 1);
        assertEquals(-1, cache.get(1), "capacity=0 must not store data");
    }

    private static void testUpdateExistingKey() {
        Q460_LFUCache cache = new Q460_LFUCache(2);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(1, 100);
        cache.put(3, 30);

        assertEquals(100, cache.get(1), "updated key should increase frequency");
        assertEquals(-1, cache.get(2), "lower-frequency key should be evicted");
        assertEquals(30, cache.get(3), "new key should remain");
    }

    private static void randomCompare() {
        Random random = new Random(20260728L);
        for (int test = 1; test <= RANDOM_CASES; test++) {
            int capacity = random.nextInt(9);
            Q460_LFUCache actual = new Q460_LFUCache(capacity);
            ReferenceLFU expected = new ReferenceLFU(capacity);

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
     * 朴素基准：get/put已有key为O(1)，淘汰时遍历全部节点，为O(N)。
     * 它和被测代码采用不同实现，适合作为对数器的正确答案。
     */
    private static class ReferenceLFU {
        private final int capacity;
        private final Map<Integer, ReferenceNode> map = new HashMap<>();
        private long clock;

        ReferenceLFU(int capacity) {
            this.capacity = capacity;
        }

        int get(int key) {
            ReferenceNode node = map.get(key);
            if (node == null) {
                return -1;
            }
            touch(node);
            return node.value;
        }

        void put(int key, int value) {
            if (capacity == 0) {
                return;
            }

            ReferenceNode node = map.get(key);
            if (node != null) {
                node.value = value;
                touch(node);
                return;
            }

            if (map.size() == capacity) {
                ReferenceNode victim = null;
                for (ReferenceNode candidate : map.values()) {
                    if (victim == null
                            || candidate.frequency < victim.frequency
                            || (candidate.frequency == victim.frequency
                            && candidate.lastUsed < victim.lastUsed)) {
                        victim = candidate;
                    }
                }
                map.remove(victim.key);
            }

            ReferenceNode newNode = new ReferenceNode(key, value);
            newNode.lastUsed = ++clock;
            map.put(key, newNode);
        }

        private void touch(ReferenceNode node) {
            node.frequency++;
            node.lastUsed = ++clock;
        }
    }

    private static class ReferenceNode {
        int key;
        int value;
        int frequency = 1;
        long lastUsed;

        ReferenceNode(int key, int value) {
            this.key = key;
            this.value = value;
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
