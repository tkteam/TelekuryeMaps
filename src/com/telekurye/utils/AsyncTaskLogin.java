package com.telekurye.utils;

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
import org.apache.http.HttpException;
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
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.telekurye.data.Person;
import com.telekurye.data.typetoken.SyncResult;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.mobileui.CourierType;
import com.telekurye.mobileui.Login;
import com.telekurye.mobileui.R;
import com.telekurye.tools.Info;
import com.telekurye.tools.LiveData;
import com.telekurye.tools.Tools;

public class AsyncTaskLogin extends AsyncTask<Void, String, Boolean> {

	Activity				Act;
	private ProgressDialog	progressDialog;
	public static int		errorStatus	= 0;

	public AsyncTaskLogin(Activity activity) {
		Act = activity;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		try {
			progressDialog = new ProgressDialog(Act);
			Resources res = Act.getResources();
			Drawable draw = res.getDrawable(R.drawable.progressbar1);
			progressDialog.setProgressDrawable(draw);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle("Giriþ Yapýlýyor...");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.show();
		}
		catch (Exception e) {
			Tools.saveErrors(e);
			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}

	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {

			doProgress("10", "Kullanýcý bilgileri kontrol ediliyor...");

			JsonToDatabase jto = new JsonToDatabase();

			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			HttpRequestForJson httpReq = new HttpRequestForJson(Info.tagLogin, Act);

			String json = httpReq.getJson();
			int HttpResponseCode = httpReq.getResponseCode();

			Type listType = new TypeToken<SyncResult<Person>>() {
			}.getType();
			SyncResult<Person> person = gson.fromJson(json, listType);

			if (HttpResponseCode != 200 || person.getProcessStatus() != 200) {
				return false;
			}
			// doProgress("40", "Tamamlanan eski görevler gönderiliyor...");
			if (Info.ISSENDFEEDBACK) {
				SendDatabaseRecords sdbr = new SendDatabaseRecords();
				sdbr.getAsyncTask(this);
				sdbr.SendRecords(Act, true);
			}

			Boolean needDbReset = jto.saveLogin(gson, json);

			if (Info.isClearAllCodeActive) {
				new AppConfig().clearAll(Act);
				Info.isClearAllCodeActive = false;
			}

			if (needDbReset) {
				DatabaseHelper.getDbHelper().clearDatabase();
			}

			doProgress("60", "Versiyon bilgileri kontrol ediliyor...");
			jto.versionControl();

			synUptodateData();
			checkVersionUpdate();

			saveLastSyncDate(Act);

			return true;

		}
		catch (Exception e) {

			errorStatus = 1;

			Tools.saveErrors(e);

			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			return false;
		}

	}

	public void doProgress(String... values) {
		publishProgress(values);
	}

	private void synUptodateData() throws HttpException, MalformedURLException, ClientProtocolException, FileNotFoundException, IOException {
		JsonToDatabase jto;
		Gson gson;
		String json;
		jto = new JsonToDatabase();

		gson = new GsonBuilder().setPrettyPrinting().setDateFormat(LiveData.DATE_FORMAT).create();
		json = new HttpRequestForJson(Info.tagLogin, Act).getJson();

		if (json.compareTo("401") == 0) {
			throw new HttpException("Hata!\nLogin.java SynUpdateData()!");
		}

		// DatabaseHelper dbh = new DatabaseHelper(Act);
		// dbh.clearMissions();

		doProgress("70", "Veritabaný Yükleniyor...");
		DatabaseHelper dbHelper = new DatabaseHelper(Act);
		dbHelper.CreateDatabase(Act);
		dbHelper.clearBasarShapeId();

		if (!Info.isDBinAssets && !dbHelper.getDbStatus()) {
			doProgress("75", "Veritabaný Ýndiriliyor...");
		}

		doProgress("80", "Görevler Yükleniyor...");
		jto.saveMissions(Act);
		doProgress("90", "Þekil Listesi Yükleniyor...");
		jto.saveBasarShapeId(Act);

		doProgress("100", "Tamamlandý!");

	}

	private void checkVersionUpdate() throws UnsupportedEncodingException, IOException, ClientProtocolException, MalformedURLException, FileNotFoundException {
		if (LiveData.versionControl.getTargetObject() != null && LiveData.versionControl.getTargetObject().getCurrentVersion() > Info.CURRENT_VERSION) {
			String DownloadUrl = LiveData.versionControl.getTargetObject().getApkFile();

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Info.LOGIN_SERVICE_URL);
			HttpResponse response = null;
			List<NameValuePair> postFields = new ArrayList<NameValuePair>();

			postFields.add(new BasicNameValuePair("f", Info.tagLogin));
			postFields.add(new BasicNameValuePair("u", Info.USERNAME));
			postFields.add(new BasicNameValuePair("p", Info.PASSWORD));
			postFields.add(new BasicNameValuePair("i", Info.IMEI));
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
				publishProgress(Integer.toString(percent), "Uygulama Ýndiriliyor Lütfen Bekleyiniz...");
			}

			fos.flush();
			fos.close();
			in.close();

			while (true) {
				String fileName = Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH + File.separator + Info.FILE_NAME;
				Intent itent = new Intent(Intent.ACTION_VIEW);
				itent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
				itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Login.AppContext.startActivity(itent);
			}
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);

		if (Integer.parseInt(values[0]) == 75) {
			this.cancel(true);
			AppConfig appConfig = new AppConfig();
			try {
				appConfig.downloadAppWithDB(progressDialog);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}

		progressDialog.setTitle(values[1]);
		progressDialog.setProgress(Integer.parseInt(values[0]));
	}

	@Override
	protected void onPostExecute(Boolean aVoid) {

		// if (new ProcessStatuses().GetAllData().get(0).getStatusCode() == 200 && aVoid) {
		if (!aVoid) {
			try {
				if ((progressDialog != null) && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
			catch (final IllegalArgumentException e) {
				// Handle or log or ignore
			}
			catch (final Exception e) {
				// Handle or log or ignore
			}
			finally {
				progressDialog = null;
			}

			if (errorStatus == 1) {
				Tools.showLongCustomToast(Act, " Sunucuya baðlanýlamadý, hata en kýsa sürede giderilecektir. Lütfen beklemede kalýnýz. ");
			}
			else {
				Tools.showLongCustomToast(Act, " Giriþ Baþarýsýz! \n\n Kullanýcý Adý veya Þifre Hatalý ");
			}
		}
		else {
			Intent i1 = new Intent(Act, CourierType.class);
			Act.startActivity(i1);
			Tools.showShortCustomToast(Act, "Giriþ Baþarýlý");
			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}
	}

	public void saveLastSyncDate(Activity act) {
		String lastSyncDate;
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		String json = new HttpRequestForJson(Info.tagServerTime, Tools.GetSyncDateRange(act)).getJson();
		Type listType = new TypeToken<SyncResult<String>>() {
		}.getType();
		SyncResult<String> date = gson.fromJson(json, listType);

		Tools.SetLastSyncDate(act, date.getTargetObject());
	}
}
