package com.evidencijaclanova;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.evidencijaclanova.ClanoviFormAdapter.ViewHolder;
import com.evidencijaclanova.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IstekleClanrineAdapter extends BaseExpandableListAdapter  {
	
	private final static int ID_IZBORNIK_DATUMA = 0;
	private List<Grupa> grupaLista;
	private int itemLayoutId;
	private int groupLayoutId;
	Context ctx;
	DataBaseHelper db;
	Integer grupaId = -1;
 	private int mGodina;
 	private int mMjesec;
 	private int mDan;
 	String DDD = "";
 	String Vrsta =  "";
 	String VrstaEvidencije = "1";
 	Integer Dan = 0;
 	Integer Mjesec = 0;
 	Integer Godina = 0;
    static List<HashMap<String,String>> privremeniDatumiUplate = new ArrayList<HashMap<String,String>>();
    static List<HashMap<String,String>> privremeniDatumiVrijediDo = new ArrayList<HashMap<String,String>>();

	
	
public IstekleClanrineAdapter(List<Grupa> catList, Context ctx, DataBaseHelper db, String VrstaEvidencije, Integer Dan, Integer Godina, Integer Mjesec) {
		
		this.itemLayoutId = R.layout.grupa_item_layout;
		this.groupLayoutId = R.layout.grupa_group_layout;
		this.grupaLista = catList;
		this.ctx = ctx;
		this.db = db;
		this.VrstaEvidencije = MainActivity.VrstaEvidencije;
		this.Godina = Godina;
		this.Mjesec = Mjesec;
		this.Dan = Dan;
	}


static class ViewHolder {
    protected TextView itemImeIPrezime;
    protected CheckBox cbOtvoriDatum;
    protected ImageView itemCGCImage;
  }

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		final Integer groupPositionX = groupPosition;
		final Integer childPositionX = childPosition;
		
		final String GrupaChild = Integer.toString(groupPositionX) + Integer.toString(childPositionX);
		
		String GrupaChildDatumUnos = Integer.toString(groupPositionX) + Integer.toString(childPositionX);
		
		ViewHolder holder;
		  
	    
	    View view = convertView;
	
	  if(view == null)
	  {
	  
		  LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.istekle_clanarine_rowlayout, parent, false);
			
			holder = new ViewHolder();
			
			holder.itemImeIPrezime = (TextView) view.findViewById(R.id.itemClanPrezimeIstekle);
			//int orientation = ctx.getResources().getConfiguration().orientation;
			//if (orientation == Configuration.ORIENTATION_LANDSCAPE)
			//{
			//	holder.itemImeIPrezime.setBackgroundResource(R.drawable.button_orange);
			//	holder.itemImeIPrezime.setText("aaaaa");
			//}
			
			//else
			//{
				//holder.itemImeIPrezime.setLayoutParams(new LayoutParams(100,LayoutParams.WRAP_CONTENT));
			//}
			
			holder.cbOtvoriDatum = (CheckBox) view.findViewById(R.id.cbOtvoriDatumIstekle);
			holder.itemCGCImage = (ImageView) view.findViewById(R.id.itemCGCImageIstekle);
			

	        view.setTag(holder);
	  }
	  
	  else
	  {
		  holder = (ViewHolder) view.getTag(); 
	  }
		
	  final ViewHolder finalHolder = holder;

	 
		final Grupa grupaItem = grupaLista.get(groupPosition);
		final Integer IdGrupaOdabrana =  grupaItem.get_id();
		grupaId = grupaItem.get_id();
		
		final Clan clanItem = grupaLista.get(groupPosition).getItemList().get(childPosition);
		
		holder.itemImeIPrezime.setText(clanItem.get_prezime() + " " + clanItem.get_ime() + "  >>>");
		
		
		final Integer clanId = clanItem.get_id();
		
		if(clanItem.get_slika() != null)
    	{
    		Bitmap slikaZaPrikaz = getImage(clanItem.get_slika());
    		holder.itemCGCImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(slikaZaPrikaz,10));
    	}
    	
    	else
    	{
    		holder.itemCGCImage.setImageResource(R.drawable.friendcaster);
    	
    	}

		if(clanItem.isSelectedPlacen() == true)
		  {
			holder.cbOtvoriDatum.setChecked(true);
			
		  }
		
		else
		{
			holder.cbOtvoriDatum.setChecked(false);
		}
		
      

		
		holder.itemImeIPrezime .setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
	     		Integer ClanIdInteger = clanItem.get_id();
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
	
		
		holder.cbOtvoriDatum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	VrstaEvidencije = MainActivity.VrstaEvidencije;
		    	final Integer VrstaEvidencijeInteger = Integer.parseInt(VrstaEvidencije);
		    	
		        if (buttonView.isPressed())
		        {
	            	final Integer grupa = grupaItem.get_id();
	        		final Integer clan = clanId;
	        		final Integer bbb = clanItem.get_id();
	        		
	        		final String GrupaPozicija = Integer.toString(groupPositionX);
	        		final String ChildPozicija =Integer.toString(childPositionX);
	        		
	        		final String GrupaPozicijaChildPozicija = GrupaPozicija + ChildPozicija;
	        	
		        	if (buttonView.isChecked())
		        	{
		          		AlertDialog.Builder priprema = new AlertDialog.Builder(ctx);
		    			priprema.setIconAttribute(android.R.attr.alertDialogIcon);
		    			priprema.setMessage(R.string.PostaviPauziraj);
		    			priprema.setPositiveButton(R.string.Da, new DialogInterface.OnClickListener()
		    			{
							public void onClick(DialogInterface dialog, int which)
							{
								long UpdateRow  = db.PauzirajClana(grupaItem.get_id(), clanItem.get_id());
				        		if(UpdateRow > 0)
				        		{
				        			clanItem.setSelectedPlacen(true);
				        		}
								
							}
						});
		    			
		    			priprema.setNegativeButton(R.string.Odustani, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								
								finalHolder.cbOtvoriDatum.setChecked(false);
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
								long UpdateRow  = db.OdPauzirajClana(grupaItem.get_id(), clanItem.get_id());
				        		if(UpdateRow > 0)
				        		{
				        			clanItem.setSelectedPlacen(false);
				        		}
								
							}
						});
		    			
		    			priprema.setNegativeButton(R.string.Odustani, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								
								finalHolder.cbOtvoriDatum.setChecked(true);
								dialog.cancel();
							}
						});
		    			
		    			AlertDialog dijalog = priprema.create();
		    			dijalog.show();
		        		
		        	}
		       }
		       
		    }
		    
		    
		});
		
		return view;
	}
	
	public static Bitmap getImage(byte[] image)
	{
	    return BitmapFactory.decodeByteArray(image, 0, image.length);
	}
	
	private String DodajVodeceNule(Integer Broj)
	{
		String rezultat = "";
	
			String BrojStringt = Integer.toString(Broj);
			if (Broj < 10)
			{
				rezultat = "0" + BrojStringt;
			}
			
			else
			{
				rezultat = BrojStringt;
			}
	 		
		return rezultat;
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
		
		final TextView grupaIme = (TextView) v.findViewById(R.id.grupaIme);

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
		return size;
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	    super.registerDataSetObserver(observer);    
	}
	


}
