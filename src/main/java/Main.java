import Extensions.Node;
import Extrapolators.Extrapolator;
import Extrapolators.InvariantsExtrapolator.*;
import Initializers.*;

import java.io.IOException;

import static Extensions.Constants.G;
import static Extensions.GridsFunctions.getMidGridFrom;
import static Extensions.GridsFunctions.getUniformGridBy;
import static Extensions.Node.getLimitedNode;

public class Main {
    private static final double
            TIME = 1.0, TAU = 0.01,
            LENGTH = 1.0, STEP_X = 0.01,
            HEIGHT = 1.0, STEP_Y = 0.01;
    private static final int
            STEPS_T = (int) (TIME / TAU + 1),
            STEPS_X = (int) (LENGTH / STEP_X + 1),
            STEPS_Y = (int) (HEIGHT / STEP_Y + 1);
    private static final double[]
            gridT = getUniformGridBy(STEPS_T, TAU),
            gridX = getUniformGridBy(STEPS_X, STEP_X), gridXMid = getMidGridFrom(gridX),
            gridY = getUniformGridBy(STEPS_Y, STEP_Y), gridYMid = getMidGridFrom(gridY);

    private static final double H0 = 1.0;
    private static final Initializer initializer = new VortexInitializer(
            LENGTH / 2, HEIGHT / 2,
            Math.sqrt(Math.pow(STEP_X, 2) + Math.pow(STEP_Y, 2)), H0
    );
    //new ConstantInitializer(H0);
    private static Node[][]
            nodesC = initializer.init(gridXMid, gridYMid),
            nodesX = initializer.init(gridX, gridYMid), nodesY = initializer.init(gridXMid, gridY);

    private static final Extrapolator extrapolator = new InvariantsExtrapolator(
            gridX, gridY,
            initializer, true
    );

    private static final String
            pathToConfig = "../output/config.csv",
            pathToOutputH = "../output/outputH.csv",
            pathToOutputU = "../output/outputU.csv",
            pathToOutputV = "../output/outputV.csv";

    public static void main(String[] args) {
        try (OutputHandler outputHandler = new OutputHandler(
                pathToConfig,
                gridT, gridXMid, gridYMid,
                pathToOutputH, pathToOutputU, pathToOutputV
        )) {
            outputHandler.addRecord(nodesC);
            for (int i = 1; i < STEPS_T; i++) {
                Node[][] newNodesC = newNodesCFrom(nodesC);
                nodesX = extrapolator.getNodesX(nodesX, nodesC, newNodesC);
                nodesY = extrapolator.getNodesY(nodesY, nodesC, newNodesC);
                nodesC = newNodesCFrom(newNodesC);

                outputHandler.addRecord(nodesC);
            }
        } catch (IOException e) {
            System.err.println("Проверьте корректность путей к выходным файлам");
            throw new RuntimeException(e);
        }
    }

    private static Node[][] newNodesCFrom(Node[][] nodesCOld) {
        final int slicesNum = gridYMid.length, sliceLength = gridXMid.length;

        Node[][] res = new Node[slicesNum][sliceLength];
        for (int i = 0; i < slicesNum; i++) {
            for (int j = 0; j < sliceLength; j++) {
                Node
                        nodeC = nodesCOld[i][j],
                        nodeL = nodesX[i][j], nodeR = nodesX[i][j + 1],
                        nodeB = nodesY[i][j], nodeT = nodesY[i + 1][j];
                double
                        h = nodeC.h() - (
                        (nodeR.h() * nodeR.u() - nodeL.h() * nodeL.u()) / STEP_X +
                                (nodeT.h() * nodeT.v() - nodeB.h() * nodeB.v()) / STEP_Y
                        ) * TAU / 2,
                        u = (nodeC.h() * nodeC.u() - (
                                (nodeR.h() * Math.pow(nodeR.u(), 2) - nodeL.h() * Math.pow(nodeL.u(), 2)) / STEP_X +
                                        (nodeT.h() * nodeT.u() * nodeT.v() - nodeB.h() * nodeB.u() * nodeB.v()) / STEP_Y +
                                        G * (Math.pow(nodeR.h(), 2) - Math.pow(nodeL.h(), 2)) / (2 * STEP_X)
                        ) * TAU / 2) / h,
                        v = (nodeC.h() * nodeC.v() - (
                                (nodeT.h() * Math.pow(nodeT.v(), 2) - nodeB.h() * Math.pow(nodeB.v(), 2)) / STEP_Y +
                                        (nodeR.h() * nodeR.u() * nodeR.v() - nodeL.h() * nodeL.u() * nodeL.v()) / STEP_X +
                                        G * (Math.pow(nodeT.h(), 2) - Math.pow(nodeB.h(), 2)) / (2 * STEP_Y)
                        ) * TAU / 2) / h;
                res[i][j] = new Node(h, u, v);//getLimitedNode(new Node(h, u, v), new Node[]{nodeL, nodeR, nodeB, nodeT, nodeC});
            }
        }
        return res;
    }
}
