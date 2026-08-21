package frequence.Graph;

/**
 * 208. 实现 Trie（前缀树）
 *
 * TODO: 节点Node不存值。 字符在边上。
 *
 * <p>实现一个Trie，用于高效存储和检索字符串集合中的键。Trie需要支持插入字符串、判断
 * 字符串是否完整存在，以及判断是否存在以指定字符串为前缀的已插入单词。
 *
 * <p><b>核心结构：</b>Trie是一棵多叉树。节点本身不需要保存当前字符，字符由父节点
 * {@code nexts}数组的下标表示：
 * <pre>
 * nexts[0] 代表经过字符'a'到达的下一节点
 * nexts[1] 代表经过字符'b'到达的下一节点
 * ...
 * nexts[25]代表经过字符'z'到达的下一节点
 * </pre>
 * 因此{@code nexts}必须是{@code Node[]}，数组元素保存下一层节点引用，不能使用
 * {@code char[]}。
 *
 * <p><b>search与startsWith的区别：</b>二者都需要完整走过传入字符串对应的路径。
 * {@code startsWith}只要求路径存在；{@code search}还要求最后到达的节点满足
 * {@code isEnd == true}，表示这里确实有一个完整单词结束。
 *
 * <p><b>字符集限制：</b>{@code Node[26]}只适用于题目规定的小写英文字母。若字符集范围
 * 不固定，应改用{@code HashMap<Character, Node>}保存稀疏分支。
 *
 * <p>设字符串长度为{@code L}，{@code insert、search、startsWith}的时间复杂度均为
 * {@code O(L)}；一次插入最坏新增{@code O(L)}个节点。
 */
public class Q208_ImplementTriePrefixTree {

    public static class Trie {

        // TODO: 【可优化-封装性】root只供Trie内部使用，并且初始化后不更换引用，
        // 可以声明为private final Node root。
        Node root;

        public Trie() {
            root = new Node();
        }

        public void insert(String word) {
            char[] chs = word.toCharArray();
            Node cur = root;
            for (int i = 0; i < chs.length; i++) {
                int index = chs[i] - 'a';
                if (cur.nexts[index] == null) {
                    cur.nexts[index] = new Node();
                }
                cur = cur.nexts[index];
            }
            cur.isEnd = true;
            // TODO: 【可优化-冗余字段】当前三个API只需要isEnd判断完整单词是否存在，
            // 不需要保存完整word。只有自动补全、恢复单词等功能需要节点保存完整字符串。
            cur.end = word;
        }

        // TODO: 【可优化-减少重复】search与startsWith的路径查找代码相同，
        // 可以抽取private Node findNode(String str)，两者只在最终判断条件上不同。
        public boolean search(String word) {
            char[] chs = word.toCharArray();
            Node cur = root;
            for (int i = 0; i < chs.length; i++) {
                int index = chs[i] - 'a';
                if (cur.nexts[index] == null) {
                    return false;
                }
                cur = cur.nexts[index];
            }
            return cur.isEnd;
        }

        public boolean startsWith(String prefix) {
            char[] chs = prefix.toCharArray();
            Node cur = root;
            for (int i = 0; i < chs.length; i++) {
                int index = chs[i] - 'a';
                if (cur.nexts[index] == null) {
                    return false;
                }
                // TODO: 【历史错误-遗漏】判断边存在后，必须沿边移动到下一节点；
                // 否则cur始终停留在原节点，后续字符会在错误层级继续查找。
                cur = cur.nexts[index];
            }
            return true;
        }
    }


    // TODO: 【可优化-封装性】Node不依赖任何外部类实例，当前static声明是正确的；
    // 它只服务于Trie内部实现，还可以进一步声明为private static class Node。
    public static class Node {
        // TODO: 【历史错误】错误写法：char[] nexts。Trie需要保存下一节点的引用，
        // 字符本身已经由数组下标表示，因此正确类型是Node[]。
        Node[] nexts;
        boolean isEnd;
        // TODO: 【可优化】本题不需要恢复完整单词，end字段以及insert中的cur.end赋值可以删除。
        String end;

        public Node() {
            nexts = new Node[26];
            // TODO: 【可精简】boolean字段默认是false、引用字段默认是null，
            // 下面两个显式初始化没有错误，但可以省略。
            isEnd = false;
            end = null;
        }
    }


    /**
     * 推荐实现：保留本题必需状态，并抽取公共路径查找逻辑。
     *
     * <p>与上面版本相比：
     * <ul>
     *     <li>{@code root}使用{@code private final}限定生命周期和访问范围；</li>
     *     <li>节点只保存孩子引用和单词结束标记，不保存冗余的完整字符串；</li>
     *     <li>{@code search}与{@code startsWith}复用{@code findNode}；</li>
     *     <li>使用静态内部节点，避免节点隐式持有外部Trie对象引用。</li>
     * </ul>
     */
    public static class TrieRecommended {

        private final TrieNode root;

        public TrieRecommended() {
            root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode cur = root;
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (cur.nexts[index] == null) {
                    cur.nexts[index] = new TrieNode();
                }
                cur = cur.nexts[index];
            }
            cur.isEnd = true;
        }

        public boolean search(String word) {
            TrieNode node = findNode(word);
            return node != null && node.isEnd;
        }

        public boolean startsWith(String prefix) {
            return findNode(prefix) != null;
        }

        /**
         * 沿字符串对应的边逐层向下查找。
         *
         * @return 路径完整存在时返回最后一个节点，否则返回{@code null}
         */
        private TrieNode findNode(String str) {
            TrieNode cur = root;
            for (int i = 0; i < str.length(); i++) {
                int index = str.charAt(i) - 'a';
                if (cur.nexts[index] == null) {
                    return null;
                }
                cur = cur.nexts[index];
            }
            return cur;
        }

        private static class TrieNode {
            private final TrieNode[] nexts = new TrieNode[26];
            private boolean isEnd;
        }
    }
}
