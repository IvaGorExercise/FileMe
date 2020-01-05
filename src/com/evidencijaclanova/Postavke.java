package com.evidencijaclanova;


import java.util.Locale;

import com.evidencijaclanova.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Postavke extends PreferenceFragment  implements OnSharedPreferenceChangeListener
{
	 public static String VrstaEvidencije = "1";
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.postavke);
	        
	        updateListPrefSummary_PREF_LIST();
	        updateJezik();
	        
	        SharedPreferences postavke = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    	VrstaEvidencije = postavke.getString("lista", "theDefaultValue");
	   }
	
	 	
	
	 @Override
	 public void onResume() {
	  super.onResume();
	  getPreferenceScreen().getSharedPreferences()
	   .registerOnSharedPreferenceChangeListener(this);
	 }
	  
	 @Override
	 public void onPause() {
	  super.onPause();
	  getPreferenceScreen().getSharedPreferences()
	   .unregisterOnSharedPreferenceChangeListener(this);
	 }
	 
	 //Apply for ListPreference with key="PREF_LIST"
	 private void updateListPrefSummary_PREF_LIST(){
	  ListPreference preference = (ListPreference)findPreference("lista");
	  CharSequence entry = ((ListPreference) preference).getEntry();
	  String select = ((ListPreference) preference).getValue();
	  Integer selectIndex = Integer.parseInt(select);
	  preference.setSummary(getResources().getString(R.string.odabranaVrstaEvidencije) + " " + select);
	  preference.setValueIndex(selectIndex-1);
	 }
	 
	 private Integer parseInt(CharSequence select) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	 public void onSharedPreferenceChanged(
	   SharedPreferences sharedPreferences, String key)
	{
	   
	  //if changed SharedPreference is ListPreference with key="PREF_LIST",
	  // update summary
	  if(key.equals("lista"))
	  {
	   updateListPrefSummary_PREF_LIST();
       SharedPreferences postavke = PreferenceManager.getDefaultSharedPreferences(getActivity());
   		VrstaEvidencije = postavke.getString("lista", "theDefaultValue");
   		
		getActivity().finish();
		 Intent intent = new Intent(getActivity(),MainActivity.class);
         startActivity(intent);
         
		
	  };
	  
	  if(key.equals("jezik"))
	  {
		 updateJezik();
		 getActivity().finish();
		 Intent intent = new Intent(getActivity(), MainActivity.class);
         startActivity(intent);
		
	  };

	 }
	
	
	 private void updateJezik(){
		  ListPreference preference = (ListPreference)findPreference("jezik");
		  CharSequence entry = ((ListPreference) preference).getEntry();
		  String select = ((ListPreference) preference).getValue();
		  String jezik = (String) ((ListPreference) preference).getEntry();
		  Integer selectIndex = Integer.parseInt(select);
		  preference.setSummary(getResources().getString(R.string.odabraniJezik) + " " + jezik);
		  preference.setValueIndex(selectIndex-1);
		  
		
		  preference.setDefaultValue(selectIndex-1);
		  
		  //Locale locale = new Locale(select);
		  
			Configuration config = new Configuration();
			switch (selectIndex) {
			case 1:
				Locale locale1 = new Locale("hr");
				config.locale = locale1;
				break;
			case 2:
				config.locale = Locale.ENGLISH;
		
			}
			getResources().updateConfiguration(config, null);
			

		 getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());
		 
		 }
	 
}
