package com.evidencijaclanova;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.evidencijaclanova.R;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class IstekleClanarineFragment extends Fragment {
	
	
	ArrayList<Grupa> CustomListViewValuesArr = null;
	private ListView listView;
    private int mNum = 0;
    List<Grupa> items = new ArrayList<Grupa>();
    private ListView mListView;
    DataBaseHelper db;
	String VrstaEvidencije = MainActivity.VrstaEvidencije;
	Button MjesecGodina;
	public ExpandableListView exList;
	IstekleClanrineAdapter exAdpt;
	
	private int gGodina;
	private int gMjesec;
	private int gDan;
	
	private TextView actualDate;
	private DatePicker datePicker;
	private Button changeDate;
	
	static final int MJESEC_GODINA_DIALOG_ID = 3;
	
    
	  @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        
	        setHasOptionsMenu(false);
	        
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

	   	 	items = dohvatiGrupe();
	   	 	
	    }
	    

	    
		 @Override
	     public View onCreateView(LayoutInflater inflater, ViewGroup container,
	             Bundle savedInstanceState) {
			 
		     getActivity().setTitle(R.string.fragmentIstekleClanarine);
	         View rootView = inflater.inflate(R.layout.istekle_clanarine_expandable_layout, container, false);
	         
	         MjesecGodina = (Button)rootView.findViewById(R.id.btnMjesecGodinaIstekla);
	         
	         Integer Jezik = MainActivity.Engleski(getActivity());
	         
	         if(Jezik == 1)
	         {
	        	 MjesecGodina.setText(getResources().getString(R.string.isteklaClanarina) + " " + Integer.toString(gDan) + "/" +  Integer.toString(gMjesec) +  "/" + Integer.toString(gGodina));
	         }
	         
	         else
	         {
	        	 MjesecGodina.setText(getResources().getString(R.string.isteklaClanarina) + " " + Integer.toString(gDan) + "." +  Integer.toString(gMjesec) +  "." + Integer.toString(gGodina) +  ". "); 
	         }
	         
	         
	         MjesecGodina.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					DatePickerDialog dialog = new DatePickerDialog(getActivity(), listenerZaDatuma, gGodina, gMjesec-1, gDan);

					dialog.show();
				
				}
				
			});
	         

	         exList = (ExpandableListView) rootView.findViewById(R.id.expandableListViewGrupaIstekle);
	         exList.setIndicatorBounds(5, 5);
	         VrstaEvidencije = MainActivity.VrstaEvidencije;
	         exAdpt = new IstekleClanrineAdapter(items, getActivity(), db, VrstaEvidencije, gDan, gGodina, gMjesec);
	         exList.setIndicatorBounds(0, 20);
	         exList.setAdapter(exAdpt);
	         
	     	int count =  exAdpt.getGroupCount();
	   		for (int i = 0; i <count ; i++)
	   		{
	   			if (exList != null)
	   			{
	   				exList.expandGroup(i);
	   		    }
	   			
	   		
	   		}
	         
	     	 Resources res = this.getResources();
		     Drawable devider = res.getDrawable(R.drawable.line);
			
	         exList.setGroupIndicator(null);
	         exList.setDivider(devider);
	         exList.setChildDivider(devider);
	         exList.setDividerHeight(1);

	         return rootView;

	     }
		 

	

		 private DatePicker findDatePicker(ViewGroup group) {
		        if (group != null) {
		            for (int i = 0, j = group.getChildCount(); i < j; i++) {
		                View child = group.getChildAt(i);
		                if (child instanceof DatePicker) {
		                    return (DatePicker) child;
		                } else if (child instanceof ViewGroup) {
		                    DatePicker result = findDatePicker((ViewGroup) child);
		                    if (result != null)
		                        return result;
		                }
		            }
		        }
		        return null;

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
			        	 MjesecGodina.setText(getResources().getString(R.string.isteklaClanarina) + " " + Integer.toString(dayOfMonth) +  "/"  + Integer.toString(monthOfYear + 1) +  "/" + Integer.toString(year));
			         }
			         else
			         {
			        	 MjesecGodina.setText(getResources().getString(R.string.isteklaClanarina) + " " + Integer.toString(dayOfMonth) +  "."  + Integer.toString(monthOfYear + 1) +  "." + Integer.toString(year) + ". ");
			         }

			   		
			   		items = dohvatiGrupe();
			   		VrstaEvidencije = MainActivity.VrstaEvidencije;
			   		exAdpt = new IstekleClanrineAdapter(items, getActivity(), db, VrstaEvidencije, dayOfMonth, year, monthOfYear + 1);
			   		//exAdpt.notifyDataSetChanged();
			   	    exList.setAdapter(exAdpt);
			   	    
			   	    
			   	    int count =  exAdpt.getGroupCount();
			   		for (int i = 0; i <count ; i++)
			   		{
			   			if (exList != null)
			   			{
			   				exList.expandGroup(i);
			   		    } 
			   		}
			
				}

		   	};
		 
		
		 
	@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
		{
			super.onCreateOptionsMenu(menu, inflater);
		}
			
		 
	 public List<Grupa> dohvatiGrupe()
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
				for(Grupa g: grupe)
				{
					g.setItemList(dohvatiClanovePoGrupama(g.get_id()));
					int BrojClanova = g.getItemList().size();
					if(BrojClanova > 0)
					{
						items.add(g);
					}	
				}
			}
			
			else
			{
				
			}

		return items;

		}



	public List<Clan> dohvatiClanovePoGrupama(Integer idGrupa)
	{
		ArrayList<Clan> items = new ArrayList<Clan>();
		ArrayList<Clan> clan = new ArrayList<Clan>();
		
		VrstaEvidencije = MainActivity.VrstaEvidencije;
		
		try {	 
			db.openDataBase();	 
			clan = db.dohvatiClanovePoGrupamaIsteklaClanarina(idGrupa);
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
				Integer clanarinaDetalj = db.ClanarinaIstekla2(idGrupa, c.get_id(), gGodina, gMjesec, gDan, VrstaEvidencije);
				if(clanarinaDetalj != null)
				{
					if(clanarinaDetalj == 0)
					{
						items.add(c);

					}
				
				}
				
			}
		}
		
		else
		{
			
		}

	return items;

	}


}



	


