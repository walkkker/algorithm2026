package treemap.compare;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;
import treemap.test20260713.AVL.AVLTreeMap;
//import treemap.compare.Code01_SizeBalancedTreeMap.*;
import treemap.test20260713.SBT.SBTreeMap;
//import treemap.Code_SkipListMap.*;
import treemap.test20260713.SkipList.SkipListMap;


// 本文件为avl、sbt、skiplist三种结构的测试文件
public class Compare {

	private static final int MAX_KEY = 500;
	private static final int MAX_VALUE = 50000;
	private static final int DEFAULT_TEST_TIME = 1000000;
	private static final int OPERATION_HISTORY_LIMIT = 20;
	private static final long PERFORMANCE_ADD_SEED = 2026071401L;
	private static final long PERFORMANCE_REMOVE_SEED = 2026071402L;

	public static void functionTest() {
		functionTest(System.nanoTime(), DEFAULT_TEST_TIME);
	}

	public static void functionTest(long seed, int testTime) {
		if (testTime < 0) {
			throw new IllegalArgumentException("testTime不能小于0");
		}
		System.out.println("功能测试开始，seed=" + seed + "，testTime=" + testTime);
		TreeMap<Integer, Integer> treeMap = new TreeMap<>();
		AVLTreeMap<Integer, Integer> avl = new AVLTreeMap<>();
		SBTreeMap<Integer, Integer> sbt = new SBTreeMap<>();
		SkipListMap<Integer, Integer> skip = new SkipListMap<>();
		Random random = new Random(seed);
		Deque<String> operationHistory = new ArrayDeque<>();

		verifyEquivalent(treeMap, avl, sbt, skip, 0, seed, -1, operationHistory);
		for (int i = 0; i < testTime; i++) {
			int addK = random.nextInt(MAX_KEY);
			int addV = random.nextInt(MAX_VALUE);
			treeMap.put(addK, addV);
			avl.put(addK, addV);
			sbt.put(addK, addV);
			skip.put(addK, addV);
			recordOperation(operationHistory, "put(" + addK + ", " + addV + ")");

			int removeK = random.nextInt(MAX_KEY);
			treeMap.remove(removeK);
			avl.remove(removeK);
			sbt.remove(removeK);
			skip.remove(removeK);
			recordOperation(operationHistory, "remove(" + removeK + ")");

			int queryKey = random.nextInt(MAX_KEY);
			verifyEquivalent(treeMap, avl, sbt, skip, queryKey, seed, i, operationHistory);
			if (addK != queryKey) {
				verifyEquivalent(treeMap, avl, sbt, skip, addK, seed, i, operationHistory);
			}
			if (removeK != queryKey && removeK != addK) {
				verifyEquivalent(treeMap, avl, sbt, skip, removeK, seed, i, operationHistory);
			}
		}
		verifyEquivalent(treeMap, avl, sbt, skip, Integer.MIN_VALUE, seed, testTime, operationHistory);
		verifyEquivalent(treeMap, avl, sbt, skip, Integer.MAX_VALUE, seed, testTime, operationHistory);
		System.out.println("功能测试通过，seed=" + seed + "，testTime=" + testTime);
	}

