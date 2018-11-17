package com.sevenstringedzithers.sitong.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ActivityStackManager {
    private static ActivityStackManager mInstance;
    private static Stack<Activity> mActivityStack;
    public static ActivityStackManager getInstance() {
        if (null == mInstance) {
            mInstance = new ActivityStackManager();
        }
        return mInstance;
    }
    private ActivityStackManager() {
        mActivityStack = new Stack<Activity>();
    }
    // 入栈
    public void addActivity(Activity activity) {
        mActivityStack.push(activity);
    }
    // 出栈
    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
    }
    //  彻底退出
    public void finishAllActivity() {
        Activity activity;
        while (!mActivityStack.empty()) {
            activity = mActivityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }
    // 结束指定类名的Activity
    public synchronized void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }
    // 查找栈中是否存在指定的activity
    public boolean checkActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }
    // 结束指定的Activity
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    // finish指定的activity之上所有的activity
    public boolean finishToActivity(Class<? extends Activity> actCls, boolean isIncludeSelf) {
        List<Activity> buf = new ArrayList<Activity>();
        int size = mActivityStack.size();
        Activity activity = null;
        for (int i = size - 1; i >= 0; i--) {
            activity = mActivityStack.get(i);
            if (activity.getClass().isAssignableFrom(actCls)) {
                for (Activity a : buf) {
                    a.finish();
                }
                return true;
            } else if (i == size - 1 && isIncludeSelf) {
                buf.add(activity);
            } else if (i != size - 1) {
                buf.add(activity);
            }
        }
        return false;
    }
}