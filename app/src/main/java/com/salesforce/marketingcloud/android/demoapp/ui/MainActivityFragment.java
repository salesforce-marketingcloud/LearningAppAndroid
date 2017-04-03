/*
 * Copyright (c) 2016, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.marketingcloud.android.demoapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salesforce.marketingcloud.MarketingCloudSdk;
import com.salesforce.marketingcloud.analytics.AnalyticsManager;
import com.salesforce.marketingcloud.android.demoapp.R;

import hugo.weaving.DebugLog;

/**
 * A placeholder fragment containing the home view.
 *
 * @author Salesforce &reg; 2017.
 */
@DebugLog
public class MainActivityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MarketingCloudSdk.requestSdk(new MarketingCloudSdk.WhenReadyListener() {
            @Override
            public void ready(MarketingCloudSdk marketingCloudSdk) {
                AnalyticsManager analyticsManager = marketingCloudSdk.getAnalyticsManager();
                analyticsManager.trackPageView("data://LaunchedMainActivity", "Main Activity Launched", null, null);
                analyticsManager.trackPageView("LaunchedMainActivity", "Main Activity Launched", null, null);
                analyticsManager.trackPageView("MainActivityLaunched", null, null, null);
            }
        });

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
