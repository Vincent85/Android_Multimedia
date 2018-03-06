package cn.cbs.com.multimedia.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import cn.cbs.com.multimedia.util.CameraManager;

/**
 * Created by cbs on 2018/3/2.
 */

public class GLPreviewSurfaceView extends GLSurfaceView implements SurfaceTexture.OnFrameAvailableListener {

    public static final String TAG = "GLPreviewSurfaceView";

    private GLPreviewRenderer mRenderer;
    private Activity mActivity;

    public GLPreviewSurfaceView(Context context) {
        this(context,null);
    }

    public GLPreviewSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mActivity = (Activity) context;
        initRenderer();
    }

    private void initRenderer() {
        if (null == mRenderer) {
            mRenderer = new GLPreviewRenderer(mActivity);
            mRenderer.setOnFrameListener(this);
        }
        setRenderer(mRenderer);
        //按需渲染
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }


    public void showCameraPreview() {
//        queueEvent(new Runnable() {
//            @Override
//            public void run() {
//                SurfaceTexture inputTexture = mRenderer.getInputTexture();
//                if (null != inputTexture) {
//                    inputTexture.updateTexImage();
//                    inputTexture.getTransformMatrix(mRenderer.getTexMatrix());
//                    Log.d(TAG, "updateTexImage");
//                }
//            }
//        });
        requestRender();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        showCameraPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        CameraManager.getInstance().releaseCamera();
        Log.d(TAG, "release camera in surfaceDestoryed");
    }

}
