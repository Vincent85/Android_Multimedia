package cn.cbs.com.multimedia.opengl.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.cbs.com.multimedia.R;
import cn.cbs.com.multimedia.opengl.objects.Mallet;
import cn.cbs.com.multimedia.opengl.objects.Puck;
import cn.cbs.com.multimedia.opengl.objects.Table;
import cn.cbs.com.multimedia.opengl.programs.ColorShaderProgram;
import cn.cbs.com.multimedia.opengl.programs.TextureShaderProgram;
import cn.cbs.com.multimedia.util.MatrixHelper;
import cn.cbs.com.multimedia.util.TextureHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

/**
 * Created by cbs on 2018/2/24.
 */

public class TextureRenderer implements GLSurfaceView.Renderer {

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjetionMatrix = new float[16];

    private final Context mContext;

    private Puck mPuck;
    private Table mTable;
    private Mallet mMallet;

    private TextureShaderProgram mTextureShaderProgram;
    private ColorShaderProgram mColorShaderProgram;

    private int mTexture;

    private final float[] modelMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];

    public TextureRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        mTable = new Table();
        mMallet = new Mallet(0.08f, 0.15f, 32);
        mPuck = new Puck(0.06f, 0.02f, 32);

        mTextureShaderProgram = new TextureShaderProgram(mContext);
        mColorShaderProgram = new ColorShaderProgram(mContext);

        mTexture = TextureHelper.loadTexture(mContext, R.drawable.air_hockey_surface);
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

        setLookAtM(viewMatrix, 0,
                0f, 1.5f, 2.5f,
                0f, 0f, 0f,
                0f, 1f, 0f);
//        setIdentityM(modelMatrix, 0);
//        translateM(modelMatrix, 0, 0f, 0f, -3f);
//        //Add rotation
//        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
//
//        //multiply the model matrix and the projection matrix
//        final float[] temp = new float[16];
//        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        //draw the table
        positionTableInScene();
        mTextureShaderProgram.useProgram();
        mTextureShaderProgram.setUniforms(modelViewProjetionMatrix, mTexture);
        mTable.bindData(mTextureShaderProgram);
        mTable.draw();

        //draw the first mallet
        positionObjectInScene(0f, mMallet.getHeight() / 2f, -0.4f);
        mColorShaderProgram.useProgram();
        mColorShaderProgram.setUniforms(modelViewProjetionMatrix, 0f, 0f, 1f);
        mMallet.bindData(mColorShaderProgram);
        mMallet.draw();

        //draw the second mallet
        positionObjectInScene(0f, mMallet.getHeight() / 2, 0.4f);
        mColorShaderProgram.setUniforms(modelViewProjetionMatrix, 0f, 0f, 1f);
        mMallet.draw();

        //draw the puck
        positionObjectInScene(0f, mPuck.height / 2, 0f);
        mColorShaderProgram.setUniforms(modelViewProjetionMatrix, 0.8f, 0.8f, 1f);
        mPuck.bindData(mColorShaderProgram);
        mPuck.draw();

//        //Draw the table
//        mTextureShaderProgram.useProgram();
//        mTextureShaderProgram.setUniforms(projectionMatrix, mTexture);
//        mTable.bindData(mTextureShaderProgram);
//        mTable.draw();
//
//        //Draw the mallets
//        mColorShaderProgram.useProgram();
//        mColorShaderProgram.setUniforms(projectionMatrix);
//        mMallet.bindData(mColorShaderProgram);
//        mMallet.draw();
    }

    private void positionTableInScene() {
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(modelViewProjetionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjetionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }
}
