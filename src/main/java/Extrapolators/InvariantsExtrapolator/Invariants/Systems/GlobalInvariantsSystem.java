package Extrapolators.InvariantsExtrapolator.Invariants.Systems;

import Extensions.Node;
import Extrapolators.InvariantsExtrapolator.Invariants.Eigenvalues.EigenvaluesSystem;
import Extrapolators.InvariantsExtrapolator.Invariants.ExtrapolatableInvariant;

import static Extensions.Constants.G;

public class GlobalInvariantsSystem implements ExtrapolatableInvariantsSystem {
    private static final ExtrapolatableInvariant
            I1x = new ExtrapolatableInvariant() {
                @Override
                public double getEigenvalue(Node node) {
                    return EigenvaluesSystem.eigenvalueByInvariantName.get("I1x").lambda(node);
                }

                @Override
                public double I(Node node, Node nodeC) {
                    return node.u() + 2 * Math.sqrt(G * node.h());
                }
            },
            I2x = new ExtrapolatableInvariant() {
                @Override
                public double getEigenvalue(Node node) {
                    return EigenvaluesSystem.eigenvalueByInvariantName.get("I2x").lambda(node);
                }

                @Override
                public double I(Node node, Node nodeC) {
                    return node.u() - 2 * Math.sqrt(G * node.h());
                }
            },
            I3x = new ExtrapolatableInvariant() {
                @Override
                public double getEigenvalue(Node node) {
                    return EigenvaluesSystem.eigenvalueByInvariantName.get("I3x").lambda(node);
                }

                @Override
                public double I(Node node, Node nodeC) {
                    return node.v();
                }
            },
            I1y = new ExtrapolatableInvariant() {
                @Override
                public double getEigenvalue(Node node) {
                    return EigenvaluesSystem.eigenvalueByInvariantName.get("I1y").lambda(node);
                }

                @Override
                public double I(Node node, Node nodeC) {
                    return node.v() + 2 * Math.sqrt(G * node.h());
                }
            },
            I2y = new ExtrapolatableInvariant() {
                @Override
                public double getEigenvalue(Node node) {
                    return EigenvaluesSystem.eigenvalueByInvariantName.get("I2y").lambda(node);
                }

                @Override
                public double I(Node node, Node nodeC) {
                    return node.v() - 2 * Math.sqrt(G * node.h());
                }
            },
            I3y = new ExtrapolatableInvariant() {
                @Override
                public double getEigenvalue(Node node) {
                    return EigenvaluesSystem.eigenvalueByInvariantName.get("I3y").lambda(node);
                }

                @Override
                public double I(Node node, Node nodeC) {
                    return node.u();
                }
            };

    @Override
    public Node getExtrapolatedNodeX(
            Node nodeL, Node nodeLC, Node nodeMid, Node nodeRC, Node nodeR,
            Node newNodeLC, Node newNodeRC,
            double dxL, double dxR, double tau
    ) {
        double
                extrapolatedFromLeftI1 = I1x.limitedExtrapolateBy(nodeL, newNodeLC, nodeL, nodeLC, nodeMid, tau, dxL),
                extrapolatedFromRightI2 = I2x.limitedExtrapolateBy(nodeR, newNodeRC, nodeMid, nodeRC, nodeR, tau, dxR),
                h = Math.pow((extrapolatedFromLeftI1 - extrapolatedFromRightI2) / 4, 2) / G,
                u = (extrapolatedFromLeftI1 + extrapolatedFromRightI2) / 2,
                v = u > 0 ?
                        I3x.limitedExtrapolateBy(nodeL, newNodeLC, nodeL, nodeLC, nodeMid, tau, dxL) :
                        I3x.limitedExtrapolateBy(nodeR, newNodeRC, nodeMid, nodeRC, nodeR, tau, dxR);
        return new Node(h, u, v);
    }

    @Override
    public Node getExtrapolatedNodeY(
            Node nodeB, Node nodeBC, Node nodeMid, Node nodeTC, Node nodeT,
            Node newNodeBC, Node newNodeTC,
            double dyB, double dyT, double tau
    ) {
        double
                extrapolatedFromBottomI1 = I1y.limitedExtrapolateBy(nodeB, newNodeBC, nodeB, nodeBC, nodeMid, tau, dyB),
                extrapolatedFromTopI2 = I2y.limitedExtrapolateBy(nodeT, newNodeTC, nodeMid, nodeTC, nodeT, tau, dyT),
                h = Math.pow((extrapolatedFromBottomI1 - extrapolatedFromTopI2) / 4, 2) / G,
                v = (extrapolatedFromBottomI1 + extrapolatedFromTopI2) / 2,
                u = v > 0 ?
                        I3y.limitedExtrapolateBy(nodeB, newNodeBC, nodeB, nodeBC, nodeMid, tau, dyB) :
                        I3y.limitedExtrapolateBy(nodeT, newNodeTC, nodeMid, nodeTC, nodeT, tau, dyT);
        return new Node(h, u, v);
    }
}
