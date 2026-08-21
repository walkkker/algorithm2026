package frequence.array;

import java.util.*;

/**
 * 本题的标准解法是（因为不让使用除法）
 *  1.【前缀/后缀】乘积数组。 -> ans[i] = leftProduct[i-1] * rightProduct[i+1]
 *  2. 优化版本(空间复杂度降为O(1)) -> 单变量leftProduct先赋值ans[i]， 然后反方向循环乘积rightProduct
 *
 */
public class Q238 {

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
