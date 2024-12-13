package Extrapolators.InvariantsExtrapolator.Invariants.Eigenvalues;

import Extensions.Node;

import java.util.Map;

import static Extensions.Constants.G;

public class EigenvaluesSystem {
    public static final Map<String, Eigenvalue> eigenvalueByInvariantName =
            Map.of(
                    "I1x", node -> node.u() + Math.sqrt(G * node.h()),
                    "I2x", node -> node.u() - Math.sqrt(G * node.h()),
                    "I3x", Node::u,
                    "I1y", node -> node.v() + Math.sqrt(G * node.h()),
                    "I2y", node -> node.v() - Math.sqrt(G * node.h()),
                    "I3y", Node::v
            );
}
