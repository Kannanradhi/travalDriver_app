package com.cabily.cabilydriver;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.app.service.ServiceConstant;
import com.app.service.ServiceRequest;
import com.cabily.cabilydriver.Pojo.RegistrationCategoryPojo;
import com.cabily.cabilydriver.Pojo.RegistrationCountryPojo;
import com.cabily.cabilydriver.Pojo.RegistrationInfoModel;
import com.cabily.cabilydriver.Pojo.RegistrationLocationPojo;
import com.cabily.cabilydriver.Utils.ConnectionDetector;
import com.cabily.cabilydriver.widgets.SnackFlashBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrationVehicleInfoActivity extends AppCompatActivity {

    private View headerView;
    private RelativeLayout headerBackRl, mainRl;
    ;
    private CollapsingToolbarLayout headerCollapseTL;
    private SnackFlashBar snackFlashBar;

    private TextView vehicleInfoNextTv;
    private RadioGroup vehicleAcRg;
    private MaterialSpinner typeSpinner, makeSpinner, modelSpinner, yearSpinner;
    ArrayAdapter<String> typeAdapter, makeAdapter, modelAdapter, yearAdapter;

    private ArrayList<String> typeSpinnerList = new ArrayList<String>();
    private ArrayList<String> makeSpinnerList = new ArrayList<String>();
    private ArrayList<String> modelSpinnerList = new ArrayList<String>();
    private ArrayList<String> yearSpinnerList = new ArrayList<String>();

    ArrayList<String> maintypecatid = new ArrayList<String>();
    ArrayList<String> maintypename = new ArrayList<String>();
    ArrayList<String> maintypenameid = new ArrayList<String>();
    ArrayList<String> maintypenamecatid = new ArrayList<String>();

    ArrayList<String> maintypenamemakers = new ArrayList<String>();
    ArrayList<String> makersname = new ArrayList<String>();
    ArrayList<String> makersnameid = new ArrayList<String>();

    ArrayList<String> maintypenamemodel = new ArrayList<String>();
    ArrayList<String> makersnamemodel = new ArrayList<String>();
    ArrayList<String> modelname = new ArrayList<String>();
    ArrayList<String> modelnameid = new ArrayList<String>();

    ArrayList<String> maintypenameyear = new ArrayList<String>();
    ArrayList<String> makersnameyear = new ArrayList<String>();
    ArrayList<String> modelnameyear = new ArrayList<String>();
    ArrayList<String> modelyear = new ArrayList<String>();

    ArrayList<String> typeIdList = new ArrayList<String>();
    ArrayList<String> makerIdList = new ArrayList<String>();
    ArrayList<String> modelIdList = new ArrayList<String>();


    private String selectedType, selectedMake, selectedModel, selectedYear;
    private Dialog dialog;
    private ServiceRequest mRequest_update;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private RegistrationInfoModel registrationInfoModel;


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
        setContentView(R.layout.activity_registration_vehicle_info);
        Init();


        headerBackRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CloseKeyBoard();
                overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);
            }
        });

        vehicleInfoNextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent nextIntent=new Intent(RegistrationLoginDetailsActivity.this,RegistrationDriverDetailsActivity.class);
                CloseKeyBoard();
                startActivity(nextIntent);
                overridePendingTransition(R.anim.reg_pull_in_left,R.anim.reg_push_out_right);*/
            }
        });
        vehicleAcRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println("****************acYesNo:" + checkedId);
            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position >= 0) {
                    try {
                        makeSpinnerList.clear();
                        modelSpinnerList.clear();
                        yearSpinnerList.clear();
                        makeAdapter.notifyDataSetChanged();
                        makeSpinner.setAdapter(makeAdapter);
                        selectedType = typeSpinnerList.get(position);
                        String selectedTypeId = typeIdList.get(position);
                        registrationInfoModel.setSelectedTypeId(selectedTypeId);
                        int typeNameMakerArraySize = registrationInfoModel.getMainTypeNameMakerArray().size();
                        int i = 0;
                        while (i < typeNameMakerArraySize) {
                            if (typeSpinnerList.get(position).equalsIgnoreCase(registrationInfoModel.getMainTypeNameMakerArray().get(i))) {
                                makeSpinnerList.add(registrationInfoModel.getMakerNameArray().get(i));
                                makerIdList.add(registrationInfoModel.getMakerIdArray().get(i));
                            }
                            i++;
                        }
                        makeAdapter.notifyDataSetChanged();
                        modelAdapter.notifyDataSetChanged();
                        yearAdapter.notifyDataSetChanged();

                        makeSpinner.setAdapter(makeAdapter);
                        modelSpinner.setAdapter(modelAdapter);
                        yearSpinner.setAdapter(yearAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position >= 0) {

                    try {
                        modelSpinnerList.clear();
                        yearSpinnerList.clear();
                        modelAdapter.notifyDataSetChanged();
                        modelSpinner.setAdapter(modelAdapter);
                        selectedMake = makeSpinnerList.get(position);
                        String selectedMakerId = makerIdList.get(position);
                        registrationInfoModel.setSelectedMakerId(selectedMakerId);
                        int makerNameModelArraySize = registrationInfoModel.getMakerNameModelArray().size();
                        int i = 0;
                        while (i < makerNameModelArraySize) {
                            if (selectedType.equalsIgnoreCase(registrationInfoModel.getMainTypeNameModelArray().get(i)) && makeSpinnerList.get(position).equalsIgnoreCase(registrationInfoModel.getMakerNameModelArray().get(i))) {
                                modelSpinnerList.add(registrationInfoModel.getModelNameArray().get(i));
                                modelIdList.add(registrationInfoModel.getModelIdArray().get(i));
                            }
                            i++;
                        }
                        modelAdapter.notifyDataSetChanged();
                        yearAdapter.notifyDataSetChanged();

                        modelSpinner.setAdapter(modelAdapter);
                        yearSpinner.setAdapter(yearAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position >= 0) {

                    try {
                        yearSpinnerList.clear();
                        yearAdapter.notifyDataSetChanged();
                        yearSpinner.setAdapter(yearAdapter);
                        selectedModel = modelSpinnerList.get(position);
                        String selectedModelId = modelIdList.get(position);
                        registrationInfoModel.setSelectedModelId(selectedModelId);
                        int modelNameYearArraySize = registrationInfoModel.getModelNameYearArray().size();
                        int i = 0;
                        while (i < modelNameYearArraySize) {
                            if (selectedType.equalsIgnoreCase(registrationInfoModel.getMainTypeNameYearArray().get(i)) && selectedMake.equalsIgnoreCase(registrationInfoModel.getMakerNameYearArray().get(i)) && modelSpinnerList.get(position).equalsIgnoreCase(registrationInfoModel.getModelNameYearArray().get(i))) {
                                yearSpinnerList.add(registrationInfoModel.getYearArray().get(i));
                            }
                            i++;
                        }
                        yearAdapter.notifyDataSetChanged();
                        yearSpinner.setAdapter(yearAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position >= 0) {
                    try {
                        registrationInfoModel.setSelectedYear(yearSpinnerList.get(position));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void Init() {
        headerView = findViewById(R.id.header_registration);
        headerBackRl = (RelativeLayout) headerView.findViewById(R.id.back_layout);
        headerCollapseTL = (CollapsingToolbarLayout) headerView.findViewById(R.id.collapsing_toolbar);
        headerCollapseTL.setTitleEnabled(true);
        headerCollapseTL.setCollapsedTitleTextColor(getResources().getColor(R.color.white_color));
        headerCollapseTL.setTitle(getResources().getString(R.string.vehicle_info_header_label));
        headerCollapseTL.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        headerCollapseTL.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);
        mainRl = (RelativeLayout) findViewById(R.id.main_layout);
        snackFlashBar = new SnackFlashBar(RegistrationVehicleInfoActivity.this);

        vehicleInfoNextTv = (TextView) findViewById(R.id.registration_vehicle_info_details_next_tv);
        vehicleAcRg = (RadioGroup) findViewById(R.id.ac_yes_no);

        typeSpinner = (MaterialSpinner) findViewById(R.id.country_spinner);
        makeSpinner = (MaterialSpinner) findViewById(R.id.make_spinner);
        modelSpinner = (MaterialSpinner) findViewById(R.id.model_spinner);
        yearSpinner = (MaterialSpinner) findViewById(R.id.year_spinner);

        registrationInfoModel = RegistrationLoginTypeActivity.registrationInfoModel;

        try {
            if (registrationInfoModel.getVehicleInfoLoaded()) {
                int typeCatArraySize = registrationInfoModel.getMainTypecateArray().size();
                int i = 0;
                while (i < typeCatArraySize) {
                    if (registrationInfoModel.getSelectedCateId().equalsIgnoreCase(registrationInfoModel.getMainTypecateArray().get(i))) {
                        typeSpinnerList.add(registrationInfoModel.getMainTypeNameCat().get(i));
                        typeIdList.add(registrationInfoModel.getMainTypeIdArray().get(i));
                    }
                    i++;
                }
            } else {
                cd = new ConnectionDetector(RegistrationVehicleInfoActivity.this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    PostRequestDriverRegVehicleInfoInit(ServiceConstant.Driver_registration_init2);
                } else {
                    snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.no_internet_connection), mainRl, "error");
                }
            }

            typeAdapter = new ArrayAdapter<String>(RegistrationVehicleInfoActivity.this, R.layout.spinner_item_registration, typeSpinnerList);
            typeAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_registration);
            typeSpinner.setAdapter(typeAdapter);

            makeAdapter = new ArrayAdapter<String>(RegistrationVehicleInfoActivity.this, R.layout.spinner_item_registration, makeSpinnerList);
            makeAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_registration);
            makeSpinner.setAdapter(makeAdapter);

            modelAdapter = new ArrayAdapter<String>(RegistrationVehicleInfoActivity.this, R.layout.spinner_item_registration, modelSpinnerList);
            modelAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_registration);
            modelSpinner.setAdapter(modelAdapter);

            yearAdapter = new ArrayAdapter<String>(RegistrationVehicleInfoActivity.this, R.layout.spinner_item_registration, yearSpinnerList);
            yearAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_registration);
            yearSpinner.setAdapter(yearAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //------------------------------------------------------PostRequestDriverRegInit

    private void PostRequestDriverRegVehicleInfoInit(String Url) {
        dialog = new Dialog(RegistrationVehicleInfoActivity.this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_loading_registration);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final TextView dialog_title = (TextView) dialog.findViewById(R.id.custom_loading_textview);
        dialog_title.setText(getResources().getString(R.string.action_loading));

        System.out.println("-----------------------------PostRequestDriverRegInit-------------------------" + Url);
        mRequest_update = new ServiceRequest(RegistrationVehicleInfoActivity.this);
        mRequest_update.makeServiceRequest(Url, Request.Method.POST, null, new ServiceRequest.ServiceListener() {

            @Override
            public void onCompleteListener(String response) {

                String status = "";
                System.out.println("-----------------------------PostRequestDriverRegInit------------------------------" + response);
                try {
                    JSONObject jobject = new JSONObject(response);

                    status = jobject.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseObject = jobject.getJSONObject("response");

                        Object isVehicleArray = responseObject.get("vehicles");
                        if (isVehicleArray instanceof JSONArray) {
                            JSONArray vehiclesarray = responseObject.getJSONArray("vehicles");
                            int vehicleArraySize = vehiclesarray.length();
                            int i = 0;
                            while (i < vehicleArraySize) {
                                JSONObject vehicleobj = vehiclesarray.getJSONObject(i);
                                String name = vehicleobj.getString("name");
                                String id = vehicleobj.getString("id");
                                maintypename.add(name);
                                maintypenameid.add(id);

                                Object isCatArray = vehicleobj.get("cat_id");
                                if (isCatArray instanceof JSONArray) {
                                    JSONArray maintypeCateidarray = vehicleobj.getJSONArray("cat_id");
                                    int cateidArraySize = maintypeCateidarray.length();
                                    int c = 0;
                                    while (c < cateidArraySize) {
                                        String typecatidstring = maintypeCateidarray.getString(c);
                                        maintypenamecatid.add(name);
                                        maintypecatid.add(typecatidstring);
                                        c++;
                                    }
                                } else {

                                }


                                Object isMakerArray = vehicleobj.get("makers");
                                if (isMakerArray instanceof JSONArray) {
                                    JSONArray makersarray = vehicleobj.getJSONArray("makers");
                                    int makerArraySize = makersarray.length();
                                    int j = 0;
                                    while (j < makerArraySize) {

                                        JSONObject makersobj = makersarray.getJSONObject(j);
                                        String makersnamestring = makersobj.getString("name");
                                        String makersid = makersobj.getString("id");

                                        maintypenamemakers.add(name);
                                        makersnameid.add(makersid);
                                        makersname.add(makersnamestring);

                                        Object isModelArray = makersobj.get("models");
                                        if (isModelArray instanceof JSONArray) {
                                            JSONArray modelarray = makersobj.getJSONArray("models");
                                            int modelArraySize = modelarray.length();
                                            int k = 0;
                                            while (k < modelArraySize) {

                                                JSONObject modelobj = modelarray.getJSONObject(k);
                                                String modelnamestring = modelobj.getString("name");
                                                String modelid = modelobj.getString("id");

                                                maintypenamemodel.add(name);
                                                makersnamemodel.add(makersnamestring);
                                                modelname.add(modelnamestring);
                                                modelnameid.add(modelid);
                                                Object isYearArray = modelobj.get("years");
                                                if (isYearArray instanceof JSONArray) {
                                                    JSONArray modelyeararray = modelobj.getJSONArray("years");
                                                    int modelyearArraySize = modelyeararray.length();
                                                    for (int l = 0; l < modelyearArraySize; l++) {

                                                        String modelyeartring = modelyeararray.getString(l);

                                                        maintypenameyear.add(name);
                                                        makersnameyear.add(makersnamestring);
                                                        modelnameyear.add(modelnamestring);
                                                        modelyear.add(modelyeartring);

                                                    }
                                                } else {

                                                }


                                                k++;
                                            }
                                        } else {

                                        }


                                        j++;
                                    }

                                } else {

                                }

                                i++;
                            }
                        } else {

                        }

                        registrationInfoModel.setLocationIdArray(maintypename, maintypecatid, maintypenameid, maintypenamemakers, makersname, makersnameid, maintypenamemodel, makersnamemodel, modelname, modelnameid, maintypenameyear, makersnameyear, modelnameyear, modelyear, maintypenamecatid);
                        for (int i = 0; i < registrationInfoModel.getMainTypecateArray().size(); i++) {
                            if (registrationInfoModel.getSelectedCateId().equalsIgnoreCase(registrationInfoModel.getMainTypecateArray().get(i))) {
                                typeSpinnerList.add(registrationInfoModel.getMainTypeNameCat().get(i));
                            }
                        }
                    } else {

                    }

                    System.out.println("*************************Init 2 details Asynctask:" + jobject);
                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onErrorListener() {
                dialog.dismiss();
            }

        });

    }


    private void CloseKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
