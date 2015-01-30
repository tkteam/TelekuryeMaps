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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.telekurye.data.Missions;
import com.telekurye.data.sync.AutoSyncHelper;
import com.telekurye.expandablelist.AnimatedExpandableListView;
import com.telekurye.expandablelist.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.telekurye.expandablelist.Child;
import com.telekurye.expandablelist.Parent;
import com.telekurye.tools.Info;
import com.telekurye.tools.LiveData;
import com.telekurye.tools.Tools;
import com.telekurye.utils.OnMissionUpdated;

public class ExpandableList extends Activity {

	private static final String			STR_CHECKED			= " has Checked!";
	private static final String			STR_UNCHECKED		= " has unChecked!";
	private int							ParentClickStatus	= -1;
	private int							ChildClickStatus	= -1;
	private ArrayList<Parent>			parents;

	TextView							tvShowEmpty;

	private AnimatedExpandableListView	listView;
	private MyExpandableListAdapter		adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandablelist);

		Tools.disableScreenLock(this);

		tvShowEmpty = (TextView) findViewById(R.id.tvEmpty);

		LiveData.mStreetsAll = new ArrayList<MissionsStreets>();

		FillData();

		AutoSyncHelper.GetInstance().SetOnMissionUpdated(new OnMissionUpdated() {

			@Override
			public void Update() {
				asyncTaskExpandableList();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		asyncTaskExpandableList();
	}

	private void showEmpty() {

		if (LiveData.mStreetsAll.size() <= 0) {
			tvShowEmpty.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
		else {
			tvShowEmpty.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);

		}
	}

	private void FillData() {
		try {
			ArrayList<Parent> list = new ArrayList<Parent>();

			MissionsStreets data = new MissionsStreets();
			List<MissionsStreets> mStreetsAll = new ArrayList<MissionsStreets>();
			List<MissionsStreets> mStreetsAllUser = data.GetAllData();

			for (int i = 0; i < mStreetsAllUser.size(); i++) {
				if (mStreetsAllUser.get(i).getUserId() == Info.UserId) {
					mStreetsAll.add(mStreetsAllUser.get(i));
				}
			}

			List<MissionsStreets> mStreetsRemove = new ArrayList<MissionsStreets>();

			LiveData.mStreetsAll = new ArrayList<MissionsStreets>();
			LiveData.mStreetsAll.addAll(mStreetsAll);

			List<MissionsStreets> mStreets = new ArrayList<MissionsStreets>();

			for (int j = 0; j < mStreetsAll.size(); j++) {
				if (mStreetsAll.get(j).getBuildingNumber_IsOdd() && !mStreetsAll.get(j).getIsDeleted()) {
					mStreets.add(mStreetsAll.get(j));
				}
			}

			Collections.sort(mStreets, new MissionsStreets());

			FillList(list, mStreets);

			loadHosts(list);
		}
		catch (Exception e) {
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

			Missions mb = new Missions();

			List<Missions> mBuilds = new ArrayList<Missions>();

			List<Missions> mBuildssAllUser = mb.GetAllData();

			for (int m = 0; m < mBuildssAllUser.size(); m++) {

				if (mBuildssAllUser.get(m).getUserId() == Info.UserId) {
					mBuilds.add(mBuildssAllUser.get(m));
				}
			}

			int sayac = 0;

			for (int j = 0; j < mBuilds.size(); j++) {
				if (mStreets.get(i).getStreetId() == mBuilds.get(j).getStreetId() && !mBuilds.get(j).getIsDeleted()) {
					sayac++;
				}
			}

			int sayac2 = sayac + 2;

			parent.setText1("" + mStreets.get(i).getName().trim() + " Sokak");
			parent.setText2("" + mStreets.get(i).getAddressText().trim());
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

				LiveData.mStreetsAll = new ArrayList<MissionsStreets>();
				LiveData.mStreetsAll.addAll(mStreetsAll);

				List<MissionsStreets> mStreets = new ArrayList<MissionsStreets>();

				for (int j = 0; j < mStreetsAll.size(); j++) {
					if (mStreetsAll.get(j).getBuildingNumber_IsOdd() && !mStreetsAll.get(j).getIsDeleted()) {
						mStreets.add(mStreetsAll.get(j));
					}
				}

				Collections.sort(mStreets, new MissionsStreets());

				FillList(list, mStreets);

				publishProgress(list);

				return list;
			}

			private void FillList(ArrayList<Parent> list, List<MissionsStreets> mStreets) {
				for (int i = 0; i < mStreets.size(); i++) {
					Parent parent = new Parent();

					// List<MissionsBuildings> mBuilds = LiveData.misBuildings;
					Missions mb = new Missions();

					List<Missions> mBuilds = new ArrayList<Missions>();

					List<Missions> mBuildssAllUser = mb.GetAllData();

					for (int m = 0; m < mBuildssAllUser.size(); m++) {
						// if (mBuildssAllUser.get(m).getUserId() == new Person().GetAllData().get(0).getId()) {
						if (mBuildssAllUser.get(m).getUserId() == Info.UserId) {
							mBuilds.add(mBuildssAllUser.get(m));
						}
					}

					int sayac = 0;

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

		LiveData.syncBack2 = syncBack2;

		int corePoolSize = 60;
		int maximumPoolSize = 80;
		int keepAliveTime = 10;

		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
		Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

		syncBack2.executeOnExecutor(threadPoolExecutor);

	}

	private void loadHosts(final ArrayList<Parent> newParents) {
		if (newParents == null)
			return;

		parents = newParents;

		// Check for ExpandableListAdapter object
		if (listView == null) {
			// Create ExpandableListAdapter Object

			// Set ExpandableListView values
			adapter = new MyExpandableListAdapter();

			// adapter.setData(items);

			Resources res = this.getResources();
			Drawable devider = res.getDrawable(R.drawable.line);

			// final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

			// Set Adapter to ExpandableList Adapter
			listView = (AnimatedExpandableListView) findViewById(R.id.listView);
			listView.setAdapter(adapter);

			listView.setGroupIndicator(null);
			listView.setDivider(devider);
			listView.setChildDivider(devider);
			listView.setDividerHeight(1);
			registerForContextMenu(listView);

			listView.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

					if (listView.isGroupExpanded(groupPosition)) {
						listView.collapseGroupWithAnimation(groupPosition);
					}
					else {
						listView.expandGroupWithAnimation(groupPosition);
					}
					return true;
				}

			});

		}
		else {
			// Refresh ExpandableListView data
			((MyExpandableListAdapter) adapter).notifyDataSetChanged();
		}

		showEmpty();
	}

	private class MyExpandableListAdapter extends AnimatedExpandableListAdapter {

		private LayoutInflater	inflater;

		public MyExpandableListAdapter() {
			inflater = LayoutInflater.from(ExpandableList.this);
		}

		// This Function used to inflate parent rows view

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parentView) {
			final Parent parent = parents.get(groupPosition);

			convertView = inflater.inflate(R.layout.grouprow, parentView, false);

			((TextView) convertView.findViewById(R.id.tvGroup1)).setText(parent.getText1().trim());
			((TextView) convertView.findViewById(R.id.tvGroup2)).setText(parent.getText2().trim());
			((TextView) convertView.findViewById(R.id.tvChildCounter)).setText(parent.getCounter().trim());

			return convertView;
		}

		@Override
		public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parentView) {
			final Parent parent = parents.get(groupPosition);
			final Child child = parent.getChildren().get(childPosition);

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.childrow, null);
			}

			((TextView) convertView.findViewById(R.id.tvChild1)).setText(child.getText1());

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if (!Info.ISTEST && Info.ISSENDFEEDBACK) {
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

						if (diff <= 0) {
							Tools.showLongCustomToast(ExpandableList.this, "Çalýþma süresi sona ermiþtir.");
							finish();
							return;
						}

					}

					Intent i = new Intent(ExpandableList.this, StreetInfo.class);
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

		@Override
		public long getChildId(int groupPosition, int childPosition) {

			if (ChildClickStatus != childPosition) {
				ChildClickStatus = childPosition;

			}

			return childPosition;
		}

		@Override
		public int getRealChildrenCount(int groupPosition) {
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
