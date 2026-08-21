package frequence.双指针.同向读写双指针_原地稳定压缩;

/**
 * LeetCode 1209：删除字符串中的所有相邻重复项 II。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743308001。用户版本见
 * {@link #myRemoveDuplicates(String, int)}。
 *
 * <p>每次删除字符串中连续 {@code k} 个相同字符，重复执行直到无法继续删除，返回最终字符串。
 * 删除一组字符后，原本不相邻的两段可能连接并形成新的可删除组，因此必须支持连锁删除。
 *
 * <p><b>模型：Q1047 的原地数组栈 + 连续次数状态。</b>
 * {@code fast} 读取原字符串，{@code slow} 表示模拟栈的下一个写入位置，
 * {@code count[i]} 表示结果栈中以位置 {@code i} 结尾的连续相同字符数量。
 *
 * <p>处理当前字符时：
 * <ol>
 *     <li>将当前字符写到 {@code chars[slow]}；</li>
 *     <li>根据前一个栈内字符计算 {@code count[slow]}；</li>
 *     <li>当连续次数达到 {@code k} 时执行 {@code slow -= k}，一次弹出整组字符。</li>
 * </ol>
 * 后续字符会直接与删除后暴露出的新栈顶比较，因此连锁删除不需要额外处理。
 * Q1047只需要判断当前字符是否等于栈顶；本题仅靠字符无法知道一组是否已经达到{@code k}个，
 * 所以{@code count}是不可缺少的附加状态。
 *
 * <p>时间复杂度为 O(N)，额外空间复杂度为 O(N)，用于保存每个栈位置的连续次数。
 */
public class Q1209_RemoveAllAdjacentDuplicatesInStringII {

    public String myRemoveDuplicates(String s, int k) {
        char[] chs = s.toCharArray();
        int w = 0;
        int[] count = new int[chs.length];
        for (int r = 0; r < chs.length; r++) {
            if (w == 0) {
                chs[w] = chs[r];
                count[w++] = 1;
            } else {
                chs[w] = chs[r];
                count[w] = chs[r] == chs[w - 1] ? count[w - 1] + 1 : 1;
                w++;
            }

            if (count[w - 1] == k) {
                w -= k;
            }

        }
        return new String(chs, 0, w);
    }

    public String removeDuplicates(String s, int k) {
        char[] chars = s.toCharArray();
        int[] count = new int[chars.length];
        int slow = 0;

        for (int fast = 0; fast < chars.length; fast++) {
            chars[slow] = chars[fast];
            if (slow > 0 && chars[slow] == chars[slow - 1]) {
                count[slow] = count[slow - 1] + 1;
            } else {
                count[slow] = 1;
            }
            slow++;

            if (count[slow - 1] == k) {
                slow -= k;
            }
        }

        return new String(chars, 0, slow);
    }
}
