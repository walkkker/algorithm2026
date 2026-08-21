package frequence.BinaryTree;

/**
 * 104. 二叉树的最大深度
 *
 * <p>给定一个二叉树的根节点 {@code root}，返回该二叉树的最大深度。
 * 最大深度是从根节点到最远叶子节点的最长路径上的节点数。
 */
public class Q104_MaximumDepthOfBinaryTree {

    public int maxDepth(TreeNode root) {
        return process(root);
    }

    public int process(TreeNode cur) {
        if (cur == null) {
            return 0;
        }
        int p1 = process(cur.left);
        int p2 = process(cur.right);
        return Math.max(p1, p2) + 1;
    }
}
