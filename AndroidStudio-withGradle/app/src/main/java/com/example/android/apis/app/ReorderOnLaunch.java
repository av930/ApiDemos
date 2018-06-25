/*

 * KJK_TALK APIDEMOS: App-> Activity->Reorder Activities
 * act A-->B-->C-->D가 잇을경우 act B를 A-->C-->D-->B 와 같이 stack의 top으로 두는 방법
 * 즉, act를 stack에서 위치를 이동할수 잇다. 관련 option 참조 바람.

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

import com.example.android.apis.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@com.example.android.apis.ActivityIsReviewed(sequence=2.5)
public class ReorderOnLaunch extends Activity {
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        
        setContentView(R.layout.reorder_on_launch);
        
        Button twoButton = (Button) findViewById(R.id.reorder_launch_two);
        twoButton.setOnClickListener(mClickListener);
    }

    private final OnClickListener mClickListener = new OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(ReorderOnLaunch.this, ReorderTwo.class));
        }
    };
}