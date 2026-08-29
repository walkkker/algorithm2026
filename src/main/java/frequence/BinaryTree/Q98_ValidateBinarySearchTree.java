package frequence.BinaryTree;
import java.util.*;
/**
 * 98. 验证二叉搜索树
 * TODO:  【解决思路】BST的中序 是升序序列 （不使用二叉树递归套路的话，用这个比较快）
 *
 * <p>给定一个二叉树的根节点 {@code root}，判断其是否为有效的二叉搜索树。
 * 对于树中的任意节点，其左子树中所有节点的值必须严格小于该节点值，
 * 右子树中所有节点的值必须严格大于该节点值，并且左右子树也必须分别是二叉搜索树。
 *
 * <p><b>专题归类：</b>BST全局有序性；中序严格递增或递归传递开放上下界。
 * 参见同目录《二叉树通用技巧与题型分类.md》和《二叉树错题本.md》的Q98条目。
 */
public class Q98_ValidateBinarySearchTree {
    // BST的中序 是升序序列
    public boolean isValidBST(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        process(root, ans);
        for (int i = 0; i < ans.size() - 1; i++) {
            if (ans.get(i) >= ans.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public void process(TreeNode cur, List<Integer> ans) {
        if (cur == null) {
            return;
        }
        process(cur.left, ans);
        ans.add(cur.val);
        process(cur.right, ans);
    }


}
