package frequence.双指针.同向读写双指针_原地稳定压缩;

/**
 * LeetCode 1047：删除字符串中的所有相邻重复项。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743210160。用户版本见
 * {@link #myRemoveDuplicates(String)}。
 *
 * <p>不断删除字符串中两个相邻且相同的字符，直到无法继续删除，返回最终字符串。
 *
 * <p><b>模型：同向读写双指针 + 原地数组栈。</b>
 * {@code fast} 读取原字符串，{@code slow} 既是下一个写入位置，也是模拟栈的栈顶。
 * {@code [0, slow)} 始终表示已经处理完的前缀在完成所有相邻重复删除后的结果：
 * <ul>
 *     <li>当前字符与栈顶不同：写入 {@code chars[slow]}，然后 {@code slow++}；</li>
 *     <li>当前字符与栈顶相同：执行 {@code slow--}，相当于弹出栈顶。</li>
 * </ul>
 *
 * <p>与普通原地稳定压缩相比，本题的 {@code slow} 不仅能前进，还能后退。
 * 例如{@code abbaca}的栈内结果依次为{@code a -> ab -> a -> 空 -> c -> ca}，
 * 后退会暴露新的栈顶，从而自然处理连锁删除。
 * 时间复杂度为 O(N)，除 {@code toCharArray()} 生成的结果数组外，额外空间复杂度为 O(1)。
 *
 * <p>进阶题：{@link Q1209_RemoveAllAdjacentDuplicatesInStringII}，需要删除连续 {@code k}
 * 个相同字符，因此还要同步维护每组字符的连续次数。
 */
public class Q1047_RemoveAllAdjacentDuplicatesInString {

    /**
     * 核心点就是：读写双指针 + 【w当栈顶】。
     * @param s
     * @return
     */
    public String myRemoveDuplicates(String s) {
        char[] chs = s.toCharArray();
        int w = 0;
        for (int r = 0; r < chs.length; r++) {
            if (w == 0 || chs[r] != chs[w - 1]) {
                chs[w++] = chs[r];
            } else {
                if (chs[r] == chs[w - 1]) {
                    w--;
                }
            }
        }
        // TODO: 【错误】返回值错了！！！ 你修改的是char[] chs, 怎么返回的是String s?
        // 错误行： return s.substring(0, w);
        return new String(chs, 0, w);
    }


    public String removeDuplicates(String s) {
        char[] chars = s.toCharArray();
        int slow = 0;

        for (int fast = 0; fast < chars.length; fast++) {
            if (slow > 0 && chars[slow - 1] == chars[fast]) {
                slow--;
            } else {
                chars[slow++] = chars[fast];
            }
        }

        return new String(chars, 0, slow);
    }
}
