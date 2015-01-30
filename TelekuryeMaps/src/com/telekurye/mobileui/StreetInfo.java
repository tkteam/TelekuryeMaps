package com.telekurye.mobileui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.telekurye.expandablelist.CustomListViewAdapter;
import com.telekurye.utils.MissionListCreator;

public class StreetInfo extends Activity {

	ListView	lv_StreetInfo;

	private int	grupid;
	private int	childid;
	private int	streettype;

	Button		btn_go_mission;

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

		MissionListCreator mlc = new MissionListCreator(this, grupid, childid, streettype);

		CustomListViewAdapter adapter = new CustomListViewAdapter(this, R.layout.street_info, mlc.getMissionList(false));
		lv_StreetInfo.setAdapter(adapter);
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
