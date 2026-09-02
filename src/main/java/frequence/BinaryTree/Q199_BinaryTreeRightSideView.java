package frequence.BinaryTree;

import java.util.*;

/**
 * 199. 二叉树的右视图
 *
 * TODO：【思路】层级遍历加强版（识别每一层），每一层最后一个节点curEnd 汇总就是答案。
 *
 * <p>给定一个二叉树的根节点 {@code root}，假设从该二叉树的右侧观察，
 * 返回按照从顶部到底部顺序能够看到的节点值。
 *
 * <p><b>专题归类：</b>层序遍历的层边界增强；每层最后一个节点即右视图结果。
 * 参见同目录《二叉树通用技巧与题型分类.md》和《二叉树错题本.md》的Q102/Q199条目。
 */
public class Q199_BinaryTreeRightSideView {

    /**
     * 2026-09-03 复写版本：使用 {@code curEnd/nextEnd} 标记层边界。
     *
     * <p><b>本次错误：</b>创建了队列，却遗漏了 {@code queue.add(root)}。BFS初始化包含两个
     * 相互独立的步骤：创建用于保存待处理节点的队列，以及把搜索起点放入队列。只完成第一步时，
     * 队列从始至终为空，{@code while (!queue.isEmpty())}一次也不会执行，方法会静默返回空结果。
     * 这种错误通常不抛异常，因此比直接报错更容易被忽略。
     *
     * <p>孩子按“先左后右”的顺序入队，所以每层最后出队的节点就是从右侧能看到的节点。
     * 每次加入孩子时更新{@code nextEnd}；处理到{@code curEnd}时，当前层结束，收集当前节点并
     * 将{@code curEnd}更新为下一层的最后一个节点。
     */
    class Solution20260903 {

        public List<Integer> rightSideView(TreeNode root) {
            if (root == null) {
                return new ArrayList<>();
            }
            List<Integer> ans = new ArrayList<>();
            // 分层BFS：curEnd是当前层最后一个节点，nextEnd是目前发现的下一层最后一个节点。
            TreeNode curEnd = root;
            TreeNode nextEnd = null;
            Queue<TreeNode> queue = new LinkedList<>();
            // TODO: 【错误-遗漏】BFS初始化包含“创建队列 + 起点入队”，不能只创建空队列。
            // 错误后果：队列为空，while一次也不执行，最终静默返回空List。
            queue.add(root);
            while (!queue.isEmpty()) {
                TreeNode cur = queue.poll();
                if (cur.left != null) {
                    queue.add(cur.left);
                    nextEnd = cur.left;
                }
                if (cur.right != null) {
                    queue.add(cur.right);
                    nextEnd = cur.right;
                }
                if (cur == curEnd) {
                    curEnd = nextEnd;
                    ans.add(cur.val);
                }
            }
            return ans;
        }
    }

    public List<Integer> rightSideView(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<Integer> ans = new ArrayList<>();
        // 层级遍历，收集每一层的最后一个节点（等同于end）
        TreeNode curEnd = root;
        TreeNode nextEnd = null;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            if (cur.left != null) {
                queue.add(cur.left);
                nextEnd = cur.left;
            }

            if (cur.right != null) {
                queue.add(cur.right);
                nextEnd = cur.right;
            }

            if (cur == curEnd) {  // TODO：这一步只存在两部分：1.【必做】curEnd=nextEnd 2. 【按题目要求收集信息】
                curEnd = nextEnd;
                ans.add(cur.val);
            }
        }
        return ans;
    }
}
