package com.vorsk.studying.io;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class Home extends SherlockActivity implements OnClickListener {

	//used to save state
	private static boolean hintShown = false;
	private static boolean answerShown = false;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        
        //make the buttons work
        findViewById(R.id.hintButton).setOnClickListener(this);
        findViewById(R.id.questionButton).setOnClickListener(this);
        findViewById(R.id.answerButton).setOnClickListener(this);
        
        updateDisplay();
        
        DownloadTask dl = new DownloadTask(this);
        dl.execute();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hintButton:
			hintShown = true;
			break;
		case R.id.questionButton:
			answerShown = false;
			break;
		case R.id.answerButton:
			answerShown = true;
			break;
		default:
			break;
		}
		updateDisplay();
		
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
	}

    /* No menu for now
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }*/
    
}
