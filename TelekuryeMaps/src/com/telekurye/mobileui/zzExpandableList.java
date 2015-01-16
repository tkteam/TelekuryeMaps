package com.telekurye.mobileui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.telekurye.data.MissionsBuildings;
import com.telekurye.data.MissionsStreets;
import com.telekurye.data.sync.AutoSyncHelper;
import com.telekurye.expandablelist.Child;
import com.telekurye.expandablelist.Parent;
import com.telekurye.observer.OnMissionUpdated;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class zzExpandableList extends ExpandableListActivity {
	// Initialize variables
	private static final String	STR_CHECKED			= " has Checked!";
	private static final String	STR_UNCHECKED		= " has unChecked!";
	private int					ParentClickStatus	= -1;
	private int					ChildClickStatus	= -1;
	private ArrayList<Parent>	parents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Tools.disableScreenLock(this);
		Resources res = this.getResources();
		Drawable devider = res.getDrawable(R.drawable.line);

		// Set ExpandableListView values

		getExpandableListView().setGroupIndicator(null);
		getExpandableListView().setDivider(devider);
		getExpandableListView().setChildDivider(devider);
		getExpandableListView().setDividerHeight(1);
		registerForContextMenu(getExpandableListView());

		// Creating static data in arraylist
		// final ArrayList<Parent> dummyList = buildDummyData();

		// Adding ArrayList data to ExpandableListView values

		Info.mStreetsAll = new ArrayList<MissionsStreets>();

		FillData();

		AutoSyncHelper.GetInstance().SetOnMissionUpdated(new OnMissionUpdated() {

			@Override
			public void Update() {
				// TODO Auto-generated method stub
				// FillData();
				asyncTaskExpandableList();
			}
		});

		// loadHosts(dummyList);
	}

	@Override
	protected void onResume() {
		super.onResume();
		asyncTaskExpandableList();
	}

	private void FillData() {
		ArrayList<Parent> list = new ArrayList<Parent>();

		MissionsStreets data = new MissionsStreets();
		List<MissionsStreets> mStreetsAll = new ArrayList<MissionsStreets>();
		List<MissionsStreets> mStreetsAllUser = data.GetAllData();

		for (int i = 0; i < mStreetsAllUser.size(); i++) {
			// if (mStreetsAllUser.get(i).getUserId() == new Person().GetAllData().get(0).getId()) {
			if (mStreetsAllUser.get(i).getUserId() == Info.UserId) {
				mStreetsAll.add(mStreetsAllUser.get(i));
			}
		}

		List<MissionsStreets> mStreetsRemove = new ArrayList<MissionsStreets>();

		// if(Info.mStreetsAll == null)
		{
			Info.mStreetsAll = new ArrayList<MissionsStreets>();
			Info.mStreetsAll.addAll(mStreetsAll);
		}
		/*
		 * else{ for(MissionsStreets mStreet : mStreetsAll){ for(MissionsStreets mStreet2 : Info.mStreetsAll) { if(mStreet.getUserDailyMissionId() == mStreet2.getUserDailyMissionId() &&
		 * mStreet.getModifiedDate().compareTo(mStreet2.getModifiedDate()) == 0) { mStreetsRemove.add(mStreet); } } } mStreetsAll.removeAll(mStreetsRemove); Info.mStreetsAll.addAll(mStreetsAll); }
		 */

		List<MissionsStreets> mStreets = new ArrayList<MissionsStreets>();

		for (int j = 0; j < mStreetsAll.size(); j++) {
			if (mStreetsAll.get(j).getBuildingNumber_IsOdd() && !mStreetsAll.get(j).getIsDeleted()) {
				mStreets.add(mStreetsAll.get(j));
			}
		}

		Collections.sort(mStreets, new MissionsStreets());

		FillList(list, mStreets);

		loadHosts(list);

		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			Tools.saveErrors(e);

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AutoSyncHelper.GetInstance().SetOnMissionUpdated(null);
	}

	private void FillList(ArrayList<Parent> list, List<MissionsStreets> mStreets) {
		for (int i = 0; i < mStreets.size(); i++) {
			Parent parent = new Parent();

			// List<MissionsBuildings> mBuilds = LiveData.misBuildings;
			MissionsBuildings mb = new MissionsBuildings();

			List<MissionsBuildings> mBuilds = new ArrayList<MissionsBuildings>();

			List<MissionsBuildings> mBuildssAllUser = mb.GetAllData();

			for (int m = 0; m < mBuildssAllUser.size(); m++) {
				// if (mBuildssAllUser.get(m).getUserId() == new Person().GetAllData().get(0).getId()) {
				if (mBuildssAllUser.get(m).getUserId() == Info.UserId) {
					mBuilds.add(mBuildssAllUser.get(m));
				}
			}

			int sayac = 0; // toplam görev sayýsýna 2 adet sokak görevi de ekleneceði için 2'den baþlattým

			for (int j = 0; j < mBuilds.size(); j++) {
				if (mStreets.get(i).getStreetId() == mBuilds.get(j).getStreetId() && !mBuilds.get(j).getIsDeleted()) {
					sayac++;
				}
			}

			int sayac2 = sayac + 2;

			parent.setText1("" + mStreets.get(i).getName() + " Sokak");
			parent.setText2("" + mStreets.get(i).getAddressText());
			parent.setCounter("[" + Integer.toString(sayac2) + "]");
			parent.setChildren(new ArrayList<Child>());
			parent.setStreetId(mStreets.get(i).getStreetId());
			parent.setLastOperationDate(mStreets.get(i).getLastOperationDate());
			parent.setTypeId(mStreets.get(i).getStreetTypeId());
			parent.setBuildingCount(sayac2);

			if (mStreets.get(i).getStreetTypeId() == 5 || mStreets.get(i).getStreetTypeId() == 0 || mStreets.get(i).getStreetTypeId() == 1) {

				Child child = new Child();
				child.setText1("Küçükten Büyüðe\n\nÖrnek: 1, 2, 3, 4");
				parent.getChildren().add(child);

				Child child1 = new Child();
				child1.setText1("Büyükten Küçüðe\n\nÖrnek: 4, 3, 2, 1");
				parent.getChildren().add(child1);

			}
			else {
				Child child = new Child();
				child.setText1("Çiftler, Küçükten Büyüðe\n\nÖrnek: 2, 4, 6");
				parent.getChildren().add(child);

				Child child1 = new Child();
				child1.setText1("Çiftler, Büyükten Küçüðe\n\nÖrnek: 6, 4, 2");
				parent.getChildren().add(child1);

				Child child2 = new Child();
				child2.setText1("Tekler, Küçükten Büyüðe\n\nÖrnek: 1, 3, 5");
				parent.getChildren().add(child2);

				Child child3 = new Child();
				child3.setText1("Tekler, Büyükten Küçüðe\n\nÖrnek: 5, 3, 1");
				parent.getChildren().add(child3);
			}

			list.add(parent);
		}
	}

	public void asyncTaskExpandableList() {

		// if (Info.syncBack2 != null) {
		// boolean b = Info.syncBack2.cancel(true);
		// }

		AsyncTask<Void, ArrayList<Parent>, ArrayList<Parent>> syncBack2 = new AsyncTask<Void, ArrayList<Parent>, ArrayList<Parent>>() {

			@Override
			protected ArrayList<Parent> doInBackground(Void... params) {

				ArrayList<Parent> list = new ArrayList<Parent>();

				// while (true && !this.isCancelled()) {
				// List<MissionsStreets> mStreetsAll = LiveData.misStreets;
				list = new ArrayList<Parent>();

				MissionsStreets data = new MissionsStreets();
				List<MissionsStreets> mStreetsAll = new ArrayList<MissionsStreets>();
				List<MissionsStreets> mStreetsAllUser = data.GetAllData();

				for (int i = 0; i < mStreetsAllUser.size(); i++) {
					// if (mStreetsAllUser.get(i).getUserId() == new Person().GetAllData().get(0).getId()) {
					if (mStreetsAllUser.get(i).getUserId() == Info.UserId) {
						mStreetsAll.add(mStreetsAllUser.get(i));
					}
				}

				List<MissionsStreets> mStreetsRemove = new ArrayList<MissionsStreets>();

				// if(Info.mStreetsAll == null)
				{
					Info.mStreetsAll = new ArrayList<MissionsStreets>();
					Info.mStreetsAll.addAll(mStreetsAll);
				}
				/*
				 * else{ for(MissionsStreets mStreet : mStreetsAll){ for(MissionsStreets mStreet2 : Info.mStreetsAll) { if(mStreet.getUserDailyMissionId() == mStreet2.getUserDailyMissionId() &&
				 * mStreet.getModifiedDate().compareTo(mStreet2.getModifiedDate()) == 0) { mStreetsRemove.add(mStreet); } } } mStreetsAll.removeAll(mStreetsRemove);
				 * Info.mStreetsAll.addAll(mStreetsAll); }
				 */

				List<MissionsStreets> mStreets = new ArrayList<MissionsStreets>();

				for (int j = 0; j < mStreetsAll.size(); j++) {
					if (mStreetsAll.get(j).getBuildingNumber_IsOdd() && !mStreetsAll.get(j).getIsDeleted()) {
						mStreets.add(mStreetsAll.get(j));
					}
				}

				Collections.sort(mStreets, new MissionsStreets());

				FillList(list, mStreets);

				publishProgress(list);

				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					Tools.saveErrors(e);

				}
				// }

				return list;
			}

			private void FillList(ArrayList<Parent> list, List<MissionsStreets> mStreets) {
				for (int i = 0; i < mStreets.size(); i++) {
					Parent parent = new Parent();

					// List<MissionsBuildings> mBuilds = LiveData.misBuildings;
					MissionsBuildings mb = new MissionsBuildings();

					List<MissionsBuildings> mBuilds = new ArrayList<MissionsBuildings>();

					List<MissionsBuildings> mBuildssAllUser = mb.GetAllData();

					for (int m = 0; m < mBuildssAllUser.size(); m++) {
						// if (mBuildssAllUser.get(m).getUserId() == new Person().GetAllData().get(0).getId()) {
						if (mBuildssAllUser.get(m).getUserId() == Info.UserId) {
							mBuilds.add(mBuildssAllUser.get(m));
						}
					}

					int sayac = 0; // toplam görev sayýsýna 2 adet sokak görevi de ekleneceði için 2'den baþlattým

					for (int j = 0; j < mBuilds.size(); j++) {
						if (mStreets.get(i).getStreetId() == mBuilds.get(j).getStreetId() && !mBuilds.get(j).getIsDeleted()) {
							sayac++;
						}
					}

					int sayac2 = sayac + 2;

					parent.setText1("" + mStreets.get(i).getName() + " Sokak");
					parent.setText2("" + mStreets.get(i).getAddressText());
					parent.setCounter("[" + Integer.toString(sayac2) + "]");
					parent.setChildren(new ArrayList<Child>());
					parent.setStreetId(mStreets.get(i).getStreetId());
					parent.setLastOperationDate(mStreets.get(i).getLastOperationDate());
					parent.setTypeId(mStreets.get(i).getStreetTypeId());
					parent.setBuildingCount(sayac);

					if (mStreets.get(i).getStreetTypeId() == 0 || mStreets.get(i).getStreetTypeId() == 1 || mStreets.get(i).getStreetTypeId() == 5) {

						Child child = new Child();
						child.setText1("Küçükten Büyüðe\n\nÖrnek: 1, 2, 3, 4");
						parent.getChildren().add(child);

						Child child1 = new Child();
						child1.setText1("Büyükten Küçüðe\n\nÖrnek: 4, 3, 2, 1");
						parent.getChildren().add(child1);

					}
					else {
						Child child = new Child();
						child.setText1("Çiftler, Küçükten Büyüðe\n\nÖrnek: 2, 4, 6");
						parent.getChildren().add(child);

						Child child1 = new Child();
						child1.setText1("Çiftler, Büyükten Küçüðe\n\nÖrnek: 6, 4, 2");
						parent.getChildren().add(child1);

						Child child2 = new Child();
						child2.setText1("Tekler, Küçükten Büyüðe\n\nÖrnek: 1, 3, 5");
						parent.getChildren().add(child2);

						Child child3 = new Child();
						child3.setText1("Tekler, Büyükten Küçüðe\n\nÖrnek: 5, 3, 1");
						parent.getChildren().add(child3);
					}

					list.add(parent);
				}
			}

			@Override
			protected void onProgressUpdate(ArrayList<Parent>... values) {
				super.onProgressUpdate(values);
				loadHosts(values[0]);
			}

		};

		Info.syncBack2 = syncBack2;

		int corePoolSize = 60;
		int maximumPoolSize = 80;
		int keepAliveTime = 10;

		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
		Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

		syncBack2.executeOnExecutor(threadPoolExecutor);

	}

	// private ArrayList<Parent> buildDummyData() {
	// ArrayList<Parent> list = new ArrayList<Parent>();
	//
	// List<MissionsStreets> mStreetsAll = new MissionsStreets().GetAllData();
	// List<MissionsStreets> mStreets = new ArrayList<MissionsStreets>();
	//
	// for (int j = 0; j < mStreetsAll.size(); j++) {
	// if (mStreetsAll.get(j).getBuildingNumber_IsOdd()) {
	// mStreets.add(mStreetsAll.get(j));
	// }
	// }
	//
	// Collections.sort(mStreets, new MissionsStreets());
	//
	// for (int i = 0; i < mStreets.size(); i++) {
	// Parent parent = new Parent();
	//
	// List<MissionsBuildings> mBuilds = new MissionsBuildings().GetAllData();
	// int sayac = 0; // toplam görev sayýsýna 2 adet sokak görevi de ekleneceði için 2'den baþlattým
	//
	// for (int j = 0; j < mBuilds.size(); j++) {
	// if (mStreets.get(i).getStreetId() == mBuilds.get(j).getStreetId()) {
	// sayac++;
	// }
	// }
	//
	// int sayac2 = sayac + 2;
	//
	// parent.setText1("" + mStreets.get(i).getName() + " Sokak");
	// parent.setText2("" + mStreets.get(i).getAddressText());
	// parent.setCounter("[" + Integer.toString(sayac2) + "]");
	// parent.setChildren(new ArrayList<Child>());
	//
	// Child child = new Child();
	// child.setText1("Artan Çift Sayýlar       - Örnek: 2, 4, 6");
	// parent.getChildren().add(child);
	//
	// Child child1 = new Child();
	// child1.setText1("Azalan Çift Sayýlar     - Örnek: 6, 4, 2");
	// parent.getChildren().add(child1);
	//
	// Child child2 = new Child();
	// child2.setText1("Artan Tek Sayýlar       - Örnek: 1, 3, 5");
	// parent.getChildren().add(child2);
	//
	// Child child3 = new Child();
	// child3.setText1("Azalan Tek Sayýlar     - Örnek: 5, 3, 1");
	// parent.getChildren().add(child3);
	//
	// list.add(parent);
	// }
	//
	// return list;
	// }
	private void loadHosts(final ArrayList<Parent> newParents) {
		if (newParents == null)
			return;

		parents = newParents;

		// Check for ExpandableListAdapter object
		if (this.getExpandableListAdapter() == null) {
			// Create ExpandableListAdapter Object
			final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

			// Set Adapter to ExpandableList Adapter
			this.setListAdapter(mAdapter);
		}
		else {
			// Refresh ExpandableListView data
			((MyExpandableListAdapter) getExpandableListAdapter()).notifyDataSetChanged();
		}
	}

	/**
	 * A Custom adapter to create Parent view (Used grouprow.xml) and Child View((Used childrow.xml).
	 */
	private class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private LayoutInflater	inflater;

		public MyExpandableListAdapter() {
			inflater = LayoutInflater.from(zzExpandableList.this);
		}

		// This Function used to inflate parent rows view

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parentView) {
			final Parent parent = parents.get(groupPosition);

			// Inflate grouprow.xml file for parent rows
			convertView = inflater.inflate(R.layout.grouprow, parentView, false);

			// Get grouprow.xml file elements and set values
			((TextView) convertView.findViewById(R.id.tvGroup1)).setText(parent.getText1());
			((TextView) convertView.findViewById(R.id.tvGroup2)).setText(parent.getText2());
			((TextView) convertView.findViewById(R.id.tvChildCounter)).setText(parent.getCounter());

			// ImageView rightcheck = (ImageView) convertView.findViewById(R.id.rightcheck);

			// Log.i("onCheckedChanged", "isChecked: "+parent.isChecked());

			// Change right check image on parent at runtime
			// if (parent.isChecked() == true) {
			// rightcheck.setImageResource(getResources().getIdentifier("com.androidexample.customexpandablelist:drawable/rightcheck", null, null));
			// } else {
			// rightcheck.setImageResource(getResources().getIdentifier("com.androidexample.customexpandablelist:drawable/button_check", null, null));
			// }

			// Get grouprow.xml file checkbox elements
			// CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			// checkbox.setChecked(parent.isChecked());

			// Set CheckUpdateListener for CheckBox (see below CheckUpdateListener class)
			// checkbox.setOnCheckedChangeListener(new CheckUpdateListener(parent));

			return convertView;
		}

		// This Function used to inflate child rows view
		@Override
		public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parentView) {
			final Parent parent = parents.get(groupPosition);
			final Child child = parent.getChildren().get(childPosition);

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.childrow, null);
			}

			((TextView) convertView.findViewById(R.id.tvChild1)).setText(child.getText1());
			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			image.setImageResource(getResources().getIdentifier("com.androidexample.customexpandablelist:drawable/setting" + parent.getName(), null, null));

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if (!Info.ISTEST) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(parent.getLastOperationDate());

						int h = cal.get(Calendar.HOUR_OF_DAY);
						int m = cal.get(Calendar.MINUTE);
						int t = h * 60 + m;

						Calendar cal2 = Calendar.getInstance();
						cal2.setTime(new Date());
						int h2 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
						int m2 = Calendar.getInstance().get(Calendar.MINUTE);
						int t2 = h2 * 60 + m2;

						double diff = t - t2;
						double selectedStreetBuildingsCount = parent.getBuildingCount();

						double d = diff / selectedStreetBuildingsCount;

						if (d < 0.67 && d > 0 && diff < 60) {
							Tools.showLongCustomToast(zzExpandableList.this, "Bu görev kalan zamanýnýzda tamamlanamaz.");
							finish();
							return;
						}
						if (d <= 0) {
							Tools.showLongCustomToast(zzExpandableList.this, "Çalýþma süresi sona ermiþtir.");
							finish();
							return;
						}
					}

					Intent i = new Intent(zzExpandableList.this, FeedBack.class);
					i.putExtra("streettype", parent.getTypeId());
					i.putExtra("grupid", parent.getStreetId());
					i.putExtra("childid", childPosition);
					startActivity(i);

				}
			});

			return convertView;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
			return parents.get(groupPosition).getChildren().get(childPosition);
		}

		// Call when child row clicked
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			/****** When Child row clicked then this function call *******/

			// Log.i("Noise", "parent == "+groupPosition+"=  child : =="+childPosition);
			if (ChildClickStatus != childPosition) {
				ChildClickStatus = childPosition;

			}

			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			int size = 0;
			if (parents.get(groupPosition).getChildren() != null)
				size = parents.get(groupPosition).getChildren().size();
			return size;
		}

		@Override
		public Object getGroup(int groupPosition) {

			return parents.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return parents.size();
		}

		// Call when parent row clicked
		@Override
		public long getGroupId(int groupPosition) {

			if (groupPosition == 2 && ParentClickStatus != groupPosition) {

				// Alert to user
			}

			ParentClickStatus = groupPosition;
			if (ParentClickStatus == 0)
				ParentClickStatus = -1;

			return parents.get(groupPosition).getStreetId();

			// return groupPosition;
		}

		@Override
		public void notifyDataSetChanged() {
			// Refresh List rows
			super.notifyDataSetChanged();
		}

		@Override
		public boolean isEmpty() {
			return ((parents == null) || parents.isEmpty());
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

		/******************* Checkbox Checked Change Listener ********************/

	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
