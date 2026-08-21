# Hot 100 Package Guides Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为 `frequence` 中约119道唯一LeetCode题目生成按现有package组织的系统化逐题详解，并建立可自动校验的总索引。

**Architecture:** 每个现有算法package拥有一份专题Markdown，每道题只在Java文件所在package完整讲解，其他专题通过相对链接引用。根索引维护主线/扩展标记、文件位置和复习顺序；校验脚本从Java文件名和Markdown标题提取题号，检查缺失、重复和失效链接。

**Tech Stack:** Markdown、Java 8代码示例、POSIX shell/Ripgrep覆盖检查、Maven验证。

---

### Task 1: 建立题目清单与归属规则

**Files:**
- Create: `src/main/java/frequence/Hot100逐题详解索引.md`
- Reference: `src/main/java/frequence/**/*.java`

- [ ] 扫描全部以`Q数字`开头的Java文件，并按题号去重。
- [ ] 将官方Hot 100题目标记为`【Hot 100】`，其余标记为`【扩展】`。
- [ ] 对Q2、Q46、Q146、Q460等重复题号指定唯一主文件和版本对比文件。
- [ ] 在根索引中记录题号、题名、专题文档和Java文件链接。

验证命令：

```bash
rg --files src/main/java/frequence -g 'Q*.java' | sort -V
```

预期：所有候选文件可被扫描，重复题号有明确归并规则。

### Task 2: 编写哈希、数组、矩阵与双指针专题

**Files:**
- Create: `src/main/java/frequence/哈希专题逐题详解.md`
- Create: `src/main/java/frequence/array/Hot100数组逐题详解.md`
- Create: `src/main/java/frequence/matrix/Hot100矩阵逐题详解.md`
- Create: `src/main/java/frequence/双指针/Hot100双指针逐题详解.md`
- Create: `src/main/java/frequence/双指针/SlidingWindow/Hot100滑动窗口逐题详解.md`
- Create: `src/main/java/frequence/双指针/同向读写双指针_原地稳定压缩/Hot100原地稳定压缩逐题详解.md`

- [ ] 每篇开头写本专题核心模型、主线题、扩展题和推荐顺序。
- [ ] 每题写核心思想、流程、正确性依据、标准Java代码、易错点、复杂度、记忆模板和项目链接。
- [ ] 将项目现有TODO错误注释归纳到易错点。

### Task 3: 编写链表、缓存与树专题

**Files:**
- Create: `src/main/java/frequence/LinkedList/Hot100链表逐题详解.md`
- Create: `src/main/java/frequence/cache/Hot100缓存设计逐题详解.md`
- Create: `src/main/java/frequence/BinaryTree/Hot100二叉树逐题详解.md`

- [ ] 链表题突出断链、重连、哨兵和指针不变量。
- [ ] 缓存题对比LRU与LFU，并引用错误版本。
- [ ] 二叉树题先定义递归返回值，再写组合逻辑，引用递归套路错误总结。

### Task 4: 编写图论、回溯、二分、栈、堆和贪心专题

**Files:**
- Create: `src/main/java/frequence/Graph/Hot100图论逐题详解.md`
- Create: `src/main/java/frequence/Backtracking/Hot100回溯逐题详解.md`
- Create: `src/main/java/frequence/BinarySearch/Hot100二分查找逐题详解.md`
- Create: `src/main/java/frequence/Stack/Hot100栈逐题详解.md`
- Create: `src/main/java/frequence/Heap/Hot100堆逐题详解.md`
- Create: `src/main/java/frequence/Greedy/Hot100贪心逐题详解.md`

- [ ] 图论明确DFS、BFS、拓扑序和Trie的选择边界。
- [ ] 回溯明确递归树、选择列表、路径、终止条件和恢复现场。
- [ ] 二分明确搜索空间、排除依据和边界语义。
- [ ] 栈/堆/贪心明确数据结构不变量和局部决策成立条件。

### Task 5: 编写动态规划与专题扩展

**Files:**
- Create: `src/main/java/frequence/dp/Hot100动态规划逐题详解.md`
- Create: `src/main/java/frequence/dp/multidimensional/Hot100多维动态规划逐题详解.md`
- Create: `src/main/java/frequence/dp/stock/Hot100股票专题逐题详解.md`
- Create: `src/main/java/frequence/permutation/Hot100排列专题逐题详解.md`
- Create: `src/main/java/frequence/skill/Hot100技巧逐题详解.md`
- Create: `src/main/java/frequence/substringandsubsequence/Hot100子串子序列逐题详解.md`

- [ ] 每道DP题先写状态定义和初始暴力/二维模型，再写空间优化。
- [ ] 股票题统一使用状态机语义，说明状态和转移边。
- [ ] 排列、技巧、子序列专题建立与主线题的交叉索引，不复制完整题解。

### Task 6: 覆盖与链接验证

**Files:**
- Create: `src/main/java/frequence/Hot100GuideCoverageCheck.java`
- Modify: `src/main/java/frequence/Hot100逐题详解索引.md`

- [ ] 从Java文件名生成唯一题号集合。
- [ ] 从专题Markdown三级标题生成已讲解题号集合。
- [ ] 对缺失题号、重复完整讲解和不存在的相对链接抛出错误。
- [ ] 把最终主线/扩展数量写回总索引。

验证命令：

```bash
mvn -q -DskipTests compile
java -cp target/classes frequence.Hot100GuideCoverageCheck
mvn -q test
```

预期：编译成功、覆盖检查通过、既有测试通过。

### Task 7: 最终一致性审查

- [ ] 检查所有题目都包含`【Hot 100】`或`【扩展】`。
- [ ] 检查每题都包含复杂度、易错点、记忆模板和Java文件链接。
- [ ] 检查代码块使用Java 8兼容语法。
- [ ] 检查根索引能够导航到全部专题文档。
- [ ] 运行完整验证并记录结果。
