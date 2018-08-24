package com.lin.proxymedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by linchen on 2018/3/21.
 * mail: linchen@sogou-inc.com
 */

public class Triangle extends GLESImpl {
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ByteBuffer indicateBuffer;
    private final float[] projectionMatrix = new float[16];
    private final String vertexShaderCode =
            "#version 300 es  \n" +
                    "layout(location = 0) in vec4 vPosition;\n" +
                    "layout(location = 1) in vec4 aColor;\n" +
                    "layout(location=3) in vec2 texCoord;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "out vec4 vColor;" +
                    "out vec2 vTextCoord;\n" +
                    "void main() {\n" +
                    "gl_Position = aMatrix*vPosition;\n" +
                    "vColor=aColor;\n" +
                    "vTextCoord=texCoord;\n" +
                    "}";

    private final String fragmentShaderCode =
            "#version 300 es  \n" +
                    "precision mediump float;\n" +
                    "out vec4 fragColor;\n" +
                    "in vec4 vColor;" +
                    "in vec2 vTextCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
//                    "fragColor = vColor;\n" +
                    "fragColor=texture(s_texture,vTextCoord);" +
                    "}";
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
            //上面
            0.5f, 0.5f, 0.5f, //0
            0.5f, 0.5f, -0.5f,//1
            -0.5f, 0.5f, 0.5f,//2
            -0.5f, 0.5f, -0.5f,//3
            //下面
            0.5f, -0.5f, 0.5f,//4
            0.5f, -0.5f, -0.5f,//5
            -0.5f, -0.5f, 0.5f,//6
            -0.5f, -0.5f, -0.5f,//7
            //前面0,2,4,6
            0.5f, 0.5f, 0.5f,//8
            -0.5f, 0.5f, 0.5f,//9
            0.5f, -0.5f, 0.5f,//10
            -0.5f, -0.5f, 0.5f,//11
            //后面1,3,7,5
            0.5f, 0.5f, -0.5f,//12
            -0.5f, 0.5f, -0.5f,//13
            0.5f, -0.5f, -0.5f,//15
            -0.5f, -0.5f, -0.5f,//14
            //左面2,3,6,7
            -0.5f, 0.5f, 0.5f,//16
            -0.5f, 0.5f, -0.5f,//17
            -0.5f, -0.5f, 0.5f,//18
            -0.5f, -0.5f, -0.5f,//19
            //右面0,1,4,5
            0.5f, 0.5f, 0.5f,//20
            0.5f, 0.5f, -0.5f,//21
            0.5f, -0.5f, 0.5f,//22
            0.5f, -0.5f, -0.5f//23
    };

    float[] textCoord = {
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f
    };
    byte indicates[] = {
            //上
            0, 1, 2,
            1, 2, 3,
            //下
            4, 5, 6,
            5, 6, 7,
            //左
            16, 17, 18,
            17, 18, 19,
            //右
            20, 21, 22,
            21, 22, 23,
            //前
            8, 9, 10,
            9, 10, 11,
            //后
            12, 13, 14,
            13, 14, 15


    };
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
    private FloatBuffer textBuffer;

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
        super();
        indicateBuffer = BufferUtils.geBuffer(indicates);
        // initialize vertex byte buffer for shape coordinates
        vertexBuffer = BufferUtils.getFloatBuffer(triangleCoords);
        colorBuffer = BufferUtils.getFloatBuffer(color);
        textBuffer = BufferUtils.getFloatBuffer(textCoord);

    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final int colorStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private int textId = -1;

    public void onDraw() {
        // Add program to OpenGL ES environment

//        Matrix.translateM(projectionMatrix,0,-0.5f,0f,0f);
        float[] rotateF = new float[16];
        Matrix.setIdentityM(rotateF, 0);
        Matrix.setRotateM(rotateF, 0, rotateAgree, 0.5f, 0.5f, 0f);

        Matrix.multiplyMM(projectionMatrix, 0, orthMatrix, 0, rotateF, 0);
        rotateAgree += 1;
        if (rotateAgree >= 360) {
            rotateAgree = 0;
        }
        GLES30.glUseProgram(mProgram);
        if (textId == -1) {
            int texCoord = GLES30.glGetAttribLocation(mProgram, "texCoord");
            GLES30.glEnableVertexAttribArray(texCoord);
            GLES30.glVertexAttribPointer(texCoord, 2, GLES30.GL_FLOAT, false, 0, textBuffer);
            int textureId = loadTexture(mContext, R.drawable.aa);
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            textId=textureId;
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
            int uTextureUnitLocation = GLES30.glGetUniformLocation(mProgram, "s_texture");
            GLES30.glUniform1i(uTextureUnitLocation, 0);
        }
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
        GLES30.glUniformMatrix4fv(aMatrixPos, 1, false, projectionMatrix, 0);

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

    int rotateAgree = 0;
    float[] orthMatrix = new float[16];

    public void onSurfaceChange(int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        final float aspectRatio = width > height ?
                ((float) width / (float) height)
                : ((float) height / (float) width);

        //按比例投放
        Matrix.setIdentityM(orthMatrix, 0);
        if (width > height) {
            Matrix.orthoM(orthMatrix, 0, -aspectRatio, aspectRatio,
                    -1f, 1f, -1f, 1f);
        } else {
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

    private void createVertextBuffer() {
        int[] value = new int[1];
        GLES30.glGenBuffers(1, value, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, value[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);
    }

    private Bitmap mBitmap;

    public int loadTexture(Context context, int resId) {
        int[] textureObjIds = new int[1];
        GLES30.glGenTextures(1, textureObjIds, 0);
        if (textureObjIds[0] == 0) {
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
            if (mBitmap == null) {
                GLES30.glDeleteTextures(1, textureObjIds, 0);
                return 0;
            }
        }
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureObjIds[0]);//bind
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, mBitmap, 0);
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);//unbind
        return textureObjIds[0];
    }
}
