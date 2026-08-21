package frequence.array.kWayMerge;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * {@link MergeKSortedArrays} 的对数器。
 *
 * <p>基准实现：将所有一维数组复制到一个数组中，再调用 {@link Arrays#sort(int[])}。
 * 被测实现：小根堆 K 路归并、平衡两两归并。
 *
 * <p>运行参数：
 * <ul>
 *   <li>不传参数或传入 {@code all}：测试注册表中的全部实现。</li>
 *   <li>{@code minHeap}：只测试要求非空、等长输入的基础小根堆版本。</li>
 *   <li>{@code minHeapWudi}：只测试支持空输入和不等长行的通用小根堆版本。</li>
 *   <li>{@code divideAndConquer}：只测试平衡两两归并版本。</li>
 * </ul>
 *
 * <p>全部方法会接受同一组通用边界测试，因此不支持通用输入的方法会报告失败。
 * 这样既能检查核心逻辑，也能明确展示各版本的适用边界。
 */
public class MergeKSortedArraysComparator {

    private static final int RANDOM_TEST_TIMES = 10_000;
    private static final Map<String, MergeMethod> METHODS = new LinkedHashMap<>();

    static {
        MergeKSortedArrays solver = new MergeKSortedArrays();
        // 注册新实现后，main方法会自动将它纳入测试流程。
        METHODS.put("minHeap", solver::mergeByMinHeap);
        METHODS.put("minHeapWudi", solver::mergeByMinHeapWudi);
        METHODS.put("divideAndConquer", solver::mergeByDivideAndConquer);
    }

    public static void main(String[] args) {
        String target = args.length == 0 ? "all" : args[0];
        int failures = 0;
        int testedMethods = 0;
        for (Map.Entry<String, MergeMethod> entry : METHODS.entrySet()) {
            if (!"all".equalsIgnoreCase(target)
                    && !entry.getKey().equalsIgnoreCase(target)) {
                continue;
            }
            testedMethods++;
            failures += testFixedCases(entry.getKey(), entry.getValue());
            failures += testRandomRectangular(entry.getKey(), entry.getValue());
            failures += testRandomJagged(entry.getKey(), entry.getValue());
        }

        if (testedMethods == 0) {
            throw new IllegalArgumentException("Unknown target: " + target
                    + ", available=" + METHODS.keySet());
        }
        if (failures != 0) {
            throw new AssertionError("MergeKSortedArraysComparator failed: " + failures);
        }
        System.out.println("MergeKSortedArraysComparator passed: " + target);
    }

    /**
     * 固定用例分别检查：
     * 1. 基础K路归并；
     * 2. PriorityQueue比较器的整数溢出；
     * 3. 每行长度不同；
     * 4. 输入中存在空行；
     * 5. 外层数组长度为0；
     * 6. Wudi版本额外检查外层数组为null。
     */
    private static int testFixedCases(String name, MergeMethod method) {
        int failures = 0;
        failures += verify(name, "basic rectangular",
                new int[][]{{1, 4, 5}, {1, 3, 4}, {2, 6, 8}}, method);
        failures += verify(name, "integer boundary",
                new int[][]{{Integer.MIN_VALUE}, {Integer.MAX_VALUE}}, method);
        failures += verify(name, "different row lengths",
                new int[][]{{1, 4}, {2, 3, 5}, {6}}, method);
        failures += verify(name, "contains empty rows",
                new int[][]{{}, {1, 2}, {}, {3}}, method);
        failures += verify(name, "empty outer array", new int[][]{}, method);
        if ("minHeapWudi".equals(name)) {
            failures += verify(name, "null outer array", null, method);
        }
        return failures;
    }

    /**
     * 生成10,000组非空、等长、行内有序的规则矩阵，
     * 用于验证三个实现共同的核心K路归并逻辑。
     */
    private static int testRandomRectangular(String name, MergeMethod method) {
        Random random = new Random(20260727L);
        for (int test = 1; test <= RANDOM_TEST_TIMES; test++) {
            int rows = random.nextInt(8) + 1;
            int columns = random.nextInt(8) + 1;
            int[][] arrays = randomSortedArrays(random, rows, columns, columns);
            int failure = verify(name, "random rectangular #" + test, arrays, method);
            if (failure != 0) {
                return failure;
            }
        }
        System.out.println("[PASS] " + name + " random rectangular x " + RANDOM_TEST_TIMES);
        return 0;
    }

    /**
     * 生成10,000组行长度随机的有序数组，行长度范围为[0, 8]，
     * 重点验证不等长行和随机空行。
     */
    private static int testRandomJagged(String name, MergeMethod method) {
        Random random = new Random(20260728L);
        for (int test = 1; test <= RANDOM_TEST_TIMES; test++) {
            int rows = random.nextInt(8) + 1;
            int[][] arrays = randomSortedArrays(random, rows, 0, 8);
            int failure = verify(name, "random jagged #" + test, arrays, method);
            if (failure != 0) {
                return failure;
            }
        }
        System.out.println("[PASS] " + name + " random jagged x " + RANDOM_TEST_TIMES);
        return 0;
    }

    /**
     * 创建随机二维数组，并分别对每一行排序，保证输入满足“每行升序”的前提。
     */
    private static int[][] randomSortedArrays(
            Random random,
            int rows,
            int minColumns,
            int maxColumns
    ) {
        int[][] arrays = new int[rows][];
        for (int row = 0; row < rows; row++) {
            int length = minColumns + random.nextInt(maxColumns - minColumns + 1);
            arrays[row] = new int[length];
            for (int col = 0; col < length; col++) {
                arrays[row][col] = random.nextInt(2_001) - 1_000;
            }
            Arrays.sort(arrays[row]);
        }
        return arrays;
    }

    /**
     * 比较被测方法和基准方法；发生差异或异常时打印可直接复现的输入。
     */
    private static int verify(
            String methodName,
            String caseName,
            int[][] arrays,
            MergeMethod method
    ) {
        int[] expected = comparator(arrays);
        try {
            int[] actual = method.merge(copy(arrays));
            if (!Arrays.equals(expected, actual)) {
                printFailure(methodName, caseName, arrays, expected, actual, null);
                return 1;
            }
        } catch (Throwable error) {
            printFailure(methodName, caseName, arrays, expected, null, error);
            return 1;
        }
        return 0;
    }

    /**
     * 基准实现：扁平化全部元素后整体排序。
     * 该方法复杂度不是最优，但逻辑简单可靠，适合作为对数器的正确答案。
     */
    private static int[] comparator(int[][] arrays) {
        if (arrays == null) {
            return new int[0];
        }
        int total = 0;
        for (int[] array : arrays) {
            total += array.length;
        }

        int[] ans = new int[total];
        int write = 0;
        for (int[] array : arrays) {
            System.arraycopy(array, 0, ans, write, array.length);
            write += array.length;
        }
        Arrays.sort(ans);
        return ans;
    }

    /**
     * 深拷贝二维数组，避免被测方法修改输入后影响其他测试。
     */
    private static int[][] copy(int[][] arrays) {
        if (arrays == null) {
            return null;
        }
        int[][] ans = new int[arrays.length][];
        for (int i = 0; i < arrays.length; i++) {
            ans[i] = Arrays.copyOf(arrays[i], arrays[i].length);
        }
        return ans;
    }

    /**
     * 输出方法名、用例名、原始输入、期望结果，以及实际结果或异常。
     */
    private static void printFailure(
            String methodName,
            String caseName,
            int[][] arrays,
            int[] expected,
            int[] actual,
            Throwable error
    ) {
        System.out.println("[FAIL] method=" + methodName + ", case=" + caseName);
        System.out.println("input=" + Arrays.deepToString(arrays));
        System.out.println("expected=" + Arrays.toString(expected));
        if (error == null) {
            System.out.println("actual=" + Arrays.toString(actual));
        } else {
            System.out.println("exception=" + error.getClass().getName()
                    + ": " + error.getMessage());
        }
    }

    @FunctionalInterface
    private interface MergeMethod {
        int[] merge(int[][] arrays);
    }
}
