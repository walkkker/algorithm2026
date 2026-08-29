package frequence.LinkedList;

/**
 * 206. 反转链表。
 *
 * <p><b>专题归类：</b>链表指针修改的最小骨架：保存后继、反转连接、推进前驱和当前节点。
 * 复盘参见同目录《链表通用技巧与题型分类.md》以及《链表专题总览与面试优先级.md》。
 */
public class Q206 {
    public ListNode reverseList(ListNode head) {
        if (head == null) {
            return null;
        }

        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }
}
