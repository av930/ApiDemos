/*
 * KJK_TALK APIDEMOS: Text-> LogTextBox1 : LogTextBox
   LogTextBox1���� LogTextBox class�� ����Ѵ�.
   link�� import�� �ƴ� build system���� �ϰ� �ȴ�.
   �Ʒ����� scrolling�� TextView �Ӽ��� default�� editable�� �ϱ����ؼ�
   TextView�� ������ �ϰ� �Ǿ���. 


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

package com.example.android.apis.text;

import android.widget.TextView;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.text.method.MovementMethod;
import android.text.Editable;
import android.util.AttributeSet;

/**
 * This is a TextView that is Editable and by default scrollable,
 * like EditText without a cursor.
 *
 * <p>
 * <b>XML attributes</b>
 * <p>
 * See
 * {@link android.R.styleable#TextView TextView Attributes},
 * {@link android.R.styleable#View View Attributes}
 */
public class LogTextBox extends TextView {
    public LogTextBox(Context context) {
        this(context, null);
    }

    public LogTextBox(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public LogTextBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //KJK_TALK: scrolling�� �ϱ� ���ؼ��� �ݵ�� overriding�ؼ� 
    //scrolling method�� �����ؾ� �Ѵ�.
    @Override
    protected MovementMethod getDefaultMovementMethod() {
        return ScrollingMovementMethod.getInstance();
    }

    //KJK_TALK: return ���� Editable�� �����ؼ� ������ ���� overriding�Ͽ���. 
    @Override
    public Editable getText() {
        return (Editable) super.getText();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, BufferType.EDITABLE);
    }
}
