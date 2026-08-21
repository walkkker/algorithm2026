# LeetCode 热题 100 与面试经典 150 题单对比

> 数据获取日期：2026-08-06。比较依据为力扣中国官方
> [LeetCode 热题 100](https://leetcode.cn/studyplan/top-100-liked/)和
> [面试经典 150 题](https://leetcode.cn/studyplan/top-interview-150/)当前公开题单。
> 题目以 LeetCode 前台题号作为集合键，避免中英文标题或翻译变化造成误判。

## 一、结论

| 集合 | 数量 | 难度分布 |
|---|---:|---|
| 两份题单重合 | 67 | 简单13 / 中等47 / 困难7 |
| 面试经典 150 独有 | 83 | 简单26 / 中等46 / 困难11 |
| LeetCode 热题 100 独有 | 33 | 简单7 / 中等21 / 困难5 |
| 两份题单合并去重 | 183 | - |

集合关系：

```text
面试经典150 = 67道重合题 + 83道独有题
热题100      = 67道重合题 + 33道独有题
两份题单并集 = 67 + 83 + 33 = 183道
```

如果已经按热题 100 学习过，不需要从面试 150 的第一题重新开始。最直接的查漏补缺策略是：

1. 优先完成本文第二部分的 **83 道面试 150 独有题**。
2. 第三部分的67道重合题只复查曾经写错、没有形成模板或长时间未复习的题。
3. 第四部分的33道热题100独有题不会出现在面试150中，但它们仍是热题100的重要补充，不能因为切换题单而遗忘。
4. 两份题单全部覆盖后，实际完成的是183道去重题，而不是250次互不相同的练习。

## 二、面试经典 150 独有题：接下来真正需要补的83题

> 下面按照面试经典150的官方专题顺序排列，并提供复选框。完成一题后可以直接将`[ ]`改为`[x]`。

### 数组 / 字符串（17题）

- [ ] [88. 合并两个有序数组](https://leetcode.cn/problems/merge-sorted-array/)（简单）
- [ ] [27. 移除元素](https://leetcode.cn/problems/remove-element/)（简单）
- [ ] [26. 删除有序数组中的重复项](https://leetcode.cn/problems/remove-duplicates-from-sorted-array/)（简单）
- [ ] [80. 删除有序数组中的重复项 II](https://leetcode.cn/problems/remove-duplicates-from-sorted-array-ii/)（中等）
- [ ] [122. 买卖股票的最佳时机 II](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/)（中等）
- [ ] [274. H 指数](https://leetcode.cn/problems/h-index/)（中等）
- [ ] [380. O(1) 时间插入、删除和获取随机元素](https://leetcode.cn/problems/insert-delete-getrandom-o1/)（中等）
- [ ] [134. 加油站](https://leetcode.cn/problems/gas-station/)（中等）
- [ ] [135. 分发糖果](https://leetcode.cn/problems/candy/)（困难）
- [ ] [13. 罗马数字转整数](https://leetcode.cn/problems/roman-to-integer/)（简单）
- [ ] [12. 整数转罗马数字](https://leetcode.cn/problems/integer-to-roman/)（中等）
- [ ] [58. 最后一个单词的长度](https://leetcode.cn/problems/length-of-last-word/)（简单）
- [ ] [14. 最长公共前缀](https://leetcode.cn/problems/longest-common-prefix/)（简单）
- [ ] [151. 反转字符串中的单词](https://leetcode.cn/problems/reverse-words-in-a-string/)（中等）
- [ ] [6. Z 字形变换](https://leetcode.cn/problems/zigzag-conversion/)（中等）
- [ ] [28. 找出字符串中第一个匹配项的下标](https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/)（简单）
- [ ] [68. 文本左右对齐](https://leetcode.cn/problems/text-justification/)（困难）

### 双指针（3题）

- [ ] [125. 验证回文串](https://leetcode.cn/problems/valid-palindrome/)（简单）
- [ ] [392. 判断子序列](https://leetcode.cn/problems/is-subsequence/)（简单）
- [ ] [167. 两数之和 II - 输入有序数组](https://leetcode.cn/problems/two-sum-ii-input-array-is-sorted/)（中等）

### 滑动窗口（2题）

- [ ] [209. 长度最小的子数组](https://leetcode.cn/problems/minimum-size-subarray-sum/)（中等）
- [ ] [30. 串联所有单词的子串](https://leetcode.cn/problems/substring-with-concatenation-of-all-words/)（困难）

### 矩阵（2题）

- [ ] [36. 有效的数独](https://leetcode.cn/problems/valid-sudoku/)（中等）
- [ ] [289. 生命游戏](https://leetcode.cn/problems/game-of-life/)（中等）

### 哈希表（6题）

- [ ] [383. 赎金信](https://leetcode.cn/problems/ransom-note/)（简单）
- [ ] [205. 同构字符串](https://leetcode.cn/problems/isomorphic-strings/)（简单）
- [ ] [290. 单词规律](https://leetcode.cn/problems/word-pattern/)（简单）
- [ ] [242. 有效的字母异位词](https://leetcode.cn/problems/valid-anagram/)（简单）
- [ ] [202. 快乐数](https://leetcode.cn/problems/happy-number/)（简单）
- [ ] [219. 存在重复元素 II](https://leetcode.cn/problems/contains-duplicate-ii/)（简单）

### 区间（3题）

- [ ] [228. 汇总区间](https://leetcode.cn/problems/summary-ranges/)（简单）
- [ ] [57. 插入区间](https://leetcode.cn/problems/insert-interval/)（中等）
- [ ] [452. 用最少数量的箭引爆气球](https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/)（中等）

### 栈（3题）

- [ ] [71. 简化路径](https://leetcode.cn/problems/simplify-path/)（中等）
- [ ] [150. 逆波兰表达式求值](https://leetcode.cn/problems/evaluate-reverse-polish-notation/)（中等）
- [ ] [224. 基本计算器](https://leetcode.cn/problems/basic-calculator/)（困难）

### 链表（4题）

- [ ] [92. 反转链表 II](https://leetcode.cn/problems/reverse-linked-list-ii/)（中等）
- [ ] [82. 删除排序链表中的重复元素 II](https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/)（中等）
- [ ] [61. 旋转链表](https://leetcode.cn/problems/rotate-list/)（中等）
- [ ] [86. 分隔链表](https://leetcode.cn/problems/partition-list/)（中等）

### 二叉树（7题）

- [ ] [100. 相同的树](https://leetcode.cn/problems/same-tree/)（简单）
- [ ] [106. 从中序与后序遍历序列构造二叉树](https://leetcode.cn/problems/construct-binary-tree-from-inorder-and-postorder-traversal/)（中等）
- [ ] [117. 填充每个节点的下一个右侧节点指针 II](https://leetcode.cn/problems/populating-next-right-pointers-in-each-node-ii/)（中等）
- [ ] [112. 路径总和](https://leetcode.cn/problems/path-sum/)（简单）
- [ ] [129. 求根节点到叶节点数字之和](https://leetcode.cn/problems/sum-root-to-leaf-numbers/)（中等）
- [ ] [173. 二叉搜索树迭代器](https://leetcode.cn/problems/binary-search-tree-iterator/)（中等）
- [ ] [222. 完全二叉树的节点个数](https://leetcode.cn/problems/count-complete-tree-nodes/)（中等）

### 二叉树层次遍历（2题）

- [ ] [637. 二叉树的层平均值](https://leetcode.cn/problems/average-of-levels-in-binary-tree/)（简单）
- [ ] [103. 二叉树的锯齿形层序遍历](https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/)（中等）

### 二叉搜索树（1题）

- [ ] [530. 二叉搜索树的最小绝对差](https://leetcode.cn/problems/minimum-absolute-difference-in-bst/)（简单）

### 图（4题）

- [ ] [130. 被围绕的区域](https://leetcode.cn/problems/surrounded-regions/)（中等）
- [ ] [133. 克隆图](https://leetcode.cn/problems/clone-graph/)（中等）
- [ ] [399. 除法求值](https://leetcode.cn/problems/evaluate-division/)（中等）
- [ ] [210. 课程表 II](https://leetcode.cn/problems/course-schedule-ii/)（中等）

### 图的广度优先搜索（3题）

- [ ] [909. 蛇梯棋](https://leetcode.cn/problems/snakes-and-ladders/)（中等）
- [ ] [433. 最小基因变化](https://leetcode.cn/problems/minimum-genetic-mutation/)（中等）
- [ ] [127. 单词接龙](https://leetcode.cn/problems/word-ladder/)（困难）

### 字典树（2题）

- [ ] [211. 添加与搜索单词 - 数据结构设计](https://leetcode.cn/problems/design-add-and-search-words-data-structure/)（中等）
- [ ] [212. 单词搜索 II](https://leetcode.cn/problems/word-search-ii/)（困难）

### 回溯（2题）

- [ ] [77. 组合](https://leetcode.cn/problems/combinations/)（中等）
- [ ] [52. N 皇后 II](https://leetcode.cn/problems/n-queens-ii/)（困难）

### 分治（1题）

- [ ] [427. 建立四叉树](https://leetcode.cn/problems/construct-quad-tree/)（中等）

### Kadane 算法（1题）

- [ ] [918. 环形子数组的最大和](https://leetcode.cn/problems/maximum-sum-circular-subarray/)（中等）

### 二分查找（1题）

- [ ] [162. 寻找峰值](https://leetcode.cn/problems/find-peak-element/)（中等）

### 堆（2题）

- [ ] [502. IPO](https://leetcode.cn/problems/ipo/)（困难）
- [ ] [373. 查找和最小的 K 对数字](https://leetcode.cn/problems/find-k-pairs-with-smallest-sums/)（中等）

### 位运算（5题）

- [ ] [67. 二进制求和](https://leetcode.cn/problems/add-binary/)（简单）
- [ ] [190. 颠倒二进制位](https://leetcode.cn/problems/reverse-bits/)（简单）
- [ ] [191. 位1的个数](https://leetcode.cn/problems/number-of-1-bits/)（简单）
- [ ] [137. 只出现一次的数字 II](https://leetcode.cn/problems/single-number-ii/)（中等）
- [ ] [201. 数字范围按位与](https://leetcode.cn/problems/bitwise-and-of-numbers-range/)（中等）

### 数学（6题）

- [ ] [9. 回文数](https://leetcode.cn/problems/palindrome-number/)（简单）
- [ ] [66. 加一](https://leetcode.cn/problems/plus-one/)（简单）
- [ ] [172. 阶乘后的零](https://leetcode.cn/problems/factorial-trailing-zeroes/)（中等）
- [ ] [69. x 的平方根 ](https://leetcode.cn/problems/sqrtx/)（简单）
- [ ] [50. Pow(x, n)](https://leetcode.cn/problems/powx-n/)（中等）
- [ ] [149. 直线上最多的点数](https://leetcode.cn/problems/max-points-on-a-line/)（困难）

### 多维动态规划（6题）

- [ ] [120. 三角形最小路径和](https://leetcode.cn/problems/triangle/)（中等）
- [ ] [63. 不同路径 II](https://leetcode.cn/problems/unique-paths-ii/)（中等）
- [ ] [97. 交错字符串](https://leetcode.cn/problems/interleaving-string/)（中等）
- [ ] [123. 买卖股票的最佳时机 III](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/)（困难）
- [ ] [188. 买卖股票的最佳时机 IV](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/)（困难）
- [ ] [221. 最大正方形](https://leetcode.cn/problems/maximal-square/)（中等）

## 三、两份题单重合的67题

> 这些题已经出现在热题100中。若之前已经真正独立完成，不需要因为进入面试150而重复从头做。

### 数组 / 字符串（7题）

- [169. 多数元素](https://leetcode.cn/problems/majority-element/)（简单）
- [189. 轮转数组](https://leetcode.cn/problems/rotate-array/)（中等）
- [121. 买卖股票的最佳时机](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/)（简单）
- [55. 跳跃游戏](https://leetcode.cn/problems/jump-game/)（中等）
- [45. 跳跃游戏 II](https://leetcode.cn/problems/jump-game-ii/)（中等）
- [238. 除了自身以外数组的乘积](https://leetcode.cn/problems/product-of-array-except-self/)（中等）
- [42. 接雨水](https://leetcode.cn/problems/trapping-rain-water/)（困难）

### 双指针（2题）

- [11. 盛最多水的容器](https://leetcode.cn/problems/container-with-most-water/)（中等）
- [15. 三数之和](https://leetcode.cn/problems/3sum/)（中等）

### 滑动窗口（2题）

- [3. 无重复字符的最长子串](https://leetcode.cn/problems/longest-substring-without-repeating-characters/)（中等）
- [76. 最小覆盖子串](https://leetcode.cn/problems/minimum-window-substring/)（困难）

### 矩阵（3题）

- [54. 螺旋矩阵](https://leetcode.cn/problems/spiral-matrix/)（中等）
- [48. 旋转图像](https://leetcode.cn/problems/rotate-image/)（中等）
- [73. 矩阵置零](https://leetcode.cn/problems/set-matrix-zeroes/)（中等）

### 哈希表（3题）

- [49. 字母异位词分组](https://leetcode.cn/problems/group-anagrams/)（中等）
- [1. 两数之和](https://leetcode.cn/problems/two-sum/)（简单）
- [128. 最长连续序列](https://leetcode.cn/problems/longest-consecutive-sequence/)（中等）

### 区间（1题）

- [56. 合并区间](https://leetcode.cn/problems/merge-intervals/)（中等）

### 栈（2题）

- [20. 有效的括号](https://leetcode.cn/problems/valid-parentheses/)（简单）
- [155. 最小栈](https://leetcode.cn/problems/min-stack/)（中等）

### 链表（7题）

- [141. 环形链表](https://leetcode.cn/problems/linked-list-cycle/)（简单）
- [2. 两数相加](https://leetcode.cn/problems/add-two-numbers/)（中等）
- [21. 合并两个有序链表](https://leetcode.cn/problems/merge-two-sorted-lists/)（简单）
- [138. 随机链表的复制](https://leetcode.cn/problems/copy-list-with-random-pointer/)（中等）
- [25. K 个一组翻转链表](https://leetcode.cn/problems/reverse-nodes-in-k-group/)（困难）
- [19. 删除链表的倒数第 N 个结点](https://leetcode.cn/problems/remove-nth-node-from-end-of-list/)（中等）
- [146. LRU 缓存](https://leetcode.cn/problems/lru-cache/)（中等）

### 二叉树（7题）

- [104. 二叉树的最大深度](https://leetcode.cn/problems/maximum-depth-of-binary-tree/)（简单）
- [226. 翻转二叉树](https://leetcode.cn/problems/invert-binary-tree/)（简单）
- [101. 对称二叉树](https://leetcode.cn/problems/symmetric-tree/)（简单）
- [105. 从前序与中序遍历序列构造二叉树](https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)（中等）
- [114. 二叉树展开为链表](https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/)（中等）
- [124. 二叉树中的最大路径和](https://leetcode.cn/problems/binary-tree-maximum-path-sum/)（困难）
- [236. 二叉树的最近公共祖先](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/)（中等）

### 二叉树层次遍历（2题）

- [199. 二叉树的右视图](https://leetcode.cn/problems/binary-tree-right-side-view/)（中等）
- [102. 二叉树的层序遍历](https://leetcode.cn/problems/binary-tree-level-order-traversal/)（中等）

### 二叉搜索树（2题）

- [230. 二叉搜索树中第 K 小的元素](https://leetcode.cn/problems/kth-smallest-element-in-a-bst/)（中等）
- [98. 验证二叉搜索树](https://leetcode.cn/problems/validate-binary-search-tree/)（中等）

### 图（2题）

- [200. 岛屿数量](https://leetcode.cn/problems/number-of-islands/)（中等）
- [207. 课程表](https://leetcode.cn/problems/course-schedule/)（中等）

### 字典树（1题）

- [208. 实现 Trie (前缀树)](https://leetcode.cn/problems/implement-trie-prefix-tree/)（中等）

### 回溯（5题）

- [17. 电话号码的字母组合](https://leetcode.cn/problems/letter-combinations-of-a-phone-number/)（中等）
- [46. 全排列](https://leetcode.cn/problems/permutations/)（中等）
- [39. 组合总和](https://leetcode.cn/problems/combination-sum/)（中等）
- [22. 括号生成](https://leetcode.cn/problems/generate-parentheses/)（中等）
- [79. 单词搜索](https://leetcode.cn/problems/word-search/)（中等）

### 分治（3题）

- [108. 将有序数组转换为二叉搜索树](https://leetcode.cn/problems/convert-sorted-array-to-binary-search-tree/)（简单）
- [148. 排序链表](https://leetcode.cn/problems/sort-list/)（中等）
- [23. 合并 K 个升序链表](https://leetcode.cn/problems/merge-k-sorted-lists/)（困难）

### Kadane 算法（1题）

- [53. 最大子数组和](https://leetcode.cn/problems/maximum-subarray/)（中等）

### 二分查找（6题）

- [35. 搜索插入位置](https://leetcode.cn/problems/search-insert-position/)（简单）
- [74. 搜索二维矩阵](https://leetcode.cn/problems/search-a-2d-matrix/)（中等）
- [33. 搜索旋转排序数组](https://leetcode.cn/problems/search-in-rotated-sorted-array/)（中等）
- [34. 在排序数组中查找元素的第一个和最后一个位置](https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/)（中等）
- [153. 寻找旋转排序数组中的最小值](https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/)（中等）
- [4. 寻找两个正序数组的中位数](https://leetcode.cn/problems/median-of-two-sorted-arrays/)（困难）

### 堆（2题）

- [215. 数组中的第K个最大元素](https://leetcode.cn/problems/kth-largest-element-in-an-array/)（中等）
- [295. 数据流的中位数](https://leetcode.cn/problems/find-median-from-data-stream/)（困难）

### 位运算（1题）

- [136. 只出现一次的数字](https://leetcode.cn/problems/single-number/)（简单）

### 一维动态规划（5题）

- [70. 爬楼梯](https://leetcode.cn/problems/climbing-stairs/)（简单）
- [198. 打家劫舍](https://leetcode.cn/problems/house-robber/)（中等）
- [139. 单词拆分](https://leetcode.cn/problems/word-break/)（中等）
- [322. 零钱兑换](https://leetcode.cn/problems/coin-change/)（中等）
- [300. 最长递增子序列](https://leetcode.cn/problems/longest-increasing-subsequence/)（中等）

### 多维动态规划（3题）

- [64. 最小路径和](https://leetcode.cn/problems/minimum-path-sum/)（中等）
- [5. 最长回文子串](https://leetcode.cn/problems/longest-palindromic-substring/)（中等）
- [72. 编辑距离](https://leetcode.cn/problems/edit-distance/)（中等）

## 四、LeetCode 热题 100 独有的33题

> 这些题不属于面试经典150。它们不是下一阶段的新增任务，但用于解释为什么两份题单不是简单的包含关系。

### 双指针（1题）

- [283. 移动零](https://leetcode.cn/problems/move-zeroes/)（简单）

### 滑动窗口（1题）

- [438. 找到字符串中所有字母异位词](https://leetcode.cn/problems/find-all-anagrams-in-a-string/)（中等）

### 子串（2题）

- [560. 和为 K 的子数组](https://leetcode.cn/problems/subarray-sum-equals-k/)（中等）
- [239. 滑动窗口最大值](https://leetcode.cn/problems/sliding-window-maximum/)（困难）

### 普通数组（1题）

- [41. 缺失的第一个正数](https://leetcode.cn/problems/first-missing-positive/)（困难）

### 矩阵（1题）

- [240. 搜索二维矩阵 II](https://leetcode.cn/problems/search-a-2d-matrix-ii/)（中等）

### 链表（5题）

- [160. 相交链表](https://leetcode.cn/problems/intersection-of-two-linked-lists/)（简单）
- [206. 反转链表](https://leetcode.cn/problems/reverse-linked-list/)（简单）
- [234. 回文链表](https://leetcode.cn/problems/palindrome-linked-list/)（简单）
- [142. 环形链表 II](https://leetcode.cn/problems/linked-list-cycle-ii/)（中等）
- [24. 两两交换链表中的节点](https://leetcode.cn/problems/swap-nodes-in-pairs/)（中等）

### 二叉树（3题）

- [94. 二叉树的中序遍历](https://leetcode.cn/problems/binary-tree-inorder-traversal/)（简单）
- [543. 二叉树的直径](https://leetcode.cn/problems/diameter-of-binary-tree/)（简单）
- [437. 路径总和 III](https://leetcode.cn/problems/path-sum-iii/)（中等）

### 图论（1题）

- [994. 腐烂的橘子](https://leetcode.cn/problems/rotting-oranges/)（中等）

### 回溯（3题）

- [78. 子集](https://leetcode.cn/problems/subsets/)（中等）
- [131. 分割回文串](https://leetcode.cn/problems/palindrome-partitioning/)（中等）
- [51. N 皇后](https://leetcode.cn/problems/n-queens/)（困难）

### 栈（3题）

- [394. 字符串解码](https://leetcode.cn/problems/decode-string/)（中等）
- [739. 每日温度](https://leetcode.cn/problems/daily-temperatures/)（中等）
- [84. 柱状图中最大的矩形](https://leetcode.cn/problems/largest-rectangle-in-histogram/)（困难）

### 堆（1题）

- [347. 前 K 个高频元素](https://leetcode.cn/problems/top-k-frequent-elements/)（中等）

### 贪心算法（1题）

- [763. 划分字母区间](https://leetcode.cn/problems/partition-labels/)（中等）

### 动态规划（5题）

- [118. 杨辉三角](https://leetcode.cn/problems/pascals-triangle/)（简单）
- [279. 完全平方数](https://leetcode.cn/problems/perfect-squares/)（中等）
- [152. 乘积最大子数组](https://leetcode.cn/problems/maximum-product-subarray/)（中等）
- [416. 分割等和子集](https://leetcode.cn/problems/partition-equal-subset-sum/)（中等）
- [32. 最长有效括号](https://leetcode.cn/problems/longest-valid-parentheses/)（困难）

### 多维动态规划（2题）

- [62. 不同路径](https://leetcode.cn/problems/unique-paths/)（中等）
- [1143. 最长公共子序列](https://leetcode.cn/problems/longest-common-subsequence/)（中等）

### 技巧（3题）

- [75. 颜色分类](https://leetcode.cn/problems/sort-colors/)（中等）
- [31. 下一个排列](https://leetcode.cn/problems/next-permutation/)（中等）
- [287. 寻找重复数](https://leetcode.cn/problems/find-the-duplicate-number/)（中等）

## 五、面试150独有题中的11道困难题

这11题建议放在对应专题的基础题完成后再做，不要为了按题单顺序而过早消耗时间。
这里仅作困难题索引，完成状态仍以第二部分的复选框为准：

- [135. 分发糖果](https://leetcode.cn/problems/candy/)（困难）
- [68. 文本左右对齐](https://leetcode.cn/problems/text-justification/)（困难）
- [30. 串联所有单词的子串](https://leetcode.cn/problems/substring-with-concatenation-of-all-words/)（困难）
- [224. 基本计算器](https://leetcode.cn/problems/basic-calculator/)（困难）
- [127. 单词接龙](https://leetcode.cn/problems/word-ladder/)（困难）
- [212. 单词搜索 II](https://leetcode.cn/problems/word-search-ii/)（困难）
- [52. N 皇后 II](https://leetcode.cn/problems/n-queens-ii/)（困难）
- [502. IPO](https://leetcode.cn/problems/ipo/)（困难）
- [149. 直线上最多的点数](https://leetcode.cn/problems/max-points-on-a-line/)（困难）
- [123. 买卖股票的最佳时机 III](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/)（困难）
- [188. 买卖股票的最佳时机 IV](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/)（困难）

## 六、推荐的查漏补缺顺序

### 第一批：低成本补齐基础接口和边界

优先处理数组/字符串、双指针、哈希表、位运算中的简单题。这一批题主要补充工程实现完整度和边界意识，
不需要引入新的复杂数据结构。

重点包括：

```text
88 合并两个有序数组
26/80 删除有序数组重复项
125 验证回文串
242 有效的字母异位词
67 二进制求和
190/191 基础位运算
66 加一
69 x的平方根
```

### 第二批：补齐经典数据结构操作

按以下专题集中完成，不建议在不同专题之间频繁切换：

```text
链表：92、82、61、86
树：100、106、117、112、129、173、222、637、103、530
图：130、133、399、210、909、433
Trie：211、212
堆：502、373
```

### 第三批：补齐面试150新增的算法模型

```text
区间：57、452
栈与表达式：71、150、224
图BFS：127
回溯：77、52
Kadane扩展：918
多维DP：120、63、97、123、188、221
数学与几何：172、50、149
```

### 第四批：最后集中处理困难题

困难题不适合穿插在基础补题流程中。先完成同专题的简单和中等题，再回到第五部分逐题攻克，
能够减少“每道题都像新技巧”的割裂感。

## 七、集合比较方法

本次不是按标题文本比较，而是：

```text
key = questionFrontendId
intersection = ids150 ∩ ids100
only150      = ids150 - ids100
only100      = ids100 - ids150
```

两个官方题单内部都没有重复题号：

```text
面试经典150：原始150题，去重后150题
热题100：原始100题，去重后100题
```

后续如果力扣调整学习计划，应重新按官方页面数据计算；本文顶部的获取日期用于标识当前快照。
