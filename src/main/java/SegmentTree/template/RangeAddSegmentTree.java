package SegmentTree.template;

/**
 * 只支持区间累加和区间查询的线段树。
 *
 * 调用约定：
 * 1. 外部数组 origin 是 0-based。
 * 2. 线段树内部使用 1-based arr。
 * 3. 对外暴露的 add(left, right, value) 和 query(left, right) 接收 0-based 下标。
 * 4. 内部递归方法 build / add / query 的区间边界使用 1-based。
 *
 * 记忆方式：
 * origin 下标是 0 ~ len - 1，线段树逻辑下标是 1 ~ len。
 * 所以外部 API 传入 origin[0..2] 时，内部递归区间需要转换为 [1..3]。
 */
public class RangeAddSegmentTree {

    private static final int ROOT = 1;
    private static final int START = 1;

    private final int[] arr;
    private final int[] sum;
    private final int[] lazy;
    private final int n;

    public RangeAddSegmentTree(int[] origin) {
        n = origin.length;
        int maxN = origin.length + 1;
        arr = new int[maxN];
        for (int i = 1; i < arr.length; i++) {
            arr[i] = origin[i - 1];
        }
        sum = new int[maxN * 4];
        lazy = new int[maxN * 4];
        build(ROOT, START, n);
    }

    public void add(int left, int right, int value) {
        add(ROOT, START, n, left + 1, right + 1, value);
    }

    public int query(int left, int right) {
        return query(ROOT, START, n, left + 1, right + 1);
    }

    public void build(int rt, int l, int r) {
        if (l == r) {
            sum[rt] = arr[l];
            return;
        }
        int mid = l + (r - l) / 2;
        build(rt * 2, l, mid);
        build(rt * 2 + 1, mid + 1, r);
        pushUp(rt);
    }

    public void add(int rt, int l, int r, int L, int R, int C) {
        if (L <= l && r <= R) {
            sum[rt] += C * (r - l + 1);
            lazy[rt] += C;
            return;
        }
        int mid = l + (r - l) / 2;
        pushDown(rt, mid - l + 1, r - mid);
        if (L <= mid) {
            add(rt * 2, l, mid, L, R, C);
        }
        if (R > mid) {
            add(rt * 2 + 1, mid + 1, r, L, R, C);
        }
        pushUp(rt);
    }

    public int query(int rt, int l, int r, int L, int R) {
        if (L <= l && r <= R) {
            return sum[rt];
        }
        int mid = l + (r - l) / 2;
        pushDown(rt, mid - l + 1, r - mid);
        int ans = 0;
        if (L <= mid) {
            ans += query(rt * 2, l, mid, L, R);
        }
        if (R > mid) {
            ans += query(rt * 2 + 1, mid + 1, r, L, R);
        }
        return ans;
    }

    private void pushUp(int rt) {
        sum[rt] = sum[rt * 2] + sum[rt * 2 + 1];
    }

    private void pushDown(int rt, int ln, int rn) {
        if (lazy[rt] == 0) {
            return;
        }
        lazy[rt * 2] += lazy[rt];
        lazy[rt * 2 + 1] += lazy[rt];
        sum[rt * 2] += lazy[rt] * ln;
        sum[rt * 2 + 1] += lazy[rt] * rn;
        lazy[rt] = 0;
    }
}
