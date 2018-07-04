演示图片
--------
![image](https://github.com/bouquet12138/pictureLibrary/blob/master/ninePoint.gif)
相关属性
--------
		<!--点的数量 需要是自然数的平方 不建议太大 比如 4 9 16 25 等-->
        <attr name="pointNum" format="integer"></attr>
        <!--点的尺寸，不要越界哦-->
        <attr name="pointSize" format="dimension"></attr>
        <!--点选中时的最大尺寸-->
        <attr name="pointSelectedMaxSize" format="dimension"></attr>
        <!--点的触摸范围-->
        <attr name="pointRange" format="dimension"></attr>
        <!--线的宽度-->
        <attr name="lineWidth" format="dimension"></attr>
        <!--默认图片样式-->
        <attr name="defaultBitmapId" format="reference"></attr>
        <!--选中的图片样式-->
        <attr name="selectedBitmapId" format="reference"></attr>
        <!--线条的颜色-->
        <attr name="lineColor" format="color"></attr>
        <!--用户是否可以触摸-->
        <attr name="canTouch" format="boolean" />
相关监听
-------
		对象.setOnEndListener(new SlowMotionNineLock.OnEndListener() {
            @Override
            public void onFinish(List<Integer> passwordArr) {
					//会将用户输入的密码以数组的形式传递出去
			}
		}
