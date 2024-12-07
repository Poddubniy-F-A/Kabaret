package Extensions;

public class GridsFunctions {
    public static double[] getUniformGridBy(int steps, double step) {
        double[] res = new double[steps];
        for (int i = 0; i < res.length; i++) {
            res[i] = i * step;
        }
        return res;
    }

    public static double[] getMidGridFrom(double[] grid) {
        double[] res = new double[grid.length - 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = (grid[i] + grid[i + 1]) / 2;
        }
        return res;
    }

    public static double[] getReflectedBoardsWithMiddlesOf(double[] grid) {
        int lastIndex = grid.length - 1;
        return new double[]{
                grid[0] - (grid[1] - grid[0]),
                grid[0] - (grid[1] - grid[0]) / 2,
                grid[lastIndex] + (grid[lastIndex] - grid[lastIndex - 1]) / 2,
                grid[lastIndex] + (grid[lastIndex] - grid[lastIndex - 1])
        };
    }

    public static String[] getStringGridFrom(double[] doubleGrid) {
        int gridLen = doubleGrid.length;

        String[] res = new String[gridLen];
        for (int i = 0; i < gridLen; i++) {
            res[i] = String.valueOf(doubleGrid[i]);
        }
        return res;
    }
}
