package com.telekurye.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.telekurye.data.SyncTime;
import com.telekurye.data_send.ExceptionFeedBack;
import com.telekurye.mobileui.R;

public class Tools {

	public static void saveErrors(Exception e) {

		ACRA.getErrorReporter().handleSilentException(e);
		e.printStackTrace();

		// Mint.logException(e);

		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		ExceptionFeedBack exfb = new ExceptionFeedBack();
		exfb.setCUSTOM_DATA(Log.getStackTraceString(e));
		// exfb.setERROR(Integer.toString(Log.getStackTraceString(e).length()));
		exfb.setERROR(e.fillInStackTrace().toString());
		exfb.setUId(Integer.toString(Info.UserId));
		exfb.setUSER_CRASH_DATE(date);
		exfb.setIsCompleted(false);
		exfb.Insert();

	}

	public static void disableScreenLock(Context c) {
		PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
		WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "INFO");
		wl.acquire();

		KeyguardManager km = (KeyguardManager) c.getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardLock kl = km.newKeyguardLock("name");
		kl.disableKeyguard();
	}

	public static String getPhoneImei(Context c) {
		TelephonyManager TelephonyMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneIMEI = TelephonyMgr.getDeviceId();
		return phoneIMEI;
	}

	public static void toggleWIFI(Context c, boolean bool) {

		WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);

		if (wifiManager.isWifiEnabled() == bool) {
			return;
		}
		else {
			wifiManager.setWifiEnabled(bool);
		}
	}

	public static String getSimCardNumber(Context c) {

		TelephonyManager phoneManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		return phoneManager.getSimSerialNumber();

	}

	public static boolean isTablet(Context c) {
		return (c.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static void toggleGPS(Context c, boolean enable) {
		String provider = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (provider.contains("gps") == enable) {
			return; // the GPS is already in the requested state
		}

		final Intent poke = new Intent();
		poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		poke.setData(Uri.parse("3"));
		c.sendBroadcast(poke);
	}

	public static void showShortCustomToast(Context c, String message) {

		LayoutInflater inflater = LayoutInflater.from(c);
		View layout;

		layout = inflater.inflate(R.layout.z_toast, null);

		TextView text = (TextView) layout.findViewById(R.id.tvToast);
		text.setText(message);

		Toast toast = new Toast(c);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();

	}

	public static void showLongCustomToast(Context c, String message) {

		LayoutInflater inflater = LayoutInflater.from(c);
		View layout;

		layout = inflater.inflate(R.layout.z_toast, null);

		TextView text = (TextView) layout.findViewById(R.id.tvToast);
		text.setText(message);

		Toast toast = new Toast(c);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();

	}

	public static boolean isConnectingToInternet(Context c) {
		ConnectivityManager connectivity = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	public static boolean isConnectingToServer() throws UnknownHostException, NullPointerException {

		StringBuffer result = null;
		Boolean isConnect = null;

		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader rd = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Info.LOGIN_SERVICE_URL);

			HttpResponse response = httpclient.execute(httppost);

			is = response.getEntity().getContent();
			isr = new InputStreamReader(is);
			rd = new BufferedReader(isr);

			result = new StringBuffer();

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			if (result.toString() == null) {
				return false;
			}
			else {
				isConnect = result.toString().equalsIgnoreCase("{\"ProcessStatus\":403}");
			}

		}
		catch (ClientProtocolException e) {
			return false;
		}
		catch (IOException e) {
			return false;
		}
		catch (IllegalStateException e) {
			return false;
		}
		catch (Exception e) {
			return false;
		}
		finally {
			try {
				is.close();
				isr.close();
				rd.close();
			}
			catch (IOException e) {
				return false;
			}

		}

		if (isConnect) {
			return true;
		}
		else {
			return false;
		}

	}

	public static Boolean isGPSEnabled(Context c) {
		LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static String getDateNow() {
		DateFormat df = new SimpleDateFormat(LiveData.DATE_FORMAT);
		String dateNow = df.format(new Date());

		return dateNow;
	}

	public static int getBatteryLevel(Context c) {
		Intent batteryIntent = c.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		// Error checking that probably isn't needed but I added just in case.
		if (level == -1 || scale == -1) {
			return 50;
		}
		float result = ((float) level / (float) scale) * 100.0f;
		return (int) result;
	}

	public static void copyFiles(File sourceLocation, File targetLocation) throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}
			File[] files = sourceLocation.listFiles();
			for (File file : files) {
				InputStream in = new FileInputStream(file);
				OutputStream out = new FileOutputStream(targetLocation + "/" + file.getName());

				// Copy the bits from input stream to output stream
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				file.delete();
			}
		}
	}

	public static void clearDirectory(File targetDirectory) throws IOException {

		if (targetDirectory.isDirectory()) {
			File[] files = targetDirectory.listFiles();
			for (File file : files) {
				file.delete();
			}
		}
	}

	public static String getNetworkType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isConnected())
			return "Ýnternet Yok"; // not connected
		if (info.getType() == ConnectivityManager.TYPE_WIFI)
			return "WIFI";
		if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			int networkType = info.getSubtype();
			switch (networkType) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return "2G";
			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return "3G";
			case TelephonyManager.NETWORK_TYPE_LTE:
				return "4G";
			default:
				return "?";
			}
		}
		return "?";
	}

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
}
