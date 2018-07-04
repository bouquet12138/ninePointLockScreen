package com.example.ninepointtask.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.ninepointtask.bean.NineLockProperty;
import com.example.ninepointtask.bean.PointInfo;
import com.example.ninepointtask.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有缓动效果的9键锁屏
 * Created by xiaohan on 2018/5/3.
 */

public class SlowMotionNineLock extends View {

    private static final String TAG = "NinePointView";

    private Paint mLinePaint = new Paint();//线画笔
    private Bitmap mOtherSelectedBitmap;//其他选中的样式
    private boolean mDrawOtherStyle = false;//绘制其他样式

    private List<Integer> mPasswordArr = new ArrayList<>();//存放用户输入的密码
    private int mColumnNum;//列数
    private int mSelectedSpacing;//分割长

    private int mCurrentSelected = -1;//当前选中
    private float mCurrentSelectedDiameter;//当前选中的半径

    private int mWidth, mHeight;//用于记录view的宽高
    private float mMoveX, mMoveY;//获取用户的移动坐标
    private float mStartX, mStartY;//开始坐标
    //private boolean mIsUp = false;//用户是否抬起

    private PointInfo mPoints[];

    private NineLockProperty mPro = new NineLockProperty();

    private final int ANIM = 1;//变大缩小动画
    /**
     * 处理事件
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == ANIM) {
                mCurrentSelectedDiameter -= (mPro.getMaxNormalDValue()) / 5f;//尺寸减小
                if (mCurrentSelectedDiameter > mPro.getPointSize()) {
                    mHandler.sendEmptyMessageDelayed(ANIM, 30);//每隔30秒刷新一下
                }
                invalidate();//重绘一下
            }
        }
    };

    public SlowMotionNineLock(Context context) {
        super(context);
        mPro.getAttrs(context);
        init();
    }

    public SlowMotionNineLock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPro.getAttrs(context, attrs);
        init();
    }

    /**
     * 初始化各个属性
     */
    private void init() {

        mPoints = new PointInfo[mPro.getPointNum()];//初始化一个存放point的数组
        mColumnNum = (int) Math.sqrt(mPoints.length);//列数
        mLinePaint.setStrokeWidth(mPro.getLineWidth());
        mLinePaint.setColor(mPro.getLineColor());
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);//圆帽
    }

    /**
     * 发布新动画
     */
    private void releaseAnim() {
        mHandler.removeMessages(ANIM);//先移除上一个动画
        mCurrentSelectedDiameter = mPro.getPointSelectedMaxSize();
        mHandler.sendEmptyMessageDelayed(ANIM, 30);//每隔30秒刷新一下
    }

    /**
     * 用户的触摸事件
     * 这个DOWN和MOVE、UP是成对的，如果没从UP释放，就不会再获得DOWN；
     * 而获得DOWN时，一定要确认消费该事件，否则MOVE和UP不会被这个VIEW的onTouchEvent接收
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!mPro.isCanTouch())//用户禁止触摸，屏蔽用户操作
            return false;

        handleEvent(event);

        return true;
    }

    /**
     * 结束绘制  重置一下
     */
    private void finishDraw() {
        for (PointInfo pointInfo : mPoints) {
            pointInfo.setSelected(false);//
            pointInfo.setNextId(pointInfo.getId());
        }
        mCurrentSelected = -1;//既然抬起那么当前没有选中了
        mPasswordArr.clear();//清除已存的密码
        invalidate();//重绘一下
    }

    /**
     * 处理事件
     *
     * @param event
     */
    private void handleEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                finishDraw();//先清除一下状态
                mDrawOtherStyle = false;//就不绘制其他样式了
                mLinePaint.setColor(mPro.getLineColor());//设回默认颜色

                for (PointInfo pointInfo : mPoints) {
                    if (pointInfo.isInMyPlace((int) event.getX(), (int) event.getY())) {
                        pointInfo.setSelected(true);//选中
                        mStartX = pointInfo.getCenterX();
                        mStartY = pointInfo.getCenterY();
                        mPasswordArr.add(pointInfo.getId());//将id加进去
                        mCurrentSelected = pointInfo.getId();
                        releaseAnim();
                        break;//找到了就不用再找了
                    }
                }
                if (mNineLockOnTouchListener != null)
                    mNineLockOnTouchListener.onTouchDown();
                invalidate();//重绘
                break;

            case MotionEvent.ACTION_MOVE:
                mMoveX = (int) event.getX();
                mMoveY = (int) event.getY();
                handlePoint();
                if (mNineLockOnTouchListener != null)
                    mNineLockOnTouchListener.onTouchMove();
                invalidate();//重绘
                break;

            case MotionEvent.ACTION_UP:
                mStartX = mStartY = mMoveX = mMoveY = 0;
                invalidate();//重绘
                if (mOnEndListener != null && mPasswordArr != null && mPasswordArr.size() != 0)
                    mOnEndListener.onFinish(mPasswordArr);//将数组传递出去
                break;

        }

    }

    /**
     * 处理一下这些点 这段代码写的我贼几把恶心
     * 为了将那些在一天线上的连起来
     */
    private void handlePoint() {

        for (PointInfo pointInfo : mPoints) {
            if (pointInfo.isInMyPlace(mMoveX, mMoveY) && !pointInfo.isSelected()) {//如果在范围之内 并且没选中
                pointInfo.setSelected(true);//设为选中
                mCurrentSelected = pointInfo.getId();

                releaseAnim();//发布渐变动画

                mStartX = pointInfo.getCenterX();
                mStartY = pointInfo.getCenterY();
                if (mPasswordArr.size() != 0) {
                    int preId = mPasswordArr.get(mPasswordArr.size() - 1);//前一个点的id

                    int firstRax = preId / mColumnNum;//求出行数
                    int firstColumn = preId % mColumnNum;//求出列数
                    int secondRax = pointInfo.getId() / mColumnNum;//当前经过点的行数
                    int secondColumn = pointInfo.getId() % mColumnNum;//当前经过点的列数

                    if (firstRax == secondRax) {//如果在同一行上
                        for (int i = firstColumn + 1; i < secondColumn; i++)  //如果第二个点在第一个点右边
                            preId = connect(preId, firstRax * mColumnNum + i);
                        for (int i = firstColumn - 1; i > secondColumn; i--) //如果第二个点在第一个点左边
                            preId = connect(preId, firstRax * mColumnNum + i);
                    } else if (firstColumn == secondColumn) {//如果在同一列上
                        for (int i = firstRax + 1; i < secondRax; i++) //如果第一个点在第二个点上面
                            preId = connect(preId, i * mColumnNum + firstColumn);
                        for (int i = firstRax - 1; i > secondRax; i--) //如果第一个点在第二个点下面
                            preId = connect(preId, i * mColumnNum + firstColumn);
                    } else if (Math.abs(firstColumn - secondColumn) == Math.abs(firstRax - secondRax)) {//如果在同一对角线上
                        int raxOffset = (firstRax > secondRax) ? -1 : 1;
                        int indexRax = firstRax + raxOffset;//临时行
                        for (int i = firstColumn + 1; i < secondColumn; i++) { //如果第二个点在第一个点右边
                            preId = connect(preId, indexRax * mColumnNum + i);//再连一下
                            indexRax = firstRax + raxOffset;//行数也跟着偏移
                        }
                        for (int i = firstColumn - 1; i > secondColumn; i--) { //如果第二个点在第一个点左边
                            preId = connect(preId, indexRax * mColumnNum + i);//再连一下
                            indexRax = firstRax + raxOffset;//行数也跟着偏移
                        }
                    }
                    mPoints[preId].setNextId(pointInfo.getId());
                }
                mPasswordArr.add(pointInfo.getId());//将点id添加进去
                break;//返回
            }
        }

    }

    /**
     * 将两个点相连返回下一个点
     */
    private int connect(int preId, int nextId) {
        Log.d(TAG, "connect: preId " + preId + " nextId " + nextId);
        if (!mPoints[nextId].isSelected()) {//如果没被选中
            mPoints[preId].setNextId(nextId);//下一个id
            mPasswordArr.add(nextId);//把这个点加进来
            mPoints[nextId].setSelected(true);//设为选中
            return nextId;
        }
        return preId;//把第一个id返回去
    }

    /**
     * 在这里得到view的宽高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        Log.d(TAG, "onSizeChanged: " + " w " + w + " h " + h + " oldW " + oldW + " oldH " + oldH);
        mWidth = w;
        mHeight = h;//得到view宽高
        initPoint();//当获得宽高好得到一下
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        if (mMoveX != 0 && mMoveY != 0 && mStartX != 0 && mStartY != 0)// 绘制当前活动的线段
            canvas.drawLine(mStartX, mStartY, mMoveX, mMoveY, mLinePaint);//绘制线
        if (mDrawOtherStyle)
            drawNinePoint(canvas, mOtherSelectedBitmap);
        else
            drawNinePoint(canvas, mPro.getSelectedBitmap());

    }

    /**
     * 绘制那几个点
     *
     * @param canvas
     */
    private void drawNinePoint(Canvas canvas, Bitmap bitmap) {

        for (PointInfo pointInfo : mPoints) { //先把用户的画出的线绘制好
            if (pointInfo.hasNextId()) { //如果有下一个
                int nextId = pointInfo.getNextId();//下一个的id
                canvas.drawLine(pointInfo.getCenterX(), pointInfo.getCenterY(),
                        mPoints[nextId].getCenterX(), mPoints[nextId].getCenterY(), mLinePaint);
            }
        }

        for (PointInfo pointInfo : mPoints) {
            if (pointInfo.isSelected()) {//如果被选中
                if (pointInfo.getId() == mCurrentSelected) {//如果id为当前选择

                    Bitmap newSelectedBitmap = BitmapUtil.compressBitmap(bitmap, (int) mCurrentSelectedDiameter, (int) mCurrentSelectedDiameter);
                    canvas.drawBitmap(newSelectedBitmap, pointInfo.getCenterX() - newSelectedBitmap.getWidth() / 2,
                            pointInfo.getCenterY() - newSelectedBitmap.getHeight() / 2, mLinePaint);//绘制一下选中图片

                } else {
                    canvas.drawBitmap(bitmap, pointInfo.getSelectedX(),
                            pointInfo.getSelectedY(), mLinePaint);//绘制一下选中图片
                }
            } else {
                canvas.drawBitmap(mPro.getDefaultBitmap(), pointInfo.getSelectedX(),
                        pointInfo.getSelectedY(), mLinePaint);//绘制一下默认图片
            }
        }

    }


    /**
     * 初始化点数组
     */
    private void initPoint() {

        boolean HGreaterThanW = (mHeight > mWidth);//高是否大于宽
        //分割宽度为宽度和高度中小的值减去6个圆的大小除以列数加1
        mSelectedSpacing = (int) ((Math.min(mWidth, mHeight) - mPro.getPointSize() * mColumnNum) / (mColumnNum + 1));//间距
        float pointRange = Math.min(mPro.getPointRange(), mPro.getPointSize() + mSelectedSpacing);////防止点范围越界
        mPro.setPointRange(pointRange);

        int edgeWidth = (int) (mSelectedSpacing * (mColumnNum / (float) (mColumnNum + 1)));//边缘宽
        int intervalWidth = (int) ((Math.min(mWidth, mHeight) - mPro.getPointSize() * mColumnNum - 2 * edgeWidth) / (float) (mColumnNum - 1));//中间宽

        int selectedX = HGreaterThanW ? edgeWidth : (mWidth - mHeight) / 2 + edgeWidth;//图片的左上角X点坐标
        int selectedY = HGreaterThanW ? mHeight - mWidth + edgeWidth : edgeWidth;//左上角Y坐标


        for (int i = 0; i < mPoints.length; i++) {
            if ((i % mColumnNum) == 0 && i != 0) {
                selectedX = HGreaterThanW ? edgeWidth : (mWidth - mHeight) / 2 + edgeWidth; //重归开头
                selectedY += mPro.getPointSize() + intervalWidth;//向下偏移
            }
            mPoints[i] = new PointInfo(i, selectedX, selectedY, mPro.getPointSize(), mPro.getPointRange());//初始化
            selectedX += mPro.getPointSize() + intervalWidth;//向右偏
        }

    }

    //绘制结束监听
    public interface OnEndListener {
        void onFinish(List<Integer> passwordArr);//将密码触底出去
    }

    private OnEndListener mOnEndListener;

    /**
     * 设置结束的监听
     */
    public void setOnEndListener(OnEndListener onEndListener) {
        mOnEndListener = onEndListener;
    }

    /**
     * 我的OnTouch监听
     */
    public interface NineLockOnTouchListener {
        void onTouchDown();

        void onTouchMove();
    }

    private NineLockOnTouchListener mNineLockOnTouchListener;

    public void setNineLockOnTouchListener(NineLockOnTouchListener nineLockOnTouchListener) {
        mNineLockOnTouchListener = nineLockOnTouchListener;
    }

    /**
     * 设置用户是否可以触摸
     */
    public void setCanTouch(boolean enabled) {

        mPro.setCanTouch(enabled);//设置用户是否可以触摸

        if (!enabled) {
            finishDraw();//重置一下
        }
    }

    /**
     * 得到密码列表
     *
     * @return
     */
    public List<Integer> getPasswordArr() {
        return mPasswordArr;
    }

    /**
     * 绘制其他状态的样式
     * 比如失败状态 完成状态等
     * 用户可以自行定义
     */
    public void drawOtherStyle(int lineColor, Bitmap otherSelectedBitmap) {
        mLinePaint.setColor(lineColor);//更改样式
        mOtherSelectedBitmap = BitmapUtil.compressBitmap(otherSelectedBitmap, (int) mPro.getPointSize(), (int) mPro.getPointSize());//压缩一下
        mDrawOtherStyle = true;//绘制其他样式
        invalidate();//重绘一下
    }

}
