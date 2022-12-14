package com.szip.smartdream.Controller;

import static com.szip.smartdream.MyApplication.FILE;
import static com.szip.smartdream.Util.mutils.checkALlPermissions;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.szip.smartdream.MyApplication;
import com.szip.smartdream.R;
import com.szip.smartdream.Service.BleService;
import com.szip.smartdream.Util.MathUitl;
import com.szip.smartdream.View.MyAlerDialog;

public class WelcomeActivity extends BaseActivity implements Runnable {

    /**
     * 延时线程
     */
    private Thread thread;
    private int time = 3;
    private int SleepEECode = 100;

    /**
     * 轻量级文件
     */
    private SharedPreferences sharedPreferencesp;

    private boolean isFirst;
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);
        app = (MyApplication) getApplicationContext();
        if (sharedPreferencesp == null) {
            sharedPreferencesp = getSharedPreferences(FILE, MODE_PRIVATE);
        }

        isFirst = sharedPreferencesp.getBoolean("isFirst", true);
        app.setUserInfo(MathUitl.loadInfoData(sharedPreferencesp));


        if (isFirst) {
            MyAlerDialog.getSingle().showAlerDialogWithPrivacy(getString(R.string.privacy1), getString(R.string.privacyTip),
                    getString(R.string.agree), getString(R.string.disagree), false, new MyAlerDialog.AlerDialogOnclickListener() {
                        @Override
                        public void onDialogTouch(boolean flag) {
                            if (flag) {
                                sharedPreferencesp.edit().putBoolean("isFirst", false).commit();
                                /**
                                 * 获取权限·
                                 * */
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                                            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, SleepEECode);
                                    }
                                    initData();
                                } else {
                                    initData();
                                }
                            } else {
                                finish();
                            }
                        }
                    }, this);
        } else {
            initData();
        }
//            MyAlerDialog.getSingle().showAlerDialogWithPrivacy(getString(R.string.privacy1),getString(R.string.privacyTip),
//                    getString(R.string.agree), getString(R.string.disagree), false, new MyAlerDialog.AlerDialogOnclickListener() {
//                        @Override
//                        public void onDialogTouch(boolean flag) {
//                            if (flag){
//                                sharedPreferencesp.edit().putBoolean("isFirst",false).commit();
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
//                                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
//                                            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, SleepEECode);
//                                    }
//                                    initData();
//                                }else {
//                                    initData();
//                                }
//                            }else{
//                                finish();
//                            }
//                        }
//                    },this);


    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == SleepEECode){
//            int code = grantResults[0];
//            int code1 = grantResults[1];
//            int code2 = grantResults[2];
//            if (code == PackageManager.PERMISSION_GRANTED&&code1 == PackageManager.PERMISSION_GRANTED&&code2 == PackageManager.PERMISSION_GRANTED){
//                initData();
//            }else {
//                WelcomeActivity.this.finish();
//            }
//        }
//    }

    private void initData() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (time != 0) {
                Thread.sleep(1000);
                time = time - 1;
            }
            init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkALlPermissions(WelcomeActivity.this);
        }
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkALlPermissions(WelcomeActivity.this))
                if (app.getStartState() == 0) {//已登录
                    if (app.getUserInfo().getDeviceCode() != null) {//已绑定
                        BleService.getInstance().setmMac(app.getUserInfo().getDeviceCode());
                        BleService.getInstance().startConnectDevice();
                        Intent guiIntent = new Intent();
                        guiIntent.setClass(WelcomeActivity.this, MainActivity.class);
                        startActivity(guiIntent);
                        finish();
                    } else {//未绑定
                        Intent in = new Intent();
                        in.setClass(WelcomeActivity.this, FindDeviceActivity.class);
                        startActivity(in);
                        finish();
                    }
                } else if (app.getStartState() == 1) {//未登录
                    Intent in = new Intent();
                    in.setClass(WelcomeActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                } else {//登陆过期
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(getString(R.string.tokenTimeout));
                        }
                    });
                    Intent in = new Intent();
                    in.setClass(WelcomeActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                }
        } else {
            startActivity(
                    new Intent(
                            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null)
                    )
            );
        }
    }
}
