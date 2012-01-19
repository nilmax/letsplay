
package com.letsplay;

import java.util.HashMap;
import java.util.List;

import com.letsplay.activity.ListFavorites;
import com.letsplay.activity.ListSportActivities;
import com.letsplay.activity.ListUsers;
import com.letsplay.activity.Messages;
import com.letsplay.comm.MessageService;
import com.letsplay.comm.MessageUpdateListener;
import com.letsplay.datamodel.SportEntry;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import edu.uci.letsplay.R;

public class LetsPlay extends TabActivity implements MessageUpdateListener{	
    private TabSpec messagesTab;
    private static final String TAB_4 = "Inbox";
    //private int i = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TabHost tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("Activities",getResources().getDrawable(R.drawable.ic_tab_recent))
                .setContent(new Intent(this, ListSportActivities.class)));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("Users",getResources().getDrawable(R.drawable.ic_tab_contacts))
                .setContent(new Intent(this, ListUsers.class)));
        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("Favorites",getResources().getDrawable(R.drawable.ic_tab_starred))
                .setContent(new Intent(this, ListFavorites.class))); 
        messagesTab = tabHost.newTabSpec("tab4")
        .setIndicator(TAB_4,getResources().getDrawable(android.R.drawable.ic_dialog_email))
        .setContent(new Intent(this, Messages.class));
        tabHost.addTab(messagesTab);
        MessageService.addMessageUpdateListener(this);
        Intent svc = new Intent(this, MessageService.class);
        startService(svc);
    }
    

    @Override 
    protected void onDestroy() {
    	super.onDestroy();
        Intent svc = new Intent(this, MessageService.class);
        stopService(svc);
    }


	@Override
	public void update(List<HashMap<String,SportEntry>> messages) {
		handler.sendEmptyMessage(0);
	}

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//Toast.makeText(LetsPlay.this, "New message arrived!", Toast.LENGTH_SHORT).show();
			//messagesTab.setIndicator(TAB_4+"("+i+")");
		}
	};

}