package cn.cbs.com.multimedia.camera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import cn.cbs.com.multimedia.util.CameraManager;

/**
 * Created by cbs on 2018/3/1.
 */

public class CameraPreviewActivity extends Activity {

    private static final String TAG = "CameraPreviewActivity";

    private GLPreviewSurfaceView mPreviewView;

    boolean useFrontCamera = true;

    private final int PERMISSION_REQUEST_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreviewView = new GLPreviewSurfaceView(this);
        setContentView(mPreviewView);


        //检查摄像头权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);
            }
        }else {
            Log.d(TAG, "permission granted");
            //打开摄像头并开始预览
//            mCameraManager.openCamera(useFrontCamera);
//            mCameraManager.startPreview();
//            mPreviewView.openCamera(useFrontCamera);
//            mPreviewView.startPreview();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mPreviewView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPreviewView.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    //打开摄像头并开始预览
//                    mCameraManager.openCamera(useFrontCamera);
//                    mCameraManager.startPreview();
//                    mPreviewView.openCamera(useFrontCamera);
//                    mPreviewView.startPreview();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
