/*
 * KJK_TALK APIDEMOS: App-> Alarm-> Alarm Controller
 * AlaramController�� OneShotTimer�� RepeatTimer BroadCast Receiver�� ����ϴµ�
 * �� ��� ��  �ٸ� process���� ��� �����ϰ� �����ϰ� �ȴ�. RepeatTimer���� ���
 * ��ϵ� Alarm�� SystemServer�� ���� �����ǰ� �׶����� RepeatTimer�� ���۽�Ű�� �ǹǷ� 
 * �ش� process�� ������ ���̴��� SystemServer�� ���� ���� �����Ǿ� ����ǰ� �ȴ�.

   KJK_TALK PENDING INTENT: 
   �Ϲ� intent�� �ٸ��� �� 3�ڿ��� intent�� ��� �����޶�� �Ҷ� ����ϴ� intents��.
   �׷��Ƿ� ��� �� 3�ڰ� ������ �ִٰ� �����ϰ� �ȴ�.
   �Ʒ��� ���� ��쿡�� AlarmManager�� PendingIntent�� ��ź����� �ȴ�. 
   

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

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Example of scheduling one-shot and repeating alarms.  See
 * {@link OneShotAlarm} for the code run when the one-shot alarm goes off, and
 * {@link RepeatingAlarm} for the code run when the repeating alarm goes off.
 * <h4>Demo</h4>
App/Service/Alarm Controller
 
<h4>Source files</h4>
<table class="LinkTable">
        <tr>
            <td class="LinkColumn">src/com.example.android.apis/app/AlarmController.java</td>
            <td class="DescrColumn">The activity that lets you schedule alarms</td>
        </tr>
        <tr>
            <td class="LinkColumn">src/com.example.android.apis/app/OneShotAlarm.java</td>
            <td class="DescrColumn">This is an intent receiver that executes when the
                one-shot alarm goes off</td>
        </tr>
        <tr>
            <td class="LinkColumn">src/com.example.android.apis/app/RepeatingAlarm.java</td>
            <td class="DescrColumn">This is an intent receiver that executes when the
                repeating alarm goes off</td>
        </tr>
        <tr>
            <td class="LinkColumn">/res/any/layout/alarm_controller.xml</td>
            <td class="DescrColumn">Defines contents of the screen</td>
        </tr>
</table>

 */
public class AlarmController extends Activity {
    Toast mToast;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alarm_controller);

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.one_shot);
        button.setOnClickListener(mOneShotListener);
        button = (Button)findViewById(R.id.start_repeating);
        button.setOnClickListener(mStartRepeatingListener);
        button = (Button)findViewById(R.id.stop_repeating);
        button.setOnClickListener(mStopRepeatingListener);
    }

    private OnClickListener mOneShotListener = new OnClickListener() {
        public void onClick(View v) {
            // When the alarm goes off, we want to broadcast an Intent to our
            // BroadcastReceiver.  Here we make an Intent with an explicit class
            // name to have our own receiver (which has been published in
            // AndroidManifest.xml) instantiated and called, and then create an
            // IntentSender to have the intent executed as a broadcast.
            //KJK_TALK: alarm�� �����ؾ��� ������ intent�� �����.OneShotAlarm:remote�� �����Ѵ�.
            Intent intent = new Intent(AlarmController.this, OneShotAlarm.class);

            //broadcast�� ���� intent�� ȹ���Ѵ�.
            PendingIntent sender = PendingIntent.getBroadcast(AlarmController.this,
                    0, intent, 0);

            // We want the alarm to go off 5 seconds from now.
            Calendar calendar = Calendar.getInstance();
            // setTimeInMillis�� calender�� ������ ���� �ð������� �������� �Լ�.
            calendar.setTimeInMillis(System.currentTimeMillis());//���� �ð��� �����Ͽ� �����´�.
            calendar.add(Calendar.SECOND, 5);//����ð��� 5�� ���� �ð��� ���� �缳���Ѵ�. ���� �̽ð����� alarm�� ȣ���Ѵ�.
            //���⼭�� calendar�� ����Ͽ� time format���� �˶��� �����Ͽ���.
            // Schedule the alarm!, �˶�����
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            //5������ �ð��� �˶��� �����ǰ�, alarm�� sender�� ȣ��ǵ��� pending intent�� ������ �ȴ�.
            // ���� pending intents�� OneShotAlarm remote process�� ����� �ȴ�.
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

            // Tell the user about what we did.
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(AlarmController.this, R.string.one_shot_scheduled,
                    Toast.LENGTH_LONG);
            mToast.show();
        }
    };

    private OnClickListener mStartRepeatingListener = new OnClickListener() {
        public void onClick(View v) {
            // When the alarm goes off, we want to broadcast an Intent to our
            // BroadcastReceiver.  Here we make an Intent with an explicit class
            // name to have our own receiver (which has been published in
            // AndroidManifest.xml) instantiated and called, and then create an
            // IntentSender to have the intent executed as a broadcast.
            // Note that unlike above, this IntentSender is configured to
            // allow itself to be sent multiple times.
            Intent intent = new Intent(AlarmController.this, RepeatingAlarm.class);
            PendingIntent sender = PendingIntent.getBroadcast(AlarmController.this,
                    0, intent, 0);
            
            // We want the alarm to go off 30 seconds from now.
            long firstTime = SystemClock.elapsedRealtime();//booting�� ���� ������ �ð� ���ϱ�
            firstTime += 5*1000;
            //���⼭�� long type�� ����Ͽ� long format���� �˶��� �����Ͽ���.
            // Schedule the alarm!
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            firstTime, 5*1000, sender);//KJK_TALK: RepeatingAlarm:remote�� �����Ѵ�.

            // Tell the user about what we did.
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(AlarmController.this, R.string.repeating_scheduled,
                    Toast.LENGTH_LONG);
            mToast.show();
        }
    };

    private OnClickListener mStopRepeatingListener = new OnClickListener() {
        public void onClick(View v) {
            // Create the same intent, and thus a matching IntentSender, for
            // the one that was scheduled.
            Intent intent = new Intent(AlarmController.this, RepeatingAlarm.class);
            PendingIntent sender = PendingIntent.getBroadcast(AlarmController.this,
                    0, intent, 0);
            
            // And cancel the alarm. �˶� ���񽺿��� ���� ����� ������.
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);

            //IAlarmManager.java�� remove method�� ���Ͽ� mRemote.transact(Stub.TRANSACTION_remove, _data, _reply, 0); ȣ��
            am.cancel(sender);

            // Tell the user about what we did.
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(AlarmController.this, R.string.repeating_unscheduled,
                    Toast.LENGTH_LONG);
            mToast.show();
        }
    };
}

