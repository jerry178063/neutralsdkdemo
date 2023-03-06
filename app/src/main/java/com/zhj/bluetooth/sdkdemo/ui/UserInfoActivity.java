package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.View;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.UserBean;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvHight)
    TextView tvHight;
    @BindView(R.id.tvWeight)
    TextView tvWeight;
    @BindView(R.id.tvStepDistance)
    TextView tvStepDistance;
    @Override
    protected int getContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.user_info_title));
        BleSdkWrapper.getUserInfo(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                UserBean userBean = (UserBean) handlerBleDataResult.data;
                //0x00:男 0x01:女 0x02:其他
                tvSex.setText(getResources().getString(R.string.user_info_sex)+userBean.getGender());
                tvAge.setText(getResources().getString(R.string.user_info_age)+userBean.getAge());
                tvHight.setText(getResources().getString(R.string.user_info_height)+userBean.getHeight()); //单位CM
                tvWeight.setText(getResources().getString(R.string.user_info_weight)+userBean.getWeight());//单位0.1KG 如果返回600  就是60KG  设置的时候也需要相对应的KG*10
                tvStepDistance.setText(getResources().getString(R.string.user_info_stride)+userBean.getStepDistance());//单位CM
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        rightText.setText("保存");
        rightText.setOnClickListener(view -> {
            UserBean userBean = new UserBean();
            userBean.setAge(25);
            userBean.setWeight(700);
            userBean.setHeight(200);
            userBean.setGender(1);
            BleSdkWrapper.setUserInfo(userBean, new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    showToast("保存成功");
                    UserInfoActivity.this.finish();
                }

                @Override
                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                }

                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        });
    }
}
