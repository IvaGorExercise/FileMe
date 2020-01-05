package com.evidencijaclanova;
import java.util.ArrayList;
import java.util.List;

import com.evidencijaclanova.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;



public class ClanoviListAdapter extends ArrayAdapter<ClanGrupa> implements Filterable   {

	Context context;
	int layoutResourceId;   
	static List<ClanGrupa>objectsClanGrupa;
	private List<ClanGrupa> origPlanetList;
	private android.widget.Filter planetFilter;
	private int layoutId;
	public static String ClanId = "0";
	
	
	public ClanoviListAdapter(Context context, int resource,  List<ClanGrupa> objects) {
		 super(context, resource, objects);
	     this.context = context;
	     this.objectsClanGrupa = objects;
	     this.origPlanetList = objects;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		 View row = convertView;
		 ViewHolder holder = new ViewHolder();
		 Bitmap slikaZaPrikaz = null;

	    if (convertView == null) 
	    {
	    	LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    	row = mInflater.inflate(R.layout.clanovilist_row_item, parent, false);
	        holder = new ViewHolder();
	        holder.imgClan = (ImageView) row.findViewById(R.id.clanImage);
	        holder.txtClanPrezime = (TextView) row.findViewById(R.id.txtClanPrezime);
	        holder.txtClanIme = (TextView) row.findViewById(R.id.txtClanIme);
	        holder.txtGrupaIme = (TextView) row.findViewById(R.id.txtGrupaIme);
	        //holder.txtClanId = (TextView) row.findViewById(R.id.txtClanId);
	        row.setTag(holder);

	    } else 
	    {
	    	
	      holder = (ViewHolder) row.getTag();
	      slikaZaPrikaz = holder.imgClan.getDrawingCache();
	      if (slikaZaPrikaz != null)
	      {
	 		 slikaZaPrikaz.recycle();
	 		 slikaZaPrikaz = null;
	 	  }
	     
	    }
	    

	    final  ClanGrupa model = objectsClanGrupa.get(position);
	    
	    if (model != null) {
	    	
	    	if(model.get_slika() != null)
	    	{
	    		//Bitmap slikaZaPrikaz = getImage(model.get_slika());
	    		
	    		slikaZaPrikaz = getImage(model.get_slika());
		    	holder.imgClan.setImageBitmap(ImageHelper.getRoundedCornerBitmap(slikaZaPrikaz,10));
		    	
	    	}
	    	
	    	else
	    	{
	    		holder.imgClan.setImageResource(R.drawable.friendcaster);
	    	}
	    	
	        holder.txtClanIme.setText(model.get_prezime() + " " + model.get_ime());
	        //holder.txtClanId.setText(Integer.toString(model.get_id()));
	        ClanId = Integer.toString(model.get_id());
	        
	        if (model.get_idGrupaLista() != null && model.get_imeGrupaLista() != null)
	        {
	        	//holder.txtGrupId.setText(model.get_idGrupaLista());
	 	        holder.txtGrupaIme.setText(model.get_imeGrupaLista());
	        }
	        else
	        {
	        	 holder.txtGrupaIme.setText("");
	        }
	       
	    }

	    return row;

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
	  

	public void resetData() {
		objectsClanGrupa = origPlanetList;
		}
		
	@Override
	public int getCount() {
	    return objectsClanGrupa.size();
	}

	@Override
	public ClanGrupa getItem(int position) {
	    // TODO Auto-generated method stub
	    return objectsClanGrupa.get(position);
	}

	private static class ViewHolder {
	    public ImageView imgClan;
	    public TextView txtClanIme;
	    public TextView txtClanPrezime;
	    public TextView txtClanId;
	    //public TextView txtGrupId;
	    public TextView txtGrupaIme;
	  }

	
	@Override
	public android.widget.Filter getFilter() {
		if (planetFilter == null)
			planetFilter = new PlanetFilter();
		
		return planetFilter;
	}



	private class PlanetFilter extends android.widget.Filter 
	{

		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) 
		{
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) 
			{
				// No filter implemented we return all the list
				results.values = origPlanetList;
				results.count = origPlanetList.size();
			}
			else {
				// We perform filtering operation
				List<ClanGrupa> nPlanetList = new ArrayList<ClanGrupa>();
				
				for (ClanGrupa p : objectsClanGrupa) {
					if (p.get_prezime().toUpperCase().startsWith(constraint.toString().toUpperCase()))
						nPlanetList.add(p);
				}
				
				results.values = nPlanetList;
				results.count = nPlanetList.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results)
		{
			
			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				objectsClanGrupa = (List<ClanGrupa>) results.values;
				notifyDataSetChanged();
			}
			
		}
		
	}

	
}
