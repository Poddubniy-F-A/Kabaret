package Initializers;

import Extensions.Node;

public class ConstantInitializer implements Initializer {
    private final double h0;

    public ConstantInitializer(double h0) {
        this.h0 = h0;
    }

    @Override
    public Node[][] init(double[] gridX, double[] gridY) {
        Node[][] res = new Node[gridY.length][gridX.length];

        for (int i = 0; i < gridY.length; i++) {
            for (int j = 0; j < gridX.length; j++) {
                res[i][j] = new Node(h0, 0.1, 0.5);
            }
        }

        return res;
    }
}
