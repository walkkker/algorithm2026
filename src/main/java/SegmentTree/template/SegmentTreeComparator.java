package SegmentTree.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 混合功能线段树对数器。
 *
 * 适用场景：
 * 1. 被测线段树同时支持区间累加 add、区间赋值 update、区间查询 query。
 * 2. 用于验证 {@link SegmentTree} 和 {@link MySegmentTree.SegmentTree} 这类“add/update 混合懒标记”的实现。
 * 3. 操作区间使用 1-based，下标约定与原始混合版线段树一致。
 *
 * 运行方式：
 * - 不传参数或传 all：测试全部已注册实现。
 * - 传 SegmentTree：只测试 SegmentTree。
 * - 传 MySegmentTree：只测试 MySegmentTree.SegmentTree。
 */
public class SegmentTreeComparator {

    private static final int ROOT = 1;
    private static final int START = 1;
    private static final int TEST_TIMES = 1000;
    private static final int OP_TIMES = 1000;
    private static final int MAX_LEN = 80;
    private static final int MAX_VALUE = 100;
    private static final long SEED = 20260710L;

    public static void main(String[] args) {
        String target = args.length == 0 ? "all" : args[0];

        if ("SegmentTree".equalsIgnoreCase(target) || "all".equalsIgnoreCase(target)) {
            run("SegmentTree", SegmentTreeAdapter::new);
        }
        if ("MySegmentTree".equalsIgnoreCase(target) || "all".equalsIgnoreCase(target)) {
            run("MySegmentTree", MySegmentTreeAdapter::new);
        }
    }

    private static void run(String name, TreeFactory factory) {
        System.out.println("开始对数器: " + name);
        Random random = new Random(SEED);

        for (int round = 0; round < TEST_TIMES; round++) {
            int[] origin = randomArray(random);
            TreeAdapter tree = factory.create(origin);
            Right right = new Right(origin);
            int n = origin.length;
            List<String> history = new ArrayList<>();

            int initialActual = tree.query(START, n);
            int initialExpected = right.query(START, n);
            if (initialActual != initialExpected) {
                fail(name, round, origin, history, "initial query", initialExpected, initialActual);
            }

            for (int op = 0; op < OP_TIMES; op++) {
                int a = random.nextInt(n) + 1;
                int b = random.nextInt(n) + 1;
                int l = Math.min(a, b);
                int r = Math.max(a, b);
                int value = random.nextInt(MAX_VALUE * 2 + 1) - MAX_VALUE;
                int opType = random.nextInt(3);

                if (opType == 0) {
                    tree.add(l, r, value);
                    right.add(l, r, value);
                    history.add("add(" + l + ", " + r + ", " + value + ")");
                } else if (opType == 1) {
                    tree.update(l, r, value);
                    right.update(l, r, value);
                    history.add("update(" + l + ", " + r + ", " + value + ")");
                } else {
                    int actual = tree.query(l, r);
                    int expected = right.query(l, r);
                    history.add("query(" + l + ", " + r + ") -> expected=" + expected + ", actual=" + actual);
                    if (actual != expected) {
                        fail(name, round, origin, history, "query(" + l + ", " + r + ")", expected, actual);
                    }
                }
            }
        }

        System.out.println(name + " 对数器通过");
    }

    private static int[] randomArray(Random random) {
        int len = random.nextInt(MAX_LEN) + 1;
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = random.nextInt(MAX_VALUE * 2 + 1) - MAX_VALUE;
        }
        return ans;
    }

    private static void fail(
            String name,
            int round,
            int[] origin,
            List<String> history,
            String failedOperation,
            int expected,
            int actual
    ) {
        StringBuilder message = new StringBuilder();
        message.append(name).append(" 对数器失败\n");
        message.append("round=").append(round).append('\n');
        message.append("origin=").append(Arrays.toString(origin)).append('\n');
        message.append("failedOperation=").append(failedOperation).append('\n');
        message.append("expected=").append(expected).append(", actual=").append(actual).append('\n');
        message.append("history:\n");
        for (String item : history) {
            message.append("  ").append(item).append('\n');
        }
        throw new AssertionError(message.toString());
    }

    private interface TreeFactory {
        TreeAdapter create(int[] origin);
    }

    private interface TreeAdapter {
        void add(int l, int r, int value);

        void update(int l, int r, int value);

        int query(int l, int r);
    }

    private static class SegmentTreeAdapter implements TreeAdapter {
        private final SegmentTree tree;
        private final int n;

        private SegmentTreeAdapter(int[] origin) {
            tree = new SegmentTree(origin);
            n = origin.length;
            tree.build(ROOT, START, n);
        }

        @Override
        public void add(int l, int r, int value) {
            tree.add(ROOT, START, n, l, r, value);
        }

        @Override
        public void update(int l, int r, int value) {
            tree.update(ROOT, START, n, l, r, value);
        }

        @Override
        public int query(int l, int r) {
            return tree.query(ROOT, START, n, l, r);
        }
    }

    private static class MySegmentTreeAdapter implements TreeAdapter {
        private final MySegmentTree.SegmentTree tree;
        private final int n;

        private MySegmentTreeAdapter(int[] origin) {
            tree = new MySegmentTree.SegmentTree(origin);
            n = origin.length;
            tree.build(ROOT, START, n);
        }

        @Override
        public void add(int l, int r, int value) {
            tree.add(ROOT, START, n, l, r, value);
        }

        @Override
        public void update(int l, int r, int value) {
            tree.update(ROOT, START, n, l, r, value);
        }

        @Override
        public int query(int l, int r) {
            return tree.query(ROOT, START, n, l, r);
        }
    }

    private static class Right {
        private final int[] arr;

        private Right(int[] origin) {
            arr = new int[origin.length + 1];
            for (int i = 0; i < origin.length; i++) {
                arr[i + 1] = origin[i];
            }
        }

        private void add(int l, int r, int value) {
            for (int i = l; i <= r; i++) {
                arr[i] += value;
            }
        }

        private void update(int l, int r, int value) {
            for (int i = l; i <= r; i++) {
                arr[i] = value;
            }
        }

        private int query(int l, int r) {
            int ans = 0;
            for (int i = l; i <= r; i++) {
                ans += arr[i];
            }
            return ans;
        }
    }
}
