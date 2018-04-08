package com.lin.proxymedia;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by linchen on 2018/4/8.
 * mail: linchen@sogou-inc.com
 */

public class ColorShaderProgram extends ShaderProgram {
    private final  int uMatrixLocation;
    private final int aPositionLocation;;
    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context,"simple_vertex_shader.glsl",
                "simple_fragment_shader.glsl");
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
    }
    public void setUniforms(float[] matrix) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }
    public int getColorAttributeLocation(){
        return aColorLocation;
    }

}
