## 重点

1. 二叉树的问题：大多数都涉及到递归。少部分类似于后继结点/层级遍历 不涉及递归。
    1. 这样的原因很简单：二叉树不像数组一样，一开始就知道大小。只能在遍历的过程中不断往下访问新节点。
    2. 因为他是层层套着，所以需要递归解决
2. 二叉树 分层:
    1. 基于层级遍历
    2. 两个变量 Node curEnd, Node nextNode
3. 完全二叉 VS. 满二叉树。
    1. 堆是完全二叉树。
    2. 满二叉树是每一层节点都是满的。 完全二叉树是指除了最后一层，其余层都是满的；最后一层节点尽可能靠左，这就是完全二叉。
4. 关于二叉树序列化与反序列化： https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
```
    // TODO: 【重点】 s.split("") 
    // 1. 参数必须使用双引号，因为参数要求是String，不能用单引号，对应char
    // 2. String.split(String regex)默认会丢弃末尾的空字符串。
    // 3. 反序列化时，需要识别"null"与数字，注意比较时不要使用【==】，而是要使用 "null".equals(xxx)
    // 4. 尤其在 levelSe&&Deser时，特别注意 root==null的情况，代码开头要做好edge case。 返回"null", deser时，if(string.equals("null")) return null
```
5. 二叉树的递归套路：【BST相关的题，Info包含max,min的】， 【process函数里 if (cur==null) return null】。  然后分类讨论，因为你无法给min max赋值。
   1. 然后一般涉及判断 当前Node cur是否是BST,那当然条件是【 l.isBST && r.isBST && cur.val > l.max && cur.val < r.min】，那么当前cur为头 也是一颗BST！

### 【重点】二叉树递归套路 - 不要怕写很多if else。这个玩法就是这样的

1. 三要素：
    1) class Info;
    2) Info process(Node root)递归函数;
    3) 主函数，用来调用递归函数
2. 考虑方向：
   1) 核心是确认最终目标是谁？ 如何结合左右信息得到最终目标？ （这个过程中涉及到的所有变量都进入Info，但是写递归逻辑时，只关心最终目标的 分类讨论。 其余变量一定好求）
   2) 以下思路帮助分类讨论以得到最终目标：
       1) 经过当前节点 + 不经过当前节点 （lowestAncestor最为经典，一定要看！！！  在【经过】的分支下，还包含了非常多的子分支，特别容易漏！！！）
       2) 简单的左右信息整合 （一定要考虑所有情况）
3. 模板注意点：
   1. base case = null时，如果Info不好构建，比如包含max,min，那么 if (base case) , return null。
   2. 续1：此时， 主体部分要分四种情况讨论，即当前node!=null时， 1）左空右空，2）左不空右空，3）左空右不空，4）左右都不空
4. 续2.1。 你会发现，只需要关注最终目标 【有值/true】 的 if情况去分析。 剩下的变量可以全部放在后面统一处理，一定好处理（当然像跟BST相关的题目，也可以跟 目标变量的if-else合到一起）。
5. 也支持多叉树的树形DP问题，可参见 **maxhappy**
   1. 对于多叉树而言，就是收集每个孩子的Info

### 以下是废弃信息。 实践证明，统一使用 左神的模板就行了。
3. BaseCase设定准则：（提前对4.的总结）
   1. BaseCase=null时可以构建Info，则走null模板。
   2. 否则，走BaseCase=head模板 （head必可以构建Info）
4. 【重要】base case的设置问题：到底是停在null 还是停在head (head.left == null && head.right == null); （看IsBST 和 IsCBT的代码）
    1) 原则: base case的设置： 能null则null；null无法构建Info，则head
    2) 选择是null还是head的标准： 本质是为了减少代码中判断null。 (参加IsCBT的代码，两种方法都有，代码量区别很大，而且代码多的head
       第一次写错了)
        1) 取决于null的 Info 是否能构建。 好构建就null；否则的话就head； =》 这是为了什么呢？好写代码，省略一些判断。
        2) 因为如果baseCase==null，return null的话。 那不如 baseCase=无子head，此时return new Info(可以构建)。
    3) 【注意！！！】 如果是base case == head的话：
        1) 首先， 左递归 右递归不能直接写。 需要if(head.left != null)检查，才能执行递归。 这是因为base
           case没有包含null，只停在无子node。 所以递归参数不能传null，会NPE。
        2) 其次代码上，要做大三类的分类讨论（这是比BaseCase=null要多写的）： 左null右不null， 左不null右null，左右都不null。
        3) 而且你要注意，想清楚所有情形（参见IsCBT的代码，左不null右null情况下 还要分类讨论）
    4) IsBST的代码 就是 base case = head的实现。 证明很好用。逻辑很清晰。 head 有 head 的公式代码，记住就行。
    5)  都是围绕着，怎么少判断nul，怎么来。 防止绕晕了（其实也不会）