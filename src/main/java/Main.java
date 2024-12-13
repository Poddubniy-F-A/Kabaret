import Extensions.Node;
import Extensions.Nodes;
import Initializers.*;
import Extensions.Fillers.Filler;
import Extensions.Fillers.VortexFiller;

import java.io.IOException;
import java.util.ArrayList;

import static Extensions.Constants.ALPHA;
import static Extensions.GridsFunctions.getMidGridFrom;
import static Extensions.GridsFunctions.getUniformGridBy;

public class Main {
    private static final double
            LENGTH = 1.0, STEP_X = 0.01,
            HEIGHT = 1.0, STEP_Y = 0.01;
    private static final int
            STEPS_X = (int) (LENGTH / STEP_X + 1),
            STEPS_Y = (int) (HEIGHT / STEP_Y + 1);
    private static final double[]
            gridX = getUniformGridBy(STEPS_X, STEP_X), gridXMid = getMidGridFrom(gridX),
            gridY = getUniformGridBy(STEPS_Y, STEP_Y), gridYMid = getMidGridFrom(gridY);
    private static final Nodes
            nodesC = new Nodes(gridXMid, gridYMid),
            nodesX = new Nodes(gridX, gridYMid),
            nodesY = new Nodes(gridXMid, gridY);

    private static final PhasesHandler phasesHandler = new PhasesHandler(nodesC, nodesX, nodesY);

    private static final double
            R0 = 3 * Math.sqrt(Math.pow(STEP_X, 2) + Math.pow(STEP_Y, 2)), H0 = 1.0,
            TIME = 200 * (2 * Math.PI * R0) / ALPHA;
    private static final Filler filler = new VortexFiller(LENGTH / 2, HEIGHT / 2, R0, H0);//new ConstantFiller(H0);//
    private static final Initializer initializer = new DefaultInitializer(filler);//new CentersOrientedInitializer(filler);//
    private static final Extrapolator extrapolator = new Extrapolator(
            nodesC, nodesX, nodesY,
            filler, true
    );

    public static void main(String[] args) {
        ArrayList<Double> times = new ArrayList<>();
        try (OutputHandler outputHandler = new OutputHandler(nodesC, times)) {
            double curTime = 0;
            times.add(curTime);

            initializer.init(nodesC, nodesX, nodesY);

            outputHandler.addRecord();
            while (curTime < TIME) {
                double curTau = phasesHandler.getTau();
                curTime += curTau;
                times.add(curTime);

                Node[][] newNodesCArray = phasesHandler.getNewNodesCFrom(nodesC.getNodesArray(), curTau);
                nodesX.setNodesArray(extrapolator.getExtrapolatedNodesX(newNodesCArray, curTau));
                nodesY.setNodesArray(extrapolator.getExtrapolatedNodesY(newNodesCArray, curTau));
                nodesC.setNodesArray(phasesHandler.getNewNodesCFrom(newNodesCArray, curTau));

                outputHandler.addRecord();
            }
        } catch (IOException e) {
            System.err.println("Проверьте корректность путей к выходным файлам");
            throw new RuntimeException(e);
        }
    }
}
