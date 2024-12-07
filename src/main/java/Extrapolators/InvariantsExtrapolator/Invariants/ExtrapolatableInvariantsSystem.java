package Extrapolators.InvariantsExtrapolator.Invariants;

import Extensions.Node;

public interface ExtrapolatableInvariantsSystem {
    Node getExtrapolatedNodeX(Node nodeL, Node newNodeLC, Node nodeR, Node newNodeRC);

    Node getExtrapolatedNodeY(Node nodeB, Node newNodeBC, Node nodeT, Node newNodeTC);
}
