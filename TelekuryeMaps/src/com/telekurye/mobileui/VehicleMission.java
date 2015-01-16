package com.telekurye.mobileui;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.telekurye.data_send.VehicleFeedBack;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;
import com.telekurye.utils.CameraHelper;
import com.telekurye.utils.PhotoInfo;

public class VehicleMission extends Activity implements OnClickListener {

	public Button		btnTakePhoto, btnSaveMission;
	public ImageView	imgVehicleMission;
	public RadioGroup	rgStatus;
	public RadioButton	rbStart, rbEnd;
	public EditText		etVehicleKm;

	CameraHelper		tp;
	public Boolean		isThereAPhoto	= false;

	PhotoInfo			info;

	String				photoName;

	private int			missionStatu	= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle);
		Tools.disableScreenLock(this);

		btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
		btnSaveMission = (Button) findViewById(R.id.btn_vehicle_km_save);
		imgVehicleMission = (ImageView) findViewById(R.id.img_vehicle_km_photo);
		rgStatus = (RadioGroup) findViewById(R.id.radioGroup1);
		rbStart = (RadioButton) findViewById(R.id.radio1);
		rbEnd = (RadioButton) findViewById(R.id.radio2);
		etVehicleKm = (EditText) findViewById(R.id.et_vehicle_km_value);

		btnTakePhoto.setOnClickListener(this);
		btnSaveMission.setOnClickListener(this);
		imgVehicleMission.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_take_photo) {
			takePhoto();
		}
		else if (v.getId() == R.id.btn_vehicle_km_save) {
			saveMission();
		}
		else if (v.getId() == R.id.img_vehicle_km_photo) {
			if (isThereAPhoto) {
				selectImage(info);
			}
		}

	}

	private void saveMission() {

		if (rgStatus.getCheckedRadioButtonId() == R.id.radio1) {
			missionStatu = 1;
		}
		else if (rgStatus.getCheckedRadioButtonId() == R.id.radio2) {
			missionStatu = 2;
		}

		if (missionStatu == 0) {
			Tools.showShortCustomToast(VehicleMission.this, "L�tfen durumunuzu se�iniz!");
			return;
		}

		if (etVehicleKm.getText().toString() == null || etVehicleKm.getText().toString().trim().equals("")) {
			Tools.showShortCustomToast(VehicleMission.this, "L�tfen arac�n�z�n KM de�erini giriniz!");
			return;
		}

		if (!isThereAPhoto) {
			Tools.showShortCustomToast(VehicleMission.this, "L�tfen 1 adet foto�raf �ekiniz!");
			return;
		}

		VehicleFeedBack vFeedback = new VehicleFeedBack();
		vFeedback.setPhotoName(photoName);
		vFeedback.setCreateDate(new Date());
		vFeedback.setStatu(missionStatu);
		vFeedback.setKM(Integer.parseInt(etVehicleKm.getText().toString()));
		vFeedback.setUserId(Info.UserId);
		vFeedback.Insert();

		Tools.showLongCustomToast(VehicleMission.this, "Bilgiler Ba�ar�yla G�nderildi!");

		Intent i = new Intent(VehicleMission.this, ExpandableList.class);
		startActivity(i);
		finish();

	}

	private void takePhoto() {
		if (!isThereAPhoto) {
			tp = new CameraHelper(VehicleMission.this);
			startActivityForResult(tp.startCamera(), tp.getRequestCode());
		}
		else {
			Tools.showShortCustomToast(VehicleMission.this, "Yanl�zca 1 foto�raf ekleyebilirsiniz!");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!isThereAPhoto) {

			info = tp.showImage(imgVehicleMission, 1, requestCode, resultCode);
			photoName = info.getName();
			isThereAPhoto = true;
		}
	}

	private void selectImage(final PhotoInfo pInfo) {
		final CharSequence[] items = { "Foto�raf� Sil", "Vazge�" };

		AlertDialog.Builder builder = new AlertDialog.Builder(VehicleMission.this);
		builder.setTitle("D�zenle");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				try {
					if (items[item].equals("Foto�raf� Sil")) {

						imgVehicleMission.setImageResource(android.R.color.transparent);
						isThereAPhoto = false;

						File file = new File(Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH + File.separator + pInfo.getName());
						file.delete();
					}

					else if (items[item].equals("Vazge�")) {
						dialog.dismiss();
					}
				}
				catch (Exception e) {
					Tools.saveErrors(e);

				}

			}
		});
		builder.show();
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
