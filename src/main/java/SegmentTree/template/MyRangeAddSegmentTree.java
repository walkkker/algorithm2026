package SegmentTree.template;

public class MyRangeAddSegmentTree {

    int[] arr;
    int[] sum;
    int[] lazy;
    int N;

    public MyRangeAddSegmentTree(int[] origin) {
        N = origin.length;

        // TODO: 根据ai advice，这里可以稍微优化一下。核心是maxN=origin.length + 1; 然后新建数组的length都是围绕maxN展开的
        int maxN = N + 1;

//        arr = new int[N + 1];
//        sum = new int[4 * N];
//        lazy = new int[4 * N];
        arr = new int[maxN];
        sum = new int[maxN * 4];
        lazy = new int[maxN * 4];

        for (int i = 1; i <= N; i++) {
            arr[i] = origin[i - 1];
        }
        // TODO: 【注意点】codex对数器 是不单独调用Build方法的，所以需要把build放在构造器里面。 这一步不是强制的

        // TODO：【错误点】我这里N是原数组长度，那么新数组范围[0, N],length=N+1
        //  所以该句错误： build(1, 1, N + 1);
        build(1, 1, N);
    }

    private void pushUp(int i) {
        sum[i] = sum[2 * i] + sum[2 * i + 1];
    }

    private void pushDown(int i, int l, int r) {
        int M = (l + r) / 2;
        int leftLen = M - l + 1;
        int rightLen = r - M; // r - M + 1;   // TODO: 【错误点】计算错误，右侧是 (M, r]
        lazy[i * 2] += lazy[i];   // TODO: 【错误点】特别注意，Lazy-add 是要累加的 += 而不是 =
        lazy[i * 2 + 1] += lazy[i];
        sum[i * 2] += lazy[i] * leftLen;
        sum[i * 2 + 1] += lazy[i] * rightLen;
        lazy[i] = 0;
    }


    public void build(int i, int l, int r) {
        if (l == r) {
            sum[i] = arr[l];
            return;
        }
        int M = (l + r) / 2;
        build(2 * i, l, M);
        build(2 * i + 1, M + 1, r);
        pushUp(i);
    }

    public void add(int i, int l, int r, int L, int R, int diff) {
        if (l >= L && r <= R) {
            lazy[i] += diff;
            sum[i] += diff * (r - l + 1);
            return;
        }
        pushDown(i, l, r);
        int M = (l + r) / 2;
        // TODO: 【错误点-漏掉if条件】这里的假设是来到当前节点，[L, R]与[l,r]是一定有重叠的，此时下列if条件成立 （而且下列if条件是必要条件）
        if (L <= M) {
            add(2 * i, l, M, L, R, diff);
        }
        if (R > M) {
            add(2 * i + 1, M + 1, r, L, R, diff);
        }
        pushUp(i);
    }

    public void add(int l, int r, int diff) {
        // TODO：【错误点-同build】我这里N是原数组长度，那么新数组范围[0, N],length=N+1
        //  错误行： add(1, 1, N + 1, l, r, diff);
        add(1, 1, N, l + 1, r + 1, diff);  // TODO：第二次错误，原始的add方法是1~based。 对外接口是以原数据的rangeAdd考虑的，所以l和r都需要+1
    }

    public int query(int i, int l, int r, int L, int R) {
        if (l >= L && r <= R) {
            return sum[i];
        }
        pushDown(i, l, r);
        int M = (l + r) / 2;
        int ans = 0;
        if (L <= M) {
            ans += query(i * 2, l, M, L, R);
        }
        if (R > M) {
            ans += query(i * 2 + 1, M + 1, r, L, R);
        }
        return ans;
    }

    public int query(int l, int r) {
        // TODO：【错误点-同build】我这里N是原数组长度，那么新数组[0, N],length=N+1
        // TODO： 错误行（范围错误）return query(1, 1, N + 1, l ,r);
        return query(1, 1, N, l + 1 ,r + 1);
    }


}
