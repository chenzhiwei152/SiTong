package com.sevenstringedzithers.sitong.view.scaleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jyall.android.common.utils.LogUtils;
import com.sevenstringedzithers.sitong.R;
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick;


public class VerticalScaleView2 extends View {
    private RVAdapterItemOnClick onclick;
    private float max;
    private float min;
    private int percentNum = 4;//每个模块下面的数量
    private int nums = 10;//多少个模块

    private int largeLineLength = 56;//长一点的线的长度
    private int largeLineHeight = 2;//长一点的线的宽度

    private int smallLineLength = 28;//短一点的线的长度
    private int smallLineHeight = 2;//短一点的线的宽度


    private Paint mLinesPaint;//刻度线画笔

    private TextPaint textPaint;
    private TextPaint textPaintSelected;

    private int maxWidth;//控件总宽度
    private int maxHeight;//空间总高度
    private int mRealWidth;//实际画布大小
    private int mRealHeight;

    private static int mDividerLines = 5;//分割线之间的间距

    private float distance;//行高
    private int cornerSize = 30;//圆角大小
    //    绘制文字的内边距
    private int topPadding = 15;
    private int leftPading = 25;

    private int paddingLeft = 200;
    private int paddingTop = 50;

    private String currentValues[] = {"0.2", "0.4", "0.6", "0.8", "1.0", "1.2"};


    private int selectedNum = 4;


    public VerticalScaleView2(Context context) {
        super(context);
        init();
    }

    public VerticalScaleView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalScaleView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

//        mCurrentLinesPaint = new Paint();
//        mCurrentLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mCurrentLinesPaint.setAntiAlias(true);
//        mCurrentLinesPaint.setDither(true);
//        mCurrentLinesPaint.setStrokeWidth(largeLineHeight);
//        mCurrentLinesPaint.setColor(getResources().getColor(R.color.color_d0a670));


//        mMiddleLinesPaint = new Paint();
//        mMiddleLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mMiddleLinesPaint.setAntiAlias(true);
//        mMiddleLinesPaint.setDither(true);
//        mMiddleLinesPaint.setStrokeWidth(largeLineHeight);
//        mMiddleLinesPaint.setColor(getResources().getColor(R.color.colorAccent));


        textPaint = new TextPaint();
        textPaint.setTextSize(40);
        textPaint.setColor(getResources().getColor(R.color.color_d0a670));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaintSelected = new TextPaint();
        textPaintSelected.setTextSize(40);
        textPaintSelected.setColor(getResources().getColor(R.color.color_000000));
        textPaintSelected.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取屏幕的宽度
        maxWidth = getMeasuredWidth() - paddingLeft;

        maxHeight = getMeasuredHeight() - paddingTop;

        LogUtils.e("VerticalScaleView2-------onMeasure" + maxWidth + "-----" + maxHeight);
        mRealWidth = maxWidth - getPaddingRight() - getPaddingLeft();
        mRealHeight = maxHeight - getPaddingTop() - getPaddingBottom();

        mDividerLines = (mRealWidth - ((percentNum - 1) * nums * smallLineHeight) - nums * largeLineHeight) / (percentNum * nums);
//计算baseline
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        distance = (fontMetrics.bottom - fontMetrics.top);
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        for (int i = 0; i < nums; i++) {
//绘制长刻度线
            canvas.drawLine(getPaddingLeft() + paddingLeft / 2 + largeLineHeight * i * percentNum + mDividerLines * i * (percentNum), maxHeight / 2 - largeLineLength / 2,
                    getPaddingLeft() + paddingLeft / 2 + largeLineHeight * i * percentNum + mDividerLines * i * percentNum, maxHeight / 2 + largeLineLength / 2, mLinesPaint);
            for (int j = 0; j < percentNum; j++) {
                //绘制短刻度线
                canvas.drawLine(getPaddingLeft() + paddingLeft / 2 + largeLineHeight * (i * percentNum + j + 1) + mDividerLines * ((i) * percentNum + j + 1), maxHeight / 2 - smallLineLength / 2,
                        getPaddingLeft() + paddingLeft / 2 + largeLineHeight * ((i) * percentNum + j + 1) + mDividerLines * ((i) * percentNum + j + 1), maxHeight / 2 + smallLineLength / 2, mLinesPaint);
            }
        }
        //绘制最后一根线
        canvas.drawLine(getPaddingLeft() + paddingLeft / 2 + largeLineHeight * nums * percentNum + mDividerLines * nums * (percentNum), maxHeight / 2 - largeLineLength / 2,
                getPaddingLeft() + paddingLeft / 2 + largeLineHeight * nums * percentNum + mDividerLines * nums * percentNum, maxHeight / 2 + largeLineLength / 2, mLinesPaint);


        for (int i = 0; i < currentValues.length; i++) {
            if (selectedNum == i) {
                Rect minRect = new Rect();

                textPaintSelected.getTextBounds(currentValues[i],0,currentValues[i].length(),minRect);



                Paint bgRect = new Paint();
                bgRect.setStyle(Paint.Style.FILL);
                bgRect.setColor(getResources().getColor(R.color.color_d0a670));
                RectF rectF = new RectF(getPaddingLeft() + paddingLeft / 2 + (nums - 1) * largeLineHeight * i + (nums - 2) * mDividerLines * i - textPaint.measureText(currentValues[i]) / 2 - leftPading, maxHeight / 2 + largeLineLength +minRect.top - topPadding+distance, getPaddingLeft() + paddingLeft / 2 + (nums - 1) * largeLineHeight * i + (nums - 2) * mDividerLines * i + textPaint.measureText(currentValues[i]) / 2 + leftPading, maxHeight / 2 + largeLineLength +minRect.bottom + topPadding+distance);
                canvas.drawRoundRect(rectF, cornerSize, cornerSize, bgRect);
                canvas.drawText(currentValues[i], getPaddingLeft() + paddingLeft / 2 + (nums - 1) * largeLineHeight * i + (nums - 2) * mDividerLines * i , maxHeight / 2 + largeLineLength + distance, textPaintSelected);
            } else {
                canvas.drawText(currentValues[i], getPaddingLeft() + paddingLeft / 2 + (nums - 1) * largeLineHeight * i + (nums - 2) * mDividerLines * i , maxHeight / 2 + largeLineLength + distance, textPaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取屏幕上点击的坐标
                float x = event.getX();
                float y = event.getY();
                //如果坐标在我们的文字区域内，则将点击的文字改颜色
                for (int i = 0; i < currentValues.length; i++) {
                    if (x >= getPaddingLeft() + paddingLeft / 2 + (nums - 1) * largeLineHeight * i + (nums - 2) * mDividerLines * i - textPaint.measureText(currentValues[i]) / 2 - leftPading && x <= getPaddingLeft() + paddingLeft / 2 + (nums - 1) * largeLineHeight * i + (nums - 2) * mDividerLines * i + textPaint.measureText(currentValues[i]) / 2 + leftPading && y >= maxHeight / 2 + largeLineLength + 20 - topPadding && y <= maxHeight / 2 + largeLineLength + distance + 20 + topPadding) {
                        selectedNum = i;
                        invalidate();
                        if (onclick!=null){
                            onclick.onItemClicked(currentValues[i]);
                        }
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public RVAdapterItemOnClick getOnclick() {
        return onclick;
    }

    public void setOnclick(RVAdapterItemOnClick onclick) {
        this.onclick = onclick;
    }
}
