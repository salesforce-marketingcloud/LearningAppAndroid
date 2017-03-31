/*
 * Copyright (c) 2016, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.marketingcloud.android.demoapp.ui;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.salesforce.marketingcloud.MarketingCloudSdk;
import com.salesforce.marketingcloud.android.demoapp.R;

import hugo.weaving.DebugLog;

/**
 * CloudPageInboxActivity works as an inbox for the Cloud Pages received.
 *
 * @author Salesforce &reg; 2017.
 */
@DebugLog
public class OriginalDocsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView markdownView = (WebView) findViewById(R.id.markdownView);
        markdownView.getSettings().setJavaScriptEnabled(true);
        markdownView.loadUrl(getResources().getString(R.string.official_remote_url));
        markdownView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        MarketingCloudSdk.requestSdk(new MarketingCloudSdk.WhenReadyListener() {
            @Override
            public void ready(MarketingCloudSdk marketingCloudSdk) {
                marketingCloudSdk.getAnalyticsManager().trackPageView("data://OriginalDocsActivity", getResources().getString(R.string.official_remote_url), null, null);
            }
        });
    }

    /**
     * Navigates back to parent's Activity: MainActivity
     *
     * @param item which is the reference to the parent's activity: MainActivity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
