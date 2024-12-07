package Extrapolators.InvariantsExtrapolator.Invariants;

import Extensions.Node;

import static Extensions.Constants.G;

public class LocalInvariantsSystem implements ExtrapolatableInvariantsSystem {
    private static final ExtrapolatableInvariant
            I1x = (node, nodeC) -> node.u() + Math.sqrt(G / nodeC.h()) * node.h(),
            I2x = (node, nodeC) -> node.u() - Math.sqrt(G / nodeC.h()) * node.h() ,
            I3x = (node, _) -> node.v(),
            I1y = (node, nodeC) -> node.v() + Math.sqrt(G / nodeC.h()) * node.h(),
            I2y = (node, nodeC) -> node.v() - Math.sqrt(G / nodeC.h()) * node.h(),
            I3y = (node, _) -> node.u();

    @Override
    public Node getExtrapolatedNodeX(Node nodeL, Node newNodeLC, Node nodeR, Node newNodeRC) {
        double
                extrapolatedFromLeftI1 = I1x.extrapolateBy(nodeL, newNodeLC),
                extrapolatedFromRightI2 = I2x.extrapolateBy(nodeR, newNodeRC),
                h = (extrapolatedFromLeftI1 - extrapolatedFromRightI2) /
                        (Math.sqrt(G / newNodeLC.h()) + Math.sqrt(G / newNodeRC.h())),
                u = (Math.sqrt(newNodeLC.h()) * extrapolatedFromLeftI1 + Math.sqrt(newNodeRC.h()) * extrapolatedFromRightI2) /
                        (Math.sqrt(newNodeLC.h()) + Math.sqrt(newNodeRC.h())),
                v = u > 0 ? I3x.extrapolateBy(nodeL, newNodeLC) : I3x.extrapolateBy(nodeR, newNodeRC);
        return new Node(h, u, v);
    }

    @Override
    public Node getExtrapolatedNodeY(Node nodeB, Node newNodeBC, Node nodeT, Node newNodeTC) {
        double
                extrapolatedFromBottomI1 = I1y.extrapolateBy(nodeB, newNodeBC),
                extrapolatedFromTopI2 = I2y.extrapolateBy(nodeT, newNodeTC),
                h = (extrapolatedFromBottomI1 - extrapolatedFromTopI2) /
                        (Math.sqrt(G / newNodeBC.h()) + Math.sqrt(G / newNodeTC.h())),
                v = (Math.sqrt(newNodeBC.h()) * extrapolatedFromBottomI1 + Math.sqrt(newNodeTC.h()) * extrapolatedFromTopI2) /
                        (Math.sqrt(newNodeBC.h()) + Math.sqrt(newNodeTC.h())),
                u = v > 0 ? I3y.extrapolateBy(nodeB, newNodeBC) : I3y.extrapolateBy(nodeT, newNodeTC);
        return new Node(h, u, v);
    }
}
