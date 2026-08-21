package kmp;

/**
 *
 * 非常重要！！！ 对应题目 TreeEqual
 *
 * 基于kmp构造 Integer[] 替代 char[]
 * 元素比较依然不能使用==：
 * （1）Integer.compare无法处理 null，需要自己实现方法
 * （2）Integer -128~127 才有缓存，能够使用==返回正确值。 超出范围时，比较的是对象本身，全部返回false！！！
 *
 *
 */
public class TestForIntegerGreaterThan127 {

    public static void main(String[] args) {
        Integer a = 128;
        Integer b = 128;
        System.out.println(a == b);   // false

        a = 127;
        b = 127;
        System.out.println(a == b);  // true

        int c = 129;
        int d = 129;
        System.out.println(c == d); // true
    }
}
