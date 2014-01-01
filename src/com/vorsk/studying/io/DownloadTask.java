package com.vorsk.studying.io;

/*
 * DownloadTask taken from StackOverflow with some modifications
 * http://stackoverflow.com/questions/3028306/download-a-file-with-android-and-showing-the-progress-in-a-progressdialog
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

//usually, subclasses of AsyncTask are declared inside the activity class.
//that way, you can easily modify the UI thread from here
public class DownloadTask extends AsyncTask<Void, Integer, String> {

 private Context context;
 private ProgressDialog mProgressDialog;
 private PowerManager.WakeLock wl;

 public DownloadTask(Context context) {
     this.context = context;
     mProgressDialog = new ProgressDialog(context);
     mProgressDialog.setMessage(context.getString(R.string.downloading_message));
     mProgressDialog.setIndeterminate(true);
     mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
     mProgressDialog.setCancelable(false);
 }

 @SuppressLint("Wakelock")
@SuppressWarnings("resource")
@Override
 protected String doInBackground(Void... nothing) {
     // take CPU lock to prevent CPU from going off if the user 
     // presses the power button during download
     PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
     wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,getClass().getName());
     wl.acquire();

     try {
         InputStream input = null;
         OutputStream output = null;
         HttpURLConnection connection = null;
         try {
             URL url = new URL(context.getString(R.string.JSON_URL));
             connection = (HttpURLConnection) url.openConnection();
             connection.connect();

             // expect HTTP 200 OK, so we don't mistakenly save error report 
             // instead of the file
             if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                  return "Server returned HTTP " + connection.getResponseCode() 
                      + " " + connection.getResponseMessage();

             // this will be useful to display download percentage
             // might be -1: server did not report the length
             int fileLength = connection.getContentLength();

             // download the file
             input = connection.getInputStream();
             output = context.openFileOutput(context.getString(R.string.local_JSON_filename),Context.MODE_PRIVATE);

             byte data[] = new byte[4096];
             long total = 0;
             int count;
             while ((count = input.read(data)) != -1) {
                 // allow canceling with back button
                 if (isCancelled()) {
                     return null;
                 }
                 total += count;
                 // publishing the progress....
                 if (fileLength > 0) // only if total length is known
                     publishProgress((int) (total * 100 / fileLength));
                 output.write(data, 0, count);
             }
         } catch (Exception e) {
             return e.toString();
         } finally {
             try {
                 if (output != null)
                     output.close();
                 if (input != null)
                     input.close();
             } 
             catch (IOException ignored) { }

             if (connection != null)
                 connection.disconnect();
         }
     } finally {
         wl.release();
     }
     return null;
 }
 
 @Override
 protected void onPreExecute() {
     super.onPreExecute();
     mProgressDialog.show();
 }

 @Override
 protected void onProgressUpdate(Integer... progress) {
     super.onProgressUpdate(progress);
     // if we get here, length is known, now set indeterminate to false
     mProgressDialog.setIndeterminate(false);
     mProgressDialog.setMax(100);
     mProgressDialog.setProgress(progress[0]);
 }

 @Override
 protected void onPostExecute(String result) {
	 if (wl != null && wl.isHeld()) {
		 wl.release();
	 }
     mProgressDialog.dismiss();
     if (result != null) {
         Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
     }else {
    	 ((HomeActivity)context).dataReady();
     }
 }
 
}