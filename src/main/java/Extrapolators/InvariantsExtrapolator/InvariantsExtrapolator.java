package Extrapolators.InvariantsExtrapolator;

import Extensions.GridsFunctions;
import Extensions.Node;
import Extrapolators.Extrapolator;
import Extrapolators.InvariantsExtrapolator.Invariants.GlobalInvariantsSystem;
import Extrapolators.InvariantsExtrapolator.Invariants.ExtrapolatableInvariantsSystem;
import Extrapolators.InvariantsExtrapolator.Invariants.LocalInvariantsSystem;
import Initializers.Initializer;

import static Extensions.GridsFunctions.*;
import static Extensions.Node.getLimitedNode;

public class InvariantsExtrapolator implements Extrapolator {
    private final int slicesNumX, sliceLengthX, slicesNumY, sliceLengthY;
    private final Node[][] reflectedNodesX, reflectedNodesY;
    private final ExtrapolatableInvariantsSystem invariantsSystem;

    public InvariantsExtrapolator(double[] gridX, double[] gridY, Initializer initializer, boolean useLocalInvariants) {
        double[] gridXMid = GridsFunctions.getMidGridFrom(gridX), gridYMid = GridsFunctions.getMidGridFrom(gridY);

        slicesNumX = gridYMid.length;
        sliceLengthX = gridX.length;

        slicesNumY = gridY.length;
        sliceLengthY = gridXMid.length;

        reflectedNodesX = initializer.init(getReflectedBoardsWithMiddlesOf(gridX), gridYMid);
        reflectedNodesY = initializer.init(gridXMid, getReflectedBoardsWithMiddlesOf(gridY));

        invariantsSystem = useLocalInvariants ? new LocalInvariantsSystem() : new GlobalInvariantsSystem();
    }

    @Override
    public Node[][] getNodesX(Node[][] nodesX, Node[][] nodesC, Node[][] newNodesC) {
        Node[][] res = new Node[slicesNumX][sliceLengthX];
        for (int i = 0; i < slicesNumX; i++) {
            for (int j = 0; j < sliceLengthX; j++) {
                Node
                        nodeL = j > 0 ? nodesX[i][j - 1] : reflectedNodesX[i][0],
                        nodeLC = j > 0 ? nodesC[i][j - 1] : reflectedNodesX[i][1],
                        newNodeLC = j > 0 ? newNodesC[i][j - 1] : reflectedNodesX[i][1],
                        newNodeRC = j < sliceLengthX - 1 ? newNodesC[i][j] : reflectedNodesX[i][2],
                        nodeRC = j < sliceLengthX - 1 ? nodesC[i][j] : reflectedNodesX[i][2],
                        nodeR = j < sliceLengthX - 1 ? nodesX[i][j + 1] : reflectedNodesX[i][3];
                res[i][j] = getLimitedNode(
                        invariantsSystem.getExtrapolatedNodeX(nodeL, newNodeLC, nodeR, newNodeRC),
                        new Node[]{nodeL, nodeLC, nodesX[i][j], nodeRC, nodeR}
                );
            }
        }
        return res;
    }

    @Override
    public Node[][] getNodesY(Node[][] nodesY, Node[][] nodesC, Node[][] newNodesC) {
        Node[][] res = new Node[slicesNumY][sliceLengthY];
        for (int i = 0; i < slicesNumY; i++) {
            for (int j = 0; j < sliceLengthY; j++) {
                Node
                        nodeB = i > 0 ? nodesY[i - 1][j] : reflectedNodesY[0][j],
                        nodeBC = i > 0 ? nodesC[i - 1][j] : reflectedNodesY[1][j],
                        newNodeBC = i > 0 ? newNodesC[i - 1][j] : reflectedNodesY[1][j],
                        newNodeTC = i < slicesNumY - 1 ? newNodesC[i][j] : reflectedNodesY[2][j],
                        nodeTC = i < slicesNumY - 1 ? nodesC[i][j] : reflectedNodesY[2][j],
                        nodeT = i < slicesNumY - 1 ? nodesY[i + 1][j] : reflectedNodesY[3][j];
                res[i][j] = getLimitedNode(
                        invariantsSystem.getExtrapolatedNodeY(nodeB, newNodeBC, nodeT, newNodeTC),
                        new Node[]{nodeB, nodeBC, nodesY[i][j], nodeTC, nodeT}
                );
            }
        }
        return res;
    }
}