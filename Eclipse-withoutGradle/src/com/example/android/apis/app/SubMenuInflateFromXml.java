/*
 * KJK_TALK APIDEMOS: App-> Menu-> Sub Inflate from XML
 * ���� ������ �����ϰ� ������ Ȯ���ϱ� ���ؼ� ���翹�� 
 */

package com.example.android.apis.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.apis.R;

/**
 * Demonstrates inflating menus from XML. There are different menu XML resources
 * that the user can choose to inflate. First, select an example resource from
 * the spinner, and then hit the menu button. To choose another, back out of the
 * activity and start over.
 */
public class SubMenuInflateFromXml extends Activity {

	private Menu mMenu;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //KJK_TALK: xml�� �ƴ� code�� layout�� �����Ѵ�.
        // Create a simple layout
        
        LinearLayout layout = new LinearLayout(this);
        //KJK_TALK: LinearLayout�� ����(������ �� ����)���� �׾ƶ�. 
        layout.setOrientation(LinearLayout.VERTICAL);
        
        setContentView(layout);
      
		Button btn1 = new Button(this);
		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "start", Toast.LENGTH_SHORT).show();
			}
		});
		btn1.setText("Select All");
		layout.addView(btn1);

        /*
        Menu menu;
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        //�ش� menu xml�� �о��
        inflater.inflate(R.menu.submenu, menu);
        */
    }

    //KJK_TALK: menu key�� �������� � option menu�� ����� �����Ѵ� 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Hold on to this

    	mMenu = menu;
        
        // Inflate the currently selected menu XML resource.
        MenuInflater inflater = getMenuInflater();
        //�ش� menu xml�� �о��
        inflater.inflate(R.menu.submenu, menu);

        mMenu.setGroupVisible(R.id.submenu, false);
        
        return true;
    }

    @Override
    public void closeOptionsMenu() {
        super.closeOptionsMenu();
    }

    //KJK_TALK: optionMenu���� Ư�� menu�� ���������� 
    //submenu�� �ִ°�� Platform���� �ڵ����� submenu�� display���ְ� 
    //�� submenu�� item�� �ٽ� click������� �ٽ� onOptionsItemSelected�� ȣ��ȴ�.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
               
            // Generic catch all for all the other menu resources
            default:
                // Don't toast text when a submenu is clicked
                if (!item.hasSubMenu()) {//submenu�� ������ ������ toast�� ���� 
                    Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;
        }
        
        return false;
    }
    
    

}
