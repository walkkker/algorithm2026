package frequence.BinaryTree;

import java.util.*;

/**
 * 94. 二叉树的中序遍历
 *
 * <p>给定一个二叉树的根节点 {@code root}，返回它的中序遍历结果。
 *
 *
 * 实现递归和非递归版本
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
