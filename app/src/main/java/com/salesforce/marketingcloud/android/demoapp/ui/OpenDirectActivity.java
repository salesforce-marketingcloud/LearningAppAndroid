package com.salesforce.marketingcloud.android.demoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.exacttarget.etpushsdk.ETLandingPagePresenter;
import com.salesforce.marketingcloud.android.demoapp.R;

import java.util.Locale;

public class OpenDirectActivity extends AppCompatActivity {

    private static final String TAG = "~#" + OpenDirectActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_direct);

        String url = null;
        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("_od")) {
            url = this.getIntent().getExtras().getString("_od");
        }

        Intent intent = new Intent(this, MainActivity.class);
        if (!TextUtils.isEmpty(url)) {
            switch (url) {
                case "527":
                    intent = new Intent(this, OriginalDocsActivity.class);
                    break;
                case "123":
                    intent = new Intent(this, MapsActivity.class);
                    break;
                default:
                    if (URLUtil.isValidUrl(url)) {
                        intent = new Intent(this, ETLandingPagePresenter.class);
                        intent.putExtras(getIntent().getExtras());
                    }
            }
        }
        Log.i(TAG, String.format(Locale.ENGLISH, "URL: %s", url));
        startActivity(intent);
        this.finish();
    }
}
