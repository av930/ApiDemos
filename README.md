# ApiDemos

## This project is for verifying compile environments in Android Studio, IntellJ and Eclipse which are major editors in Android world.

Android Studio is a fork of Intellij and add its own feature on it fit to Android.
I installed IntelliJ Communitiy version for this project.
Originally, I learn a language within VI editor, Starting from VI, one of my biggest hobby is switching IDE from one to others.
please refer : https://github.com/av930/Android_VIM, if you have a favor VIM developing android.

Anyway, In this project, I use Android sample app "ApiDemos" and check if they can be executed in real device. 

Please refer my work. 

# Editor Version 
1. Android Studio with Gradle : version 3.1.3 
2. IntelliJ Communitiy Version without Gradle : IntelliJ 2017.3.5 (Community Edition)
3. Eclipse without Gradle (supporting Android is deprecated) : Version: Photon Release (4.8.0)

# android Version 
* buildToolVersion="28" //build tool version, PeanutButter, common in all 
* mpileSdkVersion="26" //version of android.jar, Oreo, common in all 
* nSdkVersion="16" //in AndroidManifest.xml, JellyBean, AndroidStudio & IntelliJ 
* rgetSdkVersion="23" //in AndroidManifest.xml, Marshmallow, common in all 

# Exclude files
* com/example/android/apis/app/PrintBitmap.java (included in AndroidStudio)
* com/example/android/apis/os/MmsMessagingDemo.java
* com/example/android/apis/os/MmsWapPushReceiver.java

# Exception
In Eclipse, There are a lot of strict error for minSdkVersion and targetSdkVersion
android:minSdkVersion="23" android:targetSdkVersion="23"

# TodoList
* add Android Support Library in IntelliJ & Eclipse

# reference for Eclipse
+ https://github.com/dandar3
+ https://dandar3.blogspot.com/search/label/Android

