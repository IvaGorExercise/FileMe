
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
import android.opengl.Visibility;
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

public class UnosKodFragment extends Fragment implements OnItemSelectedListener, OnClickListener  {
	
	View rootView;
	private int mNum = 0;
    DataBaseHelper db;
    
	TextView txtKod;
	EditText etKod;
	Button btnUnesiKod;
    
 	private int mGodina;
 	private int mMjesec;
 	private int mDan;
 	
 	Instalacija instalacija = new Instalacija();

    
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
        
        instalacija =  Instalacija();

    }
  
   
    
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.unoskod_layout, container, false);
        
        getActivity().setTitle(R.string.fragmentUnosKoda);
        
        txtKod=(TextView)rootView.findViewById(R.id.tvUnesiKod);
        etKod=(EditText)rootView.findViewById(R.id.etUnesiKod);
        
        btnUnesiKod=(Button)rootView.findViewById(R.id.btnUnesiKod);
        btnUnesiKod.setOnClickListener(this);

  		
  		if(instalacija != null)
  		{
  			if(!instalacija.get_licencaBroj().equalsIgnoreCase(""))
  	  		{
  				//etKod.setText(instalacija.get_licencaBroj());
  				etKod.setVisibility(View.INVISIBLE);
  				btnUnesiKod.setVisibility(View.INVISIBLE);
  				StringBuilder sb = new StringBuilder();
  				sb.append(getResources().getString(R.string.LicencaBroj));
  				sb.append("\n");
  				sb.append(instalacija.get_licencaBroj());
  				sb.append("\n");
  				sb.append("\n");
  				sb.append(getResources().getString(R.string.LicencaDatum));
  				sb.append("\n");
  				
  			    Integer Jezik = MainActivity.Engleski(getActivity());
  			    
  			    String DatumLicence = "";
  			    String DatumLicenceBaza = instalacija.get_datumLicenca();
  			    
  			    if(DatumLicenceBaza != "")
  			    {
  			    	if(DatumLicenceBaza.length() >= 8)
  	  			    {
  			    		 if(Jezik == 1)
  		  		         {
  		  		        	DatumLicence = DatumLicenceBaza.substring(6,8) +  "/" + DatumLicenceBaza.substring(4,6) + "/" + DatumLicenceBaza.substring(0,4);
  		  		         }
  		  		         else
  		  		         {
  		  		        	DatumLicence = DatumLicenceBaza.substring(6,8) +  "." + DatumLicenceBaza.substring(4,6) + "." + DatumLicenceBaza.substring(0,4) +".";
  		  		         }
  	  			    }
  			    	
  			    }
  			    

  				sb.append(DatumLicence);
  				
  				txtKod.setText("");
  				txtKod.setText(sb.toString());
  	       }
  			
  			else
  			{
  				etKod.setVisibility(View.VISIBLE);
  				btnUnesiKod.setVisibility(View.VISIBLE);
  			}

       }
        
        
       			   
        return rootView;

     }
	 



@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
	}
		
	

private Boolean validationSuccess()
{
	etKod = (EditText) rootView.findViewById(R.id.etUnesiKod);

	String poruka = " ";

	if(!etKod.getText().toString().equalsIgnoreCase(""))
	{
		String kodBroj = etKod.getText().toString();
		int duzina = kodBroj.length();
		
		if (duzina > 7)
		{
			String prvi = kodBroj.substring(3, 4);
			String drugi = kodBroj.substring(6, 7);
			
			if (prvi.equals("5"))
			{
			  int  a = 1;
			}
			
			if (drugi.equals("2"))
			{
			  int  a = 1;
			}
			
			if ((prvi.equals("5")|| prvi.equals("8")) && (drugi.equals("2")|| drugi.equals("7")))
			{
				return true;
			}
			
			else
			{
				poruka = getResources().getString(R.string.validacijaKodaNeispravan) + " ";
				Toast.makeText(getActivity(), poruka ,0).show();
			    return false;
			}

		}
		
		poruka = getResources().getString(R.string.validacijaKodaNeispravan) + " ";
		Toast.makeText(getActivity(), poruka ,0).show();
	    return false;
	}
	
	else
	{
		poruka = getResources().getString(R.string.validacijaKodaNijeUnesen) + " ";
		Toast.makeText(getActivity(), poruka ,0).show();
	    return false;
	}
	

}



@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	switch(v.getId())
	{
		case R.id.btnUnesiKod:
			if (validationSuccess())
			 {
				UnesiKod(v);
			 }
			
		
			break;
	}
	
}

public void UnesiKod(View view)
{
    etKod=(EditText)rootView.findViewById(R.id.etUnesiKod);

	
    long updateinstalacija = 0;
	
	try {	 
		

		if(instalacija != null)
		{
			db.openDataBase();
			instalacija.set_licencaBroj(etKod.getText().toString());
			updateinstalacija = db.UpdateInstlacija(instalacija);
		}
		else
		{
			
		}
		
		
	}catch(SQLException sqle){	 
		throw sqle;	 
	}	
	finally
	{
		db.close();
	}
	
	if(updateinstalacija > 0)
	{		
		etKod.setVisibility(View.INVISIBLE);
			btnUnesiKod.setVisibility(View.INVISIBLE);
			StringBuilder sb = new StringBuilder();
			sb.append(getResources().getString(R.string.LicencaBroj));
			sb.append("\n");
			sb.append(instalacija.get_licencaBroj());
			sb.append("\n");
			sb.append("\n");
			sb.append(getResources().getString(R.string.LicencaDatum));
			sb.append("\n");
			
		    Integer Jezik = MainActivity.Engleski(getActivity());
		    
		    String DatumLicence = "";
		    
			Calendar c = Calendar.getInstance();
		    Integer gGodina = c.get(Calendar.YEAR);
		    Integer  gMjesec = c.get(Calendar.MONTH) + 1;
		    Integer  gDan = c.get(Calendar.DAY_OF_MONTH);
		    
	         if(Jezik == 1)
	         {
	        	DatumLicence = DodajVodeceNule(gDan) + "/" +  DodajVodeceNule(gMjesec)  + "/" + Integer.toString(gGodina) ;
	         }
	         else
	         {
	        	 DatumLicence = DodajVodeceNule(gDan) + "." +  DodajVodeceNule(gMjesec)  + "." + Integer.toString(gGodina) + "." ;
	         }
	      
	         
			sb.append(DatumLicence);
			
			txtKod.setText("");
			txtKod.setText(sb.toString());
	}
	else
	{
	
	}
}



@Override
public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	
}


@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
}

public Instalacija Instalacija()
	{
	Instalacija item = new Instalacija();
	Instalacija inst = new Instalacija();
		
		try {	 
			db.openDataBase();	 
			inst = db.dohvatiInstalciju();
		}catch(SQLException sqle){	 
		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(inst!=null)
		{
			item = inst;
		}
		
		else
		{
			
		}

	return item;

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
 
 
}


