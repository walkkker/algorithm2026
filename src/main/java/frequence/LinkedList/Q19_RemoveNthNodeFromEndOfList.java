package frequence.LinkedList;

/**
 * 19. 删除链表的倒数第 N 个结点
 *
 * <p>给定一个链表的头结点 {@code head} 和一个整数 {@code n}，删除链表的倒数第
 * {@code n} 个结点，并返回删除后的链表头结点。
 *
 * <p><b>专题归类：</b>倒数位置、固定距离双指针、前驱删除与dummy统一边界。
 * 复盘参见同目录《链表通用技巧与题型分类.md》和《链表错题本.md》的Q19条目。
 */
public class Q19_RemoveNthNodeFromEndOfList {

    public ListNode removeNthFromEnd(ListNode head, int n) {
        int len = 0;
        ListNode cur = head;
        while (cur != null) {
            len++;
            cur = cur.next;
        }
        n = len + 1 - n;
        if (n == 1) {
            return head.next;
        } else {
            // TODO: 【核心错误】我要跳到第4个节点。那么实际上我只需要在 1-3个节点上，触发cur=cur.next ！！！！
            //   cur = head;
            //   for (int i = 1; i <= n - 1; i++) {
            //       cur = cur.next;
            //   }
            // 目标是跳到第 n-1个节点。 -> 那么只需要在[1，n-2]节点上触发跳跃动作，因此总共跳跃 n-2次
            cur = head;
            for (int i = 1; i <= n - 2; i++) {  // 在[1,n-2]触发跳跃，退出循环时，跳到n-1节点上。
                cur = cur.next;
            }
            cur.next = cur.next.next;
            return head;
        }
    }
}
