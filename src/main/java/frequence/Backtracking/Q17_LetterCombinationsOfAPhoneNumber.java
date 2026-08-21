package frequence.Backtracking;

import java.util.*;

/**
 * 17. 电话号码的字母组合
 *
 * <p>给定一个仅包含数字{@code 2-9}的字符串{@code digits}，按照电话按键上数字到字母的映射，
 * 返回该字符串能够表示的所有字母组合。答案可以按任意顺序返回。
 *
 * <p>TODO: 【Codex补充-边界条件】LeetCode 17允许{@code digits.length() == 0}。
 * 空字符串没有任何字母组合，必须返回空列表{@code []}。
 *
 * <p>如果不在递归前单独处理空字符串，会立即满足{@code index == chs.length}，随后执行：
 * <pre>
 * ans.add(new String(new char[0]));
 * </pre>
 * {@code new String(new char[0])}得到空字符串，所以错误结果是一个只包含空字符串的列表
 * {@code [""]}，而不是题目要求的空列表{@code []}。
 */
public class Q17_LetterCombinationsOfAPhoneNumber {

    /**
     你的回溯部分不需要执行“恢复现场”：
     path[index] = option;
     因为每次选择都会覆盖固定下标index，不存在add/remove型容器的残留问题。Arrays.asList('a', 'b', 'c')和增强for中的自动装箱、拆箱也都是合法语法。
     */
    class Solution {

        HashMap<Character, List<Character>> map = new HashMap<>();
        // 【错误】 编译错误：普通语句不能直接放在类体中
        // 错误行：map.put('2', Arrays.asList('a', 'b', 'c'));

        public List<String> letterCombinations(String digits) {
            List<String> ans = new ArrayList<>();

            // TODO: 【Codex补充-边界条件】空字符串必须在进入递归前直接返回空列表。
            // 如果省略该判断，长度为0的tmp会立即命中递归base case，
            // new String(tmp)会生成""并加入ans，最终错误返回[""]。
            if (digits.length() == 0) {
                return ans;
            }

            map.put('2', Arrays.asList('a', 'b', 'c'));
            map.put('3', Arrays.asList('d', 'e', 'f'));
            map.put('4', Arrays.asList('g', 'h', 'i'));
            map.put('5', Arrays.asList('j', 'k', 'l'));
            map.put('6', Arrays.asList('m', 'n', 'o'));
            map.put('7', Arrays.asList('p', 'q', 'r', 's'));
            map.put('8', Arrays.asList('t', 'u', 'v'));
            map.put('9', Arrays.asList('w', 'x', 'y', 'z'));
            char[] chs = digits.toCharArray();
            char[] tmp = new char[chs.length];
            process(chs, 0, tmp, ans);
            return ans;
        }


        public void process(char[] chs, int index, char[] tmp, List<String> ans) {
            if (index == chs.length) {
                ans.add(new String(tmp));
                return;
            }
            char cur = chs[index];
            List<Character> options = map.get(cur);
            for (char option : options) {
                tmp[index] = option;
                process(chs, index + 1, tmp, ans);
            }
        }
    }

    /**
     * AI给的推荐实现
     */
    class SolutionWithAI {

        private final char[][] LETTERS = {
                {},                     // 0
                {},                     // 1
                {'a', 'b', 'c'},        // 2
                {'d', 'e', 'f'},        // 3
                {'g', 'h', 'i'},        // 4
                {'j', 'k', 'l'},        // 5
                {'m', 'n', 'o'},        // 6
                {'p', 'q', 'r', 's'},   // 7
                {'t', 'u', 'v'},        // 8
                {'w', 'x', 'y', 'z'}    // 9
        };

        public List<String> letterCombinations(String digits) {
            List<String> ans = new ArrayList<>();

            // TODO: 【Codex补充-边界条件】不进入递归，避免把长度为0的path转换成空字符串并加入结果。
            if (digits.length() == 0) {
                return ans;
            }

            char[] chs = digits.toCharArray();
            char[] path = new char[chs.length];

            process(chs, 0, path, ans);
            return ans;
        }

        private void process(
                char[] chs,
                int index,
                char[] path,
                List<String> ans) {

            if (index == chs.length) {
                ans.add(new String(path));
                return;
            }

            char[] options = LETTERS[chs[index] - '0'];

            for (char option : options) {
                path[index] = option;
                process(chs, index + 1, path, ans);
            }
        }
    }
}
