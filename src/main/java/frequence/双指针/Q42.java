package frequence.双指针;

/**
 * 经典接雨水：
 * 1. 双指针
 * 2. 前缀数组（leftMax, rightMax）
 * 3. 单调栈
 */
public class Q42 {
    /**
     *  双指针解题思路：
     *      1. 计算每列的接水量   2. 去掉前缀数组leftMax/rightMax，变成双指针对应的双变量leftMax/rightMax。
     *      2. 其中leftMax包含范围是[0,L] 注意是双闭区间； 同理 rightMax 对应[R, height.length - 1] 双闭区间
     *  【错误点】
     *  TODO：看代码，对于while循环，里面的 【判断变量一定要在 循环体的执行末尾做出改变（++或--）】。
     *       后面不能再有 关于判断变量的任何语句（比如数组下标访问），因为此时改变后的判断变量 没有经过while的合法校验。
     *       -> 此时执行 关于任何判断变量的语句会出错。 （比如 数组下标访问 会报 IndexOutOfBoundsException）
     *
     *
     * @param height
     * @return
     */
    // 双指针： 1. 计算每列的接水量   2. 去掉前缀数组leftMax/rightMax，变成双指针对应的双变量leftMax/rightMax。
    //        2. 其中leftMax包含范围是[0,L] 注意是双闭区间； 同理 rightMax 对应[R, height.length - 1] 双闭区间
    public static int trap(int[] height) {

        int L = 0;
        int R = height.length - 1;
        int leftMax = height[0];
        int rightMax = height[height.length - 1];
        int ans = 0;
        while (L <= R) {
            leftMax = Math.max(leftMax, height[L]);
            rightMax = Math.max(rightMax, height[R]);
            if (leftMax <= rightMax) {
                ans += leftMax - height[L];
                L++;  // TODO: 【再次强调！！！】 对于while而言，最后一行必须为单一的 变量+-。 后面不能再放array[index]，因为这时候的index没有经过while检查。
                // TODO: 【错误】下面这行不能放在这里。 对于while而言，最后一行必须为单一的 变量+-。 后面不能再放array[index]，因为这时候的index没有经过while检查。
                //  错误行：leftMax = Math.max(leftMax, height[L]);
            } else {
                ans += rightMax - height[R];
                R--;
                // TODO:  【同上错误】rightMax = Math.max(rightMax, height[R]);
            }
        }
        return ans;
    }


}
