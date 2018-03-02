package cn.cbs.com.multimedia.opengl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import cn.cbs.com.multimedia.opengl.renderer.TextureRenderer;
import cn.cbs.com.multimedia.opengl.view.MyGLSurfaceView;

/**
 * Created by cbs on 2018/2/23.
 */

public class MyGLActivity extends Activity implements View.OnTouchListener {

    private MyGLSurfaceView mGLView;
    private boolean isRendererSet = false;

    final TextureRenderer mRenderer = new TextureRenderer(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this);
        mGLView.setRenderer(mRenderer);
        mGLView.setOnTouchListener(this);

        setContentView(mGLView);
    }

    @Override
    public void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (null != event) {
            //convert touch coordinates into normalize device coordinates
            final float normalizedX = (event.getX() / (float)v.getWidth()) * 2 - 1;
            final float normailizeY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mGLView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mRenderer.handleTouchPressed(normalizedX,normailizeY);
                    }
                });
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mGLView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mRenderer.handleTouchDrag(normalizedX, normailizeY);
                    }
                });
            }
            return true;
        }
        return false;
    }
}
