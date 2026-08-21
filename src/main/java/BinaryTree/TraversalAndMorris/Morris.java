package BinaryTree.TraversalAndMorris;

/**
 * Morris就两个变量: Node cur.  Node mostRight
 *
 * 每到一个节点，检查左树最右节点：
 *         mostRight = cur.left
 *         if (mostRight == null)  cur = cur.right
 *         if (mostRight != null):
 *                  while (mostRight != null && mostRight != cur)
 *                      if (mostRight == null) 说明第一次到达cur，右指针指向cur + cur=cur.left
 *                      if (mostRight == cur)  说明第二次到达cur,恢复右指针=null + cur=cur.right
 *
 * 知识点：
 *   （1）有左树的节点会到达2次，没有左树的节点只会到达1次
 *   （2）cur节点 -> Morris遍历完左树后 -> 就会再次回到cur节点 -> cur右数
 *          也是基于此，完成了 先后中序遍历
 *
 * 先序后序中序：
 *  先序：第一次到达节点打印
 *  中序：第二次到达节点打印
 *  后序：(1) 处理时机在第二次回到节点时， 操作为 【逆序打印左树右边界】  （需要实现 链表反转）
 *       (2) 最后还要逆序打印整棵树的右边界 【错误漏了，千万别忘了！！！】
        // TODO：【大错误！！！】千万注意 第二次回到节点时， printEdge的位置。 左树右边界，所以必须1）先清空最右节点的右指针 2)然后再printEdge 反转链表并打印。 不然都跑哪去了=-=
        // TODO：简单记：一定要清理右指针之后，再逆序打印
 *
 *
 */
public class Morris {


    public static class Node {
        int val;
        Node left;
        Node right;

        public Node(int v) {
            val = v;
        }
    }

    public static void classicMorris(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    mostRight.right = null;
                    cur = cur.right;
                }
            } else {
                cur = cur.right;
            }
        }
    }


    // 节点第一次出现
    public static void morrisPre(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    System.out.println(cur.val);
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    mostRight.right = null;
                    cur = cur.right;
                }
            } else {
                System.out.println(cur.val);
                cur = cur.right;
            }
        }
    }

    // 左头右    第二次到达时打印
    public static void morrisIn(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    System.out.println(cur.val);
                    mostRight.right = null;
                    cur = cur.right;
                }
            } else {
                System.out.println(cur.val);
                cur = cur.right;
            }
        }
    }

    // （1）只有第二次到达时处理，-> 逆序打印左树右边界
    // TODO：【错误漏了】千万别忘了：(2) 最后还要逆序打印整棵树的右边界
    // TODO：【大错误！！！】千万注意 第二次回到节点是， printEdge的位置。 左树右边界，所以必须1）先清空最右节点的右指针 2)然后再printEdge 反转链表并打印。 不然都跑哪去了=-=
    public static void morrisPos(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    // TODO: 下面这里大错！！！ 必须先清空 左树最右节点的右指针，然后才能 逆序打印左树右边界！！！ 必然打印到哪去了=-=
                    //  printEdge(cur.left);    // 因为是第二次回来，所以一定有左树（aka. 左子节点）
                    //  mostRight.right = null;

                    mostRight.right = null;
                    // TODO: 上一行 和 下一行 位置锁死。 必须先清指针，再逆序打印
                    printEdge(cur.left);
                    cur = cur.right;
                }
            } else {
                cur = cur.right;
            }
        }
        printEdge(head);
    }


    public static void printEdge(Node head) {
        // S1: 反转链表，从而可以 O（1）空间复杂度 实现逆序打印
        head = reverse(head);
        // S2: 链表前进，打印节点value
        Node cur = head;
        while (cur != null) {
            System.out.println(cur.val);
            cur = cur.right;
        }
        // S3: 千万别忘了 反转回去
        reverse(head);
    }

    public static Node reverse(Node head) {
        Node pre = null;
        Node cur = head;
        while (cur != null) {
            Node next = cur.right;
            cur.right = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);

        morrisPre(head);
        System.out.println("========");
        morrisIn(head);
        System.out.println("========");
        morrisPos(head);
        System.out.println("========");

    }


}
