package SegmentTree.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * TODO： 这里对数器有几个坑，做对接时候要注意：
 *      1. 不会单独调用build()方法 => 需要你在构造器最后加上build方法
 *      2. 调用方法时直接传 目标范围。 不再有(root,l,r,L,R这种了)
 *          举例：tree.query(l,r)  tree.add(l,r,diff) treeUpdate(l,r,newVal)
 *      3. 【最重点】调用方法是基于origin数组的 0~based-1范围，而不是线段树内部的1~based范围。
 *          ！所以，在实现对外接口时，要将【传入的l,r右移一位 l+1,r+1】以实现【原始数组范围到线段树内部数组范围的下标映射】！
 * 拆分功能线段树对数器。
 *
 * 适用场景：
 * 1. 被测线段树只支持一种区间修改语义：区间累加 add 或区间赋值 update。
 * 2. 用于验证 {@link RangeAddSegmentTree}、{@link RangeUpdateSegmentTree}，以及后续你自己写的单功能版本。
 * 3. 对外测试接口使用 0-based，下标约定与 origin 数组一致；内部线段树是否使用 1-based 由具体实现自行处理。
 *
 * 注册方式：
 * - 在 cases() 中新增一个 Case。
 * - add 类实现使用 OperationMode.ADD。
 * - update 类实现使用 OperationMode.UPDATE。
 *
 * 运行方式：
 * - 不传参数或传 all：测试全部已注册实现。
 * - 传 add：只测试所有区间累加实现。
 * - 传 update：只测试所有区间赋值实现。
 * - 传具体类名：只测试该实现。
 */
public class SplitSegmentTreeComparator {

    private static final int TEST_TIMES = 1000;
    private static final int OP_TIMES = 1000;
    private static final int MAX_LEN = 80;
    private static final int MAX_VALUE = 100;
    private static final long SEED = 20260710L;

    public static void main(String[] args) {
        String target = args.length == 0 ? "all" : args[0];
        boolean matched = false;

        for (Case testCase : cases()) {
            if (matches(target, testCase)) {
                matched = true;
                runCase(testCase);
            }
        }

        if (!matched) {
            System.out.println("未匹配到测试目标: " + target);
            System.out.println("可用目标:");
            for (Case testCase : cases()) {
                System.out.println("  " + testCase.name + " (" + testCase.mode.name + ")");
            }
        }
    }

    private static List<Case> cases() {
        List<Case> ans = new ArrayList<>();
        ans.add(new Case("RangeAddSegmentTree", OperationMode.ADD, RangeAddAdapter::new));
        ans.add(new Case("RangeUpdateSegmentTree", OperationMode.UPDATE, RangeUpdateAdapter::new));

        // TODO：你后续自己写实现时，只需要在这里注册一个 Case。
        // 例：ans.add(new Case("MyRangeAddSegmentTree", OperationMode.ADD, MyRangeAddAdapter::new));
        ans.add(new Case("MyRangeAddSegmentTree", OperationMode.ADD, MyRangeAddAdapter::new));
        ans.add(new Case("MyRangeUpdateSegmentTree", OperationMode.UPDATE, MyRangeUpdateAdapter::new));
        return ans;
    }

    private static boolean matches(String target, Case testCase) {
        return "all".equalsIgnoreCase(target)
                || testCase.name.equalsIgnoreCase(target)
                || testCase.mode.name.equalsIgnoreCase(target);
    }

    private static void runCase(Case testCase) {
        System.out.println("开始对数器: " + testCase.name);
        Random random = new Random(SEED);

        for (int round = 0; round < TEST_TIMES; round++) {
            int[] origin = randomArray(random);
            TreeAdapter tree = testCase.factory.create(origin);
            Right right = new Right(origin);
            List<String> history = new ArrayList<>();

            assertQuery(testCase.name, round, origin, history, "initial query",
                    right.query(0, origin.length - 1), tree.query(0, origin.length - 1));

            for (int op = 0; op < OP_TIMES; op++) {
                int[] range = randomRange(random, origin.length);
                int l = range[0];
                int r = range[1];
                int value = randomValue(random);

                if (random.nextBoolean()) {
                    applyModify(testCase, tree, right, l, r, value);
                    history.add(testCase.mode.methodName + "(" + l + ", " + r + ", " + value + ")");
                } else {
                    int expected = right.query(l, r);
                    int actual = tree.query(l, r);
                    history.add("query(" + l + ", " + r + ") -> expected=" + expected + ", actual=" + actual);
                    assertQuery(testCase.name, round, origin, history, "query(" + l + ", " + r + ")", expected, actual);
                }
            }
        }

        System.out.println(testCase.name + " 对数器通过");
    }

