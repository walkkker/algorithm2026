package heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 实现加强堆：
 * 实现以下基本方法：isEmpty(),size(),contains(),peek(),push(),pop(),remove(),
 * 以及重点reassign, getAllElements, heapInsert, heapify, swap
 * 实现的主要优点是支持：
 * （1）对象类型的加强堆。
 * （2）重点：支持用户对对象进行值修改后，通过调用reassign 能够维持堆的结构。
 */
public class HeapGreater<T> {

    // 必要的四个成员变量：ArrayList代替int[]，HashMap用作反向索引，int heapSize用作堆大小，Comparator比较器 实现两个对象之间的比较
    // 实现数据结构时，都是使用private
    private ArrayList<T> heap;
    private HashMap<T, Integer> indexMap;

    private int heapSize;
    private Comparator<? super T> comp;

    public HeapGreater(Comparator<? super T> comp) {
        heap = new ArrayList<>();
        indexMap = new HashMap<>();
        heapSize = 0;
        this.comp = comp;
    }

    // 【重要】swap方法要扩展，因为交换两个对象时， indexMap也要同时更新
    private void swap(int i, int j) {
        T o1 = heap.get(i);
        T o2 = heap.get(j);
        heap.set(i, o2);
        heap.set(j, o1);
        indexMap.put(o1, j);
        indexMap.put(o2, i);
    }

    private void heapInsert(int index) {
        while (comp.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {
            swap(index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    private void heapify(int index) {
        int left = index * 2 + 1;
        while (left < heapSize) {
            int largestChild = left + 1 < heapSize && comp.compare(heap.get(left + 1), heap.get(left)) < 0 ? left + 1 : left;
            if (comp.compare(heap.get(index), heap.get(largestChild)) <= 0) {
                break;
            } else {
                swap(index, largestChild);
                index = largestChild;
                left = index * 2 + 1;
            }
        }
    }

    public boolean contains(T obj) {
        return indexMap.containsKey(obj);
    }

    // 下面实现的是 对外方法： pop, push, remove, reassign
    public void resign(T obj) {
        int index = indexMap.get(obj);
        heapInsert(index);
        heapify(index);
    }

    public void push(T obj) {
        int pos = heapSize;
        heap.add(obj);
        indexMap.put(obj, pos);
        heapSize++;
        heapInsert(pos);
    }

    public T pop() {
        T ans = heap.get(0);
        swap(0, heapSize - 1);
        // TODO: 弹出节点不要忘了删除 反向索引表
        //   一定是swap之后再删除indexMap（因为swap还要用）。
        indexMap.remove(ans);
        heap.remove(heapSize - 1);
        heapSize--;
        heapify(0);
        return ans;
    }


    // remove也不复杂，跟pop逻辑基本是一样的。
    // 只不过多一个 index < newHeapSize的判断。 因为边界情况下，不能heapInsert！！！（同时不需要heapify） 而pop只有Heapify。
    public void remove(T obj) {
        if (contains(obj)) {
            // 这里要防止 删除对象处于最后一个位置；在这种情况下，heapInsert会包含该对象，导致逻辑错误
            int index = indexMap.get(obj);

            // Step1: 不管Index在哪个位置，都先跟最后一个元素交换。 然后删除最后一个元素，然后 heapSize--
            swap(index, heapSize - 1);
            heap.remove(heapSize - 1);
            heapSize--;
            indexMap.remove(obj);

            // Step2: 检查index<heapSize ， 这一步是要判断index本来是否是最后一个元素。 因为只有index<new_heapSize的话，才需要上浮或者下移。 不然如果本来就是最后一个元素的话，不应该上浮操作的，结果你调用HeapInsert，会导致逻辑出错的
            if (index < heapSize) {
                heapInsert(index);
                heapify(index);
            }

//            // 这里要判断是否是最后一个元素，如果是最后一个元素的话，不能做heapInsert+heapify。 不然的话 HeapInsert会导致逻辑出错的
//            if (index == heapSize - 1) {
//                heap.remove(heapSize - 1);
//                heapSize--;
//            } else {
//                swap(index, heapSize - 1);
//                heap.remove(heapSize - 1);
//                heapSize--;
//                heapInsert(index);
//                heapify(index);
//            }
//            indexMap.remove(obj);
        }
    }

    public boolean isEmpty() {
        return heapSize == 0;
    }


    public static void main(String[] args) {

        ArrayList<Integer> list = new ArrayList<>(10);
        for (int i = 0; i < 12; i++) {
            list.add(5);
        }
        System.out.println(list.size());
    }


}
