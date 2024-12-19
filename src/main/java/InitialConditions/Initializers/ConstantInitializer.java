package InitialConditions.Initializers;

import Extensions.Nodes.Node;

public class ConstantInitializer implements Initializer {
    private final double h0;

    public ConstantInitializer(double h0) {
        this.h0 = h0;
    }

    @Override
    public Node[][] getInitializedArrayBy(double[] gridX, double[] gridY) {
        int slicesNum = gridY.length, sliceLength = gridX.length;

        Node[][] res = new Node[slicesNum][sliceLength];
        for (int i = 0; i < slicesNum; i++) {
            for (int j = 0; j < sliceLength; j++) {
                res[i][j] = new Node(h0, 0, 0);
            }
        }
        return res;
    }
}
