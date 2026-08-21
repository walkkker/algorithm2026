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
