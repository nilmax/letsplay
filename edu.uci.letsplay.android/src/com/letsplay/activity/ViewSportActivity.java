package com.letsplay.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.letsplay.R;
import com.letsplay.adapter.SeparatedListAdapter;
import com.letsplay.adapter.UsersAdapter;
import com.letsplay.comm.WebService;
import com.letsplay.datamodel.SportEntry;
import com.letsplay.datamodel.UserEntry;
import com.letsplay.util.Utils;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSportActivity extends ListActivity {

	private SportEntry sportEntry;
	private SeparatedListAdapter adapter;

	private static final int SUBSCRIBE = 1;
	private static final int UNSUBSCRIBE = 2;
	
	private static final int CONTACT_ITEM_POSITION = 1;
	private static final int ADDRESS_ITEM_POSITION = 3;
	
	private static final String ITEM_KEY= "key";
	private static final String ITEM_VALUE = "value";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
     	setContentView(R.layout.view_activity);
     	setup();
     	populate();
     	setAnimation();    	
    }
    
    
    
    private void setAnimation() {
    	AnimationSet set = new AnimationSet(true);

    	  Animation animation = new AlphaAnimation(0.0f, 1.0f);
    	  animation.setDuration(100);
    	  set.addAnimation(animation);

    	  animation = new TranslateAnimation(
    	      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
    	      Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
    	  );
    	  animation.setDuration(500);
    	  set.addAnimation(animation);

    	  LayoutAnimationController controller =
    	      new LayoutAnimationController(set, 0.25f);
          ListView listView = getListView();        
          listView.setLayoutAnimation(controller);
	}



	private void setup(){
     	sportEntry = (SportEntry)getIntent().getExtras().getSerializable(ListSportActivities.SPORT_ITEM);
     	
    	Button joinButton = (Button) findViewById(R.id.joinButton);
    	if (sportEntry.isAmISubscribed()){
    		joinButton.setText(R.string.menu_activity_cancel);
    	}
    	joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	int action = sportEntry.isAmISubscribed() ? UNSUBSCRIBE : SUBSCRIBE;
            	joinToGame(sportEntry.getId(),action);
            }
        });
    	Button cancelButton = (Button) findViewById(R.id.backButton);
    	cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
    
	private void joinToGame(int sportId, final int action) {
		final Message msg = new Message();
		final Context instance = this;

		final List<String> data = new ArrayList<String>();
		data.add(getResources().getString(R.string.comm_my_phone_number));
		data.add(String.valueOf(sportId));

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Toast.makeText(instance, "Saved!", Toast.LENGTH_SHORT).show();
				if (msg.obj != null) {
					Utils.getErrorDialog(instance, (String) msg.obj).show();
				}
				setResult(RESULT_OK);
				finish();
			}
		};

		final Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					WebService service = new WebService(getResources()
							.getString(R.string.comm_server_address));
					switch (action) {
					case SUBSCRIBE:
						service.userJoinSport(data);
						break;
					case UNSUBSCRIBE:
						service.userUnjoinSport(data);
						break;
					}					
				} catch (Exception e) {
					Log.e("letsplay", e.getMessage());
					msg.obj = e.getMessage();
				} finally {
					handler.sendMessage(msg);
				}
			}
		};
		thread.start();		
	}
	
    private void populate(){
        ImageView icon = (ImageView) findViewById(R.id.view_activity_icon);
		icon.setImageResource(Utils.getImageResourceFromId(sportEntry
				.getSportType()));

		TextView placeName = (TextView) findViewById(R.id.view_activity_name);
		placeName.setText(sportEntry.getPlace().getName());

		List<UserEntry> host = new LinkedList<UserEntry>();
		host.add(sportEntry.getHost());
		
		List<Map<String, String>> address = new LinkedList<Map<String, String>>();
		address.add(createItem("Place", sportEntry.getPlace().getAddress()));
		
		List<Map<String, String>> details = new LinkedList<Map<String, String>>();
		details.add(createItem("Activity", Utils.getSportNameFromId(sportEntry.getSportType())));
		details.add(createItem("Time", Utils.getTimeString(sportEntry.getTime())));
		details.add(createItem("Invited", sportEntry.getNumberOfPlayers()+" guest(s)"));
		details.add(createItem("Confirmed", sportEntry.getConfirmedNumberOfPlayers()+" guest(s)"));

		adapter = new SeparatedListAdapter(this);
		adapter.addSection("Host",new UsersAdapter(this,host, false));		
		adapter.addSection("Map address", new SimpleAdapter(this, address,
				R.layout.view_activity_info_map,
				new String[] { ITEM_KEY, ITEM_VALUE }, new int[] { R.id.map_label,
						R.id.map_value }));
		adapter.addSection("Activity details", new SimpleAdapter(this, details,
				R.layout.view_activity_info, new String[] { ITEM_KEY, ITEM_VALUE },
				new int[] { R.id.label, R.id.data }));
		
		setListAdapter(adapter);
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch(position){
        case CONTACT_ITEM_POSITION:
            Intent item = new Intent(this, ViewUser.class);
            item.putExtra(ListUsers.USER_ITEM, (Serializable)adapter.getItem(position)); 
            startActivity(item);
        	break;
        case ADDRESS_ITEM_POSITION:
        	// Choose a value for uri from the following.
        	// Search Google Maps: geo:0,0?q=query
        	// Show contacts: content://contacts/people
        	// Show a URL: http://www.google.com
        	Intent intent = new Intent(Intent.ACTION_VIEW);
        	intent.setData(Uri.parse("geo:0,0?q="+sportEntry.getPlace().getAddress()));
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(intent);
        break;
        }
    }
    
	public Map<String, String> createItem(String label, String data) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_KEY, label);
		item.put(ITEM_VALUE, data);
		return item;
	}
	

}
