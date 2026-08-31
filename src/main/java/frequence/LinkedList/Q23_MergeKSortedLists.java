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

    /**
     * 2026-09-01 我的小根堆K路归并实现。
     *
     * <p><b>核心模型：</b>堆中只保存每条非空链表当前尚未消费的第一个节点。每次弹出全局最小
     * 节点并接入结果链表，然后仅把该节点的非空后继加入堆。这样堆中最多存在K个候选节点。
     *
     * <p><b>本次错误：</b>{@code lists}数组中的元素允许是{@code null}，表示对应链表为空。
     * {@link PriorityQueue}不允许插入{@code null}，无条件执行{@code heap.offer(node)}会触发
     * {@link NullPointerException}。因此初始化堆和后续加入后继节点时都必须先做非空判断。
     *
     * <p><b>复杂度：</b>设所有链表节点总数为T、链表条数为K。每个节点入堆和出堆各一次，
     * 时间复杂度为{@code O(T log K)}，堆额外空间为{@code O(K)}。
     */
    class Solution20260901 {

        public ListNode mergeKLists(ListNode[] lists) {
            ListNode dummy = new ListNode(0);
            ListNode end = dummy;
            PriorityQueue<ListNode> heap = new PriorityQueue<>(
                    (a, b) -> Integer.compare(a.val, b.val)
            );

            for (ListNode node : lists) {
                // TODO: 【错误】lists中的链表头允许为null，而PriorityQueue不允许加入null。
                // 错误写法：heap.offer(node); 结果是NullPointerException。
                if (node != null) {
                    heap.offer(node);
                }
            }

            while (!heap.isEmpty()) {
                ListNode cur = heap.poll();
                if (cur.next != null) {
                    heap.offer(cur.next);
                }
                end.next = cur;
                end = end.next;
            }
            return dummy.next;
        }
    }

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
