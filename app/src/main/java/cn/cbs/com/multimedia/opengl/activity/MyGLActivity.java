package cn.cbs.com.multimedia.opengl.activity;

import android.app.Activity;
import android.os.Bundle;

import cn.cbs.com.multimedia.opengl.view.MyGLSurfaceView;

/**
 * Created by cbs on 2018/2/23.
 */

public class MyGLActivity extends Activity {

    private MyGLSurfaceView mGLView;
    private boolean isRendererSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this);
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
}
