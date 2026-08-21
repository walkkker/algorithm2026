package frequence.双指针.同向读写双指针_原地稳定压缩;

/**
 * LeetCode 1089：复写零。
 *
 * <p>DONE: 【已独立完成】LeetCode最新AC提交：743335550。
 *
 * <p>原地复写数组中的每个0，并将超出原数组长度的内容截断。数组长度不能改变。
 * 例如{@code [1,0,2,3,0,4,5,0]}变为{@code [1,0,0,2,3,0,0,4]}。
 *
 * <p><b>模型：输出扩张时反向读写。</b>普通元素产生一个输出，0产生两个输出。如果从左向右
 * 原地写，{@code write} 可能超过 {@code read}，从而覆盖尚未读取的数据，因此必须从后向前写。
 *
 * <p>先统计0的数量，把结果想象成长度为{@code n + zeroCount}的虚拟数组：
 * <pre>{@code
 * read  = n - 1
 * write = n + zeroCount - 1
 * }</pre>
 * 反向遍历时照常移动虚拟{@code write}，但只在{@code write < n}时写入真实数组。
 * 这样可以统一处理末尾0只复写一部分的边界情况，不需要单独分类。
 *
 * <p>核心规律：向前压缩要求{@code write <= read}；当输出可能扩张并破坏该关系时，通常改为
 * 从后向前处理。时间复杂度为 O(N)，额外空间复杂度为 O(1)。
 */
public class Q1089_DuplicateZeros {

    public void myDuplicateZeros(int[] arr) {
        int zeroCount = 0;
        for (int num : arr) {
            if (num == 0) {
                zeroCount++;
            }
        }

        int write = arr.length + zeroCount - 1;
        for (int read = arr.length - 1; read >= 0; read--) {
            if (write < arr.length) {
                arr[write] = arr[read];
            }
            write--;

            if (arr[read] == 0) {
                if (write < arr.length) {
                    arr[write] = 0;
                }
                write--;
            }
        }
    }




        public void duplicateZeros(int[] arr) {
        int zeroCount = 0;
        for (int value : arr) {
            if (value == 0) {
                zeroCount++;
            }
        }

        int read = arr.length - 1;
        int write = arr.length + zeroCount - 1;
        while (read >= 0) {
            if (write < arr.length) {
                arr[write] = arr[read];
            }

            if (arr[read] == 0) {
                write--;
                if (write < arr.length) {
                    arr[write] = 0;
                }
            }

            read--;
            write--;
        }
    }
}
