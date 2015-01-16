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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.net.Uri;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
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
import com.telekurye.utils.PhotoInfo;
import com.telekurye.utils.Point;
import com.telekurye.utils.ShapeControl;

public class FeedBack extends Activity implements OnTabChangeListener, android.location.GpsStatus.Listener, OnCameraChangeListener, OnMarkerDragListener, LocationListener, OnMapClickListener,
		OnMapLongClickListener, OnMarkerClickListener, OnClickListener, SensorEventListener {

	// List<PolygonOptions> fillPolygon;
	// PolygonOptions polyOptions;

	List<Long>						shapeIdHistory;
	FinishedShapeHistory			finishedShapes;

	int								selectedBuildingType				= 0;
	int								selectedFloorCount					= 0;

	private Boolean					isZoomOpen							= false;

	LinearLayout					llMapFragment;

	private Dialog					dialogZoom;

	Integer							firstStreetTypeId					= null;

	LatLng							currentLocation;

	ShapeControl					shapeControl;
	private List<Integer>			ShapeIdList							= null;
	private Boolean					isFinishRedShapes					= true;

	Marker							mPositionMarker;

	Thread							thread;
	int								selectType;
	Boolean							isNewBuilding						= false;

	private SensorManager			mSensorManager;

	CameraHelper					tp;
	List<PhotoInfo>					photoInfo;

	// ------ GOOGLE MAP -------
	private final int				RQS_GooglePlayServices				= 1;
	private GoogleMap				myMap;

	private Location				myLocation;
	private TextView				tvLocInfo;
	private boolean					markerClicked;
	private PolygonOptions			polygonOptions;
	private Polygon					polygon;
	private ArrayList<LatLng>		points;
	private ArrayList<LatLng>		points2;
	private Point					p1, p2;
	private Boolean					isThereAMarker						= true;
	private Boolean					isMarkerDrag						= false;
	// private Marker markerLastPoint;
	private Marker					currentMarker;
	private int						basarShapeId						= 0;
	private Boolean					isMarkerSet							= false;
	MapFragment						map;

	List<com.telekurye.kml.Polygon>	polygons;
	private String					db_path								= "/data/data/com.telekurye.mobileui/databases/";
	private String					db_name								= "geolocation2_db";
	List<Polygon>					polList								= new ArrayList<Polygon>();
	Polygon							currentPolygon						= null;
	List<Polygon>					SelectedPolygonList					= new ArrayList<Polygon>();

	// tr orta nokta 39.1116091,35.0272779
	// 40.647256, 29.274982

	// ------ TABS -------
	private TabHost					tabHost;
	private int						valueTab;

	// ------ CAMERA -------
	private static final int		CAMERA_CAPTURE_IMAGE_REQUEST_CODE	= 100;
	private static final int		MEDIA_TYPE_IMAGE					= 1;
	private static final String		IMAGE_DIRECTORY_NAME				= "TelekuryeMaps";

	private Uri						fileUri;
	// private ImageView imgPreview1;
	// private ImageView imgPreview2;
	// private ImageView imgPreview3;
	// private ImageView imgPreview4;

	private LinearLayout			llImages;
	private Button					btnCapturePicture;
	int								sayac								= 0;
	ProgressDialog					progress;

	// ----- FeedBack -------
	private TextView				tv_mission_status_value;
	private TextView				tv_apno;
	private TextView				tv_apno_value;
	private EditText				new_et_apno_value;
	private TextView				tv_apname;
	private TextView				tv_apname_value;
	private EditText				new_et_apname_value;
	private TextView				tv_address;
	private TextView				tv_address_value;
	private TextView				tv_StreetOrBuildingType;
	private Button					btnStreetOrBuildingType;
	private Button					btnSaveFeedback;
	private TextView				tv_persons;
	private TextView				tv_persons_values;
	private EditText				et_independent_section_count;
	private EditText				et_floor_count;
	private TextView				tv_independent_section_count;
	private TextView				tv_floor_count;
	LinearLayout					ll_user_feedback;
	LinearLayout					ll_mission_type_info;
	LinearLayout					ll_apno;
	private TextView				tv_building_type_info;
	private TextView				tv_building_type_info_value;
	private Button					btnMapZoom;

	// ----- GPS Location -----
	protected LocationManager		locationManager;
	private long					lastTime;
	private GpsStatus				mStatus;
	private long					mLastLocationMillis;
	private Location				mLastLocation;
	private Location				tempLocation						= null;
	private boolean					isGPSFix							= false;
	private boolean					isGpsStatus							= false;

	// ----- StatusBar ------
	public TextView					tv_info_welcome;
	public TextView					tvNetworkStatus;
	public TextView					tv_info_time;
	public TextView					tv_info_battery;
	public TextView					tv_info_accuracy;
	public TextView					tv_info_version;
	public TextView					tv_Score;
	public TextView					tv_earnings;
	public TextView					tv_last_sync_date;

	private String					name;
	private String					surname;
	private String					username;

	private Thread					uiUpdateThread;

	// ----- FEEDBACK ------

	private static float			UserAccuracy						= 5000;
	private static double			GPSLat;
	private static double			GPSLng;
	private static double			GPSAlt;

	private static double			GPSBearing;
	private static double			GPSSpeed;
	private static Date				GPSTime;

	private static double			SignedLat;
	private static double			SignedLng;
	private static int				TypeId;

	private Boolean					btnTypeStatus						= false;

	// ---------------------
	// private static Boolean photoStatus1 = false;
	// private static Boolean photoStatus2 = false;
	// private static Boolean photoStatus3 = false;
	// private static Boolean photoStatus4 = false;
	private static int				photoCount							= 0;

	public String					imgPath;
	public static int				selectImgview						= 0;
	private int						MissionCounter						= 0;
	// public int photoCounter = 0;

	private int						grupid, childid;
	ArrayList<IMission>				mMissionForFeedback;

	private List<String>			photoUrlList;

	// private List<Integer> mbTypesId; // bina tip id leri
	// private List<String> mbTypesName; // bina tip isim leri
	// private List<Integer> msTypesId; // sokak tip id leri
	// private List<String> msTypesName; // sokak tip isim leri

	List<MissionsStreets>			mAllStreets;																			// t�m sokaklar
	List<MissionsStreets>			mThisMissionStreets;																	// bu g�reve ait iki sokak
	List<MissionsBuildings>			mBuildingsOddNo;																		// se�ilen soka�a ait tek say�l� binalar
	List<MissionsBuildings>			mBuildingsEvenNo;																		// se�ilen soka�a ait �ift say�l� binalar
	MissionsStreets					ms;																					// ki�inin se�ti�i sokak
	List<MissionsStreets>			completedMissionStreets				= new ArrayList<MissionsStreets>();

	CheckBox						cbSagKapi;
	CheckBox						cbOnKapi;
	CheckBox						cbSolKapi;

	TextView						tv_street_name;

	private int						checkboxStatus						= 0;

	private int						streettype;
	List<MissionsBuildings>			mBuilds;

	UiSettings						uiSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		Tools.disableScreenLock(this);

		// fillPolygon = new ArrayList<PolygonOptions>();
		// polyOptions = new PolygonOptions();

		// ******** Components ********
		btnStreetOrBuildingType = (Button) findViewById(R.id.btn_StreetOrBuildingType);
		btnSaveFeedback = (Button) findViewById(R.id.btn_save_feedback);
		btnCapturePicture = (Button) findViewById(R.id.btnSavePhoto);

		btnCapturePicture.setOnClickListener(this);
		btnSaveFeedback.setOnClickListener(this);
		btnStreetOrBuildingType.setOnClickListener(this);

		finishedShapes = new FinishedShapeHistory();
		// imgPreview1 = (ImageView) findViewById(R.id.iv_PhotoPreview1);
		// imgPreview2 = (ImageView) findViewById(R.id.iv_PhotoPreview2);
		// imgPreview3 = (ImageView) findViewById(R.id.iv_PhotoPreview3);
		// imgPreview4 = (ImageView) findViewById(R.id.iv_PhotoPreview4);

		// imgPreview1.setOnClickListener(this);
		// imgPreview2.setOnClickListener(this);
		// imgPreview3.setOnClickListener(this);
		// imgPreview4.setOnClickListener(this);

		llImages = (LinearLayout) findViewById(R.id.llImages);
		llImages.setWeightSum(Info.PHOTO_COUNT);

		llMapFragment = (LinearLayout) findViewById(R.id.ll_mapfragment);

		btnMapZoom = (Button) findViewById(R.id.btn_map_zoom);
		btnMapZoom.setOnClickListener(this);
		btnMapZoom.setBackgroundColor(Color.RED);

		tv_apno = (TextView) findViewById(R.id.tv_apno);
		tv_apno_value = (TextView) findViewById(R.id.tv_apno_value);
		new_et_apno_value = (EditText) findViewById(R.id.new_et_apno_value);
		tv_apname = (TextView) findViewById(R.id.tv_apname);
		tv_apname_value = (TextView) findViewById(R.id.tv_apname_value);
		new_et_apname_value = (EditText) findViewById(R.id.new_et_apname_value);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_address_value = (TextView) findViewById(R.id.tv_address_value);
		tv_StreetOrBuildingType = (TextView) findViewById(R.id.tv_StreetOrBuildingType);
		tv_mission_status_value = (TextView) findViewById(R.id.tv_mission_status_value);

		et_independent_section_count = (EditText) findViewById(R.id.et_independent_section_count);
		et_floor_count = (EditText) findViewById(R.id.et_floor_count);
		tv_independent_section_count = (TextView) findViewById(R.id.tv_independent_section_count);
		tv_floor_count = (TextView) findViewById(R.id.tv_floor_count);
		ll_user_feedback = (LinearLayout) findViewById(R.id.ll_user_feedback);
		ll_mission_type_info = (LinearLayout) findViewById(R.id.ll_mission_type_info);
		ll_apno = (LinearLayout) findViewById(R.id.ll_apno);

		tv_persons = (TextView) findViewById(R.id.tv_persons);
		tv_persons_values = (TextView) findViewById(R.id.tv_persons_values);

		tv_info_welcome = (TextView) findViewById(R.id.tv_info_name_surname);
		tv_info_time = (TextView) findViewById(R.id.tv_info_time);
		tv_info_battery = (TextView) findViewById(R.id.tv_info_battery);
		tv_info_accuracy = (TextView) findViewById(R.id.tv_info_accuracy);
		tv_info_version = (TextView) findViewById(R.id.tv_info_version);
		tv_Score = (TextView) findViewById(R.id.tv_info_score);
		tv_earnings = (TextView) findViewById(R.id.tv_info_earnings);
		tvNetworkStatus = (TextView) findViewById(R.id.tv_info_internet);
		tv_last_sync_date = (TextView) findViewById(R.id.tv_info_last_sync_date);

		tv_building_type_info = (TextView) findViewById(R.id.tv_building_type_info);
		tv_building_type_info_value = (TextView) findViewById(R.id.tv_building_type_info_value);

		cbOnKapi = (CheckBox) findViewById(R.id.cbOnKapi);
		cbSolKapi = (CheckBox) findViewById(R.id.cbSolKapi);
		cbSagKapi = (CheckBox) findViewById(R.id.cbSagKapi);

		cbOnKapi.setOnClickListener(this);
		cbSolKapi.setOnClickListener(this);
		cbSagKapi.setOnClickListener(this);

		// name = new Person().GetAllData().get(0).getName();
		// surname = new Person().GetAllData().get(0).getSurname();
		// username = new Person().GetAllData().get(0).getUserName();

		Person person = new Person();

		name = person.GetById(Info.UserId).getName();
		surname = person.GetById(Info.UserId).getSurname();
		username = person.GetById(Info.UserId).getUserName();

		photoUrlList = new ArrayList<String>();

		String namesurname = name + " " + surname;

		if (namesurname.length() < 30) {
			tv_info_welcome.setTextSize(18f);
		}
		else {
			tv_info_welcome.setTextSize(13f);
		}

		tv_info_welcome.setText(namesurname);

		tv_info_version.setText("Versiyon : " + Info.CURRENT_VERSION);

		tv_info_accuracy.setBackgroundColor(Color.RED);
		tv_info_accuracy.setText("Gps Yok  ");
		tv_earnings.setText("Bilgi Yok");

		tv_street_name = (TextView) findViewById(R.id.tv_street_name1);

		photoInfo = new ArrayList<PhotoInfo>();

		Boolean hasMapCreated = showMapOnActivity();

		if (!hasMapCreated) {
			Toast.makeText(this, "L�tfen Google Play Hizmetlerini g�ncellemek �zere Ali Bahad�r Ku� ile ileti�ime ge�iniz.", Toast.LENGTH_LONG).show();
			return;
		}

		// ********* GET DATAS ********
		if (getIntent().getExtras() != null) {
			grupid = getIntent().getExtras().getInt("grupid");
			childid = getIntent().getExtras().getInt("childid");
			streettype = getIntent().getExtras().getInt("streettype");
		}

		// *****************************

		// +++++ kullan�c�n�n t�klad��� sokak belirlendi +++++

		// List<MissionsStreets> msTemp1 = LiveData.misStreets; // �iftler halinde t�m sokak listesi
		MissionsStreets data = new MissionsStreets();

		// List<MissionsStreets> msTemp1 = data.GetAllData();
		// List<MissionsStreets> msTemp2 = new ArrayList<MissionsStreets>(); // �iftler halinde olmayan sokak listesi

		// for (int i = 0; i < msTemp1.size(); i++) { // �iftler sokaklar teke d���r�l�yor
		// if (msTemp1.get(i).getBuildingNumber_IsOdd()) {
		// msTemp2.add(msTemp1.get(i));
		// }
		// }

		// ms = msTemp2.get(grupid);
		// mAllStreets = LiveData.misStreets;
		/*
		 * mAllStreets = data.GetAllData(); mThisMissionStreets = new ArrayList<MissionsStreets>();
		 * 
		 * // ++++++++++ se�ilen soka��n tek ve �ift s�ras� al�nd� for (int i = 0; i < mAllStreets.size(); i++) { if (mAllStreets.get(i).getStreetId() == ms.getStreetId()) {
		 * mThisMissionStreets.add(mAllStreets.get(i)); } }
		 */
		mThisMissionStreets = data.GetStreetsByStreetId(grupid);

		// TODO: Sefa neden ms null geliyor olabilir ona bakacak.
		for (MissionsStreets street : mThisMissionStreets) {
			if (street.getBuildingNumber_IsOdd()) {
				ms = street;
			}
		}

		if (ms == null) {
			// TODO: log g�nder.
			finish();
		}

		// +++++ se�ilen soka�a ait binalar tek ve �ift listelere ayr�ld� +++++
		// List<MissionsBuildings> mBuilds = LiveData.misBuildings;
		MissionsBuildings bData = new MissionsBuildings();
		mBuilds = bData.GetBuildingsByStreetId(ms.getStreetId());
		mBuildingsOddNo = new ArrayList<MissionsBuildings>();
		mBuildingsEvenNo = new ArrayList<MissionsBuildings>();

		// try {
		// for (int j = 0; j < mBuilds.size(); j++) {
		//
		// if ((ms.getStreetId() == mBuilds.get(j).getStreetId()) && (mBuilds.get(j).getBuildingNumber_IsOdd())) {
		// mBuildingsOddNo.add(mBuilds.get(j));
		//
		// } else if ((ms.getStreetId() == mBuilds.get(j).getStreetId()) && !(mBuilds.get(j).getBuildingNumber_IsOdd())) {
		// mBuildingsEvenNo.add(mBuilds.get(j));
		// }
		//
		// }
		// } catch (Exception e) {
		// Tools.customSendError(e);new sendErrors(e);
		// }

		try {
			for (int j = 0; j < mBuilds.size(); j++) {
				if (mBuilds.get(j).getBuildingNumber_IsOdd()) {
					mBuildingsOddNo.add(mBuilds.get(j));
				}
				else {
					mBuildingsEvenNo.add(mBuilds.get(j));
				}
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		// switch (childid) {
		// case 0:
		// Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // k���kten b�y��e s�rala
		// // mBuilds = mBuildingsEvenNo;
		// Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // b�y�kten k����e s�rala
		// Collections.reverse(mBuildingsOddNo);
		// break;
		// case 1:
		// Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // b�y�kten k����e s�rala
		// Collections.reverse(mBuildingsEvenNo);
		// // mBuilds = mBuildingsEvenNo;
		// Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // k���kten b�y��e s�rala
		// break;
		// case 2:
		// Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // k���kten b�y��e s�rala
		// // mBuilds = mBuildingsOddNo;
		// Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // b�y�kten k����e s�rala
		// Collections.reverse(mBuildingsEvenNo);
		// break;
		// case 3:
		// Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // b�y�kten k����e s�rala
		// Collections.reverse(mBuildingsOddNo);
		// // mBuilds = mBuildingsOddNo;
		// Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // k���kten b�y��e s�rala
		// break;
		// default:
		// break;
		// }

		// Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // k���kten b�y��e s�rala

		// Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // k���kten b�y��e s�rala

		// for (int i = 0; i < mBuildingsOddNo.size(); i++) {
		// System.out.println(mBuildingsOddNo.get(i).getBuildingNumber() + " - " + mBuildingsOddNo.get(i).getOrderIndex());
		// }
		// System.out.println("*********");
		// for (int i = 0; i < mBuildingsEvenNo.size(); i++) {
		// System.out.println(mBuildingsEvenNo.get(i).getBuildingNumber() + " - " + mBuildingsEvenNo.get(i).getOrderIndex());
		// }

		if (streettype == 0 || streettype == 1 || streettype == 5) { // k�me ev mi normal mi kontrol�
			mMissionForFeedback = MissionListCreator2(childid);
		}
		else {
			mMissionForFeedback = MissionListCreator(childid);
		}

		shapeIdHistory = new FinishedShapeHistory().GetShapeIdList(Info.UserId);

		// for (int i = 0; i < mMissionForFeedback.size(); i++) {
		// System.out.println(mMissionForFeedback.get(i).getBuildingNumber() + " - " + mMissionForFeedback.get(i).getOrderIndex());
		// }

		//
		//
		// mThisMissionStreets
		//
		// for (int i = 0; i < array.length; i++) {
		//
		// }

		// for (int i = 0; i < mBuildingsOddNo.size(); i++) {
		// Log.i("tekler " + i, "" + mBuildingsOddNo.get(i).getOrderIndex() + " - " + mBuildingsOddNo.get(i).getBuildingNumber());
		// }
		//
		// for (int i = 0; i < mBuildingsEvenNo.size(); i++) {
		// Log.i("�iftler " + i, "" + mBuildingsEvenNo.get(i).getOrderIndex() + " - " + mBuildingsEvenNo.get(i).getBuildingNumber());
		// }

		// *****************************

		// mbTypesName = new ArrayList<String>();
		// mbTypesId = new ArrayList<Integer>();
		// msTypesName = new ArrayList<String>();
		// msTypesId = new ArrayList<Integer>();
		//
		// List<BuildingTypes> mBuildTypes = new BuildingTypes().GetAllData(); // bina tipleri ekleniyor
		// for (int i = 0; i < mBuildTypes.size(); i++) {
		// mbTypesName.add(mBuildTypes.get(i).getName());
		// mbTypesId.add(mBuildTypes.get(i).getId());
		// }
		//
		// List<StreetTypes> mStreetTypes = new StreetTypes().GetAllData(); // sokak tipleri ekleniyor
		// for (int i = 0; i < mStreetTypes.size(); i++) {
		// msTypesName.add(mStreetTypes.get(i).getName());
		// msTypesId.add(mStreetTypes.get(i).getId());
		// }

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // pusula kodlar�
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
			// myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.0756, 28.9744), Info.MAP_ZOOM_LEVEL));
			myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.9786680000, 37.7365860000), Info.MAP_ZOOM_LEVEL));
		}

		// uiSettings.setAllGesturesEnabled(true); // *-*
		// uiSettings.setZoomControlsEnabled(true);// *-*
		// myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.0756, 28.9744), Info.MAP_ZOOM_LEVEL));// *-*

		tv_street_name.setText("<-- " + mMissionForFeedback.get(MissionCounter).getName() + " SOKAK -->");
		// Info.ImageCount = 0;

		progress = new ProgressDialog(getApplicationContext());
		progress.setMessage("L�tfen bekleyiniz");
		progress.setCancelable(false);

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

	private ArrayList<IMission> MissionListCreator(int chId) {

		ArrayList<IMission> temp = new ArrayList<IMission>();
		// bu k�s�mda kullan�c�n�n se�i�ine g�re liste
		if (chId == 0) { // artan �ift -> azalan tek

			try {
				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // k���kten b�y��e s�rala
				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // b�y�kten k����e s�rala
				Collections.reverse(mBuildingsOddNo);

				temp.add(mThisMissionStreets.get(0));
				temp.addAll(mBuildingsEvenNo);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}

				temp.addAll(mBuildingsOddNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 1) { // azalan �ift -> artan tek

			try {

				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // b�y�kten k����e s�rala
				Collections.reverse(mBuildingsEvenNo);
				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // k���kten b�y��e s�rala

				temp.add(mThisMissionStreets.get(0));
				temp.addAll(mBuildingsEvenNo);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}
				temp.addAll(mBuildingsOddNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 2) { // artan tek -> azalan �ift

			try {

				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // k���kten b�y��e s�rala
				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // b�y�kten k����e s�rala
				Collections.reverse(mBuildingsEvenNo);

				temp.add(mThisMissionStreets.get(0));
				temp.addAll(mBuildingsOddNo);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}

				temp.addAll(mBuildingsEvenNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		else if (chId == 3) { // azalan tek -> artan �ift

			try {

				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // b�y�kten k����e s�rala
				Collections.reverse(mBuildingsOddNo);
				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // k���kten b�y��e s�rala

				if (mThisMissionStreets.get(0) != null) {
					temp.add(mThisMissionStreets.get(0));
				}

				temp.addAll(mBuildingsOddNo);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}
				temp.addAll(mBuildingsEvenNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		return temp;
	}

	private ArrayList<IMission> MissionListCreator2(int chId) { // k�me evler i�in s�ralama

		ArrayList<IMission> temp = new ArrayList<IMission>();
		// bu k�s�mda kullan�c�n�n se�i�ine g�re liste
		if (chId == 0) { // artan �ift -> azalan tek

			try {
				Collections.sort(mBuilds, new MissionsBuildings()); // k���kten b�y��e s�rala

				temp.add(mThisMissionStreets.get(0));

				temp.addAll(mBuilds);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}

			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		else if (chId == 1) { // azalan �ift -> artan tek

			try {
				Collections.sort(mBuilds, new MissionsBuildings()); // b�y�kten k����e s�rala
				Collections.reverse(mBuilds);

				temp.add(mThisMissionStreets.get(0));

				temp.addAll(mBuilds);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}

		return temp;
	}

	private void fillComponent() {

		selectedBuildingType = 0;
		selectedFloorCount = 0;

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

				tv_mission_status_value.setText("Sokak G�revi");

				// -----
				tv_apname.setText("Sokak Ad� : ");
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
				btnStreetOrBuildingType.setText("Se�iniz");
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
				// tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" + mMissionForFeedback.size() + "  [+" + shapeControl.getListSize() + "]");
				// }
				// else {
				// tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" + mMissionForFeedback.size() + "  [0]");
				// }

				// for (int i = 0; i < msTypesId.size(); i++) {
				// if (mMissionForFeedback.get(MissionCounter).getTypeId() == msTypesId.get(i)) {
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
							btnStreetOrBuildingType.setText("Se�ilmi�");
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

				tv_mission_status_value.setText("D�� Kap� No G�revi");

				// ----
				tv_apname_value.setVisibility(View.VISIBLE);
				new_et_apname_value.setVisibility(View.GONE);
				if (mMissionForFeedback.get(MissionCounter).getName() != null && !mMissionForFeedback.get(MissionCounter).getName().trim().equalsIgnoreCase("")) {
					tv_apname_value.setText(mMissionForFeedback.get(MissionCounter).getName() + " Apartman�");
				}
				else {
					tv_apname_value.setText("");
				}
				tv_apname.setText("Bina Ad� : ");
				// ------
				ll_mission_type_info.setVisibility(View.VISIBLE);

				tv_building_type_info.setText("Bina Tipi Bilgisi : ");

				tv_StreetOrBuildingType.setText("Bina Tipi : ");

				btnStreetOrBuildingType.setText("Se�iniz");
				et_independent_section_count.setText("" + mMissionForFeedback.get(MissionCounter).getIndependentSectionCount());
				et_floor_count.setText("");

				// ---
				ll_user_feedback.setVisibility(View.VISIBLE);

				// ---
				tv_apno.setVisibility(View.VISIBLE);
				ll_apno.setVisibility(View.VISIBLE);
				tv_apno_value.setVisibility(View.VISIBLE);
				new_et_apno_value.setVisibility(View.GONE);
				tv_apno.setText("D�� Kap� No : ");
				tv_apno_value.setText(mMissionForFeedback.get(MissionCounter).getBuildingNumber());
				// tv_apno_value.setText(mMissionForFeedback.get(MissionCounter).getBuildingNumber() + " / " + mMissionForFeedback.get(MissionCounter).getIndependentSectionType());

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
				// tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" + mMissionForFeedback.size() + "  [+" + shapeControl.getListSize() + "]");
				// }
				// else {
				// tv_Score.setText("Durum : " + (MissionCounter + 1) + "/" + mMissionForFeedback.size() + "  [0]");
				// }

				tv_building_type_info_value.setText(mMissionForFeedback.get(MissionCounter).getIndependentSectionType());

				// for (int i = 0; i < Constant.BuildingTypes.Id().size(); i++) {
				//
				// if (mMissionForFeedback.get(MissionCounter).getIndependentSectionTypeId() == Constant.BuildingTypes.Id().get(i)) {
				//
				// TypeId = mMissionForFeedback.get(MissionCounter).getStreetTypeId();
				//
				// int BuildingType = Constant.BuildingTypes.Id().get(i);
				//
				// if (BuildingType == 16) { // tahsis
				// btnStreetOrBuildingType.setEnabled(false);
				// btnStreetOrBuildingType.setText("Se�im Yap�lamaz");
				// }
				// else if (BuildingType == 1 || BuildingType == 2 || BuildingType == 15) { // arsa,in�aat,bina d��� yap�
				//
				// et_independent_section_count.setEnabled(false);
				// et_independent_section_count.setText("");
				// et_independent_section_count.setHint("Se�ilemez");
				//
				// et_floor_count.setEnabled(false);
				// et_floor_count.setText("");
				// et_floor_count.setHint("Se�ilemez");
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
			isThereAMarker = true;
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

	}

	private void ShowSelectedMarkers() {
		if (LiveData.streetMarkers.size() > 0) {
			for (int i = 0; i < LiveData.streetMarkers.size(); i++) {
				try {
					MarkerOptions mo = new MarkerOptions();

					mo.snippet(LiveData.streetMarkers.get(i).getSnippet().trim());
					mo.title(LiveData.streetMarkers.get(i).getTitle().trim());
					mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					mo.draggable(false);
					mo.position(LiveData.streetMarkers.get(i).getPosition());

					myMap.addMarker(mo);
				}
				catch (Exception e) {
					Tools.saveErrors(e);
				}

			}
		}
	}

	// ################################### BUTTON CLICK ########################################################

	private void saveFeedback() {

		// if (firstStreetTypeId != null) {
		// btnTypeStatus = true;
		// }

		if (!Info.ISTEST) {

			if (!isMarkerSet) {
				Tools.showShortCustomToast(this, "Harita �zerinden konum se�ilmedi!");
				return;
			}
			if (!btnTypeStatus) {
				Tools.showShortCustomToast(this, "Bina/Sokak Tipi se�ilmedi.");
				return;
			}
			if ((et_floor_count.getText().toString().trim().equalsIgnoreCase("") && (isNewBuilding || mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 2)) //
					&& (TypeId != 1 && TypeId != 2 && TypeId != 12 && TypeId != 13 && TypeId != 15)) {
				Tools.showShortCustomToast(this, "L�tfen Kat Say�s�n� Giriniz.");
				return;
			}
			if (LiveData.photoinfo.size() != Info.PHOTO_COUNT) {
				Tools.showShortCustomToast(this, Info.PHOTO_COUNT + " adet foto�raf �ekilmelidir.");
				return;
			}

			if (!cbOnKapi.isChecked() && !cbSolKapi.isChecked() && !cbSagKapi.isChecked() && (isNewBuilding || mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 2)) {
				Tools.showShortCustomToast(this, "Kap�n�n bulundu�u cepheyi se�iniz.");
				return;
			}
		}

		// fillPolygon.add(polygonOptions);

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

				currentMarker.setTitle(mMissionForFeedback.get(0).getName() + " Sokak, D�� Kap� No: " + new_et_apno_value.getText().toString());
				currentMarker.setSnippet(new_et_apname_value.getText().toString() + " Apartman�");

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
			// ((MissionsStreets) mMissionForFeedback.get(MissionCounter)).Update();
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
			// mFeedBack.setBuildingName(mMissionForFeedback.get(MissionCounter - 1).getName());
			// mFeedBack.setBuildingNumber(mMissionForFeedback.get(MissionCounter - 1).getBuildingNumber());
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
			// Tools.showLongCustomToast(this, "Tamamlanmam�� Yeni Bina G�revleri Bulunmaktad�r.");
			// tabHost.setCurrentTab(0);
			// newBuildingMission();
			// isNewBuilding = true;
			// isFinishRedShapes = false;
			// return;
			// }

			Tools.showShortCustomToast(this, "Sokak G�revi Tamamland�");
			LiveData.streetMarkers.clear();

			for (MissionsStreets str : completedMissionStreets) {
				str.setIsCompleted(true);
				str.Update();
			}

			finish();
		}
		else {
			fillComponent(); // sonraki g�revin bilgileri y�kleniyor
		}
	}

	private void selectType() { // butona sokak verileri mi bina verileri mi y�klenece�i belirleniyor

		AlertDialog.Builder builder = new AlertDialog.Builder(FeedBack.this);
		builder.setTitle("Se�im Yap�n�z");

		if (isNewBuilding) {
			selectType = 2;
		}
		else {
			selectType = mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId();
		}

		if (selectType == 1) {

			Constant.StreetTypes.Id().get(0);

			// CharSequence[] choiceList = msTypesName.toArray(new CharSequence[msTypesName.size()]);

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
						et_independent_section_count.setHint("Se�ilemez");

						et_floor_count.setEnabled(false);
						et_floor_count.setText("");
						et_floor_count.setHint("Se�ilemez");

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

	// ################################### TABS ########################################################

	private void tabsProperties() {
		tabHost = (TabHost) findViewById(R.id.tabSecim);
		tabHost.setup();

		setupTab(R.id.tab1, R.drawable.info1, "Bilgi");
		setupTab(R.id.tab2, R.drawable.tick, "Foto");
		setupTab(R.id.tab3, R.drawable.camera1, "Kap�");

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
					Tools.showShortCustomToast(this, "Harita �zerinden konum se�ilmedi!");
					tabHost.setCurrentTab(0);
					return;
				}
			}
			else if (tabId.equals("Kap�")) {

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

	// ################################### GOOGLE MAP ########################################################

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
				Tools.showShortCustomToast(this, "Foto�raf �ekildikten sonra konum g�ncellenemez!");
				return;
			}

		}
		else {
			UserAccuracy = 1;
		}

		if (isNewBuilding) {
			pressShape(point);
		}
		else {
			if (mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 2) {
				pressShape(point);
			}
		}

		if (UserAccuracy <= Info.GPS_ACCURACY) {
			MarkerOptions mo = new MarkerOptions();
			mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

			if (!isNewBuilding) {

				if (mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 1) {

					if (!mMissionForFeedback.get(MissionCounter).getName().trim().equalsIgnoreCase("")) {
						mo.snippet(mMissionForFeedback.get(MissionCounter).getName() + " Sokak");
					}
					else {
						mo.snippet("");
					}

				}
				else if (mMissionForFeedback.get(MissionCounter).getUserDailyMissionTypeId() == 2) {
					if (!mMissionForFeedback.get(MissionCounter).getName().trim().equalsIgnoreCase("")) {
						mo.snippet(mMissionForFeedback.get(MissionCounter).getName() + " Apartman�");
					}
					else {
						mo.snippet("");
					}
				}

				mo.title(mMissionForFeedback.get(MissionCounter).getAddressText());

			}

			// mo.draggable(true);
			mo.position(point);

			// myMap.clear();
			if (currentMarker != null) {
				currentMarker.remove();
			}

			currentMarker = myMap.addMarker(mo);

			// refreshMap();
			// markerLastPoint.setPosition(point);
			SignedLat = point.latitude;
			SignedLng = point.longitude;

			markerClicked = false;
			isThereAMarker = false;

			isMarkerSet = true;
		}

	}

	// private void refreshMap() {
	// myMap.clear();
	// ShowSelectedMarkers();
	//
	// showPolygonOnMap();
	//
	// if (currentMarker != null) {
	//
	// MarkerOptions mo = new MarkerOptions();
	//
	// mo.snippet(currentMarker.getSnippet());
	// mo.title(currentMarker.getTitle());
	// mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
	// mo.draggable(false);
	// mo.position(currentMarker.getPosition());
	//
	// myMap.addMarker(mo);
	// }
	//
	// if (mPositionMarker != null) {
	// mPositionMarker = myMap.addMarker(getMarkerOptions(mPositionMarker.getPosition()));
	// }
	//
	// for (PolygonOptions polygonOptions : fillPolygon) {
	// myMap.addPolygon(polygonOptions);
	// }
	//
	// }

	private void pressShape(LatLng point) {
		if (currentPolygon != null) {
			currentPolygon.remove();
		}

		Boolean isPolygonSelected = false;

		for (com.telekurye.kml.Polygon pol : polygons) {
			if (PolyUtil.containsLocation(point, pol.coors, true)) {
				isPolygonSelected = true;
				basarShapeId = pol.polygonid.intValue();
				final PolygonOptions polygonOptions = new PolygonOptions();
				String[] coors = SplitUsingTokenizer(pol.coordinates, "||");

				for (String string : coors) {
					String[] coor = string.split(",");
					final LatLng p = new LatLng(Float.valueOf(coor[0]), Float.valueOf(coor[1]));
					polygonOptions.add(p);
				}

				shapeControl.CompareShapes(pol);

				finishedShapes.setShapeId(pol.polygonid);
				finishedShapes.setUserId(Info.UserId);
				finishedShapes.setUserDailyMissionId(mMissionForFeedback.get(MissionCounter).getUserDailyMissionId());
				finishedShapes.Insert();

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

		// if (!isPolygonSelected) {
		// Tools.showShortCustomToast(this, "L�tfen yap� �zerine t�klay�n�z!");
		// return;
		// }
	}

	@Override
	public void onMarkerDrag(Marker marker) {

		if (isZoomOpen) {
			return;
		}
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng point) {

		if (isZoomOpen) {
			return;
		}

		// if (isPointInPolygon(point, p1.getPoints())) {
		// Toast.makeText(FeedBack.this, "�ekil 1 i�inde", Toast.LENGTH_SHORT).show();
		//
		// p1.setFillColor("#4000FF3C");
		// p1.DrawOnMap();
		// p1.ShowOnMap();
		//
		// } else if (isPointInPolygon(point, p2.getPoints())) {
		// Toast.makeText(FeedBack.this, "�ekil 2 i�inde", Toast.LENGTH_SHORT).show();
		//
		// p2.setFillColor("#40FFD000");
		// p2.DrawOnMap();
		// p2.ShowOnMap();
		//
		// } else {
		// Toast.makeText(FeedBack.this, "t�m �ekillerin d���nda", Toast.LENGTH_SHORT).show();
		// }

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

	// ################################### CAMERA ########################################################

	private void takePhoto() {

		if (LiveData.photoinfo.size() < Info.PHOTO_COUNT) {
			tp = new CameraHelper(FeedBack.this);
			startActivityForResult(tp.startCamera(), tp.getRequestCode());
		}
		else {
			Tools.showShortCustomToast(FeedBack.this, "Foto�raf ekleme limiti doldu!");
		}

		// try {
		// if (photoStatus1 == 0) {
		// startActivityForResult(tp.startCamera(), tp.getRequestCode());
		//
		// // capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, imgPreview1.getId());
		// selectImgview = 1;
		// } else if (photoStatus2 == 0) {
		// startActivityForResult(tp.startCamera(), tp.getRequestCode());
		//
		// // capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, imgPreview2.getId());
		// selectImgview = 2;
		// } else if (photoStatus3 == 0) {
		// startActivityForResult(tp.startCamera(), tp.getRequestCode());
		//
		// // capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, imgPreview3.getId());
		// selectImgview = 3;
		// } else if (photoStatus4 == 0) {
		// startActivityForResult(tp.startCamera(), tp.getRequestCode());
		//
		// // capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, imgPreview4.getId());
		// selectImgview = 4;
		// }
		// } catch (Exception e2) {
		// }

		// if (photoStatus1 == 0) {
		// capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, imgPreview1.getId());
		// photoStatus1 = 1;
		// photoCount++;
		// } else if (photoStatus2 == 0) {
		// capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, imgPreview2.getId());
		// photoStatus2 = 1;
		// photoCount++;
		// } else if (photoStatus3 == 0) {
		// capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, imgPreview3.getId());
		// photoStatus3 = 1;
		// photoCount++;
		// } else if (photoStatus4 == 0) {
		// capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, imgPreview4.getId());
		// photoStatus4 = 1;
		// photoCount++;
		// }

	}

	// salihy: TODO: buray� d�zenle
	private void selectImage(final PhotoInfo pInfo) {
		final CharSequence[] items = { "Foto�raf� Sil", "Vazge�" };

		AlertDialog.Builder builder = new AlertDialog.Builder(FeedBack.this);
		builder.setTitle("D�zenle");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				try {
					if (items[item].equals("Foto�raf� Sil")) {

						llImages.removeView(pInfo.getImageview());
						// pInfo.getImageview().setImageResource(android.R.color.transparent);
						// photoStatus1 = false;
						File file = new File(Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH + File.separator + pInfo.getName());
						file.delete();
						LiveData.photoinfo.remove(pInfo);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (LiveData.photoinfo.size() < Info.PHOTO_COUNT && tp != null) {
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

			if (resultCode == Activity.RESULT_OK) {
				LiveData.photoinfo.add(info);
			}

		}

	}

	// ################################## GPS LOCATION ################################################################

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

//		for (Polygon pol : polList) {
//			pol.remove();
//		}
//		polList = new ArrayList<Polygon>();

		// Salihy: Sefa bu kontrol� hi� bir ko�ulda a�ma!
		// if (!Info.ISTEST)
//		{
//			if (myMap.getCameraPosition().zoom < Info.MAP_ZOOM_LEVEL) {
//				return;
//			}
//		}

		// progress.show();

//		final LatLngBounds bounds = myMap.getProjection().getVisibleRegion().latLngBounds;
//		final ArrayList<PolygonOptions> polygonOptionsList = new ArrayList<PolygonOptions>();
//
//		new Thread(new Runnable() {
//			public void run() {
//				final Context ctx = getApplicationContext();
//
////				polygons = com.telekurye.kml.Polygon.GetAround((float) bounds.southwest.latitude, (float) bounds.northeast.latitude, (float) bounds.southwest.longitude,
////						(float) bounds.northeast.longitude, ms.getDistrictId(), ShapeIdList, ctx, db_path, db_name);
//				
//				polygons = com.telekurye.kml.Polygon.GetByDistrictId(ctx, (long)ms.getDistrictId(), db_path, db_name);
//
//				int visibleShapeCount;
//				if (isZoomOpen) {
//					visibleShapeCount = 600;
//				}
//				else {
//					visibleShapeCount = 300;
//				}
//
//				for (Polygon poly : SelectedPolygonList) {
//					poly.setVisible(true);
//				}
//
//				for (com.telekurye.kml.Polygon polygon : polygons) {
//					polygon.coors = new ArrayList<LatLng>();
//
//					final PolygonOptions polygonOptions = new PolygonOptions();
//
//					String[] coors = SplitUsingTokenizer(polygon.coordinates, "||");
//
//					for (String string : coors) {
//						String[] coor = string.split(",");
//
//						final LatLng point = new LatLng(Float.valueOf(coor[0]), Float.valueOf(coor[1]));
//
//						polygon.coors.add(point);
//
//						polygonOptions.add(point);
//					}
//
//					// if (shapeControl.weHaveThisShape(polygon)) {
//					// polygonOptions.strokeColor(Color.RED);
//					// }
//					// else {
//					// polygonOptions.strokeColor(Color.GREEN);
//					// }
//					polygonOptions.strokeColor(Color.RED);
//
//					polygonOptions.fillColor(Color.TRANSPARENT);
//
//					for (Long shapeId : shapeIdHistory) {
//						if (shapeId.equals(polygon.polygonid)) {
//							polygonOptions.fillColor(0x802EFE64);
//						}
//					}
//
//					polygonOptions.strokeWidth(3);
//					polygonOptionsList.add(polygonOptions);
//				}
//
//				if (ctx != null) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							GoogleMap map = myMap;
//							for (int i = 0; i < polygonOptionsList.size(); i++) {
//								polList.add(map.addPolygon(polygonOptionsList.get(i)));
//							}
//							// progress.dismiss();
//						}
//					});
//				}
//			}
//		}).start();
	}
	
	public void LoadShapes() {
		final ArrayList<PolygonOptions> polygonOptionsList = new ArrayList<PolygonOptions>();
		
		//Bina ve sokak shapeleri bas�l�yor
		new Thread(new Runnable() {
			public void run() {
				final Context ctx = getApplicationContext();

//				polygons = com.telekurye.kml.Polygon.GetAround((float) bounds.southwest.latitude, (float) bounds.northeast.latitude, (float) bounds.southwest.longitude,
//						(float) bounds.northeast.longitude, ms.getDistrictId(), ShapeIdList, ctx, db_path, db_name);
				
				List<MissionsStreets> missStreets = MissionsStreets.GetAllDataForShape();
				List<Integer> missionStreetIdList = new ArrayList<Integer>();
				
				for (MissionsStreets str : missStreets) {
					missionStreetIdList.add(new Integer(str.getStreetId()));
				}
				
				polygons = com.telekurye.kml.Polygon.GetStreetShapeByStreetIdList(ctx, missionStreetIdList, db_path, db_name);
				
				polygons.addAll(com.telekurye.kml.Polygon.GetBuildingShapeByDistrictId(ctx, (long)ms.getDistrictId(), db_path, db_name));

				int visibleShapeCount;
				if (isZoomOpen) {
					visibleShapeCount = 600;
				}
				else {
					visibleShapeCount = 300;
				}

				for (Polygon poly : SelectedPolygonList) {
					poly.setVisible(true);
				}

				for (com.telekurye.kml.Polygon polygon : polygons) {
					polygon.coors = new ArrayList<LatLng>();

					final PolygonOptions polygonOptions = new PolygonOptions();

					String[] coors = SplitUsingTokenizer(polygon.coordinates, "||");

					for (String string : coors) {
						String[] coor = string.split(",");

						final LatLng point = new LatLng(Float.valueOf(coor[0]), Float.valueOf(coor[1]));

						polygon.coors.add(point);

						polygonOptions.add(point);
					}

					// if (shapeControl.weHaveThisShape(polygon)) {
					// polygonOptions.strokeColor(Color.RED);
					// }
					// else {
					// polygonOptions.strokeColor(Color.GREEN);
					// }
					
					if (polygon.type == 2) {
						polygonOptions.strokeColor(Color.RED);

						polygonOptions.fillColor(Color.TRANSPARENT);

						for (Long shapeId : shapeIdHistory) {
							if (shapeId.equals(polygon.polygonid)) {
								polygonOptions.fillColor(0x802EFE64);
							}
						}
					}
					else {
						if (polygon.polygonid == ms.getStreetId()) {
							polygonOptions.strokeColor(Color.YELLOW);
						}
						else if (CheckCompletionStatus(polygon, missStreets)) {
							polygonOptions.strokeColor(Color.GREEN);
						}
						else {
							polygonOptions.strokeColor(Color.WHITE);
						}
					}

					polygonOptions.strokeWidth(3);
					polygonOptionsList.add(polygonOptions);
				}

				if (ctx != null) {
					runOnUiThread(new Runnable() {
						public void run() {
							GoogleMap map = myMap;
							for (int i = 0; i < polygonOptionsList.size(); i++) {
								polList.add(map.addPolygon(polygonOptionsList.get(i)));
							}
							// progress.dismiss();
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

	public float distance(float lat_a, float lng_a, float lat_b, float lng_b) { // iki koordinat aras� uzakl�k (metre cinsinden)
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

	// ##################################################################################################

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

			//salihy: test ederken s�k�nt� yarat�yordu.
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

		tvNetworkStatus.setText(Tools.getNetworkType(FeedBack.this));

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			tvNetworkStatus.setBackgroundColor(Color.RED);
		}
		else {
			tvNetworkStatus.setBackgroundColor(Color.GREEN);
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

		if (UserAccuracy < LiveData.MAX_ACCURACY) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar calendar2 = Calendar.getInstance();

			Date dt = new Date();

			long timeRange = (dt.getTime() - lastSyncDate.getTime()) - (1000 * 60 * 60 * 2);
			// calendar2.setTimeInMillis(timeRange);

			calendar2.setTimeInMillis(timeRange);

			int saat = calendar2.get(Calendar.HOUR_OF_DAY);
			int dk = calendar2.get(Calendar.MINUTE);

			if (saat > 0) {
				tv_last_sync_date.setText("Son Senk. : " + saat + " saat " + dk + " dk �nce");
			}
			else {
				tv_last_sync_date.setText("Son Senk. : " + dk + " dk �nce");
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
			Tools.showShortCustomToast(FeedBack.this, "�lk Sokak G�revinde Yeni Bina ekleyemezsiniz");
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

		Info.PHOTO_COUNT = 3;
		llImages.setWeightSum(Info.PHOTO_COUNT);

		tv_mission_status_value.setText("Yeni D�� Kap� No G�revi");

		tv_apname.setText("Bina Ad� :");
		tv_apname_value.setVisibility(View.GONE);
		new_et_apname_value.setVisibility(View.VISIBLE);
		new_et_apname_value.setText("");
		new_et_apname_value.setHint("Bina ad� var ise buraya yaz�n�z...");

		btnStreetOrBuildingType.setText("Se�iniz");

		btnStreetOrBuildingType.setEnabled(true);

		ll_mission_type_info.setVisibility(View.GONE);
		ll_apno.setVisibility(View.VISIBLE);
		tv_apno.setVisibility(View.VISIBLE);
		tv_apno.setText("D�� Kap� No :");
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

}
