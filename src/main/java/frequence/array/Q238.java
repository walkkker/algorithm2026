package frequence.array;

import java.util.*;

/**
 * 本题的标准解法是（因为不让使用除法）
 *  1.【前缀/后缀】乘积数组。 -> ans[i] = leftProduct[i-1] * rightProduct[i+1]
 *  2. 优化版本(空间复杂度降为O(1)) -> 单变量leftProduct先赋值ans[i]， 然后反方向循环乘积rightProduct
 *
 */
public class Q238 {

    /**
     * 思路： 两个数组，前缀乘积数组 + 后缀乘积数组。
     *
     * 最终优化空间：复用ans数组保存后缀。  使用变量left存储前缀。
     *
     * <p><b>2026-08-29 重点：</b>这版代码与常见的“ans先保存左侧乘积”写法方向相反。
     * 第一遍结束后，{@code ans[i]}表示包含{@code nums[i]}在内的后缀乘积
     * {@code nums[i] * nums[i + 1] * ... * nums[n - 1]}。因此计算位置i的答案时，
     * 必须使用{@code ans[i + 1]}，它才是严格位于i右侧的乘积。
     *
     * <p><b>循环不变量：</b>
     * <ul>
     *     <li>进入第二个循环的位置i时，{@code left}等于{@code nums[0...i-1]}的乘积，不包含当前元素。</li>
     *     <li>{@code ans[i + 1]}等于{@code nums[i+1...n-1]}的乘积，不包含当前元素。</li>
     *     <li>所以{@code left * ans[i + 1]}正好等于“除nums[i]以外所有元素的乘积”。</li>
     * </ul>
     *
     * <p><b>易错点：</b>
     * <ol>
     *     <li>第二个循环只能遍历到{@code ans.length - 2}，因为代码会访问{@code ans[i + 1]}。</li>
     *     <li>必须先计算{@code ans[i]}，再执行{@code left *= nums[i]}；反过来会错误地包含当前元素。</li>
     *     <li>最后一个位置右侧没有元素，其右侧乘积是乘法单位元1，因此最终单独赋值为{@code left}。</li>
     * </ol>
     */
    public static class Solution20260829 {

        public int[] productExceptSelf(int[] nums) {
            int left = 1;
            int[] ans = new int[nums.length];
            ans[ans.length - 1] = nums[nums.length - 1];
            for (int i = nums.length - 2; i >= 0; i--) {
                ans[i] = nums[i] * ans[i + 1];
            }
            for (int i = 0; i < ans.length - 1; i++) {
                ans[i] = left * ans[i + 1];
                left = left * nums[i];
            }
            ans[ans.length - 1] = left;
            return ans;
        }
    }

    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] ans = new int[len];
        // TODO: Arrays.fill(ans, 1) 这一步绝对不能少。 因为对于 最左侧和最右侧的 ans[i]而言， 因为缺少一侧的乘积，后面又要乘以另一侧的乘积，所以应该初始化为1.
        //   当然，在我们的实现中，因为一开始是把leftProduct赋值给ans[i]。所以，只有 ans[0]没有正确初始化。可以简化赋值语句。
        ans[0] = 1;   // TODO：这一步绝对不能遗漏

        int leftProduct = nums[0];
        for (int i = 1; i < len; i++) {
            ans[i] = leftProduct;
            leftProduct *= nums[i];
        }
        int rightProduct = nums[len - 1];
        for (int i = len - 2; i >= 0; i--) {
            ans[i] *= rightProduct;
            rightProduct *= nums[i];
        }
        return ans;
    }

}
