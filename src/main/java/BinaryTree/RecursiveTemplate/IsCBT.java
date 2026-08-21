package BinaryTree.RecursiveTemplate;

/**
 * https://leetcode.cn/problems/check-completeness-of-a-binary-tree/description/
 *
 * 主要研究二叉树递归套路，base case设置问题。
 *
 * （1）方法1【犯错了】： base case == head的尝试，事实验证 能用null 就用 null，不然代码要多写 && 多讨论情形
 *      主要是 左!=null 右==null 的情形很细微，要分类讨论height==1? 第一次写错了！！！
 * （2）方法2： base case == null。 null是可以构建的
 *     你会发现 主逻辑 我直接从 方法1 中复制过来的。 省了非常多的代码，因为不用判断null。还减少了错误率。
 * （3）非递归 -> 层级遍历 + boolean leaf
 *
 *  测试链接 : https://leetcode.com/problems/check-completeness-of-a-binary-tree/
 *
 */
public class IsCBT {

    public static class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int data) {
            this.value = data;
        }
    }

    public static class Info {
        boolean isCBT;
        boolean isFull;
        int height;

        public Info(boolean _isCBT, boolean _isFull, int _height) {
            isCBT = _isCBT;
            isFull = _isFull;
            height = _height;
        }
    }

    // 方法1： base case == head的尝试，事实验证 能用null 就用 null，不然代码要多写 && 多讨论情形
    public static boolean isCBT1(TreeNode head) {
        if (head == null) {
            return true;
        }
        return process1(head).isCBT;
    }


    public static Info process1(TreeNode head) {
        if (head.left == null && head.right == null) {
            return new Info(true, true, 1);
        }

        Info left = null;
        if (head.left != null) {
            left = process1(head.left);
        }

        Info right = null;
        if (head.right != null) {
            right = process1(head.right);
        }

        if (left == null && right != null) {
            return new Info(false, false, right.height + 1);
        }

        // TODO: 【错误-想少了没想全】左树高度为1时，整棵树是CBT。
        if (left != null && right == null) {

            if (left.height == 1) {
                return new Info(true, false, 2);
            } else {
                return new Info(false, false, left.height + 1);
            }
            // 不能直接这么写
            // return new Info(false, false, left.height + 1);
        }

        boolean isCBT = false;
        boolean isFull = false;
        int height;

        if ((left.height == right.height && left.isFull && right.isCBT)
                ||
                (left.height == right.height + 1 && left.isCBT && right.isFull)
        ) {
            isCBT = true;
        }

        if (left.height == right.height && left.isFull && right.isFull) {
            isFull = true;
        }

        height = Math.max(left.height, right.height) + 1;
        return new Info(isCBT, isFull, height);
    }




    // 方法2： base case == null。 null是可以构建的
    // 你会发现 主逻辑 我直接从 方法1 中复制过来的。 省了非常多的代码，因为不用判断null。

    public static boolean isCBT2(TreeNode head) {
        if (head == null) {
            return true;
        }
        return process2(head).isCBT;
    }

    public static Info process2(TreeNode root) {
        if (root == null) {
            return new Info(true, true, 0);
        }

        // base case == null ， 那么就不用if检查了
        Info left = process2(root.left);
        Info right = process2(root.right);

        boolean isCBT = false;
        boolean isFull = false;
        int height;

        if ((left.height == right.height && left.isFull && right.isCBT)
                ||
                (left.height == right.height + 1 && left.isCBT && right.isFull)
        ) {
            isCBT = true;
        }

        if (left.height == right.height && left.isFull && right.isFull) {
            isFull = true;
        }

        height = Math.max(left.height, right.height) + 1;
        return new Info(isCBT, isFull, height);
    }


    public static class Info1 {
        boolean isCBT;
        boolean isFull;
        int height;

        public Info1 (boolean _isCBT, boolean _isFull, int _height) {
            isCBT = _isCBT;
            isFull = _isFull;
            height = _height;
        }
    }

    public static boolean isCBT(TreeNode root) {
        if (root == null) {
            return true;
        }
        return process(root).isCBT;
    }

    public static Info process(TreeNode cur) {
        if (cur == null) {
            return new Info(true, true, 0);
        }

        Info left = process(cur.left);
        Info right = process(cur.right);

        boolean isCBT = false;
        boolean isFull = false;
        int height;

        if (left.height == right.height) {
            if (left.isFull && right.isCBT) {
                isCBT = true;
            }
            if (left.isFull && right.isFull) {
                isFull = true;
            }
            height = Math.max(left.height, right.height) + 1;
            return new Info(isCBT, isFull, height);
        } else if (left.height == right.height + 1) {
            if (left.isCBT && right.isFull) {
                isCBT = true;
            }
            isFull = false;
            height = Math.max(left.height, right.height) + 1;
            return new Info(isCBT, isFull, height);
        } else {
            return new Info(isCBT, isFull, Math.max(left.height, right.height) + 1);
        }
    }


}
