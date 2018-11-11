package com.sitong.changqin.utils;

import android.os.CountDownTimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhao.wenchao on 2018/5/18.
 * email: zhao.wenchao@jyall.com
 * introduce:
 */
public class CountDownTimerUtils {
    private final static long ONE_SECOND = 1000;
    /**
     * 总倒计时时间
     */
    private long mMillisInFuture = 0;
    /**
     * 定期回调的时间 必须大于0 否则会出现ANR
     */
    private long mCountDownInterval;
    /**
     * 倒计时结束的回调
     */
    private FinishDelegate mFinishDelegate;
    /**
     * 定期回调
     */
    private TickDelegate mTickDelegate;
    private MyCountDownTimer mCountDownTimer;

    /**
     * 获取 CountDownTimerUtils
     *
     * @return CountDownTimerUtils
     */
    public static CountDownTimerUtils getCountDownTimer() {
        return new CountDownTimerUtils();
    }

    /**
     * 设置定期回调的时间 调用{@link #setTickDelegate(TickDelegate)}
     *
     * @param pCountDownInterval 定期回调的时间 必须大于0
     * @return CountDownTimerUtils
     */
    public CountDownTimerUtils setCountDownInterval(long pCountDownInterval) {
        this.mCountDownInterval = pCountDownInterval;
        return this;
    }

    /**
     * 设置倒计时结束的回调
     *
     * @param pFinishDelegate 倒计时结束的回调接口
     * @return CountDownTimerUtils
     */
    public CountDownTimerUtils setFinishDelegate(FinishDelegate pFinishDelegate) {
        this.mFinishDelegate = pFinishDelegate;
        return this;
    }

    /**
     * 设置总倒计时时间
     *
     * @param pMillisInFuture 总倒计时时间
     * @return CountDownTimerUtils
     */
    public CountDownTimerUtils setMillisInFuture(long pMillisInFuture) {
        this.mMillisInFuture = pMillisInFuture;
        return this;
    }

    /**
     * 设置定期回调
     *
     * @param pTickDelegate 定期回调接口
     * @return CountDownTimerUtils
     */
    public CountDownTimerUtils setTickDelegate(TickDelegate pTickDelegate) {
        this.mTickDelegate = pTickDelegate;
        return this;
    }

    /**
     * 开始倒计时
     */
    public void start() {
        if (mCountDownTimer == null) {
            create();
        }
        mCountDownTimer.start();
    }

    public void create() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (mCountDownInterval <= 0) {
            mCountDownInterval = mMillisInFuture + ONE_SECOND;
        }
        mCountDownTimer = new MyCountDownTimer(mMillisInFuture, mCountDownInterval);
        mCountDownTimer.setTickDelegate(mTickDelegate);
        mCountDownTimer.setFinishDelegate(mFinishDelegate);
    }

    /**
     * 取消倒计时
     */
    public void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    /**
     * 倒计时结束的回调接口
     */
    public interface FinishDelegate {
        void onFinish();
    }

    /**
     * 定期回调的接口
     */
    public interface TickDelegate {
        void onTick(long pMillisUntilFinished);
    }

    private static class MyCountDownTimer extends CountDownTimer {
        private FinishDelegate mFinishDelegate;
        private TickDelegate mTickDelegate;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mTickDelegate != null) {
                mTickDelegate.onTick(millisUntilFinished);
            }
        }

        @Override
        public void onFinish() {
            if (mFinishDelegate != null) {
                mFinishDelegate.onFinish();
            }
        }

        void setFinishDelegate(FinishDelegate pFinishDelegate) {
            this.mFinishDelegate = pFinishDelegate;
        }

        void setTickDelegate(TickDelegate pTickDelegate) {
            this.mTickDelegate = pTickDelegate;
        }
    }


    /**
     * 获取传入时间和当前时间的时间差
     * @return
     */
    public static Long getTimediff(String currentStamp,String endStamp){
        Date d1 = transForDate(Long.parseLong(endStamp));
        Date today =transForDate(Long.parseLong(currentStamp));

        return Math.abs((d1.getTime()-today.getTime())/1000);
    }

    /**
     * 时间戳转日期
     * @param ms
     * @return
     */
    public static Date transForDate(Long ms){
        if(ms==null){
            ms=0L;
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp=null;
        if(ms!=null){
            try {
                String str=sdf.format(ms);
                temp=sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }
}
