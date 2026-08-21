package linkedList;

/**
 * 【重点】可能换头; 删除一个节点一定需要preNode；     不需要preNode花招那个，cur.value = cur.next.value，然后删除cur.next节点，只是小trick。生产上会有问题
 * 【Solution】
 * 1. 不用dummyHead，先处理头，在处理后面
 * 2. DummyHead，所有节点可以统一处理
 * https://leetcode.cn/problems/shan-chu-lian-biao-de-jie-dian-lcof/description/
 */
public class DeleteGivenNode {

    public static class Node {
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    // head = removeValue(head, 2);
    // 注意可能换头
    public static Node removeValue(Node head, int value) {
        if (head == null) {
            return head;
        }
        Node cur = head;
        while (cur != null && cur.value == value) {
            cur = cur.next;
        }

        if (cur == null) {
            return null;
        }

        // 此时，（新）头结点不为空，且 new_head.value != value
        head = cur;
        Node pre = head;
        cur = cur.next;
        while (cur != null) {
            if (cur.value == value) {
                pre.next = cur.next;
                cur = cur.next;
            } else {
                pre = cur;
                cur = cur.next;
            }
        }
        return head;
    }


    // 因为可能换头，为了统一处理，添加一个DummyHead
    public static Node removeValueWithDummy(Node head, int value) {
        if (head == null) {
            return null;
        }

        Node dummy = new Node(0);
        dummy.next = head;
        head = dummy;

        Node pre = dummy;
        Node cur = dummy.next;
        while (cur != null) {
            if (cur.value == value) {
                pre.next = cur.next;
                cur = cur.next;
            } else {
                pre = cur;
                cur = cur.next;
            }
        }
        return dummy.next;
    }


}
