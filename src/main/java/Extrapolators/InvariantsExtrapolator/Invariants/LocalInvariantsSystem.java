package Extrapolators.InvariantsExtrapolator.Invariants;

import Extensions.Node;

import static Extensions.Constants.G;

public class LocalInvariantsSystem implements ExtrapolatableInvariantsSystem {
    private static final ExtrapolatableInvariant
            I1x = (node, nodeC) -> node.u() + Math.sqrt(G / nodeC.h()) * node.h(),
            I2x = (node, nodeC) -> node.u() - Math.sqrt(G / nodeC.h()) * node.h(),
            I3x = (node, _) -> node.v(),
            I1y = (node, nodeC) -> node.v() + Math.sqrt(G / nodeC.h()) * node.h(),
            I2y = (node, nodeC) -> node.v() - Math.sqrt(G / nodeC.h()) * node.h(),
            I3y = (node, _) -> node.u();

    @Override
    public Node getExtrapolatedNodeX(
            Node nodeL, Node newNodeLC, Node[] limitersL,
            Node nodeR, Node newNodeRC, Node[] limitersR
    ) {
        double
                extrapolatedFromLeftI1 = I1x.limitedExtrapolateBy(nodeL, newNodeLC, limitersL),
                extrapolatedFromRightI2 = I2x.limitedExtrapolateBy(nodeR, newNodeRC, limitersR),
                h = (extrapolatedFromLeftI1 - extrapolatedFromRightI2) /
                        (Math.sqrt(G / newNodeLC.h()) + Math.sqrt(G / newNodeRC.h())),
                u = (Math.sqrt(newNodeLC.h()) * extrapolatedFromLeftI1 + Math.sqrt(newNodeRC.h()) * extrapolatedFromRightI2) /
                        (Math.sqrt(newNodeLC.h()) + Math.sqrt(newNodeRC.h())),
                v = u > 0 ?
                        I3x.limitedExtrapolateBy(nodeL, newNodeLC, limitersL) :
                        I3x.limitedExtrapolateBy(nodeR, newNodeRC, limitersR);
        return new Node(h, u, v);
    }

    @Override
    public Node getExtrapolatedNodeY(
            Node nodeB, Node newNodeBC, Node[] limitersB,
            Node nodeT, Node newNodeTC, Node[] limitersT
    ) {
        double
                extrapolatedFromBottomI1 = I1y.limitedExtrapolateBy(nodeB, newNodeBC, limitersB),
                extrapolatedFromTopI2 = I2y.limitedExtrapolateBy(nodeT, newNodeTC, limitersT),
                h = (extrapolatedFromBottomI1 - extrapolatedFromTopI2) /
                        (Math.sqrt(G / newNodeBC.h()) + Math.sqrt(G / newNodeTC.h())),
                v = (Math.sqrt(newNodeBC.h()) * extrapolatedFromBottomI1 + Math.sqrt(newNodeTC.h()) * extrapolatedFromTopI2) /
                        (Math.sqrt(newNodeBC.h()) + Math.sqrt(newNodeTC.h())),
                u = v > 0 ?
                        I3y.limitedExtrapolateBy(nodeB, newNodeBC, limitersB) :
                        I3y.limitedExtrapolateBy(nodeT, newNodeTC, limitersT);
        return new Node(h, u, v);
    }
}
