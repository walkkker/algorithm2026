package frequence.LinkedList;

/**
 * 148. 排序链表
 *
 * <p>给定链表头结点 {@code head}，将链表中的节点按照节点值升序排列，
 * 并返回排序后链表的头结点。
 *
 * <p><b>专题归类：</b>快慢指针找中点、主动断链、递归缩小问题和两路归并。
 * 复盘参见同目录《链表通用技巧与题型分类.md》、《链表错题本.md》以及
 * {@code sort/linkedListSort/LinkedListSortNotes.md}。
 */
public class Q148_SortList {

    /**
     * 2026-09-01 我的链表归并排序实现。
     *
     * <p><b>核心是断链：</b>数组归并排序用下标区间{@code [l, r]}限定递归范围；链表没有随机访问
     * 下标，因此找到中点以后必须执行{@code slow.next = null}，把原链表切成两条以{@code null}
     * 结尾的独立链表。这等价于数组中的{@code mid = (l + r) / 2}。
     *
     * <p><b>递归契约：</b>{@code process(head)}负责对以{@code head}开头的整条独立链表排序，
     * 并返回排序后的新头节点。排序会改变节点顺序，所以左右递归的返回值都必须接住，再把两个
     * 新头节点交给{@code merge}。只调用{@code process(head)}而忽略返回值，会继续使用排序前的
     * 旧头节点，导致节点丢失或归并结果错误。
     *
     * <p><b>步骤：</b>
     * <ol>
     *     <li>快慢指针找到左半区尾节点，保存右半区头节点并断链。</li>
     *     <li>分别递归排序左右独立链表，并接住两条链表排序后的新头。</li>
     *     <li>归并两条有序链表，返回归并结果的新头。</li>
     * </ol>
     *
     * <p>时间复杂度{@code O(N log N)}；递归栈额外空间{@code O(log N)}。归并阶段直接重连
     * 原节点，不需要数组归并排序中的{@code help[]}辅助数组。
     */
    class Solution20260901 {

        public ListNode sortList(ListNode head) {
            if (head == null) {
                return null;
            }
            return process(head);
        }

        // 定义：对传入的整条独立链表排序，返回排序后的新头节点。
        public ListNode process(ListNode head) {
            if (head.next == null) {
                return head;
            }

            // 1. 快慢指针找中点，并主动断链。
            ListNode slow = head;
            ListNode fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            ListNode head2 = slow.next;
            slow.next = null;

            // TODO: 【错误】排序可能改变子链表头节点，递归返回的新头必须分别接住。
            // 错误写法：process(head); process(head2);
            head = process(head);
            head2 = process(head2);
            return merge(head, head2);
        }

        // 定义：合并两条有序链表，返回合并后链表的新头节点。
        public ListNode merge(ListNode head1, ListNode head2) {
            ListNode dummy = new ListNode(0);
            ListNode end = dummy;

            while (head1 != null && head2 != null) {
                if (head1.val < head2.val) {
                    end.next = head1;
                    head1 = head1.next;
                } else {
                    end.next = head2;
                    head2 = head2.next;
                }
                end = end.next;
            }

            // 剩余部分本身已有序，可以整段接到结果链表尾部。
            end.next = head1 != null ? head1 : head2;
            return dummy.next;
        }
    }

    // TODO: 链表版本-归并排序 =》 分界的核心 是 【断链】, 传入的参数只有ListNode （断链后 链表自带长度）
    class Solution {
        public ListNode sortList(ListNode head) {
            if (head == null) {
                return null;
            }
            return process(head);
        }

        public ListNode process(ListNode head) {
            // TODO: 【错误点】head.next==null 这个base case必须包含，不然会无限递归。
            if (head.next == null) {
                // TODO：【错误】只剩一个节点时，返回该节点
                // TODO: 【错误行】 return null;
                return head;
            }
            ListNode slow = head;
            ListNode fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            ListNode head2 = slow.next;
            slow.next = null;
            head = process(head);
            head2 = process(head2);
            return merge(head, head2);
        }

        public ListNode merge(ListNode head1, ListNode head2) {
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;
            while (head1 != null && head2 != null) {
                if (head1.val < head2.val) {
                    tail.next = head1;
                    head1 = head1.next;
                    tail = tail.next;
                } else {
                    tail.next = head2;
                    head2 = head2.next;
                    tail = tail.next;
                }
            }

            while (head1 != null) {
                tail.next = head1;
                head1 = head1.next;
                tail = tail.next;
            }

            while (head2 != null) {
                tail.next = head2;
                head2 = head2.next;
                tail = tail.next;
            }
            return dummy.next;
        }
    }
}
