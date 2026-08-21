package kmp;

// TODO： 这么简单的题目也错了。！！！
// https://leetcode.cn/problems/rotate-string/description/
public class IsRotation {
    class Solution {
        public boolean rotateString(String s, String goal) {
            // TODO: 【错误-没写第一个检查条件】aa , a。 你要先保证两个字符串是相同长度，然后再KMP
            if (s.length() != goal.length()) {
                return false;
            }

            s = s + s;
            return s.indexOf(goal) != -1;
        }
    }
}
