package com.telekurye.utils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.data_send.MissionFeedBack;
import com.telekurye.data_send.MissionFeedBackPhoto;
import com.telekurye.data_send.VehicleFeedBack;
import com.telekurye.data_send.VisitFeedBack;
import com.telekurye.tools.Info;

public class SendDatabaseRecords {

	AsyncTaskLogin	asyncTaskLogin	= null;

	public void getAsyncTask(AsyncTaskLogin asynctl) {
		asyncTaskLogin = asynctl;
	}

	private void setProgress(String... strings) {
		if (asyncTaskLogin != null) {
			asyncTaskLogin.doProgress(strings);
		}
	}

	public Boolean SendRecords(final Activity act, Boolean isLogin) throws Exception {

		// ***** Vehicle Feedbackler g�nderiliyor. *******

		setProgress("20", "Tamamlanan eski ara� g�revleri g�nderiliyor... ");
		SyncRequest<List<VehicleFeedBack>> vehicleFBack = new VehicleFeedBack().GetAllDataForSync();
		int count1 = vehicleFBack.getTypedObjects().size();

		if (count1 > 0) {

			Type listType = new TypeToken<SyncRequest<List<VehicleFeedBack>>>() {
			}.getType();

			String jsn = JSONHelper.ToJson(vehicleFBack, listType);

			Boolean isSuccess = (new SendFeedback<VehicleFeedBack>()).Send(Info.tagVehicleFeedBack, jsn);

			if (isSuccess) {

				int sayac = 1;

				for (VehicleFeedBack vhfb : vehicleFBack.getTypedObjects()) {

					setProgress("20", "Tamamlanan eski ara� g�revleri g�nderiliyor... " + sayac + "/" + count1);

					String FilePath = Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH + File.separator + vhfb.getPhotoName();
					SendImageForVehicle.Send(FilePath); // g�nder

					File file = new File(FilePath); // fotoyu sil
					file.delete();

					vhfb.setIsCompleted(true);
					vhfb.Update();
					sayac++;
				}

				Log.d("SYNC", "Vehicle Feedbackler g�nderildi");

			}
			else {
				return false;
			}

		}

		// ***** Visit Feedbackler g�nderiliyor. *******

		setProgress("30", "Ziyaret edilen eski konumlar g�nderiliyor... ");
		SyncRequest<List<VisitFeedBack>> visitFBack = new VisitFeedBack().GetAllDataForSync();
		int count2 = visitFBack.getTypedObjects().size();

		if (count2 > 0) {

			Type listType = new TypeToken<SyncRequest<List<VisitFeedBack>>>() {
			}.getType();

			String jsn = JSONHelper.ToJson(visitFBack, listType);

			Boolean isSuccess = (new SendFeedback<VisitFeedBack>()).Send(Info.tagVisitFeedBack, jsn);

			if (isSuccess) {
				int sayac = 1;
				for (VisitFeedBack vfb : visitFBack.getTypedObjects()) {
					setProgress("30", "Ziyaret edilen eski konumlar g�nderiliyor... " + sayac + "/" + count2);
					vfb.setIsCompleted(true);
					vfb.Update();
					sayac++;
				}
				Log.d("SYNC", "Visit Feedbackler g�nderildi");
			}
			else {
				return false;
			}

		}

		// ***** Mission Feedbackler g�nderiliyor. *******

		setProgress("40", "Tamamlanan eski g�revler g�nderiliyor... ");
		SyncRequest<List<MissionFeedBack>> missionFBack = new MissionFeedBack().GetAllDataForSync();
		int count3 = missionFBack.getTypedObjects().size();

		if (count3 > 0) {

			Type listType = new TypeToken<SyncRequest<List<MissionFeedBack>>>() {
			}.getType();

			String jsn = JSONHelper.ToJson(missionFBack, listType);

			Boolean isSuccess = (new SendFeedback<MissionFeedBack>()).Send(Info.tagMissionFeedBack, jsn);

			if (isSuccess) {
				int sayac = 1;
				for (MissionFeedBack mfb : missionFBack.getTypedObjects()) {
					setProgress("40", "Tamamlanan eski g�revler g�nderiliyor... " + sayac + "/" + count3);
					mfb.setIsCompleted(true);
					mfb.Update();
					sayac++;
				}

				Log.d("SYNC", "Mission Feedbackler g�nderildi");
			}
			else {
				return false;
			}

		}

		// ***** Foto�raflar g�nderiliyor. *******

		setProgress("50", "�ekilen eski foto�raflar g�nderiliyor... ");
		SyncRequest<List<MissionFeedBackPhoto>> missionFBackPhoto = new MissionFeedBackPhoto().GetAllDataForSync();
		int count4 = missionFBackPhoto.getTypedObjects().size();

		if (count4 > 0) {

			int sayac = 0;
			int sayac1 = 1;
			for (MissionFeedBackPhoto mfbp : missionFBackPhoto.getTypedObjects()) {

				setProgress("50", "�ekilen eski foto�raflar g�nderiliyor... " + sayac1 + "/" + count4);

				String TypeId = Integer.toString(mfbp.getId());
				String UserDailyMissionId = Integer.toString(mfbp.getUserDailyMissionId());

				String FilePath = Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH + File.separator + mfbp.getPhoto();

				Boolean success = SendImage.Send(Integer.toString(sayac), TypeId, UserDailyMissionId, Info.PHOTO_SYNC_URL, FilePath); // g�nder

				if (success) {
					mfbp.setIsCompleted(true);
					mfbp.Update();

					File file = new File(FilePath); // fotoyu sil
					file.delete();
					sayac++;
				}
				sayac1++;
			}

			/*
			 * for (MissionFeedBackPhoto mfbp : missionFBackPhoto.getTypedObjects()) { mfbp.setIsCompleted(true); mfbp.Update(); }
			 */
		}

		// if (!isLogin) { // ilk giri�te fazla bekletmemek i�in loginde exceptionlar� g�nderme kald�r�ld�.
		// // ****** Exception'lar g�nderiliyor. *******
		//
		// List<ExceptionFeedBack> excFBack = new ExceptionFeedBack().GetAllData();
		//
		// if (excFBack.size() > 0) {
		//
		// for (ExceptionFeedBack exceptionFeedBack : excFBack) {
		//
		// new SendErrors(exceptionFeedBack);
		//
		// exceptionFeedBack.setIsCompleted(true);
		// exceptionFeedBack.Update();
		//
		// }
		//
		// }
		// }

		return true;
	}

}
