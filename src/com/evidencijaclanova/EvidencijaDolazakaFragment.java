package com.evidencijaclanova;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.evidencijaclanova.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EvidencijaDolazakaFragment extends Fragment implements OnClickListener {
	
	ArrayList<Grupa> CustomListViewValuesArr = null;
	private ListView listView;
    private int mNum = 0;
    List<Grupa> items = new ArrayList<Grupa>();
    Grupa itemsGrupa = new Grupa();
    List<Clan> itemsClan = new ArrayList<Clan>();
    private ListView mListView;
    DataBaseHelper db;
    
    View rootView;
	private LinearLayout mLinearListView;
	Button btnUnesiTerminClan;
	Button btnBrisiTermin;
	TextView twTerminGrupaPodaci;
	
    final static String ARG_POSITION = "position";
    final static String ARG_Godina = "Godina";
    final static String ARG_Mjesec = "Mjesec";
    final static String ARG_Dan = "Dan";
    final static String ARG_RB = "RB";
    final static String ARG_ID_TERMIN = "ID_Termin";
    
    public String RB = "";
    
    Button btnTerminPromjenaDate;
	TextView twTerminPromjenaIzabraniDatum;
 	public String datumTerminaPromjena = "";
	private final static int ID_IZBORNIK_DATUMA_TERMIN_PROMJENA = 0;
 	private int mGodina = 0;
 	private int mMjesec = 0;
 	private int mDan = 0;
    
    int mCurrentPosition = -1;
    int grupaId = 0;
    long terminId = 0;
    int editStavke = 0;
    public static List<HashMap<Integer,String>> selektiraneGrupe = new ArrayList<HashMap<Integer,String>>();
    public static List<Clan> selektiraniClanovi = new ArrayList<Clan>();
    public List<TerminClan> postojeciClanTermin = new ArrayList<TerminClan>();
 
    
    private EvidencijaDolazakaAdapter adapter;
	public static String ClanId = "0";
	View mLinearView;
	
	
	private WeakReference<PozadinskiZadatakEvidencijaDolazaka> asyncTaskWeakReferenceEvidencijaDolazaka;
	PozadinskiZadatakEvidencijaDolazaka asyncTaskEvidencijaDolazaka = null;
	
	String datumTermina = "";
   
	
	
    
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
        
        
    	Bundle args = getArguments();
        if (args != null)
        {
        	grupaId = args.getInt(ARG_POSITION);
        	mGodina = args.getInt(ARG_Godina);
        	mMjesec = args.getInt(ARG_Mjesec);
        	mDan = args.getInt(ARG_Dan);
        	RB = args.getString(ARG_RB);
        	terminId = args.getLong(ARG_ID_TERMIN);
        	editStavke = 1;
        }
        
        else if (mCurrentPosition != -1)
        {
        	grupaId = mCurrentPosition;
        }
 
        
        //itemsGrupa = dohvatiGrupu(grupaId);
        
        //if(terminId > 0)
        //{
        //	postojeciClanTermin = dohvatiClanovePoTerminu((int)terminId);
        //}
   	 	
    }
    

    
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
		 
	     getActivity().setTitle(R.string.fragmentUnosTerminClan);
         rootView = inflater.inflate(R.layout.terminclanscroll_lyout, container, false);
     	 datumTermina = "";
     	
     	 btnTerminPromjenaDate=(Button)rootView.findViewById(R.id.btnTerminPromjenaDate);
     	 btnTerminPromjenaDate.setOnClickListener(this);
     	 
     	 twTerminPromjenaIzabraniDatum = (TextView)rootView.findViewById(R.id.twTerminPromjenaIzabraniDatum);
           
         btnUnesiTerminClan=(Button)rootView.findViewById(R.id.btnUnesiTerminClan);
         btnUnesiTerminClan.setOnClickListener(this);
         
         btnBrisiTermin=(Button)rootView.findViewById(R.id.btnBrisiTermin);
         btnBrisiTermin.setOnClickListener(this);
         
         
         btnBrisiTermin.setOnClickListener(new OnClickListener() {

		        @Override
		        public void onClick(View v) {

		        	BrisiTerminSve((int)terminId);
		        }
		    });
         
         
      
         twTerminGrupaPodaci = (TextView)rootView.findViewById(R.id.twTerminGrupaPodaci);
         final CheckBox cbSelectAll=(CheckBox)rootView.findViewById(R.id.cbSelectAll);

         
         
      	 mLinearListView = (LinearLayout) rootView.findViewById(R.id.linear_listview);
      	 
     	Integer Jezik = MainActivity.Engleski(getActivity());
  		
  		if(Jezik == 1)
  		{
  			datumTermina = DodajVodeceNule(mDan) + "/" + DodajVodeceNule(mMjesec)+ "/" +  DodajVodeceNule(mGodina);
  		}
  		else
  		{
  			datumTermina = DodajVodeceNule(mDan) + "." + DodajVodeceNule(mMjesec)+ "." +  DodajVodeceNule(mGodina) + ".";
  		}
         

        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
        
           	if (buttonView.isPressed())
 		        {	
		        	if (buttonView.isChecked())
		        	{
		    
		        		int  size = itemsGrupa.getItemList().size();
	        	        for(int i=0; i<size; i++) {
	        	      		CheckBox cbPrisutan = (CheckBox) ((ViewGroup) mLinearListView).getChildAt(i).findViewById(R.id.cbPrisutan);
	        	        	cbPrisutan.setChecked(true);
	        	        }
		        	}
		        	
		        	else
		        	{
		      
		        		
		        		int  size = itemsGrupa.getItemList().size();
	        	        for(int i=0; i<size; i++) {
	        	      		CheckBox cbPrisutan = (CheckBox) ((ViewGroup) mLinearListView).getChildAt(i).findViewById(R.id.cbPrisutan);
	        	        	cbPrisutan.setChecked(false);
	        	        }
		        	}
 		        
 		        }
    
            }
            
          });
        


    	//if(itemsGrupa != null)
     	//{
     	//	if(itemsGrupa.get_id() > 0)
     	//	{
     	//        twTerminGrupaPodaci.setText(itemsGrupa.get_ime().substring(0, 1).toUpperCase() + itemsGrupa.get_ime().substring(1, itemsGrupa.get_ime().length()) + " " + datumTermina + "  (" + RB + ")");
     	    
     	//     	for (int i = 0; i < itemsGrupa.getItemList().size(); i++)
     	 //    	{
     		
     	//			LayoutInflater inflater2 = null;
     	//			inflater2 = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
     	//			mLinearView = inflater.inflate(R.layout.terminclan_rowlayout, null);
     		
     	//			final TextView itemTerminClanPrezime = (TextView) mLinearView.findViewById(R.id.itemTerminClanPrezime);
     	//			final CheckBox cbPrisutan = (CheckBox) mLinearView.findViewById(R.id.cbPrisutan);
     	//			final ImageView itemCGCterminClan = (ImageView) mLinearView.findViewById(R.id.itemCGCTerminClan);
     				
     	//		    final  Clan model = itemsGrupa.getItemList().get(i);
     			    
     	//	    if (model != null) {
     			    	
     	//		    	if(model.get_slika() != null)
     	//		    	{
     	//		    		Bitmap slikaZaPrikaz = getImage(model.get_slika());
     	//			    	itemCGCterminClan.setImageBitmap(ImageHelper.getRoundedCornerBitmap(slikaZaPrikaz,10));
     	//		    	}
     			    	
     	//		    	else
     	//		    	{
     	//		    		itemCGCterminClan.setImageResource(R.drawable.friendcaster);
     	//		    	}
     			    	
     	//		        ClanId = Integer.toString(model.get_id()); 
     	//		        itemTerminClanPrezime.setText(model.get_prezime() + " " + model.get_ime());
     	//				cbPrisutan.setChecked(false);
     					
     	//				if (postojeciClanTermin.size() > 0)
     	//				{
     						
     	//					List<Integer> idPostojeci = new ArrayList<Integer>();
     								
     							
     	//					for(TerminClan tcp: postojeciClanTermin)
     	//					{
     	//						idPostojeci.add(tcp.get_idClan());
     						
     	//					}
     						
     	//					if(idPostojeci.contains(model.get_id()))
     	//					{
     	//						cbPrisutan.setChecked(true);
     	//					}
     						
     	//				}
     					

     	//		    }

     		//		mLinearListView.addView(mLinearView);
     				
     				
     	   //  	}
     	        
     		//}
     	//}
     	

         return rootView;

     }
	 

	 
