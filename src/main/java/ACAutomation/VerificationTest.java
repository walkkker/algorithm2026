package ACAutomation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class VerificationTest {

    public static class Node {
        String end;
        Boolean endUse;
        Node[] nexts;
        Node fail;

        public Node() {
            end = null;
            endUse = false;
            nexts = new Node[26];
            fail = null;
        }
    }

    public static class ACAutomation {

        private Node root;

        public ACAutomation() {
            root = new Node();
        }

        public void insert(String word) {
            Node cur = root;
            char[] chs = word.toCharArray();
            for (int i = 0; i < chs.length; i++) {
                int index = chs[i] - 'a';
                if (cur.nexts[index] == null) {
                    cur.nexts[index] = new Node();
                }
                cur = cur.nexts[index];
            }
            cur.end = word;
        }

        public void build() {
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);

            while (!queue.isEmpty()) {
                Node cur = queue.poll();
                for (int i = 0; i < 26; i++) {
                    if (cur.nexts[i] != null) {
                        cur.nexts[i].fail = root;
                        Node cFail = cur.fail;
                        while (cFail != null) {
                            if (cFail.nexts[i] != null) {
                                cur.nexts[i].fail = cFail.nexts[i];
                                break;
                            }
                            cFail = cFail.fail;
                        }
                        queue.add(cur.nexts[i]);  // TODO：【错误点】 一定要在for遍历孩子&&孩子!=null时，要把他们加入到queue里面！！！  这是层级遍历！！！
                    }
                }
            }
        }

        public List<String> containWords(String content) {

            char[] chs = content.toCharArray();
            Node cur = root;
            List<String> ans = new ArrayList<>();
            for (int i = 0; i < chs.length; i++) {
                int index = chs[i] - 'a';
                while (cur != null && cur.nexts[index] == null) {
//                    cur = cur.nexts[index];  // 【错误！！！】 你在干嘛？
                    cur = cur.fail;
                }
                // 这一步代表跳跃到的下一个节点。  匹配不上就是root，匹配上了就是cur.nexts[index]
                cur = cur == null ? root : cur.nexts[index];
                Node follow = cur;
                while (follow != null) {
                    if (follow.endUse) {
                        break;
                    }
                    // code
                    if (follow.end != null) {
                        ans.add(follow.end);
                        follow.endUse = true;
                    }
                    // code
                    follow = follow.fail;
                }
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        ACAutomation ac = new ACAutomation();
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
