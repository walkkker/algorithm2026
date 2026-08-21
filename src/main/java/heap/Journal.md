## 重点

## 大纲
1. [最大线段重合问题](src/main/java/heap/CoverMax.java)：题意见代码
2. [堆的实现](src/main/java/heap/HeapImplementation.java)：弹入，弹出，上浮heapInsert，下沉heapify
3. [加强堆实现](src/main/java/heap/HeapGreater.java)：
    - 系统提供堆，缺少**反向索引表**（哈希表实现）。 这使得 即使我们更改了对象A的值，然后堆进行上浮下沉调整时，需要遍历数组才能找到A的位置，这个时间就需要O(N)。说白了，系统堆无法快速找到指定对象的位置。 而添加反向索引表HashMap后，O(1)找元素 + O(logN)调整元素 实现快速调整
    - 允许对堆内元素的值进行修改，加强堆会对修改后的元素进行调整，维持其大/小根堆的组织。其调整的时间复杂度仅为 O(logN)，上浮或下沉操作的时间复杂度
    - 你定义一个比较器，注意：Comparator.comp(o1, o2) < 0 比较器本身就决定了 谁o1要排在前面。 而排在前面就代表着 要靠近堆顶，也就是父的位置。 
      - 说人话就是， 只关心comp(o1, o2) < 0 就可以了， ** < 0时  o1 顺序要优于 o2** aka. 理解为我们说的要往上走/comp<0时，则o1大
4. [双堆与滑动窗口中位数](dualheap/DualHeapNotes.md)：
    - 普通双堆：动态插入并查询中位数
    - 滑动窗口双堆：逻辑大小、延迟删除、prune 与 balance
    - 与 SBT、Treap、树状数组的适用场景对比



## 错题本 
