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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class KategorijeExpandableAdapter  extends BaseExpandableListAdapter  {
	
	
	private List<Kategorija> catList;
	private int itemLayoutId;
	private int groupLayoutId;
	Context ctx;
	DataBaseHelper db;
	//Integer kategorijaId = -1;
		
	public KategorijeExpandableAdapter(List<Kategorija> catList, Context ctx, DataBaseHelper db) {
		
		this.itemLayoutId = R.layout.kategorije_item_layout;
		this.groupLayoutId = R.layout.kategorije_group_layout;
		this.catList = catList;
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
			v = inflater.inflate(R.layout.kategorije_item_layout, parent, false);
		}
		
		final TextView itemNameKategorija = (TextView) v.findViewById(R.id.itemKategorijaName);
		final TextView itemDescrKategorija = (TextView) v.findViewById(R.id.itemKategorijaDescr);
				
		final Kategorija cat = catList.get(groupPosition);
		final Integer kategorijaId = cat.get_id();
		
		final Button uredi=(Button)v.findViewById(R.id.btnKategorijaUredi);
		final Button brisi =(Button)v.findViewById(R.id.btnKategorijaBrisi);
		
		 uredi.setOnClickListener(new OnClickListener() {

		        @Override
		        public void onClick(View v) {

		        	EditKategorijaFragment(kategorijaId);

		        }
		    });
		 
		 
		 brisi.setOnClickListener(new OnClickListener() {

		        @Override
		        public void onClick(View v) {

		        	BrisiKategorijaSve(kategorijaId) ;

		        }
		    });

		itemNameKategorija.setText(R.string.Opis);
		itemDescrKategorija.setText(cat.get_opis());
		
		return v;
		
	}

	public void EditKategorijaFragment(Integer kategorijaId)
	{
	   	
		KategorijaFormFragment clandetaljEdit = new KategorijaFormFragment();
         Bundle args2 = new Bundle();
         args2.putInt(KategorijaFormFragment.KATEGORIJA_ID, kategorijaId);
         clandetaljEdit.setArguments(args2);
         //FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
         FragmentTransaction transaction = ((Activity) ctx).getFragmentManager().beginTransaction();
         transaction.replace(R.id.content_frame, clandetaljEdit);
         transaction.addToBackStack(null);
         transaction.commit();
	}
	
    public void BrisiKategorijaSve(Integer kategorijaId)
	{
		final Integer KategorijaId = kategorijaId;
		AlertDialog.Builder brisanjeKategorije = new AlertDialog.Builder(ctx);
		brisanjeKategorije.setIconAttribute(android.R.attr.alertDialogIcon);
		brisanjeKategorije.setMessage(R.string.brisiKategorija);
		brisanjeKategorije.setPositiveButton(R.string.Da, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				Boolean obrisanaKategorija = db.brisiKategorijaSve(KategorijaId);
				if(obrisanaKategorija)
				{
					FragmentManager fragmentManager = ((Activity) ctx).getFragmentManager();
	    			Fragment fragment = new KategorijeListFragment();
	        	 	fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "KategorijeListFragment").addToBackStack(null).commit();
				}
			}
		});
		
		brisanjeKategorije.setNegativeButton(R.string.Odustani, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog dijalog2 = brisanjeKategorije.create();
		dijalog2.show();
	}


	@Override
	public Object getGroup(int groupPosition) {
		return catList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
	  return catList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return catList.get(groupPosition).hashCode();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.kategorije_group_layout, parent, false);
		}
		
		TextView groupName = (TextView) v.findViewById(R.id.groupName);

		Kategorija cat = catList.get(groupPosition);
		
		groupName.setText(cat.get_ime().substring(0, 1).toUpperCase() + cat.get_ime().substring(1, cat.get_ime().length()));
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
