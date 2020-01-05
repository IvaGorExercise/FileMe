package com.evidencijaclanova;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.evidencijaclanova.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiConfiguration.Status;
import android.os.AsyncTask;
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
import android.widget.Toast;

public class ClanoviGrupeClanarinaFragment extends Fragment {
	
	
	ArrayList<Grupa> CustomListViewValuesArr = null;
	private ListView listView;
    private int mNum = 0;
    List<Grupa> items = new ArrayList<Grupa>();
    private ListView mListView;
    DataBaseHelper db;
	String VrstaEvidencije = MainActivity.VrstaEvidencije;
	Button MjesecGodina;
	public ExpandableListView exList;
	ClanoviGrupeClanarinaAdapter exAdpt;
	
	private int gGodina;
	private int gMjesec;
	private int gDan;
	
	private TextView actualDate;
	private DatePicker datePicker;
	private Button changeDate;
	
	static final int MJESEC_GODINA_DIALOG_ID = 3;
	
	private WeakReference<PozadinskiZadatak> asyncTaskWeakRef;
	PozadinskiZadatak asyncTask = null;
	
    
	  @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        
	        setHasOptionsMenu(false);
	        setRetainInstance(true);
	        
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

	   	 	//items = dohvatiGrupe();
	        
	        VrstaEvidencije = MainActivity.VrstaEvidencije;
	        
	        //exAdpt = new ClanoviGrupeClanarinaAdapter(items, getActivity(), db, VrstaEvidencije, gDan, gGodina, gMjesec);
	        
	        //(new PozadinskiZadatak(VrstaEvidencije, gDan, gGodina, gMjesec)).execute();
	        
	        //startNewAsyncTask(exList, VrstaEvidencije, gDan, gGodina, gMjesec);
	        
