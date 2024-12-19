package Extrapolator.Invariants.Systems.ShallowWater;

import Extensions.Nodes.Node;
import Extrapolator.Invariants.Eigenvalue;
import Extrapolator.Invariants.Invariant;
import Extrapolator.Invariants.Systems.Extrapolatable;

import java.util.Map;

import static Extensions.Constants.G;
import static Extrapolator.Invariants.Systems.ShallowWater.InvariantName.*;

public abstract class ShallowWaterInvariantsSystem implements Extrapolatable {
    private static final Map<InvariantName, Eigenvalue> eigenvalueByInvariantName = Map.of(
            I1x, node -> node.u() + Math.sqrt(G * node.h()),
            I2x, node -> node.u() - Math.sqrt(G * node.h()),
            I3x, Node::u,
            I1y, node -> node.v() + Math.sqrt(G * node.h()),
            I2y, node -> node.v() - Math.sqrt(G * node.h()),
            I3y, Node::v
    );

    private final Map<InvariantName, Invariant> invariantByName;

    public ShallowWaterInvariantsSystem(Map<InvariantName, Invariant> invariantByName) {
        this.invariantByName = invariantByName;
    }

    protected double limitedExtrapolateBy(
            InvariantName invariantName,
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
                limiter2I = invariant.I(limiter2, nodeC),
                extrapolation = 2 * invariant.I(nodeC, nodeC) - invariant.I(node, nodeC);

        return Math.min(
                Math.max(limiter1I, Math.max(limiterCI, limiter2I)) + add,
                Math.max(
                        Math.min(limiter1I, Math.min(limiterCI, limiter2I)) + add,
                        extrapolation
                )
        );
    }
}
