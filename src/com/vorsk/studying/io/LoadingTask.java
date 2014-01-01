package com.vorsk.studying.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoadingTask extends AsyncTask<Void, Void, Void> {

	private ProgressDialog pDialog;
	private Context context;
	private ArrayList<HashMap<String,String>> questions;

	public LoadingTask(Context context,ArrayList<HashMap<String,String>> questions) {
		this.context = context;
		this.questions = questions;
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(context.getString(R.string.loading_message));
		pDialog.setCancelable(false);
		pDialog.setIndeterminate(true);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		// Showing progress dialog
		pDialog.show();
	}

	@Override
	protected Void doInBackground(Void... arg0) {

		// load json data from file
		String jsonStr = loadJSONFromAsset();

		if (jsonStr != null) {
			try {
				JSONArray qJSON = new JSONArray(jsonStr);

				// looping through All Questions
				for (int i = 0; i < qJSON.length(); i++) {
					JSONObject q = qJSON.getJSONObject(i);

					//int id = q.getInt(QuizManager.TAG_ID);
					String question = q.getString(QuizManager.TAG_QUESTION);
					String hint = q.getString(QuizManager.TAG_HINT);
					String example = q.getString(QuizManager.TAG_EXAMPLE);
					String answer = q.getString(QuizManager.TAG_ANSWER);


					// tmp hashmap for single question
					HashMap<String, String> questionMap = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					//questionMap.put(QuizManager.TAG_ID, id);
					questionMap.put(QuizManager.TAG_QUESTION, question);
					questionMap.put(QuizManager.TAG_HINT, hint);
					questionMap.put(QuizManager.TAG_EXAMPLE, example);
					questionMap.put(QuizManager.TAG_ANSWER, answer);

					// adding questions to list
					questions.add(questionMap);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("LoadingTask", "Couldn't get any data from the file");
		}
		
		//shuffle the questions loaded
		Collections.shuffle(questions);

		return null;
	}

	private String loadJSONFromAsset() {
		String json = null;
		try {
			FileInputStream fis = context.openFileInput(context.getString(R.string.local_JSON_filename));

			int size = fis.available();
			byte[] buffer = new byte[size];

			fis.read(buffer);
			fis.close();
			
			json = new String(buffer, "UTF-8");

		} catch (IOException ex) {
			Log.e("LoadingTask","Error readon saves JSON file");
			return null;
		}
		return json;

	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		// Dismiss the progress dialog
		if (pDialog.isShowing())
			pDialog.dismiss();
		//display the next question loaded
		((HomeActivity)context).displayNextQuestion();
	}

}