package com.lin.proxymedia;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by linchen on 2018/4/1.
 * mail: linchen@sogou-inc.com
 */

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";
    public static int compileVertexShader(String shader) {

        return   compileShader(GLES20.GL_VERTEX_SHADER,shader);
    }
    public static int compileFragmentShader(String shader) {

      return   compileShader(GLES20.GL_FRAGMENT_SHADER,shader);

    }

    private static int compileShader(int type, String shaderCode) {
         int shaderObjId = GLES20.glCreateShader(type);
        if (shaderObjId == 0) {
            return 0;
        }
        GLES20.glShaderSource(shaderObjId, shaderCode);//将gsls代码写入源码中，与shaderId关联起来
        GLES20.glCompileShader(shaderObjId);
         int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        //0的话就是compile失败的
        if (compileStatus[0] == 0) {
            //删除shader
            GLES20.glDeleteShader(shaderObjId);
            return 0;
        }
        return shaderObjId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
          int proId=GLES20.glCreateProgram();
        if (proId == 0) {
            return 0;
        }
        GLES20.glAttachShader(proId, vertexShaderId);
        GLES20.glAttachShader(proId, fragmentShaderId);
        GLES20.glLinkProgram(proId);
         int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(proId, GLES20.GL_LINK_STATUS, linkStatus,0);
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(proId);
            return 0;
        }
        return proId;
    }

    public static boolean validateProgram(int programId) {
        GLES20.glValidateProgram(programId);
        int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, GLES20.glGetProgramInfoLog(programId));
        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource
            , String fragmentShaderSource) {
        int vertextShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        int fragShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);
        int program = linkProgram(vertextShader, fragShader);
        boolean canUse = validateProgram(program);
//        if (canUse) {
//
//        }
        return program;
    }
}
