package com.sevenstringedzithers.sitong.ui.listerner;

public interface ProgressCallback {
    void onProgressCallback(double progress);
    void onProgressFailed();
    void onProgressSuccess();
}
