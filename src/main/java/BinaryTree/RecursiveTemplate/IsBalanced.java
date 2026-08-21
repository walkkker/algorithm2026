package BinaryTree.RecursiveTemplate;

// (1) 左右都是balanced
// (2) Math.abs(left.height - right.height) <= 1 。 左右高度差<=1

// base case = null 可以构建 Info。 走BaseCase=null模板。
public class IsBalanced {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    public static class Info {
        public boolean isBalanced;
        public int height;

        public Info(boolean _isBalanced, int _height) {
            isBalanced = _isBalanced;
            height = _height;
        }
    }

    public static boolean isBalanced2(Node head) {
        if (head == null) {
            return true;
        }
        return process(head).isBalanced;
    }

    public static Info process(Node head) {
        if (head == null) {
            return new Info(true, 0);
        }
        Info left = process(head.left);
        Info right = process(head.right);
        boolean isBalanced = false;
        int height;

        if (left.isBalanced && right.isBalanced && Math.abs(left.height - right.height) <= 1) {
            isBalanced = true;
        }

        height = Math.max(left.height, right.height) + 1;
        return new Info(isBalanced, height);
    }

    public static boolean isBalanced1(Node head) {
        boolean[] ans = new boolean[1];
        ans[0] = true;
        process1(head, ans);
        return ans[0];
    }

    public static int process1(Node head, boolean[] ans) {
        if (!ans[0] || head == null) {
            return -1;
        }
        int leftHeight = process1(head.left, ans);
        int rightHeight = process1(head.right, ans);
        if (Math.abs(leftHeight - rightHeight) > 1) {
            ans[0] = false;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }


    // for test
    public static Node generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    // for test
    public static Node generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        Node head = new Node((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }

    public static void main(String[] args) {
        int maxLevel = 5;
        int maxValue = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            if (isBalanced1(head) != isBalanced2(head)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }


}
