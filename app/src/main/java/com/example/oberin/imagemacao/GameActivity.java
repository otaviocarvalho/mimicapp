package com.example.oberin.imagemacao;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class GameActivity extends ActionBarActivity {

    // Variables for Couter
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private final long startTime = 5 * 1000;
    private final long interval = 1 * 1000;
    public TextView currentWordText;
    public TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();


        currentWordText = (TextView) this.findViewById(R.id.currentWord);
        timerText = (TextView) this.findViewById(R.id.timer);

        countDownTimer = new MyCountDownTimer(startTime, interval);
        this.startTimer();
        timerText.setText(timerText.getText() + String.valueOf((startTime / 1000)-1) );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            currentWordText.setText("");
            timerText.setText("GO!");
            countDownTimer.cancel();

            Intent callRound = new Intent(GameActivity.this, RoundActivity.class);
            callRound.putExtra("msg", "");
            //startActivity(callLanguage);
            startActivityForResult(callRound, 1);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerText.setText("" + millisUntilFinished / 1000);
        }




    }
    public void startTimer() {
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
            //          startB.setText("STOP");
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
            //            startB.setText("RESTART");

        }
    }

}