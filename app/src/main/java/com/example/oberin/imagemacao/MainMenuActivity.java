package com.example.oberin.imagemacao;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button showCategories = (Button) findViewById(R.id.button_categories);
        Button playRandom = (Button) findViewById(R.id.button_random);
        Button showSettings = (Button) findViewById(R.id.button_settings);
        Button showTutorial = (Button) findViewById(R.id.button_tutorial);

        showCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callCategories = new Intent(MainMenuActivity.this, MenuCategories.class);
                //callGame.putExtra("msg", "");
                startActivity(callCategories);
            }
        });

        playRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callGame = new Intent(MainMenuActivity.this, GameActivity.class);
                //callGame.putExtra("msg", "");
                startActivity(callGame);
            }
        });

        showSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callLanguage = new Intent(MainMenuActivity.this, SetLanguageActivity.class);
                //callLanguage.putExtra("msg", "");
                //startActivity(callLanguage);
                startActivityForResult(callLanguage, 1);
            }
        });


    }
}
