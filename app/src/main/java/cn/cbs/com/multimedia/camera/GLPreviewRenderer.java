package cn.cbs.com.multimedia.camera;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.cbs.com.multimedia.R;
import cn.cbs.com.multimedia.util.CameraManager;
import cn.cbs.com.multimedia.util.ShaderHelper;
import cn.cbs.com.multimedia.util.TextResourceReader;

import static android.opengl.GLES20.GL_FALSE;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by cbs on 2018/3/2.
 */

public class GLPreviewRenderer implements GLSurfaceView.Renderer {

    public static final String TAG = "GLPreviewRenderer";

    private static final int BYTES_PER_FLOAT = Float.SIZE / 8;

    private Context mContext;

    private int mProgram;
    private int[] mTextureIDs = new int[1];

    private int mVertexPosLoc;
    private int mTexPosLoc;
    private int mMVPMatrixLoc;
    private int mTexMatrixLoc;
    private int mTextureLoc;

    private String mVertexPosName = "aPosition";
    private String mTexPosName    = "aTextureCoord";
    private String mMVPMatrixName = "uMVPMatrix";
    private String mTexMatrixName = "uTexMatrix";
    private String mTextureName   = "sTexture";

    private float[] mMVPMatrix = new float[16];
    private float[] mTexMatrix = new float[16];

    private float[] mVertexPos = new float[] {
            -1.0f,-1.0f,
            -1.0f,1.0f,
            1.0f,1.0f,
            1.0f,-1.0f
    };

    private float[] mTexturePos = new float[] {
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,1.0f,
            1.0f,0.0f
    };

    public GLPreviewRenderer(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //gen texture id
        glGenTextures(1, mTextureIDs, 0);
        Log.d(TAG, "texture id = " + mTextureIDs[0]);
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromRes(mContext, R.raw.preview_vertex_shader),
                TextResourceReader.readTextFileFromRes(mContext, R.raw.preview_fragment_shader));

        glUseProgram(mProgram);

        //find the attribute location
        mVertexPosLoc = glGetAttribLocation(mProgram, mVertexPosName);
        mTexPosLoc = glGetAttribLocation(mProgram, mTexPosName);
        //find the uniform location
        mMVPMatrixLoc = glGetUniformLocation(mProgram, mMVPMatrixName);
        mTexMatrixLoc = glGetUniformLocation(mProgram, mTexMatrixName);
        mTextureLoc = glGetUniformLocation(mProgram, mTextureName);

        //bind and enable position attribute
        FloatBuffer positionBuffer = ByteBuffer
                .allocateDirect(mVertexPos.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mVertexPos);
        positionBuffer.position(0);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * BYTES_PER_FLOAT, positionBuffer);
        glEnableVertexAttribArray(mVertexPosLoc);

        //bind and enable texture attribute
        FloatBuffer textureBuffer = ByteBuffer
                .allocateDirect(mTexturePos.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mTexturePos);
        textureBuffer.position(0);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * BYTES_PER_FLOAT, textureBuffer);
        glEnableVertexAttribArray(mTexPosLoc);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);

        //set uniform matrix
        setIdentityM(mMVPMatrix,0);

        glUniformMatrix4fv(mMVPMatrixLoc, 1, false, mMVPMatrix, 0);
        glUniformMatrix4fv(mTexMatrixLoc, 1, false, mTexMatrix, 0);

        //bind texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GLES11Ext.GL_TEXTURE_BINDING_EXTERNAL_OES, mTextureIDs[0]);
        glUniform1i(mTextureLoc, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    public void setTexTransMatrix(float[] matrix) {
        mTexMatrix = matrix;
    }

    public int getTextureID() {
        return mTextureIDs[0];
    }

}
