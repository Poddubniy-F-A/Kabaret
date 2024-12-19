package InitialConditions;

import Extensions.Nodes.Nodes;
import InitialConditions.Initializers.Initializer;

import static Extensions.GridsFunctions.getMidGridFrom;
import static Extensions.GridsFunctions.getUniformGridBy;

public class DefaultInitialConditions implements InitialConditions {
    private final Nodes nodesC, nodesX, nodesY;

    public DefaultInitialConditions(double length, double stepX, double height, double stepY, Initializer initializer) {
        double[]
                gridX = getUniformGridBy((int) (length / stepX + 1), stepX), gridXMid = getMidGridFrom(gridX),
                gridY = getUniformGridBy((int) (height / stepY + 1), stepY), gridYMid = getMidGridFrom(gridY);

        nodesC = new Nodes(gridXMid, gridYMid, initializer.getInitializedArrayBy(gridXMid, gridYMid));
        nodesX = new Nodes(gridX, gridYMid, initializer.getInitializedArrayBy(gridX, gridYMid));
        nodesY = new Nodes(gridXMid, gridY, initializer.getInitializedArrayBy(gridXMid, gridY));
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