	private static void verifyEquivalent(
			TreeMap<Integer, Integer> treeMap,
			AVLTreeMap<Integer, Integer> avl,
			SBTreeMap<Integer, Integer> sbt,
			SkipListMap<Integer, Integer> skip,
			int queryKey,
			long seed,
			int iteration,
			Deque<String> operationHistory) {
		assertEquivalent("containsKey", treeMap.containsKey(queryKey), avl.containsKey(queryKey),
				sbt.containsKey(queryKey), skip.containsKey(queryKey), treeMap, queryKey, seed, iteration,
				operationHistory);
		assertEquivalent("get", treeMap.get(queryKey), avl.get(queryKey), sbt.get(queryKey), skip.get(queryKey),
				treeMap, queryKey, seed, iteration, operationHistory);
		assertEquivalent("floorKey", treeMap.floorKey(queryKey), avl.floorKey(queryKey), sbt.floorKey(queryKey),
				skip.floorKey(queryKey), treeMap, queryKey, seed, iteration, operationHistory);
		assertEquivalent("ceilingKey", treeMap.ceilingKey(queryKey), avl.ceilingKey(queryKey),
				sbt.ceilingKey(queryKey), skip.ceilingKey(queryKey), treeMap, queryKey, seed, iteration,
				operationHistory);

		Integer expectedFirstKey = treeMap.isEmpty() ? null : treeMap.firstKey();
		Integer expectedLastKey = treeMap.isEmpty() ? null : treeMap.lastKey();
		assertEquivalent("firstKey", expectedFirstKey, avl.firstKey(), sbt.firstKey(), skip.firstKey(), treeMap,
				queryKey, seed, iteration, operationHistory);
		assertEquivalent("lastKey", expectedLastKey, avl.lastKey(), sbt.lastKey(), skip.lastKey(), treeMap,
				queryKey, seed, iteration, operationHistory);
		assertEquivalent("size", treeMap.size(), avl.size(), sbt.size(), skip.size(), treeMap, queryKey, seed,
				iteration, operationHistory);
	}

	private static void assertEquivalent(
			String operation,
			Object expected,
			Object avlActual,
			Object sbtActual,
			Object skipActual,
			TreeMap<Integer, Integer> treeMap,
			int queryKey,
			long seed,
			int iteration,
			Deque<String> operationHistory) {
		if (Objects.equals(expected, avlActual)
				&& Objects.equals(expected, sbtActual)
				&& Objects.equals(expected, skipActual)) {
			return;
		}
		throw new AssertionError(operation
				+ "校验失败: seed=" + seed
				+ ", iteration=" + iteration
				+ ", queryKey=" + queryKey
				+ ", TreeMap=" + expected
				+ ", AVL=" + avlActual
				+ ", SBT=" + sbtActual
				+ ", SkipList=" + skipActual
				+ ", TreeMapState=" + treeMap
				+ ", recentOperations=" + operationHistory);
	}

	private static void recordOperation(Deque<String> operationHistory, String operation) {
		if (operationHistory.size() == OPERATION_HISTORY_LIMIT) {
			operationHistory.removeFirst();
		}
		operationHistory.addLast(operation);
	}

