import Extensions.Node;
import Extrapolators.Extrapolator;
import Extrapolators.InvariantsExtrapolator.*;
import Initializers.*;
import Extensions.Fillers.Filler;
import Extensions.Fillers.VortexFiller;

import java.io.IOException;
import java.util.ArrayList;

import static Extensions.GridsFunctions.getMidGridFrom;
import static Extensions.GridsFunctions.getUniformGridBy;

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

    private static final PhasesHandler phasesHandler = new PhasesHandler(gridX, gridY);

    private static final double H0 = 1.0;
    private static final Filler filler = new VortexFiller(
            LENGTH / 2, HEIGHT / 2,
            Math.sqrt(Math.pow(STEP_X, 2) + Math.pow(STEP_Y, 2)), H0
    );//new ConstantFiller(H0);//
    private static final Initializer initializer = new DefaultInitializer(
            filler,
            gridX, gridXMid, gridY, gridYMid
    );//new CentersOrientedInitializer(filler, gridXMid, gridYMid);//
    private static final Extrapolator extrapolator = new InvariantsExtrapolator(
            gridX, gridY,
            filler, true
    );

    private static final ArrayList<Double> times = new ArrayList<>();

    private static Node[][]
            nodesC = initializer.getInitializedNodesC(),
            nodesX = initializer.getInitializedNodesX(),
            nodesY = initializer.getInitializedNodesY();

    public static void main(String[] args) {
        try (OutputHandler outputHandler = new OutputHandler(pathToOutputH, pathToOutputU, pathToOutputV)) {
            double curTime = 0;

            outputHandler.addRecord(nodesC);
            times.add(curTime);
            while (curTime < TIME) {
                double curTau = phasesHandler.getTau(nodesC);

                Node[][] newNodesC = phasesHandler.getNewNodesCFrom(nodesC, nodesX, nodesY, curTau);
                nodesX = extrapolator.getExtrapolatedNodesX(nodesX, nodesC, newNodesC, curTau);
                nodesY = extrapolator.getExtrapolatedNodesY(nodesY, nodesC, newNodesC, curTau);
                nodesC = phasesHandler.getNewNodesCFrom(newNodesC, nodesX, nodesY, curTau);

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
}
