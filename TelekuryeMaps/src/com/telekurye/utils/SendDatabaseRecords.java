package com.telekurye.utils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.data_send.ExceptionFeedBack;
import com.telekurye.data_send.MissionFeedBack;
import com.telekurye.data_send.MissionFeedBackPhoto;
import com.telekurye.data_send.VehicleFeedBack;
import com.telekurye.data_send.VisitFeedBack;
import com.telekurye.tools.Info;

public class SendDatabaseRecords {

	public static Boolean SendRecords(final Activity act, Boolean isLogin) throws Exception {

		// ***** Vehicle Feedbackler gönderiliyor. *******

		SyncRequest<List<VehicleFeedBack>> vehicleFBack = new VehicleFeedBack().GetAllDataForSync();

		int count = vehicleFBack.getTypedObjects().size();

		if (count > 0) {

			Type listType = new TypeToken<SyncRequest<List<VehicleFeedBack>>>() {
			}.getType();

			String jsn = JSONHelper.ToJson(vehicleFBack, listType);

			Boolean isSuccess = (new SendFeedback<VehicleFeedBack>()).Send(Info.tagVehicleFeedBack, jsn);

			if (isSuccess) {

				for (VehicleFeedBack vhfb : vehicleFBack.getTypedObjects()) {

					String FilePath = Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH + File.separator + vhfb.getPhotoName();
					SendImageForVehicle.Send(FilePath); // gönder

					File file = new File(FilePath); // fotoyu sil
					file.delete();

					vhfb.setIsCompleted(true);
					vhfb.Update();
				}

				Log.d("SYNC", "Vehicle Feedbackler gönderildi");

			}
			else {
				return false;
			}

		}

		// ***** Visit Feedbackler gönderiliyor. *******

		SyncRequest<List<VisitFeedBack>> visitFBack = new VisitFeedBack().GetAllDataForSync();

		if (visitFBack.getTypedObjects().size() > 0) {

			Type listType = new TypeToken<SyncRequest<List<VisitFeedBack>>>() {
			}.getType();

			String jsn = JSONHelper.ToJson(visitFBack, listType);

			Boolean isSuccess = (new SendFeedback<VisitFeedBack>()).Send(Info.tagVisitFeedBack, jsn);

			if (isSuccess) {

				for (VisitFeedBack vfb : visitFBack.getTypedObjects()) {
					vfb.setIsCompleted(true);
					vfb.Update();
				}
				Log.d("SYNC", "Visit Feedbackler gönderildi");
			}
			else {
				return false;
			}

		}

		// ***** Mission Feedbackler gönderiliyor. *******

		SyncRequest<List<MissionFeedBack>> missionFBack = new MissionFeedBack().GetAllDataForSync();

		if (missionFBack.getTypedObjects().size() > 0) {

			Type listType = new TypeToken<SyncRequest<List<MissionFeedBack>>>() {
			}.getType();

			String jsn = JSONHelper.ToJson(missionFBack, listType);

			Boolean isSuccess = (new SendFeedback<MissionFeedBack>()).Send(Info.tagMissionFeedBack, jsn);

			if (isSuccess) {

				for (MissionFeedBack mfb : missionFBack.getTypedObjects()) {
					mfb.setIsCompleted(true);
					mfb.Update();
				}

				Log.d("SYNC", "Mission Feedbackler gönderildi");
			}
			else {
				return false;
			}

		}

		// ***** Fotoðraflar gönderiliyor. *******

		SyncRequest<List<MissionFeedBackPhoto>> missionFBackPhoto = new MissionFeedBackPhoto().GetAllDataForSync();

		if (missionFBackPhoto.getTypedObjects().size() > 0) {

			int sayac = 0;

			for (MissionFeedBackPhoto mfbp : missionFBackPhoto.getTypedObjects()) {

				String TypeId = Integer.toString(mfbp.getId());
				String UserDailyMissionId = Integer.toString(mfbp.getUserDailyMissionId());

				String FilePath = Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH + File.separator + mfbp.getPhoto();

				new SendImage(Integer.toString(sayac), TypeId, UserDailyMissionId, Info.PHOTO_SYNC_URL, FilePath); // gönder

				File file = new File(FilePath); // fotoyu sil
				file.delete();
				sayac++;
			}

			for (MissionFeedBackPhoto mfbp : missionFBackPhoto.getTypedObjects()) {
				mfbp.setIsCompleted(true);
				mfbp.Update();
			}
		}

		if (!isLogin) { // ilk giriþte fazla bekletmemek için loginde exceptionlarý gönderme kaldýrýldý.
			// ****** Exception'lar gönderiliyor. *******

			List<ExceptionFeedBack> excFBack = new ExceptionFeedBack().GetAllData();

			if (excFBack.size() > 0) {

				for (ExceptionFeedBack exceptionFeedBack : excFBack) {

					new SendErrors(exceptionFeedBack);

					exceptionFeedBack.setIsCompleted(true);
					exceptionFeedBack.Update();

				}

			}
		}

		return true;
	}

}
