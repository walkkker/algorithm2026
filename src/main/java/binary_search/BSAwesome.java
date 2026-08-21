package binary_search;

import java.util.Arrays;

/**
 * 不排序的情况：给定一个数组，任一相邻的元素不相等，局部最小问题
 */
public class BSAwesome {

    public static int partialLeast(int[] arr) {
        if (arr == null || arr.length < 2) {
            return -1;
        }

        int len = arr.length;

        // Step1: 先检查首尾节点。 也可以合并到while里面，但是那样的话while里面if检查太多了
        if (arr[0] < arr[1]) {
            return 0;
        }

        if (arr[len - 1] < arr[len - 2]) {
            return len - 1;
        }

        // Step2: 首尾节点都不是的话，势必中间存在谷底   因为左侧趋势向下，右侧趋势向下
//        int l = 0;
//        int r = len - 1;
        int l = 1;
        int r = len - 2;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            // 【狗屁！！！】所有的二分，mid都有可能跑到最左或者最右！！！
            // 当初错误言论，报outOfIndex:-1了。 『这边这样写是因为，因为中间一定存在局部最小，所以mid不会跑到左右边界，所以不存在数组越界行为』
            // 错误示例：[24, -66, 46, -12, 23, 38, 69, 6, -45, -8, 3] 倒数二轮l=0,r=1，此时mid=0，越界报错。
//            if (arr[mid] < arr[mid - 1] && arr[mid] < arr[mid + 1]) {
//                return mid;
//            } else if (arr[mid] > arr[mid - 1]) { // 则左区间必然存在局部最小
//                r = mid - 1;
//            } else {  // arr[mid] > arr[mid + 1] 此时右区间必然存在局部最小
//                l = mid + 1;
//            }
            // TODO: 具体解法看错题本，看左神的解法。
            //  二分法会遍历l,r范围内的所有元素。因为先检查了左右边界不是，那么此时l,r范围可以收缩到1,len-2。
            //  因为只剩中间元素需要检查了，而且可以统一使用一套if检查逻辑


            // 【Solution】即便要添加边缘检查。也不要合在下面的逻辑里（很乱），就是说不要加n-1>=0这种检查，而是分类讨论 mid的位置
            // 【Solution】可以单独检查 mid是否在位于0或者len-1。 否则的话，位置都处于中间，此时均可以使用下面的逻辑。
            // 【Solution】这样分类讨论很清晰，并且保证不会越界。
//            if (mid == 0) {
//                l = mid + 1;
//            } else if (mid == len - 1) {
//                r = mid - 1;
//            } else {
            if (arr[mid] < arr[mid - 1] && arr[mid] < arr[mid + 1]) {
                return mid;
            } else if (arr[mid] > arr[mid - 1]) { // 则左区间必然存在局部最小
                r = mid - 1;
            } else {  // arr[mid] > arr[mid + 1] 此时右区间必然存在局部最小
                l = mid + 1;
            }
//            }
        }
        return -1;
    }


    // 验证得到的结果，是不是局部最小
    public static boolean isRight(int[] arr, int index) {
        if (arr.length <= 1) {
            return true;
        }
        if (index == 0) {
            return arr[index] < arr[index + 1];
        }
        if (index == arr.length - 1) {
            return arr[index] < arr[index - 1];
        }
        return arr[index] < arr[index - 1] && arr[index] < arr[index + 1];
    }

    // 为了测试
    // 生成相邻不相等的数组
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) (Math.random() * maxSize) + 1];
        arr[0] = (int) (Math.random() * maxValue) - (int) (Math.random() * maxValue);
        for (int i = 1; i < arr.length; i++) {
            do {
                arr[i] = (int) (Math.random() * maxValue) - (int) (Math.random() * maxValue);
            } while (arr[i] == arr[i - 1]);
        }
        return arr;
    }

    // 为了测试
    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 30;
        int maxValue = 100;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
//            System.out.println(Arrays.toString(arr));
            int ans = partialLeastTest(arr);
//            System.out.println(ans);
            if (!isRight(arr, ans)) {
                System.out.println("出错了！");
                break;
            }
        }
        System.out.println("测试结束");
    }

    public static int partialLeastTest(int[] arr) {
        if (arr.length <= 1) {
            return -1;
        }

        if (arr[0] < arr[1]) {
            return 0;
        }
        if (arr[arr.length - 1] < arr[arr.length - 2]) {
            return arr.length - 1;
        }
        // TODO: 最终的可能性答案，只有可能在这个区间了。  不然的话，while循环会很难写，需要考虑边界index，不能直接检查左右节点（越界问题）
        // TODO： 【注意】这个代码的核心在于 前半部分，检查左右边界，以及设置[l,r]区间
        int l = 1;
        int r = arr.length - 2;

        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] < arr[mid - 1] && arr[mid] < arr[mid + 1]) {
                return mid;
            } else if (arr[mid] > arr[mid + 1]){
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return -1;
    }


}
