package com.lin.proxymedia;

/**
 * Created by linchen on 2018/4/8.
 * mail: linchen@sogou-inc.com
 */

public class ObjectBuilder {
   private static final int FLOATS_PER_VERTEXT=3;
   private final float[] vertexData;
   private int offset=0;

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEXT];
    }

    private static int sizeOfCirlceInVetices(int numPoints) {
        return 1+numPoints+1;
    }

    private static int sizeofOpenCylinderInVertices(int numPoints) {
        return (numPoints+1)*2;
    }

    static GenerateData createPuck(Geometry.Cyclinder puck, int numPoints) {
        int size = sizeOfCirlceInVetices(numPoints) + sizeofOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);
        Geometry.Circle puckTop = new Geometry.Circle(puck.center.traslateY(puck.height / 2f), puck.radius);

    }

    private void appendCircle(Geometry.Circle circle, int numPoints) {

    }
}
