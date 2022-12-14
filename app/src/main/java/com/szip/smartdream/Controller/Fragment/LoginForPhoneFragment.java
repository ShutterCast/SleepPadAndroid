package com.szip.smartdream.Controller.Fragment;

import static com.szip.smartdream.MyApplication.FILE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szip.smartdream.Controller.OTPScreen;
import com.szip.smartdream.Interface.OnClickForLogin;
import com.szip.smartdream.R;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;

public class LoginForPhoneFragment extends BaseFragment implements View.OnClickListener {


    private OnClickForLogin clickForLogin;
    /**
     * 忘记密码
     */
    private TextView forgetTv;

    /**
     * 选择国家
     */
    private LinearLayout layout;
    private TextView countryTv;
    /**
     * 国家代码
     */
    private TextView codeTv;
    /**
     * 手机输入框以及相关控件
     */
    private EditText phoneEt;
    private ImageView phoneClearIv;
    /**
     * 密码输入框以及相关控件
     */
    private EditText passwordEt;
    private ImageView passwordClearIv;
    private CheckBox rememberCb;
    private CheckBox lawsCb;
    /**
     * 登录按钮
     */
    private TextView loginLl;

    /**
     * 轻量级文件
     */
    private SharedPreferences sharedPreferencesp;
    ;

    /**
     * 用于确定下一步按键是否可点击
     */
    private int flagForEt;

    private String mobiletv;
    /**
     * 事件监听
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout:
                    CityPicker.getInstance()
                            .setFragmentManager(getActivity().getSupportFragmentManager())
                            .enableAnimation(true)
                            .setAnimationStyle(R.style.CustomAnim)
                            .setLocatedCity(null)
                            .setHotCities(null)
                            .setOnPickListener(new OnPickListener() {
                                @Override
                                public void onPick(int position, City data) {
                                    countryTv.setText(data == null ? "" : data.getName());
                                    codeTv.setText(data.getCode());
                                }

                                @Override
                                public void onLocate() {

                                }
                            })
                            .show();
                    break;
                case R.id.phoneClearIv:
                    phoneEt.setText("");
                    break;
                case R.id.passwordClearIv:
                    passwordEt.setText("");
                    break;

                //Set on Click Listener On Continue Button That is user Enter Mobile Number or Not
                case R.id.continueBtn:
                    if (phoneEt.getText().toString().equals("")){
                        showToast(getString(R.string.inputNum));
                    }else if (passwordEt.getText().toString().equals("")){
                        showToast(getString(R.string.password));
                    }else {

                    }


//                    if (phoneEt.getText().toString().equals("")) {
//                        showToast(getString(R.string.inputNum));
//                    } else {
//                        clickForLogin.onLogin("+91", phoneEt.getText().toString(), "", true);
//                    }

                    //-----------------------------------------------------
//                    else if (passwordEt.getText().toString().equals("")){
//                        showToast(getString(R.string.password));
//                    }else if (clickForLogin!=null)
//                        clickForLogin.onLogin(codeTv.getText().toString(),phoneEt.getText().toString(),passwordEt.getText().toString(),rememberCb.isChecked());
                    break;
                // case R.id.forgetTv:
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(),ForgetPasswordActivity.class);
//                    intent.putExtra("flag",0);
//                    startActivity(intent);
//                    break;
            }
        }
    };

    /**
     * 返回一个fragment实例，Activity中调用
     */
    public static LoginForPhoneFragment newInstance(String param) {
        Bundle bundle = new Bundle();
        bundle.putString("param", param);
        LoginForPhoneFragment fragment = new LoginForPhoneFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setClickForLogin(OnClickForLogin clickForLogin) {
        this.clickForLogin = clickForLogin;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_for_phone;
    }

    @Override
    protected void afterOnCreated(Bundle savedInstanceState) {

        initView();
        initEvent();

        loginLl.setOnClickListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        // lawsCb = getView().findViewById(R.id.lawsCb);
        //forgetTv = getView().findViewById(R.id.forgetTv);
        //layout = getView().findViewById(R.id.layout);
        // countryTv = getView().findViewById(R.id.countryTv);
        // codeTv = getView().findViewById(R.id.codeTv);
        phoneEt = getView().findViewById(R.id.phoneNumberEt);
        mobiletv = phoneEt.getText().toString();
        // phoneClearIv = getView().findViewById(R.id.phoneClearIv);
        passwordEt = getView().findViewById(R.id.passwordEt);
        // passwordClearIv = getView().findViewById(R.id.passwordClearIv);
        // rememberCb = getView().findViewById(R.id.rememberCb);
        loginLl = getView().findViewById(R.id.continueBtn);


        if (sharedPreferencesp == null)
            sharedPreferencesp = getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
        phoneEt.setText(sharedPreferencesp.getString("phone", ""));
        passwordEt.setText(sharedPreferencesp.getString("password",""));

    }

    /**
     * 初始化监听
     */
    private void initEvent() {



        // forgetTv.setOnClickListener(onClickListener);
        // layout.setOnClickListener(onClickListener);
        // phoneClearIv.setOnClickListener(onClickListener);
        //passwordClearIv.setOnClickListener(onClickListener);
//        loginLl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (phoneEt.getText().toString().equals("")) {
//                    showToast(getString(R.string.inputNum));
//                    return;
//                } else {
//                    OTPScreen otp=new OTPScreen(clickForLogin,"+91", phoneEt.getText().toString(), "", true);
//                    otp.showNow(requireFragmentManager(),"SANJAY");
////                    Intent intent = new Intent(getContext(), OTP.class);
////                    //intent.putExtra("mobileNumber" , mobileTv.toString());
////                    startActivity(intent);
//                }
//
//            }
//        });
//        lawsCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                String psd = passwordEt.getText().toString();
//                if (isChecked){
//                    passwordEt.setInputType(0x90);
//                }else {
//                    passwordEt.setInputType(0x81);
//                }
//                passwordEt.setSelection(psd.length());
//            }
//        });

        //phoneEt.addTextChangedListener(watcher);
        // passwordEt.addTextChangedListener(watcher);
        //phoneEt.setOnFocusChangeListener(focusChangeListener);
        // passwordEt.setOnFocusChangeListener(focusChangeListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.continueBtn:
                if (phoneEt.getText().toString().equals("")){
                    showToast(getString(R.string.inputNum));
                }else if (passwordEt.getText().toString().equals("")){
                    showToast("Enter Password");
                }else if (clickForLogin!=null)
                    clickForLogin.onLogin("001",phoneEt.getText().toString(),passwordEt.getText().toString(),true);
                break;
        }
    }

