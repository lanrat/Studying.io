package com.vorsk.studying.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class QuizManager {

	public static final String TAG_ID = "id";
	public static final String TAG_QUESTION = "question";
	public static final String TAG_EXAMPLE = "example";
	public static final String TAG_HINTS = "hints";
	public static final String TAG_ANSWER = "answer";

	private Context context;
	private ArrayList<Question> questions;
	private int lastQuestion;

	public QuizManager(Context context) {
		this.context = context;
		lastQuestion = -1;
	}

	public void loadData() {
		if (questions == null) {
			questions = new ArrayList<Question>();
			LoadingTask ld = new LoadingTask(context, questions);
			ld.execute();
		}
	}

	public void downloadData() {
		DownloadTask dl = new DownloadTask(context);
		dl.execute();
	}

	public boolean JSONFileExists() {
		try {
			FileInputStream fis = context.openFileInput(context
					.getString(R.string.local_JSON_filename));
			fis.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	public Question getNextQuestion() {
		if (questions == null) {
			return null;
		}
		lastQuestion++;
		if (lastQuestion == questions.size()) {
			// we have seen all questions
	         Toast.makeText(context,context.getString(R.string.all_questions_seen), Toast.LENGTH_LONG).show();

			lastQuestion = 0;
		}
		return questions.get(lastQuestion);
	}

	public static class Question {
		private int id;
		private String question;
		private String example;
		private String answer;
		private String[] hints;

		public Question(JSONObject q) throws JSONException {
			id = q.getInt(TAG_ID);
			question = q.getString(TAG_QUESTION);
			example = q.getString(TAG_EXAMPLE);
			answer = q.getString(TAG_ANSWER);
			
			JSONArray jsonHints =  q.getJSONArray(TAG_HINTS);
			hints = new String[jsonHints.length()];
			for (int i=0; i<hints.length; i++) {
				hints[i] = jsonHints.getString(i);
			}
		}
		
		public String getQuestion() {
			return this.question;
		}
		public String getExample() {
			return this.example;
		}
		public String getAnswer() {
			return this.answer;
		}
		public int getID() {
			return id;
		}
		public int getHintCount() {
			return hints.length;
		}
		public String getHint(int count) {
			StringBuilder hint = new StringBuilder();
			for (int i=0; i<count; i++) {
				if (i > 0) {
					hint.append('\n');
				}
				hint.append(hints[i]);
			}
			return hint.toString();
		}
	}

}
