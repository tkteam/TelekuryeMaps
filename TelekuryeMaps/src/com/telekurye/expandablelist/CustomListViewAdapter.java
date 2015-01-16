package com.telekurye.expandablelist;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.telekurye.data.IMission;
import com.telekurye.mobileui.R;
import com.telekurye.tools.Constant;

public class CustomListViewAdapter extends ArrayAdapter<IMission> {

	Context			context;
	List<IMission>	items;

	public CustomListViewAdapter(Context context, int resourceId, List<IMission> i) {
		super(context, resourceId, i);
		this.context = context;
		this.items = i;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView	tv_building_number;
		TextView	tv_building_name;
		TextView	tv_person_name_value;
		TextView	tv_independent_section_value;
		TextView	tv_building_type_value;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// MissionsBuildings items = (MissionsBuildings) getItem(position);
		IMission items = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_street_info, null);

			holder = new ViewHolder();

			holder.tv_building_number = (TextView) convertView.findViewById(R.id.tv_building_number);
			holder.tv_building_name = (TextView) convertView.findViewById(R.id.tv_building_name_value);
			holder.tv_person_name_value = (TextView) convertView.findViewById(R.id.tv_person_name_value);
			holder.tv_independent_section_value = (TextView) convertView.findViewById(R.id.tv_info_independent_section_value);
			holder.tv_building_type_value = (TextView) convertView.findViewById(R.id.tv_info_building_type_value);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		if (items.getName().trim().equalsIgnoreCase("") || items.getName() == null || items == null) {
			holder.tv_building_name.setText("Bilgi Yok");
		}
		else {
			holder.tv_building_name.setText(items.getName().trim());
		}

		if (items.getBuildingNumber().trim().equalsIgnoreCase("") || items.getBuildingNumber() == null || items == null) {
			holder.tv_building_number.setText("Bilgi Yok");
		}
		else {
			holder.tv_building_number.setText(items.getBuildingNumber().trim());
		}

		if (items.getPersonNameSurname().trim().equalsIgnoreCase("") || items.getPersonNameSurname() == null || items == null) {
			holder.tv_person_name_value.setText("Bilgi Yok");
		}
		else {
			holder.tv_person_name_value.setText(items.getPersonNameSurname().trim());
		}

		if (items.getIndependentSectionType().trim().equalsIgnoreCase("") || items == null) {
			holder.tv_building_type_value.setText("Bilgi Yok");
		}
		else {
			holder.tv_building_type_value.setText(items.getIndependentSectionType());
		}

		if (items.getIndependentSectionCount() < 0 || items == null) {
			holder.tv_independent_section_value.setText("Bilgi Yok");
		}
		else {
			holder.tv_independent_section_value.setText("" + items.getIndependentSectionCount());
		}

		return convertView;
	}
}