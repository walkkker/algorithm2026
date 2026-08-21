package dp.货币面值系列;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Code04_CoinsWaySameValueSamePaper {

    public static int mydp(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {   // 左神这个边界条件扣的真的很细 ——》 因为本题一定是要把纸币装起来的，所以如果arr.length==0，即便aim=0，也不能称之为 有一种方法数。 因为根本就没有纸币
            // TODO: 无效状态 你返回个 鸡毛-1? 答案是方法数呀 =》 返回0  （其实呢，这种边界条件，还是要具体看题意。 因为换个理解，如果aim=0，是否可以理解为任意长度的arr（including arr.length==0），都应该是有1中组合方式呢）
            //   return -1;
            return 0;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        // TODO: 【错误】这边的 词频统计 写错了！！！！ 啊啊啊啊啊！！！  你这个逻辑真是蠢啊！ 记住公式！！！
        for (int money : arr) {
//            if (!map.containsKey(money)) {
//                // TODO: 【错误-一定记住】 没有key，那么只加key。 value 设置为 0
////                map.put(money, 1);
//                map.put(money, 0);
//            }
//            // TODO: 续：因为这里会统一的 value+1。
//            map.put(money, map.get(money) + 1);

            // TODO: 【错误反思】我发现了，当你感觉别人好像是这么写的高级，所以没有完全过大脑每一步逻辑就写出来的时候，往往就会犯错！！！
            // TODO: 所以！！！ 我决定 先按照自己的 逻辑全写出来（尤其好像有印象，但是没有专门练习过） =》 而后，通过观察代码，再修改优化
            // TODO: 没有任何必要，一开始就写优化版本 =》 你没有那么厉害！！！
            // TODO：所以，要是我写词频统计，我会这么写，先写最完整思路的 =》 感觉到可以优化，再改代码 =》 以做对题为第一前提，第二前提再是让面试官觉得你厉害 =》 千万不要本末倒置！！！！
            if (!map.containsKey(money)) {   // 不存在 => 直接设置value=1
                map.put(money, 1);
            } else {   // 已经存在 => map.get()取出当前值 + 1
                map.put(money, map.get(money) + 1);
            }
        }
        int size = map.size();
        arr = new int[size];
        int[] count = new int[size];
        int index = 0;
        for (int money : map.keySet()) {
            arr[index] = money;
            count[index++] = map.get(money);
        }
        int n = arr.length;
        // TODO: 【错误排查】上面的词频统计 错误逻辑。 是我把生成的两个数组打印出来发现的。  下面的dp推导，我怎么看，也没觉得有问题。果然是上面的问题。 难的做出来了，结果简单的倒下了。
//        System.out.println(Arrays.toString(arr));
//        System.out.println(Arrays.toString(count));    // 明明原arr只有一个数字，count[i]=2
        /****** 至此，转换成无重复值的 有限张数dp问题 => 会有两个数组 *********/
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][aim] = 1;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= aim; j++) {
                int tmp = 0;
                for (int time = 0; time <= count[i]; time++) {
                    tmp += pick(i + 1, j + time * arr[i], dp);  // TODO： 放一个pick函数 ， 1) 语义角度，越界的dp 说明 错误状态START，那么它到END的方法数 一定是0.  2) 代码逻辑角度：而且二次验证，我们对枚举的聚合方式是 累加 =》 无效dp返回0，没问题，对累加结果不影响
                    // TODO: 1)你算完tmp，不赋值了吗？？？？？？？  2)而且你放在这里干嘛呢？  这个枚举行为 聚合得出的最终值 才是dp[i][j]的结果！！
//                    dp[i][j] = tmp;
                }
                dp[i][j] = tmp;
            }
        }
        return dp[0][0];
    }

    public static int pick(int i, int j, int[][] dp) {
        int m = dp.length;
        int n = dp[0].length;
        if (i < 0 || i >= m || j < 0 || j >= n) {  // 越界代表无效的start，那么【无效START到 目标END 的**方法数** 为 0】，从而不会影响到 dp[i][j]的累加值
            return 0;
        }
        return dp[i][j];
    }


    public static class Info {
        public int[] coins;
        public int[] zhangs;

        public Info(int[] c, int[] z) {
            coins = c;
            zhangs = z;
        }
    }

    public static Info getInfo(int[] arr) {
        HashMap<Integer, Integer> counts = new HashMap<>();
        for (int value : arr) {
            if (!counts.containsKey(value)) {
                counts.put(value, 1);
            } else {
                counts.put(value, counts.get(value) + 1);
            }
        }
        int N = counts.size();
        int[] coins = new int[N];
        int[] zhangs = new int[N];
        int index = 0;
        for (Entry<Integer, Integer> entry : counts.entrySet()) {
            coins[index] = entry.getKey();
            zhangs[index++] = entry.getValue();
        }
        return new Info(coins, zhangs);
    }

    public static int coinsWay(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        Info info = getInfo(arr);
        return process(info.coins, info.zhangs, 0, aim);
    }

    // coins 面值数组，正数且去重
    // zhangs 每种面值对应的张数
    public static int process(int[] coins, int[] zhangs, int index, int rest) {
        if (index == coins.length) {
            return rest == 0 ? 1 : 0;
        }
        int ways = 0;
        for (int zhang = 0; zhang * coins[index] <= rest && zhang <= zhangs[index]; zhang++) {
            ways += process(coins, zhangs, index + 1, rest - (zhang * coins[index]));
        }
        return ways;
    }

    public static int dp1(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        Info info = getInfo(arr);
        int[] coins = info.coins;
        int[] zhangs = info.zhangs;
        int N = coins.length;
        int[][] dp = new int[N + 1][aim + 1];
        dp[N][0] = 1;
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                int ways = 0;
                for (int zhang = 0; zhang * coins[index] <= rest && zhang <= zhangs[index]; zhang++) {
                    ways += dp[index + 1][rest - (zhang * coins[index])];
                }
                dp[index][rest] = ways;
            }
        }
        return dp[0][aim];
    }

    public static int dp2(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        Info info = getInfo(arr);
        int[] coins = info.coins;
        int[] zhangs = info.zhangs;
        int N = coins.length;
        int[][] dp = new int[N + 1][aim + 1];
        dp[N][0] = 1;
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                dp[index][rest] = dp[index + 1][rest];
                if (rest - coins[index] >= 0) {
                    dp[index][rest] += dp[index][rest - coins[index]];
                }
                if (rest - coins[index] * (zhangs[index] + 1) >= 0) {
                    dp[index][rest] -= dp[index + 1][rest - coins[index] * (zhangs[index] + 1)];
                }
            }
        }
        return dp[0][aim];
    }

    // 为了测试
    public static int[] randomArray(int maxLen, int maxValue) {
        int N = (int) (Math.random() * maxLen);
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = (int) (Math.random() * maxValue) + 1;
        }
        return arr;
    }

    // 为了测试
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    // 为了测试
    public static void main(String[] args) {
        int maxLen = 10;
        int maxValue = 20;
        int testTime = 1000000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[] arr = randomArray(maxLen, maxValue);
            int aim = (int) (Math.random() * maxValue);
            int ans1 = coinsWay(arr, aim);
            int ans2 = dp1(arr, aim);
            int ans3 = dp2(arr, aim);
            int ans4 = mydp(arr, aim);
            int ans5 = dpTest(arr, aim);
            if (ans1 != ans2 || ans1 != ans3 || ans1 != ans4 || ans1 != ans5) {
                System.out.println("Oops!");
                printArray(arr);
                System.out.println(aim);
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                System.out.println(ans4);
                System.out.println(ans5);
                break;
            }
        }
        System.out.println("测试结束");
    }


    public static int dpTest(int[] arr, int aim) {
        if (arr == null || arr.length == 0) {
            return 0;
        }


        HashMap<Integer, Integer> count = new HashMap<>();
        for (int num : arr) {
            if (!count.containsKey(num)) {
                count.put(num, 1);
            } else {
                count.put(num, count.get(num) + 1);
            }
        }
        int[] values = new int[count.size()];
        int[] nums = new int[count.size()];
        int i = 0;
        for (Integer value : count.keySet()) {
            values[i] = value;
            nums[i] = count.get(value);
            i++;
        }

        int len = values.length;
        int[][] dp = new int[len + 1][aim + 1];
        dp[len][0] = 1;

        for (i = len - 1; i >= 0; i--) {
            for (int j = 0; j <= aim; j++) {
                int ans = 0;
                for (int k = 0; k <= nums[i] && j - values[i] * k >= 0; k++) {
                    ans += dp[i + 1][j - values[i] * k];
                }
                // TODO: 【错误点】 千万不能忘！！！
                dp[i][j] = ans;
            }
        }

        return dp[0][aim];
    }
}
