package com.telekurye.mobileui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public abstract class Gui extends Activity {

	protected TextView			tv_info_welcome, tv_networkStatus, tv_info_time, tv_info_battery, tv_info_accuracy;
	protected TextView			tv_info_version, tv_Score, tv_earnings, tv_last_sync_date, tv_mission_status_value;
	protected TextView			tv_apno, tv_apno_value, tv_apname, tv_apname_value, tv_address, tv_address_value;
	protected TextView			tv_StreetOrBuildingType, tv_persons, tv_persons_values, tv_building_type_info;
	protected TextView			tv_building_type_info_value, tv_street_name, tv_independent_section_count;
	protected TextView			tv_floor_count, tvLocInfo;
	protected LinearLayout		ll_user_feedback, ll_mission_type_info, ll_apno, llMapFragment, llImages;
	protected EditText			new_et_apno_value, new_et_apname_value, et_independent_section_count, et_floor_count;
	protected Button			btnStreetOrBuildingType, btnSaveFeedback, btnMapZoom, btnCapturePicture;
	protected CheckBox			cbSagKapi, cbOnKapi, cbSolKapi;
	protected ProgressDialog	progressDialog;
	protected TabHost			tabHost;

	protected void initialize() {

		btnStreetOrBuildingType = (Button) findViewById(R.id.btn_StreetOrBuildingType);
		btnSaveFeedback = (Button) findViewById(R.id.btn_save_feedback);
		btnCapturePicture = (Button) findViewById(R.id.btnSavePhoto);
		btnMapZoom = (Button) findViewById(R.id.btn_map_zoom);

		llImages = (LinearLayout) findViewById(R.id.llImages);
		llMapFragment = (LinearLayout) findViewById(R.id.ll_mapfragment);
		ll_user_feedback = (LinearLayout) findViewById(R.id.ll_user_feedback);
		ll_mission_type_info = (LinearLayout) findViewById(R.id.ll_mission_type_info);
		ll_apno = (LinearLayout) findViewById(R.id.ll_apno);

		new_et_apno_value = (EditText) findViewById(R.id.new_et_apno_value);
		new_et_apname_value = (EditText) findViewById(R.id.new_et_apname_value);
		et_floor_count = (EditText) findViewById(R.id.et_floor_count);
		et_independent_section_count = (EditText) findViewById(R.id.et_independent_section_count);

		tv_street_name = (TextView) findViewById(R.id.tv_street_name1);
		tv_apno = (TextView) findViewById(R.id.tv_apno);
		tv_apno_value = (TextView) findViewById(R.id.tv_apno_value);
		tv_apname = (TextView) findViewById(R.id.tv_apname);
		tv_apname_value = (TextView) findViewById(R.id.tv_apname_value);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_address_value = (TextView) findViewById(R.id.tv_address_value);
		tv_StreetOrBuildingType = (TextView) findViewById(R.id.tv_StreetOrBuildingType);
		tv_mission_status_value = (TextView) findViewById(R.id.tv_mission_status_value);
		tv_independent_section_count = (TextView) findViewById(R.id.tv_independent_section_count);
		tv_floor_count = (TextView) findViewById(R.id.tv_floor_count);
		tv_persons = (TextView) findViewById(R.id.tv_persons);
		tv_persons_values = (TextView) findViewById(R.id.tv_persons_values);
		tv_info_welcome = (TextView) findViewById(R.id.tv_info_name_surname);
		tv_info_time = (TextView) findViewById(R.id.tv_info_time);
		tv_info_battery = (TextView) findViewById(R.id.tv_info_battery);
		tv_info_accuracy = (TextView) findViewById(R.id.tv_info_accuracy);
		tv_info_version = (TextView) findViewById(R.id.tv_info_version);
		tv_Score = (TextView) findViewById(R.id.tv_info_score);
		tv_earnings = (TextView) findViewById(R.id.tv_info_earnings);
		tv_networkStatus = (TextView) findViewById(R.id.tv_info_internet);
		tv_last_sync_date = (TextView) findViewById(R.id.tv_info_last_sync_date);
		tv_building_type_info = (TextView) findViewById(R.id.tv_building_type_info);
		tv_building_type_info_value = (TextView) findViewById(R.id.tv_building_type_info_value);

		cbOnKapi = (CheckBox) findViewById(R.id.cbOnKapi);
		cbSolKapi = (CheckBox) findViewById(R.id.cbSolKapi);
		cbSagKapi = (CheckBox) findViewById(R.id.cbSagKapi);

		// cbOnKapi.setOnClickListener(new FeedBack());
		// cbSolKapi.setOnClickListener(new FeedBack());
		// cbSagKapi.setOnClickListener(new FeedBack());
		// btnMapZoom.setOnClickListener(new FeedBack());
		// btnCapturePicture.setOnClickListener(new FeedBack());
		// btnSaveFeedback.setOnClickListener(new FeedBack());
		// btnStreetOrBuildingType.setOnClickListener(new FeedBack());
	}

}
