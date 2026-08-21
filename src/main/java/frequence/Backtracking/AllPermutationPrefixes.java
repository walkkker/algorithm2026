package frequence.Backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有排列前缀（All Permutation Prefixes）。
 *
 * <p>更标准的数学描述是：枚举{@code N}个互异元素的所有部分排列，即对每个
 * {@code k = 0..N}，枚举所有从{@code N}个元素中不重复选择{@code k}个元素形成的有序序列。
 *
 * <p>例如输入{@code [1,2]}，结果为：
 * <pre>
 * []
 * [1]
 * [1,2]
 * [2]
 * [2,1]
 * </pre>
 *
 * <p><b>适用场景：</b>
 * <ul>
 *     <li>结果长度可以是{@code 0..N}中的任意值；</li>
 *     <li>同一个元素在一条结果中不能重复使用；</li>
 *     <li>元素顺序有意义，因此{@code [1,2]}和{@code [2,1]}是两个不同答案；</li>
 *     <li>需要枚举所有长度的有序选择，而不只是长度固定为{@code N}的全排列。</li>
 * </ul>
 *
 * <p><b>不适用场景：</b>如果顺序不重要，例如子集或组合问题，本算法会把同一组元素的不同
 * 顺序当成不同答案，从而产生组合意义上的重复。
 *
 * <p><b>递归不变量：</b>
 * <pre>
 * nums[0, index)            当前节点已经确定的排列前缀，也是当前节点收集的答案
 * nums[index, nums.length)  当前层仍然可以选择的剩余元素
 * </pre>
 *
 * <p>每个递归节点都代表一个合法的部分排列，因此进入节点后立即收集前缀。交换表示选择，
 * 递归返回后的反向交换表示撤销选择，保证所有兄弟分支从相同父状态出发。
 *
 * <p>如果输入包含重复值，不同位置上的相同值可能生成内容相同的结果；若题目要求内容去重，
 * 需要在每一层增加去重集合。
 *
 * <p>结果数量为：
 * <pre>
 * sum(P(N, k)), k = 0..N
 * </pre>
 * 复制所有答案的时间和结果空间均与输出总元素数量成正比，最坏可记为
 * {@code O(N * N!)}；递归栈空间为{@code O(N)}。
 */
public class AllPermutationPrefixes {

    public List<List<Integer>> allPermutationPrefixes(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        process(nums, 0, ans);
        return ans;
    }

    private void process(int[] nums, int index, List<List<Integer>> ans) {
        // 当前递归节点代表长度为index的排列前缀。
        List<Integer> prefix = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            prefix.add(nums[i]);
        }
        ans.add(prefix);

        // 已经使用全部元素，当前节点是完整排列对应的叶子节点。
        if (index == nums.length) {
            return;
        }

        for (int i = index; i < nums.length; i++) {
            // 做选择：把一个剩余元素放到当前前缀末尾。
            swap(nums, index, i);
            process(nums, index + 1, ans);
            // 恢复现场：保证下一个兄弟分支从相同父状态出发。
            swap(nums, index, i);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
