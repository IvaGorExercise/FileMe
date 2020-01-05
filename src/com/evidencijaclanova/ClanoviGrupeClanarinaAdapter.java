package com.evidencijaclanova;

import java.lang.reflect.Field;
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
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClanoviGrupeClanarinaAdapter extends BaseExpandableListAdapter  {
	
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
    static List<HashMap<String,String>> privremeniIznos = new ArrayList<HashMap<String,String>>();

	
	
public ClanoviGrupeClanarinaAdapter(List<Grupa> catList, Context ctx, DataBaseHelper db, String VrstaEvidencije, Integer Dan, Integer Godina, Integer Mjesec) {
		
		this.itemLayoutId = R.layout.grupa_item_layout;
		this.groupLayoutId = R.layout.grupa_group_layout;
		this.grupaLista = catList;
		this.ctx = ctx;
		this.db = db;
		//this.VrstaEvidencije = VrstaEvidencije;
		this.VrstaEvidencije = MainActivity.VrstaEvidencije;
		this.Godina = Godina;
		this.Mjesec = Mjesec;
		this.Dan = Dan;
	}


static class ViewHolder {
    protected TextView itemImeIPrezime;
    protected CheckBox cbOtvoriDatum2;
    protected TextView txtDatumUplata;
    protected TextView txtDatumVrijediDo;
    protected TextView txtIznos;
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
	    
	    Bitmap slikaZaPrikaz = null;
	
	  if(view == null)
	  {
	  
		  LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.clanovi_grupe_clanarina_rowlayout, parent, false);
			
			holder = new ViewHolder();
			holder.itemImeIPrezime = (TextView) view.findViewById(R.id.itemClanPrezime);
			holder.txtDatumUplata= (TextView) view.findViewById(R.id.txtDatumUplata);
			holder.txtDatumVrijediDo= (TextView) view.findViewById(R.id.txtDatumVrijediDo);
			holder.itemCGCImage = (ImageView) view.findViewById(R.id.itemCGCImage);
			
			holder.txtIznos = (TextView) view.findViewById(R.id.txtIznosClanarina);
			holder.cbOtvoriDatum2 = (CheckBox) view.findViewById(R.id.cbOtvoriDatum2);
			

	        view.setTag(holder);
	  }
	  
	  else
	  {
		  holder = (ViewHolder) view.getTag(); 
		  	slikaZaPrikaz = holder.itemCGCImage.getDrawingCache();
		  	
		      if (slikaZaPrikaz != null)
		      {
		 		 slikaZaPrikaz.recycle();
		 		 slikaZaPrikaz = null;
		 	  }
		     
	  }
		
	  final ViewHolder finalHolder = holder;

	  
		
		final Grupa grupaItem = grupaLista.get(groupPosition);
		final Integer IdGrupaOdabrana =  grupaItem.get_id();
		grupaId = grupaItem.get_id();
		
		final Clan clanItem = grupaLista.get(groupPosition).getItemList().get(childPosition);
		
		
		//itemName.setText(clanItem.get_ime());
		holder.itemImeIPrezime.setText(clanItem.get_prezime() + " " + clanItem.get_ime());
		holder.txtIznos.setText("  " + Float.toString(clanItem.get_iznos()));
		
		
		final Integer clanId = clanItem.get_id();
		
		if(clanItem.get_slika() != null)
    	{
    		slikaZaPrikaz = getImage(clanItem.get_slika());
    		holder.itemCGCImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(slikaZaPrikaz,10));
    	}
    	
    	else
    	{
    		holder.itemCGCImage.setImageResource(R.drawable.friendcaster);
    	
    	}
		
		
		if(Float.toString(clanItem.get_iznos()).equals("0.0"))
		{
			holder.txtIznos.setText("");
		}
		//else
		//{
			for(HashMap<String,String> item : privremeniIznos)
			{
				if(item.containsKey(GrupaChild))
				{
					item.get(GrupaChild);
					if(item.get(GrupaChild).toString().equals("0.0"))
					{
						holder.txtIznos.setText("");
					}
					else
					{
						holder.txtIznos.setText("  " + item.get(GrupaChild));
					}
					
				}
			}
		//}
		
	
		if(clanItem.isSelectedPlacen() == true)
		  {
			holder.cbOtvoriDatum2.setChecked(true);
			
			if(clanItem.get_datumUplata() != null)
			{
					String regex1 = "[0-9]+"; 
					String data1 = clanItem.get_datumUplata();
					String Privremena = "";
					
					for(HashMap<String,String> item : privremeniDatumiUplate)
					{
						if(item.containsKey(GrupaChild))
						{
							item.get(GrupaChild);
							holder.txtDatumUplata.setText(item.get(GrupaChild));
							Privremena = item.get(GrupaChild);
						}
					}
					
					if(data1.matches(regex1))
					{
						String DatumFormatiran1 = "";
						if (Privremena != "")
						{
							 DatumFormatiran1 = Privremena;
						}
						else
						{
							 DatumFormatiran1 = FormatirajDatum(clanItem.get_datumUplata());
						}
				
						holder.txtDatumUplata.setText("  " + DatumFormatiran1);
					}

			}
			else
			{
				for(HashMap<String,String> item : privremeniDatumiUplate)
				{
					if(item.containsKey(GrupaChild))
					{
						item.get(GrupaChild);
						holder.txtDatumUplata.setText("  " + item.get(GrupaChild));
					}
				}
				
			
			}
			
			
			if(clanItem.get_datumVrijediDo() != null)
			{
				String regex2 = "[0-9]+"; 
				String data2 = clanItem.get_datumVrijediDo();
				String Privremena = "";
				
				for(HashMap<String,String> item : privremeniDatumiVrijediDo)
				{
					if(item.containsKey(GrupaChild))
					{
						item.get(GrupaChild);
						holder.txtDatumVrijediDo.setText(item.get(GrupaChild));
						Privremena = item.get(GrupaChild);
					}
				}
				
				
				if(data2.matches(regex2))
				{
					
					String DatumFormatiran2 = "";
					
					if (Privremena != "")
					{
						 DatumFormatiran2 = Privremena;
					}
					else
					{
						DatumFormatiran2 = FormatirajDatum(clanItem.get_datumVrijediDo());
					}
					
					holder.txtDatumVrijediDo.setText("  " + DatumFormatiran2);
				}	
			}
			
			else
			{
				for(HashMap<String,String> item2 : privremeniDatumiVrijediDo)
				{
					if(item2.containsKey(GrupaChild))
					{
						item2.get(GrupaChild);
						holder.txtDatumVrijediDo.setText("  " + item2.get(GrupaChild));
					}
				}
				
			
			}
			
		
		  }
		
		else
		{
			holder.txtDatumUplata.setText("");
			holder.txtDatumVrijediDo.setText("");
			holder.txtIznos.setText("");
			holder.cbOtvoriDatum2.setChecked(false);
		}
		
      

		
		
		holder.itemImeIPrezime.setOnClickListener(new View.OnClickListener() {
			
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
	
		
	
		holder.cbOtvoriDatum2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
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
	        	
		        
		        	       final Calendar c = Calendar.getInstance();
		        	        mGodina = c.get(Calendar.YEAR);
		        	        mMjesec = c.get(Calendar.MONTH);
		        	        mDan = c.get(Calendar.DAY_OF_MONTH);
		        	        
		        	        int aa = mDan;
							int bb = mMjesec;
							int cc = mGodina;
							
							int dd = Dan;
							int ee = Mjesec;
							int ff = Godina;
		        	        
		        	        String IzabraniDatumClanarina = FormatirajDatum(DatumHelper.DodajVodeceNule(Godina)  + DatumHelper.DodajVodeceNule(mMjesec + 1) + DatumHelper.DodajVodeceNule(mDan));
		        	        
		        	        if ((Mjesec != (mMjesec + 1)) && (Godina != mGodina))
			        		{
		        	        	
		        	        	mGodina = Godina;
		             	        mMjesec = Mjesec - 1;
		             	        mDan = 1;
		             	        
		             	        int a = mDan;
								int be = mMjesec;
								int ce = mGodina;
		        	        	
				        		IzabraniDatumClanarina = FormatirajDatum(DatumHelper.DodajVodeceNule(Godina)  + DatumHelper.DodajVodeceNule(Mjesec) + DatumHelper.DodajVodeceNule(1));
			        		}
		        	        
		        	        else if((Mjesec == (mMjesec + 1)) && (Godina != mGodina))
		        	        {
		        	        	mGodina = Godina;
		             	        mMjesec = Mjesec - 1;
		             	        mDan = 1;
		             	        
		             	       IzabraniDatumClanarina = FormatirajDatum(DatumHelper.DodajVodeceNule(Godina)  + DatumHelper.DodajVodeceNule(Mjesec) + DatumHelper.DodajVodeceNule(1));
		        	        }
			        		else
			        		{
			        	
			        			mGodina = mGodina;
		             	        mMjesec = mMjesec;
		             	        mDan = mDan;
		             	        
		             	        int a = mDan;
		             	        int be = mMjesec;
		             	        int ce = mGodina;
		             	        
		             	       IzabraniDatumClanarina = FormatirajDatum(DatumHelper.DodajVodeceNule(Godina)  + DatumHelper.DodajVodeceNule(mMjesec + 1) + DatumHelper.DodajVodeceNule(mDan));
			        		}
		        	       
		        	  
		        	        
		        	    	AlertDialog.Builder priprema = new AlertDialog.Builder(ctx);

		        	    	//LayoutInflater inflaterAlert = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        	        LinearLayout layout = new LinearLayout(ctx);
		        	        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		        	        layout.setOrientation(LinearLayout.VERTICAL);
		        	        layout.setLayoutParams(parms);
		        	        layout.setGravity(Gravity.CLIP_VERTICAL);
		        	        layout.setPadding(20, 10, 20, 20);
		        	    	
		        	    	final EditText etIznos = new EditText(ctx);
		        	    	//etIznos.setInputType(InputType.TYPE_CLASS_NUMBER);
		        	    	etIznos.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
		        	    	
		    
		        	    	if(grupaItem.get_naFormi() == 1 && clanItem.isSelectedPlacen()== false)
		        			{
		        		    	etIznos.setText(Float.toString(grupaItem.get_iznos()));
		        			}

		        	    	else
		        	    	{
		        	    		Boolean pozicijaClanaIznos = false;
	        					String iznosPozicijaClana = "";
	        					
	        					if(privremeniDatumiUplate.size() > 0)
		        				{
	        						for(HashMap<String,String> item : privremeniIznos)
				        			{
				        				if(item.containsKey(GrupaChild))
				        				{
				        					pozicijaClanaIznos = true;
				        					iznosPozicijaClana = item.get(GrupaChild);
				        				}
				        			}
			        	    		
			        	    		if (pozicijaClanaIznos == true)
		        					{
			        	    			etIznos.setText(iznosPozicijaClana);
		        					}
			        	    		else
			        	    		{
			        	    			etIznos.setText(Float.toString(clanItem.get_iznos()));
			        	    		}
	        						
		        				}
	        					
	        					else
	        					{
	        						etIznos.setText(Float.toString(clanItem.get_iznos()));
	        					}
		        	    		
		        	    	}
		        	    	
		        	    	
		        	    		
		        	    	
		        	    	
		        	    	
		        	    
		        
		        	    	
		        	    	
		        	    	final TextView txtFormaUnosClnarinaDatumUplate = new TextView(ctx);
		        	    	txtFormaUnosClnarinaDatumUplate.setGravity(Gravity.CENTER);
		        	    	txtFormaUnosClnarinaDatumUplate.setTextSize(16);
		        	    	txtFormaUnosClnarinaDatumUplate.setText(ctx.getResources().getString(R.string.txtFormaUnosClnarinaDatumUplate));
		        	    	layout.addView(txtFormaUnosClnarinaDatumUplate);
		        	    	
		        	   
		        	    	final TextView twIzabraniDatumClanarina = new TextView(ctx);
		        	    	twIzabraniDatumClanarina.setGravity(Gravity.CENTER);
		        	    	twIzabraniDatumClanarina.setTextSize(22);
		        	    	twIzabraniDatumClanarina.setTextColor(ctx.getResources().getColor(R.color.blue01));
		        	    	twIzabraniDatumClanarina.setTypeface(null, Typeface.BOLD);
		        	    	twIzabraniDatumClanarina.setPadding(0, 0, 0, 15);
		        	    	  
		     
		        	    	
		        	    	if(clanItem.get_datumUplata() != null)
		        			{
		        					String regex1 = "[0-9]+"; 
		        					String data1 = clanItem.get_datumUplata();
		        					String Privremena = "";
		        					
		        					for(HashMap<String,String> item : privremeniDatumiUplate)
		        					{
		        						if(item.containsKey(GrupaChild))
		        						{
		        							item.get(GrupaChild);
		        							twIzabraniDatumClanarina.setText(item.get(GrupaChild));
		        							Privremena = item.get(GrupaChild);
		        							
		        							int MjececPrivremeni = DeformatirajVratiDioDatumaInteger(item.get(GrupaChild), 2);
			        						mDan = DeformatirajVratiDioDatumaInteger(item.get(GrupaChild), 1);
			        						mMjesec = MjececPrivremeni-1;
			        						mGodina = DeformatirajVratiDioDatumaInteger(item.get(GrupaChild), 3);
		        						}
		        					}
		        					
		        					if(data1.matches(regex1))
		        					{
		        						String DatumFormatiran1 = "";
		        						if (Privremena != "")
		        						{
		        							 DatumFormatiran1 = Privremena;
		        						}
		        						else
		        						{
		        							 DatumFormatiran1 = FormatirajDatum(clanItem.get_datumUplata());
		        						}
		        				
		        						twIzabraniDatumClanarina.setText("  " + DatumFormatiran1);
		        						int MjececPrivremeni = DeformatirajVratiDioDatumaInteger(DatumFormatiran1, 2);
		        						mDan = DeformatirajVratiDioDatumaInteger(DatumFormatiran1, 1);
		        						mMjesec = MjececPrivremeni-1;
		        						mGodina = DeformatirajVratiDioDatumaInteger(DatumFormatiran1, 3);
		        					}

		        			}
		        			else
		        			{
		        				if(privremeniDatumiUplate.size() > 0)
		        				{
		        					Boolean pozicijaClana = false;
		        					String datumPozicijaClana = "";
		        					
		        					for(HashMap<String,String> item : privremeniDatumiUplate)
			        				{
		        						
			        					if(item.containsKey(GrupaChild))
			        					{
			        						pozicijaClana = true;
			        						item.get(GrupaChild);
			        						datumPozicijaClana = item.get(GrupaChild);

			        					}
			        				}
		        					
		        					if (pozicijaClana == true)
		        					{
		        						twIzabraniDatumClanarina.setText("  " + datumPozicijaClana);

		        						int MjececPrivremeni = DeformatirajVratiDioDatumaInteger(datumPozicijaClana, 2);
		        						mDan = DeformatirajVratiDioDatumaInteger(datumPozicijaClana, 1);
		        						mMjesec = MjececPrivremeni-1;
		        						mGodina = DeformatirajVratiDioDatumaInteger(datumPozicijaClana, 3);
		        					}
		        					
		        					else
		        					{
		        						twIzabraniDatumClanarina.setText(IzabraniDatumClanarina);
		        					}

		        				}
		        				else
		        				{
		        					twIzabraniDatumClanarina.setText(IzabraniDatumClanarina);
		        				}
		        			
		        			}
		        	    	
		        	    	
		        	    	
		        	    	
		        	    	
		        	    	
		        	    	
		        	    	
		        	    	
		        	   
		        		      
		        	    	//priprema.setView(twIzabraniDatumClanarina);
		        	    	layout.addView(twIzabraniDatumClanarina);
		        	    	
		        	       	final Button btnSetDate = new Button(ctx);
		        	    	//btnSetDate.setBackgroundResource(R.drawable.calendar);
		        	    	btnSetDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.calendar);
		        	    	btnSetDate.setTextSize(16);
		        	    	btnSetDate.setText(ctx.getResources().getString(R.string.txtFormaUnosClnarinaDatumUplateIzaberi));
		        	    	btnSetDate.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
		        	    	btnSetDate.setPadding(0, 0, 0, 2);
		        	    	
		        	    	
		        	    	final TextView txtFormaUnosClnarinaiznos = new TextView(ctx);
		        	    	txtFormaUnosClnarinaiznos.setGravity(Gravity.LEFT);
		        	    	txtFormaUnosClnarinaiznos.setTextSize(16);
		        	    	txtFormaUnosClnarinaiznos.setText(ctx.getResources().getString(R.string.txtFormaUnosClnarinaIznos));
		        	    	
		        	    	
		        	    	
		        	    
		        	    	btnSetDate.setOnClickListener(new View.OnClickListener() {
		        				
		        				@Override
		        				public void onClick(View v)
		        				{
		        				
		        					DatePickerDialog dialog = new DatePickerDialog(ctx,
		    		        				
		        				        	 new DatePickerDialog.OnDateSetListener()
		        				        	    {
		        				        			
		        				        	 	   	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
		        				        	 	   	{
		        				        	 	   		mGodina = year;
		        				        	 	   		mMjesec = monthOfYear;
		        				        	 	   		mDan = dayOfMonth;
		        				        	 	   		
		        				        	 	   		twIzabraniDatumClanarina.setText(FormatirajDatum(DatumHelper.DodajVodeceNule(mGodina)  + DatumHelper.DodajVodeceNule(mMjesec + 1) + DatumHelper.DodajVodeceNule(mDan)));
		        				        	 	   		
		        				        	 		}
		        				        	   }
		        				        				
		        				        		, mGodina, mMjesec, mDan);
		        					
		        					Integer M = mMjesec;
		    		        		Integer bbbbb = Mjesec;
		    		        		
		    		        		
		    		        		int m1 = mMjesec;
		    		        		int m2 = Mjesec;
		    		        		
		    		        		int g1 = mGodina;
		    		        		int g2 = Godina;
		    		        		
		    		        		int d1 = mDan;
		    		        		int d2 = mDan;
		    		        		
		    		        		
		    		        		
		    		        		
		    		        		if (Mjesec-1 != (mMjesec))
		    		        		{
		    		        			c.set(c.DAY_OF_MONTH, 1);
		    			        		c.set(c.MONTH, Mjesec-1 );
		    			        		c.set(c.YEAR, Godina );
		    			        		dialog.getDatePicker().init(Godina, Mjesec-1, 1, null);
		    		        			
		    		        		}
		    		        		else
		    		        		{
		    		        			//c.set(c.DAY_OF_MONTH, 1);
		    			        		c.set(c.MONTH, mMjesec );
		    			        		//c.set(c.YEAR, mGodina );
		    			        		c.set(c.YEAR, Godina );
		    			        		dialog.getDatePicker().init(Godina, mMjesec, mDan, null);
		    		        		}
		    		        	
		    		        		c.set(c.DAY_OF_MONTH, 1);
		    		        		dialog.getDatePicker().setMinDate(c.getTimeInMillis());
		    		        		
		    		        		Integer ZadnjiDan = c.getActualMaximum(c.DAY_OF_MONTH);
		    		        		c.set(c.DAY_OF_MONTH, ZadnjiDan);
		    		        		
		    		        		
		    		        		dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
		    		        		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, ctx.getResources().getString(R.string.Odustani),
		    	        	                new DialogInterface.OnClickListener()
		    	        				{
		    	        	                    public void onClick(DialogInterface dialog, int which)
		    	        	                    {
		    	        	                        if (which == DialogInterface.BUTTON_NEGATIVE)
		    	        	                        {
		    	        	                            dialog.cancel();
		    	        	                        }
		    	        	                    }
		    	        	                });
		    		        		
		    		    			dialog.show();
		        					
		        				
		        				}
		        				
		        			});
		        	    	
		        	    	
		        	    	layout.addView(btnSetDate);
		        	    	
		        	    	
		        	    	layout.addView(txtFormaUnosClnarinaiznos);
		        	    	
		        	    	layout.addView(etIznos);
		        	    	
		        	      
			    			
			    			//priprema.setView(etNadimak);
			    			
			    			priprema.setView(layout);
			    			
			    			
			    			priprema.setPositiveButton(R.string.Spremi, new DialogInterface.OnClickListener()
			    			{
								public void onClick(DialogInterface dialog, int which)
								{
									int a = mDan;
									int be = mMjesec;
									int c = mGodina;

			        	 	   		//final String datumaUplata = SelektiraniDatum();
									final String datumaUplata = SelektiraniDatumVrijednosti(mDan, mMjesec, mGodina);
			        	 	   		final String datumFormatirani = FormatirajDatum(datumaUplata);
			        	 	   		finalHolder.txtDatumUplata.setText(datumFormatirani);
			        	 	   		Clanarina unosClanarina = new Clanarina();
			        	 	   		unosClanarina.set_idClan(clanItem.get_id());
			        	 	   		unosClanarina.set_idGrupa(grupaItem.get_id());
			        	 	   		unosClanarina.set_godina(VratiDioDatumaInteger(datumaUplata, 3));
			        	 	   		unosClanarina.set_mjesec(VratiDioDatumaInteger(datumaUplata, 2));
			        	 	   		unosClanarina.set_dan(VratiDioDatumaInteger(datumaUplata, 1));
			        	 	   		unosClanarina.set_datumUplata(datumaUplata);
			        	 	   		
			        	 	   		String textIznos = etIznos.getText().toString();
			        	 	   		if(!textIznos.equalsIgnoreCase(""))
			        	 	   		{
			        	 	    		unosClanarina.set_iznos(Float.parseFloat(textIznos));
			        	 	   		}
			        	 	   		
			        	 	   		twIzabraniDatumClanarina.setText(datumFormatirani);
			        	 	   		

			        	 	   		
			        	 			switch(VrstaEvidencijeInteger)
			        				{
			        					case 1:
			        						Integer ZadnjiDan = ZadnjiDanMjeseca(datumaUplata);
			        						Integer DanDo = ZadnjiDan;
			        						Integer MjesecDo = VratiDioDatumaInteger(datumaUplata, 2);
			        						Integer GodinaDo = VratiDioDatumaInteger(datumaUplata, 3);
			        						String DatumVrijediDo = Integer.toString(GodinaDo) + DodajVodeceNule(MjesecDo)+ DodajVodeceNule(DanDo);
			        				   		unosClanarina.set_datumVrijediDo(DatumVrijediDo);
			        				   		unosClanarina.set_danDo(DanDo);
			        				   		unosClanarina.set_mjesecDo(MjesecDo);
			        				   		unosClanarina.set_godinaDo(GodinaDo);
			        				   		unosClanarina.set_vrsta(VrstaEvidencijeInteger);
			        						
			        	       	 			break;
			        	       	 			
			        					case 2:
			        						Integer DanUplata = VratiDioDatumaInteger(datumaUplata, 1);
			        						Integer MjesecUplata = VratiDioDatumaInteger(datumaUplata, 2);
			        						Integer GodinaUplata = VratiDioDatumaInteger(datumaUplata, 3);
			        						Integer DanDo2 = 0;
			        						Integer MjesecDo2 = 0;
			        						Integer GodinaDo2 = 0;
			        						
			        						if(MjesecUplata == 12)
			        						{
			        							MjesecDo2 = 1;
			        							GodinaDo2 = GodinaUplata + 1;
			        						}
			        						
			        						else
			        						{
			        							MjesecDo2 = MjesecUplata + 1;
			        							GodinaDo2 = GodinaUplata;
			        						}
			        						
			        						
			        						if(MjesecDo2 == 2)
			        						{
			        							if(DanUplata > 28)
			        							{
			        								DanDo2 = 28;
			        							}
			        							else
			        							{
			        								DanDo2 = DanUplata;
			        							}
			        							
			        				
			        						}
			        						
			        						else if(MjesecDo2 == 4 || MjesecDo2 == 6  || MjesecDo2 == 9 || MjesecDo2 == 11 )
			        						{
			        							if(DanUplata > 30)
			        							{
			        								DanDo2 = 30;
			        							}
			        							else
			        							{
			        								DanDo2 = DanUplata;
			        							}
			        							
			        						}
			        						
			        						else if(MjesecDo2 == 1 || MjesecDo2 == 3 || MjesecDo2 == 5  || MjesecDo2 == 7 || MjesecDo2 == 8 || MjesecDo2 == 10 || MjesecDo2 == 12 )
			        						{
			        							DanDo2 = DanUplata;
			        						}
			        						
			        						DatumVrijediDo = Integer.toString(GodinaDo2) + DodajVodeceNule(MjesecDo2)+ DodajVodeceNule(DanDo2);
			        				   		unosClanarina.set_datumVrijediDo(DatumVrijediDo);
			        				   		unosClanarina.set_danDo(DanDo2);
			        				   		unosClanarina.set_mjesecDo(MjesecDo2);
			        				   		unosClanarina.set_godinaDo(GodinaDo2);
			        				   		unosClanarina.set_vrsta(VrstaEvidencijeInteger);

			        	       	 			break;

			        				}
			        	 			
			        	 			if(clanItem.isSelectedPlacen() == true)
			        	 			{
			        	 				long obrisni  = db.BrisiClanarina(grupaItem.get_id(), clanItem.get_id(), Godina, Mjesec);
						        		if(obrisni > 0)
						        		{
						        			long dodanaClanarina = db.DodajClanarina2(unosClanarina);
					        	 			
					        	 			if(dodanaClanarina > 0)
							        		{
							        			 Clanarina upisana = db.DohvatiClanarina2(dodanaClanarina);
							        			if(upisana != null)
							        			{
							        				if(upisana.get_id() > 0)
								        			{
							        					notifyDataSetChanged();
							        					clanItem.setSelectedPlacen(true);
							        					
							        					 HashMap<String, String> datumU = new HashMap<String,String>();
							        					 datumU.put(GrupaPozicijaChildPozicija, FormatirajDatum(upisana.get_datumUplata()));
							        					 privremeniDatumiUplate.add(datumU);
							        					 
							        					 
							        					 HashMap<String, String> datumVD = new HashMap<String,String>();
							        					 datumVD.put(GrupaPozicijaChildPozicija, FormatirajDatum(unosClanarina.get_datumVrijediDo()));
							        					 privremeniDatumiVrijediDo.add(datumVD);
							        					 
							        					 HashMap<String, String> iznosVD = new HashMap<String,String>();
							        					 iznosVD.put(GrupaPozicijaChildPozicija, Float.toString(unosClanarina.get_iznos()));
							        					 privremeniIznos.add(iznosVD);
							        					    
								        			}
							        			}
							        		
							        		}
						        		}
			        	 			}
			        	 			else
			        	 			{
			        	 				long dodanaClanarina = db.DodajClanarina2(unosClanarina);
				        	 			
				        	 			if(dodanaClanarina > 0)
						        		{
						        			 Clanarina upisana = db.DohvatiClanarina2(dodanaClanarina);
						        			if(upisana != null)
						        			{
						        				if(upisana.get_id() > 0)
							        			{
						        					notifyDataSetChanged();
						        					clanItem.setSelectedPlacen(true);
						        					
						        					 HashMap<String, String> datumU = new HashMap<String,String>();
						        					 datumU.put(GrupaPozicijaChildPozicija, FormatirajDatum(upisana.get_datumUplata()));
						        					 privremeniDatumiUplate.add(datumU);
						        					 
						        					 
						        					 HashMap<String, String> datumVD = new HashMap<String,String>();
						        					 datumVD.put(GrupaPozicijaChildPozicija, FormatirajDatum(unosClanarina.get_datumVrijediDo()));
						        					 privremeniDatumiVrijediDo.add(datumVD);
						        					 
						        					 HashMap<String, String> iznosVD = new HashMap<String,String>();
						        					 iznosVD.put(GrupaPozicijaChildPozicija, Float.toString(unosClanarina.get_iznos()));
						        					 privremeniIznos.add(iznosVD);
						        					    
							        			}
						        			}
						        		
						        		}
			        	 			}
			        	 			
								}
							});
			    			
			    			priprema.setNegativeButton(R.string.Odustani, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									
									if(clanItem.isSelectedPlacen() == true)
									{
										finalHolder.cbOtvoriDatum2.setChecked(true);
									}
									else
									{
										finalHolder.cbOtvoriDatum2.setChecked(false);
									}

									dialog.cancel();
								}
							});
			    			
			    			
			    			if (!buttonView.isChecked())
			  		        {
			    				priprema.setNeutralButton(ctx.getResources().getString(R.string.Obrisi), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										
										AlertDialog.Builder pripremaBrisi = new AlertDialog.Builder(ctx);
										pripremaBrisi.setIconAttribute(android.R.attr.alertDialogIcon);
										pripremaBrisi.setMessage(R.string.BrisiClanarina);
										pripremaBrisi.setPositiveButton(R.string.Da, new DialogInterface.OnClickListener()
						    			{
											public void onClick(DialogInterface dialog, int which)
											{
												long obrisni  = db.BrisiClanarina(grupaItem.get_id(), clanItem.get_id(), Godina, Mjesec);
								        		if(obrisni > 0)
								        		{
								        			notifyDataSetChanged();
								        			clanItem.setSelectedPlacen(false);
								        			finalHolder.txtDatumUplata.setText("");
								        			finalHolder.txtDatumVrijediDo.setText("");
								        			finalHolder.txtIznos.setText("");
								        		}
												
											}
										});
						    			
										pripremaBrisi.setNegativeButton(R.string.Odustani, new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												
												dialog.cancel();
											}
										});
						    			
						    			AlertDialog dijalogBrisi = pripremaBrisi.create();
						    			dijalogBrisi.show();
						    			
										//dialog.cancel();
									}
								});
			    				  
			  		        }
			    			
			    			AlertDialog dijalog = priprema.create();
			    			dijalog.show();   
		        	        
		       }
		       
		    }
		    
		    
		});
		
	
		return view;
	}
	
	public static Bitmap getImage(byte[] image)
	{
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

	private Integer ZadnjiDanMjeseca(String Datum)
	{
		Integer rezultat = 28;
		
		try
		{
	 		String sDan = Datum.substring(6, 8);
	 		String sMjesec = Datum.substring(4, 6);
	 		String sGodina = Datum.substring(0, 4);
	 		
	 		Integer Dan = Integer.parseInt(sDan);
	 		Integer Mjesec  = Integer.parseInt(sMjesec);
	 		Integer Godina = Integer.parseInt(sGodina);
	 		
			Calendar noviKalendar = Calendar.getInstance();
			noviKalendar.set(Calendar.MONTH, Mjesec - 1);
			noviKalendar.set(Calendar.YEAR, Godina);
			noviKalendar.set(Calendar.DAY_OF_MONTH, 1);
			Integer ZadnjiDan = noviKalendar.getActualMaximum(noviKalendar.DAY_OF_MONTH);
			rezultat = ZadnjiDan;
			
		}
		catch(Exception e)
		{
			
		}


		return rezultat;
	}
	
	
	
	private Integer VratiDioDatumaInteger(String Datum, Integer dio)
	{
		Integer rezultat = 1;
		
		try
		{
			String sDan = Datum.substring(6, 8);
	 		String sMjesec = Datum.substring(4, 6);
	 		String sGodina = Datum.substring(0, 4);
	 		
	 		Integer Dan = Integer.parseInt(sDan);
	 		Integer Mjesec  = Integer.parseInt(sMjesec);
	 		Integer Godina = Integer.parseInt(sGodina);
	 		
			switch (dio)
			{
				case 1:
					rezultat = Dan;
       	 			break;
       	 			
				case 2:
					rezultat = Mjesec;
       	 			break;
       	 			
				case 3:
					rezultat = Godina;
       	 			break;
			}
			
		}
		catch(Exception e)
		{
			
		}


		return rezultat;
	}
	
	
	
	private String SelektiraniDatum()
   	{
 		String sDan;
 		String sMjesec;
 		String sGodin = Integer.toString(mGodina);
 		String rezult = "";
 		
		if (mDan < 10)
		{
			sDan = "0" + Integer.toString(mDan);
		}
		
		else
		{
			sDan = Integer.toString(mDan);
		}
		
		if ((mMjesec+ 1) < 10)
		{
			sMjesec = "0" + Integer.toString(mMjesec+ 1);
		}
		
		else
		{
			sMjesec = Integer.toString(mMjesec + 1);
		}
		
		rezult = sGodin  + sMjesec +  sDan;
 		
 		return rezult;
   	}
	
	
	private String SelektiraniDatumVrijednosti(int mDan, int mMjesec, int mGodina)
   	{
 		String sDan;
 		String sMjesec;
 		String sGodin = Integer.toString(mGodina);
 		String rezult = "";
 		
		if (mDan < 10)
		{
			sDan = "0" + Integer.toString(mDan);
		}
		
		else
		{
			sDan = Integer.toString(mDan);
		}
		
		if ((mMjesec+ 1) < 10)
		{
			sMjesec = "0" + Integer.toString(mMjesec+ 1);
		}
		
		else
		{
			sMjesec = Integer.toString(mMjesec + 1);
		}
		
		rezult = sGodin  + sMjesec +  sDan;
 		
 		return rezult;
   	}
	
	private String FormatirajDatum(String Datum)
   	{ 
		String Rezult = "";
 		String sDan = Datum.substring(6, 8);
 		String sMjesec = Datum.substring(4, 6);
 		String sGodina = Datum.substring(0, 4);
 		
 		
 		Integer Dan = Integer.parseInt(sDan);
 		Integer Mjesec  = Integer.parseInt(sMjesec);
 		Integer Godina = Integer.parseInt(sGodina);
 		
 	
		if (Dan < 10)
		{
			sDan = "0" + Integer.toString(Dan);
		}
		
		else
		{
			sDan = Integer.toString(Dan);
		}
		
		if (Mjesec < 10)
		{
			sMjesec = "0" + Integer.toString(Mjesec);
		}
		
		else
		{
			sMjesec = Integer.toString(Mjesec);
		}
		
		
		Integer Jezik = MainActivity.Engleski(ctx);
		if(Jezik == 1)
		{
			Rezult = (new StringBuilder().append(sDan).append("/").append(sMjesec).append("/").append(sGodina)).toString();
		}
		else
		{
			Rezult = (new StringBuilder().append(sDan).append(".").append(sMjesec).append(".").append(sGodina).append(".")).toString();
		}
		
 		
 		return Rezult;
   	}
	
	
	private Integer DeformatirajVratiDioDatumaInteger(String Datum, Integer dio)
	{
		Integer rezultat = 1;
		String pDan = Integer.toString(mDan);
 		String pMjesec = Integer.toString(mMjesec);
 		String pGodina = Integer.toString(mGodina);
		
		try
		{
			String regex = "[/.]";
			
			String[] parts = Datum.split(regex);
			
			if (parts.length > 0)
			{
				pDan = parts[0];
				pMjesec = parts[1];
				pGodina = parts[2];
				
				if (pDan.substring(0, 1).equals("0"))
				{
					pDan = pDan.substring(1, pDan.length());
				}
				
				if (pMjesec.substring(0, 1).equals("0"))
				{
					pMjesec = pMjesec.substring(1, pMjesec.length());
				}
			}
					
	 		
	 		Integer Dan = Integer.parseInt(pDan);
	 		Integer Mjesec  = Integer.parseInt(pMjesec);
	 		Integer Godina = Integer.parseInt(pGodina);
	 		
			switch (dio)
			{
				case 1:
					rezultat = Dan;
       	 			break;
       	 			
				case 2:
					rezultat = Mjesec;
       	 			break;
       	 			
				case 3:
					rezultat = Godina;
       	 			break;
			}
			
		}
		catch(Exception e)
		{
			
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
		System.out.println("Child for group ["+groupPosition+"] is ["+size+"]");
		return size;
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	    super.registerDataSetObserver(observer);    
	}
	


}
