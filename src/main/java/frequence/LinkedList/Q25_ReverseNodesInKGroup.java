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
 *
 * <p><b>专题归类：</b>分组边界检查、区间反转、前驱重连和反转后的头尾身份交换。
 * 复盘参见同目录《链表通用技巧与题型分类.md》和《链表错题本.md》的Q25条目。
 */
public class Q25_ReverseNodesInKGroup {

    /**
     * 2026-09-23 第三遍，修正计数条件中的后置自减错误。
     *
     * <p><b>我的理解：</b>处理链表问题最直观的办法：不要省代码，用变量锁住地址，
     * 而不要依赖会改动的指针字段；提前保存pre、start、end、post。
     * 上一次错误点：如果节点总数不是k的整数倍，最后剩余节点必须保持原有顺序。
     *
     * <p><b>补充定义：</b>Java变量保存节点引用，而不是固定物理地址；修改next不会改变
     * 已保存引用所指的节点。每轮cur是待反转组的前驱，先确认完整k个，再冻结边界并反转。
     * 反转后end是本组头，start是本组尾，也是下一组的前驱。
     *
     * <p>本版在k为正整数的题目约束下正确。时间O(N)，额外空间O(1)，无递归栈。
     * 计数错误详见同目录《链表错题本.md》的2026-09-23记录。
     */
    public static class SolutionThirdReview20260923 {
        public ListNode reverseKGroup(ListNode head, int k) {
            ListNode dummy = new ListNode(0);
            dummy.next = head;
            ListNode cur = dummy;
            while (cur != null) { // TODO: 我的每一个开始点，都是[start,end]的前一个节点。
                ListNode pre = cur;
                ListNode start = cur.next;
                ListNode end = countKNodes(start, k);
                if (end == null) {
                    return dummy.next;
                }
                ListNode post = end.next;
                reverse(start, end);
                pre.next = end;
                start.next = post;
                // 我的原注释：跳转到下一段的终点。这个终点实际上是之前的start。
                // TODO: 【表述修正】是本组反转后的尾节点，即下一组的前驱，不是下一组终点。
                cur = start;
            }
            return dummy.next;
        }

        /**
         * 从start开始数k个节点：足够则返回第k个节点，不足则返回null。
         * start已算第1个，所以只需移动k-1次；即使计数归零时cur为null，也仍应返回null。
         */
        private ListNode countKNodes(ListNode start, int k) {
            ListNode cur = start;
            k--;
            // TODO: 【错误-超级错误】一开始写的是while(k-- > 0 && cur != null)。
            // 节点充裕时，最后一次条件判断用0比较后仍会自减，退出时k=-1，而不是0。
            // 因此原来的return k == 0 ? cur : null会把完整分组误判为不足k个。
            // 我的原理解：我们预期k=3成功执行三次后，k==0。
            // TODO: 【计数修正】本方法先k--，原始k=3只需移动两次；第三次条件检查不应修改k。
            // 【解决方案】以后不要再inline了，分开写：条件只判断，循环体末尾再k--。
            // 补充：不是所有内联自减都有错，而是退出后还依赖计数值时，要明确最后一次求值的副作用。
            while (k > 0 && cur != null) {
                cur = cur.next;
                k--;
            }
            return k == 0 ? cur : null;
        }

        private void reverse(ListNode head, ListNode end) {
            ListNode cur = head;
            // TODO: 【旧错复盘】end.next会被改写，必须提前保存终止节点。
            ListNode des = end.next;
            ListNode pre = null;
            while (cur != des) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
        }
    }

    /**
     * 2026-08-31 我的四节点变量分组反转实现。
     *
     * <p><b>核心思路：</b>{@code dummy + pre、start、end、endNext}。先把本轮会受到
     * 指针修改影响的四个边界节点保存下来，完成反转后再依据它们的固定语义重新连接：
     * <pre>{@code
     * 反转前：pre -> start -> ... -> end -> endNext
     * 反转后：pre -> end   -> ... -> start -> endNext
     * }
     * 因此{@code end}成为新头，{@code start}成为新尾，下一轮前驱必须更新为{@code start}。
     *
     * <p><b>退出时机：</b>
     * <ul>
     *     <li>{@code while (pre.next != null)}负责所有节点恰好按k倍数处理完成后的正常终止。</li>
     *     <li>{@code countK(start, k) == null}负责识别末尾不足k个节点，并保持它们原有顺序。</li>
     * </ul>
     * 只有{@code countK}能够区分“完整k个且end恰好是尾节点”和“不足k个”：两种情况下都可能
     * 看到链表尾部，所以不能使用{@code end.next == null}判断分组是否完整。
     *
     * <p><b>有限区间反转：</b>必须在修改指针前把{@code end.next}保存到稳定变量
     * {@code endNext}。反转过程中{@code end.next}本身会改变，不能把动态表达式
     * {@code end.next}直接放入循环终止条件。标准循环判断当前节点：
     * {@code while (cur != endNext)}，而不是判断{@code cur.next}。
     */
    public class Solution20260831 {

        public ListNode reverseKGroup(ListNode head, int k) {
            ListNode dummy = new ListNode(0);
            dummy.next = head;
            ListNode pre = dummy;

            while (pre.next != null) {
                ListNode start = pre.next;
                ListNode end = countK(start, k);

                // TODO: 【错误-遗漏】不能使用end.next == null判断“不足k个”。
                // 当本组恰好有k个并且end正好是链表尾节点时，end.next同样为null。
                // 必须让countK仅在不足k个时返回null。
                if (end == null) {
                    break;
                }

                ListNode endNext = end.next;
                reverse(start, end);

                pre.next = end;
                start.next = endNext;
                pre = start;
            }
            return dummy.next;
        }

        /**
         * 从head开始数k个节点：完整k个时返回第k个节点end，不足k个时返回null。
         */
        public ListNode countK(ListNode head, int k) {
            k--;
            while (head.next != null && k > 0) {
                head = head.next;
                k--;
            }
            return k > 0 ? null : head;
        }

        public ListNode reverse(ListNode head, ListNode end) {
            ListNode pre = null;
            ListNode cur = head;

            // TODO: 【超级错误修正】end.next在反转过程中会改变，必须在修改指针前冻结边界。
            ListNode endNext = end.next;
            while (cur != endNext) {
                // TODO: 【边界判断修正】判断cur是否到达固定终点，不能写cur.next != endNext。
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }
    }

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
