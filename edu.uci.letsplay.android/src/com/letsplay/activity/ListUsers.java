package com.letsplay.activity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.letsplay.R;
import com.letsplay.adapter.SeparatedListAdapter;
import com.letsplay.adapter.UsersAdapter;
import com.letsplay.comm.AccessServletException;
import com.letsplay.comm.WebService;
import com.letsplay.datamodel.UserEntry;
import com.letsplay.util.Utils;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
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
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ListUsers extends ListActivity {
	protected SeparatedListAdapter adapter;
	
	public static final String USER_ITEM = "user-item"; 
	private static final int VIEW_ID = Menu.FIRST;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.list_contacts);
		refreshList();
		registerForContextMenu(getListView());
		setAnimation();
        getListView().setTextFilterEnabled(true);
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
	
	protected void populate(List<UserEntry> list){
		adapter = new SeparatedListAdapter(this);
		adapter.addSection("Nearby users", new UsersAdapter(this,list,false));	
		setListAdapter(adapter);
	}
	
	private void showUser(int position){
        Intent item = new Intent(this, ViewUser.class);
        item.putExtra(USER_ITEM, (Serializable)adapter.getItem(position)); 
        startActivityForResult(item, 0);
	}
	
	protected void refreshList() {
    	final List<UserEntry> list = new LinkedList<UserEntry>();
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
					list.addAll(getListOfUsers());
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
	
	protected List<UserEntry> getListOfUsers() throws NotFoundException, AccessServletException {
		WebService service = new WebService(getResources()
				.getString(R.string.comm_server_address));
		return service.getListOfUsers(getResources()
				.getString(R.string.comm_my_phone_number));
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);       
        showUser(position);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        refreshList();
    }
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
    	case VIEW_ID:
    		showUser(info.position);
    		return true;
		}
		return super.onContextItemSelected(item);
	}
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, VIEW_ID, 0, R.string.menu_view_user);
	}
}
