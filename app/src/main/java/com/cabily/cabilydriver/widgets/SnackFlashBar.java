package com.cabily.cabilydriver.widgets;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cabily.cabilydriver.R;


/**
 * Created by CAS64 on 8/22/2018.
 */

public class SnackFlashBar {
    private Context context;
    private static final int snackBarDur = 1000 * 3;
    private static final int snackBarHeight = 150;
    private static final int snackLeftIconSize = 40;
    private static final int snackIconPadding = 8;
    private int icon;
    private Animation viewIn, viewOut;
    RelativeLayout snackRl;
    View bgView;
    private Handler handler;

    public SnackFlashBar(Context context) {
        this.context = context;
    }

    public SnackFlashBar(Context context,String title, String Message, ViewGroup relativeLayout, String alertStatus) {
        this.context = context;
        viewIn = AnimationUtils.loadAnimation(context, R.anim.top_in);
        viewOut = AnimationUtils.loadAnimation(context, R.anim.top_out);
        ViewGroup mParent = relativeLayout;
        View snackView = View.inflate(context, R.layout.snack_view, mParent);
        snackRl = (RelativeLayout) snackView.findViewById(R.id.snackRl);
        TextView textView1 = (TextView) snackView.findViewById(R.id.txt_title);
        TextView textView2 = (TextView) snackView.findViewById(R.id.txt_message);
        bgView = (View) snackView.findViewById(R.id.bg_view);
        textView1.setText(title);
        textView2.setText(Message);
        ViewIn();

    }

    public void SnackBar(String title, String Message, ViewGroup relativeLayout, String alertStatus) {
        viewIn = AnimationUtils.loadAnimation(context, R.anim.top_in);
        viewOut = AnimationUtils.loadAnimation(context, R.anim.top_out);
        ViewGroup mParent = relativeLayout;
        View snackView = View.inflate(context, R.layout.snack_view, mParent);
        snackRl = (RelativeLayout) snackView.findViewById(R.id.snackRl);
        TextView textView1 = (TextView) snackView.findViewById(R.id.txt_title);
        TextView textView2 = (TextView) snackView.findViewById(R.id.txt_message);
        bgView = (View) snackView.findViewById(R.id.bg_view);
        textView1.setText(title);
        textView2.setText(Message);
        ViewIn();

    }

    private void ViewIn() {

        snackRl.setVisibility(View.VISIBLE);

        snackRl.setVisibility(View.VISIBLE);
        bgView.setVisibility(View.VISIBLE);
        snackRl.startAnimation(viewIn);
        viewIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        ViewOut();
                    }
                };
                handler.postDelayed(runnable, snackBarDur);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void ViewOut() {
        snackRl.startAnimation(viewOut);
        viewOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                snackRl.setVisibility(View.GONE);
                bgView.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
