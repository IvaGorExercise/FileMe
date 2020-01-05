package com.evidencijaclanova;

import java.util.ArrayList;
import java.util.List;

import com.evidencijaclanova.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class KategorijaFormFragment extends Fragment implements OnClickListener {
	
	final static String KATEGORIJA_ID = "Kategorija_ID";
	int mCurrentPosition = -1;
	int kategorijaId = -1;
	
	View rootView;
	EditText etKategorijaIme;
	EditText etKategorijOpis;
	TextView txtUpisanaKategorija;
	DataBaseHelper db;
	Button btnUnesiKategoriju;
	Button btnDohvatiKategorije;
	Kategorija KategorijaDetalj = null;
	

    public KategorijaFormFragment() {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    	db  = new DataBaseHelper(getActivity()); 
    	
    	Bundle args = getArguments();
        if (args != null)
        {
        	kategorijaId = args.getInt(KATEGORIJA_ID);
        }
        
        else
        {
        	kategorijaId = -1;
        }
    	
        
    	if(kategorijaId  > 0)
  		{
  			KategorijaDetalj = KategorijaDetalj(kategorijaId);
  		}
    	
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    
        getActivity().setTitle(R.string.fragmentKategorija);
  		rootView = inflater.inflate(R.layout.kategorija_form, container, false);
  		
  		btnUnesiKategoriju=(Button)rootView.findViewById(R.id.btnUnesiKategoriju);
  		btnUnesiKategoriju.setOnClickListener(this);
  		
  		
  		if(KategorijaDetalj  != null)
  		{
   			EditText etKategorijaIme = (EditText) rootView.findViewById(R.id.etKategorijaIme);
   			EditText etKategorijaOpis = (EditText) rootView.findViewById(R.id.etKategorijaOpis);
   			
   			etKategorijaIme.setText(KategorijaDetalj.get_ime());
   			etKategorijaOpis.setText(KategorijaDetalj.get_opis());
       }
  	  
  		
       return rootView;
    }
    
    
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

   }
	


	@Override
	public void onClick(View v) {
		
		switch(v.getId())
    	{
    		case R.id.btnUnesiKategoriju:
    			if (validationSuccess())
   			 	{
    				upisiKategoriju(v);
   			 	}
    			break;
    	}
	}
	
	 private Boolean validationSuccess()
	 {
		   EditText kategorijaIme = (EditText) rootView.findViewById(R.id.etKategorijaIme);
			String poruka = "Unesite ";
			
			if(kategorijaIme.getText().toString().equalsIgnoreCase("") )
			{
				poruka = poruka + "ime kategorije";
			    Toast.makeText(getActivity(), poruka ,0).show();
			    return false;
			}	
			

		    return true;
	 }
	
	
	public void upisiKategoriju(View view)
	{
		etKategorijaIme = (EditText) rootView.findViewById(R.id.etKategorijaIme);
		etKategorijOpis = (EditText) rootView.findViewById(R.id.etKategorijaOpis);
		
		boolean upisikategoriju = false;
		
		try {	 
			db.openDataBase();
			Kategorija kateg = new Kategorija();
			kateg.set_ime(etKategorijaIme.getText().toString());
			kateg.set_opis(etKategorijOpis.getText().toString());  
			
			
			if(KategorijaDetalj != null)
			{
				kateg.set_id(KategorijaDetalj.get_id());
				upisikategoriju = db.updateKategorija(kateg);
			}
			else
			{
				upisikategoriju = db.dodajKategoriju(kateg);
			}
			
			
		}catch(SQLException sqle){	 
 		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(upisikategoriju)
		{

			FragmentManager fragmentManager = getFragmentManager();
			KategorijeListFragment fragment = new KategorijeListFragment();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "KategorijeListFragment").addToBackStack(null).commit();
			etKategorijaIme.setText("");;
			etKategorijOpis.setText("");

		}
		else
		{
		
			Toast.makeText(getActivity(),"Kategorija nije upisan!" ,Toast.LENGTH_SHORT).show();
		}
	}
	

	
    public Kategorija KategorijaDetalj(Integer id)
  	{
    	Kategorija item = new Kategorija();
    	Kategorija kategorija = new Kategorija();
  		
  		try {	 
  			db.openDataBase();	 
  			kategorija = db.KategorijaDetalj(id);
  		}catch(SQLException sqle){	 
   		throw sqle;	 
  		}	
  		finally
  		{
  			db.close();
  		}
  		if(kategorija!=null)
  		{
  			item = kategorija;
  		}
  		
  		else
  		{
  			
  		}

  	return item;

  	}
    

}
