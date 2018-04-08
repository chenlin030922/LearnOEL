package com.lin.proxymedia;

import android.opengl.GLES20;

import static com.lin.proxymedia.Constants.BYTES_PER_FLOAT;

/**
 * Created by linchen on 2018/4/8.
 * mail: linchen@sogou-inc.com
 */

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) *
            BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA={
            //order of coord:x,y,r,g,b
            0f,-0.4f,0f,0f,1f,
            0f,0.4f,1f,0f,0f
    };
    private final VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindDate(ColorShaderProgram program) {
        vertexArray.setVertexAttribPointer(0, program.getPositionAttributeLocation()
                , POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, program.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT, STRIDE);

    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,2);
    }


}
