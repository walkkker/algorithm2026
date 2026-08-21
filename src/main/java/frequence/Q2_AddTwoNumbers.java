package frequence;

public class Q2_AddTwoNumbers {

    public static class ListNode {
        int val;
        ListNode next;

        public ListNode(int _value) {
            val = _value;
        }
    }

    public static ListNode addTwoNumbers(ListNode s1, ListNode s2) {
        if (s1 == null && s2 == null) {
            return null;
        }
        if (s1 == null) {
            return s2;
        }
        if (s2 == null) {
            return s1;
        }

        // TODO：【解题关键】下面两个变量是关键。 一个是当前值，一个是进位。  （一定要注意 cur的计算要 考虑到进位，如下代码）
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
