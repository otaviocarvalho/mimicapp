package com.example.oberin.imagemacao;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RoundActivity extends ActionBarActivity {

    // Variables for Couter
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;
    public TextView currentWordText;
    public TextView timerText;
    public boolean hasFrontalCameraFlag;
    public String videoName = "/myFunVideo.mp4";


    List<Word> Words = new ArrayList<Word>();
    //ListView wordListView;
    DatabaseHandler dbHandler;


    private static final int VIDEO_CAPTURE = 101;
    public static Intent recorVideoIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        Intent intent = getIntent();

        currentWordText = (TextView) this.findViewById(R.id.currentWord);
        timerText = (TextView) this.findViewById(R.id.timer);
        //wordListView = (ListView) findViewById(R.id.currentWord);
        dbHandler = new DatabaseHandler(getApplicationContext());

        countDownTimer = new MyCountDownTimer(startTime, interval);
        this.startTimer();
        timerText.setText(timerText.getText() + String.valueOf((startTime / 1000) - 1));
        if (hasFrontalCamera()) {
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

        if (dbHandler.getWordCount() != 0)
            Words.addAll((dbHandler.getAllWords()));
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


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            currentWordText.setText("");
            timerText.setText("Game Over");
            countDownTimer.cancel();
            setResult(RESULT_OK, recorVideoIntent);
            finish();

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

}