    private static void applyModify(Case testCase, TreeAdapter tree, Right right, int l, int r, int value) {
        if (testCase.mode == OperationMode.ADD) {
            tree.modify(l, r, value);
            right.add(l, r, value);
        } else if (testCase.mode == OperationMode.UPDATE) {
            tree.modify(l, r, value);
            right.update(l, r, value);
        } else {
            throw new IllegalStateException("unknown mode: " + testCase.mode);
        }
    }

    private static int[] randomArray(Random random) {
        int len = random.nextInt(MAX_LEN) + 1;
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = randomValue(random);
        }
        return ans;
    }

    private static int[] randomRange(Random random, int n) {
        int a = random.nextInt(n);
        int b = random.nextInt(n);
        return new int[]{Math.min(a, b), Math.max(a, b)};
    }

    private static int randomValue(Random random) {
        return random.nextInt(MAX_VALUE * 2 + 1) - MAX_VALUE;
    }

    private static void assertQuery(
            String name,
            int round,
            int[] origin,
            List<String> history,
            String operation,
            int expected,
            int actual
    ) {
        if (expected == actual) {
            return;
        }
        StringBuilder message = new StringBuilder();
        message.append(name).append(" 对数器失败\n");
        message.append("round=").append(round).append('\n');
        message.append("origin=").append(Arrays.toString(origin)).append('\n');
        message.append("failedOperation=").append(operation).append('\n');
        message.append("expected=").append(expected).append(", actual=").append(actual).append('\n');
        message.append("history:\n");
        for (String item : history) {
            message.append("  ").append(item).append('\n');
        }
        throw new AssertionError(message.toString());
    }

    private enum OperationMode {
        ADD("add", "add"),
        UPDATE("update", "update");

        private final String name;
        private final String methodName;

        OperationMode(String name, String methodName) {
            this.name = name;
            this.methodName = methodName;
        }
    }

    private static class Case {
        private final String name;
        private final OperationMode mode;
        private final TreeFactory factory;

        private Case(String name, OperationMode mode, TreeFactory factory) {
            this.name = name;
            this.mode = mode;
            this.factory = factory;
        }
    }

    private interface TreeFactory {
        TreeAdapter create(int[] origin);
    }

    private interface TreeAdapter {
        void modify(int l, int r, int value);

        int query(int l, int r);
    }

    private static class RangeAddAdapter implements TreeAdapter {
        private final RangeAddSegmentTree tree;

        private RangeAddAdapter(int[] origin) {
            tree = new RangeAddSegmentTree(origin);
        }

        @Override
        public void modify(int l, int r, int value) {
            tree.add(l, r, value);
        }

        @Override
        public int query(int l, int r) {
            return tree.query(l, r);
        }
    }

    private static class RangeUpdateAdapter implements TreeAdapter {
        private final RangeUpdateSegmentTree tree;

        private RangeUpdateAdapter(int[] origin) {
            tree = new RangeUpdateSegmentTree(origin);
        }

        @Override
        public void modify(int l, int r, int value) {
            tree.update(l, r, value);
        }

        @Override
        public int query(int l, int r) {
            return tree.query(l, r);
        }
    }

    public static class MyRangeAddAdapter implements TreeAdapter {

        private final MyRangeAddSegmentTree tree;

        public MyRangeAddAdapter(int[] origin) {
            this.tree = new MyRangeAddSegmentTree(origin);
        }

        @Override
        public void modify(int l, int r, int value) {
            tree.add(l, r, value);
        }

        @Override
        public int query(int l, int r) {
            return tree.query(l, r);
        }
    }


    public static class MyRangeUpdateAdapter implements TreeAdapter {

        private final MyRangeUpdateSegmentTree tree;

        public MyRangeUpdateAdapter(int[] origin) {
            this.tree = new MyRangeUpdateSegmentTree(origin);
        }

        @Override
        public void modify(int l, int r, int value) {
            tree.update(l, r, value);
        }

        @Override
        public int query(int l, int r) {
            return tree.query(l, r);
        }
    }

    private static class Right {
        private final int[] arr;

        private Right(int[] origin) {
            arr = new int[origin.length];
            for (int i = 0; i < origin.length; i++) {
                arr[i] = origin[i];
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
