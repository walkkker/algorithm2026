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
