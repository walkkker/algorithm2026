package frequence.LinkedList;

/**
 * 138. 随机链表的复制
 *
 * <p>给定一个链表，每个节点除 {@code next} 指针外，还包含一个 {@code random} 指针；
 * {@code random} 可以指向链表中的任意节点或 {@code null}。构造该链表的深拷贝并返回新链表头结点。
 * 新链表中的所有节点都必须是新创建的节点，且节点值及 {@code next}/{@code random} 连接关系
 * 应与原链表一致。
 *
 * <p><b>专题归类：</b>深拷贝、原新节点映射、穿插节点编码映射以及最终拆链恢复。
 * 复盘参见同目录《链表通用技巧与题型分类.md》和《链表错题本.md》的Q138条目。
 */
public class Q138_CopyListWithRandomPointer {

    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }

        Node cur = head;
        while (cur != null) {
            Node next = cur.next;
            cur.next = new Node(cur.val);
            cur.next.next = next;
            cur = next;
        }

        cur = head;
        while (cur != null) {
            if (cur.random == null) {
                cur.next.random = null;
            } else {
                cur.next.random = cur.random.next;
            }
            cur = cur.next.next;
        }

        Node newHead = head.next;
        Node newTail = newHead;
        Node tail = head;
        cur = head.next.next;
        while (cur != null) {
            Node next = cur.next;
            Node nextNext = cur.next.next;
            tail.next = cur;
            tail = tail.next;
            newTail.next = next;
            newTail = newTail.next;
            cur = nextNext;
        }
        tail.next = null;
        newTail.next = null;
        return newHead;
    }

    public static class Node {
        int val;
        Node next;
        Node random;

        Node(int val) {
            this.val = val;
        }
    }
}
