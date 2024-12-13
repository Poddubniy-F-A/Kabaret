import Extensions.Node;

import static Extensions.Constants.*;
import static Extensions.Constants.G;
import static Extensions.GridsFunctions.getStepsArrayOf;

public class PhasesHandler {
    private final double[] stepsArrayX, stepsArrayY;
    private final int slicesNum, sliceLength;

    public PhasesHandler(double[] gridX, double[] gridY) {
        stepsArrayX = getStepsArrayOf(gridX);
        stepsArrayY = getStepsArrayOf(gridY);

        slicesNum = gridY.length - 1;
        sliceLength = gridX.length - 1;
    }

    public double getTau(Node[][] nodesC) {
        double res = MIN_TAU;
        for (int i = 0; i < nodesC.length; i++) {
            for (int j = 0; j < nodesC[0].length; j++) {
                Node node = nodesC[i][j];
                res = Math.min(res, Math.min(
                        stepsArrayX[j] / (Math.sqrt(G * node.h()) + Math.abs(node.u())),
                        stepsArrayY[i] / (Math.sqrt(G * node.h()) + Math.abs(node.v()))
                ));
            }
        }
        return CFL * res;
    }

    public Node[][] getNewNodesCFrom(Node[][] nodesCOld, Node[][] nodesX, Node[][] nodesY, double tau) {
        Node[][] res = new Node[slicesNum][sliceLength];
        for (int i = 0; i < slicesNum; i++) {
            double dy = stepsArrayY[i];
            for (int j = 0; j < sliceLength; j++) {
                Node
                        nodeC = nodesCOld[i][j],
                        nodeL = nodesX[i][j], nodeR = nodesX[i][j + 1],
                        nodeB = nodesY[i][j], nodeT = nodesY[i + 1][j];
                double
                        dx = stepsArrayX[j],
                        h = nodeC.h() - (
                        (nodeR.h() * nodeR.u() - nodeL.h() * nodeL.u()) / dx +
                                (nodeT.h() * nodeT.v() - nodeB.h() * nodeB.v()) / dy
                        ) * tau / 2,
                        u = (nodeC.h() * nodeC.u() - (
                                (nodeR.h() * Math.pow(nodeR.u(), 2) - nodeL.h() * Math.pow(nodeL.u(), 2)) / dx +
                                        (nodeT.h() * nodeT.u() * nodeT.v() - nodeB.h() * nodeB.u() * nodeB.v()) / dy +
                                        G * (Math.pow(nodeR.h(), 2) - Math.pow(nodeL.h(), 2)) / (2 * dx)
                        ) * tau / 2) / h,
                        v = (nodeC.h() * nodeC.v() - (
                                (nodeT.h() * Math.pow(nodeT.v(), 2) - nodeB.h() * Math.pow(nodeB.v(), 2)) / dy +
                                        (nodeR.h() * nodeR.u() * nodeR.v() - nodeL.h() * nodeL.u() * nodeL.v()) / dx +
                                        G * (Math.pow(nodeT.h(), 2) - Math.pow(nodeB.h(), 2)) / (2 * dy)
                        ) * tau / 2) / h;
                res[i][j] = new Node(h, u, v);
            }
        }
        return res;
    }
}
