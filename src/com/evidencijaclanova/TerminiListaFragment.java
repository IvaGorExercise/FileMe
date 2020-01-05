package com.evidencijaclanova;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.evidencijaclanova.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TerminiListaFragment extends Fragment {
	
	ArrayList<Grupa> CustomListViewValuesArr = null;
	private ListView listView;
    private int mNum = 0;
    List<Grupa> items = new ArrayList<Grupa>();
    List<Grupa> itemsGrupa = new ArrayList<Grupa>();
    List<Termin> itemsTermin = new ArrayList<Termin>();
    List<TerminClan> itemsTerminClan = new ArrayList<TerminClan>();
    private ListView mListView;
    DataBaseHelper db;
    
	private LinearLayout mLinearListView;
	boolean isFirstViewClick=false;
	boolean isSecondViewClick=false;
	
	Button MjesecGodina;
	private int gGodina;
	private int gMjesec;
	private int gDan;
	
	View rootView;
	
	private WeakReference<PozadinskiZadatakTerminiLista> asyncTaskWeakRefTerminiLista;
	PozadinskiZadatakTerminiLista asyncTaskTerminiLista = null;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
   	 	
   		db  = new DataBaseHelper(getActivity()); 

        try { 
        	db.createDataBase(); 
	 	} catch (IOException ioe) {	 
	 		throw new Error("Unable to create database");	 
	 	}
        
        
        final Calendar c = Calendar.getInstance();
        gGodina = c.get(Calendar.YEAR);
        gMjesec = c.get(Calendar.MONTH) + 1;
        gDan = c.get(Calendar.DAY_OF_MONTH);
        
        //itemsGrupa = dohvatiGrupe(gMjesec, gGodina);
   	 	
    }
    

    
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
		 
	     getActivity().setTitle(R.string.fragmentClanoviPoGrupm);
         //View rootView = inflater.inflate(R.layout.terminilista_layout, container, false);
         rootView = inflater.inflate(R.layout.terminilista_layout, container, false);
         
         getActivity().setTitle(R.string.fragmentUnosTerminList);
         
         MjesecGodina = (Button)rootView.findViewById(R.id.btnMjesecGodinaTermin);
      
         MjesecGodina.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v)
			{
				ClanoviGrupeClanarinaAdapter.privremeniDatumiUplate.clear();
			    ClanoviGrupeClanarinaAdapter.privremeniDatumiVrijediDo.clear();
			         
				DatePickerDialog dialog = new DatePickerDialog(getActivity(), listenerZaDatuma, gGodina, gMjesec-1, gDan);

				 try{
					    Field[] datePickerDialogFields = dialog.getClass().getDeclaredFields();
					    for (Field datePickerDialogField : datePickerDialogFields) { 
					        if (datePickerDialogField.getName().equals("mDatePicker")) {
					            datePickerDialogField.setAccessible(true);
					            DatePicker datePicker = (DatePicker) datePickerDialogField.get(dialog);
					            Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
					            for (Field datePickerField : datePickerFields) {
					               if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField.getName())) {
					                  datePickerField.setAccessible(true);
					                  Object dayPicker = new Object();
					                  dayPicker = datePickerField.get(datePicker);
					                  ((View) dayPicker).setVisibility(View.GONE);
					               }
					            }
					         }

					      }
					    }catch(Exception ex){
					    }
				
				dialog.show();
			
			}
			
		});
         

        Integer Jezik = MainActivity.Engleski(getActivity());
        
        if(Jezik == 1)
        {
       	   MjesecGodina.setText(getResources().getString(R.string.terminiZa) + " " + DatumHelper.DodajVodeceNule(gMjesec) +  "/" + Integer.toString(gGodina));
        }
        else
        {
       	   MjesecGodina.setText(getResources().getString(R.string.terminiZa) + " " + DatumHelper.DodajVodeceNule(gMjesec) +  ". " + Integer.toString(gGodina) +  ". ");
        }
     	
         
 		mLinearListView = (LinearLayout) rootView.findViewById(R.id.linear_listview);
 		
 		//PostaviView(itemsGrupa);
	
	
         
   
         return rootView;

     }

	 
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.terminlista_izbornik, menu);
	}
		

