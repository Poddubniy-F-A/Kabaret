package InitialConditions.Initializers;

import Extensions.Nodes.Node;

import static Extensions.Constants.*;

public class VortexInitializer implements Initializer {
    private final double x0, y0, r0, h0;

    public VortexInitializer(double x0, double y0, double r0, double h0) {
        this.x0 = x0;
        this.y0 = y0;
        this.r0 = r0;
        this.h0 = h0;
    }

    @Override
    public Node[][] getInitializedArrayBy(double[] gridX, double[] gridY) {
        int slicesNum = gridY.length, sliceLength = gridX.length;

        Node[][] res = new Node[slicesNum][sliceLength];
        for (int i = 0; i < slicesNum; i++) {
            for (int j = 0; j < sliceLength; j++) {
                res[i][j] = new Node(
                        h(gridX[j], gridY[i]),
                        u(gridX[j], gridY[i]),
                        v(gridX[j], gridY[i])
                );
            }
        }
        return res;
    }

    private double h(double x, double y) {
        return h0 - Math.pow(ALPHA, 2) * Math.exp(2 * BETA * (1 - Math.pow((r(x, y) / r0), 2))) / (4 * G * BETA);
    }

    private double u(double x, double y) {
        return ALPHA * Math.exp(BETA * (1 - Math.pow((r(x, y) / r0), 2))) * (y - y0) / r0;
    }

    private double v(double x, double y) {
        return -ALPHA * Math.exp(BETA * (1 - Math.pow((r(x, y) / r0), 2))) * (x - x0) / r0;
    }

    private double r(double x, double y) {
        return Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2));
    }
}
