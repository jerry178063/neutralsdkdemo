package com.zhj.bluetooth.sdkdemo.base;

public interface IBaseView {
    void showMsg(String msg);

    void showNetError(String msg);

    void showLoading();

    void showLoadingFalse();

    void hideLoading();

    void goBack();
}
