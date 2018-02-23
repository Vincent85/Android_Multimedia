package cn.cbs.com.multimedia.opengl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cn.cbs.com.multimedia.view.opengl.view.MyGLSurfaceView;

/**
 * Created by cbs on 2018/2/23.
 */

public class MyGLActivity extends Activity {

    private MyGLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }

}
