package treemap.sbt.SortedSBTWithRepeatedValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * SBTSet、SBTreeMap 和 ValueIndex 重复值方案的统一对数器。
 * 对数器使用 JDK TreeSet、TreeMap 和逐窗口排序作为参照实现。
 */
public class SBTTemplateComparator {

    private static final long SEED = 20260716L;

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void testSet() {
        Random random = new Random(SEED);
        SBTSet<Integer> actual = new SBTSet<>();
        TreeSet<Integer> expected = new TreeSet<>();

        for (int operation = 0; operation < 20_000; operation++) {
            int value = random.nextInt(101) - 50;
            int type = random.nextInt(3);
            if (type == 0) {
                check(actual.add(value) == expected.add(value), "SBTSet add mismatch: " + value);
            } else if (type == 1) {
                check(actual.remove(value) == expected.remove(value), "SBTSet remove mismatch: " + value);
            } else {
                check(actual.contains(value) == expected.contains(value), "SBTSet contains mismatch: " + value);
            }

            check(actual.size() == expected.size(), "SBTSet size mismatch");
            if (operation % 20 == 0) {
                List<Integer> ordered = new ArrayList<>(expected);
                for (int index = 0; index < ordered.size(); index++) {
                    check(actual.getIndexKey(index).equals(ordered.get(index)),
                            "SBTSet index mismatch: " + index);
                }
                for (int query = -55; query <= 55; query++) {
                    int less = 0;
                    int lessOrEqual = 0;
                    for (int num : ordered) {
                        less += num < query ? 1 : 0;
                        lessOrEqual += num <= query ? 1 : 0;
                    }
                    check(actual.countLess(query) == less, "SBTSet countLess mismatch: " + query);
                    check(actual.countLessOrEqual(query) == lessOrEqual,
                            "SBTSet countLessOrEqual mismatch: " + query);
                    check(equal(actual.floor(query), expected.floor(query)), "SBTSet floor mismatch: " + query);
                    check(equal(actual.ceiling(query), expected.ceiling(query)),
                            "SBTSet ceiling mismatch: " + query);
                }
            }
        }
    }

    private static void testInterviewSet() {
        testInterviewCoreCases();

        Random random = new Random(SEED + 3);
        SBTSetInterview<Integer> actual = new SBTSetInterview<>();
        TreeSet<Integer> expected = new TreeSet<>();

        for (int operation = 0; operation < 20_000; operation++) {
            int value = random.nextInt(101) - 50;
            if (random.nextBoolean()) {
                check(actual.add(value) == expected.add(value),
                        "SBTSetInterview add mismatch: " + value);
            } else {
                check(actual.remove(value) == expected.remove(value),
                        "SBTSetInterview remove mismatch: " + value);
            }

            check(actual.size() == expected.size(), "SBTSetInterview size mismatch");
            if (operation % 20 == 0) {
                List<Integer> ordered = new ArrayList<>(expected);
                for (int index = 0; index < ordered.size(); index++) {
                    check(actual.getIndexKey(index).equals(ordered.get(index)),
                            "SBTSetInterview index mismatch: " + index);
                }
                for (int query = -55; query <= 55; query++) {
                    check(actual.countLess(query) == expected.headSet(query, false).size(),
                            "SBTSetInterview countLess mismatch: " + query);
                    check(actual.countLessOrEqual(query) == expected.headSet(query, true).size(),
                            "SBTSetInterview countLessOrEqual mismatch: " + query);
                }
            }
        }
    }

    /** 定向覆盖四种旋转、三种删除和排名边界，失败时比随机用例更容易定位。 */
    private static void testInterviewCoreCases() {
        assertInterviewOrder(new int[]{3, 2, 1}, new int[]{1, 2, 3}); // LL
        assertInterviewOrder(new int[]{3, 1, 2}, new int[]{1, 2, 3}); // LR
        assertInterviewOrder(new int[]{1, 2, 3}, new int[]{1, 2, 3}); // RR
        assertInterviewOrder(new int[]{1, 3, 2}, new int[]{1, 2, 3}); // RL

        SBTSetInterview<Integer> tree = new SBTSetInterview<>();
        for (int value : new int[]{4, 2, 6, 1, 3, 5, 7}) {
            tree.add(value);
        }
        check(tree.remove(1), "interview delete leaf failed");
        check(tree.remove(2), "interview delete one-child node failed");
        check(tree.remove(4), "interview delete two-child root failed");
        check(!tree.remove(100), "interview remove absent key should return false");

        int[] expected = {3, 5, 6, 7};
        check(tree.size() == expected.length, "interview deterministic size mismatch");
        for (int index = 0; index < expected.length; index++) {
            check(tree.getIndexKey(index) == expected[index],
                    "interview deterministic index mismatch: " + index);
        }
        check(tree.countLess(5) == 1, "interview existing-key rank mismatch");
        check(tree.countLess(4) == 1, "interview absent-key rank mismatch");
        check(tree.countLessOrEqual(5) == 2, "interview less-or-equal mismatch");
    }

