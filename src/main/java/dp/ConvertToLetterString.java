package dp;

/**
 * - [把数字翻译成字符串](src/main/java/dp/ConvertToLetterString.java):
 * - https://leetcode-cn.com/problems/ba-shu-zi-fan-yi-cheng-zi-fu-chuan-lcof/ . “把数字翻译成字符串”
 * - 对于每一个i或（i, i+1）的pair，直至 i == N， 从左至右的递归
 * - 【错误点】 1) base case 返回值。 并且经尝试，base case 必须是 i==length，不能是i==length-1。 返回值要为1.
 * - 【错误点】 2) 在暴力递归转动态规划的过程中，if else 没有理清楚。 暴力递归可以不那么清晰写if-else是因为有些if语句中有return语句。 而改成动态规划的过程中，因为没有返回return了改成了dp[i]，由于只允许走一条分支，所以必须补上 if - else；说白了，创建条件分支结构时，没有必要非要省if-else，先保证结果的正确性
 */
public class ConvertToLetterString {
    // str只含有数字字符0~9
    // 返回多少种转化方案
    public int crackNumber(int ciphertext) {
        String s = String.valueOf(ciphertext);
        char[] chs = s.toCharArray();
        int n = chs.length;
        int[] dp = new int[n + 1];
        // 核心1： 常见的从左到右的尝试模型， [1,n-1]的位置，我们让他到达n，此时dp[n]=1 代表完成了一种解密
        dp[n] = 1;
        for (int i = n - 1; i >= 0; i--) {
            int p1 = dp[i + 1];
            // 核心2：这里的分析
            // (1) 依据依赖关系 dp[i] = dp[i+1] + dp[i+2](满足某些条件下)  => i+2<=n
            // (2) 语义分析: 主要关心什么情况下可以 将接下来的两个数字一起解密 =》 1+[0-9]; 2+[0-5]
            if (i + 2 <= n && (chs[i] == '1' || (chs[i] == '2' && chs[i + 1] <= '5' && chs[i + 1] >= '0'))) {
                p1 += dp[i + 2];
            }
            dp[i] = p1;
        }
        return dp[0];
    }


    public static int dpTest(int text) {
        char[] chs = String.valueOf(text).toCharArray();
        int len = chs.length;
        int[] dp = new int[len + 1];
        // TODO: 【错误点】组合数类的问题， end-end的 dp[end] 应该赋值为1，而不是0！！！ 因为组合数，到达最后的位置，代表前面成功组合完成了，此时是1.  当然，从另一个角度来理解，end-end 是一种组合，所以1
        dp[len] = 1;
        dp[len - 1] = 1;
        // TODO: 【错误点】因为依赖关系包含dp[i+1],dp[i+2]，所以我直接计算了dp[len],dp[len-1]，保证依赖关系可以直接写代码，不需要考虑越界问题。
        //  所以，下面写错了，，你要从 倒数第三个开始计算，即 int i = len - 2
        //  【错误】 for (int i = len - 1; i >= 0; i--) {
        for (int i = len - 2; i >= 0; i--) {
            int ans = 0;
            if (chs[i] == '1') {
                ans += dp[i + 1];
                ans += dp[i + 2];
            } else if (chs[i] == '2') {
                if (chs[i + 1] <= '5') {
                    ans += dp[i + 1];
                    ans += dp[i + 2];
                } else {
                    ans += dp[i + 1];
                }
            } else {
                ans += dp[i + 1];
            }
            // TODO: 【错误点】求你了，最后不要忘了 把 dp[i]=ans 赋值了
            dp[i] = ans;
        }
        return dp[0];
    }
}