    /**
     * 输入框键入监听器
     * */
//    private TextWatcher watcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }

//        @Override
//        public void afterTextChanged(Editable s) {
//            String data = s.toString();
//
//            switch (flagForEt){
//                case 0:
//                    if (TextUtils.isEmpty(data)){
//                        phoneClearIv.setVisibility(View.GONE);
//                    }else {
////                        phoneClearIv.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case 1:
//                    if (TextUtils.isEmpty(data)){
//                        passwordClearIv.setVisibility(View.GONE);
//                    }else {
//                        passwordClearIv.setVisibility(View.VISIBLE);
//                    }
//                    break;
//            }
//        }
    // };

    /**
     * 输入框焦点监听
     * */
//    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
//        @Override
//        public void onFocusChange(View v, boolean hasFocus) {
//            switch (v.getId()){
//                case R.id.phoneEt:
//                    if (!hasFocus)
//                        phoneClearIv.setVisibility(View.GONE);
//                    else {
//                        flagForEt = 0;
//                        if (!phoneEt.getText().toString().equals(""))
//                            phoneClearIv.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case R.id.passwordEt:
//                    if (!hasFocus)
//                        passwordClearIv.setVisibility(View.GONE);
//                    else {
//                        flagForEt = 1;
//                        if (!passwordEt.getText().toString().equals(""))
//                            passwordClearIv.setVisibility(View.VISIBLE);
//                    }
//                    break;
//            }
//        }
//    };

}
