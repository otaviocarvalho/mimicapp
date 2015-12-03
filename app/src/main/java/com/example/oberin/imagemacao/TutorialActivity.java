package com.example.oberin.imagemacao;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.view.MotionEvent;
import android.widget.TextView;

import org.w3c.dom.Text;


public class TutorialActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Button playGame = (Button) findViewById(R.id.button_pular);
        Button voltar = (Button) findViewById(R.id.button_voltar);

        View minhaTela = findViewById(R.id.telaTutorial);


        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callGame = new Intent(TutorialActivity.this, GameActivity.class);
                //callGame.putExtra("msg", "");
                startActivity(callGame);
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltar = new Intent(TutorialActivity.this, MainMenuActivity.class);
                //callGameCategories.putExtra("msg", "");
                startActivity(voltar);
            }
        });

        minhaTela.setOnTouchListener(new OnSwipeTouchListener(this.getApplicationContext()) {

            TextView aux_pagina = (TextView) findViewById(R.id.num_pag);
            String txt_pagina = aux_pagina.getText().toString();
            int num_pagina  = Integer.parseInt(txt_pagina);

            @Override
            public void onSwipeLeft() {
                if(num_pagina < 4)
                    num_pagina++;

                renderTutorial(num_pagina);
            }

            public void onSwipeRight() {
                if(num_pagina > 1)
                    num_pagina--;

                renderTutorial(num_pagina);
            }

        });

    }


    public void renderTutorial(int numPag){
        TextView conteudo = (TextView) findViewById(R.id.textoTutorial);
        TextView pagina = (TextView) findViewById(R.id.num_pag);

        String texto = "";

        switch (numPag) {
            case 1:
                texto = "O app de Mímica é uma ótima ferramenta para se divertir com a família e seus amigos e treinar seus idiomas!";
                break;
            case 2:
                texto = "No menu principal você pode selecionar 'Jogar com Categorias' para escolher quais os tipos de mímicas do seu jogo OU 'Jogar aleatoriamente' com todas as mímicas que o Imagem Ação oferece!";
                break;
            case 3:
                texto = "Neste menu você também pode escolher o idioma do aplicativo.";
                break;
            case 4:
                texto = "Durante o jogo, segure seu aparelho na testa, na orientação paisagem e gire ele para baixo para contabilizar um acerto. Para contabilizar um erro, gire para cima.";
                break;
        }

        conteudo.setText(texto);
        pagina.setText(String.valueOf(numPag));
    }



    /**
     * Detects left and right swipes across a view.
     */
    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

}
