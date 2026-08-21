package sort;

import java.util.Arrays;

public class QuickSort {

    /***
     * 对应荷兰国旗问题 netherLandsFlag。
     *
     * 输入：给定arr, 左边界l， 右边界r。  以arr[r]为pivot。执行荷兰国旗。
     * 返回值：=pivot 区间的左右边界下标。
     *
     * 核心逻辑就是划定less, more区间边界。将i与边界前的元素进行交换，然后移动边界。
     */
    public static int[] partition(int[] arr, int l, int r) {
        int less = l - 1;   // 指代视频中的左区间，L  即<pivot的区间
        int more = r;       // 指代视频中的右区间，R  即>pivot的区间。 这里之所以不是r+1，因为arr[r]=pivot，最终会swap(arr, more, r)
        int i = l;
        int pivot = arr[r];
        while (i < more) {
            if (arr[i] < pivot) {
                swap(arr, i++, ++less);
                // 这里不要忘了移动左边界
//                less++;
            } else if (arr[i] == pivot) {
                i++;
            } else {
                swap(arr, i, --more);
//                more--;
            }
        }
        swap(arr, more, r);
        return new int[]{less + 1, more};
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void process(int[] arr, int l, int r) {
        // TODO: 这边要注意跟mergeSort（可以only l == r）不一样，必须是l>=r 。 因为equalArea有可能在最左边或最右边。
        //  导致再次递归时，子节点的 r到了 父区间的l-1（左节点）.  或者l到了 父区间的r+1（右节点）
        if (l >= r) {
            return;
        }
        int len = r - l + 1;
        int randomIndex = l + (int) (Math.random() * len);
        swap(arr, randomIndex, r);
        int[] p = partition(arr, l, r);
        process(arr, l, p[0] - 1);
        process(arr, p[1] + 1, r);
    }

    public static void quickSort1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }


    /***
     * quickSort 非递归版
     * 因为是先partition，后递归。 因此需要压栈。 可以使用系统Stack 也可以使用数组栈
     * @param args
     */
    public static void quickSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int len = arr.length;
        Op[] stack = new Op[len];
        int sp = 0; // 【重要】栈顶指针：exclusive， 同时表示size

        stack[sp++] = new Op(0, len - 1);

        while (sp != 0) {
            // 因为涉及到这么多的 l, r操作，后面建议新建新变量。不要一直使用op.l。（左神倒是使用的op.l）

            Op op = stack[--sp];  // 弹栈

            // 相当于base case
            if (op.l >= op.r) {
                continue;
            }

            swap(arr, op.l + (int) (Math.random() * (op.r - op.l + 1)), op.r);
            int[] equalArea = partition(arr, op.l, op.r);
            // 一定先右 后左 压栈 -> 这样才能先左后右弹栈
            // 左神3.0版本使用的Queue LinkedList，FIFO。那么就是 先左后右了
            stack[sp++] = new Op(equalArea[1] + 1, op.r);
            stack[sp++] = new Op(op.l, equalArea[0] - 1);

        }
    }


    public static class Op {
        int l;
        int r;

        public Op(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }


    public static void main(String[] args) {
        int[] ints = {1, 54, 7, 8, 9, 345, 7, -23, 10};
        quickSort2(ints);
        System.out.println(Arrays.toString(ints));

    }

}
