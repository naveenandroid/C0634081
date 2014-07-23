package com.example.personaldairy.adapters;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.personaldairy.adapters.R;
import com.example.personaldairy.dto.NoteItem;

public class LVDairyNote extends BaseAdapter {

	public  ArrayList<NoteItem> listItems = null;
	LayoutInflater vi;
	private Context mContext;
	public LVDairyNote(Context context,ArrayList<NoteItem> list)
	{
		mContext=context;
		listItems=list;
		vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = vi.inflate(R.layout.row_note, null);
			holder = new ViewHolder();
			holder.tvNote = (TextView) convertView.findViewById(R.id.tvNotes);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvNote.setText(listItems.get(position).getNote());
		holder.tvDate.setText(listItems.get(position).getDate());
		
		return convertView;
	}
	static class ViewHolder {
		TextView tvNote;
		TextView tvDate; 

	}
	
	public void updateAdapter(ArrayList<NoteItem> result)
	{
		listItems = result;
		notifyDataSetChanged();
		
	}
}
