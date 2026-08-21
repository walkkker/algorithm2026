package linkedList;

// 测试链接 : https://leetcode.com/problems/copy-list-with-random-pointer/

/**
 * 其实就三步，核心还是 Node cur = head, while(cur != null) 的链表遍历 + head+tail的链表拆分
 *
 * 步骤：
 *   (1) 遍历节点，再cur 与 next之间插入 copyNode
 *   (2) 遍历节点（一次两跳），根据cur.random 修改cur.next.random
 *   (3) 链表拆分，新旧链表（对应两组head+tail），【注意拆分的易错点！！！】最后一定要设置 tail.next=null
 */
public class CopyListWithRandom {

    public static class Node {
        public int val;
        public Node next;
        public Node random;

        public Node(int val) {
            this.val = val;
        }
    }


    public static Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        // S1: 第一次遍历，挂上copyNode
        while (cur != null) {
            Node next = cur.next;
            Node copy = new Node(cur.val);
            cur.next = copy;
            copy.next = next;
            cur = next;
        }

        // S2： 第二次遍历，给copyNode挂random pointer
        // 这里要注意，有些节点的random是可以指向null的，这个要检查
        cur = head;
        while (cur != null) {
            cur.next.random = cur.random == null ? null : cur.random.next;
            cur = cur.next.next;
        }


        // S3: 拆链 - 这块跟 链表分割三分区一样， Head + Tail
        Node copyHead = head.next;
        Node copyTail = head.next;
        Node tail = head;
        cur = head.next.next;
        while (cur != null) {
            tail.next = cur;
            tail = tail.next;           // TODO: 尾节点添加节点后，别忘了移动尾节点！！！
            copyTail.next = cur.next;
            copyTail = copyTail.next;   // TODO: 尾节点添加节点后，别忘了移动尾节点！！！
            cur = cur.next.next;
        }

        // TODO: 放在while里面，每一步都清空的话，不多申请Next, NextNext变量的话，会出现修改了tail.next=null后导致cur.next.next=null的问题。因为你不能访问一个已经被修改的节点，这样会出现预期外的访问。
        //  所以，结论：统一在while外面处理，最后的尾节点的next指针即可。前面的在挂链过程中，next指针都自动被修改了。
        // 最后注意清理尾指针
        tail.next = null;
        copyTail.next = null;
        return copyHead;
    }


}
