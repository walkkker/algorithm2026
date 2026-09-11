package frequence.Backtracking;

import java.util.*;

/**
 * 22. 括号生成
 *
 * <p>给定一个整数{@code n}，返回所有由{@code n}对括号组成并且格式有效的字符串组合。
 */
public class Q22_GenerateParentheses {

    /**
     * 2026-09-12 复盘版本：使用左右括号使用数量描述递归状态。
     */
    class SolutionReviewed20260912 {

        /**
         * 本题枚举的是：（l和r初始化为0）
         * 1. 满足l < n时添加左括号。
         * 2. 满足l > r时添加右括号。
         *
         * base case设置为r == n。
         *
         * <p>TODO: 【AI补充-状态定义】l和r分别表示当前路径中已经使用的左括号和右括号数量，
         * 递归过程始终维持{@code 0 <= r <= l <= n}。
         *
         * <p>TODO: 【AI补充-base case依据】在上述不变量成立的前提下，{@code r == n}
         * 必然推出{@code l == n}，所以当前路径恰好包含n对括号，可以收集答案。
         */
        public List<String> generateParenthesis(int n) {
            int l = 0;
            int r = 0;
            List<Character> path = new ArrayList<>();
            List<String> ans = new ArrayList<>();
            process(l, r, n, path, ans);
            return ans;
        }

        /**
         * 递归定义：
         * 当前path已经包含l个左括号和r个右括号；本方法负责枚举剩余位置的所有合法括号选择，
         * 并将最终形成的完整有效括号字符串加入ans。
         *
         * <p>后置条件：返回时path必须恢复成进入本方法时的状态。
         */
        private void process(
                int l,
                int r,
                int n,
                List<Character> path,
                List<String> ans) {

            if (r == n) {
                StringBuilder sb = new StringBuilder();
                for (char c : path) {
                    sb.append(c);
                }
                ans.add(sb.toString());
                return;
            }

            // 什么情况下可以加l？
            if (l < n) {
                path.add('(');
                process(l + 1, r, n, path, ans);
                path.remove(path.size() - 1);
            }

            // 什么情况下可以加r？
            if (l > r) {
                path.add(')');
                process(l, r + 1, n, path, ans);
                path.remove(path.size() - 1);
            }

            // TODO: 【AI补充-重要】这里必须是两个独立的if，不能写成if...else if。
            // 当l<n且l>r时，添加左括号和右括号都是合法选择，两个分支都必须搜索。
        }
    }

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
