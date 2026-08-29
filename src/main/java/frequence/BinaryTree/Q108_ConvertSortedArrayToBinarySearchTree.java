package frequence.BinaryTree;

/**
 * // TODO: 【注意】这道题要背一下。 有序数组转换为平衡BST。其实就是递归，然后每次取 mid元素构建节点， 左侧区间构建左子树，右侧区间构建右子树。
 *
 * 108. 将有序数组转换为二叉搜索树
 *
 * <p>给定一个按照严格升序排列的整数数组 {@code nums}，将其转换为一棵高度平衡的二叉搜索树，
 * 并返回该树的根节点。高度平衡二叉树要求每个节点的左右子树高度差不超过1。
 *
 * <p><b>专题归类：</b>数组区间建树；选择中点作为根，使左右区间规模尽量平衡。
 * 参见同目录《二叉树通用技巧与题型分类.md》。
 */
public class Q108_ConvertSortedArrayToBinarySearchTree {

    public TreeNode sortedArrayToBST(int[] nums) {
        return process(nums, 0, nums.length - 1);
    }

    public TreeNode process(int[] nums, int l, int r) {
        if (l == r) {
            return new TreeNode(nums[l]);
        }
        if (l > r) {
            return null;
        }
        int m = (l + r) / 2;
        TreeNode cur = new TreeNode(nums[m]);
        cur.left = process(nums, l, m - 1);
        cur.right = process(nums, m + 1, r);
        return cur;
    }
}
