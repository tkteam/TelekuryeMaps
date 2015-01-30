package com.telekurye.data.sync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.telekurye.data.VersionUpdate;
import com.telekurye.data.typetoken.SyncResult;
import com.telekurye.mobileui.Login;
import com.telekurye.tools.Info;
import com.telekurye.tools.LiveData;
import com.telekurye.tools.Tools;
import com.telekurye.utils.HttpRequestForJson;
import com.telekurye.utils.JsonToDatabase;
import com.telekurye.utils.OnMissionUpdated;
import com.telekurye.utils.SendDatabaseRecords;

public class AutoSyncHelper {

	boolean							AutoSyncStart	= false;

	private static AutoSyncHelper	StaticInstance;
	private OnMissionUpdated		_update;

	public static synchronized AutoSyncHelper GetInstance() {
		if (StaticInstance == null) {
			StaticInstance = new AutoSyncHelper();
		}

		return StaticInstance;
	}

	public void SetOnMissionUpdated(OnMissionUpdated mission) {
		_update = mission;
	}

	// A timertask will update our data for every 2 minutes

	// salihy: ibrahimin anýsýna bu fonksiyonun adýný deðiþtirmiyorum
	public void startbackgroundUpdateAutomagically(Activity act) {

		// if((connectManager.getNetworkInfo(0).getState()==NetworkInfo.State.CONNECTED)||(connectManager.getNetworkInfo(1).getState()==NetworkInfo.State.CONNECTED)){

		Log.v("SENKRONIZASYON", "SENK Baþladý");

		asyncTaskSync(act);

	}

	ProgressDialog	progressDialog;
	Boolean			hasUpdate	= false;

	private void asyncTaskSync(final Activity act) {

		AsyncTask<Activity, String, Void> sync = new AsyncTask<Activity, String, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			private void checkVersionUpdate() throws UnsupportedEncodingException, IOException, ClientProtocolException, MalformedURLException, FileNotFoundException {
				if (LiveData.versionControl.getTargetObject() != null && LiveData.versionControl.getTargetObject().getCurrentVersion() > Info.CURRENT_VERSION
						&& LiveData.versionControl.getTargetObject().getNeedsUrgentUpdate()) {

					String DownloadUrl = LiveData.versionControl.getTargetObject().getApkFile();

					HttpClient client = new DefaultHttpClient();

					HttpPost post = new HttpPost(Info.LOGIN_SERVICE_URL);
					HttpResponse response = null;
					List<NameValuePair> postFields = new ArrayList<NameValuePair>();

					TelephonyManager telephonyManager = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);

					postFields.add(new BasicNameValuePair("f", Info.tagLogin));
					postFields.add(new BasicNameValuePair("u", Info.USERNAME));
					postFields.add(new BasicNameValuePair("p", Info.PASSWORD));
					postFields.add(new BasicNameValuePair("i", Info.IMEI));
					postFields.add(new BasicNameValuePair("i", telephonyManager.getDeviceId()));
					postFields.add(new BasicNameValuePair("s", telephonyManager.getSimSerialNumber()));
					postFields.add(new BasicNameValuePair("v", Integer.toString(Info.CURRENT_VERSION)));

					post.setEntity(new UrlEncodedFormEntity(postFields, HTTP.UTF_8));

					response = client.execute(post);
					HttpGet get = new HttpGet(DownloadUrl);
					int TotalFileSize = Integer.parseInt(new URL(DownloadUrl).openConnection().getHeaderField("Content-Length"));
					response = client.execute(get);

					HttpEntity entity = response.getEntity();
					InputStream in = entity.getContent();

					File path = new File(Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH);
					path.mkdirs();
					File file = new File(path, Info.FILE_NAME);
					FileOutputStream fos = new FileOutputStream(file);

					int downloadedSize = 0;
					byte[] buffer = new byte[1024];
					int len1 = 0;
					while ((len1 = in.read(buffer)) > 0) {
						fos.write(buffer, 0, len1);
						downloadedSize += len1;
						int percent = (int) (((float) downloadedSize / (float) TotalFileSize) * 100);
						// publishProgress("Uygulama Ýndiriliyor Lütfen Bekleyiniz...", Integer.toString(percent));
					}

					fos.flush();
					fos.close();
					in.close();

					Login.AppContext.getSharedPreferences(VersionUpdate.PREFS_VERSION, Login.AppContext.MODE_PRIVATE).edit().putBoolean(VersionUpdate.PREFS_DO_REBOOT, true).commit();

					Login.AppContext.getSharedPreferences(VersionUpdate.PREFS_VERSION, Login.AppContext.MODE_PRIVATE).edit().putInt(VersionUpdate.PREFS_LAST_VERSION, Info.CURRENT_VERSION).commit();

					while (true) {

						String fileName = Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH + File.separator + Info.FILE_NAME;
						Intent itent = new Intent(Intent.ACTION_VIEW);
						itent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
						itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Login.AppContext.startActivity(itent);
					}
				}

				// progressDialog.dismiss();
				//

			}

			@Override
			protected Void doInBackground(Activity... params) {

				while (true) {

					try {
						if (Tools.isConnectingToServer()) { // Tools.isConnectingToInternet(act.getApplicationContext())

							String date = getCurrentDate();

							if (date == null || date.isEmpty()) {
								continue;
							}

							JsonToDatabase jto = new JsonToDatabase();

							Boolean hasSent = false;

							if (Info.ISSENDFEEDBACK) {
								hasSent = SendDatabaseRecords.SendRecords(act, false);
							}

							Boolean hasUpdated = jto.saveMissions(params[0]);
//							jto.saveBasarShapeId(act);
							jto.versionControl();
							jto.saveEarnings(act);

							if (hasUpdated && hasSent) {
								saveLastSyncDate(act, date);
							}

							if (hasUpdated && _update != null) {

								_update.Update();
								hasUpdated = false;
							}

							checkVersionUpdate();

							// publishProgress("Senkronizasyon Tamamlandý.", "100");
						}

					}
					catch (Exception e) {
						Tools.saveErrors(e);
					}

					try {
						Thread.sleep(Info.SYNCPERIOD);
					}
					catch (InterruptedException e) {
						Tools.saveErrors(e);
					}

				}
			}

			private String getCurrentDate() {
				String lastSyncDate;
				Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
				String json = new HttpRequestForJson(Info.tagServerTime, Tools.GetSyncDateRange(act)).getJson();
				Type listType = new TypeToken<SyncResult<String>>() {
				}.getType();
				SyncResult<String> date = gson.fromJson(json, listType);
				return date.getTargetObject();
			}

			@Override
			protected void onProgressUpdate(final String... values) {
				super.onProgressUpdate(values);

			}

			@Override
			protected void onPostExecute(Void aVoid) {

			}

		};
		sync.execute(act);
	}

	public void saveLastSyncDate(Activity act, String date) {
		// String lastSyncDate;
		// Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// String json = new HttpRequestForJson(Info.tagServerTime, Info.GetSyncDateRange(act)).getJson();
		// Type listType = new TypeToken<SyncResult<String>>() {
		// }.getType();
		// SyncResult<String> date = gson.fromJson(json, listType);

		if (date != null && !date.isEmpty()) {
			Tools.SetLastSyncDate(act, date);
		}
	}

}
