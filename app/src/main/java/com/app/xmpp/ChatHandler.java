package com.app.xmpp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.app.service.ServiceConstant;
import com.app.sqliteDb.ChatDatabaseHelper;
import com.cabily.cabilydriver.AdsPage;
import com.cabily.cabilydriver.ChatActivity;
import com.cabily.cabilydriver.DriverAlertActivity;
import com.cabily.cabilydriver.Helper.GEODBHelper;
import com.cabily.cabilydriver.NewTripAlert;
import com.cabily.cabilydriver.Pojo.ChatPojo;
import com.cabily.cabilydriver.PushNotificationAlert;
import com.cabily.cabilydriver.R;
import com.cabily.cabilydriver.Splash;
import com.cabily.cabilydriver.Utils.SessionManager;

import org.jivesoftware.smack.packet.Message;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by user88 on 11/4/2015.
 */
public class ChatHandler {

    private Context context;
    private IntentService service;
    private GEODBHelper myDBHelper;
    private SessionManager session;
    static SimpleDateFormat df_time = new SimpleDateFormat("hh:mm a");
    ChatDatabaseHelper db;
    SessionManager sessionManager;
    private String rideID;

    public ChatHandler(Context context, IntentService service) {
        this.context = context;
        this.service = service;
        session = new SessionManager(context);
        myDBHelper = new GEODBHelper(context);
    }

    public ChatHandler(Context context) {
        this.context = context;
        session = new SessionManager(context);
        myDBHelper = new GEODBHelper(context);

    }

