/*
 * KJK_TALK APIDEMOS: App-> Service-> Local Service Controller-> LocalService.java
 * KJK_TALK APIDEMOS: App-> Service-> Local Service Binding-> LocalService.java

 * ���� class�� Service class�� Act�� �ٸ��� UI�� ���� �ʴ´� 
 * Act�� ������ �ϳ��� handler�ȿ��� �����ϰ� �ȴ�. 
 * � ���񽺳� �ϸ�, ���۰� ���ÿ� status bar�� service�� ���۵Ǿ����� 
 * �˸���, ����Ǹ� toast�� ��� �˷��ִ� ����� �ϰ� �ȴ�
 * 
 * � apk�� service�� ������ �ִ��� �ƴ��� �Ǻ��Ϸ��� adb shell dumpsys activity services ������� Ȯ���� �����ϴ�.

 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

/**
 * This is an example of implementing an application service that runs locally
 * in the same process as the application.  The {@link LocalServiceActivities.Controller}
 * and {@link LocalServiceActivities.Binding} classes show how to interact with the
 * service.
 *
 * <p>Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service.  This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */
//BEGIN_INCLUDE(service)
public class LocalService extends Service {
    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        //onBind���� service�� ����������connect �Ǿ����� ȣ��ǰ� �ȴ�.
        LocalService getService() {
            return LocalService.this;
        }
    }
    
    @Override
    public void onCreate() {
        //KJK_TALK SYSTEM SERVICE: getSystemService�� �̿��Ͽ� ��� ���
/*
        WINDOW_SERVICE ("window"):
            The top-level window manager in which you can place custom windows. The returned object is a android.view.WindowManager. 
        LAYOUT_INFLATER_SERVICE ("layout_inflater") 
            A android.view.LayoutInflater for inflating layout resources in this context. 
        ACTIVITY_SERVICE ("activity") 
            A android.app.ActivityManager for interacting with the global activity state of the system. 
        POWER_SERVICE ("power") 
            A android.os.PowerManager for controlling power management. 
        ALARM_SERVICE ("alarm") 
            A android.app.AlarmManager for receiving intents at the time of your choosing. 
        NOTIFICATION_SERVICE ("notification") 
            A android.app.NotificationManager for informing the user of background events. 
        KEYGUARD_SERVICE ("keyguard") 
            A android.app.KeyguardManager for controlling keyguard. 
        LOCATION_SERVICE ("location") 
            A android.location.LocationManager for controlling location (e.g., GPS) updates. 
        SEARCH_SERVICE ("search") 
            A android.app.SearchManager for handling search. 
        VIBRATOR_SERVICE ("vibrator") 
            A android.os.Vibrator for interacting with the vibrator hardware. 
        CONNECTIVITY_SERVICE ("connection") 
            A ConnectivityManager for handling management of network connections. 
        WIFI_SERVICE ("wifi") 
            A WifiManager for management of Wi-Fi connectivity. 
        INPUT_METHOD_SERVICE ("input_method") 
            An InputMethodManager for management of input methods. 
        UI_MODE_SERVICE ("uimode") 
            An android.app.UiModeManager for controlling UI modes. 
*/
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        //KJK_TALK: status bar�� ����� NOTI�� ����Ѵ�. 
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped. KJK_TALK: message box ���: Context, ���ڿ�, ��� duration  
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    //KJK_TALK: LocalServiceBinding.java���� bindService method�� ȣ���Ҷ� 
    //�� class�� onBind�� ȣ��ǰ� �ȴ�. �̶� ���� ���� class�� instanceȭ ���� ���� ���¶��
    //Framework���� LocalService�� �����ڸ� �̿��� ������ onBind�� ȣ���ϰ� �ȴ�. 
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        //KJK_TALK: getText�� context class�� ����, 
        CharSequence text = getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LocalServiceActivities.Controller.class), 0);

        // Set the info for the views that show in the notification panel.
        // notification panel�� ������ ��������.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.stat_sample)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.local_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        //KJK_TALK: status bar�� ������ NOTI�� ����Ѵ�. 
        mNM.notify(NOTIFICATION, notification);
    }
}
//END_INCLUDE(service)
