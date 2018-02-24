package cn.cbs.com.multimedia.util;

import android.util.Log;

import java.util.logging.Logger;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Created by cbs on 2018/2/23.
 */

public class ShaderHelper {

    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(shaderCode, GL_VERTEX_SHADER);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private static int compileShader(String shaderCode,int shaderType) {
        final int shaderObjectID = glCreateShader(shaderType);
        if (shaderObjectID == 0) {
            if (LoggerUtil.DEBUG) {
                Log.w(TAG,"Could not create new shader");
            }
            return 0;
        }

        glShaderSource(shaderObjectID, shaderCode);

        glCompileShader(shaderObjectID);

        final int[] status = new int[1];
        glGetShaderiv(shaderObjectID, GL_COMPILE_STATUS, status, 0);
        if(LoggerUtil.DEBUG) {
            Log.d(TAG, "result of compiling status: \n" + shaderCode +
                    "\n" + glGetShaderInfoLog(shaderObjectID));
        }

        if (status[0] == 0) {
            glDeleteShader(shaderObjectID);
            if (LoggerUtil.DEBUG) {
                Log.w(TAG, "Compilation failed.");
            }
            return 0;
        }

        return shaderObjectID;
    }

    public static int linkProgram(int vertexShader, int fragmentShader) {
        final int programObjectID = glCreateProgram();

        if (programObjectID == 0) {
            if (LoggerUtil.DEBUG) {
                Log.w(TAG, "Could not create program");
            }
            return 0;
        }

        glAttachShader(programObjectID, vertexShader);
        glAttachShader(programObjectID, fragmentShader);

        glLinkProgram(programObjectID);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectID, GL_LINK_STATUS, linkStatus, 0);

        if (LoggerUtil.DEBUG) {
            Log.d(TAG, "Results of linking program:\n"
                    + glGetProgramInfoLog(programObjectID));
        }

        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectID);

            if (LoggerUtil.DEBUG) {
                Log.w(TAG, "Linking of program failed");
            }
            return 0;
        }

        return programObjectID;
    }

    public static boolean validateProgram(int programObjectID) {
        glValidateProgram(programObjectID);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectID, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.d(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog " + glGetProgramInfoLog(programObjectID));

        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;

        //Compile the shader
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        //Link them into a shader program
        program = linkProgram(vertexShader, fragmentShader);

        if (LoggerUtil.DEBUG) {
            validateProgram(program);
        }

        return program;
    }
}
