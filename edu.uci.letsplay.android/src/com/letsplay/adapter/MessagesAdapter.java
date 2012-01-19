package com.letsplay.adapter;

import java.util.HashMap;
import java.util.List;

import com.letsplay.R;
import com.letsplay.datamodel.SportEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessagesAdapter extends BaseAdapter {
	private List<HashMap<String,SportEntry>> list;
	private Context context;

	public MessagesAdapter(Context context, List<HashMap<String,SportEntry>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.list_messages_item, null);
		}
		
		HashMap<String,SportEntry> item  = list.get(position);
		String msg = item.keySet().iterator().next();
		SportEntry sportEntry = item.get(msg);
		
        TextView messageDescription = (TextView) view.findViewById(R.id.message_description);
        messageDescription.setText(msg);

        if (sportEntry != null){
        	TextView messageInfo = (TextView) view.findViewById(R.id.message_info);
        	messageInfo.setText(sportEntry.getPlace().getAddress());
        }
		return view;
	}
}
