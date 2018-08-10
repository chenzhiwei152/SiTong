package com.jyall.android.common.evnetbus;

/**
 */
public class EventBusCenter<T> {

    /**
     * 接收的数据
     */
    private T data;
    /**
     * 接收码
     */
    private int eventCode = -1;

    public EventBusCenter(int eventCode) {
        this.eventCode = eventCode;
    }

    public EventBusCenter(int eventCode, T data) {
        this.eventCode = eventCode;
        this.data = data;
    }


    public int getEvenCode() {
        return eventCode;
    }

    public T getData() {
        return data;
    }


}
