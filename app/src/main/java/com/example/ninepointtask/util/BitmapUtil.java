package com.example.ninepointtask.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.ninepointtask.R;

/**
 * Created by xiaohan on 2018/5/3.
 */

public class BitmapUtil {

    /**
     * 构造器私有
     */
    private BitmapUtil() {
    }

    /**
     * 将图片压缩到指定尺寸
     *
     * @param bitmap
     * @param targetWidth
     * @param targetHeight
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {

        Matrix matrix = new Matrix();
        matrix.postScale(targetWidth / (float) bitmap.getWidth(), targetHeight / (float) bitmap.getHeight());//先压缩一下
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);//压缩一下默认图片

        return newBitmap;
    }

    /**
     * 根据图片源将图片压缩到指定尺寸
     * @param imageRes
     */
    public static Bitmap compressBitmap(int imageRes, Context context, int targetWidth, int targetHeight) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageRes);
        return compressBitmap(bitmap, targetWidth, targetHeight);
    }

}
