package com.example.ninepointtask.bean;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.example.ninepointtask.R;
import com.example.ninepointtask.util.BitmapUtil;
import com.example.ninepointtask.util.DensityUtil;

public class NineLockProperty {

    private int mPointNum;
    private float mPointSize;
    private float mPointSelectedMaxSize;
    private float mPointRange;
    private float mLineWidth;
    private int mDefaultBitmapId;
    private int mSelectedBitmapId;
    private int mLineColor;
    private boolean mCanTouch;

    private Bitmap mDefaultBitmap;
    private Bitmap mSelectedBitmap;//选中的图片

    public void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlowMotionNineLock);

        mPointNum = typedArray.getInt(R.styleable.SlowMotionNineLock_pointNum, 9);
        mPointSize = typedArray.getDimension(R.styleable.SlowMotionNineLock_pointSize, DensityUtil.dipToPx(20));
        mPointSelectedMaxSize = typedArray.getDimension(R.styleable.SlowMotionNineLock_pointSelectedMaxSize, DensityUtil.dipToPx(45));
        mPointRange = typedArray.getDimension(R.styleable.SlowMotionNineLock_pointRange, DensityUtil.dipToPx(55));
        mLineWidth = typedArray.getDimension
                (R.styleable.SlowMotionNineLock_lineWidth, DensityUtil.dipToPx(3));
        mDefaultBitmapId = typedArray.getResourceId(R.styleable.SlowMotionNineLock_defaultBitmapId, R.drawable.default_point);
        mSelectedBitmapId = typedArray.getResourceId(R.styleable.SlowMotionNineLock_selectedBitmapId, R.drawable.default_point);
        mLineColor = typedArray.getColor(R.styleable.SlowMotionNineLock_lineColor, 0xff485365);

        mCanTouch = typedArray.getBoolean(R.styleable.SlowMotionNineLock_canTouch, true);

        mDefaultBitmap = BitmapUtil.compressBitmap(mDefaultBitmapId, context, (int) mPointSize, (int) mPointSize);
        mSelectedBitmap = BitmapUtil.compressBitmap(mSelectedBitmapId, context, (int) mPointSize, (int) mPointSize);

        typedArray.recycle();
    }

    public void getAttrs(Context context) {
        mPointNum = 9;
        mPointSize = DensityUtil.dipToPx(20);
        mPointSelectedMaxSize = DensityUtil.dipToPx(45);
        mPointRange = DensityUtil.dipToPx(55);
        mLineWidth = DensityUtil.dipToPx(3);
        mDefaultBitmapId = R.drawable.default_point;
        mSelectedBitmapId = R.drawable.success_point;
        mLineColor = 0xff485365;

        mCanTouch = true;//默认可以触摸

        mDefaultBitmap = BitmapUtil.compressBitmap(mDefaultBitmapId, context, (int) mPointSize, (int) mPointSize);
        mSelectedBitmap = BitmapUtil.compressBitmap(mSelectedBitmapId, context, (int) mPointSize, (int) mPointSize);
    }

    public int getPointNum() {
        return mPointNum;
    }

    public float getPointSize() {
        return mPointSize;
    }

    public float getPointSelectedMaxSize() {
        return mPointSelectedMaxSize;
    }

    public float getPointRange() {
        return mPointRange;
    }

    public float getLineWidth() {
        return mLineWidth;
    }


    public int getLineColor() {
        return mLineColor;
    }

    public boolean isCanTouch() {
        return mCanTouch;
    }

    /**
     * 得到最大尺寸和正常尺寸的差值
     *
     * @return
     */
    public float getMaxNormalDValue() {
        return mPointSelectedMaxSize - mPointSize;
    }

    public Bitmap getDefaultBitmap() {
        return mDefaultBitmap;
    }

    public Bitmap getSelectedBitmap() {
        return mSelectedBitmap;
    }

    public void setPointRange(float pointRange) {
        mPointRange = pointRange;
    }

    public void setCanTouch(boolean canTouch) {
        mCanTouch = canTouch;
    }
}
