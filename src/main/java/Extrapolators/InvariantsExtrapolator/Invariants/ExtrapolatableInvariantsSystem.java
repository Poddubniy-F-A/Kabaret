package Extrapolators.InvariantsExtrapolator.Invariants;

import Extensions.Node;

public interface ExtrapolatableInvariantsSystem {
    Node getExtrapolatedNodeX(
            Node nodeL, Node newNodeLC, Node[] limitersL,
            Node nodeR, Node newNodeRC, Node[] limitersR
    );

    Node getExtrapolatedNodeY(
            Node nodeB, Node newNodeBC, Node[] limitersB,
            Node nodeT, Node newNodeTC, Node[] limitersT
    );
}
