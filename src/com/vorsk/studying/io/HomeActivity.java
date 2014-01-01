package com.vorsk.studying.io;

import com.actionbarsherlock.app.SherlockActivity;
import com.tjeannin.apprate.AppRate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends SherlockActivity implements OnClickListener {

	private static QuizManager qm;
	public static final String PREFS_NAME = "StudyingPrefsFile";
	public static final String PREF_DOWNLOAD_KEY = "last_download";
	private static final long WEEK_MS = 604800000;
	
	//used to save state
	private static boolean hintShown = false;
	private static boolean answerShown = false;
	private static QuizManager.Question question;
	private static int hintCount = 0;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        
        long lastDownload = settings.getLong(PREF_DOWNLOAD_KEY, 0);
        if ((lastDownload + WEEK_MS) < System.currentTimeMillis()) {
        	//it has been > 1 week since last download
        	Log.d("Home Activity","> 1 week since last download");
        	startStudying(true);
        }else {
        	startStudying(false);
        }
        
        //make the buttons work
        findViewById(R.id.hintButton).setOnClickListener(this);
        findViewById(R.id.questionButton).setOnClickListener(this);
        findViewById(R.id.answerButton).setOnClickListener(this);
 
        updateDisplay();
        
        //AppRate
        new AppRate(this)
	        .setShowIfAppHasCrashed(false)
	        .setMinDaysUntilPrompt(4)
	        .setMinLaunchesUntilPrompt(10)
	        .init();
    }
    
    private void startStudying(boolean forceDownload) {
        if (qm == null){
        	qm = new QuizManager(this);
        }
        
        if (!forceDownload && qm.JSONFileExists()) {
        	this.dataReady();
        }else {
        	qm.downloadData();
        }
    }
    
    public void dataReady() {
    	qm.loadData();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hintButton:
			if (question != null && hintCount >= question.getHintCount()) {
		         Toast.makeText(this,getString(R.string.no_more_hints), Toast.LENGTH_SHORT).show();
			}else {
				hintShown = true;
				hintCount++;
				updateDisplay();
			}
			break;
		case R.id.questionButton:
			if (question == null) {
				startStudying(false);
			}else {
				displayNextQuestion();
			}
			break;
		case R.id.answerButton:
			answerShown = true;
			updateDisplay();
			break;
		default:
			break;
		}
		
		
	}
	private void updateDisplay() {
		if (hintShown) {
			findViewById(R.id.hintBox).setVisibility(View.VISIBLE);
		}else {
			findViewById(R.id.hintBox).setVisibility(View.GONE);
		}
		
		if (answerShown) {
			findViewById(R.id.answerBox).setVisibility(View.VISIBLE);
			findViewById(R.id.questionButton).setVisibility(View.VISIBLE);
			findViewById(R.id.answerButton).setVisibility(View.GONE);
		} else {
			findViewById(R.id.answerBox).setVisibility(View.GONE);
			findViewById(R.id.questionButton).setVisibility(View.GONE);
			findViewById(R.id.answerButton).setVisibility(View.VISIBLE);
		}
		
		if (question != null)
		{
			((TextView)findViewById(R.id.questionText)).setText(question.getQuestion());
			((TextView)findViewById(R.id.exampleText)).setText(question.getExample());
			((TextView)findViewById(R.id.hintText)).setText(question.getHint(hintCount));
			((TextView)findViewById(R.id.answerText)).setText(question.getAnswer());
		}
	}
	
	public void displayNextQuestion() {
		question = qm.getNextQuestion();
		hintShown = false;
		answerShown = false;
		hintCount = 0;
		updateDisplay();
	}
	
	
    /* No menu for now
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }*/
    
}
