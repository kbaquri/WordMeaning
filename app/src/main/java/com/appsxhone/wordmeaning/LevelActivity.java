package com.appsxhone.wordmeaning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Sameer on 01-Mar-16.
 */
public class LevelActivity extends AppCompatActivity {
    Button btnBegginer, btnIntermediate, btnHard, btnExpert;
    ImageView splash;
    Intent levelSelect;
    SoundPlayer soundPlayer;

    Animation animBlink,animMoveFromRight,animMoveFromLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        soundPlayer = new SoundPlayer(this);
        soundPlayer.resumeSound(this);

        btnBegginer = (Button) findViewById(R.id.btnLevelBigginer);
        btnIntermediate = (Button) findViewById(R.id.btnLevelIntermediate);
        btnHard = (Button) findViewById(R.id.btnLevelHard);
        btnExpert = (Button) findViewById(R.id.btnLevelExpert);

        splash = (ImageView) findViewById(R.id.splash);

        animation();

        levelSelect = new Intent(getApplicationContext(),GameActivity.class);

        btnBegginer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelSelect.putExtra("LEVEL", 1);
                startActivity(levelSelect);
                finish();
            }
        });
        btnIntermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelSelect.putExtra("LEVEL", 2);
                startActivity(levelSelect);
                finish();
            }
        });
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelSelect.putExtra("LEVEL", 3);
                startActivity(levelSelect);
                finish();
            }
        });
        btnExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelSelect.putExtra("LEVEL", 4);
                startActivity(levelSelect);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundPlayer.resumeSound(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPlayer.pauseSound(this);
    }
    public void animation() {
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animMoveFromRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_from_right);
        animMoveFromLeft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_from_left);

        btnBegginer.startAnimation(animMoveFromLeft);
        btnIntermediate.startAnimation(animMoveFromRight);
        btnHard.startAnimation(animMoveFromLeft);
        btnExpert.startAnimation(animMoveFromRight);
        splash.startAnimation(animBlink);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),StartActivity.class));
        finish();
    }
}
