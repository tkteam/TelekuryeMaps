package com.telekurye.mobileui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class Gui extends Activity {
	public static TextView			tv_info_welcome, tv_networkStatus, tv_info_time, tv_info_battery, tv_info_accuracy;
	public static TextView			tv_info_version, tv_Score, tv_earnings, tv_last_sync_date, tv_mission_status_value;
	public static TextView			tv_apno, tv_apno_value, tv_apname, tv_apname_value, tv_address, tv_address_value;
	public static TextView			tv_StreetOrBuildingType, tv_persons, tv_persons_values, tv_building_type_info;
	public static TextView			tv_building_type_info_value, tv_street_name, tv_independent_section_count;
	public static TextView			tv_floor_count, tvLocInfo;
	public static LinearLayout		ll_user_feedback, ll_mission_type_info, ll_apno, llMapFragment, llImages;
	public static EditText			new_et_apno_value, new_et_apname_value, et_independent_section_count, et_floor_count;
	public static Button			btnStreetOrBuildingType, btnSaveFeedback, btnMapZoom, btnCapturePicture;
	public static CheckBox			cbSagKapi, cbOnKapi, cbSolKapi;
	public static ProgressDialog	progressDialog;
	public static TabHost			tabHost;

	public Gui(Activity act) {

		btnStreetOrBuildingType = (Button) act.findViewById(R.id.btn_StreetOrBuildingType);
		btnSaveFeedback = (Button) act.findViewById(R.id.btn_save_feedback);
		btnCapturePicture = (Button) act.findViewById(R.id.btnSavePhoto);
		btnMapZoom = (Button) act.findViewById(R.id.btn_map_zoom);

		llImages = (LinearLayout) act.findViewById(R.id.llImages);
		llMapFragment = (LinearLayout) act.findViewById(R.id.ll_mapfragment);
		ll_user_feedback = (LinearLayout) act.findViewById(R.id.ll_user_feedback);
		ll_mission_type_info = (LinearLayout) act.findViewById(R.id.ll_mission_type_info);
		ll_apno = (LinearLayout) act.findViewById(R.id.ll_apno);

		new_et_apno_value = (EditText) act.findViewById(R.id.new_et_apno_value);
		new_et_apname_value = (EditText) act.findViewById(R.id.new_et_apname_value);
		et_floor_count = (EditText) act.findViewById(R.id.et_floor_count);
		et_independent_section_count = (EditText) act.findViewById(R.id.et_independent_section_count);

		tv_street_name = (TextView) act.findViewById(R.id.tv_street_name1);
		tv_apno = (TextView) act.findViewById(R.id.tv_apno);
		tv_apno_value = (TextView) act.findViewById(R.id.tv_apno_value);
		tv_apname = (TextView) act.findViewById(R.id.tv_apname);
		tv_apname_value = (TextView) act.findViewById(R.id.tv_apname_value);
		tv_address = (TextView) act.findViewById(R.id.tv_address);
		tv_address_value = (TextView) act.findViewById(R.id.tv_address_value);
		tv_StreetOrBuildingType = (TextView) act.findViewById(R.id.tv_StreetOrBuildingType);
		tv_mission_status_value = (TextView) act.findViewById(R.id.tv_mission_status_value);
		tv_independent_section_count = (TextView) act.findViewById(R.id.tv_independent_section_count);
		tv_floor_count = (TextView) act.findViewById(R.id.tv_floor_count);
		tv_persons = (TextView) act.findViewById(R.id.tv_persons);
		tv_persons_values = (TextView) act.findViewById(R.id.tv_persons_values);
		tv_info_welcome = (TextView) act.findViewById(R.id.tv_info_name_surname);
		tv_info_time = (TextView) act.findViewById(R.id.tv_info_time);
		tv_info_battery = (TextView) act.findViewById(R.id.tv_info_battery);
		tv_info_accuracy = (TextView) act.findViewById(R.id.tv_info_accuracy);
		tv_info_version = (TextView) act.findViewById(R.id.tv_info_version);
		tv_Score = (TextView) act.findViewById(R.id.tv_info_score);
		tv_earnings = (TextView) act.findViewById(R.id.tv_info_earnings);
		tv_networkStatus = (TextView) act.findViewById(R.id.tv_info_internet);
		tv_last_sync_date = (TextView) act.findViewById(R.id.tv_info_last_sync_date);
		tv_building_type_info = (TextView) act.findViewById(R.id.tv_building_type_info);
		tv_building_type_info_value = (TextView) act.findViewById(R.id.tv_building_type_info_value);

		cbOnKapi = (CheckBox) act.findViewById(R.id.cbOnKapi);
		cbSolKapi = (CheckBox) act.findViewById(R.id.cbSolKapi);
		cbSagKapi = (CheckBox) act.findViewById(R.id.cbSagKapi);
		
		
	

	}

}
