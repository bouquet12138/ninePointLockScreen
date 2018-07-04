package com.example.ninepointtask.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.ninepointtask.R;
import com.example.ninepointtask.view.SlowMotionNineLock;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends BaseActivity {

    private List<Integer> mPasswordArr = new ArrayList<>();
    private SlowMotionNineLock mSlowMotionNineLock;
    private TextView mHintView;

    private int mFailNum = 0;//失败次数
    private int mDisableTime = 0;//被禁用的时间
    private final int REFRESH_DISABLE = 1;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH_DISABLE) {
                mHintView.setText("请您在（" + mDisableTime + "）秒后重试");
                mDisableTime--;//禁用时间--
                if (mDisableTime < 0) {
                    mHintView.setText("");//变为以前的样子
                    mSlowMotionNineLock.setCanTouch(true);//又可以用了
                } else {
                    mHandler.sendEmptyMessageDelayed(REFRESH_DISABLE, 1000);//给自身发送延时消息
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initData();
        initView();
        initListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //[7, 4, 2, 1, 0, 8, 6]
        mPasswordArr.add(7);
        mPasswordArr.add(4);
        mPasswordArr.add(2);
        mPasswordArr.add(1);
        mPasswordArr.add(0);
        mPasswordArr.add(8);
        mPasswordArr.add(6);
    }

    /**
     * 初始化view
     */
    private void initView() {
        mSlowMotionNineLock = findViewById(R.id.slowMotionNineLock);
        mHintView = findViewById(R.id.hintText);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mSlowMotionNineLock.setOnEndListener(new SlowMotionNineLock.OnEndListener() {
            @Override
            public void onFinish(List<Integer> passwordArr) {

                if (judgeEqual(passwordArr, mPasswordArr)) {
                    mSlowMotionNineLock.drawOtherStyle(0xff339d5b,
                            BitmapFactory.decodeResource(getResources(), R.drawable.success_point));
                    mHintView.setText("图案绘制正确");
                } else {
                    mSlowMotionNineLock.drawOtherStyle(0xffdc4444,
                            BitmapFactory.decodeResource(getResources(), R.drawable.fail_point));
                    mHintView.setText("图案绘制不正确");
                    mFailNum++;//失败次数加加

                    if (mFailNum >= 5) {
                        mFailNum = 0;//失败次数归零
                        mSlowMotionNineLock.setCanTouch(false);//不可以触摸了
                        mDisableTime = 50;//禁用个50秒
                        mHandler.sendEmptyMessage(REFRESH_DISABLE);//刷新禁用
                    }

                }

            }
        });

        mSlowMotionNineLock.setNineLockOnTouchListener(new SlowMotionNineLock.NineLockOnTouchListener() {
            @Override
            public void onTouchDown() {

                if (mSlowMotionNineLock.getPasswordArr() != null && mSlowMotionNineLock.getPasswordArr().size() != 0) {
                    mHintView.setText("松开手指结束绘制");
                } else {
                    mHintView.setText("");
                }

            }

            @Override
            public void onTouchMove() {
                if (!mHintView.getText().toString().equals("松开手指结束绘制") && mSlowMotionNineLock.getPasswordArr() != null && mSlowMotionNineLock.getPasswordArr().size() != 0) {
                    mHintView.setText("松开手指结束绘制");
                }
            }
        });

    }

    /**
     * 判断两个数组是否相同
     *
     * @return
     */
    public boolean judgeEqual(List<Integer> list1, List<Integer> list2) {

        if (list1 == null && list2 == null)
            return true;

        if (list1 == null || list2 == null)
            return false;

        if (list1.size() != list2.size())
            return false;

        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i)))
                return false;
        }
        return true;
    }


}
