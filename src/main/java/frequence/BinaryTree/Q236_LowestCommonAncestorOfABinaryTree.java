package frequence.BinaryTree;

/**
 * 236. 二叉树的最近公共祖先
 *
 * <p>给定一棵二叉树的根节点 {@code root}，以及树中的两个节点 {@code p} 和 {@code q}，
 * 返回这两个节点的最近公共祖先。
 *
 * <p>最近公共祖先是满足以下条件的最深节点：{@code p} 和 {@code q} 都位于该节点的子树中。
 * 根据定义，一个节点也可以是它自己的祖先。
 *
 * <p><b>二叉树递归套路：</b>每棵子树向父节点返回三个信息：
 * <ul>
 *     <li>{@code ancestor}：当前子树是否已经找到最近公共祖先；</li>
 *     <li>{@code containsA}：当前子树是否包含节点{@code p}；</li>
 *     <li>{@code containsB}：当前子树是否包含节点{@code q}。</li>
 * </ul>
 *
 * <p><b>ancestor判断顺序：</b>如果左子树或右子树已经找到答案，直接向上返回该答案。
 * 只有左右子树都没有答案时，才判断当前子树是否同时包含{@code p、q}；如果同时包含，
 * 当前节点就是二者第一次汇合的位置，即最近公共祖先。
 *
 * <p>为什么不需要继续枚举“p在左、q在右”“当前节点是p、q在子树”等组合：
 * 如果{@code p、q}都在同一棵子树中，那棵子树的{@code ancestor}必然已经非空；
 * 前两个分支没有返回答案，但当前子树又同时包含二者，说明二者在当前节点处分开，
 * 或者当前节点本身就是其中一个。
 *
 * <p><b>注意：</b>LeetCode题目保证{@code p、q}都存在于树中且{@code p != q}。
 * 简化后的{@code containsA && containsB}也可以自然支持{@code p == q}；原来的四组枚举条件
 * 则没有覆盖“当前节点同时是p和q”的扩展情况。
 *
 * <p>时间复杂度为{@code O(N)}，每个节点只处理一次；递归栈空间为{@code O(H)}，
 * 其中{@code H}是树高，二叉树退化成链表时最坏为{@code O(N)}。
 */
public class Q236_LowestCommonAncestorOfABinaryTree {

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        return process(root, p, q).ancestor;
    }

    public Info process(TreeNode cur, TreeNode p, TreeNode q) {
        if (cur == null) {
            return new Info(null, false, false);
        }

        Info left = process(cur.left, p, q);
        Info right = process(cur.right, p, q);

        TreeNode ancestor;
        boolean containsA;
        boolean containsB;


        containsA = left.containsA || right.containsA || cur == p;
        containsB = left.containsB || right.containsB || cur == q;


        if (left.ancestor != null) {
            ancestor = left.ancestor;
        } else if (right.ancestor != null) {
            ancestor = right.ancestor;
        // TODO: 【优化点】下面四组条件逻辑正确，但重复枚举了p、q位于左右子树或当前节点的组合，
        // 面试时条件较长、容易遗漏。前两个分支已经排除了“答案完整位于某棵子树”的情况，
        // 因此这里只需判断当前子树是否同时包含p和q。
        // } else if ((left.containsA && (right.containsB || cur == q))
        //         || (left.containsB && (right.containsA || cur == p))
        //         || (right.containsA && (left.containsB || cur == q))
        //         || (right.containsB && (left.containsA || cur == p))) {
        } else if (containsA && containsB) {
            ancestor = cur;
        } else {
            ancestor = null;
        }

        return new Info(ancestor, containsA, containsB);
    }


    public class Info {
        TreeNode ancestor;
        boolean containsA;
        boolean containsB;

        public Info(TreeNode _an, boolean _ca, boolean _cb) {
            ancestor = _an;
            containsA = _ca;
            containsB = _cb;
        }
    }
}
