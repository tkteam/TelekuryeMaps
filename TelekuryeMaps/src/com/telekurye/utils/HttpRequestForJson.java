package com.telekurye.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class HttpRequestForJson {

	private String	Json;

	private int		ResponseCode;	;

	public HttpRequestForJson(String tag, Activity act) {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Info.LOGIN_SERVICE_URL);

		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 1000 * 60 * 30;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 1000 * 60 * 30;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		httpclient.setParams(httpParameters);

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("f", tag));
			nameValuePairs.add(new BasicNameValuePair("u", Info.USERNAME));
			nameValuePairs.add(new BasicNameValuePair("p", Info.PASSWORD));
			nameValuePairs.add(new BasicNameValuePair("v", Integer.toString(Info.CURRENT_VERSION)));

			if (act != null) {
				TelephonyManager telephonyManager = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
				nameValuePairs.add(new BasicNameValuePair("i", telephonyManager.getDeviceId()));
				nameValuePairs.add(new BasicNameValuePair("s", telephonyManager.getSimSerialNumber()));
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			ResponseCode = response.getStatusLine().getStatusCode();

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			Json = result.toString();
			System.out.println(Json);
		}
		catch (ClientProtocolException e) {
			Tools.saveErrors(e);

		}
		catch (IOException e) {
			Tools.saveErrors(e);

		}

	}

	public HttpRequestForJson(String tag, String DateRange) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Info.LOGIN_SERVICE_URL);
		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("f", tag));
			nameValuePairs.add(new BasicNameValuePair("u", Info.USERNAME));
			nameValuePairs.add(new BasicNameValuePair("p", Info.PASSWORD));
			nameValuePairs.add(new BasicNameValuePair("i", Info.IMEI));
			nameValuePairs.add(new BasicNameValuePair("v", Integer.toString(Info.CURRENT_VERSION)));
			nameValuePairs.add(new BasicNameValuePair("syncJ", DateRange));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			ResponseCode = response.getStatusLine().getStatusCode();
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			Json = result.toString();
			System.out.println(Json);
		}
		catch (ClientProtocolException e) {
			Tools.saveErrors(e);
		}
		catch (IOException e) {
			Tools.saveErrors(e);
		}

	}

	public String getJson() {
		return Json;
	}

	public int getResponseCode() {
		return ResponseCode;
	}

}
