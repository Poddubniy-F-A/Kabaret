package Extrapolator.Invariants;

import Extensions.Nodes.Node;

@FunctionalInterface
public interface Invariant {
    double I(Node node, Node nodeC);
}
