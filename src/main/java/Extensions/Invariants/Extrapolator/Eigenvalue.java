package Extensions.Invariants.Extrapolator;

import Extensions.Node;

@FunctionalInterface
public interface Eigenvalue {
    double lambda(Node node);
}
