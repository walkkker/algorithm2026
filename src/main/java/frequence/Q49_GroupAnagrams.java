package frequence;

import java.util.*;

/**
 * 字母异位词分组：将具有相同字符频次的字符串放入同一组。
 *
 * <p>例如输入：
 * <pre>{@code
 * ["eat", "tea", "tan", "ate", "nat", "bat"]
 * }</pre>
 *
 * <p>当前实现先统计每个字符串中 26 个小写字母的出现次数，再按照字母顺序
 * 重新生成字符串作为 HashMap 的 key：
 * <pre>{@code
 * "eat"、"tea"、"ate" -> a:1, e:1, t:1 -> key = "aet"
 * "tan"、"nat"        -> a:1, n:1, t:1 -> key = "ant"
 * "bat"               -> a:1, b:1, t:1 -> key = "abt"
 * }</pre>
 * 因此，字母组成相同但排列顺序不同的字符串会得到相同 key，并进入同一个 List。
 * 最终得到的分组等价于：
 * <pre>{@code
 * [["eat", "tea", "ate"], ["tan", "nat"], ["bat"]]
 * }</pre>
 * HashMap 不保证遍历顺序，因此各分组在结果中的先后顺序不固定。
 *
 * <p>另一种写法是不重新生成 "aet"，而是直接把 26 个频次编码成 key。
 * 编码时必须添加分隔符，例如 {@code #1#0#0...}。如果直接拼接数字，
 * {@code [1, 11]} 和 {@code [11, 1]} 都会得到 {@code "111"}，产生错误分组。
 * 频次编码可以减少长字符串的 key 长度，但不会改变算法复杂度。
 *
 * <p>复杂度：设所有字符串的字符总数为 L，单个字符串的最大长度为 M。
 * 当前计数方案的时间复杂度为 O(L)，每个字符只需统计和生成一次；
 * 如果对每个字符串使用 Arrays.sort，则时间复杂度为 O(L log M)。
 * 因为任何正确算法都必须读取每个字符，所以 O(L) 已经达到渐进意义上的最优复杂度。
 * 当前实现使用长度为 26 的计数数组，因此仅适用于小写英文字母 {@code 'a' 到 'z'}。
 */
public class Q49_GroupAnagrams {

    public List<List<String>> groupAnagrams(String[] strs) {
        HashMap<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            String sorted = getSortedString(s);
            if (!map.containsKey(sorted)) {
                map.put(sorted, new ArrayList<>());
            }
            map.get(sorted).add(s);
        }
        // TODO：【优化】可以合并成下面依据， map.values() 返回的是 Collection<V>
        //   List<List<String>> ans = new ArrayList<>(map.values());
        List<List<String>> ans = new ArrayList<>();
        for (List<String> list : map.values()) {
            ans.add(list);
        }
        return ans;
    }

    public static String getSortedString(String s) {
        char[] chs = s.toCharArray();
        int[] count = new int[26];
        for (int i = 0; i < chs.length; i++) {
            count[chs[i] - 'a']++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count.length; i++) {
            char c = (char) ('a' + i);
            while (count[i]-- > 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
