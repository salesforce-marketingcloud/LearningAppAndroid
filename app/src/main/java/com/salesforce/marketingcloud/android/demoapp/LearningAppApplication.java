/*
 * Copyright (c) 2016, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.marketingcloud.android.demoapp;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.salesforce.marketingcloud.InitializationStatus;
import com.salesforce.marketingcloud.MCLogListener;
import com.salesforce.marketingcloud.MarketingCloudConfig;
import com.salesforce.marketingcloud.MarketingCloudSdk;
import com.salesforce.marketingcloud.android.demoapp.data.MCBeacon;
import com.salesforce.marketingcloud.android.demoapp.data.MCGeofence;
import com.salesforce.marketingcloud.android.demoapp.data.MCLocationManager;
import com.salesforce.marketingcloud.messages.Region;
import com.salesforce.marketingcloud.messages.RegionMessageManager;
import com.salesforce.marketingcloud.messages.geofence.GeofenceMessageResponse;
import com.salesforce.marketingcloud.messages.proximity.ProximityMessageResponse;
import com.salesforce.marketingcloud.notifications.NotificationCustomizationOptions;
import com.salesforce.marketingcloud.notifications.NotificationManager;
import com.salesforce.marketingcloud.notifications.NotificationMessage;
import com.salesforce.marketingcloud.registration.Registration;
import com.salesforce.marketingcloud.registration.RegistrationManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * LearningAppApplication is the primary application class.
 * This class extends Application to provide global activities.
 * <p/>
 *
 * @author Salesforce &reg; 2017.
 */
