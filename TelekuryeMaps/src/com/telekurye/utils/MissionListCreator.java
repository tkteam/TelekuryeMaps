package com.telekurye.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;

import com.telekurye.data.IMission;
import com.telekurye.data.MissionsBuildings;
import com.telekurye.data.MissionsStreets;
import com.telekurye.tools.Tools;

public class MissionListCreator {

	Activity						act;

	private int						grupid, childid, streettype;
	private List<MissionsStreets>	mThisMissionStreets;			// bu g�reve ait iki sokak
	private List<MissionsBuildings>	mBuildingsOddNo;				// se�ilen soka�a ait tek say�l� binalar
	private List<MissionsBuildings>	mBuildingsEvenNo;				// se�ilen soka�a ait �ift say�l� binalar
	private List<MissionsBuildings>	mBuilds;
	private MissionsStreets			ms;

	public MissionListCreator(Activity activity, int grupId, int childId, int streetType) {
		act = activity;
		grupid = grupId;
		childid = childId;
		streettype = streetType;

		MissionsStreets data = new MissionsStreets();

		mThisMissionStreets = data.GetStreetsByStreetId(grupid);

		// TODO: Sefa neden ms null geliyor olabilir ona bakacak.
		for (MissionsStreets street : mThisMissionStreets) {
			if (street.getBuildingNumber_IsOdd()) {
				ms = street;
			}
		}

		if (ms == null) {
			act.finish();
		}

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

	}

	private ArrayList<IMission> MissionListCreator1(int chId) {

		ArrayList<IMission> temp = new ArrayList<IMission>();
		// bu k�s�mda kullan�c�n�n se�i�ine g�re liste
		if (chId == 0) { // artan �ift -> azalan tek

			try {
				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // k���kten b�y��e s�rala
				Collections.sort(mBuildingsOddNo, new ReverseSorter()); // b�y�kten k����e s�rala
				//Collections.reverse(mBuildingsOddNo);

				temp.add(mThisMissionStreets.get(0));
				temp.addAll(mBuildingsEvenNo);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}

				temp.addAll(mBuildingsOddNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 1) { // azalan �ift -> artan tek

			try {

				Collections.sort(mBuildingsEvenNo, new ReverseSorter()); // b�y�kten k����e s�rala
				//Collections.reverse(mBuildingsEvenNo);
				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // k���kten b�y��e s�rala

				temp.add(mThisMissionStreets.get(0));
				temp.addAll(mBuildingsEvenNo);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}
				temp.addAll(mBuildingsOddNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 2) { // artan tek -> azalan �ift

			try {

				Collections.sort(mBuildingsOddNo, new MissionsBuildings()); // k���kten b�y��e s�rala
				Collections.sort(mBuildingsEvenNo, new ReverseSorter()); // b�y�kten k����e s�rala
				//Collections.reverse(mBuildingsEvenNo);

				temp.add(mThisMissionStreets.get(0));
				temp.addAll(mBuildingsOddNo);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}

				temp.addAll(mBuildingsEvenNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 3) { // azalan tek -> artan �ift

			try {

				Collections.sort(mBuildingsOddNo, new ReverseSorter()); // b�y�kten k����e s�rala
				//Collections.reverse(mBuildingsOddNo);
				Collections.sort(mBuildingsEvenNo, new MissionsBuildings()); // k���kten b�y��e s�rala

				if (mThisMissionStreets.get(0) != null) {
					temp.add(mThisMissionStreets.get(0));
				}

				temp.addAll(mBuildingsOddNo);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}
				temp.addAll(mBuildingsEvenNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		return temp;
	}

	
	public class ReverseSorter implements Comparator<IMission> {
		@Override
		public int compare(IMission lhs, IMission rhs) {
			if (rhs.getOrderIndex() - lhs.getOrderIndex() != 0) {
				return rhs.getOrderIndex() - lhs.getOrderIndex();
			}
			else {
				return lhs.getBuildingNumber().compareTo(rhs.getBuildingNumber());
			}
		}
	}
	
	private ArrayList<IMission> MissionListCreator2(int chId) { // k�me evler i�in s�ralama

		ArrayList<IMission> temp = new ArrayList<IMission>();
		// bu k�s�mda kullan�c�n�n se�i�ine g�re liste
		if (chId == 0) { // artan �ift -> azalan tek

			try {
				Collections.sort(mBuilds, new MissionsBuildings()); // k���kten b�y��e s�rala

				temp.add(mThisMissionStreets.get(0));

				temp.addAll(mBuilds);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}

			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		else if (chId == 1) { // azalan �ift -> artan tek

			try {
				Collections.sort(mBuilds, new MissionsBuildings()); // b�y�kten k����e s�rala
				Collections.reverse(mBuilds);

				temp.add(mThisMissionStreets.get(0));

				temp.addAll(mBuilds);

				if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
					temp.add(mThisMissionStreets.get(1));
				}
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}

		return temp;
	}

	public ArrayList<IMission> getMissionList() {
		ArrayList<IMission> mList = new ArrayList<IMission>();

		if (streettype == 0 || streettype == 1 || streettype == 5) { // k�me ev mi normal mi kontrol�
			mList = MissionListCreator2(childid);
		}
		else {
			mList = MissionListCreator1(childid);
		}

		return mList;
	}

	public MissionsStreets getThisStreet() {
		return ms;
	}

	public String getMainNumber(String str) {
		String[] separated = str.split(" - ");
		return separated[0];
	}

}
