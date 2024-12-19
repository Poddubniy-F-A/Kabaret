package Extrapolator.Invariants;

import Extensions.Nodes.Node;

@FunctionalInterface
public interface Eigenvalue {
    double lambda(Node node);
}
