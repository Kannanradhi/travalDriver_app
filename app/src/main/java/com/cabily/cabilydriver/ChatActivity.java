package com.cabily.cabilydriver;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.app.service.ServiceConstant;
import com.app.service.ServiceRequest;
import com.app.sqliteDb.ChatDatabaseHelper;
import com.app.xmpp.LocalBinder;
import com.app.xmpp.MyXMPP;
import com.app.xmpp.XmppService;
import com.cabily.cabilydriver.Pojo.ChatPojo;
import com.cabily.cabilydriver.Utils.ConnectionDetector;
import com.cabily.cabilydriver.Utils.RoundedImageView;
import com.cabily.cabilydriver.Utils.SessionManager;
import com.cabily.cabilydriver.adapter.ChatAdapter;
import com.cabily.cabilydriver.widgets.PkDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.jivesoftware.smack.chat.Chat;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.google.android.gms.wearable.DataMap.TAG;

public class ChatActivity extends AppCompatActivity {

    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private SessionManager session;
    private RelativeLayout Rl_back;
    private static ListView listView;

    private EditText Et_message;
    private ImageView Iv_send;
    private RelativeLayout Rl_ActiveChat, Rl_deActiveChat;
    private TextView Tv_senderName;
    private RoundedImageView Iv_senderImage;
    private static TextView Tv_status;
    private static ArrayList<ChatPojo> chatList;
    private static ChatAdapter adapter;
    private Chat chat;
    private String sDriverID = "";
    private String sRideID = "";
    BroadcastReceiver updateReciver;
    static SimpleDateFormat df_time = new SimpleDateFormat("hh:mm a");

    private static Context mContext;
    public ChatDatabaseHelper db;

    private boolean isDataAvailable = false, isSenderAvailable = false, isReceiverAvailable = false, isChatAvailable = false;
    private Dialog mLoadingDialog;

    private ServiceRequest mRequest;

    private String sSenderName = "", sSenderID = "", sSenderImage = "";
    private static String sReceiverStatus = "";
    private String sChatStatus = "";
    private String sToID = "";

    public static ChatActivity chat_activity;


    private XmppService xmppService;

    private boolean mBounded;
    private String rideType;


    private final ServiceConnection mConnection = new ServiceConnection() {

        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            xmppService = ((LocalBinder<XmppService>) service).getService();
            mBounded = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            xmppService = null;
            mBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };
    private String sUserStatus = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_chat_bg));
        mContext = ChatActivity.this;
        initialize();
        doBindService();

        HashMap<String,String> ride=session.getrideType();
        rideType=ride.get(SessionManager.KEY_RIDE_TYPE);
        if(rideType.equalsIgnoreCase("Normal"))
        {
            db.updateStatus();
        }else
        {
            db.updateStatusShare(sRideID);
        }

//        db.updateStatus();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.app.conversation.chat");
        filter.addAction("com.app.conversation.chat.send");
