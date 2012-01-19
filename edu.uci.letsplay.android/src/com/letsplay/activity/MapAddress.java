/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.letsplay.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.letsplay.R;
import com.letsplay.util.AddressLocationsOverlay;


public class MapAddress extends MapActivity {

	private LinearLayout linearLayout;
	private MapView mapView;
	private ZoomControls mZoom;
	private MapController controller;
	private List<Overlay> mapOverlays;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);
        linearLayout = (LinearLayout) findViewById(R.id.zoomview);
        mapView = (MapView) findViewById(R.id.mapview);
        mZoom = (ZoomControls) mapView.getZoomControls();
        controller = mapView.getController();
        linearLayout.addView(mZoom);
        
        mapOverlays = mapView.getOverlays();
        AddressLocationsOverlay locations = new AddressLocationsOverlay(getResources().getDrawable(R.drawable.bubble));
        
        GeoPoint point = getAddressPoint();
        
        //MyLocationOverlay loc = new MyLocationOverlay(this,mapView);
        
        controller.animateTo(point); 
        controller.setZoom(16);
        
        
        locations.addOverlay(new OverlayItem(point, "title", "snippet"));
        mapOverlays.add(locations);
    }

    @Override
    protected boolean isRouteDisplayed() { 
    	return false; 
    }
    
    private GeoPoint getAddressPoint(){
     	Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
          List<Address> addressResult = geocoder.getFromLocationName("8827 Palo Verde Road, 92617", 1);
          if (!addressResult.isEmpty()) {
            Address resultAddress = addressResult.get(0);            
            return new GeoPoint((int) (resultAddress.getLatitude() * 1000000),
                    (int) (resultAddress.getLongitude() * 1000000)); 
          }
        } catch (IOException e) {
          Log.d("Contact Location Lookup Failed", e.getMessage());
        }
        return new GeoPoint(0,0);
    }
    

    
    /*
    class MyLocationOverlay extends Overlay {

    	@Override
    	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {

    	super.draw(canvas, mapView, shadow);
    	Paint paint = new Paint();
//
    	Drawable drawable = getResources().getDrawable(R.drawable.bubble);

    	//
    	Point myScreenCoords = new Point();

    	mapView.getProjection().toPixels(p, myScreenCoords);
    	
    	paint.setStrokeWidth(1);
    	paint.setARGB(255, 255, 255, 255);
    	paint.setStyle(Paint.Style.STROKE);
		
    	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);

    	canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);

    	//canvas.drawText("Here I am…", myScreenCoords.x, myScreenCoords.y, paint);

    	return true;

    	}
    }*/
}
