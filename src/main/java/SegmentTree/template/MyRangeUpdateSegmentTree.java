package SegmentTree.template;

public class MyRangeUpdateSegmentTree {
// TODO: 【重点】RangeUpdate中 int[] update和boolean[] isChanged相生相伴

    int[] arr;
    int[] sum;
    int[] update;
    boolean[] isChanged;
    int N;

    public MyRangeUpdateSegmentTree(int[] origin) {
        N = origin.length;
        int maxN = N + 1;
        arr = new int[maxN];
        sum = new int[maxN * 4];
        update = new int[maxN * 4];
        isChanged = new boolean[maxN * 4];
        for (int i = 1; i <= N; i++) {
            arr[i] = origin[i - 1];
        }
        build(1, 1, N);
    }

    private void pushUp(int i) {
        sum[i] = sum[i * 2] + sum[i * 2 + 1];
    }

    private void pushDown(int i, int l, int r) {
        // TODO: 第一个出错点
        if (isChanged[i]) {
            int M = (l + r) / 2;
            int leftLen = M - l + 1;
            int rightLen = r - M;
            update[i * 2] = update[i];
            update[i * 2 + 1] = update[i];
            // TODO: pushDown出了update别忘了 isChanged也要更新
            isChanged[i * 2] = true;
            isChanged[i * 2 + 1] = true;
            sum[i * 2] = leftLen * update[i];
            sum[i * 2 + 1] = rightLen * update[i];
            isChanged[i] = false;
        }
    }


    private void build(int i, int l, int r) {
        if (l == r) {
            sum[i] = arr[l];
            return;
        }
        int M = (l + r) / 2;
        build(i * 2, l, M);
        build(i * 2 + 1, M + 1, r);
        pushUp(i);
    }

    public void update(int i, int l, int r, int L, int R, int val) {
        if (l >= L && r <= R) {
            update[i] = val;
            isChanged[i] = true;
            sum[i] = val * (r - l + 1);
            return;
        }
        pushDown(i, l, r);
        int M = (l + r) / 2;
        if (L <= M) {
            update(i * 2, l, M, L, R, val);
        }
        if (R > M) {
            update(i * 2 + 1, M + 1, r, L, R, val);
        }
        pushUp(i);
    }

    // TODO: 这里为了匹配对数器，对数器传参是0~based-1。由于线段树内部是1~based处理，所以我们写一个外部接口转化，将外部传参l,r都右移一位
    public void update(int l, int r, int val) {
        update(1, 1, N, l + 1, r + 1, val);
    }

    public int query(int i, int l, int r, int L, int R) {
        if (l >= L && r <= R) {
            return sum[i];
        }
        pushDown(i, l, r);
        int mid  = (l + r) / 2;
        int ans = 0;
        if (L <= mid) {
            ans += query(i * 2, l, mid, L, R);
        }
        if (R > mid) {
            ans += query(i * 2 + 1, mid + 1, r, L, R);
        }
        return ans;
    }

    // TODO: 这里为了匹配对数器，对数器传参是0~based-1。由于线段树内部是1~based处理，所以我们写一个外部接口转化
    public int query(int l, int r) {
//        return query(1, 1, N, l, r);
        return query(1, 1, N, l + 1, r + 1);  // query的范围基于0~based-1，所以我们需要右移一位
    }




}
