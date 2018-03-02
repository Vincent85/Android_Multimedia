#extension GL_OES_EGL_image_external;

precision mediump float;

varying vec2 vTexureCoord;
uniform sampleExternalOES sTexture;

void main()
{
    gl_FragColor = texture2D(sTexture,vTextureCoord);
}