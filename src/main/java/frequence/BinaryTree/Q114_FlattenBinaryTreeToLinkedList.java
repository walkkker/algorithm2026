package frequence.BinaryTree;

/**
 * 114. 二叉树展开为链表 =》 本题两个方法都需要认真看
 * TODO：【难题-错了很多遍】两个方法 1. 递归返回尾节点 O(H)  2. 原地修改 O(1)  这里指额外空间复杂度
 *  1. 递归是通用框架：左右子树先处理完成，当前节点再根据左右子树返回的信息完成拼接。
 *  2. 原地修改：左子树搬到右边，原右子树挂到左子树右边界，循环继续展开。
 *  H表示树高：平衡树为O(logN)，退化成链时最坏为O(N)，所以递归空间不能统一写成O(logN)。
 *
 * <p>给定一个二叉树的根节点 {@code root}，将其原地展开为一条单链表。
 * 展开后的链表使用二叉树节点的 {@code right} 指针连接下一个节点，
 * 所有节点的 {@code left} 指针必须为 {@code null}，节点顺序应与二叉树的前序遍历一致。
 *
 * <p><b>专题归类：</b>递归返回尾节点与原地结构改写。参见同目录
 * 《二叉树通用技巧与题型分类.md》和《二叉树错题本.md》的Q114条目。
 */
public class Q114_FlattenBinaryTreeToLinkedList {

    /**
     * 递归通用框架：先分别展开左右子树，再由当前节点完成两条链表的拼接。
     *
     * <p>时间复杂度为O(N)，递归栈空间为O(H)。
     *
     * <p>实现时出现过两个致命错误：
     * 1. 只有存在左子树时才能执行搬移，否则会把原右子树断开；
     * 2. 递归函数返回当前子树展开后的尾节点，不能无条件返回rightTail。
     */
    public void flattenWithRecursive(TreeNode root) {
        process(root);
    }

    /**
     * 递归契约：将以cur为根的二叉树展开，并返回展开后链表的尾节点。
     *
     * <p>只要cur非空，返回值就必须是一个非空尾节点。这个契约决定了最终返回值必须按照
     * rightTail、leftTail、cur的优先级选择。
     */
    public TreeNode process(TreeNode cur) {
        if (cur == null) {
            return null;
        }

        // 先完成左右子树内部的展开。此时cur.left和cur.right仍分别保存两条链表的头节点，
        // leftTail和rightTail则分别保存两条链表的尾节点，所以后续修改不会丢失子树。
        TreeNode leftTail = process(cur.left);
        TreeNode rightTail = process(cur.right);

        if (leftTail != null) {  // leftTail == null 说明左子树为空，就不用动了。
            // 前序顺序要求：cur -> 展开的左子树 -> 展开的右子树。
            leftTail.right = cur.right;
            cur.right = cur.left;
            cur.left = null;
        }
        // TODO: 【致命错误1】不能放在外面。如果cur.left==null，放在外面，直接把右子树断链了
        //  重点关注是否存在左子树，只有当左子树存在时，才进行 前序顺序的处理。
        //  cur.right = cur.left;
        //  cur.left = null;

        // TODO: 【致命错误2！！！】不能无条件返回rightTail。   返回的一定是一个非null节点。
        // 当右子树为空时，尾节点可能是leftTail；叶节点的尾节点是cur自己。
        // 错误：return rightTail;
        // 尾节点优先级：存在右链表则取rightTail；否则取leftTail；叶节点返回cur自己。
        return rightTail == null ? (leftTail == null ? cur : leftTail) : rightTail;
    }

    /**
     * 原地版本：时间复杂度O(N)，额外空间复杂度O(1)，这是渐进复杂度最优解。
     *
     * <p>真正的核心不是“一次性展开左子树”，而是重新安排后续处理顺序：
     * 前序遍历要求cur之后先处理左子树，再处理原右子树。递归/显式栈会暂存原右子树；
     * 原地版本则把原右子树挂到左子树的右边界，直接使用树本身的指针保存待处理任务。
     *
     * 1. 找到左子树的最右节点
     * 2. 最右节点接住原右子树
     * 3. 当前节点的right指向原左子树
     * 4. 当前节点的left置空
     * 5. 沿新的right继续处理
     *
     * <p>循环不变量：cur之前的right链已经完全展开；以cur开始的结构仍然包含尚未处理的
     * 前序序列。本轮只修改cur和leftRightMost两个节点，共写入三个指针字段。
     *
     * <p>注意：leftRightMost不一定已经是左子树最终前序链表的尾节点。如果它还存在左子树，
     * 后续处理到它时，会继续把暂存在right上的原右子树向后移动，因此最终仍能保证
     * “完整左子树在前、原右子树在后”。
     */
    public void flatten(TreeNode root) {
        TreeNode cur = root;
        while (cur != null) {
            if (cur.left != null) {

                TreeNode leftRightMost = cur.left;
                while (leftRightMost.right != null) {
                    leftRightMost = leftRightMost.right;
                }

                // TODO: 【关键顺序，不能打乱】
                // 1. 先让左子树右边界接住原右子树，保证原右子树不会丢失；
                // 2. 再让cur.right接住原左子树；
                // 3. 最后清空cur.left，此时左子树已经可以通过cur.right访问。
                leftRightMost.right = cur.right;
                cur.right = cur.left;
                cur.left = null;
            }
            // 无论本轮是否存在左子树，cur.right都指向前序序列中的下一个待处理节点。
            cur = cur.right;
        }
    }
}
