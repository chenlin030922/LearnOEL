package com.lin.proxymedia;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by linchen on 2018/4/8.
 * mail: linchen@sogou-inc.com
 */

public class ShaderProgram {
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected int program;

    protected ShaderProgram(Context context, String vertexShader, String fragShader) {
        program=ShaderHelper.buildProgram(
                Utils.readAssertResource(context,vertexShader),
                Utils.readAssertResource(context,fragShader));
    }
    public void useProgram(){
        GLES20.glUseProgram(program);
    }
}
