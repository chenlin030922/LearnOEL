package com.lin.proxymedia;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by linchen on 2018/3/21.
 * mail: linchen@sogou-inc.com
 */

public class Square {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;
    // number of coordinates per vertex in this array

    protected Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    //使用一个绘制列表来告诉OpenGL ES图形处理流程应该如何画这些顶点,0对应squareCoords数组中的0位置点，以此类推
    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    private static final String VERTEX_SHADER =
            "attribute vec4 vPosition;\n"
                    + "void main() {\n"
                    + " gl_Position = vPosition;\n"
                    + "}";
    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n"
                    +"uniform vec4 vColor;\n"
                    + "void main() {\n"
                    + " gl_FragColor = vColor;\n"
                    + "}";
    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        //创建 GLSL 程序
        mProgram = GLES20.glCreateProgram();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                VERTEX_SHADER);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                FRAGMENT_SHADER);
        //attatch shader 代码
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        //链接 GLSL 程序
        GLES20.glLinkProgram(mProgram);

    }
    int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        //加载 shader 代码
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
    float[] color =new float[]{0.4f,0.4f,0.8f,1.0f,
            0.2f,0.2f,0.4f,1.0f,
            0.1f,0.1f,0.1f,1.0f};
    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    private int mPositionHandle;
    private int mColorHandle;
    public void onDraw(){
        //使用 GLSL 程序
        GLES20.glUseProgram(mProgram);
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
