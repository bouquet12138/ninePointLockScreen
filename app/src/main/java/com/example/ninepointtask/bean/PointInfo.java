package com.example.ninepointtask.bean;

/**
 * 锁屏中点的信息类
 */

public class PointInfo {

    private int mId; //点的id

    private int mNextId;//当前点指向的下一个点的id

    private boolean mSelected;//是否选中

    private int mSelectedX; // 被选中时图片的左上角X坐标

    private int mSelectedY; // 被选中时图片的左上角Y坐标

    private float mRangeWidth;//范围

    private float mSelectedWidth;//选中显示的范围

    /**
     * 构造方法
     * selectedWidth 选中时图片选中的范围
     * rangeWidth 触摸能响应事件的范围 如果rangeWidth > selectedWith 相当于加了个padding
     */
    public PointInfo(int id, int selectedX,
                     int selectedY, float selectedWidth, float rangeWidth) {
        mId = id;
        mNextId = mId;
        mSelectedX = selectedX;
        mSelectedY = selectedY;
        mSelectedWidth = selectedWidth;//选中范围
        mRangeWidth = rangeWidth;
    }

    public void setNextId(int nextId) {
        mNextId = nextId;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public int getId() {
        return mId;
    }

    public int getNextId() {
        return mNextId;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public int getSelectedX() {
        return mSelectedX;
    }

    public int getSelectedY() {
        return mSelectedY;
    }

    /**
     * 是否有下一个id
     *
     * @return
     */
    public boolean hasNextId() {
        return mNextId != mId;
    }

    /**
     * 得到中心X
     *
     * @return
     */
    public float getCenterX() {
        return mSelectedX + mSelectedWidth / 2;
    }

    /**
     * 得到中心Y
     *
     * @return
     */
    public float getCenterY() {
        return mSelectedY + mSelectedWidth / 2;
    }

    /**
     * 坐标(x,y)是否在当前点的范围内
     *
     * @return
     */
    public boolean isInMyPlace(float x,float y) {
        boolean inX = x >= getCenterX() - mRangeWidth / 2 && x <= getCenterX() + mRangeWidth / 2;
        boolean inY = y >= getCenterY() - mRangeWidth / 2 && y <= getCenterY() + mRangeWidth / 2;
        return (inX && inY);//只有两个都为真时才为真
    }

}
