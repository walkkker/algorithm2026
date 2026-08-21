package unionfind;

import java.io.*;

/**
 * 如果调用isSameSet和union的总次数逼近或超过O(N)，请做到单次调用isSameSet或union方法的平均时间复杂度为O(1)
 *
 * 其实这份代码可以不看，没有实战性，直接看 朋友圈 和 岛屿数量就行了
 **/

// 数组实现
// 主要方法：
//      find(x): 找root + 路径压缩
//      isSameSet(x, y): 是否属于集合
//      union(x, y): x所属集合 与 y所属集合 合并

// 这个文件课上没有讲
// 原理和课上讲的完全一样
// 最大的区别就是这个文件实现的并查集是用数组结构，而不是map结构
// 请务必理解这个文件的实现，而且还提供了测试链接
// 提交如下的code，并把"Code06_UnionFind"这个类名改成"Main"
// 在测试链接里可以直接通过
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 测试链接 : https://www.nowcoder.com/questionTerminal/e7ed657974934a30b2010046536a5372
public class UnionFind {

    public static int MAXN = 1000001;

    public static int[] father = new int[MAXN];

    public static int[] size = new int[MAXN];

    public static int[] stack = new int[MAXN];
    public static int sets;


    public static void init(int N) {
        // TODO: 【錯誤-漏掉】这个是必要的，一定要初始化，跟HashMap实现是一样的。 size=1, father指向自己！！！
        for (int i = 0; i <= N; i++) {
            father[i] = i;
            size[i] = 1;
        }
        sets = N;
    }

    public static int find(int i) {
        int stacki = 0;
        while (i != father[i]) {
            stack[stacki++] = i;
            i = father[i];
        }
        // 退出时 i == father[i], 也就是 该集合的根

        // 弹栈，路径压缩
        for (stacki--; stacki >= 0; stacki--) {
            father[stack[stacki]] = i;
        }
        return i;
    }

    public static boolean isSameSet(int x, int y) {
        int xFather = find(x);
        int yFather = find(y);
        return xFather == yFather;
    }

    public static void union(int x, int y) {
        int xF = find(x);
        int yF = find(y);
        if (xF != yF) {
            if (size[xF] > size[yF]) {
                father[yF] = xF;
                size[xF] += size[yF];
            } else {
                father[xF] = yF;
                size[yF] += size[xF];
            }
            sets--;
        }
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            init(n);
            in.nextToken();
            int m = (int) in.nval;
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int op = (int) in.nval;
                in.nextToken();
                int x = (int) in.nval;
                in.nextToken();
                int y = (int) in.nval;
                if (op == 1) {
                    out.println(isSameSet(x, y) ? "Yes" : "No");
                    out.flush();
                } else {
                    union(x, y);
                }
            }
        }
    }

}
