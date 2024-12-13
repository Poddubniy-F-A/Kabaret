package Extrapolators.InvariantsExtrapolator;

import Extensions.Node;
import Extrapolators.Extrapolator;
import Extrapolators.InvariantsExtrapolator.Invariants.Systems.*;
import Extensions.Fillers.Filler;

import static Extensions.GridsFunctions.*;

public class InvariantsExtrapolator implements Extrapolator {
    private final int slicesNumX, sliceLengthX, slicesNumY, sliceLengthY;
    private final Node[][] reflectedNodesX, reflectedNodesY;
    private final ExtrapolatableInvariantsSystem invariantsSystem;
    private final double[] stepsArrayX, stepsArrayY;

    public InvariantsExtrapolator(double[] gridX, double[] gridY, Filler filler, boolean useLocalInvariants) {
        double[] gridXMid = getMidGridFrom(gridX), gridYMid = getMidGridFrom(gridY);

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

    @Override
    public Node[][] getExtrapolatedNodesX(Node[][] nodesX, Node[][] nodesC, Node[][] newNodesC, double tau) {
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
                res[i][j] = invariantsSystem.getExtrapolatedNodeX(
                        nodeL, nodeLC, nodesX[i][j], nodeRC, nodeR,
                        newNodeLC, newNodeRC,
                        stepsArrayX[j], stepsArrayX[j + 1], tau
                );
            }
        }
        return res;
    }

    @Override
    public Node[][] getExtrapolatedNodesY(Node[][] nodesY, Node[][] nodesC, Node[][] newNodesC, double tau) {
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
                res[i][j] = invariantsSystem.getExtrapolatedNodeY(
                        nodeB, nodeBC, nodesY[i][j], nodeTC, nodeT,
                        newNodeBC, newNodeTC,
                        stepsArrayY[i], stepsArrayY[i + 1], tau
                );
            }
        }
        return res;
    }
}