package com.vuzix.main.menu;

import java.util.ArrayList;
import java.util.List;

import com.vuzix.modeles.Appli;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AppliAdapter extends ArrayAdapter{
	private Context context;
	private ArrayList<Appli> listAppli;
	//List mMyComplexClassList;
	private LayoutInflater layoutInflater;
	private int selectedPosition;
	
	public AppliAdapter(Context context, int layoutResourceId, ArrayList<Appli> appli) {
		super(context, layoutResourceId, appli);
		this.context = context;
		this.listAppli = appli;
		this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return listAppli.size();
	}
	
	public Appli getItem(int position) {
		return listAppli.get(position);
	}
	
	public long getItemId(int position) {
		return  position;
	}
	
	public void setSelectedState(int selectedPosition) {
	    this.selectedPosition = selectedPosition;
	    //mSelectedAnswerState = state;
	} 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = convertView;
		if (convertView == null)
        {
			result = LayoutInflater.from(getContext()).inflate(R.layout.ligne_appli, parent, false);
        }
		Appli appli = getItem(position);
		TextView tvName = (TextView)result.findViewById(R.id.tvAppliName);
		tvName.setText(appli.getName());
		//result.setBackgroundResource(R.drawable.list_item_selector);
		if (position == selectedPosition){
			result.setBackgroundColor(Color.BLUE);
		}else{
			result.setBackgroundColor(Color.TRANSPARENT);
		}
        return result;
	}
	
	public void updateData(){
		this.notifyDataSetChanged();
	}
		
}
