package com.telekurye.mobileui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.telekurye.data.Person;
import com.telekurye.data.VersionUpdate;
import com.telekurye.data.typetoken.SyncResult;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.tools.Info;
import com.telekurye.tools.LiveData;
import com.telekurye.tools.Tools;
import com.telekurye.utils.AppConfig;
import com.telekurye.utils.HttpRequestForJson;
import com.telekurye.utils.JsonToDatabase;
import com.telekurye.utils.SendDatabaseRecords;
import com.telekurye.utils.ShellHelper;

public class Login extends Activity implements OnClickListener {

	public static Context		AppContext			= null;
	public static int			errorStatus			= 0;
	EditText					user_name;
	EditText					user_password;
	Button						login_button;
	Button						btnGetPassword;
	ProgressDialog				progressDialog;
	TextView					tvVersion;

	public static final String	PREFS_NAME			= "user_remember_log";
	public static final String	PREF_CHECK_STATUS	= "checkstatus";
	public static boolean		isDatabaseReset		= false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// if (Info.ISTEST) {
		// Mint.initAndStartSession(Login.this, "0491bf0a");
		// }
		// else {
		// Mint.initAndStartSession(Login.this, "284c6362");
		// Tools.toggleWIFI(Login.this, false);
		// // Tools.toggleGPS(Login.this, true);
		// }
		//
		// Mint.enableDebug();

		Tools.disableScreenLock(this);

		AppContext = this.getApplicationContext();

		user_name = (EditText) findViewById(R.id.user_name);
		user_password = (EditText) findViewById(R.id.user_password);
		login_button = (Button) findViewById(R.id.login_button);
		btnGetPassword = (Button) findViewById(R.id.BtnSifreIste);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("Versiyon : " + Info.CURRENT_VERSION);

		user_name.setSelection(1);

		login_button.setOnClickListener(this);
		btnGetPassword.setOnClickListener(this);

		SharedPreferences prefVersion = getSharedPreferences(VersionUpdate.PREFS_VERSION, MODE_PRIVATE);
		Boolean prefReboot = prefVersion.getBoolean(VersionUpdate.PREFS_DO_REBOOT, false);

		Integer prefLastVersion = prefVersion.getInt(VersionUpdate.PREFS_LAST_VERSION, 0);

		List<String> cmdList = new ArrayList<String>();
		cmdList.add("rm " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/TelekuryeMap.apk");

		if (prefReboot && prefLastVersion < Info.CURRENT_VERSION) {

			prefVersion.edit().putBoolean(VersionUpdate.PREFS_DO_REBOOT, false).commit();

			cmdList.add("rm " + Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH + File.separator + Info.FILE_NAME);
			ShellHelper.doCmds(cmdList);
			ShellHelper.Reboot();
		}

		if (Info.ISTEST) {
			user_name.setText("5552222222");
			user_password.setText("2");
			Info.SYNCPERIOD = 1000 * 30;
		}

	}

