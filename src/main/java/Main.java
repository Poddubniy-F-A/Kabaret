import Extensions.Node;
import Extrapolators.Extrapolator;
import Extrapolators.InvariantsExtrapolator.*;
import Initializers.*;

import java.io.IOException;
import java.util.ArrayList;

import static Extensions.Constants.*;
import static Extensions.GridsFunctions.getMidGridFrom;
import static Extensions.GridsFunctions.getUniformGridBy;
import static Extensions.Node.getAverage;

public class Main {
    private static final String
            pathToConfig = "../output/config.csv",
            pathToOutputH = "../output/outputH.csv",
            pathToOutputU = "../output/outputU.csv",
            pathToOutputV = "../output/outputV.csv";

    private static final double
            TIME = 10.0,
            LENGTH = 1.0, STEP_X = 0.01,
            HEIGHT = 1.0, STEP_Y = 0.01;
    private static final int
            STEPS_X = (int) (LENGTH / STEP_X + 1),
            STEPS_Y = (int) (HEIGHT / STEP_Y + 1);
    private static final double[]
            gridX = getUniformGridBy(STEPS_X, STEP_X), gridXMid = getMidGridFrom(gridX),
            gridY = getUniformGridBy(STEPS_Y, STEP_Y), gridYMid = getMidGridFrom(gridY);

    private static final double H0 = 1.0;
    private static final Initializer initializer = new VortexInitializer(
            LENGTH / 2, HEIGHT / 2,
            Math.sqrt(Math.pow(STEP_X, 2) + Math.pow(STEP_Y, 2)), H0
    );//new ConstantInitializer(H0);//
    private static final Extrapolator extrapolator = new InvariantsExtrapolator(
            gridX, gridY,
            initializer, false
    );

    private static final ArrayList<Double> times = new ArrayList<>();
    private static double curTime = 0, curTau;

    private static Node[][]
            nodesC = initializer.init(gridXMid, gridYMid),
            nodesX = initializer.init(gridX, gridYMid),//getNodesXFromNodesC(),//
            nodesY = initializer.init(gridXMid, gridY);//getNodesYFromNodesC();//

    public static void main(String[] args) {
        try (OutputHandler outputHandler = new OutputHandler(pathToOutputH, pathToOutputU, pathToOutputV)) {
            outputHandler.addRecord(nodesC);
            times.add(curTime);
            while (curTime < TIME) {
                updateTau();

                Node[][] newNodesC = newNodesCFrom(nodesC);
                nodesX = extrapolator.getExtrapolatedNodesX(nodesX, nodesC, newNodesC);
                nodesY = extrapolator.getExtrapolatedNodesY(nodesY, nodesC, newNodesC);
                nodesC = newNodesCFrom(newNodesC);

                outputHandler.addRecord(nodesC);
                curTime += curTau;
                times.add(curTime);
            }

            outputHandler.fillConfig(pathToConfig, gridXMid, gridYMid, times);
        } catch (IOException e) {
            System.err.println("Проверьте корректность путей к выходным файлам");
            throw new RuntimeException(e);
        }
    }

    private static Node[][] getNodesXFromNodesC() {
        final int slicesC = nodesC.length, sliceCLength = nodesC[0].length;

        Node[][] res = new Node[slicesC][sliceCLength + 1];
        for (int i = 0; i < slicesC; i++) {
            for (int j = 0; j <= sliceCLength; j++) {
                res[i][j] = getAverage(nodesC[i][Math.max(j - 1, 0)], nodesC[i][Math.min(j, sliceCLength - 1)]);
            }
        }
        return res;
    }

    private static Node[][] getNodesYFromNodesC() {
        final int slicesC = nodesC.length, sliceCLength = nodesC[0].length;

        Node[][] res = new Node[slicesC + 1][sliceCLength];
        for (int i = 0; i <= slicesC; i++) {
            for (int j = 0; j < sliceCLength; j++) {
                res[i][j] = getAverage(nodesC[Math.max(i - 1, 0)][j], nodesC[Math.min(i, slicesC - 1)][j]);
            }
        }
        return res;
    }

    private static void updateTau() {
        curTau = MIN_TAU;
        for (int i = 0; i < gridY.length - 1; i++) {
            for (int j = 0; j < gridX.length - 1; j++) {
                Node node = nodesC[i][j];
                curTau = Math.min(curTau, Math.min(
                        (gridX[j + 1] - gridX[j]) / (Math.sqrt(G * node.h()) + Math.abs(node.u())),
                        (gridY[i + 1] - gridY[i]) / (Math.sqrt(G * node.h()) + Math.abs(node.v()))
                ));
            }
        }
        curTau *= CFL;
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
                        ) * curTau / 2,
                        u = (nodeC.h() * nodeC.u() - (
                                (nodeR.h() * Math.pow(nodeR.u(), 2) - nodeL.h() * Math.pow(nodeL.u(), 2)) / STEP_X +
                                        (nodeT.h() * nodeT.u() * nodeT.v() - nodeB.h() * nodeB.u() * nodeB.v()) / STEP_Y +
                                        G * (Math.pow(nodeR.h(), 2) - Math.pow(nodeL.h(), 2)) / (2 * STEP_X)
                        ) * curTau / 2) / h,
                        v = (nodeC.h() * nodeC.v() - (
                                (nodeT.h() * Math.pow(nodeT.v(), 2) - nodeB.h() * Math.pow(nodeB.v(), 2)) / STEP_Y +
                                        (nodeR.h() * nodeR.u() * nodeR.v() - nodeL.h() * nodeL.u() * nodeL.v()) / STEP_X +
                                        G * (Math.pow(nodeT.h(), 2) - Math.pow(nodeB.h(), 2)) / (2 * STEP_Y)
                        ) * curTau / 2) / h;
                res[i][j] = new Node(h, u, v);//nodesCOld[i][j];//
            }
        }
        return res;
    }
}
