package com.example.ninepointtask.util;

import android.util.Log;

import com.example.ninepointtask.bean.PointInfo;

import java.util.List;

/**
 * 点的工具类
 * Created by xiaohan on 2018/5/3.
 */

public class PointUtil {

    private static final String TAG = "PointUtil";

    private PointUtil() {
    }

    /**
     *  用算法处理一下这些点防止有越过的点 唉
     * @param points
     * @param pointInfo
     */
    public static void handlePoint(PointInfo points[], PointInfo pointInfo,List<Integer> passwordArr) {

        if (points == null || points.length == 0 || passwordArr == null || passwordArr.size() <= 1)
            return;

        int mColumnNum = (int) Math.sqrt(points.length);

        int preId = passwordArr.get(passwordArr.size() - 1);//前一个点的id

        int firstRax = preId / mColumnNum;//求出行数
        int firstColumn = preId % mColumnNum;//求出列数
        int secondRax = pointInfo.getId() / mColumnNum;//当前经过点的行数
        int secondColumn = pointInfo.getId() % mColumnNum;//当前经过点的列数

        Log.d(TAG, "handlePoint: firstRaw  " + firstRax + " firstColumn " + firstColumn);
        Log.d(TAG, "handlePoint: secondRaw " + secondRax + " secondColumn  " + secondColumn);

        if (firstRax == secondRax) {//如果在同一行上
            for (int i = firstColumn + 1; i < secondColumn; i++)  //如果第二个点在第一个点右边
                preId = connect(preId, firstRax * mColumnNum + i, points,passwordArr);
            for (int i = firstColumn - 1; i > secondColumn; i--) //如果第二个点在第一个点左边
                preId = connect(preId, firstRax * mColumnNum + i, points, passwordArr);
        } else if (firstColumn == secondColumn) {//如果在同一列上
            for (int i = firstRax + 1; i < secondRax; i++) //如果第一个点在第二个点上面
                preId = connect(preId, i * mColumnNum + firstColumn, points, passwordArr);
            for (int i = firstRax - 1; i > secondRax; i--) //如果第一个点在第二个点下面
                preId = connect(preId, i * mColumnNum + firstColumn, points, passwordArr);
        } else if (Math.abs(firstColumn - secondColumn) == Math.abs(firstRax - secondRax)) {//如果在同一对角线上
            int raxOffset = (firstRax > secondRax) ? -1 : 1;
            int indexRax = firstRax + raxOffset;//临时行
            for (int i = firstColumn + 1; i < secondColumn; i++) { //如果第二个点在第一个点右边
                preId = connect(preId, indexRax * mColumnNum + i, points, passwordArr);//再连一下
                indexRax = firstRax + raxOffset;//行数也跟着偏移
            }
            for (int i = firstColumn - 1; i > secondColumn; i--) { //如果第二个点在第一个点左边
                preId = connect(preId, indexRax * mColumnNum + i, points, passwordArr);//再连一下
                indexRax = firstRax + raxOffset;//行数也跟着偏移
            }
        }

        points[preId].setNextId(pointInfo.getId());

    }


    /**
     * 将点数组的两个点相连返回连接的点
     */
    public static int connect(int preId, int nextId, PointInfo pointInfoArr[], List<Integer> passwordArr) {
        Log.d(TAG, "connect: preId " + preId + " nextId " + nextId);
        if (!pointInfoArr[nextId].isSelected()) {//如果没被选中
            pointInfoArr[preId].setNextId(nextId);//下一个id
            pointInfoArr[nextId].setSelected(true);//设为选中
            passwordArr.add(nextId);
            return nextId;
        }
        return preId;//把第一个id返回去
    }

}
