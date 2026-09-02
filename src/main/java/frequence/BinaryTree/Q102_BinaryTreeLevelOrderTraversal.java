package frequence.BinaryTree;

import java.util.*;

/**
 * 102. 二叉树的层序遍历
 *
 * <p>给定一个二叉树的根节点 {@code root}，按照从上到下、从左到右的顺序逐层访问节点，
 * 并返回每一层节点值组成的列表。
 *
 * <p><b>专题归类：</b>BFS、队列与层边界识别。参见同目录《二叉树通用技巧与题型分类.md》
 * 和《二叉树错题本.md》的Q102/Q199条目。
 */
public class Q102_BinaryTreeLevelOrderTraversal {

    /**
     * 2026-09-02 我的curEnd/nextEnd层序遍历实现。
     *
     * <p><b>层边界不变量：</b>{@code curEnd}表示当前层最后一个节点，{@code nextEnd}表示目前
     * 已发现的下一层最后一个节点。每次子节点入队时更新nextEnd；当弹出的cur等于curEnd时，
     * 当前层处理完毕，收集layer，并令{@code curEnd = nextEnd}进入下一层。
     *
     * <p><b>本次错误一：</b>题目允许空树。必须在根节点入队前处理{@code root == null}，否则
     * 后续取出空节点并访问{@code cur.val}会触发NullPointerException。需要精确区分：当前使用的
     * LinkedList技术上允许保存null，但本算法不允许空节点进入队列；如果改用ArrayDeque，加入
     * null的动作本身就会直接抛出NullPointerException。
     *
     * <p><b>本次易错点二：</b>{@code ans.add(layer)}保存的是当前ArrayList对象的引用，不是内容
     * 快照。收集一层后必须创建新的layer对象；如果直接执行{@code layer.clear()}并继续复用，
     * ans中已经保存的同一对象也会被清空。另一种合法写法是先保存快照
     * {@code ans.add(new ArrayList<>(layer))}，然后再clear。
     */
    class Solution20260902 {

        public List<List<Integer>> levelOrder(TreeNode root) {
            // TODO: 【错误-edge case】题目允许0个节点，必须在root入队前处理空树。
            if (root == null) {
                return new ArrayList<>();
            }

            List<List<Integer>> ans = new ArrayList<>();
            Queue<TreeNode> queue = new LinkedList<>();
            TreeNode curEnd = root;
            TreeNode nextEnd = null;
            queue.add(root);
            List<Integer> layer = new ArrayList<>();

            while (!queue.isEmpty()) {
                TreeNode cur = queue.poll();
                layer.add(cur.val);

                // 层序遍历的扩展动作：加入孩子时，最后加入的孩子就是目前下一层的尾节点。
                if (cur.left != null) {
                    nextEnd = cur.left;
                    queue.add(cur.left);
                }
                if (cur.right != null) {
                    nextEnd = cur.right;
                    queue.add(cur.right);
                }

                if (cur == curEnd) {
                    curEnd = nextEnd;
                    ans.add(layer);

                    // TODO: 【易错】ans保存的是layer引用。不能add后执行layer.clear()继续复用，
                    // 否则会同步清空ans中已经收集的这一层；这里必须切换到新的ArrayList对象。
                    layer = new ArrayList<>();
                }
            }
            return ans;
        }
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        // TODO: 【错误1】树中节点数目在范围 [0, 2000] 内，注意有0。 不做null检查，下面就会NPE ->  cur.val
        if (root == null) {
            return new ArrayList<>();
        }

        List<List<Integer>> ans = new ArrayList<>();
        TreeNode curEnd = root;
        // TODO: 【错误2】需要初始化（即便是赋值null），不然存在java语法错误。“Variable 'nextEnd' might not have been initialized
        TreeNode nextEnd = null;
        Queue<TreeNode> queue = new LinkedList<>();
        // curEnd = root;
        queue.add(root);
        List<Integer> tmp = new ArrayList<>();
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            tmp.add(cur.val);

            if (cur.left != null) {
                queue.add(cur.left);
                nextEnd = cur.left;
            }
            if (cur.right != null) {
                queue.add(cur.right);
                nextEnd = cur.right;
            }

            if (cur == curEnd) {
                ans.add(tmp);
                tmp = new ArrayList<>();
                curEnd = nextEnd;
            }
        }
        return ans;
    }
}
