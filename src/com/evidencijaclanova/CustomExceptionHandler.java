package com.evidencijaclanova;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.security.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

public class CustomExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler defaultUEH;

    private String localPath;

    private String url;

    /* 
     * if any of the parameters is null, the respective functionality 
     * will not be used 
     */
    public CustomExceptionHandler(String localPath, String url) {
        this.localPath = localPath;
        this.url = url;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {

    	 Time now = new Time();
         now.setToNow();
         String timestamp = now.format("%Y_%m_%d_%H_%M_%S");
        //String timestamp = TimestampFormatter.getInstance().getTimestamp();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = timestamp + ".stacktrace";
        

        if (localPath != null) {
           writeToFile(stacktrace, filename);
       }
        if (url != null) {
            //sendToServer(stacktrace, filename);
        	
        	(new SendData(stacktrace, filename)).execute();
        }

        defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String stacktrace, String filename) {
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(
                    localPath + "/" + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToServer(String stacktrace, String filename) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        nvps.add(new BasicNameValuePair("filename", filename));
        nvps.add(new BasicNameValuePair("stacktrace", stacktrace));
        try {
            httpPost.setEntity(
                    new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

 class SendData extends AsyncTask<String, Integer, Void> {
	 
	 String filename;
	 String stacktrace;
	 
	 public SendData(String filename, String stacktrace)
	 {
		 this.filename = filename;
		 this.stacktrace = stacktrace;
	 }
	 
	 
    protected void doInBackground() {
//Create a new HttpClient and Post Header
HttpClient client = new DefaultHttpClient();
String postURL = ("http://filemeerror.fortius-info.hr/upload.php");
HttpPost post = new HttpPost(postURL);
try {
   // Add the data
   List<NameValuePair> pairs = new ArrayList<NameValuePair>(3);
   pairs.add(new BasicNameValuePair("filename", filename));
   pairs.add(new BasicNameValuePair("stacktrace", stacktrace));
   UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(pairs);
   post.setEntity(uefe);
   // Execute the HTTP Post Request
   HttpResponse response = client.execute(post);
   // Convert the response into a String
   HttpEntity resEntity = response.getEntity();
   if (resEntity != null) {
       Log.i("RESPONSE", EntityUtils.toString(resEntity));
   }
} catch (UnsupportedEncodingException uee) {
   uee.printStackTrace();
} catch (ClientProtocolException cpe) {
   cpe.printStackTrace();
} catch (IOException ioe) {
   ioe.printStackTrace();
}
       
}

protected void onProgressUpdate() {
   //called when the background task makes any progress
}

 protected void onPreExecute() {
    //called before doInBackground() is started
}
protected void onPostExecute() {
    //called after doInBackground() has finished 
}

@Override
protected Void doInBackground(String... arg0) {
	// TODO Auto-generated method stub
	return null;
}
 }


