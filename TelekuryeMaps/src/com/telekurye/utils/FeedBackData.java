package com.telekurye.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.data_send.MissionFeedBack;

public class FeedBackData {

	public void SendDistributionMissionFeedBack() {

		Type listType = new TypeToken<SyncRequest<List<MissionFeedBack>>>() {
		}.getType();

		String jsn = JSONHelper.ToJson("", listType);
		System.out.println(jsn);

		// sendFeedback sf = new sendFeedback(jsn,"");

	}

}
