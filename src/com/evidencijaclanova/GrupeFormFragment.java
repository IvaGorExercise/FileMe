package com.evidencijaclanova;

import java.util.ArrayList;
import java.util.List;

import com.evidencijaclanova.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GrupeFormFragment extends Fragment implements OnClickListener {

	final static String GRUPA_ID = "Grupa_ID";
	int mCurrentPosition = -1;
	int grupaId = -1;
	
	View rootView;
	EditText etGrupaIme;
	EditText etGrupaOpis;
	EditText etGrupaCijena;
	EditText etGrupaValuta;
	CheckBox cbGrupaCijenaNaFormi;
	TextView txtUpisanaGrupa;
	DataBaseHelper db;
	Button btnUnesiGrupu;
	Button btnDohvatiGrupu;

	Grupa GrupaDetalj = null;
	

	
	public ArrayList<Integer> izAdaptera;
	
	   
    public GrupeFormFragment() {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    	db  = new DataBaseHelper(getActivity()); 
    	
    	Bundle args = getArguments();
        if (args != null)
        {
        	grupaId = args.getInt(GRUPA_ID);
        }
        
        else
        {
        	grupaId = -1;
        }
    	
        
    	if(grupaId  > 0)
  		{
    		GrupaDetalj = GrupaDetalj(grupaId);
  		}

    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    
        getActivity().setTitle(R.string.fragmentGrupa);
  		rootView = inflater.inflate(R.layout.grupa_form, container, false);
  		
  		btnUnesiGrupu=(Button)rootView.findViewById(R.id.btnUnesiGrupu);
  		btnUnesiGrupu.setOnClickListener(this);

  		
  		if(GrupaDetalj  != null)
  		{
   			EditText etGrupaIme = (EditText) rootView.findViewById(R.id.etGrupaIme);
   			EditText etGrupaOpis = (EditText) rootView.findViewById(R.id.etGrupaOpis);
   			EditText etGrupaCijena = (EditText) rootView.findViewById(R.id.etGrupaCijena);
   			EditText etGrupaValuta = (EditText) rootView.findViewById(R.id.etGrupaValuta);
   			CheckBox cbGrupaNaFormi = (CheckBox) rootView.findViewById(R.id.cbGrupaCijenaNaFormi);
   			
   			etGrupaIme.setText(GrupaDetalj.get_ime());
   			etGrupaOpis.setText(GrupaDetalj.get_opis());
   			etGrupaCijena.setText(Float.toString(GrupaDetalj.get_iznos()));
   			etGrupaValuta.setText(GrupaDetalj.get_valuta());
   			
   			if(GrupaDetalj.get_naFormi() == 1)
   			{
   				cbGrupaNaFormi.setChecked(true);
   			}
   			else
   			{
   				cbGrupaNaFormi.setChecked(false);
   			}
       }
  		
       return rootView;
    }
    
    
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

   }
	

    
    private void setListAdapter(ArrayAdapter<Grupa> adapter) {
		// TODO Auto-generated method stub
		
	}


   

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
    	{
    		case R.id.btnUnesiGrupu:
    			if (validationSuccess())
    			 {
    				upisiGrupu(v);
    			 }
    			
    		
    			break;
    	}
		
	}
	
	 private Boolean validationSuccess()
	 {
		   EditText grupaIme = (EditText) rootView.findViewById(R.id.etGrupaIme);
			String poruka = "Unesite ";
			
			if(grupaIme.getText().toString().equalsIgnoreCase("") )
			{
				poruka = poruka + "ime grupe";
			    Toast.makeText(getActivity(), poruka ,0).show();
			    return false;
			}	
			

		    return true;
	 }
	
	
	public void upisiGrupu(View view)
	{
		etGrupaIme = (EditText) rootView.findViewById(R.id.etGrupaIme);
		etGrupaOpis = (EditText) rootView.findViewById(R.id.etGrupaOpis);
		etGrupaCijena = (EditText) rootView.findViewById(R.id.etGrupaCijena) ;
		etGrupaValuta = (EditText) rootView.findViewById(R.id.etGrupaValuta);
		cbGrupaCijenaNaFormi = (CheckBox) rootView.findViewById(R.id.cbGrupaCijenaNaFormi);
		
		boolean upisigrupu = false;
		
		try {	 
			db.openDataBase();
			Grupa grup = new Grupa();
			grup.set_ime(etGrupaIme.getText().toString());
			grup.set_opis(etGrupaOpis.getText().toString());
			grup.set_valuta(etGrupaValuta.getText().toString());
			
			if(!etGrupaCijena.getText().toString().equalsIgnoreCase(""))
			{
				grup.set_iznos(Float.parseFloat(etGrupaCijena.getText().toString()));
			}
			else
			{
				grup.set_iznos(Float.parseFloat("0.0"));
			}
			
			
			if(cbGrupaCijenaNaFormi.isChecked())
			{
				grup.set_naFormi(1);
			}
			else
			{
				grup.set_naFormi(0);
			}
			
			
			

			if(GrupaDetalj != null)
			{
				grup.set_id(GrupaDetalj.get_id());
				upisigrupu = db.updateGrupa(grup);
			}
			else
			{
				upisigrupu = db.dodajGrupu(grup);
			}
			
			
		}catch(SQLException sqle){	 
 		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(upisigrupu)
		{	
			FragmentManager fragmentManager = getFragmentManager();
			GrupeListFragment fragment5 = new GrupeListFragment();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment5, "GrupeListFragment").addToBackStack(null).commit();
			etGrupaIme.setText("");
			etGrupaOpis.setText("");
			etGrupaCijena.setText("");
			etGrupaValuta.setText("");
			cbGrupaCijenaNaFormi.setChecked(false);
		}
		else
		{

		}
	}
	

	
    public Grupa GrupaDetalj(Integer id)
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
  			item = grupa;
  		}
  		
  		else
  		{
  			
  		}

  	return item;

  	}

}
