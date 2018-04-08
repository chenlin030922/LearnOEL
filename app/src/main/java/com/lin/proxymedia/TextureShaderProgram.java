package com.lin.proxymedia;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by linchen on 2018/4/8.
 * mail: linchen@sogou-inc.com
 */

public class TextureShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int aPositionLocation;
    private final int aTextureCoordinateLocation;

    public TextureShaderProgram(Context context) {
        super(context, "texture_vertex_shader.glsl",
                "texture_fragment_shader.glsl");
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinateLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);

    }

    public void setUniforms(float[] matrix, int textureId) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getaTextureCoordinateLocation() {
        return aTextureCoordinateLocation;
    }


}
