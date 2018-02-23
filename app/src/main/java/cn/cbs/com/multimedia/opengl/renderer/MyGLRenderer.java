package cn.cbs.com.multimedia.opengl.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.cbs.com.multimedia.R;
import cn.cbs.com.multimedia.util.LoggerUtil;
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
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

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
    private int aColorLocation;
    private int aPositionLocation;

    private final FloatBuffer vertexData;

    private Context mContext;

    private int mProgramID;


    public MyGLRenderer(Context context) {
        mContext = context;
        float verticies[] = {
                //Triangle fan,Order of coordinates X,Y,R,G,B
                0.0f,0.0f,   1f,  1f,  1f,
                -0.5f,-0.5f, 0.7f,0.7f,0.7f,
                0.5f,-0.5f,  0.7f,0.7f,0.7f,
                0.5f,0.5f,   0.7f,0.7f,0.7f,
                -0.5f,0.5f,  0.7f,0.7f,0.7f,
                -0.5f,-0.5f, 0.7f,0.7f,0.7f,

                // line
                -0.5f,0.0f,  1f,0f,0f,
                0.5f,0f,     1f,0f,0f,

                //mallets
                0f,-0.25f,   0f,0f,1f,
                0f,0.25f,    1f,0f,0f
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

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glDrawArrays(GL_LINES, 6, 2);

        glDrawArrays(GL_POINTS, 8, 1);

        glDrawArrays(GL_POINTS, 9, 1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);


    }
}
