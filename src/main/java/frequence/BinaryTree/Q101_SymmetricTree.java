package frequence.BinaryTree;

/**
 * 101. 对称二叉树 TODO: 这道题值得看：【1.核心思路，构建双节点递归】 【2.镜像比较时，左树左孩子 对应 右树右孩子； 左树右孩子 对应 右树左孩子】
 * TODO：【核心思路】左右子树是否互为镜像。 需要构建一个包含双节点的递归。
 * TODO: 【错误！！！要的是轴对称呀】这道题错了！！ 轴对称呀！！！ 不是cur1.left == cur2.left，应该是cur1.left==cur2.right。 自己画张图就知道了
 *
 * <p>给定一个二叉树的根节点 {@code root}，判断该二叉树是否关于其中心轴镜像对称。
 */
public class Q101_SymmetricTree {

    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return process(root.left, root.right);
    }

    public boolean process(TreeNode cur1, TreeNode cur2) {
        if (cur1 == null && cur2 != null) {
            return false;
        }
        if (cur1 != null && cur2 == null) {
            return false;
        }
        if (cur1 == null && cur2 == null) {
            return true;
        }
        boolean p1 = cur1.val == cur2.val;
        // TODO: 【错误点】轴对称，对应的是 cur1.left对应cur2.right
        //  boolean p2 = process(cur1.left, cur2.left);
        //  boolean p3 = process(cur1.right, cur2.right);
        boolean p2 = process(cur1.left, cur2.right);
        boolean p3 = process(cur1.right, cur2.left);
        return p1 && p2 && p3;
    }
}
