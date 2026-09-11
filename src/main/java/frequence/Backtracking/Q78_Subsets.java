package frequence.Backtracking;

import java.util.*;

/**
 * 78. 子集
 *
 * TODO：【错误】本题的子集 其实就是指 子序列。 递归上就是 在每个元素选择 要/不要。 使用List<Integer>接
 *
 * <p>给定一个元素互不相同的整数数组{@code nums}，返回该数组所有可能的子集。解集不能包含
 * 重复子集，可以按任意顺序返回。
 */
public class Q78_Subsets {

    /**
     * 2026-09-11 复盘版本：每个元素分别枚举“选择”和“不选择”。
     */
    class SolutionReviewed20260911 {

        /**
         * 回溯：
         * 1. 依然是到达决策树终点时记录。（不需要提前记录！！！）
         * 2. 每个节点的枚举行为不再是全排列的for，而是选择当前 / 不选择当前。
         *
         * 实现的逻辑依然是：
         * 1. 核心思想：缩小问题规模。
         * 2. 语义层定义后的实现：确定i位置选/不选，剩下的交给剩余后缀递归。
         *    直到i==arr.length，此时属于base case。
         *
         * <p>TODO: 【AI补充-适用范围】“到达决策树终点时记录，不需要提前记录”是当前
         * “选/不选”二叉决策树的写法，不是所有子集代码的绝对要求。另一种start + for模型
         * 会在每个递归节点先记录当前path，再继续枚举后续元素，两种模型都能完整生成子集。
         */
        public List<List<Integer>> subsets(int[] nums) {
            List<Integer> path = new ArrayList<>();
            List<List<Integer>> ans = new ArrayList<>();
            process(nums, 0, path, ans);
            return ans;
        }

        /**
         * 我的递归定义：
         * 已经对nums[0, i)中的每个元素作出了选或不选的决定，选择结果记录在path中；
         * 当前方法负责枚举nums[i, nums.length)的所有选择组合，并把完整子集加入ans。
         *
         * <p>TODO: 【AI补充-后置条件】方法返回时，path必须恢复成进入本方法时的状态，
         * 保证当前递归节点的兄弟分支从相同的已选前缀开始。
         */
        private void process(
                int[] nums,
                int i,
                List<Integer> path,
                List<List<Integer>> ans) {

            if (i == nums.length) {
                // 所有元素均已完成选/不选决策，此时才形成当前模型的一片叶子。
                ans.add(new ArrayList<>(path));
                return;
            }

            // 枚举行为一：不选择nums[i]，path没有变化，因此不需要恢复现场。
            process(nums, i + 1, path, ans);

            // 枚举行为二：选择nums[i]。
            path.add(nums[i]);
            process(nums, i + 1, path, ans);
            // List是共享可变路径，选择分支返回后必须撤销本层选择。
            path.remove(path.size() - 1);
        }
    }

    /**
     因为会还原现场，所以每一个递归节点的排列都不一样。 => 不是的，单纯收集List，会出现大量重复。 因为固定第一个节点后，所有第二个节点的递归都会重复收集 第一个节点对应的字符串。  （不过这不是本题的子集了，属于子串）


     【错误】本题子集其实对应子序列，看题，看例子。
     */
    class Solution {
        public List<List<Integer>> subsets(int[] nums) {
            List<List<Integer>> ans = new ArrayList<>();
            List<Integer> tmp = new ArrayList<>();
            process(nums, 0, tmp, ans);
            return ans;
        }

        public void process(int[] nums, int index, List<Integer> tmp, List<List<Integer>> ans) {
            if (index == nums.length) {
                ans.add(new ArrayList<>(tmp));
                return;
            }
            // 1. 不选当前元素
            process(nums, index + 1, tmp, ans);

            // 2. 选当前元素 + 还原现场（去掉元素，这样回到上层节点的 tmp对应的路径信息是对应的）
            tmp.add(nums[index]);
            process(nums, index + 1, tmp, ans);
            tmp.remove(tmp.size() - 1);
        }


    }}
