package com.sitong.changqin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.sitong.changqin.utils.ExtraUtils;
import com.stringedzithers.sitong.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen.zhiwei on 2018-3-19.
 */

public class ChangqinView extends LinearLayout {
    private String TAG = "ChangqinView";
    private static final int mPianoLineNumber = 7;//总共的琴弦数量
    private static final int mPianoAnchorPoint = 13;//所有锚点数量
    private static int mDividerLines = 20;//左侧琴弦之间的分割高度
    private static int mRightDividerLines = 20;//右侧琴弦之间的分割高度
    private static int lineHeight = 2;//线条的宽度


    private Paint mLinesPaint;//琴弦画笔
    private Paint mLinesAnchorPaint;//琴弦上面的点画笔
    private Paint mPonitRightPointPaint;//锚点圆点画笔
    private Paint mRightPointPaint;//最右边圆点画笔

    private int maxWidth;//控件总宽度
    private int maxHeight;//空间总高度
    private int mRealWidth;//实际画布大小
    private int mRealHeight;
    private Map<Integer, Float> mMoveMap = new HashMap<>();//在线上动态显示的点
    private Context mContext;

    private int mCyclerviewRadius = 4;//最右边圆的半径
    private int mActiveCyclerviewRadius = 7;//动态点半径
    private int mPointCyclerviewRadius = 12;//锚点的半径


    private float[] mLinesPoints;//所有琴弦点
    private float[] mLinesRightPoints;//琴弦最右边的点
    private float[] mLinesTopPoints;//琴弦上边的13个锚点

    private ArrayList<Integer> pointLists = new ArrayList<>();//所有琴弦点


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

    @SuppressLint("ResourceAsColor")
    private void init() {

        mLinesPaint = new Paint();
        mLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinesPaint.setAntiAlias(true);
        mLinesPaint.setDither(true);
        mLinesPaint.setStrokeWidth(2);
        mLinesPaint.setColor(getResources().getColor(R.color.color_d0a670));

        mLinesAnchorPaint = new Paint();
        mLinesAnchorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinesAnchorPaint.setAntiAlias(true);
        mLinesAnchorPaint.setColor(Color.WHITE);


        mPonitRightPointPaint = new Paint();
        mPonitRightPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPonitRightPointPaint.setAntiAlias(true);
        mPonitRightPointPaint.setColor(getResources().getColor(R.color.color_d0a670));


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
//        LogUtils("高度：" + maxHeight);
//        LogUtils("宽度：" + maxWidth);
//        LogUtils("宽度getY() ：" + getY() );
//        LogUtils("宽度mRealHeight ：" + mRealHeight );

        mDividerLines = (int) ((maxHeight - getPaddingBottom() - getPaddingTop() - lineHeight * mPianoLineNumber) / mPianoLineNumber / 1.5);
        mRightDividerLines = mDividerLines + 10;
        LogUtils("宽度mDividerLines ：" + mDividerLines);

//琴弦view 对应的point
        mLinesPoints = new float[]{
                (int) getX() + getPaddingLeft(), (maxHeight / 2) - 3 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) - 3 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (maxHeight / 2) - 2 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) - 2 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (maxHeight / 2) - 1 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) - 1 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (maxHeight / 2), (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2),
                (int) getX() + getPaddingLeft(), (maxHeight / 2) + 1 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) + 1 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (maxHeight / 2) + 2 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) + 2 * mRightDividerLines,
                (int) getX() + getPaddingLeft(), (maxHeight / 2) + 3 * mDividerLines, (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) + 3 * mRightDividerLines,
        };

        pointLists.add((int) getX() + getPaddingLeft());
        pointLists.add((maxHeight / 2) - 3 * mDividerLines);
        pointLists.add((int) getX() + getPaddingLeft() + mRealWidth);
        pointLists.add((maxHeight / 2) - 3 * mRightDividerLines);

        pointLists.add((int) getX() + getPaddingLeft());
        pointLists.add((maxHeight / 2) - 2 * mDividerLines);
        pointLists.add((int) getX() + getPaddingLeft() + mRealWidth);
        pointLists.add((maxHeight / 2) - 2 * mRightDividerLines);

        pointLists.add((int) getX() + getPaddingLeft());
        pointLists.add((maxHeight / 2) - 1 * mDividerLines);
        pointLists.add((int) getX() + getPaddingLeft() + mRealWidth);
        pointLists.add((maxHeight / 2) - 1 * mRightDividerLines);

        pointLists.add((int) getX() + getPaddingLeft());
        pointLists.add((maxHeight / 2));
        pointLists.add((int) getX() + getPaddingLeft() + mRealWidth);
        pointLists.add((maxHeight / 2));

        pointLists.add((int) getX() + getPaddingLeft());
        pointLists.add((maxHeight / 2) + 1 * mDividerLines);
        pointLists.add((int) getX() + getPaddingLeft() + mRealWidth);
        pointLists.add((maxHeight / 2) + 1 * mRightDividerLines);

        pointLists.add((int) getX() + getPaddingLeft());
        pointLists.add((maxHeight / 2) + 2 * mDividerLines);
        pointLists.add((int) getX() + getPaddingLeft() + mRealWidth);
        pointLists.add((maxHeight / 2) + 2 * mRightDividerLines);


        pointLists.add((int) getX() + getPaddingLeft());
        pointLists.add((maxHeight / 2) + 3 * mDividerLines);
        pointLists.add((int) getX() + getPaddingLeft() + mRealWidth);
        pointLists.add((maxHeight / 2) + 3 * mRightDividerLines);

        //上方锚点point
        mLinesTopPoints = new float[]{
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.1), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.2), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.25), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.3), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.35), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.4), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.45), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.5), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.6), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.7), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.8), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.9), (maxHeight / 2) - 5 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth) * 0.95), (maxHeight / 2) - 5 * mRightDividerLines,

        };
        //最右边的point
        mLinesRightPoints = new float[]{
                (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) - 3 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) - 2 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) - 1 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2),
                (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) + 1 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) + 2 * mRightDividerLines,
                (int) getX() + getPaddingLeft() + mRealWidth, (maxHeight / 2) + 3 * mRightDividerLines,

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
            canvas.drawCircle(mLinesTopPoints[i], mLinesTopPoints[i += 1], mPointCyclerviewRadius, mPonitRightPointPaint);
        }
//        canvas.drawPoints(mLinesTopPoints,mRightPointPaint);

        for (Integer key : mMoveMap.keySet()) {
            Float value = mMoveMap.get(key);

            PointF f1 = new PointF(pointLists.get((key - 1) * 4), pointLists.get((key - 1) * 4 + 1));
            PointF f2 = new PointF(pointLists.get((key - 1) * 4 + 2), pointLists.get((key - 1) * 4 + 3));
            canvas.drawCircle(ExtraUtils.Companion.CalculateBezierPointForQuadratic1(value, f1, f2).x, ExtraUtils.Companion.CalculateBezierPointForQuadratic1(value, f1, f2).y, mActiveCyclerviewRadius, mRightPointPaint);
        }

    }

    private void LogUtils(String s) {
        Log.e(TAG, s);
    }

}

