package Initializers;

import Extensions.Fillers.Filler;
import Extensions.Nodes;

public class DefaultInitializer implements Initializer {
    private final Filler filler;

    public DefaultInitializer(Filler filler) {
        this.filler = filler;
    }

    @Override
    public void init(Nodes nodesC, Nodes nodesX, Nodes nodesY) {
        nodesC.setNodesArray(filler);
        nodesX.setNodesArray(filler);
        nodesY.setNodesArray(filler);
    }
}
