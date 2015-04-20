package com.telekurye.mobileui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.telekurye.data.VersionUpdate;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;
import com.telekurye.utils.AppConfig;
import com.telekurye.utils.AsyncTaskLogin;
import com.telekurye.utils.ShellHelper;

public class Login extends Activity implements OnClickListener {

	public static Context		AppContext			= null;

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
		// cmdList.add("rm " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/TelekuryeMap.apk");

		if (prefReboot && prefLastVersion < Info.CURRENT_VERSION) {

			prefVersion.edit().putBoolean(VersionUpdate.PREFS_DO_REBOOT, false).commit();

			// cmdList.add("rm " + Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH + File.separator + Info.FILE_NAME);
			ShellHelper.doCmds(cmdList);
			ShellHelper.Reboot();
		}

		if (Info.ISTEST) {
			user_name.setText("5077735214");
			user_password.setText("478629");
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

				Info.isClearAllCodeActive = true;
				this.user_name.setText("");
				this.user_password.setText("");
				Tools.showShortCustomToast(Login.this, "Þimdi kendi kullanýcý adýnýz ve þifreniz ile giriþ yapýnýz.");
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

				AsyncTaskLogin asyncTaskLogin = new AsyncTaskLogin(Login.this);
				asyncTaskLogin.execute();
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
