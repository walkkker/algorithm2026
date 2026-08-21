package treemap.sbt.DynamicArrayForImplicitSBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DynamicArrayForSBT 的对数器。
 *
 * <p>ArrayList 作为参照实现，同时检查：</p>
 * <ol>
 *     <li>按下标 add/delete/get 的行为一致。</li>
 *     <li>每个节点的 size 与真实子树节点数一致。</li>
 *     <li>每个节点满足 SBT 的 LL/LR/RR/RL 规模约束。</li>
 *     <li>公开方法拒绝非法下标。</li>
 * </ol>
 */
public class DynamicArrayForSBTComparator {

    private static final long SEED = 20260720L;

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void testValidOperations() {
        final int testTimes = 1_000;
        final int operations = 300;
        Random random = new Random(SEED);

        for (int test = 0; test < testTimes; test++) {
            DynamicArrayForSBT.SBT<Integer> actual = new DynamicArrayForSBT.SBT<>();
            ArrayList<Integer> expected = new ArrayList<>();

            for (int operation = 0; operation < operations; operation++) {
                double decide = random.nextDouble();
                if (expected.isEmpty() || decide < 0.45) {
                    int index = random.nextInt(expected.size() + 1);
                    int value = random.nextInt(21) - 10;
                    expected.add(index, value);
                    actual.add(index, value);
                } else if (decide < 0.75) {
                    int index = random.nextInt(expected.size());
                    expected.remove(index);
                    actual.delete(index);
                } else {
                    int index = random.nextInt(expected.size());
                    check(actual.get(index).equals(expected.get(index)),
                            context("get mismatch", test, operation, index, expected));
                }

                assertSameState(actual, expected, test, operation);
            }

            // 专门覆盖连续删除到空，再从空表重新插入。
            while (!expected.isEmpty()) {
                int index = random.nextInt(expected.size());
                expected.remove(index);
                actual.delete(index);
                assertSameState(actual, expected, test, operations);
            }
            actual.add(0, 100);
            expected.add(100);
            assertSameState(actual, expected, test, operations + 1);
        }
    }

    private static void assertSameState(DynamicArrayForSBT.SBT<Integer> actual,
                                        List<Integer> expected,
                                        int test,
                                        int operation) {
        check(actual.size() == expected.size(),
                context("size mismatch", test, operation, -1, expected));

        for (int index = 0; index < expected.size(); index++) {
            check(actual.get(index).equals(expected.get(index)),
                    context("full scan mismatch", test, operation, index, expected));
        }

        ArrayList<Integer> inOrder = new ArrayList<>();
        int realSize = validateNode(actual.root, inOrder, test, operation);
        check(realSize == expected.size(),
                context("real subtree size mismatch", test, operation, -1, expected));
        check(inOrder.equals(expected),
                context("in-order sequence mismatch: " + inOrder,
                        test, operation, -1, expected));
    }

    private static int validateNode(DynamicArrayForSBT.Node<Integer> cur,
                                    List<Integer> inOrder,
                                    int test,
                                    int operation) {
        if (cur == null) {
            return 0;
        }

        int leftSize = validateNode(cur.l, inOrder, test, operation);
        inOrder.add(cur.value);
        int rightSize = validateNode(cur.r, inOrder, test, operation);
        int expectedSize = leftSize + rightSize + 1;

        check(cur.size == expectedSize,
                "node size mismatch, value=" + cur.value
                        + ", stored=" + cur.size + ", real=" + expectedSize
                        + ", test=" + test + ", operation=" + operation);

        int ll = cur.l == null || cur.l.l == null ? 0 : cur.l.l.size;
        int lr = cur.l == null || cur.l.r == null ? 0 : cur.l.r.size;
        int rl = cur.r == null || cur.r.l == null ? 0 : cur.r.l.size;
        int rr = cur.r == null || cur.r.r == null ? 0 : cur.r.r.size;
        check(ll <= rightSize && lr <= rightSize && rr <= leftSize && rl <= leftSize,
                "SBT balance invariant mismatch, value=" + cur.value
                        + ", test=" + test + ", operation=" + operation);
        return expectedSize;
    }

    private static void testInvalidIndexes() {
        DynamicArrayForSBT.SBT<Integer> empty = new DynamicArrayForSBT.SBT<>();
        expectIndexOutOfBounds(() -> empty.add(-1, 1), "add negative index");
        expectIndexOutOfBounds(() -> empty.add(1, 1), "add index greater than size");
        expectIndexOutOfBounds(() -> empty.delete(0), "delete from empty list");
        expectIndexOutOfBounds(() -> empty.get(0), "get from empty list");

        DynamicArrayForSBT.SBT<Integer> one = new DynamicArrayForSBT.SBT<>();
        one.add(0, 1);
        expectIndexOutOfBounds(() -> one.add(2, 2), "add index greater than size");
        expectIndexOutOfBounds(() -> one.delete(-1), "delete negative index");
        expectIndexOutOfBounds(() -> one.delete(1), "delete index equal to size");
        expectIndexOutOfBounds(() -> one.get(-1), "get negative index");
        expectIndexOutOfBounds(() -> one.get(1), "get index equal to size");
    }

    private static void expectIndexOutOfBounds(Runnable operation, String scene) {
        try {
            operation.run();
        } catch (IndexOutOfBoundsException expected) {
            return;
        }
        throw new AssertionError("expected IndexOutOfBoundsException: " + scene);
    }

    private static String context(String message,
                                  int test,
                                  int operation,
                                  int index,
                                  List<Integer> expected) {
        return message + ", test=" + test + ", operation=" + operation
                + ", index=" + index + ", expected=" + expected;
    }

    public static void main(String[] args) {
        testValidOperations();
        System.out.println("valid operation comparator passed, seed=" + SEED);
        testInvalidIndexes();
        System.out.println("invalid index contract passed");
    }
}
