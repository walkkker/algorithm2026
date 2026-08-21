package frequence.Backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * 46. 全排列
 *
 * <p>给定一个不含重复数字的整数数组{@code nums}，返回其中所有可能的全排列。答案可以按任意
 * 顺序返回。
 *
 * <p><b>递归树模型：</b>{@code index}表示当前需要确定的排列位置。在
 * {@code [index, nums.length)}范围内依次选择一个元素，通过交换将它放到{@code index}，
 * 然后递归确定下一个位置。
 *
 * <p><b>递归不变量：</b>每次进入{@code process(nums, index, ans)}时：
 * <pre>
 * nums[0, index)            已经确定的排列前缀
 * nums[index, nums.length)  当前层可以选择的剩余元素
 * </pre>
 *
 * <p><b>为什么必须恢复现场：</b>{@code swap(nums, index, i)}同时修改了两个数组位置。
 * 当前分支递归结束后，必须再次交换相同的两个位置，将数组恢复成进入该分支之前的父状态。
 * 这样for循环的每个兄弟分支才能从完全相同的候选集合和排列顺序出发。
 *
 * <p>如果输入为{@code [1,2,3]}且不执行第二次swap，第一棵子树结束后数组会停留在
 * {@code [1,3,2]}，后续兄弟分支将在被修改的数组上继续交换。最终可能重复生成
 * {@code [1,2,3]、[1,3,2]}，同时遗漏{@code [2,1,3]、[2,3,1]}。
 *
 * <p>这也是“固定长度数组通常不需要恢复”的重要例外：
 * <pre>
 * 固定下标覆盖：兄弟分支会重写同一位置，通常不需要恢复。
 * 数组交换：改变了父节点的候选状态，递归结束后必须交换回来。
 * </pre>
 *
 * <p>共有{@code N!}个排列，每个答案需要复制{@code N}个元素，时间复杂度为
 * {@code O(N * N!)}；递归栈空间为{@code O(N)}，返回结果空间为{@code O(N * N!)}。
 *
 * <p>本文件是“递归树生成全部排列”的回溯视角。Q31直接后继、基于Q31的字典序
 * 全排列和Q60阶乘反排名的横向对比，见
 * {@code frequence/permutation/字典序排列家族.md}。
 */
public class Q46_Permutations {

    class Solution {
        public List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> ans = new ArrayList<>();
            process(nums, 0, ans);
            return ans;
        }

        public void process(int[] nums, int index, List<List<Integer>> ans) {
            if (index == nums.length) {
                List<Integer> tmp = new ArrayList<>();
                for (int n : nums) {
                    tmp.add(n);
                }
                ans.add(tmp);
                return;   // TODO: 收集完千万别忘了 return。
            }

            for (int i = index; i < nums.length; i++) {
                // 做选择：把当前候选元素放到index位置。
                swap(nums, index, i);
                process(nums, index + 1, ans);
                // TODO: 【关键-必须恢复现场】撤销当前选择，保证下一个i从相同父状态出发。
                // 如果省略，兄弟分支会在被上一分支修改过的数组上继续交换，造成重复和遗漏。
                swap(nums, index, i);
            }
        }


        public void swap(int[] arr, int i, int j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }}
