package com.telekurye.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.telekurye.data_send.ExceptionFeedBack;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class SendErrors {

	public SendErrors(ExceptionFeedBack exfb) {

		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Info.ERROR_LOG_URL);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("UId", exfb.getUId()));
			nameValuePairs.add(new BasicNameValuePair("ERROR", exfb.getERROR()));
			nameValuePairs.add(new BasicNameValuePair("CUSTOM_DATA", exfb.getCUSTOM_DATA()));
			nameValuePairs.add(new BasicNameValuePair("USER_CRASH_DATE", exfb.getUSER_CRASH_DATE()));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			Log.v("ERROR_LOG", result.toString());

		}
		catch (Exception e1) {
			Tools.saveErrors(e1);

		}

	}

}
