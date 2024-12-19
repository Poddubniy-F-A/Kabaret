import Extensions.Nodes.*;

import static Extensions.Constants.*;
import static Extensions.GridsFunctions.getStepsArrayOf;

public class PhasesHandler {
    private final Nodes nodesC, nodesX, nodesY;
    private final double[] stepsArrayX, stepsArrayY;

    public PhasesHandler(Nodes nodesC, Nodes nodesX, Nodes nodesY) {
        this.nodesC = nodesC;
        this.nodesX = nodesX;
        this.nodesY = nodesY;

        stepsArrayX = getStepsArrayOf(nodesX.getGridX());
        stepsArrayY = getStepsArrayOf(nodesY.getGridY());
    }

    public double getTau() {
        double res = MIN_TAU;
        for (int i = 0; i < nodesC.getSizeY(); i++) {
            for (int j = 0; j < nodesC.getSizeX(); j++) {
                Node node = nodesC.getNode(i, j);
                res = Math.min(res, Math.min(
                        stepsArrayX[j] / (Math.sqrt(G * node.h()) + Math.abs(node.u())),
                        stepsArrayY[i] / (Math.sqrt(G * node.h()) + Math.abs(node.v()))
                ));
            }
        }
        return CFL * res;
    }

    public Node[][] getNewNodesCFrom(Node[][] nodesCOld, double tau) {
        final int slicesNum = nodesC.getSizeY(), sliceLength = nodesC.getSizeX();

        Node[][] res = new Node[slicesNum][sliceLength];
        for (int i = 0; i < slicesNum; i++) {
            double dy = stepsArrayY[i];
            for (int j = 0; j < sliceLength; j++) {
                Node
                        nodeC = nodesCOld[i][j],
                        nodeL = nodesX.getNode(i, j), nodeR = nodesX.getNode(i, j + 1),
                        nodeB = nodesY.getNode(i, j), nodeT = nodesY.getNode(i + 1, j);
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
