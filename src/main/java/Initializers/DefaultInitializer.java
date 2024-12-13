package Initializers;

import Extensions.Node;
import Extensions.Fillers.Filler;

public class DefaultInitializer implements Initializer {
    private final Node[][] nodesC, nodesX, nodesY;

    public DefaultInitializer(Filler filler, double[] gridX, double[] gridXMid, double[] gridY, double[] gridYMid) {
        nodesC = filler.getFilledArrayBy(gridXMid, gridYMid);
        nodesX = filler.getFilledArrayBy(gridX, gridYMid);
        nodesY = filler.getFilledArrayBy(gridXMid, gridY);
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
