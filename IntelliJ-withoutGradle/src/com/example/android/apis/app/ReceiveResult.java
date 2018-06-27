/*
 * KJK_TALK APIDEMOS: App-> Activity-> ReceiveResult.java
 하나의 activity가 다른 하나의 activity를 실행하고 실행결과에 따른 결과값을 
 받는것을 보여주는 예제로 그 결과값은 resultcode와 intent로 들어오게 된다.


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
import android.content.Intent;
import android.text.Editable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Shows how an activity can send data to its launching activity when done.y.
 * <p>This can be used, for example, to implement a dialog alowing the user to
pick an e-mail address or image -- the picking activity sends the selected
data back to the originating activity when done.</p>

<p>The example here is composed of two activities: ReceiveResult launches
the picking activity and receives its results; SendResult allows the user
to pick something and sends the selection back to its caller.  Implementing
this functionality involves the
{@link android.app.Activity#setResult setResult()} method for sending a
result and
{@link android.app.Activity#onActivityResult onActivityResult()} to
receive it.</p>

<h4>Demo</h4>
App/Activity/Receive Result
 
<h4>Source files</h4>
<table class="LinkTable">
        <tr>
            <td class="LinkColumn">src/com.example.android.apis/app/ReceiveResult.java</td>
            <td class="DescrColumn">Launches pick activity and receives its result</td>
        </tr>
        <tr>
            <td class="LinkColumn">src/com.example.android.apis/app/SendResult.java</td>
            <td class="DescrColumn">Allows user to pick an option and sends it back to its caller</td>
        </tr>
        <tr>
            <td class="LinkColumn">/res/any/layout/receive_result.xml</td>
            <td class="DescrColumn">Defines contents of the ReceiveResult screen</td>
        </tr>
        <tr>
            <td class="LinkColumn">/res/any/layout/send_result.xml</td>
            <td class="DescrColumn">Defines contents of the SendResult screen</td>
        </tr>
</table>

 */

@com.example.android.apis.ActivityIsReviewed(sequence=2.2)
public class ReceiveResult extends Activity {
    /**
     * Initialization of the Activity after it is first created.  Must at least
     * call {@link android.app.Activity#setContentView setContentView()} to
     * describe what is to be displayed in the screen.
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);

        // See assets/res/any/layout/hello_world.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        setContentView(R.layout.receive_result);

        // Retrieve the TextView widget that will display results.
        mResults = (TextView)findViewById(R.id.results);

        // This allows us to later extend the text buffer.
        mResults.setText(mResults.getText(), TextView.BufferType.EDITABLE);

        // Watch for button clicks.
        Button getButton = (Button)findViewById(R.id.get);
        getButton.setOnClickListener(mGetListener);
    }

    /**
     * This method is called when the sending activity has finished, with the
     * result it supplied.
     * 
     * @param requestCode The original request code as given to
     *                    startActivity().
     * @param resultCode From sending activity as per setResult().
     * @param data From sending activity as per setResult().
     KJK_TALK: onActivityResult 이 함수는 startActivityForResult를 사용하여 B act를 호출한후
     그것이 종료될때 자동으로 cb함수처럼 호출되게 된다.
     */
    @Override
	protected void onActivityResult(int requestCode, int resultCode,
		Intent data) {
        // You can use the requestCode to select between multiple child
        // activities you may have started.  Here there is only one thing
        // we launch.
        //여러개의 startActivityForResult를 사용할때, onActivityResult도 여러개가 오는데
        //이때, 어떤 Act가 종료되어 오는지 구별하기 위해 GET_COED 를 사용
        if (requestCode == GET_CODE) {

            // We will be adding to our text.
            Editable text = (Editable)mResults.getText();

            // This is a standard resultCode that is sent back if the
            // activity doesn't supply an explicit result.  It will also
            // be returned if the activity failed to launch.
            if (resultCode == RESULT_CANCELED) {
                text.append("(cancelled)");

            // Our protocol with the sending activity is that it will send
            // text in 'data' as its result.
            } else {
                text.append("(okay ");
                text.append(Integer.toString(resultCode));
                text.append(") ");
                if (data != null) {
                    text.append(data.getAction());
                }
            }

            text.append("\n");
        }
    }

    // Definition of the one requestCode we use for receiving resuls.
    static final private int GET_CODE = 0;

    private OnClickListener mGetListener = new OnClickListener() {
        public void onClick(View v) {
            // Start the activity whose result we want to retrieve.  The
            // result will come back with request code GET_CODE.
            Intent intent = new Intent(ReceiveResult.this, SendResult.class);//SendResult.class 라는 act를 launch
            startActivityForResult(intent, GET_CODE);//startActivityForResult A가 B를 실행후 B의 실행값을 리턴받을때
        }
    };

    private TextView mResults;
}

