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
 *
 * <p><b>专题归类：</b>底向上Info、可向父节点延伸的单链贡献与子树完整答案分离。
 * 参见同目录《二叉树通用技巧与题型分类.md》、《二叉树错题本.md》以及
 * 《二叉树递归套路 易犯错误注意事项.md》的Q124条目。
 */
public class Q124_BinaryTreeMaximumPathSum {

    /**
     * 2026-09-04 复写版本：使用Info同时维护“可继续向父节点延伸的单链”和“子树完整答案”。
     *
     * <p><b>Info定义：</b>
     * <ul>
     *     <li>{@code maxLine}：必须从当前子树根节点开始，只能选择一个孩子继续向下，
     *     因而可以交给父节点继续拼接。</li>
     *     <li>{@code maxSum}：当前子树内部任意合法路径的最大和，可以同时连接左右单链，
     *     但它是最终候选答案，不能再交给父节点继续延伸。</li>
     * </ul>
     *
     * <p><b>关键限制：</b>一条路径在当前节点最多连接“父、左、右”中的两个方向。
     * {@code maxLine}如果同时选择左右孩子，再连接父节点就会形成三个方向的分叉，因此只能取
     * {@code max(left.maxLine, right.maxLine, 0)}。只有计算以当前节点为最高连接点的完整路径时，
     * 才能同时接收左右两条正贡献单链。
     *
     * <p>本实现显式区分叶节点、双子树和单子树，逻辑正确且便于逐类检查；也可以使用统一的
     * 空树Info和同一组公式消除分支。时间复杂度O(N)，递归栈空间O(H)。
     */
    class Solution20260904 {

        public int maxPathSum(TreeNode root) {
            return process(root).maxSum;
        }

        // TODO: 【重点】为了让父节点能够连续拼接，maxLine必须经过当前节点；同理，父节点使用的
        // l.maxLine/r.maxLine也必须分别经过左、右孩子的头节点。并且maxLine只能选择一边延伸。
        public class Info {
            int maxLine;
            int maxSum;

            public Info(int _l, int _s) {
                maxLine = _l;
                maxSum = _s;
            }
        }

        public Info process(TreeNode cur) {
            if (cur == null) {
                return null;
            }
            Info l = process(cur.left);
            Info r = process(cur.right);

            int maxLine;
            int maxSum;

            if (l == null && r == null) {
                return new Info(cur.val, cur.val);
            } else if (l != null && r != null) {
                // Step1：maxLine必须经过cur；选取的孩子maxLine也必须经过该孩子的头节点，
                // 这样“cur -> child -> child.maxLine”才是一条连续路径。
                // 只能选择一个孩子，或两个孩子都不选。
                // TODO: 【易错点】不能把左右两条maxLine都放进maxLine，否则父节点再连接时会分叉。
                maxLine = cur.val + Math.max(0, Math.max(l.maxLine, r.maxLine));

                // Step2：计算以cur为最高连接点的完整路径；左右正贡献在这里才可以同时选择。
                int throughCur = cur.val;
                if (l.maxLine > 0) {
                    throughCur += l.maxLine;
                }
                if (r.maxLine > 0) {
                    throughCur += r.maxLine;
                }

                // Step3：完整答案可能经过cur，也可能完全位于左子树或右子树。
                maxSum = Math.max(Math.max(r.maxSum, l.maxSum), throughCur);
                return new Info(maxLine, maxSum);
            } else {
                Info available = l != null ? l : r;
                maxLine = cur.val;
                if (available.maxLine > 0) {
                    maxLine += available.maxLine;
                }
                maxSum = Math.max(maxLine, available.maxSum);
                return new Info(maxLine, maxSum);
            }
        }
    }

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
