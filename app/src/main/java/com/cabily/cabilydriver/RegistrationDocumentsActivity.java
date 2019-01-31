package com.cabily.cabilydriver;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class RegistrationDocumentsActivity extends AppCompatActivity {
    private View headerView;
    private RelativeLayout headerBackRl;
    private CollapsingToolbarLayout headerCollapseTL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_documents);

        Init();
    }

    private void Init() {
        headerView = findViewById(R.id.header_registration);
        headerBackRl = (RelativeLayout) headerView.findViewById(R.id.back_layout);
        headerCollapseTL = (CollapsingToolbarLayout) headerView.findViewById(R.id.collapsing_toolbar);
        headerCollapseTL.setTitleEnabled(true);
        headerCollapseTL.setCollapsedTitleTextColor(getResources().getColor(R.color.white_color));
        headerCollapseTL.setTitle(getResources().getString(R.string.documents_header_label));
        headerCollapseTL.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        headerCollapseTL.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);
    }
}
