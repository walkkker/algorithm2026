package frequence.LinkedList;

/**
 * 2. 两数相加。
 *
 * <p><b>专题归类：</b>多链表同步遍历、进位状态、{@code dummy + tail}结果链构造。
 * 复盘参见同目录《链表通用技巧与题型分类.md》的“基础遍历与构造”以及《链表错题本.md》的Q2条目。
 */
public class Q2 {

    /**
     * 2026-08-31 我的同步遍历实现。
     *
     * <p><b>核心结构：</b>表示和构造一条链表，需要同时维护头部入口和尾部追加位置。
     * {@code dummy.next}是结果头部，{@code end}始终指向结果链表当前尾节点。
     *
     * <p><b>本次错误：</b>循环体中只顾着移动输出尾指针{@code end}，遗漏了输入指针
     * {@code l1、l2、rest}的移动。输出指针推进只表示“结果写入完成”，并不表示输入节点已经
     * 被消费；如果输入指针不移动，下一轮仍会重复处理同一节点，最终形成死循环。
     *
     * <p><b>循环不变量：</b>{@code dummy.next...end}保存所有已经计算完成的低位；
     * {@code l1、l2}或{@code rest}指向尚未消费的下一位；{@code add}表示传递给下一位的进位。
     */
    public class Solution20260831 {

        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            /*
             * 表示一个链表，必须有头有尾。
             */
            ListNode dummy = new ListNode(0);
            ListNode end = dummy;
            int cur = 0;
            int add = 0;

            while (l1 != null && l2 != null) {
                cur = (add + l1.val + l2.val) % 10;
                add = (add + l1.val + l2.val) / 10;
                end.next = new ListNode(cur);
                end = end.next;

                // TODO: 【错误-遗漏】只顾end移动，while里面l1、l2的移动不要忘了。
                l1 = l1.next;
                l2 = l2.next;
            }

            ListNode rest = l1 != null ? l1 : l2;
            while (rest != null) {
                cur = (add + rest.val) % 10;
                add = (add + rest.val) / 10;
                end.next = new ListNode(cur);
                end = end.next;

                // TODO: 【错误-遗漏】处理剩余链表时，同样不能忘记推进输入指针。
                rest = rest.next;
            }

            if (add != 0) {
                end.next = new ListNode(1);
                end = end.next;
            }
            return dummy.next;
        }
    }

    /**
     TODO：【错误点】又是while最后忘了 移动链表。 （while里面逻辑一多，就容易忘记移动链表）

     */
    class Solution {
        public ListNode addTwoNumbers(ListNode s1, ListNode s2) {
            if (s1 == null && s2 == null) {
                return null;
            }
            if (s1 == null) {
                return s2;
            }
            if (s2 == null) {
                return s1;
            }

            int cur = 0;
            int add = 0;
            ListNode cur1 = s1;
            ListNode cur2 = s2;
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;
            while (cur1 != null && cur2 != null) {
                cur = (cur1.val + cur2.val + add) % 10;
                add = (cur1.val + cur2.val + add) / 10;
                tail.next = new ListNode(cur);
                tail = tail.next;

                // TODO: 【错误】【超级错误！！】遍历链表呀！！！ 一定不能忘了
                cur1 = cur1.next;
                cur2 = cur2.next;
            }

            while (cur1 != null) {
                cur = (cur1.val + add) % 10;
                add = (cur1.val + add) / 10;
                tail.next = new ListNode(cur);
                tail = tail.next;
                // TODO: 【错误】【超级错误！！】遍历链表呀！！！ 一定不能忘了
                cur1 = cur1.next;
            }

            while (cur2 != null) {
                cur = (cur2.val + add) % 10;
                add = (cur2.val + add) / 10;
                tail.next = new ListNode(cur);
                tail = tail.next;
                // TODO: 【错误】【超级错误！！】遍历链表呀！！！ 一定不能忘了
                cur2 = cur2.next;
            }

            if (add == 1) {
                tail.next = new ListNode(1);
            }

            return dummy.next;
        }

    }
}
