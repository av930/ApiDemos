/*
 * KJK_TALK APIDEMOS: App-> Preferences-> 2. Launching Preferences-> AdvancedPreferences
 * ����A
 * 2. Launching Preferences���� button�� click�ϱ����� TextView�� Counter ���� ��µȴ�.
 * �̶� default ���� �������� ���ؼ� PreferenceManager.setDefaultValues(R.xml.advanced_preferences)�� 
 * ���ְ� �Ǹ� advanced_preferences.xml �� ���ǵ� �׸��� access�Ҽ� �ְ� �Ǿ� �ʱⰪ�� �����ü� �ְ� �ȴ�.
 * ��, preference activity�� �����ϱ����� �̸� preference���� �����ü� �ִ°��̴� 
 *����B 
 * ���� ���� onCreate���� advanced_preferences.xml�� inplate�Ͽ� ������� ���� �����ְԵǰ�
 * ���⼭ preference �� Textview�� checkbox�� �޸� list�� �����ְ� �Ǵµ�, 
 * checkbox ��� advanced_preferences.xml���� �ش� field�� �о�� code���� �ֱ�������
 * checkbox�� en/disable �Ѵ�.

 *   KJK_TALK APIDEMOS: App-> Preferences-> 6. AdvancedPreferences ����B�� �ϰ� �ȴ�.    
 
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

package com.example.android.apis.preference;

import com.example.android.apis.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.CheckBoxPreference;
import android.widget.Toast;

/**
 * Example that shows finding a preference from the hierarchy and a custom preference type.
 */// KJK_TALK: Preference�� �о���� ���� PreferenceActivity�� ����ߴ�.
 // ���⿡ preference�� �����Ҷ����� ȣ���ϱ����� OnSharedPreferenceChangeListener�� �޾Ҵ�.
public class AdvancedPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    public static final String KEY_MY_PREFERENCE = "my_preference";
    public static final String KEY_ADVANCED_CHECKBOX_PREFERENCE = "advanced_checkbox_preference";

    private CheckBoxPreference mCheckBoxPreference;
    //KJK_CHECK: Handler: Handler�� ������ٴ� ���� ���� msg�� ���� handler�� ������ �ΰڴٴ� ��!!
    // �̷��� ������� handler�� ���� process�� Looper�� �ڵ������� ����ȴ�.
    private Handler mHandler = new Handler();

    /**
     * This is a simple example of controlling a preference from code.
     */
    private Runnable mForceCheckBoxRunnable = new Runnable() {
        public void run() {
            if (mCheckBoxPreference != null) {
                //�ֱ������� checkbox�� check/uncheck�Ѵ�.
                mCheckBoxPreference.setChecked(!mCheckBoxPreference.isChecked());
            }

            // Force toggle again in a second
            // KJK_TALK: ���� runnable�� mHandler�� �����ϴ� thread(���� thread)���� �ش� �ð� ���Ŀ� 
            // msg.callback���� ��ϵǾ� thread�� �ƴ� �Ϲ� �Լ� call�� �����ϵ��� �����.
            // �� thread�� �������� �ʰ� msg handler�� �����Ѵ�.
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the XML preferences file, preference.xml�� �о��
        addPreferencesFromResource(R.xml.advanced_preferences);

        // Get a reference to the checkbox preference
        //KJK_TALK:preference.xml����"advanced_checkbox_preference" ���ڿ��� ã�� checkbox�� setting�� �о�´�.
        //ROM���� �������°��� �ƴ�. 
        //�Ϲ������� ������ xml���� �������µ�, ����� ���ǿ� ���� ROM�� ���� xml�� �� preference�� �������� 
        //�����ü� �ִ�.
        mCheckBoxPreference = (CheckBoxPreference)getPreferenceScreen().findPreference(
                KEY_ADVANCED_CHECKBOX_PREFERENCE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Start the force toggle, thread.start�� �ƴ� �Ϲ� �Լ�call�� ����
        mForceCheckBoxRunnable.run();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        mHandler.removeCallbacks(mForceCheckBoxRunnable);
    }

    //preference�� �����Ҷ����� ȣ��ȴ�.
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Let's do something when my counter preference value changes
        if (key.equals(KEY_MY_PREFERENCE)) {
            Toast.makeText(this, "Thanks! You increased my count to "
                    + sharedPreferences.getInt(key, 0), Toast.LENGTH_SHORT).show();
        }
    }

}
