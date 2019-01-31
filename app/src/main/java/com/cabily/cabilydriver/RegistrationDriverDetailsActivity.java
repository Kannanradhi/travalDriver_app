package com.cabily.cabilydriver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabily.cabilydriver.Pojo.RegistrationCountryPojo;
import com.cabily.cabilydriver.widgets.SnackFlashBar;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrationDriverDetailsActivity extends AppCompatActivity {
    private View headerView;
    private RelativeLayout headerBackRl, mainRl;
    private SnackFlashBar snackFlashBar;
    private CollapsingToolbarLayout headerCollapseTL;

    private TextView driverDetailsNextTv;
    private EditText addressEt, cityEt, stateEt, zipCodeEt;
    private MaterialSpinner countrySpinner;
    private ArrayList<RegistrationCountryPojo> countryArrayList = new ArrayList<>();
    private ArrayList<String> countryArray = new ArrayList<>();
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
        setContentView(R.layout.activity_registration_driver_details);
        Init();

        headerBackRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CloseKeyBoard();
                overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);
            }
        });

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0)
                {
                    Toast.makeText(getApplicationContext(),countryArrayList.get(position).getCountryId()+" and "+countryArrayList.get(position).getCountryDialCode(),Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Nothing selected spineer",Toast.LENGTH_SHORT).show();

            }
        });

        driverDetailsNextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressEt.getText().toString().trim().length() == 0) {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.driver_details_address_empty_error), mainRl, "error");
                } else if (cityEt.getText().toString().trim().length() == 0) {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.driver_details_city_empty_error), mainRl, "error");
                } else if (stateEt.getText().toString().trim().length() == 0) {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.driver_details_state_empty_error), mainRl, "error");

                } else if (zipCodeEt.getText().toString().trim().length() == 0) {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.driver_details_zip_code_empty_error), mainRl, "error");

                } else {
//                    RegistrationInfoModel registrationInfoModel = new RegistrationInfoModel();
                    RegistrationLoginTypeActivity.registrationInfoModel.setDriverDetailsAttr(addressEt.getText().toString(), cityEt.getText().toString(), stateEt.getText().toString(), zipCodeEt.getText().toString());

                    Intent nextIntent = new Intent(RegistrationDriverDetailsActivity.this, RegistrationVehicleInfoActivity.class);
                    CloseKeyBoard();
                    startActivity(nextIntent);
                    overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);
                }

            }
        });
    }

    private void Init() {
        headerView = findViewById(R.id.header_registration);
        headerBackRl = (RelativeLayout) headerView.findViewById(R.id.back_layout);
        snackFlashBar = new SnackFlashBar(RegistrationDriverDetailsActivity.this);
        mainRl = (RelativeLayout) findViewById(R.id.main_layout);
        headerCollapseTL = (CollapsingToolbarLayout) headerView.findViewById(R.id.collapsing_toolbar);
        headerCollapseTL.setTitleEnabled(true);
        headerCollapseTL.setCollapsedTitleTextColor(getResources().getColor(R.color.white_color));
        headerCollapseTL.setTitle(getResources().getString(R.string.driver_details_header_label));
        headerCollapseTL.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        headerCollapseTL.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);

        driverDetailsNextTv = (TextView) findViewById(R.id.registration_driver_details_details_next_tv);
        addressEt = (EditText) findViewById(R.id.registration_driver_details_address_editText);
        cityEt = (EditText) findViewById(R.id.registration_driver_details_city_editText);
        stateEt = (EditText) findViewById(R.id.registration_driver_details_state_editText);
        zipCodeEt = (EditText) findViewById(R.id.registration_driver_details_zipcode_editText);
        countrySpinner = (MaterialSpinner) findViewById(R.id.country_spinner);
        Intent in = getIntent();
//        registrationInfoModel = (RegistrationInfoModel) in.getSerializableExtra("modelObject");
        countryArrayList =  RegistrationLoginTypeActivity.registrationInfoModel.getCountryArray();
        int countryArraySize=countryArrayList.size();

        for(int i=0;i<countryArraySize;i++)
        {
            String country=countryArrayList.get(i).getCountryName();
            countryArray.add(country);
        }

        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(RegistrationDriverDetailsActivity.this,R.layout.spinner_item_registration,countryArray);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_registration);
        countrySpinner.setAdapter(spinnerAdapter);
    }

    private void CloseKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
