package cn.cbs.com.multimedia.opengl.objects;


import java.util.List;

import cn.cbs.com.multimedia.opengl.data.VertexArray;
import cn.cbs.com.multimedia.opengl.programs.ColorShaderProgram;

/**
 * Created by cbs on 2018/2/24.
 */

public class Mallet {

    private static final int POSITION_COMPONENT_COUNT  = 3;

    private final float radius;
    private final float height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(
                new Geometry.Point(0f, 0f, 0f), radius, height, numPointsAroundMallet);

        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand command : drawList) {
            command.draw();
        }
    }

    public float getHeight() {
        return height;
    }
}
