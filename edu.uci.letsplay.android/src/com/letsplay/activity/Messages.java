package com.letsplay.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.letsplay.R;
import com.letsplay.adapter.MessagesAdapter;
import com.letsplay.adapter.SeparatedListAdapter;
import com.letsplay.comm.MessageService;
import com.letsplay.comm.MessageUpdateListener;
import com.letsplay.datamodel.SportEntry;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class Messages extends ListActivity implements MessageUpdateListener{
	private SeparatedListAdapter adapter;
	private List<HashMap<String,SportEntry>> list = new ArrayList<HashMap<String,SportEntry>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_messages);
		setAnimation();
		MessageService.addMessageUpdateListener(this);
	}

	private void setAnimation() {
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(100);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(500);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f);
		ListView listView = getListView();
		listView.setLayoutAnimation(controller);
	}
	
	private void populate(){
		adapter = new SeparatedListAdapter(this);
		adapter.addSection("Your messages", new MessagesAdapter(this,list));	
		setListAdapter(adapter);
	}

	@Override
	public void update(List<HashMap<String,SportEntry>> messages) {
		/*
		 * UIThreadUtilities.runOnUIThread( Messages.this, new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub
		 * 
		 * }});
		 */
		list = messages;		
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);       
        //showUser(position);
    }
    
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("letsplay", "onResume");
		populate();
	}

}
