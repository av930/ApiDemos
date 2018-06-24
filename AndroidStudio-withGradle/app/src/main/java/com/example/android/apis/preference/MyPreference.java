/*
 * KJK_TALK APIDEMOS: App-> Preferences-> 2. Launching Preferences-> AdvancedPreferences -> Mypreference.java
 * KJK_TALK APIDEMOS: App-> Preferences-> 6. AdvancedPreferences-> AdvancedPreferences -> Mypreference.java
 TextView�� Checkbox�� �ƴ� User Defined preference�� advanced_preferences.xml���� �����Ͽ� 
 act ���۽� �ڵ�����  MyPreference.java�� �����ǰ� �ϰ�  preference�� click���� event�� ����������
 listener�� �ڵ����� ������ ���̵��� �Ѵ�.


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

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * This is an example of a custom preference type. The preference counts the
 * number of clicks it has received and stores/retrieves it from the storage.
 */
public class MyPreference extends Preference { //KJK_TALK: �׳�  Preference�� ��ӹ޾Ҵ�.
    private int mClickCounter;

    // This is the constructor called by the inflater
    // KJK_TALK: XML�� inflate�Ҷ� ȣ����� �˼� �ִ�.
    public MyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        //����� ���� layout���� preference �� �ϳ��� item���� ���ȴ�.
        setWidgetLayoutResource(R.layout.preference_widget_mypreference);        
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        // Set our custom views inside the layout
        // xml���� ���� layout�� ����ϰڴٰ� ǥ��, 
        // ������ layout�� textview�� ���� ������ ���ڸ� ����ϵ��� set���ش�.
        final TextView myTextView = (TextView) view.findViewById(R.id.mypreference_widget);
        if (myTextView != null) {
            myTextView.setText(String.valueOf(mClickCounter));
        }
    }

    // button ���� ������ 1 �� ������Ű�� method 
    @Override
    protected void onClick() {
        int newValue = mClickCounter + 1;
        // Give the client a chance to ignore this change if they deem it
        // invalid
        if (!callChangeListener(newValue)) {
            // They don't want the value to be set
            return;
        }

        // Increment counter
        mClickCounter = newValue;
        // ���� 
        // Save to persistent storage (this method will make sure this
        // preference should be persistent, along with other useful checks)
        persistInt(mClickCounter);
        
        // Data has changed, notify so UI can be refreshed!
        // KJK_TALK: data�� ����Ǿ����� �˷��ִ� �Լ� window�� UpdateData()�� ����.
        notifyChanged();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // This preference type's value type is Integer, so we read the default
        // value from the attributes as an Integer.
        return a.getInteger(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            // Restore state
            mClickCounter = getPersistedInt(mClickCounter);
        } else {
            // Set state
            int value = (Integer) defaultValue;
            mClickCounter = value;
            persistInt(value);
        }
    }

    //KJK_TALK: onSaveInstanceState ���ڱ� ����Ǵ� ��Ȳ�� �ڵ������� ȣ��Ǵ� method�� 
    //���Ŀ� �����Ҷ� �� ������ ������ �����ϰ� �ȴ�. 
    @Override
    protected Parcelable onSaveInstanceState() {
        /*
         * Suppose a client uses this preference type without persisting. We
         * must save the instance state so it is able to, for example, survive
         * orientation changes.
         */

        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        // Save the instance state, ������ ����Ǿ������� clickCounter ���� �����ϰ� �ȴ�.
        final SavedState myState = new SavedState(superState);
        myState.clickCounter = mClickCounter;
        return myState;
    }

    //KJK_TALK: onRestoreInstanceState ���ڱ� ����Ǵ� ��Ȳ�� �ڵ������� ������ ���� ȣ��Ǵ� �ڵ�
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        // Restore the instance state
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        mClickCounter = myState.clickCounter;
        notifyChanged();
    }


    //KJK_TALK: preference���� �����ϰ��� �ϴ� ���� ������ ���� class
    /**
     * SavedState, a subclass of {@link BaseSavedState}, will store the state
     * of MyPreference, a subclass of Preference.
     * <p>
     * It is important to always call through to super methods.
     */
    private static class SavedState extends BaseSavedState {
        int clickCounter;

        public SavedState(Parcel source) {
            super(source);

            // Restore the click counter
            clickCounter = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            // Save the click counter
            dest.writeInt(clickCounter);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
