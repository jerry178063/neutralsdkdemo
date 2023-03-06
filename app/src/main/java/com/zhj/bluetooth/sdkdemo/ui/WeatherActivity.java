package com.zhj.bluetooth.sdkdemo.ui;

import android.view.View;
import android.widget.EditText;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.WeatherBean;
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil;

import butterknife.BindView;

public class WeatherActivity extends BaseActivity {

    @BindView(R.id.etWeatherUnit)
    EditText etWeatherUnit;
    @BindView(R.id.etWeatherTemp)
    EditText etWeatherTemp;
    @BindView(R.id.etWeatherType)
    EditText etWeatherType;
    @Override
    protected int getContentView() {
        return R.layout.activity_weather;
    }

    private WeatherBean weatherBean;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText("天气设置");
        rightText.setText("设置");
        weatherBean = new WeatherBean();
        weatherBean.setCurrentTemp(36);
        weatherBean.setState(1);
        weatherBean.setTempUnit(0);
        weatherBean.setWeatherType(1);
        rightText.setOnClickListener(view -> {
            weatherBean.setCurrentTemp(Integer.parseInt(etWeatherTemp.getText().toString()));
            weatherBean.setTempUnit(Integer.parseInt(etWeatherUnit.getText().toString()));
            weatherBean.setWeatherType(Integer.parseInt(etWeatherType.getText().toString()));
            weatherBean.setMaxTemp(Integer.parseInt(etWeatherTemp.getText().toString())+1);
            weatherBean.setMinTemp(Integer.parseInt(etWeatherTemp.getText().toString())-1);

            WeatherBean weatherBeanTomorrow = new WeatherBean();
            weatherBeanTomorrow.setWeatherType(Integer.parseInt(etWeatherType.getText().toString())+1);
            weatherBeanTomorrow.setMaxTemp(Integer.parseInt(etWeatherTemp.getText().toString())+2);
            weatherBeanTomorrow.setMinTemp(Integer.parseInt(etWeatherTemp.getText().toString())-2);

            WeatherBean weatherBeanAfterTomorrow = new WeatherBean();
            weatherBeanAfterTomorrow.setWeatherType(Integer.parseInt(etWeatherType.getText().toString())+2);
            weatherBeanAfterTomorrow.setMaxTemp(Integer.parseInt(etWeatherTemp.getText().toString())+3);
            weatherBeanAfterTomorrow.setMinTemp(Integer.parseInt(etWeatherTemp.getText().toString())-3);
            LogUtil.d("type:"+weatherBeanTomorrow.getWeatherType()+","+weatherBeanAfterTomorrow.getWeatherType());
//            BleSdkWrapper.setWeather(weatherBean,new OnLeWriteCharacteristicListener() {
//                @Override
//                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//
//                }
//
//                @Override
//                public void onFailed(WriteBleException e) {
//
//                }
//            });
        });

        etWeatherUnit.setText(weatherBean.getTempUnit()+"");
        etWeatherTemp.setText(weatherBean.getCurrentTemp()+"");
        etWeatherType.setText(weatherBean.getWeatherType()+"");
    }
}
