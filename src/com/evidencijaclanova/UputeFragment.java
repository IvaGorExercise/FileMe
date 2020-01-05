package com.evidencijaclanova;

import com.evidencijaclanova.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UputeFragment extends Fragment {

	View rootView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState); 
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    
        getActivity().setTitle(R.string.fragmentUpute);
  		rootView = inflater.inflate(R.layout.upute_fragment, container, false);
  		
  		
       return rootView;
    }
    

}
