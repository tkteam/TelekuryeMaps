package com.telekurye.mobileui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.telekurye.data.IMission;
import com.telekurye.data.MissionsBuildings;
import com.telekurye.data.MissionsStreets;
import com.telekurye.expandablelist.CustomListViewAdapter;
import com.telekurye.tools.Tools;

public class StreetInfo extends Activity {

	List<MissionsStreets>	mThisMissionStreets;
	MissionsStreets			ms;
	List<MissionsStreets>	mAllStreets;			// bu göreve ait iki sokak
	List<MissionsBuildings>	mBuildingsOddNo;		// seçilen sokaða ait tek sayýlý binalar
	List<MissionsBuildings>	mBuildingsEvenNo;
	List<MissionsBuildings>	mBuilds;
	ArrayList<IMission>		mMissionForFeedback;

	ListView				lv_StreetInfo;

	private int				grupid;
	private int				childid;
	private int				streettype;

	Button					btn_go_mission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.street_info);

		btn_go_mission = (Button) findViewById(R.id.btn_go_mission);

		if (getIntent().getExtras() != null) {
			grupid = getIntent().getExtras().getInt("grupid");
			childid = getIntent().getExtras().getInt("childid");
			streettype = getIntent().getExtras().getInt("streettype");
		}

		btn_go_mission.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(StreetInfo.this, FeedBack.class);
				i.putExtra("streettype", streettype);
				i.putExtra("grupid", grupid);
				i.putExtra("childid", childid);
				startActivity(i);
				finish();
			}
		});

		lv_StreetInfo = (ListView) findViewById(R.id.lv_street_info);

		MissionsStreets data = new MissionsStreets();

		mThisMissionStreets = data.GetStreetsByStreetId(grupid);

		// TODO: Sefa neden ms null geliyor olabilir ona bakacak.
		for (MissionsStreets street : mThisMissionStreets) {
			if (street.getBuildingNumber_IsOdd()) {
				ms = street;
			}
		}

		if (ms == null) {
			// TODO: log gönder.
			finish();
		}

		// +++++ seçilen sokaða ait binalar tek ve çift listelere ayrýldý +++++
		// List<MissionsBuildings> mBuilds = LiveData.misBuildings;
		MissionsBuildings bData = new MissionsBuildings();
		mBuilds = bData.GetBuildingsByStreetId(ms.getStreetId());
		mBuildingsOddNo = new ArrayList<MissionsBuildings>();
		mBuildingsEvenNo = new ArrayList<MissionsBuildings>();

		try {
			for (int j = 0; j < mBuilds.size(); j++) {
				if (mBuilds.get(j).getBuildingNumber_IsOdd()) {
					mBuildingsOddNo.add(mBuilds.get(j));
				}
				else {
					mBuildingsEvenNo.add(mBuilds.get(j));
				}
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		if (streettype == 0 || streettype == 1 || streettype == 5) { // küme ev mi normal mi kontrolü
			mMissionForFeedback = MissionListCreator2(childid);
		}
		else {
			mMissionForFeedback = MissionListCreator(childid);
		}

		// for (int i = 0; i < mMissionForFeedback.size(); i++) {
		// System.out.println(mMissionForFeedback.get(i).getBuildingNumber() + " - " + mMissionForFeedback.get(i).getOrderIndex());
		// }

		// List<MissionsBuildings> mb = (List<MissionsBuildings>)mMissionForFeedback;

		CustomListViewAdapter adapter = new CustomListViewAdapter(this, R.layout.street_info, mMissionForFeedback);
		lv_StreetInfo.setAdapter(adapter);

	}

	private ArrayList<IMission> MissionListCreator(int chId) {

		ArrayList<IMission> temp = new ArrayList<IMission>();
		// bu kýsýmda kullanýcýnýn seçiþine göre liste
		if (chId == 0) { // artan çift -> azalan tek

			try {
				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // küçükten büyüðe sýrala
				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // büyükten küçüðe sýrala
				Collections.reverse(mBuildingsOddNo);

				// temp.add(mThisMissionStreets.get(0));
				temp.addAll(mBuildingsEvenNo);
				//
				// if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
				// temp.add(mThisMissionStreets.get(1));
				// }

				temp.addAll(mBuildingsOddNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 1) { // azalan çift -> artan tek

			try {

				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // büyükten küçüðe sýrala
				Collections.reverse(mBuildingsEvenNo);
				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // küçükten büyüðe sýrala

				// temp.add(mThisMissionStreets.get(0));
				temp.addAll(mBuildingsEvenNo);

				// if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
				// temp.add(mThisMissionStreets.get(1));
				// }
				temp.addAll(mBuildingsOddNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 2) { // artan tek -> azalan çift

			try {

				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // küçükten büyüðe sýrala
				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // büyükten küçüðe sýrala
				Collections.reverse(mBuildingsEvenNo);

				// temp.add(mThisMissionStreets.get(0));
				temp.addAll(mBuildingsOddNo);

				// if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
				// temp.add(mThisMissionStreets.get(1));
				// }

				temp.addAll(mBuildingsEvenNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		else if (chId == 3) { // azalan tek -> artan çift

			try {

				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // büyükten küçüðe sýrala
				Collections.reverse(mBuildingsOddNo);
				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // küçükten büyüðe sýrala

				// if (mThisMissionStreets.get(0) != null) {
				// temp.add(mThisMissionStreets.get(0));
				// }

				temp.addAll(mBuildingsOddNo);

				// if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
				// temp.add(mThisMissionStreets.get(1));
				// }
				temp.addAll(mBuildingsEvenNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		return temp;
	}

	private ArrayList<IMission> MissionListCreator2(int chId) { // küme evler için sýralama

		ArrayList<IMission> temp = new ArrayList<IMission>();
		// bu kýsýmda kullanýcýnýn seçiþine göre liste
		if (chId == 0) { // artan çift -> azalan tek

			try {
				Collections.sort(mBuilds, new MissionsBuildings()); // küçükten büyüðe sýrala

				// temp.add(mThisMissionStreets.get(0));

				temp.addAll(mBuilds);

				// if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
				// temp.add(mThisMissionStreets.get(1));
				// }

			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		else if (chId == 1) { // azalan çift -> artan tek

			try {
				Collections.sort(mBuilds, new MissionsBuildings()); // büyükten küçüðe sýrala
				Collections.reverse(mBuilds);

				// temp.add(mThisMissionStreets.get(0));

				temp.addAll(mBuilds);

				// if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
				// temp.add(mThisMissionStreets.get(1));
				// }
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}

		return temp;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
