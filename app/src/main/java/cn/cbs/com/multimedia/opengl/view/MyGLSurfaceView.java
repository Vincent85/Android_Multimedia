package cn.cbs.com.multimedia.opengl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import cn.cbs.com.multimedia.opengl.renderer.MyGLRenderer;
import cn.cbs.com.multimedia.opengl.renderer.TextureRenderer;

/**
 * Created by cbs on 2018/2/23.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    private Renderer mRenderer;

    public MyGLSurfaceView(Context context) {
        this(context,null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        initRenderer();
    }

    private void initRenderer() {
//        mRenderer = new MyGLRenderer(getContext());
        mRenderer = new TextureRenderer(getContext());
        setRenderer(mRenderer);
//        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
