package com.letsplay.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.letsplay.R;
import com.letsplay.datamodel.SportTypes;


public class Utils {

	public static int getImageResourceFromId(int id){
		int result = 0;
		switch (id) {
		case SportTypes.BASKETBALL:
			result = R.drawable.icon_basketball;
			break;
		case SportTypes.FOOTBALL:
			result = R.drawable.icon_football;
			break;
		case SportTypes.SOCCER:
			result = R.drawable.icon_soccer;
			break;		
		case SportTypes.VOLLEYBALL:
			result = R.drawable.icon_volleyball;
			break;
		}
		return result;
	}
	
	public static String getSportNameFromId(int id){
		String result = "";
		switch (id) {
		case SportTypes.BASKETBALL:
			result = "Basketball";
			break;
		case SportTypes.FOOTBALL:
			result = "Football";
			break;
		case SportTypes.SOCCER:
			result = "Soccer";
			break;		
		case SportTypes.VOLLEYBALL:
			result = "Volleyball";
			break;
		}
		return result;
	}
	
	public static int getSportIdFromName(String name){
		int result = -1;
		if ("Basketball".equals(name)){
			return SportTypes.BASKETBALL;
		} else if ("Football".equals(name)){
			return SportTypes.BASKETBALL;
		} else if ("Soccer".equals(name)){
			return SportTypes.SOCCER;
		} else if ("Volleyball".equals(name)){
			return SportTypes.VOLLEYBALL;
		}
		return result;
	}
	
	public static String[] getSportNames(){
		return new String[]{"Basketball","Football","Soccer","Volleyball"};
	}
	
	public static String getLongDateString(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");
		return sdf.format(d);
	}
	
	public static String getMediumDateString(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd");
		return sdf.format(d);
	}
	
	public static String getShortDateString(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}
	
	public static String getTimeString(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		return sdf.format(d);		
	}
	
	public static Date getDate(String longDate){
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");
		try {
			return sdf.parse(longDate);
		} catch (ParseException e) {
			//e.printStackTrace();
		}	
		return null;	
	}
	
	public static Date getDate(int y, int m, int d){
		SimpleDateFormat sdf = new SimpleDateFormat("d M yyyy");
		try {
			return sdf.parse(d+" "+m+" "+y);
		} catch (ParseException e) {
			//e.printStackTrace();
		}	
		return null;
	}
	
	public static Date getTime(int hh, int mm){
		SimpleDateFormat sdf = new SimpleDateFormat("hh mm");
		try {
			return sdf.parse(hh+" "+mm);
		} catch (ParseException e) {
			//e.printStackTrace();
		}	
		return null;
	}
	
	public static Date getTime(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			//e.printStackTrace();
		}	
		return null;		
	}
	
    public static AlertDialog.Builder getErrorDialog(Context c, String message){
   	 return new AlertDialog.Builder(c).setTitle("Error").setMessage(message).setIcon(android.R.drawable.ic_dialog_alert);
   }
    
    public static ProgressDialog getProgressDialog(Context c, String message){
    	ProgressDialog pd = new ProgressDialog(c);
    	pd.setTitle("Please wait...");
    	pd.setMessage(message);
    	pd.setIndeterminate(true);
    	pd.setCancelable(true);
    	return pd;
    }
    
    public static Address getAddressPoint(Context c, String address){
     	Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        try {
          List<Address> addressResult = geocoder.getFromLocationName(address, 1);
          if (!addressResult.isEmpty()) {
            return addressResult.get(0);            
          }
        } catch (IOException e) {
          Log.d("Contact Location Lookup Failed", e.getMessage());
        }
        return null;
    }
	
}
