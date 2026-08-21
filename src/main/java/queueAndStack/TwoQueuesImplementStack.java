package queueAndStack;

import java.util.LinkedList;
import java.util.Queue;

/**
 * https://leetcode.cn/problems/implement-stack-using-queues/
 * 跟双栈实现队列不一样，那个是两个栈互相倒数据，可以同时存在数据。
 * <p>
 * 这个其实只有queue存数据。
 * 具体是每次 queue存数据，help用来在弹出时（短暂）存n-1的数据。
 */
public class TwoQueuesImplementStack {

    class MyStack {

        Queue<Integer> queue;
        Queue<Integer> help;

        public MyStack() {
            queue = new LinkedList<>();
            help = new LinkedList<>();
        }

        public void push(int x) {
            queue.offer(x);
        }

        public int pop() {
            if (queue.isEmpty()) {
                throw new RuntimeException("The stack is Empty!");
            }
            while (queue.size() > 1) {
                 help.offer(queue.poll());
            }
            int ans = queue.poll();
            swapQueueAndHelp();
            return ans;
        }

        public int top() {
            if (queue.isEmpty()) {
                throw new RuntimeException("The stack is Empty!");
            }
            while (queue.size() > 1) {
                help.offer(queue.poll());
            }
            int ans = queue.peek();
            help.offer(queue.poll());
            swapQueueAndHelp();
            return ans;
        }

        public boolean empty() {
            return queue.isEmpty();
        }


        public void swapQueueAndHelp() {
            Queue<Integer> tmp = queue;
            queue = help;
            help = tmp;
        }
    }

}
