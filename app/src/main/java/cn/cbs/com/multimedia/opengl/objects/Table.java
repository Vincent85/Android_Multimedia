package cn.cbs.com.multimedia.opengl.objects;

import cn.cbs.com.multimedia.opengl.data.VertexArray;
import cn.cbs.com.multimedia.opengl.programs.TextureShaderProgram;
import cn.cbs.com.multimedia.util.Constants;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by cbs on 2018/2/24.
 */

public class Table {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            //order of coordinates: X,Y,S,T
            //Triangle fan
            0f,   0f,      0.5f,0.5f,
            -0.5f,-0.8f,   0f,0.9f,
            0.5f, -0.8f,   1f,0.9f,
            0.5f, 0.8f,    1f,0.1f,
            -0.5f,0.8f,    0f,0.1f,
            -0.5f,-0.8f,   0f,0.9f
    };

    private final VertexArray mVertexArray;

    public Table() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        mVertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        mVertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLE_FAN,0,6);
    }
}
