![Marketing Cloud](imgReadMe/marketing_cloud_logo.png)

# README

>Marketing Cloud Learning Apps are free to use but are not official Salesforce.com Marketing Cloud products and should be considered community projects--these apps are not officially tested or documented. For help on any Marketing Cloud Learning App please consult the Salesforce message boards or the issues section of this repository. Salesforce Marketing Cloud support is not available for these applications.

<a name="0001"></a>
# About

This project provides a template for creating a mobile app (Android or iOS) that uses the Journey Builder for Apps SDK.  It is also a UI for exploring its features and provides a mechanism to collect and send debugging information to learn about the workings of the SDK as you explore.

#Get Started

###Provision your Learning App with Google

Provisioning ensures that users receive push messages from your app. The first step in provisioning is to [retrieve the Legacy server key and Sender ID for your app](http://salesforce-marketingcloud.github.io/JB4A-SDK-Android/provisioning/google.html) so that you can put this information into Marketing Cloud.

###Create your app in the App Center

In order to connect your iOS app to the Marketing Cloud, you must first [create a MobilePush app in the App Center](http://salesforce-marketingcloud.github.io/JB4A-SDK-Android/create-apps/create-apps-app-center.html). App Center represents the central development console for using Fuel APIs and building Marketing Cloud apps.

###Place the App Center Credentials in your Application
On Android, we utilize a 'secrets.xml' file to contain the application credntials given by the App Center above. Inside the secrets.cml file, set the following:

**secrets.xml**

1. `app_id`: the App ID for your development app as defined in the App Center section of the Marketing Cloud.

2. `gcm_sender_id`: the Google Cloud Messaging ID as defined in the Google Cloud Developers Console for your app.

3. `access_token`: the Access Token for your development app as defined in the App Center section of the Marketing Cloud.

> Note: You can use different keys for the staging/testing phase and the production phase.  Staging/testing keys are indicated by the prefix `staging_`. By default, the Learning App will utilize the `staging_ app_id`, `access_token`, and `gcm_sender_id`.

# Resources

For more information, check out our [developer documentation](http://salesforce-marketingcloud.github.io/JB4A-SDK-Android/).
