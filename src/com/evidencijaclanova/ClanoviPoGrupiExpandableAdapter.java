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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ClanoviPoGrupiExpandableAdapter extends BaseExpandableListAdapter {
	
	private List<Grupa> grupaLista;
	private int itemLayoutId;
	private int groupLayoutId;
	Context ctx;
	DataBaseHelper db;
	Integer grupaId = -1;
	
	
public ClanoviPoGrupiExpandableAdapter(List<Grupa> catList, Context ctx, DataBaseHelper db) {
		
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
		Bitmap slikaZaPrikaz = null;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.clanovi_po_grupama_rowlayout, parent, false);
		}
		
		final TextView itemIme = (TextView) v.findViewById(R.id.itemClanIme);
		final TextView itemPrezime = (TextView) v.findViewById(R.id.itemClanPrezime);
		final CheckBox cbOtvoriPauziran = (CheckBox) v.findViewById(R.id.cbOtvoriPauziran);
		final ImageView itemImage = (ImageView) v.findViewById(R.id.itemClanImage);
				
		final Grupa gru = grupaLista.get(groupPosition);
		grupaId = gru.get_id();
		
		final Clan det = grupaLista.get(groupPosition).getItemList().get(childPosition);
		
	    slikaZaPrikaz = itemImage.getDrawingCache();
	      if (slikaZaPrikaz != null)
	      {
	 		 slikaZaPrikaz.recycle();
	 	  }
		
		if(det.get_slika() != null)
    	{
    		slikaZaPrikaz = getImage(det.get_slika());
    		itemImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(slikaZaPrikaz,10));
    	}
    	
    	else
    	{
    		itemImage.setImageResource(R.drawable.friendcaster);
    	
    	}
		

		itemIme.setText(det.get_prezime() + " " + det.get_ime() + "  >>>");
		
		if(det.get_puza() == 1)
		  {
			cbOtvoriPauziran.setChecked(true);
		  }
		
		else
		{
			cbOtvoriPauziran.setChecked(false);
		}
		
		
		itemIme.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
	     		 Integer ClanIdInteger = det.get_id();
	     		 ClanoviDetaljFragment clandetaljFragment = new ClanoviDetaljFragment();
	             Bundle args2 = new Bundle();
	             args2.putInt(ClanoviDetaljFragment.ARG_POSITION, ClanIdInteger);
	             clandetaljFragment.setArguments(args2);
	             FragmentTransaction transaction = ((Activity) ctx).getFragmentManager().beginTransaction();
	             transaction.replace(R.id.content_frame, clandetaljFragment);
	             transaction.addToBackStack(null);
	             transaction.commit();
			}
		});
		
		cbOtvoriPauziran.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {

		        if (buttonView.isPressed())
		        {
	            	final Integer grupa = gru.get_id();
	        		final Integer bbb = det.get_id();
	        		

		        	if (buttonView.isChecked())
		        	{
		          		AlertDialog.Builder priprema = new AlertDialog.Builder(ctx);
		    			priprema.setIconAttribute(android.R.attr.alertDialogIcon);
		    			priprema.setMessage(R.string.PostaviPauziraj);
		    	
		    			priprema.setPositiveButton(R.string.Da, new DialogInterface.OnClickListener()
		    			{
							public void onClick(DialogInterface dialog, int which)
							{
								long UpdateRow  = db.PauzirajClana(gru.get_id(), det.get_id());
				        		if(UpdateRow > 0)
				        		{
				        			det.set_puza(1);
				        			cbOtvoriPauziran.setChecked(true);
				        		}
								
							}
						});
		    			
		    			priprema.setNegativeButton(R.string.Odustani, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								
								cbOtvoriPauziran.setChecked(false);
								dialog.cancel();
							}
						});
		    			
		    			AlertDialog dijalog = priprema.create();
		    			dijalog.show();
		        	        
		        	}
		        	
		        	else
		        	{
		        		
		        		AlertDialog.Builder priprema = new AlertDialog.Builder(ctx);
		    			priprema.setIconAttribute(android.R.attr.alertDialogIcon);
		    			priprema.setMessage(R.string.MakniPauziraj);
		    			priprema.setPositiveButton(R.string.Da, new DialogInterface.OnClickListener()
		    			{
							public void onClick(DialogInterface dialog, int which)
							{
								long UpdateRow  = db.OdPauzirajClana(gru.get_id(), det.get_id());
				        		if(UpdateRow > 0)
				        		{
				        			det.set_puza(0);
				        			cbOtvoriPauziran.setChecked(false);
				        		}
								
							}
						});
		    			
		    			priprema.setNegativeButton(R.string.Odustani, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								
								cbOtvoriPauziran.setChecked(true);
								dialog.cancel();
							}
						});
		    			
		    			AlertDialog dijalog = priprema.create();
		    			dijalog.show();
		        		
		        	}
		       }
		       
		    }
		    
		    
		});

		return v;
		
	}
	
	  public static Bitmap getImage(byte[] image) {
	        //return BitmapFactory.decodeByteArray(image, 0, image.length);
		  
	 	  Bitmap bitmap = null;
		    
  	    try {
  	    	
  	    	BitmapFactory.Options options=new BitmapFactory.Options();
  	        options.inPurgeable = true; 
  	        Bitmap songImage1 = BitmapFactory.decodeByteArray(image,0, image.length,options);
  	        bitmap = Bitmap.createScaledBitmap(songImage1, 50 , 50 , false);
  	           
  	    }
  	    catch (OutOfMemoryError oom)
  	    {
  	        Log.w(" XXX OutOfMemoryError getImage ", oom.getMessage());
  	        System.gc();
  	    }

  	    return bitmap;
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

		Grupa gru = grupaLista.get(groupPosition);
		final int brojClanova = gru.getItemList().size();
		
		grupaIme.setText(gru.get_ime().substring(0, 1).toUpperCase() + gru.get_ime().substring(1, gru.get_ime().length()) + " (" + Integer.toString(brojClanova) + ") ");
		
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
	public Object getChild(int groupPosition, int childPosition) {
		return grupaLista.get(groupPosition).getItemList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return grupaLista.get(groupPosition).getItemList().get(childPosition).hashCode();
	}


	@Override
	public int getChildrenCount(int groupPosition) {
		int size = grupaLista.get(groupPosition).getItemList().size();
		System.out.println("Child for group ["+groupPosition+"] is ["+size+"]");
		return size;
	}


}
