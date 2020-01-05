package com.evidencijaclanova;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.evidencijaclanova.ClanoviFormFragment.KategorijaPolja;
import com.evidencijaclanova.R;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ClanoviDetaljFragment extends Fragment implements OnClickListener  {
	
	final static int BRISANJE_CLAN_DIJALOG = 1;
	
	View rootView;
	
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    int clanId = 0;
    
    
    TextView article;
	DataBaseHelper db;
	TextView tvClanKategorijaIme;
	TextView tvClanKategorijaVrijednost;
	TableLayout table_layoutZaKategorije;
	Button clanDetaljUredi;
	Button clanDetaljBrisi;
	
	Clan ClanDetalj = new Clan();
	List<Grupa> ClanGrupe = new  ArrayList<Grupa>();
	List<ClanKategorija> ClanKategorija = new  ArrayList<ClanKategorija>();
	TextView PopisGrup;



    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
    	db  = new DataBaseHelper(getActivity());
    	
    	
    	Bundle args = getArguments();
        if (args != null)
        {
        	clanId = args.getInt(ARG_POSITION);
        }
        
        else if (mCurrentPosition != -1)
        {
        	clanId = mCurrentPosition;
        }
 

         ClanDetalj = ClanDetalj(clanId);
         ClanGrupe = ClanGrupeDetalj(clanId);
         ClanKategorija = ClanKategorijaDetalj(clanId);
         
    }
    
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState)
    {

            if (savedInstanceState != null)
            {
                mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
            }

            getActivity().setTitle(R.string.fragmentClanDetalj);
      		rootView = inflater.inflate(R.layout.clan_detalj, container, false);
      		
      		//Labele
      		TextView txtClanDetaljSpol1=(TextView)rootView.findViewById(R.id.txtClanDetaljSpol1);
      		TextView txtClanDetaljDatumRodjenja1=(TextView)rootView.findViewById(R.id.txtClanDetaljDatumRodjenja1);
    		TextView txtClanDetaljTelefon1=(TextView)rootView.findViewById(R.id.txtClanDetaljTelefon1);
    		TextView txtClanDetaljEmail1=(TextView)rootView.findViewById(R.id.txtClanDetaljEmail1);
    		TextView txtClanDetaljAdresa1=(TextView)rootView.findViewById(R.id.txtClanDetaljAdresa1);
    		TextView txtClanDetaljOpis1=(TextView)rootView.findViewById(R.id.txtClanDetaljOpis1);
    		Button btnClanDetaljUredi=(Button)rootView.findViewById(R.id.btnClanDetaljUredi);
    		Button btnClanDetaljBrisi=(Button)rootView.findViewById(R.id.btnClanDetaljBrisi);
   
    		//TextView txtClanDetaljGrupe1A=(TextView)rootView.findViewById(R.id.txtClanDetaljGrupe1);
      		
      		ImageView imgClanDetaljImage = (ImageView) rootView.findViewById(R.id.imgClanDetaljImage);
      		TextView txtPrezime=(TextView)rootView.findViewById(R.id.txtClanDetaljPrezime);
      		TextView txtIme=(TextView)rootView.findViewById(R.id.txtClanDetaljIme);
      		TextView txtClanDetaljSpol=(TextView)rootView.findViewById(R.id.txtClanDetaljSpol);
      		TextView txtClanDetaljDatumRodjenja=(TextView)rootView.findViewById(R.id.txtClanDetaljDatumRodjenja);
      		TextView txtClanDetaljKolikoImaGodina=(TextView)rootView.findViewById(R.id.txtClanDetaljKolikoImaGodina);
    		TextView txtClanDetaljTelefon=(TextView)rootView.findViewById(R.id.txtClanDetaljTelefon);
    		TextView txtClanDetaljEmail=(TextView)rootView.findViewById(R.id.txtClanDetaljEmail);
    		TextView txtClanDetaljAdresa=(TextView)rootView.findViewById(R.id.txtClanDetaljAdresa);
    		TextView txtClanDetaljOpis=(TextView)rootView.findViewById(R.id.txtClanDetaljOpis);
    		TextView txtClanDetaljGrupe=(TextView)rootView.findViewById(R.id.txtClanDetaljGrupe);
    		TextView txtClanDetaljGrupe1=(TextView)rootView.findViewById(R.id.txtClanDetaljGrupe1);
    		

            clanDetaljUredi=(Button)rootView.findViewById(R.id.btnClanDetaljUredi);
            clanDetaljUredi.setOnClickListener(this);
            
            clanDetaljBrisi=(Button)rootView.findViewById(R.id.btnClanDetaljBrisi);
            clanDetaljBrisi.setOnClickListener(this);
    		
    		
      		String datumRodjenjaDio = "";
      		String datumRodjenja = "";
      		String mojegrupe = "";
      		
      		
      		
      		if(clanId > 0)
      		{

      			txtClanDetaljSpol1.setVisibility(View.VISIBLE);
        		txtClanDetaljDatumRodjenja1.setVisibility(View.VISIBLE);
        		txtClanDetaljTelefon1.setVisibility(View.VISIBLE);
        		txtClanDetaljEmail1.setVisibility(View.VISIBLE);
        		txtClanDetaljAdresa1.setVisibility(View.VISIBLE);
        		txtClanDetaljOpis1.setVisibility(View.VISIBLE);
        		btnClanDetaljUredi.setVisibility(View.VISIBLE);
        		btnClanDetaljBrisi.setVisibility(View.VISIBLE);
        		
        		
        		
          		if(ClanDetalj.get_slika() != null)
    	    	{
    	    		Bitmap slikaZaPrikaz = getImage(ClanDetalj.get_slika());
    	    		//imgClanDetaljImage.setImageBitmap(slikaZaPrikaz);
    	    		imgClanDetaljImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(Bitmap.createScaledBitmap(slikaZaPrikaz, 90, 90, false),10));
    	    	
    	    	}
          		
       	    	else
       	    	{
       	    		imgClanDetaljImage.setImageResource(R.drawable.friendcaster);
       	    	}
          		
          		txtPrezime.setText(ClanDetalj.get_prezime() + " ");
          		txtIme.setText(ClanDetalj.get_ime());
          		
          		
          		if(ClanDetalj.get_spol() == 1)
          		{
          			txtClanDetaljSpol.setText(" Ž");
          		}
          		
          		else if (ClanDetalj.get_spol() == 2)
          		{
          			txtClanDetaljSpol.setText(" M");
          		}
          		
          		String kolikoImaGodina = "";
          		
          		if(ClanDetalj.get_datumRodjenja() != null)
          		{
              		datumRodjenjaDio = ClanDetalj.get_datumRodjenja();
              		String godina = datumRodjenjaDio.substring(0, 4);
              		String mjesec = datumRodjenjaDio.substring(4, 6);
              		String dan = datumRodjenjaDio.substring(6, 8);
              		
              		Integer Jezik = MainActivity.Engleski(getActivity());
              		
              		if(Jezik == 1)
              		{
              			datumRodjenja = dan + "/" + mjesec + "/" + godina;
              		}
              		else
              		{
              			datumRodjenja = dan + "." + mjesec + "." + godina+ ".";
              		}
              		
              		kolikoImaGodina = "(" + KolikoImaGodina(godina, mjesec, dan) + ")";
          		}
          		txtClanDetaljDatumRodjenja.setText(datumRodjenja);
          		txtClanDetaljKolikoImaGodina.setText(kolikoImaGodina);
          		txtClanDetaljTelefon.setText(ClanDetalj.get_telefon());
          		txtClanDetaljEmail.setText(ClanDetalj.get_email());
          		txtClanDetaljAdresa.setText(ClanDetalj.get_adresa());
          		txtClanDetaljOpis.setText(ClanDetalj.get_opis());

          		txtClanDetaljGrupe1.setText(R.string.rijecGrupeZaClana);
          		
          		if(ClanGrupe != null)
          		{
          			if(ClanGrupe.size() > 0)
              		{
              		
          				for(Grupa item: ClanGrupe)
          				{
          					mojegrupe = mojegrupe + item.get_ime() + ", ";
          				}
      
          			int aaa = mojegrupe.length(); 
          			mojegrupe = mojegrupe.substring(0, mojegrupe.length()-2);
          			txtClanDetaljGrupe.setText(mojegrupe);
          			
              		}
          		}
          		
          	  if (ClanKategorija != null)
              {
          		 LinearLayout l = (LinearLayout)rootView.findViewById(R.id.ClanDetaljLayoutZaKriterije);
          		 table_layoutZaKategorije = (TableLayout) rootView.findViewById(R.id.tableLayoutZaKriterije);
           
           	   
           	   if (ClanKategorija.size() > 0)
                  {
           		   	TableRow row2 = new TableRow(getActivity());
           		   	tvClanKategorijaIme = new TextView(getActivity());
           		   	tvClanKategorijaIme.setText(getResources().getString(R.string.txtMojiAtributi));
           		   	tvClanKategorijaIme.setTextColor(getResources().getColor(R.color.blue03));
           		   	tvClanKategorijaIme.setTextSize(14);
           			tvClanKategorijaIme.setTypeface(null, Typeface.BOLD);
           		   	row2.addView(tvClanKategorijaIme);
           		   	table_layoutZaKategorije.addView(row2);
           		
           		   	for(ClanKategorija id: ClanKategorija)
         		    {
           		   	 TableRow row = new TableRow(getActivity());
           			   row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
           			   TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
           			   lp.setMargins(0,5,0,0);             
        			  row.setLayoutParams(lp);
           			
          			   tvClanKategorijaIme = new TextView(getActivity());
          			   tvClanKategorijaIme.setId(id.get_id());
          			   tvClanKategorijaIme.setText(id.get_ime().substring(0, 1).toUpperCase() + id.get_ime().substring(1, id.get_ime().length())+":");
          			   tvClanKategorijaIme.setTextColor(getResources().getColor(R.color.blue01));
          			   tvClanKategorijaIme.setTextSize(14);
          			   row.addView(tvClanKategorijaIme);
          			   
          			   // ovo za test
          			   table_layoutZaKategorije.addView(row);
          			   row = new TableRow(getActivity());
          			   TableLayout.LayoutParams lp2 = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
          			   lp2.setMargins(0,0,0,0);             
          			   row.setLayoutParams(lp2);
          			   
          			   
         			   //row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
          			   tvClanKategorijaVrijednost = new TextView(getActivity());
          			   tvClanKategorijaVrijednost.setText("" + id.get_vrijednost());
          			   tvClanKategorijaVrijednost.setTextSize(18);
          			   tvClanKategorijaVrijednost.getResources().getColor(R.color.sivitext);
        			   tvClanKategorijaVrijednost.setWidth(250);
        			   tvClanKategorijaVrijednost.setSingleLine(false);
          			   row.addView(tvClanKategorijaVrijednost);
          			   
          			  table_layoutZaKategorije.addView(row);
      
         		    }
           		   
                  }
           	   
              }
      		}
      		else
      		{

        		txtClanDetaljSpol1.setVisibility(View.INVISIBLE);
        		txtClanDetaljDatumRodjenja1.setVisibility(View.INVISIBLE);
        		txtClanDetaljTelefon1.setVisibility(View.INVISIBLE);
        		txtClanDetaljEmail1.setVisibility(View.INVISIBLE);
        		txtClanDetaljAdresa1.setVisibility(View.INVISIBLE);
        		txtClanDetaljOpis1.setVisibility(View.INVISIBLE);
        		btnClanDetaljUredi.setVisibility(View.INVISIBLE);
        		btnClanDetaljBrisi.setVisibility(View.INVISIBLE);

      		}
      		
      		

           return rootView;
           
        }
    
	  public static Bitmap getImage(byte[] image) {
	        return BitmapFactory.decodeByteArray(image, 0, image.length);
	    }
    
    
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);


	}
    
    
    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null)
        {
        	clanId = args.getInt(ARG_POSITION);
        	//updateArticleView(args.getInt(ARG_POSITION), clanId);
        }
        else if (mCurrentPosition != -1)
        {

        	clanId = mCurrentPosition;
            //updateArticleView(mCurrentPosition, clanId);
        }
    }
    
    public void updateArticleView(int position, Integer ClanIdInteger) {
       
    	clanId = ClanIdInteger;
    	
        mCurrentPosition = position;
    
    	
    	TextView txtClanDetaljSpol1=(TextView)rootView.findViewById(R.id.txtClanDetaljSpol1);
  		TextView txtClanDetaljDatumRodjenja1=(TextView)rootView.findViewById(R.id.txtClanDetaljDatumRodjenja1);
		TextView txtClanDetaljTelefon1=(TextView)rootView.findViewById(R.id.txtClanDetaljTelefon1);
		TextView txtClanDetaljEmail1=(TextView)rootView.findViewById(R.id.txtClanDetaljEmail1);
		TextView txtClanDetaljAdresa1=(TextView)rootView.findViewById(R.id.txtClanDetaljAdresa1);
		TextView txtClanDetaljOpis1=(TextView)rootView.findViewById(R.id.txtClanDetaljOpis1);
		Button btnClanDetaljUredi=(Button)rootView.findViewById(R.id.btnClanDetaljUredi);
		Button btnClanDetaljBrisi=(Button)rootView.findViewById(R.id.btnClanDetaljBrisi);

		//TextView txtClanDetaljGrupe1A=(TextView)rootView.findViewById(R.id.txtClanDetaljGrupe1);
  		
  		ImageView imgClanDetaljImage = (ImageView) rootView.findViewById(R.id.imgClanDetaljImage);
  		TextView txtPrezime=(TextView)rootView.findViewById(R.id.txtClanDetaljPrezime);
  		TextView txtIme=(TextView)rootView.findViewById(R.id.txtClanDetaljIme);
  		TextView txtClanDetaljSpol=(TextView)rootView.findViewById(R.id.txtClanDetaljSpol);
  		TextView txtClanDetaljDatumRodjenja=(TextView)rootView.findViewById(R.id.txtClanDetaljDatumRodjenja);
  		TextView txtClanDetaljKolikoImaGodina=(TextView)rootView.findViewById(R.id.txtClanDetaljKolikoImaGodina);
		TextView txtClanDetaljTelefon=(TextView)rootView.findViewById(R.id.txtClanDetaljTelefon);
		TextView txtClanDetaljEmail=(TextView)rootView.findViewById(R.id.txtClanDetaljEmail);
		TextView txtClanDetaljAdresa=(TextView)rootView.findViewById(R.id.txtClanDetaljAdresa);
		TextView txtClanDetaljOpis=(TextView)rootView.findViewById(R.id.txtClanDetaljOpis);
		TextView txtClanDetaljGrupe=(TextView)rootView.findViewById(R.id.txtClanDetaljGrupe);
		TextView txtClanDetaljGrupe1=(TextView)rootView.findViewById(R.id.txtClanDetaljGrupe1);
		

        clanDetaljUredi=(Button)rootView.findViewById(R.id.btnClanDetaljUredi);
        clanDetaljUredi.setOnClickListener(this);
        
        clanDetaljBrisi=(Button)rootView.findViewById(R.id.btnClanDetaljBrisi);
        clanDetaljBrisi.setOnClickListener(this);
		
		
  		String datumRodjenjaDio = "";
  		String datumRodjenja = "";
  		String mojegrupe = "";
  		
  		
  		
  		if(clanId > 0)
  		{

  			txtClanDetaljSpol1.setVisibility(View.VISIBLE);
    		txtClanDetaljDatumRodjenja1.setVisibility(View.VISIBLE);
    		txtClanDetaljTelefon1.setVisibility(View.VISIBLE);
    		txtClanDetaljEmail1.setVisibility(View.VISIBLE);
    		txtClanDetaljAdresa1.setVisibility(View.VISIBLE);
    		txtClanDetaljOpis1.setVisibility(View.VISIBLE);
    		btnClanDetaljUredi.setVisibility(View.VISIBLE);
    		btnClanDetaljBrisi.setVisibility(View.VISIBLE);
    		
    		
    		
      		if(ClanDetalj.get_slika() != null)
	    	{
	    		Bitmap slikaZaPrikaz = getImage(ClanDetalj.get_slika());
	    		//imgClanDetaljImage.setImageBitmap(slikaZaPrikaz);
	    		imgClanDetaljImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(Bitmap.createScaledBitmap(slikaZaPrikaz, 90, 90, false),10));
	    	
	    	}
      		
   	    	else
   	    	{
   	    		imgClanDetaljImage.setImageResource(R.drawable.friendcaster);
   	    	}
      		
      		txtPrezime.setText(ClanDetalj.get_prezime() + " ");
      		txtIme.setText(ClanDetalj.get_ime());
      		
      		
      		if(ClanDetalj.get_spol() == 1)
      		{
      			txtClanDetaljSpol.setText(" Ž");
      		}
      		
      		else if (ClanDetalj.get_spol() == 2)
      		{
      			txtClanDetaljSpol.setText(" M");
      		}
      		
      		String kolikoImaGodina = "";
      		
      		if(ClanDetalj.get_datumRodjenja() != null)
      		{
          		datumRodjenjaDio = ClanDetalj.get_datumRodjenja();
          		String godina = datumRodjenjaDio.substring(0, 4);
          		String mjesec = datumRodjenjaDio.substring(4, 6);
          		String dan = datumRodjenjaDio.substring(6, 8);
          		datumRodjenja = dan + "." + mjesec + "." + godina+ ".";
          		
          		kolikoImaGodina = "(" + KolikoImaGodina(godina, mjesec, dan) + ")";
      		}
      		txtClanDetaljDatumRodjenja.setText(datumRodjenja);
      		txtClanDetaljKolikoImaGodina.setText(kolikoImaGodina);
      		txtClanDetaljTelefon.setText(ClanDetalj.get_telefon());
      		txtClanDetaljEmail.setText(ClanDetalj.get_email());
      		txtClanDetaljAdresa.setText(ClanDetalj.get_adresa());
      		txtClanDetaljOpis.setText(ClanDetalj.get_opis());

      		txtClanDetaljGrupe1.setText(R.string.rijecGrupeZaClana);
      		
      		if(ClanGrupe != null)
      		{
      			if(ClanGrupe.size() > 0)
          		{
          		
      				for(Grupa item: ClanGrupe)
      				{
      					mojegrupe = mojegrupe + item.get_ime() + ", ";
      				}
  
      			int aaa = mojegrupe.length(); 
      			mojegrupe = mojegrupe.substring(0, mojegrupe.length()-2);
      			txtClanDetaljGrupe.setText(mojegrupe);
      			
          		}
      		}
      		
      	  if (ClanKategorija != null)
          {
      		 LinearLayout l = (LinearLayout)rootView.findViewById(R.id.ClanDetaljLayoutZaKriterije);
      		 table_layoutZaKategorije = (TableLayout) rootView.findViewById(R.id.tableLayoutZaKriterije);
       
       	   
       	   if (ClanKategorija.size() > 0)
              {
       		   	
       		   	for(ClanKategorija id: ClanKategorija)
     		    {
       		   	 TableRow row = new TableRow(getActivity());
       			   row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
       			
      			   tvClanKategorijaIme = new TextView(getActivity());
      			   tvClanKategorijaIme.setId(id.get_id());
      			   tvClanKategorijaIme.setText(id.get_ime().substring(0, 1).toUpperCase() + id.get_ime().substring(1, id.get_ime().length())+":");
      			   tvClanKategorijaIme.setTextColor(getResources().getColor(R.color.blue01));
      			   tvClanKategorijaIme.setTextSize(14);
      			   row.addView(tvClanKategorijaIme);
      			   
      			   // ovo za test
      			   table_layoutZaKategorije.addView(row);
      			   row = new TableRow(getActivity());
    			   row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
      			   
      			   
     			   //row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
      			   tvClanKategorijaVrijednost = new TextView(getActivity());
      			   tvClanKategorijaVrijednost.setText(" " + id.get_vrijednost());
      			   tvClanKategorijaVrijednost.setTextSize(18);
    			   tvClanKategorijaVrijednost.setWidth(250);
    			   tvClanKategorijaVrijednost.setSingleLine(false);
      			   row.addView(tvClanKategorijaVrijednost);
      			   
      			  table_layoutZaKategorije.addView(row);
  
     		    }
       		   
              }
       	   
          }
  		}
  		else
  		{

    		txtClanDetaljSpol1.setVisibility(View.INVISIBLE);
    		txtClanDetaljDatumRodjenja1.setVisibility(View.INVISIBLE);
    		txtClanDetaljTelefon1.setVisibility(View.INVISIBLE);
    		txtClanDetaljEmail1.setVisibility(View.INVISIBLE);
    		txtClanDetaljAdresa1.setVisibility(View.INVISIBLE);
    		txtClanDetaljOpis1.setVisibility(View.INVISIBLE);
    		btnClanDetaljUredi.setVisibility(View.INVISIBLE);
    		btnClanDetaljBrisi.setVisibility(View.INVISIBLE);

  		}
  		
          		

      	
    
        
        
    }
    
    
    
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
    
    
    
    public String KolikoImaGodina(String Godina, String Mjesec, String Dan)
    {
    	String rezultat = "";
    	
    	Integer DanRodjenja = Integer.parseInt(Dan);
 		Integer MjesecRodjenja  = Integer.parseInt(Mjesec);
 		Integer GodinaRodjenja = Integer.parseInt(Godina);
 		
 		
 		Calendar rodjenje = Calendar.getInstance();
 		rodjenje.set(Calendar.YEAR, GodinaRodjenja);
 		rodjenje.set(Calendar.MONTH, MjesecRodjenja);
 		rodjenje.set(Calendar.DAY_OF_MONTH, DanRodjenja);
 		
 		Calendar sada = Calendar.getInstance();
 		
 		long msDiff = sada.getTimeInMillis() - rodjenje.getTimeInMillis();
 		long msYear = 1000L * 60 * 60 * 24 * 365;
    	
    	return String.valueOf(msDiff / msYear);
    
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


	@Override
	public void onClick(View v) {
		
		switch(v.getId())
    	{
    		case R.id.btnClanDetaljUredi:
    			EditClanFragment(clanId);
    			break;
    		case R.id.btnClanDetaljBrisi:
    			BrisiClanSve(clanId);
    			break;
	
    	}
		
	}
	
	public void EditClanFragment(Integer clanId)
	{
		ClanoviFormFragment clandetaljEdit = new ClanoviFormFragment();
         Bundle args2 = new Bundle();
         args2.putInt(ClanoviFormFragment.CLAN_ID, clanId);
         clandetaljEdit.setArguments(args2);
         FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
         transaction.replace(R.id.content_frame, clandetaljEdit);
         transaction.addToBackStack(null);
         transaction.commit();
	}
	
	public void BrisiClanSve(Integer clanId)
	{
		final Integer ClanId = clanId;
		AlertDialog.Builder brisanjClana = new AlertDialog.Builder(getActivity());
		brisanjClana.setIconAttribute(android.R.attr.alertDialogIcon);
		brisanjClana.setMessage(R.string.brisiClana);
		brisanjClana.setPositiveButton(R.string.Da, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				Boolean obrisaniclan = db.brisiClanSve(ClanId);
				if(obrisaniclan)
				{
					FragmentManager fragmentManager = getFragmentManager();
	    			Fragment fragment = new ClanoviListFragment();
	        	 	fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "ClanoviListFragment").addToBackStack(null).commit();
				}
			}
		});
		
		brisanjClana.setNegativeButton(R.string.Odustani, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog dijalog2 = brisanjClana.create();
		dijalog2.show();
	}
	
    private void showDialog(int brisanjeClanDijalog) {
		// TODO Auto-generated method stub
		
	}

 
}