	@Override
	public void onClick(View v) {

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode != ConnectionResult.SUCCESS) {

			GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1);
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000).show();
			}
			Tools.showLongCustomToast(Login.this, "Lütfen Google Play Hizmetlerini güncellemek için Sayýn Ali Bahadýr Kuþ (0533 601 4699) ile iletiþime geçiniz.");
			return;
		}

		if (v.getId() == R.id.login_button) {

			try {
				Info.USERNAME = user_name.getText().toString();
				Info.PASSWORD = user_password.getText().toString();

				Boolean isPass = passwordControl();
				Boolean isConnect = Tools.isConnectingToInternet(Login.this);

				loginuser(isPass, isConnect);

			}
			catch (Exception e) {
				errorStatus = 1;
				Tools.saveErrors(e);
			}

		}
		else if (v.getId() == R.id.BtnSifreIste) {

			if (Tools.isConnectingToInternet(Login.this)) {
				if (user_name.getText().toString().length() == 10) {
					Info.USERNAME = user_name.getText().toString();

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
					alertDialogBuilder.setTitle("Þifre Ýsteði");
					alertDialogBuilder.setMessage("Þifreniz Cep Telefonunuza Gönderilecektir. Kabul Ediyorsanýz Tamam'a Týklayýnýz.").setCancelable(false)
							.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									getPassword();
								}
							}).setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});

					AlertDialog alertDialog = alertDialogBuilder.create();

					alertDialog.show();

				}
				else {
					Tools.showShortCustomToast(Login.this, "Hatalý Giriþ! Lütfen Cep Telefonu Numaranýzý Doðru Giriniz");
				}
			}
			else {
				Tools.showLongCustomToast(Login.this, "Ýnternet Baðlantýsý Bulunamadý!");
			}

		}

	}

	private void loginuser(Boolean isPass, Boolean isConnect) throws IOException, MalformedURLException, ClientProtocolException, FileNotFoundException {
		if (isPass) {

			if (Info.USERNAME.equalsIgnoreCase(AppConfig.downloadAndInstallApk)) {
				if (isConnect)
					new AppConfig().downloadAndInstallApk(Login.this);
				else
					Tools.showShortCustomToast(Login.this, "Ýnternet baðlantýsý bulunamadý!");
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.openChrome)) {
				if (isConnect)
					new AppConfig().openChrome(Login.this);
				else
					Tools.showShortCustomToast(Login.this, "Ýnternet baðlantýsý bulunamadý!");
			}
			if (Info.USERNAME.equalsIgnoreCase(AppConfig.clearDatabase)) {
				new AppConfig().clearDatabase(Login.this);
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.clearAll)) {
				new AppConfig().clearAll(Login.this);
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.restartDevice)) {
				new AppConfig().restartDevice(Login.this);
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.openSettings)) {
				new AppConfig().openSettings(Login.this);
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.openSettingsThisApp)) {
				new AppConfig().openSettingsThisApp(Login.this);
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.openFileManager)) {
				new AppConfig().openFileManager(Login.this);
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.uninstallApp)) {
				new AppConfig().uninstallApp(Login.this);
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.installApp)) {
				new AppConfig().installApp(Login.this);
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.clearDownloadDirectory)) {
				new AppConfig().clearDownloadDirectory(Login.this);
			}
			else if (Info.USERNAME.equalsIgnoreCase(AppConfig.clearPhotosDirectory)) {
				new AppConfig().clearPhotosDirectory(Login.this);
			}
		}
		else {
			if (isConnect) {
				asyncTaskLogin();
			}
			else {
				Tools.showShortCustomToast(Login.this, "Ýnternet baðlantýsý bulunamadý!");
			}

		}
	}

	private Boolean passwordControl() {

		String part1 = null;
		String part2 = null;
		String part3 = null;

		String truePassword;

		int h2 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int m2 = Calendar.getInstance().get(Calendar.MINUTE);

		if (h2 < 10) {
			part1 = "0" + h2;
		}
		else {
			part1 = Integer.toString(h2);
		}

		part2 = Info.USERNAME;

		if (m2 < 30) {
			part3 = "1";
		}
		else {
			part3 = "2";
		}

		truePassword = part1 + part2 + part3;

		if (truePassword.equalsIgnoreCase(Info.PASSWORD)) {
			return true;
		}
		else {
			return false;
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

	private void asyncTaskLogin() {

		AsyncTask<Void, String, Boolean> login = new AsyncTask<Void, String, Boolean>() {

			@Override
			protected void onPreExecute() {

				super.onPreExecute();

				try {

					progressDialog = new ProgressDialog(Login.this);
					Resources res = getResources();
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

					errorStatus = 1;
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}

			}

			@Override
			protected Boolean doInBackground(Void... params) {

				try {

					publishProgress("Kullanýcý bilgileri kontrol ediliyor...", "20");

					JsonToDatabase jto = new JsonToDatabase();

					Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
					HttpRequestForJson httpReq = new HttpRequestForJson(Info.tagLogin, Login.this);

					String json = httpReq.getJson();
					int HttpResponseCode = httpReq.getResponseCode();

					Type listType = new TypeToken<SyncResult<Person>>() {
					}.getType();
					SyncResult<Person> person = gson.fromJson(json, listType);

					if (HttpResponseCode != 200 || person.getProcessStatus() != 200) {
						return false;
					}

					publishProgress("Tamamlanan eski görevler gönderiliyor...", "40");

					SendDatabaseRecords.SendRecords(Login.this, true);

					Boolean needDbReset = jto.saveLogin(gson, json);

					if (needDbReset) {
						DatabaseHelper.getDbHelper().clearDatabase();
					}
					publishProgress("Versiyon bilgileri kontrol ediliyor...", "60");
					jto.versionControl();

					synUptodateData();
					checkVersionUpdate();

					saveLastSyncDate(Login.this);

					return true;

				}
				catch (Exception e) {

					Tools.saveErrors(e);
					errorStatus = 1;
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}

					return false;
				}

			}

			private void synUptodateData() throws HttpException {
				JsonToDatabase jto;
				Gson gson;
				String json;
				jto = new JsonToDatabase();

				gson = new GsonBuilder().setPrettyPrinting().setDateFormat(LiveData.DATE_FORMAT).create();
				json = new HttpRequestForJson(Info.tagLogin, Login.this).getJson();

				if (json.compareTo("401") == 0) {
					throw new HttpException("Hata!\nLogin.java SynUpdateData()!");
				}

				// DatabaseHelper dbh = new DatabaseHelper(Login.this);
				// dbh.clearMissions();

				publishProgress("Veritabaný Yükleniyor...", "70");
				DatabaseHelper dbHelper = new DatabaseHelper(Login.this);
				dbHelper.CreateDatabase(Login.this);

				publishProgress("Görevler Yükleniyor...", "80");
				jto.saveMissions(Login.this);
				publishProgress("Þekil Listesi Yükleniyor...", "90");
				jto.saveBasarShapeId(Login.this);
				publishProgress("Tamamlandý!", "100");

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
						publishProgress("Uygulama Ýndiriliyor Lütfen Bekleyiniz...", Integer.toString(percent));
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
				progressDialog.setTitle(values[0]);
				progressDialog.setProgress(Integer.parseInt(values[1]));
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

					Tools.showShortCustomToast(Login.this, " Giriþ Baþarýsýz! \n\n Kullanýcý Adý veya Þifre Hatalý ");
				}
				else {
					Intent i1 = new Intent(Login.this, CourierType.class);
					startActivity(i1);

					Tools.showShortCustomToast(Login.this, "Giriþ Baþarýlý");
				}
			}

		};

		login.execute(); // login.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	private void getPassword() {

		AsyncTask<Void, String, Void> syncBack2 = new AsyncTask<Void, String, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				try {

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(Info.LOGIN_SERVICE_URL);

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("f", Info.tagGetPassword));
					nameValuePairs.add(new BasicNameValuePair("u", Info.USERNAME));

					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = httpclient.execute(httppost);
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

					StringBuffer result = new StringBuffer();

					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}

					publishProgress(result.toString());

				}
				catch (Exception e) {

					Tools.saveErrors(e);

				}

				return null;
			}

			@Override
			protected void onProgressUpdate(String... values) {
				super.onProgressUpdate(values);

				if (values[0].equalsIgnoreCase("{\"ProcessStatus\":200}")) {
					Tools.showLongCustomToast(Login.this, "Þifreniz Cep Telefonunuza Baþarýyla Gönderilmiþtir");
				}
				else {
					Tools.showLongCustomToast(Login.this, "Þifre Gönderilemedi! Telefon Numaranýzý Doðru Yazdýðýnýza Emin Olunuz");
				}

			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);

			}

		};
		syncBack2.execute();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {

			switch (event.getKeyCode()) {

			case KeyEvent.KEYCODE_HOME:

				return true;
			}
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

}
