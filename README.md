![Marketing Cloud](imgReadMe/marketing_cloud_logo.png)

# README

>Marketing Cloud Learning Apps are free to use but are not official Salesforce Marketing Cloud products and should be considered community projects.  These apps are not officially tested or documented. For help on any Marketing Cloud Learning App, consult the Salesforce message boards or the issues section of this repository. Salesforce Marketing Cloud support is not available for these applications.

<a name="0001"></a>
# About

This project provides a template for creating a mobile app (Android) that uses the Journey Builder for Apps SDK.  It is also a UI for exploring the SDK's features and provides a mechanism to collect and send debugging information to learn about the workings of the SDK as you explore.

#Get Started

###Provision your Learning App with Google

Provisioning ensures that users receive push messages from your app. The first step in provisioning is to [retrieve the Legacy server key and Sender ID for your app](http://salesforce-marketingcloud.github.io/JB4A-SDK-Android/provisioning/google.html) so that you can put this information into Marketing Cloud.

###Create your Application in App Center

To connect your Android app to Marketing Cloud, [create a MobilePush app in App Center](http://salesforce-marketingcloud.github.io/JB4A-SDK-Android/create-apps/create-apps-app-center.html). App Center is the central development console for using Fuel APIs and building Marketing Cloud apps.

###Add App Center Credentials to your Application
In the Application class you will find the SDK's initialization code.  Set your Application ID, Access Token and Sender ID as well as any features you want to enable.

# Resources

For more information, check out our [developer documentation](http://salesforce-marketingcloud.github.io/JB4A-SDK-Android/).
