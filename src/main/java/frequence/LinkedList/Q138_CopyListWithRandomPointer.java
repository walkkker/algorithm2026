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

    /**
     * 2026-08-31 我的穿插节点深拷贝实现。
     *
     * <p><b>三步：</b>
     * <ol>
     *     <li>在每个原节点后插入对应复制节点，形成{@code original -> copy -> nextOriginal}。</li>
     *     <li>依据原节点的{@code random}为复制节点设置{@code random}。</li>
     *     <li>拆分穿插链表，恢复原链表并提取复制链表，最后明确清理两个尾节点的{@code next}。</li>
     * </ol>
     *
     * <p><b>映射关系：</b>穿插完成后，任意原节点{@code original}对应的复制节点就是
     * {@code original.next}。因此，当{@code original.random != null}时：
     * <pre>{@code
     * copy.random = original.random.next;
     * }
     *
     * <p><b>本次错误：</b>{@code random}根据题意既可能指向节点，也可能为{@code null}。
     * 不能无条件读取{@code cur.random.next}；必须先判断{@code cur.random == null}，否则会
     * 触发NullPointerException。这属于通用的“解引用前检查可空引用”错误。
     */
    public class Solution20260831 {

        public Node copyRandomList(Node head) {
            Node cur = head;

            // 第一步：在每个原节点后面插入对应的复制节点。
            while (cur != null) {
                Node next = cur.next;
                cur.next = new Node(cur.val);
                cur.next.next = next;
                cur = next;
            }

            // 第二步：利用original.next == copy建立复制节点的random连接。
            cur = head;
            while (cur != null) {
                Node copy = cur.next;

                // TODO: 【错误】random允许为null，不能直接执行copy.random = cur.random.next。
                // 必须先分类讨论，否则访问null.next会触发NullPointerException。
                if (cur.random == null) {
                    copy.random = null;
                } else {
                    copy.random = cur.random.next;
                }
                cur = cur.next.next;
            }

            // 第三步：使用两组dummy/end分别重建原链表和复制链表。
            Node dummy = new Node(0);
            Node copyDummy = new Node(0);
            Node end = dummy;
            Node copyEnd = copyDummy;
            cur = head;
            while (cur != null) {
                end.next = cur;
                copyEnd.next = cur.next;
                end = end.next;
                copyEnd = copyEnd.next;
                cur = cur.next.next;
            }

            // TODO: 【结构收尾】拆链完成后明确终止两条链，避免残留穿插连接或意外成环。
            end.next = null;
            copyEnd.next = null;
            return copyDummy.next;
        }
    }

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
