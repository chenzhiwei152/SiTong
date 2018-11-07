package com.sitong.changqin.ui.listerner;

public interface ProgressCallback {
    void onProgressCallback(double progress);
    void onProgressFailed();
    void onProgressSuccess();
}
