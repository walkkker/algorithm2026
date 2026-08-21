package frequence.skill;

/**
 * 169. 多数元素
 *
 * <p>给定一个大小为{@code N}的数组，返回其中的多数元素。多数元素是指在数组中出现次数
 * 严格大于{@code N / 2}的元素，题目保证多数元素一定存在。
 *
 * <p><b>Boyer-Moore多数投票：</b>把不同元素两两抵消。即使让每个非多数元素都与多数元素
 * 抵消，由于多数元素出现次数超过数组长度的一半，最终候选值仍然一定是多数元素。
 *
 * <p>{@code count}表示当前候选值相对于其他值的“未抵消票数”：票数归零时，前缀中的票
 * 已经完全抵消，可以从当前位置重新选择候选值。
 *
 * <p><b>限制条件：</b>本题保证多数元素存在，因此最后可以直接返回候选值。如果迁移到
 * “多数元素可能不存在”的题目，必须再遍历一次数组验证候选值出现次数是否超过一半。
 *
 * <p>时间复杂度{@code O(N)}，额外空间复杂度{@code O(1)}。
 */
public class Q169_MajorityElement {

    public static class Solution {

        public int majorityElement(int[] nums) {
            int candidate = 0;
            int count = 0;
            for (int num : nums) {
                if (count == 0) {
                    candidate = num;
                    count++;
                } else if (candidate == num) {
                    count++;
                } else {
                    count--;
                }
            }
            return candidate;
        }
    }
}
