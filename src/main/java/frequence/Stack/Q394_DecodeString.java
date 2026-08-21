package frequence.Stack;
import java.util.*;

/**
 * 394. 字符串解码
 *
 * TODO：核心是 使用 栈表示嵌套结构，栈存储之前的信息
 * TODO：【难】解题思路见 字符串解码.md。
 *
 * <p>TODO: 【核心不变量】{@code current}始终表示当前括号层已经解码完成的字符串；
 * {@code countStack}和字符串栈保存进入当前层之前的外层现场。
 *
 * <p>TODO: 【执行步骤】
 * <pre>
 * 数字：累计完整重复次数，注意可能是多位数；
 * '['：保存外层count和current，然后重置状态并进入新的一层；
 * 字母：直接追加到当前层current；
 * ']'：当前层结束，弹出外层现场，将当前层重复后合并回外层。
 * </pre>
 *
 * <p>TODO: 【模型理解】遇到{@code '['}相当于进入下一层递归，遇到{@code ']'}相当于当前
 * 递归层返回。两个显式栈承担的就是递归调用栈保存现场的职责。
 *
 * <p>时间复杂度为{@code O(N + L)}，其中N是编码串长度，L是最终解码结果长度；
 * 空间复杂度为{@code O(L + D)}，其中D是最大嵌套深度。
 *
 * <p>给定一个经过编码的字符串{@code s}，返回它解码后的字符串。编码规则为
 * {@code k[encoded_string]}，表示方括号内部的字符串需要重复{@code k}次。
 *
 * <p>{@code k}保证为正整数，输入字符串格式有效，不包含多余空格，且原始数据中不包含数字；
 * 所有数字只表示重复次数。
 */
public class Q394_DecodeString {

    public String decodeString(String s) {
       char[] chs = s.toCharArray();
       // TODO: 【数据结构】countStack保存每一层的重复次数；stack保存进入该层前已经构造的外层字符串。
       // TODO: 【可优化】Stack没有逻辑错误；Java工程代码通常优先使用Deque和ArrayDeque作为栈。
       Stack<Integer> countStack = new Stack<>();
       Stack<StringBuilder> stack = new Stack<>();

       // TODO: current只属于当前括号层；count表示当前尚未遇到'['的完整重复次数。
       StringBuilder current = new StringBuilder();
       int count = 0;

       for (int i = 0; i < chs.length; i++) {
           if (chs[i] >= '0' && chs[i] <= '9') {    // TODO: 【超重点】这里太细了： 如何确认 字符 是否是数字？
               count = count * 10 + (chs[i] - '0'); // TODO: 【超重点-超细】1. 数字可能是多位数字； 2. 如何将 字符转化为数字？
           } else if (chs[i] == '[') {
               // TODO: 【步骤1-保存现场】必须同时保存重复次数和外层字符串，缺少任意一个都无法恢复外层。
               countStack.push(count);
               stack.push(current);
               // TODO: 【步骤2-进入下一层】保存完成后必须重置current和count，避免内层继承外层状态。
               current = new StringBuilder();
               count = 0;
           } else if (chs[i] == ']') {
               // TODO: 【步骤3-当前层返回】此时current是完整的内层结果，outer是进入本层前的外层结果。
               int repeat = countStack.pop();
               StringBuilder outer = stack.pop();

                for (int time = 0; time < repeat; time++) {
                   outer.append(current);
               }
               // TODO: 【不能遗漏】合并完成后必须让current重新指向outer；
               // 后续字符属于恢复后的外层，而不是已经结束的内层。
               current = outer;
           } else {
               // 普通字母属于当前括号层，直接追加。
               current.append(chs[i]);
           }
       }
       return current.toString();
    }
}
