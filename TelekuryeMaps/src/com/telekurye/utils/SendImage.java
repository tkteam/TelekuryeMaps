package com.telekurye.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.telekurye.data.Person;
import com.telekurye.data.typetoken.SyncResult;
import com.telekurye.tools.Info;
import com.telekurye.tools.LiveData;
import com.telekurye.tools.Tools;

public class SendImage {

	public static Boolean Send(String order, String UserDailyMissionTypeId, String UserDailyMissionId, String urlTo, String filepath) throws Exception {

		Boolean	hasSuccess	= true;
		
		HttpURLConnection conn = null;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;

		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
		String lineEnd = "\r\n";

		String result = "";

		// CookieStore cookies = Login();
		//
		// List<Cookie> asc = cookies.getCookies();
		//
		// String cookie = asc.get(0).getValue();

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		String[] q = filepath.split("/");
		int idx = q.length - 1;

		try {

			DateFormat df = new SimpleDateFormat(LiveData.DATE_FORMAT);
			String dateNow = null;
			try {
				dateNow = df.format(new Date());
			}
			catch (ParseException e) {
				Tools.saveErrors(e);

			}

			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);

			URL url = new URL(urlTo);
			conn = (HttpURLConnection) url.openConnection();

			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			// conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("Username", Info.USERNAME);
			conn.setRequestProperty("Password", Info.PASSWORD);
			conn.setRequestProperty("UploadDate", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			conn.setRequestProperty("UserDailyMissionTypeId", UserDailyMissionTypeId);
			conn.setRequestProperty("UserDailyMissionId", UserDailyMissionId);
			conn.setRequestProperty("PhotoOrder", order);

			outputStream = new DataOutputStream(conn.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + q[idx] + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);

			// Upload POST Data
			// String[] posts = post.split("&");
			// int max = posts.length;
			// for (int i = 0; i < max; i++) {
			// outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			// String[] kv = posts[i].split("=");
			// outputStream.writeBytes("Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
			// outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
			// outputStream.writeBytes(lineEnd);
			// outputStream.writeBytes(kv[1]);
			// outputStream.writeBytes(lineEnd);
			// }

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			inputStream = conn.getInputStream();
			result = convertStreamToString(inputStream);

			// if (result.compareTo("200") != 0) {
			// hasError = true;
			// }

			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

			if (result != null && result.compareTo("") != 0) {
				Type listType = new TypeToken<SyncResult<Person>>() {
				}.getType();
				SyncResult<Person> person = gson.fromJson(result, listType);

				if (person.getProcessStatus() != 200) {
					hasSuccess = false;
				}

			}
			else {
				hasSuccess = false;
			}

			// ****************************
			// int ch;
			// StringBuffer b = new StringBuffer();
			// while ((ch = inputStream.read()) != -1) {
			// b.append((char) ch);
			// }
			// String result2 = b.toString();
			// System.out.println(result2);
			// ****************************

			fileInputStream.close();
			inputStream.close();
			outputStream.flush();
			outputStream.close();
			System.out.println("send Image -> -> " + result);

		}
		catch (Exception e) {

			hasSuccess = false;
			Tools.saveErrors(e);
		}

		
		return hasSuccess;
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		catch (IOException e) {
			Tools.saveErrors(e);

			//
		}
		finally {
			try {
				is.close();
			}
			catch (IOException e) {
				Tools.saveErrors(e);

			}
		}
		return sb.toString();
	}

	public CookieStore Login() {

		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet(Info.LOGIN_SERVICE_URL + "?f=login&u=" + Info.USERNAME + "&p=" + Info.PASSWORD + "&i=" + Info.IMEI);

		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		}
		catch (ClientProtocolException e1) {
			Tools.saveErrors(e1);
		}
		catch (IOException e1) {
			Tools.saveErrors(e1);
		}

		HttpEntity entity = response.getEntity();

		if (entity != null) {
			try {
				entity.consumeContent();
			}
			catch (IOException e) {
				Tools.saveErrors(e);

			}
		}

		return httpclient.getCookieStore();
	}
}