	public static void performanceTest() {
		System.out.println("性能测试开始");
		TreeMap<Integer, Integer> treeMap;
		AVLTreeMap<Integer, Integer> avl;
		SBTreeMap<Integer, Integer> sbt;
		SkipListMap<Integer, Integer> skip;
		long start;
		long end;
		Random workloadRandom;
		int max = 5000000;
		treeMap = new TreeMap<>();
		avl = new AVLTreeMap<>();
		sbt = new SBTreeMap<>();
		skip = new SkipListMap<>();
		System.out.println("顺序递增加入测试，数据规模 : " + max);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			treeMap.put(i, i);
		}
		end = System.currentTimeMillis();
		System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			avl.put(i, i);
		}
		end = System.currentTimeMillis();
		System.out.println("avl 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			sbt.put(i, i);
		}
		end = System.currentTimeMillis();
		System.out.println("sbt 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			skip.put(i, i);
		}
		end = System.currentTimeMillis();
		System.out.println("skip 运行时间 : " + (end - start) + "ms");

		System.out.println("顺序递增删除测试，数据规模 : " + max);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			treeMap.remove(i);
		}
		end = System.currentTimeMillis();
		System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			avl.remove(i);
		}
		end = System.currentTimeMillis();
		System.out.println("avl 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			sbt.remove(i);
		}
		end = System.currentTimeMillis();
		System.out.println("sbt 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			skip.remove(i);
		}
		end = System.currentTimeMillis();
		System.out.println("skip 运行时间 : " + (end - start) + "ms");

		System.out.println("顺序递减加入测试，数据规模 : " + max);
		start = System.currentTimeMillis();
		for (int i = max - 1; i >= 0; i--) {
			treeMap.put(i, i);
		}
		end = System.currentTimeMillis();
		System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = max - 1; i >= 0; i--) {
			avl.put(i, i);
		}
		end = System.currentTimeMillis();
		System.out.println("avl 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = max - 1; i >= 0; i--) {
			sbt.put(i, i);
		}
		end = System.currentTimeMillis();
		System.out.println("sbt 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = max - 1; i >= 0; i--) {
			skip.put(i, i);
		}
		end = System.currentTimeMillis();
		System.out.println("skip 运行时间 : " + (end - start) + "ms");

		System.out.println("顺序递减删除测试，数据规模 : " + max);
		start = System.currentTimeMillis();
		for (int i = max - 1; i >= 0; i--) {
			treeMap.remove(i);
		}
		end = System.currentTimeMillis();
		System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = max - 1; i >= 0; i--) {
			avl.remove(i);
		}
		end = System.currentTimeMillis();
		System.out.println("avl 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = max - 1; i >= 0; i--) {
			sbt.remove(i);
		}
		end = System.currentTimeMillis();
		System.out.println("sbt 运行时间 : " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = max - 1; i >= 0; i--) {
			skip.remove(i);
		}
		end = System.currentTimeMillis();
		System.out.println("skip 运行时间 : " + (end - start) + "ms");

		System.out.println("随机加入测试，数据规模 : " + max + "，seed : " + PERFORMANCE_ADD_SEED);
		workloadRandom = new Random(PERFORMANCE_ADD_SEED);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			treeMap.put(workloadRandom.nextInt(max), i);
		}
		end = System.currentTimeMillis();
		System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

		workloadRandom = new Random(PERFORMANCE_ADD_SEED);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			avl.put(workloadRandom.nextInt(max), i);
		}
		end = System.currentTimeMillis();
		System.out.println("avl 运行时间 : " + (end - start) + "ms");

		workloadRandom = new Random(PERFORMANCE_ADD_SEED);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			sbt.put(workloadRandom.nextInt(max), i);
		}
		end = System.currentTimeMillis();
		System.out.println("sbt 运行时间 : " + (end - start) + "ms");

		workloadRandom = new Random(PERFORMANCE_ADD_SEED);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			skip.put(workloadRandom.nextInt(max), i);
		}
		end = System.currentTimeMillis();
		System.out.println("skip 运行时间 : " + (end - start) + "ms");

		System.out.println("随机删除测试，数据规模 : " + max + "，seed : " + PERFORMANCE_REMOVE_SEED);
		workloadRandom = new Random(PERFORMANCE_REMOVE_SEED);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			treeMap.remove(workloadRandom.nextInt(max));
		}
		end = System.currentTimeMillis();
		System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

		workloadRandom = new Random(PERFORMANCE_REMOVE_SEED);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			avl.remove(workloadRandom.nextInt(max));
		}
		end = System.currentTimeMillis();
		System.out.println("avl 运行时间 : " + (end - start) + "ms");

		workloadRandom = new Random(PERFORMANCE_REMOVE_SEED);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			sbt.remove(workloadRandom.nextInt(max));
		}
		end = System.currentTimeMillis();
		System.out.println("sbt 运行时间 : " + (end - start) + "ms");

		workloadRandom = new Random(PERFORMANCE_REMOVE_SEED);
		start = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			skip.remove(workloadRandom.nextInt(max));
		}
		end = System.currentTimeMillis();
		System.out.println("skip 运行时间 : " + (end - start) + "ms");

		System.out.println("性能测试结束");
	}

	public static void main(String[] args) {
		functionTest();
		System.out.println("======");
		performanceTest();
	}

}
