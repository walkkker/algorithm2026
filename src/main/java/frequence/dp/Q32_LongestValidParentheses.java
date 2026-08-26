package frequence.dp;

import java.util.*;
/**
 * 32. 最长有效括号
 *
 * <p>给定只包含左右括号的字符串，返回最长格式正确且连续的括号子串长度。
 *
 * <p><b>DP类型：</b>状态拓扑属于“一维位置结尾型DP”，状态值语义属于“最大值DP”。
 * {@code dp[i]}表示必须以位置i的右括号结尾的最长有效括号子串长度。
 *
 * <p><b>状态定义：</b>{@code dp[i]}表示必须以i位置结尾的最长有效括号子串长度。只有
 * {@code s[i] == ')'}时状态才可能非0。
 *
 * <p><b>两种转移：</b>如果前一个字符是左括号，直接形成一对并连接i-2之前的答案；如果前一个
 * 字符是右括号，先跳过以i-1结尾的有效段，再检查它前面的字符能否与当前右括号匹配，匹配后还要
 * 连接更左侧相邻的有效段。
 *
 * <p>本题属于“一维位置结尾型DP”，详细模型参见同目录《一维位置结尾型DP.md》；总体分类参见
 * 《动态规划题型共性总结.md》。易错点是计算匹配位置后必须先检查下标边界，并且最终答案是
 * 所有{@code dp[i]}的最大值，不一定是{@code dp[n-1]}。
 *
 * <p><b>一维DP复盘：</b>栈和DP版本都能达到O(N)，但DP版本更能体现状态设计：{@code dp[i]}
 * 强制表示必须以右括号i结尾的有效长度，因此左括号位置状态自然为0。当前右括号先利用
 * {@code dp[i-1]}跳过一整段有效结构，再检查结构左边界能否匹配；匹配成功后还要连接更左侧紧邻的
 * {@code dp[pos-1]}。这种前驱不是固定相邻位置，而是“由已有长度计算出来的结构跳跃”。完整归纳
 * 参见同目录《一维DP核心总结.md》。
 *
 * <p>本题作为连续子串结尾型DP的专题索引参见
 * {@code frequence/substringandsubsequence/子串与子序列区别.md}。
 */
public class Q32_LongestValidParentheses {

    /**
     两个做法：
     1. 栈 -> 直观 (stack 里面存下标！！！) =》 其实也是求每一个i作为end的有效括号长度。
     2. DP

     栈的核心思路是：如果chs[i]==')',且与stack.peek()匹配成功。则stack.peek()下面的元素tmp，就是前面（暂时）不能组成有效括号的部分。
     此时用 i - tmp 得到的就是，以i为end的有效括号长度。

     【注意】tmp可能不存在，因为此时可能stack.size()==1,pop后，栈为空。 那么此时i对应的长度就是 i+1！！！      AI是里面一开始塞了一个-1，但是我觉得单独判断栈是否为空思路也很清晰。
     */
    class SolutionWithStack {
        public int longestValidParentheses(String s) {
            // TODO: 【错误】stack里面是要存下标的
            // Stack<Character> stack = new Stack<>();
            Stack<Integer> stack = new Stack<>();
            char[] chs = s.toCharArray();
            int ans = 0;
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] == '(') {
                    stack.push(i);
                } else {
                    // chs[i] == ')'
                    if (stack.isEmpty() || chs[stack.peek()] == ')') {
                        stack.push(i);
                    } else {
                        stack.pop();
                        int p = stack.isEmpty() ? i + 1 : i - stack.peek();
                        ans = Math.max(ans, p);
                    }
                }
            }
            return ans;
        }
    }


    /**
     一维end类型DP

     必须以i结尾时，最长有效括号的长度。

     状态转移：
     (pos, i) 长度为dp[i-1]  =>    pos = i - 1 - dp[i-1]

     if ( pos >= 0 && chs[pos]=='(' ) {dp[i] = dp[i-1] + 2 + (? - 1 >= 0 ? dp[? - 1] : 0)}}
     else 其余情况下 dp[i]都为0

     */
    class SolutionWithDP {
        public int longestValidParentheses(String s) {
            char[] chs = s.toCharArray();
            int[] dp = new int[chs.length];
            int ans = 0;
            for (int i = 1; i < chs.length; i++) {
                // 只有一种情况赋值，必须满足 chs[i]==')' && pos>=0 && chs[pos]=='('
                if (chs[i] == ')') {
                    // dp[i-1]给出紧邻左侧完整有效段的长度，直接跨过它定位待匹配左括号。
                    int pos = i - dp[i - 1] - 1;
                    if (pos >= 0 && chs[pos] == '(') {
                        dp[i] = 2 + dp[i - 1] + (pos - 1 >= 0 ? dp[pos - 1] : 0);
                    }  // TODO: 别忘了加上dp[pos-1]，这一部分有效括号是 连着当前部分的，所以也需要加上。
                }
                ans = Math.max(ans, dp[i]);
            }
            return ans;
        }
    }






    public static class RecommendedSolution {

        public int longestValidParentheses(String s) {
            char[] chs = s.toCharArray();
            int[] dp = new int[chs.length];
            int ans = 0;

            for (int i = 1; i < chs.length; i++) {
                if (chs[i] != ')') {
                    continue;
                }

                if (chs[i - 1] == '(') {
                    // ...()：当前括号贡献2，再连接i-2位置结尾的有效段。
                    dp[i] = 2 + (i >= 2 ? dp[i - 2] : 0);
                } else {
                    // ...))：跳过以i-1结尾的有效段，寻找可能匹配当前')'的左括号。
                    int matchingLeft = i - dp[i - 1] - 1;
                    if (matchingLeft >= 0 && chs[matchingLeft] == '(') {
                        dp[i] = dp[i - 1] + 2;
                        // 匹配成功后，继续连接matchingLeft左侧紧邻的有效段。
                        if (matchingLeft >= 1) {
                            dp[i] += dp[matchingLeft - 1];
                        }
                    }
                }
                ans = Math.max(ans, dp[i]);
            }
            return ans;
        }
    }
}
