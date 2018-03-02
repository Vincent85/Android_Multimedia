package cn.cbs.com.multimedia.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * Created by cbs on 2018/3/2.
 */

public class GLPreviewSurfaceView extends GLSurfaceView implements SurfaceTexture.OnFrameAvailableListener {

    public static final String TAG = "GLPreviewSurfaceView";

    private GLPreviewRenderer mRenderer;

    private Camera mCamera;
    private SurfaceTexture mInputTexture;
    //转换矩阵
    private final float[] mTransformMatrix = new float[16];

    public GLPreviewSurfaceView(Context context) {
        this(context,null);
    }

    public GLPreviewSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        initRenderer();
    }

    private void initRenderer() {
        if (null == mRenderer) {
            mRenderer = new GLPreviewRenderer(getContext());
        }
        setRenderer(mRenderer);
        //按需渲染
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        openCamera(true);
//        startPreview();
//    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseCamera();
    }
    public void openCamera(boolean isFront) {
        if (null == mCamera) {
            int cameraID = getCameraID(isFront);
            if (cameraID == -1) {
                Log.e(TAG, "getCameraID failed");
                throw new RuntimeException("getCameraID failed,please check the camera configure of you device.");
            }
            try {
                mCamera = Camera.open(cameraID);
            } catch (Exception e) {
                Log.e(TAG, "open camera failed." + e.getLocalizedMessage());
            }
            configCameraParam();

        }
    }

    private void configCameraParam() {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        Camera.Size size = getOptimalPreviewSize(sizes);
        parameters.setPreviewSize(size.width, size.height);

        mCamera.setParameters(parameters);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes) {
        //TODO
        return sizes.get(0);
    }

    private int getCameraID(boolean isFront) {
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        int cameraFacing = isFront ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
        for(int i=0; i<cameraCount; ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == cameraFacing) {
                return i;
            }
        }
        return -1;
    }
    public void startPreview() {
        if (null != mCamera) {
            mInputTexture = new SurfaceTexture(mRenderer.getTextureID());
            Log.d(TAG, "textureID = " + mRenderer.getTextureID());
            mInputTexture.setOnFrameAvailableListener(GLPreviewSurfaceView.this);
            try {
                //设置预览纹理并打开预览
                mCamera.setPreviewTexture(mInputTexture);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.e(TAG, "setPreviewTexture failed," + e.getLocalizedMessage());
            }

        }
    }

    public void releaseCamera() {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void showCameraPreview() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (null != mInputTexture) {
                    mInputTexture.updateTexImage();
                    mInputTexture.getTransformMatrix(mTransformMatrix);
                    mRenderer.setTexTransMatrix(mTransformMatrix);
                }
            }
        });
        requestRender();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        showCameraPreview();
    }

}
