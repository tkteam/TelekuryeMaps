package com.telekurye.utils;

import java.util.ArrayList;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class Point {

	private ArrayList<LatLng>	Points;
	private String				Description;
	private LatLng				AnimateCamera;
	private float				Zoom;
	private int					StrokeWidth;
	private int					StrokeColor;
	private int					FillColor;

	private GoogleMap			myMap;
	private PolygonOptions		polygonOptions;

	public Point(GoogleMap map) {
		myMap = map;
	}

	public void DrawOnMap() {

		polygonOptions = new PolygonOptions().addAll(Points);
		polygonOptions.strokeColor(StrokeColor);
		polygonOptions.fillColor(FillColor);
		polygonOptions.strokeWidth(StrokeWidth);
		Polygon polygon = myMap.addPolygon(polygonOptions);

	}

	public void ShowOnMap() {
		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AnimateCamera, Zoom));
	}

	public ArrayList<LatLng> getPoints() {
		return Points;
	}

	public String getDescription() {
		return Description;
	}

	public LatLng getAnimateCamera() {
		return AnimateCamera;
	}

	public float getZoom() {
		return Zoom;
	}

	public int getStrokeWidth() {
		return StrokeWidth;
	}

	public int getStrokeColor() {
		return StrokeColor;
	}

	public int getFillColor() {
		return FillColor;
	}

	public void setPoints(ArrayList<LatLng> points) {
		Points = points;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setAnimateCamera(LatLng animateCamera) {
		AnimateCamera = animateCamera;
	}

	public void setAnimateCamera(Double locX, Double locY) {
		LatLng point = new LatLng(locX, locY);
		AnimateCamera = point;
	}

	public void setZoom(float zoom) {
		Zoom = zoom;
	}

	public void setStrokeWidth(int strokeWidth) {
		StrokeWidth = strokeWidth;
	}

	public void setStrokeColor(int strokeColor) {
		StrokeColor = strokeColor;
	}

	public void setFillColor(int fillColor) {
		FillColor = fillColor;
	}

	public void setStrokeColor(String strokeColor) {
		StrokeColor = Color.parseColor(strokeColor);
	}

	public void setFillColor(String fillColor) {
		FillColor = Color.parseColor(fillColor);
	}

}
