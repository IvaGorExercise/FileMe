package com.evidencijaclanova;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.evidencijaclanova.ClanoviFormFragment.KategorijaPolja;
import com.evidencijaclanova.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UnosEvidencijeFragment extends Fragment implements OnItemSelectedListener, OnClickListener  {
	
	View rootView;
	private ListView listView;
	private int mNum = 0;
    List<Grupa> items = new ArrayList<Grupa>();
    List<GrupaSpinner> itemsSpinner = new ArrayList<GrupaSpinner>();
    private ListView mListView;
    DataBaseHelper db;
    
    Button setTerminDate;
    Button btnUnesiTermin;
	TextView twTerminIzabraniDatum;
	EditText etTerminRB;
 	private int mGodina;
 	private int mMjesec;
 	private int mDan;
 	public String datumTermina;
 	private final static int ID_IZBORNIK_DATUMA_TERMIN = 0;
 	private int IdGrupaGlobal = 0;
 	
 	int terminId = 0;
 	
 	final static String TERMIN_ID = "Termin_ID";
	Grupa TerminDetalj = null;
    
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
        	terminId = args.getInt(TERMIN_ID);
        }
        
        else
        {
        	terminId = -1;
        }
    	
        
    	if(terminId  > 0)
  		{
    		//TerminDetalj = TerminDetalj(terminId);
  		}
        
   	 	items = dohvatiGrupe();
   	 	
    }
  
   
    
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.termin_unos_layout, container, false);
        
        getActivity().setTitle(R.string.fragmentUnosTermina);
         
        btnUnesiTermin=(Button)rootView.findViewById(R.id.btnUnesiTermin);
        btnUnesiTermin.setOnClickListener(this);
 	   
         setTerminDate=(Button)rootView.findViewById(R.id.btnTerminDate);
         setTerminDate.setOnClickListener(this);
         twTerminIzabraniDatum = (TextView)rootView.findViewById(R.id.twTerminIzabraniDatum);
         etTerminRB = (EditText)rootView.findViewById(R.id.etTerminRB);
         
         etTerminRB.setText("1");
  	   
         
         Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerTerminGrupe);
    
         if(items.size() > 0)
         {
        	
        		for(Grupa item: items)
				{
        			GrupaSpinner noviItem = new GrupaSpinner();
        			noviItem.set_ime(item.get_ime());
        			noviItem.set_id(item.get_id());
        			itemsSpinner.add(noviItem);
				}
        		
        		
                ArrayAdapter<GrupaSpinner> arrayadapter = new ArrayAdapter<GrupaSpinner>(getActivity(), android.R.layout.simple_spinner_item, itemsSpinner);               
                arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayadapter);
                 
                spinner.setOnItemSelectedListener(this);
                 
                GrupaSpinner selected = (GrupaSpinner) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
             	IdGrupaGlobal = selected.get_id();	
         }
        
       			   
        return rootView;

     }
	 

	 
private GrupaSpinner GrupaSpinner() {
		// TODO Auto-generated method stub
		return null;
	}



@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.terminevidencija_izbornik, menu);
	}
		
	

public boolean onOptionsItemSelected(MenuItem item)
{
	FragmentManager fragmentManager = getFragmentManager();
	
	switch(item.getItemId())
	{
		
		case R.id.izb_pregledtermina:
			TerminiListaFragment fragment2 = new TerminiListaFragment();
	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2, "TerminiListaFragment").addToBackStack(null).commit();
			return true;
			
			
		default:
			return super.onOptionsItemSelected(item);

	}
}
  
  List<Grupa> dohvatiGrupe()
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
				items.add(c);
			}
		}
		
		else
		{
			
		}

	return items;

	}



@Override
public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

	GrupaSpinner selected = (GrupaSpinner) parent.getItemAtPosition(pos);
	IdGrupaGlobal = selected.get_id();
}



@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
}



@Override
public void onClick(View v) {
	
	switch(v.getId())
	{
		case R.id.btnTerminDate:
			onCreateDialog(ID_IZBORNIK_DATUMA_TERMIN);
			break;
		case R.id.btnUnesiTermin:
			  if (validationSuccess())
			  {
				  upisiTermin(v);
			  }
			
			break;
		

	}
}
  


