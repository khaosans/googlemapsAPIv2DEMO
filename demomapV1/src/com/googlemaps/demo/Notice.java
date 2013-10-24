/*
 * Souriya Khaosanga
 * Android Maps Demo
 * Notice.java
 * 
 */

package com.googlemaps.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;

public class Notice extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_notice);

		((TextView) findViewById(R.id.textId)).setText(GooglePlayServicesUtil
				.getOpenSourceSoftwareLicenseInfo(this));

	}

}
