package dp;

import java.util.Arrays;
import java.util.PriorityQueue;


/**
 * 1. 这题拆分成两题，可以看作是两个模型，很有借鉴意义。
 * （1）结合timeline的贪心：int[] arr 咖啡机的冲咖啡时间， int n人数。  要求返回一个新数组int[n] ans。 实现在全局最早完成冲咖啡的目标下，每一个人冲（/喝）完咖啡的时间点。
 * （2）从左往右（i位置 用洗碗机 or 等待挥发） + 业务尝试（核心就是timeline的范围题目没给，要自己判断）模型
 * - BaseCase是让你出数组边界的 =》 从左到右模型 我一般i的范围都会取 [0,n] 其中n代表没有元素的位置（数组最右元素的右侧了） =》 因为这个时候的base case好定义。 （当然你说拿最后一个位置行不行，当然可以，而且那样错误的dp初始化 也不会报错了，因为递归会在最后一个节点终止，不会吧timelineMax 再传给递归 触发访问dp[][]）
 * <p>
 * TODO：【记忆化搜索-超级重要坑】process函数内 【dp缓存】 一定要写在 【base case】 【后面】。 因为任何的越界问题，都是由base case拦住的。 放行的有效值，再代表着dp[][]可以访问。
 *  =>（比如i<0 || i>=arr.length） => 这些非法位置 是无法用dp缓存的 =》 只有有效下标才能让dp缓存  =》 非法下标（参数）都是由base case直接拦住。
 */
// 题目
// 数组arr代表每一个咖啡机冲一杯咖啡的时间，每个咖啡机只能串行的制造咖啡。
// 现在有n个人需要喝咖啡，只能用咖啡机来制造咖啡。
// 认为每个人喝咖啡的时间非常短，冲好的时间即是喝完的时间。
// 每个人喝完之后咖啡杯可以选择洗或者自然挥发干净，只有一台洗咖啡杯的机器，只能串行的洗咖啡杯。
// 洗杯子的机器洗完一个杯子时间为a，任何一个杯子自然挥发干净的时间为b。
// 四个参数：arr, n, a, b
// 假设时间点从0开始，返回所有人喝完咖啡并洗完咖啡杯的全部过程结束后，至少来到什么时间点。
public class Coffee {

    public static int[][] dp;

