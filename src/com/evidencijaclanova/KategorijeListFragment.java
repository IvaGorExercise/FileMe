package com.evidencijaclanova;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evidencijaclanova.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
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
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class KategorijeListFragment extends Fragment  {
	
    List<Kategorija> items = new ArrayList<Kategorija>();
    private int mNum = 0;
    DataBaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setHasOptionsMenu(true);
        
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
   	 	
   		db  = new DataBaseHelper(getActivity()); 

        try { 
        	db.createDataBase(); 
	 	} catch (IOException ioe) {	 
	 		throw new Error("Unable to create database");	 
	 	}
        
   	 	items = dohvatiKategorije();

    }
    
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.kategorije_expandable_layout, container, false);
         
         
         ExpandableListView exList = (ExpandableListView) rootView.findViewById(R.id.expandableListView1);
         exList.setIndicatorBounds(5, 5);
         KategorijeExpandableAdapter exAdpt = new KategorijeExpandableAdapter(items, getActivity(), db);
         exList.setIndicatorBounds(0, 20);
         exList.setAdapter(exAdpt);
         TextView empty=(TextView)rootView.findViewById(R.id.emptyKategorija);
         exList.setEmptyView(empty);
         
         Resources res = this.getResources();
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
		inflater.inflate(R.menu.kategorije_izbornik, menu);
	}

 
public boolean onOptionsItemSelected(MenuItem item)
{
	switch(item.getItemId())
	{
		case R.id.izb_novakategorija:
			FragmentManager fragmentManager = getFragmentManager();
			KategorijaFormFragment fragment = new KategorijaFormFragment();
	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "KategorijaFormFragment").addToBackStack(null).commit();
			return true;
			
			
		default:
			return super.onOptionsItemSelected(item);

	}
}
    
    
    List<Kategorija> dohvatiKategorije()
  		{
  			ArrayList<Kategorija> items = new ArrayList<Kategorija>();
  			ArrayList<Kategorija> kategorije = new ArrayList<Kategorija>();
  			
  			try {	 
  				db.openDataBase();	 
  				kategorije = db.dohvatiKategorije();
  			}catch(SQLException sqle){	 
  	 		throw sqle;	 
  			}	
  			finally
  			{
  				db.close();
  			}
  			if(kategorije!=null)
  			{
  				for(Kategorija c: kategorije)
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
