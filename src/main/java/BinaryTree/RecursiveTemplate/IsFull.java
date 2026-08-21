package BinaryTree.RecursiveTemplate;


// left.isFull && right.isFull && left.height == right.height

// For BaseCase==null, can construct Info
public class IsFull {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    public static class Info {
        boolean isFull;
        int height;

        public Info(boolean _isFull, int _height) {
            isFull = _isFull;
            height = _height;
        }
    }

    // semantics: return the Info for the whole sub-tree using head as the root
    public static Info process(Node head) {
        if (head == null) {
            return new Info(true, 0);
        }
        Info left = process(head.left);
        Info right = process(head.right);

        boolean isFull = false;
        int height;

        if (left.isFull && right.isFull && left.height == right.height) {
            isFull = true;
        }
        height = Math.max(left.height, right.height) + 1;
        return new Info(isFull, height);
    }

    public static boolean isFull2(Node root) {
        if (root == null) {
            return true;
        }
        return process(root).isFull;
    }

    // 第一种方法
    // 收集整棵树的高度h，和节点数n
    // 只有满二叉树满足 : 2 ^ h - 1 == n
    public static boolean isFull1(Node head) {
        if (head == null) {
            return true;
        }
        Info1 all = process1(head);
        // 1左移n位  就是 2^n
        return (1 << all.height) - 1 == all.nodes;
        // 建议下面这个，直接使用 系统提供的函数 Math.pow()
//		return (Math.pow(2, all.height) - 1) == all.nodes;
    }

    public static class Info1 {
        public int height;
        public int nodes;

        public Info1(int h, int n) {
            height = h;
            nodes = n;
        }
    }

    public static Info1 process1(Node head) {
        if (head == null) {
            return new Info1(0, 0);
        }
        Info1 leftInfo = process1(head.left);
        Info1 rightInfo = process1(head.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        int nodes = leftInfo.nodes + rightInfo.nodes + 1;
        return new Info1(height, nodes);
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
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            if (isFull1(head) != isFull2(head)) {
                System.out.println("出错了!");
            }
        }
        System.out.println("测试结束");
    }




}
