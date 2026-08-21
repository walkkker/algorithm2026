package frequence.双指针.同向读写双指针_原地稳定压缩;

/**
 * LeetCode 443：压缩字符串。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743173217。
 *
 * <p>将连续相同字符分组。每组先写字符；组长度大于1时，再逐位写入十进制长度。
 * 例如12个连续的{@code 'a'}应写成{@code ['a', '1', '2']}，不是把数值12当作一个字符。
 * 返回压缩结果长度，结果保存在原数组前缀中。
 *
 * <p><b>模型：一个输入分组产生一个或多个输出。</b>
 * {@code read} 一次消费完整的一组相同字符，{@code write} 负责生成字符和计数字符：
 * <pre>{@code
 * [0, write)  已生成的压缩结果
 * [0, read)   已消费的原始输入
 * [read, n)   尚未读取的原始输入
 * }</pre>
 *
 * <p>原地向前写是安全的，因为每个已经消费的非空分组至少产生一个字符，且产生的计数字符数
 * 不会超过该组原长度，所以整个过程中始终满足 {@code write <= read}，不会覆盖未读取数据。
 *
 * <p>这道题把普通过滤的“一个输入产生0或1个输出”扩展为“一组输入产生1或多个输出”。
 * 时间复杂度为 O(N)，忽略十进制计数的临时字符数组时额外空间复杂度为 O(1)。
 */
public class Q443_StringCompression {

    /**
     * 用户在LeetCode独立完成的AC版本。
     */
    public int myCompress(char[] chars) {
        int count = 0;
        int w = 0;
        for (int r = 0; r < chars.length; r++) {
            if (w == 0) {
                // TODO: 【曾遗漏】进入新的分组时必须写字符并推进w，不能只初始化count。
                chars[w++] = chars[r];
                count = 1;
            } else {
                if (chars[r] == chars[w - 1]) {
                    count++;
                } else {
                    if (count > 1) {
                        char[] tmp = String.valueOf(count).toCharArray();
                        for (char c : tmp) {
                            chars[w++] = c;
                        }
                    }
                    chars[w++] = chars[r];
                    count = 1;
                }
            }
        }
        if (count > 1) {
            char[] tmp = String.valueOf(count).toCharArray();
            for (char c : tmp) {
                chars[w++] = c;
            }
        }
        return w;
    }

    public int compress(char[] chars) {
        int read = 0;
        int write = 0;

        while (read < chars.length) {
            char current = chars[read];
            int groupStart = read;
            while (read < chars.length && chars[read] == current) {
                read++;
            }

            int groupLength = read - groupStart;
            chars[write++] = current;
            if (groupLength > 1) {
                char[] digits = String.valueOf(groupLength).toCharArray();
                for (char digit : digits) {
                    chars[write++] = digit;
                }
            }
        }

        return write;
    }
}
