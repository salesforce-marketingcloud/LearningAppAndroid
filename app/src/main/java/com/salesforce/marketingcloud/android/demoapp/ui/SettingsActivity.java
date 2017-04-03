/*
 * Copyright (c) 2016, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.marketingcloud.android.demoapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.salesforce.marketingcloud.android.demoapp.R;

import hugo.weaving.DebugLog;

/**
 * SettingsActivity is the primary settings activity.
 *
 * This activity extends AppCompatActivity and provides the primary settings interface to collect user preferences.
 *
 * It is implemented calling a SettingsFragment class.
 *
 *
 * @author Salesforce &reg; 2017.
 */

@DebugLog
public class SettingsActivity extends AppCompatActivity {

    private boolean mListStyled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Just a hack to remove section dividers
        if (!mListStyled) {
            View rootView = findViewById(android.R.id.content);
            if (rootView != null) {
                ListView list = (ListView) rootView.findViewById(android.R.id.list);
                list.setPadding(0, 0, 0, 0);
                list.setDivider(null);
                //any other styling call
                mListStyled = true;
            }
        }
    }
}