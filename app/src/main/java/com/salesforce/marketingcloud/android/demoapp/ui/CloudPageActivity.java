/*
 * Copyright (c) 2016, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.marketingcloud.android.demoapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.salesforce.marketingcloud.MarketingCloudSdk;
import com.salesforce.marketingcloud.android.demoapp.R;

import hugo.weaving.DebugLog;

/**
 * CloudPageActivity displays a Cloud Page.
 *
 * @author Salesforce &reg; 2017.
 */
@DebugLog
public class CloudPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.action_cloudpage_inbox));

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);

        WebView webView = new WebView(this);
        webView.loadUrl(getIntent().getStringExtra("_x"));
        webView.getSettings().setJavaScriptEnabled(true);
        linearLayout.addView(webView);
        setContentView(linearLayout);

        MarketingCloudSdk.requestSdk(new MarketingCloudSdk.WhenReadyListener() {
            @Override
            public void ready(MarketingCloudSdk marketingCloudSdk) {
                marketingCloudSdk.getAnalyticsManager().trackPageView(getIntent().getStringExtra("_x"), "Cloud Page displayed", null, null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
    }

}
