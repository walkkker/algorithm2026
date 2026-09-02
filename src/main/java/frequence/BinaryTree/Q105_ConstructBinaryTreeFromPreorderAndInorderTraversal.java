package frequence.BinaryTree;

import java.util.*;
/**
 * 105. 从前序与中序遍历序列构造二叉树
 *
 * TODO: 【注意】第一个版本不是最优解，最差情况会退化到O(N^2)。 因为最差情况，递归深度N, 每一层递归 查找inorder目标元素N
 * TODO: 【注意】第二个版本是最优解。 加一个全局变量HashMap，把每层递归查找头元素的时间 优化到O（1）.
 *
 * <p>给定两个整数数组 {@code preorder} 和 {@code inorder}，其中 {@code preorder}
 * 是二叉树的前序遍历，{@code inorder} 是同一棵树的中序遍历，请构造并返回这棵二叉树。
 *
 * <p>题目保证输入的前序遍历和中序遍历均不包含重复元素，并且二者一定对应同一棵二叉树。
 *
 * <p><b>专题归类：</b>数组区间递归、根位置定位和左右子树长度同步切分。
 * 参见同目录《二叉树通用技巧与题型分类.md》和《二叉树错题本.md》的Q105条目。
 */
public class Q105_ConstructBinaryTreeFromPreorderAndInorderTraversal {

    /**
     * 2026-09-03 复写版本：前序确定根节点，中序确定左右子树范围，并使用HashMap优化根位置查询。
     *
     * <p><b>递归契约：</b>根据{@code preorder[preL..preR]}和
     * {@code inorder[inL..inR]}构造同一棵子树，并返回该子树根节点。
     * 前序区间首元素是当前根；根在中序区间中的位置决定左子树节点数：
     * {@code leftSize = rootIndex - inL}。四个子区间必须全部由这个长度统一推导。
     *
     * <p><b>复杂度：</b>预处理HashMap需要O(N)时间和O(N)空间；每个节点只创建一次，因此构造
     * 阶段为O(N)，递归栈为O(H)。若每层都线性扫描中序数组，斜树情况下会退化为O(N^2)。
     *
     * <p><b>重要前提：</b>节点值不能重复。否则{@code value -> inorderIndex}不能唯一确定根位置，
     * 仅凭前序和中序遍历通常也不能唯一还原二叉树。
     *
     * <p><b>状态初始化：</b>在入口方法中重新创建{@code map}，可以确保同一个Solution对象连续
     * 调用{@code buildTree}时，每次调用都使用独立索引状态。字段声明处初始化并不必然导致本题
     * 出错，但复用同一个可变Map会保留旧条目；入口处重建更符合“每次调用状态隔离”的原则。
     */
    class Solution20260903 {

        HashMap<Integer, Integer> map;

        public TreeNode buildTree(int[] preorder, int[] inorder) {
            // TODO: 【注意点】入口处重新初始化，避免可变字段跨多次buildTree调用保留旧状态。
            map = new HashMap<>();
            for (int i = 0; i < inorder.length; i++) {
                map.put(inorder[i], i);
            }
            return process(preorder, 0, preorder.length - 1,
                    inorder, 0, inorder.length - 1);
        }

        /**
         * 当前两组闭区间都表示同一棵待构造子树。
         */
        private TreeNode process(int[] pre, int preL, int preR,
                                 int[] in, int inL, int inR) {
            if (preL > preR) {
                return null;
            }

            int rootValue = pre[preL];
            int rootIndex = map.get(rootValue);
            int leftSize = rootIndex - inL;
            // TODO: 【可精简】rightSize可以帮助理解区间，但当前公式没有使用该变量。
            int rightSize = inR - rootIndex;

            TreeNode cur = new TreeNode(rootValue);
            cur.left = process(pre, preL + 1, preL + leftSize,
                    in, inL, rootIndex - 1);
            cur.right = process(pre, preL + leftSize + 1, preR,
                    in, rootIndex + 1, inR);
            return cur;
        }
    }


    /**
     * TODO:  这是做的第一个版本。 不是最优解。
     * TODO： 我做的不是最优解，最差情况退化到O(N^2)。  左下到右上的单链表。
     * TODO： 【问题】他的问题在于，每次递归时， 遍历inorder 去确定 headNum的位置。 最差每次要O(N)。 而如果你使用了HashMap，这个过程就能优化到O(1)
     */
    class Solution {
        public TreeNode buildTree(int[] preorder, int[] inorder) {
            int len = preorder.length;
            return process(preorder, 0, len - 1, inorder, 0, len - 1);
        }

        // 要递归，参数总要有范围的概念（链表/二叉树自带null，但是数组需要 l, r）
        public TreeNode process(int[] preorder, int l1, int r1, int[] inorder, int l2, int r2) {
            if (l1 > r1) {
                return null;
            }
            if (l1 == r1) {
                return new TreeNode(preorder[l1]);
            }
            int headNum = preorder[l1];
            int pos = -1;
            for (int i = l2; i <= r2; i++) {
                if (inorder[i] == headNum) {
                    pos = i;
                    break;
                }
            }
            int leftLen = pos - l2;
            int rightLen = r2 - pos;
            TreeNode cur = new TreeNode(headNum);
            cur.left = process(preorder, l1 + 1, l1 + leftLen, inorder, l2, pos - 1);
            cur.right = process(preorder, l1 + leftLen + 1, r1, inorder, pos + 1, r2);
            return cur;
        }
    }


    /**
     *  TODO: 下面的版本是最优解。
     */
    class Solution2 {

        HashMap<Integer, Integer> map = new HashMap<>();

        public TreeNode buildTree(int[] preorder, int[] inorder) {
            for (int i = 0; i < inorder.length; i++) {
                map.put(inorder[i], i);
            }

            int len = preorder.length;
            return process(preorder, 0, len - 1, inorder, 0, len - 1);
        }

        // 要递归，参数总要有范围的概念（链表/二叉树自带null，但是数组需要 l, r）
        public TreeNode process(int[] preorder, int l1, int r1, int[] inorder, int l2, int r2) {
            if (l1 > r1) {
                return null;
            }
            if (l1 == r1) {
                return new TreeNode(preorder[l1]);
            }
            int headNum = preorder[l1];
            int pos = map.get(headNum);
            int leftLen = pos - l2;
            int rightLen = r2 - pos;
            TreeNode cur = new TreeNode(headNum);
            cur.left = process(preorder, l1 + 1, l1 + leftLen, inorder, l2, pos - 1);
            cur.right = process(preorder, l1 + leftLen + 1, r1, inorder, pos + 1, r2);
            return cur;
        }
    }
}
