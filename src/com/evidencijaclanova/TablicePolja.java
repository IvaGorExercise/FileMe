package com.evidencijaclanova;

public class TablicePolja {
	
	public static final class Clan
	{
		public static final String _id = "_id";
		public static final String Clan = "Clan";
		public static final String Ime = "Ime";
		public static final String Prezime = "Prezime";
		public static final String Telefon = "Telefon";
		public static final String Email = "Email";
		public static final String Adresa = "ADresa";
		public static final String DatumRodjenja = "DatumRodjenja";
		public static final String Spol = "Spol";
		public static final String Slika = "Slika";
		public static final String Opis = "Opis";

	}
	
	public static final class Kategorija
	{
		public static final String _id = "_id";
		public static final String Kategorija = "Kategorija";
		public static final String Ime = "Ime";
		public static final String Opis = "Opis";
		public static final String VrstaPolja = "Vrsta polja";
		public static final String Vrijednosti = "Vrijednosti";
	}
	
	public static final class Grupa
	{
		public static final String _id = "_id";
		public static final String Grupa = "Grupa";
		public static final String Ime = "Ime";
		public static final String Opis = "Opis";
		public static final String Iznos = "Iznos";
		public static final String NaFormi = "NaFormi";
		public static final String Valuta = "Valuta";
		public static final String Polje01 = "Polje01";
		public static final String Polje02 = "Polje02";
		public static final String Polje03 = "Polje03";

	}
	
	public static final class ClanGrupa
	{
		public static final String _id = "_id";
		public static final String ClanGrupa = "ClanGrupa";
		public static final String IdClan = "IdClan";
		public static final String IdGrupa = "IdGrupa";
		public static final String DatumPristupa = "DatumPristupa";
		public static final String Pauza = "Pauza";

	}
	
	public static final class Clanarina
	{
		public static final String _id = "_id";
		public static final String Clanarina = "Clanarina";
		public static final String Clanarina2 = "Clanarina2";
		public static final String IdClan = "IdClan";
		public static final String IdGrupa = "IdGrupa";
		public static final String Godina = "Godina";
		public static final String Mjesec = "Mjesec";
		public static final String Dan = "Dan";
		public static final String DatumUplata = "DatumUplata";
		public static final String DatumVrijediDo = "DatumVrijediDo";
		public static final String DanDo = "DanDo";
		public static final String MjesecDo = "MjesecDo";
		public static final String GodinaDo = "GodinaDo";
		public static final String Vrsta = "Vrsta";
		public static final String Iznos = "Iznos";

	}
	
	public static final class ClanKategorija
	{
		public static final String ClanKategorija = "ClanKategorija";
		public static final String IdClan = "IdClan";
		public static final String IdKategorija = "IdKategorija";
		public static final String Vrijednost = "Vrijednost";

	}
	
	public static final class Instalacija
	{
		public static final String _id = "_id";
		public static final String Instalacija = "Instalacija";
		public static final String Instaliran = "Instaliran";
		public static final String DatumInstalacija = "DatumInstalacija";
		public static final String ProbniPeriod = "ProbniPeriod";
		public static final String Licenca = "Licenca";
		public static final String DatumLicenca = "DatumLicenca";
		public static final String LicencaBroj = "LicencaBroj";
		public static final String KorisnickoIme = "KorisnickoIme";
		public static final String Lozinka = "Lozinka";
		public static final String TelefonID = "TelefonID";
		public static final String WebAplikcija = "WebAplikcija";
		


	}
	
	public static final class Termin
	{
		public static final String _id = "_id";
		public static final String Termin = "Termin";
		public static final String idGrupa = "idGrupa";
		public static final String Godina = "Godina";
		public static final String Mjesec = "Mjesec";
		public static final String Dan = "Dan";
		public static final String RedniBroj = "RedniBroj";

	}
	
	public static final class TerminClan
	{
		public static final String _id = "_id";
		public static final String TerminClan = "terminclan";
		public static final String idTermin = "idTermin";
		public static final String idClan = "idClan";
		public static final String Prisutan = "Prisutan";
		public static final String Ime = "Ime";
		public static final String Prezime = "Prezime";
	}
	

}


