package BinaryTree.NonTemplate;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 【二叉树的序列化和反序列化】 序列化一棵树一定要包含Null
 * <p>
 * 就是二叉树的序列化和反序列化： 唯2注意点：
 * （1）叶子节点的null都要包含进去，非常重要。 WHY？ 因为反序列化时，null代表着【停止】。
 * （2）不能用中序，中序不具有唯一性
 * <p>
 * <p>
 * 除此之外，很经典的一道题：
 * 先序  递归
 * 后续 递归，但是 左右头无法处理 ，所以 反序列化时，搞一个栈 变成 头右左，后面拿 栈做递归构建BT
 *  TODO: 层级遍历： 我做错了。 用左神版本的，重复有繁琐的代码就单独写一个函数，整个代码逻辑和清晰度会好很多
 * <p>
 * <p>
 * /*
 * * 二叉树可以通过先序、后序或者按层遍历的方式序列化和反序列化，
 * * 以下代码全部实现了。
 * * 但是，二叉树无法通过中序遍历的方式实现序列化和反序列化
 * * 因为不同的两棵树，可能得到同样的中序序列，即便补了空位置也可能一样。
 * * 比如如下两棵树
 * *         __2
 * *        /
 * *       1
 * *       和
 * *       1__
 * *          \
 * *           2
 * * 补足空位置的中序遍历结果都是{ null, 1, null, 2, null}
 * *
 * *
 */
public class SerializeAndReconstructTree {
    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }


    public static Queue<String> preSerial(Node head) {
        if (head == null) {
            return new LinkedList<>();
        }
        Queue<String> queue = new LinkedList<>();
        pres(head, queue);
        return queue;
    }

    public static void pres(Node cur, Queue<String> queue) {
        if (cur == null) {
            queue.add("null");
            return;
        }
        queue.add(String.valueOf(cur.value));
        pres(cur.left, queue);
        pres(cur.right, queue);
    }


    public static Node buildByPreQueue(Queue<String> queue) {
        if (queue.isEmpty()) {
            return null;
        }
        return preb(queue);
    }

    // 递归含义：给定一个queue，返回以队首元素 为头的 整棵树
    public static Node preb(Queue<String> queue) {
        String val = queue.poll();
        if (val.equals("null")) {
            return null;
        }

        Node cur = new Node(Integer.valueOf(val));
        cur.left = preb(queue);
        cur.right = preb(queue);
        return cur;
    }

    public static Queue<String> levelSerial(Node head) {
        if (head == null) {
            return new LinkedList<>();
        }
        Queue<String> serialQueue = new LinkedList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            serialQueue.add(cur == null ? null : String.valueOf(cur.value));
            if (cur != null) {
                queue.add(cur.left);
                queue.add(cur.right);
            }
        }
        return serialQueue;
    }


    public static Node buildByLevelQueue1(Queue<String> serialQueue) {
        if (serialQueue.isEmpty()) {
            return null;
        }
        Queue<Node> queue = new LinkedList<>();
        Node head = new Node(Integer.valueOf(serialQueue.poll()));
        queue.add(head);
        while (!queue.isEmpty()) {  // 父亲挂孩子， 然后孩子进queue
            Node cur = queue.poll();
            String leftVal = serialQueue.poll();
            cur.left = leftVal == null ? null : new Node(Integer.valueOf(leftVal));
            String rightVal = serialQueue.poll();
            cur.right = rightVal == null ? null : new Node(Integer.valueOf(rightVal));
            if (cur.left != null) {
                queue.add(cur.left);
            }
            if (cur.right != null) {
                queue.add(cur.right);
            }
        }
        return head;
    }


    // TODO: 下面这个是左神版本的， 用左神版本的。 核心逻辑很简单，重建也是在做一次 层级遍历。 遍历到父亲的时候，挂serialList的孩子
    public static Node buildByLevelQueue(Queue<String> levelList) {
        if (levelList == null || levelList.size() == 0) {
            return null;
        }
        Node head = generateNode(levelList.poll());
        Queue<Node> queue = new LinkedList<Node>();
        if (head != null) {
            queue.add(head);
        }
        Node node = null;
        while (!queue.isEmpty()) {
            node = queue.poll();
            node.left = generateNode(levelList.poll());
            node.right = generateNode(levelList.poll());
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        return head;
    }

    public static Node generateNode(String val) {
        if (val == null) {
            return null;
        }
        return new Node(Integer.valueOf(val));
    }


    public static Queue<String> posSerial(Node head) {
        Queue<String> ans = new LinkedList<>();
        poss(head, ans);
        return ans;
    }

    public static void poss(Node head, Queue<String> ans) {
        if (head == null) {
            ans.add(null);
        } else {
            poss(head.left, ans);
            poss(head.right, ans);
            ans.add(String.valueOf(head.value));
        }
    }

    public static Node buildByPosQueue(Queue<String> poslist) {
        if (poslist == null || poslist.size() == 0) {
            return null;
        }
        // 左右中  ->  stack(中右左)
        Stack<String> stack = new Stack<>();
        while (!poslist.isEmpty()) {
            stack.push(poslist.poll());
        }
        return posb(stack);
    }

    public static Node posb(Stack<String> posstack) {
        String value = posstack.pop();
        if (value == null) {
            return null;
        }
        Node head = new Node(Integer.valueOf(value));
        head.right = posb(posstack);
        head.left = posb(posstack);
        return head;
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

    // for test
    public static boolean isSameValueStructure(Node head1, Node head2) {
        if (head1 == null && head2 != null) {
            return false;
        }
        if (head1 != null && head2 == null) {
            return false;
        }
        if (head1 == null && head2 == null) {
            return true;
        }
        if (head1.value != head2.value) {
            return false;
        }
        return isSameValueStructure(head1.left, head2.left) && isSameValueStructure(head1.right, head2.right);
    }

    // for test
    public static void printTree(Node head) {
        System.out.println("Binary Tree:");
        printInOrder(head, 0, "H", 17);
        System.out.println();
    }

    public static void printInOrder(Node head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printInOrder(head.right, height + 1, "v", len);
        String val = to + head.value + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printInOrder(head.left, height + 1, "^", len);
    }

    public static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        int maxLevel = 5;
        int maxValue = 100;
        int testTimes = 1000000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            Queue<String> pre = preSerial(head);
            Queue<String> pos = posSerial(head);
            Queue<String> level = levelSerial(head);
            Node preBuild = buildByPreQueue(pre);
            Node posBuild = buildByPosQueue(pos);
            Node levelBuild = buildByLevelQueue1(level);
            if (!isSameValueStructure(preBuild, posBuild) || !isSameValueStructure(posBuild, levelBuild)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish!");

    }

}
