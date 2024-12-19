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

    public static double[] extendedStepsArrayOf(double[] grid) {
        int lastIndex = grid.length - 1;

        double[] extendedGrid = new double[grid.length + 2];
        extendedGrid[0] = grid[0] - (grid[1] - grid[0]);
        System.arraycopy(grid, 0, extendedGrid, 1, grid.length);
        extendedGrid[extendedGrid.length - 1] = grid[lastIndex] + (grid[lastIndex] - grid[lastIndex - 1]);
        return getStepsArrayOf(extendedGrid);
    }

    public static double[] getStepsArrayOf(double[] grid) {
        double[] res = new double[grid.length - 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = grid[i + 1] - grid[i];
        }
        return res;
    }
}
