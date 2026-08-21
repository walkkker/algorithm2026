## Points
1. 核心：nexts数组构建。 每一个元素该元素**之前**的**最长** 【前缀后缀】匹配长度。
2. 时间复杂度，是构建了两个变量，包含了while条件包含的所有变量 同时二者覆盖了 所有if-else分支的数值改变（每个if-else都有导致 其一 发生改变）。 例如，kmp: x && x-y.   nexts: i && i-cn

## 错题本
1. [TreeEqual.java](TreeEqual.java) 很多错：包括大思路错误以及小代码错误！！！
   - 本题总结：
     - 可以扩展kmp，把char[]替换成Integer[]，代码99%都没变
     - 【唯一注意点+致命错误点】1. 二叉树使用前序遍历才可以 包含子树 （中序和层级不行）
     - 【超级无敌注意！】2. 对象数组 必须自己构造isEqual(考虑null的问题) + Obj.equals(obj2)或者Integer.compare/String.compare方法 -> 绝对不能使用==方法
       - 注意！绝对不能使用 == 比较对象，即便是 Integer也不行！！！
       - 原因：Integer举例 -128 ~ 127有缓存，超出比如Integer a=128, 导致 Integer[] arr中对象使用==，值一样，但是返回false！！！
