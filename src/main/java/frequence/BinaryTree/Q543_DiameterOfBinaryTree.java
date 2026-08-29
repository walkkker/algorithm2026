package frequence.BinaryTree;

/**
 * 543. 二叉树的直径
 *
 * <p>给定一个二叉树的根节点 {@code root}，返回该二叉树的直径。
 * 二叉树的直径是任意两个节点之间最长路径的长度，该路径可能经过根节点，也可能不经过根节点。
 * 两个节点之间路径的长度以路径经过的边数表示。
 *
 * <p><b>专题归类：</b>底向上Info、向父节点返回高度、子树内维护完整直径，并统一节点数与边数。
 * 参见同目录《二叉树通用技巧与题型分类.md》、《二叉树错题本.md》以及
 * 《二叉树递归套路 易犯错误注意事项.md》的Q543条目。
 */
public class Q543_DiameterOfBinaryTree {

    class Solution {
        public int diameterOfBinaryTree(TreeNode root) {
            return process(root).maxDistance - 1;  // TODO: 【错误】本题的距离表示边的数量。 需要我们结果-1 （因为我们的结果求的是点的数量）
        }

        public Info process(TreeNode cur) {
            if (cur == null) {
                return new Info(0, 0);
            }
            Info left = process(cur.left);
            Info right = process(cur.right);

            int p1 = left.height + right.height + 1;

            int height = Math.max(left.height, right.height) + 1;
            int maxDistance = Math.max(Math.max(left.maxDistance, right.maxDistance), p1);
            return new Info(height, maxDistance);
        }


        public class Info {
            int height;
            int maxDistance;

            public Info(int _h, int _m) {
                height = _h;
                maxDistance = _m;
            }
        }
    }}