    private static void assertInterviewOrder(int[] input, int[] expected) {
        SBTSetInterview<Integer> tree = new SBTSetInterview<>();
        for (int value : input) {
            tree.add(value);
        }
        check(tree.size() == expected.length, "interview rotation size mismatch");
        for (int index = 0; index < expected.length; index++) {
            check(tree.getIndexKey(index) == expected[index],
                    "interview rotation order mismatch: " + Arrays.toString(input));
        }
    }

    private static void testMap() {
        Random random = new Random(SEED + 1);
        SBTreeMap<Integer, Integer> actual = new SBTreeMap<>();
        TreeMap<Integer, Integer> expected = new TreeMap<>();

        for (int operation = 0; operation < 20_000; operation++) {
            int key = random.nextInt(101) - 50;
            int value = random.nextInt();
            int type = random.nextInt(4);
            if (type == 0) {
                actual.put(key, value);
                expected.put(key, value);
            } else if (type == 1) {
                boolean expectedRemoved = expected.remove(key) != null;
                check(actual.remove(key) == expectedRemoved, "SBTreeMap remove mismatch: " + key);
            } else if (type == 2) {
                check(equal(actual.get(key), expected.get(key)), "SBTreeMap get mismatch: " + key);
            } else {
                check(actual.containsKey(key) == expected.containsKey(key),
                        "SBTreeMap contains mismatch: " + key);
            }

            check(actual.size() == expected.size(), "SBTreeMap size mismatch");
            if (operation % 20 == 0) {
                List<Integer> orderedKeys = new ArrayList<>(expected.keySet());
                for (int index = 0; index < orderedKeys.size(); index++) {
                    Integer expectedKey = orderedKeys.get(index);
                    check(actual.getIndexKey(index).equals(expectedKey),
                            "SBTreeMap index key mismatch: " + index);
                    check(equal(actual.getIndexValue(index), expected.get(expectedKey)),
                            "SBTreeMap index value mismatch: " + index);
                }
                for (int query = -55; query <= 55; query++) {
                    check(actual.countLessKey(query) == expected.headMap(query, false).size(),
                            "SBTreeMap countLessKey mismatch: " + query);
                    check(actual.countLessOrEqualKey(query) == expected.headMap(query, true).size(),
                            "SBTreeMap countLessOrEqualKey mismatch: " + query);
                    check(equal(actual.floorKey(query), expected.floorKey(query)),
                            "SBTreeMap floor mismatch: " + query);
                    check(equal(actual.ceilingKey(query), expected.ceilingKey(query)),
                            "SBTreeMap ceiling mismatch: " + query);
                }
            }
        }
    }

    private static void testValueIndexSlidingWindow() {
        Random random = new Random(SEED + 2);
        for (int test = 0; test < 5_000; test++) {
            int length = random.nextInt(20) + 1;
            int k = random.nextInt(length) + 1;
            int[] nums = new int[length];
            for (int i = 0; i < length; i++) {
                nums[i] = random.nextInt(11) - 5;
            }

            SBTSet<SBTSet.ValueIndex> tree = new SBTSet<>();
            for (int i = 0; i < k; i++) {
                check(tree.add(new SBTSet.ValueIndex(nums[i], i)), "ValueIndex should be unique");
            }
            for (int left = 0; left + k <= length; left++) {
                double actualMedian;
                if ((k & 1) == 1) {
                    actualMedian = tree.getIndexKey(k / 2).getValue();
                } else {
                    long leftMiddle = tree.getIndexKey(k / 2 - 1).getValue();
                    long rightMiddle = tree.getIndexKey(k / 2).getValue();
                    actualMedian = (leftMiddle + rightMiddle) / 2.0;
                }
                int[] window = Arrays.copyOfRange(nums, left, left + k);
                Arrays.sort(window);
                double expectedMedian = (k & 1) == 1
                        ? window[k / 2]
                        : ((long) window[k / 2 - 1] + window[k / 2]) / 2.0;
                check(Double.compare(actualMedian, expectedMedian) == 0,
                        "ValueIndex median mismatch, nums=" + Arrays.toString(nums) + ", k=" + k);

                for (int value = -6; value <= 6; value++) {
                    int less = tree.countLess(new SBTSet.ValueIndex(value, Long.MIN_VALUE));
                    int lessOrEqual = tree.countLess(new SBTSet.ValueIndex(value, Long.MAX_VALUE));
                    int expectedLess = 0;
                    int expectedEqual = 0;
                    for (int num : window) {
                        expectedLess += num < value ? 1 : 0;
                        expectedEqual += num == value ? 1 : 0;
                    }
                    check(less == expectedLess, "ValueIndex value-level less mismatch: " + value);
                    check(lessOrEqual - less == expectedEqual,
                            "ValueIndex duplicate count mismatch: " + value);
                }

                if (left + k < length) {
                    check(tree.remove(new SBTSet.ValueIndex(nums[left], left)),
                            "ValueIndex outgoing key should exist");
                    check(tree.add(new SBTSet.ValueIndex(nums[left + k], left + k)),
                            "ValueIndex incoming key should be unique");
                }
            }
        }
    }

    private static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static void main(String[] args) {
        testSet();
        testInterviewSet();
        testMap();
        testValueIndexSlidingWindow();
        System.out.println("SBT template comparator passed, seed=" + SEED);
    }
}
