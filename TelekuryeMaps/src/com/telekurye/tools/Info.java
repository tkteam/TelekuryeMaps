package com.telekurye.tools;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;

import com.telekurye.data.MissionsStreets;
import com.telekurye.data.SyncTime;
import com.telekurye.expandablelist.Parent;

public class Info {

	// _________________________________________________________

	public final static Boolean	ISTEST					= true;

	// _________________________________________________________

	public static int			PHOTO_COUNT				= 3;

	public static int			SYNCPERIOD				= 1000 * 60 * 2;

	public final static int		CURRENT_VERSION			= 58;

	public final static int		SCALED_PHOTO_PERCENT	= 70;

	public static float			GPS_MIN_DISTANCE		= 0;
	public static long			GPS_UPDATE_TIME			= 1000;
	public static int			GPS_ACCURACY			= 30;

	public static float			MAP_ZOOM_LEVEL			= 19.0f;

	public final static String	UPDATE_DOWNLOAD_PATH	= "Download";
	public static final String	PHOTO_STORAGE_PATH		= "TelekuryeMaps";
	
	public static final String	MAP_DBNAME				= "geolocation3_db";

	// public final static String UPDATE_DOWNLOAD_PATH = "/storage/emulated/0/Download/";
	// public static final String PHOTO_STORAGE_PATH = "/sdcard/TelekuryeMaps/";
	public final static String	FILE_NAME				= "TelekuryeMaps.apk";

	public static int			GPS_CONTROL_PERIOD		= 1000 * 3;																			// metre cinsinden doðruluk deðeri
	public static int			GPS_CONTROL_COUNT		= SYNCPERIOD / GPS_CONTROL_PERIOD;

	public static String		DATE_FORMAT				= "yyyy-MM-dd HH:mm:ss";
	public static String		SYNCDATERANGE			= "{\"EndSyncDate\":\"2034-08-25T10:13:09\",\"LastSyncDate\":\"1986-08-22T00:30:00\"}";

	public static String GetSyncDateRange(Activity act) {
		// SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
		// String lastSyncDate = sharedPref.getString("LastSyncDate_" + Info.USERNAME, "1986-08-22T00:30:00");
		String lastSyncDate = null;
		SyncTime syncFetcherObj = new SyncTime();
		// List<SyncTime> fetchedObjects = syncFetcherObj.GetAllData();
		List<SyncTime> fetchedObjects = syncFetcherObj.GetDataForUser(Info.UserId);
		if (fetchedObjects.size() > 0) {
			lastSyncDate = fetchedObjects.get(0).getLastSyncDate();
		}
		else {
			lastSyncDate = "1987-03-03T03:00:00";
		}

		return "{\"EndSyncDate\":\"2034-08-25T10:13:09\",\"LastSyncDate\":\"" + lastSyncDate + "\"}";
	}

	public static void SetLastSyncDate(Activity act, String date) {
		// SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
		//
		// SharedPreferences.Editor editor = sharedPref.edit();
		// editor.putString("LastSyncDate_" + Info.USERNAME, date);
		// editor.commit();
		SyncTime syncFetcherObj = new SyncTime();
		// List<SyncTime> fetchedObjects = syncFetcherObj.GetAllData();
		List<SyncTime> fetchedObjects = syncFetcherObj.GetDataForUser(Info.UserId);

		if (fetchedObjects.size() == 0) {
			fetchedObjects.add(new SyncTime());
		}
		fetchedObjects.get(0).setLastSyncDate(date);
		fetchedObjects.get(0).setUserId(Info.UserId);
		fetchedObjects.get(0).Insert();
	}

	public static String												USERNAME				= "";
	public static String												PASSWORD				= "";
	public static String												IMEI					= "1";
	public static int													UserId					= 0;

	public static String												LOGIN_SERVICE_URL		= "http://maksandroid.terralabs.com.tr/Default.aspx";
	public static String												PHOTO_SYNC_URL			= "http://maksandroid.terralabs.com.tr/PhotoSync.aspx";
	public static String												ERROR_LOG_URL			= "http://maksandroid.terralabs.com.tr/ExceptionLog.aspx";
	public static String												VEHICLE_PHOTO_SYNC_URL	= "http://maksandroid.terralabs.com.tr/PhotoSyncVehicle.aspx";

	// wifiye geç
	// public static String LOGIN_SERVICE_URL = "http://192.168.11.11/MAKS.Android.Web/Default.aspx";
	// public static String PHOTO_SYNC_URL = "http://192.168.11.11/MAKS.Android.Web/PhotoSync.aspx";
	// public static String ERROR_LOG_URL = "http://192.168.11.11/MAKS.Android.Web/ExceptionLog.aspx";
	// public static String VEHICLE_PHOTO_SYNC_URL = "http://192.168.11.11/MAKS.Android.Web/PhotoSyncVehicle.aspx";

	// ** Senkronizasyon
	public static String												tagLogin				= "login";
	public static String												tagBuildingTypes		= "syncbuildingtypes";
	public static String												tagStreetTypes			= "syncstreettypes";
	public static String												tagSyncMissions			= "syncmissions";
	public static String												tagServerTime			= "gsdt";
	public static String												tagVersionControl		= "checkforupdate";
	public static String												tagGetPassword			= "getpassword";
	public static String												tagEarnings				= "syncgetincome";

	// ** Feedback
	public static String												tagMissionFeedBack		= "syncmissionfeedbacks";
	public static String												tagVehicleFeedBack		= "syncvehiclefeedback";
	public static String												tagVisitFeedBack		= "syncvisitfeedback";

	public static AsyncTask<Void, ArrayList<Parent>, ArrayList<Parent>>	syncBack2				= null;
	public static List<MissionsStreets>									mStreetsAll				= null;

	// public static int ImageCount;

}
