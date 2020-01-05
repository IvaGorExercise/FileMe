package com.evidencijaclanova;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.evidencijaclanova.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ClanoviListFragment extends ListFragment {

    OnHeadlineSelectedListener mCallback;
    
	ArrayList<ClanGrupa> CustomListViewValuesArr = null;
	private ListView listView;
    private int mNum = 0;
    List<ClanGrupa> items = new ArrayList<ClanGrupa>();
    List<ClanGrupa> itemsAsyn = new ArrayList<ClanGrupa>();
    private ClanoviListAdapter adapter;
    private ListView mListView;
    DataBaseHelper db;
    TextView empty;
    EditText editTxt;
    
    
    PozadinskiZadatakListaClanova asyncTaskClanLista = null;
	private WeakReference<PozadinskiZadatakListaClanova> asyncTaskWeakRef;
    
    
   
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    
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
        
   	 	//items = dohvatiClanove();
        
        //(new PozadinskiZadatak()).execute();
        
		 //startNewAsyncTask(listView);
       
    }
    

    
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.clanovi_lisview, container, false);
         
         getActivity().setTitle(R.string.fragmentPopisClanova);
         listView = (ListView) rootView.findViewById(android.R.id.list);
         
         empty = (TextView)rootView.findViewById(android.R.id.empty);
         listView.setEmptyView(empty);

         Integer Jezik = MainActivity.Engleski(getActivity());
         
         listView.setTextFilterEnabled(true);
         editTxt = (EditText) rootView.findViewById(R.id.editTxt);
         
        // editTxt.addTextChangedListener(new TextWatcher() {
             
		//		@Override
		//		public void onTextChanged(CharSequence s, int start, int before, int count) {
		//			System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
		//			if (count < before) {
		//				// We're deleting char so we need to reset the adapter data
		//				adapter.resetData();
		//			}
						
		//			adapter.getFilter().filter(s.toString());
					
		//		}
				
		//      	@Override
		//		public void beforeTextChanged(CharSequence s, int start, int count,
		//				int after) {
					
		//		}
				
			//	@Override
			//	public void afterTextChanged(Editable s) {
		//		}
		//	});
     
         
         return rootView;
         
     }
	 

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.clanovi_izbornik, menu);
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.izb_noviclan:
				FragmentManager fragmentManager = getFragmentManager();
				ClanoviFormFragment fragment = new ClanoviFormFragment();
    	 		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "ClanoviFormFragment").addToBackStack(null).commit();
				return true;
				
				
			default:
				return super.onOptionsItemSelected(item);

		}
	}
	  
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	 //adapter = new ClanoviListAdapter(getActivity(), R.layout.tabitem, items);
	 //setListAdapter(adapter);
	    
	 
	 OnItemClickListener itemClickListener = new OnItemClickListener() {
     	@Override
     	public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
     	
     		String clanId = ClanoviListAdapter.ClanId;
     		final  ClanGrupa model = ClanoviListAdapter.objectsClanGrupa.get(position);
     		Integer ClanIdInteger = model.get_id();
     	
             ClanoviDetaljFragment clandetaljFragment = new ClanoviDetaljFragment();
             Bundle args2 = new Bundle();
             args2.putInt(ClanoviDetaljFragment.ARG_POSITION, ClanIdInteger);
             clandetaljFragment.setArguments(args2);
             FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
             transaction.replace(R.id.content_frame, clandetaljFragment);
             transaction.addToBackStack(null);
             transaction.commit();

     		}        	
		};
		
		
		 startNewAsyncTaskClanoviList(listView);
		 
		 
	      editTxt.addTextChangedListener(new TextWatcher() {
	             
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
						if (count < before) {
							// We're deleting char so we need to reset the adapter data
							
							if(adapter != null)
							{
								adapter.resetData();
							}
							else
							{
								//Toast.makeText(getActivity(), "adapter null u onTextChanged ", Toast.LENGTH_SHORT).show();
							}
							
						}
						
						if(adapter != null)
						{
							adapter.getFilter().filter(s.toString());
						}
						else
						{
							//Toast.makeText(getActivity(), "adapter null u onTextChanged ", Toast.LENGTH_SHORT).show();
						}
							
					
						
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						
					}
					
					@Override
					public void afterTextChanged(Editable s) {
					}
				});
	     
		   
		// Setting the item click listener for the listview
		listView.setOnItemClickListener(itemClickListener);
   }
	
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
    
    
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        mCallback.onArticleSelected(position);
        getListView().setItemChecked(position, true);
    }
    
	
	
 List<ClanGrupa> dohvatiClanove()
	{
		ArrayList<ClanGrupa> items = new ArrayList<ClanGrupa>();
		ArrayList<ClanGrupa> clanovi = new ArrayList<ClanGrupa>();
		
		try {	 
			db.openDataBase();	 
			clanovi = db.dohvatiClanoveVise();
		}catch(SQLException sqle){	 
 		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(clanovi!=null)
		{
			for(ClanGrupa c: clanovi)
			{
				items.add(c);
			}
		}
		
		else
		{
			
		}

	return items;

	}
 
 
 private void startNewAsyncTaskClanoviList(ListView elv) {
	asyncTaskClanLista = new PozadinskiZadatakListaClanova(this, elv, getActivity());
	    this.asyncTaskWeakRef = new WeakReference<PozadinskiZadatakListaClanova >(asyncTaskClanLista);
	    asyncTaskClanLista.execute();
}

 

	private class PozadinskiZadatakListaClanova extends AsyncTask<String, Void, List<ClanGrupa> >
	{

    	Activity mContex;
    	ListView elv;
    	private WeakReference<ClanoviListFragment> fragmentWeakRef;
    	

    	public PozadinskiZadatakListaClanova(ClanoviListFragment fragment, ListView elv, Activity contex)
    	{

    	    this.mContex=contex;
    	    this.fragmentWeakRef = new WeakReference<ClanoviListFragment>(fragment);
    	    this.elv = elv;
    	}
    	
	
		private final ProgressDialog dialog = new ProgressDialog(getActivity());
		
		protected void onPreExecute() {
			this.dialog.setMessage(getResources().getString(R.string.Loading));
			//this.dialog.setMessage("Loading ClanoviListFragment...");
			this.dialog.show();
		}
		
	
		
		@Override
		protected List<ClanGrupa> doInBackground(String... params)
		{
			 List<ClanGrupa> result = new  ArrayList<ClanGrupa>();
			  
			  try {
					 result = dohvatiClanove();
					 int a = result.size();
					 Log.d(" XXX 2 doInBackground ClanoviListFragment result.size() ", Integer.toString(a));
			  	}
			  
				catch (Exception e)
				{
			    	e.printStackTrace();
			    	Log.d(" XXX Exception ClanoviListFragment er.toString(a) ", e.getMessage()); 
			    	
			    	
		   			String errorMessage =(e.getMessage()==null)?"Message is empty":e.getMessage();
		   			Log.d(" XXX Exception ClanoviListFragment er.toString(a) ", errorMessage); 
		    	
		   			Intent intent = new Intent(getActivity(), CrashActivity.class);
		   			intent.putExtra("error", e.toString());
		   			getActivity().startActivity(intent);

					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(10);
			 	}
			  
			  
				 return result;
	
		}
		

			

	   @Override
	   protected void onPostExecute(List<ClanGrupa> result) 
	   {
		   super.onPostExecute(result);
		   
		   dialog.dismiss();
		   
		   	if (this.fragmentWeakRef.get() != null)
		   	{
		   		try 
		   		{
			    	List<ClanGrupa> items = new ArrayList<ClanGrupa>();
			   		items = result;
			   	  
			   	
			   		if(items.size() > 0)
			   		{

			   			adapter = new ClanoviListAdapter(getActivity(), R.layout.tabitem, items);
			   			
			   			
			   		   if(adapter != null)
					   {
						   if(listView != null)
						   {
							   setListAdapter(adapter);
					   			dialog.dismiss();
						   } 
						   
						   else
						   {
							   Log.d(" XXX listView null ", "null");
							   Toast.makeText(getActivity(), " XXX listView null ", Toast.LENGTH_SHORT).show();
							   
							   	Intent intent = new Intent(getActivity(), CrashActivity.class);
					   			intent.putExtra("error", "XXX listView null");
					   			getActivity().startActivity(intent);

								android.os.Process.killProcess(android.os.Process.myPid());
								System.exit(10);
						   }
					   }
					   
					   else
					   {
						   Log.d(" XXX adapter null ", "null");
						   Toast.makeText(getActivity(), " XXX adapter null ", Toast.LENGTH_SHORT).show();
						   asyncTaskClanLista.cancel(true);
					   }
			   			
			   			
					   
			   		}
			   		
			   		else
			   		{
			   			empty.setText(R.string.NemaClanova);
			   		}
				
		   		}
		  
		   		catch (Exception e)
		   		{
		   			e.printStackTrace();
		    	
		   			String errorMessage =(e.getMessage()==null)?"Message is empty":e.getMessage();
		   			Log.d(" XXX onPostExecute ClanoviListFragment er.toString(a) ", errorMessage);
		   			asyncTaskClanLista.cancel(true);
		    	
		   			Intent intent = new Intent(getActivity(), CrashActivity.class);
		   			intent.putExtra("error", e.toString());
		   			getActivity().startActivity(intent);

					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(10);
		    			    
		   		}
			   
       
		   }
	   }
	   
	   	  
	}
	
 
    
}
