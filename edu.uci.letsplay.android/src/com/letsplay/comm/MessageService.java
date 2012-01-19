package com.letsplay.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.letsplay.R;
import com.letsplay.comm.WebService;
import com.letsplay.datamodel.SportEntry;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MessageService extends Service {

	private Timer timer = new Timer();
	private static final long UPDATE_INTERVAL = 10000;
	private static List<MessageUpdateListener> listeners = new ArrayList<MessageUpdateListener>() ;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		startService();
	}

	private void startService() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
//				getMessages();
			}
		}, 0, UPDATE_INTERVAL);
		Log.i("letsplay", "Timer started!!!");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopService();
	}

	private void stopService() {
		if (timer != null) {
			timer.cancel();
		}
		Log.i("letsplay", "Timer stopped!!!");
	}
	
	private void getMessages(){
		List<HashMap<String,SportEntry>> list = new ArrayList<HashMap<String,SportEntry>>();
		try {
			WebService service = new WebService(getResources().getString(
					R.string.comm_server_address));
			list.addAll(service.checkInbox(getResources()
					.getString(R.string.comm_my_phone_number)));
			for(MessageUpdateListener l:listeners){
				l.update(list);
			}
		} catch (Exception e) {
			Log.e("letsplay", e.getMessage());
		}
	}
	
	public static void addMessageUpdateListener(MessageUpdateListener l){
		listeners.add(l);
	}
	
	public static void removeMessageUpdateListener(MessageUpdateListener l){
		listeners.remove(l);
	}
}
