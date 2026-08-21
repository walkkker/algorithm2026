package sort.linkedListSort;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Random;

/**
 * 双向链表随机快速排序练习。
 *
 * <p>核心思想：从当前链表随机选择并摘除 pivot，把剩余节点逐个断开，
 * 按照小于、等于、大于 pivot 分配到三个双向链表；递归排序小于区和大于区，
 * 最后拼接三个区间。</p>
 *
 * <p>解题步骤：</p>
 * <ol>
 *     <li>明确递归函数的输入边界和节点数量，并随机选择 pivot。</li>
 *     <li>摘除 pivot；遍历剩余节点时先保存 next，再清理 next 和 last。</li>
 *     <li>为三个分区分别维护 head、tail 和 size。</li>
 *     <li>递归排序小于区和大于区，递归结果同时返回 head 和 tail。</li>
 *     <li>拼接时同步设置前驱的 next 与后继的 last，并清理最终头尾边界。</li>
 * </ol>
 *
 * <p>目标复杂度：期望 O(N log N) 时间，最坏 O(N^2)；递归栈期望 O(log N)。</p>
 *
 * <p>运行 main 方法启动对数器。对数器使用 Arrays.sort 生成参考答案，
 * 并检查排序结果、节点身份、节点数量、环、next/last 一致性和反向遍历结果。</p>
 */
public class DoubleLinkedListQuickSortPractice {

    private static final int TEST_TIMES = 50_000;
    private static final int MAX_SIZE = 50;
    private static final int MAX_VALUE = 100;
    private static final long RANDOM_SEED = 20260715L;

    public static class Node {
        public int value;
        public Node last;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * TODO: 请在这里实现双向链表随机快速排序，只允许重组原节点，不要创建数据节点替代原节点。
     */
    public static Node quickSort(Node head) {
        throw new UnsupportedOperationException("TODO: implement double-linked-list quick sort");
    }

    private static void checkCase(int[] input) {
        Node head = buildList(input);
        IdentityHashMap<Node, Boolean> originalNodes = collectNodes(head);
        int[] expected = input.clone();
        Arrays.sort(expected);

        Node actual = quickSort(head);
        assertDoubleList(actual, expected, originalNodes, input);
    }

    private static Node buildList(int[] values) {
        Node head = null;
        Node tail = null;
        for (int value : values) {
            Node node = new Node(value);
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
                node.last = tail;
            }
            tail = node;
        }
        return head;
    }

    private static IdentityHashMap<Node, Boolean> collectNodes(Node head) {
        IdentityHashMap<Node, Boolean> nodes = new IdentityHashMap<>();
        while (head != null) {
            nodes.put(head, Boolean.TRUE);
            head = head.next;
        }
        return nodes;
    }

    private static void assertDoubleList(Node head, int[] expected,
                                         IdentityHashMap<Node, Boolean> originalNodes,
                                         int[] input) {
        if (head != null && head.last != null) {
            fail("头节点 last 未清空", input, expected);
        }

        IdentityHashMap<Node, Boolean> visited = new IdentityHashMap<>();
        Node cur = head;
        Node previous = null;
        Node tail = null;
        int index = 0;

        while (cur != null) {
            if (visited.put(cur, Boolean.TRUE) != null) {
                fail("正向链表成环", input, expected);
            }
            if (!originalNodes.containsKey(cur)) {
                fail("出现非原链表节点", input, expected);
            }
            if (cur.last != previous) {
                fail("last 指针与正向前驱不一致，index=" + index, input, expected);
            }
            if (index >= expected.length) {
                fail("排序后节点数量增加", input, expected);
            }
            if (cur.value != expected[index]) {
                fail("排序结果错误，index=" + index + ", actual=" + cur.value,
                        input, expected);
            }

            previous = cur;
            tail = cur;
            cur = cur.next;
            index++;
        }

        if (index != expected.length || visited.size() != originalNodes.size()) {
            fail("排序后节点丢失", input, expected);
        }
        if (tail != null && tail.next != null) {
            fail("尾节点 next 未清空", input, expected);
        }

        Node next = null;
        int reverseIndex = expected.length - 1;
        cur = tail;
        while (cur != null) {
            if (cur.next != next) {
                fail("next 指针与反向后继不一致，reverseIndex=" + reverseIndex,
                        input, expected);
            }
            if (reverseIndex < 0 || cur.value != expected[reverseIndex]) {
                fail("反向遍历结果错误，reverseIndex=" + reverseIndex,
                        input, expected);
            }
            next = cur;
            cur = cur.last;
            reverseIndex--;
        }
        if (reverseIndex != -1) {
            fail("反向遍历节点数量错误", input, expected);
        }
    }

    private static void fail(String reason, int[] input, int[] expected) {
        throw new AssertionError(reason
                + "\ninput=" + Arrays.toString(input)
                + "\nexpected=" + Arrays.toString(expected));
    }

    private static void runFixedCases() {
        checkCase(new int[]{});
        checkCase(new int[]{1});
        checkCase(new int[]{1, 2, 3, 4});
        checkCase(new int[]{4, 3, 2, 1});
        checkCase(new int[]{3, 1, 3, 2, 1});
        checkCase(new int[]{0, -2, 5, -2, 1});
    }

    public static void main(String[] args) {
        System.out.println("DoubleLinkedListQuickSortPractice 对数器开始");
        runFixedCases();

        Random random = new Random(RANDOM_SEED);
        for (int test = 0; test < TEST_TIMES; test++) {
            int[] input = randomArray(random);
            checkCase(input);
        }
        System.out.println("DoubleLinkedListQuickSortPractice 对数器通过");
    }

    private static int[] randomArray(Random random) {
        int[] arr = new int[random.nextInt(MAX_SIZE + 1)];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(MAX_VALUE * 2 + 1) - MAX_VALUE;
        }
        return arr;
    }
}
