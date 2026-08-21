package queueAndStack;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


/**
 * 链表实现栈和队列：使用双端队列实现Deque，然后使用Deque实现栈和队列
 * 1. 实现的双端队列只负责接受T类型的值。 内部包装的Node类型是不需要调用方知道的。
 *
 */
public class DoubleEndQueueToStackAndQueue {

    // TODO: 双向链表节点， 每次添加/删除 节点 都要处理 两个相邻节点的next,last指针，不要漏了！！！
    public static class Node<T> {
        public T val;
        public Node next;
        public Node last;

        public Node(T val) {
            this.val = val;
        }
    }

    public static class DoubleEndQueue<T> {
        public Node<T> head;
        public Node<T> tail;

        public void offerFirst(T value) {
            // 每次用户提供一个值，都需要包装成内部class Node
            Node<T> node = new Node<>(value);
            if (head == null) {
                head = node;
                tail = node;
            } else {
                // TODO: 错了。 双向链表呀！！！ 新加节点的指针也要加上的！！

                // 少了下面
                node.next = head;

                head.last = node;
                head = head.last;

            }
        }

        public void offerLast(T value) {
            Node<T> node = new Node(value);
            if (head == null) {
                head = tail = node;
            } else {
                node.last = tail;
                tail.next = node;
                tail = tail.next;
            }
        }

        public T pollFirst() {
            if (head == null) {
                return null;
            }

            // 分情况： 只有一个节点 / >1个节点

            Node<T> node = head;

            if (head == tail) {
                head = tail = null;
            } else {
                head = head.next;
                // 断开连接指针
                head.last = null;
                node.next = null;
            }
            return node.val;
        }

        public T pollLast() {
            if (tail == null) {
                return null;
            }
            // TODO: 第一次写的时候，下面写错了！！！  链表一定要思维连贯！！！  pollLast怎么能取Head呢？？？？？？ 肯定取tail节点呀（不论是 1node or >1nodes）
            //  Node<T> node = head;   // 值已经拿到了，接下来处理指针
            Node<T> node = tail;   // 值已经拿到了，接下来处理指针
            // 同样，检查 only 1 nodes | > 1 nodes
            if (head == tail) {
                head = tail = null;
            } else {
                tail = tail.last;
                tail.next = null;
                node.last = null;
            }
            // 处理完head tail指针
            return node.val;
        }

        public boolean isEmpty() {
            return head == null;
        }

    }

    public static class MyQueue<T> {
        DoubleEndQueue<T> deque;
        public MyQueue() {
            deque = new DoubleEndQueue<>();
        }

        public void offer(T value) {
            deque.offerLast(value);
        }

        public T poll() {
            return deque.pollFirst();
        }

        public boolean isEmpty() {
            return deque.isEmpty();
        }
    }




    public static class MyStack<T> {
        public DoubleEndQueue<T> deque;
        public MyStack() {
            deque = new DoubleEndQueue<>();
        }

        public void push(T value) {
            deque.offerFirst(value);
        }

        public T pop()  {
            return deque.pollFirst();
        }

        public boolean isEmpty() {
            return deque.isEmpty();
        }
    }



    public static boolean isEqual(Integer o1, Integer o2) {
        if (o1 == null && o2 != null) {
            return false;
        }
        if (o1 != null && o2 == null) {
            return false;
        }
        if (o1 == null && o2 == null) {
            return true;
        }
        return o1.equals(o2);
    }

    public static void main(String[] args) {
        int oneTestDataNum = 100;
        int value = 10000;
        int testTimes = 100000;
        for (int i = 0; i < testTimes; i++) {
            MyStack<Integer> myStack = new MyStack<>();
            MyQueue<Integer> myQueue = new MyQueue<>();
            Stack<Integer> stack = new Stack<>();
            Queue<Integer> queue = new LinkedList<>();
            for (int j = 0; j < oneTestDataNum; j++) {
                int nums = (int) (Math.random() * value);
                if (stack.isEmpty()) {
                    myStack.push(nums);
                    stack.push(nums);
                } else {
                    if (Math.random() < 0.5) {
                        myStack.push(nums);
                        stack.push(nums);
                    } else {
                        if (!isEqual(myStack.pop(), stack.pop())) {
                            System.out.println("oops!");
                        }
                    }
                }
                int numq = (int) (Math.random() * value);
                if (queue.isEmpty()) {
                    myQueue.offer(numq);
                    queue.offer(numq);
                } else {
                    if (Math.random() < 0.5) {
                        myQueue.offer(numq);
                        queue.offer(numq);
                    } else {
                        if (!isEqual(myQueue.poll(), queue.poll())) {
                            System.out.println("oops!");
                        }
                    }
                }
            }
        }
        System.out.println("finish!");
    }


}
