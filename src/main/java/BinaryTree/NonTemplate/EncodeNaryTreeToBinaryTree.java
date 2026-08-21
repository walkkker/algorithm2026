package BinaryTree.NonTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 本代码待验证 没有跑过
 * (1) 一句话：左树右边界   才能不冲突。
 * (2) 使用递归
 * (3) encode递归: n-ary 的 children 转化为 左树右边界
 * (4) decode递归: 2-ary 的 左树右边界 转化为 n-ary的children
 */
public class EncodeNaryTreeToBinaryTree {
    // In my assumption, the recursive method can be designed for the whole tree or just children.
    // It just depends on how you design the up-stream call and how to break down the recursive method.

    // I would like to try my method: recursive for the whole tree instead of zuo's method (which is designed for children)

    // 提交时不要提交这个类
    public static class Node {
        public int val;
        public List<Node> children;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    ;

    // 提交时不要提交这个类
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }


    class Codec {
        // Encodes an n-ary tree to a binary tree.
        public TreeNode encode(Node root) {
            if (root == null) {
                return null;
            }
            return en(root);
        }

        // 递归先定语义：传入一个n叉树的头结点，返回encode后的二叉树的头结点
        // 拆解：只考虑最小模型。 将每个孩子挂在 左树右边界. -》 n-ary的childeren 转成 2-ary左树右边界
        public TreeNode en(Node root) {
            // base case + 剪枝  就没有使用 if(cur==null) return null;
            if (root.children.size() == 0) {
                return new TreeNode(root.val);
            }
            List<Node> children = root.children;
            TreeNode treeNodeRoot = new TreeNode(root.val);
            treeNodeRoot.left = en(children.get(0));
            TreeNode cur = treeNodeRoot.left;
            for (int i = 1; i < children.size(); i++) {
                cur.right = en(children.get(i));
                cur = cur.right;
            }
            return treeNodeRoot;
        }


        // Decodes your binary tree to an n-ary tree.
        // encode阶段都是挂在了左树右边界。所以decode时，也是从左树右边界恢复就可以了
        public Node decode(TreeNode root) {
            if (root == null) {
                return null;
            }
            return de(root);
        }

        // 语义：给二叉树的头结点，返回decode后的多叉树的头结点
        // breakDown: 2-ary 左树右边界 转成 n-ary children
        public Node de(TreeNode root) {
            if (root == null) {
                return null;
            }
            if (root != null && root.left == null) {
                // TODO: 这里要注意，我们本题的假设是 Node如果是叶子节点， childeren != null,而是=空list
                return new Node(root.val, new ArrayList<>());
            }
            List<Node> children = new ArrayList<>();
            TreeNode cur = root.left;
            while (cur != null) {
                children.add(decode(cur));
                cur = cur.right;
            }
            return new Node(root.val, children);
        }
    }


}
