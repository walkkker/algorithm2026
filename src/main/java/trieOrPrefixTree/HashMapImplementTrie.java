package trieOrPrefixTree;

import java.util.HashMap;

/*
    前缀树
    该前缀树用HashMap实现

    本质上没有区别，改动点如下：
    (0) 下标索引不同: int a = chs[i] - 'a'   -->    int index = (int) chs[i];
    (1) 数组访问变成哈希表访问: node.nexts[index].pass -> node.nexts.get(index).pass
    (2) erase方法中，检查 字母边是否存在  && 置空/移除孩子节点 的方式不同： （其实hashmap也可以使用跟数组实现一样的 检查Null或者置为null的方法，但是就按左神的来吧）
        (2.1) node.nexts[index] != null   ===> node.nexts.containsKey(index)
        (2.2) node.nexts[index] = null    ===> node.nexts.remove(index)
 */
public class HashMapImplementTrie {

    class Trie {

        class Node {
            int pass;
            int end;
            HashMap<Integer, Node> nexts;

            public Node() {
                pass = 0;
                end = 0;
                nexts = new HashMap<>();
            }
        }

        private Node root;

        public Trie() {
            root = new Node();
        }

        public int countWordsEqualTo(String word) {
            if (word == null) {
                return 0;
            }
            char[] chs = word.toCharArray();
            Node cur = root;
            for (int i = 0; i < chs.length; i++) {
                int index = (int) chs[i];
                if (cur.nexts.get(index) == null) {
                    return 0;
                }
                cur = cur.nexts.get(index);
            }
            return cur.end;
        }

        public int countWordsStartingWith(String pre) {
            if (pre == null) {
                return 0;
            }
            char[] chs = pre.toCharArray();
            Node cur = root;
            for (int i = 0; i < chs.length; i++) {
                int index = (int) chs[i];
                if (cur.nexts.get(index) == null) {
                    return 0;
                }
                cur = cur.nexts.get(index);
            }
            return cur.pass;
        }

        public void insert(String word) {
            if (word == null) {
                return;
            }

            char[] chs = word.toCharArray();
            Node cur = root;
            cur.pass++;
            for (int i = 0; i < chs.length; i++) {
                int index = (int) chs[i];
                if (!cur.nexts.containsKey(index)) {
                    cur.nexts.put(index, new Node());
                }
                cur = cur.nexts.get(index);
                cur.pass++;
            }
            cur.end++;
        }

        public void erase(String word) {
            if (countWordsEqualTo(word) != 0) {
                char[] chs = word.toCharArray();
                Node cur = root;
                cur.pass--;
                for (int i = 0; i < chs.length; i++) {
                    int index = (int) chs[i];
                    // TODO：清空孩子节点为null的话，必须在父节点的位置做
                    if (cur.nexts.get(index).pass == 1) {
                        cur.nexts.remove(index);
                        return;       // TODO【错误遗漏】 必须加return，不然会开始错跳节点（错误路线）。整棵前缀树会出错。
                    } else {      // >1 时
                        cur = cur.nexts.get(index);
                        cur.pass--;
                    }
                }
                cur.end--;
            }


        }


    }

    public static void main(String[] args) {
        Trie trie = new HashMapImplementTrie().new Trie();

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
