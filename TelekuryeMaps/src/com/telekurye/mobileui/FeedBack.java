package com.telekurye.mobileui;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.telekurye.data.BasarShapeId;
import com.telekurye.data.FinishedShapeHistory;
import com.telekurye.data.IMission;
import com.telekurye.data.MissionsBuildings;
import com.telekurye.data.MissionsStreets;
import com.telekurye.data.Person;
import com.telekurye.data.SyncTime;
import com.telekurye.data_send.MissionFeedBack;
import com.telekurye.data_send.MissionFeedBackPhoto;
import com.telekurye.data_send.VisitFeedBack;
import com.telekurye.maphelpers.PolyUtil;
import com.telekurye.tools.Constant;
import com.telekurye.tools.Info;
import com.telekurye.tools.LiveData;
import com.telekurye.tools.Tools;
import com.telekurye.utils.CameraHelper;
import com.telekurye.utils.MissionListCreator;
import com.telekurye.utils.PhotoInfo;
import com.telekurye.utils.ShapeControl;

public class FeedBack extends Activity implements OnTabChangeListener, android.location.GpsStatus.Listener, OnCameraChangeListener, OnMarkerDragListener, LocationListener, OnMapClickListener,
		OnMapLongClickListener, OnMarkerClickListener, OnClickListener, SensorEventListener {

	// --------- GUI -------
	private TextView						tv_info_welcome, tv_networkStatus, tv_info_time, tv_info_battery, tv_info_accuracy;
	private TextView						tv_info_version, tv_Score, tv_earnings, tv_last_sync_date, tv_mission_status_value;
	private TextView						tv_apno, tv_apno_value, tv_apname, tv_apname_value, tv_address, tv_address_value;
	private TextView						tv_StreetOrBuildingType, tv_persons, tv_persons_values, tv_building_type_info;
	private TextView						tv_building_type_info_value, tv_street_name, tv_independent_section_count;
	private TextView						tv_floor_count, tvLocInfo;
	private LinearLayout					ll_user_feedback, ll_mission_type_info, ll_apno, llMapFragment, llImages;
	private EditText						new_et_apno_value, new_et_apname_value, et_independent_section_count, et_floor_count;
	private Button							btnStreetOrBuildingType, btnSaveFeedback, btnMapZoom, btnCapturePicture;
	private CheckBox						cbSagKapi, cbOnKapi, cbSolKapi;
	private ProgressDialog					progressDialog;
	private TabHost							tabHost;

	// ------- MISSION FEEDBACK --------
	private double							GPSLat;
	private double							GPSLng;
	private double							GPSAlt;
	private double							GPSBearing;
	private double							GPSSpeed;
	private Date							GPSTime;
	private double							SignedLat;
	private double							SignedLng;
	private int								TypeId;
	private int								checkboxStatus			= 0;
	private int								basarShapeId			= 0;

	// ------- KML & SHAPE --------
	private List<BasarShapeId>				shapeListFromHost;
	private List<Long>						shapeIdHistory;
	private FinishedShapeHistory			finishedShapes;
	private ShapeControl					shapeControl;
	private List<Integer>					ShapeIdList				= null;
	private Boolean							isFinishRedShapes		= true;
	private List<com.telekurye.kml.Polygon>	polygons;
	private String							db_path					= "/data/data/com.telekurye.mobileui/databases/";
	private String							db_name					= Info.MAP_DBNAME;
	private List<Polygon>					polList					= new ArrayList<Polygon>();
	private List<Polyline>					polylineList			= new ArrayList<Polyline>();
	private Polygon							currentPolygon			= null;
	private List<Polygon>					SelectedPolygonList		= new ArrayList<Polygon>();
	private List<Long>						greenShapes;

	// ------ GOOGLE MAP -------
	private final int						RQS_GooglePlayServices	= 1;
	private GoogleMap						myMap;
	private LatLng							currentLocation;
	private Marker							mPositionMarker;
	private Marker							currentMarker;
	private Boolean							isMarkerSet				= false;
	private MapFragment						map;

	// ----- GPS -----
	protected LocationManager				locationManager;
	private long							mLastLocationMillis;
	private Location						mLastLocation;
	private Location						tempLocation			= null;
	private boolean							isGPSFix				= false;
	private float							UserAccuracy			= 5000;

	// ----------------------------------
	private int								valueTab;
	private int								selectType;
	private Boolean							btnTypeStatus			= false;
	private Boolean							isZoomOpen				= false;
	private Boolean							isNewBuilding			= false;
	private Integer							firstStreetTypeId		= null;
	private SensorManager					mSensorManager;
	private CameraHelper					tp;
	private int								MissionCounter			= 0;
	private int								grupid, childid, streettype;
	private ArrayList<IMission>				mMissionForFeedback;
	private List<MissionsStreets>			completedMissionStreets	= new ArrayList<MissionsStreets>();
	private MissionsStreets					ms;
	private UiSettings						uiSettings;
	private Thread							uiUpdateThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		Tools.disableScreenLock(this);

		// ---------- Components ------------
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

		cbOnKapi.setOnClickListener(this);
		cbSolKapi.setOnClickListener(this);
		cbSagKapi.setOnClickListener(this);
		btnMapZoom.setOnClickListener(this);
		btnCapturePicture.setOnClickListener(this);
		btnSaveFeedback.setOnClickListener(this);
		btnStreetOrBuildingType.setOnClickListener(this);

		// ---------------- GET DATAS ---------------------
		if (getIntent().getExtras() != null) {
			grupid = getIntent().getExtras().getInt("grupid");
			childid = getIntent().getExtras().getInt("childid");
			streettype = getIntent().getExtras().getInt("streettype");
		}

		MissionListCreator mlc = new MissionListCreator(this, grupid, childid, streettype);
		mMissionForFeedback = mlc.getMissionList(true);
		ms = mlc.getThisStreet();

		Person person = new Person();
		String namesurname = person.GetById(Info.UserId).getName() + " " + person.GetById(Info.UserId).getSurname();

		if (namesurname.length() < 30) {
			tv_info_welcome.setTextSize(18f);
		}
		else {
			tv_info_welcome.setTextSize(13f);
		}

		tv_info_welcome.setText(namesurname);
		tv_info_version.setText("Versiyon : " + Info.CURRENT_VERSION);
		llImages.setWeightSum(Info.PHOTO_COUNT);
		btnMapZoom.setBackgroundColor(Color.RED);
		tv_info_accuracy.setBackgroundColor(Color.RED);
		tv_info_accuracy.setText("Gps Yok  ");
		tv_earnings.setText("Bilgi Yok");

		Boolean hasMapCreated = showMapOnActivity();

		if (!hasMapCreated) {
			Toast.makeText(this, "Lütfen Google Play Hizmetlerini güncellemek üzere Ali Bahadýr Kuþ ile iletiþime geçiniz.", Toast.LENGTH_LONG).show();
			return;
		}

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);

		uiSettings = myMap.getUiSettings();

		if (!Info.ISTEST) {

			uiSettings.setAllGesturesEnabled(false);
			uiSettings.setZoomControlsEnabled(false);
			uiSettings.setCompassEnabled(true);
			uiSettings.setIndoorLevelPickerEnabled(true);
			uiSettings.setMyLocationButtonEnabled(true);
			uiSettings.setMapToolbarEnabled(false);

			// u.setRotateGesturesEnabled(true);
			// u.setScrollGesturesEnabled(false);
			// u.setTiltGesturesEnabled(false);
			// u.setZoomGesturesEnabled(false);

		}
		else {
			// myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
			// LatLng(41.0756, 28.9744), Info.MAP_ZOOM_LEVEL));
			myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.9786680000, 37.7365860000), Info.MAP_ZOOM_LEVEL));
		}

		// uiSettings.setAllGesturesEnabled(true); // *-*
		// uiSettings.setZoomControlsEnabled(true);// *-*
		// myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.0756, 28.9744), Info.MAP_ZOOM_LEVEL));// *-*

		myMap.animateCamera(CameraUpdateFactory.zoomTo(Info.MAP_ZOOM_LEVEL));

		tv_street_name.setText("<-- " + mMissionForFeedback.get(MissionCounter).getName() + " SOKAK -->");

		ShapeIdList = new ArrayList<Integer>();
		ShapeIdList = FillShapeIds();
		ShapeIdList = com.telekurye.kml.Polygon.GetNonMatchedShapeIdList(ShapeIdList, this);

		shapeControl = new ShapeControl(ShapeIdList);

		LoadShapes();

		fillComponent();
		tabsProperties();
	}

	private List<Integer> FillShapeIds() {
		List<Integer> ShapeIdList = new ArrayList<Integer>();
		ShapeIdList.add(542297);
		ShapeIdList.add(507561);
		ShapeIdList.add(507569);
		ShapeIdList.add(507568);
		ShapeIdList.add(507553);
		ShapeIdList.add(507558);
		ShapeIdList.add(507544);
		ShapeIdList.add(507543);
		ShapeIdList.add(507541);
		ShapeIdList.add(507619);
		ShapeIdList.add(507564);
		ShapeIdList.add(507548);
		ShapeIdList.add(548428);
		ShapeIdList.add(544907);
		ShapeIdList.add(540123);
		ShapeIdList.add(540165);
		ShapeIdList.add(548838);
		ShapeIdList.add(543582);
		ShapeIdList.add(548421);
		ShapeIdList.add(548448);
		ShapeIdList.add(548422);
		ShapeIdList.add(548423);
		ShapeIdList.add(543380);
		ShapeIdList.add(581406);
		ShapeIdList.add(580807);
		ShapeIdList.add(543366);
		ShapeIdList.add(580756);
		ShapeIdList.add(580790);
		ShapeIdList.add(581261);
		ShapeIdList.add(581401);
		ShapeIdList.add(580915);
		ShapeIdList.add(580938);
		ShapeIdList.add(580802);
		ShapeIdList.add(580887);
		ShapeIdList.add(580923);
		ShapeIdList.add(580894);
		ShapeIdList.add(403790);
		ShapeIdList.add(403796);
		ShapeIdList.add(579432);
		ShapeIdList.add(580960);
		ShapeIdList.add(580944);
		ShapeIdList.add(580945);
		ShapeIdList.add(580893);
		ShapeIdList.add(579423);
		ShapeIdList.add(580892);
		ShapeIdList.add(535415);
		ShapeIdList.add(580880);
		ShapeIdList.add(580751);
		ShapeIdList.add(580911);
		ShapeIdList.add(581239);
		ShapeIdList.add(580872);
		ShapeIdList.add(535100);
		ShapeIdList.add(581246);
		ShapeIdList.add(581245);
		ShapeIdList.add(581426);
		ShapeIdList.add(543602);
		ShapeIdList.add(581398);
		ShapeIdList.add(535145);
		ShapeIdList.add(580876);
		ShapeIdList.add(580783);
		ShapeIdList.add(410296);
		ShapeIdList.add(581577);
		ShapeIdList.add(580741);
		ShapeIdList.add(410041);
		ShapeIdList.add(580819);
		ShapeIdList.add(580875);
		ShapeIdList.add(581165);
		ShapeIdList.add(581241);
		ShapeIdList.add(581240);
		ShapeIdList.add(580769);
		ShapeIdList.add(580781);
		ShapeIdList.add(543596);
		ShapeIdList.add(540268);
		ShapeIdList.add(548707);
		ShapeIdList.add(542068);
		ShapeIdList.add(540266);
		ShapeIdList.add(540267);
		ShapeIdList.add(540242);
		ShapeIdList.add(543189);
		ShapeIdList.add(540145);
		ShapeIdList.add(540097);
		ShapeIdList.add(540238);
		ShapeIdList.add(541947);
		ShapeIdList.add(540240);
		ShapeIdList.add(540114);
		ShapeIdList.add(543232);
		ShapeIdList.add(543734);
		ShapeIdList.add(542207);
		ShapeIdList.add(543489);
		ShapeIdList.add(542074);
		ShapeIdList.add(401816);
		ShapeIdList.add(542108);
		ShapeIdList.add(548444);
		ShapeIdList.add(542292);
		ShapeIdList.add(542204);
		ShapeIdList.add(535143);
		ShapeIdList.add(580940);
		ShapeIdList.add(543578);
		ShapeIdList.add(543190);
		ShapeIdList.add(580942);
		return ShapeIdList;
	}

	private void fillComponent() {

		btnStreetOrBuildingType.setEnabled(true);
		et_independent_section_count.setEnabled(true);
		et_independent_section_count.setText("");
		et_independent_section_count.setHint("");
		et_floor_count.setEnabled(true);
		et_floor_count.setText("");
		et_floor_count.setHint("");

		isNewBuilding = false;

		try {

			if (mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 1) {

				Info.PHOTO_COUNT = 2;
				llImages.setWeightSum(Info.PHOTO_COUNT);

				tv_mission_status_value.setText("Sokak Görevi");

				// -----
				tv_apname.setText("Sokak Adý : ");
				tv_apname_value.setVisibility(View.VISIBLE);
				new_et_apname_value.setVisibility(View.GONE);

				if (mMissionForFeedback.get(MissionCounter).getName() != null && !mMissionForFeedback.get(MissionCounter).getName().trim().equalsIgnoreCase("")) {
					tv_apname_value.setText(mMissionForFeedback.get(MissionCounter).getName() + " Sokak");
				}
				else {
					tv_apname_value.setText("");
				}
				// ------
				ll_mission_type_info.setVisibility(View.VISIBLE);
				btnStreetOrBuildingType.setText("Seçiniz");
				tv_building_type_info.setText("Sokak Tipi Bilgisi : ");

				// -----
				new_et_apno_value.setVisibility(View.GONE);
				tv_apno.setVisibility(View.GONE);
				ll_apno.setVisibility(View.GONE);
				// tv_apno.setText("Sokak No : ");
				// tv_apno_value.setVisibility(View.VISIBLE);
				// tv_apno_value.setText("");
				// -----
				tv_StreetOrBuildingType.setText("Sokak Tipi : ");
				// -----
				ll_user_feedback.setVisibility(View.GONE);
				et_floor_count.setText("");
				// ----
				tv_address.setVisibility(View.VISIBLE);
				tv_address_value.setVisibility(View.VISIBLE);
				tv_address_value.setText(mMissionForFeedback.get(MissionCounter).getAddressText().trim());
				// ----
				tv_persons.setVisibility(View.VISIBLE);
				tv_persons_values.setVisibility(View.VISIBLE);
				tv_persons_values.setText("");
				// ---

				tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" + mMissionForFeedback.size());

				// if (shapeControl != null || shapeControl.getListSize() > 0) {
				// tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" +
				// mMissionForFeedback.size() + "  [+" +
				// shapeControl.getListSize() + "]");
				// }
				// else {
				// tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" +
				// mMissionForFeedback.size() + "  [0]");
				// }

				// for (int i = 0; i < msTypesId.size(); i++) {
				// if (mMissionForFeedback.get(MissionCounter).getTypeId() ==
				// msTypesId.get(i)) {
				// btnStreetOrBuildingType.setText(msTypesName.get(i));
				// TypeId = mMissionForFeedback.get(MissionCounter).getTypeId();
				// }
				// }

				for (int i = 0; i < Constant.StreetTypes.Id().size(); i++) {
					if (mMissionForFeedback.get(MissionCounter).getStreetTypeId() == Constant.StreetTypes.Id().get(i)) {
						tv_building_type_info_value.setText(Constant.StreetTypes.Name().get(i));

						if (firstStreetTypeId != null) {
							TypeId = firstStreetTypeId.intValue();
							tv_building_type_info_value.setText(Constant.StreetTypes.Name().get(TypeId));
							btnStreetOrBuildingType.setText("Seçilmiþ");
							btnStreetOrBuildingType.setEnabled(false);
							btnTypeStatus = true;
						}
						else {
							TypeId = mMissionForFeedback.get(MissionCounter).getStreetTypeId();
							btnStreetOrBuildingType.setEnabled(true);
						}
					}
				}

			}
			else if (mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 2) {

				Info.PHOTO_COUNT = 3;
				llImages.setWeightSum(Info.PHOTO_COUNT);

				tv_mission_status_value.setText("Dýþ Kapý No Görevi");

				// ----
				tv_apname_value.setVisibility(View.VISIBLE);
				new_et_apname_value.setVisibility(View.GONE);
				if (mMissionForFeedback.get(MissionCounter).getName() != null && !mMissionForFeedback.get(MissionCounter).getName().trim().equalsIgnoreCase("")) {
					tv_apname_value.setText(mMissionForFeedback.get(MissionCounter).getName() + " Apartmaný");
				}
				else {
					tv_apname_value.setText("");
				}
				tv_apname.setText("Bina Adý : ");
				// ------
				ll_mission_type_info.setVisibility(View.VISIBLE);

				tv_building_type_info.setText("Bina Tipi Bilgisi : ");

				tv_StreetOrBuildingType.setText("Bina Tipi : ");

				btnStreetOrBuildingType.setText("Seçiniz");
				et_independent_section_count.setText("" + mMissionForFeedback.get(MissionCounter).getIndependentSectionCount());
				et_floor_count.setText("");

				// ---
				ll_user_feedback.setVisibility(View.VISIBLE);

				// ---
				tv_apno.setVisibility(View.VISIBLE);
				ll_apno.setVisibility(View.VISIBLE);
				tv_apno_value.setVisibility(View.VISIBLE);
				new_et_apno_value.setVisibility(View.GONE);
				tv_apno.setText("Dýþ Kapý No : ");
				tv_apno_value.setText(mMissionForFeedback.get(MissionCounter).getBuildingNumber());
				// tv_apno_value.setText(mMissionForFeedback.get(MissionCounter).getBuildingNumber()
				// + " / " +
				// mMissionForFeedback.get(MissionCounter).getIndependentSectionType());

				tv_address.setVisibility(View.VISIBLE);
				tv_address_value.setVisibility(View.VISIBLE);
				tv_address_value.setText(mMissionForFeedback.get(MissionCounter).getAddressText().trim());

				// ---

				String nameSurname = mMissionForFeedback.get(MissionCounter).getPersonNameSurname();
				if (nameSurname == null) {
					nameSurname = "";
				}

				tv_persons.setVisibility(View.VISIBLE);
				tv_persons_values.setVisibility(View.VISIBLE);
				tv_persons_values.setText(nameSurname);

				tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" + mMissionForFeedback.size());

				// if (shapeControl != null || shapeControl.getListSize() > 0) {
				// tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" +
				// mMissionForFeedback.size() + "  [+" +
				// shapeControl.getListSize() + "]");
				// }
				// else {
				// tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" +
				// mMissionForFeedback.size() + "  [0]");
				// }

				tv_building_type_info_value.setText(mMissionForFeedback.get(MissionCounter).getIndependentSectionType());

				// for (int i = 0; i < Constant.BuildingTypes.Id().size(); i++)
				// {
				//
				// if
				// (mMissionForFeedback.get(MissionCounter).getIndependentSectionTypeId()
				// == Constant.BuildingTypes.Id().get(i)) {
				//
				// TypeId =
				// mMissionForFeedback.get(MissionCounter).getStreetTypeId();
				//
				// int BuildingType = Constant.BuildingTypes.Id().get(i);
				//
				// if (BuildingType == 16) { // tahsis
				// btnStreetOrBuildingType.setEnabled(false);
				// btnStreetOrBuildingType.setText("Seçim Yapýlamaz");
				// }
				// else if (BuildingType == 1 || BuildingType == 2 ||
				// BuildingType == 15) { // arsa,inþaat,bina dýþý yapý
				//
				// et_independent_section_count.setEnabled(false);
				// et_independent_section_count.setText("");
				// et_independent_section_count.setHint("Seçilemez");
				//
				// et_floor_count.setEnabled(false);
				// et_floor_count.setText("");
				// et_floor_count.setHint("Seçilemez");
				// }
				//
				// }
				//
				// }
			}

			// ###################################

			cbOnKapi.setChecked(false);
			cbSolKapi.setChecked(false);
			cbSagKapi.setChecked(false);

			LiveData.userDailyMissionId = mMissionForFeedback.get(MissionCounter).getUserDailyMissionId();

			ShowSelectedMarkers();
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

	}

	private void ShowSelectedMarkers() {
		if (LiveData.streetMarkers.size() > 0) {
			for (int i = 0; i < LiveData.streetMarkers.size(); i++) {
				try {
					if (LiveData.streetMarkers.get(i).getSnippet() != null && LiveData.streetMarkers.get(i).getTitle() != null && LiveData.streetMarkers.get(i).getPosition() != null) {
						MarkerOptions mo = new MarkerOptions();
						mo.snippet(LiveData.streetMarkers.get(i).getSnippet().trim());
						mo.title(LiveData.streetMarkers.get(i).getTitle().trim());
						mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
						mo.draggable(false);
						mo.position(LiveData.streetMarkers.get(i).getPosition());

						myMap.addMarker(mo);
					}
				}
				catch (Exception e) {
					Tools.saveErrors(e);
				}

			}
		}
	}

	// ################################### BUTTON CLICK ##############################################

	private void saveFeedback() {

		// if (firstStreetTypeId != null) {
		// btnTypeStatus = true;
		// }

		if (!Info.ISTEST) {

			if (!isMarkerSet) {
				Tools.showShortCustomToast(this, "Harita üzerinden konum seçilmedi!");
				return;
			}
			if (!btnTypeStatus) {
				Tools.showShortCustomToast(this, "Bina/Sokak Tipi seçilmedi.");
				return;
			}
			if ((et_floor_count.getText().toString().trim().equalsIgnoreCase("") && (isNewBuilding || mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 2)) //
					&& (TypeId != 1 && TypeId != 2 && TypeId != 12 && TypeId != 13 && TypeId != 15)) {
				Tools.showShortCustomToast(this, "Lütfen Kat Sayýsýný Giriniz.");
				return;
			}
			if (LiveData.photoinfo.size() != Info.PHOTO_COUNT) {
				Tools.showShortCustomToast(this, Info.PHOTO_COUNT + " adet fotoðraf çekilmelidir.");
				return;
			}

			if (!cbOnKapi.isChecked() && !cbSolKapi.isChecked() && !cbSagKapi.isChecked() && (isNewBuilding || mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 2)) {
				Tools.showShortCustomToast(this, "Kapýnýn bulunduðu cepheyi seçiniz.");
				return;
			}
		}

		// fillPolygon.add(polygonOptions);

		greenShapes.add((long) basarShapeId);

		if (finishedShapes != null) {
			finishedShapes.Insert();
		}

		if (!isNewBuilding) {
			MissionCounter++;
		}

		long lDate = new Date().getTime();
		int lDateTime = -(int) (lDate % ((long) Integer.MAX_VALUE));

		// (int) -lDateTime
		// int imageCount = 0;

		try {

			for (PhotoInfo photoInfo : LiveData.photoinfo) {

				MissionFeedBackPhoto mFeedBackPhoto = new MissionFeedBackPhoto();
				mFeedBackPhoto.setCreateDate(new Date());

				if (isNewBuilding) {
					mFeedBackPhoto.setUserDailyMissionId(lDateTime);
				}
				else {
					mFeedBackPhoto.setUserDailyMissionId(mMissionForFeedback.get(MissionCounter - 1).getUserDailyMissionId());
				}

				// mFeedBackPhoto.setPhoto(imgPreview1.getTag().toString());
				mFeedBackPhoto.setPhoto(photoInfo.getName());
				mFeedBackPhoto.Insert();
			}

		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		if (isNewBuilding) {

			try {
				MissionFeedBack mNewFeedback = new MissionFeedBack();

				mNewFeedback.setUserDailyMissionId(lDateTime);
				mNewFeedback.setStreetId(mMissionForFeedback.get(0).getStreetId());

				if (!new_et_apname_value.getText().toString().trim().equalsIgnoreCase("")) {
					mNewFeedback.setBuildingName(new_et_apname_value.getText().toString());
				}

				if (!new_et_apno_value.getText().toString().trim().equalsIgnoreCase("")) {
					mNewFeedback.setBuildingNumber(new_et_apno_value.getText().toString());
				}

				if (!et_floor_count.getText().toString().trim().equalsIgnoreCase("")) {
					mNewFeedback.setFloorCount(Integer.parseInt(et_floor_count.getText().toString()));
				}
				else {
					mNewFeedback.setFloorCount(0);
				}

				if (!et_independent_section_count.getText().toString().trim().equalsIgnoreCase("")) {
					mNewFeedback.setIndependentSectionCount(Integer.parseInt(et_independent_section_count.getText().toString()));
				}
				else {
					mNewFeedback.setIndependentSectionCount(0);
				}

				mNewFeedback.setIndependentSectionTypeId(TypeId);

				mNewFeedback.setIsCompleted(false);
				// mNewFeedback.setTypeId(TypeId);
				mNewFeedback.setGPSLat(GPSLat);
				mNewFeedback.setGPSLng(GPSLng);
				mNewFeedback.setGPSAltitude(GPSAlt);

				mNewFeedback.setGPSBearing(GPSBearing);
				mNewFeedback.setGPSSpeed(GPSSpeed);
				mNewFeedback.setGPSTime(GPSTime);

				mNewFeedback.setSignedLat(SignedLat);
				mNewFeedback.setSignedLng(SignedLng);
				mNewFeedback.setOutDoorPositionStatu(checkboxStatus);
				mNewFeedback.setOperationDate(new Date());

				mNewFeedback.setBasarShapeId(basarShapeId);
				basarShapeId = 0;
				mNewFeedback.Insert();

				new_et_apname_value.setText("");
				new_et_apno_value.setText("");

				currentMarker.setTitle(mMissionForFeedback.get(0).getName() + " Sokak, Dýþ Kapý No: " + new_et_apno_value.getText().toString());
				currentMarker.setSnippet(new_et_apname_value.getText().toString() + " Apartmaný");

				llImages.removeAllViews();
				LiveData.photoinfo = new ArrayList<PhotoInfo>();

				tabHost.setCurrentTab(0);
				btnTypeStatus = false;
				LiveData.streetMarkers.add(currentMarker);
				// myMap.clear();
				currentMarker.remove();
				currentMarker = null;

				isMarkerSet = false;

				// ShowSelectedMarkers();
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

			currentPolygon = null;

			// if (isFinishRedShapes) {
			fillComponent();
			// }
			// else {
			// AllMissionsCompleted();
			// }

			return;
		}

		llImages.removeAllViews();
		LiveData.photoinfo = new ArrayList<PhotoInfo>();

		if (mMissionForFeedback.get(MissionCounter - 1).getUserDailyMissionTypeId() == 1) {
			firstStreetTypeId = TypeId;
			// ((MissionsStreets)
			// mMissionForFeedback.get(MissionCounter)).Update();
			completedMissionStreets.add(((MissionsStreets) mMissionForFeedback.get(MissionCounter - 1)));
		}
		else {
			mMissionForFeedback.get(MissionCounter - 1).setIsCompleted(true);
			((MissionsBuildings) mMissionForFeedback.get(MissionCounter - 1)).Update();
		}

		MissionFeedBack mFeedBack = new MissionFeedBack();
		mFeedBack.setIsCompleted(false);
		mFeedBack.setUserDailyMissionId(mMissionForFeedback.get(MissionCounter - 1).getUserDailyMissionId());

		if (mMissionForFeedback.get(MissionCounter - 1).getUserDailyMissionTypeId() == 1) {
			mFeedBack.setTypeId(TypeId);
		}
		else {
			// mFeedBack.setBuildingName(mMissionForFeedback.get(MissionCounter
			// - 1).getName());
			// mFeedBack.setBuildingNumber(mMissionForFeedback.get(MissionCounter
			// - 1).getBuildingNumber());
			mFeedBack.setIndependentSectionTypeId(TypeId);

			if (!et_floor_count.getText().toString().equalsIgnoreCase("")) {
				mFeedBack.setFloorCount(Integer.parseInt(et_floor_count.getText().toString()));
			}
			else {
				mFeedBack.setFloorCount(0);
			}
			if (!et_independent_section_count.getText().toString().equalsIgnoreCase("")) {
				mFeedBack.setIndependentSectionCount(Integer.parseInt(et_independent_section_count.getText().toString()));
			}
			else {
				mFeedBack.setIndependentSectionCount(0);
			}
		}

		mFeedBack.setGPSLat(GPSLat);
		mFeedBack.setGPSLng(GPSLng);
		mFeedBack.setGPSAltitude(GPSAlt);
		mFeedBack.setStreetId(mMissionForFeedback.get(MissionCounter - 1).getStreetId());
		mFeedBack.setGPSBearing(GPSBearing);
		mFeedBack.setGPSSpeed(GPSSpeed);
		mFeedBack.setGPSTime(GPSTime);

		mFeedBack.setSignedLat(SignedLat);
		mFeedBack.setSignedLng(SignedLng);
		mFeedBack.setOutDoorPositionStatu(checkboxStatus);
		mFeedBack.setOperationDate(new Date());

		mFeedBack.setBasarShapeId(basarShapeId);
		basarShapeId = 0;

		mFeedBack.Insert();

		tabHost.setCurrentTab(0);
		btnTypeStatus = false;
		et_floor_count.setText("");
		et_independent_section_count.setText("");
		if (currentMarker != null) {
			LiveData.streetMarkers.add(currentMarker);
			// myMap.clear();
			currentMarker.remove();
			currentMarker = null;

			isMarkerSet = false;
		}

		currentPolygon = null;

		AllMissionsCompleted();
	}

	private void AllMissionsCompleted() {

		ShowSelectedMarkers();

		if (MissionCounter >= (mMissionForFeedback.size())) {

			// if (!shapeControl.isListEmpty()) {
			// Tools.showLongCustomToast(this,
			// "Tamamlanmamýþ Yeni Bina Görevleri Bulunmaktadýr.");
			// tabHost.setCurrentTab(0);
			// newBuildingMission();
			// isNewBuilding = true;
			// isFinishRedShapes = false;
			// return;
			// }

			Tools.showShortCustomToast(this, "Sokak Görevi Tamamlandý");
			LiveData.streetMarkers.clear();

			for (MissionsStreets str : completedMissionStreets) {
				str.setIsCompleted(true);
				str.Update();
			}

			finish();
		}
		else {
			fillComponent(); // sonraki görevin bilgileri yükleniyor
		}
	}

	private void selectType() { // butona sokak verileri mi bina verileri mi yükleneceði belirleniyor

		AlertDialog.Builder builder = new AlertDialog.Builder(FeedBack.this);
		builder.setTitle("Seçim Yapýnýz");

		if (isNewBuilding) {
			selectType = 2;
		}
		else {
			selectType = mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId();
		}

		if (selectType == 1) {

			Constant.StreetTypes.Id().get(0);

			// CharSequence[] choiceList = msTypesName.toArray(new
			// CharSequence[msTypesName.size()]);

			CharSequence[] choiceList = Constant.StreetTypes.Name().toArray(new CharSequence[Constant.StreetTypes.Name().size()]);
			builder.setItems(choiceList, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					btnTypeStatus = true;

					TypeId = Constant.StreetTypes.Id().get(item);
					btnStreetOrBuildingType.setText(Constant.StreetTypes.Name().get(item));

					// TypeId = msTypesId.get(item);
					// btnStreetOrBuildingType.setText(msTypesName.get(item));
				}
			});

		}
		else if (selectType == 2) {

			CharSequence[] choiceList = Constant.BuildingTypes.Name().toArray(new CharSequence[Constant.BuildingTypes.Name().size()]);

			builder.setItems(choiceList, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					btnTypeStatus = true;

					TypeId = Constant.BuildingTypes.Id().get(item);
					btnStreetOrBuildingType.setText(Constant.BuildingTypes.Name().get(item));

					if (TypeId == 1 || TypeId == 2 || TypeId == 12 || TypeId == 13 || TypeId == 15) {
						et_independent_section_count.setEnabled(false);
						et_independent_section_count.setText("");
						et_independent_section_count.setHint("Seçilemez");

						et_floor_count.setEnabled(false);
						et_floor_count.setText("");
						et_floor_count.setHint("Seçilemez");

						new_et_apno_value.setText("");
					}
					else {
						et_independent_section_count.setEnabled(true);
						et_independent_section_count.setText("" + mMissionForFeedback.get(MissionCounter).getIndependentSectionCount());
						et_independent_section_count.setHint("");

						et_floor_count.setEnabled(true);
						et_floor_count.setText("");
						et_floor_count.setHint("");
					}

					// TypeId = mbTypesId.get(item);
					// btnStreetOrBuildingType.setText(mbTypesName.get(item));

				}
			});

		}

		AlertDialog alert = builder.create();
		alert.show();

	}

	// ################################### TABS
	// ########################################################

	private void tabsProperties() {
		tabHost = (TabHost) findViewById(R.id.tabSecim);
		tabHost.setup();

		setupTab(R.id.tab1, R.drawable.info1, "Bilgi");
		setupTab(R.id.tab2, R.drawable.tick, "Foto");
		setupTab(R.id.tab3, R.drawable.camera1, "Kapý");

		tabHost.getTabWidget().getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(0, 60, 0.33f));
		tabHost.getTabWidget().getChildAt(1).setLayoutParams(new LinearLayout.LayoutParams(0, 60, 0.34f));
		tabHost.getTabWidget().getChildAt(2).setLayoutParams(new LinearLayout.LayoutParams(0, 60, 0.33f));

		tabHost.setCurrentTab(valueTab);
		tabHost.setOnTabChangedListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LiveData.photoinfo = new ArrayList<PhotoInfo>();
		llImages.removeAllViews();
	}

	private void setupTab(int tab, int icon, final String text) {

		View view = LayoutInflater.from(tabHost.getContext()).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setTextSize(25);
		// tv.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
		tv.setText(text);

		TabSpec setContent = tabHost.newTabSpec(text).setIndicator(view).setContent(tab);
		tabHost.addTab(setContent);

	}

	@Override
	public void onTabChanged(String tabId) {

		if (!Info.ISTEST) {

			if (tabId.equals("Bilgi")) {

			}
			else if (tabId.equals("Foto")) {
				if (!isMarkerSet) {
					Tools.showShortCustomToast(this, "Harita üzerinden konum seçilmedi!");
					tabHost.setCurrentTab(0);
					return;
				}
			}
			else if (tabId.equals("Kapý")) {

				if (mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 1 && !isNewBuilding) {
					tabHost.setCurrentTab(1);
					return;
				}

				if (LiveData.photoinfo.size() != Info.PHOTO_COUNT) {
					tabHost.setCurrentTab(1);
					return;
				}
				if (!isMarkerSet) {
					tabHost.setCurrentTab(1);
					return;
				}
			}
		}

	}

	// ################################### GOOGLE MAP ##############################################

	@Override
	public boolean onMarkerClick(Marker marker) {

		return false;
	}

	@Override
	public void onMapLongClick(LatLng point) {

		if (isZoomOpen) {
			return;
		}

		if (!Info.ISTEST) {
			if (LiveData.photoinfo.size() > 0) {
				Tools.showShortCustomToast(this, "Fotoðraf çekildikten sonra konum güncellenemez!");
				return;
			}
		}
		else {
			UserAccuracy = 1;
		}

		if (UserAccuracy > Info.GPS_ACCURACY) {
			Tools.showShortCustomToast(this, "GPS deðeri çok büyük!");
			return;
		}

		if (currentPolygon != null) {
			currentPolygon.remove();
			currentPolygon = null;
		}

		if (currentMarker != null) {
			currentMarker.remove();
			currentMarker = null;
		}

		MarkerOptions mo = new MarkerOptions();
		mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

		if (isNewBuilding) {

			if (!isPressShape(point)) {
				Tools.showShortCustomToast(this, "Lütfen kýrmýzý þekillere týklayýnýz!");
				return;
			}

		}
		else {

			if (mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 1) {

				if (!mMissionForFeedback.get(MissionCounter).getName().trim().equalsIgnoreCase("")) {
					mo.snippet(mMissionForFeedback.get(MissionCounter).getName() + " Sokak");
				}
				else {
					mo.snippet("");
				}

			}

			else if (mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 2) {

				isPressShape(point);

				if (!mMissionForFeedback.get(MissionCounter).getName().trim().equalsIgnoreCase("")) {
					mo.snippet(mMissionForFeedback.get(MissionCounter).getName() + " Apartmaný");
				}
				else {
					mo.snippet("");
				}
			}

			mo.title(mMissionForFeedback.get(MissionCounter).getAddressText());

		}

		mo.position(point);
		currentMarker = myMap.addMarker(mo);

		SignedLat = point.latitude;
		SignedLng = point.longitude;

		isMarkerSet = true;

	}

	private Boolean isPressShape(LatLng point) {

		Boolean isPolygonSelected = false;

		try {
			for (com.telekurye.kml.Polygon pol : polygons) {
				if (PolyUtil.containsLocation(point, pol.coors, true) && pol.type == 2) {

					for (Long greenshapes : greenShapes) {
						if (pol.polygonid.equals(greenshapes)) {

							return false;
						}
					}

					isPolygonSelected = true;
					basarShapeId = pol.polygonid.intValue();
					final PolygonOptions polygonOptions = new PolygonOptions();
					String[] coors = SplitUsingTokenizer(pol.coordinates, "||");

					for (String string : coors) {
						String[] coor = string.split(",");
						final LatLng p = new LatLng(Float.valueOf(coor[0]), Float.valueOf(coor[1]));
						polygonOptions.add(p);
					}

					// shapeControl.CompareShapes(pol);

					finishedShapes = new FinishedShapeHistory();
					finishedShapes.setShapeId(pol.polygonid);
					finishedShapes.setUserId(Info.UserId);
					finishedShapes.setUserDailyMissionId(mMissionForFeedback.get(MissionCounter).getUserDailyMissionId());

					polygonOptions.strokeColor(Color.GREEN);
					polygonOptions.fillColor(0x802EFE64);

					// if (shapeControl.CompareShapes(pol)) {
					// polygonOptions.strokeColor(Color.RED);
					// polygonOptions.fillColor(0x802EFE64);
					// // polygonOptions.fillColor(0x80FF0000);
					// }
					// else {
					// polygonOptions.strokeColor(Color.GREEN);
					// polygonOptions.fillColor(0x802EFE64);
					// }

					polygonOptions.strokeWidth(3);

					// polyOptions = polygonOptions;
					currentPolygon = myMap.addPolygon(polygonOptions);

				}
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		// if (!isPolygonSelected) {
		// Tools.showShortCustomToast(this, "Lütfen yapý üzerine týklayýnýz!");
		// }

		return isPolygonSelected;
	}

	@Override
	public void onMarkerDrag(Marker marker) {

		if (isZoomOpen) {
			return;
		}
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {

		if (isZoomOpen) {
			return;
		}

		// markerLastPoint = marker;
		SignedLat = marker.getPosition().latitude;
		SignedLng = marker.getPosition().longitude;

	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		if (isZoomOpen) {
			return;
		}
	}

	@Override
	public void onMapClick(LatLng point) {

		if (isZoomOpen) {
			return;
		}

	}

	private Boolean showMapOnActivity() {

		boolean isGPSEnabled = false;
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (isGPSEnabled) {

			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Info.GPS_UPDATE_TIME, Info.GPS_MIN_DISTANCE, this);
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			locationManager.addGpsStatusListener(this);

		}

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS) {

			if (map == null)
				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

			if (myMap == null)
				myMap = map.getMap();
			myMap.clear();
			LiveData.streetMarkers.clear();

			myMap.setMyLocationEnabled(true);
			myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			myMap.setOnMapLongClickListener(this);
			myMap.setOnMarkerClickListener(this);
			myMap.setOnMarkerDragListener(this);
			myMap.setOnCameraChangeListener(this);

		}
		else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000).show();

			}
			return false;
		}
		return true;
	}

	private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
		int intersectCount = 0;
		for (int j = 0; j < vertices.size() - 1; j++) {
			if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
				intersectCount++;
			}
		}

		return ((intersectCount % 2) == 1);
	}

	private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

		double aY = vertA.latitude;
		double bY = vertB.latitude;
		double aX = vertA.longitude;
		double bX = vertB.longitude;
		double pY = tap.latitude;
		double pX = tap.longitude;

		if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
			return false;
		}

		double m = (aY - bY) / (aX - bX);
		double bee = (-aX) * m + aY;
		double x = (pY - bee) / m;

		return x > pX;
	}

	// ######################## CAMERA #############################

	private void takePhoto() {

		try {
			if (LiveData.photoinfo.size() < Info.PHOTO_COUNT) {
				tp = new CameraHelper(FeedBack.this);
				startActivityForResult(tp.startCamera(), tp.getRequestCode());
			}
			else {
				Tools.showShortCustomToast(FeedBack.this, "Fotoðraf ekleme limiti doldu!");
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

	}

	// salihy: TODO: burayý düzenle
	private void selectImage(final PhotoInfo pInfo) {
		final CharSequence[] items = { "Fotoðrafý Sil", "Vazgeç" };

		AlertDialog.Builder builder = new AlertDialog.Builder(FeedBack.this);
		builder.setTitle("Düzenle");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				try {
					if (items[item].equals("Fotoðrafý Sil")) {

						llImages.removeView(pInfo.getImageview());
						// pInfo.getImageview().setImageResource(android.R.color.transparent);
						// photoStatus1 = false;
						File file = new File(Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH + File.separator + pInfo.getName());
						file.delete();
						LiveData.photoinfo.remove(pInfo);
					}

					else if (items[item].equals("Vazgeç")) {
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			if (LiveData.photoinfo.size() < Info.PHOTO_COUNT && tp != null && resultCode == Activity.RESULT_OK) {
				ImageView iv = new ImageView(this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
				iv.setLayoutParams(layoutParams);
				// tp = new CameraHelper(FeedBack.this);
				final PhotoInfo info = tp.showImage(iv, 1, requestCode, resultCode);
				llImages.addView(iv);

				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						selectImage(info);
					}
				});

				LiveData.photoinfo.add(info);

			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

	}

	// ######################## GPS LOCATION ###########################################

	@Override
	public void onLocationChanged(Location location) {

		currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

		try {
			if (mPositionMarker != null) {
				mPositionMarker.remove();
				mPositionMarker = null;
				mPositionMarker = myMap.addMarker(getMarkerOptions(currentLocation));
			}
			else {
				mPositionMarker = myMap.addMarker(getMarkerOptions(currentLocation));
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		StatusBarValues();

		mLastLocationMillis = SystemClock.elapsedRealtime();
		mLastLocation = location;

		if (!Info.ISTEST && !isZoomOpen) { // *-*
			myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, Info.MAP_ZOOM_LEVEL), 1000, new CancelableCallback() {

				@Override
				public void onFinish() {
					Projection projection = myMap.getProjection();
					android.graphics.Point point = projection.toScreenLocation(currentLocation);
					LatLng offsetPosition = projection.fromScreenLocation(point);
					myMap.animateCamera(CameraUpdateFactory.newLatLng(offsetPosition), 300, null);
				}

				@Override
				public void onCancel() {
				}
			});
		}

		GPSLat = location.getLatitude();
		GPSLng = location.getLongitude();
		GPSAlt = location.getAltitude();

		GPSBearing = (double) location.getBearing();
		GPSSpeed = (double) location.getSpeed();
		GPSTime = new Date(location.getTime());

		UserAccuracy = location.getAccuracy();

		// try {
		// if (mPositionMarker == null) {
		// mPositionMarker = myMap.addMarker(getMarkerOptions(currentLocation));
		//
		// }
		// else {
		// mPositionMarker.setPosition(currentLocation);
		// }
		// }
		// catch (Exception e) {
		// Tools.saveErrors(e);
		// }

		if (tempLocation == null || (tempLocation.getLatitude() != location.getLatitude() && tempLocation.getLongitude() != location.getLongitude())) {
			try {
				VisitFeedBack mVisitFeedBack = new VisitFeedBack();
				mVisitFeedBack.setCourierId(Info.UserId);
				mVisitFeedBack.setCreateDate(new Date());
				mVisitFeedBack.setDeviceId(Integer.parseInt(Info.IMEI));
				mVisitFeedBack.setGPSAccuricy(UserAccuracy);
				mVisitFeedBack.setGPSLat(GPSLat);
				mVisitFeedBack.setGPSLng(GPSLng);
				mVisitFeedBack.setGPSAltitude(GPSAlt);
				mVisitFeedBack.setGPSBearing(GPSBearing);
				mVisitFeedBack.setGPSSpeed(GPSSpeed);
				mVisitFeedBack.setGPSTime(GPSTime);
				mVisitFeedBack.setUserDailyMissionId(LiveData.userDailyMissionId);
				mVisitFeedBack.setSimCardNo(Tools.getSimCardNumber(FeedBack.this));
				mVisitFeedBack.Insert();
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

			tempLocation = location;
		}
		// refreshMap();
	}

	@Override
	public void onCameraChange(CameraPosition position) {

		// float maxZoom = 20.0f;
		// float minZoom = 17.0f;
		//
		// if ((position.zoom > maxZoom)) {
		// myMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
		// } else if (position.zoom < minZoom) {
		// myMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));
		// }

		// for (Polygon pol : polList) {
		// pol.remove();
		// }
		// polList = new ArrayList<Polygon>();

		// Salihy: Sefa bu kontrolü hiç bir koþulda açma!
		// if (!Info.ISTEST)
		// {
		// if (myMap.getCameraPosition().zoom < Info.MAP_ZOOM_LEVEL) {
		// return;
		// }
		// }

		// progress.show();

		// final LatLngBounds bounds =
		// myMap.getProjection().getVisibleRegion().latLngBounds;
		// final ArrayList<PolygonOptions> polygonOptionsList = new
		// ArrayList<PolygonOptions>();
		//
		// new Thread(new Runnable() {
		// public void run() {
		// final Context ctx = getApplicationContext();
		//
		// // polygons = com.telekurye.kml.Polygon.GetAround((float)
		// bounds.southwest.latitude, (float) bounds.northeast.latitude, (float)
		// bounds.southwest.longitude,
		// // (float) bounds.northeast.longitude, ms.getDistrictId(),
		// ShapeIdList, ctx, db_path, db_name);
		//
		// polygons = com.telekurye.kml.Polygon.GetByDistrictId(ctx,
		// (long)ms.getDistrictId(), db_path, db_name);
		//
		// int visibleShapeCount;
		// if (isZoomOpen) {
		// visibleShapeCount = 600;
		// }
		// else {
		// visibleShapeCount = 300;
		// }
		//
		// for (Polygon poly : SelectedPolygonList) {
		// poly.setVisible(true);
		// }
		//
		// for (com.telekurye.kml.Polygon polygon : polygons) {
		// polygon.coors = new ArrayList<LatLng>();
		//
		// final PolygonOptions polygonOptions = new PolygonOptions();
		//
		// String[] coors = SplitUsingTokenizer(polygon.coordinates, "||");
		//
		// for (String string : coors) {
		// String[] coor = string.split(",");
		//
		// final LatLng point = new LatLng(Float.valueOf(coor[0]),
		// Float.valueOf(coor[1]));
		//
		// polygon.coors.add(point);
		//
		// polygonOptions.add(point);
		// }
		//
		// // if (shapeControl.weHaveThisShape(polygon)) {
		// // polygonOptions.strokeColor(Color.RED);
		// // }
		// // else {
		// // polygonOptions.strokeColor(Color.GREEN);
		// // }
		// polygonOptions.strokeColor(Color.RED);
		//
		// polygonOptions.fillColor(Color.TRANSPARENT);
		//
		// for (Long shapeId : shapeIdHistory) {
		// if (shapeId.equals(polygon.polygonid)) {
		// polygonOptions.fillColor(0x802EFE64);
		// }
		// }
		//
		// polygonOptions.strokeWidth(3);
		// polygonOptionsList.add(polygonOptions);
		// }
		//
		// if (ctx != null) {
		// runOnUiThread(new Runnable() {
		// public void run() {
		// GoogleMap map = myMap;
		// for (int i = 0; i < polygonOptionsList.size(); i++) {
		// polList.add(map.addPolygon(polygonOptionsList.get(i)));
		// }
		// // progress.dismiss();
		// }
		// });
		// }
		// }
		// }).start();
	}

	public void LoadShapes() {

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}

		progressDialog = new ProgressDialog(FeedBack.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("Lütfen Bekleyiniz...");
		progressDialog.setMessage("Þekiller Yükleniyor...");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(false);
		progressDialog.show();

		try {
			shapeIdHistory = new FinishedShapeHistory().GetShapeIdList(Info.UserId);
			shapeListFromHost = new BasarShapeId().GetAllData();

			greenShapes = new ArrayList<Long>();

			greenShapes.addAll(shapeIdHistory);

			for (BasarShapeId basarShapeId : shapeListFromHost) {
				greenShapes.add(basarShapeId.getBasarShapeId());
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		final ArrayList<PolygonOptions> polygonOptionsList = new ArrayList<PolygonOptions>();
		final ArrayList<PolylineOptions> polylineOptionsList = new ArrayList<PolylineOptions>();

		// Bina ve sokak shapeleri basýlýyor
		new Thread(new Runnable() {
			public void run() {
				final Context ctx = getApplicationContext();

				// polygons = com.telekurye.kml.Polygon.GetAround((float)
				// bounds.southwest.latitude, (float) bounds.northeast.latitude,
				// (float) bounds.southwest.longitude,
				// (float) bounds.northeast.longitude, ms.getDistrictId(),
				// ShapeIdList, ctx, db_path, db_name);

				List<MissionsStreets> missStreets = MissionsStreets.GetAllDataForShape();
				List<Integer> missionStreetIdList = new ArrayList<Integer>();

				for (MissionsStreets str : missStreets) {
					missionStreetIdList.add(new Integer(str.getStreetId()));
				}

				try {
					polygons = new ArrayList<com.telekurye.kml.Polygon>();
					polygons.addAll(com.telekurye.kml.Polygon.GetStreetShapeByStreetIdList(ctx, missionStreetIdList, db_path, db_name));
					polygons.addAll(com.telekurye.kml.Polygon.GetBuildingShapeByDistrictId(ctx, (long) ms.getDistrictId(), db_path, db_name));
				}
				catch (Exception e) {
					Tools.saveErrors(e);
				}

				for (Polygon poly : SelectedPolygonList) {
					poly.setVisible(true);
				}

				for (com.telekurye.kml.Polygon polygon : polygons) {
					polygon.coors = new ArrayList<LatLng>();

					if (polygon.type == 1) {
						final PolylineOptions polygonOptions = new PolylineOptions();

						String[] coors = SplitUsingTokenizer(polygon.coordinates, "||");

						for (String string : coors) {
							String[] coor = string.split(",");

							final LatLng point = new LatLng(Float.valueOf(coor[0]), Float.valueOf(coor[1]));

							polygon.coors.add(point);

							polygonOptions.add(point);
						}

						if (polygon.polygonid == ms.getStreetId()) {
							polygonOptions.color(Color.YELLOW);
						}
						else if (CheckCompletionStatus(polygon, missStreets)) {
							polygonOptions.color(Color.GREEN);
						}
						else {
							polygonOptions.color(Color.WHITE);
						}

						polygonOptions.width(3);
						polylineOptionsList.add(polygonOptions);
					}
					else if (polygon.type == 2) {
						final PolygonOptions polygonOptions = new PolygonOptions();

						String[] coors = SplitUsingTokenizer(polygon.coordinates, "||");

						for (String string : coors) {
							String[] coor = string.split(",");

							final LatLng point = new LatLng(Float.valueOf(coor[0]), Float.valueOf(coor[1]));

							polygon.coors.add(point);

							polygonOptions.add(point);
						}

						polygonOptions.strokeColor(Color.RED);

						polygonOptions.fillColor(Color.TRANSPARENT);

						for (Long shapeId : shapeIdHistory) {
							if (shapeId.equals(polygon.polygonid)) {
								polygonOptions.fillColor(0x802EFE64);
							}
						}

						for (BasarShapeId basarShapeId : shapeListFromHost) {
							if (basarShapeId.getBasarShapeId().equals(polygon.polygonid)) {
								polygonOptions.strokeColor(Color.GREEN);
							}
						}
						polygonOptions.strokeWidth(3);
						polygonOptionsList.add(polygonOptions);
					}
				}

				if (ctx != null) {
					runOnUiThread(new Runnable() {
						public void run() {
							GoogleMap map = myMap;
							for (int i = 0; i < polygonOptionsList.size(); i++) {
								polList.add(map.addPolygon(polygonOptionsList.get(i)));
							}

							for (int i = 0; i < polylineOptionsList.size(); i++) {
								polylineList.add(map.addPolyline(polylineOptionsList.get(i)));
							}
							if (progressDialog != null) {
								progressDialog.dismiss();
								progressDialog = null;
							}
						}
					});
				}
			}

			private boolean CheckCompletionStatus(com.telekurye.kml.Polygon polygon, List<MissionsStreets> missStreets) {

				for (MissionsStreets mstr : missStreets) {
					if (polygon.polygonid == mstr.getStreetId()) {

						if (mstr.getIsCompleted()) {
							return true;
						}
						else {
							return false;
						}
					}
				}

				return false;
			}
		}).start();
	}

	public float distance(float lat_a, float lng_a, float lat_b, float lng_b) { // iki koordinat arasý uzaklýk (metre cinsinden)
		double earthRadius = 3958.75;
		double latDiff = Math.toRadians(lat_b - lat_a);
		double lngDiff = Math.toRadians(lng_b - lng_a);
		double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) * Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;

		int meterConversion = 1609;

		return new Float(distance * meterConversion).floatValue();
	}

	public static String[] SplitUsingTokenizer(String subject, String delimiters) {
		StringTokenizer strTkn = new StringTokenizer(subject, delimiters);
		ArrayList<String> arrLis = new ArrayList<String>(subject.length());

		while (strTkn.hasMoreTokens())
			arrLis.add(strTkn.nextToken());

		return arrLis.toArray(new String[0]);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	// ###############################################################

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_save_feedback) {
			saveFeedback();
		}
		else if (v.getId() == R.id.btn_map_zoom) {
			mapZoom();
		}
		else if (v.getId() == R.id.btnSavePhoto) {
			takePhoto();
		}
		else if (v.getId() == R.id.btn_StreetOrBuildingType) {
			selectType();
		}
		else if (v.getId() == R.id.cbOnKapi) {
			if (((CheckBox) v).isChecked()) {
				cbSagKapi.setChecked(false);
				cbSolKapi.setChecked(false);
				checkboxStatus = 1;
			}
			else {
				cbOnKapi.setChecked(true);
			}
		}
		else if (v.getId() == R.id.cbSagKapi) {
			if (((CheckBox) v).isChecked()) {
				cbOnKapi.setChecked(false);
				cbSolKapi.setChecked(false);
				checkboxStatus = 2;
			}
			else {
				cbSagKapi.setChecked(true);
			}
		}
		else if (v.getId() == R.id.cbSolKapi) {
			if (((CheckBox) v).isChecked()) {
				cbSagKapi.setChecked(false);
				cbOnKapi.setChecked(false);
				checkboxStatus = 3;
			}
			else {
				cbSolKapi.setChecked(true);
			}
		}

	}

	private void mapZoom() {

		if (isZoomOpen) {
			isZoomOpen = false;
		}
		else {
			isZoomOpen = true;
		}

		if (isZoomOpen) {
			tabHost.setVisibility(View.GONE);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			llMapFragment.setLayoutParams(layoutParams);

			// uiSettings.setAllGesturesEnabled(true);
			uiSettings.setRotateGesturesEnabled(true);
			uiSettings.setScrollGesturesEnabled(true);
			uiSettings.setTiltGesturesEnabled(false);
			uiSettings.setZoomGesturesEnabled(false);

			uiSettings.setZoomControlsEnabled(false);
			uiSettings.setCompassEnabled(true);
			uiSettings.setIndoorLevelPickerEnabled(true);
			uiSettings.setMyLocationButtonEnabled(true);
			uiSettings.setMapToolbarEnabled(false);
			btnMapZoom.setBackgroundColor(Color.argb(255, 0, 150, 45));

			Info.MAP_ZOOM_LEVEL = 18.0f;

		}
		else {
			tabHost.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 45f);
			llMapFragment.setLayoutParams(layoutParams2);

			// salihy: test ederken sýkýntý yaratýyordu.
			if (!Info.ISTEST) {
				uiSettings.setAllGesturesEnabled(false);
				uiSettings.setZoomControlsEnabled(false);
				uiSettings.setCompassEnabled(true);
				uiSettings.setIndoorLevelPickerEnabled(true);
				uiSettings.setMyLocationButtonEnabled(true);
				uiSettings.setMapToolbarEnabled(false);
			}

			btnMapZoom.setBackgroundColor(Color.RED);

			Info.MAP_ZOOM_LEVEL = 20.0f;

		}

		// uiSettings.setAllGesturesEnabled(true); // *-*
		// uiSettings.setZoomControlsEnabled(true); // *-*

		myMap.animateCamera(CameraUpdateFactory.zoomTo(Info.MAP_ZOOM_LEVEL));
	}

	@Override
	public void onGpsStatusChanged(int event) {

		switch (event) {
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			if (mLastLocation != null)
				isGPSFix = (SystemClock.elapsedRealtime() - mLastLocationMillis) < 3000;

			if (isGPSFix) { // A fix has been acquired.

			}
			else {
				UserAccuracy = 5000;
			}

			break;
		}

		isGPSFix = true;
	}

	float	lastDegreeX	= 0;

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = Math.round(event.values[0] - 90) % 360;

		try {
			if (Math.abs(lastDegreeX - x) > 1) {
				if (mPositionMarker != null) {
					mPositionMarker.setRotation(x);
					lastDegreeX = x;
				}
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPressed() {

		if (Info.ISTEST) {
			finish();
		}
		else {
			return;
		}

		// finish();
		// Intent i = new Intent(FeedBack.this, ExpandableList.class);
		// startActivity(i);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (uiUpdateThread != null && uiUpdateThread.isAlive()) {
			uiUpdateThread.interrupt();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		uiUpdateThread = StatusBarThread();

		if (!uiUpdateThread.isAlive()) {
			uiUpdateThread.start();
		}

	}

	private Thread StatusBarThread() {

		final Handler mHandler = new Handler();

		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {

				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (!Thread.interrupted()) {
							StatusBarValues();
							mHandler.postDelayed(this, Info.SYNCPERIOD / 2);
						}
					}
				});

			}
		});

		return th;
	}

	private void StatusBarValues() {

		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		String mHour = null, mMinute = null;

		if (hour < 10) {
			mHour = "0" + hour;
		}
		else {
			mHour = "" + hour;
		}

		if (minute < 10) {
			mMinute = "0" + minute;
		}
		else {
			mMinute = "" + minute;
		}

		String time = "Saat : " + mHour + ":" + mMinute;
		String battery = "Pil : %" + Tools.getBatteryLevel(FeedBack.this);

		tv_info_time.setText(time);
		tv_info_battery.setText(battery);

		tv_networkStatus.setText(Tools.getNetworkType(FeedBack.this));

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			tv_networkStatus.setBackgroundColor(Color.RED);
		}
		else {
			tv_networkStatus.setBackgroundColor(Color.GREEN);
		}

		if (LiveData.Earnings != null) {

			tv_earnings.setText(LiveData.Earnings);
		}
		else {
			tv_earnings.setText("Bilgi Yok");
		}

		if (isGPSFix && UserAccuracy != 0 && UserAccuracy < 100) {
			tv_info_accuracy.setText("" + UserAccuracy);
		}
		else {
			tv_info_accuracy.setText("Gps Yok");
		}

		if (UserAccuracy < Info.GPS_ACCURACY) {
			tv_info_accuracy.setBackgroundColor(Color.GREEN);
		}
		else {
			tv_info_accuracy.setBackgroundColor(Color.RED);
		}

		String lastSync = new SyncTime().getRow(0).getLastSyncDate();

		if (lastSync != null) {
			Date lastSyncDate = null;
			try {
				lastSyncDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(lastSync);
			}
			catch (ParseException e) {
				Tools.saveErrors(e);
			}

			Calendar calendar2 = Calendar.getInstance();

			Date dt = new Date();

			long timeRange = (dt.getTime() - lastSyncDate.getTime()) - (1000 * 60 * 60 * 2);
			// calendar2.setTimeInMillis(timeRange);

			calendar2.setTimeInMillis(timeRange);

			int saat = calendar2.get(Calendar.HOUR_OF_DAY);
			int dk = calendar2.get(Calendar.MINUTE);

			if (saat > 0) {
				tv_last_sync_date.setText("Son Senk. : " + saat + " saat " + dk + " dk önce");
			}
			else {
				tv_last_sync_date.setText("Son Senk. : " + dk + " dk önce");
			}
			if (saat == 0 && dk <= 30) {
				tv_last_sync_date.setBackgroundColor(Color.GREEN);
			}
			else {
				tv_last_sync_date.setBackgroundColor(Color.RED);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 1 && mMissionForFeedback.get(MissionCounter).getBuildingNumber_IsOdd()) {
			Tools.showShortCustomToast(FeedBack.this, "Ýlk Sokak Görevinde Yeni Bina ekleyemezsiniz");
			return false;
		}

		if (item.getItemId() == R.id.new_building_menu) {

			if (!isNewBuilding) {
				newBuildingMission();
				tabHost.setCurrentTab(0);
				isNewBuilding = true;
			}
		}
		else if (item.getItemId() == R.id.new_building_menu2) {

			if (isNewBuilding && isFinishRedShapes) {
				fillComponent();
				tabHost.setCurrentTab(0);
				// isNewBuilding = false;
			}
			else {
				closeOptionsMenu();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void newBuildingMission() {

		if (currentMarker != null) {
			currentMarker.remove();
			currentMarker = null;
		}

		if (currentPolygon != null) {
			currentPolygon.remove();
			currentPolygon = null;
		}

		isMarkerSet = false;

		Info.PHOTO_COUNT = 3;
		llImages.setWeightSum(Info.PHOTO_COUNT);

		tv_mission_status_value.setText("Yeni Dýþ Kapý No Görevi");

		tv_apname.setText("Bina Adý :");
		tv_apname_value.setVisibility(View.GONE);
		new_et_apname_value.setVisibility(View.VISIBLE);
		new_et_apname_value.setText("");
		new_et_apname_value.setHint("Bina adý var ise buraya yazýnýz...");

		btnStreetOrBuildingType.setText("Seçiniz");

		btnStreetOrBuildingType.setEnabled(true);

		ll_mission_type_info.setVisibility(View.GONE);
		ll_apno.setVisibility(View.VISIBLE);
		tv_apno.setVisibility(View.VISIBLE);
		tv_apno.setText("Dýþ Kapý No :");
		tv_apno_value.setVisibility(View.GONE);
		new_et_apno_value.setVisibility(View.VISIBLE);
		new_et_apno_value.setText("");
		new_et_apno_value.setHint("Giriniz");

		ll_user_feedback.setVisibility(View.VISIBLE);
		et_floor_count.setText("");
		et_independent_section_count.setText("");

		tv_address_value.setVisibility(View.GONE);
		tv_address.setVisibility(View.GONE);

		tv_persons.setVisibility(View.GONE);
		tv_persons_values.setVisibility(View.GONE);

		tv_StreetOrBuildingType.setText("Bina Tipi : ");

		cbOnKapi.setChecked(false);
		cbSolKapi.setChecked(false);
		cbSagKapi.setChecked(false);

		et_independent_section_count.setEnabled(true);
		et_independent_section_count.setText("");
		et_independent_section_count.setHint("Giriniz");

		et_floor_count.setEnabled(true);
		et_floor_count.setText("");
		et_floor_count.setHint("Giriniz");

	}

	private MarkerOptions getMarkerOptions(LatLng position) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.flat(true);
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrowred));
		markerOptions.anchor(0.5f, 0.5f);
		markerOptions.position(position);

		return markerOptions;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
