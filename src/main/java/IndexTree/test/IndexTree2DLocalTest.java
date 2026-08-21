package IndexTree.test;

import java.util.Random;

public class IndexTree2DLocalTest {

    public static void main(String[] args) {
        testLeetCodeExample();
        testSingleRowAndColumn();
        testRandomAgainstNaive();
        System.out.println("IndexTree2DLocalTest passed");
    }

    private static void testLeetCodeExample() {
        int[][] matrix = {
                {3, 0, 1, 4, 2},
                {5, 6, 3, 2, 1},
                {1, 2, 0, 1, 5},
                {4, 1, 0, 1, 7},
                {1, 0, 3, 0, 5}
        };
        IndexTree2D_20260710 tree = new IndexTree2D_20260710(copy(matrix));
        NaiveMatrix naive = new NaiveMatrix(copy(matrix));

        assertEquals(8, tree.regionSum(2, 1, 4, 3), "leetcode example initial sum");
        tree.update(3, 2, 2);
        naive.update(3, 2, 2);
        assertEquals(naive.sumRegion(2, 1, 4, 3), tree.regionSum(2, 1, 4, 3), "leetcode example after update");
    }

    private static void testSingleRowAndColumn() {
        int[][] singleRow = {{1, -2, 3, 4}};
        IndexTree2D_20260710 rowTree = new IndexTree2D_20260710(copy(singleRow));
        assertEquals(6, rowTree.regionSum(0, 0, 0, 3), "single row full range");
        rowTree.update(0, 1, 5);
        assertEquals(13, rowTree.regionSum(0, 0, 0, 3), "single row after update");

        int[][] singleColumn = {{1}, {-2}, {3}, {4}};
        IndexTree2D_20260710 columnTree = new IndexTree2D_20260710(copy(singleColumn));
        assertEquals(6, columnTree.regionSum(0, 0, 3, 0), "single column full range");
        columnTree.update(1, 0, 5);
        assertEquals(13, columnTree.regionSum(0, 0, 3, 0), "single column after update");
    }

    private static void testRandomAgainstNaive() {
        Random random = new Random(20260710L);
        for (int round = 0; round < 300; round++) {
            int rows = 1 + random.nextInt(12);
            int cols = 1 + random.nextInt(12);
            int[][] matrix = randomMatrix(rows, cols, random);
            IndexTree2D_20260710 tree = new IndexTree2D_20260710(copy(matrix));
            NaiveMatrix naive = new NaiveMatrix(copy(matrix));

            for (int op = 0; op < 1000; op++) {
                if (random.nextBoolean()) {
                    int r = random.nextInt(rows);
                    int c = random.nextInt(cols);
                    int val = random.nextInt(41) - 20;
                    tree.update(r, c, val);
                    naive.update(r, c, val);
                } else {
                    int r1 = random.nextInt(rows);
                    int r2 = random.nextInt(rows);
                    int c1 = random.nextInt(cols);
                    int c2 = random.nextInt(cols);
                    if (r1 > r2) {
                        int tmp = r1;
                        r1 = r2;
                        r2 = tmp;
                    }
                    if (c1 > c2) {
                        int tmp = c1;
                        c1 = c2;
                        c2 = tmp;
                    }
                    int expected = naive.sumRegion(r1, c1, r2, c2);
                    int actual = tree.regionSum(r1, c1, r2, c2);
                    assertEquals(expected, actual,
                            "random round=" + round + ", op=" + op
                                    + ", region=(" + r1 + "," + c1 + ")-(" + r2 + "," + c2 + ")");
                }
            }
        }
    }

    private static int[][] randomMatrix(int rows, int cols, Random random) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(41) - 20;
            }
        }
        return matrix;
    }

    private static int[][] copy(int[][] matrix) {
        int[][] ans = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, ans[i], 0, matrix[i].length);
        }
        return ans;
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ", expected=" + expected + ", actual=" + actual);
        }
    }

    private static class NaiveMatrix {
        private final int[][] nums;

        private NaiveMatrix(int[][] matrix) {
            nums = matrix;
        }

        private void update(int row, int col, int val) {
            nums[row][col] = val;
        }

        private int sumRegion(int row1, int col1, int row2, int col2) {
            int ans = 0;
            for (int i = row1; i <= row2; i++) {
                for (int j = col1; j <= col2; j++) {
                    ans += nums[i][j];
                }
            }
            return ans;
        }
    }
}
