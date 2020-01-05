package com.evidencijaclanova;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.evidencijaclanova.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CrashActivity extends Activity {
	
	private int gGodina;
	private int gMjesec;
	private int gDan;
	TextView error;
	TextView pristojnaGreska;
	Button btnZatvoriAplikaaciju;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.activity_main);
		

		error = (TextView) findViewById(R.id.error);
		//error.setText(getIntent().getStringExtra("error"));
		String tekstError = getIntent().getStringExtra("error");

		pristojnaGreska = (TextView) findViewById(R.id.txtPristojnaGreska);
		pristojnaGreska.setText(R.string.pristojnaGreska);
		
		
		//LinearLayout l = (LinearLayout) findViewById(R.id.layoutGreskaZatvori);
   
		//btnZatvoriAplikaaciju = new Button(this);
		//btnZatvoriAplikaaciju.setBackgroundResource(R.drawable.button_orange);
		//btnZatvoriAplikaaciju.setTextSize(18);
		//btnZatvoriAplikaaciju.setTextColor(getResources().getColor(R.color.blue01));
		//btnZatvoriAplikaaciju.setTypeface(null, Typeface.BOLD);
		//btnZatvoriAplikaaciju.setText(R.string.greskaZatvoriAplikciju);
		//btnZatvoriAplikaaciju.setOnClickListener(new OnClickListener() {
       	 //       public void onClick(View v) {
       	 //       	finish();
       	 //      	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
       	   //     	startActivity(intent);
       	  //      }
       	 //   });
		
		//l.addView(btnZatvoriAplikaaciju);
		
		 final Calendar c = Calendar.getInstance();
	     gGodina = c.get(Calendar.YEAR);
	     gMjesec = c.get(Calendar.MONTH) + 1;
	     gDan = c.get(Calendar.DAY_OF_MONTH);
	     
	     String Dan = Integer.toString(gDan);
	     String Mjesec = Integer.toString(gMjesec);
	     String Godina = Integer.toString(gGodina);
		
		StringBuilder errorReport = new StringBuilder();
		errorReport.append("\n");
		errorReport.append("\n");
		errorReport.append(Dan + "-" + Mjesec + "-" + Godina);
		errorReport.append("\n");
		errorReport.append("------------------------------------------------------------------------------------------------------------------------------------------------\n");
		errorReport.append(tekstError);

		
		
		new MyAsyncTask().execute(errorReport.toString());	
		
		finish();
       	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
       	startActivity(intent);
		
		

	}
	
	private boolean haveNetworkConnection() {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	        {
	        	 if (ni.isConnected())
		                haveConnectedWifi = true;
	        }
	           
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	        {
	        	 if (ni.isConnected())
		                haveConnectedMobile = true;
	        	
	        }
	           
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}
	
	
	
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
			@Override
				protected Double doInBackground(String... params) {
					// TODO Auto-generated method stub
					postData(params[0]);
					return null;
			}
 
				protected void onPostExecute(Double result){

				}
				
 
				public void postData(String valueIWantToSend) {
					// Create a new HttpClient and Post Header
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost("http://filemeerror.fortius-info.hr/receiver.php");
 
					try 
					{
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("myHttpData", valueIWantToSend));
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
						// Execute HTTP Post Request
						HttpResponse response = httpclient.execute(httppost);
 
					}
					catch (ClientProtocolException e)
					{
	
					}
					catch (IOException e)
					{

					}
		}
 
	}
	
	

}
