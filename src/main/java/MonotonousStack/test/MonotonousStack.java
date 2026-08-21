package MonotonousStack.test;
import java.util.*;
public class MonotonousStack {


    public static int[][] getNearLessNoRepeat(int[] arr) {
        int len = arr.length;
        int[][] ans = new int[len][2];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[i] < arr[stack.peek()]) {
                int popI = stack.pop();
                int l = stack.isEmpty() ? -1 : stack.peek();
                int r = i;
                ans[popI][0] = l;
                ans[popI][1] = r;
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int popI = stack.pop();
            int l = stack.isEmpty() ? -1 : stack.peek();
            int r = len;
            ans[popI][0] = l;
            ans[popI][1] = r;
        }
        return ans;
    }


    // 核心就是 Stack<List<Integer>> 相同的元素放在 stack的一层，使用List装
    public static int[][] getNearLessWithRepeat(int[] arr) {
        int len = arr.length;
        int[][] ans = new int[len][2];
        Stack<List<Integer>> stack = new Stack<>();
        for (int i = 0; i < len; i++)  {
            while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]) {
                List<Integer> popList = stack.pop();
                int l = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
                int r = i;
                for (int popI : popList) {
                    ans[popI][0] = l;
                    ans[popI][1] = r;
                }
            }

            if (stack.isEmpty() || arr[stack.peek().get(0)] != arr[i]) {
                stack.push(new LinkedList<>());
            }
            List<Integer> peekList = stack.peek();
            peekList.add(i);
        }
        while (!stack.isEmpty()) {
            List<Integer> popList = stack.pop();
            int l = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
            int r = len;
            for (int popI : popList) {
                ans[popI][0] = l;
                ans[popI][1] = r;
            }
        }
        return ans;
    }



    // for test
    public static int[] getRandomArrayNoRepeat(int size) {
        int[] arr = new int[(int) (Math.random() * size) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        for (int i = 0; i < arr.length; i++) {
            int swapIndex = (int) (Math.random() * arr.length);
            int tmp = arr[swapIndex];
            arr[swapIndex] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }

    // for test
    public static int[] getRandomArray(int size, int max) {
        int[] arr = new int[(int) (Math.random() * size) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
        }
        return arr;
    }

    // for test
    public static int[][] rightWay(int[] arr) {
        int[][] res = new int[arr.length][2];
        for (int i = 0; i < arr.length; i++) {
            int leftLessIndex = -1;
//            int rightLessIndex = -1;
            // 这里我改了左神的值，我把右侧不存在 nearLess的情况，对应的值 变成了 arr.length
            int rightLessIndex = arr.length;
            int cur = i - 1;
            while (cur >= 0) {
                if (arr[cur] < arr[i]) {
                    leftLessIndex = cur;
                    break;
                }
                cur--;
            }
            cur = i + 1;
            while (cur < arr.length) {
                if (arr[cur] < arr[i]) {
                    rightLessIndex = cur;
                    break;
                }
                cur++;
            }
            res[i][0] = leftLessIndex;
            res[i][1] = rightLessIndex;
        }
        return res;
    }

    // for test
    public static boolean isEqual(int[][] res1, int[][] res2) {
        if (res1.length != res2.length) {
            return false;
        }
        for (int i = 0; i < res1.length; i++) {
            if (res1[i][0] != res2[i][0] || res1[i][1] != res2[i][1]) {
                return false;
            }
        }

        return true;
    }

    // for test
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int size = 10;
        int max = 20;
        int testTimes = 2000000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = getRandomArrayNoRepeat(size);
            int[] arr2 = getRandomArray(size, max);
            if (!isEqual(getNearLessNoRepeat(arr1), rightWay(arr1))) {
                System.out.println("Oops!");
                printArray(arr1);
                break;
            }
            if (!isEqual(getNearLessWithRepeat(arr2), rightWay(arr2))) {
                System.out.println("Oops!");
                printArray(arr2);
                break;
            }
        }
        System.out.println("测试结束");
    }

}