@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.terminclan_izbornik, menu);
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
			
		case R.id.izb_pregledtermina:
			TerminiListaFragment fragment2 = new TerminiListaFragment();
	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2, "TerminiListaFragment").addToBackStack(null).commit();
			return true;	
			
		default:
			return super.onOptionsItemSelected(item);

	}
}
  

public static Bitmap getImage(byte[] image) {
   // return BitmapFactory.decodeByteArray(image, 0, image.length);
	
	BitmapFactory.Options options=new BitmapFactory.Options();
    options.inPurgeable = true; 
    Bitmap songImage1 = BitmapFactory.decodeByteArray(image,0, image.length,options);
    Bitmap songImage = Bitmap.createScaledBitmap(songImage1, 35 , 35 , true);
	return songImage;
	
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

 public Grupa dohvatiGrupu(Integer id)
	{
		Grupa item = new Grupa();
		Grupa grupa = new Grupa();
		
		try {	 
			db.openDataBase();	 
			grupa = db.GrupaDetalj(id);
		}catch(SQLException sqle){	 
 		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(grupa!=null)
		{

			grupa.setItemList(dohvatiClanovePoGrupama(grupa.get_id()));
			item = grupa;
		}
		
	return item;

	}

public List<Clan> dohvatiClanovePoGrupama(Integer idGrupa)
	{
		ArrayList<Clan> items = new ArrayList<Clan>();
		ArrayList<Clan> clan = new ArrayList<Clan>();
		
		try {	 
			db.openDataBase();	 
			clan = db.dohvatiClanovePoGrupama(idGrupa);
		}catch(SQLException sqle){	 
		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(clan!=null)
		{
			for(Clan c: clan)
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



 	@Override
 	public void onClick(View v)
 	{
		switch(v.getId())
		{
		case R.id.btnTerminPromjenaDate:
			onCreateDialog(ID_IZBORNIK_DATUMA_TERMIN_PROMJENA);
			break;
			case R.id.btnUnesiTerminClan:
				upisiTerminClan(v);
				break;
		}
	
	}
 	
 	
 	public void upisiTerminClan(View view)
 	{
 	    List<Clan> itemsClanZaUnos = new ArrayList<Clan>();
 	    long IdTerminZaUnosLong = terminId;
 	    int IdTerminZaUnos = (int)IdTerminZaUnosLong;
 	    
 		boolean upisiTerminClan = false;
 	    
 		int  size = itemsGrupa.getItemList().size();
 		
 		
        for(int i=0; i<size; i++)
        {
      		CheckBox cbPrisutan = (CheckBox) ((ViewGroup) mLinearListView).getChildAt(i).findViewById(R.id.cbPrisutan);
      		
      		if(cbPrisutan.isChecked())
      		{
      		   final  Clan model = itemsGrupa.getItemList().get(i);
      		   
      		 itemsClanZaUnos.add(model);
      		}

        }

    	try
    	{
    		
			db.openDataBase();
			
			 if(itemsClanZaUnos.size() > 0 &&  IdTerminZaUnos > 0)
		     {
				 
				 if(editStavke  == 0)
				 {
					 upisiTerminClan = db.dodajTerminClan(itemsClanZaUnos, IdTerminZaUnos);
				 }
				 
				 else
				 {
				
					 upisiTerminClan = db.editTerminClan(itemsClanZaUnos, IdTerminZaUnos, grupaId, RB,  mDan, mMjesec, mGodina);
				 }
			
		     }
			 
			 
		}
    	catch(SQLException sqle)
    	{	 
 		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(upisiTerminClan)
		{	
			FragmentManager fragmentManager = getFragmentManager();
			UnosEvidencijeFragment fragment = new UnosEvidencijeFragment();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "UnosEvidencijeFragment").addToBackStack(null).commit();

		}
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
    
    
    
    
    public Dialog onCreateDialog(int id) {

    	switch(id)
    	{
    		case ID_IZBORNIK_DATUMA_TERMIN_PROMJENA:
    			Calendar cMax = Calendar.getInstance();
    			
    			DatePickerDialog dialog = new DatePickerDialog(getActivity(), listenerZaDatuma, cMax.get(Calendar.YEAR), cMax.get(Calendar.MONTH), cMax.get(Calendar.DAY_OF_MONTH));
    			Calendar cMin = Calendar.getInstance();
    			cMin.set(cMin.DAY_OF_MONTH, 1);
    			cMin.set(cMin.MONTH, 0 );
    			cMin.set(cMin.YEAR, 1930);
    			
    			
        		dialog.getDatePicker().setMinDate(cMin.getTimeInMillis());
        		dialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
        		
        		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
        	                "Odustani",
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
    	return null;
    }

    private void ispisDatuma()
    	{ 
    		String sDan;
    		String sMjesec;
    		String sGodin = Integer.toString(mGodina);
    		
    		twTerminPromjenaIzabraniDatum =  (TextView)rootView.findViewById(R.id.twTerminPromjenaIzabraniDatum);
    		
    	if (mDan < 10)
    	{
    		sDan = "0" + Integer.toString(mDan);
    	}
    	
    	else
    	{
    		sDan = Integer.toString(mDan);
    	}
    	
    	if (mMjesec < 10)
    	{
    		sMjesec = "0" + Integer.toString(mMjesec);
    	}
    	
    	else
    	{
    		sMjesec = Integer.toString(mMjesec);
    	}
    	
    	Integer Jezik = MainActivity.Engleski(getActivity());
    	
    	if(Jezik == 1)
    	{
    		twTerminPromjenaIzabraniDatum.setText(new StringBuilder().append(sDan).append("/").append(sMjesec).append("/").append(mGodina));
    		
    	}
    	else
    	{
    		twTerminPromjenaIzabraniDatum.setText(new StringBuilder().append(sDan).append(".").append(sMjesec).append(".").append(mGodina).append("."));
    	}
    	
    		
    	datumTerminaPromjena = sGodin  + sMjesec +  sDan;
    		
    	}

    private DatePickerDialog.OnDateSetListener listenerZaDatuma = new DatePickerDialog.OnDateSetListener()
    {
    	   	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
    	   	{
    	   		mGodina = year;
    	   		mMjesec = monthOfYear + 1;
    	   		mDan = dayOfMonth;
    	   		ispisDatuma();
    	
    		}
    };
  
    
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		    startNewAsyncTaskEvidencijaDolazaka();
	}   	
	   	
	   	

    public void PostaviViewEvidencija(Grupa itemsGrupa)
    {
  
    	   
    	if(itemsGrupa != null)
     	{
     		if(itemsGrupa.get_id() > 0)
     		{
     	        twTerminGrupaPodaci.setText(itemsGrupa.get_ime().substring(0, 1).toUpperCase() + itemsGrupa.get_ime().substring(1, itemsGrupa.get_ime().length()) + " " + datumTermina + "  (" + RB + ")");
     	    
     	     	for (int i = 0; i < itemsGrupa.getItemList().size(); i++)
     	     	{
     		
     				LayoutInflater inflater2 = null;
     				inflater2 = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
     				mLinearView = inflater2.inflate(R.layout.terminclan_rowlayout, null);
     		
     				final TextView itemTerminClanPrezime = (TextView) mLinearView.findViewById(R.id.itemTerminClanPrezime);
     				final CheckBox cbPrisutan = (CheckBox) mLinearView.findViewById(R.id.cbPrisutan);
     				final ImageView itemCGCterminClan = (ImageView) mLinearView.findViewById(R.id.itemCGCTerminClan);
     				
     			    final  Clan model = itemsGrupa.getItemList().get(i);
     			    
     			    if (model != null)
     			    {
     			    	
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
     					
     					if (postojeciClanTermin.size() > 0)
     					{
     						
     						List<Integer> idPostojeci = new ArrayList<Integer>();
     								
     							
     						for(TerminClan tcp: postojeciClanTermin)
     						{
     							idPostojeci.add(tcp.get_idClan());
     						
     						}
     						
     						if(idPostojeci.contains(model.get_id()))
     						{
     							cbPrisutan.setChecked(true);
     						}
     						
     					}
     					

     			    }

     				mLinearListView.addView(mLinearView);
     				
     	     	}
     		}
     	}
   	
    }
	
	
	
	
		 
  private void startNewAsyncTaskEvidencijaDolazaka() {
			      
	  		asyncTaskEvidencijaDolazaka = new PozadinskiZadatakEvidencijaDolazaka(this, getActivity());
			this.asyncTaskWeakReferenceEvidencijaDolazaka = new WeakReference<PozadinskiZadatakEvidencijaDolazaka >(asyncTaskEvidencijaDolazaka); 
			asyncTaskEvidencijaDolazaka.execute();
			   	
		}
  
  
  private class PozadinskiZadatakEvidencijaDolazaka extends AsyncTask<String, Void, Grupa>
	{

  	Activity mContex;
  	private WeakReference<EvidencijaDolazakaFragment> fragmentWeakRef;
  	

public PozadinskiZadatakEvidencijaDolazaka(EvidencijaDolazakaFragment fragment, Activity contex)
  	{

  	    this.mContex=contex;
  	    this.fragmentWeakRef = new WeakReference<EvidencijaDolazakaFragment>(fragment);

  	}
  	
		private final ProgressDialog dialog = new ProgressDialog(getActivity());
		
		protected void onPreExecute() {
			this.dialog.setMessage(getResources().getString(R.string.Loading));
			this.dialog.show();
		 }
		
		
		
		@Override
		protected Grupa doInBackground(String... params)
		{
			  Grupa result = new  Grupa();
			  
			  try {
					 result = dohvatiGrupu(grupaId);
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
			  
			
			   
			   if(result  == null)
			   {
				  dialog.dismiss();
			   }
			   
				 return result;
	
		}
		

			
	   @Override
	   protected void onPostExecute(Grupa result) 
	   {
		   super.onPostExecute(result);
		   
		   dialog.dismiss();
		   
		   	if (this.fragmentWeakRef.get() != null)
		   	{
		   		try 
		   		{
			    	Grupa items = new Grupa();
			   		items = result;
			   		
			   		if(items != null)
			   		{
			   			itemsGrupa = items;
			   			
			   			if(terminId > 0)
			   			{
			   				postojeciClanTermin = dohvatiClanovePoTerminu((int)terminId);
			   			}
			   			
			   			PostaviViewEvidencija(items);
			   		}
			   		
		   		}
		  
		   		catch (Exception e)
		   		{
		   			e.printStackTrace();
		    	
		   			String errorMessage =(e.getMessage()==null)?"Message is empty":e.getMessage();
		   			Log.d(" XXX onPostExecute EvidencijaDolazakaFragment e ", errorMessage);
		   		 	asyncTaskEvidencijaDolazaka.cancel(true);
		    	
		   			Intent intent = new Intent(getActivity(), CrashActivity.class);
		   			intent.putExtra("error", e.toString());
		   			getActivity().startActivity(intent);

					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(10);
		    			    
		   		}
			   
		   }
		   	
		   	else
		   	{
		   		asyncTaskEvidencijaDolazaka.cancel(true);
		   	}
		   
	
		}
	   
	   	  
	}
    
}
