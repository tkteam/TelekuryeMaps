package com.telekurye.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;

import com.telekurye.data.Missions;
import com.telekurye.tools.Tools;

public class MissionListCreator {

	Activity				act;

	private int				grupid, childid, streettype;
	private List<Missions>	mThisMissionStreets;			// bu göreve ait iki sokak
	private List<Missions>	mBuildingsOddNo;				// seçilen sokaða ait tek sayýlý binalar
	private List<Missions>	mBuildingsEvenNo;				// seçilen sokaða ait çift sayýlý binalar
	private List<Missions>	mBuilds;
	private Missions		ms;

	public MissionListCreator(Activity activity, int grupId, int childId, int streetType) {

		act = activity;
		grupid = grupId;
		childid = childId;
		streettype = streetType;

		Missions data = new Missions(); // missionstreets

		mThisMissionStreets = data.GetStreetsByStreetId(grupid);

		// TODO: Sefa neden ms null geliyor olabilir ona bakacak.
		for (Missions street : mThisMissionStreets) {
			if (street.getBuildingNumber_IsOdd()) {
				ms = street;
			}
		}

		if (ms == null) {
			act.finish();
		}

		Missions bData = new Missions();
		mBuilds = bData.GetBuildingsByStreetId(ms.getStreetId());
		mBuildingsOddNo = new ArrayList<Missions>();
		mBuildingsEvenNo = new ArrayList<Missions>();

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

	private ArrayList<Missions> MissionListCreator1(int chId, Boolean showStreets) {

		ArrayList<Missions> temp = new ArrayList<Missions>();
		// bu kýsýmda kullanýcýnýn seçiþine göre liste
		if (chId == 0) { // artan çift -> azalan tek

			try {
				Collections.sort(mBuildingsEvenNo, new Missions()); // küçükten büyüðe sýrala
				Collections.sort(mBuildingsOddNo, new ReverseSorter()); // büyükten küçüðe sýrala
				// Collections.reverse(mBuildingsOddNo);

				if (showStreets) {
					if (mThisMissionStreets.get(0) != null) {
						temp.add(mThisMissionStreets.get(0));
					}
				}

				temp.addAll(mBuildingsEvenNo);

				if (showStreets) {
					if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
						temp.add(mThisMissionStreets.get(1));
					}
				}

				temp.addAll(mBuildingsOddNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 1) { // azalan çift -> artan tek

			try {

				Collections.sort(mBuildingsEvenNo, new ReverseSorter()); // büyükten küçüðe sýrala
				// Collections.reverse(mBuildingsEvenNo);
				Collections.sort(mBuildingsOddNo, new Missions()); // küçükten büyüðe sýrala

				if (showStreets) {
					if (mThisMissionStreets.get(0) != null) {
						temp.add(mThisMissionStreets.get(0));
					}
				}
				temp.addAll(mBuildingsEvenNo);

				if (showStreets) {
					if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
						temp.add(mThisMissionStreets.get(1));
					}
				}
				temp.addAll(mBuildingsOddNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 2) { // artan tek -> azalan çift

			try {

				Collections.sort(mBuildingsOddNo, new Missions()); // küçükten büyüðe sýrala
				Collections.sort(mBuildingsEvenNo, new ReverseSorter()); // büyükten küçüðe sýrala
				// Collections.reverse(mBuildingsEvenNo);
				if (showStreets) {
					if (mThisMissionStreets.get(0) != null) {
						temp.add(mThisMissionStreets.get(0));
					}
				}
				temp.addAll(mBuildingsOddNo);

				if (showStreets) {
					if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
						temp.add(mThisMissionStreets.get(1));
					}
				}

				temp.addAll(mBuildingsEvenNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);
			}

		}
		else if (chId == 3) { // azalan tek -> artan çift

			try {

				Collections.sort(mBuildingsOddNo, new ReverseSorter()); // büyükten küçüðe sýrala
				// Collections.reverse(mBuildingsOddNo);
				Collections.sort(mBuildingsEvenNo, new Missions()); // küçükten büyüðe sýrala

				if (showStreets) {
					if (mThisMissionStreets.get(0) != null) {
						temp.add(mThisMissionStreets.get(0));
					}
				}

				temp.addAll(mBuildingsOddNo);

				if (showStreets) {
					if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
						temp.add(mThisMissionStreets.get(1));
					}
				}
				temp.addAll(mBuildingsEvenNo);
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		return temp;
	}

	public class ReverseSorter implements Comparator<Missions> {
		@Override
		public int compare(Missions lhs, Missions rhs) {
			if (rhs.getOrderIndex() - lhs.getOrderIndex() != 0) {
				return rhs.getOrderIndex() - lhs.getOrderIndex();
			}
			else {
				return lhs.getBuildingNumber().compareTo(rhs.getBuildingNumber());
			}
		}
	}

	private ArrayList<Missions> MissionListCreator2(int chId, Boolean showStreets) { // küme evler için sýralama

		ArrayList<Missions> temp = new ArrayList<Missions>();
		// bu kýsýmda kullanýcýnýn seçiþine göre liste
		if (chId == 0) { // artan çift -> azalan tek

			try {
				Collections.sort(mBuilds, new Missions()); // küçükten büyüðe sýrala

				if (showStreets) {
					if (mThisMissionStreets.get(0) != null) {
						temp.add(mThisMissionStreets.get(0));
					}
				}

				temp.addAll(mBuilds);

				if (showStreets) {
					if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
						temp.add(mThisMissionStreets.get(1));
					}
				}

			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}
		else if (chId == 1) { // azalan çift -> artan tek

			try {
				Collections.sort(mBuilds, new Missions()); // büyükten küçüðe sýrala
				Collections.reverse(mBuilds);

				if (showStreets) {
					if (mThisMissionStreets.get(0) != null) {
						temp.add(mThisMissionStreets.get(0));
					}
				}

				temp.addAll(mBuilds);

				if (showStreets) {
					if (mThisMissionStreets.size() > 1 && mThisMissionStreets.get(1) != null) {
						temp.add(mThisMissionStreets.get(1));
					}
				}
			}
			catch (Exception e) {
				Tools.saveErrors(e);

			}

		}

		return temp;
	}

	public ArrayList<Missions> getMissionList(Boolean showStreets) {
		ArrayList<Missions> mList = new ArrayList<Missions>();

		if (streettype == 0 || streettype == 1 || streettype == 5) { // küme ev mi normal mi kontrolü
			mList = MissionListCreator2(childid, showStreets);
		}
		else {
			mList = MissionListCreator1(childid, showStreets);
		}

		return mList;
	}

	public Missions getThisStreet() {
		return ms;
	}

	public String getMainNumber(String str) {
		String[] separated = str.split(" - ");
		return separated[0];
	}

}
