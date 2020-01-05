package com.evidencijaclanova;

import java.util.List;

import com.evidencijaclanova.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class GrupeExpandableAdapter extends BaseExpandableListAdapter  {
	
	private List<Grupa> grupaLista;
	private int itemLayoutId;
	private int groupLayoutId;
	Context ctx;
	DataBaseHelper db;
	Integer grupaId = -1;
		
	public GrupeExpandableAdapter(List<Grupa> catList, Context ctx, DataBaseHelper db) {
		
		this.itemLayoutId = R.layout.grupa_item_layout;
		this.groupLayoutId = R.layout.grupa_group_layout;
		this.grupaLista = catList;
		this.ctx = ctx;
		this.db = db; 
	}
	

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.grupa_item_layout, parent, false);
		}
		
		final TextView itemName = (TextView) v.findViewById(R.id.itemGrupaIme);
		final TextView itemDescr = (TextView) v.findViewById(R.id.itemGrupaOpis);
				
		final Grupa gru = grupaLista.get(groupPosition);
		final Integer IdGrupaOdabrana = gru.get_id();
		
		final Button uredi=(Button)v.findViewById(R.id.btnGrupaUredi);
		final Button brisi =(Button)v.findViewById(R.id.btnGrupaBrisi);
		
		 uredi.setOnClickListener(new OnClickListener() {

		        @Override
		        public void onClick(View v) {

		        	EditGrupaFragment(IdGrupaOdabrana);

		        }
		    });
		 
		 
		 brisi.setOnClickListener(new OnClickListener() {

		        @Override
		        public void onClick(View v) {

		        	BrisiGrupaSve(IdGrupaOdabrana) ;

		        }
		    });

		itemName.setText(R.string.Opis);
		itemDescr.setText(gru.get_opis());
		return v;
		
	}

	public void EditGrupaFragment(Integer grupaId)
	{
	   	
		GrupeFormFragment clandetaljEdit = new GrupeFormFragment();
         Bundle args2 = new Bundle();
         args2.putInt(GrupeFormFragment.GRUPA_ID, grupaId);
         clandetaljEdit.setArguments(args2);
         FragmentTransaction transaction = ((Activity) ctx).getFragmentManager().beginTransaction();
         transaction.replace(R.id.content_frame, clandetaljEdit);
         transaction.addToBackStack(null);
         transaction.commit();
	}
	
    public void BrisiGrupaSve(Integer grupaId)
	{
		final Integer GrupaId = grupaId;
		AlertDialog.Builder brisanjegrupe = new AlertDialog.Builder(ctx);
		brisanjegrupe.setIconAttribute(android.R.attr.alertDialogIcon);
		brisanjegrupe.setMessage(R.string.brisiGrupa);
		brisanjegrupe.setPositiveButton(R.string.Da, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				Boolean obrisanaGrupa = db.brisiGrupaSve(GrupaId);
				if(obrisanaGrupa)
				{
					FragmentManager fragmentManager = ((Activity) ctx).getFragmentManager();
	    			Fragment fragment = new GrupeListFragment();
	        	 	fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "GrupeListFragment").addToBackStack(null).commit();
				}
			}
		});
		
		brisanjegrupe.setNegativeButton(R.string.Odustani, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog dijalog2 = brisanjegrupe.create();
		dijalog2.show();
	}


	@Override
	public Object getGroup(int groupPosition) {
		return grupaLista.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
	  return grupaLista.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return grupaLista.get(groupPosition).hashCode();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.grupa_group_layout, parent, false);
		}
		
		TextView grupaIme = (TextView) v.findViewById(R.id.grupaIme);

		final Grupa gru = grupaLista.get(groupPosition);
		
		grupaIme.setText(gru.get_ime().substring(0, 1).toUpperCase() + gru.get_ime().substring(1, gru.get_ime().length()));
		//groupDescr.setText(cat.get_opis());
		
		return v;

	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

}
