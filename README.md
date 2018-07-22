# ApiDemos

## This project is for verifying compile environments in Android Studio, IntellJ and Eclipse which are major editors in Android world.

Android Studio is a fork of Intellij and add its own features on it to fit to Android.
I used IntelliJ Communitiy version for this project.
In this project, I use Android sample app "ApiDemos" and only check basic things, compiling and running.
Please refer my work. 


### Editor Version 
1. Android Studio with Gradle : Version 3.1.3 
2. IntelliJ Communitiy Version without Gradle : IntelliJ 2017.3.5 (Community Edition)
3. Eclipse without Gradle (supporting Android is deprecated) : Version: Photon Release (4.8.0)


### Android Version 
* buildToolVersion="28" //build tool version, PeanutButter, common in all 
* commpileSdkVersion="26" //version of android.jar, Oreo, common in all 
* minSdkVersion="16" //in AndroidManifest.xml, JellyBean, AndroidStudio & IntelliJ 
* targetSdkVersion="23" //in AndroidManifest.xml, Marshmallow, common in all 


### ApiDemos Version
it is from tag android-8.1.0_r32 from full source.
your refer commit 2017.12.19. master in https://github.com/aosp-mirror/platform_development
* com/example/android/apis/app/PrintBitmap.java (included in AndroidStudio)
* com/example/android/apis/os/MmsMessagingDemo.java
* com/example/android/apis/os/MmsWapPushReceiver.java


### Exception
In Eclipse, There are a lot of strict error for minSdkVersion and targetSdkVersion
android:minSdkVersion="23" android:targetSdkVersion="23"


### TodoList
* add Android Support Library in IntelliJ & Eclipse


### reference for Eclipse
* https://github.com/dandar3
* https://dandar3.blogspot.com/search/label/Android

