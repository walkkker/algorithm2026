package dp;

/**
 * TODO:结合 RobotWalk 可以发现。 主函数的递归调用 && dp[?][?]的含义和最终返回坐标
 *      【在我们的实现中，语义都是 i,j代表起始状态。 dp[?][?]代表从i&&j的起始状态 到最终目标 的值。】
 *      ！！！简而言之，int[?][?] dp返回时 ，??就填 【问题的初始状态】就可以。！！！
 * <p>
 * <p>
 * 输入是 int[] arr 数组，代表 cardsInLine。
 * <p>
 * 问题：两个人都绝顶聪明，根据规则，返回获胜者的分数 (先手可能获胜 、 后手也可能获胜)
 * <p>
 * 答案：
 * (1) 范围尝试模型 l , r
 * (2) 设置两个递归函数：先手f、 后手g -> 递归函数之间是可以互调的，只要 你在每次互相调用的情况下，【缩小问题规模】，那么就会到达base case
 */
public class CardsInLine {
    // TODO: 关于互相调用下的 base case的深入理解。（1） f->g->f->g...->BaseCase 交替递归 （2）问题规模依次-1缩小
    //  虽然f和g在互相调用，但是他俩都在不断地缩小数据规模，所以最终会收到 base case
    //  补充：如果你看递归栈的话，会发现是 f->g->f->g->[f->]BaseCase 循环调用，直到BaseCase然后依次返回值。
    //  【重点来了】，所以可以知道，如果设置BaseCase为 l>r，那么一定可以到达这个状态。 因为不断在减小
    //  【续补充】：看了下左神的代码，他是 l==r。 这么看起来，因为不断再减小， 最后到达baseCase的 可能是f也可能是g，但是其中一个一定会到达 f==g的case（这俩f,g都有可能 到达baseCase，而另一个是r-l=1的调用它的）。 然后直接返回值。 然后按照递归调用链依次返回。
    //   但是这f&&g的 base case必须是一样的啊。 因为不确定最终是谁会到达base case。

    /***********         记忆化搜索 - 普通递归在后面           **************/
    public static int f2(int l, int r, int[] arr, int[][] dpf, int[][] dpg) {
        // TODO: 这里特别注意，因为 轮流递归，所以 dp傻缓存，你也要传两个！！！【注意区分好谁給谁用】
        //  仔细看下代码，f 和 g都需要传两个int[][] dp
        if (dpf[l][r] != -1) {
            return dpf[l][r];
        }
        if (l > r) {
            return 0;
        }
        // 存在两种情况（选左或选右），比大小
        // 因为我是先手，所以选最大。
        int p1 = arr[l] + g2(l + 1, r, arr, dpf, dpg);
        int p2 = arr[r] + g2(l, r - 1, arr, dpf, dpg);
        int ans = Math.max(p1, p2);
        dpf[l][r] = ans;
        return ans;
    }


    public static int g2(int l, int r, int[] arr, int[][] dpf, int[][] dpg) {
        if (dpg[l][r] != -1) {
            return dpg[l][r];
        }

        // todo: 这是人写出来的吗？？？
//        if (r > l) {
        if (l > r) {
            return 0;
        }
        // 对方选完左 或者 选完右，所以对方会给我留下的就是我先手的值。
        // 注意：因为这个是对方先发起的，所以给我留下的两个选择，会返回最小的那个可能性 （对方选左或者选右，目标是让我 得分最低）
        int p1 = f2(l + 1, r, arr, dpf, dpg);
        // TODO: 你在梦游吗？
//        int p2 = f1(l, r + 1, arr);
        int p2 = f2(l, r - 1, arr, dpf, dpg);
        int ans = Math.min(p1, p2);
        dpg[l][r] = ans;
        return ans;
    }

    public static int win2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        int[][] dpf = new int[n][n];
        int[][] dpg = new int[n][n];
        // TODO: 记忆化搜索/傻缓存 必须初始化为-1
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dpf[i][j] = -1;
                dpg[i][j] = -1;
            }
        }


        int first = f1(0, arr.length - 1, arr);
        int second = g1(0, arr.length - 1, arr);
//        int sum = 0;
//        for (int num : arr) {
//            sum += num;
//        }

        return Math.max(first, second);
    }


    /*********     下面是朴素方法，普通递归             ************/

    public static int f1(int l, int r, int[] arr) {
        if (l > r) {
            return 0;
        }
        // 存在两种情况（选左或选右），比大小
        // 因为我是先手，所以选最大。
        int p1 = arr[l] + g1(l + 1, r, arr);
        int p2 = arr[r] + g1(l, r - 1, arr);
        return Math.max(p1, p2);
    }


    public static int g1(int l, int r, int[] arr) {
        // todo: 这是人写出来的吗？？？
//        if (r > l) {
        if (l > r) {
            return 0;
        }
        // 对方选完左 或者 选完右，所以对方会给我留下的就是我先手的值。
        // 注意：因为这个是对方先发起的，所以给我留下的两个选择，会返回最小的那个可能性 （对方选左或者选右，目标是让我 得分最低）
        int p1 = f1(l + 1, r, arr);
        // TODO: 你在梦游吗？
//        int p2 = f1(l, r + 1, arr);
        int p2 = f1(l, r - 1, arr);
        return Math.min(p1, p2);
    }

    public static int win1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int first = f1(0, arr.length - 1, arr);
