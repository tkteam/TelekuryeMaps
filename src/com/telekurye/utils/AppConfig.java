package com.telekurye.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.telekurye.database.DatabaseHelper;
import com.telekurye.mobileui.Login;
import com.telekurye.mobileui.R;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class AppConfig {

	// device
	public static String	restartDevice			= "0011";

	// install & uninstall
	public static String	installApp				= "0021";
	public static String	uninstallApp			= "0022";
	public static String	downloadAndInstallApk	= "0023";

	// clear
	public static String	clearDatabase			= "0031";
	public static String	clearDownloadDirectory	= "0032";
	public static String	clearPhotosDirectory	= "0033";

	// open
	public static String	openFileManager			= "0041";
	public static String	openChrome				= "0042";
	public static String	openSettings			= "0043";
	public static String	openSettingsThisApp		= "0044";

	// multi process
	public static String	clearAll				= "0051";

	public void clearAll(Activity act) throws IOException {
		clearDownloadDirectory(act);
		clearPhotosDirectory(act);
		clearDatabase(act);
	}

	public void restartDevice(Activity act) {
		Toast.makeText(act, "Cihaz Yeniden Ba�lat�l�yor...", Toast.LENGTH_LONG).show();
		ShellHelper.Reboot();
	}

	public void clearDatabase(Activity act) {
		// Toast.makeText(act, "T�m Veriler Sliniyor...", Toast.LENGTH_LONG).show();
		new DatabaseHelper(act).clearDatabase();
	}

	public void installApp(Activity act) {

		Toast.makeText(act, "Uygulama Y�kleniyor...", Toast.LENGTH_LONG).show();
		String file = Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH + File.separator + "telekuryeConfig.apk";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.fromFile(new File(file)), "application/vnd.android.package-archive");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		act.startActivity(i);
	}

	public void uninstallApp(Activity act) {
		Toast.makeText(act, "Uygulama Kald�r�l�yor...", Toast.LENGTH_LONG).show();
		Uri packageURI = Uri.parse("package:" + "com.telekurye.mobileui");
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		act.startActivity(uninstallIntent);
	}

	public void openFileManager(Activity act) {
		Toast.makeText(act, "Dosya Y�neticisi A��l�yor...", Toast.LENGTH_LONG).show();
		Intent i = new Intent();
		i.setAction("com.sec.android.app.myfiles.PICK_DATA");
		i.putExtra("CONTENT_TYPE", "*/*");
		act.startActivity(i);

	}

	public void openChrome(Activity act) {
		Toast.makeText(act, "Taray�c� A��l�yor...", Toast.LENGTH_LONG).show();
		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com.tr"));
		act.startActivity(intent);
	}

	public void openSettings(Activity act) {
		Toast.makeText(act, "Ayarlar A��l�yor...", Toast.LENGTH_LONG).show();
		act.startActivityForResult(new Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS), 0);

	}

	public void openSettingsThisApp(Activity act) {
		Toast.makeText(act, "Uygulama Ayarlar� A��l�yor...", Toast.LENGTH_LONG).show();
		Uri packageURI = Uri.parse("package:" + "com.telekurye.mobileui");
		Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", packageURI);
		act.startActivity(intent);

	}

	public void clearPhotosDirectory(Activity act) throws IOException {

		// Toast.makeText(act, "T�m Foto�raflar Siliniyor...", Toast.LENGTH_LONG).show();

		File targetDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH);

		if (targetDirectory.isDirectory()) {
			File[] files = targetDirectory.listFiles();
			for (File file : files) {
				file.delete();
			}
		}
	}

	public void clearDownloadDirectory(Activity act) throws IOException {

		// Toast.makeText(act, "T�m Dosyalar Siliniyor...", Toast.LENGTH_LONG).show();

		File targetDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH);

		if (targetDirectory.isDirectory()) {
			File[] files = targetDirectory.listFiles();
			for (File file : files) {
				file.delete();
			}
		}
	}

	public void downloadAndInstallApk(Activity act) throws IOException, MalformedURLException, ClientProtocolException, FileNotFoundException {
		Toast.makeText(act, "Dosya �ndiriliyor...", Toast.LENGTH_LONG).show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// String DownloadUrl = "http://maksandroid.terralabs.com.tr/AndroidVersions/TelekuryeMaps" + Info.CURRENT_VERSION + ".apk";

					String DownloadUrl = "http://maksandroid.terralabs.com.tr/AndroidVersions/TelekuryeMaps+(Info.CURRENT_VERSION+1)+.apk";

					HttpClient client = new DefaultHttpClient();

					HttpGet get = new HttpGet(DownloadUrl);
					int TotalFileSize = Integer.parseInt(new URL(DownloadUrl).openConnection().getHeaderField("Content-Length"));
					HttpResponse response = client.execute(get);

					HttpEntity entity = response.getEntity();
					InputStream in = entity.getContent();

					File path = new File(Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH);
					path.mkdirs();
					File file = new File(path, "telekuryeConfig.apk");
					FileOutputStream fos = new FileOutputStream(file);

					int downloadedSize = 0;
					byte[] buffer = new byte[1024];
					int len1 = 0;
					while ((len1 = in.read(buffer)) > 0) {
						fos.write(buffer, 0, len1);
						downloadedSize += len1;
						int percent = (int) (((float) downloadedSize / (float) TotalFileSize) * 100);
					}

					fos.flush();
					fos.close();
					in.close();

					while (true) {
						String fileName = Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH + File.separator + "telekuryeConfig.apk";
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Login.AppContext.startActivity(i);

					}

				}
				catch (Exception e) {
					Tools.saveErrors(e);
				}
			}
		}).start();

	}

	public void downloadAppWithDB(final ProgressDialog progressDialog) {

		AsyncTask<Void, String, Boolean> downloadAndInstall = new AsyncTask<Void, String, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				progressDialog.setProgress(0);
				progressDialog.setTitle("Veritaban� Eklenmi� Versiyon �ndiriliyor L�tfen Bekleyiniz...");

			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					String DownloadUrl = "http://maksandroid.terralabs.com.tr/AndroidVersions/TelekuryeMapsDB" + Info.CURRENT_VERSION + ".apk";

					// String DownloadUrl = "http://maksandroid.terralabs.com.tr/AndroidVersions/TelekuryeMaps62.apk";

					HttpClient client = new DefaultHttpClient();

					HttpGet get = new HttpGet(DownloadUrl);
					int TotalFileSize = Integer.parseInt(new URL(DownloadUrl).openConnection().getHeaderField("Content-Length"));
					HttpResponse response = client.execute(get);

					HttpEntity entity = response.getEntity();
					InputStream in = entity.getContent();

					File path = new File(Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH);
					path.mkdirs();
					File file = new File(path, "telekuryeConfig.apk");
					FileOutputStream fos = new FileOutputStream(file);

					int downloadedSize = 0;
					byte[] buffer = new byte[1024];
					int len1 = 0;
					while ((len1 = in.read(buffer)) > 0) {
						fos.write(buffer, 0, len1);
						downloadedSize += len1;
						int percent = (int) (((float) downloadedSize / (float) TotalFileSize) * 100);
						publishProgress(Integer.toString(percent), "Veritaban� Eklenmi� Versiyon �ndiriliyor L�tfen Bekleyiniz...");
					}

					fos.flush();
					fos.close();
					in.close();

					while (true) {
						String fileName = Environment.getExternalStorageDirectory() + File.separator + Info.UPDATE_DOWNLOAD_PATH + File.separator + "telekuryeConfig.apk";
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Login.AppContext.startActivity(i);

					}

				}
				catch (Exception e) {
					Tools.saveErrors(e);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(String... values) {
				super.onProgressUpdate(values);

				progressDialog.setTitle(values[0]);
				progressDialog.setProgress(Integer.parseInt(values[1]));

			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}
		};
		downloadAndInstall.execute();

	}

}
