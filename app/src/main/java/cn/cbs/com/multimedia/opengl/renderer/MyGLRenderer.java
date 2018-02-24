package cn.cbs.com.multimedia.opengl.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.cbs.com.multimedia.R;
import cn.cbs.com.multimedia.util.LoggerUtil;
import cn.cbs.com.multimedia.util.MatrixHelper;
import cn.cbs.com.multimedia.util.ShaderHelper;
import cn.cbs.com.multimedia.util.TextResourceReader;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by cbs on 2018/2/23.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final String u_Matrix = "u_Matrix";
    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    private final FloatBuffer vertexData;
    private final float[] modelMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];

    private Context mContext;

    private int mProgramID;


    public MyGLRenderer(Context context) {
        mContext = context;
        float verticies[] = {
                //Triangle fan,Order of coordinates X,Y,R,G,B
                0.0f,0.0f,     1f,  1f,  1f,
                -0.5f,-0.8f,   0.7f,0.7f,0.7f,
                0.5f,-0.8f,    0.7f,0.7f,0.7f,
                0.5f,0.8f,     0.7f,0.7f,0.7f,
                -0.5f,0.8f,    0.7f,0.7f,0.7f,
                -0.5f,-0.8f,   0.7f,0.7f,0.7f,

                // line
                -0.5f,0.0f,  1f,0f,0f,
                0.5f,0f,     1f,0f,0f,

                //mallets
                0f,-0.4f,   0f,0f,1f,
                0f,0.4f,    1f,0f,0f
        };
        vertexData = ByteBuffer
                .allocateDirect(verticies.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(verticies);
    }


    @Override

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShader = TextResourceReader.readTextFileFromRes(mContext, R.raw.vertex_shader);
        String fragmentShader = TextResourceReader.readTextFileFromRes(mContext, R.raw.fragment_shader);

        mProgramID = ShaderHelper.linkProgram(ShaderHelper.compileVertexShader(vertexShader),
                ShaderHelper.compileFragmentShader(fragmentShader));

        if (LoggerUtil.DEBUG) {
            ShaderHelper.validateProgram(mProgramID);
        }

        glUseProgram(mProgramID);
        aPositionLocation = glGetAttribLocation(mProgramID, A_POSITION);
        aColorLocation = glGetAttribLocation(mProgramID, A_COLOR);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);

        uMatrixLocation = glGetUniformLocation(mProgramID, u_Matrix);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glDrawArrays(GL_LINES, 6, 2);

        glDrawArrays(GL_POINTS, 8, 1);

        glDrawArrays(GL_POINTS, 9, 1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

//        final float aspectRatio = width > height ? (float)width / (float)height
//                                                    : (float)height / (float)width;
//
//        if (width > height) {
//            //Landscape
//            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//        }else {
//            //portrait
//            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//        }
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
//        perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -3f);
        //Add rotation
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        //multiply the model matrix and the projection matrix
        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }
}
