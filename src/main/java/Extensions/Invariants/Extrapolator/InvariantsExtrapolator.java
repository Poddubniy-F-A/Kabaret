package Extensions.Invariants.Extrapolator;

import Extensions.Node;
import Extensions.Invariants.Invariant;

import java.util.Map;

import static Extensions.Constants.G;

public class InvariantsExtrapolator {
    private static final Map<String, Eigenvalue> eigenvalueByInvariantName =
            Map.of(
                    "I1x", node -> node.u() + Math.sqrt(G * node.h()),
                    "I2x", node -> node.u() - Math.sqrt(G * node.h()),
                    "I3x", Node::u,
                    "I1y", node -> node.v() + Math.sqrt(G * node.h()),
                    "I2y", node -> node.v() - Math.sqrt(G * node.h()),
                    "I3y", Node::v
            );

    private final Map<String, Invariant> invariantByName;

    public InvariantsExtrapolator(Map<String, Invariant> invariantByName) {
        this.invariantByName = invariantByName;
    }

    public double limitedExtrapolateBy(
            String invariantName,
            Node node, Node nodeC,
            Node limiter1, Node limiterC, Node limiter2,
            double tau, double dh
    ) {
        Invariant invariant = invariantByName.get(invariantName);
        Eigenvalue eigenvalue = eigenvalueByInvariantName.get(invariantName);

        double add = tau * ((invariant.I(nodeC, nodeC) - invariant.I(limiterC, nodeC)) / (tau / 2) +
                eigenvalue.lambda(nodeC) * (invariant.I(limiter2, nodeC) - invariant.I(limiter1, nodeC)) / dh),
                limiter1I = invariant.I(limiter1, nodeC),
                limiterCI = invariant.I(limiterC, nodeC),
                limiter2I = invariant.I(limiter2, nodeC);
        return Math.min(
                Math.max(limiter1I, Math.max(limiterCI, limiter2I)) + add,
                Math.max(
                        Math.min(limiter1I, Math.min(limiterCI, limiter2I)) + add,
                        2 * invariant.I(nodeC, nodeC) - invariant.I(node, nodeC)
                )
        );
    }
}
