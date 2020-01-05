package com.evidencijaclanova;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

	
	 class Clan {
		
		//private variables 
	    private int _id; 
	    private String _ime; 
	    private String _prezime;
	    private String _telefon; 
	    private String _email; 
	    private String _adresa; 
	    private String _datumRodjenja;
	    private int _spol; 
	    private byte[] _slika;
	    private boolean selectedPlacen;
	    private String _datumUplata;
	    private String _datumVrijediDo;
	    private String _opis;
	    private int _puza;
	    private float _iznos;

	    public Clan()
	    {
	    }
	    
	    public Clan(Integer Id, String Ime, String Prezime, byte[] Slika, int Pauza){ 
	    	this.set_id(Id);
	        this.set_ime(Ime);
	        this.set_prezime(Prezime);
	        this.set_slika(Slika);
	        this.set_puza(Pauza);
	    }
	    
	    public Clan(Integer Id, String Ime, String Prezime, String Telefon, String Email, String Adresa, String DatumRodjenja, int Spol){ 
	    	this.set_id(Id);
	        this.set_ime(Ime);
	        this.set_prezime(Prezime); 
	        this.set_telefon(Telefon);
	        this.set_email(Email); 
	        this.set_adresa(Adresa);
	        this.set_datumRodjenja(DatumRodjenja);
	        this.set_spol(Spol);
	    }
	    
	    public Clan(String Ime, String Prezime, String Telefon, String Email, String Adresa, String DatumRodjenja, int Spol){ 
	        this.set_ime(Ime);
	        this.set_prezime(Prezime); 
	        this.set_telefon(Telefon);
	        this.set_email(Email); 
	        this.set_adresa(Adresa);
	        this.set_datumRodjenja(DatumRodjenja);
	        this.set_spol(Spol);
	    }
	    
	    // constructor 
	    public Clan(String Ime, String Prezime, String Telefon, String Email, String Adresa, String DatumRodjenja, int Spol, byte[] Slika, String Opis){ 
	        this.set_ime(Ime);
	        this.set_prezime(Prezime); 
	        this.set_telefon(Telefon);
	        this.set_email(Email); 
	        this.set_adresa(Adresa);
	        this.set_datumRodjenja(DatumRodjenja);
	        this.set_spol(Spol);
	        this.set_slika(Slika);
	        this.set_opis(Opis);
	    }


		public int get_id() {
			return _id;
		}


		public void set_id(int _id) {
			this._id = _id;
		}

		public String get_ime() {
			return _ime;
		}


		public void set_ime(String _ime) {
			this._ime = _ime;
		}

		public String get_prezime() {
			return _prezime;
		}


		public void set_prezime(String _prezime) {
			this._prezime = _prezime;
		}


		public String get_telefon() {
			return _telefon;
		}


		public void set_telefon(String _telefon) {
			this._telefon = _telefon;
		}


		public String get_email() {
			return _email;
		}


		public void set_email(String _email) {
			this._email = _email;
		}


		public String get_adresa() {
			return _adresa;
		}


		public void set_adresa(String _adresa) {
			this._adresa = _adresa;
		}


		public String get_datumRodjenja() {
			return _datumRodjenja;
		}


		public void set_datumRodjenja(String _datumRodjenja) {
			this._datumRodjenja = _datumRodjenja;
		}
		
		public int get_spol() {
			return _spol;
		}


		public void set_spol(int _spol) {
			this._spol = _spol;
		}
		
		public byte[] get_slika() {
			return _slika;
		}


		public void set_slika(byte[] _slika) {
			this._slika = _slika;
		}

		public boolean isSelectedPlacen() {
			return selectedPlacen;
		}

		public void setSelectedPlacen(boolean selectedPlacen) {
			this.selectedPlacen = selectedPlacen;
		}

		public String get_datumUplata() {
			return _datumUplata;
		}

		public void set_datumUplata(String _datumUplata) {
			this._datumUplata = _datumUplata;
		}

		public String get_datumVrijediDo() {
			return _datumVrijediDo;
		}

		public void set_datumVrijediDo(String _datumVrijediDo) {
			this._datumVrijediDo = _datumVrijediDo;
		}

		public String get_opis() {
			return _opis;
		}

		public void set_opis(String _opis) {
			this._opis = _opis;
		}

		public int get_puza() {
			return _puza;
		}

		public void set_puza(int _puza) {
			this._puza = _puza;
		}

		public float get_iznos() {
			return _iznos;
		}

		public void set_iznos(float _iznos) {
			this._iznos = _iznos;
		}
		
	

	}
	
	
	 class Kategorija {

	    private int _id; 
	    private String _ime;
	    private String _opis;
	    private String _vrijednost; 


	    public Kategorija ()
	    {
	    }

	    public Kategorija(int Id, String Ime, String Opis){ 
	    	this._id = Id;
	        this.set_ime(Ime);
	        this.set_opis(Opis);
	
	    }
	    

		public int get_id() {
			return _id;
		}


		public void set_id(int _id) {
			this._id = _id;
		}


		public String get_ime() {
			return _ime;
		}


		public void set_ime(String _ime) {
			this._ime = _ime;
		}


		public String get_opis() {
			return _opis;
		}


		public void set_opis(String _opis) {
			this._opis = _opis;
		}

		public String get_vrijednost() {
			return _vrijednost;
		}

		public void set_vrijednost(String _vrijednost) {
			this._vrijednost = _vrijednost;
		}
	    
	}
	 
	 
	 class Grupa {

		    private int _id; 
		    private String _ime;
		    private String _opis;
			private boolean selected;
			private int _idGrupa;
			private int _ukupnoClanova;
			
		    private List<Clan> itemList = new ArrayList<Clan>();
		    private List<Termin> itemListTermin = new ArrayList<Termin>();
		    
		    private float _iznos;
			private int _naFormi;
		    private String _valuta;
		    private String _polje01;
		    private String _polje02;
		    private String _polje03;
		    


		    public Grupa ()
		    {
		    }
		    
		    // constructor 
		    public Grupa(String Ime, String Opis){ 
		        this.set_ime(Ime);
		        this.set_opis(Opis);
		        this.set_idGrupa(get_id());
		
		    }
		    
		    
		    public Grupa(int Id, String Ime, String Opis){ 
		    	this._id = Id;
		        this.set_ime(Ime);
		        this.set_opis(Opis);
		        this.set_idGrupa(Id);
		        selected = false;
		    }
		    
		    
		    public Grupa(int Id, String Ime, String Opis, float Iznos, int NaFormi, String Valuta){ 
		    	this._id = Id;
		        this.set_ime(Ime);
		        this.set_opis(Opis);
		        this.set_iznos(Iznos);
		        this.set_naFormi(NaFormi);
		        this.set_valuta(Valuta);
		        this.set_idGrupa(Id);
		        selected = false;
		    }
		    

			public int get_id() {
				return _id;
			}


			public void set_id(int _id) {
				this._id = _id;
			}


			public String get_ime() {
				return _ime;
			}


			public void set_ime(String _ime) {
				this._ime = _ime;
			}

			public String get_opis() {
				return _opis;
			}

			public void set_opis(String _opis) {
				this._opis = _opis;
			}
			
			 public boolean isSelected() {
				   return selected;
			 }

			 public void setSelected(boolean selected) {
				    this.selected = selected;
			 }
			 
			public int get_idGrupa() {
					return _id;
			}


			public void set_idGrupa(int _idGrupa) {
					this._idGrupa = _idGrupa;
			}
			
			public List<Clan> getItemList() {
				return itemList;
			}

			public void setItemList(List<Clan> itemList) {
				this.itemList = itemList;
			}
			
			public List<Termin> getItemListTermin() {
				return itemListTermin;
			}

			public void setItemListTermin(List<Termin> itemListTermin) {
				this.itemListTermin = itemListTermin;
			}

			public int get_ukupnoClanova() {
				return _ukupnoClanova;
			}

			public void set_ukupnoClanova(int _ukupnoClanova) {
				this._ukupnoClanova = _ukupnoClanova;
			}

			public float get_iznos() {
				return _iznos;
			}

			public void set_iznos(float _iznos) {
				this._iznos = _iznos;
			}

			public int get_naFormi() {
				return _naFormi;
			}

			public void set_naFormi(int _naFormi) {
				this._naFormi = _naFormi;
			}

			public String get_valuta() {
				return _valuta;
			}

			public void set_valuta(String _valuta) {
				this._valuta = _valuta;
			}

			public String get_polje01() {
				return _polje01;
			}

			public void set_polje01(String _polje01) {
				this._polje01 = _polje01;
			}

			public String get_polje02() {
				return _polje02;
			}

			public void set_polje02(String _polje02) {
				this._polje02 = _polje02;
			}

			public String get_polje03() {
				return _polje03;
			}

			public void set_polje03(String _polje03) {
				this._polje03 = _polje03;
			}	  
		    
		}
	 
	 
	 
	 class ClanGrupa {
			
		//private variables 
	    private int _id; 
	    private String _ime; 
	    private String _prezime;
	    private String _telefon; 
	    private String _email; 
	    private String _adresa; 
	    private String _datumRodjenja;
	    private int _spol; 
	    private byte[] _slika;
	    private String _idGrupaLista;
	    private String _imeGrupaLista;
	    private int _idGrupa;
	    private int _idClan; 

	    
	    public ClanGrupa(Integer Id, Integer IdClan, Integer IdGrupa){ 
	    	this._id = Id;
	    	this.set_idClan(IdClan);
	    	this.set_idGrupa(IdGrupa);
	    }
	    
	    public ClanGrupa(String Ime, String Prezime, String Telefon, String Email, String Adresa, String DatumRodjenja, int Spol){ 
	        this.set_ime(Ime);
	        this.set_prezime(Prezime); 
	        this.set_telefon(Telefon);
	        this.set_email(Email); 
	        this.set_adresa(Adresa);
	        this.set_datumRodjenja(DatumRodjenja);
	        this.set_spol(Spol);
	    }
	    
	    // constructor 
	    public ClanGrupa(String Ime, String Prezime, String Telefon, String Email, String Adresa, String DatumRodjenja, int Spol, byte[] Slika){ 
	        this.set_ime(Ime);
	        this.set_prezime(Prezime); 
	        this.set_telefon(Telefon);
	        this.set_email(Email); 
	        this.set_adresa(Adresa);
	        this.set_datumRodjenja(DatumRodjenja);
	        this.set_spol(Spol);
	        this.set_slika(Slika);
	    }
	    
	    // constructor 
	    public ClanGrupa(Integer Id, String Ime, String Prezime, String Telefon, String Email, String Adresa, String DatumRodjenja, int Spol, byte[] Slika, String IdGrupaLista, String ImeGrupaLista){ 
	    	this._id = Id;
	    	this.set_ime(Ime);
	        this.set_prezime(Prezime); 
	        this.set_telefon(Telefon);
	        this.set_email(Email); 
	        this.set_adresa(Adresa);
	        this.set_datumRodjenja(DatumRodjenja);
	        this.set_spol(Spol);
	        this.set_slika(Slika);
	        this.set_idGrupaLista(IdGrupaLista);
	        this.set_imeGrupaLista(ImeGrupaLista);
	    }


		public int get_id() {
			return _id;
		}


		public void set_id(int _id) {
			this._id = _id;
		}

		public String get_ime() {
			return _ime;
		}


		public void set_ime(String _ime) {
			this._ime = _ime;
		}

		public String get_prezime() {
			return _prezime;
		}


		public void set_prezime(String _prezime) {
			this._prezime = _prezime;
		}


		public String get_telefon() {
			return _telefon;
		}


		public void set_telefon(String _telefon) {
			this._telefon = _telefon;
		}


		public String get_email() {
			return _email;
		}


		public void set_email(String _email) {
			this._email = _email;
		}


		public String get_adresa() {
			return _adresa;
		}


		public void set_adresa(String _adresa) {
			this._adresa = _adresa;
		}


		public String get_datumRodjenja() {
			return _datumRodjenja;
		}


		public void set_datumRodjenja(String _datumRodjenja) {
			this._datumRodjenja = _datumRodjenja;
		}
		
		public int get_spol() {
			return _spol;
		}


		public void set_spol(int _spol) {
			this._spol = _spol;
		}
		
		public byte[] get_slika() {
			return _slika;
		}


		public void set_slika(byte[] _slika) {
			this._slika = _slika;
		}

		public String get_idGrupaLista() {
			return _idGrupaLista;
		}

		public void set_idGrupaLista(String _idGrupaLista) {
			this._idGrupaLista = _idGrupaLista;
		}

		public String get_imeGrupaLista() {
			return _imeGrupaLista;
		}

		public void set_imeGrupaLista(String _imeGrupaLista) {
			this._imeGrupaLista = _imeGrupaLista;
		}

		public int get_idGrupa() {
			return _idGrupa;
		}

		public void set_idGrupa(int _idGrupa) {
			this._idGrupa = _idGrupa;
		}

		public int get_idClan() {
			return _idClan;
		}

		public void set_idClan(int _idClan) {
			this._idClan = _idClan;
		}
		
	}
	 
	 class ClanKategorija {
	    private int _id;
	    private int _idClan; 
	    private int _idKategorija;
	    private String _vrijednost;
	    private String _ime;
	    
	    
	    public ClanKategorija(){ 

	    }
	    
	    public ClanKategorija(Integer Id, Integer IdClan, Integer IdKategorija, String Vrijednost){ 
	    	this._id = Id;
	    	this.set_idClan(IdClan);
	    	this.set_idKategorija(IdKategorija);
	    	this.set_vrijednost(Vrijednost);
	    }
	    
	    public ClanKategorija(Integer Id, Integer IdClan, Integer IdKategorija, String Vrijednost, String Ime){ 
	    	this._id = Id;
	    	this.set_idClan(IdClan);
	    	this.set_idKategorija(IdKategorija);
	    	this.set_vrijednost(Vrijednost);
	     	this.set_ime(Ime);
	    	
	    }
	    
	    
		public int get_id() {
			return _id;
		}

		public void set_id(int _id) {
			this._id = _id;
		}

		public int get_idClan() {
			return _idClan;
		}

		public void set_idClan(int _idClan) {
			this._idClan = _idClan;
		}

		public int get_idKategorija() {
			return _idKategorija;
		}

		public void set_idKategorija(int _idKategorija) {
			this._idKategorija = _idKategorija;
		}

		public String get_vrijednost() {
			return _vrijednost;
		}

		public void set_vrijednost(String _vrijednost) {
			this._vrijednost = _vrijednost;
		}

		public String get_ime() {
			return _ime;
		}

		public void set_ime(String _ime) {
			this._ime = _ime;
		}

	 }
	 
	 
	 class Clanarina {
			
		//private variables 
	    private int _id; 
	    private int _idClan; 
	    private int _idGrupa;
	    private int _godina;
	    private int _mjesec;
	    private int _dan;
	    private String _datumUplata;
	    private String _datumVrijediDo;
	    private int _danDo;
	    private int _mjesecDo;
	    private int _godinaDo;
	    private int _vrsta;
	    private float _iznos;
	 	    
	    public Clanarina(){ 
	    
	    }
	    
	    
	    public Clanarina(Integer Id, Integer IdClan, Integer IdGrupa){ 
	    	this.set_id(Id);
	    	this.set_idClan(IdClan);
	    	this.set_idGrupa(IdGrupa);
	    }
	    
	    public Clanarina(Integer Id, Integer IdClan, Integer IdGrupa, String DatumUplata, String DatumVrijediDo){ 
	    	this.set_id(Id);
	    	this.set_idClan(IdClan);
	    	this.set_idGrupa(IdGrupa);
	    	this.set_datumUplata(DatumUplata);
	    	this.set_datumVrijediDo(DatumVrijediDo);
	    }
	    
	    public Clanarina(Integer Id, Integer IdClan, Integer IdGrupa, Integer Godina, Integer Mjesec, Integer Dan, String DatumUplata, String DatumVrijediDo, Integer DanDo, Integer MjesecDo, Integer GodinaDo, Integer Vrsta, float Iznos){ 
	    	this.set_id(Id);
	    	this.set_idClan(IdClan);
	    	this.set_idGrupa(IdGrupa);
	    	this.set_mjesec(Mjesec);
	    	this.set_godina(Godina);
	    	this.set_dan(Dan);
	      	this.set_datumUplata(DatumUplata);
	      	this.set_datumVrijediDo(DatumVrijediDo);
	      	this.set_danDo(DanDo);
	      	this.set_mjesecDo(MjesecDo);
	      	this.set_godinaDo(GodinaDo);
	    	this.set_vrsta(Vrsta);
	    	this.set_iznos(Iznos);
	    }


	    public int get_id() {
			return _id;
		}


	    public void set_id(int _id) {
			this._id = _id;
		}


		public int get_idClan() {
			return _idClan;
		}


		public void set_idClan(int _idClan) {
			this._idClan = _idClan;
		}


		public int get_idGrupa() {
			return _idGrupa;
		}


		public void set_idGrupa(int _idGrupa) {
			this._idGrupa = _idGrupa;
		}


		public int get_godina() {
			return _godina;
		}


		public void set_godina(int _Godina) {
			this._godina = _Godina;
		}


		public int get_mjesec() {
			return _mjesec;
		}


		public void set_mjesec(int _Mjesec) {
			this._mjesec = _Mjesec;
		}

		
		public int get_dan() {
			return _dan;
		}


		public void set_dan(int _Dan) {
			this._dan = _Dan;
		}

		public String get_datumUplata() {
			return _datumUplata;
		}


		public void set_datumUplata(String _datumUplata) {
			this._datumUplata = _datumUplata;
		}


		public String get_datumVrijediDo() {
			return _datumVrijediDo;
		}


		public void set_datumVrijediDo(String _datumVrijediDo) {
			this._datumVrijediDo = _datumVrijediDo;
		}


		public int get_danDo() {
			return _danDo;
		}


		public void set_danDo(int _danDo) {
			this._danDo = _danDo;
		}


		public int get_mjesecDo() {
			return _mjesecDo;
		}


		public void set_mjesecDo(int _mjesecDo) {
			this._mjesecDo = _mjesecDo;
		}


		public int get_godinaDo() {
			return _godinaDo;
		}


		public void set_godinaDo(int _godinaDo) {
			this._godinaDo = _godinaDo;
		}


		public int get_vrsta() {
			return _vrsta;
		}


		public void set_vrsta(int _vrsta) {
			this._vrsta = _vrsta;
		}


		public float get_iznos() {
			return _iznos;
		}


		public void set_iznos(float _iznos) {
			this._iznos = _iznos;
		}
 
	 }
	 
	 
	 
	 
	 
	 class Instalacija {
			
	    private int _id; 
	    private int _instaliran; 
	    private String _datumInstalacija;
	    private int _probniPeriod; 
	    private int _licenca; 
	    private String _datumLicenca; 
	    private String _licencaBroj;
	    private String _korisnickoIme;
	    private String _lozinka;
	    private String _telefonID;
	    private int _webAplikcija;
	    

	    
	    public Instalacija(){ 
	    }
	     
	    
	    public Instalacija(Integer Id, Integer Instaliran, String DatumInstalacija, int ProbniPeriod, int Licenca, String DatumLicenca, String LicencaBroj ){ 
	    	this.set_id(Id);
	    	this.set_instaliran(Instaliran);
	    	this.set_datumInstalacija(DatumInstalacija);
	    	this.set_probniPeriod(ProbniPeriod);
	    	this.set_licenca(Licenca);
	    	this.set_datumLicenca(DatumLicenca);
	    	this.set_licencaBroj(LicencaBroj);
	    }
	    

		public int get_id() {
			return _id;
		}

		public void set_id(int _id) {
			this._id = _id;
		}

		public int get_instaliran() {
			return _instaliran;
		}

		private void set_instaliran(int _instaliran) {
			this._instaliran = _instaliran;
		}

		public String get_datumInstalacija() {
			return _datumInstalacija;
		}

		public void set_datumInstalacija(String _datumInstalacija) {
			this._datumInstalacija = _datumInstalacija;
		}

		private int get_probniPeriod() {
			return _probniPeriod;
		}

		public void set_probniPeriod(int _probniPeriod) {
			this._probniPeriod = _probniPeriod;
		}

		public int get_licenca() {
			return _licenca;
		}

		public void set_licenca(int _licenca) {
			this._licenca = _licenca;
		}

		public String get_datumLicenca() {
			return _datumLicenca;
		}

		public void set_datumLicenca(String _datumLicenca) {
			this._datumLicenca = _datumLicenca;
		}

		public String get_licencaBroj() {
			return _licencaBroj;
		}

		public void set_licencaBroj(String _licencaBroj) {
			this._licencaBroj = _licencaBroj;
		}


		public String get_korisnickoIme() {
			return _korisnickoIme;
		}


		public void set_korisnickoIme(String _korisnickoIme) {
			this._korisnickoIme = _korisnickoIme;
		}


		public String get_lozinka() {
			return _lozinka;
		}


		public void set_lozinka(String _lozinka) {
			this._lozinka = _lozinka;
		}


		public String get_telefonID() {
			return _telefonID;
		}


		public void set_telefonID(String _telefonID) {
			this._telefonID = _telefonID;
		}


		public int get_webAplikcija() {
			return _webAplikcija;
		}


		public void set_webAplikcija(int _webAplikcija) {
			this._webAplikcija = _webAplikcija;
		}
	    
	    
     }
	 
	 
	 class Termin {
			
		    private int _id; 
		    private int _idGrupa; 
		    private int _godina;
		    private int _mjesec; 
		    private int _dan; 
		    private int _redniBroj;
		    
		    private List<TerminClan> itemListTerminClan = new ArrayList<TerminClan>();
		    
	    
		    public Termin(){ 
		    }
		    		    
		    public Termin(int Id, int IdGrupa, int Godina, int Mjesec, int Dan, int RedniBroj){ 
		
		    	this.set_id(Id);
		    	this.set_idGrupa(IdGrupa);
		    	this.set_godina(Godina);
		    	this.set_mjesec(Mjesec);
		    	this.set_dan(Dan);
		    	this.set_redniBroj(RedniBroj);
		    	
		    }

			public int get_id() {
				return _id;
			}

			public void set_id(int _id) {
				this._id = _id;
			}

			public int get_idGrupa() {
				return _idGrupa;
			}

			public void set_idGrupa(int _idGrupa) {
				this._idGrupa = _idGrupa;
			}

			public int get_godina() {
				return _godina;
			}

			public void set_godina(int _godina) {
				this._godina = _godina;
			}

			public int get_mjesec() {
				return _mjesec;
			}

			public void set_mjesec(int _mjesec) {
				this._mjesec = _mjesec;
			}

			public int get_dan() {
				return _dan;
			}

			public void set_dan(int _dan) {
				this._dan = _dan;
			}

			public int get_redniBroj() {
				return _redniBroj;
			}

			public void set_redniBroj(int _redniBroj) {
				this._redniBroj = _redniBroj;
			}
			
			
			public List<TerminClan> getItemListTerminClan() {
				return itemListTerminClan;
			}
			
			public void setItemListTerminClan(List<TerminClan> itemListTerminClan) {
				this.itemListTerminClan = itemListTerminClan;
			}
			
			

	 }
	 


	 class TerminClan {
			
		    private int _id; 
		    private int _idTermin;
		    private int _idClan;
		    private int _prisutan;
		    private String _ime;
		    private String _prezime; 


		    public TerminClan(){ 
		    }
		    		    
		    public TerminClan(int Id, int _idTermin, int _idClan, int _prisutan, String _ime, String _prezime){ 
		
		    	this.set_id(Id);
		    	this.set_idTermin(_idTermin);
		    	this.set_idClan(_idClan);
		    	this.set_prisutan(_prisutan);
		    	this.set_ime(_ime);
		    	this.set_prezime(_prezime);
		    }
		    

			public int get_id() {
				return _id;
			}

			public void set_id(int _id) {
				this._id = _id;
			}

			public int get_idTermin() {
				return _idTermin;
			}

			public void set_idTermin(int _idTermin) {
				this._idTermin = _idTermin;
			}

			public int get_idClan() {
				return _idClan;
			}

			public void set_idClan(int _idClan) {
				this._idClan = _idClan;
			}

			public int get_prisutan() {
				return _prisutan;
			}

			public void set_prisutan(int _prisutan) {
				this._prisutan = _prisutan;
			}

			public String get_ime() {
				return _ime;
			}

			public void set_ime(String _ime) {
				this._ime = _ime;
			}

			public String get_prezime() {
				return _prezime;
			}

			public void set_prezime(String _prezime) {
				this._prezime = _prezime;
			}
    
	 }

		

 
	 class Category implements Serializable {
			
			private long id;
			private String name;
			private String descr;
			
			//private List<ItemDetail> itemList = new ArrayList<ItemDetail>();

			public Category(long id, String name, String descr) {
				this.id = id;
				this.name = name;
				this.descr = descr;
			}

			public long getId() {
				return id;
			}

			public void setId(long id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getDescr() {
				return descr;
			}

			public void setDescr(String descr) {
				this.descr = descr;
			}

			//public List<ItemDetail> getItemList() {
			//	return itemList;
			//}

			//public void setItemList(List<ItemDetail> itemList) {
			//	this.itemList = itemList;
			//}
			
			

		}
	 
	 
	 class ItemDetail2 implements Serializable {
			
			private long id;
			private int imgId;
			private String name;
			private String descr;
			
			
			
			public ItemDetail2(long id, int imgId, String name, String descr) {
				this.id = id;
				this.imgId = imgId;
				this.name = name;
				this.descr = descr;
			}
			
			
			public long getId() {
				return id;
			}
			public void setId(long id) {
				this.id = id;
			}
			public int getImgId() {
				return imgId;
			}
			public void setImgId(int imgId) {
				this.imgId = imgId;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getDescr() {
				return descr;
			}
			public void setDescr(String descr) {
				this.descr = descr;
			}
			
			
		}
	 
	 
	

	 
	


