package com.zhj.bluetooth.sdkdemo.ui;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;

import java.util.Random;

public class PairingActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_pairing;
    }
    int i1 ;
    int i2 ;
    int i3 ;
    int i4 ;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_send_pairing));
        Random random = new Random();
        i1 = random.nextInt(10);
        i2 = random.nextInt(10);
        i3 = random.nextInt(10);
        i4 = random.nextInt(10);

    }

    private Dialog showPairingDialog(){
        Dialog dialog = new Dialog(this, R.style.center_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pairing,null);
        EditText et1 = view.findViewById(R.id.et1);
        EditText et2 = view.findViewById(R.id.et2);
        EditText et3 = view.findViewById(R.id.et3);
        EditText et4 = view.findViewById(R.id.et4);
        et1.setFocusable(true);
        et1.setFocusableInTouchMode(true);
        et1.requestFocus();
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >=1){
                    et2.requestFocus();
                }
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >=1){
                    et3.requestFocus();
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >=1){
                    et4.requestFocus();
                }
            }
        });

        view.findViewById(R.id.tvCanle).setOnClickListener(v -> {
            dialog.dismiss();
            //退出输入  解绑
        });
        view.findViewById(R.id.tvSure).setOnClickListener(v ->{
            if(TextUtils.isEmpty(et1.getText().toString()) || TextUtils.isEmpty(et2.getText().toString()) ||
                    TextUtils.isEmpty(et3.getText().toString()) || TextUtils.isEmpty(et4.getText().toString()) ){
                showToast(getResources().getString(R.string.scan_device_info_put_complete_code));
            }else{
                if(Integer.parseInt(et1.getText().toString()) == i1 &&
                        Integer.parseInt(et2.getText().toString()) == i2 &&
                        Integer.parseInt(et3.getText().toString()) == i3 &&
                        Integer.parseInt(et4.getText().toString()) == i4 ){
                    dialog.dismiss();
                }else{
                    showToast(getResources().getString(R.string.scan_device_info_put_error));
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");
                }
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = this.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }
}
