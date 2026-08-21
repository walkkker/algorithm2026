package SegmentTree.template;

/**
 * 1. pushUp pushDown build update add
 */
public class MySegmentTree {

    public static class SegmentTree {

        int[] arr;
        int[] sum;
        int[] lazy;
        int[] change;
        boolean[] update;

        public SegmentTree(int[] origin) {
            int MAXN = origin.length + 1;
            arr = new int[MAXN];
            // TODO: 错误原因：构造器只分配 arr 但没有把 origin 拷贝到 1-based arr，导致 build() 读取 arr[l] 时全是默认值 0；修改意见：将 origin[i - 1] 写入 arr[i]，让 build() 能基于原始数组初始化 sum。
            // 错误代码：这里缺少 origin -> arr 的初始化拷贝。
            for (int i = 1; i < arr.length; i++) {
                arr[i] = origin[i - 1];
            }
            sum = new int[MAXN * 4];
            lazy = new int[MAXN * 4];
            change = new int[MAXN * 4];
            update = new boolean[MAXN * 4];
        }

        public void pushUp(int i) {
            sum[i] = sum[2 * i] + sum[2 * i + 1];
        }

        public void pushDown(int i, int ln, int rn) {
            if (update[i]) {
                update[i * 2] = true;
                update[i * 2 + 1] = true;
                change[i * 2] = change[i];
                change[i * 2 + 1] = change[i];
                lazy[i * 2] = 0;
                lazy[i * 2 + 1] = 0;
                sum[i * 2] = change[i] * ln;
                sum[i * 2 + 1] = change[i] * rn;
                update[i] = false;
            }

            if (lazy[i] != 0) {
                lazy[2 * i] += lazy[i];
                lazy[2 * i + 1] += lazy[i];
                sum[2 * i] += lazy[i] * ln;
                sum[2 * i + 1] += lazy[i] * rn;
                lazy[i] = 0;
            }
        }

        public void build(int root, int l, int r) {
            if (l == r) {
                sum[root] = arr[l];
                return;
            }
            int mid = (l + r) / 2;
            build(root * 2, l, mid);
            build(root * 2 + 1, mid + 1, r);
            pushUp(root);
        }

        public void update(int i, int l, int r, int L, int R, int C) {
            if (l >= L && r <= R) {
                update[i] = true;
                change[i] = C;
                lazy[i] = 0;
                sum[i] = C * (r - l + 1);
                return;
            }
            int mid = (l + r) / 2;
            pushDown(i, mid - l + 1, r - mid);
            // TODO: 【错误】特别注意！！！线段树的子递归，需要 if，不加if的话base case停不了。 会无限递归下去，最终stackOverflow || IndexOutOfBounds
            //            update(i * 2, l, mid, L, R, C);
            //            update(i * 2 + 1, mid + 1, r, L, R, C);
            if (L <= mid) {
                update(i * 2, l, mid, L, R, C);
            }
            if (R > mid) {
                update(i * 2 + 1, mid + 1, r, L, R, C);
            }
            pushUp(i);
        }

        public void add(int i, int l, int r, int L, int R, int C) {
            if (l >= L && r <= R) {
                lazy[i] += C;
                sum[i] += C * (r - l + 1);
                return;
            }
            int mid = (l + r) / 2;
            pushDown(i, mid - l + 1, r - mid);
            // TODO: 【错误-同理没有if!!! 无限递归了！！】
            //            add(i * 2, l, mid, L, R, C);
            //            add(i * 2 + 1, mid + 1, r, L, R, C);
            if (L <= mid) {
                add(i * 2, l, mid, L, R, C);
            }
            if (R > mid) {
                add(i * 2 + 1, mid + 1, r, L, R, C);
            }
            pushUp(i);
        }

        public int query(int i, int l, int r, int L, int R) {
            if (l >= L && r <= R) {
                return sum[i];
            }
            int mid = (l + r) / 2;
            pushDown(i, mid - l + 1, r - mid);
            // TODO: 【错误-同理没有if！！！！ 报错无限递归到数组越界】
            int p1 = 0;
            if (L <= mid) {
                p1 = query(i * 2, l, mid, L, R);
            }
            int p2 = 0;
            if (R > mid) {
                p2 = query(i * 2 + 1, mid + 1, r, L, R);
            }
            return p1 + p2;
        }


    }


}
