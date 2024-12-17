import Extensions.Invariants.Systems.ExtrapolatableInvariantsSystem;
import Extensions.Invariants.Systems.GlobalInvariantsSystem;
import Extensions.Invariants.Systems.LocalInvariantsSystem;
import Extensions.Node;
import Extensions.Nodes;
import Extensions.Fillers.Filler;

import static Extensions.GridsFunctions.*;

public class Extrapolator {
    private final Nodes nodesC, nodesX, nodesY;
    private final int slicesNumX, sliceLengthX, slicesNumY, sliceLengthY;
    private final Node[][] reflectedNodesX, reflectedNodesY;
    private final ExtrapolatableInvariantsSystem invariantsSystem;
    private final double[] stepsArrayX, stepsArrayY;

    public Extrapolator(Nodes nodesC, Nodes nodesX, Nodes nodesY, Filler filler, boolean useLocalInvariants) {
        this.nodesC = nodesC;
        this.nodesX = nodesX;
        this.nodesY = nodesY;

        double[]
                gridX = nodesX.getGridX(), gridXMid = nodesC.getGridX(),
                gridY = nodesY.getGridY(), gridYMid = nodesC.getGridY();

        slicesNumX = gridYMid.length;
        sliceLengthX = gridX.length;
        slicesNumY = gridY.length;
        sliceLengthY = gridXMid.length;

        reflectedNodesX = filler.getFilledArrayBy(getReflectedBoardsWithMiddlesOf(gridX), gridYMid);
        reflectedNodesY = filler.getFilledArrayBy(gridXMid, getReflectedBoardsWithMiddlesOf(gridY));

        invariantsSystem = useLocalInvariants ? new LocalInvariantsSystem() : new GlobalInvariantsSystem();

        stepsArrayX = allStepsArrayOf(gridX);
        stepsArrayY = allStepsArrayOf(gridY);
    }

    private double[] allStepsArrayOf(double[] grid) {
        double[] res = new double[grid.length + 1];
        res[0] = getReflectedStartOf(grid);
        System.arraycopy(getStepsArrayOf(grid), 0, res, 1, res.length - 2);
        res[res.length - 1] = getReflectedEndOf(grid);
        return res;
    }

    public Node[][] getExtrapolatedNodesX(Node[][] newNodesCArray, double tau) {
        Node[][] res = new Node[slicesNumX][sliceLengthX];
        for (int i = 0; i < slicesNumX; i++) {
            for (int j = 0; j < sliceLengthX; j++) {
                Node
                        nodeL = j > 0 ? nodesX.getNode(i, j - 1) : reflectedNodesX[i][0],
                        nodeLC = j > 0 ? nodesC.getNode(i, j - 1) : reflectedNodesX[i][1],
                        newNodeLC = j > 0 ? newNodesCArray[i][j - 1] : reflectedNodesX[i][1],
                        newNodeRC = j < sliceLengthX - 1 ? newNodesCArray[i][j] : reflectedNodesX[i][2],
                        nodeRC = j < sliceLengthX - 1 ? nodesC.getNode(i, j) : reflectedNodesX[i][2],
                        nodeR = j < sliceLengthX - 1 ? nodesX.getNode(i, j + 1) : reflectedNodesX[i][3];
                res[i][j] = invariantsSystem.getExtrapolatedNodeX(
                        nodeL, nodeLC, nodesX.getNode(i, j), nodeRC, nodeR,
                        newNodeLC, newNodeRC,
                        stepsArrayX[j], stepsArrayX[j + 1], tau
                );
            }
        }
        return res;
    }

    public Node[][] getExtrapolatedNodesY(Node[][] newNodesCArray, double tau) {
        Node[][] res = new Node[slicesNumY][sliceLengthY];
        for (int i = 0; i < slicesNumY; i++) {
            for (int j = 0; j < sliceLengthY; j++) {
                Node
                        nodeB = i > 0 ? nodesY.getNode(i - 1, j) : reflectedNodesY[0][j],
                        nodeBC = i > 0 ? nodesC.getNode(i - 1, j) : reflectedNodesY[1][j],
                        newNodeBC = i > 0 ? newNodesCArray[i - 1][j] : reflectedNodesY[1][j],
                        newNodeTC = i < slicesNumY - 1 ? newNodesCArray[i][j] : reflectedNodesY[2][j],
                        nodeTC = i < slicesNumY - 1 ? nodesC.getNode(i, j) : reflectedNodesY[2][j],
                        nodeT = i < slicesNumY - 1 ? nodesY.getNode(i + 1, j) : reflectedNodesY[3][j];
                res[i][j] = invariantsSystem.getExtrapolatedNodeY(
                        nodeB, nodeBC, nodesY.getNode(i, j), nodeTC, nodeT,
                        newNodeBC, newNodeTC,
                        stepsArrayY[i], stepsArrayY[i + 1], tau
                );
            }
        }
        return res;
    }
}