    public void onHandleChatMessage(Message message) {
        try {
            String data = URLDecoder.decode(message.getBody(), "UTF-8");
            System.out.println("----------jai1---Xmpp response---------------------" + data);
            JSONObject messageObject = new JSONObject(data);
            String action = (String) messageObject.get(ServiceConstant.ACTION_LABEL);
            System.out.println("-------jai1------Xmpp action---------------------" + action);

            Calendar cal = Calendar.getInstance();
            String sCurrentTime = df_time.format(cal.getTime());
            db = new ChatDatabaseHelper(context);
            if (action.equalsIgnoreCase("PK-TYPING-STOP") || action.equalsIgnoreCase("PK_ONLINE") || action.equalsIgnoreCase("PK_OFFLINE") || action.equalsIgnoreCase("PK-TYPING-START")) {
                Intent local = new Intent();
                local.setAction("com.app.conversation.chat");
                local.putExtra("chat_msg", data);
                context.sendBroadcast(local);


            } else if (action.equalsIgnoreCase("dectar_chat")) {


                HashMap<String, String> state = session.getAppStatus();
                String Str_driverState = state.get(SessionManager.KEY_APP_STATUS);
                String sMessage1 = "", rideId = "";

                sMessage1 = messageObject.getString("desc");
                rideId = messageObject.getString("ride_id");


                db.addChat(new ChatPojo(sMessage1, "OTHER", sCurrentTime, "0", Integer.parseInt(rideId)));
                if (Str_driverState.equalsIgnoreCase("resume")) {
                    Intent local = new Intent();
                    local.setAction("com.app.conversation.chat.send");
                    local.putExtra("chat_msg", data);
                    context.sendBroadcast(local);

                } else {

                    Intent notificationIntent;
                    if (Str_driverState.equalsIgnoreCase("pause")) {
                    /*    HashMap<String, String> ride = sessionManager.getrideId();
                        rideID = ride.get(SessionManager.KEY_RIDE_ID);*/
//                        rideID="";
                        notificationIntent = new Intent(context, ChatActivity.class);
                        notificationIntent.putExtra("Ride_id", rideId);
                        if (session == null) {
                            session = new SessionManager(context);
                        }
                        session.setchatNotify("1");
                        if(ChatActivity.chat_activity!=null) {
                            ChatActivity.chat_activity.finish();
                        }

                    } else {
                        if (session == null) {
                            session = new SessionManager(context);
                        }
                        session.setchatNotify("1");
                        notificationIntent = new Intent(context, Splash.class);
                    }

                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Resources res = context.getResources();
                    Notification n;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String channelId = "chat";
                        CharSequence channelName = "chatChannel";
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
                        nm.createNotificationChannel(notificationChannel);

                        Notification.Builder builder = new Notification.Builder(context, channelId);
                        builder.setContentIntent(contentIntent)
                                .setSmallIcon(R.drawable.pushlogodri)
                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.applogo))
                                .setTicker(sMessage1)
                                .setColor(context.getResources().getColor(R.color.app_color))
                                .setWhen(System.currentTimeMillis())
                                .setAutoCancel(true)
                                .setContentTitle(context.getResources().getString(R.string.app_name))
                                .setContentText(sMessage1);
                        n = builder.getNotification();
                    } else {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        builder.setContentIntent(contentIntent)
                                .setSmallIcon(R.drawable.pushlogodri)
                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.applogo))
                                .setTicker(sMessage1)
                                .setColor(context.getResources().getColor(R.color.app_color))
                                .setWhen(System.currentTimeMillis())
                                .setAutoCancel(true)
                                .setContentTitle(context.getResources().getString(R.string.app_name))
                                .setLights(0xffff0000, 100, 2000)
                                .setPriority(Notification.DEFAULT_SOUND)
                                .setContentText(sMessage1);
                        n = builder.getNotification();
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int smallIconViewId = context.getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());

                        if (smallIconViewId != 0) {
                            if (n.contentView != null)
                                n.contentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

                            if (n.headsUpContentView != null)
                                n.headsUpContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

                            if (n.bigContentView != null)
                                n.bigContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);
                        }
                    }
                    n.flags |= Notification.FLAG_AUTO_CANCEL;
                    n.defaults |= Notification.DEFAULT_ALL;
                    nm.notify(0, n);
                }


            } else {
                if (ServiceConstant.ACTION_TAG_RIDE_REQUEST.equalsIgnoreCase(action)) {
                    rideRequest(data);
                } else if (ServiceConstant.ACTION_TAG_RIDE_CANCELLED.equalsIgnoreCase(action)) {
                    rideCancelled(messageObject);
                } else if (ServiceConstant.ACTION_TAG_RECEIVE_CASH.equalsIgnoreCase(action)) {
                    receiveCash(messageObject);
                } else if (ServiceConstant.ACTION_TAG_PAYMENT_PAID.equalsIgnoreCase(action)) {
                    paymentPaid(messageObject);
                } else if (ServiceConstant.ACTION_TAG_NEW_TRIP.equalsIgnoreCase(action)) {
                    newTipAlert(messageObject);
                } else if (ServiceConstant.pushNotification_Ads.equalsIgnoreCase(action)) {
                    display_Ads(messageObject);
                } else if (ServiceConstant.ACTION_RIDE_COMPLETED.equalsIgnoreCase(action)) {


                    myDBHelper.Delete("");
                    //  endTripHandler.removeCallbacks(endTripRunnable);
                    myDBHelper.insertDriverStatus("0");
                    session.setWaitingStatus("0");
                    session.setWaitingTime("0");

                    rideCompleted(messageObject);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rideRequest(String message) {
        System.out.println("xmpp---------------------jai" + message);
        try {
            Intent intent = new Intent(context, DriverAlertActivity.class);
            intent.putExtra("EXTRA", message);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rideCompleted(JSONObject messageObject) throws Exception {

        System.out.println("----jai1----------ride_completed" + messageObject);
        Intent intent = new Intent(context, PushNotificationAlert.class);
        intent.putExtra("Message", messageObject.getString("message"));
        intent.putExtra("Action", messageObject.getString("action"));
        intent.putExtra("RideId", messageObject.getString("key1"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void rideCancelled(JSONObject messageObject) throws Exception {


        Intent intent = new Intent(context, PushNotificationAlert.class);
        intent.putExtra("Message", messageObject.getString("message"));
        intent.putExtra("Action", messageObject.getString("action"));
        intent.putExtra("RideId", messageObject.getString("key1"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void receiveCash(JSONObject messageObject) throws Exception {
/*        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.finish.OtpPage");
        context.sendOrderedBroadcast(broadcastIntent,null);*/
        System.out.println("rideId----------------xmpp" + messageObject.getString("key1"));

        Intent intent = new Intent(context, PushNotificationAlert.class);
        intent.putExtra("Message", messageObject.getString("message"));
        intent.putExtra("Action", messageObject.getString("action"));
        intent.putExtra("amount", messageObject.getString("key3"));
        intent.putExtra("RideId", messageObject.getString("key1"));
        intent.putExtra("Currencycode", messageObject.getString("key4"));

        System.out.println("currncyputex---------------" + messageObject.getString("key4"));
        System.out.println("amountputex---------------" + messageObject.getString("key3"));


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void paymentPaid(JSONObject messageObject) throws Exception {
      /*  Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.finish.OtpPage");
        context.sendOrderedBroadcast(broadcastIntent,null);

        Intent broadcastIntent_payment = new Intent();
        broadcastIntent_payment.setAction("com.finish.PaymentPage");
        context.sendOrderedBroadcast(broadcastIntent_payment,null);

        Intent broadcastIntent_paymenttrip = new Intent();
        broadcastIntent_paymenttrip.setAction("com.finish.tripsummerydetail");
        context.sendOrderedBroadcast(broadcastIntent_paymenttrip,null);*/


        Intent intent = new Intent(context, PushNotificationAlert.class);
        intent.putExtra("Message", messageObject.getString("message"));
        intent.putExtra("Action", messageObject.getString("action"));
        intent.putExtra("RideId", messageObject.getString("key1"));


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void newTipAlert(JSONObject messageObject) throws Exception {

        System.out.println("---------------inside new trip------------------");

        Intent intent = new Intent(context, NewTripAlert.class);
        intent.putExtra("Message", messageObject.getString("message"));
        intent.putExtra("Action", messageObject.getString("action"));
        intent.putExtra("Username", messageObject.getString("key1"));
        intent.putExtra("Mobilenumber", messageObject.getString("key3"));
        intent.putExtra("UserImage", messageObject.getString("key4"));
        intent.putExtra("UserRating", messageObject.getString("key5"));
        intent.putExtra("RideId", messageObject.getString("key6"));
        intent.putExtra("UserPickuplocation", messageObject.getString("key7"));
        intent.putExtra("UserPickupTime", messageObject.getString("key10"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

   /* private void newTipAlert(JSONObject messageObject) throws Exception {

        System.out.println("---------------inside new trip------------------");

        Intent intent = new Intent(context, NewTripAlert.class);
        intent.putExtra("Message", messageObject.getString("message"));
        intent.putExtra("Action", messageObject.getString("action"));
        intent.putExtra("Username", messageObject.getString("key1"));
        intent.putExtra("Mobilenumber", messageObject.getString("key3"));
        intent.putExtra("UserImage", messageObject.getString("key4"));
        intent.putExtra("UserRating", messageObject.getString("key5"));
        intent.putExtra("RideId", messageObject.getString("key6"));
        intent.putExtra("UserPickuplocation", messageObject.getString("key7"));
        intent.putExtra("UserPickupTime", messageObject.getString("key10"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        service.startActivity(intent);

    }*/


    private void display_Ads(JSONObject messageObject) throws Exception {
        Intent i1 = new Intent(context, AdsPage.class);
        i1.putExtra("AdsTitle", messageObject.getString(ServiceConstant.Ads_title));
        i1.putExtra("AdsMessage", messageObject.getString(ServiceConstant.Ads_Message));
        if (messageObject.has(ServiceConstant.Ads_image)) {
            i1.putExtra("AdsBanner", messageObject.getString(ServiceConstant.Ads_image));
        }
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i1);
    }


}

