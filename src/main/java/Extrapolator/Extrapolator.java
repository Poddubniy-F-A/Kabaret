package Extrapolator;

import Extrapolator.Invariants.Systems.Extrapolatable;
import Extensions.Nodes.*;

import java.util.Arrays;

import static Extensions.GridsFunctions.extendedStepsArrayOf;

public class Extrapolator {
    private final Nodes nodesC, nodesX, nodesY;
    private final int slicesNumX, sliceLengthX, slicesNumY, sliceLengthY;
    private final Node[][] reflectedNodesX, reflectedNodesY;
    private final double[] stepsArrayX, stepsArrayY;
    private final Extrapolatable invariantsSystem;

    public Extrapolator(Nodes nodesC, Nodes nodesX, Nodes nodesY, Extrapolatable invariantsSystem) {
        this.nodesC = nodesC;
        this.nodesX = nodesX;
        this.nodesY = nodesY;

        slicesNumX = nodesX.getSizeY();
        sliceLengthX = nodesX.getSizeX();
        slicesNumY = nodesY.getSizeY();
        sliceLengthY = nodesY.getSizeX();

        reflectedNodesX = Arrays.stream(nodesX.getNodesArray()).map(nodes -> new Node[]{
                nodes[0], nodes[0], nodes[sliceLengthX - 1], nodes[sliceLengthX - 1]
        }).toArray(Node[][]::new);
        reflectedNodesY = new Node[][]{
                nodesY.getNodesArray()[0],
                nodesY.getNodesArray()[0],
                nodesY.getNodesArray()[slicesNumY - 1],
                nodesY.getNodesArray()[slicesNumY - 1],
        };

        stepsArrayX = extendedStepsArrayOf(nodesX.getGridX());
        stepsArrayY = extendedStepsArrayOf(nodesY.getGridY());

        this.invariantsSystem = invariantsSystem;
    }

    public Node[][] getExtrapolatedNodesArrayX(Node[][] newNodesCArray, double tau) {
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

    public Node[][] getExtrapolatedNodesArrayY(Node[][] newNodesCArray, double tau) {
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