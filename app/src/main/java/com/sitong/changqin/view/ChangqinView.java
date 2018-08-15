package com.sitong.changqin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen.zhiwei on 2018-3-19.
 */

public class ChangqinView extends LinearLayout {
    private String TAG = "ChangqinView";
    private static final int mPianoLineNumber = 7;//总共的琴弦数量
    private static final int mPianoAnchorPoint = 13;//所有锚点数量
    private static int mDividerLines = 10;//左侧琴弦之间的分割高度
    private static int mRightDividerLines = 10;//右侧琴弦之间的分割高度
    private static int lineHeight = 1;//线条的宽度


    private Paint mLinesPaint;//琴弦画笔
    private Paint mLinesAnchorPaint;//琴弦上面的点画笔
    private Paint mRightPointPaint;//最右边圆点画笔

    private int maxWidth;//控件总宽度
    private int maxHeight;//空间总高度
    private int mRealWidth;//实际画布大小
    private int mRealHeight;
    private Map<Integer, Float> mMoveMap = new HashMap<>();//在线上动态显示的点
    private Context mContext;

    private int mCyclerviewRadius = 8;//最右边圆的半径


    private float[] mLinesPoints;//所有琴弦点
    private float[] mLinesRightPoints;//琴弦最右边的点
    private float[] mLinesTopPoints;//琴弦上边的13个锚点


    public ChangqinView(Context context) {
        super(context);
        setWillNotDraw(false);
        mContext = context;
    }

    public ChangqinView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mContext = context;
    }

    public ChangqinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mContext = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {

        mLinesPaint = new Paint();
        mLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinesPaint.setAntiAlias(true);
        mLinesPaint.setDither(true);
        mLinesPaint.setStrokeWidth(2);
        mLinesPaint.setColor(Color.BLUE);

        mLinesAnchorPaint = new Paint();
        mLinesAnchorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinesAnchorPaint.setAntiAlias(true);
        mLinesAnchorPaint.setColor(Color.WHITE);


        mRightPointPaint = new Paint();
        mRightPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mRightPointPaint.setAntiAlias(true);
        mRightPointPaint.setColor(Color.WHITE);

    }


    public void setmMoveMap(Map<Integer, Float> mMoveMap) {
        this.mMoveMap = mMoveMap;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //获取屏幕的宽度
        maxWidth = getWidth();

        maxHeight = getHeight();

        mRealWidth = maxWidth - getPaddingRight() - getPaddingLeft();
        mRealHeight = maxHeight - getPaddingTop() - getPaddingBottom();
        LogUtils("高度：" + maxHeight);
        LogUtils("宽度：" + maxWidth);

        mDividerLines = (maxHeight - getPaddingBottom() - getPaddingTop() - lineHeight * mPianoLineNumber) / mPianoLineNumber / 4;
        mRightDividerLines = (maxHeight - getPaddingBottom() - getPaddingTop() - lineHeight * mPianoLineNumber) / mPianoLineNumber / 4 + 20;


//琴弦view 对应的point
        mLinesPoints = new float[]{
                (int) getX() + getPaddingLeft(), (getY() + mRealHeight / 2) - 3 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) - 3 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (getY() + mRealHeight / 2) - 2 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) - 2 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (getY() + mRealHeight / 2) - 1 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) - 1 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (getY() + mRealHeight / 2), (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2),
                (int) getX() + getPaddingLeft(), (getY() + mRealHeight / 2) + 1 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) + 1 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (getY() + mRealHeight / 2) + 2 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) + 2 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (getY() + mRealHeight / 2) + 3 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) + 3 * mRightDividerLines,
        };
        //上方锚点point
        mLinesTopPoints = new float[]{
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.1), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.2), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.25), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.3), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.35), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.4), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.45), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.5), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.6), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.7), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.8), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.9), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.95), (getY() + mRealHeight / 2) - 4 * mRightDividerLines,

        };
        //最右边的point
        mLinesRightPoints = new float[]{
                (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) - 3 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) - 2 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) - 1 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2),
                (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) + 1 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) + 2 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (getY() + mRealHeight / 2) + 3 * mRightDividerLines,

        };

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画线
        canvas.drawLines(mLinesPoints, mLinesPaint);

        //画最右边的点
        for (int i = 0; i < mLinesRightPoints.length; i++) {
            canvas.drawCircle(mLinesRightPoints[i], mLinesRightPoints[i += 1], mCyclerviewRadius, mRightPointPaint);
        }
        //画最上边的点
        for (int i = 0; i < mLinesTopPoints.length; i++) {
            canvas.drawCircle(mLinesTopPoints[i], mLinesTopPoints[i += 1], mCyclerviewRadius, mRightPointPaint);
        }
//        canvas.drawPoints(mLinesTopPoints,mRightPointPaint);

        for (Integer key : mMoveMap.keySet()) {
            Float value = mMoveMap.get(key);
            canvas.drawCircle((float) ((getX() + getPaddingLeft() + mRealWidth) * value), ((float) ((((getY() + mRealHeight / 2) + (key - 4) * mRightDividerLines) - ((getY() + mRealHeight / 2) + (key - 4) * mDividerLines)) * value + (getY() + mRealHeight / 2) + (key - 4) * mDividerLines)), mCyclerviewRadius, mRightPointPaint);
        }

    }

    private void LogUtils(String s) {
        Log.e(TAG, s);
    }

}

