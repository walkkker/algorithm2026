package frequence.BinaryTree;

import java.util.*;

/**
 * 94. 二叉树的中序遍历
 *
 * <p>给定一个二叉树的根节点 {@code root}，返回它的中序遍历结果。
 *
 *
 * 实现递归和非递归版本
 *
 * <p><b>专题归类：</b>DFS访问时机、递归栈与显式栈模拟；BST相关题目的基础。
 * 参见同目录《二叉树通用技巧与题型分类.md》和《二叉树专题总览与面试优先级.md》。
 */
public class Q94_BinaryTreeInorderTraversal {

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        process(root, ans);
        return ans;
    }

    private void process(TreeNode head, List<Integer> ans) {
        if (head == null) {
            return;
        }
        process(head.left, ans);
        ans.add(head.val);
        process(head.right, ans);
    }

    public List<Integer> inorderTraversalUnRecursive(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        List<Integer> ans = new ArrayList<>();
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                ans.add(cur.val);
                cur = cur.right;
            }
        }
        return ans;
    }


    }
