package frequence;

import java.util.*;

/**
 * HashMap
 *
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 要求时间复杂度为 O(N)。
 *
 * TODO：核心思路： 1. 有前序，不是起点，直接跳过。  2. 无前序，是起点，开始向后遍历
 *
 * TODO: 【重要、高级】 通用思想：当从每个元素出发会产生重复遍历时(换句话说，【不想让每个元素出现重复遍历】)，【只从能够代表整个结构的
 *     “起点”出发】。 =》 【由此，解题核心变成了** 找起点 **】本题中，“不存在前驱的数字”就是连续序列的唯一代表点（人话就是 起点）。
 *
 * TODO: <p><b>核心结论：只从连续序列的起点向后扩展。</b>
 * 当 {@code num - 1} 不在集合中时，{@code num} 才是某个连续序列的起点；
 * 如果 {@code num - 1} 已经存在，说明当前数字属于别的序列中间，直接跳过，避免重复扫描。
 *
 * <p>例如：
 * <pre>{@code
 * nums = [100, 4, 200, 1, 3, 2]
 * set  = {100, 4, 200, 1, 3, 2}
 *
 * 1 的前驱 0 不存在：从 1 开始扩展，得到 1 -> 2 -> 3 -> 4，长度为 4
 * 2、3、4 的前驱存在：它们不是起点，全部跳过
 * 100 和 200 没有前驱：分别形成长度为 1 的序列
 * }</pre>
 *
 * <p><b>必须遍历去重后的 HashSet，不能遍历原数组。</b>
 * 例如 {@code nums = [1, 1, 1, 1, 2, 3, 4]}。如果遍历原数组，
 * 每个重复的 {@code 1} 都会再次扫描 {@code 2、3、4}；遍历 HashSet 后，
 * 起点 {@code 1} 只会处理一次。HashSet 同时提供均摊 O(1) 的前驱、后继查询。
 *
 * <p>虽然代码结构是 {@code for + while}，但每个序列只从唯一的起点完整扩展一次，
 * 每个不同数字至多在扩展过程中被访问一次，所以总时间复杂度仍为 O(N)，空间复杂度为 O(N)。
 *
 * <p><b>通用思想：</b>当从每个元素出发会产生重复遍历时，只从能够代表整个结构的
 * “起点”出发。本题中，“不存在前驱的数字”就是连续序列的唯一代表点。
 */
public class Q128_LongestConsecutiveSequence {

    /**
     * 这个版本是错误的。 因为这对应的是 序列元素在原数组中有序。 而题目说的是，序列元素在原数组中无序，也算作连续。
     *
     * @param nums
     * @return
     */
    public int WrongVersionLongestConsecutive(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i] - 1)) {
                map.put(nums[i], map.get(nums[i] - 1) + 1);
            } else {
                map.put(nums[i], 1);
            }
        }
        int ans = 0;
        for (int len : map.values()) {
            ans = Math.max(ans, len);
        }
        return ans;
    }

    /**
     * HashSet 最优解：先去重，再且仅从不存在前驱的数字开始向后统计连续长度。
     *
     * TODO：【核心】一句话，找开头，向后遍历。 这样就是O(N)
     *
     * @param nums 未排序的整数数组
     * @return 最长连续序列的长度
     */
    public int longestConsecutive(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }

        int ans = 0;
        for (int num : set) {
            if (set.contains(num - 1)) {
                continue;
            }

            int cur = num;
            int len = 1;

            while (set.contains(cur + 1)) {
                cur++;
                len++;
            }

            ans = Math.max(ans, len);
        }
        return ans;
    }

}
