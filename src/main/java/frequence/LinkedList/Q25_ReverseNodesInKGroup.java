package frequence.LinkedList;

/**
 * 25. K 个一组翻转链表
 * TODO：【重要】本题的雏形是Q24两两反转，框架是一样的。
 * TODO：【重要】核心逻辑是一样的， 前驱节点，反转后面的组，注意反转后的newHead/newTail。
 * TODO：【错误】本题有很重要的错误，在prev.next 跳转到 下一个前驱的时候， 必要连错节点。  下一组反转后，头尾交换。自己看代码。
 *
 * TODO： AI也给出建议，可以把 reverse返回值变成 class Info {ListNode newHead, ListNode newTail} 会更清晰。 但本题可以强行记住，后面使用 prev = start （反转组的尾节点）跳跃，不使用class Info。
 *
 * <p>给定链表头结点 {@code head} 和正整数 {@code k}，每 {@code k} 个节点为一组进行翻转，
 * 并返回修改后的链表头结点。若最后剩余节点不足 {@code k} 个，则保持其原有顺序。
 * 必须通过调整节点连接关系完成翻转，不能只修改节点内部的值。
 */
public class Q25_ReverseNodesInKGroup {

    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null) {
            return null;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode cur = dummy;
        while (cur != null) {
            // TODO；【修正】必须在翻转前保存start：翻转后start会从本组头节点变成本组尾节点。
            ListNode start = cur.next;
            ListNode end = findK(start, k);
            if (end == null) {
                break;
            } else {
                cur.next = reverse(start, end);

                // TODO: 【致命错误】cur的语义是“下一组的前驱节点”，所以它必须始终指向上一组翻转后的尾节点。
                //  翻转[start, end]后，end变成新头，start变成新尾；若错误地令cur=end，
                //  下一轮findK会从已经翻转过的组内继续查找，造成节点被重复翻转，链表结构和分组边界全部错误。
                //  【修改方式】：翻转完成后令cur=start，使cur移动到当前组的新尾节点。
                // TODO: 【错误语句】cur = end;
                cur = start;
            }
        }
        return dummy.next;
    }

    // TODO: 【注意】此处的逆转，[start,end]，逆转后，start.next需要赋值 原来end的next
    public ListNode reverse(ListNode start, ListNode end) {
        ListNode pre = null;
        ListNode cur = start;
        ListNode endNext = end.next;
        while (cur != endNext) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        start.next = endNext;
        return pre;
    }

    public ListNode findK(ListNode start, int k) {
        ListNode cur = start;
        while (cur != null) {
            k--;
            if (k == 0) {
                break;
            }
            cur = cur.next;
        }
        return cur;
    }
}
