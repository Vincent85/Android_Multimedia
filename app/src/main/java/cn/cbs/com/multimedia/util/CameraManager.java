package cn.cbs.com.multimedia.util;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import static android.opengl.GLES20.glGenTextures;

/**
 * Created by cbs on 2018/3/2.
 */

public class CameraManager {

    public static final String TAG = "CameraManager";

    private static CameraManager sInstance;

    private Camera mCamera;

//    private int[] mTextureIDs = new int[1];
//    private SurfaceTexture mSurfaceTexture;
//
//    private SurfaceTexture.OnFrameAvailableListener mFrameAvailableListener;


    private CameraManager() {
    }

    public static CameraManager getInstance() {
        if (null == sInstance) {
            sInstance = new CameraManager();
        }
        return sInstance;
    }

    /**
     * 打开相机
     * @param isFront 是否打开前置
     */
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

    /**
     * 开始预览
     * 预览纹理绑定到SurfaceTexture
     */
//    public void startPreview() {
//        if (null != mCamera) {
//            //创建纹理
//            glGenTextures(1, mTextureIDs, 0);
//            Log.d(TAG,"genTexture id = " + mTextureIDs[0]);
//            mSurfaceTexture = new SurfaceTexture(mTextureIDs[0]);
//            mSurfaceTexture.setOnFrameAvailableListener(mFrameAvailableListener);
//
//            try {
//                //设置预览纹理并打开预览
//                mCamera.setPreviewTexture(mSurfaceTexture);
//                mCamera.startPreview();
//                Log.d(TAG, "startPreview");
//            } catch (IOException e) {
//                Log.e(TAG, "setPreviewTexture failed," + e.getLocalizedMessage());
//            }
//
//        }
//    }

    public void releaseCamera() {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
//    public void setFrameAvailableListener(SurfaceTexture.OnFrameAvailableListener mFrameAvailableListener) {
//        this.mFrameAvailableListener = mFrameAvailableListener;
//    }
//
//    public SurfaceTexture getSurfaceTexture() {
//        return mSurfaceTexture;
//    }
//
//    public int[] getTextureIDs() {
//        return mTextureIDs;
//    }

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


}
