package com.vorsk.studying.io;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Home extends SherlockActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        
        //make the buttons work
        findViewById(R.id.hintButton).setOnClickListener(this);
        findViewById(R.id.questionButton).setOnClickListener(this);
        findViewById(R.id.answerButton).setOnClickListener(this);

       
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hintButton:
			findViewById(R.id.hintBox).setVisibility(View.VISIBLE);
			break;
		case R.id.questionButton:
			findViewById(R.id.answerBox).setVisibility(View.GONE);
			findViewById(R.id.questionButton).setVisibility(View.GONE);
			findViewById(R.id.answerButton).setVisibility(View.VISIBLE);
			break;
		case R.id.answerButton:
			findViewById(R.id.answerBox).setVisibility(View.VISIBLE);
			findViewById(R.id.questionButton).setVisibility(View.VISIBLE);
			findViewById(R.id.answerButton).setVisibility(View.GONE);
			break;
		default:
			break;
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
