/*
 * Copyright (c) 2016, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.marketingcloud.android.demoapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salesforce.marketingcloud.android.demoapp.LearningAppApplication;
import com.salesforce.marketingcloud.android.demoapp.R;

import hugo.weaving.DebugLog;

@DebugLog
public class InformationFragment extends android.app.Fragment {
    private static final String TAG = "~#InformationFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        return rootView;
    }
}
