package linkedList;

/**
 * 160. Intersection of Two Linked Lists（两个无环单链表的交点）
 * 142. Linked List Cycle II（判断链表是否有环并返回入环节点）
 */
public class FindFirstIntersectNode {

    // 主函数：先判断两个链表的入环节点。 （有，有）或者 （无，无）才进一步讨论。 否则，直接判断无相交节点
    public static ListNode getIntersectNode(ListNode head1, ListNode head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        ListNode loop1 = findLoopNode(head1);
        ListNode loop2 = findLoopNode(head2);
        if (loop1 == null && loop2 == null) {    // 无环链表相交问题
            return noLoop(head1, head2);
        } else if (loop1 != null && loop2 != null) {  // 有环链表相交问题
            return bothLoop(head1, loop1, head2, loop2);
        } else {     // 一个有环，一个无环，一定不相交
            return null;
        }


    }


    // S1: 找到链表第一个入环节点，如果无环，返回null
    // 快慢指针相遇证明有环，快指针重置head,快慢指针分别一次一步相遇 【即为入环点】
    public static ListNode findLoopNode(ListNode head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        ListNode slow = head.next;
        ListNode fast = head.next.next;

        while (slow != fast) {
            if (fast.next == null || fast.next.next == null) {
                return null;     // fast跳跃过程中，发现无环
            }
            slow = slow.next;
            fast = fast.next.next;
        }

        // 退出循环，意味着slow fast相遇。 此时'fast' set to 'head'
        fast = head;

        // 快慢指针 各走一步，相遇即为入环点
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    // S2: 已经判断两个链表都无环，判断第一个相交节点。无则返回null
    // 先要判断有无相交；有的话，让长的先走，然后长短一起走 边检查
    public static ListNode noLoop(ListNode head1, ListNode head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        ListNode cur1 = head1;
        ListNode cur2 = head2;
        int n = 0;
        // 退出循环时，到达最后一个节点
        while (cur1.next != null) {
            n++;
            cur1 = cur1.next;
        }

        while (cur2.next != null) {
            n--;
            cur2 = cur2.next;
        }

        // 最后一个节点都不相交，则两个链表必定不相交
        if (cur1 != cur2) {
            return null;
        }

        ListNode longNode = n >= 0 ? head1 : head2;
        ListNode shortNode = longNode == head1 ? head2 : head1;
        n = Math.abs(n);
        // longNode先走多出来的n步。
        while (n > 0) {
            longNode = longNode.next;
            n--;
        }

        // 至此,longNode 与 shortNode到达尾节点的长度相同
        while (longNode != shortNode) {
            longNode = longNode.next;
            shortNode = shortNode.next;
        }
        return longNode;
    }


    // S3: 已经判断两个链表有环，判断第一个相遇节点。 无则返回Null
    // 该情形存在三种情况： 两环不相交，相交于环外（主要处理这个，其实可以看作是无环节点的相交问题），相交于环内
    public static ListNode bothLoop(ListNode head1, ListNode loop1, ListNode head2, ListNode loop2) {
        if (loop1 == loop2) {     // 相交于环外
            int n = 0;
            ListNode cur1 = head1;
            ListNode cur2 = head2;
            while (cur1 != loop1) {
                n++;
                cur1 = cur1.next;
            }
            while (cur2 != loop1) {
                n--;
                cur2 = cur2.next;
            }

            ListNode longNode = n >= 0 ? head1 : head2;
            ListNode shortNode = longNode == head1 ? head2 : head1;
            n = Math.abs(n);
            while (n-- > 0) {
                longNode = longNode.next;
            }
            while (longNode != shortNode) {
                longNode = longNode.next;
                shortNode = shortNode.next;
            }
            return longNode;
        } else {
            ListNode find = loop1.next;
            while (find != loop1) {
                if (find == loop2) {
                    return loop2;
                }
                find = find.next;
            }
            return null;
        }
    }


    // 补充题目1： 无环链表的相交节点
    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode(int x) {
     *         val = x;
     *         next = null;
     *     }
     * }
     */
    public class Solution {

        public ListNode getIntersectionNode(ListNode head1, ListNode head2) {
            // TODO: 【错误点1】两个链表，注意 head1, tail1, cur1都区分开
            ListNode tail1 = head1;
            ListNode cur1 = head1;
            int len1 = 0;
            while (cur1 != null) {
                len1++;
                tail1 = cur1;
                cur1 = cur1.next;
            }

            ListNode tail2 = head2;
            ListNode cur2 = head2;
            int len2 = 0;
            while (cur2 != null) {
                len2++;
                tail2 = cur2;
                cur2 = cur2.next;
            }

            if (tail1 != tail2) {
                return null;
            }

            // TODO: 【超级错误点】ListNode long 是完全错误的！！！ 不能把关键字作为变量名！！！
            ListNode l;
            ListNode s;
            if (len1 > len2) {
                l = head1;
                s = head2;
            } else {
                l = head2;
                s = head1;
            }

            int diff = Math.abs(len1 - len2);
            while (diff-- > 0) {
                l = l.next;
            }
            while (l != s) {
                l = l.next;
                s = s.next;
            }
            return l;
        }
    }

}
