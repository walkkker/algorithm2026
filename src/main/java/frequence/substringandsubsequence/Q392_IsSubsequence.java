package frequence.substringandsubsequence;

/**
 * 392. 判断子序列
 *
 * <p><b>DONE: 【已独立完成】</b>当前实现已通过固定用例验证。
 * 状态总表参见{@code frequence/待独立完成题目清单.md}。
 *
 * <p>给定字符串s和t，判断s是否为t的子序列。子序列不要求连续，但必须保持原字符相对顺序。
 *
 * <p><b>算法类型：</b>双指针，不需要DP。因为本题只判断一个固定序列能否按顺序匹配，不存在
 * “求最长、求最少、统计所有方案”等需要保存重叠子问题答案的目标。指针sourceIndex只在匹配成功
 * 时前进，targetIndex始终向右扫描，最终检查s是否全部匹配。
 *
 * <p>时间复杂度O(|T|)，额外空间O(1)。相关模型参见同目录《子串与子序列专题.md》。
 */
public class Q392_IsSubsequence {

    /**
     * TODO: 【一开始的错误】你的两个指针职责写反了。  题目是：s是否是t的子序列。  我写成了t是否是s的子序列。
     * @param s
     * @param t
     * @return
     */

    public boolean myIsSubsequence(String s, String t) {
        char[] source = s.toCharArray();
        char[] target = t.toCharArray();
        int p1 = source.length - 1;
        int p2 = target.length - 1;
        while (p1 >= 0 && p2 >= 0) {
            if (source[p1] == target[p2]) {
                p1--;
                p2--;
            } else {
                p2--;
            }
        }
        return p1 == -1;
    }





        public boolean isSubsequence(String s, String t) {
        char[] source = s.toCharArray();
        char[] target = t.toCharArray();
        int sourceIndex = 0;

        for (int targetIndex = 0;
             targetIndex < target.length && sourceIndex < source.length;
             targetIndex++) {
            if (source[sourceIndex] == target[targetIndex]) {
                sourceIndex++;
            }
        }
        return sourceIndex == source.length;
    }
}
