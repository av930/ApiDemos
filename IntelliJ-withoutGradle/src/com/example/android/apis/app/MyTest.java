package com.example.android.apis.app;


import android.os.Bundle;
import android.util.Log;





/**
 * KJK_TALK APIDEMOS: App-> Activity-> MyTest
 * Activity를 상속해서 Activity를 만든 경우로 부모를 그대로 호출한다.
 *
 */
@com.example.android.apis.ActivityIsReviewed(sequence=1.1) 
public class MyTest extends HelloWorld
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Log.v(this.getClass().toString(), this.toString());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "MyTest";
    }
    
}
