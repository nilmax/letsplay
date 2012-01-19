package com.letsplay.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.letsplay.R;
import com.letsplay.datamodel.SportEntry;
import com.letsplay.util.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SportActivitiesAdapter extends BaseAdapter {

	private List<SportEntry> list;
	private Context context;

	public SportActivitiesAdapter(Context context, List<SportEntry> list) {
		this.context = context;
		this.list = list;
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
            view = LayoutInflater.from(context).inflate(R.layout.list_activities_item, null);
		}
		
		SportEntry item  = list.get(position);
		
        ImageView i = (ImageView) view.findViewById(R.id.activity_icon);
        i.setImageResource(Utils.getImageResourceFromId(item.getSportType()));
        
        TextView place = (TextView) view.findViewById(R.id.activity_name);
        place.setText(item.getPlace().getName());

        TextView info = (TextView) view.findViewById(R.id.activity_address);
        info.setText(item.getPlace().getAddress());

        TextView date = (TextView) view.findViewById(R.id.activity_date);
        date.setText(Utils.getMediumDateString(item.getDate()));
        
        DecimalFormat df = new DecimalFormat("0.0");
        TextView distance = (TextView) view.findViewById(R.id.activity_distance);
        distance.setText(df.format(item.getPlace().getDistance())+" mi");
        
		return view;
	}
}