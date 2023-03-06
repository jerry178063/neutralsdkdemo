package com.zhj.bluetooth.sdkdemo.base;


import com.zhj.bluetooth.sdkdemo.util.CnWinUtil;
import com.zhj.bluetooth.sdkdemo.util.DialogHelperNew;

/**
 * MVP模式的Activity的基类
 */
public abstract class BaseMvpActivity<T extends BasePersenter> extends BaseActivity implements IBaseView{

    protected T mPresenter;

    @Override
    protected void onViewCreate() {
        super.onViewCreate();
        mPresenter = CnWinUtil.getT(this, 0);
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }


    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void showLoading() {
        DialogHelperNew.buildWaitDialog(this,true);
    }

    @Override
    public void showLoadingFalse() {
        DialogHelperNew.buildWaitDialog(this,false);
    }

    @Override
    public void hideLoading() {
        DialogHelperNew.dismissWait();
    }

    @Override
    public void showNetError(String msg) {

    }

    @Override
    public void goBack() {
        this.finish();
    }
}
