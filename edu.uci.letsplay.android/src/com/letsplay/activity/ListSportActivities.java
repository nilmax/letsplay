package com.letsplay.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.letsplay.R;
import com.letsplay.adapter.SeparatedListAdapter;
import com.letsplay.adapter.SportActivitiesAdapter;
import com.letsplay.comm.WebService;
import com.letsplay.datamodel.SportEntry;
import com.letsplay.datamodel.SportTypes;
import com.letsplay.util.Utils;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ListSportActivities extends ListActivity {
	private SeparatedListAdapter adapter;
	
	public static final String SPORT_ITEM = "sport-item"; 
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int VIEW_ID = Menu.FIRST + 2;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);				
		setContentView(R.layout.list_activities);
		refreshList();
		registerForContextMenu(getListView());
		setAnimation();
		//populate();
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

	private void populate(List<SportEntry> list){
		List<SportEntry> myList = new ArrayList<SportEntry>();
		List<SportEntry> othersList = new ArrayList<SportEntry>();
		
		for (SportEntry entry : list) {
			if (entry.isAmISubscribed()){
				myList.add(entry);
			} else {
				othersList.add(entry);
			}
		}
		
		adapter = new SeparatedListAdapter(this);
		if (myList.size() > 0){
			adapter.addSection("My activities", new SportActivitiesAdapter(this,myList));
		}
		if (othersList.size() > 0) {
			adapter.addSection("Other activities", new SportActivitiesAdapter(this,othersList));
		}		
		setListAdapter(adapter);
	}
	
	private void createNewActivity(){
        Intent item = new Intent(this, CreateActivity.class);
        startActivityForResult(item, 0);
	}
	
	private void showActivity(int position){
        Intent item = new Intent(this, ViewSportActivity.class);
        item.putExtra(SPORT_ITEM, (Serializable)adapter.getItem(position)); 
        startActivityForResult(item, 0);
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);       
        showActivity(position);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        refreshList();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_activity_new).setIcon(android.R.drawable.ic_menu_add);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            createNewActivity();
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info;
        try {
             info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            Log.e("letsplay", "bad menuInfo", e);
            return;
        }
        SportEntry entry = (SportEntry)adapter.getItem(info.position);
        if (entry!=null){
        	menu.setHeaderTitle(Utils.getSportNameFromId(entry.getSportType()));
        	if (entry.isAmISubscribed()){
        		menu.add(0, DELETE_ID, 0, R.string.menu_activity_cancel);
        	}
        	menu.add(0, VIEW_ID, 0, R.string.menu_activity_view);
        }
	}
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
    	case DELETE_ID:
    		SportEntry entry = (SportEntry)adapter.getItem(info.position);
    		cancelSubscription(entry.getId());
    		return true;
    	case VIEW_ID:
    		showActivity(info.position);
    		return true;
		}
		return super.onContextItemSelected(item);
	}
        
    private void cancelSubscription(int sportId) {
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
				refreshList();
			}
		};

		final Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					WebService service = new WebService(getResources()
							.getString(R.string.comm_server_address));
					service.userUnjoinSport(data);
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

	private void refreshList(){
    	final List<SportEntry> list = new LinkedList<SportEntry>();
		final ProgressDialog pd = Utils.getProgressDialog(this, getResources()
				.getString(R.string.comm_msg_getting_data));
		final Message msg = new Message();
		final Context instance = this;

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				populate(list);
				pd.dismiss();
				if (msg.obj != null) {
					Utils.getErrorDialog(instance, (String) msg.obj).show();
				}
			}
		};

		final Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					WebService service = new WebService(getResources()
							.getString(R.string.comm_server_address));
					list.addAll(service.getListOfSports(getResources()
							.getString(R.string.comm_my_phone_number)));
//					list.addAll(getSportActivities());
				} catch (Exception e) {
					Log.e("letsplay", e.getMessage());
					msg.obj = e.getMessage();
				} finally {
					handler.sendMessage(msg);
				}
			}
		};
		pd.show();
		thread.start();
    }
    
    private List<SportEntry> getSportActivities(){
    	List<SportEntry> list = new LinkedList<SportEntry>();
    	list.add(new SportEntry(0,SportTypes.SOCCER,
				"Anteater Recreation Center (ARC)",
				"680 California Ave, Irvine, CA 92697", new Date(),1.0));
    	list.add(new SportEntry(1,SportTypes.BASKETBALL,
				"Anteater Recreation Center (ARC)",
				"680 California Ave, Irvine, CA 92697", new Date(),1.0));
    	list.add(new SportEntry(2,SportTypes.FOOTBALL,
				"Bren Events Center",
				"West Peltason Dr, Irvine, CA 92697",new Date(),1.0));
    	list.add(new SportEntry(3,SportTypes.VOLLEYBALL,
				"Anteater Recreation Center (ARC)",
				"680 California Ave, Irvine, CA 92697",new Date(),1.0));
    	list.get(0).setAmISubscribed(true);
    	return list;
    }
    
}
