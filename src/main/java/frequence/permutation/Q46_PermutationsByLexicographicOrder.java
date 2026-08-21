package frequence.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 46. 全排列的字典序生成版本
 *
 * <p>项目中已经存在基于交换回溯的
 * {@code frequence.Backtracking.Q46_Permutations}。该版本不复制回溯模板，而是用来验证
 * Q31与全排列之间的关系：
 *
 * <pre>
 * 先将数组排为字典序最小状态；
 * 收集当前排列；
 * 反复调用Q31进入直接后继；
 * 回到最小排列时结束。
 * </pre>
 *
 * <p>与回溯版本的区别：
 * <ul>
 *     <li>回溯版本通过“当前位置放哪个元素”枚举递归树。</li>
 *     <li>字典序版本将排列当成一条有序序列，每次向后移动一个排名。</li>
 * </ul>
 *
 * <p>输入互异时共有{@code N!}个结果；含重复值时，Q31会直接跳到下一个不同排列，
 * 因此本方法返回所有不重复排列。方法内部先复制输入，不会修改调用者的数组。
 *
 * <p>对于互异元素，时间复杂度为{@code O(N * N!)}，结果空间为
 * {@code O(N * N!)}，排除返回结果后的额外空间为{@code O(N)}。
 */
public class Q46_PermutationsByLexicographicOrder {

    public static class Solution {

        public List<List<Integer>> permute(int[] nums) {
            int[] current = nums.clone();
            Arrays.sort(current);
            int[] first = current.clone();
            List<List<Integer>> ans = new ArrayList<List<Integer>>();
            Q31_NextPermutation.Solution nextPermutation
                    = new Q31_NextPermutation.Solution();

            do {
                ans.add(toList(current));
                nextPermutation.nextPermutation(current);
            } while (!Arrays.equals(current, first));

            return ans;
        }

        private List<Integer> toList(int[] nums) {
            List<Integer> result = new ArrayList<Integer>(nums.length);
            for (int num : nums) {
                result.add(num);
            }
            return result;
        }
    }
}
