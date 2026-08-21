package bit_calculation;

public class Swap {
    /**
     * 要求 i和j必须是不同位置。  不然arr[i]会变为0.
     *
     * 因为 ^ = 无进位相加
     * @param arr
     * @param i
     * @param j
     */
    public static void swap(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    public static void main(String[] args) {
        int a = 12; // 0000 1100
        System.out.println((1<<31) | a);  // -2147483636
        System.out.println((1<<31) + a);  // 2147483636


    }
}
