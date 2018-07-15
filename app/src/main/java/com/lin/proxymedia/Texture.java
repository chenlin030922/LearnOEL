package com.lin.proxymedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

/**
 * Created by linchen on 2018/7/15.
 * mail: linchen@sogou-inc.com
 */
public class Texture extends GLESImpl {

    private float[] triangleCoords = {
            0.5f, -0.5f,0f,
            -0.5f, -0.5f,0f,
            -0.5f, 0.5f,0f,
            0.5f, 0.5f,0f

    };
    private float[] colors={
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.8f,
            1.0f, 0.5f, 0.0f,
            1.0f, 0.5f, 0.3f
    };
    private float[] textCoords={
            0f,1.0f,
            1f,1f,
            1.0f,0.0f,
            0.0f,0f,
    };
    private FloatBuffer vertexBuffer;
    private final String vertexShaderCode =
            "#version 300 es  \n" +
                    "layout(location = 0) in vec4 vPosition;\n" +
                    "layout(location = 1) in vec4 aColor;\n" +
                    "layout(location=3) in vec2 texCoord;\n"+
//                    "uniform mat4 aMatrix;\n" +
                    "out vec4 vColor;" +
                    "out vec2 vTextCoord;\n" +
                    "void main() {\n" +
                    "gl_Position = vPosition;\n" +
                    "vColor=aColor;\n" +
                    "vTextCoord=texCoord;\n" +
                    "}";

    private final String fragmentShaderCode =
            "#version 300 es  \n" +
                    "precision mediump float;\n" +
                    "out vec4 fragColor;\n" +
                    "in vec4 vColor;" +
                    "in vec2 vTextCoord;\n" +
                    "uniform sampler2D s_texture;\n"+
                    "void main() {\n" +
//                    "fragColor = vColor;\n" +
                    "fragColor=texture(s_texture,vTextCoord);"+
                    "}";
    float[] orthMatrix = new float[16];
    private int mColorHandle;
    private FloatBuffer colorBuffer;
    private FloatBuffer textBuffer;
    public Texture() {
        super();
        vertexBuffer = BufferUtils.getFloatBuffer(triangleCoords);
        colorBuffer = BufferUtils.getFloatBuffer(colors);
        textBuffer = BufferUtils.getFloatBuffer(textCoords);
    }
    int rotateAgree=0;
    int mPositionHandle;
    private final float[] projectionMatrix = new float[16];
    @Override
    public void onDraw() {
        float[] rotateF = new float[16];
        Matrix.setIdentityM(rotateF, 0);
        Matrix.setRotateM(rotateF,0,rotateAgree,0.5f,0.5f,0f);

        Matrix.multiplyMM(projectionMatrix,0,orthMatrix,0,rotateF,0);
        rotateAgree+=1;
        if (rotateAgree >=360) {
            rotateAgree=0;
        }

        GLES30.glUseProgram(mProgram);
        int texCoord = GLES30.glGetAttribLocation(mProgram, "texCoord");
        GLES30.glEnableVertexAttribArray(texCoord);
        GLES30.glVertexAttribPointer(texCoord, 2, GLES30.GL_FLOAT, false, 0, textBuffer);
        int textureId = loadTexture(mContext, R.drawable.aa);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        int uTextureUnitLocation = GLES30.glGetUniformLocation(mProgram, "s_texture");
        GLES30.glUniform1i(uTextureUnitLocation, 0);

        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
//        createVertextBuffer();
        // Enable a handle to th、e triangle vertices
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the triangle coordinate data
        GLES30.glVertexAttribPointer(mPositionHandle, 3,
                GLES30.GL_FLOAT, false,
                0, vertexBuffer);
        int aMatrixPos = GLES30.glGetUniformLocation(mProgram, "aMatrix");
        GLES30.glUniformMatrix4fv(aMatrixPos,1,false,
                projectionMatrix,0);

        // Set color for drawing the triangle
//        GLES30.glVertexAttrib4fv(mColorHandle,  color, 0);

        // Draw the triangle
      GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN,0,triangleCoords.length/3);

        // Disable vertex array
        GLES30.glDisableVertexAttribArray(mPositionHandle);
    }

    @Override
    public void onSurfaceChange(int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        final float aspectRatio = width > height ?
                ((float) width / (float) height)
                : ((float) height / (float) width);

        //按比例投放
        Matrix.setIdentityM(orthMatrix, 0);
        if (width > height) {
            Matrix.orthoM(orthMatrix,0,-aspectRatio,aspectRatio,
                    -1f,1f,-1f,1f);
        }else {
            Matrix.orthoM(orthMatrix, 0, -1f, 1f,
                    -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    @Override
    protected int getVertexShader() {
        return loadShader(GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
    }

    @Override
    protected int getFragmentShader() {
        return loadShader(GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
    }

    public static int loadTexture(Context context, int resId) {
        int[] textureObjIds = new int[1];
        GLES30.glGenTextures(1, textureObjIds, 0);
        if (textureObjIds[0] == 0) {
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        if (bitmap == null) {
            GLES30.glDeleteTextures(1, textureObjIds, 0);
            return 0;
        }
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureObjIds[0]);//bind
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);//unbind
        return textureObjIds[0];
    }

}
