package sort.linkedListSort;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Random;

/**
 * 单链表归并排序练习。
 * TODO：核心就是 断链
 *
 * <p>核心思想：使用快慢指针把链表分成左右两部分，递归排序两个子链表，
 * 再通过移动 next 指针合并两个有序链表。</p>
 *
 * <p>递归契约：sort(head) 接收一条以 head 开始、以 null 结束的独立链表，
 * 返回该链表排序后的新头节点。排序会改变子链表头，因此父递归必须使用返回值接住：</p>
 *
 * <pre>{@code
 * Node left = sort(leftHead);
 * Node right = sort(rightHead);
 * return merge(left, right);
 * }</pre>
 *
 * <p>不需要把子链表的原前驱节点传入递归。递归前通过 slow.next = null
 * 将左右区域物理断开；递归后，父函数的 left/right 局部变量已经指向各自的新头，
 * merge 再把两条独立有序链表连接成当前范围的新链表。</p>
 *
 * <p>数组归并依靠固定下标表示子区间，排序后区间起点不会变化；链表归并依靠断链
 * 表示子区间，并通过递归返回值传递排序后的新头节点。</p>
 *
 * <p>解题步骤：</p>
 * <ol>
 *     <li>使用快慢指针定位中点前一个节点。</li>
 *     <li>断开左右链表，避免递归区间相互连接。</li>
 *     <li>递归排序左右子链表。</li>
 *     <li>迭代合并两个有序链表，并返回合并后的头节点。</li>
 * </ol>
 *
 * <p>直接复用原节点时不需要数组归并的 help[]。自顶向下版本目标复杂度为
 * O(N log N) 时间、O(log N) 递归栈空间；自底向上版本可以做到 O(1) 额外空间。</p>
 *
 * <p>运行 main 方法启动对数器。对数器使用 Arrays.sort 生成参考答案，
 * 并检查排序结果、节点数量、节点身份和链表是否成环。</p>
 */
public class LinkedListMergeSort {

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
     * DONE: 【已独立完成】单链表归并排序，只重组原节点，不创建数据节点替代原节点。
     */
    public static Node sort(Node head) {
//        throw new UnsupportedOperationException("TODO: implement linked-list merge sort");
        return process(head);
    }

    public static Node process(Node head) {
        if (head == null) {
            return null;
        }
        // TODO: 【超级错误点！！！】下面这句left = process(left) 一直报错 stackOverflow
        // TODO: 【原因】base case 很有问题。 当只有一个元素时，不能再递归！！！
        //  递归导致栈溢出：【一定存在 自己调用自己的】行为。  从而导致无限递归！！！
        //  【解决方案】类比 数组版本，  l>=r 呀！！！！  一个元素直接返回！！！
        //   递归函数，能base case一定base case。 防止遗漏，导致自己递归自己，最终无限递归。
        if (head.next == null) {
            return head;
        }



        // 快慢指针找中点 => 中或上中
        Node slow = head;
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        Node left = head;
        Node right = slow.next;
        slow.next = null;   // TODO：一定要断链。 断链意味着缩小子问题

        left = process(left);
        right = process(right);
        return merge(left, right);
    }

    public static Node merge(Node left, Node right) {
        Node dummyHead = new Node(0);
        Node tail = dummyHead;
        while (left != null && right != null) {
            if (left.value < right.value) {
                tail.next = left;
                tail = tail.next;
                left = left.next;
            } else {
                tail.next = right;
                tail = tail.next;
                right = right.next;
            }
        }
        while (left != null) {
            tail.next = left;
            tail = tail.next;
            left = left.next;
        }
        while (right != null) {
            tail.next = right;
            tail = tail.next;
            right = right.next;
        }
        return dummyHead.next;
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
        System.out.println("LinkedListMergeSort 对数器开始");
        runFixedCases();

        Random random = new Random(RANDOM_SEED);
        for (int test = 0; test < TEST_TIMES; test++) {
            int[] input = randomArray(random);
            checkCase(input);
        }
        System.out.println("LinkedListMergeSort 对数器通过");
    }

    private static int[] randomArray(Random random) {
        int[] arr = new int[random.nextInt(MAX_SIZE + 1)];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(MAX_VALUE * 2 + 1) - MAX_VALUE;
        }
        return arr;
    }
}