public boolean onOptionsItemSelected(MenuItem item)
{
	FragmentManager fragmentManager = getFragmentManager();
	
	switch(item.getItemId())
	{
	
		case R.id.izb_novitermin:
			UnosEvidencijeFragment fragment = new UnosEvidencijeFragment();
	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "UnosEvidencijeFragment").addToBackStack(null).commit();
			return true;
			
			
		default:
			return super.onOptionsItemSelected(item);

	}
}
  
 public List<Grupa> dohvatiGrupe(int Mjesec, int Godina)
	{
		ArrayList<Grupa> items = new ArrayList<Grupa>();
		ArrayList<Grupa> grupe = new ArrayList<Grupa>();
		
		try {	 
			db.openDataBase();	 
			grupe = db.dohvatiGrupe();
		}catch(SQLException sqle){	 
 		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(grupe!=null)
		{
			for(Grupa c: grupe)
			{
				c.setItemListTermin(dohvatiTerminePoGrupama(c.get_id(), Mjesec, Godina));
				items.add(c);
			}
			
			db.close();
		}
		
		else
		{
			
		}

	return items;

	}

public List<Termin> dohvatiTerminePoGrupama(Integer idGrupa, Integer Mjesec, Integer Godina)
	{
		ArrayList<Termin> items = new ArrayList<Termin>();
		ArrayList<Termin> ter = new ArrayList<Termin>();
		
		try {	 
			db.openDataBase();	 
			ter = db.dohvatiTerminePoGrupama(idGrupa, Mjesec, Godina);
		}catch(SQLException sqle){	 
		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(ter!=null)
		{
			for(Termin c: ter)
			{
				c.setItemListTerminClan(dohvatiClanovePoTerminu(c.get_id()));
				items.add(c);
			}
		}
		
		else
		{
			
		}

	return items;

	}

public List<TerminClan> dohvatiClanovePoTerminu(Integer idTermin)
{
	ArrayList<TerminClan> items = new ArrayList<TerminClan>();
	ArrayList<TerminClan> ter = new ArrayList<TerminClan>();
	
	try {	 
		db.openDataBase();	 
		ter = db.dohvatiClanPoTerminu(idTermin);
	}catch(SQLException sqle){	 
	throw sqle;	 
	}	
	finally
	{
		db.close();
	}
	if(ter!=null)
	{
		for(TerminClan c: ter)
		{
			items.add(c);
		}
	}
	
	else
	{
		
	}

return items;

}


 
 private List<Clan> createItems(int num) {
 	List<Clan> result = new ArrayList<Clan>();
 	
 	for (int i=0; i < num; i++) {
 		Clan item = new Clan();
 		result.add(item);
 	}
 	
 	return result;
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
	
    public void BrisiTerminSve(int terminId)
	{
		final Integer Termin = terminId;
		AlertDialog.Builder brisanjegrupe = new AlertDialog.Builder(getActivity());
		brisanjegrupe.setIconAttribute(android.R.attr.alertDialogIcon);
		brisanjegrupe.setMessage(R.string.brisiTermin);
		brisanjegrupe.setPositiveButton(R.string.Da, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				Boolean obrisantermin = db.brisiTerminSve(Termin);
				if(obrisantermin)
				{
					FragmentManager fragmentManager = getActivity().getFragmentManager();
	    			Fragment fragment = new TerminiListaFragment();
	        	 	fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "TerminiListaFragment").addToBackStack(null).commit();
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
    
    
    
    public void PostaviView(List<Grupa> itemsGrupGeneral)
    {
    	isFirstViewClick=false;
    	isSecondViewClick=false;
    	
	   mLinearListView = (LinearLayout) rootView.findViewById(R.id.linear_listview);
 		

		for (int i = 0; i < itemsGrupa.size(); i++)
		{
			
			int brojTermina = itemsGrupGeneral.get(i).getItemListTermin().size();
			
			if(brojTermina > 0)
			{
		
			LayoutInflater inflater2 = null;
			inflater2 = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
			View mLinearView = inflater2.inflate(R.layout.row_first, null);
			
			final TextView textTLGrupaIme = (TextView) mLinearView.findViewById(R.id.textTLGrupaIme);
			final RelativeLayout mLinearFirstArrow=(RelativeLayout)mLinearView.findViewById(R.id.linearFirst);
			final ImageView mImageArrowFirst=(ImageView)mLinearView.findViewById(R.id.imageFirstArrow);
			final LinearLayout mLinearScrollSecond=(LinearLayout)mLinearView.findViewById(R.id.linear_scroll);
			
			if(isFirstViewClick==false){
			mLinearScrollSecond.setVisibility(View.GONE);
			mImageArrowFirst.setBackgroundResource(R.drawable.arw_lt);
			}
			else{
				mLinearScrollSecond.setVisibility(View.VISIBLE);
				mImageArrowFirst.setBackgroundResource(R.drawable.arw_down);
			}
			
			mLinearFirstArrow.setOnClickListener(new OnClickListener() {
				
			
				@Override
				public void onClick(View v) {
				
					if(isFirstViewClick==false){
						isFirstViewClick=true;
						mImageArrowFirst.setBackgroundResource(R.drawable.arw_down);
						mLinearScrollSecond.setVisibility(View.VISIBLE);
						
					}else{
						isFirstViewClick=false;
						mImageArrowFirst.setBackgroundResource(R.drawable.arw_lt);
						mLinearScrollSecond.setVisibility(View.GONE);	
					}
					
					
				} 
			});
			
			
			final String name = itemsGrupGeneral.get(i).get_ime().substring(0, 1).toUpperCase() + itemsGrupa.get(i).get_ime().substring(1, itemsGrupa.get(i).get_ime().length());
			
			textTLGrupaIme.setText(name);
			
			
		
				
		    for (int j = 0; j < itemsGrupGeneral.get(i).getItemListTermin().size(); j++)
		    {
				
				LayoutInflater inflater3 = null;
				inflater3 = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
				View mLinearView2 = inflater2.inflate(R.layout.row_second, null);
		    
				TextView textTLTerminIme = (TextView) mLinearView2.findViewById(R.id.textTLTerminIme);
				final RelativeLayout mLinearSecondArrow=(RelativeLayout)mLinearView2.findViewById(R.id.linearSecond);
				final ImageButton btnUrediTermin=(ImageButton)mLinearView2.findViewById(R.id.btnUrediTerminClan);
				final ImageButton btnObrisiTerminClan=(ImageButton)mLinearView2.findViewById(R.id.btnObrisiTerminClan);
				final ImageView mImageArrowSecond=(ImageView)mLinearView2.findViewById(R.id.imageSecondArrow);
				final LinearLayout mLinearScrollThird=(LinearLayout)mLinearView2.findViewById(R.id.linear_scroll_third);
				
				
				if(isSecondViewClick==false){
					mLinearScrollThird.setVisibility(View.GONE);
					mImageArrowSecond.setBackgroundResource(R.drawable.arw_lt);
					}
					else{
						mLinearScrollThird.setVisibility(View.VISIBLE);
						mImageArrowSecond.setBackgroundResource(R.drawable.arw_down);
					}
				

				mLinearSecondArrow.setOnTouchListener(new OnTouchListener() {
						
					
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							
							if(isSecondViewClick==false){
								isSecondViewClick=true;
								mImageArrowSecond.setBackgroundResource(R.drawable.arw_down);
								mLinearScrollThird.setVisibility(View.VISIBLE);
								
							}else{
								isSecondViewClick=false;
								mImageArrowSecond.setBackgroundResource(R.drawable.arw_lt);
								mLinearScrollThird.setVisibility(View.GONE);	
							}
							return false;
						} 
					});
				
	
				final int Grupa = itemsGrupa.get(i).get_id();
				final int Termin = itemsGrupa.get(i).getItemListTermin().get(j).get_id();
				final int mDan  = itemsGrupa.get(i).getItemListTermin().get(j).get_dan();
				final int mMjesec  = itemsGrupa.get(i).getItemListTermin().get(j).get_mjesec();
				final int mGodina  = itemsGrupa.get(i).getItemListTermin().get(j).get_godina();
				final int RB  = itemsGrupa.get(i).getItemListTermin().get(j).get_redniBroj();
				String datum = "";
				
				
				
				btnUrediTermin.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
			     		Integer TerminId = Termin;
			     	
			            Bundle args2 = new Bundle();
			           
			         	FragmentManager fragmentManager = getFragmentManager();
			    		EvidencijaDolazakaFragment fragment = new EvidencijaDolazakaFragment();
	
			    		args2.putLong(EvidencijaDolazakaFragment.ARG_ID_TERMIN, Termin);
			            args2.putInt(EvidencijaDolazakaFragment.ARG_POSITION, Grupa);
			            args2.putInt(EvidencijaDolazakaFragment.ARG_Godina, mGodina);
			            args2.putInt(EvidencijaDolazakaFragment.ARG_Mjesec, mMjesec);
			            args2.putInt(EvidencijaDolazakaFragment.ARG_Dan, mDan);
			            args2.putString(EvidencijaDolazakaFragment.ARG_RB, Integer.toString(RB));
			            fragment.setArguments(args2);
			    		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "EvidencijaDolazakaFragment").addToBackStack(null).commit();
			    	
						
					}
				});
				
				
				btnObrisiTerminClan.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
			     		Integer TerminId = Termin;
			     		BrisiTerminSve(Termin);
			    	
					}
				});
				
				
				Integer Jezik = MainActivity.Engleski(getActivity());
				
				
				if(Jezik == 1)
		     	{
					datum =  DodajVodeceNule(mDan) + "/" + DodajVodeceNule(mMjesec)+ "/" +  DodajVodeceNule(mGodina);
		     		
		     	}
		     	else
		     	{
		     		datum= DodajVodeceNule(mDan) + "." + DodajVodeceNule(mMjesec)+ "." +  DodajVodeceNule(mGodina) + ".";
		     	}
				
				final String catName = datum + " - " +  Integer.toString(RB);
				textTLTerminIme.setText(catName);
				
				
				  for (int k = 0; k < itemsGrupGeneral.get(i).getItemListTermin().get(j).getItemListTerminClan().size(); k++) {
						
						LayoutInflater inflater4 = null;
						inflater4 = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
						View mLinearView3 = inflater3.inflate(R.layout.row_third, null);
				    
						TextView textTLClanIme = (TextView) mLinearView3.findViewById(R.id.textTLClanIme);
						final String itemName = itemsGrupa.get(i).getItemListTermin().get(j).getItemListTerminClan().get(k).get_prezime() + " " + itemsGrupa.get(i).getItemListTermin().get(j).getItemListTerminClan().get(k).get_ime();
						textTLClanIme.setText(itemName);
						
						mLinearScrollThird.addView(mLinearView3);
				  }
				  
				  mLinearScrollSecond.addView(mLinearView2);
		    }
		    
		    
		    mLinearListView.addView(mLinearView);
		    
			}
		}
         
   

    }
    
        
	 private DatePickerDialog.OnDateSetListener listenerZaDatuma = new DatePickerDialog.OnDateSetListener()
	   {
		   	@SuppressLint("NewApi")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
		   	{
		   		gGodina = year;
		   		gMjesec = monthOfYear + 1;
		   		gDan = dayOfMonth;
		   		

		   	   Integer Jezik = MainActivity.Engleski(getActivity());
		         
		         if(Jezik == 1)
		         {
		        	 MjesecGodina.setText(getResources().getString(R.string.terminiZa) + " " + DatumHelper.DodajVodeceNule(monthOfYear + 1) +  "/" + Integer.toString(year));
		         }
		         else
		         {
		        	MjesecGodina.setText(getResources().getString(R.string.terminiZa) + " " + DatumHelper.DodajVodeceNule(monthOfYear + 1) +  ". " + Integer.toString(year) + ". ");
		         }
		         
		         
		         
		        itemsGrupa = dohvatiGrupe(monthOfYear + 1, year);
		     	mLinearListView.removeAllViews();
		     	
		     	//PostaviView(itemsGrupa);
		     	
		        startNewAsyncTaskTerminLista(gGodina, gMjesec);
		     
		    
			}

	   	};

	   
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		     startNewAsyncTaskTerminLista(gGodina, gMjesec);
	}   	
	   	
	   	
	   	
		 
  private void startNewAsyncTaskTerminLista(int gGodina, int gMjesec) {
			      
	  		asyncTaskTerminiLista = new PozadinskiZadatakTerminiLista(this, getActivity(), gGodina,  gMjesec);
			this.asyncTaskWeakRefTerminiLista = new WeakReference<PozadinskiZadatakTerminiLista >(asyncTaskTerminiLista); 
			asyncTaskTerminiLista.execute();
			   	
		}
  
	   	
