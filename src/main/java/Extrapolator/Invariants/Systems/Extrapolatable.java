package Extrapolator.Invariants.Systems;

import Extensions.Nodes.Node;

public interface Extrapolatable {
    Node getExtrapolatedNodeX(
            Node nodeL, Node nodeLC, Node nodeMid, Node nodeRC, Node nodeR,
            Node newNodeLC, Node newNodeRC,
            double dxL, double dxR, double tau
    );

    Node getExtrapolatedNodeY(
            Node nodeB, Node nodeBC, Node nodeMid, Node nodeTC, Node nodeT,
            Node newNodeBC, Node newNodeTC,
            double dyB, double dyT, double tau
    );
}
