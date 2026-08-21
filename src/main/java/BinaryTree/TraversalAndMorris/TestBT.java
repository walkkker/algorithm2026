package BinaryTree.TraversalAndMorris;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class TestBT {
    // levelTraverse, RecursiveTraverse(pre, in, post), UnRecursiveTraverse(pre, in, post)
    public static class Node {
        int value;
        Node left;
        Node right;

        public Node(int _value) {
            value = _value;
        }
    }

    public static void levelTraverse(Node root) {
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            System.out.println(cur.value);
            if (cur.left != null) {
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            }
        }
    }

    public static void pre(Node head) {
        if (head == null) {
            return;
        }
        System.out.println(head.value);
        pre(head.left);
        pre(head.right);
    }

    public static void in(Node head) {
        if (head == null) {
            return;
        }
        in(head.left);
        System.out.println(head.value);
        in(head.right);
    }

    public static void post(Node head) {
        if (head == null) {
            return;
        }

        post(head.left);
        post(head.right);
        System.out.println(head.value);
    }

    /**
     * UnRecursive BT Traverse
     **/
    public static void UnrecursivePre(Node head) {
        if (head == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(head);
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            System.out.println(cur.value);
            if (cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.left != null) {
                stack.push(cur.left);
            }
        }
    }

    public static void UnrecursivePost(Node head) {
        if (head == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        Stack<Node> ans = new Stack<>();
        stack.push(head);
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            ans.push(cur);
            if (cur.left != null) {
                stack.push(cur.left);
            }
            if (cur.right != null) {
                stack.push(cur.right);
            }
        }
        while (!ans.isEmpty()) {
            Node cur = ans.pop();
            System.out.println(cur.value);
        }
    }
    
    
    public static void UnrecursiveIn(Node head) {
        if (head == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                System.out.println(cur.value);
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
        post(head);
        System.out.println("========");

        UnrecursivePre(head);
        System.out.println("========");
        UnrecursiveIn(head);
        System.out.println("========");
        UnrecursivePost(head);
        System.out.println("========");

    }
}

