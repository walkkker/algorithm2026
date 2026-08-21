package ACAutomation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * (1) build: 层级遍历+父亲给孩子挂fail指针。
 * 	- 实现细节：使用cfail遍历cur的fail链路，寻找第一个 cfail.nexts[index]!=null的节点。  此时cur.nexts[index].fail = cfail.nexts[index]
 *
 * (2) containWords:
 */
public class Code04_AC2 {

	// 前缀树的节点
	public static class Node {
		// 如果一个node，end为空，不是结尾
		// 如果end不为空，表示这个点是某个字符串的结尾，end的值就是这个字符串
		public String end;
		// 只有在上面的end变量不为空的时候，endUse才有意义
		// 表示，这个字符串之前有没有加入过答案
		public boolean endUse;
		public Node fail;   // 我站在当前节点，下一个字符匹配适配的情况下（没边），我跳到哪里再进行匹配
		public Node[] nexts;

		public Node() {
			endUse = false;
			end = null;
			fail = null;
			nexts = new Node[26];
		}
	}

	public static class ACAutomation {
		private Node root;    // 字符是在边上，节点只是用来记录信息。

		public ACAutomation() {
			root = new Node();
		}

		public void insert(String s) {
			char[] str = s.toCharArray();
			Node cur = root;
			int index = 0;
			for (int i = 0; i < str.length; i++) {
				index = str[i] - 'a';
				if (cur.nexts[index] == null) {
					cur.nexts[index] = new Node();
				}
				cur = cur.nexts[index];
			}
			cur.end = s;
		}

		public void build() {  // 核心逻辑：站在parent-cur的位置，给child cur.nexts[i] 挂fail指针。 因为基于cur.fail可以依次遍历到所有与后缀匹配的前缀字符节点（按匹配长度降序），此时可以把cur.nexts[i] 与 cFail.next[i]进行比较，可以确定cur.nexts[i].fail到底指向哪一个
			Queue<Node> queue = new LinkedList<>();   // 层级遍历
			queue.add(root);
			Node cur = null;      // 当前位于哪个节点
			Node cfail = null;	  // cfail=cur-fail=当前节点进行下一个字符匹配失败时，cfail指向 与当前字符串(截止到cur)最大后缀匹配的 最大前缀的字符串的位置。
			while (!queue.isEmpty()) {   // 层级遍历->每个节点都遍历->每次cur 给 cur.nexts[i]挂fail
				// 某个父亲，cur
				cur = queue.poll();
				for (int i = 0; i < 26; i++) { // 遍历cur的所有孩子
					// cur -> 父亲  i号儿子，必须把i号儿子的fail指针设置好！
					if (cur.nexts[i] != null) { // 如果真的有i号儿子
						cur.nexts[i].fail = root;
						cfail = cur.fail;
						while (cfail != null) {
							if (cfail.nexts[i] != null) {
								cur.nexts[i].fail = cfail.nexts[i];
								break;
							}
							cfail = cfail.fail;
						}
						queue.add(cur.nexts[i]);
					}
				}
			}
		}

		// 大文章：content
		public List<String> containWords(String content) { // containWords没有cfail，就是cur在trie上不断跳跃。
			char[] str = content.toCharArray();
			Node cur = root;
			Node follow = null;
			int index = 0;
			List<String> ans = new ArrayList<>();
			for (int i = 0; i < str.length; i++) {
				index = str[i] - 'a'; // 路
				// 如果当前字符在这条路上没配出来，就随着fail方向走向下条路径。 fail指针 经过的所有节点(包括root)，都需要查看nexts[index]==null?，但凡!=null，都意味着 next节点 与当前大文章指针 匹配上了，cur就要跳转过去。
				while (cur.nexts[index] == null && cur != root) {  // 1) 没路代表没匹配上，所以要cur = cur.fail。 2) cur != root 是因为，他要保证前一个条件不出现 NPE。  因为如果root.nexts[index]==null，此时cur.fail==null。 那么while条件就会NPE。 3) 但是第二个条件 千万不能理解成 到了root意味着要从下一个大文章指针开始匹配了，不是的。 到了root，有可能跳跃下一个节点 有可能停留在root。
					cur = cur.fail;
				}
				// 1) 现在来到的路径，是可以继续匹配的
				// 2) 现在来到的节点，就是前缀树的根节点
				// 这里特别注意： 存在可能性 root.nexts[index] != null，此时cur= root.nexts[index]
				// 所以 上一句和下一句 是必须搭配在一起的一对 java语句。  while退出时，即便是cur==root退出的。 需要判断此时root.nexts[index]是否有节点。 有节点，说明匹配上了；无节点，则cur保留root。等待大文章指针前进，从下一个字符开始重新匹配。
				cur = cur.nexts[index] != null ? cur.nexts[index] : root;
				follow = cur;
				while (follow != root) {
					if (follow.endUse) {
						break;
					}
					// 不同的需求，在这一段之间修改
					if (follow.end != null) {
						ans.add(follow.end);
						follow.endUse = true;
					}
					// 不同的需求，在这一段之间修改
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
