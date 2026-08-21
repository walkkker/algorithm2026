package BinaryTree.TraversalAndMorris;

import java.util.Stack;


/**
 * 核心逻辑：都用到栈（pos用两个）。 栈弹出元素时，代表打印元素之时。
 * <p>
 * 前序：一个栈。弹出时，打印->先压right child->再压left child。 顺序实现 头，左，右
 * <p>
 * 后序：两个栈。 前序压栈顺序反过来（先左后右），此时弹出顺序为【头右左】。 弹出时压入栈2，实现【左右头】。
 * <p>
 * 中序：（1）只要cur!=null，则左边界压栈。 （2）弹出元素打印，然后cur=cur.right。
 */
public class UnRecursiveTraversalBT {

    public static class Node {
        int val;
        Node left;
        Node right;

        public Node(int v) {
            val = v;
        }
    }

    public static void pre(Node head) {
        if (head == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(head);
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            System.out.println(cur.val);
            // 压入时，先右后走  -> 弹出时，才能先左后右   实现 头左右遍历
            if (cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.left != null) {
                stack.push(cur.left);
            }
        }
    }


    public static void pos(Node head) {
        if (head == null) {
            return;
        }

        Stack<Node> stack1 = new Stack<>();
        Stack<Node> stack2 = new Stack<>();
        stack1.push(head);
        while (!stack1.isEmpty()) {
            Node cur = stack1.pop();
            stack2.push(cur);
            if (cur.left != null) {
                stack1.push(cur.left);
            }
            if (cur.right != null) {
                stack1.push(cur.right);
            }
        }
        while (!stack2.isEmpty()) {
            Node cur = stack2.pop();
            System.out.println(cur.val);
        }
    }


    public static void in(Node head) {
        if (head == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                // TODO: 这个地方可以优化为下面两行，不用while
//                while (cur != null) {
//                    stack.push(cur);
//                    cur = cur.left;
//                }
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                System.out.println(cur.val);
                cur = cur.right;
            }
        }
    }
    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);

        pre(head);
        System.out.println("========");
        in(head);
        System.out.println("========");
        pos(head);
        System.out.println("========");
    }


}
