/*
 * Souriya Khaosanga
 * Android Maps Demo
 * FileManager.java
 * 
 */

package com.googlemaps.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import android.os.Environment;

import com.google.android.gms.maps.GoogleMap;

/*
 * Used handle external files for the overall program
 * 
 */
public class FileManager {

	int markers = 0;
	File file = new File(Environment.getExternalStorageDirectory().getPath(),
			"mapPoints.txt");

	public FileManager() {

		try {
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath(), "mapPoints.txt");
			BufferedWriter output = new BufferedWriter(new FileWriter(file,
					true));
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void add(String to_add) {

		try {
			// increment the number of markers
			++markers;

			String text = to_add;

			BufferedWriter output = new BufferedWriter(new FileWriter(file,
					true));
			output.write(text + "\n");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void read(GoogleMap map) {

		InputStream fis;
		BufferedReader br;
		try {

			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis,
					Charset.forName("UTF-8")));
			while ((br.readLine()) != null) {
				// Deal with the line
				// map.addMarker();
			}

			// Done with the file
			br.close();
			br = null;
			fis = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}