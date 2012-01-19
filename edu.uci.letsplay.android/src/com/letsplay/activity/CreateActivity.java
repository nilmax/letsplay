package com.letsplay.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.letsplay.R;
import com.letsplay.comm.WebService;
import com.letsplay.datamodel.Place;
import com.letsplay.datamodel.SportEntry;
import com.letsplay.util.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CreateActivity extends Activity{
	private List<Place> commonPlaces;
	
    private static final int TIME_DIALOG_ID = 0;
    private static final int DATE_DIALOG_ID = 1;
	
    private AutoCompleteTextView placeName;
    private TextView placeAddress;
    private Spinner activityType;
    private Spinner numberPlayers;
    private Button pickDate;
    private Button pickTime;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setTitle("New activity");
    	setContentView(R.layout.edit_activity);
    	setup();
    }

	private void setup() {
        ArrayAdapter<String> adapter;
        commonPlaces = getCommonPlaces();
        
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getPlacesString(commonPlaces));
        placeName = (AutoCompleteTextView) findViewById(R.id.edit_activity_place_name);
        placeName.setAdapter(adapter);
        placeName.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				for (Place place : commonPlaces) {
					if (place.getName().equals(placeName.getText().toString())){
						placeAddress.setText(place.getAddress());
						break;
					}
				}
			}});
        
        placeAddress = (TextView) findViewById(R.id.edit_activity_address);
        
        activityType = (Spinner) findViewById(R.id.edit_activiy_type);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utils.getSportNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityType.setAdapter(adapter);
        
        String[] playersValues = new String[22];
        for (int i=0;i<playersValues.length;i++){
        	playersValues[i] = String.valueOf(i+1);
        }       
        numberPlayers = (Spinner) findViewById(R.id.edit_activity_number_players);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playersValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberPlayers.setAdapter(adapter);
        
    	Button saveButton = (Button) findViewById(R.id.saveButton);
    	saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	SportEntry entry = getSportEntryFromUI();
            	saveSportEntryOnDatabase(entry);
            }
        });
    	Button discardButton = (Button) findViewById(R.id.discardButton);
    	discardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        pickDate = (Button) findViewById(R.id.edit_activity_date);
        pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        pickTime = (Button) findViewById(R.id.edit_activity_time);
        pickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
	}
	
	private List<Place> getCommonPlaces() {
		//String[] result = new String[]{"Anteater Recreation Center (ARC)","Bren Events Center"};
		List<Place> places = new ArrayList<Place>();
		try {
			WebService service = new WebService(getResources().getString(
					R.string.comm_server_address));
			places.addAll(service.getListOfPlaces(getResources()
					.getString(R.string.comm_my_phone_number)));
		} catch (Exception e) {
			Log.e("letsplay", e.getMessage());
		}
		return places;
	}
	
	private String[] getPlacesString(List<Place> places){
		int i = 0;
		String[] result = new String[places.size()];
		for (Place place : places) {
			result[i++] = place.getName();
		}
		return result;
	}
	
	private SportEntry getSportEntryFromUI() {
		String selectedSportName = Utils.getSportNames()[activityType.getSelectedItemPosition()];
		SportEntry entry = new SportEntry(-1, Utils.getSportIdFromName(selectedSportName), placeName.getText().toString(),
				placeAddress.getText().toString(), Utils.getDate(pickDate
						.getText().toString()), 0);	
		Address point = Utils.getAddressPoint(this, entry.getPlace().getAddress());
		if (point != null){
			entry.getPlace().setLatitude(point.getLatitude());
			entry.getPlace().setLongitude(point.getLongitude());
		}
		entry.setNumberOfPlayers(numberPlayers.getSelectedItemPosition());
		entry.setTime(Utils.getTime(pickTime.getText().toString()));
		return entry;
	}
	
	private void saveSportEntryOnDatabase(SportEntry entry) {
		final Message msg = new Message();
		final Context instance = this;

		final List<Object> data = new ArrayList<Object>();
		data.add(getResources().getString(R.string.comm_my_phone_number));
		data.add(entry);

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
					service.createSportActivity(data);
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
	
    @Override
    protected Dialog onCreateDialog(int id) {
		final Calendar c = Calendar.getInstance();		
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							pickTime.setText(Utils.getTimeString(Utils.getTime(hourOfDay, minute)));
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					false);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							pickDate.setText(Utils.getLongDateString(Utils.getDate(year, monthOfYear+1, dayOfMonth)));
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
							.get(Calendar.DAY_OF_MONTH));
		}		
		return null;
	}
	
}
