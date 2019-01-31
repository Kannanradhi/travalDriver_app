package com.cabily.cabilydriver;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.app.service.ServiceConstant;
import com.app.service.ServiceRequest;
import com.cabily.cabilydriver.Pojo.RegistrationCategoryPojo;
import com.cabily.cabilydriver.Pojo.RegistrationCountryPojo;
import com.cabily.cabilydriver.Pojo.RegistrationInfoModel;
import com.cabily.cabilydriver.Pojo.RegistrationLocationPojo;
import com.cabily.cabilydriver.Utils.ConnectionDetector;
import com.cabily.cabilydriver.Utils.SessionManager;
import com.cabily.cabilydriver.widgets.SnackFlashBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrationLoginTypeActivity extends AppCompatActivity {
    private View headerView;
    private RelativeLayout headerBackRl;
    private SnackFlashBar snackFlashBar;
    private RelativeLayout mainRl;
    private CollapsingToolbarLayout headerCollapseTL;

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


    private TextView locationTypeNextTv;
    private MaterialSpinner locationSpinner, categorySpinner;
    private ServiceRequest mRequest_update;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private Dialog dialog;
    private ArrayList<RegistrationCountryPojo> countryArrayList = new ArrayList<RegistrationCountryPojo>();
    private ArrayList<String> locationIdList = new ArrayList<String>();
    private ArrayList<String> locationCityList = new ArrayList<String>();
    ArrayList<RegistrationLocationPojo> locationCateArrayList = new ArrayList<>();
    ;
    ArrayList<RegistrationCategoryPojo> categoryArrayList = new ArrayList<>();
    ArrayList<String> catelist = new ArrayList<>();
    ArrayAdapter<String> cateAdapter, cityAdapter;
    public static RegistrationInfoModel registrationInfoModel;

    private JSONObject mInitJsonObject;
    InitDetailsBackgroundAsynctask initDetailsBackgroundAsynctask;
    private String userID = "", gcmID = "", language_code = "", Agent_Name = "";
    private String status = "";


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
    protected void onDestroy() {
        super.onDestroy();
        if (initDetailsBackgroundAsynctask != null) {
            initDetailsBackgroundAsynctask.cancel(true);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_login_type);
        Init();

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    try {
                        catelist.clear();
                        cateAdapter.notifyDataSetChanged();
                        categorySpinner.setAdapter(cateAdapter);
                        registrationInfoModel.setSelectedLocationId(locationCateArrayList.get(position).getLocationId());
                        for (int i = 0; i < categoryArrayList.size(); i++) {
                            if (locationCateArrayList.get(position).getLocationCity().equalsIgnoreCase(categoryArrayList.get(i).getCitySelectedName())) {
                                catelist.add(categoryArrayList.get(i).getCategory());
                            }

                        }
                        cateAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position >= 0) {
                    try {
                        for (int i = 0; i < categoryArrayList.size(); i++) {
                            if (catelist.get(position).equalsIgnoreCase(categoryArrayList.get(i).getCategory())) {
                                registrationInfoModel.setSelectedCateId(categoryArrayList.get(i).getCategoryId());
                                System.out.println("**********************selected cate id:" + registrationInfoModel.getSelectedCateId());
                            }

                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                try {
                    Intent nextIntent = new Intent(RegistrationLoginTypeActivity.this, RegistrationMobileVerificationActivity.class);
                    startActivity(nextIntent);
                    overridePendingTransition(R.anim.reg_pull_in_left, R.anim.reg_push_out_right);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void Init() {
        headerView = (View) findViewById(R.id.header_registration);
        headerBackRl = (RelativeLayout) headerView.findViewById(R.id.back_layout);
        snackFlashBar = new SnackFlashBar(RegistrationLoginTypeActivity.this);
        mainRl = (RelativeLayout) findViewById(R.id.main_layout);
        headerCollapseTL = (CollapsingToolbarLayout) headerView.findViewById(R.id.collapsing_toolbar);
        headerCollapseTL.setTitleEnabled(true);
        headerCollapseTL.setCollapsedTitleTextColor(getResources().getColor(R.color.white_color));
        headerCollapseTL.setTitle(getResources().getString(R.string.location_type_header_label));
        headerCollapseTL.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        headerCollapseTL.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);
//        headerCollapseTL.setCollapsedTitleGravity(View.FOCUS_LEFT);

        locationTypeNextTv = (TextView) findViewById(R.id.location_type_next_tv);
        locationSpinner = (MaterialSpinner) findViewById(R.id.location_spinner);
        categorySpinner = (MaterialSpinner) findViewById(R.id.category_spinner);
        registrationInfoModel = new RegistrationInfoModel();

        cityAdapter = new ArrayAdapter<String>(RegistrationLoginTypeActivity.this, R.layout.spinner_item_registration, locationCityList);
        cityAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_registration);
        locationSpinner.setAdapter(cityAdapter);

        cateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_registration, catelist);
        cateAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_registration);
        categorySpinner.setAdapter(cateAdapter);

        cd = new ConnectionDetector(RegistrationLoginTypeActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            PostRequestDriverRegInit(ServiceConstant.Driver_registration_init);
        } else {
            snackFlashBar.SnackBar(getResources().getString(R.string.app_name), getResources().getString(R.string.no_internet_connection), mainRl, "error");
        }

    }

    //------------------------------------------------------PostRequestDriverRegInit

    private void PostRequestDriverRegInit(String Url) {
        dialog = new Dialog(RegistrationLoginTypeActivity.this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_loading_registration);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final TextView dialog_title = (TextView) dialog.findViewById(R.id.custom_loading_textview);
        dialog_title.setText(getResources().getString(R.string.action_loading));

        System.out.println("-----------------------------PostRequestDriverRegInit-------------------------" + Url);
        mRequest_update = new ServiceRequest(RegistrationLoginTypeActivity.this);
        mRequest_update.makeServiceRequest(Url, Request.Method.POST, null, new ServiceRequest.ServiceListener() {

            @Override
            public void onCompleteListener(String response) {

                String mStatus = "";
                System.out.println("-----------------------------PostRequestDriverRegInit------------------------------" + response);
                try {
                    JSONObject jobject = new JSONObject(response);
                    mStatus = jobject.getString("status");
                    JSONObject responseObject = jobject.getJSONObject("response");
                    if (mStatus.equalsIgnoreCase("1")) {
                        Object isLocationArrayObject = responseObject.get("locations");
                        Object isCountryArrayObject = responseObject.get("countries");
                        if (isLocationArrayObject instanceof JSONArray) {
                            JSONArray locationArray = responseObject.getJSONArray("locations");
                            int locationArraySize = locationArray.length();
//                            locationIdList.clear();
                            locationCityList.clear();
                            categoryArrayList.clear();
                            locationCateArrayList.clear();
                            for (int i = 0; i < locationArraySize; i++) {
                                JSONObject location = locationArray.getJSONObject(i);
//                                locationIdList.add(location.getString("id"));
                                locationCityList.add(location.getString("city"));
                                String locationId = location.getString("id");
                                String locationCity = location.getString("city");
                                Object iscateArrayObject = location.get("category");
                                if (iscateArrayObject instanceof JSONArray) {
                                    JSONArray cateArray = location.getJSONArray("category");
                                    int cateArraySize = cateArray.length();
                                    for (int k = 0; k < cateArraySize; k++) {
                                        JSONObject cate = cateArray.getJSONObject(k);
                                        RegistrationCategoryPojo pojo = new RegistrationCategoryPojo();
                                        pojo.setCategoryId(cate.getString("id"));
                                        pojo.setCategory(cate.getString("category"));
                                        pojo.setCitySelectedName(locationCity);
                                        categoryArrayList.add(pojo);
                                    }
                                    RegistrationLocationPojo regPojo = new RegistrationLocationPojo(locationId, locationCity, categoryArrayList);
                                    locationCateArrayList.add(regPojo);
                                }

                            }
                          /*  registrationInfoModel.setLocationIdArray(locationIdList);
                            registrationInfoModel.setLocationCityArray(locationCityList);*/
//                            locationCityList = registrationInfoModel.getLocationCityArray();
                            cityAdapter.notifyDataSetChanged();
                            locationSpinner.setAdapter(cityAdapter);

                        }

                        if (isCountryArrayObject instanceof JSONArray) {
                            JSONArray countryArray = responseObject.getJSONArray("countries");
                            int countryArraySize = countryArray.length();
                            countryArrayList.clear();
                            for (int i = 0; i < countryArraySize; i++) {
                                JSONObject country = countryArray.getJSONObject(i);
                                RegistrationCountryPojo locationPojo = new RegistrationCountryPojo();
                                locationPojo.setCountryId(country.getString("id"));
                                locationPojo.setCountryName(country.getString("name"));
                                locationPojo.setCountryDialCode(country.getString("dial_code"));
                                countryArrayList.add(locationPojo);
                            }
                            registrationInfoModel.setCountryArray(countryArrayList);
                        }

                        initDetailsBackgroundAsynctask = new InitDetailsBackgroundAsynctask();
                        initDetailsBackgroundAsynctask.execute(ServiceConstant.Driver_registration_init2);

                    } else {

                    }

                    dialog.dismiss();
                } catch (Exception e) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onErrorListener() {
                dialog.dismiss();
            }

        });

    }


    @SuppressLint("StaticFieldLeak")
    private class InitDetailsBackgroundAsynctask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SessionManager sessionManager = new SessionManager(getApplicationContext());
            HashMap<String, String> language = sessionManager.getLanaguageCode();
            HashMap<String, String> user = sessionManager.getUserDetails();
            userID = user.get(SessionManager.KEY_DRIVERID);
            gcmID = user.get(SessionManager.KEY_GCM_ID);
            Agent_Name = user.get(SessionManager.KEY_ID_NAME);
            language_code = user.get(SessionManager.KEY_Language_code);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "", status = "";
            StringBuilder sb = new StringBuilder();
            try {
                InputStream in = null;
                try {
                    URL url = new URL(strings[0]);
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setRequestProperty("Authkey", Agent_Name);
                    urlConnection.setRequestProperty("isapplication", ServiceConstant.isapplication);
                    urlConnection.setRequestProperty("applanguage", language_code);
                    urlConnection.setRequestProperty("apptype", ServiceConstant.cabily_AppType);
                    urlConnection.setRequestProperty("driverid", userID);
                    urlConnection.setRequestProperty("apptoken", gcmID);


                    in = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader is = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(is);
                    String read = br.readLine();
                    while (read != null) {
                        sb.append(read);
                        read = br.readLine();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (result != null) {
                    JSONObject jobject = new JSONObject(result);

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
                                                    int l = 0;
                                                    while (l < modelyearArraySize) {

                                                        String modelyeartring = modelyeararray.getString(l);

                                                        maintypenameyear.add(name);
                                                        makersnameyear.add(makersnamestring);
                                                        modelnameyear.add(modelnamestring);
                                                        modelyear.add(modelyeartring);

                                                        l++;
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
                            registrationInfoModel.setVehicleInfoLoaded(true);
                        } else {
                            registrationInfoModel.setVehicleInfoLoaded(false);
                        }

                        registrationInfoModel.setLocationIdArray(maintypename, maintypecatid, maintypenameid, maintypenamemakers, makersname, makersnameid, maintypenamemodel, makersnamemodel, modelname, modelnameid, maintypenameyear, makersnameyear, modelnameyear, modelyear, maintypenamecatid);

                    } else {
                        registrationInfoModel.setVehicleInfoLoaded(false);
                    }

                    System.out.println("*************************Init 2 details Asynctask:" + jobject);
                } else {
                    registrationInfoModel.setVehicleInfoLoaded(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                registrationInfoModel.setVehicleInfoLoaded(false);
            }
        }
    }


    private void CloseKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<RegistrationCategoryPojo> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<RegistrationCategoryPojo> asr) {
            this.asr = asr;
            activity = context;
        }


//        @Override
//        public State getItem(int position) {
//            return stateList.get(position);
//        }

        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(RegistrationLoginTypeActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
            txt.setGravity(Gravity.LEFT);
            txt.setText(asr.get(position).getCategory());
//            txt.setTypeface(tf);
//            if(position == 0){
//                txt.setTextColor(Color.parseColor("#bdbdbd"));
//            }else {
            txt.setTextColor(Color.parseColor("#464646"));
//            }

            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(RegistrationLoginTypeActivity.this);
            txt.setGravity(Gravity.LEFT);
            txt.setPadding(0, 0, 0, 0);
            txt.setTextSize(16);
//            txt.setTypeface(tf);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            txt.setText(asr.get(i).getCategory());

//            if(i == 0){
//                txt.setTextColor(Color.parseColor("#bdbdbd"));
//            }else {
            txt.setTextColor(Color.parseColor("#464646"));
//            }


//            txt.setTextColor(Color.parseColor("#464646"));
            return txt;
        }

    }
}
