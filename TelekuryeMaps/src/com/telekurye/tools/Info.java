package com.telekurye.tools;

public class Info {

	// ######################### KONTROL EDÝLECEKLER #############################

	public final static Boolean	ISTEST					= true;

	public final static int		CURRENT_VERSION			= 61;
	public static final int		DATABASE_VERSION		= 61;
	public static final String	DATABASE_NAME			= "telekuryemaps793.db";
	public static final String	MAP_DBNAME				= "geolocation6_db";

	public static String		LOGIN_SERVICE_URL		= "http://maksandroid.terralabs.com.tr/Default.aspx";
	public static String		PHOTO_SYNC_URL			= "http://maksandroid.terralabs.com.tr/PhotoSync.aspx";
	public static String		ERROR_LOG_URL			= "http://maksandroid.terralabs.com.tr/ExceptionLog.aspx";
	public static String		VEHICLE_PHOTO_SYNC_URL	= "http://maksandroid.terralabs.com.tr/PhotoSyncVehicle.aspx";

	// ###########################################################################

	// wifiye geç
	// public static String LOGIN_SERVICE_URL = "http://192.168.11.10/MAKS.Android.Web/Default.aspx";
	// public static String PHOTO_SYNC_URL = "http://192.168.11.10/MAKS.Android.Web/PhotoSync.aspx";
	// public static String ERROR_LOG_URL = "http://192.168.11.10/MAKS.Android.Web/ExceptionLog.aspx";
	// public static String VEHICLE_PHOTO_SYNC_URL = "http://192.168.11.10/MAKS.Android.Web/PhotoSyncVehicle.aspx";

	public static float			MAP_ZOOM_LEVEL			= 19.0f;
	public static int			PHOTO_COUNT				= 3;
	public static int			SYNCPERIOD				= 1000 * 60 * 2;
	public final static int		SCALED_PHOTO_PERCENT	= 70;

	public static float			GPS_MIN_DISTANCE		= 0;
	public static long			GPS_UPDATE_TIME			= 1000;
	public static int			GPS_ACCURACY			= 30;

	public final static String	UPDATE_DOWNLOAD_PATH	= "Download";
	public static final String	PHOTO_STORAGE_PATH		= "TelekuryeMaps";
	public final static String	FILE_NAME				= "TelekuryeMaps.apk";

	public static String		USERNAME				= "";
	public static String		PASSWORD				= "";
	public static String		IMEI					= "1";
	public static int			UserId					= 0;

	// ** Senkronizasyon
	public static String		tagLogin				= "login";
	public static String		tagBuildingTypes		= "syncbuildingtypes";
	public static String		tagStreetTypes			= "syncstreettypes";
	public static String		tagSyncMissions			= "syncmissions";
	public static String		tagServerTime			= "gsdt";
	public static String		tagVersionControl		= "checkforupdate"; 
	public static String		tagGetPassword			= "getpassword";
	public static String		tagEarnings				= "syncgetincome";
	public static String		tagBasarShapeId			= "getBasarShapeId";
	// ** Feedback
	public static String		tagMissionFeedBack		= "syncmissionfeedbacks";
	public static String		tagVehicleFeedBack		= "syncvehiclefeedback";
	public static String		tagVisitFeedBack		= "syncvisitfeedback";

}
