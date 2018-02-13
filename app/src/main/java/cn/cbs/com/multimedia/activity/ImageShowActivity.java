package cn.cbs.com.multimedia.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import cn.cbs.com.multimedia.R;

/**
 * Created by cbs on 2018/2/13.
 */

public class ImageShowActivity extends Activity implements SurfaceHolder.Callback{

    ImageView mIv;
    SurfaceView mSfv;

    SurfaceHolder mHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        initView();

        displayInIV();
    }

    private void initView() {
        mIv = findViewById(R.id.iv);
        mSfv = findViewById(R.id.sfv);

        mSfv.getHolder().addCallback(this);
    }

    private void displayInIV() {
        mIv.setImageResource(R.drawable.beauty);
    }

    private void drawInSrv() {
        Canvas canvas = mHolder.lockCanvas();
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.beauty);
        Rect area = new Rect(0, 0, mSfv.getWidth(), mSfv.getHeight());
        canvas.drawBitmap(bmp,null,area,null);
        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawInSrv();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
