package linkedList;

import java.util.ArrayList;

/**
 * 中或上中
 * 中或下中
 * 中或上中的前一个
 * 中或下中的前一个
 */
public class LinkedListMid {

    public static ListNode getMidOrUpMid(ListNode head) {
        // <= 2个节点时，都是返回head
        if (head == null || head.next == null || head.next.next == null) {
            return head;
        }

        ListNode slow = head.next;
        ListNode fast = head.next.next;

        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


    public static ListNode getMidOrDownMid(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head.next;
        ListNode fast = head.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public static ListNode getMidOrUpMidPre (ListNode head) {
        // 这里改了 相对于not pre版本
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }

        ListNode slow = head;
        ListNode fast = head.next.next;

        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public static ListNode getMidOrDownMidPre(ListNode head) {
        // 这里改了
        if (head == null || head.next == null) {
            return null;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


    public static ListNode right1(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode cur = head;
        ArrayList<ListNode> arr = new ArrayList<>();
        while (cur != null) {
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size() - 1) / 2);
    }

    public static ListNode right2(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode cur = head;
        ArrayList<ListNode> arr = new ArrayList<>();
        while (cur != null) {
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get(arr.size() / 2);
    }

    public static ListNode right3(ListNode head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        ListNode cur = head;
        ArrayList<ListNode> arr = new ArrayList<>();
        while (cur != null) {
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size() - 3) / 2);
    }

    public static ListNode right4(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        ListNode cur = head;
        ArrayList<ListNode> arr = new ArrayList<>();
        while (cur != null) {
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size() - 2) / 2);
    }

    public static void main(String[] args) {
        ListNode test = null;
        test = new ListNode(0);
        test.next = new ListNode(1);
        test.next.next = new ListNode(2);
        test.next.next.next = new ListNode(3);
        test.next.next.next.next = new ListNode(4);
        test.next.next.next.next.next = new ListNode(5);
        test.next.next.next.next.next.next = new ListNode(6);
        test.next.next.next.next.next.next.next = new ListNode(7);
        test.next.next.next.next.next.next.next.next = new ListNode(8);

        ListNode ans1 = null;
        ListNode ans2 = null;

        ans1 = getMidOrUpMid(test);
        ans2 = right1(test);
        System.out.println(ans1 != null ? ans1.value : "无");
        System.out.println(ans2 != null ? ans2.value : "无");

        ans1 = getMidOrDownMid(test);
        ans2 = right2(test);
        System.out.println(ans1 != null ? ans1.value : "无");
        System.out.println(ans2 != null ? ans2.value : "无");

        ans1 = getMidOrUpMidPre(test);
        ans2 = right3(test);
        System.out.println(ans1 != null ? ans1.value : "无");
        System.out.println(ans2 != null ? ans2.value : "无");

        ans1 = getMidOrDownMidPre(test);
        ans2 = right4(test);
        System.out.println(ans1 != null ? ans1.value : "无");
        System.out.println(ans2 != null ? ans2.value : "无");

    }



}
