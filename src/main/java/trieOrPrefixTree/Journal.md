## 重点
1. 【该点也是错误点！！！】insert/erase方法下，初始化Node cur = root时，也需要对 cur.pass进行++或者--。aka. root节点也需要及时的记录pass这个属性。（insert时添加，erase时减少）维持整个trie的一致性。（即每个节点记录pass（经过）和end（结尾，空字符串是root.end++）的数量）
   1. 之所以root很重要，因为涉及到 "" - 空字符串的添加和删除以及startingWith。