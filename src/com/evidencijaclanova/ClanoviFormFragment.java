package com.evidencijaclanova;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.evidencijaclanova.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClanoviFormFragment extends Fragment implements OnClickListener{

    public static final String ARG_PLANET_NUMBER = "planet_number";
	final static String CLAN_ID = "Clan_ID";
	int mCurrentPosition = -1;
	int clanId = -1;
    
    View rootView;
 	ImageView viewImage;
 	Button selectPhoto;
    Button setDateOfBirth;
    Button unesiClana;
    Button dohvatiClanove;
	DataBaseHelper db;
	EditText etClanIme;
	EditText etClanPrezime;
	EditText etClanTelefon;
	EditText etClanEmail;
	EditText etClanAdresa;
	EditText etClanOpis;
	TextView txtUpisaniClan;
	EditText etIzabraniDatum;
	TextView twClanEmail;
	TextView txtPopisGrupaZaClana;
	Button btnDohvatiGrupe;
	protected Button selectColoursButton;
	TextView txtOdabrani;
	
 	private int mGodina;
 	private int mMjesec;
 	private int mDan;
 	public String datumRodjenja;
 	private final static int ID_IZBORNIK_DATUMA = 0;
 	int brojGrupa = 0;
 	
 	protected CharSequence[] colours = { "Red", "Green", "Blue", "Yellow", "Orange", "Purple" };
	protected ArrayList<String> selectedColours = new ArrayList<String>();
	
 	public List<Grupa> listaGrupa;
 	public List<Kategorija> listaKategorija;

	protected ArrayList<String> selectedColours2 = new ArrayList<String>();
	
	public ArrayList<Integer> izAdaptera;
	public static ArrayList<Integer> selektiraneGrupe = new ArrayList<Integer>();
	public static ArrayList<Integer> obrisaneGrupe = new ArrayList<Integer>();
	public static ArrayList<Integer> grupeZaClanaZaProvjeruSpremanja = new ArrayList<Integer>();
	
	 
	final TextView[] tv=new TextView[100];
	final EditText[] et=new EditText[100];
	TextView tv2;
	EditText et2;
	List<TextView> alltextViews= new ArrayList<TextView>();
	List<EditText> allEds = new ArrayList<EditText>();
	 
	public ArrayList<KategorijaPolja> kategorijaPoljaLista = new  ArrayList<KategorijaPolja>();
	
	
	Clan ClanDetalj = null;
	List<Grupa> ClanGrupe = new  ArrayList<Grupa>();
	List<ClanKategorija> ClanKategorija = new  ArrayList<ClanKategorija>();
	TextView PopisGrup;
	

 	
    public ClanoviFormFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
 		db  = new DataBaseHelper(getActivity());
 		
 	   
 		Bundle args = getArguments();
        if (args != null)
        {
        	clanId = args.getInt(CLAN_ID);
        }
        
        else
        {
        	clanId = -1;
        }
 		
 		 listaGrupa = new ArrayList<Grupa>();
         listaGrupa = dohvatiGrupLista();
         
         listaKategorija = new ArrayList<Kategorija>();
         listaKategorija = dohvatiKategorijaLista();
         
         MainActivity.ClanPromijenjenActivity = 0;
         grupeZaClanaZaProvjeruSpremanja.clear();
         
         if(clanId  > 0)
         {
        	 ClanDetalj = ClanDetalj(clanId);
        	 
        	 ClanGrupe = ClanGrupeDetalj(clanId);
             ClanKategorija = ClanKategorijaDetalj(clanId);
         }
         
         
    }
    
    public final static boolean isValidEmail(CharSequence target) {

    	 if (target == null) {
    	     return false;
    	 } else {
    	     return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    	 }
    	}
    
    
    
    @Override  
    public void onPause() {
    	
		  super.onPause();
		  
	   }
    

	public int ProvjeraKorisnika()
	{
		int rezult = 0;
		
		if(ClanDetalj != null){
			rezult = 1;
		}
		else
		{
			rezult = 0;
		}
		
		return rezult;
	}

	  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
    	
    	
        getActivity().setTitle(R.string.fragmentUnosClanova);
		rootView = inflater.inflate(R.layout.clan_form, container, false);
		
 		if(savedInstanceState != null)
 		{
 	
 	    }
 		
 		
	   selectPhoto=(Button)rootView.findViewById(R.id.btnSelectPhoto);
	   selectPhoto.setOnClickListener(this);
	   etClanEmail = (EditText) rootView.findViewById(R.id.etClanEmail);
	   twClanEmail = (TextView) rootView.findViewById(R.id.twClanEmail);
	   
		//etClanEmail.addTextChangedListener(new TextWatcher() { 
		//        public void afterTextChanged(Editable s) { 
		//            if (isValidEmail(etClanEmail.getText().toString()))
		//           {
		//            	 twClanEmail.setText(R.string.twTxtClanEmail);
		//            }
		 //           else
		 //           {
		 //           	 twClanEmail.setText("email mora sadržavati @ i domenu!");
		 //           }
		 //       } 
		 //       public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
		 //       public void onTextChanged(CharSequence s, int start, int before, int count) {} 
		 //   }); 
	    
	   //setDateOfBirth=(Button)rootView.findViewById(R.id.btnSetDate);
	   //setDateOfBirth.setOnClickListener(this);
	   
	   
	   
	   etIzabraniDatum=(EditText)rootView.findViewById(R.id.etIzabraniDatum);
	   etIzabraniDatum.setOnClickListener(this);
	   
	   unesiClana=(Button)rootView.findViewById(R.id.btnUnesiClana);
	   unesiClana.setOnClickListener(this);
	  
	   txtOdabrani  =(TextView)rootView.findViewById(R.id.txtOdabrani);
	    
       viewImage=(ImageView)rootView.findViewById(R.id.viewImage);
    			
       if (ClanDetalj != null)
       {
 		
    	   	String mojegrupe = "";
    	   	String mojegrupeId = "";
    	   	
    	   	
    	   	etClanIme = (EditText) rootView.findViewById(R.id.etClanIme);
   			etClanPrezime = (EditText) rootView.findViewById(R.id.etClanPrezime);
   			etClanTelefon = (EditText) rootView.findViewById(R.id.etClanTelefon);
   			etClanEmail = (EditText) rootView.findViewById(R.id.etClanEmail);
   			etClanAdresa = (EditText) rootView.findViewById(R.id.etClanAdresa);
   			etClanOpis = (EditText) rootView.findViewById(R.id.etClanOpis);
   			RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.rgSpol);
   			etIzabraniDatum = (EditText)rootView.findViewById(R.id.etIzabraniDatum);
   			TextView txtPopisGrupaZaClana = (TextView)rootView.findViewById(R.id.txtPopisGrupaZaClana);
   			TextView txtPopisGrupaZaClanaId = (TextView)rootView.findViewById(R.id.txtOdabrani);
   			ImageView itemImage = (ImageView) rootView.findViewById(R.id.viewImage);
   			
   			etClanIme.setText(ClanDetalj.get_ime());
   			etClanPrezime.setText(ClanDetalj.get_prezime());
   			etClanTelefon.setText(ClanDetalj.get_telefon());
   			etClanEmail.setText(ClanDetalj.get_email());
   			etClanAdresa.setText(ClanDetalj.get_adresa());
   			etClanOpis.setText(ClanDetalj.get_opis());
   			
   			
   		   	etClanIme.addTextChangedListener(new TextWatcher() { 
		        public void afterTextChanged(Editable s)
		        { 
		        	MainActivity.ClanPromijenjenActivity = 1;
		        } 
		        
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
		        public void onTextChanged(CharSequence s, int start, int before, int count) {} 
		    });
   		   	
   		   	etClanPrezime.addTextChangedListener(new TextWatcher() { 
		        public void afterTextChanged(Editable s)
		        { 
		        	MainActivity.ClanPromijenjenActivity = 1;
		        } 
		        
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
		        public void onTextChanged(CharSequence s, int start, int before, int count) {} 
		    });
   		   	
   		   	etClanTelefon.addTextChangedListener(new TextWatcher() { 
		        public void afterTextChanged(Editable s)
		        { 
		        	MainActivity.ClanPromijenjenActivity = 1;
		        } 
		        
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
		        public void onTextChanged(CharSequence s, int start, int before, int count) {} 
		    });
   		 
   		   	etClanEmail.addTextChangedListener(new TextWatcher() { 
		        public void afterTextChanged(Editable s)
		        { 
		        	MainActivity.ClanPromijenjenActivity = 1;
		        } 
		        
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
		        public void onTextChanged(CharSequence s, int start, int before, int count) {} 
		    });
   		
   		   	etClanAdresa.addTextChangedListener(new TextWatcher() { 
		        public void afterTextChanged(Editable s)
		        { 
		        	MainActivity.ClanPromijenjenActivity = 1;
		        } 
		        
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
		        public void onTextChanged(CharSequence s, int start, int before, int count) {} 
		    });
   		
   		   	etClanOpis.addTextChangedListener(new TextWatcher() { 
		        public void afterTextChanged(Editable s)
		        { 
		        	MainActivity.ClanPromijenjenActivity = 1;
		        } 
		        
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
		        public void onTextChanged(CharSequence s, int start, int before, int count) {} 
		    });
   		
   		

   			
   			if(ClanDetalj.get_datumRodjenja() != null)
   			{
   				String regex = "[0-9]+"; 
   				String data = ClanDetalj.get_datumRodjenja();
   				
   				if(data.matches(regex))
   				{
   					String DatumFormatiran = FormatirajDatum(ClanDetalj.get_datumRodjenja());
   	   				etIzabraniDatum.setText(DatumFormatiran);
   	   				
   	   				etIzabraniDatum.addTextChangedListener(new TextWatcher() { 
   			        public void afterTextChanged(Editable s)
   			        { 
   			        	MainActivity.ClanPromijenjenActivity = 1;
   			        } 
   			        
   			        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
   			        public void onTextChanged(CharSequence s, int start, int before, int count) {} 
   	   				});
   	   		  	
   	   				datumRodjenja= ClanDetalj.get_datumRodjenja();

   				}
   			
   			}
   			
   			if(ClanDetalj.get_slika() != null)
   	    	{
   	    		Bitmap slikaZaPrikaz = getImage(ClanDetalj.get_slika());
   	    		itemImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(slikaZaPrikaz,10));
   	    		
   	    		LinearLayout.LayoutParams parematerSlike = new LinearLayout.LayoutParams(80, 80);
   	    		parematerSlike.width = 80;
   	    		parematerSlike.height = 80;
   	    		parematerSlike.gravity = Gravity.CENTER;
   	    		itemImage.setLayoutParams(parematerSlike);
	
   	    	}
   	    	
   	    	else
   	    	{
   	    		//itemImage.setImageResource(R.drawable.friendcaster);
   	    	}
   			
   			
   			if(ClanDetalj.get_spol() == 1)
   			{
   				rg.check(R.id.rbZena);
   			}
   			
   			else if(ClanDetalj.get_spol() == 2)
   			{
   				rg.check(R.id.rbMusko);
   			}
   			
   			rg.setOnCheckedChangeListener(new OnCheckedChangeListener() 
   			{
   				@Override
   				public void onCheckedChanged(RadioGroup group, int checkedId) {

   					MainActivity.ClanPromijenjenActivity = 1;
   	        }
   	    });
   			
   		
   			
   			
   			
   			if(ClanGrupe != null)
      		{
   				if(ClanGrupe.size() > 0)
   	      		{
   				  					
   					for(Grupa item: ClanGrupe)
   					{
   						grupeZaClanaZaProvjeruSpremanja.add(item.get_idGrupa());
   						
   						mojegrupe = mojegrupe + item.get_ime()+ ", ";
   						mojegrupeId = mojegrupeId + item.get_idGrupa()+ ", ";
   					}
      	
   					mojegrupe = mojegrupe.substring(0, mojegrupe.length()-2);
   					mojegrupeId = mojegrupeId.substring(0, mojegrupeId.length()-2);
   					txtPopisGrupaZaClana.setText(mojegrupe);
   					txtPopisGrupaZaClana.setVisibility(View.INVISIBLE);
   					txtPopisGrupaZaClanaId.setText(mojegrupeId);
   					txtPopisGrupaZaClanaId.setVisibility(View.INVISIBLE);

   	      		}
      		}		
       }

       if (listaGrupa != null)
       {
    	   LinearLayout l = (LinearLayout)rootView.findViewById(R.id.ClanFormLayoutZaGrupe);
    	   
    	   if (listaGrupa.size() > 0)
           {
    		    l = (LinearLayout)rootView.findViewById(R.id.ClanFormLayoutZaGrupe);
        	   btnDohvatiGrupe = new Button(getActivity());
        	   btnDohvatiGrupe.setBackgroundResource(R.drawable.button_orange);
        	   btnDohvatiGrupe.setTextSize(20);
        	   btnDohvatiGrupe.setTextColor(getResources().getColor(R.color.blue01));
        	   btnDohvatiGrupe.setTypeface(null, Typeface.BOLD);
        	   btnDohvatiGrupe.setText(R.string.izaberite);
        	   btnDohvatiGrupe.setOnClickListener(new OnClickListener() {
        	        public void onClick(View v) {
        	        	showSelectColoursDialog();
        	        }
        	    });
        	   //btnDohvatiGrupe.setOnClickListener(this);
        	   l.addView(btnDohvatiGrupe);
           }
       }
       
       if (listaKategorija != null)
       {
    	   if (listaKategorija.size() > 0)
           {
    		   LinearLayout l = (LinearLayout)rootView.findViewById(R.id.ClanFormLayoutZaKriterij);
    		   for(Kategorija id: listaKategorija)
   		    	{
    			   tv2 =new TextView(getActivity());
    			   alltextViews.add(tv2);
    			   tv2.setId(id.get_id());
    			   tv2.setTextColor(getResources().getColor(R.color.blue01));
    			   //tv2.setText(id.get_ime() + ":");
    			   tv2.setText(id.get_ime().substring(0, 1).toUpperCase() + id.get_ime().substring(1, id.get_ime().length())+":");
    			   et2=new EditText(getActivity());
    			   allEds.add(et2);
    			   et2.setId(id.get_id());
    			   et2.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    			   
    			   if (ClanKategorija != null)
                   {
    				   
                	   if (ClanKategorija.size() > 0)
                       {
                		   for(ClanKategorija ClanKategorijaElement: ClanKategorija)
               		    	{
                			   if(ClanKategorijaElement.get_idKategorija() == id.get_id() )
                			   {
                				   et2.setText(ClanKategorijaElement.get_vrijednost());
                				   
                				   et2.addTextChangedListener(new TextWatcher() { 
                				        public void afterTextChanged(Editable s)
                				        { 
                				        	MainActivity.ClanPromijenjenActivity = 1;
                				        } 
                				        
                				        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
                				        public void onTextChanged(CharSequence s, int start, int before, int count) {} 
                				    });
                				   
                			   }
               		    	}
                       }
                	   
                   }
    	
            	   l.addView(tv2);
            	   l.addView(et2);
   		    	}

           }

       }
       
        return rootView;
    }
    
	  public static Bitmap getImage(byte[] image) {
	        return BitmapFactory.decodeByteArray(image, 0, image.length);
	    }

    public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    ArrayAdapter<Grupa> adapter = new ClanoviFormAdapter(getActivity(), listaGrupa, ClanGrupe);
	    setListAdapter(adapter);
   }
    
    private void setListAdapter(ArrayAdapter<Grupa> adapter) {
		// TODO Auto-generated method stub
		
	}
   
    private DatePickerDialog.OnDateSetListener listenerZaDatuma = new DatePickerDialog.OnDateSetListener()
    {
 	   	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
 	   	{
 	   		mGodina = year;
 	   		mMjesec = monthOfYear + 1;
 	   		mDan = dayOfMonth;
 	   		ispisDatuma();
 	
 		}
   };
   
	private void ispisDatuma()
   	{ 
 		String sDan;
 		String sMjesec;
 		String sGodin = Integer.toString(mGodina);
 		
		etIzabraniDatum =  (EditText)rootView.findViewById(R.id.etIzabraniDatum);
		
		if (mDan < 10)
		{
			sDan = "0" + Integer.toString(mDan);
		}
		
		else
		{
			sDan = Integer.toString(mDan);
		}
		
		if (mMjesec < 10)
		{
			sMjesec = "0" + Integer.toString(mMjesec);
		}
		
		else
		{
			sMjesec = Integer.toString(mMjesec);
		}
		
		Integer Jezik = MainActivity.Engleski(getActivity());
		
		if(Jezik == 1)
		{
			etIzabraniDatum.setText(new StringBuilder().append(sDan).append("/").append(sMjesec).append("/").append(mGodina));
			
		}
		else
		{
			etIzabraniDatum.setText(new StringBuilder().append(sDan).append(".").append(sMjesec).append(".").append(mGodina).append("."));
		}
		
 		
 		datumRodjenja = sGodin  + sMjesec +  sDan;
 		
   	}
	
	private String FormatirajDatum(String Datum)
   	{ 
		String Rezult = "";
 		String sDan = Datum.substring(6, 8);
 		String sMjesec = Datum.substring(4, 6);
 		String sGodina = Datum.substring(0, 4);
 		
 		
 		Integer Dan = Integer.parseInt(sDan);
 		Integer Mjesec  = Integer.parseInt(sMjesec);
 		Integer Godina = Integer.parseInt(sGodina);
 		
 	
		if (Dan < 10)
		{
			sDan = "0" + Integer.toString(Dan);
		}
		
		else
		{
			sDan = Integer.toString(Dan);
		}
		
		if (Mjesec < 10)
		{
			sMjesec = "0" + Integer.toString(Mjesec);
		}
		
		else
		{
			sMjesec = Integer.toString(Mjesec);
		}
		
		
		Integer Jezik = MainActivity.Engleski(getActivity());
		
		if(Jezik == 1)
		{
			Rezult = (new StringBuilder().append(sDan).append("/").append(sMjesec).append("/").append(sGodina)).toString();
		}
		
		else
		{
			Rezult = (new StringBuilder().append(sDan).append(".").append(sMjesec).append(".").append(sGodina).append(".")).toString();
		}
		
 		
 		return Rezult;
   	}


	public Dialog onCreateDialog(int id) {

		switch(id)
    	{
    		case ID_IZBORNIK_DATUMA:
    			Calendar cMax = Calendar.getInstance();
    			
    			DatePickerDialog dialog = new DatePickerDialog(getActivity(), listenerZaDatuma, cMax.get(Calendar.YEAR), cMax.get(Calendar.MONTH), cMax.get(Calendar.DAY_OF_MONTH));
    			Calendar cMin = Calendar.getInstance();
    			cMin.set(cMin.DAY_OF_MONTH, 1);
    			cMin.set(cMin.MONTH, 0 );
    			cMin.set(cMin.YEAR, 1930);
    			
    			
        		dialog.getDatePicker().setMinDate(cMin.getTimeInMillis());
        		dialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
        		
        		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
        				getResources().getString(R.string.Odustani),
        	                new DialogInterface.OnClickListener()
        				{
        	                    public void onClick(DialogInterface dialog, int which)
        	                    {
        	                        if (which == DialogInterface.BUTTON_NEGATIVE)
        	                        {
        	                            dialog.cancel();
   
        	                    }
        	                    }
        	                });
        	
        		
        		 
    			dialog.show();
    	}
    	return null;
	}
	

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
    	{
    		case R.id.btnSelectPhoto:
    			SelectImage();
    			break;
    		case R.id.etIzabraniDatum:
    			onCreateDialog(ID_IZBORNIK_DATUMA);
    			break;
    		case R.id.btnUnesiClana:
    			  if (validationSuccess())
    			  {
    				  upisiClana(v);
    				  MainActivity.ClanPromijenjenActivity = 0;
    			  }
    			
    			
    			break;

    	}
			  
	}
	
	 private Boolean validationSuccess()
	 {
		 	etClanIme = (EditText) rootView.findViewById(R.id.etClanIme);
			etClanPrezime = (EditText) rootView.findViewById(R.id.etClanPrezime);
			etClanEmail = (EditText) rootView.findViewById(R.id.etClanEmail);
			String poruka = getResources().getString(R.string.validaciaUnesite) + " ";
			String porukEmaila = "";
			
			if(etClanIme.getText().toString().equalsIgnoreCase("") && !etClanPrezime.getText().toString().equalsIgnoreCase("") )
			{
				poruka = poruka + getResources().getString(R.string.validacijaIme);
			}	
			
			if (etClanPrezime.getText().toString().equalsIgnoreCase("") && !etClanIme.getText().toString().equalsIgnoreCase(""))
			{
				poruka = poruka +  getResources().getString(R.string.validacijaPrezime);
			}
			
			if (etClanIme.getText().toString().equalsIgnoreCase("") && etClanPrezime.getText().toString().equalsIgnoreCase(""))
			{
				poruka = poruka + getResources().getString(R.string.validacijaImeIPrezime);
			}
			
			
			if(!etClanEmail.getText().toString().equalsIgnoreCase("") )
			{
				if(!isValidEmail(etClanEmail.getText().toString()))
				{
					porukEmaila =  getResources().getString(R.string.validacijaEmail);
				}
			}
			
			
		    if(etClanIme.getText().toString().equalsIgnoreCase("") || etClanPrezime.getText().toString().equalsIgnoreCase("") )
		    {
		    	if (!porukEmaila.equalsIgnoreCase(""))
		    	{
		    		poruka = poruka + " " + porukEmaila;
		    	}
		    	Toast.makeText(getActivity(), poruka, 0).show();
		      return false;
		    }
		    
		    else if(!porukEmaila.equalsIgnoreCase(""))
		    {
		    	Toast.makeText(getActivity(), porukEmaila, 0).show();
		    	  return false;
		    }

		    return true;
	 }
	
	private void SelectImage() {

			final String Snimi =  getResources().getString(R.string.Snimi);
			final String SlikaIzAlbuma =  getResources().getString(R.string.SlikaIzAlbuma);
			final String Odustani =  getResources().getString(R.string.Odustani);
		
	        final CharSequence[] options = { Snimi, SlikaIzAlbuma, Odustani};

	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(getResources().getString(R.string.dodajSliku));
	        builder.setItems(options, new DialogInterface.OnClickListener() {

	            @Override

	            public void onClick(DialogInterface dialog, int item) {

	                if (item == 0)
	                {
	                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
	                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	                    startActivityForResult(intent, 1);
	                }

	                else if (item == 1)
	                {
	                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	                    startActivityForResult(intent, 2);
	                }

	                else if (item == 2) {

	                    dialog.dismiss();

	                }

	            }

	        });

	        builder.show();

	   }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //if (resultCode == RESULT_OK) {

            if (requestCode == 1)
            {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }

                try {

                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    
                    // ovo dole testno za funkciju ShrinkBitmap zakometirano
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions);
                    bitmap =scaleDownLargeImageWithAspectRatio(bitmap, 80, 80);
                    //bitmap = ShrinkBitmap(f.getAbsolutePath(), 80, 80);
              
                    try {
                        ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }
                    catch (Exception e) {

                    }
                    
                    viewImage.setImageBitmap(bitmap);
                    LinearLayout.LayoutParams parematerSlike = new LinearLayout.LayoutParams(80, 80);
                    parematerSlike.width = 80;
                    parematerSlike.height = 80;
                    parematerSlike.gravity = Gravity.CENTER;
                    viewImage.setLayoutParams(parematerSlike);
                    viewImage.requestFocus();
                    
                    if(ClanDetalj != null)
                    {
                    	MainActivity.ClanPromijenjenActivity = 1;
                    }
                    
                    
                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {

                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2)
            	
            {
            	try
            	{
            		   Uri selectedImage = data.getData();
                       String[] filePath = { MediaStore.Images.Media.DATA };
                       Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
                       c.moveToFirst();
                       int columnIndex = c.getColumnIndex(filePath[0]);
                       String picturePath = c.getString(columnIndex);
                       c.close();
                       
                       // ovo dole testno za funkciju ShrinkBitmap zakometirano
                       Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                       //Bitmap thumbnail  = ShrinkBitmap(picturePath, 80, 80);
                       thumbnail =scaleDownLargeImageWithAspectRatio(thumbnail, 80, 80);
                       

                       try {
                           ExifInterface exif = new ExifInterface(picturePath);
                           int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                           Log.d("EXIF", "Exif: " + orientation);
                           Matrix matrix = new Matrix();
                           if (orientation == 6) {
                               matrix.postRotate(90);
                           }
                           else if (orientation == 3) {
                               matrix.postRotate(180);
                           }
                           else if (orientation == 8) {
                               matrix.postRotate(270);
                           }
                           thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
                       }
                       catch (Exception e) {

                       }
              
                       Log.w("path of image from gallery......******************.........", picturePath+"");
                       //viewImage.setImageBitmap(thumbnail);
                       //viewImage.setImageBitmap(Bitmap.createScaledBitmap(thumbnail, 50, 50, false));
                       //viewImage.setImageBitmap((scaleDownLargeImageWithAspectRatio(thumbnail, 80, 80)));
                       viewImage.setImageBitmap(thumbnail);
                       LinearLayout.LayoutParams parematerSlike = new LinearLayout.LayoutParams(80, 80);
                       parematerSlike.width = 80;
                       parematerSlike.height = 80;
                       parematerSlike.gravity = Gravity.CENTER;
                       viewImage.setLayoutParams(parematerSlike);
                       viewImage.requestFocus();
                       
                       if(ClanDetalj != null)
                       {
                       	MainActivity.ClanPromijenjenActivity = 1;
                       }
          
            	}
            	
            catch (Exception e){

                e.printStackTrace();

            	}	
             

            }

        }

   // }   

	
	Bitmap ShrinkBitmap(String file, int width, int height){
		  
	    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	        bmpFactoryOptions.inJustDecodeBounds = true;
	        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
	        
	        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
	        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
	        
	        if (heightRatio > 1 || widthRatio > 1)
	        {
	         if (heightRatio > widthRatio)
	         {
	         bmpFactoryOptions.inSampleSize = heightRatio;
	         }else
	         {
	          bmpFactoryOptions.inSampleSize = widthRatio; 
	         }
	        }
	        
	        bmpFactoryOptions.inJustDecodeBounds = false;
	       bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
	     return bitmap;
	    }
	
	

	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		   super.onSaveInstanceState(outState);
		   outState.putString("datumRodjenja", datumRodjenja);
	// 
	}
	
	
	Bitmap scaleDownLargeImageWithAspectRatio(Bitmap image, int displayWidth, int displayHeight)
    {
        int imaheVerticalAspectRatio,imageHorizontalAspectRatio;
        float bestFitScalingFactor=0;
        float percesionValue=(float) 0.2;

        //getAspect Ratio of Image
        int imageHeight=(int) (Math.ceil((double) image.getHeight()/100)*100);
        int imageWidth=(int) (Math.ceil((double) image.getWidth()/100)*100);
        int GCD=BigInteger.valueOf(imageHeight).gcd(BigInteger.valueOf(imageWidth)).intValue();
        imaheVerticalAspectRatio=imageHeight/GCD;
        imageHorizontalAspectRatio=imageWidth/GCD;
        Log.i("scaleDownLargeImageWIthAspectRatio","Image Dimensions(W:H): "+imageWidth+":"+imageHeight);
        Log.i("scaleDownLargeImageWIthAspectRatio","Image AspectRatio(W:H): "+imageHorizontalAspectRatio+":"+imaheVerticalAspectRatio);

        //getContainer Dimensions
        //int displayWidth = getWindowManager().getDefaultDisplay().getWidth();
        //int displayHeight = getWindowManager().getDefaultDisplay().getHeight();
        //int displayWidth = 70;
        //int displayHeight = 70;
       //I wanted to show the image to fit the entire device, as a best case. So my ccontainer dimensions were displayWidth & displayHeight. For your case, you will need to fetch container dimensions at run time or you can pass static values to these two parameters 

        int leftMargin = 0;
        int rightMargin = 0;
        int topMargin = 0;
        int bottomMargin = 0;
        int containerWidth = displayWidth - (leftMargin + rightMargin);
        int containerHeight = displayHeight - (topMargin + bottomMargin);
        Log.i("scaleDownLargeImageWIthAspectRatio","Container dimensions(W:H): "+containerWidth+":"+containerHeight);

        //iterate to get bestFitScaleFactor per constraints
        while((imageHorizontalAspectRatio*bestFitScalingFactor <= containerWidth) && 
                (imaheVerticalAspectRatio*bestFitScalingFactor<= containerHeight))
        {
            bestFitScalingFactor+=percesionValue;
        }

        //return bestFit bitmap
        int bestFitHeight=(int) (imaheVerticalAspectRatio*bestFitScalingFactor);
        int bestFitWidth=(int) (imageHorizontalAspectRatio*bestFitScalingFactor);
        Log.i("scaleDownLargeImageWIthAspectRatio","bestFitScalingFactor: "+bestFitScalingFactor);
        Log.i("scaleDownLargeImageWIthAspectRatio","bestFitOutPutDimesions(W:H): "+bestFitWidth+":"+bestFitHeight);
        image=Bitmap.createScaledBitmap(image, bestFitWidth,bestFitHeight, true);

        //Position the bitmap centre of the container
        int leftPadding=(containerWidth-image.getWidth())/2;
        int topPadding=(containerHeight-image.getHeight())/2;
        Bitmap backDrop=Bitmap.createBitmap(containerWidth, containerHeight, Bitmap.Config.RGB_565);
        Canvas can = new Canvas(backDrop);
        can.drawBitmap(image, leftPadding, topPadding, null);

        return backDrop;
    }
		 
	public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

	
	
	
	public void upisiClana(View view)
	{
		etClanIme = (EditText) rootView.findViewById(R.id.etClanIme);
		etClanPrezime = (EditText) rootView.findViewById(R.id.etClanPrezime);
		etClanTelefon = (EditText) rootView.findViewById(R.id.etClanTelefon);
		etClanEmail = (EditText) rootView.findViewById(R.id.etClanEmail);
		etClanAdresa = (EditText) rootView.findViewById(R.id.etClanAdresa);
		etClanOpis = (EditText) rootView.findViewById(R.id.etClanOpis);
		txtUpisaniClan = (TextView)rootView.findViewById(R.id.txtUpisaniClan);
		etIzabraniDatum = (EditText)rootView.findViewById(R.id.etIzabraniDatum);
		RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.rgSpol);
		viewImage = (ImageView) rootView.findViewById(R.id.viewImage);
		byte[] slikaInsert = null;
		Bitmap bitmapSlika = null;
		String ImePrezimeNovi = "";
		
		try {	 
			bitmapSlika = ((BitmapDrawable)viewImage.getDrawable()).getBitmap();
			slikaInsert = getBytes(bitmapSlika);
		}
		
		catch(Exception ex)
		{
			slikaInsert = null;
		}
		
		
		boolean upisiclan = false;
		try {	 
			db.openDataBase();
			Clan clan = new Clan();
			clan.set_ime(etClanIme.getText().toString());
			clan.set_prezime(etClanPrezime.getText().toString());
			clan.set_telefon(etClanTelefon.getText().toString());
			clan.set_email(etClanEmail.getText().toString());
			clan.set_adresa(etClanAdresa.getText().toString());
			clan.set_opis(etClanOpis.getText().toString());
			clan.set_datumRodjenja(datumRodjenja);
			//clan.set_slika(slikaInsert);
			clan.set_slika(slikaInsert);

			
			ImePrezimeNovi = etClanPrezime.getText().toString() + " " + etClanIme.getText().toString();
			
			switch (rg.getCheckedRadioButtonId())
			{
				case R.id.rbZena:
					clan.set_spol(1);
					break;
				case R.id.rbMusko:
					clan.set_spol(2);
					break;
				 default :
					 clan.set_spol(0);
					 break;
				
			}

		
			
			for(int i=0; i < allEds.size(); i++)
			{
				KategorijaPolja kategorijaPolja = new KategorijaPolja();
				kategorijaPolja.id = allEds.get(i).getId();
				kategorijaPolja.value = allEds.get(i).getText().toString();
				kategorijaPoljaLista.add(kategorijaPolja);

			}
			
			if(clanId > 0)
			{
				ArrayList<Integer> stareGrupe = new ArrayList<Integer>();
				
				if(ClanGrupe.size()  > 0)
				{
					for(Grupa g: ClanGrupe)
					{
						stareGrupe.add(g.get_idGrupa());
					}
					
				}
				
				
				obrisaneGrupe = ClanoviFormAdapter.obrisaneGrupe;
				
				
				if(ClanoviFormAdapter.selektiraneGrupe.size() > 0)
				{
					selektiraneGrupe = ClanoviFormAdapter.selektiraneGrupe;
				}
				
				else if(obrisaneGrupe.size() > 0 && (obrisaneGrupe.size() == stareGrupe.size()))
				{
					selektiraneGrupe = ClanoviFormAdapter.selektiraneGrupe;
				}
				
				else
				{
					selektiraneGrupe = stareGrupe;
				}
				

				
				clan.set_id(clanId);
				upisiclan = db.updateClanVise(clan, selektiraneGrupe, stareGrupe, kategorijaPoljaLista);
				if(upisiclan)
				{
					ClanoviFormAdapter.obrisaneGrupe.clear();
		     		 Integer ClanIdInteger = clanId;
		     		 ClanoviDetaljFragment clandetaljFragment = new ClanoviDetaljFragment();
		             Bundle args2 = new Bundle();
		             args2.putInt(ClanoviDetaljFragment.ARG_POSITION, ClanIdInteger);
		             clandetaljFragment.setArguments(args2);
		             FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
		             transaction.replace(R.id.content_frame, clandetaljFragment);
		             transaction.addToBackStack(null);
		             transaction.commit();
				}
			}
			else
			{
				selektiraneGrupe = ClanoviFormAdapter.selektiraneGrupe;
				upisiclan = db.dodajClanVise(clan, selektiraneGrupe, kategorijaPoljaLista);
			}
			
			
			
		}catch(SQLException sqle){	 
 		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(upisiclan)
		{
		
			FragmentManager fragmentManager = getFragmentManager();
			ClanoviListFragment fragment = new ClanoviListFragment();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "ClanoviListFragment").addToBackStack(null).commit();
			//String put = db.DB_PATH;
			Toast.makeText(getActivity(),  getResources().getString(R.string.uneseniPodaciZa) + " " + ImePrezimeNovi,Toast.LENGTH_SHORT).show();
			etClanIme.setText("");;
			etClanPrezime.setText("");
			etClanTelefon.setText("");
			etClanEmail.setText("");
			etClanAdresa.setText("");
			etClanOpis.setText("");;
			etIzabraniDatum.setText("");
			twClanEmail.setText(R.string.twTxtClanEmail);
			rg.clearCheck();
			TextView txtPopisGrupaZaClana = (TextView)rootView.findViewById(R.id.txtPopisGrupaZaClana);
   			TextView txtPopisGrupaZaClanaId = (TextView)rootView.findViewById(R.id.txtOdabrani);
			txtPopisGrupaZaClana.setText("");
			txtPopisGrupaZaClana.setVisibility(View.INVISIBLE);
			txtPopisGrupaZaClanaId.setText("");
			txtPopisGrupaZaClanaId.setVisibility(View.INVISIBLE);
			ClanoviFormAdapter.selektiraneGrupe.clear();
			//ImageView itemImage = (ImageView) rootView.findViewById(R.id.viewImage);
			//itemImage.setImageResource(R.drawable.friendcaster);
			
			ClanGrupe = null;

			for(Grupa item: listaGrupa)
			{
				item.setSelected(false);
			}
			
			ArrayAdapter<Grupa> adapter = new ClanoviFormAdapter(getActivity(), listaGrupa, ClanGrupe);
			
			// CheckBox repeatChkBx = (CheckBox) rootView.findViewById(R.id.check);
			
			
		}
		else
		{
		
			Toast.makeText(getActivity(),"Èlan nije upisan!" ,Toast.LENGTH_SHORT).show();
		}
	}
	

	public void dohvatiClanove(View view)
	{
		etClanIme = (EditText) rootView.findViewById(R.id.etClanIme);
		etClanPrezime = (EditText) rootView.findViewById(R.id.etClanPrezime);
		etClanTelefon = (EditText) rootView.findViewById(R.id.etClanTelefon);
		etClanEmail = (EditText) rootView.findViewById(R.id.etClanEmail);
		etClanAdresa = (EditText) rootView.findViewById(R.id.etClanAdresa);
		txtUpisaniClan = (TextView)rootView.findViewById(R.id.txtUpisaniClan);
		
		ArrayList<Clan> clanovi = new ArrayList<Clan>();
		try {	 
			db.openDataBase();	 
			clanovi = db.dohvatiClanove();
		}catch(SQLException sqle){	 
 		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		txtUpisaniClan.setText("");
		String tekst="";
		if(clanovi!=null)
		{
			for(Clan c: clanovi)
			{
				tekst += c.get_ime() + " " + c.get_prezime() + " " + c.get_telefon() + " " + c.get_email() + " " + c.get_adresa() + "\n";
			}
		}
		txtUpisaniClan.setText(tekst);
	}
	
	
	
	  public List<Grupa> dohvatiGrupLista()
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
					items.add(g);
				}
			}
			
			else
			{
				
			}

		return items;

		}
	
	protected void showSelectColoursDialog() {
		boolean[] checkedColours = new boolean[colours.length];
		int count = colours.length;

		for(int i = 0; i < count; i++)
			checkedColours[i] = selectedColours.contains(colours[i]);

		DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if(isChecked)
					selectedColours.add((String) colours[which]);
				else
					selectedColours.remove(colours[which]);
				
				onChangeSelectedColours();
			}
		};
		
		DialogInterface.OnClickListener coloursDialogCliclListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				Log.d("coloursDialogCliclListener", "coloursDialogCliclListener");
				
				if(ClanDetalj != null)
				{
					
						if(grupeZaClanaZaProvjeruSpremanja.containsAll(ClanoviFormAdapter.selektiraneGrupe) && ClanoviFormAdapter.selektiraneGrupe.containsAll(grupeZaClanaZaProvjeruSpremanja))
						{
							 MainActivity.ClanPromijenjenActivity = 0;
						}
						else
						{
							 MainActivity.ClanPromijenjenActivity = 1;
						}
					
					
				}
			

				dialog.dismiss();
			}
		};
		
		

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getResources().getString(R.string.IzaberiteGrupu));
		ArrayAdapter<Grupa> adapter = new ClanoviFormAdapter(getActivity(), listaGrupa, ClanGrupe);
		 setListAdapter(adapter);
		
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			  @Override
			  public void onClick(DialogInterface dialog, int which) {
					
			     	Log.d("adapter", "onClick");
			  }
			});
		
	builder.setPositiveButton("OK", coloursDialogCliclListener);

		AlertDialog dialog = builder.create();

		dialog.show();
	}
	
	
	
	protected void onChangeSelectedColours() {
		StringBuilder stringBuilder = new StringBuilder();

		for(CharSequence colour : selectedColours)
			stringBuilder.append(colour + ",");
		
		Log.d("a: ", stringBuilder.toString());
		
	}
	
	class MyData {
        public MyData(String value, String spinnerText) {
            this.spinnerText = spinnerText;
            this.value = value;
        }

        public String getSpinnerText() {
            return spinnerText;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return spinnerText;
        }

        String spinnerText;
        String value;
    }
	
	
	public List<Kategorija> dohvatiKategorijaLista()
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
			for(Kategorija k: kategorije)
			{
				items.add(k);
			}
		}
		
		else
		{
			
		}

	return items;

	}
	
    public Clan ClanDetalj(Integer id)
  	{
    	Clan item = new Clan();
    	Clan clan = new Clan();
  		
  		try {	 
  			db.openDataBase();	 
  			clan = db.ClanDetalj(id);
  		}catch(SQLException sqle){	 
   		throw sqle;	 
  		}	
  		finally
  		{
  			db.close();
  		}
  		if(clan!=null)
  		{
  			item = clan;
  		}
  		
  		else
  		{
  			
  		}

  	return item;

  	}
    
    
    public List<Grupa> ClanGrupeDetalj(Integer id)
	{
		ArrayList<Grupa> items = new ArrayList<Grupa>();
		ArrayList<Grupa> grupe = new ArrayList<Grupa>();
		
		try {	 
			db.openDataBase();	 
			grupe = db.ClanGrupeDetalj(id);
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
				g.setSelected(true);
				items.add(g);
			}
		}
		
		else
		{
			
		}

	return items;

	}
	
	public List<ClanKategorija> ClanKategorijaDetalj(Integer id)
	{
		ArrayList<ClanKategorija> items = new ArrayList<ClanKategorija>();
		ArrayList<ClanKategorija> kategorije = new ArrayList<ClanKategorija>();
		
		try {	 
			db.openDataBase();	 
			kategorije = db.ClanKategorijeDetalj(id);
		}catch(SQLException sqle){	 
 		throw sqle;	 
		}	
		finally
		{
			db.close();
		}
		if(kategorije!=null)
		{
			for(ClanKategorija k: kategorije)
			{
				items.add(k);
			}
		}
		
		else
		{
			
		}

	return items;

	}
		
	
	 class KategorijaPolja{
		  int id;
		  String value;
		}
	
}

