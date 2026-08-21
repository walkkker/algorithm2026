package frequence.Backtracking;

import java.util.*;

/**
 * 22. 括号生成
 *
 * <p>给定一个整数{@code n}，返回所有由{@code n}对括号组成并且格式有效的字符串组合。
 */
public class Q22_GenerateParentheses {
    // 格式有效： 左括号>=右括号
    // 又是长度固定，可以使用char[]装载中间量。 不存在add/remove容器残余，因此不需要 恢复现场
    public List<String> generateParenthesis(int n) {
        char[] tmp = new char[2 * n];
        List<String> ans = new ArrayList<>();
        process(0, n, 0, 0, tmp, ans);
        return ans;
    }

    public void process(int index, int n, int left, int right, char[] tmp, List<String> ans) {
        if (index == 2 * n) {
            ans.add(new String(tmp));
            return;
        }
        if (left == right) {
            tmp[index] = '(';
            process(index + 1, n, left + 1, right, tmp ,ans);
        } else if (left > right) {
            if (left == n) {
                tmp[index] = ')';
                process(index + 1, n, left, right + 1, tmp, ans);
            } else {
                tmp[index] = '(';
                process(index + 1, n, left + 1, right, tmp, ans);
                tmp[index] = ')';
                process(index + 1, n, left, right + 1, tmp, ans);
            }
        }
    }
}