    /*
    拆成两个问题：
    (1) 最早喝完咖啡的时间 => 产生一个数组，让最终的结束时间最早   => 这个也超级有价值-> 看我的实现，我觉得很好 =》用到了 堆，以及设计了很好的 比较器
    (2) 基于(1)的数组，递归 最早结束 洗咖啡
     */
    public static int minTime1(int[] arr, int n, int a, int b) {
        int[] line = getFinishedTime(arr, n);
        Arrays.sort(line);
        // timeline 的最大值 => 所有人都用洗碗机 的最终时间
        int timeline = 0;
        for (int finishTime : line) {
            int start = Math.max(timeline, finishTime);
            int end = start + a;
            timeline = end;
        }
        // TODO: 【错误】业务尝试模型=》需要自己求timeline的最大边界=>我们通过全部人都洗碗得到最大的timeline=》这个timeline就是会在递归中传递到的！！！ （你想，最后一个元素 并不是我们的base case，所以在全洗碗的情况下， i=n-1时 得到了 timeline， 此时还要递归一次到 i=n 那么此时传递进来的 timeline就是最大值，会去访问 int[][] dp，所以越界了）
        //  =》然后dp[][]的列范围就可以确定为[0, timeline] =》 特别注意，【timelineMax是会访问到的坐标！！！】=》列大小 要设置为 timelineMax + 1呀
        //        dp = new int[n + 1][timeline];
        dp = new int[n + 1][timeline];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < timeline; j++) {
                dp[i][j] = -1;
            }
        }

        int ans = process(0, 0, line, a, b);
        return ans;
    }

    // 贪心：
    // 先解决第一个问题 使用小根堆，但是注意 自定义对象+比较器。 因为我们假设当前是最后一个i，那么如何定义堆内的值 能够获取 全局最小的结束时间呢
    public static int[] getFinishedTime(int[] arr, int n) {
        PriorityQueue<Record> heap = new PriorityQueue<>((o1, o2) -> (o1.timeline + o1.coffeeTime) - (o2.timeline + o2.coffeeTime));
        for (int num : arr) {
            heap.add(new Record(num, 0));
        }
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            Record record = heap.poll();
            int finishLine = record.timeline + record.coffeeTime;
            ans[i] = finishLine;
            record.timeline = finishLine;
            heap.add(record);
        }
        return ans;
    }

    // 给堆用的 自定义对象，必须结合两个属性。才能让 line[i] 得到 以i为终止时的 最早的结束时间。
    public static class Record {
        int coffeeTime;
        int timeline;

        public Record(int _coffeeTime, int _timeline) {
            coffeeTime = _coffeeTime;
            timeline = _timeline;
        }
    }

    /*
        i have 2 choices：
            (1) wash machine -> Math.max(washFinishTime, 递归)
            (2) wait for dry -> Math.max(dryTime, 递归)
     */
    // TODO：【记忆化搜索-超级重要坑】process函数内 【dp缓存】 一定要写在 【base case】 【后面】。 因为任何的越界问题，都是由base case拦住的。 放行的有效值，再代表着dp[][]可以访问。
    //     *  =>（比如i<0 || i>=arr.length） => 这些非法位置 是无法用dp缓存的 =》 只有有效下标才能让dp缓存  =》 非法下标（参数）都是由base case直接拦住。
    public static int process(int i, int timeline, int[] line, int a, int b) {
        // TODO: dp缓存必须放在 base case后面！！！(不是说放在前面就一定不对了，而是说放在后面一定能对！) 不然越界参数 会导致 dp[][] 访问报错 - ArrayIndexOutOfBoundsException

//        if (dp[i][timeline] != -1) {
//            return dp[i][timeline];
//        }

//        if (i == line.length) {
//            return timeline;
//        }
        if (i == line.length - 1) {
            // TODO: 这边的语义 有点晕了
            // 已经到达最后一个元素了， 没有依赖了。 START2END语义下，*要么挥发 要么洗碗*。 这个语义代表着 跟前面无关（不用管，只需要看timeline），就看当前状态！！！
            // 这个base case的意思也就是，现在只有一个元素，请你返回处理它的 完成的最早时间： 洗它 or 挥发它 就完事了！！！！！ start状态就是现在，就一个杯子。 END就是处理干净所有杯子的最小结束时间！！！
            int p1 = Math.min(Math.max(line[line.length - 1], timeline) + a, line[line.length - 1] + b);
            // TODO： 错误，返回Min呀，不是max!!!!!   => 只考虑 Math.min(洗这个杯子的结束时间， 挥发这个杯子的结束时间);
            return p1;
        }

        if (dp[i][timeline] != -1) {
            return dp[i][timeline];
        }

        // wash
        int start = Math.max(timeline, line[i]);  // 这个很重要，wash的话，起始点 不一定是timeline
        int end = start + a;
        int p1 = Math.max(end, process(i + 1, end, line, a, b));


        // dry
        end = line[i] + b;
        int p2 = Math.max(end, process(i + 1, timeline, line, a, b));

        int ans = Math.min(p1, p2);
        dp[i][timeline] = ans;
        return ans;
    }


    /*****    对数器       *****/
    // 验证的方法
    // 彻底的暴力
    // 很慢但是绝对正确
    public static int right(int[] arr, int n, int a, int b) {
        int[] times = new int[arr.length];
        int[] drink = new int[n];
        return forceMake(arr, times, 0, drink, n, a, b);
    }

    // 每个人暴力尝试用每一个咖啡机给自己做咖啡
    public static int forceMake(int[] arr, int[] times, int kth, int[] drink, int n, int a, int b) {
        if (kth == n) {
            int[] drinkSorted = Arrays.copyOf(drink, kth);
            Arrays.sort(drinkSorted);
            return forceWash(drinkSorted, a, b, 0, 0, 0);
        }
        int time = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            int work = arr[i];
            int pre = times[i];
            drink[kth] = pre + work;
            times[i] = pre + work;
            time = Math.min(time, forceMake(arr, times, kth + 1, drink, n, a, b));
            drink[kth] = 0;
            times[i] = pre;
        }
        return time;
    }

    public static int forceWash(int[] drinks, int a, int b, int index, int washLine, int time) {
        if (index == drinks.length) {
            return time;
        }
        // 选择一：当前index号咖啡杯，选择用洗咖啡机刷干净
        int wash = Math.max(drinks[index], washLine) + a;
        int ans1 = forceWash(drinks, a, b, index + 1, wash, Math.max(wash, time));

        // 选择二：当前index号咖啡杯，选择自然挥发
        int dry = drinks[index] + b;
        int ans2 = forceWash(drinks, a, b, index + 1, washLine, Math.max(dry, time));
        return Math.min(ans1, ans2);
    }


    // for test
    public static int[] randomArray(int len, int max) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * max) + 1;
        }
        return arr;
    }

    // for test
    public static void printArray(int[] arr) {
        System.out.print("arr : ");
        for (int j = 0; j < arr.length; j++) {
            System.out.print(arr[j] + ", ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int len = 10;
        int max = 10;
        int testTime = 1000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[] arr = randomArray(len, max);
            int n = (int) (Math.random() * 7) + 1;
            int a = (int) (Math.random() * 7) + 1;
            int b = (int) (Math.random() * 10) + 1;
            int ans1 = right(arr, n, a, b);
            int ans2 = minTime1(arr, n, a, b);
//            int ans3 = minTime2(arr, n, a, b);
            int ans3 = coffeeTest(arr, n, a, b);
            if (ans1 != ans2 || ans1 != ans3) {
                printArray(arr);
                System.out.println("n : " + n);
                System.out.println("a : " + a);
                System.out.println("b : " + b);
                System.out.println(ans1 + " , " + ans2);
                System.out.println(ans1 + " , " + ans3);
                System.out.println("===============");
                break;
            }
//            System.out.println(ans1 + " , " + ans2);
        }
        System.out.println("测试结束");

    }

    public static int coffeeTest(int[] arr, int n, int a, int b) {
        int[] people = finishDrinking(arr, n);
        return dpTest(people, a, b);
    }

    public static class Record1 {
        int timeline;
        int index;

        public Record1(int _timeline, int _index) {
            timeline = _timeline;
            index = _index;
        }
    }

    public static int[] finishDrinking(int[] arr, int n) {
        int[] ans = new int[n];
        int finishLine = 0;
        PriorityQueue<Record1> heap = new PriorityQueue<>((a, b) -> (a.timeline - b.timeline));
        for (int i = 0; i < arr.length; i++) {
            heap.offer(new Record1(arr[i], i));
        }
        for (int i = 0; i < n; i++) {
            Record1 record = heap.poll();
            ans[i] = record.timeline;
            finishLine = Math.max(finishLine, record.timeline);
            record.timeline += arr[record.index];
            heap.offer(record);
        }
        return ans;
    }

    // a是机器洗杯子，b是自然挥发
    public static int dpTest(int[] people, int a, int b) {
        int n = people.length;
        Arrays.sort(people);

        int timeline = 0;
        for (int p : people) {
            timeline = Math.max(p, timeline) + a;
        }
        timeline = Math.max(timeline, people[people.length - 1] + b);

        int[][] dp = new int[n + 1][timeline + 1];   // 洗碗机最早的timeline
        for (int j = 0; j <= timeline; j++) {
            dp[n][j] = j;
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= timeline; j++) {
//                int t;
//                if (j + a > timeline) {
//                    t = people[i];
//                } else {
//                    t = Math.max(people[i], j);
//                }
                int t = Math.max(people[i], j);
                int p1;
                if (t + a > timeline) {
                    p1 = Integer.MAX_VALUE;
                } else {
                    p1 = dp[i + 1][t + a];
                }

                // TODO: 【错误点】粗心，分心。  笔记本上写timeline是变量，但是这里实际上是对应的j。  timeline已经赋值最大了，这里是要写j （代表最晚timeline）
                //  所以命名很重要，不要前后容易混乱。
                // int p2 = Math.max(people[i] + b, dp[i + 1][timeline]);
                int p2 = Math.max(people[i] + b, dp[i + 1][j]);
                dp[i][j] = Math.min(p1, p2);
            }
        }
        return dp[0][0];
    }


}



