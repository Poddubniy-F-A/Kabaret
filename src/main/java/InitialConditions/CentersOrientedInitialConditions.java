package InitialConditions;

import Extensions.Nodes.Node;
import Extensions.Nodes.Nodes;
import InitialConditions.Initializers.Initializer;

import static Extensions.GridsFunctions.getMidGridFrom;
import static Extensions.GridsFunctions.getUniformGridBy;
import static Extensions.Nodes.Node.getAverage;

public class CentersOrientedInitialConditions implements InitialConditions {
    private final Nodes nodesC, nodesX, nodesY;

    public CentersOrientedInitialConditions(double length, double stepX, double height, double stepY, Initializer initializer) {
        double[]
                gridX = getUniformGridBy((int) (length / stepX + 1), stepX), gridXMid = getMidGridFrom(gridX),
                gridY = getUniformGridBy((int) (height / stepY + 1), stepY), gridYMid = getMidGridFrom(gridY);

        Node[][] nodesArrayC = initializer.getInitializedArrayBy(gridXMid, gridYMid);
        nodesC = new Nodes(gridXMid, gridYMid, nodesArrayC);

        int slicesNumX = gridYMid.length, sliceLengthX = gridX.length;
        Node[][] nodesArrayX = new Node[slicesNumX][sliceLengthX];
        for (int i = 0; i < slicesNumX; i++) {
            for (int j = 0; j < sliceLengthX; j++) {
                nodesArrayX[i][j] = getAverage(
                        nodesArrayC[i][Math.max(j - 1, 0)],
                        nodesArrayC[i][Math.min(j, sliceLengthX - 2)]
                );
            }
        }
        nodesX = new Nodes(gridX, gridYMid, nodesArrayX);

        int slicesNumY = gridY.length, sliceLengthY = gridXMid.length;
        Node[][] nodesArrayY = new Node[slicesNumY][sliceLengthY];
        for (int i = 0; i < slicesNumY; i++) {
            for (int j = 0; j < sliceLengthY; j++) {
                nodesArrayY[i][j] = getAverage(
                        nodesArrayC[Math.max(i - 1, 0)][j],
                        nodesArrayC[Math.min(i, slicesNumY - 2)][j]
                );
            }
        }
        nodesY = new Nodes(gridXMid, gridY, nodesArrayY);
    }

    @Override
    public Nodes getNodesC() {
        return nodesC;
    }

    @Override
    public Nodes getNodesX() {
        return nodesX;
    }

    @Override
    public Nodes getNodesY() {
        return nodesY;
    }
}
