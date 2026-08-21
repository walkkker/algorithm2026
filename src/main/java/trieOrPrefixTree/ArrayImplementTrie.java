package trieOrPrefixTree;

// 前缀树
// 数组实现：适用于a-z 26个小写字母，通过ascii码相减 实现范围[0,25]
// 字母在边上，不在节点上。   第一个root节点默认存在。
// 所以从第一个点出发，n个边，必然跳n个点
// TODO: 错误点，insert/erase时 root.pass都需要修改
public class ArrayImplementTrie {

    class Trie {

        class Node {
            int pass;
            int end;
            Node[] nexts;

            public Node() {
                pass = 0;
                end = 0;
                nexts = new Node[26];    // 26 个 小写字母
            }
        }

        private Node root;

        public Trie() {
            root = new Node();
        }

        // 先实现countWordsEqualTo, countWordsStartingWith
        public int countWordsEqualTo(String word) {
            if (word == null) {
                return 0;
            }
            char[] chs = word.toCharArray();
            Node cur = root;
            for (int i = 0; i < chs.length; i++) {
                int index = chs[i] - 'a';
                if (cur.nexts[index] == null) {
                    return 0;
                }
                cur = cur.nexts[index];
            }
            return cur.end;
        }


        public int countWordsStartingWith(String word) {
            if (word == null) {
                return 0;
            }
            char[] chs = word.toCharArray();
            Node cur = root;
            for (int i = 0; i < chs.length; i++) {
                int index = chs[i] - 'a';
                if (cur.nexts[index] == null) {
                    return 0;
                }
                cur = cur.nexts[index];
            }
            return cur.pass;
        }


        // 实现 insert, erase,
        public void insert(String word) {
            if (word == null) {
                return;
            }

            char[] chs = word.toCharArray();
            Node cur = root;
            // 漏了下面这句
            cur.pass++;
            for (int i = 0; i < chs.length; i++) {
                int index = chs[i] - 'a';
                if (cur.nexts[index] == null) {
                    cur.nexts[index] = new Node();
                }
                cur = cur.nexts[index];
                cur.pass++;
            }
            cur.end++;
        }


        // 清除的代码是很重要的
        public void erase(String word) {
            if (word == null) {
                return;
            }
            // 首先 查询if检查下有这个词，才能进行清除
            if (countWordsEqualTo(word) != 0) {
                char[] chs = word.toCharArray();
                Node cur = root;
                cur.pass--;
                for (int i = 0; i < chs.length; i++) {
                    int index = chs[i] - 'a';
                    // 因为检查过了，所以现在是一定有路
                    if (cur.nexts[index].pass == 1) {
                        cur.nexts[index] = null;
                        // TODO: erase方法，置空后必须return。不然出错
                        return;
                    } else {
                        cur = cur.nexts[index];
                        cur.pass--;
                    }
                }
                cur.end--;
            }
        }
    }

    public static void main(String[] args) {
        Trie trie = new ArrayImplementTrie().new Trie();

        System.out.println("=== 测试1: 基本功能 ===");
        trie.insert("apple");
        trie.insert("app");
        trie.insert("application");

        System.out.println("插入后:");
        System.out.println("countWordsEqualTo(\"app\"): " + trie.countWordsEqualTo("app")); // 1
        System.out.println("countWordsStartingWith(\"app\"): " + trie.countWordsStartingWith("app")); // 3

        System.out.println("\n=== 测试2: 删除叶子节点 ===");
        trie.erase("application");
        System.out.println("删除application后:");
        System.out.println("countWordsEqualTo(\"application\"): " + trie.countWordsEqualTo("application")); // 0
        System.out.println("countWordsStartingWith(\"app\"): " + trie.countWordsStartingWith("app")); // 2

        System.out.println("\n=== 测试3: 删除中间节点 ===");
        trie.insert("application");
        trie.insert("apple");
        System.out.println("重新插入后countWordsEqualTo(\"apple\"): " + trie.countWordsEqualTo("apple")); // 2
        trie.erase("apple");
        System.out.println("删除一次apple后countWordsEqualTo(\"apple\"): " + trie.countWordsEqualTo("apple")); // 1

        System.out.println("\n=== 测试4: 删除唯一实例 ===");
        trie.erase("application");
        System.out.println("删除唯一的application后:");
        System.out.println("countWordsEqualTo(\"application\"): " + trie.countWordsEqualTo("application")); // 0

        System.out.println("\n=== 测试5: 空字符串测试 ===");
        trie.insert("");
        System.out.println("插入空字符串后:");
        System.out.println("countWordsEqualTo(\"\"): " + trie.countWordsEqualTo("")); // 1
        System.out.println("countWordsStartingWith(\"\"): " + trie.countWordsStartingWith(""));
        // root.pass = 3 (app, apple, "")

        System.out.println("\n所有测试通过！");
    }


}
