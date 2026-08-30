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
     * 2026-08-31 进阶：dummy节点 + 固定距离快慢指针。
     *
     * <p>这个方法和下面先统计长度的实现，在渐进时间复杂度和指针总移动次数上没有本质区别：
     * {@code fast}走完整条链表，{@code slow}还会走{@code len - n}个节点。所谓“一趟扫描”
     * 更准确地理解为：使用一次连续遍历完成定位，不进行“统计长度后重新从头定位”的两个串行阶段。
     *
     * <p><b>固定距离：</b>
     * <pre>{@code
     * fast先走n步；
     * 然后slow和fast一起走；
     * fast到达尾节点时，slow到达正序第len-n个节点。
     * }
     * </pre>
     * 要删除的是正序第{@code len-n+1}个节点，因此{@code slow}最终正好停在它的前驱
     * {@code len-n}位置，不需要提前统计{@code len}。
     *
     * <p><b>dummy不改变倒数距离：</b>原链表的倒数第n个节点加上dummy后仍然是倒数第n个
     * 真实节点；dummy只是在头节点之前增加了一个统一前驱，使删除原头节点也能执行同一句
     * {@code slow.next = slow.next.next}。
     *
     * <p><b>变量注意：</b>下面使用{@code while (n-- > 0)}后，局部变量{@code n}会被修改；
     * 上述公式中的n始终表示方法刚进入时的原始输入值。
     */
    public class SolutionOnePass20260831 {

        public ListNode removeNthFromEnd(ListNode head, int n) {
            ListNode dummy = new ListNode(0);
            dummy.next = head;
            ListNode fast = dummy;
            ListNode slow = dummy;

            while (n-- > 0) {
                fast = fast.next;
            }

            // TODO: 【注意-易错】fast到达尾节点就应该停止，不能走到null。
            // 此时slow从dummy出发移动了len-originalN次，正好停在待删除节点的前驱。
            while (fast.next != null) {
                fast = fast.next;
                slow = slow.next;
            }

            slow.next = slow.next.next;
            return dummy.next;
        }
    }

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
