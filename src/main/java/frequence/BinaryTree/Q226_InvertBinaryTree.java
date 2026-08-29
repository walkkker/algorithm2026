package frequence.BinaryTree;

/**
 * 226. 翻转二叉树
 *
 * <p>给定一个二叉树的根节点 {@code root}，，返回翻转后二叉树的根节点。
 *
 * <p><b>专题归类：</b>直接递归改写；覆盖{@code left/right}前必须冻结原子树入口。
 * 参见同目录《二叉树通用技巧与题型分类.md》和《二叉树错题本.md》的Q226条目。
 */
public class Q226_InvertBinaryTree {

    /**
     TODO:【致命错误】覆盖原指针导致子树丢失的致命错误
     */
    public TreeNode invertTree(TreeNode root) {
        return process(root);
    }

    public TreeNode process(TreeNode cur) {
        if (cur == null) {
            return null;
        }
        // TODO：【错误如下】
        //   cur.left = process(cur.right);
        //   cur.right = process(cur.left);   TODO: 这里cur.left已经被覆盖了，左树已经丢失

        // TODO: 【注意】你要注意，swap方法表明，只有等号右侧的值，可以再使用，因为没有被覆盖！！ 对于等号左侧的值而言，【尤其指针】，一定注意被覆盖的问题。
        TreeNode leftHead = cur.left;
        TreeNode rightHead = cur.right;

        cur.left = process(rightHead);  // TODO: 看来【额外建立引用】，而不是（省代码）用指针 代替入口，是一个很好的习惯，能够在思维不细致的情况下 避免指针值被覆盖/遗漏被修改的问题。 （reverseLinkedList 就是引用）
        cur.right = process(leftHead);

        return cur;
    }

}
