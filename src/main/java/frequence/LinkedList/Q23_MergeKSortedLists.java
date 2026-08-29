package frequence.LinkedList;

/**
 * TODO: 链表版本-K路合并 （使用小根堆PriorityQueue）
 *
 * 23. 合并 K 个升序链表
 *
 * <p>给定一个链表数组 {@code lists}，其中每个链表均已按节点值升序排列。
 * 将所有链表合并为一个升序链表，并返回合并后链表的头结点。
 *
 * <p><b>专题归类：</b>K路归并、小根堆维护每一路当前最小节点。总节点数为T、链表数为K时，
 * 时间复杂度为{@code O(T log K)}。参见同目录《链表通用技巧与题型分类.md》以及
 * {@code frequence/array/kWayMerge/note.md}。
 */
import java.util.*;
public class Q23_MergeKSortedLists {

    public ListNode mergeKLists(ListNode[] lists) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        // TODO: 【通用改进】a.val - b.val在更大值域下可能整数溢出，稳定写法是Integer.compare(a.val, b.val)。
        PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> (a.val - b.val));
        for (ListNode head : lists) {
            if (head != null) {
                heap.add(head);
            }
        }
        while (!heap.isEmpty()) {
            ListNode cur = heap.poll();
            tail.next = cur;
            tail = tail.next;

            if (cur.next != null) {
                heap.add(cur.next);
            }
        }
        return dummy.next;
    }
}
