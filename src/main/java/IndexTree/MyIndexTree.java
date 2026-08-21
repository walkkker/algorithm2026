package IndexTree;

public class MyIndexTree {

    public static class IndexTree {
        int[] tree;

        public IndexTree(int size) {
            tree = new int[size + 1];
        }

        public void add(int i, int num) {
//            i = i + 1;
            for (; i < tree.length; i += (i & (-i))) {
                tree[i] += num;
            }
        }

        public int sum (int i) {
//            i = i + 1;
            int ans = 0;
            for (; i > 0; i -= (i & (-i))) {
                ans += tree[i];
            }
            return ans;
        }

    }
}
