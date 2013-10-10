package com.googlemaps.demo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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

	final static String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1000&types=cafe&name=starbucks&sensor=true&key=AIzaSyDNmhilRqYcFKo2Sp8HB8HgLnR05hLCplE";
	private GoogleMap googlemap;
	Marker markerTouch;
	Polyline line;

	// Json code stuff here
	HttpClient client = new DefaultHttpClient();
	JSONObject jsonResponse;
	JSONObject jsonResult;
	JSONObject jsonLocation;
	JSONObject jsonObj;
	JSONArray jsonArray;
	JSONObject holder;

	// create touch points
	LatLng firstPoint = null;
	LatLng lastPoint = null;

	// create a new file
	FileManager file = new FileManager();

	// store the color from the menu
	private int colorValue = 1;

	// function is used to return the json object
	public JSONObject mapper(String toString) throws ClientProtocolException,
			IOException, JSONException {
		StringBuilder url = new StringBuilder(URL);
		// used to add to the code
		url.append("");

		HttpGet get = new HttpGet(url.toString());

		HttpResponse r = client.execute(get);
		int status = r.getStatusLine().getStatusCode();
		if (status == 200) {

			// used to track the location of the string
			file.add("status code 200");
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			jsonResponse = new JSONObject(data);
			jsonArray = jsonResponse.getJSONArray("results");
			jsonObj = jsonArray.getJSONObject(0);
			jsonLocation = jsonObj.getJSONObject("location");
			String test = jsonLocation.getString("lng");
			file.add(test);

			return jsonLocation;
		} else {
			file.add("error at  client");
			return null;
		}

	}

	public class Read extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				holder = mapper("");
				return jsonLocation.getString("lat");

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// check if the google play is available
		if (IsGooglePlay()) { // set the page
			setContentView(R.layout.activity_main);
			setUpMap();
		}

		// create the read
		new Read().execute("lat");
		new Read().execute("lng");
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
