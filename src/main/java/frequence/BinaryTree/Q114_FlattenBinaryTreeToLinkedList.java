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
     * 2026-09-03 复写版本：不使用递归栈或额外容器，直接在原树上重排前序后继关系。
     *
     * <p><b>核心步骤：</b>当{@code cur}存在左子树时，先找到左子树当前结构中的最右节点
     * {@code des}，令{@code des.right = cur.right}保存原右子树；再把原左子树移动到
     * {@code cur.right}；最后清空{@code cur.left}。之后始终沿新的right指针继续处理。
     *
     * <p><b>本次遗漏：</b>重连{@code cur.right = cur.left}后，忘记执行
     * {@code cur.left = null}。清空left不是可选的整理动作，而是题目要求和循环不变量的一部分：
     * 已处理节点必须只通过right连接。如果遗漏，节点会同时保留left和right对同一子树的引用，
     * 最终结构仍然不是题目要求的单链表。
     *
     * <p><b>文字笔误：</b>应当记为“左子树最右节点{@code .right = cur.right}”，用于接住
     * 原右子树；不能写成{@code .right = cur.left}，否则会错误地指回左子树。
     */
    class Solution20260903 {

        public void flatten(TreeNode root) {
            /*
             * 思路：目标顺序是前序遍历，即根、左、右。
             * 每个节点先让左子树最右节点.right接住cur.right，再把左子树移动到cur.right。
             * 遍历方向始终沿right指针前进。
             */
            TreeNode cur = root;
            while (cur != null) {
                // Step1：存在左子树时，先保存原右子树，再把左子树移动到右侧。
                if (cur.left != null) {
                    TreeNode des = cur.left;
                    while (des.right != null) {
                        des = des.right;
                    }
                    des.right = cur.right;
                    cur.right = cur.left;
                    // TODO: 【错误-遗漏】别忘了cur.left置为null。
                    // 原因：题目要求展开后所有left都为空，这也是“已处理部分已经成为单链”的不变量。
                    cur.left = null;
                }
                // Step2：新的right就是前序序列中的下一个待处理节点。
                cur = cur.right;
            }
        }
    }

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
     * <p><b>O(1)的准确含义：</b>本方法不使用递归，因此没有系统递归栈；也不使用显式Stack、
     * Queue、数组或集合。只使用cur和leftRightMost等固定数量的节点引用，所需辅助空间不会
     * 随节点数N或树高H增长。O(1)不表示一个局部变量都不能使用，而是变量数量保持常数级。
     * 输入树本身以及题目要求原地形成的right链不计入额外空间。
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
     * <p><b>微观理解：</b>对于每个存在左子树的cur，都把左子树插入“cur与原右子树”之间：
     * <pre>
     * cur -> 左子树 -> 原右子树
     * </pre>
     * 然后执行{@code cur = cur.right}，继续用同样规则处理前序序列中的下一个节点。
     *
     * <p><b>宏观结果：</b>每轮都让cur的right先指向左子树，并把原右子树安排在完整左子树之后；
     * 同时清空cur.left。所有节点处理完成后，每个left都为null，所有节点只通过right连接，
     * right链顺序正好是“根、左子树、右子树”的前序遍历顺序。
     *
     * <p>注意：leftRightMost不一定已经是左子树最终前序链表的尾节点。如果它还存在左子树，
     * 后续处理到它时，会继续把暂存在right上的原右子树向后移动，因此最终仍能保证
     * “完整左子树在前、原右子树在后”。所以更准确的术语是“当前结构中的左子树右边界节点”，
     * 不能简单理解为此刻已经确定的“前序最后节点”。
     */
    public void flatten(TreeNode root) {
        TreeNode cur = root;
        while (cur != null) {
            if (cur.left != null) {

                // leftRightMost是当前结构中的左子树右边界节点；它不一定已经是完整左子树
                // 最终前序链的尾节点，后续沿right遍历时还会继续处理它内部尚未搬移的左子树。
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
            // 遍历方向始终是cur = cur.right：本轮重连后，right正好指向前序序列中的下一个
            // 待处理节点。微观上逐节点执行相同规则，宏观上最终形成完整前序right链。
            cur = cur.right;
        }
    }
}
