package com.telekurye.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.telekurye.data.Person;
import com.telekurye.data.typetoken.SyncResult;
import com.telekurye.mobileui.Login;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class SendFeedback<T> {

	public SendFeedback() {
	}

	public Boolean Send(String tag, String JsonString) {
		Boolean retVal = false; 
		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = Info.LOGIN_SERVICE_URL;
			HttpPost post = new HttpPost(postURL);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("f", tag));
			params.add(new BasicNameValuePair("u", Info.USERNAME));
			params.add(new BasicNameValuePair("p", Info.PASSWORD));
			params.add(new BasicNameValuePair("i", Info.IMEI));
			params.add(new BasicNameValuePair("syncJ", JsonString));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);

			System.out.println("Feedback Response Code : " + responsePOST.getStatusLine().getStatusCode());

			// if (responsePOST.getStatusLine().getStatusCode() == 200) {
			// retVal = true;
			// } else {
			// retVal = false;
			// }

			String line = EntityUtils.toString(responsePOST.getEntity());

			// BufferedReader rd = new BufferedReader(new InputStreamReader(responsePOST.getEntity().getContent()));
			//
			// StringBuffer result = new StringBuffer();
			// String line = "";
			// while ((line = rd.readLine()) != null) {
			// result.append(line);
			// }

			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

			if (line.compareTo("") != 0) {
				Type listType = new TypeToken<SyncResult<T>>() {
				}.getType();
				SyncResult<T> person = gson.fromJson(line, listType);

				if (person.getProcessStatus() != 200) {
					retVal = false;
				}
				else {
					retVal = true;
				}
			}
			else {
				retVal = false;
			}

			// System.out.println(result.toString());
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return retVal;
	}

	public String SendDistricts(String tag, String JsonString) {

		String json = "";

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = Info.LOGIN_SERVICE_URL;
			HttpPost post = new HttpPost(postURL);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("f", tag));
			params.add(new BasicNameValuePair("u", Info.USERNAME));
			params.add(new BasicNameValuePair("p", Info.PASSWORD));
			params.add(new BasicNameValuePair("i", Info.IMEI));
			params.add(new BasicNameValuePair("syncJ", JsonString));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);

			// String line = EntityUtils.toString(responsePOST.getEntity());

			BufferedReader rd = new BufferedReader(new InputStreamReader(responsePOST.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			json = result.toString();
			// System.out.println(result.toString());
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return json;
	}

}
