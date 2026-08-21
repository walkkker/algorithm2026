package MonotonousStack;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 注意点：
 * (1) 单调栈：不管是 classic 还是 做题，单调栈 不存 相同值的坐标（aka. 标准单调栈里面的值都是不同的），一定涉及到 linkedList存 或者 弹出
 * (2) 有重复值求最近小于的下标时，使用链表，注意弹出元素后，如果stack.peek()有值，要取 （list.size - 1）的位置，不能取0的位置。
 */
public class MonotonousStack {

    // Method1: 这里是指 arr 无重复元素
    public static int[][] getNearLessNoRepeat(int[] arr) {
        int[][] res = new int[arr.length][2];
        // stack里面存的是 下标
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            // S1： 每一个元素进来时，先往外弹
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                int popI = stack.pop();
                int leftNearLess = stack.isEmpty() ? -1 : stack.peek();
                res[popI][0] = leftNearLess;
                res[popI][1] = i;
            }
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            int popI = stack.pop();
            int leftNearLess = stack.isEmpty() ? -1 : stack.peek();
            res[popI][0] = leftNearLess;
            res[popI][1] = arr.length;
        }

        return res;
    }


    // Method2: 输入参数int[] arr存在重复值，栈里放链表（相同值 放入同一个链表）
    //  注意，不管是 第一阶段的加入元素时弹出， 还是第二阶段的统一弹出。 每个弹出链表内的所有元素，leftNearLess 和 rightNearLess 的值都是相同的
    // TODO: 跟左神稍微有点不一样，左神这里使用的是 ArrayList，我使用的是LinkedList。 都可以，代码完全一样！
    public static int[][] getNearLessWithRepeat(int[] arr) {
        int[][] res = new int[arr.length][2];
        Stack<LinkedList<Integer>> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]) {
                LinkedList<Integer> list = stack.pop();
                // TODO: 这里特别重要，一定取的是 list.get(size - 1)的元素，而不能随意取 list.get(0) 的元素
                int leftNearLess = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
                for (int popI : list) {
                    res[popI][0] = leftNearLess;
                    res[popI][1] = i;
                }
            }

            // S2: 放入当前下标
            // 发现if else下，插入链表的动作只绑定一个场景，剩下都是Push new list
            // 于是，if(唯一场景) -> 只有栈不为空，且peek list的元素 与 arr[i]相同，arr[i]才直接插入链表。
            // 否则，都是新建一个链表，然后Push stack（这里同时包含了两个场景：1) stack is empty 2) peek ele != arr[i]）
            if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
                stack.peek().add(i);
            } else {
                LinkedList<Integer> list = new LinkedList<>();
                list.add(i);
                stack.push(list);
            }
        }

        while (!stack.isEmpty()) {
            LinkedList<Integer> list = stack.pop();
            // TODO: 这里也是要注意的点， 每次 取leftNearLess时，都要注意必须取 链表的最后一个元素
            int leftNearLess = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
//          这个也可以： int leftNearLess = stack.isEmpty() ? -1 : stack.peek().peekLast();
            for (int popI : list) {
                res[popI][0] = leftNearLess;
                res[popI][1] = arr.length;
            }
        }
        return res;
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
