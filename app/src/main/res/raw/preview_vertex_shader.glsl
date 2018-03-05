
uniform mat4 uMVPMatrix;
uniform mat4 uTexMatrix;

attribute vec4 aPosition;
attribute vec2 aTextureCoord;

varying vec2 vTextureCoord;

void main()
{
    gl_Position = uMVPMatrix * aPosition;
    vTextureCoord = vec2(1.0 - aTextureCoord.x,aTextureCoord.y);
    //vTextureCoord = aTextureCoord;
}
