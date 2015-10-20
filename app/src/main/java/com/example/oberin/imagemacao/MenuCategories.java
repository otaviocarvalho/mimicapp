package com.example.oberin.imagemacao;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuCategories extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_categories);


        Button playCategories = (Button) findViewById(R.id.btnCategoryPlay);

        playCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callGameCategories = new Intent(MenuCategories.this, GameActivity.class);
                callGameCategories.putExtra("msg", "");
                startActivity(callGameCategories);
            }
        });



    }
}
