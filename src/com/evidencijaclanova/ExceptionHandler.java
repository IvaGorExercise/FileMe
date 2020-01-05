package com.evidencijaclanova;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

public class ExceptionHandler implements
		java.lang.Thread.UncaughtExceptionHandler {
	private final Activity myContext;
	private final String LINE_SEPARATOR = "\n";
	private String url;

	public ExceptionHandler(Activity context) {
		myContext = context;
	}

	public void uncaughtException(Thread thread, Throwable exception) {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		StringBuilder errorReport = new StringBuilder();
		errorReport.append("******* CAUSE OF ERROR *******\n");
		errorReport.append(stackTrace.toString());
		errorReport.append("\n******* DEVICE INFORMATION *******\n");
		errorReport.append("Brand: ");
		errorReport.append(Build.BRAND);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Device: ");
		errorReport.append(Build.DEVICE);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Model: ");
		errorReport.append(Build.MODEL);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Id: ");
		errorReport.append(Build.ID);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Product: ");
		errorReport.append(Build.PRODUCT);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("\n******* FIRMWARE *******\n");
		errorReport.append("SDK: ");
		errorReport.append(Build.VERSION.SDK);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Release: ");
		errorReport.append(Build.VERSION.RELEASE);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Incremental: ");
		errorReport.append(Build.VERSION.INCREMENTAL);
		errorReport.append(LINE_SEPARATOR);

		
		Intent intent = new Intent(myContext, CrashActivity.class);
		intent.putExtra("error", errorReport.toString());
		myContext.startActivity(intent);

		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}
	
	

	

    

}

