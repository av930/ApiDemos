/*
 * KJK_TALK APIDEMOS: App-> Service-> Foreground Service Controller
 * 사용자 interaction이 있는 UI를 가진 foreground service와 사용자 interaction이 없는 background service를 만드는 예제로
 * foreground service를 만드는 목적은 service priority 때문에 service가 강제 종료되지 말아야할 서비스(notification 출력) 
 * 생성을 위해서 이다. priority는 foreground>visible>service>background>empty 로 나뉘게 되는데, 기본적으로 service는
 * service 등급을 가지게 되나, 명시적으로 memory reclaim시 해당 service가 kill되지 않도록 명시적으로 priority를 foreground로
 * 변경해주기 위한 API이다. 이때 home을 눌러 api demos가 보이지않게 되면, visible로 변경되고 보이면 foreground로 되게 된다.
 * 
 * 
 * 
 * 추가적으로 여기서 파악해야 할것은 없어진 method를 호출하는 방법으로, getClass().getMethod("method 이름", parameter type)
 * 형식으로 호출이 가능하다. 이를 reflection(리플렉션) 기법이라고 한다.

 * Copyright (C) 2009 The Android Open Source Project
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

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

/**
 * This is an example of implementing an application service that can
 * run in the "foreground".  It shows how to code this to work well by using
 * the improved Android 2.0 APIs when available and otherwise falling back
 * to the original APIs.  Yes: you can take this exact code, compile it
 * against the Android 2.0 SDK, and it will run against everything down to
 * Android 1.0.
 */
public class ForegroundService extends Service {
    static final String ACTION_FOREGROUND = "com.example.android.apis.FOREGROUND";
    static final String ACTION_FOREGROUND_WAKELOCK = "com.example.android.apis.FOREGROUND_WAKELOCK";
    static final String ACTION_BACKGROUND = "com.example.android.apis.BACKGROUND";
    static final String ACTION_BACKGROUND_WAKELOCK = "com.example.android.apis.BACKGROUND_WAKELOCK";

 // BEGIN_INCLUDE(foreground_compatibility)
    private static final Class<?>[] mSetForegroundSignature = new Class[] {
        boolean.class};
    private static final Class<?>[] mStartForegroundSignature = new Class[] {
        int.class, Notification.class};
    private static final Class<?>[] mStopForegroundSignature = new Class[] {
        boolean.class};
    
    private NotificationManager mNM;
    private Method mSetForeground;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mSetForegroundArgs = new Object[1];
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];
    
    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        }
    }
    
    /**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat(int id, Notification notification) {
        // If we have the new startForeground API, then use it.
        //KJK_TALK: onCreate에서 startForeground가 존재하면 해당 method를 설정하고, 존재하지 않으면 null로 설정한다.
        //따라서 존재하지 않을 경우 1.6이하인 경우 setForeground method가 실행되게 된다.
        if (mStartForeground != null) {
            mStartForegroundArgs[0] = Integer.valueOf(id);
            mStartForegroundArgs[1] = notification;
                //forground로 만드는 API
            invokeMethod(mStartForeground, mStartForegroundArgs);
            return;
        }
        
        // Fall back on the old API.
        mSetForegroundArgs[0] = Boolean.TRUE;
        invokeMethod(mSetForeground, mSetForegroundArgs);
        mNM.notify(id, notification);
    }
    
    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     */
    void stopForegroundCompat(int id) {
        // If we have the new stopForeground API, then use it.
        if (mStopForeground != null) {
            mStopForegroundArgs[0] = Boolean.TRUE;
                //ForeGround Service를 stop 시키는 API
            invokeMethod(mStopForeground, mStopForegroundArgs);
            return;
        }
        
        // Fall back on the old API.  Note to cancel BEFORE changing the
        // foreground state, since we could be killed at that point.
        mNM.cancel(id);
        mSetForegroundArgs[0] = Boolean.FALSE;
        invokeMethod(mSetForeground, mSetForegroundArgs);
    }
    
    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        try {
            //KJK_TALK: foreground method에 startForeground 함수를 mapping한다.
            //startForeground함수가 실제로 해당 service를 forground로 만들어주는 함수
            //v2.0이전에는 이것이 setForeground 함수였다.
            //Signature는 실행시킬때 넣을 parameter를 말한다. 
            
            mStartForeground = getClass().getMethod("startForeground",
                    mStartForegroundSignature);
            mStopForeground = getClass().getMethod("stopForeground",
                    mStopForegroundSignature);
            return;
        } catch (NoSuchMethodException e) {
            // Running on an older platform.
            mStartForeground = mStopForeground = null;
        }
        try {
            mSetForeground = getClass().getMethod("setForeground",
                    mSetForegroundSignature);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "OS doesn't have Service.startForeground OR Service.setForeground!");
        }
    }

    @Override
    public void onDestroy() {
        handleDestroy();
        // Make sure our notification is gone.
        stopForegroundCompat(R.string.foreground_service_started);
    }
// END_INCLUDE(foreground_compatibility)