//        filter.addAction("com.finish.Chatpage");

        updateReciver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                System.out.println("---------------chat Message-----------------" + intent.getStringExtra("chat_msg"));
                if (intent != null) {

                    Calendar cal = Calendar.getInstance();
                    String sCurrentTime = df_time.format(cal.getTime());

                    String sMessage1 = "";
                    String sAction = "";
                    String sMessage = intent.getStringExtra("chat_msg");
                    String rideId;

                    try {
                        JSONObject messageObject = new JSONObject(sMessage);
                        System.out.println("----------messageObject------" + messageObject);
                        sMessage1 = messageObject.getString("desc");
                        sAction = messageObject.getString("action");
                        rideId= messageObject.getString("ride_id");

                        if (intent.getAction().equals("com.app.conversation.chat")) {




                    /*String sMessage = intent.getStringExtra("chat_msg");
                    System.out.println("---------------chat Message-----------------" + sMessage);

                    if (sMessage.equalsIgnoreCase("PK-TYPING-START")) {
                        Tv_status.setVisibility(View.VISIBLE);
                        Tv_status.setText(mContext.getResources().getString(R.string.action_typing));
                    } else if (sMessage.equalsIgnoreCase("PK-TYPING-STOP")) {

                        if (sReceiverStatus.equalsIgnoreCase("online")) {
                            Tv_status.setVisibility(View.VISIBLE);
                            Tv_status.setText(mContext.getResources().getString(R.string.chat_page_label_online));
                            Tv_status.setTextColor(Color.parseColor("#B1E664"));
                        } else {
                            Tv_status.setVisibility(View.GONE);
                        }

                    } else {
//                        Tv_status.setVisibility(View.GONE);

                        Calendar cal = Calendar.getInstance();
                        String sCurrentTime = df_time.format(cal.getTime());

                        ChatPojo pojo = new ChatPojo();
                        pojo.setMessage(sMessage);
                        pojo.setType("OTHER");
                        pojo.setTime(sCurrentTime);

                        chatList.add(pojo);
                        adapter.notifyDataSetChanged();
                        scrollMyListViewToBottom();


                    }*/


                       /* Calendar cal = Calendar.getInstance();
                        String sCurrentTime = df_time.format(cal.getTime());

                        String sMessage1 = "";
                        String sAction = "";*/
//                    int state = message.arg1;
//                        String sMessage = intent.getStringExtra("chat_msg");
                      /*  try {*/
                        /*JSONObject messageObject = new JSONObject(sMessage);
                        System.out.println("----------messageObject------" + messageObject);
                        sMessage1 = messageObject.getString("message");
                        sAction = messageObject.getString("action");


                        System.out.println("---------------chat Message-----------------" + sMessage);*/

                            if (sAction.equalsIgnoreCase("PK-TYPING-START")) {
//                                Tv_status.setVisibility(View.VISIBLE);
                                Tv_status.setText(mContext.getResources().getString(R.string.action_typing));
                            } else if (sAction.equalsIgnoreCase("PK-TYPING-STOP")) {

                                if (sReceiverStatus.equalsIgnoreCase("online")) {
//                                    Tv_status.setVisibility(View.VISIBLE);
                                    Tv_status.setText(mContext.getResources().getString(R.string.chat_page_label_online));
                                    Tv_status.setTextColor(Color.parseColor("#B1E664"));
                                    Rl_ActiveChat.setVisibility(View.VISIBLE);
//                                    Rl_deActiveChat.setVisibility(View.GONE);
                                } else {
                                    Tv_status.setVisibility(View.GONE);
                                }
                            } else if (sAction.equalsIgnoreCase("PK_ONLINE")) {
                                sReceiverStatus = "online";
                                if (sChatStatus.equalsIgnoreCase("open")) {
                                    Tv_status.setText(mContext.getResources().getString(R.string.chat_page_label_online));
//                                    Tv_status.setVisibility(View.VISIBLE);
                                    Tv_status.setTextColor(Color.parseColor("#B1E664"));
                                    Rl_ActiveChat.setVisibility(View.VISIBLE);
//                                    Rl_deActiveChat.setVisibility(View.GONE);
                                } else {
                                    Tv_status.setText(mContext.getResources().getString(R.string.chat_page_label_online));
                                    Tv_status.setTextColor(Color.parseColor("#FF0000"));
                                    Rl_ActiveChat.setVisibility(View.VISIBLE);
//                                    Rl_deActiveChat.setVisibility(View.GONE);
                                }
                            } else if (sAction.equalsIgnoreCase("PK_OFFLINE")) {
//                                Tv_status.setVisibility(View.VISIBLE);
                                Tv_status.setText(mContext.getResources().getString(R.string.chat_page_label_offline));
                                Tv_status.setTextColor(Color.parseColor("#FF0000"));
//                                Rl_ActiveChat.setVisibility(View.GONE);
//                                Rl_deActiveChat.setVisibility(View.VISIBLE);
                            } /*else if (sAction.equalsIgnoreCase("sending_message")) {
                                Tv_status.setVisibility(View.GONE);
                                ChatPojo pojo = new ChatPojo();
                                pojo.setMessage(sMessage1);
                                pojo.setType("OTHER");
                                pojo.setTime(sCurrentTime);

                                chatList.add(pojo);
                                adapter.notifyDataSetChanged();
                                scrollMyListViewToBottom();
//                            db.addChat(pojo);
                            } else {
                                Tv_status.setVisibility(View.GONE);
                                ChatPojo pojo = new ChatPojo();
                                pojo.setMessage(sMessage1);
                                pojo.setType("OTHER");
                                pojo.setTime(sCurrentTime);

                                chatList.add(pojo);
                                adapter.notifyDataSetChanged();
                                scrollMyListViewToBottom();
//                            db.addChat(pojo);
                            }*/

                    /*    } catch (JSONException e) {
                            e.printStackTrace();
                        }*/


                        } else if (intent.getAction().equals("com.app.conversation.chat.send")) {

//                            Tv_status.setVisibility(View.GONE);
                            ChatPojo pojo = new ChatPojo();
                            pojo.setMessage(sMessage1);
                            pojo.setType("OTHER");
                            pojo.setTime(sCurrentTime);
                            pojo.setChatRideId(Integer.parseInt(rideId));

                            HashMap<String,String> ride=session.getrideType();
                            rideType=ride.get(SessionManager.KEY_RIDE_TYPE);
                            if(rideType.equalsIgnoreCase("Normal"))
                            {
                                chatList.add(pojo);
                                db.updateStatus();
                            }else
                            {
                               if(sRideID.equalsIgnoreCase(rideId))
                               {
                                   chatList.add(pojo);
                                   db.updateStatusShare(rideId);
                               }

                            }
//                            chatList.add(pojo);

//                    chatList=db.getAllChat();
                            adapter.notifyDataSetChanged();

                            scrollMyListViewToBottom();
                        }/*else if (intent.getAction().equals("com.finish.Chatpage")) {
                            finish();
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };


        registerReceiver(updateReciver, filter);


        Rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // close keyboard
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(Rl_back.getWindowToken(), 0);

//                hideTyping();
//                userOffLine();

                onBackPressed();
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd = new ConnectionDetector(ChatActivity.this);
                isInternetPresent = cd.isConnectingToInternet();

                HashMap<String, String> state = session.getuserStatus();
                sUserStatus = state.get(SessionManager.KEY_USERSTATUS);

                if (isInternetPresent) {

//                    if ("online".equalsIgnoreCase(sUserStatus)) {

                        String sMessage = Et_message.getText().toString();

                        if (!sMessage.trim().equalsIgnoreCase("")) {
                            if (sMessage != null && sMessage.length() > 0) {
                                try {

                                    Calendar cal = Calendar.getInstance();
                                    String sCurrentTime = df_time.format(cal.getTime());

                                    ChatPojo pojo = new ChatPojo();
                                    pojo.setMessage(sMessage);
                                    pojo.setType("SELF");
                                    pojo.setTime(sCurrentTime);
                                    pojo.setChatRideId(Integer.parseInt(sRideID));

                                    chatList.add(pojo);
                                    adapter.notifyDataSetChanged();
                                    scrollMyListViewToBottom();


                          /*  JSONObject job = new JSONObject();
                            job.put("action", "sending_message");
                            job.put("type", "chat");
                            job.put("driverid", sDriverID);
                            job.put("rideid", sRideID);
                            job.put("message", sMessage);*/

                                    JSONObject job = new JSONObject();
                                    job.put("action", "dectar_chat");
                                    job.put("type", "0");
                                    job.put("sender_ID", sDriverID);
                                    job.put("ride_id", sRideID);
                                    job.put("desc", sMessage);
                                    job.put("driver_image", "");
                                    job.put("driver_name", "");
                                    job.put("voice_timing", "");
//                            job.put("type", "0");
                                    String data = URLEncoder.encode(job.toString(), "UTF-8");

                                    int xmppSentStatus = XmppService.xmpp.sendMessage(sToID, data);
                                    System.out.println("****************************message send return type*****************" + xmppSentStatus);
                                    db.addChat(pojo);

/*
                            chat = ChatService.createChat(sToID);
                            chat.sendMessage(job.toString());*/


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Et_message.getText().clear();
                            }
                        }
                 /*   } else {
                        alert1(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.chat_page_label_message_cannot_send));
                    }*/
                } else {
                    alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
                }

            }
        });

    }

    private void initialize() {
        cd = new ConnectionDetector(ChatActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        session = new SessionManager(ChatActivity.this);
        chatList = new ArrayList<ChatPojo>();
        db = new ChatDatabaseHelper(getApplicationContext());
        chat_activity = ChatActivity.this;
        session.setchatNotify("0");


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Rl_back = (RelativeLayout) findViewById(R.id.chatPage_headerBar_back_layout);
        Tv_senderName = (TextView) findViewById(R.id.chatPage_headerBar_senderName_textView);
        Iv_senderImage = (RoundedImageView) findViewById(R.id.chatPage_header_senderImage);
        listView = (ListView) findViewById(R.id.chatPage_listView);

        Tv_status = (TextView) findViewById(R.id.chatPage_headerBar_senderName_status);

        Et_message = (EditText) findViewById(R.id.chatPage_message_editText);
        Iv_send = (ImageView) findViewById(R.id.chatPage_send_imageView);
        Rl_ActiveChat = (RelativeLayout) findViewById(R.id.chatPage_bottom_layout);
        Rl_deActiveChat = (RelativeLayout) findViewById(R.id.chatPage_noChat_layout);

        Et_message.addTextChangedListener(chatEditorWatcher);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        sDriverID = user.get(SessionManager.KEY_DRIVERID);


        Intent intent = getIntent();
        if (intent != null) {
            if(intent.getStringExtra("Ride_id")==null)
            {
                HashMap<String,String> ride=session.getUserDetails();
                sRideID=ride.get(SessionManager.KEY_id);

            }else {
                sRideID = intent.getStringExtra("Ride_id");
            }


            System.out.println("-----------Intent sRideID------------" + sRideID);

            if (isInternetPresent) {
                postRequest_ChatDetail(ServiceConstant.chat_detail_url);
            } else {
                alert(getResources().getString(R.string.alert_sorry_label_title), getResources().getString(R.string.alert_nointernet));
            }
        }
        HashMap<String,String> ride=session.getrideType();
        rideType=ride.get(SessionManager.KEY_RIDE_TYPE);
        if(rideType.equalsIgnoreCase("Normal"))
        {
            chatList = db.getAllChat();
        }else
        {
            chatList = db.getChat(Integer.parseInt(sRideID));
        }
//        chatList = db.getAllChat();

        adapter = new ChatAdapter(ChatActivity.this, chatList);
        listView.setAdapter(adapter);

    }


    //--------Alert Method------
    private void alert(String title, String alert) {

        final PkDialog mDialog = new PkDialog(ChatActivity.this);
        mDialog.setDialogTitle(title);
        mDialog.setDialogMessage(alert);
        mDialog.setPositiveButton(getResources().getString(R.string.alert_label_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void alert1(String title, String alert) {

        final PkDialog mDialog = new PkDialog(ChatActivity.this);
        mDialog.setDialogTitle(title);
        mDialog.setDialogMessage(alert);
        mDialog.setPositiveButton(getResources().getString(R.string.alert_label_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                // close keyboard
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(Rl_back.getWindowToken(), 0);

//                hideTyping();
//                userOffLine();

                onBackPressed();
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        mDialog.show();
    }

    //---Scroll ListView to bottom---
    private static void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(adapter.getCount() - 1);
            }
        });
    }


    //-----------------------Chat Detail Post Request-----------------
    private void postRequest_ChatDetail(String Url) {

        mLoadingDialog = new Dialog(ChatActivity.this);
        mLoadingDialog.getWindow();
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.custom_loading);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();

        final TextView dialog_title = (TextView) mLoadingDialog.findViewById(R.id.custom_loading_textview);
        dialog_title.setText(getResources().getString(R.string.action_loading));

       /* mLoadingDialog = new LoadingDialog(ChatActivity.this);
        mLoadingDialog.setLoadingTitle(getResources().getString(R.string.action_loading));
        mLoadingDialog.show();*/

        System.out.println("-------------Chat Detail Url----------------" + Url);

        System.out.println("-----------id------------" + sDriverID);
        System.out.println("-----------Ride_id------------" + sRideID);


        HashMap<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("user_type", "driver");
        jsonParams.put("id", sDriverID);
        jsonParams.put("ride_id", sRideID);

        mRequest = new ServiceRequest(ChatActivity.this);
        mRequest.makeServiceRequest(Url, Request.Method.POST, jsonParams, new ServiceRequest.ServiceListener() {
            @Override
            public void onCompleteListener(String response) {

                System.out.println("------------Chat Detail Response----------------" + response);

                String sStatus = "";
                try {
                    JSONObject object = new JSONObject(response);
                    sStatus = object.getString("status");
                    if (sStatus.equalsIgnoreCase("1")) {

                        Object check_response_object = object.get("response");
                        if (check_response_object instanceof JSONObject) {
                            JSONObject response_object = object.getJSONObject("response");
                            if (response_object.length() > 0) {


                                Object check_receiver_object = response_object.get("receiver");
                                if (check_receiver_object instanceof JSONObject) {
                                    JSONObject receiver_object = response_object.getJSONObject("receiver");

                                    if (receiver_object.length() > 0) {
                                        sSenderID = receiver_object.getString("id");
                                        sSenderName = receiver_object.getString("name");
                                        sSenderImage = receiver_object.getString("image");

                                        isReceiverAvailable = true;
                                    } else {
                                        isReceiverAvailable = false;
                                    }
                                } else {
                                    isReceiverAvailable = false;
                                }


                                Object check_chat_object = response_object.get("chat");
                                if (check_chat_object instanceof JSONObject) {
                                    JSONObject chat_object = response_object.getJSONObject("chat");

                                    if (chat_object.length() > 0) {
                                        sReceiverStatus = chat_object.getString("receiver_status");
                                        sChatStatus = chat_object.getString("chat_status");
                                        session.setuserStatus(sReceiverStatus);

                                        isChatAvailable = true;
                                    } else {
                                        isChatAvailable = false;
                                    }
                                } else {
                                    isChatAvailable = false;
                                }


                                isDataAvailable = true;
                            } else {
                                isDataAvailable = false;
                            }
                        } else {
                            isDataAvailable = false;
                        }
                    }


                    if (sStatus.equalsIgnoreCase("1")) {
                        if (isDataAvailable) {
                            if (isChatAvailable) {
                                if (sReceiverStatus.equalsIgnoreCase("online") && sChatStatus.equalsIgnoreCase("open")) {
                                    Rl_ActiveChat.setVisibility(View.VISIBLE);
//                                    Rl_deActiveChat.setVisibility(View.GONE);
                                    Tv_status.setText(getResources().getString(R.string.chat_page_label_online));
                                    Tv_status.setTextColor(Color.parseColor("#B1E664"));
//                                    Tv_status.setVisibility(View.VISIBLE);
                                } else {
//                                    Rl_ActiveChat.setVisibility(View.GONE);
//                                    Rl_deActiveChat.setVisibility(View.VISIBLE);
                                    Tv_status.setText(getResources().getString(R.string.chat_page_label_offline));
                                    Tv_status.setTextColor(Color.parseColor("#C5C5C5"));
//                                    Tv_status.setVisibility(View.VISIBLE);
                                }

                                if (isReceiverAvailable) {
                                    String hostName = session.getXmpp().get(SessionManager.KEY_HOST_NAME);
                                    sToID = sSenderID + "@" + hostName;

                                    System.out.println("---------------kannan user send id------" + sToID);

//                                    chat = ChatService.createChat(sToID);
                                    userOnLine();
//                                    hideTyping();
//                                    ChatService.setChatMessenger(new Messenger(new MessageHandler()));
//                                    ChatService.enableChat();

                                    Tv_senderName.setText(sSenderName);
                                    Picasso.with(ChatActivity.this).load(sSenderImage).error(R.drawable.placeholder_icon)
                                            .placeholder(R.drawable.placeholder_icon).memoryPolicy(MemoryPolicy.NO_CACHE).into(Iv_senderImage);
                                } else {
                                    final PkDialog mDialog = new PkDialog(ChatActivity.this);
                                    mDialog.setDialogTitle(getResources().getString(R.string.alert_sorry_label_title));
                                    mDialog.setDialogMessage(getResources().getString(R.string.chat_page_label_server_error));
                                    mDialog.setPositiveButton(getResources().getString(R.string.alert_label_ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDialog.dismiss();
                                            onBackPressed();
                                            finish();
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        }
                                    });
                                    mDialog.show();
                                }
                            }

                            if (getIntent().hasExtra("msg") && getIntent().hasExtra("time")) {
                                String msg = getIntent().getStringExtra("msg");
                                String time = getIntent().getStringExtra("time");
                                sendSingleMsg(msg, time);
                            }

                        }
                    } else {

                    }


                } catch (JSONException e) {
                    mLoadingDialog.dismiss();
                    e.printStackTrace();
                }
                mLoadingDialog.dismiss();
            }

            @Override
            public void onErrorListener() {
                mLoadingDialog.dismiss();
            }
        });
    }


    private void sendSingleMsg(String msg, String time) {

        ChatPojo pojo = new ChatPojo();
        pojo.setMessage(msg);
        pojo.setType("OTHER");
        pojo.setTime(time);
        pojo.setChatRideId(Integer.parseInt(sRideID));

        chatList.add(pojo);
        adapter.notifyDataSetChanged();
        scrollMyListViewToBottom();

    }


    //----Show typing text----
    private void showTyping() {
        try {

            JSONObject job = new JSONObject();
            job.put("action", "PK-TYPING-START");
            job.put("type", "0");
            job.put("sender_ID", sDriverID);
            job.put("ride_id", sRideID);
            job.put("desc", "");
            job.put("driver_image", "");
            job.put("driver_name", "");
            job.put("voice_timing", "");
//                            job.put("type", "0");
            String data = URLEncoder.encode(job.toString(), "UTF-8");



           /* JSONObject job = new JSONObject();
            job.put("action", "PK-TYPING-START");
            job.put("type", "0");
            job.put("driverid", sDriverID);
            job.put("rideid", sRideID);
            job.put("message", "");*/

            int xmppSentStatus = XmppService.xmpp.sendMessage(sToID, data);

           /* chat = ChatService.createChat(sToID);
            chat.sendMessage(job.toString());
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----Hide typing text----
    private void hideTyping() {
        try {


            JSONObject job = new JSONObject();
            job.put("action", "PK-TYPING-STOP");
            job.put("type", "0");
            job.put("sender_ID", sDriverID);
            job.put("ride_id", sRideID);
            job.put("desc", "");
            job.put("driver_image", "");
            job.put("driver_name", "");
            job.put("voice_timing", "");
//                            job.put("type", "0");
            String data = URLEncoder.encode(job.toString(), "UTF-8");


/*
            JSONObject job = new JSONObject();
            job.put("action", "PK-TYPING-STOP");
            job.put("type", "0");
            job.put("driverid", sDriverID);
            job.put("rideid", sRideID);
            job.put("message", "");*/


            int xmppSentStatus = XmppService.xmpp.sendMessage(sToID, data);

            /*chat = ChatService.createChat(sToID);
            chat.sendMessage(job.toString());*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //----user offline text----
    private void userOffLine() {
        try {

            JSONObject job = new JSONObject();
            job.put("action", "PK_OFFLINE");
            job.put("type", "0");
            job.put("sender_ID", sDriverID);
            job.put("ride_id", sRideID);
            job.put("desc", "");
            job.put("driver_image", "");
            job.put("driver_name", "");
            job.put("voice_timing", "");
//                            job.put("type", "0");
            String data = URLEncoder.encode(job.toString(), "UTF-8");

          /*  JSONObject job = new JSONObject();
            job.put("action", "PK_OFFLINE");
            job.put("type", "0");
            job.put("driverid", sDriverID);
            job.put("rideid", sRideID);
            job.put("message", "");
*/
            int xmppSentStatus = XmppService.xmpp.sendMessage(sToID, data);

           /* chat = ChatService.createChat(sToID);
            chat.sendMessage(job.toString());*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----user offline text----
    private void userOnLine() {
        try {

            JSONObject job = new JSONObject();
            job.put("action", "PK_ONLINE");
            job.put("type", "0");
            job.put("sender_ID", sDriverID);
            job.put("ride_id", sRideID);
            job.put("desc", "");
            job.put("driver_image", "");
            job.put("driver_name", "");
            job.put("voice_timing", "");
//                            job.put("type", "0");
            String data = URLEncoder.encode(job.toString(), "UTF-8");

           /* JSONObject job = new JSONObject();
            job.put("action", "PK_ONLINE");
            job.put("type", "0");
            job.put("driverid", sDriverID);
            job.put("rideid", sRideID);
            job.put("message", "");*/

            int xmppSentStatus = XmppService.xmpp.sendMessage(sToID, data);

           /* chat = ChatService.createChat(sToID);
            chat.sendMessage(job.toString());
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //-----------Code for TextWatcher----------
    private TextWatcher chatEditorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (Et_message.getText().toString().length() == 0) {
                Iv_send.setImageResource(R.drawable.ic_chat_end_unfill);
                Iv_send.setEnabled(false);
            } else {
                Iv_send.setImageResource(R.drawable.ic_chat_send_fill);
                Iv_send.setEnabled(true);
            }


            if (Et_message.getText().toString().length() > 0) {
//                showTyping();
            } else {
//                hideTyping();
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
//        if (!ChatService.isConnected) {
//            ChatService.startUserAction(ChatPage.this);
//        }
//
//        ChatService.enableChat();

        if (MyXMPP.instance != null) {
            MyXMPP.enableChat();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
//        ChatService.disableChat();

        if (MyXMPP.instance != null) {
            MyXMPP.disableChat();

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ChatService.disableChat();
        unregisterReceiver(updateReciver);
        MyXMPP.disableChat();
        doUnbindService();

    }

    void doBindService() {
        System.out.println("---------jai-----service bind------");
        bindService(new Intent(this, XmppService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }


}
