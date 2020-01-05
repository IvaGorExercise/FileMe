package com.evidencijaclanova;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evidencijaclanova.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GrupeListFragment extends Fragment {
	
	ArrayList<Grupa> CustomListViewValuesArr = null;
	private ListView listView;
    private int mNum = 0;
    List<Grupa> items = new ArrayList<Grupa>();
    private ListView mListView;
    DataBaseHelper db;
    
	
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
	        
	   	 	items = dohvatiGrupe();
	   	 	
	    }
	  
	   
	    
		 @Override
	     public View onCreateView(LayoutInflater inflater, ViewGroup container,
	             Bundle savedInstanceState) {
	         View rootView = inflater.inflate(R.layout.grupa_expandable_layout, container, false);
	         
	         ExpandableListView exList = (ExpandableListView) rootView.findViewById(R.id.expandableListViewGrupa);
	         exList.setIndicatorBounds(5, 5);
	         GrupeExpandableAdapter exAdpt = new GrupeExpandableAdapter(items, getActivity(), db);
	         exList.setIndicatorBounds(0, 20);
	         exList.setAdapter(exAdpt);
	         TextView empty=(TextView)rootView.findViewById(R.id.emptyGrupa);
	         exList.setEmptyView(empty);

	         
	     	 Resources res = this.getResources();
		     //Drawable devider = res.getDrawable(R.drawable.line);
	     	 Drawable devider = res.getDrawable(R.drawable.line);
		     
	         exList.setGroupIndicator(null);
	         exList.setDivider(devider);
	         exList.setChildDivider(devider);
	         exList.setChildDivider(devider);
	         exList.setDividerHeight(1);
	   
	         return rootView;

	     }
		 
	
		 
	@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
		{
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.grupe_izbornik, menu);
		}
			
		
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.izb_novagrupa:
				FragmentManager fragmentManager = getFragmentManager();
				GrupeFormFragment fragment = new GrupeFormFragment();
    	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "GrupeFormFragment").addToBackStack(null).commit();
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
	    

}