// BEGIN_INCLUDE(start_compatibility)
    // This is the old onStart method that will be called on the pre-2.0
    // platform.  On 2.0 or later we override onStartCommand() so this
    // method will not be called.
    // KJK_TALK: version 2.0 이후에는 onStart 대신 onStartCommand가 호출된다.따라서 onStartCommand는 onCreate이후에 호출된다.
    @Override
    public void onStart(Intent intent, int startId) {
        handleCommand(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        // KJK_TALK onStartCommand는 flag값에 대해서 반환해줘야 한다. 
        return START_STICKY;
    }
// END_INCLUDE(start_compatibility)

    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler();
    private Runnable mPulser = new Runnable() {
        @Override public void run() {
            Log.i("ForegroundService", "PULSE!");
            mHandler.postDelayed(this, 5*1000);
        }
    };

    void handleCommand(Intent intent) {
        //KJK_TALK: intent.getAction으로 현재 service를 어떤 intent action으로 실행했는지 알아낸다.
        if (ACTION_FOREGROUND.equals(intent.getAction())  // ForeGround일 경우
                || ACTION_FOREGROUND_WAKELOCK.equals(intent.getAction())) {
            // In this sample, we'll use the same text for the ticker and the expanded notification
            CharSequence text = getText(R.string.foreground_service_started);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, Controller.class), 0);

            // Set the info for the views that show in the notification panel.
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.stat_sample)  // the status icon
                    .setTicker(text)  // the status text
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentTitle(getText(R.string.alarm_service_label))  // the label
                    .setContentText(text)  // the contents of the entry
                    .setContentIntent(contentIntent)  // The intent to send when clicked
                    .build();

            startForegroundCompat(R.string.foreground_service_started, notification);
            
        } else if (ACTION_BACKGROUND.equals(intent.getAction()) //BackGround일경우 
                || ACTION_BACKGROUND_WAKELOCK.equals(intent.getAction())) {
            stopForegroundCompat(R.string.foreground_service_started);
        }

        if (ACTION_FOREGROUND_WAKELOCK.equals(intent.getAction())
                || ACTION_BACKGROUND_WAKELOCK.equals(intent.getAction())) {
            if (mWakeLock == null) {
                mWakeLock = getSystemService(PowerManager.class).newWakeLock(
                        PowerManager.PARTIAL_WAKE_LOCK, "wake-service");
                mWakeLock.acquire();
            } else {
                releaseWakeLock();
            }
        }

        mHandler.removeCallbacks(mPulser);
        mPulser.run();
    }

    void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    void handleDestroy() {
        releaseWakeLock();
        mHandler.removeCallbacks(mPulser);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    // ----------------------------------------------------------------------

    /**
     * <p>Example of explicitly starting and stopping the {@link ForegroundService}.
     * 
     * <p>Note that this is implemented as an inner class only keep the sample
     * all together; typically this code would appear in some separate class.
     */
    @com.example.android.apis.ActivityIsReviewed(sequence=1.0)
    public static class Controller extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.foreground_service_controller);

            // Watch for button clicks.
            Button button = (Button)findViewById(R.id.start_foreground);
            button.setOnClickListener(mForegroundListener);
            button = (Button)findViewById(R.id.start_foreground_wakelock);
            button.setOnClickListener(mForegroundWakelockListener);
            button = (Button)findViewById(R.id.start_background);
            button.setOnClickListener(mBackgroundListener);
            button = (Button)findViewById(R.id.start_background_wakelock);
            button.setOnClickListener(mBackgroundWakelockListener);
            button = (Button)findViewById(R.id.stop);
            button.setOnClickListener(mStopListener);
            button = (Button)findViewById(R.id.start_foreground_2);
            button.setOnClickListener(mForegroundListener2);
            button = (Button)findViewById(R.id.stop_2);
            button.setOnClickListener(mStopListener2);
        }

        private OnClickListener mForegroundListener = new OnClickListener() {
            public void onClick(View v) {//KJK_TALK: service를 forground로 실행시킨다. UI(notification)를 가진 서비스
                Intent intent = new Intent(ForegroundService.ACTION_FOREGROUND);
                intent.setClass(Controller.this, ForegroundService.class);
                startService(intent);
            }
        };

        private OnClickListener mForegroundWakelockListener = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ForegroundService.ACTION_FOREGROUND_WAKELOCK);
                intent.setClass(Controller.this, ForegroundService.class);
                startService(intent);
            }
        };

        private OnClickListener mBackgroundListener = new OnClickListener() {
            public void onClick(View v) {//KJK_TALK: service를 forground로 실행시킨다. UI가 없는 서비스
                Intent intent = new Intent(ForegroundService.ACTION_BACKGROUND);
                intent.setClass(Controller.this, ForegroundService.class);
                startService(intent);
            }
        };

        private OnClickListener mBackgroundWakelockListener = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ForegroundService.ACTION_BACKGROUND_WAKELOCK);
                intent.setClass(Controller.this, ForegroundService.class);
                startService(intent);
            }
        };

        private OnClickListener mStopListener = new OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(Controller.this,
                        ForegroundService.class));
            }
        };

        private OnClickListener mForegroundListener2 = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ForegroundService.ACTION_FOREGROUND);
                intent.setClass(Controller.this, ForegroundService2.class);
                startService(intent);
            }
        };

        private OnClickListener mStopListener2 = new OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(Controller.this,
                        ForegroundService2.class));
            }
        };

    }
}
