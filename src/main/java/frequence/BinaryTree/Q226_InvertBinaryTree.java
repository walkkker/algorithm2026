package frequence.BinaryTree;

/**
 * 226. 翻转二叉树
 *
 * <p>给定一个二叉树的根节点 {@code root}，，返回翻转后二叉树的根节点。
 *
 * <p><b>专题归类：</b>直接递归改写；覆盖{@code left/right}前必须冻结原子树入口。
 * 参见同目录《二叉树通用技巧与题型分类.md》和《二叉树错题本.md》的Q226条目。
 *
 * <p><b>2026-09-02致命错误复盘：</b>下面两条Java语句按顺序执行，不是同时交换：
 * <pre>{@code
 * cur.left = process(cur.right);
 * cur.right = process(cur.left);
 * }</pre>
 * 假设原左子树为L、原右子树为R。第一行执行完以后，{@code cur.left}已经变成
 * {@code process(R)}的结果；第二行读取的{@code cur.left}是覆盖后的新值，不再是L。
 * 因此原左子树L失去入口，右子树还可能被重复处理并同时被left/right引用。
 *
 * <p>通用原则：对象字段出现在赋值号左侧时，该字段会在语句完成后立即被覆盖。如果后续语句
 * 仍需要它的旧值，必须在覆盖前保存到独立局部变量。多写引用不是冗余，而是在隔离原结构和
 * 目标结构。
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
        // TODO：【致命错误-覆盖后读取新值】下面两行不是同时交换，而是按顺序执行。
        //   cur.left = process(cur.right);
        //   cur.right = process(cur.left);
        // 第一行结束后cur.left已经指向翻转后的原右子树；第二行读取的是这个新值，
        // 原左子树入口已经丢失。还可能造成左右字段引用同一棵子树。

        // TODO: 【正确修正】在任何覆盖发生前，先冻结原左右子树入口。
        TreeNode leftHead = cur.left;
        TreeNode rightHead = cur.right;

        // 右侧只读取独立局部变量，不再依赖已经被覆盖的cur.left/cur.right字段。
        cur.left = process(rightHead);
        cur.right = process(leftHead);

        return cur;
    }

}
