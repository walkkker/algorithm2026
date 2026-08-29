package frequence.LinkedList;

/**
 * 2. 两数相加。
 *
 * <p><b>专题归类：</b>多链表同步遍历、进位状态、{@code dummy + tail}结果链构造。
 * 复盘参见同目录《链表通用技巧与题型分类.md》的“基础遍历与构造”以及《链表错题本.md》的Q2条目。
 */
public class Q2 {
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
