package frequence.BinaryTree;

import java.util.*;

/**
 * 102. 二叉树的层序遍历
 *
 * <p>给定一个二叉树的根节点 {@code root}，按照从上到下、从左到右的顺序逐层访问节点，
 * 并返回每一层节点值组成的列表。
 *
 * <p><b>专题归类：</b>BFS、队列与层边界识别。参见同目录《二叉树通用技巧与题型分类.md》
 * 和《二叉树错题本.md》的Q102/Q199条目。
 */
public class Q102_BinaryTreeLevelOrderTraversal {
    public List<List<Integer>> levelOrder(TreeNode root) {
        // TODO: 【错误1】树中节点数目在范围 [0, 2000] 内，注意有0。 不做null检查，下面就会NPE ->  cur.val
        if (root == null) {
            return new ArrayList<>();
        }

        List<List<Integer>> ans = new ArrayList<>();
        TreeNode curEnd = root;
        // TODO: 【错误2】需要初始化（即便是赋值null），不然存在java语法错误。“Variable 'nextEnd' might not have been initialized
        TreeNode nextEnd = null;
        Queue<TreeNode> queue = new LinkedList<>();
        // curEnd = root;
        queue.add(root);
        List<Integer> tmp = new ArrayList<>();
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            tmp.add(cur.val);

            if (cur.left != null) {
                queue.add(cur.left);
                nextEnd = cur.left;
            }
            if (cur.right != null) {
                queue.add(cur.right);
                nextEnd = cur.right;
            }

            if (cur == curEnd) {
                ans.add(tmp);
                tmp = new ArrayList<>();
                curEnd = nextEnd;
            }
        }
        return ans;
    }
}
