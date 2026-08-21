package ACAutomation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * AC自动机结合了Trie树和KMP算法的思想，能够在O(n)时间复杂度内完成多模式串匹配，是解决敏感词过滤、病毒特征匹配等实际问题的理论最优选择
 * <p>
 * 掌握核心原理：理解AC自动机=Trie树+KMP失败指针的构建过程
 * <p>
 * 大文章指针不回退
 * <p>
 * TODO: 【错误点】错误点主要来自于 for(int i; i < chs.length; i++) {int index = chs[i]-'a'}后，轮到计算 cur.nexts[]时，这里一定要填 index（符合[0-25]） 而不是i！！！ 务必要思路清晰！！！
 */
public class ACAutomation {

    // 该题前提： 敏感词集合 与 大文章都是小写。
    public static class Node {
        String end;
        boolean endUsed;
        Node fail;   // 相当于next指针
        Node[] nexts;

        public Node() {
            end = null;
            endUsed = false;
            fail = null;
            nexts = new Node[26];  // a-z 26个lowercase字母
        }
    }

    public static class AC {

        Node root;

        public AC() {
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
            cur.end = word;
        }

        public void build() {    // 层级遍历
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                Node cur = queue.poll();
                for (int i = 0; i < 26; i++) {
                    if (cur.nexts[i] != null) {
                        // 顺着fail指针，找到第一个cfail.nexts[index]!=null,此时 cur.nexts[index].fail = cfail.nexts[index]
                        Node cfail = cur.fail;
                        while (cfail != null && cfail.nexts[i] == null) {
                            cfail = cfail.fail;
                        }
                        cur.nexts[i].fail = cfail == null ? root : cfail.nexts[i];
                        // TODO: cur.nexts[index].fail赋值后， 别忘了层级遍历要把 子节点加入queue
                        queue.add(cur.nexts[i]);
                    }
                }
            }
        }

        public List<String> containWords(String content) {
            List<String> ans = new ArrayList<>();
            char[] chs = content.toCharArray();
            Node cur = root;   // trie指针，在前缀树上来回跳

            for (int i = 0; i < chs.length; i++) {
                int index = chs[i] - 'a';
                // fail指针允许遍历trie的所有节点，以此 寻找fail.nexts[index]!=null。 所以如果一直找不到(到了root也没有)，那么cur = root.fail => cur=null。 所以，此时null状态表示，找不到trie节点匹配这个字符。
                while (cur != null && cur.nexts[index] == null) {
                    cur = cur.fail;
                }
                cur = cur == null ? root : cur.nexts[index];
                // 匹配成功后，沿着fail指针搂， 因为沿着fail指针走的都是 完整的前缀字符串（可能会含有完整字符串，即end!=null）
                Node follow = cur;
                while (follow != root) {
                    if (follow.endUsed) {
                        break;
                    }
                    // 下面为自定义部分START - 不同的需求在这里修改
                    if (follow.end != null) {
                        ans.add(follow.end);
                        follow.endUsed = true;
                    }
                    // 上面为自定义部分END - 不同的需求在这里修改
                    follow = follow.fail;
                }
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        AC ac = new AC();
        ac.insert("dhe");
        ac.insert("he");
        ac.insert("abcdheks");
        // 设置fail指针
        ac.build();

        List<String> contains = ac.containWords("abcdhekskdjfafhasldkflskdjhwqaeruv");
        for (String word : contains) {
            System.out.println(word);
        }
    }


}


