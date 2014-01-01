package com.vorsk.studying.io;

import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class HomeActivity extends SherlockActivity implements OnClickListener {

	//used to save state
	private static boolean hintShown = false;
	private static boolean answerShown = false;
	private static QuizManager qm;
	private static HashMap<String, String> question;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        if (qm == null){
        	qm = new QuizManager(this);
        }
        
        if (qm.JSONFileExists()) {
        	this.dataReady();
        }else {
        	qm.downloadData();
        }
        
        //make the buttons work
        findViewById(R.id.hintButton).setOnClickListener(this);
        findViewById(R.id.questionButton).setOnClickListener(this);
        findViewById(R.id.answerButton).setOnClickListener(this);
        
        updateDisplay();
    }
    
    public void dataReady() {
    	qm.loadData();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hintButton:
			hintShown = true;
			updateDisplay();
			break;
		case R.id.questionButton:
			displayNextQuestion();
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
			((TextView)findViewById(R.id.questionText)).setText(question.get(QuizManager.TAG_QUESTION));
			((TextView)findViewById(R.id.exampleText)).setText(question.get(QuizManager.TAG_EXAMPLE));
			((TextView)findViewById(R.id.hintText)).setText(question.get(QuizManager.TAG_HINT));
			((TextView)findViewById(R.id.answerText)).setText(question.get(QuizManager.TAG_ANSWER));
		}
	}
	
	public void displayNextQuestion() {
		question = qm.getNextQuestion();
		hintShown = false;
		answerShown = false;
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
