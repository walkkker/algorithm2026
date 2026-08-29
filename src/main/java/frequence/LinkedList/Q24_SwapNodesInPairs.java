package frequence.LinkedList;

/**
 * TODO：【重要】本题是 反转K个链表 的雏形。 核心逻辑是一样的，见下方 TODO:【核心】-> 前驱节点，反转后面的组，注意反转后的newHead/newTail
 *
 *
 * 24. 两两交换链表中的节点
 * TODO：【核心】1.dummy + reversePair()方法 + 前驱.next = reversePair(前驱.next)，前驱=前驱.next.next  (即跳到下一个逆转的前驱)
 *      【错误点】第一遍错了，就是忽略了 两两交换后，会产生新头部。  因此必须在前驱触发 【后两个交换】，然后前驱.next 接住 【新头部】。
 * <p>给定一个链表，两两交换其中相邻的节点，并返回交换后链表的头结点。
 * 必须通过调整节点连接关系完成交换，不能只修改节点内部的值。
 *
 * TODO: 【错误】链表反转/改指针，最好的办法就是使用 引用。 不要一直赋值.next .next指针，容易错
 *
 * <p><b>专题归类：</b>dummy、分组前驱、固定长度局部反转、新头/新尾重连；它是Q25的
 * 最小分组原型。参见同目录《链表通用技巧与题型分类.md》和《链表错题本.md》的Q24条目。
 */
public class Q24_SwapNodesInPairs {

    public ListNode swapPairs(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode cur = dummy;
        // TODO: 【注意】每次跳跃，我要跳到 【被逆转的两个节点的】 【前一个节点】。  因为前驱要接住 新头部。
        while (cur.next != null && cur.next.next != null) {
            cur.next = reversePair(cur.next);
            cur = cur.next.next;
        }
        return dummy.next;
    }

    public static ListNode reversePair(ListNode cur) {
        ListNode next = cur.next;
        ListNode nextNext = next.next;
        next.next = cur;
        cur.next = nextNext;
        return next;
    }
}
