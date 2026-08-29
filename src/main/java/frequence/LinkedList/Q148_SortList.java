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
