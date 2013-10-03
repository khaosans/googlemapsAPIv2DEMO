package com.googlemaps.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity implements LocationListener {

	private GoogleMap googlemap;
	Marker markerTouch;
	Polyline line;

	// create touch points
	LatLng firstPoint = null;
	LatLng lastPoint = null;

	// create a new file
	FileManager file = new FileManager();

	// store the color from the menu
	private int colorValue = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// check if the google play is available
		if (IsGooglePlay()) { // set the page
			setContentView(R.layout.activity_main);
			setUpMap();
		}
	}

	// make the menu options
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		getMenuInflater().inflate(R.menu.testmenu, menu);

		return true;
	}

	// add the menu item
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.legal_notices) {
			startActivity(new Intent(this, Notice.class));
		} // button setPoint1 is used
		else if (item.getItemId() == R.id.setPoint1) {
			colorValue = 1;
		} else if (item.getItemId() == R.id.setPoint2) {
			colorValue = 2;
		} else if (item.getItemId() == R.id.setPoint3) {

			if (firstPoint != null && lastPoint != null) {

				line = googlemap.addPolyline(new PolylineOptions()
						.add(firstPoint, lastPoint).width(5).color(Color.RED));
			}
			// change color back to default
			colorValue = 1;
		} else
			return true;
		return true;
	}

	@SuppressLint("NewApi")
	// suppress the API levels error
	private void setUpMap() {
		if (googlemap == null) {
			googlemap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			if (googlemap != null) {

				// add some code to initialize map
				googlemap.setMyLocationEnabled(true);

				LocationManager my_location = (LocationManager) getSystemService(LOCATION_SERVICE);

				String provider = my_location.getBestProvider(new Criteria(),
						true);
				if (provider == null) {
					onProviderDisabled(provider);

				}
				Location loc = my_location.getLastKnownLocation(provider);

				// change the location
				if (loc != null) {
					onLocationChanged(loc);
				}

				// on map click listener
				googlemap.setOnMapLongClickListener(onLongClickMapSettings());
			}
		}
	}

	private OnMapLongClickListener onLongClickMapSettings() {
		return new OnMapLongClickListener() {

			public void onMapLongClick(LatLng point) { // used to test to print
														// out

				Log.i(point.toString(), "User Long Clicked");

				// print out the colors for the buttons
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(point.latitude, point.longitude)).title(
						point.toString());

				if (colorValue == 1) {
					// set the firstPoint to the touched position
					firstPoint = point;
					googlemap
							.addMarker(marker
									.position(point)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
				}
				if (colorValue == 2) {

					lastPoint = point;
					googlemap
							.addMarker(marker
									.position(point)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
				}

				file.add(point.toString());
			}

		};

	}

	// check to see if the google play services exist private boolean
	public boolean IsGooglePlay() { // TODO Auto-generated method stub
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (status == ConnectionResult.SUCCESS) {
			return true;
		} else {
			((Dialog) GooglePlayServicesUtil.getErrorDialog(status, this, 10))
					.show();
		}
		return false;
	}

	// provides location change

	@Override
	public void onLocationChanged(Location location) {
		LatLng latlng = new LatLng(location.getLatitude(),
				location.getLongitude());

		// move the camera to
		googlemap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		googlemap.animateCamera(CameraUpdateFactory.zoomTo(10));
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
