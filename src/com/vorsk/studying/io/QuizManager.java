package com.vorsk.studying.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class QuizManager {
	
	public static final String TAG_ID = "id";
	public static final String TAG_QUESTION = "question";
	public static final String TAG_EXAMPLE = "example";
	public static final String TAG_HINT = "hints";
	public static final String TAG_ANSWER = "answer";
	
	
	private Context context;
	private ArrayList<HashMap<String,String>> questions;
	private int lastQuestion;
	
	public QuizManager(Context context) {
		this.context = context;
		lastQuestion = -1;
	}
	
	public void loadData() {
		if (questions == null)
		{
			questions = new ArrayList<HashMap<String,String>>();
			LoadingTask ld = new LoadingTask(context,questions);
			ld.execute();
		}
	}
	
	public void downloadData() {
		DownloadTask dl = new DownloadTask(context);
		dl.execute();
	}
	
	public boolean JSONFileExists() {
		try {
			FileInputStream fis = context.openFileInput(context.getString(R.string.local_JSON_filename));
			fis.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	public HashMap<String, String> getNextQuestion() {
		if (questions == null) {
			return null;
		}
		lastQuestion++;
		if (lastQuestion == questions.size()) {
			//we have seen all questions
			//TODO display prompt to user
			
			lastQuestion = 0;
		}
		return questions.get(lastQuestion);
	}

}