public class LearningAppApplication extends Application implements MarketingCloudSdk.InitializationListener,
        RegistrationManager.RegistrationEventListener, RegionMessageManager.GeofenceMessageResponseListener,
        RegionMessageManager.ProximityMessageResponseListener, NotificationManager.NotificationBuilder {

    /**
     * Set to true to show how Salesforce analytics will save statistics for
     * how your customers use the app.
     */
    public static final boolean ANALYTICS_ENABLED = true;
    /**
     * Set to true to test how notifications can send your app customers to
     * different web pages.
     */
    public static final boolean INBOX_ENABLED = true;
    /**
     * Set to true to show how Predictive Intelligence analytics (PIAnalytics) will
     * save statistics for how your customers use the app (by invitation at this point).
     */
    public static final boolean PI_ENABLED = true;
    /**
     * Set to true to show how beacons messages works within the SDK.
     */
    public static final boolean PROXIMITY_ENABLED = false;
    /**
     * Set to true to show how geo fencing works within the SDK.
     */
    public static final boolean LOCATION_ENABLED = true;
    public static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    private static final String TAG = "~#LearningApp";
    private SharedPreferences sharedPreferences;
    private String lastBeaconReceivedEventDatetime = "";

    public String getLastBeaconReceivedEventDatetime() {
        if (TextUtils.isEmpty(lastBeaconReceivedEventDatetime)) {
            lastBeaconReceivedEventDatetime = sharedPreferences.getString("lastBeaconReceivedEventDatetime", "Last Event Datetime Not Available");
        }
        return lastBeaconReceivedEventDatetime;
    }

    /**
     * The onCreate() method initialize your app.
     * <p/>
     * In {@link MarketingCloudConfig.Builder} you must set several parameters:
     * <ul>
     * <li>
     * AppId and AccessToken: these values are taken from the Marketing Cloud definition for your app.
     * </li>
     * <li>
     * SenderID for the push notifications: this value is taken from the Google API console.
     * </li>
     * <li>
     * You also set whether you enable LocationManager, CloudPages, and Analytics.
     * </li>
     * </ul>
     **/
    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("AndroidLearningApp", Context.MODE_PRIVATE);

        /** Register to receive push notifications. */
        MarketingCloudSdk.setLogLevel(BuildConfig.DEBUG ? Log.VERBOSE : Log.ERROR);
        MarketingCloudSdk.setLogListener(new MCLogListener.AndroidLogListener());
        MarketingCloudSdk.init(this, MarketingCloudConfig.builder() //

                // PROVIDE YOUR APPLICATION'S VALUES
                .setApplicationId() // ENTER YOUR MARKETING CLOUD APPLICATION ID HERE
                .setAccessToken() // ENTER YOUR MARKETING CLOUD ACCESS TOKEN HERE
                .setSenderId() // ENTER YOUR GOOGLE SENDER ID HERE
                .setMarketingCloudServerUrl() // ENTER YOUR MARKETING CLOUD URL

                // ENABLE MARKETING CLOUD FEATURES
                .setAnalyticsEnabled(ANALYTICS_ENABLED)
                .setPiAnalyticsEnabled(PI_ENABLED)
                .setInboxEnabled(INBOX_ENABLED)
                .setGeofencingEnabled(LOCATION_ENABLED)
                .setProximityEnabled(PROXIMITY_ENABLED)

                .setNotificationCustomizationOptions(NotificationCustomizationOptions.create(R.drawable.ic_stat_app_logo_transparent))
                .build(this), this);

        MarketingCloudSdk.requestSdk(new MarketingCloudSdk.WhenReadyListener() {
            @Override
            public void ready(MarketingCloudSdk sdk) {
                sdk.getRegistrationManager().registerForRegistrationEvents(LearningAppApplication.this);
                sdk.getRegionMessageManager().registerGeofenceMessageResponseListener(LearningAppApplication.this);
                sdk.getRegionMessageManager().registerProximityMessageResponseListener(LearningAppApplication.this);
            }
        });
    }

    /**
     * Called when sdk initialization has completed.
     * <p/>
     * When the SDK initialization is completed.
     */
    @Override
    public void complete(InitializationStatus status) {
        if (!status.isUsable()) {
            Log.e(TAG, "Marketing Cloud Sdk init failed.", status.unrecoverableException());
        } else {
            MarketingCloudSdk cloudSdk = MarketingCloudSdk.getInstance();
            cloudSdk.getAnalyticsManager().trackPageView("data://ReadyAimFireCompleted", "Marketing Cloud SDK Initialization Complete");

            if (status.locationsError()) {
                final GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                Log.i(TAG, String.format(Locale.ENGLISH, "Google Play Services Availability: %s", googleApiAvailability.getErrorString(status.playServicesStatus())));
                if (googleApiAvailability.isUserResolvableError(status.playServicesStatus())) {
                    googleApiAvailability.showErrorNotification(LearningAppApplication.this, status.playServicesStatus());
                }
            }
        }
    }


    @Override
    public NotificationCompat.Builder setupNotificationBuilder(@NonNull Context context, @NonNull NotificationMessage notificationMessage) {
        NotificationCompat.Builder builder = NotificationManager.getDefaultNotificationBuilder(context, notificationMessage, NotificationManager.createDefaultNotificationChannel(context), R.drawable.ic_stat_app_logo_transparent);

        Map<String, String> customKeys = notificationMessage.customKeys();
        if (!customKeys.containsKey("category") || !customKeys.containsKey("sale_date")) {
            return builder;
        }

        if ("sale".equalsIgnoreCase(customKeys.get("category"))) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            try {
                Date saleDate = simpleDateFormat.parse(customKeys.get("sale_date"));
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, saleDate.getTime())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, saleDate.getTime())
                        .putExtra(CalendarContract.Events.TITLE, customKeys.get("event_title"))
                        .putExtra(CalendarContract.Events.DESCRIPTION, customKeys.get("alert"))
                        .putExtra(CalendarContract.Events.HAS_ALARM, 1)
                        .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, R.id.interactive_notification_reminder, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.addAction(android.R.drawable.ic_menu_my_calendar, getString(R.string.in_btn_add_reminder), pendingIntent);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return builder;
    }

    /**
     * Listens for Registrations
     * <p/>
     * This method is one of several methods to log notifications when an event occurs in the SDK.
     * Different attributes indicate which event has occurred.
     * <p/>
     * RegistrationEvent will be triggered when the SDK receives the response from the
     * registration as triggered by the com.google.android.c2dm.intent.REGISTRATION intent.
     */
    @Override
    public void onRegistrationReceived(@NonNull Registration registration) {
        MarketingCloudSdk.getInstance().getAnalyticsManager().trackPageView("data://RegistrationEvent", "Registration Event Completed");
        Log.d(TAG, registration.toString());
        Log.d(TAG, String.format("Last sent: %1$d", System.currentTimeMillis()));
    }

    /**
     * Listens for a GeofenceResponses.
     * <p/>
     * This event retrieves the data related to geolocations
     * and saves them as a list of MCGeofence in MCLocationManager
     */
    @Override
    public void onGeofenceMessageResponse(GeofenceMessageResponse response) {
        MarketingCloudSdk.getInstance().getAnalyticsManager().trackPageView("data://GeofenceResponseEvent", "Geofence Response Event Received");
        List<Region> regions = response.fences();
        for (Region r : regions) {
            MCGeofence newLocation = new MCGeofence();
            LatLng latLng = new LatLng(r.center().latitude(), r.center().longitude());
            newLocation.setCoordenates(latLng);
            newLocation.setRadius(r.radius());
            newLocation.setName(r.name());
            MCLocationManager.getInstance().getGeofences().add(newLocation);
        }
    }

    /**
     * Listens for a ProximityMessage responses.
     * <p/>
     * This event retrieves the data related to beacon messages and saves them
     * as a list of MCBeacon in MCLocationManager.
     * <p/>
     */
    @Override
    public void onProximityMessageResponse(ProximityMessageResponse response) {
        MarketingCloudSdk.getInstance().getAnalyticsManager().trackPageView("data://BeaconResponse", "Beacon Response Event Received");
        List<Region> regions = response.beacons();
        for (Region r : regions) {
            MCBeacon newBeacon = new MCBeacon();
            LatLng latLng = new LatLng(r.center().latitude(), r.center().longitude());
            newBeacon.setCoordenates(latLng);
            newBeacon.setRadius(getResources().getInteger(R.integer.beacon_radius));
            newBeacon.setName(r.name());
            newBeacon.setGuid(r.proximityUuid());
            MCLocationManager.getInstance().getBeacons().add(newBeacon);
            lastBeaconReceivedEventDatetime = timestampFormat.format(new Date(System.currentTimeMillis()));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lastBeaconReceivedEventDatetime", lastBeaconReceivedEventDatetime);
        }
    }
}
