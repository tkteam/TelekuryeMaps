package com.telekurye.kml.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.telekurye.kml.Polygon;
import com.telekurye.maphelpers.Coordinate;
import com.telekurye.tools.Tools;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class PlacemarkSaxHandler extends DefaultHandler {
	Activity				context;
	boolean					currentElement	= false;
	String					currentValue	= "";
	TextView				lblCount;
	TextView				lblStatus;

	int						count			= 0;

	public List<Polygon>	polygons		= new ArrayList<Polygon>();

	Polygon					polygon;

	public PlacemarkSaxHandler(Activity context, TextView lblCount, TextView lblStatus) {
		this.context = context;
		this.lblCount = lblCount;
		this.lblStatus = lblStatus;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		currentElement = true;

		if (qName.equals("Polygon")) {
			polygon = new Polygon();
			// polygon.coordinates = new ArrayList<Coordinate>();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentElement = false;

		if (qName.equals("coordinates")) {
			currentValue = currentValue.trim().replace(",0", ",");

			String[] array = currentValue.split(",");

			for (int i = 0; i < array.length; i = i + 2) {
				String lat = array[i + 1];
				String lng = array[i];

				Coordinate coor = new Coordinate();
				coor.latitude = Float.valueOf(lat);
				coor.longitude = Float.valueOf(lng);
				// polygon.coordinates.add(coor);

				if (i == 0) {
					polygon.minlatitude = coor.latitude;
					polygon.minlongitude = coor.longitude;
					polygon.maxlatitude = coor.latitude;
					polygon.maxlongitude = coor.longitude;

					polygon.coordinates = String.valueOf(coor.latitude) + "," + String.valueOf(coor.longitude);
				}
				else {
					if (polygon.minlatitude > coor.latitude)
						polygon.minlatitude = coor.latitude;

					if (polygon.minlongitude > coor.longitude)
						polygon.minlongitude = coor.longitude;

					if (polygon.maxlatitude < coor.latitude)
						polygon.maxlatitude = coor.latitude;

					if (polygon.maxlatitude < coor.latitude)
						polygon.maxlatitude = coor.latitude;

					polygon.coordinates = polygon.coordinates + "||" + String.valueOf(coor.latitude) + "," + String.valueOf(coor.longitude);
				}
			}
		}
		else if (qName.equals("Polygon")) {
			// polygons.add(polygon);

			count++;

			try {
				if (polygon.coordinates != null) {
					polygon.InsertOrUpdate(context);
				}
			}
			catch (SQLException e) {
				Tools.saveErrors(e);
				context.runOnUiThread(new Runnable() {
					public void run() {
						lblStatus.setText("Baþarýsýz");
					}
				});
			}
			catch (final Exception e1) {
				Tools.saveErrors(e1);
				context.runOnUiThread(new Runnable() {
					public void run() {
						lblStatus.setText("Baþarýsýz");
					}
				});
			}

			context.runOnUiThread(new Runnable() {
				public void run() {
					lblCount.setText("Polygon Count: " + count);
				}
			});
		}

		currentValue = "";
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (currentElement) {
			currentValue = currentValue + new String(ch, start, length);
		}
	}
}