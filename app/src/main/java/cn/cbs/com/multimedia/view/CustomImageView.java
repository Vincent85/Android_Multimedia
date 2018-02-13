package cn.cbs.com.multimedia.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.cbs.com.multimedia.R;

/**
 * Created by cbs on 2018/2/13.
 */

public class CustomImageView extends View {

    private BitmapDrawable mDrawable;
    private Bitmap mBmp;

    public CustomImageView(Context context) {
        this(context,null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBmp = BitmapFactory.decodeResource(getResources(), R.drawable.beauty);
        mDrawable = new BitmapDrawable(mBmp);
        mDrawable.setBounds(0,0,mBmp.getWidth(),mBmp.getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBmp.getWidth(),mBmp.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawable.draw(canvas);
    }



}
