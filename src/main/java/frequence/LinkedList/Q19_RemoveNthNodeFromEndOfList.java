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

    /**
     * 2026-08-31 我的两次遍历实现。
     *
     * <p><b>难点1：倒数位置转换为正序位置。</b>位置使用1-based：
     * <pre>{@code
     * 倒数第1个 -> 正序第len个
     * 倒数第2个 -> 正序第len-1个
     * 倒数第n个 -> 正序第len-n+1个
     * }
     * </pre>
     * 因此目标节点的正序位置是{@code k = len - n + 1}。
     *
     * <p><b>难点2：节点位置不等于移动次数。</b>删除正序第k个节点，需要先停在它的前驱，
     * 即正序第{@code k-1}个节点。从第1个节点{@code head}出发，到达第{@code k-1}个节点
     * 只需要移动：
     * <pre>{@code
     * (k - 1) - 1 = k - 2 次
     * }
     * </pre>
     * 例如删除第5个节点，需要停在第4个节点；从第1个节点出发移动3次即可。
     */
    public class Solution20260831 {

        public ListNode removeNthFromEnd(ListNode head, int n) {
            ListNode cur = head;
            int len = 0;
            while (cur != null) {
                len++;
                cur = cur.next;
            }

            // TODO: 【难点1-确定正序位置】1-based：倒1->len，倒2->len-1，倒n->len-n+1。
            int k = len - n + 1;
            if (k == 1) {
                return head.next;
            } else {
                cur = head;
                // TODO: 【难点2-区分位置和移动次数】删除第k个节点，要停在第k-1个前驱；
                // 从第1个节点出发，只需移动k-2次。例如删除第5个节点，移动3次到第4个节点。
                k = k - 2;
                while (k-- > 0) {
                    cur = cur.next;
                }
                cur.next = cur.next.next;
                return head;
            }
        }
    }

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
