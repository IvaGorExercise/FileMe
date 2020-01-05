package com.evidencijaclanova;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.evidencijaclanova.R;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.SQLException;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends  FragmentActivity implements OnClickListener, TabListener   {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mGlavniMenuTitle;
	    
	DataBaseHelper db;
	EditText etClanIme;
	EditText etClanPrezime;
	EditText etClanTelefon;
	EditText etClanEmail;
	EditText etClanAdresa;
	TextView txtUpisaniClan;
	TextView twIzabraniDatum;
	EditText etGrupaIme;
	EditText etGrupaOpis;
	
	ListView list;
    public  MainActivity CustomListView = null;
    public  ArrayList<Clan> CustomListViewValuesArr = new ArrayList<Clan>();
    public static ClanoviListFragment fragment;
     
    private SimpleAdapter mAdapter;
    private List<HashMap<String,String>> mList;
	private ListView mDrawerListA;
    String[] mCountries;
    final private String COUNTRY = "country";
    final private String FLAG = "flag";
    List<Fragment> fragList = new ArrayList<Fragment>();
    
   Integer DaLiPostojiClan = 0;
   
   private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   private final long ONE_DAY = 24 * 60 * 60 * 1000;
   
   public static String VrstaEvidencije = "1";
   public static int ClanPromijenjenActivity = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(MainActivity.this));
		
		setContentView(R.layout.activity_main);
		
	    MainActivity.ClanPromijenjenActivity = 0;
		
		
	    SharedPreferences postavke = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	VrstaEvidencije = postavke.getString("lista", "theDefaultValue");
    	
    	if(VrstaEvidencije != null && !VrstaEvidencije.equals("theDefaultValue"))
        {   
    		VrstaEvidencije = VrstaEvidencije;

        }
       else{
    	   VrstaEvidencije = "1";
        }
    	
    	
        String JezikString = postavke.getString("jezik", "theDefaultValue");
        
    	if(JezikString != null && !JezikString.equals("theDefaultValue"))
        {   
    		JezikString = JezikString;
        }
    	
       else{
    	   JezikString = "1";
        }
    	
        
        Integer Jezik = Integer.parseInt(JezikString);
        
        
        if(Jezik == 2)
        {
        	  Locale locale = new Locale("en");
              Locale.setDefault(locale);
              Configuration config = new Configuration();
              config.locale = locale;
              getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        }
        else
        {
        	  Locale locale = new Locale("hr");
              Locale.setDefault(locale);
              Configuration config = new Configuration();
              config.locale = locale;
              getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        }
        
     


		mTitle = mDrawerTitle = getTitle();
		mGlavniMenuTitle = getResources().getStringArray(R.array.menudrawer_lista);
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	    
	    String[] from = { FLAG,COUNTRY };
	    int[] to = { R.id.flag , R.id.country};
	    
	    mCountries = getResources().getStringArray(R.array.menudrawer_lista);
	    int[] mFlags = new int[]{
	    R.drawable.ic_clanarina,
	    R.drawable.ic_istekla,
	    R.drawable.ic_social_group,
	    R.drawable.ic_social_add_person,
	    R.drawable.ic_peopleingroup,
	    R.drawable.ic_group,
	    R.drawable.ic_atribut,
	    R.drawable.ic_social_notifications,
	    R.drawable.ic_action_navigation_close,
	    R.drawable.ic_action_settings,
	    R.drawable.ic_action_info_outline,
	    R.drawable.ic_action_licence
	    };
	    
	    mList = new ArrayList<HashMap<String,String>>();
	    for(int i=0;i<12;i++){
	    HashMap<String, String> hm = new HashMap<String,String>();
	    hm.put(COUNTRY, mCountries[i]);
	    hm.put(FLAG, Integer.toString(mFlags[i]) );
	    mList.add(hm);
	    }
	    
	    mDrawerList = (ListView) findViewById(R.id.left_drawer);
	    mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_list_with_image_item, from, to);
	    mDrawerList.setAdapter(mAdapter);
	    
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);
        
		
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                )
        {
            public void onDrawerClosed(View view)
            {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

       if (savedInstanceState == null) {
            selectItem(0);
        }
       
      
       
	}
	
	
	@Override
	public void onResume() {
	    super.onResume(); 

	    SharedPreferences postavke = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    String JezikString = postavke.getString("jezik", "theDefaultValue");
    	
        Integer Jezik = MainActivity.Engleski(this);
        
        if(Jezik == 1)
        {
        	  Locale locale = new Locale("en");
              Locale.setDefault(locale);
              Configuration config = new Configuration();
              config.locale = locale;
              getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
              //Toast.makeText(this, "Resume " + locale.toString(), Toast.LENGTH_SHORT).show();
        }
        else
        {
        	  Locale locale = new Locale("hr");
              Locale.setDefault(locale);
              Configuration config = new Configuration();
              config.locale = locale;
              getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
              //Toast.makeText(this, "Resume " + locale.toString(), Toast.LENGTH_SHORT).show();
        }
        
        
        //Toast.makeText(this, "Activity onResume ", Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	protected void onStart()
	{
	    super.onResume(); 
		//Toast.makeText(this, "Activity onStart ", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart(); 
		//Toast.makeText(this, "Activity onRestart ", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause(); 
		//Toast.makeText(this, "Activity onPause ", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop(); 
		//Toast.makeText(this, "Activity onStop ", Toast.LENGTH_SHORT).show();
	}


	
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Fragment f = null;
		ClanoviListFragment tf = null;
		
		if (fragList.size() > tab.getPosition())
				fragList.get(tab.getPosition());
		
		if (f == null) {
			tf = new ClanoviListFragment();
			Bundle data = new Bundle();
			data.putInt("idx",  tab.getPosition());
			tf.setArguments(data);
			fragList.add(tf);
		}
		else
			tf = (ClanoviListFragment) f;
		
		ft.replace(R.id.content_frame, tf);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (fragList.size() > tab.getPosition()) {
			ft.remove(fragList.get(tab.getPosition()));
		}
		
	}

 
    public void onItemClick(int mPosition)
    {
   	 Clan tempValues = (Clan) CustomListViewValuesArr.get(mPosition);          

        Toast.makeText(CustomListView,
                ""+tempValues.get_ime() + "Image:", Toast.LENGTH_LONG)
        .show();
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		
		 MenuInflater inflanter = getMenuInflater();
	        inflanter.inflate(R.menu.izbornik, menu);
	        
		return true;	
		
	}
	
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
       // menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    

	
	  public boolean onOptionsItemSelected(MenuItem item) {
	    // The action bar home/up action should open or close the drawer.
	    // ActionBarDrawerToggle will take care of this.
	    	   
		 

	    	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	    	            return true;
	    	        }
	    	        
	    	        

	    	        FragmentManager fragmentManager = getFragmentManager();
	    	        int kojiBroj = -1;
	    	        
	    	       	switch(item.getItemId())
	    	    	{
	    	       	

	    	    		case R.id.izb_clanarina:
	    	    			
	    	    			kojiBroj = 1;
	    	    		 
	    	    			if(ClanPromijenjenActivity == 1)
	    	        		{
	    	    				DilogSpremiPromjeneKratkiMenu(kojiBroj);
	    	    				return false;
	    	        		}
	    	    			 else
	    	    			 {
	    	    				ClanoviGrupeClanarinaFragment fragment1 = new ClanoviGrupeClanarinaFragment();
	    	    	    		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment1, "Èlanarina").addToBackStack(null).commit();
	    	    	    		return true;
	    	    			 }
	    	    			
	    	    	
	    	    			
	    	    		case R.id.izb_isteklaclanarina:
	    	    			
	    	    			kojiBroj = 2; 
	    	    			if(ClanPromijenjenActivity == 1)
	    	        		{
	    	    				DilogSpremiPromjeneKratkiMenu(kojiBroj);
	    	    				return false;
	    	        		}
	    	    			else
	    	    			{
	    	    				IstekleClanarineFragment fragment2 = new IstekleClanarineFragment();
		    	    			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2, "Èlanarina").addToBackStack(null).commit();
		    	    			return true;
	    	    			}
	    	    		
	    	    			
	    	    		case R.id.izb_popisclanova:
	    	    			
	    	    			kojiBroj = 3; 
	    	    			if(ClanPromijenjenActivity == 1)
	    	        		{
	    	    				DilogSpremiPromjeneKratkiMenu(kojiBroj);
	    	    				return false;
	    	        		}
	    	    			else
	    	    			{
	    	    				ClanoviListFragment  fragment3 = new ClanoviListFragment();
	    	    	        	fragmentManager.beginTransaction().replace(R.id.content_frame, fragment3, "Popis èlanova").addToBackStack(null).commit();
	    	    	        	return true;
	    	    			}
	    	    				
	
	    	    		case R.id.izb_unosclanova:
	    	    			
	    	    			kojiBroj = 4;
	    	    			
	    	    			if(ClanPromijenjenActivity == 1)
	    	        		{
	    	    				DilogSpremiPromjeneKratkiMenu(kojiBroj);
	    	    				return false;
	    	        		}
	    	    			{
	    	    				ClanoviFormFragment fragment4 = new ClanoviFormFragment();
		    	    	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment4, "Unos èlanova").addToBackStack(null).commit();
		    	    			return true;
	    	    			}
	    	    			
	    	    			
	    	    		case R.id.izb_evidencijanastava:
	    	    			
	    	    			kojiBroj = 5;
	    	    			if(ClanPromijenjenActivity == 1)
	    	        		{
	    	    				DilogSpremiPromjeneKratkiMenu(kojiBroj);
	    	    				return false;
	    	        		}
	    	    			{
	    	    				UnosEvidencijeFragment fragment5 = new UnosEvidencijeFragment();
		    	    	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment5, "Unos Evidencije Fragment").addToBackStack(null).commit();
		    	    	 		return true;
	    	    			}
	    	    			
	    	    			case R.id.izb_izlaz:
	    	    				AlertDialog.Builder upozorenje = new AlertDialog.Builder(this); 
	    	    				upozorenje.setIconAttribute(android.R.attr.alertDialogIcon);
	    	    				upozorenje.setTitle(getResources().getString(R.string.UpozorenjeAplikacija));
	    	    				upozorenje.setMessage(getResources().getString(R.string.ZatvoriAplikaciju)); 

	    	    				upozorenje.setPositiveButton(getResources().getString(R.string.Da), new DialogInterface.OnClickListener() { 
	    	       			   	public void onClick(DialogInterface dialog, int id)
	    	       			    { 
	    	       			   		MainActivity.this.finish();
	    	       			    } 
	    	       			    
	    	       			    }); 
	    	       			   
	    	       			 upozorenje.setNegativeButton(getResources().getString(R.string.Odustani),  
	    	       			            new DialogInterface.OnClickListener() { 
	    	       			    public void onClick(DialogInterface dialog, int id) { 
	    	       			            dialog.cancel(); 
	    	       			        } 
	    	       			    }); 
	    	       			   
	    	       			   AlertDialog dijalogUpozorenje = upozorenje.create(); 
	    	       			   dijalogUpozorenje.show();
		    	    	 		return true;
	    	    			
	    	    			
	    	    		 // R.id.action_websearch:
	    	    	     //    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
	    	    	     //    intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
	    	    	     // catch event that there's no activity to handle intent
	    	    	    //    if (intent.resolveActivity(getPackageManager()) != null) {
	    	    	    //        startActivity(intent);
	    	    	    //    } else {
	    	    	   //        Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_SHORT).show();
	    	    	   //   }
	    	    	  //   return true;
	    	    			
	    	    		default:
	    	    			return super.onOptionsItemSelected(item);
	    	    	}
	    	    }
	    	    
	    	    

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    private void selectItem(int position) {

    	FragmentManager fragmentManager = getFragmentManager();
    	

      	switch(position)
    	{
      	
    		case 0:
    			db  = new DataBaseHelper(this); 
    			  try { 
    		        	db.createDataBase(); 
    			 	} catch (IOException ioe) {	 
    			 		throw new Error("Unable to create database");	 
    			 	}
    			
    			DaLiPostojiClan = DaLiPostojiClan();
    			
    		   	if(DaLiPostojiClan > 0)
    	      	{
    		   		if(ClanPromijenjenActivity == 1)
        			{
        				DilogSpremiPromjene(0);
        				break;
        			}
    		   		
    		   		{
    		   	   		ClanoviGrupeClanarinaFragment fragment0 = new ClanoviGrupeClanarinaFragment();
        	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment0, "Èlanarina").addToBackStack(null).commit(); 
    		   		}
    		
    	      	}
    		   	
    		   	else
    		   	{
    			  UputeFragment fragment7 = new UputeFragment();
        	 	  fragmentManager.beginTransaction().replace(R.id.content_frame, fragment7, "Upute").addToBackStack(null).commit();
    		   	
    		   	}
    			
			 break;
      	
    		case 1:
    			if(ClanPromijenjenActivity == 1)
    			{
    				DilogSpremiPromjene(1);
    				break;
    			}
    			
    			{
        			IstekleClanarineFragment fragment1 = new IstekleClanarineFragment();
    	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment1, "Istekle èlanrine").addToBackStack(null).commit();
    			}

    			 break;

    		case 2:
    			if(ClanPromijenjenActivity == 1)
    			{
    				DilogSpremiPromjene(2);
    				break;

    			}
    			{
    	  			ClanoviListFragment fragment2 = new ClanoviListFragment();
        	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2, "Popis èlanova").addToBackStack(null).commit();
    			}
  
    			 break;
    	

    		case 3:
    			if(ClanPromijenjenActivity == 1)
    			{
    				DilogSpremiPromjene(3);
    				break;
    			
    			}
    			else
    			{
    				ClanoviFormFragment fragment3 = new ClanoviFormFragment();
        	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment3, "Unos èlanova").addToBackStack(null).commit();
    			}
    			
    			 break;
    			 
    		case 4:
    			
    			
    			if(ClanPromijenjenActivity == 1)
    			{
    				
              	  AlertDialog.Builder izlaz = new AlertDialog.Builder(this); 
     			   izlaz.setTitle(getResources().getString(R.string.NespremljenePromjeneClan)); 
     			   izlaz.setIconAttribute(android.R.attr.alertDialogIcon); 
     			   izlaz.setMessage(getResources().getString(R.string.IzlazNespremljeniClan)); 

     			   izlaz.setPositiveButton(getResources().getString(R.string.Da), new DialogInterface.OnClickListener() { 
     			    public void onClick(DialogInterface dialog, int id)
     			    { 
     			    	MainActivity.ClanPromijenjenActivity = 0;
     			    	ClanoviPoGrupamaFragment fragment4 = new ClanoviPoGrupamaFragment();
     			    	FragmentManager fragmentManager = getFragmentManager();
        	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment4, "Èlanovi po grupama").addToBackStack(null).commit();
     			      } 
     			    
     			    }); 
     			   
     			   izlaz.setNegativeButton(getResources().getString(R.string.Odustani),  
     			            new DialogInterface.OnClickListener() { 
     			    public void onClick(DialogInterface dialog, int id) { 
     			            dialog.cancel(); 
     			        } 
     			    }); 
     			   
     			   AlertDialog dijalog1 = izlaz.create(); 
     			   dijalog1.show(); 
     			   
    			}
    			else
    			{
    				ClanoviPoGrupamaFragment fragment4 = new ClanoviPoGrupamaFragment();
    	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment4, "Èlanovi po grupama").addToBackStack(null).commit();
    			}
    			 break;
    		
    		case 5:
    			if(ClanPromijenjenActivity == 1)
    			{
    				DilogSpremiPromjene(5);
    				break;
    			
    			}
    			{
    				GrupeListFragment fragment5 = new GrupeListFragment();
    	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment5, "Grupe").addToBackStack(null).commit();
    			}
    			
    			 break;
    			 
    		case 6:
    			if(ClanPromijenjenActivity == 1)
    			{
    				DilogSpremiPromjene(6);
    				break;
    			
    			}
    			{
    				KategorijeListFragment fragment6 = new KategorijeListFragment();
    	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment6, "Atributi").addToBackStack(null).commit();
    			}
    			
    			 break; 
    			 
    		case 7:
    			if(ClanPromijenjenActivity == 1)
    			{
    				DilogSpremiPromjene(7);
    				break;
    			
    			}
    			{
    				UnosEvidencijeFragment fragment7 = new UnosEvidencijeFragment();
    	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment7, "Unos Evidencije Fragment").addToBackStack(null).commit();
    			}
    		
    			 break;
    			 
    		case 8:
    			AlertDialog.Builder upozorenje = new AlertDialog.Builder(this); 
				upozorenje.setIconAttribute(android.R.attr.alertDialogIcon);
				upozorenje.setTitle(getResources().getString(R.string.UpozorenjeAplikacija));
				upozorenje.setMessage(getResources().getString(R.string.ZatvoriAplikaciju)); 

				upozorenje.setPositiveButton(getResources().getString(R.string.Da), new DialogInterface.OnClickListener() { 
   			   	public void onClick(DialogInterface dialog, int id)
   			    { 
   			   		MainActivity.this.finish();
   			    } 
   			    
   			    }); 
   			   
   			 upozorenje.setNegativeButton(getResources().getString(R.string.Odustani),  
   			            new DialogInterface.OnClickListener() { 
   			    public void onClick(DialogInterface dialog, int id) { 
   			            dialog.cancel(); 
   			        } 
   			    }); 
   			   
   			   AlertDialog dijalogUpozorenje = upozorenje.create(); 
   			   dijalogUpozorenje.show();
   			   break;
    			 
    			 
    		case 9:
    			if(ClanPromijenjenActivity == 1)
    			{
    				DilogSpremiPromjene(9);
    				break;
    			
    			}
    			{
    				Postavke fragment9 = new Postavke();
    	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment9, "Postavke").addToBackStack(null).commit();
        			//Intent i = new Intent(getApplicationContext(), MyPreferenceActivity.class);
    		    	//startActivity(i);
    			}
    
    			break;
    			
    		case 10:
    			if(ClanPromijenjenActivity == 1)
    			{
    				DilogSpremiPromjene(10);
    				break;
    			
    			}
    			{
    				UputeFragment fragment10 = new UputeFragment();
    	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment10, "Upute").addToBackStack(null).commit();
    			}
    		
    			 break;
    			 
    		case 11:
    			if(ClanPromijenjenActivity == 1)
    			{
    				DilogSpremiPromjene(11);
    				break;
    			
    			}
    			{
    				UnosKodFragment fragment11 = new UnosKodFragment();
    	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment11, "UnosKodFragment").addToBackStack(null).commit();
    			}
    			
    	}
   
 

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mGlavniMenuTitle[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    
    public void DilogSpremiPromjene(final int Broj)
    {
    	
  
    AlertDialog.Builder izlaz = new AlertDialog.Builder(this); 
	   izlaz.setTitle(getResources().getString(R.string.NespremljenePromjeneClan)); 
	   izlaz.setIconAttribute(android.R.attr.alertDialogIcon); 
	   izlaz.setMessage(getResources().getString(R.string.IzlazNespremljeniClan)); 

	   izlaz.setPositiveButton(getResources().getString(R.string.Da), new DialogInterface.OnClickListener() { 
	    public void onClick(DialogInterface dialog, int id)
	    { 
	    
	    	FragmentManager fragmentManager = getFragmentManager();
	    	
		 	switch(Broj)
	    	{
	   case 0:
		   MainActivity.ClanPromijenjenActivity = 0;
	   		ClanoviGrupeClanarinaFragment fragment0 = new ClanoviGrupeClanarinaFragment();
 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment0, "Èlanarina").addToBackStack(null).commit(); 
	 			 break;

	    case 1:
		    MainActivity.ClanPromijenjenActivity = 0;
			IstekleClanarineFragment fragment1 = new IstekleClanarineFragment();
	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment1, "Istekle èlanrine").addToBackStack(null).commit();
			 break;

		case 2:
		    MainActivity.ClanPromijenjenActivity = 0;
			ClanoviListFragment fragment2 = new ClanoviListFragment();
    	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2, "Popis èlanova").addToBackStack(null).commit();
			 break;
	

		case 3:
		    MainActivity.ClanPromijenjenActivity = 0;
			ClanoviFormFragment fragment3 = new ClanoviFormFragment();
    	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment3, "Unos èlanova").addToBackStack(null).commit();
			 break;
			 
		case 4:
			
		    MainActivity.ClanPromijenjenActivity = 0;
			ClanoviPoGrupamaFragment fragment4 = new ClanoviPoGrupamaFragment();
	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment4, "Èlanovi po grupama").addToBackStack(null).commit();
			 break;
		
		case 5:
		    MainActivity.ClanPromijenjenActivity = 0;
			GrupeListFragment fragment5 = new GrupeListFragment();
	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment5, "Grupe").addToBackStack(null).commit();
			 break;
			 
		case 6:
		    MainActivity.ClanPromijenjenActivity = 0;
			KategorijeListFragment fragment6 = new KategorijeListFragment();
	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment6, "Atributi").addToBackStack(null).commit();
			 break; 
			 
		case 7:
		    MainActivity.ClanPromijenjenActivity = 0;
			UnosEvidencijeFragment fragment7 = new UnosEvidencijeFragment();
	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment7, "Unos Evidencije Fragment").addToBackStack(null).commit();
			 break;
			 
		case 9:
		    MainActivity.ClanPromijenjenActivity = 0;
			Postavke fragment9 = new Postavke();
 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment9, "Postavke").addToBackStack(null).commit();
			break;
			
		case 10:
		    MainActivity.ClanPromijenjenActivity = 0;
			UputeFragment fragment10 = new UputeFragment();
	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment10, "Upute").addToBackStack(null).commit();
			 break;
			 
		case 11:
		    MainActivity.ClanPromijenjenActivity = 0;
			UnosKodFragment fragment11 = new UnosKodFragment();
	 			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment11, "UnosKodFragment").addToBackStack(null).commit();
			 break;
			 
	    	}
			
	      } 
	    
	    }); 
	   
	   izlaz.setNegativeButton(getResources().getString(R.string.Odustani),  
	            new DialogInterface.OnClickListener() { 
	    public void onClick(DialogInterface dialog, int id) { 
	            dialog.cancel(); 
	        } 
	    }); 
	   
	   AlertDialog dijalog1 = izlaz.create(); 
	   dijalog1.show();
	   
    }
    
    
    
    public void DilogSpremiPromjeneKratkiMenu(final int Broj)
    {
    	
  
    AlertDialog.Builder izlaz = new AlertDialog.Builder(this); 
	   izlaz.setTitle(getResources().getString(R.string.NespremljenePromjeneClan)); 
	   izlaz.setIconAttribute(android.R.attr.alertDialogIcon); 
	   izlaz.setMessage(getResources().getString(R.string.IzlazNespremljeniClan)); 

	   izlaz.setPositiveButton(getResources().getString(R.string.Da), new DialogInterface.OnClickListener() { 
	    public void onClick(DialogInterface dialog, int id)
	    { 
	    
	    	FragmentManager fragmentManager = getFragmentManager();
	    	
		 	switch(Broj)
	    	{
			
	    	case 1:
	    	    MainActivity.ClanPromijenjenActivity = 0;
	    		ClanoviGrupeClanarinaFragment fragment1 = new ClanoviGrupeClanarinaFragment();
	    		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment1, "Èlanarina").addToBackStack(null).commit();
			 break;

	    	case 2:
	    	    MainActivity.ClanPromijenjenActivity = 0;
	    		IstekleClanarineFragment fragment2 = new IstekleClanarineFragment();
    			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2, "Èlanarina").addToBackStack(null).commit();
			 break;
	

	    	case 3:
	    	    MainActivity.ClanPromijenjenActivity = 0;
	    		ClanoviListFragment  fragment3 = new ClanoviListFragment();
	        	fragmentManager.beginTransaction().replace(R.id.content_frame, fragment3, "Popis èlanova").addToBackStack(null).commit();
			 break;
			 
	      	case 4:
	      		MainActivity.ClanPromijenjenActivity = 0;
				ClanoviFormFragment fragment4 = new ClanoviFormFragment();
    	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment4, "Unos èlanova").addToBackStack(null).commit();
    	 		break;
    	 		
	    	case 5:
	    		MainActivity.ClanPromijenjenActivity = 0;
    	 		UnosEvidencijeFragment fragment5 = new UnosEvidencijeFragment();
    	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment5, "Unos Evidencije Fragment").addToBackStack(null).commit();
    	 		break;

	    	}
			
	      } 
	    
	    }); 
	   
	   izlaz.setNegativeButton(getResources().getString(R.string.Odustani),  
	            new DialogInterface.OnClickListener() { 
	    public void onClick(DialogInterface dialog, int id) { 
	            dialog.cancel(); 
	        } 
	    }); 
	   
	   AlertDialog dijalog1 = izlaz.create(); 
	   dijalog1.show();
	   
    }
    
    
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		
		FragmentManager fragmentManager = getFragmentManager();
		
	    if(fragmentManager.getBackStackEntryCount() > 1) 
	    {
	        fragmentManager.popBackStack();
	    }
	    else 
	    {
	        //super.onBackPressed();
	        
	    	AlertDialog.Builder upozorenje = new AlertDialog.Builder(this); 
			upozorenje.setIconAttribute(android.R.attr.alertDialogIcon);
			upozorenje.setTitle(getResources().getString(R.string.UpozorenjeAplikacija));
			upozorenje.setMessage(getResources().getString(R.string.ZatvoriAplikaciju)); 

			upozorenje.setPositiveButton(getResources().getString(R.string.Da), new DialogInterface.OnClickListener() { 
			   	public void onClick(DialogInterface dialog, int id)
			    { 
			   		MainActivity.this.finish();
			    } 
			    
			    }); 
			   
			 upozorenje.setNegativeButton(getResources().getString(R.string.Odustani),  
			            new DialogInterface.OnClickListener() { 
			    public void onClick(DialogInterface dialog, int id) { 
			            dialog.cancel(); 
			        } 
			    }); 
			   
			   AlertDialog dijalogUpozorenje = upozorenje.create(); 
			   dijalogUpozorenje.show();
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
	 
	 
	 public static Integer Engleski(Context context)
	 {
		 Integer rezult = 0;
		  SharedPreferences postavke = PreferenceManager.getDefaultSharedPreferences(context);
		  String JezikString = postavke.getString("jezik", "theDefaultValue");
		  
		  
		  if(JezikString != null && !JezikString.equals("theDefaultValue"))
	        {   
	    		JezikString = JezikString;
	        }
	    	
	       else{
	    	   JezikString = "1";
	        }
	    	
	        
	        Integer Jezik = Integer.parseInt(JezikString);
	        
	        if(Jezik == 2)
	        {
	        	  Locale locale = new Locale("en");
	              Locale.setDefault(locale);
	              Configuration config = new Configuration();
	              config.locale = locale;
	              context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
	              rezult = 1; 
	        }
	        else
	        {
	        	  Locale locale = new Locale("hr");
	              Locale.setDefault(locale);
	              Configuration config = new Configuration();
	              config.locale = locale;
	              context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
	              rezult = 0; 
	        }
		    
		    
		 //Locale currentResources = context.getResources().getConfiguration().locale;
		 
		 //if(currentResources != null)
	     // {
		//	 if(currentResources.toString().length() >= 2)
		//	 {
			//	 String prefiks = currentResources.toString().substring(0, 2);
				 
			//	 if(prefiks.contains("en"))
			//	 {
			//		 rezult = 1; 
			//	 }
			// }
		// }
		 
		 return rezult;
		 
	 }
	
	    
}
