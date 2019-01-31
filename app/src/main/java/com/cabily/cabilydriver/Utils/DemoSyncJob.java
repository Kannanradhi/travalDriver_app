package com.cabily.cabilydriver.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.app.xmpp.MyXMPP;
import com.app.xmpp.XmppService;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.HashMap;

public class DemoSyncJob extends Job {
    private static int jobId = -1;
    public static final String TAG = ">>>> job_demo_tag";
    public static SessionManager sessionManager;
    static Context context;

    @Override
    protected Result onRunJob(Params params) {
        // run your job here
        Log.d(TAG, "onRunJob: ");
        if (sessionManager == null) {
            sessionManager = new SessionManager(this.getContext());
        }
        context = this.getContext();
        scheduleJob(context);


        if (!isMyServiceRunning(this.getContext(), XmppService.class)) {

            if (MyXMPP.connected) {
                Log.d(TAG, "onRunJob: Xmpp connected");
            } else {
                sessionManager = new SessionManager(this.getContext());

                // get user data from session
                HashMap<String, String> domain = sessionManager.getXmpp();
                String ServiceName = domain.get(SessionManager.KEY_HOST_NAME);
                String HostAddress = domain.get(SessionManager.KEY_HOST_URL);

                HashMap<String, String> user = sessionManager.getUserDetails();
                String USERNAME = user.get(SessionManager.KEY_DRIVERID);
                String PASSWORD = user.get(SessionManager.KEY_SEC_KEY);

                System.out.println("----------ServiceName------------" + ServiceName);
                System.out.println("----------HostAddress------------" + HostAddress);
                System.out.println("----------USERNAME------------" + USERNAME);
                System.out.println("----------PASSWORD------------" + PASSWORD);

                MyXMPP xmpp = MyXMPP.getInstance(this.getContext(), ServiceName, HostAddress, USERNAME, PASSWORD);
                xmpp.connect("onCreate");
            }
        } else {
            if (MyXMPP.connected) {
                Log.d(TAG, "onRunJob: Xmpp connected");
            } else {
                sessionManager = new SessionManager(this.getContext());

                // get user data from session
                HashMap<String, String> domain = sessionManager.getXmpp();
                String ServiceName = domain.get(SessionManager.KEY_HOST_NAME);
                String HostAddress = domain.get(SessionManager.KEY_HOST_URL);

                HashMap<String, String> user = sessionManager.getUserDetails();
                String USERNAME = user.get(SessionManager.KEY_DRIVERID);
                String PASSWORD = user.get(SessionManager.KEY_SEC_KEY);

                System.out.println("----------ServiceName------------" + ServiceName);
                System.out.println("----------HostAddress------------" + HostAddress);
                System.out.println("----------USERNAME------------" + USERNAME);
                System.out.println("----------PASSWORD------------" + PASSWORD);

                MyXMPP xmpp = MyXMPP.getInstance(this.getContext(), ServiceName, HostAddress, USERNAME, PASSWORD);
                xmpp.connect("onCreate");
            }
        }

        return Result.SUCCESS;
    }

    public static void scheduleJob(Context context) {

        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        int demoSyncJob = new JobRequest.Builder(DemoSyncJob.TAG)
                .setExecutionWindow(1_000L, 30_000L)
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
        sessionManager.setjobid(demoSyncJob);
    }


    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "isMyServiceRunning: ", e);
        }
        return false;
    }
}