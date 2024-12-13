package Initializers.Fillers;

import Extensions.Node;

public class ConstantFiller implements Filler {
    private final double h0;

    public ConstantFiller(double h0) {
        this.h0 = h0;
    }

    @Override
    public Node[][] getFilledArrayBy(double[] gridX, double[] gridY) {
        int slicesNum = gridY.length, sliceLength = gridX.length;

        Node[][] res = new Node[slicesNum][sliceLength];
        for (int i = 0; i < gridY.length; i++) {
            for (int j = 0; j < gridX.length; j++) {
                res[i][j] = new Node(h0, 0.1, 0.5);
            }
        }
        return res;
    }
}
