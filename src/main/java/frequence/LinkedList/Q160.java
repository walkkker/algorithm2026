package frequence.LinkedList;

/**
 * 相交节点：有错误！！！   遍历完len后，需要重新对cur1,cur2赋值。
 *
 * <p><b>专题归类：</b>长度对齐、指针换道和节点引用身份比较。复盘参见同目录
 * 《链表通用技巧与题型分类.md》和《链表错题本.md》的Q160条目。
 */
public class Q160 {

    public class Solution {
        public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            if (headA == null || headB == null) {
                return null;
            }


            int len = 0;
            ListNode cur1 = headA;
            ListNode cur2 = headB;
            while (cur1 != null) {
                len++;
                cur1 = cur1.next;
            }
            while (cur2 != null) {
                len--;
                cur2 = cur2.next;
            }

            // TODO: 【超级错误-遗漏】cur1, cur2遍历完len后。需要cur1,cur2重新从头走，一定要重新赋值！！！
            // cur1 = len >= 0 ? cur1 : cur2;
            cur1 = len >= 0 ? headA : headB;
            cur2 = cur1 == headA ? headB : headA;
            len = Math.abs(len);

            while (len-- > 0) {
                cur1 = cur1.next;
            }

            while (cur1 != null && cur2 != null) {
                if (cur1 == cur2) {
                    return cur1;
                }
                cur1 = cur1.next;
                cur2 = cur2.next;
            }
            return null;
        }
    }
}
