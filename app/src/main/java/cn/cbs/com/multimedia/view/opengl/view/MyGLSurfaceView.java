package cn.cbs.com.multimedia.view.opengl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import cn.cbs.com.multimedia.view.opengl.renderer.MyGLRenderer;

/**
 * Created by cbs on 2018/2/23.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    private MyGLRenderer mRenderer;

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
        mRenderer = new MyGLRenderer(getContext());
        setRenderer(mRenderer);
    }
}
