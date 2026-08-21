package heap;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 一个经典的堆结构，是用数组实现的。
 * 核心记住三个关系(用0,1,2记): leftChile=2*parent+1, rightChild=2*parent+2
 * parent = (anyChild - 1) / 2
 * <p>
 * <p>
 * (1) 堆一定是一个【完全二叉树】，但不是一定要是一个【满二叉树】
 */
public class Heap {

    // int类型 大根堆
    public static class MyMaxHeap {
        private int[] heap;

        // 这个final左神标记的好
        private final int limit;
        private int heapSize;

        public MyMaxHeap(int limit) {
            this.limit = limit;
            heap = new int[limit];
            heapSize = 0;
        }

        // 给定arr, 和 Index。 把index的数 向上看
        public void heapInsert(int[] arr, int index) {
            // 往上找，只需要跟父亲 比大小就可以了。 因为当前父亲一定大于兄弟
            // 这里可以不用管0的问题，index = 0时，  (index - 1) / 2 = 0
            while (heap[index] > heap[(index - 1) / 2]) {
                swap(heap, index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }

        // 给定 int[] arr, int index, int heapSize
        // 向下看
        public static void heapify(int[] arr, int index, int heapSize) {
            int left = index * 2 + 1;
            while (left < heapSize) {
                int largestChild = left + 1 < heapSize ? (arr[left] > arr[left + 1] ? left : left + 1) : left;
                if (arr[index] >= arr[largestChild]) {
                    break;
                } else {
                    swap(arr, index, largestChild);
                    index = largestChild;
                    left = index * 2 + 1;
                }
            }
        }

        public void push(int val) {
            if (heapSize == limit) {
                throw new RuntimeException("The heap is full!");
            }
            heap[heapSize++] = val;
            heapInsert(heap, heapSize - 1);
        }


        public int pop() {
            if (heapSize == 0) {
                throw new RuntimeException("The heap is empty!");
            }
            int ans = heap[0];
            swap(heap, 0, --heapSize);
            heapify(heap, 0, heapSize);
            return ans;
        }

        public static void swap(int[] arr, int i, int j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }

        public boolean isEmpty() {
            return heapSize == 0;
        }

        public boolean isFull() {
            return heapSize == limit;
        }

    }

    public static class RightMaxHeap {
        private int[] arr;
        private final int limit;
        private int size;

        public RightMaxHeap(int limit) {
            arr = new int[limit];
            this.limit = limit;
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == limit;
        }

        public void push(int value) {
            if (size == limit) {
                throw new RuntimeException("heap is full");
            }
            arr[size++] = value;
        }

        public int pop() {
            int maxIndex = 0;
            for (int i = 1; i < size; i++) {
                if (arr[i] > arr[maxIndex]) {
                    maxIndex = i;
                }
            }
            int ans = arr[maxIndex];
            arr[maxIndex] = arr[--size];
            return ans;
        }


    }

    public static class MyComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }

    }

    public static void main(String[] args) {

        // 默认小根堆
//        PriorityQueue<Integer> heap = new PriorityQueue<>(new MyComparator());
        // 还可以写lambda
        PriorityQueue<Integer> heap = new PriorityQueue<>((o1, o2) -> o2 - o1);
        heap.add(5);
        heap.add(5);
        heap.add(5);
        heap.add(3);
        // 5 , 3
        System.out.println(heap.peek());
        heap.add(7);
        heap.add(0);
        heap.add(7);
        heap.add(0);
        heap.add(7);
        heap.add(0);
        System.out.println(heap.peek());
        while (!heap.isEmpty()) {
            System.out.println(heap.poll());
        }

        int value = 1000;
        int limit = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            int curLimit = (int) (Math.random() * limit) + 1;
            MyMaxHeap my = new MyMaxHeap(curLimit);
            RightMaxHeap test = new RightMaxHeap(curLimit);
            int curOpTimes = (int) (Math.random() * limit);
            for (int j = 0; j < curOpTimes; j++) {
                if (my.isEmpty() != test.isEmpty()) {
                    System.out.println("Oops!");
                }
                if (my.isFull() != test.isFull()) {
                    System.out.println("Oops!");
                }
                if (my.isEmpty()) {
                    int curValue = (int) (Math.random() * value);
                    my.push(curValue);
                    test.push(curValue);
                } else if (my.isFull()) {
                    if (my.pop() != test.pop()) {
                        System.out.println("Oops!");
                    }
                } else {
                    if (Math.random() < 0.5) {
                        int curValue = (int) (Math.random() * value);
                        my.push(curValue);
                        test.push(curValue);
                    } else {
                        if (my.pop() != test.pop()) {
                            System.out.println("Oops!");
                        }
                    }
                }
            }
        }
        System.out.println("finish!");

    }


}
