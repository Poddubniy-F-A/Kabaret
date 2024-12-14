package Initializers;

import Extensions.Node;
import Extensions.Fillers.Filler;
import Extensions.Nodes;

import static Extensions.Node.getAverage;

public class CentersOrientedInitializer implements Initializer {
    private final Filler filler;

    public CentersOrientedInitializer(Filler filler) {
        this.filler = filler;
    }

    @Override
    public void init(Nodes nodesC, Nodes nodesX, Nodes nodesY) {
        nodesC.setNodesArray(filler);

        int slicesNumX = nodesX.getSizeY(), sliceLengthX = nodesX.getSizeX();
        Node[][] nodesXArray = new Node[slicesNumX][sliceLengthX];
        for (int i = 0; i < slicesNumX; i++) {
            for (int j = 0; j < sliceLengthX; j++) {
                nodesXArray[i][j] = getAverage(
                        nodesC.getNode(i, Math.max(j - 1, 0)),
                        nodesC.getNode(i, Math.min(j, sliceLengthX - 2))
                );
            }
        }
        nodesX.setNodesArray(nodesXArray);

        int slicesNumY = nodesY.getSizeY(), sliceLengthY = nodesY.getSizeX();
        Node[][] nodesYArray = new Node[slicesNumY][sliceLengthY];
        for (int i = 0; i < slicesNumY; i++) {
            for (int j = 0; j < sliceLengthY; j++) {
                nodesYArray[i][j] = getAverage(
                        nodesC.getNode(Math.max(i - 1, 0), j),
                        nodesC.getNode(Math.min(i, slicesNumY - 2), j)
                );
            }
        }
        nodesY.setNodesArray(nodesYArray);
    }
}
