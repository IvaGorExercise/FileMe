
package com.evidencijaclanova;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.evidencijaclanova.ClanoviFormFragment.KategorijaPolja;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper{
	

	public static String DB_PATH = "/data/data/com.evidencijaclanova/databases/"; 
    private static String DB_NAME = "evidencija.db3";
 
    private SQLiteDatabase myDataBase;  
    private final Context myContext;
   
    /**
     * Constructor 
     * @param context
     */
    public DataBaseHelper(Context context) { 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }    
    
    public void createDataBase() throws IOException{
     	boolean dbExist = checkDataBase();
     	if(dbExist){
    		//ništa ako veæ postoji baza
			Log.d("CreateDatabase vec postoji: ", "CreateDatabase vec postoji");
    	}else{     		
        	this.getReadableDatabase(); 
        	try {
        		this.close();
    			copyDataBase(); 
    			Log.d("Baza kopirana: ", "Baza kopirana");
    			
    			long PrvaInstalacija = PrvaInstalacija();
    			String PrvaInstalacijaString = Long.toString(PrvaInstalacija);
    			Log.d("XXXXXXX: ", PrvaInstalacijaString);
    			
    			
    			
    		} catch (IOException e) { 
        		throw new Error("Error copying database"); 
        	}
    	} 
    }
 
    private boolean checkDataBase(){
     	SQLiteDatabase checkDB = null; 
    	try{
    		File f = new File(DB_PATH);
    		if (!f.exists()) {
    		f.mkdir();
    		}
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY); 
    	}catch(SQLiteException e){ 
    		//Nema baze... 
    	} 
    	if(checkDB != null){ 
    		checkDB.close(); 
    	} 
    	return checkDB != null;
    }   
    
    private void copyDataBase() throws IOException
    { 
    	InputStream myInput = myContext.getAssets().open(DB_NAME); 
    	//Put do baze
    	String outFileName = DB_PATH + DB_NAME; 
    	
    	OutputStream myOutput = new FileOutputStream(outFileName); 
    	
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}    	
    	//Zatvorit stremove
    	myOutput.flush();
    	myOutput.close();
    	myInput.close(); 
    } 
    
    public void openDataBase() throws SQLException{ 
    	//Otvorit bazu
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE); 
		Log.d("openDataBase: ", "openDataBase");
    }
 
    @Override
	public synchronized void close() {
	    if(myDataBase != null)
		    myDataBase.close(); 
	    super.close();
	} 

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		 switch (oldVersion) {
	        case 1:
	            //db.execSQL(SQL_MY_TABLE);
	            break;

	        case 2:
	            //db.execSQL("ALTER TABLE myTable ADD COLUMN myNewColumn TEXT");
	            break;
	    }
	}	

	
	public final static boolean isValidEmail(CharSequence target) {

		 if (target == null) {
		     return false;
		 } else {
		     return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		 }
		}
 
	public boolean dodajClanVise(Clan cl, ArrayList<Integer> gr, ArrayList<KategorijaPolja>kp) 
	{
		boolean vrati= false;		
		try {
			TablicePolja.Clan tablicePoljaClan = new TablicePolja.Clan();
			
			ContentValues values = new ContentValues();
			values.put(tablicePoljaClan.Ime, cl.get_ime());
			values.put(tablicePoljaClan.Prezime, cl.get_prezime());
			values.put(tablicePoljaClan.Telefon, cl.get_telefon());
			values.put(tablicePoljaClan.Email, cl.get_email());
			values.put(tablicePoljaClan.Adresa, cl.get_adresa());
			values.put(tablicePoljaClan.DatumRodjenja, cl.get_datumRodjenja());
			values.put(tablicePoljaClan.Spol, cl.get_spol());
			values.put(tablicePoljaClan.Slika, cl.get_slika());
			values.put(tablicePoljaClan.Opis, cl.get_opis());
			long newRowId;
			newRowId = myDataBase.insert(tablicePoljaClan.Clan, null, values);
			
			if(newRowId > 0)
			{
				TablicePolja.ClanGrupa tablicePoljaClanGrupa = new TablicePolja.ClanGrupa();
				
				if(gr != null)
				{
					for(Integer id: gr)
					{
					
						ContentValues valuesGrupe = new ContentValues();
						valuesGrupe.put(tablicePoljaClanGrupa.IdClan, newRowId);
						valuesGrupe.put(tablicePoljaClanGrupa.IdGrupa, id);
						String datumUpis = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
						valuesGrupe.put(tablicePoljaClanGrupa.DatumPristupa, datumUpis);
						valuesGrupe.put(tablicePoljaClanGrupa.Pauza, 0);
						
						long newRowIdClanGrupa;
						newRowIdClanGrupa = myDataBase.insert(tablicePoljaClanGrupa.ClanGrupa, null, valuesGrupe);
					}
				}
				
				
				TablicePolja.ClanKategorija tablicePoljaClanKategorija = new TablicePolja.ClanKategorija();
				
				if(kp != null)
				{
					for(KategorijaPolja item: kp)
					{
						ContentValues valuesKategorija = new ContentValues();
						valuesKategorija.put(tablicePoljaClanKategorija.IdClan, newRowId);
						valuesKategorija.put(tablicePoljaClanKategorija.IdKategorija, item.id);
						valuesKategorija.put(tablicePoljaClanKategorija.Vrijednost, item.value);
						
						long newRowIdClanKategorija;
						newRowIdClanKategorija = myDataBase.insert(tablicePoljaClanKategorija.ClanKategorija, null, valuesKategorija);
						long a = newRowIdClanKategorija;
						long b = 17;
					}
				}
			}
			
			Log.d("idClan: ", Long.toString(newRowId));
			vrati = true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("dodajClan: ", e.getMessage().toString());
			vrati= false;
		}				
  		return vrati; 
	}
	
	public ArrayList<Clan> dohvatiClanove()  
	{  
		ArrayList<Clan> vrati= new ArrayList<Clan>();		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM clan",new String [] {}); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				Clan clan= new Clan(cur.getString(1),cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6), cur.getInt(7), cur.getBlob(8), cur.getString(9));

				if(clan!=null)
				{
					vrati.add(clan);  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	public ArrayList<ClanGrupa> dohvatiClanoveVise()  
	{  
		ArrayList<ClanGrupa> vrati= new ArrayList<ClanGrupa>();		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM view_clangrupalista ORDER BY Prezime COLLATE NOCASE ",new String [] {});
			//Cursor cur=myDataBase.rawQuery("SELECT * FROM view_clangrupalista ORDER BY Prezime ",new String [] {}); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				ClanGrupa clangrupa= new ClanGrupa(cur.getInt(0), cur.getString(1),cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6), cur.getInt(7), cur.getBlob(8), cur.getString(10), cur.getString(11));

				if(clangrupa!=null)
				{
					vrati.add(clangrupa);  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	
	public Integer DohvatiBrojClanova()  
	{  
		Integer vrati= 0;	
		
		try {
	
			Cursor cursor=myDataBase.rawQuery("SELECT count(*) FROM clan",new String [] {}); 
			cursor.moveToFirst();
		     vrati= cursor.getInt(0);
		    cursor.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}


	public boolean dodajGrupu(Grupa gr) 
	{
		boolean vrati= false;		
		try {
			TablicePolja.Grupa tablicePoljaGrupa = new TablicePolja.Grupa();
			
			ContentValues values = new ContentValues();
			values.put(tablicePoljaGrupa.Ime, gr.get_ime());
			values.put(tablicePoljaGrupa.Opis, gr.get_opis());
			values.put(tablicePoljaGrupa.Iznos, gr.get_iznos());
			values.put(tablicePoljaGrupa.NaFormi, gr.get_naFormi());
			values.put(tablicePoljaGrupa.Valuta, gr.get_valuta());


			long newRowId;
			newRowId = myDataBase.insert(tablicePoljaGrupa.Grupa, null, values);
			Log.d("idGrupa: ", Long.toString(newRowId));
			vrati = true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("dodajGrupa: ", e.getMessage().toString());
			vrati= false;
		}				
  		return vrati; 
	}
	
	public ArrayList<Grupa> dohvatiGrupe()  
	{  
		ArrayList<Grupa> vrati= new ArrayList<Grupa>();		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM grupa ORDER BY Ime ASC ",new String [] {}); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				//Grupa grup= new Grupa(cur.getInt(0), cur.getString(1),cur.getString(2));
				 Grupa grup= new Grupa(cur.getInt(0), cur.getString(1),cur.getString(2), cur.getFloat(3), cur.getInt(4), cur.getString(5));

				if(grup!=null)
				{
					vrati.add(grup);  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	} 
	
	public boolean dodajKategoriju(Kategorija ka) 
	{
		boolean vrati= false;		
		try {
			TablicePolja.Kategorija tablicePoljaKategorija = new TablicePolja.Kategorija();
			
			ContentValues values = new ContentValues();
			values.put(tablicePoljaKategorija.Ime, ka.get_ime());
			values.put(tablicePoljaKategorija.Opis, ka.get_opis());

			long newRowId;
			newRowId = myDataBase.insert(tablicePoljaKategorija.Kategorija, null, values);
			Log.d("idKategorija: ", Long.toString(newRowId));
			vrati = true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("dodajKategoriju: ", e.getMessage().toString());
			vrati= false;
		}				
  		return vrati; 
	}
	
	public ArrayList<Kategorija> dohvatiKategorije()  
	{  
		ArrayList<Kategorija> vrati= new ArrayList<Kategorija>();		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM kategorija ORDER BY Ime ",new String [] {}); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				Kategorija kateg= new Kategorija(cur.getInt(0), cur.getString(1),cur.getString(4));

				if(kateg!=null)
				{
					vrati.add(kateg);  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	} 
	
	 
	public Clan ClanDetalj(Integer id) 
	{
		Clan vrati= new Clan();		
		try {
			//Cursor cur=myDataBase.rawQuery("SELECT * FROM clan",new String [] {}); 
			Cursor cur =  myDataBase.rawQuery("SELECT * FROM  clan  where _id ='" + id + "'" , null);
			
			 if (cur != null)
	            {
	             if (cur.moveToFirst())
	                {
	            	 Clan clan= new Clan(cur.getString(1),cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6), cur.getInt(7), cur.getBlob(8), cur.getString(10));
	            	 vrati = clan;
	                }
	             cur.close();
	            }

			
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati; 
		
	}
	
	public ArrayList<Grupa> ClanGrupeDetalj(Integer id)  
	{  
		ArrayList<Grupa> vrati= new ArrayList<Grupa>();
		ArrayList<Integer> idGrupe = new ArrayList<Integer>();
		String whereDio = "";
		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM  clangrupa  where idClan ='" + id + "'" , null); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{
				ClanGrupa clangrup= new ClanGrupa(cur.getInt(0), cur.getInt(1), cur.getInt(2));

				if(clangrup!=null)
				{
					idGrupe.add(clangrup.get_idGrupa());  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
			
			
			if(idGrupe != null)
			{
				for(Integer idGrupa: idGrupe)
				{
					whereDio = whereDio +  Integer.toString(idGrupa) + ",";
				
				}
		
			}
			
			whereDio = whereDio.substring(0, whereDio.length()-1);
			
			String a = "SELECT * FROM  grupa  where _id IN ( " + whereDio + ") ";
			Cursor cur2=myDataBase.rawQuery("SELECT * FROM  grupa  where _id IN ( " + whereDio + ") " , null);
			//Cursor cur2=myDataBase.rawQuery("SELECT * FROM  grupa  where _id IN (4,5,6) " , null);
			cur2.moveToFirst();
			while(cur2.isAfterLast()==false)
			{
				//Grupa grup= new Grupa(cur2.getInt(0), cur2.getString(1), cur2.getString(2));
				 Grupa grup= new Grupa(cur2.getInt(0), cur2.getString(1),cur2.getString(2), cur2.getFloat(3), cur2.getInt(4), cur2.getString(5));				

				if(grup!=null)
				{
					vrati.add(grup);  				
				}
				cur2.moveToNext();  		
			}	 
			cur2.close();
			
		
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	

	public ArrayList<ClanKategorija> ClanKategorijeDetalj (Integer id)  
	{  
		ArrayList<ClanKategorija> vrati= new ArrayList<ClanKategorija>();
		ArrayList<ClanKategorija> clanKategorijaLista = new ArrayList<ClanKategorija>();
		String whereDio = "";
		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM  clankategorija  where idClan ='" + id + "'" , null); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{
				ClanKategorija clankategorija= new ClanKategorija(cur.getInt(0), cur.getInt(1), cur.getInt(2), cur.getString(3));

				if(clankategorija!=null)
				{
					ClanKategorija item = new ClanKategorija();
					item.set_idClan(clankategorija.get_idClan());
					item.set_idKategorija(clankategorija.get_idKategorija());
					item.set_vrijednost(clankategorija.get_vrijednost());
					clanKategorijaLista.add(item);
			
				}
				cur.moveToNext();  		
			}	 
			cur.close();
			
			
			if(clanKategorijaLista != null)
			{
				for(ClanKategorija idKategorija: clanKategorijaLista)
				{
					whereDio = whereDio +  Integer.toString(idKategorija.get_idKategorija()) + ",";
				
				}
		
			}
			
			whereDio = whereDio.substring(0, whereDio.length()-1);
			
			String a = "SELECT * FROM  kategorija  where _id IN ( " + whereDio + ") ";
			Cursor cur2=myDataBase.rawQuery("SELECT * FROM  kategorija  where _id IN ( " + whereDio + ") ORDER BY Ime" , null);
			cur2.moveToFirst();
			while(cur2.isAfterLast()==false)
			{
				Kategorija kateg= new Kategorija(cur2.getInt(0), cur2.getString(1), cur2.getString(2));

				if(kateg!=null)
				{
					for(ClanKategorija clanKategorijaPojedinacni: clanKategorijaLista)
					{
						if(clanKategorijaPojedinacni.get_idKategorija() == kateg.get_id())
						{
							ClanKategorija itemreturn = new ClanKategorija();
							itemreturn.set_idKategorija(clanKategorijaPojedinacni.get_idKategorija());
							itemreturn.set_id(clanKategorijaPojedinacni.get_id());
							itemreturn.set_ime(kateg.get_ime());
							itemreturn.set_vrijednost(clanKategorijaPojedinacni.get_vrijednost());
							vrati.add(itemreturn);
						}
						
					
					}
							
				}
				cur2.moveToNext();  		
			}	 
			cur2.close();
			
		
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	public boolean updateClanVise(Clan cl, ArrayList<Integer> selektiraneGrupe, ArrayList<Integer> stareGrupe, ArrayList<KategorijaPolja>kp) 
	{
		boolean vrati= false;
		String whereDioRemove = "";
		try {
			TablicePolja.Clan tablicePoljaClan = new TablicePolja.Clan();
			
			ContentValues values = new ContentValues();
			values.put(tablicePoljaClan._id, cl.get_id());
			values.put(tablicePoljaClan.Ime, cl.get_ime());
			values.put(tablicePoljaClan.Prezime, cl.get_prezime());
			values.put(tablicePoljaClan.Telefon, cl.get_telefon());
			values.put(tablicePoljaClan.Email, cl.get_email());
			values.put(tablicePoljaClan.Adresa, cl.get_adresa());
			values.put(tablicePoljaClan.DatumRodjenja, cl.get_datumRodjenja());
			values.put(tablicePoljaClan.Spol, cl.get_spol());
			values.put(tablicePoljaClan.Slika, cl.get_slika());
			values.put(tablicePoljaClan.Opis, cl.get_opis());
			long newRowId;
			
			String selection = tablicePoljaClan._id + " LIKE ? ";
			String[] selectionArgs = { String.valueOf(cl.get_id()) };

			newRowId = myDataBase.update(tablicePoljaClan.Clan, values,  selection, selectionArgs);
						

			if(newRowId > 0)
			{
				if(!selektiraneGrupe.equals(stareGrupe))
				{
					ArrayList<Integer> add = new ArrayList<Integer>(selektiraneGrupe);
					ArrayList<Integer> remove = new ArrayList<Integer>(stareGrupe);
					add.removeAll(stareGrupe);
					remove.removeAll(selektiraneGrupe);
					
					
					if(remove.size() > 0)
					{
						for(Integer idGrupa: remove)
						{
							whereDioRemove = whereDioRemove +  Integer.toString(idGrupa) + ",";
						
						}
						
						whereDioRemove = whereDioRemove.substring(0, whereDioRemove.length()-1);
						
						String upitClanGrupa = "DELETE FROM  clangrupa  where idClan = " + cl.get_id() + " AND idGrupa IN ( "  + whereDioRemove + ") ";
						myDataBase.execSQL(upitClanGrupa);
						
					}
					
					if(add.size() > 0)
					{
					
					    TablicePolja.ClanGrupa tablicePoljaClanGrupa = new TablicePolja.ClanGrupa();
					
							for(Integer id: add)
								{
								
									ContentValues valuesGrupe = new ContentValues();
									valuesGrupe.put(tablicePoljaClanGrupa.IdClan, cl.get_id());
									valuesGrupe.put(tablicePoljaClanGrupa.IdGrupa, id);
									String datumUpis = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
									valuesGrupe.put(tablicePoljaClanGrupa.DatumPristupa, datumUpis);
									valuesGrupe.put(tablicePoljaClanGrupa.Pauza, 0);
									
									long newRowIdClanGrupa;
									newRowIdClanGrupa = myDataBase.insert(tablicePoljaClanGrupa.ClanGrupa, null, valuesGrupe);
								}
						
					}
				}
				
			
				String upitClanKategorija = "DELETE FROM  clankategorija  where idClan = " + cl.get_id();
				myDataBase.execSQL(upitClanKategorija);
				
				TablicePolja.ClanKategorija tablicePoljaClanKategorija = new TablicePolja.ClanKategorija();
				
				if(kp != null)
				{
					for(KategorijaPolja item: kp)
					{
						ContentValues valuesKategorija = new ContentValues();
						valuesKategorija.put(tablicePoljaClanKategorija.IdClan, cl.get_id());
						valuesKategorija.put(tablicePoljaClanKategorija.IdKategorija, item.id);
						valuesKategorija.put(tablicePoljaClanKategorija.Vrijednost, item.value);
						
						long newRowIdClanKategorija;
						newRowIdClanKategorija = myDataBase.insert(tablicePoljaClanKategorija.ClanKategorija, null, valuesKategorija);
						long a = newRowIdClanKategorija;
					}
				}
			}
			
			Log.d("idClan: ", Long.toString(newRowId));
			vrati = true;
		} catch (Exception e) {
			e.printStackTrace();
			vrati= false;
		}				
  		return vrati; 
	}
	
	public boolean brisiClanSve(Integer clanId) 
	{
		boolean vrati= false;		
		try {
			 
			if(!myDataBase.isOpen())
			{
				this.openDataBase();
			}
			
			String upitClanClanrina = "DELETE FROM  clanarina  where idClan = " + clanId + " ";
			myDataBase.execSQL(upitClanClanrina);
			
			String upitClanGrupa = "DELETE FROM  clangrupa  where idClan = " + clanId + " ";
			myDataBase.execSQL(upitClanGrupa);
					
			String upitClanKategorija = "DELETE FROM  clankategorija  where idClan = " + clanId + "  ";
			myDataBase.execSQL(upitClanKategorija);
			
			String upitClan = "DELETE FROM  clan  where _id = " + clanId + " ";
			myDataBase.execSQL(upitClan);
				
			vrati = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			vrati= false;
		}				
  		return vrati; 
	}
	
	
	 
	public Kategorija KategorijaDetalj(Integer id) 
	{
		Kategorija vrati= new Kategorija();		
		try {
			Cursor cur =  myDataBase.rawQuery("SELECT * FROM  kategorija  where _id ='" + id + "'" , null);
			
			 if (cur != null)
	            {
	             if (cur.moveToFirst())
	                {
	            	 Kategorija kategorija= new Kategorija(cur.getInt(0), cur.getString(1),cur.getString(4));
	            	 vrati = kategorija;
	                }
	             cur.close();
	            }

			
		} catch (Exception e) {			
			e.printStackTrace();
		}
 		return vrati; 
		
	}
	
	
	public Grupa GrupaDetalj(Integer id) 
	{
		Grupa vrati= new Grupa();		
		try {
			Cursor cur =  myDataBase.rawQuery("SELECT * FROM  grupa  where _id ='" + id + "'" , null);
			
			 if (cur != null)
	            {
	             if (cur.moveToFirst())
	                {
	            	 Grupa grupa= new Grupa(cur.getInt(0), cur.getString(1),cur.getString(2), cur.getFloat(3), cur.getInt(4), cur.getString(5));
	            	 vrati = grupa;
	                }
	             cur.close();
	            }

			
		} catch (Exception e) {			
			e.printStackTrace();
		}
 		return vrati; 
		
	}
	
	
	public boolean updateGrupa(Grupa gr) 
	{
		boolean vrati= false;		
		try {
			TablicePolja.Grupa tablicePoljaGrupa = new TablicePolja.Grupa();
			
			ContentValues values = new ContentValues();
			values.put(tablicePoljaGrupa.Ime, gr.get_ime());
			values.put(tablicePoljaGrupa.Opis, gr.get_opis());
			values.put(tablicePoljaGrupa.Iznos, gr.get_iznos());
			values.put(tablicePoljaGrupa.NaFormi, gr.get_naFormi());
			values.put(tablicePoljaGrupa.Valuta, gr.get_valuta());

			long newRowId;
		
			String selection = tablicePoljaGrupa._id + " LIKE ? ";
			String[] selectionArgs = { String.valueOf(gr.get_id()) };

			newRowId = myDataBase.update(tablicePoljaGrupa.Grupa, values,  selection, selectionArgs);
			
			vrati = true;
		} catch (Exception e) {
			e.printStackTrace();
			vrati= false;
		}				
  		return vrati; 
	}
	
	
	public boolean brisiGrupaSve(Integer grupaId) 
	{
		boolean vrati= false;		
		try {
			 
			if(!myDataBase.isOpen())
			{
				this.openDataBase();
			}
			
			String upitClanGrupa = "DELETE FROM  clangrupa  where idGrupa = " + grupaId + " ";
			myDataBase.execSQL(upitClanGrupa);
					

			String upitGrupa = "DELETE FROM  grupa  where _id = " + grupaId + " ";
			myDataBase.execSQL(upitGrupa);
				
			vrati = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			vrati= false;
		}				
  		return vrati; 
	}
	
	
	
	public boolean updateKategorija(Kategorija ka) 
	{
		boolean vrati= false;		
		try {
			TablicePolja.Kategorija tablicePoljaKategorija = new TablicePolja.Kategorija();
			
			ContentValues values = new ContentValues();
			values.put(tablicePoljaKategorija.Ime, ka.get_ime());
			values.put(tablicePoljaKategorija.Opis, ka.get_opis());

			long newRowId;
		
			String selection = tablicePoljaKategorija._id + " LIKE ? ";
			String[] selectionArgs = { String.valueOf(ka.get_id()) };

			newRowId = myDataBase.update(tablicePoljaKategorija.Kategorija, values,  selection, selectionArgs);
			
			vrati = true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("dodajKategoriju: ", e.getMessage().toString());
			vrati= false;
		}				
  		return vrati; 
	}
	
	public boolean brisiKategorijaSve(Integer kategorijaId) 
	{
		boolean vrati= false;		
		try {
			 
			if(!myDataBase.isOpen())
			{
				this.openDataBase();
			}
			
			String upitClanKategorija = "DELETE FROM  clankategorija  where idKategorija = " + kategorijaId + " ";
			myDataBase.execSQL(upitClanKategorija);
					

			String upitKategorija = "DELETE FROM  kategorija  where _id = " + kategorijaId + " ";
			myDataBase.execSQL(upitKategorija);
				
			vrati = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			vrati= false;
		}				
  		return vrati; 
	}
	
	
	public ArrayList<Clan> dohvatiClanovePoGrupama(Integer idGrupa)  
	{  
		ArrayList<Clan> vrati= new ArrayList<Clan>();		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM view_clanovipogrupama WHERE idGrupa = " + idGrupa + " ORDER BY Prezime COLLATE NOCASE " , new String [] {}); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				Clan clangrupa= new Clan(cur.getInt(0), cur.getString(1),cur.getString(2), cur.getBlob(4), cur.getInt(5));

				if(clangrupa!=null)
				{
					vrati.add(clangrupa);  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	

	public Clanarina ClanarinaZaMjesec2(Integer idGrupa, Integer idClan, Integer Godina, Integer Mjesec)  
	{  
		Clanarina vrati= new Clanarina();			
		try {
			
			if(!myDataBase.isOpen())
			{
				this.openDataBase();
				Log.d("OD this.openDataBase(); ", "DatabseHelper ClanarinaZaMjesec2");
			}
			
			Cursor cursor=myDataBase.rawQuery("SELECT * FROM clanarina WHERE idGrupa = " + idGrupa + " AND idClan = " + idClan + " AND Godina =  " + Godina +  "  AND Mjesec = " +  Mjesec + " ", new String [] {});
			//Cursor cursor=myDataBase.rawQuery("SELECT * FROM clanarina2 WHERE idGrupa = " + idGrupa + " AND idClan = " + idClan + " ", new String [] {});
			
			 if (cursor != null)
	          {
	             if (cursor.moveToFirst())
	                {
	            		Clanarina clanarina= new Clanarina(cursor.getInt(0), cursor.getInt(1),cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8),cursor.getInt(9),cursor.getInt(10),cursor.getInt(11), cursor.getFloat(12));
	            		vrati = clanarina;
	                }
	             	cursor.close();
	           }
			
				 
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	
	
	public long DodajClanarina2(Clanarina zaDodati) 
	{
		long vrati= 0;		
		try {
			if(!myDataBase.isOpen())
			{
				this.openDataBase();
			}
			
			
			Clanarina clanarina = null;
			
			Cursor cursor=myDataBase.rawQuery("SELECT * FROM clanarina WHERE idGrupa = " + zaDodati.get_idGrupa() + " AND idClan = " + zaDodati.get_idClan() + " AND Godina =  " + zaDodati.get_godina() +  "  AND Mjesec = " +  zaDodati.get_mjesec() + " ", new String [] {});
				
				 if (cursor != null)
		          {
		             if (cursor.moveToFirst())
		                {
		            		clanarina= new Clanarina(cursor.getInt(0), cursor.getInt(1),cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8),cursor.getInt(9),cursor.getInt(10),cursor.getInt(11), cursor.getFloat(12));
		                }
		             	cursor.close();
		           }
			 
				 if (clanarina == null)
				 {
						TablicePolja.Clanarina tablicePoljaClanarina = new TablicePolja.Clanarina();
						
						ContentValues values = new ContentValues();
						values.put(tablicePoljaClanarina.IdClan, zaDodati.get_idClan());
						values.put(tablicePoljaClanarina.IdGrupa, zaDodati.get_idGrupa());
						values.put(tablicePoljaClanarina.Godina, zaDodati.get_godina());
						values.put(tablicePoljaClanarina.Mjesec, zaDodati.get_mjesec());
						values.put(tablicePoljaClanarina.Dan, zaDodati.get_dan());
						values.put(tablicePoljaClanarina.DatumUplata, zaDodati.get_datumUplata());
						values.put(tablicePoljaClanarina.DatumVrijediDo, zaDodati.get_datumVrijediDo());
						values.put(tablicePoljaClanarina.DanDo, zaDodati.get_danDo());
						values.put(tablicePoljaClanarina.MjesecDo, zaDodati.get_mjesecDo());
						values.put(tablicePoljaClanarina.GodinaDo, zaDodati.get_godinaDo());
						values.put(tablicePoljaClanarina.Vrsta, zaDodati.get_vrsta());
						values.put(tablicePoljaClanarina.Iznos, zaDodati.get_iznos());

						long newRowId;
						newRowId = myDataBase.insert(tablicePoljaClanarina.Clanarina, null, values);
						if(newRowId > 0)
						{
							vrati = newRowId;	
						}
				 }
				 
				 else
				 {
					 vrati = -1;	
				 }
		
			
		} catch (Exception e) {
			e.printStackTrace();
			vrati= 0;
		}				
  		return vrati; 
	}
	
	
	public long BrisiClanarina(Integer idGrupa, Integer idClan, Integer Godina, Integer Mjesec) 
	{
		long vrati= 0;		
		try {
				if(!myDataBase.isOpen())
				{
					this.openDataBase();
				}
			
				TablicePolja.Clanarina tablicePoljaClanarina = new TablicePolja.Clanarina();
				
				  String sql = "SELECT * FROM clanarina WHERE idGrupa = " + idGrupa + " AND idClan = " + idClan + " AND Godina =  " + Godina +  "  AND Mjesec = " +  Mjesec + " ";
			        Cursor cursor = myDataBase.rawQuery(sql, null);
			        cursor.moveToFirst();
			        while(!cursor.isAfterLast()){
			        	myDataBase.delete(tablicePoljaClanarina.Clanarina,  "_id = " + cursor.getLong(0), null);
			            cursor.moveToNext();
			            vrati ++;
			        }
			
			}
		
		catch (Exception e)
		{			
			e.printStackTrace();
		}
			
  		return vrati; 
	}
	

	public Clanarina DohvatiClanarina2(long id) 
	{
		Clanarina vrati= new Clanarina();		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM clanarina WHERE _id = " + id + "  " , new String [] {}); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				Clanarina clanarina= new Clanarina(cur.getInt(0), cur.getInt(1), cur.getInt(2), cur.getString(6), cur.getString(7));

				if(clanarina!=null)
				{
					vrati = clanarina;  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}			
  		return vrati; 
	}
	
	
	public ArrayList<Clan> dohvatiClanovePoGrupamaIsteklaClanarina(Integer idGrupa)  
	{  
		ArrayList<Clan> vrati= new ArrayList<Clan>();		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM view_clanovipogrupama WHERE idGrupa = " + idGrupa + " AND Pauza = 0 ORDER BY Prezime COLLATE NOCASE " , new String [] {}); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				Clan clangrupa= new Clan(cur.getInt(0), cur.getString(1),cur.getString(2), cur.getBlob(4), cur.getInt(5));

				if(clangrupa!=null)
				{
					vrati.add(clangrupa);  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	
	public Integer ClanarinaIstekla(Integer idGrupa, Integer idClan, Integer Godina, Integer Mjesec, Integer Dan)  
	{  
		Integer vrati= 0;			
		try {
			
			if(!myDataBase.isOpen())
			{
				this.openDataBase();
			}
			
			Cursor cursor=myDataBase.rawQuery("SELECT * FROM clanarina WHERE idGrupa = " + idGrupa + " AND idClan = " + idClan + " AND GodinaDo =  " + Godina +  "  AND MjesecDo = " +  Mjesec + " AND DanDo >= " +  Dan + " ", new String [] {});
			
			 if (cursor != null)
	          {
	             if (cursor.moveToFirst())
	                {
	            	 	vrati ++;
	                }
	             	cursor.close();
	           }
			
				 
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	public Integer ClanarinaIstekla2(Integer idGrupa, Integer idClan, Integer Godina, Integer Mjesec, Integer Dan, String VrstaEvidencije)  
	{  
		Integer Vrsta = Integer.parseInt(VrstaEvidencije);
		
		Integer vrati= 0;	
		
		switch(Vrsta)
		{
			case 1:
				try {
					
					if(!myDataBase.isOpen())
					{
						this.openDataBase();
					}
					
					Cursor cursor=myDataBase.rawQuery("SELECT * FROM clanarina WHERE idGrupa = " + idGrupa + " AND idClan = " + idClan + " AND Godina =  " + Godina +  "  AND Mjesec = " +  Mjesec + " ", new String [] {});
					
					 if (cursor != null)
			          {
			             if (cursor.moveToFirst())
			                {
			            	 	vrati ++;
			                }
			             	cursor.close();
			           }
						 
				}
				catch (Exception e)
				{			
					e.printStackTrace();
				}
				
			break;
			
			case 2:
		try {
			
				Integer MjesecDo = Mjesec;
				Integer MjesecPrije = Mjesec;
				
				Integer GodinaDo = Godina;
				Integer GodinaPrije = Godina;
				
				
				if(Mjesec == 12)
				{
					MjesecDo = 1;
					GodinaDo = Godina + 1;
				}
				else
				{
					MjesecDo = Mjesec + 1;
					GodinaDo = Godina;
				}
				
				
				if(Mjesec == 1)
				{
					MjesecPrije = 12;
					GodinaPrije = Godina - 1;
				}
				else
				{
					MjesecPrije = Mjesec - 1;
					GodinaPrije = Godina;
				}
				
				String DatumZaKojiProvjera = Integer.toString(Godina) + "-" + DodajVodeceNule(Mjesec)+ "-" +  DodajVodeceNule(Dan);
				
				if(!myDataBase.isOpen())
					{
						this.openDataBase();
					}
					
					Cursor cursor=myDataBase.rawQuery("SELECT * FROM clanarina WHERE idGrupa = " + idGrupa + " AND idClan = " + idClan + " AND Godina =  " + Godina +  "  AND Mjesec = " +  Mjesec + " " +  "  AND MjesecDo = " +  MjesecDo + " AND GodinaDo = " +  GodinaDo +  " AND date('" + DatumZaKojiProvjera +"') BETWEEN date(substr(DatumUplata,1,4) || '-' || substr(DatumUplata,5,2) || '-' ||substr(DatumUplata,7,2) ) AND  date(substr(DatumVrijediDo,1,4) || '-' || substr(DatumVrijediDo,5,2) || '-' ||substr(DatumVrijediDo,7,2) ) " + " ", new String [] {});

					 if (cursor != null && cursor.getCount()>0)
			          {
			             if (cursor.moveToFirst())
			                {
			            	 	vrati ++;
			                }
			             	cursor.close();
			           }
					 else
					 {
						 Cursor cursor2=myDataBase.rawQuery("SELECT * FROM clanarina WHERE idGrupa = " + idGrupa + " AND idClan = " + idClan + " AND Godina =  " + GodinaPrije +  "  AND Mjesec = " +  MjesecPrije  +  "  AND MjesecDo = " +  Mjesec +  "  AND GodinaDo = " +  Godina +  " AND  date(substr(DatumVrijediDo,1,4) || '-' || substr(DatumVrijediDo,5,2) || '-' ||substr(DatumVrijediDo,7,2) )  >=  date('" + DatumZaKojiProvjera +"') " + " ", new String [] {});
						 
						 if (cursor2 != null  && cursor2.getCount()>0)
				          {
				             if (cursor2.moveToFirst())
				                {
				            	 	vrati ++;
				                }
				             	cursor2.close();
				           }
					 }
						 
				}
				catch (Exception e)
				{			
					e.printStackTrace();
				}
			break;
		}
		
	
  		return vrati;  	
	}
	
	
	public long PauzirajClana(Integer idGrupa, Integer idClan) 
	{
		long vrati= 0;
		long IdClanGrupa= 0;
		long UpdateRow= 0;
		Integer idClanVarijabla = idClan;
		Integer idGrupaVarijabla = idGrupa;
		String datumUpis = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		
		
		try {
				if(!myDataBase.isOpen())
				{
					this.openDataBase();
				}

				try {

					 String sql = "SELECT * FROM clangrupa WHERE idGrupa = " + idGrupaVarijabla + " AND idClan = " + idClanVarijabla + " ";
				     Cursor cursor = myDataBase.rawQuery(sql, null);
					
					 if (cursor != null)
			         {
			             if (cursor.moveToFirst())
			                {
			            	 	IdClanGrupa = cursor.getLong(0);
			            	 	idClanVarijabla = cursor.getInt(1);
			            	 	idGrupaVarijabla = cursor.getInt(2);
			              	 	datumUpis = cursor.getString(3);
			                }
			             cursor.close();
			             
			 			TablicePolja.ClanGrupa tablicePoljaClanGrupa = new TablicePolja.ClanGrupa();
						ContentValues valuesGrupe = new ContentValues();
						valuesGrupe.put(tablicePoljaClanGrupa.IdClan, idClan);
						valuesGrupe.put(tablicePoljaClanGrupa.IdGrupa, idGrupa);
						valuesGrupe.put(tablicePoljaClanGrupa.DatumPristupa, datumUpis);
						valuesGrupe.put(tablicePoljaClanGrupa.Pauza, 1);
						
						
						String selection = tablicePoljaClanGrupa._id + " LIKE ? ";
						String[] selectionArgs = { String.valueOf(IdClanGrupa) };
						UpdateRow = myDataBase.update(tablicePoljaClanGrupa.ClanGrupa, valuesGrupe,  selection, selectionArgs);
			          }

					
				} catch (Exception e)
				{			
					e.printStackTrace();
				}
				
		  		return UpdateRow; 
				

			}
		
		catch (Exception e)
		{			
			e.printStackTrace();
		}
			
  		return vrati; 
	}
	
	public long OdPauzirajClana(Integer idGrupa, Integer idClan) 
	{
		long vrati= 0;
		long IdClanGrupa= 0;
		long UpdateRow= 0;
		Integer idClanVarijabla = idClan;
		Integer idGrupaVarijabla = idGrupa;
		String datumUpis = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		
		try {
				if(!myDataBase.isOpen())
				{
					this.openDataBase();
				}

				try {

					 String sql = "SELECT * FROM clangrupa WHERE idGrupa = " + idGrupaVarijabla + " AND idClan = " + idClanVarijabla + " ";
				     Cursor cursor = myDataBase.rawQuery(sql, null);
					
					 if (cursor != null)
			         {
			             if (cursor.moveToFirst())
			                {
			            	 	IdClanGrupa = cursor.getLong(0);
			            	 	idClanVarijabla = cursor.getInt(1);
			            	 	idGrupaVarijabla = cursor.getInt(2);
			              	 	datumUpis = cursor.getString(3);
			                }
			             cursor.close();
			             
			 			TablicePolja.ClanGrupa tablicePoljaClanGrupa = new TablicePolja.ClanGrupa();
						ContentValues valuesGrupe = new ContentValues();
						valuesGrupe.put(tablicePoljaClanGrupa.IdClan, idClan);
						valuesGrupe.put(tablicePoljaClanGrupa.IdGrupa, idGrupa);
						valuesGrupe.put(tablicePoljaClanGrupa.DatumPristupa, datumUpis);
						valuesGrupe.put(tablicePoljaClanGrupa.Pauza, 0);
						
						
						String selection = tablicePoljaClanGrupa._id + " LIKE ? ";
						String[] selectionArgs = { String.valueOf(IdClanGrupa) };
						UpdateRow = myDataBase.update(tablicePoljaClanGrupa.ClanGrupa, valuesGrupe,  selection, selectionArgs);
			          }

					
				} catch (Exception e)
				{			
					e.printStackTrace();
				}
				
		  		return UpdateRow; 
				

			}
		
		catch (Exception e)
		{			
			e.printStackTrace();
		}
			
  		return vrati; 
	}
	
	private String DodajVodeceNule(Integer Broj)
	{
		String rezultat = "";
	
			String BrojStringt = Integer.toString(Broj);
			if (Broj < 10)
			{
				rezultat = "0" + BrojStringt;
			}
			
			else
			{
				rezultat = BrojStringt;
			}
	 		
		return rezultat;
	}
	
	
	public long PrvaInstalacija() 
	{
		long vrati= 0;		
		try {
			
			if(myDataBase == null)
			{
				this.openDataBase();
			}
			
			    Calendar c = Calendar.getInstance();
			    Integer gGodina = c.get(Calendar.YEAR);
			    Integer  gMjesec = c.get(Calendar.MONTH) + 1;
			    Integer  gDan = c.get(Calendar.DAY_OF_MONTH);
			    
			    String DatumInstalacija =  Integer.toString(gGodina) + DodajVodeceNule(gMjesec) +  DodajVodeceNule(gDan);
		        
				TablicePolja.Instalacija tablicePoljaInstalacija = new TablicePolja.Instalacija();
						
				ContentValues values = new ContentValues();
				values.put(tablicePoljaInstalacija.Instaliran, 1);
				values.put(tablicePoljaInstalacija.DatumInstalacija, DatumInstalacija);
				values.put(tablicePoljaInstalacija.ProbniPeriod, 0);
				values.put(tablicePoljaInstalacija.Licenca, 0);
				values.put(tablicePoljaInstalacija.DatumLicenca, "");
				values.put(tablicePoljaInstalacija.LicencaBroj, "");

				long newRowId;
				newRowId = myDataBase.insert(tablicePoljaInstalacija.Instalacija, null, values);
				if(newRowId > 0)
				{
					vrati = newRowId;	
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
			vrati= 0;
		}				
  		return vrati; 
	}
	
	
	
	public long UpdateInstlacija(Instalacija ins) 
	{
		long vrati= 0;		
		try {
			
			if(myDataBase == null)
			{
				this.openDataBase();
			}
			
			 	Calendar c = Calendar.getInstance();
			    Integer gGodina = c.get(Calendar.YEAR);
			    Integer  gMjesec = c.get(Calendar.MONTH) + 1;
			    Integer  gDan = c.get(Calendar.DAY_OF_MONTH);
			    
			    String DatumLicenca =  Integer.toString(gGodina) + DodajVodeceNule(gMjesec) +  DodajVodeceNule(gDan);
		
				TablicePolja.Instalacija tablicePoljaInstalacija = new TablicePolja.Instalacija();
						
				ContentValues values = new ContentValues();
				values.put(tablicePoljaInstalacija.Instaliran, ins.get_instaliran());
				values.put(tablicePoljaInstalacija.DatumInstalacija, ins.get_datumInstalacija());
				values.put(tablicePoljaInstalacija.ProbniPeriod, 0);
				values.put(tablicePoljaInstalacija.Licenca, 1);
				values.put(tablicePoljaInstalacija.DatumLicenca, DatumLicenca);
				values.put(tablicePoljaInstalacija.LicencaBroj, ins.get_licencaBroj());

				long newRowId;
				
				String selection = tablicePoljaInstalacija._id + " LIKE ? ";
				String[] selectionArgs = { String.valueOf(ins.get_id()) };

				
				newRowId = myDataBase.update(tablicePoljaInstalacija.Instalacija, values,  selection, selectionArgs);
				
				if(newRowId > 0)
				{
					vrati = newRowId;	
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
			vrati= 0;
		}				
  		return vrati; 
	}
	
	
	public Instalacija dohvatiInstalciju()  
	{  
		if(!myDataBase.isOpen())
		{
			this.openDataBase();
		}
		
		Instalacija vrati= new Instalacija();		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM instalacija",new String [] {}); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				Instalacija instal= new Instalacija(cur.getInt(0), cur.getInt(1), cur.getString(2), cur.getInt(3), cur.getInt(4), cur.getString(5), cur.getString(6));

				if(instal!=null)
				{
					vrati = instal;  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	public long dodajTermin(Termin tr) 
	{
		long vrati= 0;		
		try {
			TablicePolja.Termin tablicePoljaTermin = new TablicePolja.Termin();
			
			ContentValues values = new ContentValues();
			values.put(tablicePoljaTermin.idGrupa, tr.get_idGrupa());
			values.put(tablicePoljaTermin.Godina, tr.get_godina());
			values.put(tablicePoljaTermin.Mjesec, tr.get_mjesec());
			values.put(tablicePoljaTermin.Dan, tr.get_dan());
			values.put(tablicePoljaTermin.RedniBroj, tr.get_redniBroj());


			long newRowId;
			newRowId = myDataBase.insert(tablicePoljaTermin.Termin, null, values);
			Log.d("idTermin: ", Long.toString(newRowId));
			vrati = newRowId;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("dodajTermin: ", e.getMessage().toString());
			vrati= 0;
		}				
  		return vrati; 
	}
	
	
	public boolean dodajTerminClan(List<Clan> clanLista, int terminId) 
	{
		boolean vrati= false;
		
		try {
			
			TablicePolja.TerminClan tablicePoljaTerminClan = new TablicePolja.TerminClan();
			
			for(Clan clanItem: clanLista)
			{

				ContentValues valuesTerminClan = new ContentValues();
				valuesTerminClan.put(tablicePoljaTerminClan.idTermin, terminId);
				valuesTerminClan.put(tablicePoljaTerminClan.idClan, clanItem.get_id());
				valuesTerminClan.put(tablicePoljaTerminClan.Prisutan, 1);
				valuesTerminClan.put(tablicePoljaTerminClan.Ime, clanItem.get_ime());
				valuesTerminClan.put(tablicePoljaTerminClan.Prezime, clanItem.get_prezime());
				
				long newRowIdTerminClan;
				newRowIdTerminClan = myDataBase.insert(tablicePoljaTerminClan.TerminClan, null, valuesTerminClan);
			}
			
			vrati = true;
			
		}
		
		catch (Exception e) 
		{
			e.printStackTrace();
			vrati= false;
		}				
  		return vrati; 
	}
	
	
	public boolean editTerminClan(List<Clan> clanLista, int terminId, int grupaId, String Rb, int mDan, int mMjesec, int mGodina) 
	{
		boolean vrati= false;
		
		try {
			
			if(grupaId > 0 && Rb != "" && mDan > 0 &&  mMjesec > 0 && mGodina > 0)
			{
				TablicePolja.Termin tablicePoljaTermin = new TablicePolja.Termin();
				
				ContentValues values = new ContentValues();
				values.put(tablicePoljaTermin.Godina, mGodina);
				values.put(tablicePoljaTermin.Mjesec, mMjesec);
				values.put(tablicePoljaTermin.Dan, mDan);
				values.put(tablicePoljaTermin.RedniBroj, Rb);
				
				long newRowId;
			
				String selection = tablicePoljaTermin._id + " LIKE ? ";
				String[] selectionArgs = { String.valueOf(terminId) };

				newRowId = myDataBase.update(tablicePoljaTermin.Termin, values,  selection, selectionArgs);
			}
			
			
			String upitClanGrupa = "DELETE FROM  terminclan  where idTermin = " + terminId + " ";
			myDataBase.execSQL(upitClanGrupa);
			
			
			TablicePolja.TerminClan tablicePoljaTerminClan = new TablicePolja.TerminClan();
			
			for(Clan clanItem: clanLista)
			{

				ContentValues valuesTerminClan = new ContentValues();
				valuesTerminClan.put(tablicePoljaTerminClan.idTermin, terminId);
				valuesTerminClan.put(tablicePoljaTerminClan.idClan, clanItem.get_id());
				valuesTerminClan.put(tablicePoljaTerminClan.Prisutan, 1);
				valuesTerminClan.put(tablicePoljaTerminClan.Ime, clanItem.get_ime());
				valuesTerminClan.put(tablicePoljaTerminClan.Prezime, clanItem.get_prezime());
				
				long newRowIdTerminClan;
				newRowIdTerminClan = myDataBase.insert(tablicePoljaTerminClan.TerminClan, null, valuesTerminClan);
			}
			
			vrati = true;
			
		}
		
		catch (Exception e) 
		{
			e.printStackTrace();
			vrati= false;
		}				
  		return vrati; 
	}
	
	
	
	
	public ArrayList<Termin> dohvatiTerminePoGrupama(Integer idGrupa, int Mjesec, int Godina)  
	{  
		ArrayList<Termin> vrati= new ArrayList<Termin>();		
		try {
			
			Cursor cur=myDataBase.rawQuery("SELECT * FROM termin WHERE idGrupa = " + idGrupa + " AND Mjesec = " +  Mjesec + " AND Godina = " +  Godina +  " ORDER BY Godina, Mjesec, Dan DESC" + "  " , new String [] {});
			//Cursor cur=myDataBase.rawQuery("SELECT * FROM termin WHERE idGrupa = " + idGrupa + "  " , new String [] {});
			//Cursor cur=myDataBase.rawQuery("SELECT * FROM termin " , new String [] {}); 
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				Termin ter= new Termin(cur.getInt(0), cur.getInt(1),cur.getInt(2), cur.getInt(3), cur.getInt(4), cur.getInt(5));

				if(ter!=null)
				{
					vrati.add(ter);  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	

	public ArrayList<TerminClan> dohvatiClanPoTerminu(Integer idTermin)  
	{  
		ArrayList<TerminClan> vrati= new ArrayList<TerminClan>();		
		try {
			Cursor cur=myDataBase.rawQuery("SELECT * FROM terminclan WHERE idTermin = " + idTermin + "  " , new String [] {});
			cur.moveToFirst();
			while(cur.isAfterLast()==false)
			{

				TerminClan ter= new TerminClan(cur.getInt(0), cur.getInt(1),cur.getInt(2), cur.getInt(3), cur.getString(4), cur.getString(5));

				if(ter!=null)
				{
					vrati.add(ter);  				
				}
				cur.moveToNext();  		
			}	 
			cur.close();
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
  		return vrati;  	
	}
	
	
	
	public boolean brisiTerminSve(int terminId) 
	{
		boolean vrati= false;
		
		try {
			
			if(!myDataBase.isOpen())
			{
				this.openDataBase();
			}
			
			String upitDeleteTerminClan = "DELETE FROM  terminclan  where idTermin = " + terminId + " ";
			myDataBase.execSQL(upitDeleteTerminClan);
			
			String upitDeleteTermin = "DELETE FROM termin  where _id = " + terminId + " ";
			myDataBase.execSQL(upitDeleteTermin);
			
			vrati = true;
			
		}
		
		catch (Exception e) 
		{
			e.printStackTrace();
			vrati= false;
		}				
  		return vrati; 
	}
	
	
	
}
