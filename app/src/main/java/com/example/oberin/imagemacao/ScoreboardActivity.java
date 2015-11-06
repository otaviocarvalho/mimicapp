package com.example.oberin.imagemacao;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ScoreboardActivity extends ActionBarActivity {
    public TextView resultText;
    public TextView pointsHome;
    public TextView pointsAway;
    public Button buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        resultText = (TextView) this.findViewById(R.id.textResult);
        pointsHome = (TextView) this.findViewById(R.id.pointsHome);
        pointsAway = (TextView) this.findViewById(R.id.pointsAway);

        buttonHome = (Button) findViewById(R.id.buttonHome);

        // Read points from Parcelable
        if (getIntent().hasExtra("scoreboard")) {
            Scoreboard curScore = getIntent().getParcelableExtra("scoreboard");

            resultText.setText(curScore.getMatchResult());
            pointsHome.setText(curScore.getPointsHome().toString());
            pointsAway.setText(curScore.getPointsAway().toString());
            Toast.makeText(this, "away: " + curScore.getPointsAway().toString() + " home: " + curScore.getPointsHome().toString(), Toast.LENGTH_LONG).show();
        }

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callHome = new Intent(ScoreboardActivity.this, MainActivity.class);
                startActivity(callHome);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scoreboard, menu);
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
}
