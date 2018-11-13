package com.sitong.changqin.view.scaleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.sevenstringedzithers.sitong.R;


public class VerticalScaleView1 extends View {
    private float max;
    private float min;
    private int percentNum = 4;//每个模块下面的数量
    private int nums = 10;//多少个模块

    private int largeLineLength = 56;//长一点的线的长度
    private int largeLineHeight = 2;//长一点的线的宽度

    private int smallLineLength = 28;//短一点的线的长度
    private int smallLineHeight = 2;//短一点的线的宽度

    private int currentLineLength = 140;
    private int currentLineHeight = 2;

    private int middleLineLength=100;//中间线的长度


    private Paint mLinesPaint;//刻度线画笔
    private Paint mCurrentLinesPaint;//当前刻度线画笔
    private Paint mMiddleLinesPaint;//中间刻度线画笔

    private TextPaint textPaint;

    private int maxWidth;//控件总宽度
    private int maxHeight;//空间总高度
    private int mRealWidth;//实际画布大小
    private int mRealHeight;

    private static int mDividerLines = 5;//分割线之间的间距

    private String currentValue="440";


    public VerticalScaleView1(Context context) {
        super(context);
        init();
    }

    public VerticalScaleView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalScaleView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLinesPaint = new Paint();
        mLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinesPaint.setAntiAlias(true);
        mLinesPaint.setDither(true);
        mLinesPaint.setStrokeWidth(largeLineHeight);
        mLinesPaint.setColor(getResources().getColor(R.color.color_d0a670));

        mCurrentLinesPaint = new Paint();
        mCurrentLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCurrentLinesPaint.setAntiAlias(true);
        mCurrentLinesPaint.setDither(true);
        mCurrentLinesPaint.setStrokeWidth(largeLineHeight);
        mCurrentLinesPaint.setColor(getResources().getColor(R.color.color_d0a670));


        mMiddleLinesPaint = new Paint();
        mMiddleLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mMiddleLinesPaint.setAntiAlias(true);
        mMiddleLinesPaint.setDither(true);
        mMiddleLinesPaint.setStrokeWidth(largeLineHeight);
        mMiddleLinesPaint.setColor(getResources().getColor(R.color.colorAccent));


        textPaint=new TextPaint();
        textPaint.setTextSize(80);
        textPaint.setColor(getResources().getColor(R.color.color_d0a670));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取屏幕的宽度
        maxWidth = getWidth();

        maxHeight = getHeight();

        mRealWidth = maxWidth - getPaddingRight() - getPaddingLeft();
        mRealHeight = maxHeight - getPaddingTop() - getPaddingBottom();

        mDividerLines = (mRealHeight - ((percentNum - 1) * nums * smallLineHeight) - nums * largeLineHeight) / (percentNum * nums + 1);

    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        for (int i = 0; i < nums; i++) {
//绘制长刻度线
            canvas.drawLine(maxWidth / 2 - largeLineLength / 2, getPaddingTop() + largeLineHeight * i * percentNum + mDividerLines * i * (percentNum),
                    maxWidth / 2 + largeLineLength / 2, getPaddingTop() + largeLineHeight * i * percentNum + mDividerLines * i * percentNum, mLinesPaint);
            for (int j = 0; j < percentNum; j++) {
                //绘制短刻度线
                canvas.drawLine(maxWidth / 2 - smallLineLength / 2, getPaddingTop() + largeLineHeight * (i * percentNum + j + 1) + mDividerLines * ((i) * percentNum + j + 1),
                        maxWidth / 2 + smallLineLength / 2, getPaddingTop() + largeLineHeight * ((i) * percentNum + j + 1) + mDividerLines * ((i) * percentNum + j + 1), mLinesPaint);
            }
        }
        //绘制最后一根线
        canvas.drawLine(maxWidth / 2 - largeLineLength / 2, getPaddingTop() + largeLineHeight * nums * percentNum + mDividerLines * nums * (percentNum),
                maxWidth / 2 + largeLineLength / 2, getPaddingTop() + largeLineHeight * nums * percentNum + mDividerLines * nums * percentNum, mLinesPaint);
//绘制单位hz
        canvas.drawText("Hz",maxWidth / 2 + largeLineLength / 2+40, getPaddingTop()+26, textPaint);
//绘制中间线
        canvas.drawLine(maxWidth / 2 - middleLineLength / 2, getPaddingTop() + largeLineHeight * nums * percentNum/2 + mDividerLines * nums * (percentNum)/2,
                maxWidth / 2 + middleLineLength / 2, getPaddingTop() + largeLineHeight * nums * percentNum /2+ mDividerLines * nums * percentNum/2, mMiddleLinesPaint);
//绘制中间位置的hz数值
        canvas.drawText(currentValue,maxWidth / 2 + largeLineLength / 2+40, getPaddingTop()+ largeLineHeight * nums * percentNum /2+mDividerLines  * nums * percentNum /2+26, textPaint);

    }
}
