import Extensions.Nodes.*;
import Extrapolator.Extrapolator;
import Extrapolator.Invariants.Systems.Extrapolatable;
import Extrapolator.Invariants.Systems.ShallowWater.Implementations.*;
import InitialConditions.*;
import InitialConditions.Initializers.*;

import java.io.IOException;
import java.util.ArrayList;

import static Extensions.Constants.ALPHA;

public class Main {
    private static final double
            LENGTH = 1.0, STEP_X = 0.02,
            HEIGHT = 1.0, STEP_Y = 0.01,
            R0 = 3 * Math.sqrt(Math.pow(STEP_X, 2) + Math.pow(STEP_Y, 2)),
            TIME = 200 * (2 * Math.PI * R0) / ALPHA;

    private static final Initializer initializer = new VortexInitializer(LENGTH / 2, HEIGHT / 2, R0, 1.0); // ConstantInitializer(1.0);
    private static final InitialConditions initialConditions = new CentersOrientedInitialConditions( // DefaultInitialConditions(
            LENGTH, STEP_X, HEIGHT, STEP_Y,
            initializer
    );

    private static final Extrapolatable invariantsSystem = new LocalInvariantsSystem(); // GlobalInvariantsSystem();

    public static void main(String[] args) {
        final Nodes
                nodesC = initialConditions.getNodesC(),
                nodesX = initialConditions.getNodesX(),
                nodesY = initialConditions.getNodesY();

        final PhasesHandler phasesHandler = new PhasesHandler(nodesC, nodesX, nodesY);
        final Extrapolator extrapolator = new Extrapolator(nodesC, nodesX, nodesY, invariantsSystem);

        final ArrayList<Double> times = new ArrayList<>();

        try (final OutputHandler outputHandler = new OutputHandler(nodesC, times)) {
            double curTime = 0;
            times.add(curTime);

            outputHandler.addRecord();

            while (curTime < TIME) {
                double tau = phasesHandler.getTau();
                curTime += tau;
                times.add(curTime);

                Node[][] newNodesArrayC = phasesHandler.getNewNodesCFrom(nodesC.getNodesArray(), tau);
                nodesX.setNodesArray(extrapolator.getExtrapolatedNodesArrayX(newNodesArrayC, tau));
                nodesY.setNodesArray(extrapolator.getExtrapolatedNodesArrayY(newNodesArrayC, tau));
                nodesC.setNodesArray(phasesHandler.getNewNodesCFrom(newNodesArrayC, tau));

                outputHandler.addRecord();
            }
        } catch (IOException e) {
            System.err.println("Проверьте корректность путей к выходным файлам");
            throw new RuntimeException(e);
        }
    }
}
