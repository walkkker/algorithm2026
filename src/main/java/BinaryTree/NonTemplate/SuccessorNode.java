package BinaryTree.NonTemplate;

/**
 * 中序遍历的后继结点
 * <p>
 * (1) node.right != null -> 取右数最左节点 作为后继
 * (2) node.right == null -> 把node 作为 左树最右节点的 头，为后继
 */
public class SuccessorNode {
    public static class Node {
        public int value;
        public Node left;
        public Node right;
        public Node parent;

        public Node(int data) {
            this.value = data;
        }
    }

    public static Node getSuccessorNode(Node node) {
        if (node == null) {
            return null;
        }

        // S1: 右子树最左节点
        if (node.right != null) {
            Node cur = node.right;
            while (cur.left != null) {
                cur = cur.left;
            }
            return cur;
        } else {
            // S2: 当前节点作为左树最右节点的 头结点

            // 如果parent!=null && parent.right == node，那么一直往上跳
            while (node.parent != null && node.parent.right == node) {
                node = node.parent;
            }
            // 退出循环存在两种情况：（1）parent==null (2) parent!=null && node.parent.left == node
            // 此时按理说应该分类讨论return值的，但是会发现(1)应该返回的nul，其实==Parent。  所以可以将(1) (2)合并，统一返回parent就可以了。
            return node.parent;
        }
    }

    public static void main(String[] args) {
        Node head = new Node(6);
        head.parent = null;
        head.left = new Node(3);
        head.left.parent = head;
        head.left.left = new Node(1);
        head.left.left.parent = head.left;
        head.left.left.right = new Node(2);
        head.left.left.right.parent = head.left.left;
        head.left.right = new Node(4);
        head.left.right.parent = head.left;
        head.left.right.right = new Node(5);
        head.left.right.right.parent = head.left.right;
        head.right = new Node(9);
        head.right.parent = head;
        head.right.left = new Node(8);
        head.right.left.parent = head.right;
        head.right.left.left = new Node(7);
        head.right.left.left.parent = head.right.left;
        head.right.right = new Node(10);
        head.right.right.parent = head.right;

        Node test = head.left.left;
        System.out.println(test.value + " next: " + successorNode(test).value);
        test = head.left.left.right;
        System.out.println(test.value + " next: " + successorNode(test).value);
        test = head.left;
        System.out.println(test.value + " next: " + successorNode(test).value);
        test = head.left.right;
        System.out.println(test.value + " next: " + successorNode(test).value);
        test = head.left.right.right;
        System.out.println(test.value + " next: " + successorNode(test).value);
        test = head;
        System.out.println(test.value + " next: " + successorNode(test).value);
        test = head.right.left.left;
        System.out.println(test.value + " next: " + successorNode(test).value);
        test = head.right.left;
        System.out.println(test.value + " next: " + successorNode(test).value);
        test = head.right;
        System.out.println(test.value + " next: " + successorNode(test).value);
        test = head.right.right; // 10's next is null
        System.out.println(test.value + " next: " + successorNode(test));
    }

    public static Node successorNode(Node head) {
        if (head == null) {
            return null;
        }

        // 1. 右子树的最左节点 2. 当前节点作为左子树最右节点的父节点
        if (head.right != null) {
            // TODO: 【错误点】cur = head.right。 然后在右子树上 找最左节点
            // Node cur = head;
            Node cur = head.right;
            while (cur.left != null) {
                cur = cur.left;
            }
            return cur;
        } else {
            Node cur = head;
            while (cur.parent != null && cur.parent.right == cur) {
                cur = cur.parent;
            }
            return cur.parent;
        }
    }


}
