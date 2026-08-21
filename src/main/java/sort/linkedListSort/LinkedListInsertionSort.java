package sort.linkedListSort;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Random;

/**
 * 单链表插入排序练习。
 *
 * <p>核心思想：维护一个已经有序的左侧链表，依次从未排序区域取出当前节点，
 * 在有序链表中找到插入前驱，再通过修改 next 指针把当前节点插入目标位置。</p>
 *
 * <p>解题步骤：</p>
 * <ol>
 *     <li>使用 dummy 简化头节点前插的边界处理。</li>
 *     <li>修改当前节点指针前，先保存原链表中的 next。</li>
 *     <li>从 dummy 开始寻找有序区域中的插入前驱。</li>
 *     <li>将当前节点插入后，继续处理之前保存的 next。</li>
 * </ol>
 *
 * <p>目标复杂度：O(N^2) 时间，O(1) 额外空间。</p>
 *
 * <p>运行 main 方法启动对数器。对数器使用 Arrays.sort 生成参考答案，
 * 并检查排序结果、节点数量、节点身份和链表是否成环。</p>
 */
public class LinkedListInsertionSort {

    private static final int TEST_TIMES = 100_000;
    private static final int MAX_SIZE = 50;
    private static final int MAX_VALUE = 100;
    private static final long RANDOM_SEED = 20260715L;

    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * DONE: 【已独立完成】单链表插入排序，只重组原节点，不创建数据节点替代原节点。
     */
    public static Node sort(Node head) {
//        throw new UnsupportedOperationException("TODO: implement linked-list insertion sort");

        Node dummy = new Node(0);
        Node cur = head;
        while (cur != null) {
            Node next = cur.next; // 标记next

            cur.next = null;   // detach TODO: 特别重要！！！

            addToSortedLinkedList(dummy, cur);

            cur = next;
        }

        return dummy.next;

    }

    public static void addToSortedLinkedList(Node dummyHead, Node target) {
        // 插入节点的逻辑， 如果开头是个dummy，后面直接可以统一了。 因为检查条件是 cur != null && cur.val < targetVal
        Node pre = dummyHead;
        Node cur = dummyHead.next;

        while (cur != null && cur.value < target.value) {
            pre = cur;
            cur = cur.next;
        }
        pre.next = target;
        target.next = cur;
    }

    private static void checkCase(int[] input) {
        Node head = buildList(input);
        IdentityHashMap<Node, Boolean> originalNodes = collectNodes(head);
        int[] expected = input.clone();
        Arrays.sort(expected);

        Node actual = sort(head);
        assertSingleList(actual, expected, originalNodes, input);
    }

    private static Node buildList(int[] values) {
        Node dummy = new Node(0);
        Node tail = dummy;
        for (int value : values) {
            tail.next = new Node(value);
            tail = tail.next;
        }
        return dummy.next;
    }

    private static IdentityHashMap<Node, Boolean> collectNodes(Node head) {
        IdentityHashMap<Node, Boolean> nodes = new IdentityHashMap<>();
        while (head != null) {
            nodes.put(head, Boolean.TRUE);
            head = head.next;
        }
        return nodes;
    }

    private static void assertSingleList(Node head, int[] expected,
                                         IdentityHashMap<Node, Boolean> originalNodes,
                                         int[] input) {
        IdentityHashMap<Node, Boolean> visited = new IdentityHashMap<>();
        Node cur = head;
        int index = 0;

        while (cur != null) {
            if (visited.put(cur, Boolean.TRUE) != null) {
                fail("链表成环", input, expected);
            }
            if (!originalNodes.containsKey(cur)) {
                fail("出现非原链表节点", input, expected);
            }
            if (index >= expected.length) {
                fail("排序后节点数量增加", input, expected);
            }
            if (cur.value != expected[index]) {
                fail("排序结果错误，index=" + index + ", actual=" + cur.value,
                        input, expected);
            }
            index++;
            cur = cur.next;
        }

        if (index != expected.length || visited.size() != originalNodes.size()) {
            fail("排序后节点丢失", input, expected);
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
        System.out.println("LinkedListInsertionSort 对数器开始");
        runFixedCases();

        Random random = new Random(RANDOM_SEED);
        for (int test = 0; test < TEST_TIMES; test++) {
            int[] input = randomArray(random);
            checkCase(input);
        }
        System.out.println("LinkedListInsertionSort 对数器通过");
    }

    private static int[] randomArray(Random random) {
        int[] arr = new int[random.nextInt(MAX_SIZE + 1)];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(MAX_VALUE * 2 + 1) - MAX_VALUE;
        }
        return arr;
    }
}
