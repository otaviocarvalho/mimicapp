package com.example.oberin.imagemacao;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RoundActivity extends ActionBarActivity implements SensorEventListener{

    SensorManager sensorManager;
    // Variables for Counter
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private boolean downMovTriggered= false;
    private boolean upMovTriggered = false;
    private final long startTime = 30000;
    private final long interval = 1000;
    public TextView currentWordText;
    public TextView timerText;
    public TextView sensorXText;
    public TextView sensorYText;
    public TextView sensorZText;
    public boolean hasFrontalCameraFlag;
    public String videoName = "/myFunVideo.mp4";

    // Word
    List<Word> Words = new ArrayList<>();
    private int currentWordIndex;
    //ListView wordListView;
    DatabaseHandler dbHandler;

    // Scoreboard
    Scoreboard curScore = new Scoreboard("home", 0, "away", 0);
    public TextView scoreHome;
    public TextView scoreAway;

    private static final boolean DEBUG_FLAG = true;

    private static final int VIDEO_CAPTURE = 101;
    public static Intent recorVideoIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

        Intent intent = getIntent();

        currentWordText = (TextView) this.findViewById(R.id.textResult);
        timerText = (TextView) this.findViewById(R.id.timer);

        sensorXText = (TextView) this.findViewById(R.id.sensorX);
        sensorYText= (TextView) this.findViewById(R.id.sensorY);
        sensorZText = (TextView) this.findViewById(R.id.sensorZ);


        //wordListView = (ListView) findViewById(R.id.currentWord);
        dbHandler = new DatabaseHandler(getApplicationContext());
        dbHandler.populateTableWord();

        getRandomRoundWords();
        countDownTimer = new MyCountDownTimer(startTime, interval);

        setNextWordInScreen();


        this.startTimer();
        timerText.setText(timerText.getText() + String.valueOf((startTime / 1000) - 1));
        if (hasFrontalCamera()) {
            if (DEBUG_FLAG)
                Toast.makeText(this, "HAS Frontal Camera",
                    Toast.LENGTH_LONG).show();
            /*
            File mediaFile =
                    new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + videoName);
            recorVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            Uri videoUri = Uri.fromFile(mediaFile);
            recorVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(recorVideoIntent, VIDEO_CAPTURE);
            */
        }


        /*populateList();*/

        /*
        List<Word> roundWords = dbHandler.getAllWords();

        int wordCount = dbHandler.getWordCount();

        for (int i = 0; i< wordCount; i++){
            Words.add(roundWords.get(i));
        }



        if (roundWords.isEmpty())
        {   //Action that show
            // int j = 0;
        }*/

        // Scoreboard
        scoreHome = (TextView) this.findViewById(R.id.pointsHome);
        scoreAway = (TextView) this.findViewById(R.id.pointsHome);
        //if (getIntent().hasExtra("scoreboard")) {
        //    Scoreboard auxScore = getIntent().getParcelableExtra("scoreboard");
        //    Toast.makeText(this, "Scoreboard: " + this.curScore.UserName, Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "Scoreboard: ", Toast.LENGTH_LONG).show();
        //}
    }

    public void getRandomRoundWords(){

        if (dbHandler.getWordCount() != 0) {
            Words.addAll((dbHandler.getAllWords()));
            Collections.shuffle(Words);
        }
    }

    private boolean wordExists(Word word){
        String wordLocal = word.getWord();
        int wordCount = Words.size();

        for (int i = 0; i< wordCount;i++){
            if (wordLocal.compareToIgnoreCase(Words.get(i).getWord()) == 0)
                return true;
        }

        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_round, menu);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float sensorX = event.values[0];
            float sensorY = event.values[1];
            float sensorZ = event.values[2];
            if (DEBUG_FLAG) {
                sensorXText.setText(String.valueOf(sensorX));
                sensorYText.setText(String.valueOf(sensorY));
                sensorZText.setText(String.valueOf(sensorZ));
            }
            if (sensorZ < -5.0&& !downMovTriggered) { // Movimento para baixo efetuado
                downMovTriggered = true;
                signalCorrectGuess();
            }

            if (sensorZ > -5.0 && downMovTriggered) { // Celular voltou do Movimento para baixo
                downMovTriggered = false;
            }

            if (sensorZ > 5 && !upMovTriggered) { // Movimento para cima efetuado
                upMovTriggered = true;
                signalSkipWord();
            }
            if (sensorZ < 5.0 && upMovTriggered) { // Celular voltou do Movimento para cima
                upMovTriggered= false;
            }
        }
    }

    private void setNextWordInScreen(){
        if (currentWordIndex < Words.size()) {
            Toast.makeText(this, "Home: " + curScore.getPointsHome().toString() + " Away: " + curScore.getPointsAway().toString(), Toast.LENGTH_LONG).show();
            currentWordIndex++;
        }
        else {
            Toast.makeText(this, "Awesome!!! You completed the round. Easy champ, we're almost out of words. =D",
                    Toast.LENGTH_LONG).show();
            //Intent callMain = new Intent(RoundActivity.this, MainActivity.class);
            //startActivityForResult(callMain, 1);

            Intent callScoreboard = new Intent(RoundActivity.this, ScoreboardActivity.class);
            callScoreboard.putExtra("scoreboard", this.curScore);
            startActivityForResult(callScoreboard, 1);
        }

        currentWordText.setText(Words.get(currentWordIndex).getWord());
    }

    private void updateScoreboard(String player) {
        if (player.equals("home")) {
            curScore.setPointsHome(curScore.getPointsHome() + 1);
            scoreHome.setText(curScore.getPointsHome().toString());
        }
        else if (player.equals("away")) {
            curScore.setPointsAway(curScore.getPointsAway() + 1);
            scoreAway.setText(curScore.getPointsAway().toString());
        }
    }

    private void signalSkipWord() {
        this.updateScoreboard("home");
        setNextWordInScreen();
    }

    private void signalCorrectGuess() {
        this.updateScoreboard("away");
        setNextWordInScreen();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            timerText.setText("Game Over");
            countDownTimer.cancel();
            setResult(RESULT_OK, recorVideoIntent);
            finish();

            //Intent callMain = new Intent(RoundActivity.this, MainActivity.class);
            //startActivityForResult(callMain, 1);
            Intent callScoreboard = new Intent(RoundActivity.this, ScoreboardActivity.class);
            callScoreboard.putExtra("scoreboard", RoundActivity.this.curScore);
            startActivityForResult(callScoreboard, 1);

/*
            Intent callRound = new Intent(GameActivity.this, RoundActivity.class);
            callRound.putExtra("msg", "");
            //startActivity(callLanguage);
            startActivityForResult(callRound, 1);
  */
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

    private boolean hasFrontalCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT)){
            hasFrontalCameraFlag = true;
            return true;
        } else {
            hasFrontalCameraFlag = false;
            return false;
        }
    }

   /* private void populateList() {
        ArrayAdapter<Word> adapter = new WordListAdapter();
        wordListView.setAdapter(adapter);
    }*/
    // Method to add Word not supposed to be used in by the user
    private void addWord(String word, String category){
        Words.add(new Word(0,word, category));

    }
    /*
    private class WordListAdapter extends ArrayAdapter<Word>{
        public WordListAdapter(){
            super (RoundActivity.this,R.layout.activity_round, Words);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.activity_round, parent,false);
            Word currentWord = Words.get(position);

            TextView word = (TextView) view.findViewById(R.id.currentWord);
            word.setText(currentWord.getWord());


            return view;
        }
    }
*/
}
