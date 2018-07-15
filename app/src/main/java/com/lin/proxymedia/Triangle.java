package com.lin.proxymedia;

import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by linchen on 2018/3/21.
 * mail: linchen@sogou-inc.com
 */

public class Triangle {
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ByteBuffer indicateBuffer;
    private final float[] projectionMatrix = new float[16];
    private final String vertexShaderCode =
            "#version 300 es  \n" +
                    "layout(location = 0) in vec4 vPosition;\n" +
                    "layout(location = 1) in vec4 aColor;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "out vec4 vColor;" +
                    "void main() {\n" +
                    "gl_Position = aMatrix*vPosition;\n" +
                    "vColor=aColor;\n" +
                    "}";

    private final String fragmentShaderCode =
            "#version 300 es  \n" +
                    "precision mediump float;\n" +
                    "out vec4 fragColor;\n" +
                    "in vec4 vColor;" +
                    "void main() {\n" +
                    "fragColor = vColor;\n" +
                    "}";
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,

            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,

    };
    private final int mProgram;
    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            1.0f, 0.5f, 0.0f,
            1.0f, 0.5f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f
    };
    byte indicates[] = {
            //上
            0, 1, 2,
            1, 2, 3,
            //下
            4, 5, 6,
            5, 6, 7,
            //左
            2, 3, 6,
            3, 6, 7,
            //右
            0, 1, 4,
            1, 4, 5,
            //前
            0, 2, 4,
            2, 4, 6,
            //后
            1, 3, 7,
            1, 7, 5


    };

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }

    public Triangle() {
        indicateBuffer = ByteBuffer.allocate(indicates.length);
        indicateBuffer.put(indicates);
        indicateBuffer.position(0);
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        ByteBuffer bb1 = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                color.length * 4);
        // use the device hardware's native byte order
        bb1.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        colorBuffer = bb1.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        colorBuffer.put(color);
        // set the buffer to read the first coordinate
        colorBuffer.position(0);
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES30.glCreateProgram();

        // add the vertex shader to program
        GLES30.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES30.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES30.glLinkProgram(mProgram);
    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final int colorStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void onDraw() {
        // Add program to OpenGL ES environment

//        Matrix.translateM(projectionMatrix,0,-0.5f,0f,0f);
        float[] rotateF = new float[16];
        Matrix.setIdentityM(rotateF, 0);
        Matrix.setRotateM(rotateF,0,rotateAgree,0.5f,0.5f,0f);

        Matrix.multiplyMM(projectionMatrix,0,orthMatrix,0,rotateF,0);
        rotateAgree+=1;
        if (rotateAgree >=360) {
            rotateAgree=0;
        }
        GLES30.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
//        createVertextBuffer();
        // Enable a handle to th、e triangle vertices
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the triangle coordinate data
        GLES30.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                0, vertexBuffer);
        // get handle to fragment shader's vColor member
        mColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
        int aMatrixPos = GLES30.glGetUniformLocation(mProgram, "aMatrix");
        GLES30.glUniformMatrix4fv(aMatrixPos,1,false,projectionMatrix,0);

        // Set color for drawing the triangle
//        GLES30.glVertexAttrib4fv(mColorHandle,  color, 0);

        GLES30.glEnableVertexAttribArray(mColorHandle);
        GLES30.glVertexAttribPointer(mColorHandle, 3,
                GLES30.GL_FLOAT, false,
                0, colorBuffer);
        // Draw the triangle
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indicates.length,
                GLES30.GL_UNSIGNED_BYTE, indicateBuffer);

        // Disable vertex array
        GLES30.glDisableVertexAttribArray(mPositionHandle);
    }
    int rotateAgree=0;
    float[] orthMatrix = new float[16];
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

    private void createVertextBuffer() {
        int[] value = new int[1];
        GLES30.glGenBuffers(1, value, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, value[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);
    }

    private void createElementBuffer() {

    }
}
