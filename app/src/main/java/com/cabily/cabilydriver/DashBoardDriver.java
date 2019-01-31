package com.cabily.cabilydriver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.latlnginterpolation.LatLngInterpolator;
import com.app.latlnginterpolation.MarkerAnimation;
import com.app.service.ServiceConstant;
import com.app.service.ServiceManager;
import com.app.service.ServiceRequest;
import com.app.xmpp.XmppService;
import com.cabily.cabilydriver.Helper.GEODBHelper;
import com.cabily.cabilydriver.Helper.GEOService;
import com.cabily.cabilydriver.Utils.ChatAvailabilityCheck;
import com.cabily.cabilydriver.Utils.ConnectionDetector;
import com.cabily.cabilydriver.Utils.CurrencySymbolConverter;
import com.cabily.cabilydriver.Utils.GPSTracker;
import com.cabily.cabilydriver.Utils.RoundedImageView;
import com.cabily.cabilydriver.Utils.SessionManager;
import com.cabily.cabilydriver.widgets.PkDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.romainpiel.shimmer.Shimmer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 */
public class DashBoardDriver extends Fragment implements View.OnClickListener, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static View parentView;
    private SessionManager session;
    private RoundedImageView user_img;
    private String driver_img = "", driver_name = "", vehicle_name = "", vehicle_no = "", car_image = "";
    private Dialog dialog;
    private StringRequest postrequest;
    private String driver_id = "";
    private ServiceRequest mRequest;
    private String Str_currencglobal = "";
    private Currency currency_code;
    private String sCurrencySymbol = "";
    private  String Str_mode_status="",Str_MultiCarStatus="",Str_driver_MultiCarStatus="";


    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private Button Bt_Go_Online;


    private ActionBar actionBar;

    private boolean isLastTripAvailable = false;
    private boolean isTodayEarningsAvailable = false;
    private boolean isTodayTipsAvailable = false;

    private Currency currencycode1;
    private Currency currencycode2;
    private Currency currencycode;

    GPSTracker gps;
    private GoogleMap googleMap;
    private RelativeLayout alert_layout;
    private TextView alert_textview;
    MarkerOptions markerOptions;
    private String Str_driver_status = "", go_Online_Status = "", go_Online_String = "";
    private boolean show_progress_status = false;
    private String Str_currency_code = "";
    String availability = "";
    private TextView Tv_driver_name, Tv_Driver_Vechile_no, Tv_Driver_vechile_model, Tv_car_category;

    private TextView Tv_lasttrip_ridetime, Tv_lasttrip_ridedate, Tv_lasttrip_earnings;
    private TextView Tv_today_earnings_onlinehours, Tv_todayearnigs_trips, Tv_todayearnings_earnings;
    private TextView Tv_todaytips_trips, Tv_todaytips_tips, tv_Rating;
    private Shimmer shimmer;
    private RoundedImageView Im_driver_img;
    private double strlat, strlon;
    private RatingBar driver_rating;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    Marker marker;
    public static Location myLocation;
    private ServiceRequest mRequest_update;
    private ToggleButton Tb_toggle ;
    private String Str_rideId="";
    private RelativeLayout Rl_layout_available_status, Rl_traffic, Rl_layout_verify_status;
    private LinearLayout layout_lastrip_main;
    TextView tv_driver_name, driver_vehicle_number1, tv_verify_driver;
    private Handler mapHandler = new Handler();
    private GEODBHelper myDBHelper;
    private float totalDistanceTravelled;

    int LAYOUT_FLAG;
    private SlidingUpPanelLayout mLayout;
    private static final String TAG = "DemoActivity";
    private ImageView refresh_button;
    private LinearLayout Rl_Down;
    private RelativeLayout Rl_map;
    private PkDialog mDialog;
    Marker drivermarker;
    private TextView Emty_Text,Tv_Go_onoff;

    //timer variable
    public static final long INTERVAL=30000;//variable to execute services every 10 second
    private Timer mTimer=null; // timer handling
    private Handler mHandler=new Handler();

    String base64;
    Bitmap bmp;
    private double MyCurrent_lat = 0.0, MyCurrent_long = 0.0;
    private LatLng newLatLng, oldLatLng;
    LatLngInterpolator mLatLngInterpolator;
    Location oldLocation;
    double myMovingDistance = 0.0;
    private Marker currentMarker;
    private float distance_to = 0;

    private RelativeLayout Rl_toggle;
    private ToggleButton Tb_toggle_multicat ;
    private TextView Tv_toggle_multicat;

    private Runnable mapRunnable = new Runnable() {
        @Override
        public void run() {
            gps = new GPSTracker(getActivity());
            if (gps != null && gps.canGetLocation()) {
                /*System.out.println("======map handler===========");
                postRequest(ServiceConstant.UPDATE_CURRENT_LOCATION);*/
                if (myLocation != null) {

                    if (mRequest_update != null) {
                        mRequest_update.cancelRequest();
                    }
                    if (Tb_toggle.isChecked()) {
                        PostRequest(ServiceConstant.UPDATE_CURRENT_LOCATION);
                    }
                }

            } else {
                enableGpsService();
            }
            mapHandler.postDelayed(this, 30000);
        }
    };
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (parentView != null) {
            ViewGroup parent = (ViewGroup) parentView.getParent();
            if (parent != null)
                parent.removeView(parentView);
        }
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN |
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


            parentView = inflater.inflate(R.layout.driver_dash_board_new, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        parentView.findViewById(R.id.ham_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationDrawerNew.openDrawer();
               /* if (resideMenu != null) {
                    resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                }*/
            }
        });
        try {
            String home = getActivity().getResources().getString(R.string.home);
            getActivity().setTitle("" + home);
        } catch (Exception e) {
        }
        //  setUpViews();
        initialize(parentView);
        initilizeMap();

        if (!isMyServiceRunning(XmppService.class)) {
            getActivity().startService(new Intent(getActivity(), XmppService.class));
        } else {
            getActivity().stopService(new Intent(getActivity(), XmppService.class));
            getActivity().startService(new Intent(getActivity(), XmppService.class));
        }
        refresh_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //feb 1
                //  ChatingService.startDriverAction(DriverMapActivity.this);
                setLocationRequest();
                buildGoogleApiClient();


                if (gps.canGetLocation() && gps.isgpsenabled()) {
                    double Dlatitude = gps.getLatitude();
                    double Dlongitude = gps.getLongitude();

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Dlatitude, Dlongitude)).zoom(17).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }

            }
        });

        Tb_toggle_multicat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("--------Toggle_bt.getText()-------------" + Tb_toggle_multicat.getText().toString());

                cd = new ConnectionDetector(getActivity());
                isInternetPresent = cd.isConnectingToInternet();
                if (Tb_toggle_multicat.getText().toString().equals("ON")) {
                    if (isInternetPresent) {
                        driverDashboard_Toggle_PostRequest(ServiceConstant.toggle_button, "ON");
                    } else {
                        Alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
                    }
                } else {
                    if (isInternetPresent) {
                        driverDashboard_Toggle_PostRequest(ServiceConstant.toggle_button, "OFF");
                    } else {
                        Alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
                    }
                }
            }
        });
        Bt_Go_Online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(getActivity());
                if (go_Online_Status.equalsIgnoreCase("1")) {

                    if (gps.isgpsenabled() && gps.canGetLocation()) {
                        cd = new ConnectionDetector(getActivity());
                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent) {
                            session.createSessionOnline("1");
                            session.createSessionOnline("1");
                            showDialog(getResources().getString(R.string.action_loading));
                            HashMap<String, String> jsonParams = new HashMap<String, String>();
                            HashMap<String, String> userDetails = session.getUserDetails();
                            HashMap<String, String> onlinedetails = session.getOnlineDetails();
                            String driverId = userDetails.get("driverid");
                            jsonParams.put("driver_id", "" + driverId);
                            jsonParams.put("availability", "" + "Yes");
                            System.out.println("availability-----" + "Yes");
                            System.out.println("driver_id-----" + driverId);
                            ServiceManager manager = new ServiceManager(getActivity(), updateAvailablityServiceListener);
                            manager.makeServiceRequest(ServiceConstant.UPDATE_AVAILABILITY, Request.Method.POST, jsonParams);
                            System.out.println("go_onlineurl-----" + ServiceConstant.UPDATE_AVAILABILITY);
                        } else {
                            Alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
                        }
                    } else {
                        System.out.println("enable gps");
                       // gps.showSettingsAlert();
                        enableGpsService();
                    }


                } else {

                    Alert(getResources().getString(R.string.alert_sorry_label_title), go_Online_String);

                }
            }
        });

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            Log.i(TAG, "onPanelSlide, offset " + slideOffset);
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            Log.i(TAG, "onPanelStateChanged " + newState);
        }
    });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    });
        Rl_layout_available_status.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!Str_rideId.equals("")) {
                Intent intent = new Intent(getActivity(), TripSummaryDetail.class);
                intent.putExtra("ride_id", Str_rideId);
                System.out.println("StrRideID---------" + Str_rideId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

        }
    });
       /* layout_lastrip_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TripSummaryDetail.class);
                intent.putExtra("ride_id", Str_rideId);
                System.out.println("StrRideID---------" + Str_rideId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });*/
        Rl_Down.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //   scrollView3.setEnabled(true);
            return false;
        }
    });

          Rl_map.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            System.out.println("==========venky");
            return false;
        }
    });

            Tb_toggle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            System.out.println("--------Toggle_bt.getText()-------------" + Tb_toggle.getText().toString());

            cd = new ConnectionDetector(getActivity());
            isInternetPresent = cd.isConnectingToInternet();
            if (Tb_toggle.isChecked()) {
                mDialog = new PkDialog(getActivity());
                mDialog.setDialogTitle(getResources().getString(R.string.alert));
                mDialog.setDialogMessage(getResources().getString(R.string.go_online_alert));
                mDialog.setPositiveButton(getResources().getString(R.string.alert_label_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isInternetPresent) {
                            gps = new GPSTracker(getActivity());
                            if(go_Online_Status.equalsIgnoreCase("1"))
                            {
                                Tb_toggle.setChecked(true);
                                //mapRunnable.run();
                                // StartTimer();
                                Tv_Go_onoff.setText(getResources().getString(R.string.goonline_label ));
                                if (gps.isgpsenabled() && gps.canGetLocation()) {
                                    cd = new ConnectionDetector(getActivity());
                                    isInternetPresent = cd.isConnectingToInternet();
                                    if (isInternetPresent) {
                                        session.createSessionOnline("1");
                                        session.createSessionOnline("1");
                                        //FEB 1
                        /*if (!ChatingService.isConnected) {
                            ChatingService.startDriverAction(getActivity());
                        }*/
                                        showDialog(getResources().getString(R.string.action_loading));
                                        HashMap<String, String> jsonParams = new HashMap<String, String>();
                                        HashMap<String, String> userDetails = session.getUserDetails();
                                        HashMap<String, String> onlinedetails = session.getOnlineDetails();
                                        String driverId = userDetails.get("driverid");
                                        jsonParams.put("driver_id", "" + driverId);
                                        jsonParams.put("availability", "" + "Yes");
                                        System.out.println("availability-----" + "Yes");
                                        System.out.println("driver_id-----" + driverId);
                                        ServiceManager manager = new ServiceManager(getActivity(), updateAvailablityServiceListener);
                                        manager.makeServiceRequest(ServiceConstant.UPDATE_AVAILABILITY, Request.Method.POST, jsonParams);
                                        System.out.println("go_onlineurl-----" + ServiceConstant.UPDATE_AVAILABILITY);
                                    } else {
                                        Alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
                                    }
                                } else {
                                    System.out.println("enable gps");
                                    gps.showSettingsAlert();
                                    enableGpsService();
                                }



                            }
                            else {

                                Alert(getResources().getString(R.string.alert_sorry_label_title), go_Online_String);

                            }
                            //  Toast.makeText(getActivity(),"Cool first Code",Toast.LENGTH_SHORT).show();
                            //  driverDashboard_Toggle_PostRequest(ServiceConstant.toggle_button;
                        } else {
                            Alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
                        }
                    }
                });
                mDialog.setNegativeButton(getResources().getString(R.string.action_cancel_alert), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tb_toggle.setChecked(false);
                        Tv_Go_onoff.setText(getResources().getString(R.string.go_offline ));
                        mDialog.dismiss();
                    }
                });
                mDialog.show();

            } else {
                if (isInternetPresent) {
                    final PkDialog mDialog = new PkDialog(getActivity());
                    mDialog.setDialogTitle(getResources().getString(R.string.alert));
                    mDialog.setDialogMessage(getResources().getString(R.string.dialog_go_offline));
                    mDialog.setPositiveButton(getResources().getString(R.string.alert_label_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goOffLine();

                            Tb_toggle.setChecked(false);
                            Tv_Go_onoff.setText(getResources().getString(R.string.go_offline ));
                            mDialog.dismiss();
                        }
                    });
                    mDialog.setNegativeButton(getResources().getString(R.string.action_cancel_alert), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Tb_toggle.setChecked(true);
                            Tv_Go_onoff.setText(getResources().getString(R.string.goonline_label ));
                            mDialog.dismiss();
                        }
                    });
                    mDialog.show();


                    // driverDashboard_Toggle_PostRequest(ServiceConstant.toggle_button);
                } else {
                    Alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
                }
            }
        }
    });
        setLocationRequest();
        buildGoogleApiClient();
        //addView();
        return parentView;
    }

    WindowManager windowManager;
    ImageView imageView;



    public static android.support.v4.app.FragmentActivity getCurrentActivty() throws Exception {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        HashMap activities = (HashMap) activitiesField.get(activityThread);
        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            if (!pausedField.getBoolean(activityRecord)) {
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                android.support.v4.app.FragmentActivity activity = (android.support.v4.app.FragmentActivity) activityField.get(activityRecord);
                return activity;
            }
        }
        return null;
    }




    public void showDialog(String message) {
        dialog = new Dialog(getActivity());
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void setLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void initialize(View rootview) {
        session = new SessionManager(getActivity());
        gps = new GPSTracker(getActivity());
        myDBHelper = new GEODBHelper(getActivity());
        String status = myDBHelper.retriveStatus();




        shimmer = new Shimmer();
        HashMap<String, String> user = session.getUserDetails();
        driver_id = user.get(SessionManager.KEY_DRIVERID);
        driver_img = user.get(SessionManager.KEY_DRIVER_IMAGE);
        driver_name = user.get(SessionManager.KEY_DRIVER_NAME);
        vehicle_no = user.get(SessionManager.KEY_VEHICLENO);
        vehicle_name = user.get(SessionManager.KEY_VEHICLE_MODEL);
        Tv_lasttrip_ridetime = (TextView) rootview.findViewById(R.id.dashboard_ride_time);
        tv_Rating = (TextView) rootview.findViewById(R.id.rating_label);
        Tv_lasttrip_ridedate = (TextView) rootview.findViewById(R.id.dashboard_last_trip_ride_date);
        Tv_lasttrip_earnings = (TextView) rootview.findViewById(R.id.netAmount_price_last_trips);
        Tv_today_earnings_onlinehours = (TextView) rootview.findViewById(R.id.dashboard_today_earnings_onlinetime);
        Tv_todayearnigs_trips = (TextView) rootview.findViewById(R.id.dashboard_today_earnings_trips);
        Tv_todayearnings_earnings = (TextView) rootview.findViewById(R.id.netAmount_price_today_earnings);
        Tv_todaytips_trips = (TextView) rootview.findViewById(R.id.dashboard_todays_trips);
        Tv_todaytips_tips = (TextView) rootview.findViewById(R.id.netAmount_price_today_tips);
        Bt_Go_Online = (Button) rootview.findViewById(R.id.Bt_gonlinebutton);
        mLayout = (SlidingUpPanelLayout) rootview.findViewById(R.id.sliding_layout);
        refresh_button = (ImageButton) rootview.findViewById(R.id.refresh);
        Tv_driver_name = (TextView) rootview.findViewById(R.id.home_user_name);
        Tv_Driver_vechile_model = (TextView) rootview.findViewById(R.id.home_car_name);
        user_img = (RoundedImageView) rootview.findViewById(R.id.dasboard_driverimg);
        Tv_Driver_Vechile_no = (TextView) rootview.findViewById(R.id.home_car_no);
        Tv_car_category = (TextView) rootview.findViewById(R.id.home_car_category);
        driver_rating = (RatingBar) rootview.findViewById(R.id.driver_dashboard_ratting);
        Tb_toggle = (ToggleButton)rootview.findViewById(R.id.driver_dashboard_toggle_button_onoff);
        //mLayout = (SlidingUpPanelLayout) rootview.findViewById(R.id.sliding_layout);
        Tv_Go_onoff = (TextView)rootview.findViewById(R.id.go_on_off_text);
        // scrollView3 = (ScrollView)rootview.findViewById(R.id.scrollView3);
        Rl_Down = (LinearLayout)rootview.findViewById(R.id.layout_viwes_dashboard);
        Rl_map = (RelativeLayout)rootview.findViewById(R.id.map_layout);

        Rl_layout_available_status = (RelativeLayout) rootview.findViewById(R.id.layout_available_status);
        Rl_layout_verify_status = (RelativeLayout) rootview.findViewById(R.id.layout_verify_status);
        layout_lastrip_main = (LinearLayout)rootview.findViewById(R.id.layout_lastrip_main);
        tv_verify_driver = (TextView) rootview.findViewById(R.id.map_verify_status);
        Picasso.with(getActivity()).load(driver_img).placeholder(R.drawable.nouserimg).into(user_img);

        Rl_toggle = (RelativeLayout)rootview.findViewById(R.id.driver_dashboard_toogle_layout);
        Tb_toggle_multicat = (ToggleButton)rootview.findViewById(R.id.driver_dashboard_toggle_button);
        Tv_toggle_multicat = (TextView)rootview.findViewById(R.id.driver_dashboard_toggle_textview);


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();

        session.setXmppServiceState("");


    }

    @Override
    public void onPause() {
        super.onPause();
        System.gc();
    }

    private void initilizeMap() {


        MapFragment mapFragment = ((MapFragment) getActivity().getFragmentManager().findFragmentById(
                R.id.driver_dashboradsmain_map));
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap arg) {
                loadMap(arg);
            }
        });


    }

    public void loadMap(GoogleMap arg) {
        googleMap = arg;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(false);
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getActivity(), R.raw.mapstyle));
    /*    googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(false);*/

        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);

        if (gps != null && gps.canGetLocation()) {
            double Dlatitude = gps.getLatitude();
            double Dlongitude = gps.getLongitude();
            MyCurrent_lat = Dlatitude;
            MyCurrent_long = Dlongitude;
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Dlatitude, Dlongitude)).zoom(17).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } else {
            if (alert_layout != null && alert_textview != null) {
                alert_layout.setVisibility(View.VISIBLE);
                alert_textview.setText(getResources().getString(R.string.alert_gpsEnable));
            }
        }
        markerOptions = new MarkerOptions();

        if (googleMap != null) {
            googleMap.clear();
        }
        if (isInternetPresent) {
            driverdashboard_PostRequest(ServiceConstant.driver_dashboard);
            System.out.println("driverdashboardurl------------" + ServiceConstant.driver_dashboard);
        } else {
            Alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
        }


    }


    public static boolean isOnline = false;

    private ServiceManager.ServiceListener updateAvailablityServiceListener = new ServiceManager.ServiceListener() {
        @Override
        public void onCompleteListener(Object object) {
            try {
                dismissDialog();
                String response = (String) object;

                JSONObject object1 = new JSONObject(response);
                if (object1.length() > 0) {
                    String status = object1.getString("status");
                    if (status.equalsIgnoreCase("1")) {

                        mDialog.dismiss();

                        if (!isMyServiceRunning(XmppService.class)) {
                            getActivity().startService(new Intent(getActivity(), XmppService.class));
                        }

                        session.setXmppServiceState("online");
                        isOnline = true;
                        Tb_toggle.setChecked(true);
                        StartTimer();
                        String Str_driver_name = object1.getString("driver_name");
                        String Str_vechile_no = object1.getString("vehicle_number");

                        Tv_driver_name.setText(Str_driver_name);
                        Tv_Driver_Vechile_no.setText(Str_vechile_no);

                        session.setdriverNameUpdate(Str_driver_name);
                        session.setVechileNumberUpdate(Str_vechile_no);
                        availability = availability;
                        DriverMapclass();

                       /* isOnline = true;
                        Intent i = new Intent(getActivity(), DriverMapActivity.class);
                        i.putExtra("availability", availability);
                        startActivity(i);*/
                    }
                }


                System.out.println("onlineresponse-------" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onErrorListener(Object obj) {
            dismissDialog();
        }
    };


    public void dismissDialog() {
        if (dialog != null)
            dialog.dismiss();
    }


    //Enabling Gps Service
    private void enableGpsService() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });
    }

    private void Alert(String title, String message) {
        final PkDialog mDialog = new PkDialog(getActivity());
        mDialog.setDialogTitle(title);
        mDialog.setDialogMessage(message);
        mDialog.setPositiveButton(getResources().getString(R.string.alert_label_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


    private void driverdashboard_PostRequest(String Url) {
        dialog = new Dialog(getActivity());
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        TextView dialog_title = (TextView) dialog.findViewById(R.id.custom_loading_textview);
        dialog_title.setText(getResources().getString(R.string.action_loading));

        System.out.println("-------------dashboard----------------" + Url);

        HashMap<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("driver_id", driver_id);
        jsonParams.put("driver_lat", String.valueOf(MyCurrent_lat));
        jsonParams.put("driver_lon", String.valueOf(MyCurrent_long));
        System.out.println("-------------dashboard--jsonParams--------------" + jsonParams);
        mRequest = new ServiceRequest(getActivity());
        mRequest.makeServiceRequest(Url, Request.Method.POST, jsonParams, new ServiceRequest.ServiceListener() {

            @Override
            public void onCompleteListener(String response) {

                Log.e("dashboards", response);
                System.out.println("dashboards---------" + response);
                String Str_status = "", Str_response = "", Str_driver_name = "", Str_driver_img = "", Str_driver_category = "", Str_vechile_no = "", Str_vechile_model = "", Str_driver_id = "",
                        Str_driver_lattitude = "", Str_driver_longitude = "", Str_driver_ratting = "", Str_lasttrip_ridetime = "", Str_lasttrip_ridedate = "",
                        Str_lasttrip_earnings = "", Str_lasttrip_currencycode = "", Str_todayearnings_onlinehours = "", Str_todayearnings_trips = "",
                        Str_todayearnings_earnings = "", Str_todayearnings_currencycode = "", Str_todaytips_trips = "", Str_todaytips_tips = "", Str_todaytips_currencycode = "";

                try {

                    JSONObject jobject = new JSONObject(response);

                    Str_status = jobject.getString("status");

                    if (Str_status.equalsIgnoreCase("1")) {
                        JSONObject object = jobject.getJSONObject("response");

                        Str_currencglobal = object.getString("currency");
                        //    currency_code = Currency.getInstance(getLocale(Str_currencglobal));

                        Str_driver_id = object.getString("driver_id");
                        Str_driver_name = object.getString("driver_name");
                        Str_driver_status = object.getString("driver_status");
                        availability = object.getString("availability_string");
                        Str_vechile_no = object.getString("vehicle_number");
                        Str_vechile_model = object.getString("vehicle_model");
                        Str_driver_img = object.getString("driver_image");
                        Str_driver_ratting = object.getString("driver_review");
                        Str_driver_category = object.getString("driver_category");
                        driver_img = object.getString("driver_image");
                        if (object.has("multi_car_status")) {
                            Str_MultiCarStatus = object.getString("multi_car_status");
                        }
                        if (object.has("driver_multi_car_status")) {
                            Str_driver_MultiCarStatus = object.getString("driver_multi_car_status");
                        }
                            if (object.has("awaiting_request_category")) {
                                Str_mode_status = object.getString("awaiting_request_category");
                            }
                        Str_driver_lattitude = object.getString("driver_lat");
                        Str_driver_longitude = object.getString("driver_lon");
                        strlat = Double.parseDouble(Str_driver_lattitude);
                        strlon = Double.parseDouble(Str_driver_longitude);
                        car_image = object.getString("category_icon");
                        go_Online_Status = object.getString("go_online_status");
                        go_Online_String = object.getString("go_online_string");
                        Object check_last_trip_object = object.get("last_trip");
                        if (check_last_trip_object instanceof JSONObject) {

                            JSONObject jobject1 = object.getJSONObject("last_trip");
                            if (jobject1.length() > 0) {
                                Str_lasttrip_ridetime = jobject1.getString("ride_time");
                                Str_lasttrip_ridedate = jobject1.getString("ride_date");
                                Str_lasttrip_currencycode = jobject1.getString("currency");
                                // currencycode = Currency.getInstance(getLocale(Str_lasttrip_currencycode));

                                sCurrencySymbol = CurrencySymbolConverter.getCurrencySymbol(Str_lasttrip_currencycode);

                                Str_lasttrip_earnings = sCurrencySymbol + jobject1.getString("earnings");
                                isLastTripAvailable = true;

                                System.out.println("ridetim------" + Str_lasttrip_ridetime);
                                System.out.println("ridedate------" + Str_lasttrip_ridedate);
                                System.out.println("amount------" + Str_lasttrip_earnings);

                            } else {
                                isLastTripAvailable = false;
                            }
                        } else {
                            isLastTripAvailable = false;
                        }

                        Object check_today_earnings_object = object.get("today_earnings");
                        if (check_today_earnings_object instanceof JSONObject) {
                            JSONObject jobject2 = object.getJSONObject("today_earnings");
                            if (jobject2.length() > 0) {
                                Str_todayearnings_onlinehours = jobject2.getString("online_hours");
                                Str_todayearnings_trips = jobject2.getString("trips");
                                Str_todayearnings_currencycode = jobject2.getString("currency");

                                // currencycode1= Currency.getInstance(getLocale(Str_todayearnings_currencycode));

                                sCurrencySymbol = CurrencySymbolConverter.getCurrencySymbol(Str_todayearnings_currencycode);

                                Str_todayearnings_earnings = sCurrencySymbol + jobject2.getString("earnings");
                                isTodayEarningsAvailable = true;
                            } else {
                                isTodayEarningsAvailable = false;
                            }
                        } else {
                            isTodayEarningsAvailable = false;
                        }

                        Object check_today_tips_object = object.get("today_tips");
                        if (check_today_tips_object instanceof JSONObject) {
                            JSONObject jobject3 = object.getJSONObject("today_tips");
                            if (jobject3.length() > 0) {
                                Str_todaytips_trips = jobject3.getString("trips");
                                Str_todaytips_currencycode = jobject3.getString("currency");
                                //currencycode2 = Currency.getInstance(getLocale(Str_todaytips_currencycode));

                                sCurrencySymbol = CurrencySymbolConverter.getCurrencySymbol(Str_todaytips_currencycode);

                                Str_todaytips_tips = sCurrencySymbol + jobject3.getString("tips");
                                isTodayTipsAvailable = true;
                            } else {
                                isTodayTipsAvailable = false;
                            }
                        } else {
                            isTodayTipsAvailable = false;
                        }

                    } else {
                        Str_response = jobject.getString("response");
                    }

                    if (Str_status.equalsIgnoreCase("1")) {
                        Tv_Driver_vechile_model.setText(Str_vechile_model);
                        Tv_driver_name.setText(Str_driver_name);
                        Tv_Driver_Vechile_no.setText(Str_vechile_no);
                        driver_rating.setRating(Float.parseFloat(Str_driver_ratting));
                        Tv_car_category.setText(Str_driver_category);
                        tv_Rating.setText("(" + Str_driver_ratting + "/5)");
                        session.createServiceStatus("");
                        session.set_driverdetails(Str_driver_category,Str_vechile_no,Str_driver_ratting);
                        session.setdriver_image(driver_img);
                        session.setdriverNameUpdate(Str_driver_name);
                        session.setVechileNumberUpdate(Str_vechile_no);
                        session.setVechileModelUpdate(Str_vechile_model);
                        Picasso.with(getActivity()).load(driver_img).placeholder(R.drawable.nouserimg).into(user_img);
                        NavigationDrawerNew.navigationNotifyChange();

                        if (Str_MultiCarStatus.equalsIgnoreCase("ON")) {
                            Rl_toggle.setVisibility(View.VISIBLE);

                            if (Str_driver_MultiCarStatus.equalsIgnoreCase("ON")) {
                                Tb_toggle_multicat.setChecked(true);
                                Tv_toggle_multicat.setText(Str_mode_status);
                            } else {
                                Tb_toggle_multicat.setChecked(false);
                                Tv_toggle_multicat.setText(Str_mode_status);
                            }

                        } else {
                            Rl_toggle.setVisibility(View.GONE);
                        }
                        Tv_toggle_multicat.setText("Mini, Seedan, Suv");
                        Rl_toggle.setVisibility(View.VISIBLE);

                        if (isLastTripAvailable == true) {
                            Tv_lasttrip_ridetime.setText(Str_lasttrip_ridetime);
                            Tv_lasttrip_ridedate.setText(Str_lasttrip_ridedate);
                            Tv_lasttrip_earnings.setText(Str_lasttrip_earnings);
                        } else {
                            Tv_lasttrip_ridetime.setText(getResources().getString(R.string.lasttrip_emtpy_label1));
                            Tv_lasttrip_ridedate.setText(getResources().getString(R.string.lasttrip_emtpy_label));
                            Tv_lasttrip_earnings.setText(sCurrencySymbol + "0.00");
                        }

                        if (isTodayEarningsAvailable == true) {
                            Tv_today_earnings_onlinehours.setText(Str_todayearnings_onlinehours);
                            Tv_todayearnigs_trips.setText(Str_todayearnings_trips + " " + getResources().getString(R.string.driver_dash_board_estimatednet_trips));
                            Tv_todayearnings_earnings.setText(Str_todayearnings_earnings);
                        } else {
                            Tv_today_earnings_onlinehours.setText(getResources().getString(R.string.todayearning_no_online_label));
                            Tv_todayearnigs_trips.setText(getResources().getString(R.string.todayearning_no_trips_label));
                            Tv_todayearnings_earnings.setText(sCurrencySymbol + "0.00");
                        }

                        if (isTodayTipsAvailable == true) {
                            Tv_todaytips_tips.setText(Str_todaytips_tips);
                            Tv_todaytips_trips.setText(Str_todaytips_trips + " " + getResources().getString(R.string.driver_dash_board_estimatednet_trips));
                        } else {
                            Tv_todaytips_tips.setText(sCurrencySymbol + "0.00");
                            Tv_todaytips_trips.setText((getResources().getString(R.string.todayearning_no_tips_label)));
                        }

                        //---------------map marker--------------


                        if ("yes".equalsIgnoreCase(Str_driver_status)) {
                            /*if(go_Online_Status.equalsIgnoreCase("1"))
                            {*/

                            cd = new ConnectionDetector(getActivity());
                            isInternetPresent = cd.isConnectingToInternet();
                            if (isInternetPresent) {
                                session.createSessionOnline("1");

                                if (!isMyServiceRunning(XmppService.class)) {
                                    getActivity().startService(new Intent(getActivity(), XmppService.class));
                                }

                                session.setXmppServiceState("online");

                                isOnline = true;
                                Tb_toggle.setChecked(true);
                                Tv_Go_onoff.setText(getResources().getString(R.string.goonline_label ));
                                StartTimer();
                                availability = availability;
                                DriverMapclass();

                             /*   Intent i = new Intent(getActivity(), DriverMapActivity.class);
                                i.putExtra("availability", availability);
                                startActivity(i);*/

                                /*ServiceManager manager = new ServiceManager(getActivity(), updateAvailablityServiceListener);
                                manager.makeServiceRequest(ServiceConstant.UPDATE_AVAILABILITY, Request.Method.POST, jsonParams);
                                System.out.println("go_onlineurl-----" + ServiceConstant.UPDATE_AVAILABILITY);*/
                            } else {
                                Alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
                            }

                            /*}
                            /*else {
                                Alert(getResources().getString(R.string.alert_sorry_label_title), go_Online_String);
                            }*/

                        }else {
                            Tv_Go_onoff.setText(getResources().getString(R.string.go_offline ));
                        }


                        new CarImageSetCar().execute();
                           /* URL url = new URL(car_image);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap b = BitmapFactory.decodeStream(input);
                            bmp = b;
                            String s = BitMapToString(b);
                            System.out.println("bytearray" + s);
                            session.setVehicle_BitmapImage(s);*/

                        /*   if (marker != null) {
                            marker.remove();
                        }
                        marker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng((strlat), (strlon)))
                                .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                        // Move the camera to last position with a zoom level
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng((strlat), (strlon))).zoom(17).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        session.setVehicleImage(car_image);*/


                        if (session.isAds()) {
                            HashMap<String, String> ads = session.getAds();
                            String title = ads.get(SessionManager.KEY_AD_TITLE);
                            String msg = ads.get(SessionManager.KEY_AD_MSG);
                            String banner = ads.get(SessionManager.KEY_AD_BANNER);
                            System.out.println("--------jai----ads-title-----------" + title);
                            System.out.println("--------jai----ads-msg-----------" + msg);
                            System.out.println("--------jai----ads-banner-----------" + banner);

                            Intent i1 = new Intent(getActivity(), AdsPage.class);
                            i1.putExtra("AdsTitle", title);
                            i1.putExtra("AdsMessage", msg);
                            i1.putExtra("AdsBanner", banner);
                            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);


                        }

                    } else {
                        Alert(getResources().getString(R.string.alert_sorry_label_title), Str_response);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }

            @Override
            public void onErrorListener() {
                dialog.dismiss();
            }
        });


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        boolean b = false;
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                System.out.println("1 already running");
                b = true;
                break;
            } else {
                System.out.println("2 not running");
                b = false;
            }
        }
        System.out.println("3 not running");
        return b;
    }



    @Override
    public void onConnected(Bundle bundle) {
        try {
            if (gps != null && gps.canGetLocation() && gps.isgpsenabled()) {
            }
            myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            if (myLocation != null) {
               /* if (drivermarker != null) {
                    drivermarker.remove();
                }*/

                if (googleMap != null) {
                    HashMap<String, String> bitmap = session.getBitmapCode();
                    base64 = bitmap.get(SessionManager.KEY_VEHICLE_BitMap_IMAGE);
                    bmp = StringToBitMap(base64);
                    /*if (bmp != null) {
                        drivermarker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()),
                                17));
                    }*/
                }

                //MarkerOptions marker = new MarkerOptions();
                //marker.position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
                //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_car));
                // if(currentMarker != null){
                //    currentMarker.remove();
                // }
                // currentMarker = googleMap.addMarker(marker);
                if (Tb_toggle.isChecked()) {
                    PostRequest(ServiceConstant.UPDATE_CURRENT_LOCATION);
                    System.out.println("online------------------" + ServiceConstant.UPDATE_CURRENT_LOCATION);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (this.myLocation != null) {
            distance_to = location.distanceTo(myLocation);
            System.out.println("---------distance to-----------" + location.distanceTo(myLocation));
        }
        this.myLocation = location;


        if (myLocation != null) {
            try {

                MyCurrent_lat = myLocation.getLatitude();
                MyCurrent_long = myLocation.getLongitude();

                LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                if (oldLatLng == null) {
                    System.out.println("----------inside oldLatLngnull--------");
                    oldLatLng = latLng;
                }
                newLatLng = latLng;
                if (mLatLngInterpolator == null) {
                    mLatLngInterpolator = new LatLngInterpolator.Linear();
                }
                oldLocation = new Location("");
                oldLocation.setLatitude(oldLatLng.latitude);
                oldLocation.setLongitude(oldLatLng.longitude);

                float bearingValue = oldLocation.bearingTo(location);
                myMovingDistance = oldLocation.distanceTo(location);

                System.out.println("movingdistacn------------" + myMovingDistance);

                if (myMovingDistance > 2) {

                    if (currentMarker != null) {
                        currentMarker.remove();
                    }

                    if (googleMap != null) {
                        if (bmp != null) {

                            if (drivermarker != null) {
                                System.out.println("---------inside new bearing value drivermarker != null-------------++" + bearingValue);

                                if (!String.valueOf(bearingValue).equalsIgnoreCase("NaN")) {
                                    if (location.getAccuracy() < 100.0 && location.getSpeed() < 6.95) {
                                        //drivermarker.setRotation(bearingValue);
                                        rotateMarker(drivermarker, bearingValue, googleMap);
                                        MarkerAnimation.animateMarkerToGB(drivermarker, latLng, mLatLngInterpolator);

                                        float zoom = googleMap.getCameraPosition().zoom;
                                        // System.out.println("===========isMapTouched=========" + isMapToched);
                                        //   if (!isMapToched) {
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoom).build();
                                        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                        googleMap.moveCamera(camUpdate);
                                        //   }
//new change
                                    }
                                }
                            } else {
                                if (currentMarker != null) {
                                    currentMarker.remove();
                                }
                                drivermarker = googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                                        .anchor(0.5f, 0.5f)
                                        .rotation(myLocation.getBearing())
                                        .flat(true));
                            }


                        }
                    }
                }
                oldLatLng = newLatLng;
                //      sendLocationToUser(myLocation);
            } catch (Exception e) {
            }
        }
    }
    static public void rotateMarker(final Marker marker, final float toRotation, GoogleMap map) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = marker.getRotation();
        final long duration = 1555;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                marker.setRotation(-rot > 180 ? rot / 2 : rot);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    //method to convert currency code to currency symbol
    private static Locale getLocale(String strCode) {

        for (Locale locale : NumberFormat.getAvailableLocales()) {
            String code = NumberFormat.getCurrencyInstance(locale).getCurrency().getCurrencyCode();
            if (strCode.equals(code)) {
                return locale;
            }
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        System.out.println("enable gps1");
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        System.out.println("enable gps2");
                        enableGpsService();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void PostRequest(String Url) {

        HashMap<String, String> jsonParams = new HashMap<String, String>();
        HashMap<String, String> userDetails = session.getUserDetails();
        String driverId = userDetails.get("driverid");
        System.out.println("driverId-------------" + driverId);
        System.out.println("latitude-------------" + myLocation.getLatitude());
        System.out.println("longitude-------------" + myLocation.getLongitude());
        System.out.println("**********venki******driverMapactivity****URL*******" + Url);
        jsonParams.put("driver_id", "" + driverId);
        jsonParams.put("latitude", "" + myLocation.getLatitude());
        jsonParams.put("longitude", "" + myLocation.getLongitude());
        System.out.println("**********venki******driverMapactivity****jsonParams*******" + jsonParams);

        mRequest_update = new ServiceRequest(getActivity());
        mRequest_update.makeServiceRequest(Url, Request.Method.POST, jsonParams, new ServiceRequest.ServiceListener() {

            @Override
            public void onCompleteListener(String response) {
                System.out.println("**********venki******driverMapactivity****response*******" + response);
                String Str_status = "",
                        Str_availablestaus = "",
                        Str_message = "";

                try {
                    JSONObject jobject = new JSONObject(response);

                    Str_status = jobject.getString("status");
                    if ("1".equalsIgnoreCase(Str_status)) {
                        JSONObject jobject2 = jobject.getJSONObject("response");
                        Str_availablestaus = jobject2.getString("availability");
                        Str_message = jobject2.getString("message");
                        Str_rideId = jobject2.getString("ride_id");
                        System.out.println("rideIDDresponse----------" + Str_rideId);
                        System.out.println("online----------" + response);
                  /*      if (Str_availablestaus.equalsIgnoreCase("Unavailable")) {
                            Rl_layout_available_status.setVisibility(View.VISIBLE);
                            Tb_toggle.setVisibility(View.INVISIBLE);

                        } else {
                            Rl_layout_available_status.setVisibility(View.GONE);
                            Tb_toggle.setVisibility(View.VISIBLE);


                        }*/
                        if (Str_availablestaus.equalsIgnoreCase("Unavailable")) {
                            Rl_layout_available_status.setVisibility(View.INVISIBLE);
                            Tb_toggle.setVisibility(View.INVISIBLE);


//                            if (session.getTripStatus().equalsIgnoreCase("1")) {
                            if (!NavigationDrawerNew.sPushType) {
                                Intent trip_intent = new Intent(getActivity(), TripPage.class);
                                trip_intent.putExtra("interrupted", "Yes");
                                startActivity(trip_intent);
                            }
//                            }
                            //   finish();

                        } else {
                            Rl_layout_available_status.setVisibility(View.GONE);
                            Tb_toggle.setVisibility(View.VISIBLE);
                        }
                        showVerifyStatus(jobject2);

                        if ("No".equalsIgnoreCase(jobject2.getString("verify_status")) || Str_availablestaus.equalsIgnoreCase("Unavailable")) {
                            Rl_traffic.setVisibility(View.INVISIBLE);
                        } else {
                            Rl_traffic.setVisibility(View.VISIBLE);
                        }

                        if (session.isAds()) {
                            HashMap<String, String> ads = session.getAds();
                            String title = ads.get(SessionManager.KEY_AD_TITLE);
                            String msg = ads.get(SessionManager.KEY_AD_MSG);
                            String banner = ads.get(SessionManager.KEY_AD_BANNER);
                            System.out.println("--------jai----ads-title-----------" + title);
                            System.out.println("--------jai----ads-msg-----------" + msg);
                            System.out.println("--------jai----ads-banner-----------" + banner);

                            Intent i1 = new Intent(getActivity(), AdsPage.class);
                            i1.putExtra("AdsTitle", title);
                            i1.putExtra("AdsMessage", msg);
                            i1.putExtra("AdsBanner", banner);
                            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);


                        }


                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onErrorListener() {
            }

        });

    }

    private void showVerifyStatus(JSONObject object) {
        try {
            String verify_status = object.getString("verify_status");
            if ("No".equalsIgnoreCase(verify_status)) {
                String verify_string = object.getString("errorMsg");
                Rl_traffic.setVisibility(View.INVISIBLE);
                Rl_layout_verify_status.setVisibility(View.VISIBLE);
                tv_verify_driver.setText(verify_string);
                //  findViewById(R.id.layout_verify_status).setVisibility(View.VISIBLE);
            } else {
                Rl_traffic.setVisibility(View.VISIBLE);
                Rl_layout_verify_status.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            Rl_layout_verify_status.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }
    @Override
    public void onStop() {
        mapHandler.removeCallbacks(mapRunnable);

        super.onStop();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        mapHandler.removeCallbacks(mapRunnable);
        StopTimer();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        startLocationUpdates();

        super.onResume();
        if (Tb_toggle.isChecked()) {
            // mapRunnable.run();
            ///    StartTimer();
            if (myLocation == null) {
                myLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            }
            ChatAvailabilityCheck chatAvailability = new ChatAvailabilityCheck(getActivity(), "available");
            chatAvailability.postChatRequest();
        }

    }
    private void StartTimer() {
        if(mTimer!=null) {
            mTimer.cancel();
        }
        else {
            mTimer = new Timer();
        }// recreate new timer
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(),0,INTERVAL);//
    }
    private void StopTimer(){
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    public void goOffLine() {

        DashBoardDriver.isOnline = false;

        showDialog("");

        ArrayList<LatLng> distance_travelled = myDBHelper.getDataDistance("1");

        calculateDistance(distance_travelled);


        System.out.println("-----------jai---total_distance-------------------" + distance_travelled.toString().replace("[", "").replace("]", "").replace(" ", ""));

        myDBHelper.Delete("");


        HashMap<String, String> jsonParams = new HashMap<String, String>();

        HashMap<String, String> userDetails = session.getUserDetails();

        String driverId = userDetails.get("driverid");

        jsonParams.put("driver_id", "" + driverId);

        jsonParams.put("availability", "" + "No");

        /*jsonParams.put("distance", "20");*/

        jsonParams.put("distance", String.valueOf(totalDistanceTravelled / 1000));

        System.out.println("availability-------------" + "No");

        System.out.println("offline driver_id-------------" + driverId);

        System.out.println("og offline-----------jsonParams-------------" + jsonParams);

        System.out.println("og offline-----------url-------------" + ServiceConstant.UPDATE_AVAILABILITY);

        ServiceManager manager = new ServiceManager(getActivity(), updateAvailabilityServiceListener);

//        ChatingService.closeConnection();

        manager.makeServiceRequest(ServiceConstant.UPDATE_AVAILABILITY, Request.Method.POST, jsonParams);

        myDBHelper.insertDriverStatus("2");

        session.createServiceStatus("");

        Intent serviceIntent = new Intent(getActivity(), GEOService.class);

        getActivity().stopService(serviceIntent);

    }

    private void calculateDistance(ArrayList<LatLng> points) {

        float tempTotalDistance = 0;

        for (int i = 0; i < points.size() - 1; i++) {
            LatLng pointA = points.get(i);
            LatLng pointB = points.get(i + 1);
            float[] results = new float[3];
            Location.distanceBetween(pointA.latitude, pointA.longitude, pointB.latitude, pointB.longitude, results);
            tempTotalDistance += results[0];
        }

        totalDistanceTravelled = tempTotalDistance;
    }
    private ServiceManager.ServiceListener updateAvailabilityServiceListener = new ServiceManager.ServiceListener() {
        @Override
        public void onCompleteListener(Object object) {
            try {
                dismissDialog();
                String response = (String) object;
                System.out.println("goofflineresponse---------" + response);


                JSONObject object1 = new JSONObject(response);

                /*session.setXmppServiceState("");

                //Stop xmpp service
                stopService(new Intent(getApplicationContext(), XmppService.class));*/


                if (object1.length() > 0) {
                    String status = object1.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        //   finish();
                        /*Intent i = new Intent(getApplicationContext(), NavigationDrawer.class);
                        startActivity(i);
                        finish();*/
                        StopTimer();
                        Tb_toggle.setChecked(false);
                        Tv_Go_onoff.setText(getResources().getString(R.string.go_offline ));
                        mapHandler.removeCallbacks(mapRunnable);
                        if (isInternetPresent) {
                            driverdashboard_PostRequest(ServiceConstant.driver_dashboard);
                            System.out.println("driverdashboardurl------------" + ServiceConstant.driver_dashboard);
                        } else {
                            Alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
                        }

                        /*Intent i = new Intent(getActivity(), NavigationDrawerNew.class);
                        startActivity(i);*/
                        //finish();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onErrorListener(Object obj) {
            dismissDialog();
            getActivity().finish();
        }
    };
    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    gps = new GPSTracker(getActivity());
                    if (gps != null && gps.canGetLocation()) {
                /*System.out.println("======map handler===========");
                postRequest(ServiceConstant.UPDATE_CURRENT_LOCATION);*/
                        if (myLocation != null) {

                            if (mRequest_update != null) {
                                mRequest_update.cancelRequest();
                            }
                            if (Tb_toggle.isChecked()) {
                                PostRequest(ServiceConstant.UPDATE_CURRENT_LOCATION);
                            }
                        }

                    } else {
                        enableGpsService();
                    }

                }
            });
        }
    }
    public Bitmap StringToBitMap(String encodedString) {
        System.out.println("base 64" + encodedString);
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void DriverMapclass(){

        String status = myDBHelper.retriveStatus();
        if (availability.equalsIgnoreCase("No")) {
            Rl_layout_available_status.setVisibility(View.VISIBLE);
            Tb_toggle.setVisibility(View.INVISIBLE);


            if (status.equalsIgnoreCase("1")) {
                myDBHelper.insertDriverStatus("1");
            } else if (status.equalsIgnoreCase("0")) {
                myDBHelper.insertDriverStatus("0");
            } else if (status.equalsIgnoreCase("3")) {
                myDBHelper.insertDriverStatus("3");
            } else {
                myDBHelper.insertDriverStatus("0");
            }


        } else {
            myDBHelper.insertDriverStatus("0");
            Tb_toggle.setVisibility(View.VISIBLE);
            Rl_layout_available_status.setVisibility(View.GONE);
        }
        String status1 = myDBHelper.retriveStatus();
        System.out.println("driver  status jai " + status1);

        HashMap<String, String> service = session.getServiceStatus();
        String service_status = service.get(SessionManager.KEY_SERVICE_STATUS);

        System.out.println("service status jai " + service_status);

        if (service_status.equalsIgnoreCase("1")) {
            //Service already starts
            System.out.println("already running");
            if (isMyServiceRunning(GEOService.class)) {
                System.out.println("already running");

            } else {
                Intent serviceIntent = new Intent(getActivity(), GEOService.class);
                getActivity().startService(serviceIntent);
                System.out.println("not running");
            }
        } else {
            System.out.println("srvice starts");

            if (isMyServiceRunning(GEOService.class)) {
                System.out.println("already running");
                session.createServiceStatus("1");

            } else {
                Intent serviceIntent = new Intent(getActivity(), GEOService.class);
                getActivity().startService(serviceIntent);
                System.out.println("not running");
            }

        }


        if (gps != null && gps.canGetLocation() && gps.isgpsenabled()) {
        } else {
            enableGpsService();
            //showGpsDisableDialog(getResources().getString(R.string.label_gps_textview));
        }



        String notifStatus = session.getNotificationStatus();
        System.out.println("DriverMapActivity Notification Status" + notifStatus);

        if (notifStatus.equalsIgnoreCase(ServiceConstant.ACTION_TAG_RIDE_REQUEST)) {

            if (session.getDriverAlertData().length() > 0) {
                Intent intent = new Intent(getActivity(), DriverAlertActivity.class);
                intent.putExtra(DriverAlertActivity.EXTRA, session.getDriverAlertData());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                session.setNotificationStatus("");
            }

        }
    }

    private class CarImageSetCar extends AsyncTask<String, Void, String> {

        String response = "";
        private double dLatitude = 0.0;
        private double dLongitude = 0.0;

        CarImageSetCar() {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(car_image);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap b = BitmapFactory.decodeStream(input);
                bmp = b;
                String s = BitMapToString(b);
                System.out.println("bytearray" + s);
                session.setVehicle_BitmapImage(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
          /*  if (mDriverMarker != null) {
                mDriverMarker.remove();
            }*/
            try {

                marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng((strlat), (strlon)))
                        .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                // Move the camera to last position with a zoom level
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng((strlat), (strlon))).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                session.setVehicleImage(car_image);



            } catch (Exception e) {
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng((strlat), (strlon)))
                        .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                // Move the camera to last position with a zoom level
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng((strlat), (strlon))).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                session.setVehicleImage(car_image);
                }
            }
        }

    private void driverDashboard_Toggle_PostRequest(String Url, String mode) {
        dialog = new Dialog(getActivity());
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TextView dialog_title = (TextView) dialog.findViewById(R.id.custom_loading_textview);
        dialog_title.setText(getResources().getString(R.string.action_loading));

        System.out.println("-------------dashboard mode url----------------" + Url);
        System.out.println("-------------dashboard mode status----------------" + mode);

        HashMap<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("driver_id", driver_id);
        jsonParams.put("mode", mode);

        mRequest = new ServiceRequest(getActivity());
        mRequest.makeServiceRequest(Url, Request.Method.POST, jsonParams, new ServiceRequest.ServiceListener() {

            @Override
            public void onCompleteListener(String response) {
                Log.e("togglebtn", response);

                String sStatus = "";
                try {
                    JSONObject jobject = new JSONObject(response);
                    sStatus = jobject.getString("status");

                    if (sStatus.equalsIgnoreCase("1")) {
                        JSONObject object = jobject.getJSONObject("response");
                        Str_mode_status = object.getString("awaiting_request_category");
                        System.out.println("********venki*******Str_mode_status***********"+Str_mode_status);

                        Tv_toggle_multicat.setText(Str_mode_status);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }

            @Override
            public void onErrorListener() {
                dialog.dismiss();
            }
        });
    }
    }

