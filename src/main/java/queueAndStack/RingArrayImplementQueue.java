package queueAndStack;

/**
 * 1. 在环形数组实现queue中，pushi 新添加元素 ++，popi弹出元素 ++。 popi始终追逐pushi 实现FIFO
 * 2. 关键点：范围区间表示太过麻烦，因为队列满和队列空时，两个指针分别是互相追逐的状态，临界位置很难判定（因为如果pushi=0,popi=-1初始状态），你会发现满和空，这两个指针位置都一样，需要靠上一个追逐动作（谁追谁）来确认满还是空，很麻烦。
 *      所以，我们说过，没有什么是不能通过加一个变量解决的：加一个size -> 直接解决该问题
 */
public class RingArrayImplementQueue {

    public static class Queue {
        private int[] arr;
        private int pushi;
        private int polli;

        private int size;
        private int limit;

        public Queue(int limit) {
            arr = new int[limit];
            limit = limit;
            size = 0;
            // TODO: 我觉得这两个值是最讲究的。 因为有了size变量，所以pushi和popi不需要考虑边界表示的问题
            //  ，可以直接代表成 pushi -> 新添元素的位置； polli -> 弹出元素的位置
            pushi = 0;   // 放入的位置
            polli = 0;   // 弹出的位置
        }

        public int nextIndex(int i) {
            return i == limit - 1 ? 0 : i + 1;
        }

        public void push(int val) {
            if (size < limit) {
                // TODO: 每个变量都要考虑到，漏了size
                size++;
                arr[pushi] = val;
                pushi = nextIndex(pushi);
            } else {
                throw new RuntimeException("the queue is full");
            }
        }

        public int poll() {
            if (size == 0) {
                throw new RuntimeException("The queue is empty!");
            }
            size--;
            int ans = arr[polli];
            polli = nextIndex(polli);
            return ans;
        }

        public boolean isEmpty() {
            return size == 0;
        }


    }
}