private class PozadinskiZadatakTerminiLista extends AsyncTask<String, Void, List<Grupa>>
		{

	    	private final int M;
	    	private final int G;
	    	Activity mContex;
	    	
	    	private WeakReference<TerminiListaFragment> fragmentWeakRef;
	    	

	 public PozadinskiZadatakTerminiLista(TerminiListaFragment fragment, Activity contex,  int G, int M)
	    	{

	    		this.M = M;
	    		this.G = G;
	    	    this.mContex=contex;
	    	    this.fragmentWeakRef = new WeakReference<TerminiListaFragment>(fragment);

	    	}
	    	
			private final ProgressDialog dialog = new ProgressDialog(getActivity());
			
			protected void onPreExecute() {
				this.dialog.setMessage(getResources().getString(R.string.Loading));
				this.dialog.show();
			 }
			
			
			
			@Override
			protected List<Grupa> doInBackground(String... params)
			{
				  List<Grupa> result = new  ArrayList<Grupa>();
				  
				  try {
						 //result = dohvatiGrupe();
						 result = dohvatiGrupe(gMjesec, gGodina);
						 int a = result.size();
				  	}
				  
					catch (Exception e)
					{
				    	e.printStackTrace();
				    	
				    	String errorMessage =(e.getMessage()==null)?"Message is empty":e.getMessage();
				    	
				       	Intent intent = new Intent(getActivity(), CrashActivity.class);
			   			intent.putExtra("error", errorMessage);
			   			getActivity().startActivity(intent);

						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(10);
				 	}
				  
				
				   
				   if(result.size() == 0)
				   {
					  dialog.dismiss();
				   }
				   
					 return result;
		
			}
			

				
		   @Override
		   protected void onPostExecute(List<Grupa> result) 
		   {
			   super.onPostExecute(result);
			   
			   dialog.dismiss();
			   
			   	if (this.fragmentWeakRef.get() != null)
			   	{
			   		try 
			   		{
				    	List<Grupa> items = new ArrayList<Grupa>();
				   		items = result;
				   		
				   		if(items.size() > 0)
				   		{
				   			itemsGrupa = items;
				   			PostaviView(itemsGrupa);
				   		}
				   		
			   		}
			  
			   		catch (Exception e)
			   		{
			   			e.printStackTrace();
			    	
			   			String errorMessage =(e.getMessage()==null)?"Message is empty":e.getMessage();
			   			Log.d(" XXX onPostExecute TerminListaClanarinaFragment e ", errorMessage);
			   		 	asyncTaskTerminiLista.cancel(true);
			    	
			   			Intent intent = new Intent(getActivity(), CrashActivity.class);
			   			intent.putExtra("error", e.toString());
			   			getActivity().startActivity(intent);

						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(10);
			    			    
			   		}
				   
			   }
			   	
			   	else
			   	{
			   		asyncTaskTerminiLista.cancel(true);
			   	}
			   
		
			}
		   
		   	  
		}
}
