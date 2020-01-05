package com.evidencijaclanova;

import java.util.ArrayList;
import java.util.List;

import com.evidencijaclanova.R;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class EvidencijaDolazakaAdapter extends ArrayAdapter<Clan> {
	
	Context context;
	int layoutResourceId;   
	static List<Clan>objectsClanGrupa;
	private List<Clan> origPlanetList;
	public List<TerminClan> postojeci;

	private int layoutId;
	public static String ClanId = "0";
	
	
	public EvidencijaDolazakaAdapter(Context context, int resource,  List<Clan> objects, List<TerminClan> objectsBaza) {
		 super(context, resource, objects);
	     this.context = context;
	     this.objectsClanGrupa = objects;
	     this.origPlanetList = objects;
	     this.postojeci = objectsBaza;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
	View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.terminclan_rowlayout, parent, false);
		}
		

			final TextView itemTerminClanPrezime = (TextView) v.findViewById(R.id.itemTerminClanPrezime);
			final CheckBox cbPrisutan = (CheckBox) v.findViewById(R.id.cbPrisutan);
			final ImageView itemCGCterminClan = (ImageView) v.findViewById(R.id.itemCGCTerminClan);
			
		    final  Clan model = objectsClanGrupa.get(position);
		    
		    if (model != null) {
		    	
		    	if(model.get_slika() != null)
		    	{
		    		Bitmap slikaZaPrikaz = getImage(model.get_slika());
			    	itemCGCterminClan.setImageBitmap(ImageHelper.getRoundedCornerBitmap(slikaZaPrikaz,10));
		    	}
		    	
		    	else
		    	{
		    		itemCGCterminClan.setImageResource(R.drawable.friendcaster);
		    	}
		    	
		        ClanId = Integer.toString(model.get_id()); 
		        itemTerminClanPrezime.setText(model.get_prezime() + " " + model.get_ime());
		        
		        
				cbPrisutan.setChecked(false);
				
				if (postojeci.size() > 0)
				{
					
					List<Integer> idPostojeci = new ArrayList<Integer>();
							
						
					for(TerminClan tcp: postojeci)
					{
						idPostojeci.add(tcp.get_idClan());
					
					}
					
					if(idPostojeci.contains(model.get_id()))
					{
						cbPrisutan.setChecked(true);
					}
					
				}
				

		    }
			
		

	    return v;

	}
	
	  public static Bitmap getImage(byte[] image) {
	        return BitmapFactory.decodeByteArray(image, 0, image.length);
	    }

	public void resetData() {
		objectsClanGrupa = origPlanetList;
		}
		
	@Override
	public int getCount() {
	    return objectsClanGrupa.size();
	}

	@Override
	public Clan getItem(int position) {
	    // TODO Auto-generated method stub
	    return objectsClanGrupa.get(position);
	}


}
