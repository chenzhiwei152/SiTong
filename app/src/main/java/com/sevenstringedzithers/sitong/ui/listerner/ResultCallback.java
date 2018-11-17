package com.sevenstringedzithers.sitong.ui.listerner;

public interface ResultCallback<T> {
    void onsFailed(T reason);
    void onSuccess(T result);
}
