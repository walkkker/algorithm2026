package frequence.skill;

/**
 * 136. 只出现一次的数字
 *
 * <p>给定一个非空整数数组，除某个元素只出现一次外，其余每个元素均出现两次，
 * 找出只出现一次的元素。要求使用线性时间复杂度，并且只使用常数额外空间。
 *
 * <p><b>核心性质：</b>异或可以理解为二进制位上的无进位相加，满足交换律和结合律：
 * <pre>
 * a ^ a = 0
 * a ^ 0 = a
 * </pre>
 * 成对元素经过异或后全部抵消，最终只剩下出现一次的元素。
 *
 * <p>时间复杂度{@code O(N)}，额外空间复杂度{@code O(1)}。
 */
public class Q136_SingleNumber {

    public static class Solution {

        public int singleNumber(int[] nums) {
            // TODO: 【错误】异或累积值不能初始化为1，因为异或运算不存在“a ^ 1 = a”。
            // 错误行：int eor = 1;
            // 修正依据：0才是异或运算的单位元，a ^ 0 = a。
            int eor = 0;
            for (int num : nums) {
                eor ^= num;
            }
            return eor;
        }
    }
}
