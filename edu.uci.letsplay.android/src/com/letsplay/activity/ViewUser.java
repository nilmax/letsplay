package com.letsplay.activity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.letsplay.R;
import com.letsplay.adapter.SeparatedListAdapter;
import com.letsplay.comm.AccessServletException;
import com.letsplay.comm.WebService;
import com.letsplay.datamodel.Place;
import com.letsplay.datamodel.UserEntry;
import com.letsplay.util.Utils;

import android.app.ListActivity;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ViewUser extends ListActivity{
	private UserEntry userEntry;
	private SeparatedListAdapter adapter;

	private static final String ITEM_KEY= "key";
	private static final String ITEM_VALUE = "value";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
     	setContentView(R.layout.view_user);
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
     	userEntry = (UserEntry)getIntent().getExtras().getSerializable(ListUsers.USER_ITEM);
    }
	
	private void populate() {
        ImageView userPhoto = (ImageView) findViewById(R.id.view_user_photo);      
        if (userEntry.getPicture().length != 0){
        	Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(userEntry.getPicture()));
        	userPhoto.setImageBitmap(bitmap);
        } 
		
        TextView userName = (TextView)findViewById(R.id.view_user_name);
        userName.setText(userEntry.getFirstName()+" "+userEntry.getLastName());
        
        CheckBox star = (CheckBox)findViewById(R.id.view_user_star);
        star.setChecked(userEntry.isFavorite());
        star.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	userEntry.setFavorite(!userEntry.isFavorite());
            	setFavorite(userEntry);
            }
        });

		List<Map<String, String>> userInfo = new LinkedList<Map<String, String>>();
		userInfo.add(createItem("Gender", userEntry.getGender()));
		userInfo.add(createItem("Age", String.valueOf(userEntry.getAge())));
		
		List<Map<String, String>> userPreferredSports = new LinkedList<Map<String, String>>();
		int[] sports = userEntry.getPreferredSports();
		for(int i=0;i<sports.length;i++){
			userPreferredSports.add(createItem("Type", Utils.getSportNameFromId(sports[i])));
		}
		
		adapter = new SeparatedListAdapter(this);
		adapter.addSection("Contact information", new SimpleAdapter(this, userInfo,
				R.layout.view_activity_info, new String[] { ITEM_KEY,
						ITEM_VALUE }, new int[] { R.id.label, R.id.data }));
		adapter.addSection("Preferred sports", new SimpleAdapter(this, userPreferredSports,
				R.layout.view_activity_info, new String[] { ITEM_KEY,
						ITEM_VALUE }, new int[] { R.id.label, R.id.data }));
		setListAdapter(adapter);
	}
	
	private void setFavorite(UserEntry userEntry) {
		final List<String> data = new ArrayList<String>();
		data.add(getResources().getString(R.string.comm_my_phone_number));
		data.add(userEntry.getLogin());

		try {
			WebService service = new WebService(getResources().getString(
					R.string.comm_server_address));
			if (userEntry.isFavorite()){
				service.setFavorite(data);
			} else {
				service.deleteFavorite(data);
			}
		} catch (Exception e) {
			Log.e("letsplay", e.getMessage());
		}
	}
	
	public Map<String, String> createItem(String label, String data) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_KEY, label);
		item.put(ITEM_VALUE, data);
		return item;
	}
}
