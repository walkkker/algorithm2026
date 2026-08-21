package bit_calculation;

public class EvenTimesOddTimes {

    //Q1: arr中，只有一种数，出现奇数次
    public static void printOddTimesNum1(int[] arr) {
        // 思路很简单： a^a = 0, a^0 = a
        // 循环遍历一遍，全部异或
        int odd = 0;
        for (int num : arr) {
            odd ^= num;
        }
        System.out.println(odd);
    }

    // Q2: arr中，有两种数，出现奇数次
    public static void printOddTimesNum2(int[] arr) {
        /*
        思路：假设n1 n2为出现odd times的两个数字
        1. eor = n1 ^ n2 , 取eor最右侧的1 代表着 n1/n2 之间一定一个数字在该位置上是1，另一个是0
        2. 由此实现整个数组的分流 -> 两个集合 分别分开n1, n2。 每个集合内eor完就是n1,n2了
         */
        int eor = 0;
        for (int num : arr) {
            eor ^= num;       // S1: eor  =  n1 ^ n2
        }

        // S2: 取最右侧的1
        int mostRightOne = eor & (-eor);

        // S3： 分流两个集合
        int n1 = 0;    // 0 ^ a = a
        int n2 = 0;
        for (int num : arr) {
            if ((num & mostRightOne) == 0) {
                n1 ^= num;
            } else {
                n2 ^= num;
            }
        }
        System.out.println(n1 + " : " + n2);
    }

    public static void main(String[] args) {
        int a = 5;
        int b = 7;

        a = a ^ b;
        b = a ^ b;
        a = a ^ b;

        System.out.println(a);
        System.out.println(b);

        int[] arr1 = {3, 3, 2, 3, 1, 1, 1, 3, 1, 1, 1};
        printOddTimesNum1(arr1);  // 2

        int[] arr2 = {4, 3, 4, 2, 2, 2, 4, 1, 1, 1, 3, 3, 1, 1, 1, 4, 2, 2};
        printOddTimesNum2(arr2); // 2, 3
    }
}
