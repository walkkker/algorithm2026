package linkedList;

import java.util.Stack;

/**
 * 三种方式实现：with 空间复杂度 N, N/2, 1
 * 栈；栈with一半节点；反转链表
 * https://leetcode.cn/problems/aMhZSa/description/
 *
 *
 * TODO：易错！！！ base case问题，一定要避免NPE  （涉及到 快慢指针 ， 中或下中）
 *
 */
public class IsPalindromeList {
    class Solution {
        public boolean isPalindrome(ListNode head) {
            // TODO: 【超级错误点】这个是必须考虑的base case。  因为【中或下中】起始点为head.next。
            if (head.next == null) {
                return true;
            }


            // 中和下中： TODO： 【特别注意】起始点一定要防止是Null     head.next
            // TODO: 【超级易错点】选择或下中时候，因为起始点是 head.next，特别容易遗漏空指针问题！！！
            //    所以！！！ 对于中或下中的问题，因为要在base case里面直接 包含进去？
            //  所谓的base case： 因为后面正文的前提条件是head.next!=null， 所以base case只需要考虑 head==null 以及 head.next == null。
            ListNode f = head.next;
            ListNode s = head.next;
            while (f.next != null && f.next.next != null) {
                s = s.next;
                f = f.next.next;
            }
            ListNode tail = reverse(s);
            ListNode cur1 = head;
            ListNode cur2 = tail;
            while (cur1 != null && cur2 != null) {
                if (cur1.val != cur2.val) {
                    return false;
                } else {
                    cur1 = cur1.next;
                    cur2 = cur2.next;
                }
            }
            reverse(tail);
            return true;
        }

        public ListNode reverse(ListNode head) {
            ListNode cur = head;
            ListNode pre = null;
            ListNode next;
            while (cur != null) { // 本质就是 建立单独的引用，不再依赖.next指针
                next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }

    }

    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    // S1： 回文序列 ，前后对称，所以正着读反着读 都一样。所以把Node节点放到栈里，相当于 头跟尾比较
    // Q: 但是你用stack的话，不也可以直接装到arrayList里面，然后l,r双指针比对？
    public static boolean isPalindrome1(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        Stack<ListNode> stack = new Stack<>();
        ListNode cur = head;
        while (cur != null) {
            stack.push(cur);
            cur = cur.next;
        }
        cur = head;
        while (cur != null) {
            if (cur.val != stack.pop().val) {
                return false;
            }
            cur = cur.next;
        }
        return true;
    }


    // S2： 回文序列 前后对称。 寻找mid or downMid。 将后面的节点加入到Stack里面。 从而可以实现类比l ,r双指针的比对
    // O（N/2）空间。 因为stack只存 后半段Node
    public static boolean isPalindrome2(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        // 先找 mid or downMid
        ListNode slow = head.next;
        ListNode fast = head.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            // TODO: 写错了！！！ fast = fast.next;
            fast = fast.next.next;
        }
        ListNode cur = slow;
        Stack<ListNode> stack = new Stack<>();
        while (cur != null) {
            stack.push(cur);
            cur = cur.next;
        }
        cur = head;
        while (stack.size() != 0) {
            if (cur.val != stack.pop().val) {
                return false;
            }
            cur = cur.next;
        }
        return true;
    }

    // 这个是面试解法：最优解
    // 涉及修改链表，检查完之后再恢复回去。 因为是完全在链表上操作，所以空间复杂度O(1)
    public static boolean isPalindrome3(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        // 找到 mid or downMid ， 因为要对next反转
        ListNode slow = head.next;
        ListNode fast = head.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode end = reverseList(slow);
        ListNode l = head;
        ListNode r = end;
        boolean ans = true;
        while (r != null) {
            if (l.val != r.val) {
                // 不能直接return，因为最后还要把 后半段逆转回来
                ans = false;
                break;
            }
            l = l.next;
            r = r.next;
        }
        reverseList(end);
        return ans;
    }

    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }


    public static void printLinkedList(ListNode ListNode) {
        System.out.print("Linked List: ");
        while (ListNode != null) {
            System.out.print(ListNode.val + " ");
            ListNode = ListNode.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {

        ListNode head = null;
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new ListNode(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new ListNode(1);
        head.next = new ListNode(2);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new ListNode(1);
        head.next = new ListNode(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(2);
        head.next.next.next = new ListNode(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(2);
        head.next.next.next.next = new ListNode(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

    }

}