public void upisiTermin(View view)
{

	 twTerminIzabraniDatum = (TextView)rootView.findViewById(R.id.twTerminIzabraniDatum);
	 etTerminRB = (EditText)rootView.findViewById(R.id.etTerminRB);
	
	long upisigrupu = 0;
	
	try {	 
		db.openDataBase();
		Termin term = new Termin();
		term.set_idGrupa(IdGrupaGlobal);
		term.set_godina(mGodina);
		term.set_mjesec(mMjesec);
		term.set_dan(mDan);
		term.set_redniBroj(Integer.parseInt(etTerminRB.getText().toString()));
		
		
		if(TerminDetalj != null)
		{
			//upisigrupu = db.updateGrupa(term);
		}
		else
		{
			upisigrupu = db.dodajTermin(term);
		}
		
		
	}catch(SQLException sqle){	 
		throw sqle;	 
	}	
	finally
	{
		db.close();
	}
	if(upisigrupu > 0)
	{	
		FragmentManager fragmentManager = getFragmentManager();
		EvidencijaDolazakaFragment fragment = new EvidencijaDolazakaFragment();
		Bundle args2 = new Bundle();
		
		args2.putLong(EvidencijaDolazakaFragment.ARG_ID_TERMIN, upisigrupu);
        args2.putInt(EvidencijaDolazakaFragment.ARG_POSITION, IdGrupaGlobal);
        args2.putInt(EvidencijaDolazakaFragment.ARG_Godina, mGodina);
        args2.putInt(EvidencijaDolazakaFragment.ARG_Mjesec, mMjesec);
        args2.putInt(EvidencijaDolazakaFragment.ARG_Dan, mDan);
        args2.putString(EvidencijaDolazakaFragment.ARG_RB, etTerminRB.getText().toString());
        fragment.setArguments(args2);
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "EvidencijaDolazakaFragment").addToBackStack(null).commit();
		etTerminRB.setText("1");
		twTerminIzabraniDatum.setText("");
		IdGrupaGlobal = 0;

		
	}
	else
	{
	
		Toast.makeText(getActivity(),"Termin nije upisan!", Toast.LENGTH_SHORT).show();
	}
}

public Termin TerminDetalj(Integer id)
	{
	Termin item = new Termin();
	Termin termin = new Termin();
		
		try {	 
			db.openDataBase();	 
			//termin = db.GrupaDetalj(id);
		}catch(SQLException sqle){	 
		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(termin!=null)
		{
			item = termin;
		}
		
	return item;

	}

private Boolean validationSuccess()
{
	
	twTerminIzabraniDatum = (TextView)rootView.findViewById(R.id.twTerminIzabraniDatum);
	etTerminRB = (EditText)rootView.findViewById(R.id.etTerminRB);
	String poruka = getResources().getString(R.string.validaciaUnesite) + " ";

	
	if(IdGrupaGlobal == 0 )
	{
		poruka = poruka + getResources().getString(R.string.validacijaTerminGrupa) + " ";
	}	
	
	if(twTerminIzabraniDatum.getText().toString().equalsIgnoreCase("") && !etTerminRB.getText().toString().equalsIgnoreCase("") )
	{
		poruka = poruka + getResources().getString(R.string.validacijaTerminDatum);
	}	
	
	if (etTerminRB.getText().toString().equalsIgnoreCase("") && !twTerminIzabraniDatum.getText().toString().equalsIgnoreCase(""))
	{
		poruka = poruka +  getResources().getString(R.string.validacijaTerminRedniBroj);
	}
	
	if (twTerminIzabraniDatum.getText().toString().equalsIgnoreCase("") && etTerminRB.getText().toString().equalsIgnoreCase(""))
	{
		poruka = poruka + getResources().getString(R.string.validacijaTerminDatum) + " " + getResources().getString(R.string.validacijaTerminRedniBroj)  ;
	}
	
    if(twTerminIzabraniDatum.getText().toString().equalsIgnoreCase("") || etTerminRB.getText().toString().equalsIgnoreCase("") || IdGrupaGlobal == 0)
    {
    	Toast.makeText(getActivity(), poruka, 0).show();
      return false;
    }
	
	 return true;
}

public Dialog onCreateDialog(int id) {

	switch(id)
	{
		case ID_IZBORNIK_DATUMA_TERMIN:
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
		
		twTerminIzabraniDatum =  (TextView)rootView.findViewById(R.id.twTerminIzabraniDatum);
		
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
		twTerminIzabraniDatum.setText(new StringBuilder().append(sDan).append("/").append(sMjesec).append("/").append(mGodina));
		
	}
	else
	{
		twTerminIzabraniDatum.setText(new StringBuilder().append(sDan).append(".").append(sMjesec).append(".").append(mGodina).append("."));
	}
	
		
		datumTermina = sGodin  + sMjesec +  sDan;
		
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

}

class GrupaSpinner {
	
    private int id;
    private String ime;
    
    
    public GrupaSpinner()
    {
    }
    
    
    public int get_id() {
        return id;
    }
    public void set_id(int id) {
        this.id = id;
    }
    public String get_ime() {
        return ime;
    }
    public void set_ime(String ime) {
        this.ime = ime;
    }

     @Override
     public String toString() {          
         return  ime;
     }
}

