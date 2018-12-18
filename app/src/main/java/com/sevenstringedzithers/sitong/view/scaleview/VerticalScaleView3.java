package com.sevenstringedzithers.sitong.view.scaleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jyall.android.common.utils.LogUtils;
import com.sevenstringedzithers.sitong.R;
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick;


public class VerticalScaleView3 extends View {
    private RVAdapterItemOnClick onclick;
    private float max;
    private float min;
    private int nums = 4;//多少个模块

    private int largePointRadius = 20;//长一点的线的长度
    private int smallPointRadius = 10;//长一点的线的宽度


    private Paint mLinesPaint;//刻度线画笔


    private int maxWidth;//控件总宽度
    private int maxHeight;//空间总高度
    private int mRealWidth;//实际画布大小
    private int mRealHeight;

    private static int mDividerLines = 5;//分割线之间的间距


    private int selectedNum = -1;


    public VerticalScaleView3(Context context) {
        super(context);
        init();
    }

    public VerticalScaleView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalScaleView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLinesPaint = new Paint();
        mLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinesPaint.setAntiAlias(true);
        mLinesPaint.setDither(true);
        mLinesPaint.setColor(getResources().getColor(R.color.color_d0a670));



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取屏幕的宽度
        maxWidth = getMeasuredWidth();

        maxHeight = getMeasuredHeight();

        LogUtils.e("VerticalScaleView2-------onMeasure" + maxWidth + "-----" + maxHeight);
        mRealWidth = maxWidth - getPaddingRight() - getPaddingLeft();
        mRealHeight = maxHeight - getPaddingTop() - getPaddingBottom();

        mDividerLines = (mRealWidth  -nums * largePointRadius*2) / nums/3;
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        for (int i = 0; i < nums; i++) {
            if (selectedNum==i){
                canvas.drawCircle( (maxWidth-largePointRadius*nums*2-mDividerLines*nums)/2+ largePointRadius * (i) * 2 +mDividerLines*i, maxHeight / 2 ,
                        largePointRadius, mLinesPaint);
            }else {
                canvas.drawCircle( (maxWidth-largePointRadius*nums*2-mDividerLines*nums)/2+ largePointRadius * (i) * 2 +mDividerLines*i, maxHeight / 2 ,
                        smallPointRadius, mLinesPaint);
            }

        }



    }

    public int getSelectedNum() {
        return selectedNum;
    }

    public void setSelectedNum(int selectedNum) {
        this.selectedNum = selectedNum;
        invalidate();
    }

    public void setNums(int nums) {
        this.nums = nums;
        invalidate();
    }
}
