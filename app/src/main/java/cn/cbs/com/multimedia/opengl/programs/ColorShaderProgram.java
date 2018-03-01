package cn.cbs.com.multimedia.opengl.programs;

import android.content.Context;

import cn.cbs.com.multimedia.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by cbs on 2018/2/24.
 */

public class ColorShaderProgram extends ShaderProgram {

    //uniform locations
    private final int uMatrixLocation;
    private final int uColorLocation;

    //Attribute locations
    private final int aPositionLocation;
//    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.vertex_shader, R.raw.fragment_shader);

        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
//        aColorLocation = glGetAttribLocation(mProgram, A_COLOR);
        uColorLocation = glGetUniformLocation(mProgram, U_COLOR);
    }

    public void setUniforms(float[] matrix,float r,float g,float b) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform4f(uColorLocation, r, g, b, 1f);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

//    public int getColorAttributeLocation() {
//        return aColorLocation;
//    }
}
