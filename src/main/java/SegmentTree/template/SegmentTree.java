package SegmentTree.template;

/**
 * 特别注意线段树的使用方式啊： build(1, 1, origin.length) 因为rt=1, 最左边界=1 最右边界=origin.length
 * 第二个注意点，add时，pushDown中，关于lazy[rt]的更新。 都是lazy+= 而不是lazy=！！！（跟update不同！！！！） 因为之前可能加过值，此时又加了
 * <p>
 * 核心思想：
 * 简单记：
 * (a) **能懒则懒，懒不住就下发，让孩子懒**。 【特别注意】懒住的那一层，是要更新sum的
 * (b) 实现层面，除了PushDown和pushUp都是递归操作：
 * build只有pushUp，只需要汇总信息; build就是从底下建起一棵树。只有pushUp。
 * add/update pushDown+pushUp 因为涉及到下发和更新sum
 * query 只有pushDown 只涉及到下发（因为可能懒不住，需要更小的线段返回值）。 但是因为没有更新(add/update)，父节点的sum本来就是对的，所以不需要pushUp。
 * <p>
 * （1）能懒则懒。注意，懒住的线段，是更新sum的，他只是存了 懒住了让下面线段不更新的哪些信息。 因为是支持查到这个线段时，直接返回sum的，所以sum一定要更新。
 * （2）懒意味着，下面的线段sum,add,update都不更新。但是懒上面的sum一定全部更新的（因为上面没懒住，所以下推到了懒住的线段）。
 * （3）懒不住，则必须PushDown。不存在2层存在懒信息，然后直接更新/添加/查询 3层sum的。因为不下推的话，下面的数据是错的，无法使用。
 * （4）【重要】懒上面层一定是对的，懒下面层是没有更新的：（1）所以递归完成后，要pushUp（2）同时，所以如果当前无法懒住，要让child懒，则需要先更新对 child信息，所以要先pushDown，然后再递归孩子。
 */
public class SegmentTree {
    // 基本属性, 构造器
    // pushUp, pushDown
    // build, add, update, query
    private int MAXN;
    private int[] arr;   // 用作转换数组变成从1开头
    private int[] sum;   // 后续相关联的sum,lazy,update,change 都是对应从1开头的
    private int[] lazy;
    private int[] change;
    private boolean[] update;


    // 构造器：
    // 入参：origin原数组
    // 执行功能：初始化所有的数组，对应1开头
    public SegmentTree(int[] origin) {
        MAXN = origin.length + 1;
        arr = new int[MAXN];
        // TODO: 构造器内的内容：把外部数组 放到内部数组，效果就是 平均每个位置右移一位
        for (int i = 1; i < arr.length; i++) {
            arr[i] = origin[i - 1];
        }
        sum = new int[MAXN * 4];
        lazy = new int[MAXN * 4];
        update = new boolean[MAXN * 4];
        change = new int[MAXN * 4];
    }

    // 父结果 = 左信息 联合 右信息
    public void pushUp(int rt) {
        sum[rt] = sum[rt * 2] + sum[rt * 2 + 1];
    }

    public void pushDown(int rt, int ln, int rn) {
        // 一定要先考虑update，后考虑lazy。  因为同时存在一定意味着现有update 后来了lazy

        // 先确定左孩子区间， 右孩子区间
        if (update[rt]) {  // sum[rt]一定是已经更新了，所以if内不需要处理，只需要做下推
            // 左右孩子更新update, 更新左右孩子sum，更新左右孩子lazy，更新自身update
            // 必要更新元素: change update sum   + 清理自己update=false
            change[rt * 2] = change[rt];
            change[rt * 2 + 1] = change[rt];
            update[rt * 2] = true;
            update[rt * 2 + 1] = true;
            sum[rt * 2] = change[rt] * ln;
            sum[rt * 2 + 1] = change[rt] * rn;
            lazy[rt * 2] = 0;
            lazy[rt * 2 + 1] = 0;
            update[rt] = false;   // 最后别忘了清空 rt的状态
        }

        // lazy 与 update并存的话，代表 先update懒住，然后出现了lazy懒住。 不然的话， lazy=0
        if (lazy[rt] != 0) {
            // TODO: 【错误】下面注意！！同样的,这里的lazy也都是要 lazy+=， 不是 lazy=！！！！！
            //  lazy[rt * 2] = lazy[rt];
            //  lazy[rt * 2 + 1] = lazy[rt];
            // 先搞左右孩子。 注意，懒下来了，(1) lazy（错误点：特别注意，要 lazy+= 而不是lazy=，跟update不同！！！）  (2) sum   +   清理自己lazy=0
            lazy[rt * 2] += lazy[rt];
            lazy[rt * 2 + 1] += lazy[rt];
            sum[rt * 2] += lazy[rt] * ln;
            sum[rt * 2 + 1] += lazy[rt] * rn;
            lazy[rt] = 0;
        }

    }

    // 四剑客，都是递归？ Build, update, add, query
    public void build(int rt, int l, int r) {
        if (l == r) {
            // TODO: 【错误】！！！ 如下
            //  sum[rt] = arr[rt];
            sum[rt] = arr[l];
            // TODO: 而且递归呀，base case怎么能忘了return？  即便返回类型是void也必须记得return!!!
            return;
        }
        int mid = l + (r - l) / 2;
        // 左孩子算出值， 右孩子算出值
        build(rt * 2, l, mid);
        build(rt * 2 + 1, mid + 1, r);
        // pushUp 算出 当前parent值
        pushUp(rt);
    }

