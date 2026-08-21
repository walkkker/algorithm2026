package IndexTree;

/**
 * 场景：单点更新 -> 区间查询（本质是O(1)求前缀和）
 *
 * 核心记忆点：
 *  （1）add时， 【自身当前位置 + 不断添加最右侧的1】
 *  （2）sum（l, r）时，以preSum(l)举例：【自身 + 不断减掉最右侧的1】
 */
public class IndexTree1D {


    public static class IndexTree {

        int[] tree;

        public IndexTree(int size) {
            // 0位置 弃而不用
            tree = new int[size + 1];
        }


        // 注意传参是ith => int ith 表示start from 1
        public int sum(int ith) {
            int ans = 0;
            for (int i = ith; i > 0; i -= (i & -i)) {
                ans += tree[i];
            }
            return ans;
        }


        public void add(int ith, int d) {
            for (int i = ith; i < tree.length; i += (i & -i)) {
                tree[i] += d;
            }
        }

    }




    public static class Right {
        private int[] nums;
        private int N;

        public Right(int size) {
            N = size + 1;
            nums = new int[N + 1];
        }

        public int sum(int index) {
            int ret = 0;
            for (int i = 1; i <= index; i++) {
                ret += nums[i];
            }
            return ret;
        }

        public void add(int index, int d) {
            nums[index] += d;
        }

    }

    public static void main(String[] args) {
        int N = 100;
        int V = 100;
        int testTime = 2000000;
//        IndexTree tree = new IndexTree(N);
        MyIndexTree.IndexTree tree = new MyIndexTree.IndexTree(N);
        Right test = new Right(N);
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int index = (int) (Math.random() * N) + 1;  // [1, N]
            if (Math.random() <= 0.5) {
                int add = (int) (Math.random() * V);
                tree.add(index, add);
                test.add(index, add);
            } else {
                if (tree.sum(index) != test.sum(index)) {
                    System.out.println("Oops!");
                }
            }
        }
        System.out.println("test finish");
    }


}
