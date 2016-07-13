package com.salesforce.marketingcloud.android.demoapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.salesforce.marketingcloud.android.demoapp.LearningAppApplication;
import com.salesforce.marketingcloud.android.demoapp.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView lastBeaconReceivedEventDatetime = (TextView) findViewById(R.id.info_last_beacon_received_event_datetime);
        LearningAppApplication learningAppApplication = ((LearningAppApplication) getApplication());
        lastBeaconReceivedEventDatetime.setText(learningAppApplication.getLastBeaconReceivedEventDatetime());
    }

}
