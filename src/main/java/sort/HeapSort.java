package sort;

/***
 * 1. 堆本身非常重要，在外排序-K路归并的优化/TopK门槛堆（小根堆求top大，大根堆求top小） 问题中非常重要。
 * 2. 系统内部实现的堆 PriorityQueue：
 *     （1）默认小根堆。
 *     （2）可以通过Comparator 定义排序规则
 * 3. 在堆排序中，可以通过自下而上建堆来优化建堆时间N*logN -> N。但是后续的排序过程都是需要N*logN:
 *  （1）仅考虑堆排时，只需要实现heapify,heapInsert以及swap方法即可。
 *  （2）如果要自己实现一个堆结构，那么基于heapify和heapInsert实现push pop size isFull等功能
 *
 *  4. 记牢公式：
 *  父x， 左孩子: 2x+1, 右孩子: 2x + 2  （下标 0 , 1, 2）
 *  孩子节点y（无论左右）， 父: (y - 1) / 2 (依然下标0, 1, 2)
 */
public class HeapSort {

    /**
     * 升序构建大根堆。
     * heapInsert 向上调整，只需要index, 不需要heapSize。
     * 【注意】heapify 向下调整，所以必须要heapSize
     *
     * @param arr
     */
    public static void heapInsert(int[] arr, int index) {
        // (1) 只需要跟父 比大小即可。  > 父， 则一定 > 兄弟树，此时 swap(子，父)
        // (2) 关于while条件：跟父节点比较，最后一定会跑到父节点。  -1/2 = 0

        // 下面这个while条件，即便到了0 最后也是 arr[0]比较arr[0]。所以不加父节点>=0的判断
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, (index - 1) - 1);  // TODO: 这里是不是写错了 (index-1)/2
            index = (index - 1) / 2;
        }
    }


    // 向下调整，稍微复杂点
    // 如果node不是最大的值，那么要将node,leftChild,rightChild中的最大者与node交换。 然后循环下去
    public static void heapify(int[] arr, int index, int heapSize) {
        // 先确定 孩子节点最大值 -> 有无左孩子？(左孩子index < heapSize?) 有无右孩子？(右孩子index < heapSize) 比较最大值
        int left = index * 2 + 1;
        while (left < heapSize) {
            // 比较精华的是上下这两句
            int largestChild = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;

            if (arr[index] >= arr[largestChild]) {
                break;
            } else {
                swap(arr, index, largestChild);
                index = largestChild;
                left = index * 2 + 1;
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 这是只用heapify实现的 堆排序。 在构建堆阶段优化时间复杂度
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

//        // Another option: 从上往下构建堆。  O(N * logN)
//        for (int i = 0; i < arr.length; i++) {
//            heapInsert(arr, i);
//        }

        // Step1: 从下往上构建堆 O（N），  heapSize 始终是 数组的大小
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }

        int heapSize = arr.length;
        // Step2: 交换堆顶到数组尾部(大根堆) ==> heapSize-- ==>  heapify新堆顶
        while (heapSize > 0) {
            swap(arr, 0, heapSize - 1);
            heapSize--;
            heapify(arr, 0, heapSize);
        }

    }


    public static void main(String[] args) {
        System.out.println(-1 / 2);
    }


}
