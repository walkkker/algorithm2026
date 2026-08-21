package BinaryTree.NonTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 两个方法：基于层级遍历
 * (1) 笨方法：O(N)空间复杂度 HashMap<节点，层数>  每次遍历父节点的时候，会在Map里面记录 （childNode,所在层数）
 * (2) 最优解：O(1)空间 两个变量： Node curEnd, Node nextEnd。 同样是在 父节点遍历时，更新nextEnd。
 * - 每个节点都检查自己是否==curEnd, 若yes，则表明 当前层结束 -> 即可 统计信息等操作
 **/

public class TreeMaxWidth {

    public static class Node {
        int val;
        Node left;
        Node right;

        public Node(int _val) {
            val = _val;
        }
    }

    // 两个变量 curEnd, nextEnd
    public static int maxWidthNoMap(Node root) {
        if (root == null) {
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        Node curEnd = root;
        Node nextEnd = null;
        int maxWidth = 0;
        int widthInLevel = 0;    // 左： int curLevelNodes = 0
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            widthInLevel++;

            if (cur.left != null) {
                queue.add(cur.left);
                nextEnd = cur.left;
            }

            if (cur.right != null) {
                queue.add(cur.right);
                nextEnd = cur.right;
            }

            if (cur == curEnd) {
                // 先统计 再更新
                maxWidth = Math.max(maxWidth, widthInLevel);

                curEnd = nextEnd;
                widthInLevel = 0;
            }
        }
        return maxWidth;
    }

    public static int maxWidthUseMap(Node head) {
        if (head == null) {
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        // key 在 哪一层，value
        HashMap<Node, Integer> levelMap = new HashMap<>();
        levelMap.put(head, 1);
        int curLevel = 1; // 当前你正在统计哪一层的宽度
        int curLevelNodes = 0; // 当前层curLevel层，宽度目前是多少
        int max = 0;
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            int curNodeLevel = levelMap.get(cur);
            if (cur.left != null) {
                levelMap.put(cur.left, curNodeLevel + 1);
                queue.add(cur.left);
            }
            if (cur.right != null) {
                levelMap.put(cur.right, curNodeLevel + 1);
                queue.add(cur.right);
            }
            if (curNodeLevel == curLevel) {
                curLevelNodes++;
            } else {
                max = Math.max(max, curLevelNodes);
                curLevel++;
                curLevelNodes = 1;
            }
        }
        max = Math.max(max, curLevelNodes);
        return max;
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
        int maxLevel = 10;
        int maxValue = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            if (maxWidthUseMap(head) != maxWidth(head)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");

    }


    public static int maxWidth(Node head) {
        if (head == null) {  // TODO: 【错误点】root==null 的对数器 没考虑到
            return 0;
        }

        Node curEnd = head;
        Node nextEnd = head;
        int max = 0;
        int widthInLevel = 0;
        Queue<Node> queue = new LinkedList<>();
        queue.offer(head);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            widthInLevel++;
            if (cur.left != null) {
                queue.offer(cur.left);
                nextEnd = cur.left;
            }
            if (cur.right != null) {
                queue.offer(cur.right);
                nextEnd = cur.right;
            }
            if (cur == curEnd) {
                max = Math.max(max, widthInLevel);
                widthInLevel = 0;
                curEnd = nextEnd;
            }
        }
        return max;
    }


}
