package com.telekurye.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.telekurye.mobileui.Login;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class CameraHelper {

	private String		FOLDER_NAME			= "TelekuryeMaps";
	private int			REQUEST_CODE		= 1;
	private int			ImageQuality		= 80;
	private int			ImageResizePersent	= 40;
	private int			ImageRotateAngle	= 90;

	private Context		mContext;
	private String		randomUUID;
	private Uri			ImageUri;

	private PhotoInfo	photoInfo;

	public CameraHelper(Context context) {
		mContext = context;
		photoInfo = new PhotoInfo();
	}

	public Intent startCamera() {

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File photo = createOutputMediaFile();
		if (photo != null) {
			ImageUri = Uri.fromFile(photo);
			photoInfo.setPath(ImageUri.toString());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);

		}
		return intent;
	}

	public PhotoInfo showImage(ImageView iv, int PhotoId, int requestCode, int resultCode) {

		if (requestCode == REQUEST_CODE) {

			if (resultCode == Activity.RESULT_OK) {

				photoInfo = new PhotoInfo();
				photoInfo.setId(PhotoId);
				try {

					Uri selectedImage = ImageUri;
					ContentResolver cr = mContext.getContentResolver();
					Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);

					bitmap = resizeBitmap(bitmap);

					photoInfo.setPath(ImageUri.toString());

					String[] uris = ImageUri.toString().split("/");

					photoInfo.setName(uris[uris.length - 1]);
					photoInfo.setBitmap(bitmap);
					iv.setImageBitmap(bitmap);
					photoInfo.setImageview(iv);

				}
				catch (Exception e) {
					Tools.saveErrors(e);
					Log.e("Camera", e.toString());
				}

			}
			else {
				ImageUri = null;
				iv.setImageBitmap(null);
			}

		}
		return photoInfo;

	}

	private Bitmap resizeBitmap(Bitmap bitmap) {
		Bitmap photo = bitmap;
		try {

			int imgWith = (bitmap.getWidth() * ImageResizePersent) / 100;
			int imgHeight = (bitmap.getHeight() * ImageResizePersent) / 100;

			photo = RotateBitmap(Bitmap.createScaledBitmap(photo, imgWith, imgHeight, false), ImageRotateAngle);

			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, ImageQuality, bytes);

			File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FOLDER_NAME + File.separator + randomUUID + ".jpg");
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
		return photo;
	}

	private File createOutputMediaFile() {

		File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(FOLDER_NAME, FOLDER_NAME + " klasörü oluþturulamadý!");
				return null;
			}
		}

		randomUUID = UUID.randomUUID().toString();
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator + randomUUID + ".jpg");
		photoInfo.setName(randomUUID + ".jpg");
		return mediaFile;
	}

	private Bitmap RotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public String getImageStringUri() {
		return ImageUri.toString();
	}

	public Uri getImageUri() {
		return ImageUri;
	}

	public String getImageName() {
		return randomUUID;
	}

	public int getRequestCode() {
		return REQUEST_CODE;
	}

}
