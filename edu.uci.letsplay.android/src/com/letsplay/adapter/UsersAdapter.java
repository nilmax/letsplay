package com.letsplay.adapter;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.List;

import com.letsplay.R;
import com.letsplay.datamodel.UserEntry;
import com.letsplay.util.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UsersAdapter extends BaseAdapter {

	private List<UserEntry> list;
	private Context context;
	private boolean star;

	public UsersAdapter(Context context, List<UserEntry> list, boolean star) {
		this.context = context;
		this.list = list;
		this.star = star;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_users_item, null);
		}
		
		UserEntry item  = list.get(position);
		
        ImageView userPhoto = (ImageView) view.findViewById(R.id.user_photo);      
        if (item.getPicture().length != 0){
        	Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(item.getPicture()));
        	userPhoto.setImageBitmap(bitmap);
        } 
         
        TextView userName = (TextView) view.findViewById(R.id.user_name);
        userName.setText(item.getFirstName()+" "+item.getLastName());

        String prefSports = "";
		int[] sports = item.getPreferredSports();
		for(int i=0;i<sports.length;i++){
			prefSports += ", " + Utils.getSportNameFromId(sports[i]);
		}
		TextView userInfo = (TextView) view.findViewById(R.id.user_info);
		if (prefSports.length() > 0){
			prefSports = prefSports.substring(1).trim();
		}
        userInfo.setText(prefSports);
        
        if (star) {
			userInfo.setCompoundDrawablesWithIntrinsicBounds(context
					.getResources().getDrawable(R.drawable.star_on), null,
					null, null);
		}
        
        DecimalFormat df = new DecimalFormat("0.0");
        TextView userDistance = (TextView) view.findViewById(R.id.user_distance);
        if (item.getDistance() > 0) {
        	userDistance.setText(df.format(item.getDistance())+" mi");
        }
        
		return view;
	}
}