package com.lin.proxymedia;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by linchen on 2018/4/8.
 * mail: linchen@sogou-inc.com
 */

public class TextureRenderer implements GLSurfaceView.Renderer {
    private Context mContext;
    private final float[] projectionMatrix = new float[16];//4*4矩阵
    final float[] modelMatrix = new float[16];
    private Table mTable;
    private Mallet mMallet;
    private TextureShaderProgram mTextureShaderProgram;
    private ColorShaderProgram mColorShaderProgram;
    private int texture;
    public TextureRenderer(Context context) {
        mContext=context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 0);
        mTable = new Table();
        mMallet = new Mallet();
        mTextureShaderProgram = new TextureShaderProgram(mContext);
        mColorShaderProgram = new ColorShaderProgram(mContext);
        texture = TextureHelper.loadTexture(mContext, R.drawable.timg);
        Log.e("aaa","");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        MatrixHelper.perspectiveM(projectionMatrix, 45,
                ((float) width) / ((float) height), 1f, 10f);
//            //初始化一个Matrix
        Matrix.setIdentityM(modelMatrix,0);
        float[] temp = new float[16];
        Matrix.translateM(modelMatrix,0,0,0,-2.6f);
        Matrix.rotateM(modelMatrix,0,-60f,1f,0,0);
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //Draw the table
        mTextureShaderProgram.useProgram();
        mTextureShaderProgram.setUniforms(projectionMatrix, texture);
        mTable.bindData(mTextureShaderProgram);
        mTable.draw();

        //Draw the mallet
        mColorShaderProgram.useProgram();
        mColorShaderProgram.setUniforms(projectionMatrix);
        mMallet.bindDate(mColorShaderProgram);
        mMallet.draw();
    }
}
