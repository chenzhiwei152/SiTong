package com.sitong.changqin.ui.listerner;

public interface ResultCallback<T> {
    void onsFailed(T reason);
    void onSuccess(T result);
}
