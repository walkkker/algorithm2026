package frequence.Stack;

import java.util.Stack;

/**
 * 20. 有效的括号
 *
 * <p>给定一个只包含字符{@code '('、')'、'{'、'}'、'['、']'}的字符串{@code s}，
 * 判断字符串是否有效。
 *
 * <p>有效字符串必须满足：左括号必须使用相同类型的右括号闭合，左括号必须按照正确顺序闭合，
 * 并且每个右括号都有对应的同类型左括号。
 *
 * <p><b>核心思路：</b>左括号入栈；遇到右括号时，先检查栈是否为空，再检查它是否与栈顶匹配。
 * 扫描结束后栈必须为空，否则仍有未闭合的左括号。时间复杂度{@code O(N)}，空间复杂度
 * {@code O(N)}。
 */
public class Q20_ValidParentheses {

    /**
     * 2026-09-14复盘版本：先判断当前字符属于左括号还是右括号，再分类处理。
     *
     * <p><b>第一类，右括号：</b>右括号只触发匹配和出栈，绝不入栈。只有栈非空并且栈顶
     * 正好等于当前右括号时才能弹出；否则当前前缀已经不可能成为有效括号串，应立即返回
     * {@code false}。
     *
     * <p><b>第二类，左括号：</b>左括号负责入栈。为了简化后续匹配，不保存左括号本身，
     * 而是直接保存它期望遇到的右括号，例如读到{@code '('}就压入{@code ')'}。
     *
     * <p><b>栈不变量：</b>从栈底到栈顶保存的是所有尚未闭合的左括号所期望的右括号；
     * 栈顶就是当前必须最先出现的右括号。这正好表达了括号嵌套的后进先出关系。
     *
     * <p>扫描结束后仍需检查栈是否为空：非空说明存在只有左括号、没有对应右括号的情况。
     * 时间复杂度为{@code O(N)}，额外空间复杂度为{@code O(N)}。
     *
     * <p>TODO: 【API优化，不影响正确性】当前使用{@link Stack}完全正确；现代Java通常推荐
     * 使用{@code Deque<Character>}配合{@code push、peek、pop}作为栈，因为{@code Stack}
     * 是较旧的同步容器。面试中当前写法无需因此修改。
     */
    class SolutionReviewed20260914 {
        public boolean isValid(String s) {
            char[] chs = s.toCharArray();
            Stack<Character> stack = new Stack<>();

            for (char c : chs) {
                if (c == ')' || c == '}' || c == ']') {
                    // 右括号只负责匹配和出栈。栈空或类型不匹配时，当前前缀已经无效。
                    if (!stack.isEmpty() && stack.peek() == c) {
                        stack.pop();
                    } else {
                        return false;
                    }
                } else {
                    // 左括号入栈时直接保存期望的右括号，后续只需一次相等判断。
                    if (c == '(') {
                        stack.push(')');
                    } else if (c == '[') {
                        stack.push(']');
                    } else {
                        // 题目保证输入只包含六种括号，因此剩余左括号只能是'{'.
                        stack.push('}');
                    }
                }
            }
            return stack.isEmpty();
        }
    }

    public boolean isValid(String s) {
        char[] chs = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] == '(' || chs[i] == '{' || chs[i] == '[') {
                stack.push(chs[i]);
            } else {
                if (stack.isEmpty()) {
                    return false;
                } else {
                    if ((chs[i] == ')' && stack.peek() != '(')
                            || (chs[i] == ']' && stack.peek() != '[')
                            || (chs[i] == '}' && stack.peek() != '{')) {
                        return false;
                    } else {
                        stack.pop();
                    }
                }
            }
        }
        return stack.isEmpty();
    }
}
