package com.lin.proxymedia;

import android.content.Context;
import android.opengl.GLES30;

/**
 * Created by linchen on 2018/7/15.
 * mail: linchen@sogou-inc.com
 */
public abstract class GLESImpl {
    protected Context mContext;
    protected int mProgram;

    public GLESImpl() {
        initProgram();
    }

    private void initProgram() {
        int vertexShader = getVertexShader();
        int fragmentShader = getFragmentShader();

        // create empty OpenGL ES Program
        mProgram = GLES30.glCreateProgram();

        // add the vertex shader to program
        GLES30.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES30.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES30.glLinkProgram(mProgram);
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }

    public abstract void onDraw();

    public abstract void onSurfaceChange(int width, int height);

    protected abstract int getVertexShader();

    protected abstract int getFragmentShader();

}
