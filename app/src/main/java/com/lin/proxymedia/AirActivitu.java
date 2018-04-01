package com.lin.proxymedia;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirActivitu extends AppCompatActivity {
    private GLSurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new GLSurfaceView(this);
        setContentView(mSurfaceView);
        mSurfaceView.setEGLContextClientVersion(2);//GL 2
        mSurfaceView.setRenderer(new SRenderer());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
    }

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) *
            BYTES_PER_FLOAT;
    String vertexShaderSource;
    String fragmentShaderSource;
    private int mProgramObjId;

    private class SRenderer implements GLSurfaceView.Renderer {
        private static final String A_COLOR = "a_Color";//与glsl文件对应
        private static final String A_POSITION = "a_Position";//与glsl文件对应
        private int aPositionLocation;
        private int aColorLocation;
        //        private int uColorLocation;
        float[] tableVerticesWithTraingles = {
                0f, 0f,        1f, 1f, 1f,
                - 0.5f, -0.5f, 0.3f, 0.7f, 0.7f,
                0.5f, -0.5f,   0.4f, 0.7f, 0.7f,
                0.5f, 0.5f,    0.7f, 0.1f, 1f,
                -0.5f, 0.5f,   1f, 0.7f, 0.7f,
                -0.5f, -0.5f,  0.7f, 0.2f, 0.2f,
                //line
                -0.5f, 0f,     1f, 0f, 0f,
                0.5f, 0f,      1f, 0f, 0f,
                //Mallets
                0f, -0.25f,    0f, 0f, 1f,
                0f, 0.25f,     1f, 0f, 0f};
        private final FloatBuffer vertexData;

        public SRenderer() {
            vertexData = ByteBuffer.allocateDirect(tableVerticesWithTraingles.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();

            fragmentShaderSource = Utils.readAssertResource(AirActivitu.this, "simple_fragment_shader.glsl");
            vertexShaderSource = Utils.readAssertResource(AirActivitu.this, "simple_vertex_shader.glsl");
            vertexData.put(tableVerticesWithTraingles);
        }

        /**
         * 三种情况触发
         * 1.第一次创建
         * 2.设备重新被唤醒
         * 3.重新返回这个Act
         *
         * @param gl
         * @param config
         */
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0, 0, 0, 0);
            int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
            int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
            mProgramObjId = ShaderHelper.linkProgram(vertexShader, fragmentShader);
            GLES20.glUseProgram(mProgramObjId);

            aPositionLocation = GLES20.glGetAttribLocation(mProgramObjId, A_POSITION);
            vertexData.position(0);//aPositionLocation起点
            GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                    false, STRIDE, vertexData);
            GLES20.glEnableVertexAttribArray(aPositionLocation);

            aColorLocation = GLES20.glGetAttribLocation(mProgramObjId, A_COLOR);
            vertexData.position(POSITION_COMPONENT_COUNT);//aColorLocation起点
            GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
                    false, STRIDE, vertexData);
            GLES20.glEnableVertexAttribArray(aColorLocation);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            //该方法相当于设置画笔的颜色：色值，红，绿，蓝
            //因为色值已经在数组中说明了，所以，不需要人工画上去了
//            GLES20.glUniform4f(uColorLocation, 1f, 1f, 1f, 1f);
            /**
             * 画三角形，读取6个点,即tableVerticesWithTraingles的6个Vertex
             在上面glVertexAttribPointer（）我们告诉系统一个顶点使用两个值，即POSTION_COMPOENT_COUNT=2，
             所以一个Vertex是(0f,0f)
             画三角形
             GL_TRIANGLE_FAN和GL_TRIANGLES是有区别的
             */
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
//            GLES20.glUniform4f(uColorLocation, 1f, 0f, 0f, 1f);
         //   画线
            GLES20.glDrawArrays(GLES20.GL_LINES,6,2);
            //画mallets
//            GLES20.glUniform4f(uColorLocation, 0f, 0f, 1f, 1f);
            GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);
//            GLES20.glUniform4f(uColorLocation, 0.5f, 0.5f, 0.3f, 1f);
            GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);
        }
    }


}