	        Log.d(" 1 VrstaEvidencije ", VrstaEvidencije);
	        Log.d("1 gDan ", Integer.toString(gDan));
	        Log.d("1 gGodina ", Integer.toString(gGodina));
	        Log.d("1 gMjesec ", Integer.toString(gMjesec));
	        
	        
		   //Toast.makeText(getActivity(), " Fragment onCreate ", Toast.LENGTH_SHORT).show();
	   	 	
	    }
	    

	    
		 @Override
	     public View onCreateView(LayoutInflater inflater, ViewGroup container,
	             Bundle savedInstanceState) {
			 
		   //(new PozadinskiZadatak(getActivity(),VrstaEvidencije, gDan, gGodina, gMjesec)).execute();
		   
		    Log.d(" 1 VrstaEvidencije ", VrstaEvidencije);
	        Log.d("1 gDan ", Integer.toString(gDan));
	        Log.d("1 gGodina ", Integer.toString(gGodina));
	        Log.d("1 gMjesec ", Integer.toString(gMjesec));
	        
	        ClanoviGrupeClanarinaAdapter.privremeniDatumiUplate.clear();
		    ClanoviGrupeClanarinaAdapter.privremeniDatumiVrijediDo.clear();
		    ClanoviGrupeClanarinaAdapter.privremeniIznos.clear();
		        
			 
		     getActivity().setTitle(R.string.fragmentClanarina);
	         View rootView = inflater.inflate(R.layout.clanovi_grupe_clanarina_expandable_layout, container, false);
	         
	         MjesecGodina = (Button)rootView.findViewById(R.id.btnMjesecGodina);
	         
	         Integer Jezik = MainActivity.Engleski(getActivity());
	         
	         if(Jezik == 1)
	         {
	        	   MjesecGodina.setText(getResources().getString(R.string.clanarinaZa) + " " + DatumHelper.DodajVodeceNule(gMjesec) +  "/" + Integer.toString(gGodina));
	         }
	         else
	         {
	        	   MjesecGodina.setText(getResources().getString(R.string.clanarinaZa) + " " + DatumHelper.DodajVodeceNule(gMjesec) +  ". " + Integer.toString(gGodina) +  ". ");
	         }
	      
	         
	         MjesecGodina.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					ClanoviGrupeClanarinaAdapter.privremeniDatumiUplate.clear();
				    ClanoviGrupeClanarinaAdapter.privremeniDatumiVrijediDo.clear();
				    ClanoviGrupeClanarinaAdapter.privremeniIznos.clear();
				         
					DatePickerDialog dialog = new DatePickerDialog(getActivity(), listenerZaDatuma, gGodina, gMjesec-1, gDan);

					 try{
						    Field[] datePickerDialogFields = dialog.getClass().getDeclaredFields();
						    for (Field datePickerDialogField : datePickerDialogFields) { 
						        if (datePickerDialogField.getName().equals("mDatePicker")) {
						            datePickerDialogField.setAccessible(true);
						            DatePicker datePicker = (DatePicker) datePickerDialogField.get(dialog);
						            Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
						            for (Field datePickerField : datePickerFields) {
						               if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField.getName())) {
						                  datePickerField.setAccessible(true);
						                  Object dayPicker = new Object();
						                  dayPicker = datePickerField.get(datePicker);
						                  ((View) dayPicker).setVisibility(View.GONE);
						               }
						            }
						         }

						      }
						    }catch(Exception ex){
						    }
					
					dialog.show();
				
				}
				
			});
	         

	         exList = (ExpandableListView) rootView.findViewById(R.id.expandableListViewGrupa);
	         exList.setIndicatorBounds(5, 5);
	         //VrstaEvidencije = MainActivity.VrstaEvidencije;
	         //exAdpt = new ClanoviGrupeClanarinaAdapter(items, getActivity(), db, VrstaEvidencije, gDan, gGodina, gMjesec);
	         exList.setIndicatorBounds(0, 20);
	         //exList.setAdapter(exAdpt);
	         
	     	 Resources res = this.getResources();
		     Drawable devider = res.getDrawable(R.drawable.line);
			
	         exList.setGroupIndicator(null);
	         exList.setDivider(devider);
	         exList.setChildDivider(devider);
	         exList.setDividerHeight(1);
	         
	         
	         //startNewAsyncTask(exList, VrstaEvidencije, gDan, gGodina, gMjesec);
	         
	         //Toast.makeText(getActivity(), " Fragment onCreateView ", Toast.LENGTH_SHORT).show();
	         
	         return rootView;

	     }
		
		 
		 
		 
   @Override
	 public void onAttach(Activity activity) {
	           super.onAttach(activity);
	    	   //Toast.makeText(getActivity(), " Fragment onAttach ", Toast.LENGTH_SHORT).show();
	 }
   
	
   
   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
       //Toast.makeText(getActivity(), " Fragment onActivityCreated ", Toast.LENGTH_SHORT).show();
       startNewAsyncTask(exList, VrstaEvidencije, gDan, gGodina, gMjesec);
   }
		 
		 
		 
   private void startNewAsyncTask(ExpandableListView elv, String VrstaEvidencije, int gDan, int gGodina, int gMjesec) {
			      
			asyncTask = new PozadinskiZadatak(this, elv, getActivity(), VrstaEvidencije,  gDan,  gGodina,  gMjesec);
			this.asyncTaskWeakRef = new WeakReference<PozadinskiZadatak >(asyncTask); 
			asyncTask.execute();
			   	
		}

		
	 //private boolean isAsyncTaskPendingOrRunning() {
	//		    return this.asyncTaskWeakRef != null &&
	//		          this.asyncTaskWeakRef.get() != null &&
	//		          !this.asyncTaskWeakRef.get().getStatus().equals(Status.FINISHED);
	// }
	 
		 
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
			   		
			   		int count =  exAdpt.getGroupCount();
			   		for (int i = 0; i <count ; i++)
			   		{
			   			if (exList != null) {
			   		
			   				if (exList.isGroupExpanded(i))
			   				{
			   					exList.collapseGroup(i);
			   				} 
			   		    } 
			   		}
			   		
			   	   Integer Jezik = MainActivity.Engleski(getActivity());
			   	   
			   	   //Toast.makeText(getActivity(), "onDateSet " + Integer.toString(Jezik), Toast.LENGTH_SHORT).show();
			         
			         if(Jezik == 1)
			         {
			        	 MjesecGodina.setText(getResources().getString(R.string.clanarinaZa) + " " + DatumHelper.DodajVodeceNule(monthOfYear + 1) +  "/" + Integer.toString(year));
			         }
			         else
			         {
			        	MjesecGodina.setText(getResources().getString(R.string.clanarinaZa) + " " + DatumHelper.DodajVodeceNule(monthOfYear + 1) +  ". " + Integer.toString(year) + ". ");
			         }
			         
			   		
			   		//items = dohvatiGrupe();
			   	
			   		//exAdpt = new ClanoviGrupeClanarinaAdapter(items, getActivity(), db, VrstaEvidencije, dayOfMonth, year, monthOfYear + 1);
			   		////exAdpt.notifyDataSetChanged();
			   	    //exList.setAdapter(exAdpt);
			   	    
			   	   //(new PozadinskiZadatak(getActivity(), VrstaEvidencije, dayOfMonth, year, monthOfYear + 1)).execute();
			         
			         startNewAsyncTask(exList, VrstaEvidencije, dayOfMonth, year, monthOfYear + 1);
			
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
				Log.d("OD dohvatiGrupe ", "frgment funkcija dohvatiGrupe()");
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

	public List<Clan> dohvatiClanovePoGrupama2(Integer idGrupa)
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
	
	
	public List<Clan> dohvatiClanovePoGrupama(Integer idGrupa)
	{
		ArrayList<Clan> items = new ArrayList<Clan>();
		ArrayList<Clan> clan = new ArrayList<Clan>();
		
		try {	 
			db.openDataBase();	 
			clan = db.dohvatiClanovePoGrupama(idGrupa);
			Log.d("OD dohvatiClanovePoGrupama ", "frgment dohvatiClanovePoGrupama");
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
				Clanarina clanarinaDetalj = db.ClanarinaZaMjesec2(idGrupa, c.get_id(), gGodina, gMjesec);
				Log.d("ClanarinaZaMjesec2 ", "frgment ClanarinaZaMjesec2");
				if(clanarinaDetalj != null)
				{
					if(clanarinaDetalj.get_id() > 0)
					{
						c.setSelectedPlacen(true);
						c.set_datumUplata(clanarinaDetalj.get_datumUplata());
						c.set_datumVrijediDo(clanarinaDetalj.get_datumVrijediDo());
						c.set_iznos(clanarinaDetalj.get_iznos());
					}
					else
					{
						c.setSelectedPlacen(false);
					}
				}
				
		
				items.add(c);
				
				db.close();
			}
		}
		
		else
		{
			
		}

	return items;

	}
	
	private class PozadinskiZadatak extends AsyncTask<String, Void, List<Grupa>>
	{
		private final String VE;
		private final int D;
    	private final int M;
    	private final int G;
    	Activity mContex;
    	ExpandableListView elv;
    	
    	private WeakReference<ClanoviGrupeClanarinaFragment> fragmentWeakRef;
    	

    	public PozadinskiZadatak(ClanoviGrupeClanarinaFragment fragment, ExpandableListView elv, Activity contex, String VE, int D, int G, int M)
    	{
    		this.VE = VE;
    		this.D = D;
    		this.M = M;
    		this.G = G;
    	    this.mContex=contex;
    	    this.fragmentWeakRef = new WeakReference<ClanoviGrupeClanarinaFragment>(fragment);
    	    this.elv = elv;
    	}
    	
		private final ProgressDialog dialog = new ProgressDialog(getActivity());
		
		protected void onPreExecute() {
			this.dialog.setMessage(getResources().getString(R.string.Loading));
			//this.dialog.setMessage("Loading clanoviGrupeClanrina...");
			this.dialog.show();
		 }
		
		
		
		@Override
		protected List<Grupa> doInBackground(String... params)
		{
			  List<Grupa> result = new  ArrayList<Grupa>();
			  
			  try {
					 result = dohvatiGrupe();
					 int a = result.size();
					 Log.d(" XXX doInBackground result.size() ", Integer.toString(a));
			  	}
			  
				catch (Exception e)
				{
			    	e.printStackTrace();
			    	Log.d(" XXX Exception er.toString(a) ", e.getMessage());
			    	
			    	String errorMessage =(e.getMessage()==null)?"Message is empty":e.getMessage();
			    	
			       	Intent intent = new Intent(getActivity(), CrashActivity.class);
		   			intent.putExtra("error", errorMessage);
		   			getActivity().startActivity(intent);

					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(10);
			 	}
			  
			
			   
			   if(result.size() == 0)
			   {
				  dialog.dismiss();
			   }
			   
				 return result;
	
		}
		

			
	   @Override
	   protected void onPostExecute(List<Grupa> result) 
	   {
		   super.onPostExecute(result);
		   
		   dialog.dismiss();
		   
		   	if (this.fragmentWeakRef.get() != null)
		   	{
		   		try 
		   		{
			    	List<Grupa> items = new ArrayList<Grupa>();
			   		items = result;
			   		
			   		if(items.size() > 0)
			   		{
			   		   exAdpt = new ClanoviGrupeClanarinaAdapter(items, mContex, db, VE, D, G, M);
					   Log.d(" XXX Novi adapter ", exAdpt.db.toString()); 
					   
					   
					   if(exAdpt != null)
					   {
						   if( elv != null)
						   {
							   elv.setAdapter(exAdpt);
							   dialog.dismiss();
						   } 
						   
						   else
						   {
							   	Log.d(" XXX elv null ", "null");
							   	Toast.makeText(getActivity(), " XXX elv null ", Toast.LENGTH_SHORT).show();
							   	
							   	dialog.dismiss();
							   	
							   	Intent intent = new Intent(getActivity(), CrashActivity.class);
					   			intent.putExtra("error", "XXX elv null");
					   			getActivity().startActivity(intent);

								android.os.Process.killProcess(android.os.Process.myPid());
								System.exit(10);
						
						   }
					   }
					   
					   else
					   {
						   Log.d(" XXX exAdpt null ", "null");
						   Toast.makeText(getActivity(), " XXX exAdpt null ", Toast.LENGTH_SHORT).show();
						   asyncTask.cancel(true);
					   }
					
			   		}
			   		
				
		   		}
		  
		   		catch (Exception e)
		   		{
		   			e.printStackTrace();
		    	
		   			String errorMessage =(e.getMessage()==null)?"Message is empty":e.getMessage();
		   			Log.d(" XXX onPostExecute ClanoviGrupeClanarinaFragment e ", errorMessage);
		   		 	asyncTask.cancel(true);
		    	
		   			Intent intent = new Intent(getActivity(), CrashActivity.class);
		   			intent.putExtra("error", e.toString());
		   			getActivity().startActivity(intent);

					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(10);
		    			    
		   		}
			   
       
		   }
		   	
		   	else
		   	{
		   		asyncTask.cancel(true);
		   	}
		   
	
		}
	   
	   	  
	}

}
