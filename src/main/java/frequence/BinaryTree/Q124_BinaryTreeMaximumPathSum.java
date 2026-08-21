package frequence.BinaryTree;

/**
 * 124. 二叉树中的最大路径和
 *
 * <p>二叉树中的路径是一个节点序列，序列中相邻节点之间存在一条边。同一个节点在一条路径中
 * 最多出现一次，路径至少包含一个节点，并且不要求经过根节点。
 *
 * <p>路径和是路径中所有节点值的总和。给定一棵二叉树的根节点 {@code root}，返回任意非空
 * 路径的最大路径和。
 *
 * <p><b>核心状态：</b>能够继续交给父节点使用的路径，必须是“从当前节点出发，只向下选择
 * 一侧”的单链；只有在计算当前子树最终答案时，才允许把左链、当前节点和右链连接起来。
 *
 * <p>例如 {@code 1 -> 2}，并且节点 {@code 2} 同时有左右孩子 {@code 3、4}。
 * 节点2处可以形成完整路径 {@code 3-2-4}，但这条路径不能再连接父节点1，否则节点2处会
 * 产生三个方向的分叉，不再是一条合法路径。
 */
public class Q124_BinaryTreeMaximumPathSum {

    public int maxPathSum(TreeNode root) {
        Info info = process(root);
        return Math.max(info.yes, info.no);
    }


    public Info process(TreeNode cur) {
        if (cur == null) {
            return new Info(Integer.MIN_VALUE, Integer.MIN_VALUE);
        }

        Info left = process(cur.left);
        Info right = process(cur.right);

        int yes;
        int no;

        no = Math.max(Math.max(left.yes, left.no), Math.max(right.yes, right.no));

        // TODO: 【致命错误】yes表示“经过当前节点，并且可以同时连接左右子树”的完整路径。
        // left.yes/right.yes可能已经同时连接了各自的左右子树，不能再连接cur，否则会形成
        // 三个方向的分叉，不再是一条合法路径。
        // 【修正思路】Info必须额外提供line：从当前节点出发、只能向下选择一侧的最大路径和。
        // 当前节点计算完整路径时应使用left.line和right.line；向父节点返回时也只能返回line。
        yes = Math.max(left.yes, 0) + Math.max(right.yes, 0) + cur.val;
        return new Info(yes, no);

    }


    public class Info {
        int yes;
        int no;

        public Info(int _yes, int _no) {
            yes = _yes;
            no = _no;
        }
    }


    class Solution {
        public int maxPathSum(TreeNode root) {
            return process(root).maxDistance;
        }


        public Info process(TreeNode cur) {
            if (cur == null) {
                return new Info(Integer.MIN_VALUE, Integer.MIN_VALUE);
            }

            Info left = process(cur.left);
            Info right = process(cur.right);

            int line;
            int maxDistance;


            // 【递归不变量】line必须从cur出发，并且只能向左或向右选择一侧。
            line = Math.max(Math.max(left.line, right.line), 0) + cur.val;
            // p1是以cur为最高连接点的完整路径，此时才允许同时连接左右两条单链。
            int p1 = Math.max(left.line, 0) + Math.max(right.line, 0) + cur.val;
            // TODO: 【命名建议】maxDistance逻辑正确，但本题计算的是路径和，
            // 命名为maxPathSum会比maxDistance更准确。
            maxDistance = Math.max(Math.max(left.maxDistance, right.maxDistance), p1);
            return new Info(line, maxDistance);

        }


        public class Info {
            int line;
            int maxDistance;

            public Info(int _l, int _m) {
                line = _l;
                maxDistance = _m;
            }
        }
    }
}
