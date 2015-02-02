package com.telekurye.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class PhotoInfo {

	private String		Path;
	private String		Name;
	private int			Id;
	private Bitmap		Bitmap;
	private ImageView	Imageview;
	private File		file;

	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Bitmap getBitmap() {
		return Bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		Bitmap = bitmap;
	}

	public ImageView getImageview() {
		return Imageview;
	}

	public void setImageview(ImageView imageview) {
		Imageview = imageview;
	}

	public void setFile(File file){
		this.file = file;
	}
	
	public File getFile()
	{
		return file;
	}
}