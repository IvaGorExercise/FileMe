package com.evidencijaclanova;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evidencijaclanova.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Color;
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

public class ClanoviPoGrupamaFragment extends Fragment {
	
	ArrayList<Grupa> CustomListViewValuesArr = null;
	private ListView listView;
    private int mNum = 0;
    List<Grupa> items = new ArrayList<Grupa>();
    private ListView mListView;
    DataBaseHelper db;
    TextView twMemberCount;
    int BrojClanova  = 0;
    
    
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
   	    BrojClanova = DaLiPostojiClan();
   	 	
    }
    

    
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
		 
	     getActivity().setTitle(R.string.fragmentClanoviPoGrupm);
         View rootView = inflater.inflate(R.layout.clanovipogrupama_expandable_layout, container, false);
         
         ExpandableListView exList = (ExpandableListView) rootView.findViewById(R.id.expandableListViewGrupa);
         exList.setIndicatorBounds(5, 5);
         ClanoviPoGrupiExpandableAdapter exAdpt = new ClanoviPoGrupiExpandableAdapter(items, getActivity(), db);
         exList.setIndicatorBounds(0, 20);
         exList.setAdapter(exAdpt);
         TextView empty=(TextView)rootView.findViewById(R.id.emptyGrupa);
         exList.setEmptyView(empty);
         
     	 Resources res = this.getResources();
	     Drawable devider = res.getDrawable(R.drawable.line);

	     
         exList.setGroupIndicator(null);
         exList.setDivider(devider);
         exList.setChildDivider(devider);
         exList.setChildDivider(devider);
         exList.setDividerHeight(1);
                 
        
      
         if(BrojClanova > 0){
        	
        	   twMemberCount = (TextView)rootView.findViewById(R.id.twMemberCount);
               twMemberCount.setText(getResources().getString(R.string.brojClanova) + " " + Integer.toString(BrojClanova));
         }
         

         
         //exList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

           //  @Override
           //  public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        
           // 	 Clan clan = items.get(groupPosition).getItemList().get(childPosition);
            //	 Integer ClanIdInteger = clan.get_id();
   	     	//	 ClanoviDetaljFragment clandetaljFragment = new ClanoviDetaljFragment();
   	         //    Bundle args2 = new Bundle();
   	         //    args2.putInt(ClanoviDetaljFragment.ARG_POSITION, ClanIdInteger);
   	         //    clandetaljFragment.setArguments(args2);
   	         //    FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
   	          //   transaction.replace(R.id.content_frame, clandetaljFragment);
   	          //   transaction.addToBackStack(null);
   	          //   transaction.commit();
   	             	
              //   return true;
           //  }
        // });
   
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


Integer DaLiPostojiClan()
{
	Integer vrati = 0;
	
	try {	 
		db.openDataBase();	 
		vrati = db.DohvatiBrojClanova();
	}catch(SQLException sqle){	 
		throw sqle;	 
	}	
	finally
	{
		db.close();
	}

return vrati;

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
			for(Grupa c: grupe)
			{
				c.setItemList(dohvatiClanovePoGrupama(c.get_id()));
				items.add(c);
				
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
    

}