    public void update(int rt, int l, int r, int L, int R, int C) {
        if (l >= L && r <= R) {
            update[rt] = true;
            change[rt] = C;
            sum[rt] = C * (r - l + 1);
            lazy[rt] = 0;
            return;
        }
        // 当前任务躲不掉，无法懒更新，要往下发
        int mid = l + (r - l) / 2;
        int ln = mid - l + 1;
        int rn = r - mid;
        pushDown(rt, ln, rn);
        // TODO: 【错误】别忘了必须加 if检查
        if (L <= mid) {       // 如果涉及左区间
            update(rt * 2, l, mid, L, R, C);
        }
        if (R >= mid + 1) {   // 如果涉及右区间
            update(rt * 2 + 1, mid + 1, r, L, R, C);
        }
        pushUp(rt);
    }

    public void add(int rt, int l, int r, int L, int R, int C) {
        if (l >= L && r <= R) {
            // TODO: 【错误！！！】 add时， lazy上面可能有 之前懒add的值，所以此处不能使用 lazy=，要使用lazy+=
            //  错误line：lazy[rt] = C;
            lazy[rt] += C;
            sum[rt] += C * (r - l + 1);
            return;
        }

        int mid = l + (r - l) / 2;
        int ln = mid - l + 1;
        int rn = r - mid;
        pushDown(rt, ln, rn);
        if (L <= mid) {
            add(rt * 2, l, mid, L, R, C);
        }
        if (R >= mid + 1) {
            add(rt * 2 + 1, mid + 1, r, L, R, C);
        }
        pushUp(rt);
    }


    public int query(int rt, int l, int r, int L, int R) {
        if (l >= L && r <= R) {
            return sum[rt];
        }
        // 此时要从孩子节点去抓值，但是此时孩子节点的值没有更新，被我懒住了，所以我要下推-更新孩子的值
        int mid = l + (r - l) / 2;
        pushDown(rt, mid - l + 1, r - mid);
        int ans = 0;
        if (L <= mid) {
            ans += query(rt * 2, l, mid, L, R);
        }

        if (R >= mid + 1) {
            ans += query(rt * 2 + 1, mid + 1, r, L, R);
        }
        return ans;
    }

    public static class Right {
        public int[] arr;

        public Right(int[] origin) {
            arr = new int[origin.length + 1];
            for (int i = 0; i < origin.length; i++) {
                arr[i + 1] = origin[i];
            }
        }

        public void update(int L, int R, int C) {
            for (int i = L; i <= R; i++) {
                arr[i] = C;
            }
        }

        public void add(int L, int R, int C) {
            for (int i = L; i <= R; i++) {
                arr[i] += C;
            }
        }

        public long query(int L, int R) {
            long ans = 0;
            for (int i = L; i <= R; i++) {
                ans += arr[i];
            }
            return ans;
        }

    }

    public static int[] genarateRandomArray(int len, int max) {
        int size = (int) (Math.random() * len) + 1;
        int[] origin = new int[size];
        for (int i = 0; i < size; i++) {
            origin[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
        }
        return origin;
    }

    public static boolean test() {
        int len = 100;
        int max = 1000;
        int testTimes = 5000;
        int addOrUpdateTimes = 1000;
        int queryTimes = 500;
        for (int i = 0; i < testTimes; i++) {
            int[] origin = genarateRandomArray(len, max);
//            SegmentTree seg = new SegmentTree(origin);
            MySegmentTree.SegmentTree seg = new MySegmentTree.SegmentTree(origin);
            int S = 1;
            int N = origin.length;
            int root = 1;
            seg.build(root, S, N);
            Right rig = new Right(origin);
            for (int j = 0; j < addOrUpdateTimes; j++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                int C = (int) (Math.random() * max) - (int) (Math.random() * max);
                if (Math.random() < 0.5) {
//                    seg.add(L, R, C, S, N, root);
                    seg.add(root, S, N, L, R, C);
                    rig.add(L, R, C);
                } else {
//                    seg.update(L, R, C, S, N, root);
                    seg.update(root, S, N, L, R, C);
                    rig.update(L, R, C);
                }
            }
            for (int k = 0; k < queryTimes; k++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
//                long ans1 = seg.query(L, R, S, N, root);
                long ans1 = seg.query(root, S, N, L, R);
                long ans2 = rig.query(L, R);
                if (ans1 != ans2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] origin = {2, 1, 1, 2, 3, 4, 5};
        SegmentTree seg = new SegmentTree(origin);
        int S = 1; // 整个区间的开始位置，规定从1开始，不从0开始 -> 固定
        int N = origin.length; // 整个区间的结束位置，规定能到N，不是N-1 -> 固定
        int root = 1; // 整棵树的头节点位置，规定是1，不是0 -> 固定
        int L = 2; // 操作区间的开始位置 -> 可变
        int R = 5; // 操作区间的结束位置 -> 可变
        int C = 4; // 要加的数字或者要更新的数字 -> 可变
        // TODO： build方法的调用 -> 区间生成，必须在[1,N]整个范围上build
        seg.build(root, S, N);
        // 区间修改，可以改变L、R和C的值，其他值不可改变
//        seg.add(L, R, C, S, N, root);
        seg.add(root, S, N, L, R, C);
        // 区间更新，可以改变L、R和C的值，其他值不可改变
//        seg.update(L, R, C, S, N, root);
        seg.update(root, S, N, L, R, C);
        // 区间查询，可以改变L和R的值，其他值不可改变
//        long sum = seg.query(L, R, S, N, root);
        long sum = seg.query(root, S, N, L, R);
        System.out.println(sum);

        System.out.println("对数器测试开始...");
        System.out.println("测试结果 : " + (test() ? "通过" : "未通过"));

    }


}
