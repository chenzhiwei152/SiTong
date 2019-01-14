package com.sevenstringedzithers.sitong.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import com.jyall.android.common.utils.UIUtil;
import com.sevenstringedzithers.sitong.R;
import com.sevenstringedzithers.sitong.utils.ExtraUtils;

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
    private Paint mPintStroke;//空心

    private int maxWidth;//控件总宽度
    private int maxHeight;//空间总高度
    private int mRealWidth;//实际画布大小
    private int mRealHeight;
    private Map<Integer, Float> mMoveMap = new HashMap<>();//在线上动态显示的点
    private boolean isYanyin = false;
    private boolean ishuayin = false;
    private HashMap<Integer, Float> toPercent;
    private Context mContext;

    private int mCyclerviewRadius = 4;//最右边圆的半径
    private int mActiveCyclerviewRadius = 7;//动态点半径
    private int mPointCyclerviewRadius = 9;//锚点的半径


    private float[] mLinesPoints;//所有琴弦点
    private float[] mLinesRightPoints;//琴弦最右边的点
    private float[] mLinesTopPoints;//琴弦上边的13个锚点

    private int DURATION = 0;//动画持续时间
    private float mInterpolatedTime;

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

        mCyclerviewRadius=UIUtil.dip2px(mContext,1f);
        mActiveCyclerviewRadius=UIUtil.dip2px(mContext,2f);
        mPointCyclerviewRadius=UIUtil.dip2px(mContext,3f);


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

        mPintStroke = new Paint();
        mPintStroke.setStyle(Paint.Style.STROKE);
        mPintStroke.setStrokeWidth(1);
        mPintStroke.setAntiAlias(true);
        mPintStroke.setColor(Color.WHITE);

    }


    public void setmMoveMap(Map<Integer, Float> mMoveMap, boolean isYanyin, boolean ishuayin, Double duration, HashMap<Integer, Float> toPercent) {
        this.mMoveMap = mMoveMap;
        this.isYanyin = isYanyin;
        this.DURATION = (int) (duration * 1000);
        this.ishuayin = ishuayin;
        this.toPercent = toPercent;
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
                (float) ((getX() + getPaddingLeft() + mRealWidth / 8)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth / 6)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 0.2)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 0.25)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth / 3)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 0.4)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 0.5)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 0.6)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 2 / 3)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 0.75)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 0.8)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 5 / 6)), (maxHeight / 2) - 4 * mRightDividerLines,
                (float) ((getX() + getPaddingLeft() + mRealWidth * 7 / 8)), (maxHeight / 2) - 4 * mRightDividerLines,

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

            if (ishuayin && toPercent.size() > 0) {
                value += (toPercent.get(key) - value) * mInterpolatedTime;
            }
            PointF f1 = new PointF(pointLists.get((key - 1) * 4), pointLists.get((key - 1) * 4 + 1));
            PointF f2 = new PointF(pointLists.get((key - 1) * 4 + 2), pointLists.get((key - 1) * 4 + 3));
            PointF result = ExtraUtils.Companion.CalculateBezierPointForQuadratic1(value, f1, f2);
            if (isYanyin) {
                canvas.drawCircle(result.x, result.y, mActiveCyclerviewRadius, mPintStroke);
            } else {
                canvas.drawCircle(result.x, result.y, mActiveCyclerviewRadius, mRightPointPaint);
            }
        }

    }

    private class MoveAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }
    }

    public void startAnimation() {
        mInterpolatedTime = 0;
        MoveAnimation move = new MoveAnimation();
        move.setDuration(DURATION);
        move.setInterpolator(new LinearInterpolator());
        startAnimation(move);

    }

    public void startAnim() {
        this.startAnimation(new BarAnimation());
    }

    public class BarAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            postInvalidate();
            Log.e("AmountView", "tempcount:" + mInterpolatedTime);
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(DURATION);
            //设置动画结束后保留效果
            setFillAfter(true);
            setInterpolator(new LinearInterpolator());
        }
    }

    private void LogUtils(String s) {
        Log.e(TAG, s);
    }

}

