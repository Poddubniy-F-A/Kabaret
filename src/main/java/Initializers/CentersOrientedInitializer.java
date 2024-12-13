package Initializers;

import Extensions.Node;
import Extensions.Fillers.Filler;

import static Extensions.Node.getAverage;

public class CentersOrientedInitializer implements Initializer {
    private final Node[][] nodesC, nodesX, nodesY;

    public CentersOrientedInitializer(Filler filler, double[] gridXMid, double[] gridYMid) {
        final int slicesC = gridYMid.length, sliceCLength = gridXMid.length;

        nodesC = filler.getFilledArrayBy(gridXMid, gridYMid);

        nodesX = new Node[slicesC][sliceCLength + 1];
        for (int i = 0; i < slicesC; i++) {
            for (int j = 0; j <= sliceCLength; j++) {
                nodesX[i][j] = getAverage(nodesC[i][Math.max(j - 1, 0)], nodesC[i][Math.min(j, sliceCLength - 1)]);
            }
        }

        nodesY = new Node[slicesC + 1][sliceCLength];
        for (int i = 0; i <= slicesC; i++) {
            for (int j = 0; j < sliceCLength; j++) {
                nodesY[i][j] = getAverage(nodesC[Math.max(i - 1, 0)][j], nodesC[Math.min(i, slicesC - 1)][j]);
            }
        }
    }

    @Override
    public Node[][] getInitializedNodesC() {
        return nodesC;
    }

    @Override
    public Node[][] getInitializedNodesX() {
        return nodesX;
    }

    @Override
    public Node[][] getInitializedNodesY() {
        return nodesY;
    }
}
