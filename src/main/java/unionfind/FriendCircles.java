package unionfind;

// 本题为leetcode原题
// 测试链接：https://leetcode.cn/problems/number-of-provinces/description/
// 可以直接通过
public class FriendCircles {
    // 20260625最新版
    class Solution {
        public int findCircleNum(int[][] isConnected) {
            int n = isConnected.length;
            UnionFind uf = new UnionFind(n);
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (isConnected[i][j] == 1) {
                        uf.union(i, j);
                    }
                }
            }
            return uf.sets();
        }

        // TODO: 下面这个去掉static，就当做一个inner class。 整个class Solution也能pass leetcode
        public class UnionFind {
            int[] father;
            int[] size;
            int[] stack;
            int sets;

            public UnionFind(int n) {
                // TODO: 【易错点】初始化时，所有 属性都要初始化，尤其是所有数组要初始化，并且赋值（father[i] = i,  size[i] = 1）
                father = new int[n];
                size = new int[n];
                stack = new int[n];
                sets = n;
                for (int i = 0; i < n; i++) {
                    father[i] = i;
                    size[i] = 1;
                }
            }


            public int find(int a) {
                int i = 0;
                while (a != father[a]) {
                    stack[i++] = a;
                    a = father[a];
                }
                while (i > 0) {    // TODO: 出栈(stack[--i], i对应着stackSize。   出栈的每个元素父亲置为a)
                    father[stack[--i]] = a;
                }
                return a;
            }

            public void union(int a, int b) {
                int fa = find(a);
                int fb = find(b);
                if (fa != fb) {
                    if (size[fa] > size[fb]) {
                        father[fb] = fa;
                        size[fa] += size[fb];
                    } else {
                        father[fa] = fb;
                        size[fb] += size[fa];
                    }
                    sets--;   // TODO: 【易错点】千万别忘了 sets--
                }
            }

            public int sets() {
                return sets;
            }


        }
    }

    public static int findCircleNum(int[][] M) {
        int n = M.length;
        UnionFind uf = new UnionFind(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (M[i][j] == 1) {
                    uf.union(i, j);
                }
            }
        }
        return uf.sets();
    }

    public static class UnionFind {

        int[] father;
        int[] size;
        int[] stack;
        int sets;

        public UnionFind(int N) {
            father = new int[N];
            size = new int[N];
            stack = new int[N];
            sets = N;
            // TODO: 【错误-漏掉初始化！！！】光顾着按照输入的元素数量初始化数组了！！！ 漏了最关键的初始化
            //   size=1, father指向自己！！！！ 绝对不能漏掉！！！
            //   不要一写数组就混乱了，步骤跟hashmap是一样的！！！
            for (int i = 0; i < N; i++) {
                father[i] = i;
                size[i] = 1;
            }
        }


        public int find(int i) {
            int si = 0;
            while (i != father[i]) {
                stack[si++] = i;
                i = father[i];
            }
            for (si--; si >= 0; si--) {
                father[stack[si]] = i;
            }
            return i;
        }

        public void union(int x, int y) {
            int xf = find(x);
            int yf = find(y);
            if (xf != yf) {
                if (size[xf] >= size[yf]) {
                    father[yf] = xf;
                    size[xf] += size[yf];
                } else {   // size[xf] < size[yf]
                    father[xf] = yf;
                    size[yf] += size[xf];
                }
                sets--;
            }
        }

        public int sets() {
            return sets;
        }
    }


}