//        int second = g1(0, arr.length - 1, arr);
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }

        return Math.max(first, sum - first);
    }


    /********  转DP: dp不像傻缓存，不需要初始化（傻缓存初始化 是为了区分 缓存是否命中）。 dp是直接填表，依据依赖关系依次 推出表格值    *************/
    public static int win3(int[] arr) {
        int n = arr.length;
        int[][] f = new int[n][n];
        int[][] g = new int[n][n];
        // TODO： baseCase： l > r 对应的就是 对角线的 下方。 都为0
        //  看下依赖关系：f依赖g的左+下。 g依赖f的左+下。  =》 与对角线平行的方向 依次填充 / 逆着来，由下往上 由左往右


        // TODO: 【错误！！！】从下往上推，你怎么会写出这样的东西？？？？   鞭打自己必须记住
        //        for (int i = n - 1; i >= 0; i++) {
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                // 又回到通用的表达式了， 有下选下，有左选左 =》 整合：比较

                // f
                int p1 = i + 1 < n ? arr[i] + g[i + 1][j] : -1;
                // TODO：【错误】这里你又错了！！！  很多情况下都是可能没左的！！！ 比如n=1的最右下也没左 ； n>1时，最左上也没有左 （谁叫你要从对角线开始写的=-=）
                //  注意：我们这个写法 是因为发现 对角线及上部分，左 下 一定存在1/2所以这么写。 但是这么写 会有一个局限性，n=1时，结果会错误-> 得到2147483647 （variable second）。  => 【所以看下还是采取左神的办法，先把 依赖不全部存在的先单独填好。 】
//                int p2 = arr[j] + g[i][j - 1];
                int p2 = j - 1 >= 0 ? arr[j] + g[i][j - 1] : -1;
                f[i][j] = Math.max(p1, p2);

                //g
                p1 = i + 1 < n ? f[i + 1][j] : Integer.MAX_VALUE;  //TODO: 这么写的时候，一定要小心！！！  我们要比较的是Math.min，所以这里要取最大值了。 跟上面的f是不同的，那个是取最大值，所以 无效值设置成了-1
                // TODO: 同理错误
//                p2 = f[i][j - 1];
                p2 = j - 1 >= 0 ? f[i][j - 1] : Integer.MAX_VALUE;
                g[i][j] = Math.min(p1, p2);
            }
        }
        int first = f[0][n - 1];
        int second = g[0][n - 1];
        return Math.max(first, second);
    }


    // TODO: 优化版本（能够自动处理好n=1的情况） -》 第三版没有考虑n=1的情况。 =》 结论：有时候process()的base case没有完全涵盖 int[][] dp的base case。
    //  什么意思呢？就是改int[][]的时候，你可以 扩大写递归的base case。  是完全合理的
    public static int win4(int[] arr) {
        int n = arr.length;
        int[][] f = new int[n][n];
        int[][] g = new int[n][n];
        // S1: l > r 对角线下半区 为 0
        // S2: l==r 对角线  f 和 g也可以定值 （f[i][i]=arr[i], g[i][i]=0）
        // S3: 此时剩余的l<r部分，全部都有 下 左
        for (int i = 0; i < n; i++) {
            f[i][i] = arr[i];
        }

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                // 此时，左 下 都有，不需要检查了
                f[i][j] = Math.max(arr[i] + g[i + 1][j], arr[j] + g[i][j - 1]);
                g[i][j] = Math.min(f[i + 1][j], f[i][j - 1]);
            }
        }
        // 这里展示下左神如何实现 斜着填 （平行于对角线的填法）
        /*
        for (int startCol = 1; startCol < N; startCol++) {
            int L = 0;
            int R = startCol;
            while (R < N) {
                fmap[L][R] = Math.max(arr[L] + gmap[L + 1][R], arr[R] + gmap[L][R - 1]);
                gmap[L][R] = Math.min(fmap[L + 1][R], fmap[L][R - 1]);
                L++;
                R++;
            }
        }
         */
        int first = f[0][n - 1];
        int second = g[0][n - 1];
        return Math.max(first, second);
    }

    public static void main(String[] args) {
        int[] arr = {5, 7, 4, 5, 8, 1, 6, 0, 3, 4, 6, 1, 7, 24, 15, 19};
//        int[] arr = {5};
        System.out.println(win1(arr));
        System.out.println(win2(arr));
        System.out.println(win3(arr));
        System.out.println(win4(arr));
        System.out.println(dpTest(arr));
    }

    public static int dpTest(int[] arr) {
        int len = arr.length;
        int[][] first = new int[len][len];
        int[][] after = new int[len][len];

        for (int i = 0, j = 0; i < len; i++, j++) {
            first[i][j] = arr[i];
            after[i][j] = 0;
        }

        for (int i = len - 2; i >= 0; i--) {
            for (int j = i + 1; j < len; j++) {
                first[i][j] = Math.max(arr[i] + after[i + 1][j], arr[j] + after[i][j - 1]);
                after[i][j] = Math.min(first[i + 1][j], first[i][j - 1]);
            }
        }
        return Math.max(first[0][len - 1], after[0][len - 1]);

    }

}
