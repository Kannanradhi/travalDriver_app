package com.cabily.cabilydriver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

public class RegistrationMobileVerificationActivity extends AppCompatActivity {
    private View headerView;
    private RelativeLayout headerBackRl;
    private CollapsingToolbarLayout headerCollapseTL;

    private RelativeLayout mobileNumberRl;
    private ImageView codeArrowIv;
    private TextInputLayout codeTIL, mobileNumberTIl, otpTIl;
    private EditText codeEt, mobileNumberEt, otpEt;
    private TextView locationTypeNextTv;
    private CountryCodePicker ccp;
//    private RegistrationInfoModel registrationInfoModel;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            finish();
            CloseKeyBoard();
            overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);

            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_mobile_verification);
        Init();

        headerBackRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CloseKeyBoard();
                overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);
            }
        });

        locationTypeNextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(otpTIl.getVisibility()==View.VISIBLE)
                {
                    Intent nextIntent = new Intent(RegistrationMobileVerificationActivity.this, RegistrationLoginDetailsActivity.class);
//                nextIntent.putExtra("modelObject", registrationInfoModel);
                    CloseKeyBoard();
                    startActivity(nextIntent);
                    overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);
                }else {
                    otpTIl.setVisibility(View.VISIBLE);
                    mobileNumberRl.setVisibility(View.GONE);
                    OpenKeyBoard();
                }

            }
        });

    }

    private void Init() {
//        Intent in = getIntent();
//        registrationInfoModel = (RegistrationInfoModel) in.getSerializableExtra("modelObject");

        headerView = findViewById(R.id.header_registration);
        headerBackRl = (RelativeLayout) headerView.findViewById(R.id.back_layout);
        headerCollapseTL = (CollapsingToolbarLayout) headerView.findViewById(R.id.collapsing_toolbar);
        headerCollapseTL.setTitleEnabled(true);
        headerCollapseTL.setCollapsedTitleTextColor(getResources().getColor(R.color.white_color));
        headerCollapseTL.setTitle(getResources().getString(R.string.mobile_verification_header_label));
        headerCollapseTL.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        headerCollapseTL.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);

        locationTypeNextTv = (TextView) findViewById(R.id.registration_mobile_next_tv);
        mobileNumberRl = (RelativeLayout) findViewById(R.id.mobile_number_layout);
//        codeArrowIv = (ImageView) findViewById(R.id.code_drop_down_arrow);
//        codeTIL = (TextInputLayout) findViewById(R.id.registration_input_layout_code);
        mobileNumberTIl = (TextInputLayout) findViewById(R.id.registration_input_layout_mobile);
        otpTIl = (TextInputLayout) findViewById(R.id.registration_input_layout_otp);
//        codeEt = (EditText) findViewById(R.id.registration_country_code_et);
        mobileNumberEt = (EditText) findViewById(R.id.registration_phone_editText);
        otpEt = (EditText) findViewById(R.id.registration_otp_editText);
        ccp = (CountryCodePicker) findViewById(R.id.registration_input_ccp);



    }

    private void CloseKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private void OpenKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

    }
}
