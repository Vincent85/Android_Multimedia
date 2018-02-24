package cn.cbs.com.multimedia.opengl.programs;

import android.content.Context;

import cn.cbs.com.multimedia.util.ShaderHelper;
import cn.cbs.com.multimedia.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by cbs on 2018/2/24.
 */

public class ShaderProgram {

    //Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    //Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected final int mProgram;

    protected ShaderProgram(Context context, int vertexShaderResourceID,
                            int fragmentShaderResourceID) {
        //Compile the shdaers and link the program
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromRes(context, vertexShaderResourceID),
                TextResourceReader.readTextFileFromRes(context, fragmentShaderResourceID));

    }

    public void useProgram() {
        glUseProgram(mProgram);
    }
}
