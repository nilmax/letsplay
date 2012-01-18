package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.letsplay.datamodel.SportTypes;


public class Utils {

	public static byte[] InputStreamToByteArray(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int b;
		if (in!=null) {
			try {
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toByteArray();
	}
	
//	public static int getImageResourceFromId(int id){
//		int result = 0;
//		switch (id) {
//		case SportTypes.BASKETBALL:
//			result = R.drawable.icon_basketball;
//			break;
//		case SportTypes.FOOTBALL:
//			result = R.drawable.icon_football;
//			break;
//		case SportTypes.SOCCER:
//			result = R.drawable.icon_soccer;
//			break;		
//		case SportTypes.VOLLEYBALL:
//			result = R.drawable.icon_volleyball;
//			break;
//		}
//		return result;
//	}
	
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
	
	// TODO
	public static String getShortDateString(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}
	
	// TODO
	public static Date getDate(String longDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(longDate);
		} catch (ParseException e) {
			//e.printStackTrace();
		}	
		return null;	
	}
	
	// TODO
	public static String getTimeString(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		return sdf.format(d);		
	}
	
	// TODO
	public static Date getTime(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		try {
			return sdf.parse(time);
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
	
}
